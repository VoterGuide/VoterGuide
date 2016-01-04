package g0v.ly.android.voterguide.ui.info;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.net.WebRequest;
import g0v.ly.android.voterguide.ui.MainActivity;

public class CandidateInfoFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(CandidateInfoFragment.class);

    private static final String KEY_CANDIDATE_INFO_NAME = "key.candidate.info.name";
    private static final String KEY_CANDIDATE_INFO_GENDER = "key.candidate.info.gender";
    private static final String KEY_CANDIDATE_INFO_PARTY = "key.candidate.info.party";
    private static final String KEY_CANDIDATE_INFO_PHOTO = "key.candidate.info.photo";

    private Map<String, String> candidateInfos = new HashMap<>();

    @Bind(R.id.candidate_photo_imageview) ImageView candidatePhotoImageView;
    @Bind(R.id.candidate_name_textview) TextView candidateNameTextView;
    @Bind(R.id.candidate_gender_textview) TextView candidateGenderTextView;
    @Bind(R.id.candidate_party_textview) TextView candidatePartyTextView;

    public static CandidateInfoFragment newFragment() {
        return new CandidateInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candidate_info, container, false);

        ButterKnife.bind(this, rootView);

        String selectedCandidateInfoUrl = getArguments().getString(MainActivity.KEY_FRAGMENT_BUNDLE_CANDIDATE_INFO);
        getCandidateInfo(selectedCandidateInfoUrl);

        return rootView;
    }

    private void getCandidateInfo(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String rawResultString = WebRequest.create()
                        .sendHttpRequestForResponse(url, "");

                if (rawResultString != null) {
                    try {
                        candidateInfos = resultParser(new JSONObject(rawResultString));
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                        logger.debug(e.getMessage());
                    }
                }

                downloadAndShowCandidatePhoto(candidateInfos.get(KEY_CANDIDATE_INFO_PHOTO));

                if (candidateInfos.size() > 0) {
                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUi();
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private Map<String, String> resultParser(JSONObject jsonObject) {
        Map<String, String> result = new HashMap<>();

        try {
            result.put(KEY_CANDIDATE_INFO_NAME, jsonObject.getString("name"));
            result.put(KEY_CANDIDATE_INFO_GENDER, jsonObject.getString("gender"));
            result.put(KEY_CANDIDATE_INFO_PARTY, jsonObject.getString("party"));

            String photoUrl = composePhotoUrl(jsonObject);

            result.put(KEY_CANDIDATE_INFO_PHOTO, photoUrl);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    /*
    Photo url : http://g0v-data.github.io/cec-crawler/images/018-新竹市-選舉區-3-林家宇.jpg
     */
    private String composePhotoUrl(JSONObject jsonObject) {
        String url = "http://g0v-data.github.io/cec-crawler/images/";
        String cityNo;
        String cityName;
        String number;
        String name;

        try {
            JSONObject cecDataObject = jsonObject.getJSONObject("cec_data");
            cityNo = cecDataObject.getString("cityno");
            cityName = jsonObject.getString("county");
            number = jsonObject.getString("number");
            name = jsonObject.getString("name");

            String paramString = cityNo + "-" + cityName + "-選舉區-" + number + "-" + name + ".jpg";
            paramString = URLEncoder.encode(paramString, "UTF-8");
            url += paramString;
        }
        catch (JSONException | UnsupportedEncodingException e) {
            logger.debug(e.getMessage());
        }

        return url;
    }

    private void downloadAndShowCandidatePhoto(final String photoUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap map = downloadImage(photoUrl);

                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            candidatePhotoImageView.setImageBitmap(map);
                        }
                    });
                }
            }
        }).start();
    }

    private void updateUi() {
        candidateNameTextView.setText(candidateInfos.get(KEY_CANDIDATE_INFO_NAME));
        candidateGenderTextView.setText(candidateInfos.get(KEY_CANDIDATE_INFO_GENDER));
        candidatePartyTextView.setText(candidateInfos.get(KEY_CANDIDATE_INFO_PARTY));
    }

    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
            bitmap = BitmapFactory.decodeStream(stream, null, bmOptions);
            stream.close();
        } catch (IOException e) {
            logger.debug(e.getMessage());
        }
        return bitmap;
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
