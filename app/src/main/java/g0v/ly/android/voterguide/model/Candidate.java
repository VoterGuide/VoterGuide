package g0v.ly.android.voterguide.model;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.concurrent.Callable;

import g0v.ly.android.voterguide.utilities.InternalStorageHolder;

public class Candidate extends Observable {
    private static final Logger logger = LoggerFactory.getLogger(Candidate.class);

    public String county;
    public String district;
    public int number;
    public String name;
    public String gender;
    public String age;
    public String sessionName;
    public String cityNumber;
    public String party;

    public String drawNumber;
    public String education;
    public String experiences;
    public String manifesto;

    public boolean hasContribution = false;
    public String contributionBalance;
    public String contributionInTotal;
    public String contributionOutTotal;

    public boolean elected = false;
    public int votes;
    public String votesPercentageString;

    private String photoUrl;

    public Candidate(JSONObject rawObject, ListeningExecutorService servicePool) {
        try {
            JSONObject cecDataObject = rawObject.getJSONObject("cec_data");

            county = rawObject.getString("county");
            district = rawObject.getString("district");
            number = rawObject.getInt("number");
            name = rawObject.getString("name");
            gender = rawObject.getString("gender");
            age = birthdayToAge(cecDataObject.getString("birthdate"));
            sessionName = cecDataObject.getString("sessionname");
            cityNumber = cecDataObject.getString("cityno");
            party = rawObject.getString("party");
            drawNumber = cecDataObject.getString("drawno");
            education = cecDataObject.getString("rptedu");
            experiences = cecDataObject.getString("rptexp");
            manifesto = cecDataObject.getString("rptpolitics");

            education = reviseNewlineSyntax(education);
            experiences = reviseNewlineSyntax(experiences);
            manifesto = reviseNewlineSyntax(manifesto);

            if (!rawObject.isNull("politicalcontributions")) {
                JSONObject contributionObject = rawObject.getJSONObject("politicalcontributions");
                hasContribution = contributionObject != null;
                if (hasContribution) {
                    contributionBalance = contributionObject.getString("balance");
                    contributionInTotal = contributionObject.getString("in_total");
                    contributionOutTotal = contributionObject.getString("out_total");
                }
            }

            elected = rawObject.getBoolean("elected");
            votes = rawObject.getInt("votes");
            double votesPercentage = Double.parseDouble(rawObject.getString("votes_percentage"));
            votesPercentageString = String.format(Locale.getDefault(), "%.2f", votesPercentage);

            photoUrl = composePhotoUrl();
            loadPhoto(servicePool);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getPhoto() {
        InternalStorageHolder internalStorageHolder = InternalStorageHolder.getInstance();
        return internalStorageHolder.loadImageFromStorage(this);
    }

    private void loadPhoto(ListeningExecutorService servicePool) {
        final InternalStorageHolder internalStorageHolder = InternalStorageHolder.getInstance();
        if (internalStorageHolder.loadImageFromStorage(this) == null) {
            logger.debug("Start download {}'s photo", name);

            ListenableFuture<Bitmap> downloadPhotoFuture = servicePool.submit(new Callable<Bitmap>() {
                @Override
                public Bitmap call() throws Exception {
                    Bitmap bitmap = null;
                    InputStream stream;
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    bmOptions.inSampleSize = 1;

                    try {
                        stream = getHttpConnection(photoUrl);
                        if (stream != null) {
                            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
                            stream.close();
                        } else {
                            logger.debug("Download candidate's photo failed.");
                        }
                    } catch (IOException e) {
                        logger.debug(e.getMessage());
                    }
                    return bitmap;
                }
            });

            Futures.addCallback(downloadPhotoFuture, new FutureCallback<Bitmap>() {
                @Override
                public void onSuccess(Bitmap result) {
                    internalStorageHolder.saveToInternalSorage(Candidate.this, result);

                    setChanged();
                    notifyObservers();
                }

                @Override
                public void onFailure(@NonNull Throwable t) {
                    logger.debug("Failed to download candidate [{}]'s photo.", name);
                }
            });
        }
    }

    /**
     * Age parser
     *
     * @param birthdayString - e.g. Apr 29, 1976 12:00:00 AM
     * @return age - in String
     */
    private String birthdayToAge(String birthdayString) {
        DateFormat apiFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss aaa", Locale.US);
        Calendar birthday = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        String ageString = "";

        try {
            birthday.setTime(apiFormat.parse(birthdayString));
            int age = today.get(Calendar.YEAR) - birthday.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < birthday.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            ageString = String.valueOf(age);
        } catch (ParseException e) {
            logger.debug(e.getMessage());
        }

        return ageString;
    }

    private String reviseNewlineSyntax(String before) {
        String after = before.replace("&nbsp;", "\n");
        return after.replace("<BR>", "");
    }

    /**
     * http://g0v-data.github.io/cec-crawler/images/[cityno]-[county]-[sessionname]-[number]-[name].jpg
     * e.g. http://g0v-data.github.io/cec-crawler/images/018-新竹市-選舉區-3-林家宇.jpg
     *
     * @return photoURl
     */
    private String composePhotoUrl() {
        String url = "http://g0v-data.github.io/cec-crawler/images/";
        try {
            String paramString = cityNumber + "-" + county + "-" + sessionName + "-" + number + "-" + name + ".jpg";
            paramString = URLEncoder.encode(paramString, "UTF-8");
            url += paramString;
        } catch (UnsupportedEncodingException e) {
            logger.debug(e.getMessage());
        }

        return url;
    }

    // Makes HttpURLConnection and returns InputStream
    private InputStream getHttpConnection(String urlString) throws IOException {
        InputStream stream = null;
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();

        try {
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();

            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
        }
        return stream;
    }
}
