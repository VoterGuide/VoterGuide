package g0v.ly.android.voterguide.model;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Candidate {
    private static final Logger logger = LoggerFactory.getLogger(Candidate.class);

    public String county;
    public String district;
    public int number;
    public String name;
    public String gender;
    public String sessionName;
    public String cityNumber;
    public String party;

    public Bitmap photo;
    public String photoUrl;

    public Candidate(JSONObject rawObject) {
        try {
            JSONObject cecDataObject = rawObject.getJSONObject("cec_data");

            county = rawObject.getString("county");
            district = rawObject.getString("district");
            number = rawObject.getInt("number");
            name = rawObject.getString("name");
            gender = rawObject.getString("gender");
            sessionName = cecDataObject.getString("sessionname");
            cityNumber = cecDataObject.getString("cityno");
            party = rawObject.getString("party");
            photoUrl = composePhotoUrl();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
    Photo url : http://g0v-data.github.io/cec-crawler/images/018-新竹市-選舉區-3-林家宇.jpg
     */
    private String composePhotoUrl() {
        String url = "http://g0v-data.github.io/cec-crawler/images/";
        try {
            String paramString = cityNumber + "-" + county + "-" + sessionName +"-" + number + "-" + name + ".jpg";
            paramString = URLEncoder.encode(paramString, "UTF-8");
            url += paramString;
        }
        catch (UnsupportedEncodingException e) {
            logger.debug(e.getMessage());
        }

        return url;
    }
}
