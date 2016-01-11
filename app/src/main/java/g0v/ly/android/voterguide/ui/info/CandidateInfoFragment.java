package g0v.ly.android.voterguide.ui.info;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.model.Candidate;
import g0v.ly.android.voterguide.model.CandidatesManager;

public class CandidateInfoFragment extends Fragment implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(CandidateInfoFragment.class);

    private Candidate candidate;
    private String candidateName;

    @Bind(R.id.candidate_photo_imageview) ImageView candidatePhotoImageView;
    @Bind(R.id.candidate_name_textview) TextView candidateNameTextView;
    @Bind(R.id.candidate_gender_textview) TextView candidateGenderTextView;
    @Bind(R.id.candidate_party_textview) TextView candidatePartyTextView;

    public static CandidateInfoFragment newFragment(String name) {
        return new CandidateInfoFragment(name);
    }

    private CandidateInfoFragment(String candidateName) {
        this.candidateName = candidateName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candidate_info, container, false);

        ButterKnife.bind(this, rootView);

        CandidatesManager candidatesManager = CandidatesManager.getInstance();
        candidate = candidatesManager.getCandidateWithName(candidateName);
        candidate.addObserver(this);

        candidateNameTextView.setText(candidate.name);
        candidateGenderTextView.setText(candidate.gender);
        candidatePartyTextView.setText(candidate.party);

        Bitmap photo = candidate.getPhoto();
        if (photo != null) {
            candidatePhotoImageView.setImageBitmap(photo);
        }
        else {
            logger.debug("Fail to get candidate's photo");
        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        candidate.deleteObserver(this);
    }

    @Override
    public void update(final Observable observable, Object data) {
        Activity activity = getActivity();
        if (activity != null) {
            final Candidate candidate = (Candidate) observable;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    candidatePhotoImageView.setImageBitmap(candidate.getPhoto());
                }
            });
        }
    }
}
