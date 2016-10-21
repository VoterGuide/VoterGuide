package g0v.ly.android.voterguide;

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

    @Test
    public void isShowAppGoal() {
        // in entry (main) page
        onView(withText(R.string.about_this_app)).check(matches(isDisplayed()));

        // go to about page
        onView(withText(R.string.about_this_app)).perform(ViewActions.click());

        // scroll down
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.app_goal_title))));

        // check is showing app goal
        onView(withText(R.string.app_goal_title)).check(matches(isDisplayed()));

        // back to main page
        pressBack();
    }

    @Test
    public void isShowHowToUseApp() {
        // in entry (main) page
        onView(withText(R.string.about_this_app)).check(matches(isDisplayed()));

        // go to about page
        onView(withText(R.string.about_this_app)).perform(ViewActions.click());

        // scroll down
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.how_to_use_app_title))));

        // check is showing app goal
        onView(withText(R.string.how_to_use_app_title)).check(matches(isDisplayed()));

        // back to main page
        pressBack();
    }

    @Test
    public void isShowContentSource() {
        // in entry (main) page
        onView(withText(R.string.about_this_app)).check(matches(isDisplayed()));

        // go to about page
        onView(withText(R.string.about_this_app)).perform(ViewActions.click());

        // scroll down
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.content_source_title))));

        // check is showing app goal
        onView(withText(R.string.content_source_title)).check(matches(isDisplayed()));

        // back to main page
        pressBack();
    }

    @Test
    public void isShowAboutProject() {
        // in entry (main) page
        onView(withText(R.string.about_this_app)).check(matches(isDisplayed()));

        // go to about page
        onView(withText(R.string.about_this_app)).perform(ViewActions.click());

        // scroll down
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.about_project_title))));

        // check is showing app goal
        onView(withText(R.string.about_project_title)).check(matches(isDisplayed()));

        // back to main page
        pressBack();
    }

    @Test
    public void isShowDisclaimer() {
        // in entry (main) page
        onView(withText(R.string.about_this_app)).check(matches(isDisplayed()));

        // go to about page
        onView(withText(R.string.about_this_app)).perform(ViewActions.click());

        // scroll down
        onView(withId(R.id.recycler_view)).perform(RecyclerViewActions.scrollTo(hasDescendant(withText(R.string.disclaimer_title))));

        // check is showing app goal
        onView(withText(R.string.disclaimer_title)).check(matches(isDisplayed()));

        // back to main page
        pressBack();
    }
}

