package g0v.ly.android.voterguide.ui.info;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.model.Candidate;
import g0v.ly.android.voterguide.model.CandidatesManager;
import g0v.ly.android.voterguide.ui.MainActivity;

public class ViewPagerCandidateInfoFragment extends Fragment {

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
        downloadCandidates();

        return rootView;
    }

    private void downloadCandidates() {
        // XXX: callback or future
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (selectedCountyString != null) {
                    getCandidatesWithCounty(selectedCountyString);
                }
                if (selectedDistrictString != null) {
                    getCandidatesWithDistrict(selectedDistrictString);
                }

                for (Candidate candidate : candidatesList) {
                    fragments.add(new CandidateInfoFragment(candidate.name));
                }

                Activity activity = getActivity();
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            PagerAdapter pagerAdapter = new ViewPagerAdapter(ViewPagerCandidateInfoFragment.this);
                            viewPager.setAdapter(pagerAdapter);
                        }
                    });
                }
            }
        }).start();
    }

    private void getCandidatesWithCounty(String countyString) {
        CandidatesManager candidatesManager = CandidatesManager.getInstance();
        candidatesList = candidatesManager.getCandidatesWithCounty(countyString);
    }

    private void getCandidatesWithDistrict(String districtString) {
        List<Candidate> tempCandidates = new ArrayList<>();
        for (Candidate candidate : candidatesList) {
            if (candidate.district.equals(districtString)) {
                tempCandidates.add(candidate);
            }
        }
        candidatesList = tempCandidates;
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(Fragment fragment) {
            super(fragment.getChildFragmentManager());
        }

        @Override
        public Fragment getItem(int i) {
            // TODO: get previous fragment's y position and pass to next fragment.
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
        // TODO: show neighbors
/*
        @Override
        public float getPageWidth(int position) {
            return 0.8f;
        }
*/
    }
}
