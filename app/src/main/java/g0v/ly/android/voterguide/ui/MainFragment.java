package g0v.ly.android.voterguide.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import g0v.ly.android.voterguide.R;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.WeakReference;

public class MainFragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(MainFragment.class);

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
        Button gotoGuideBtn = (Button) rootView.findViewById(R.id.goto_guide_btn);
        Button gotoInfoBtn = (Button) rootView.findViewById(R.id.goto_info_btn);

        gotoGuideBtn.setOnClickListener(buttonListener);
        gotoInfoBtn.setOnClickListener(buttonListener);

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
                    logger.error("go to guide btn clicked");

                    callback.gotoGuide();
                    break;
                case R.id.goto_info_btn:
                    logger.error("go to info btn clicked");

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
