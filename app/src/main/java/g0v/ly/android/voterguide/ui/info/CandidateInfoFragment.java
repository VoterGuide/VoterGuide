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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.model.Candidate;
import g0v.ly.android.voterguide.model.CandidatesManager;
import g0v.ly.android.voterguide.ui.MainActivity;

public class CandidateInfoFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(CandidateInfoFragment.class);

    private Candidate candidate;

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

        String candidateName = getArguments().getString(MainActivity.KEY_FRAGMENT_BUNDLE_CANDIDATE_INFO);
        CandidatesManager candidatesManager = CandidatesManager.getInstance();
        candidate = candidatesManager.getCandidateWithName(candidateName);

        if (candidate == null) {
            // TODO: show error page
            logger.warn("Fail to get candidate with name.");
        }
        else {
            updateUi();
            downloadAndShowCandidatePhoto(candidate.photoUrl);
        }

        return rootView;
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
        candidateNameTextView.setText(candidate.name);
        candidateGenderTextView.setText(candidate.gender);
        candidatePartyTextView.setText(candidate.party);
    }

    private Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 1;

        try {
            stream = getHttpConnection(url);
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
