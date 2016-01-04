package g0v.ly.android.voterguide.ui.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import g0v.ly.android.voterguide.R;

public class GuideFragment extends Fragment {

    private int currentStep = 0;
    private TextView guideText;
    private TextView previousStepBtn;

    public static GuideFragment newFragment() {
        GuideFragment fragment = new GuideFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_guide, container, false);

        guideText = (TextView) rootView.findViewById(R.id.guide_text);
        previousStepBtn = (TextView) rootView.findViewById(R.id.previous_step_btn);
        TextView nextStepBtn = (TextView) rootView.findViewById(R.id.next_step_btn);

        previousStepBtn.setOnClickListener(onClickListener);
        nextStepBtn.setOnClickListener(onClickListener);

        return rootView;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.previous_step_btn:
                    currentStep--;
                    break;
                case R.id.next_step_btn:
                    currentStep++;
                    break;
            }

            if (currentStep > 0) {
                previousStepBtn.setVisibility(View.VISIBLE);
            }
            else {
                previousStepBtn.setVisibility(View.GONE);
            }

            guideText.setText("step: " + currentStep);
        }
    };
}
