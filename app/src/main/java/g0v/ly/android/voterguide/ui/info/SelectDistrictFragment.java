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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.model.ElectionDistrict;
import g0v.ly.android.voterguide.model.ElectionDistrictManager;
import g0v.ly.android.voterguide.ui.MainActivity;

public class SelectDistrictFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(SelectDistrictFragment.class);
    @Bind(R.id.districts_listview) ListView districtsListView;

    private List<ElectionDistrict> districts = new ArrayList<>();
    private String selectedCountyString;

    public static SelectDistrictFragment newFragment() {
        return new SelectDistrictFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_district, container, false);

        ButterKnife.bind(this, rootView);

        selectedCountyString = getArguments().getString(MainActivity.BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING);
        getDistricts(selectedCountyString);

        if (districts.size() > 1) {
            districtsListView.setAdapter(new ListViewAdapter(districts));
            districtsListView.setOnItemClickListener(itemClickListener);
        }
        else {
            logger.debug("Failed to get districts.");
        }

        return rootView;
    }

    private void getDistricts(String countyString) {
        ElectionDistrictManager electionDistrictManager = ElectionDistrictManager.getInstance();
        districts = electionDistrictManager.getElectionDistrictsWithCounty(countyString);
    }

    private class ListViewAdapter extends BaseAdapter {
        private class ViewHolder {
            TextView titleTextView;
            TextView subTitleTextView;
        }

        List<ElectionDistrict> electionDistricts = new ArrayList<>();

        public ListViewAdapter(List<ElectionDistrict> electionDistricts) {
            this.electionDistricts = electionDistricts;
        }

        @Override
        public int getCount() {
            return electionDistricts.size();
        }

        @Override
        public Object getItem(int position) {
            return electionDistricts.get(position);
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
                rowView = inflater.inflate(R.layout.row_region, null);

                viewHolder = new ViewHolder();
                viewHolder.titleTextView = (TextView) rowView.findViewById(R.id.title_textview);
                viewHolder.subTitleTextView = (TextView) rowView.findViewById(R.id.subtitle_textview);
                viewHolder.subTitleTextView.setVisibility(View.VISIBLE);

                rowView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            viewHolder.titleTextView.setText(electionDistricts.get(position).sessionName);
            viewHolder.subTitleTextView.setText(electionDistricts.get(position).district);

            return rowView;
        }
    }

    private ListView.OnItemClickListener itemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Activity activity = SelectDistrictFragment.this.getActivity();
            if (activity instanceof MainActivity) {
                String districtString = districts.get(position).sessionName;

                Map<String, String> params = new HashMap<>();
                params.put(MainActivity.BUNDLE_KEY_SELECTED_COUNTY_STRING, selectedCountyString);
                params.put(MainActivity.BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING, districtString);
                ((MainActivity) activity).gotoFragmentWithState(MainActivity.State.STATE_INFO_CANDIDATES_LIST, params);
            }
        }
    };
}
