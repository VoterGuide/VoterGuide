package g0v.ly.android.voterguide.utilities;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import g0v.ly.android.voterguide.R;
import g0v.ly.android.voterguide.model.ElectionDistrict;

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
    private Map<String, Drawable> countyToDrawableMap = new HashMap<>();
    private List<ElectionDistrict> electionDistricts = new ArrayList<>();

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
        /*
        List<String> countiesListOfOthers = ImmutableList.<String>builder()
                .add("山地原住民")
                .add("平地原住民")
                .add("全國不分區")
                .add("僑居國外國民")
                .build();
        */

        countiesOfTaiwan.put(TaiwanRegions.NorthernTaiwan, countiesListOfNorthernTaiwan);
        countiesOfTaiwan.put(TaiwanRegions.CentralTaiwan, countiesListOfCentralTaiwan);
        countiesOfTaiwan.put(TaiwanRegions.SouthernTaiwan, countiesListOfSouthernTaiwan);
        countiesOfTaiwan.put(TaiwanRegions.EasternTaiwan, countiesListOfEasternTaiwan);
        countiesOfTaiwan.put(TaiwanRegions.Islands, countiesListOfTaiwanIslands);
        //countiesOfTaiwan.put(TaiwanRegions.Others, countiesListOfOthers);
    }

    @SuppressWarnings("ConstantConditions")
    public void prepareCountyToDrawableMap(Activity activity) {
        countyToDrawableMap = ImmutableBiMap.<String, Drawable>builder()
                .put("基隆市", ContextCompat.getDrawable(activity, R.drawable.keelungcity))
                .put("臺北市", ContextCompat.getDrawable(activity, R.drawable.taipeicity))
                .put("新北市", ContextCompat.getDrawable(activity, R.drawable.newtaipeicity))
                .put("桃園市", ContextCompat.getDrawable(activity, R.drawable.taoyuan))
                .put("宜蘭縣", ContextCompat.getDrawable(activity, R.drawable.yilan))
                .put("新竹市", ContextCompat.getDrawable(activity, R.drawable.hsinchucity))
                .put("新竹縣", ContextCompat.getDrawable(activity, R.drawable.hsinchu))
                .put("苗栗縣", ContextCompat.getDrawable(activity, R.drawable.miaoli))
                .put("臺中市", ContextCompat.getDrawable(activity, R.drawable.taichung))
                .put("彰化縣", ContextCompat.getDrawable(activity, R.drawable.changhua))
                .put("雲林縣", ContextCompat.getDrawable(activity, R.drawable.yunlin))
                .put("南投縣", ContextCompat.getDrawable(activity, R.drawable.nantou))
                .put("嘉義縣", ContextCompat.getDrawable(activity, R.drawable.chiayi))
                .put("嘉義市", ContextCompat.getDrawable(activity, R.drawable.chiayicity))
                .put("臺南市", ContextCompat.getDrawable(activity, R.drawable.tainan))
                .put("高雄市", ContextCompat.getDrawable(activity, R.drawable.kaohsiung))
                .put("屏東縣", ContextCompat.getDrawable(activity, R.drawable.pingtung))
                .put("花蓮縣", ContextCompat.getDrawable(activity, R.drawable.hualien))
                .put("臺東縣", ContextCompat.getDrawable(activity, R.drawable.taitung))
                .put("澎湖縣", ContextCompat.getDrawable(activity, R.drawable.penghu))
                .build();
    }

    private void prepareDistrictsInfo() {
        // 臺北市
        electionDistricts.add(new ElectionDistrict("臺北市", "第1選舉區", "士林區，北投區"));
        electionDistricts.add(new ElectionDistrict("臺北市", "第2選舉區", "大同區，士林區"));
        electionDistricts.add(new ElectionDistrict("臺北市", "第3選舉區", "中山區，松山區"));
        electionDistricts.add(new ElectionDistrict("臺北市", "第4選舉區", "內湖區，南港區"));
        electionDistricts.add(new ElectionDistrict("臺北市", "第5選舉區", "萬華區，中正區"));
        electionDistricts.add(new ElectionDistrict("臺北市", "第6選舉區", "大安區"));
        electionDistricts.add(new ElectionDistrict("臺北市", "第7選舉區", "松山區，信義區"));
        electionDistricts.add(new ElectionDistrict("臺北市", "第8選舉區", "中正區，文山區"));

        //新北市
        electionDistricts.add(new ElectionDistrict("新北市", "第1選舉區", "泰山區，八里區，淡水區，林口區，石門區，三芝區"));
        electionDistricts.add(new ElectionDistrict("新北市", "第2選舉區", "三重區，蘆洲區，五股區"));
        electionDistricts.add(new ElectionDistrict("新北市", "第3選舉區", "三重區"));
        electionDistricts.add(new ElectionDistrict("新北市", "第4選舉區", "新莊區"));
        electionDistricts.add(new ElectionDistrict("新北市", "第5選舉區", "鶯歌區，新莊區，樹林區"));
        electionDistricts.add(new ElectionDistrict("新北市", "第6選舉區", "板橋區"));
        electionDistricts.add(new ElectionDistrict("新北市", "第7選舉區", "板橋區"));
        electionDistricts.add(new ElectionDistrict("新北市", "第8選舉區", "中和區"));
        electionDistricts.add(new ElectionDistrict("新北市", "第9選舉區", "中和區，永和區"));
        electionDistricts.add(new ElectionDistrict("新北市", "第10選舉區", "三峽區，土城區"));
        electionDistricts.add(new ElectionDistrict("新北市", "第11選舉區", "坪林區，石碇區，烏來區，深坑區，新店區"));
        electionDistricts.add(new ElectionDistrict("新北市", "第12選舉區", "雙溪區，貢寮區，金山區，汐止區，瑞芳區，平溪區，萬里區"));

        //桃園市
        electionDistricts.add(new ElectionDistrict("桃園市", "第1選舉區", "桃園市，蘆竹鄉，龜山鄉"));
        electionDistricts.add(new ElectionDistrict("桃園市", "第2選舉區", "楊梅市，觀音鄉，新屋鄉，大園鄉"));
        electionDistricts.add(new ElectionDistrict("桃園市", "第3選舉區", "中壢市"));
        electionDistricts.add(new ElectionDistrict("桃園市", "第4選舉區", "桃園市"));
        electionDistricts.add(new ElectionDistrict("桃園市", "第5選舉區", "龍潭鄉，平鎮市"));
        electionDistricts.add(new ElectionDistrict("桃園市", "第6選舉區", "大溪鎮，八德市，中壢市，復興鄉"));

        //苗栗縣
        electionDistricts.add(new ElectionDistrict("苗栗縣", "第1選舉區", "西湖鄉，造橋鄉，銅鑼鄉，後龍鎮，竹南鎮，苑裡鎮，通霄鎮，三義鄉"));
        electionDistricts.add(new ElectionDistrict("苗栗縣", "第2選舉區", "泰安鄉，三灣鄉，頭份鎮，南庄鄉，獅潭鄉，頭屋鄉，卓蘭鎮，大湖鄉，公館鄉，苗栗市"));

        //臺中市
        electionDistricts.add(new ElectionDistrict("臺中市", "第1選舉區", "外埔區，清水區，梧棲區，大甲區，大安區"));
        electionDistricts.add(new ElectionDistrict("臺中市", "第2選舉區", "大里區，霧峰區，大肚區，龍井區，烏日區，沙鹿區"));
        electionDistricts.add(new ElectionDistrict("臺中市", "第3選舉區", "后里區，神岡區，大雅區，潭子區"));
        electionDistricts.add(new ElectionDistrict("臺中市", "第4選舉區", "西屯區，南屯區"));
        electionDistricts.add(new ElectionDistrict("臺中市", "第5選舉區", "北屯區，北區"));
        electionDistricts.add(new ElectionDistrict("臺中市", "第6選舉區", "東區，南區，中區，西區"));
        electionDistricts.add(new ElectionDistrict("臺中市", "第7選舉區", "大里區，太平區"));
        electionDistricts.add(new ElectionDistrict("臺中市", "第8選舉區", "東勢區，新社區，石岡區，和平區，豐原區"));

        //彰化縣
        electionDistricts.add(new ElectionDistrict("彰化縣", "第1選舉區", "福興鄉，和美鎮，秀水鄉，線西鄉，鹿港鎮，伸港鄉"));
        electionDistricts.add(new ElectionDistrict("彰化縣", "第2選舉區", "芬園鄉，彰化市，花壇鄉"));
        electionDistricts.add(new ElectionDistrict("彰化縣", "第3選舉區", "北斗鎮，埔鹽鄉，溪州鄉，芳苑鄉，竹塘鄉，二林鎮，溪湖鎮，埔心鄉，埤頭鄉，大城鄉"));
        electionDistricts.add(new ElectionDistrict("彰化縣", "第4選舉區", "田尾鄉，大村鄉，社頭鄉，永靖鄉，員林鎮，田中鎮，二水鄉"));

        //雲林縣
        electionDistricts.add(new ElectionDistrict("雲林縣", "第1選舉區", "水林鄉，褒忠鄉，麥寮鄉，口湖鄉，土庫鎮，元長鄉，虎尾鎮，北港鎮，臺西鄉，東勢鄉，四湖鄉"));
        electionDistricts.add(new ElectionDistrict("雲林縣", "第2選舉區", "林內鄉，莿桐鄉，古坑鄉，大埤鄉，西螺鎮，斗六市，斗南鎮，二崙鄉，崙背鄉"));

        //南投縣
        electionDistricts.add(new ElectionDistrict("南投縣", "第1選舉區", "魚池鄉，仁愛鄉，中寮鄉，草屯鎮，國姓鄉，埔里鎮"));
        electionDistricts.add(new ElectionDistrict("南投縣", "第2選舉區", "竹山鎮，集集鎮，南投市，鹿谷鄉，名間鄉，信義鄉，水里鄉"));

        //嘉義縣
        electionDistricts.add(new ElectionDistrict("嘉義縣", "第1選舉區", "鹿草鄉，朴子市，太保市，東石鄉，布袋鎮，義竹鄉，水上鄉，六腳鄉"));
        electionDistricts.add(new ElectionDistrict("嘉義縣", "第2選舉區", "阿里山鄉，竹崎鄉，大埔鄉，大林鎮，民雄鄉，梅山鄉，中埔鄉，新港鄉，番路鄉，溪口鄉"));

        //臺南市
        electionDistricts.add(new ElectionDistrict("臺南市", "第1選舉區", "官田區，東山區，後壁區，鹽水區，將軍區，北門區，學甲區，新營區，白河區，柳營區，下營區，六甲區"));
        electionDistricts.add(new ElectionDistrict("臺南市", "第2選舉區", "安定區，麻豆區，山上區，玉井區，善化區，新化區，新市區，大內區，左鎮區，南化區，七股區，佳里區，楠西區，西港區"));
        electionDistricts.add(new ElectionDistrict("臺南市", "第3選舉區", "北區，中西區，安南區"));
        electionDistricts.add(new ElectionDistrict("臺南市", "第4選舉區", "東區，安平區，南區"));
        electionDistricts.add(new ElectionDistrict("臺南市", "第5選舉區", "歸仁區，龍崎區，關廟區，永康區，仁德區"));

        //高雄市
        electionDistricts.add(new ElectionDistrict("高雄市", "第1選舉區", "大社區，杉林區，桃源區，大樹區，甲仙區，茂林區，六龜區，燕巢區，那瑪夏區，美濃區，田寮區，阿蓮區，內門區，旗山區"));
        electionDistricts.add(new ElectionDistrict("高雄市", "第2選舉區", "永安區，梓官區，橋頭區，湖內區，茄萣區，彌陀區，岡山區，路竹區"));
        electionDistricts.add(new ElectionDistrict("高雄市", "第3選舉區", "楠梓區，左營區"));
        electionDistricts.add(new ElectionDistrict("高雄市", "第4選舉區", "鳥松區，大寮區，仁武區，林園區"));
        electionDistricts.add(new ElectionDistrict("高雄市", "第5選舉區", "三民區，鼓山區，鹽埕區，旗津區"));
        electionDistricts.add(new ElectionDistrict("高雄市", "第6選舉區", "三民區"));
        electionDistricts.add(new ElectionDistrict("高雄市", "第7選舉區", "前鎮區，前金區，新興區，苓雅區"));
        electionDistricts.add(new ElectionDistrict("高雄市", "第8選舉區", "鳳山區"));
        electionDistricts.add(new ElectionDistrict("高雄市", "第9選舉區", "前鎮區，小港區"));

        //屏東縣
        electionDistricts.add(new ElectionDistrict("屏東縣", "第1選舉區", "鹽埔鄉，霧臺鄉，泰武鄉，內埔鄉，潮州鎮，里港鄉，三地門鄉，長治鄉，萬巒鄉，瑪家鄉，九如鄉，高樹鄉，竹田鄉"));
        electionDistricts.add(new ElectionDistrict("屏東縣", "第2選舉區", "麟洛鄉，屏東市，萬丹鄉"));
        electionDistricts.add(new ElectionDistrict("屏東縣", "第3選舉區", "琉球鄉，來義鄉，牡丹鄉，林邊鄉，恆春鎮，崁頂鄉，枋寮鄉，新園鄉，滿州鄉，春日鄉，佳冬鄉，新埤鄉，獅子鄉，東港鎮，枋山鄉，南州鄉，車城鄉"));
    }

    public List<String> getTaiwanCounties() {
        List<String> counties = new ArrayList<>();

        counties.addAll(countiesOfTaiwan.get(TaiwanRegions.NorthernTaiwan));
        counties.addAll(countiesOfTaiwan.get(TaiwanRegions.CentralTaiwan));
        counties.addAll(countiesOfTaiwan.get(TaiwanRegions.SouthernTaiwan));
        counties.addAll(countiesOfTaiwan.get(TaiwanRegions.EasternTaiwan));
        counties.addAll(countiesOfTaiwan.get(TaiwanRegions.Islands));
        //counties.addAll(countiesOfTaiwan.get(TaiwanRegions.Others));

        return counties;
    }

    public List<ElectionDistrict> getElectionDistricts() {
        return electionDistricts;
    }

    public Map<String, Drawable> getCountyToDrawableMap() {
        return countyToDrawableMap;
    }
}
