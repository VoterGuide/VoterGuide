package g0v.ly.android.voterguide.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;

public class MainFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(MainFragment.class);

    @Bind(R.id.goto_guide_btn) RelativeLayout gotoGuideButton;
    @Bind(R.id.goto_info_btn) RelativeLayout gotoInfoButton;

    public static MainFragment newFragment() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);

                ButterKnife.bind(this, rootView);
                gotoGuideButton.setOnClickListener(buttonListener);
                gotoInfoButton.setOnClickListener(buttonListener);

                return rootView;
            }

            private View.OnClickListener buttonListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Activity activity = getActivity();
                    if (activity == null || !(activity instanceof MainActivity)) {
                        logger.debug("Failed to get activity.");
                        return;
                    }

                    switch (v.getId()) {
                        case R.id.goto_guide_btn:
                            ((MainActivity) activity).gotoFragmentWithState(MainActivity.State.STATE_GUIDE, null);
                            break;
                        case R.id.goto_info_btn:
                            ((MainActivity) activity).gotoFragmentWithState(MainActivity.State.STATE_INFO_COUNTIES_LIST, null);
                            break;
                    }
        }
    };
}
