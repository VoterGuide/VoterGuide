package g0v.ly.android.voterguide.ui.info;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.model.Candidate;
import g0v.ly.android.voterguide.model.CandidatesManager;
import g0v.ly.android.voterguide.ui.MainActivity;

public class CandidateInfoFragment extends Fragment implements Observer {
    private static final Logger logger = LoggerFactory.getLogger(CandidateInfoFragment.class);

    private static final String BUNDLE_ARGUMENTS_NAME = "bundle.arguments.name";

    private Candidate candidate;

    @Bind(R.id.candidate_photo_imageview) SubsamplingScaleImageView candidatePhotoImageView;
    @Bind(R.id.candidate_name_textview) TextView candidateNameTextView;
    @Bind(R.id.candidate_age_textview) TextView candidateAgeTextView;
    @Bind(R.id.candidate_gender_textview) TextView candidateGenderTextView;
    @Bind(R.id.candidate_party_textview) TextView candidatePartyTextView;
    @Bind(R.id.candidate_contribution_textview) TextView candidateContributionTextView;
    @Bind(R.id.candidate_education_textview) TextView candidateEducationTextView;
    @Bind(R.id.candidate_experiences_textview) TextView candidateExperiencesTextView;
    @Bind(R.id.candidate_manifesto_textview) TextView candidateManifestoTextView;
    @Bind(R.id.elected_imageview) ImageView candidateElectedImageView;
    @Bind(R.id.candidate_votes_textview) TextView candidateVotesTextView;
    @Bind(R.id.candidate_votes_percentage_textview) TextView candidateVotesPercentageTextView;

    public static CandidateInfoFragment newFragment() {
        return new CandidateInfoFragment();
    }

    public CandidateInfoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_candidate_info, container, false);

        ButterKnife.bind(this, rootView);

        String candidateName = getArguments().getString(MainActivity.BUNDLE_KEY_SELECTED_CANDIDATE_NAME_STRING, "candidate_name");

        CandidatesManager candidatesManager = CandidatesManager.getInstance();
        candidate = candidatesManager.getCandidateWithName(candidateName);
        candidate.addObserver(this);

        candidateNameTextView.setText(candidate.name);
        candidateAgeTextView.setText(candidate.age);
        candidateGenderTextView.setText(candidate.gender);
        candidatePartyTextView.setText(candidate.party);
        candidateEducationTextView.setText(candidate.education);
        candidateExperiencesTextView.setText(candidate.experiences);
        candidateManifestoTextView.setText(candidate.manifesto);


        candidateVotesTextView.setText(getString(R.string.candidate_votes_content, candidate.votes));
        candidateVotesPercentageTextView.setText(String.format(getResources().getString(R.string.candidate_votes_percentage_content), candidate.votesPercentageString));

        if (candidate.hasContribution) {
            candidateContributionTextView.setVisibility(View.VISIBLE);
            candidateContributionTextView.setText(candidate.contributionBalance);
        }
        else {
            candidateContributionTextView.setVisibility(View.GONE);
        }

        if (candidate.elected) {
            candidateElectedImageView.setVisibility(View.VISIBLE);
        }
        else {
            candidateElectedImageView.setVisibility(View.INVISIBLE);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap photo = candidate.getPhoto();
                if (photo != null) {
                    candidatePhotoImageView.post(new Runnable() {
                        @Override
                        public void run() {
                            candidatePhotoImageView.setImage(ImageSource.bitmap(photo));
                        }
                    });
                }
                else {
                    logger.debug("Fail to get candidate's photo");
                }
            }
        }).start();

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
                    candidatePhotoImageView.setImage(ImageSource.bitmap(candidate.getPhoto()));
                }
            });
        }
    }
}
