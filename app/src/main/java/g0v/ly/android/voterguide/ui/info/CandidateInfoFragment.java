package g0v.ly.android.voterguide.ui.info;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private Map<String, String> candidateInfos = new HashMap<>();

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
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }

    private void updateUi() {
        candidateNameTextView.setText(candidateInfos.get(KEY_CANDIDATE_INFO_NAME));
        candidateGenderTextView.setText(candidateInfos.get(KEY_CANDIDATE_INFO_GENDER));
        candidatePartyTextView.setText(candidateInfos.get(KEY_CANDIDATE_INFO_PARTY));
    }
}
