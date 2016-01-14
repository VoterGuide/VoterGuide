package g0v.ly.android.voterguide.ui.info;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

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

        Activity activity = getActivity();
        if (activity != null) {
            LinearLayoutManager llm = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(llm);
        }
        else {
            logger.debug("Failed to get context");
        }

        if (districts.size() > 1) {
            RecyclerViewAdapter adapter = new RecyclerViewAdapter(districts);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adapter);
            adapter.setOnItemClickListener(onItemClickListener);
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

    private RecyclerViewAdapter.OnItemClickListener onItemClickListener = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
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

    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView titleTextView;
            TextView subTitleTextView;
            public ViewHolder(View itemView) {
                super(itemView);

                titleTextView = (TextView) itemView.findViewById(R.id.title_textview);
                subTitleTextView = (TextView) itemView.findViewById(R.id.subtitle_textview);
                subTitleTextView.setVisibility(View.VISIBLE);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        }

        public interface OnItemClickListener {
            void onItemClick(View view , int position);
        }

        List<ElectionDistrict> electionDistricts = new ArrayList<>();
        OnItemClickListener onItemClickListener;

        public RecyclerViewAdapter(List<ElectionDistrict> electionDistricts) {
            this.electionDistricts = electionDistricts;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_region_card, parent, false);
            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.titleTextView.setText(electionDistricts.get(position).sessionName);
            viewHolder.subTitleTextView.setText(electionDistricts.get(position).district);
        }

        @Override
        public int getItemCount() {
            return electionDistricts.size();
        }

        public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }
}
