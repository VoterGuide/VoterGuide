package g0v.ly.android.voterguide.model;

import android.content.Context;

import com.google.common.collect.ImmutableList;

import java.util.List;

import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.VoterGuideApplication;

public class AboutContentManager {

    private static AboutContentManager instance;

    public static AboutContentManager getInstance() {
        synchronized (AboutContentManager.class) {
            if (instance == null) {
                instance = new AboutContentManager();
            }
        }
        return instance;
    }

    private AboutContentManager() {}

    public List<String> getAboutTitleStringList() {
        Context applicationContext = VoterGuideApplication.getInstance();

        return ImmutableList.<String>builder()
                .add(applicationContext.getString(R.string.app_goal_title))
                .add(applicationContext.getString(R.string.how_to_use_app_title))
                .add(applicationContext.getString(R.string.content_source_title))
                .add(applicationContext.getString(R.string.about_project_title))
                .add(applicationContext.getString(R.string.disclaimer_title))
                .build();
    }

    public List<String> getAboutContentStringList() {
        Context applicationContext = VoterGuideApplication.getInstance();

        return ImmutableList.<String>builder()
                .add(applicationContext.getString(R.string.app_goal_content))
                .add(applicationContext.getString(R.string.how_to_use_app_content))
                .add(applicationContext.getString(R.string.content_source_content) +
                        applicationContext.getString(R.string.g0v_ly_vote_api_url))
                .add(applicationContext.getString(R.string.about_project_content0) + " " +
                        applicationContext.getString(R.string.app_version) + "\n" +
                        applicationContext.getString(R.string.about_project_content1) +
                        applicationContext.getString(R.string.project_github_url) + "\n" +
                        applicationContext.getString(R.string.about_project_content2) +
                        applicationContext.getString(R.string.project_waffle_url))
                .add(applicationContext.getString(R.string.disclaimer_content))
                .build();
    }
}
