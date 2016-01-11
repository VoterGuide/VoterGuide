package g0v.ly.android.voterguide.model;

public class ElectionDistrict {
    public String county;
    public String district;
    public String sessionName;

    public ElectionDistrict(String county, String sessionName, String district) {
        this.county = county;
        this.sessionName = sessionName;
        this.district = district;
    }
}
