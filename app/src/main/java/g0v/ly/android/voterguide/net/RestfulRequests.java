package g0v.ly.android.voterguide.net;

import org.json.JSONObject;

import java.util.Map;

public class RestfulRequests {

    private RestfulRequests instance;

    public RestfulRequests getInstance() {
        synchronized (RestfulRequests.class) {
            if (instance == null) {
                instance = new RestfulRequests();
            }
        }
        return instance;
    }

    private RestfulRequests() {
        // do nothing
    }

    public JSONObject post(String url, Map<String, String> params) {
        JSONObject resultObjet = null;
        return resultObjet;
    }

    public JSONObject get() {
        JSONObject resultObjet = null;
        return resultObjet;
    }

    private String composeRequestUrl(String url, Map<String, String> params) {
        String requestUrl = url;

        if (params.size() > 0) {

        }

        return requestUrl;
    }
}
