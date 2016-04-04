package g0v.ly.android.voterguide.ui.info;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import g0v.ly.android.voterguide.model.ElectionDistrictManager;
import g0v.ly.android.voterguide.ui.MainActivity;
import g0v.ly.android.voterguide.utilities.HardCodeInfos;

public class SelectCountyFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(SelectCountyFragment.class);

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private static List<String> counties = new ArrayList<>();

    public static SelectCountyFragment newFragment() {
        HardCodeInfos hardCodeInfos = HardCodeInfos.getInstance();
        counties = hardCodeInfos.getTaiwanCounties();
        return new SelectCountyFragment();
    }

    public SelectCountyFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_county, container, false);

        ButterKnife.bind(this, rootView);

        Activity activity = getActivity();
        if (activity != null) {
            LinearLayoutManager llm = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(llm);
        }
        else {
            logger.debug("Failed to get context");
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(counties);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(onItemClickListener);

        return rootView;
    }

    private boolean isCountyHasMultipleDistrict(String selectedCountyString) {
        ElectionDistrictManager electionDistrictManager = ElectionDistrictManager.getInstance();
        return electionDistrictManager.isCountyHasMultipleDistrict(selectedCountyString);
    }

    private RecyclerViewAdapter.OnItemClickListener onItemClickListener = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
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

    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            ImageView countyImage;
            TextView titleTextView;

            public ViewHolder(View itemView) {
                super(itemView);

                titleTextView = (TextView) itemView.findViewById(R.id.title_textview);
                countyImage = (ImageView) itemView.findViewById(R.id.county_imageview);

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

        List<String> counties = new ArrayList<>();
        OnItemClickListener onItemClickListener;

        public RecyclerViewAdapter(List<String> counties) {
            this.counties = counties;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_county_card, parent, false);
            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            String county = counties.get(position);
            viewHolder.titleTextView.setText(county);

            Map<String, Drawable> countyToDrawableMap = HardCodeInfos.getInstance().getCountyToDrawableMap();
            Drawable countyImage = countyToDrawableMap.get(county);
            if (countyImage != null) {
                viewHolder.countyImage.setVisibility(View.VISIBLE);
                viewHolder.countyImage.setImageDrawable(countyImage);
            }
            else {
                viewHolder.countyImage.setVisibility(View.GONE);
            }
        }

        @Override
        public int getItemCount() {
            return counties.size();
        }

        public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }
}
