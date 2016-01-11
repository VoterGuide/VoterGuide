package g0v.ly.android.voterguide.ui.info;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.model.Candidate;
import g0v.ly.android.voterguide.model.CandidatesManager;
import g0v.ly.android.voterguide.ui.MainActivity;

public class ViewPagerCandidateInfoFragment extends Fragment implements Observer {
    private PagerAdapter pagerAdapter;

    private List<Candidate> candidatesList = new ArrayList<>();
    private List<CandidateInfoFragment> fragments = new ArrayList<>();

    private String selectedCountyString;
    private String selectedDistrictString;

    @Bind(R.id.view_pager) ViewPager viewPager;

    public static ViewPagerCandidateInfoFragment newFragment() {
        return new ViewPagerCandidateInfoFragment();
    }

    private ViewPagerCandidateInfoFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CandidatesManager.getInstance().addObserver(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CandidatesManager.getInstance().deleteObserver(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_viewpager_candidate_info, container, false);

        ButterKnife.bind(this, rootView);

        final Bundle arguments = getArguments();
        if (arguments.containsKey(MainActivity.BUNDLE_KEY_SELECTED_COUNTY_STRING)) {
            selectedCountyString = getArguments().getString(MainActivity.BUNDLE_KEY_SELECTED_COUNTY_STRING);
        }
        if (arguments.containsKey(MainActivity.BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING)) {
            selectedDistrictString = getArguments().getString(MainActivity.BUNDLE_KEY_SELECTED_CANDIDATE_DISTRICT_STRING);
        }
        getCandidates();

        pagerAdapter = new ViewPagerAdapter(ViewPagerCandidateInfoFragment.this);

        return rootView;
    }

    private void getCandidates() {
        if (selectedCountyString != null) {
            CandidatesManager candidatesManager = CandidatesManager.getInstance();
            candidatesList = candidatesManager.getCandidatesWithCounty(selectedCountyString);
        }
        if (selectedDistrictString != null) {
            List<Candidate> tempCandidates = new ArrayList<>();
            for (Candidate candidate : candidatesList) {
                if (candidate.district.equals(selectedDistrictString)) {
                    tempCandidates.add(candidate);
                }
            }
            candidatesList = tempCandidates;
        }

        if (candidatesList.size() > 0) {
            for (Candidate candidate : candidatesList) {
                fragments.add(CandidateInfoFragment.newFragment(candidate.name));
            }
        }
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void update(Observable observable, Object data) {
        Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getCandidates();
                }
            });
        }
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(Fragment fragment) {
            super(fragment.getChildFragmentManager());
        }

        @Override
        public Fragment getItem(int i) {
            return null;//fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
/*
        @Override
        public float getPageWidth(int position) {
            return 0.8f;
        }
*/
    }
}
