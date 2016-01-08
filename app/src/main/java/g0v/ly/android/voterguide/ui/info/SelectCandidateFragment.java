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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.model.Candidate;
import g0v.ly.android.voterguide.model.CandidatesManager;
import g0v.ly.android.voterguide.ui.MainActivity;

public class SelectCandidateFragment extends Fragment implements Observer {

    private ListViewAdapter adapter = new ListViewAdapter();

    private List<Candidate> candidatesList = new ArrayList<>();
    private String selectedCountyString;
    private String selectedDistrictString;

    @Bind(R.id.candidates_listview) ListView candidatesListView;

    public static SelectCandidateFragment newFragment() {
        return new SelectCandidateFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CandidatesManager.getInstance().addObserver(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_candidate, container, false);

        ButterKnife.bind(this, rootView);

        final Bundle arguments = getArguments();
        if (arguments.containsKey(MainActivity.BUNDLE_KEY_SELECTED_COUNTY_STRING)) {
            selectedCountyString = getArguments().getString(MainActivity.BUNDLE_KEY_SELECTED_COUNTY_STRING);
        }
        if (arguments.containsKey(MainActivity.BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING)) {
            selectedDistrictString = getArguments().getString(MainActivity.BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING);
        }

        getCandidates();
        candidatesListView.setAdapter(adapter);
        candidatesListView.setOnItemClickListener(itemClickListener);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CandidatesManager.getInstance().deleteObserver(this);
    }

    private void getCandidates() {
        if (selectedCountyString != null) {
            CandidatesManager candidatesManager = CandidatesManager.getInstance();
            candidatesList = candidatesManager.getCandidatesWithCounty(selectedCountyString);
        }
        if (selectedDistrictString != null) {
            List<Candidate> tempCandidates = new ArrayList<>();
            for (Candidate candidate : candidatesList) {
                if (candidate.district.equals(selectedDistrictString)) {
                    tempCandidates.add(candidate);
                }
            }
            candidatesList = tempCandidates;
        }

        adapter.setList(candidatesList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void update(Observable observable, Object data) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getCandidates();
                }
            });
        }
    }

    private class ListViewAdapter extends BaseAdapter {
        private class ViewHolder {
            TextView candidateNameTextView;
        }

        List<Candidate> candidates = new ArrayList<>();

        public void setList(List<Candidate> candidates) {
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

            viewHolder.candidateNameTextView.setText(candidates.get(position).name);

            return rowView;
        }
    }

    private ListView.OnItemClickListener itemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Activity activity = SelectCandidateFragment.this.getActivity();
            if (activity instanceof MainActivity) {
                String candidateName = candidatesList.get(position).name;

                Map<String, String> params = new HashMap<>();
                params.put(MainActivity.BUNDLE_KEY_SELECTED_CANDIDATE_NAME_STRING, candidateName);
                ((MainActivity) activity).gotoFragmentWithState(MainActivity.State.STATE_INFO_CANDIDATE, params);
            }
        }
    };
}
