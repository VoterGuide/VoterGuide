package g0v.ly.android.voterguide.ui.info;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pnikosis.materialishprogress.ProgressWheel;

import de.hdodenhof.circleimageview.CircleImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    private static final Logger logger = LoggerFactory.getLogger(SelectCandidateFragment.class);

    private RecyclerViewAdapter adapter = new RecyclerViewAdapter();

    private List<Candidate> candidatesList = new ArrayList<>();
    private String selectedCountyString;
    private String selectedDistrictString;

    @Bind(R.id.progress_circle) ProgressWheel progressCircle;
    @Bind(R.id.recycler_view) RecyclerView recyclerView;

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

        Activity activity = getActivity();
        if (activity != null) {
            LinearLayoutManager llm = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(llm);
        }
        else {
            logger.debug("Failed to get context");
        }

        getCandidates();

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(onItemClickListener);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CandidatesManager.getInstance().deleteObserver(this);
        ButterKnife.unbind(this);
    }

    private void getCandidates() {
        if (selectedCountyString != null) {
            CandidatesManager candidatesManager = CandidatesManager.getInstance();
            candidatesList = candidatesManager.getCandidatesWithCounty(selectedCountyString);
        }
        if (selectedDistrictString != null) {
            List<Candidate> tempCandidates = new ArrayList<>();
            for (Candidate candidate : candidatesList) {
                if (candidate.sessionName.equals(selectedDistrictString)) {
                    tempCandidates.add(candidate);
                }
            }
            candidatesList = tempCandidates;
        }

        for (Candidate candidate : candidatesList) {
            candidate.addObserver(this);
        }

        sortCandidateListWithNumber();

        if (candidatesList.size() > 0 && progressCircle != null && progressCircle.getVisibility() == View.VISIBLE) {
            progressCircle.setVisibility(View.GONE);
        }
        adapter.setList(candidatesList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void update(final Observable observable, Object data) {
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

    private void sortCandidateListWithNumber() {
        Collections.sort(candidatesList, new Comparator<Candidate>() {
            @Override
            public int compare(Candidate c1, Candidate c2) {
                return c1.number - c2.number;
            }
        });
    }

    private RecyclerViewAdapter.OnItemClickListener onItemClickListener = new RecyclerViewAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Activity activity = SelectCandidateFragment.this.getActivity();
            if (activity instanceof MainActivity) {
                String candidateName = candidatesList.get(position).name;

                Map<String, String> params = new HashMap<>();
                params.put(MainActivity.BUNDLE_KEY_SELECTED_CANDIDATE_NAME_STRING, candidateName);
                ((MainActivity) activity).gotoFragmentWithState(MainActivity.State.STATE_INFO_CANDIDATE, params);
            }
        }
    };

    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            CardView cardView;
            CircleImageView candidatePhoto;
            TextView candidateNameTextView;
            TextView candidatePartyTextView;
            TextView candidateGenderTextView;
            TextView candidateAgeTextView;
            ImageView candidateElectedImageView;

            public ViewHolder(View itemView) {
                super(itemView);

                cardView = (CardView) itemView.findViewById(R.id.cardview);
                candidateNameTextView = (TextView) cardView.findViewById(R.id.candidate_name_textview);
                candidatePartyTextView = (TextView) cardView.findViewById(R.id.candidate_party_textview);
                candidateGenderTextView = (TextView) cardView.findViewById(R.id.candidate_gender_textview);
                candidateAgeTextView = (TextView) cardView.findViewById(R.id.candidate_age_textview);
                candidatePhoto = (CircleImageView) cardView.findViewById(R.id.candidate_photo_imageview);
                candidateElectedImageView = (ImageView) cardView.findViewById(R.id.elected_imageview);

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
            void onItemClick(View view, int position);
        }

        List<Candidate> candidates = new ArrayList<>();
        OnItemClickListener onItemClickListener;

        public void setList(List<Candidate> candidates) {
            this.candidates = candidates;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_candidate_card, parent, false);
            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            Candidate candidate = candidates.get(position);
            viewHolder.candidateNameTextView.setText(candidate.name);
            viewHolder.candidatePartyTextView.setText(candidate.party);
            viewHolder.candidateGenderTextView.setText(candidate.gender);
            viewHolder.candidateAgeTextView.setText(candidate.age);
            viewHolder.candidatePhoto.setImageBitmap(candidate.getPhoto());

            if (candidate.elected) {
                viewHolder.candidateElectedImageView.setVisibility(View.VISIBLE);
            }
            else {
                viewHolder.candidateElectedImageView.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return candidates.size();
        }

        public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }
}
