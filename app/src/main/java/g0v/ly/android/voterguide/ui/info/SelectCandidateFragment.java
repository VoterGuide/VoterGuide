package g0v.ly.android.voterguide.ui.info;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.net.WebRequest;
import g0v.ly.android.voterguide.ui.MainActivity;

public class SelectCandidateFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(SelectCandidateFragment.class);

    private Map<String, String> candidatesNameToUrlMap = new HashMap<>();

    @Bind(R.id.candidates_listview) ListView candidatesListView;

    public static SelectCandidateFragment newFragment() {
        return new SelectCandidateFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_candidate, container, false);

        ButterKnife.bind(this, rootView);

        String selectedCountyString = getArguments().getString(MainActivity.KEY_FRAGMENT_BUNDLE_CANDIDATES_LIST);
        getCandidates(selectedCountyString);

        return rootView;
    }

    private void getCandidates(final String countyString) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String countryStringInEnglish = "";
                try {
                    countryStringInEnglish = URLEncoder.encode(countyString, "UTF-8");
                }
                catch (UnsupportedEncodingException e) {
                    logger.debug(e.getMessage());
                }

                String rawResultString = WebRequest.create()
                        .sendHttpRequestForResponse(WebRequest.G0V_LY_VOTE_API_URL, "ad=9&county=" + countryStringInEnglish);

                if (rawResultString != null) {
                    candidatesNameToUrlMap = resultParser(rawResultString);

                    final List<String> candidatesList = new ArrayList<>(candidatesNameToUrlMap.keySet());

                    Activity activity = getActivity();
                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                candidatesListView.setAdapter(new ListViewAdapter(candidatesList));
                                candidatesListView.setOnItemClickListener(itemClickListener);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private Map<String, String> resultParser(String rawResult) {
        Map<String, String> countyCandidates = new HashMap<>();

        try {
            JSONObject rawObject = new JSONObject(rawResult);
            JSONArray candidatesArray = rawObject.getJSONArray("results");

            for (int i = 0; i < candidatesArray.length(); i++) {
                JSONObject candidate = candidatesArray.getJSONObject(i);
                countyCandidates.put(candidate.getString("name"), candidate.getString("url"));
            }
        }
        catch (JSONException e) {
            logger.debug(e.getMessage());
        }

        return countyCandidates;
    }

    private class ListViewAdapter extends BaseAdapter {
        private class ViewHolder {
            TextView candidateNameTextView;
        }

        List<String> candidates = new ArrayList<>();

        public ListViewAdapter(List<String> candidates) {
            this.candidates = candidates;
        }

        @Override
        public int getCount() {
            return candidates.size();
        }

        @Override
        public Object getItem(int position) {
            return candidates.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {View rowView = convertView;

            ViewHolder viewHolder;
            if (rowView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                rowView = inflater.inflate(R.layout.row_candidate, null);

                viewHolder = new ViewHolder();
                viewHolder.candidateNameTextView = (TextView) rowView.findViewById(R.id.candidate_name_textview);

                rowView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            viewHolder.candidateNameTextView.setText(candidates.get(position));

            return rowView;
        }
    }

    private ListView.OnItemClickListener itemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Activity activity = SelectCandidateFragment.this.getActivity();
            if (activity instanceof MainActivity) {
                List<String> candidatesList = new ArrayList<>(candidatesNameToUrlMap.keySet());
                String candidateName = candidatesList.get(position);
                String candidateInfoUrl = candidatesNameToUrlMap.get(candidateName);

                ((MainActivity) activity).gotoFragmentWithState(MainActivity.State.STATE_INFO_CANDIDATE, candidateInfoUrl);
            }
        }
    };
}