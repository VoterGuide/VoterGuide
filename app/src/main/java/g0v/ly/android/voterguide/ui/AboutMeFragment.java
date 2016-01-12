package g0v.ly.android.voterguide.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;

public class AboutMeFragment extends Fragment {

    public AboutMeFragment newFragment() {
        return new AboutMeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about_me, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
