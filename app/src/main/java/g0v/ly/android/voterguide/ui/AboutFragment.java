package g0v.ly.android.voterguide.ui;

import com.google.common.collect.ImmutableList;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;

public class AboutFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(AboutFragment.class);

    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private List<String> aboutInfoTitles;
    private List<String> aboutInfoContents;

    public static AboutFragment newFragment() {
        return new AboutFragment();
    }

    private AboutFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_me, container, false);
        ButterKnife.bind(this, rootView);

        Activity activity = getActivity();
        if (activity != null) {
            LinearLayoutManager llm = new LinearLayoutManager(activity);
            recyclerView.setLayoutManager(llm);

            aboutInfoTitles = ImmutableList.<String>builder()
                    .add(activity.getString(R.string.app_goal_title))
                    .add(activity.getString(R.string.how_to_use_app_title))
                    .add(activity.getString(R.string.content_source_title))
                    .add(activity.getString(R.string.about_project_title))
                    .add(activity.getString(R.string.disclaimer_title))
                    .build();

            aboutInfoContents = ImmutableList.<String>builder()
                    .add(activity.getString(R.string.app_goal_content))
                    .add(activity.getString(R.string.how_to_use_app_content))
                    .add(activity.getString(R.string.content_source_content) +
                        activity.getString(R.string.g0v_ly_vote_api_url))
                    .add(activity.getString(R.string.about_project_content0) + " " +
                            activity.getString(R.string.app_version) + "\n" +
                            activity.getString(R.string.about_project_content1) +
                            activity.getString(R.string.project_github_url) + "\n" +
                            activity.getString(R.string.about_project_content2) +
                            activity.getString(R.string.project_waffle_url))
                    .add(activity.getString(R.string.disclaimer_content))
                    .build();
        }
        else {
            // TODO: error page
            logger.debug("Failed to get context");
        }

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(aboutInfoTitles, aboutInfoContents);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    private static class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView;
            TextView contentTextView;

            public ViewHolder(View itemView) {
                super(itemView);

                titleTextView = (TextView) itemView.findViewById(R.id.title_textview);
                contentTextView = (TextView) itemView.findViewById(R.id.content_textview);
            }
        }

        List<String> aboutInfoTitles = new ArrayList<>();
        List<String> aboutInfoContents = new ArrayList<>();

        public RecyclerViewAdapter(List<String> aboutInfoTitles, List<String> aboutInfoContents) {
            this.aboutInfoTitles = aboutInfoTitles;
            this.aboutInfoContents = aboutInfoContents;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_about_card, parent, false);
            return new ViewHolder(rootView);
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            viewHolder.titleTextView.setText(aboutInfoTitles.get(position));
            viewHolder.contentTextView.setText(aboutInfoContents.get(position));

            if (position == 2) {
                viewHolder.contentTextView.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

        @Override
        public int getItemCount() {
            return aboutInfoTitles.size();
        }
    }
}
