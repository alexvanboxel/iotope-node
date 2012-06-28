package org.iotope.node.conf;

import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AppAndFilterTest {
    
    private static Cfg cfg;
    
    @BeforeClass
    public static void before() throws Exception {
        Configuration c = new Configuration();
        c.loadResource("appandfilter.xml");
        cfg = c.getConfig();
    }
    
    @AfterClass
    public static void after() {
        cfg = null;
    }

    @Test
    public void configtest1() throws Exception {
        CfgApplication cfgApp = cfg.getPipeline().getApplications().get(0);
        Assert.assertEquals("urn:iotope.app:iotope.org:ndef", cfgApp.getURN());
        List<CfgFilter> filters = cfgApp.getFilters();
        Assert.assertEquals(2, filters.size());
        CfgFilter filter1 = filters.get(0);
        Assert.assertEquals("urn:iotope.filter:iotope.org:ndef", filter1.getURN());
        CfgFilter filter2 = filters.get(1);
        Assert.assertEquals("urn:iotope.filter:iotope.org:legacy", filter2.getURN());
    }
    
}
