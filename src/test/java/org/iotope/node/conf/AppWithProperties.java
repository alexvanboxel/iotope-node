package org.iotope.node.conf;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class AppWithProperties {
    
    private static Cfg cfg;
    
    @BeforeClass
    public static void before() throws Exception {
        Configuration c = new Configuration();
        c.loadResource("appwithproperties.xml");
        cfg = c.getConfig();
    }
    
    @AfterClass
    public static void after() {
        cfg = null;
    }

    @Test
    public void configtest1() throws Exception {
        CfgApplication cfgApp = cfg.getPipeline().getApplications().get(0);
        Assert.assertEquals("urn:iotope.app:iotope.org:webhook", cfgApp.getURN());
        List<CfgFilter> filters = cfgApp.getFilters();
        Assert.assertEquals(0, filters.size());
        Map<String,String> properties = cfgApp.getProperties();
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("http://localhost:8811/iotopehook", properties.get("url"));
    }
    
}
