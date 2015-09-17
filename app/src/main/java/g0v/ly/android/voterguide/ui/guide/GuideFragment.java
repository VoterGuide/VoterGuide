package g0v.ly.android.voterguide.ui.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import g0v.ly.android.voterguide.R;

public class GuideFragment extends Fragment {

    public static GuideFragment newFragment() {
        GuideFragment fragment = new GuideFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guide, container, false);

        // TODO: get views

        return rootView;
    }
}
