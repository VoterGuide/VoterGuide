package g0v.ly.android.voterguide;

import android.app.Application;

public class VoterGuideApplication extends Application {

    private static VoterGuideApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    public static VoterGuideApplication getInstance() {
        return instance;
    }
}
