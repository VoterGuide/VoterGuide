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
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.Callable;

public class Candidate {
    private static final Logger logger = LoggerFactory.getLogger(Candidate.class);

    public interface Callback {
        void onPhotoDownloadComplete(Bitmap photo);
    }
    private WeakReference<Callback> callbackRef;

    public String county;
    public String district;
    public int number;
    public String name;
    public String gender;
    public String sessionName;
    public String cityNumber;
    public String party;
    public Bitmap photo;

    private String photoUrl;

    public Candidate(JSONObject rawObject, ListeningExecutorService servicePool) {
        try {
            JSONObject cecDataObject = rawObject.getJSONObject("cec_data");

            county = rawObject.getString("county");
            district = rawObject.getString("district");
            number = rawObject.getInt("number");
            name = rawObject.getString("name");
            gender = rawObject.getString("gender");
            sessionName = cecDataObject.getString("sessionname");
            cityNumber = cecDataObject.getString("cityno");
            party = rawObject.getString("party");
            photoUrl = composePhotoUrl();

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
                        }
                        else {
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
                    photo = result;

                    Callback callback = getCallback();
                    if (callback != null) {
                        callback.onPhotoDownloadComplete(photo);
                    }
                }

                @Override
                public void onFailure(@NonNull Throwable t) {
                    logger.debug("Failed to download candidate [{}]'s photo.", name);
                }
            });
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
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
            String paramString = cityNumber + "-" + county + "-" + sessionName +"-" + number + "-" + name + ".jpg";
            paramString = URLEncoder.encode(paramString, "UTF-8");
            url += paramString;
        }
        catch (UnsupportedEncodingException e) {
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

    public void setCallback(Callback cb) {
        callbackRef = new WeakReference<>(cb);
    }

    public Callback getCallback() {
        if (callbackRef != null) {
            return callbackRef.get();
        }
        return null;
    }
}
