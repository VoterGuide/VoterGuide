package g0v.ly.android.voterguide.model;

public class ElectionDistrict {
    public String county;
    public String district;
    public String sessionName;

    public ElectionDistrict(String county, String district, String sessionName) {
        this.county = county;
        this.district = district;
        this.sessionName = sessionName;
    }
}
