package g0v.ly.android.voterguide.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.ui.guide.GuideFragment;
import g0v.ly.android.voterguide.ui.info.SelectCandidateFragment;
import g0v.ly.android.voterguide.ui.info.SelectCountyFragment;

public class MainActivity extends AppCompatActivity {
    public static String KEY_FRAGMENT_TRANSACTION_BUNDLE_ARGUMENT = "key.fragment.transaction.bundle.argument";

    public enum State {
        STATE_MAIN("state.main"),
        STATE_GUIDE("state.guide"),
        STATE_INFO_COUNTIES("state.info.counties"),
        STATE_INFO_CANDIDATES("state.info.candidates"),
        STATE_INFO_CANDIDATE("state.info");

        private final String id;
        State(String id) {
            this.id = id;
        }
    }

    private State state = State.STATE_MAIN;
    private String bundleMessages = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

    private void launch(State state) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(state.id);

        boolean stacked = false;

        switch (state) {
            case STATE_MAIN:
                fragment = MainFragment.newFragment(mainFragmentCallback);
                break;
            case STATE_GUIDE:
                fragment = GuideFragment.newFragment();
                stacked = true;
                break;
            case STATE_INFO_COUNTIES:
                fragment = SelectCountyFragment.newFragment();
                stacked = true;
                break;
            case STATE_INFO_CANDIDATES:
                fragment = SelectCandidateFragment.newFragment();
                stacked = true;
                break;
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHolder, fragment, state.id);
        if (stacked) {
            fragmentTransaction.addToBackStack(null);
        }
        if (bundleMessages.length() > 0) {
            Bundle args = new Bundle();
            args.putString(KEY_FRAGMENT_TRANSACTION_BUNDLE_ARGUMENT, bundleMessages);
            fragment.setArguments(args);

            bundleMessages = "";
        }
        fragmentTransaction.commit();
    }

    private MainFragment.Callback mainFragmentCallback = new MainFragment.Callback() {

        @Override
        public void gotoGuide() {
            state = State.STATE_GUIDE;
            launch(state);
        }

        @Override
        public void gotoInfo() {
            state = State.STATE_INFO_COUNTIES;
            launch(state);

        }
    };

    public void gotoFragmentWithState(State state, String message) {
        this.state = state;
        bundleMessages = message;
        launch(this.state);
    }
}
