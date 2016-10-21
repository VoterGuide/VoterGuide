package g0v.ly.android.voterguide;

import android.content.Context;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import g0v.ly.android.voterguide.ui.MainActivity;

@RunWith(AndroidJUnit4.class)
public class AboutPageEspressoTest {

    /**
     * 不知道為什麼
     * 用 onView(withId(R.id.goto_about_btn)).perform(click()) 就是會挫賽
     */

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setUp() throws Exception {
        // in entry (main) page
        onView(withText(R.string.about_this_app)).check(matches(isDisplayed()));

        // go to about page
        onView(withText(R.string.about_this_app)).perform(ViewActions.click());
    }

    @Test
    public void isShowAllTitle() {
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.app_goal_title))));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.how_to_use_app_title))));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.content_source_title))));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.about_project_title))));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.disclaimer_title))));
    }

    @Test
    public void isShowAllContent() {
        Context applicationContext = VoterGuideApplication.getInstance();
        String contentSourceString = applicationContext.getString(R.string.content_source_content) +
                applicationContext.getString(R.string.g0v_ly_vote_api_url);
        String aboutProjectString = applicationContext.getString(R.string.about_project_content0) + " " +
                applicationContext.getString(R.string.app_version) + "\n" +
                applicationContext.getString(R.string.about_project_content1) +
                applicationContext.getString(R.string.project_github_url) + "\n" +
                applicationContext.getString(R.string.about_project_content2) +
                applicationContext.getString(R.string.project_waffle_url);

        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.app_goal_content))));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.how_to_use_app_content))));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(contentSourceString))));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(aboutProjectString))));
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.disclaimer_content))));
    }

    @After
    public void tearDown() throws Exception {
        // back to main page
        pressBack();
    }
}

