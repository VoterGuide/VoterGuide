package g0v.ly.android.voterguide.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import g0v.ly.android.voterguide.R;

public class MainActivity extends AppCompatActivity {

    private enum State {
        STATE_MAIN("state.main");

        private final String id;
        State(String id) {
            this.id = id;
        }
    }

    private State state = State.STATE_MAIN;

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
        }

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentHolder, fragment, state.id);
        if (stacked) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    private MainFragment.Callback mainFragmentCallback = new MainFragment.Callback() {

        @Override
        public void gotoGuide() {

        }

        @Override
        public void gotoInfo() {

        }
    };
}
