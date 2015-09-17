package g0v.ly.android.voterguide.ui.info;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import g0v.ly.android.voterguide.R;

public class InfoFragment extends Fragment {

    public static InfoFragment newFragment() {
        InfoFragment fragment = new InfoFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        // TODO: get views

        return rootView;
    }
}
