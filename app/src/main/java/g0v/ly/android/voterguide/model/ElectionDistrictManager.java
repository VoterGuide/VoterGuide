package g0v.ly.android.voterguide.model;

import java.util.ArrayList;
import java.util.List;

import g0v.ly.android.voterguide.utilities.HardCodeInfos;

public class ElectionDistrictManager {

    private static ElectionDistrictManager instance;

    List<ElectionDistrict> allElectionDistricts = new ArrayList<>();
    List<ElectionDistrict> countiesHasMultipleElectionDistricts = new ArrayList<>();

    private ElectionDistrictManager() {
        HardCodeInfos hardCodeInfos = HardCodeInfos.getInstance();
        allElectionDistricts = hardCodeInfos.getElectionDistricts();

        String tempCounty = "";
        for (ElectionDistrict electionDistrict : allElectionDistricts) {
            if (!electionDistrict.county.equals(tempCounty)) {
                tempCounty = electionDistrict.county;
                countiesHasMultipleElectionDistricts.add(electionDistrict);
            }
        }
    }

    public static ElectionDistrictManager getInstance() {
        synchronized (ElectionDistrictManager.class) {
            if (instance == null) {
                instance = new ElectionDistrictManager();
            }
        }
        return instance;
    }

    public List<ElectionDistrict> getElectionDistrictsWithCounty(String countyString) {
        List<ElectionDistrict> electionDistricts = new ArrayList<>();
        for (ElectionDistrict electionDistrict : allElectionDistricts) {
            if (electionDistrict.county.equals(countyString)) {
                electionDistricts.add(electionDistrict);
            }
        }

        return electionDistricts;
    }

    public List<ElectionDistrict> getCountiesHasMultipleElectionDistricts() {
        return countiesHasMultipleElectionDistricts;
    }

    public boolean isCountyHasMultipleDistrict(String countyString) {
        boolean hasMultipleDistrict = false;

        for(ElectionDistrict electionDistrict : countiesHasMultipleElectionDistricts) {
            if (electionDistrict.county.equals(countyString)) {
                hasMultipleDistrict = true;
                break;
            }
        }

        return hasMultipleDistrict;
    }
}
