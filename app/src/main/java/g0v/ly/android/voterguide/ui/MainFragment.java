package g0v.ly.android.voterguide.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;

public class MainFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(MainFragment.class);

    @Bind(R.id.goto_guide_btn) TextView gotoGuideButton;
    @Bind(R.id.goto_info_btn) TextView gotoInfoButton;

    public interface Callback {
        void gotoGuide();
        void gotoInfo();
    }

    private WeakReference<Callback> callbackRef;

    public static MainFragment newFragment(Callback cb) {
        MainFragment fragment = new MainFragment();
        fragment.setCallback(cb);
        return fragment;
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
                    Callback callback = getCallback();
                    if (callback == null) {
                        return;
                    }

                    switch (v.getId()) {
                        case R.id.goto_guide_btn:
                            callback.gotoGuide();
                            break;
                        case R.id.goto_info_btn:
                            callback.gotoInfo();
                            break;
                    }
        }
    };

    public void setCallback(Callback cb) {
        callbackRef = new WeakReference<Callback>(cb);
    }

    public Callback getCallback() {
        if (callbackRef != null) {
            return callbackRef.get();
        }
        return null;
    }
}
