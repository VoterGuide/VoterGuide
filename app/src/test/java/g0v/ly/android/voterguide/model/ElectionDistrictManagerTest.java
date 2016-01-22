package g0v.ly.android.voterguide.model;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class ElectionDistrictManagerTest {

    ElectionDistrictManager electionDistrictManager;

    @Before
    public void setUp() throws Exception {
        electionDistrictManager = ElectionDistrictManager.getInstance();
    }

    @Test
    public void testIsCountyHasMultipleDistrict() throws Exception {
        Assert.assertFalse(electionDistrictManager.isCountyHasMultipleDistrict("基隆市"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("臺北市"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("新北市"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("桃園市"));
        Assert.assertFalse(electionDistrictManager.isCountyHasMultipleDistrict("宜蘭縣"));
        Assert.assertFalse(electionDistrictManager.isCountyHasMultipleDistrict("新竹市"));
        Assert.assertFalse(electionDistrictManager.isCountyHasMultipleDistrict("新竹縣"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("苗栗縣"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("臺中市"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("彰化縣"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("雲林縣"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("南投縣"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("嘉義縣"));
        Assert.assertFalse(electionDistrictManager.isCountyHasMultipleDistrict("嘉義市"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("臺南市"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("高雄市"));
        Assert.assertTrue(electionDistrictManager.isCountyHasMultipleDistrict("屏東縣"));
        Assert.assertFalse(electionDistrictManager.isCountyHasMultipleDistrict("花蓮縣"));
        Assert.assertFalse(electionDistrictManager.isCountyHasMultipleDistrict("臺東縣"));
        Assert.assertFalse(electionDistrictManager.isCountyHasMultipleDistrict("澎湖縣"));
        Assert.assertFalse(electionDistrictManager.isCountyHasMultipleDistrict("金門縣"));
        Assert.assertFalse(electionDistrictManager.isCountyHasMultipleDistrict("連江縣"));
    }

    @Test
    public void testGetCountiesHasMultipleElectionDistricts() throws Exception {
        Assert.assertEquals(12, electionDistrictManager.getCountiesHasMultipleElectionDistricts().size());
    }

    @Test
    public void testGetElectionDistrictsWithCounty() throws Exception {
        Assert.assertEquals(9, electionDistrictManager.getElectionDistrictsWithCounty("高雄市").size());
    }
}
