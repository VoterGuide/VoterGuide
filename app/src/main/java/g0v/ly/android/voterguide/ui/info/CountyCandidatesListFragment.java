package g0v.ly.android.voterguide.ui.info;

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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.net.WebRequest;
import g0v.ly.android.voterguide.ui.MainActivity;

public class CountyCandidatesListFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(CountyCandidatesListFragment.class);

    @Bind(R.id.candidates_listview) ListView candidatesListView;

    public static CountyCandidatesListFragment newFragment() {
        return new CountyCandidatesListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_candidate, container, false);

        ButterKnife.bind(this, rootView);

        String selectedCountyString = getArguments().getString(MainActivity.KEY_FRAGMENT_TRANSACTION_BUNDLE_ARGUMENT);
        logger.error("selectedCountyString: {}", selectedCountyString);

        getCandidates(selectedCountyString);
        //countiesListView.setAdapter(new ListViewAdapter());
        //countiesListView.setOnItemClickListener(itemClickListener);

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
                }

                String rawResultString = WebRequest.create()
                        .sendHttpRequestForResponse(WebRequest.G0V_LY_VOTE_API_URL, "ad=9&county=" + countryStringInEnglish);

                logger.error("raw result: {}", rawResultString);

                if (rawResultString != null) {
                    final List<String> candidatesList = resultParser(rawResultString);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            candidatesListView.setAdapter(new ListViewAdapter(candidatesList));
                            candidatesListView.setOnItemClickListener(itemClickListener);
                        }
                    });
                }
            }
        }).start();
    }

    private List<String> resultParser(String rawResult) {
        List<String> countyCandidates = new ArrayList<>();

        try {
            JSONObject rawObject = new JSONObject(rawResult);
            JSONArray candidatesArray = rawObject.getJSONArray("results");

            for (int i = 0; i < candidatesArray.length(); i++) {
                JSONObject candidate = candidatesArray.getJSONObject(i);
                countyCandidates.add(candidate.getString("name"));
            }
        }
        catch (JSONException e) {
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
            // TODO: goto candidate info
        }
    };
}
