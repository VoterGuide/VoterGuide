package g0v.ly.android.voterguide.utilities;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HardCodeInfos {

    private static HardCodeInfos instance;

    public static HardCodeInfos getInstance() {
        synchronized (HardCodeInfos.class) {
            if (instance == null) {
                instance = new HardCodeInfos();
            }
        }
        return instance;
    }

    public enum TaiwanRegions {
        NorthernTaiwan,
        CentralTaiwan,
        SouthernTaiwan,
        EasternTaiwan,
        Islands,
        Others
    }

    private Map<TaiwanRegions, List<String>> countiesOfTaiwan = new HashMap<>();

    private HardCodeInfos() {
        List<String> countiesListOfNorthernTaiwan = ImmutableList.<String>builder()
                .add("基隆市")
                .add("台北市")
                .add("新北市")
                .add("桃園市")
                .add("宜蘭縣")
                .add("新竹市")
                .add("新竹縣")
                .build();
        List<String> countiesListOfCentralTaiwan = ImmutableList.<String>builder()
                .add("苗栗縣")
                .add("台中市")
                .add("彰化縣")
                .add("雲林縣")
                .add("南投縣")
                .build();
        List<String> countiesListOfSouthernTaiwan = ImmutableList.<String>builder()
                .add("嘉義縣")
                .add("嘉義市")
                .add("台南市")
                .add("高雄市")
                .add("屏東縣")
                .build();
        List<String> countiesListOfEasternTaiwan = ImmutableList.<String>builder()
                .add("花蓮縣")
                .add("台東縣")
                .build();
        List<String> countiesListOfTaiwanIslands = ImmutableList.<String>builder()
                .add("澎湖縣")
                .add("金門縣")
                .add("連江縣")
                .build();
        List<String> countiesListOfOthers = ImmutableList.<String>builder()
                .add("山地原住民")
                .add("平地原住民")
                .add("全國不分區")
                .add("僑居國外國民")
                .build();

        countiesOfTaiwan.put(TaiwanRegions.NorthernTaiwan, countiesListOfNorthernTaiwan);
        countiesOfTaiwan.put(TaiwanRegions.CentralTaiwan, countiesListOfCentralTaiwan);
        countiesOfTaiwan.put(TaiwanRegions.SouthernTaiwan, countiesListOfSouthernTaiwan);
        countiesOfTaiwan.put(TaiwanRegions.EasternTaiwan, countiesListOfEasternTaiwan);
        countiesOfTaiwan.put(TaiwanRegions.Islands, countiesListOfTaiwanIslands);
        countiesOfTaiwan.put(TaiwanRegions.Others, countiesListOfOthers);
    }

    public List<String> getTaiwanCounties() {
        List<String> counties = new ArrayList<>();

        counties.addAll(countiesOfTaiwan.get(TaiwanRegions.NorthernTaiwan));
        counties.addAll(countiesOfTaiwan.get(TaiwanRegions.CentralTaiwan));
        counties.addAll(countiesOfTaiwan.get(TaiwanRegions.SouthernTaiwan));
        counties.addAll(countiesOfTaiwan.get(TaiwanRegions.EasternTaiwan));
        counties.addAll(countiesOfTaiwan.get(TaiwanRegions.Islands));
        counties.addAll(countiesOfTaiwan.get(TaiwanRegions.Others));

        return counties;
    }

    public List<String> getCountiesListFromRegion(TaiwanRegions region) {
        return countiesOfTaiwan.get(region);
    }
}
