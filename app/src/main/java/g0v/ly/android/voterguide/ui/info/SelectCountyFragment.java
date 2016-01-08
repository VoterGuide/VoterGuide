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

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.model.ElectionDistrictManager;
import g0v.ly.android.voterguide.ui.MainActivity;
import g0v.ly.android.voterguide.utilities.HardCodeInfos;

public class SelectCountyFragment extends Fragment {
    @Bind(R.id.counties_listview) ListView countiesListView;

    private static List<String> counties = new ArrayList<>();

    public static SelectCountyFragment newFragment() {
        HardCodeInfos hardCodeInfos = HardCodeInfos.getInstance();
        counties = hardCodeInfos.getTaiwanCounties();
        return new SelectCountyFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_county, container, false);

        ButterKnife.bind(this, rootView);

        countiesListView.setAdapter(new ListViewAdapter(counties));
        countiesListView.setOnItemClickListener(itemClickListener);

        return rootView;
    }

    private class ListViewAdapter extends BaseAdapter {
        private class ViewHolder {
            TextView titleTextView;
        }

        List<String> counties = new ArrayList<>();

        public ListViewAdapter(List<String> counties) {
            this.counties = counties;
        }

        @Override
        public int getCount() {
            return counties.size();
        }

        @Override
        public Object getItem(int position) {
            return counties.get(position);
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
                rowView = inflater.inflate(R.layout.row_counties_list, null);

                viewHolder = new ViewHolder();
                viewHolder.titleTextView = (TextView) rowView.findViewById(R.id.county_title_textview);

                rowView.setTag(viewHolder);
            }
            else {
                viewHolder = (ViewHolder) rowView.getTag();
            }

            viewHolder.titleTextView.setText(counties.get(position));

            return rowView;
        }
    }

    private ListView.OnItemClickListener itemClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Activity activity = SelectCountyFragment.this.getActivity();
            if (activity instanceof MainActivity) {
                String selectedCountyString = counties.get(position);

                // TODO: download select county's candidate list

                boolean isCountyHasMultipleDistrict = isCountyHasMultipleDistrict(selectedCountyString);
                MainActivity.State state;
                Map<String, String> params = new HashMap<>();

                if (isCountyHasMultipleDistrict) {
                    state = MainActivity.State.STATE_INFO_DISTRICT_LIST;
                    params.put(MainActivity.BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING, selectedCountyString);
                }
                else {
                    state = MainActivity.State.STATE_INFO_CANDIDATES_LIST;
                    params.put(MainActivity.BUNDLE_KEY_SELECTED_COUNTY_STRING, selectedCountyString);
                }

                ((MainActivity) activity).gotoFragmentWithState(state, params);
            }
        }
    };

    private boolean isCountyHasMultipleDistrict(String selectedCountyString) {
        ElectionDistrictManager electionDistrictManager = ElectionDistrictManager.getInstance();
        return electionDistrictManager.isCountyHasMultipleDistrict(selectedCountyString);
    }
}
