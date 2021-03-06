package g0v.ly.android.voterguide.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;

import java.util.HashMap;
import java.util.Map;

import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.ui.guide.GuideFragment;
import g0v.ly.android.voterguide.ui.info.CandidateInfoFragment;
import g0v.ly.android.voterguide.ui.info.SelectCandidateFragment;
import g0v.ly.android.voterguide.ui.info.SelectCountyFragment;
import g0v.ly.android.voterguide.ui.info.SelectDistrictFragment;
import g0v.ly.android.voterguide.utilities.FontManager;
import g0v.ly.android.voterguide.utilities.HardCodeInfos;
import g0v.ly.android.voterguide.utilities.InternalStorageHolder;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity {
    public static String BUNDLE_KEY_SELECTED_COUNTY_STRING = "bundle.key.selected.county";
    public static String BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING = "bundle.key.selected.candidate.district";
    public static String BUNDLE_KEY_SELECTED_CANDIDATE_NAME_STRING = "bundle.key.selected.candidate.name";

    public enum State {
        STATE_MAIN("state.main"),
        STATE_GUIDE("state.guide"),
        STATE_ABOUT("state.about"),
        STATE_INFO_COUNTIES_LIST("state.info.counties"),
        STATE_INFO_DISTRICT_LIST("state.info.districts"),
        STATE_INFO_CANDIDATES_LIST("state.info.candidates"),
        STATE_INFO_CANDIDATE("state.info");

        private final String id;
        State(String id) {
            this.id = id;
        }
    }

    private State state;
    private Map<String, String> bundleMessages = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        FontManager.getInstance().setContext(this);

        state = State.STATE_MAIN;

        InternalStorageHolder.getInstance().setContext(this);
        HardCodeInfos.getInstance().prepareCountyToDrawableMap(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        launch(state);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void launch(State newState) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(newState.id);

        if (state != null && state == newState && fragment != null) {
            return;
        }

        boolean stacked = false;
        Bundle args;
        String districtString;

        switch (newState) {
            case STATE_MAIN:
                fragment = MainFragment.newFragment();
                break;
            case STATE_GUIDE:
                fragment = GuideFragment.newFragment();
                stacked = true;
                break;
            case STATE_ABOUT:
                fragment = AboutFragment.newFragment();
                stacked = true;
                break;
            case STATE_INFO_COUNTIES_LIST:
                fragment = SelectCountyFragment.newFragment();
                stacked = true;
                break;
            case STATE_INFO_DISTRICT_LIST:
                fragment = SelectDistrictFragment.newFragment();
                stacked = true;

                args = new Bundle();
                districtString = bundleMessages.get(BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING);
                if (districtString != null) {
                    args.putString(BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING, districtString);
                }
                fragment.setArguments(args);
                break;
            case STATE_INFO_CANDIDATES_LIST:
                // NOTE: Fragment ViewPager can't update dynamically
                //fragment = ViewPagerCandidateInfoFragment.newFragment();//SelectCandidateFragment.newFragment();

                fragment = SelectCandidateFragment.newFragment();
                stacked = true;

                String countyString = bundleMessages.get(BUNDLE_KEY_SELECTED_COUNTY_STRING);
                districtString = bundleMessages.get(BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING);
                args = new Bundle();
                if (countyString != null) {
                    args.putString(BUNDLE_KEY_SELECTED_COUNTY_STRING, countyString);
                }
                if (districtString != null) {
                    args.putString(BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING, districtString);
                }
                fragment.setArguments(args);
                break;
            case STATE_INFO_CANDIDATE:
                stacked = true;

                args = new Bundle();
                String nameString = bundleMessages.get(BUNDLE_KEY_SELECTED_CANDIDATE_NAME_STRING);
                if (nameString != null) {
                    args.putString(BUNDLE_KEY_SELECTED_CANDIDATE_NAME_STRING, nameString);
                    fragment = CandidateInfoFragment.newFragment();
                    fragment.setArguments(args);
                }
                break;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.fragment_slide_left_enter,
                R.animator.fragment_slide_left_exit,
                R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        fragmentTransaction.replace(R.id.fragment_holder, fragment, newState.id);
        if (stacked) {
            fragmentTransaction.addToBackStack(null);
        }

        fragmentTransaction.commit();
    }

    public void gotoFragmentWithState(State state, Map<String, String> messages) {
        this.state = state;
        if (bundleMessages != null) {
            bundleMessages.clear();
        }
        bundleMessages = messages;
        launch(this.state);
    }
}
