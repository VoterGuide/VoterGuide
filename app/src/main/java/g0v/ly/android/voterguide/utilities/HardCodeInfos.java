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

    public enum Districts {
        臺北市("臺北市"),
        新北市("新北市"),
        桃園市("桃園市"),
        苗栗縣("苗栗縣"),
        臺中市("臺中市"),
        彰化縣("彰化縣"),
        雲林縣("雲林縣"),
        南投縣("南投縣"),
        嘉義縣("嘉義縣"),
        臺南市("臺南市"),
        高雄市("高雄市"),
        屏東縣("屏東縣");

        private final String name;

        Districts(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return (otherName != null) && name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    private Map<TaiwanRegions, List<String>> countiesOfTaiwan = new HashMap<>();
    private Map<String, List<String>> districts = new HashMap<>();

    private HardCodeInfos() {
        prepareCountiesInfo();
        prepareDistrictsInfo();
    }

    private void prepareCountiesInfo() {
        List<String> countiesListOfNorthernTaiwan = ImmutableList.<String>builder()
                .add("基隆市")
                .add("臺北市")
                .add("新北市")
                .add("桃園市")
                .add("宜蘭縣")
                .add("新竹市")
                .add("新竹縣")
                .build();
        List<String> countiesListOfCentralTaiwan = ImmutableList.<String>builder()
                .add("苗栗縣")
                .add("臺中市")
                .add("彰化縣")
                .add("雲林縣")
                .add("南投縣")
                .build();
        List<String> countiesListOfSouthernTaiwan = ImmutableList.<String>builder()
                .add("嘉義縣")
                .add("嘉義市")
                .add("臺南市")
                .add("高雄市")
                .add("屏東縣")
                .build();
        List<String> countiesListOfEasternTaiwan = ImmutableList.<String>builder()
                .add("花蓮縣")
                .add("臺東縣")
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

    private void prepareDistrictsInfo() {
        List<String> taipeiCityDistricts = ImmutableList.<String>builder()
                .add("士林區，北投區")
                .add("大同區，士林區")
                .add("中山區，松山區")
                .add("內湖區，南港區")
                .add("萬華區，中正區")
                .add("大安區")
                .add("松山區，信義區")
                .add("中正區，文山區")
                .build();
        districts.put(Districts.臺北市.toString(), taipeiCityDistricts);


        List<String> newTaipeiCityDistricts = ImmutableList.<String>builder()
                .add("泰山區，八里區，淡水區，林口區，石門區，三芝區")
                .add("三重區，蘆洲區，五股區")
                .add("三重區")
                .add("新莊區")
                .add("鶯歌區，新莊區，樹林區")
                .add("板橋區")
                .add("松山區，信義區")
                .add("中正區，文山區")
                .build();
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
