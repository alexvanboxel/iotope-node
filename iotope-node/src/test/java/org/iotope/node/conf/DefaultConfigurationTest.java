package org.iotope.node.conf;

import java.util.List;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultConfigurationTest {
    
    private static Cfg cfg;
    
    @BeforeClass
    public static void before() throws Exception {
        Configuration c = new Configuration();
        c.loadResource("/META-INF/config.xml");
        cfg = c.getConfig();
    }
    
    @AfterClass
    public static void after() {
        cfg = null;
    }
    
    @Test
    public void techMifareUltralight() throws Exception {
        CfgTech tech = cfg.getTechnology(CfgTech.Protocol.MIFARE_ULTRALIGHT);
        Assert.assertNotNull(tech);
        Assert.assertTrue(tech.isDetect());
    }
    
    @Test
    public void techMifareClassic() throws Exception {
        CfgTech tech = cfg.getTechnology(CfgTech.Protocol.MIFARE_CLASSIC);
        Assert.assertNotNull(tech);
        Assert.assertTrue(tech.isDetect());
    }
    
    @Test
    public void pipelineApplications() throws Exception {
        CfgPipeline pipeline = cfg.getPipeline();
        Assert.assertNotNull(pipeline);
        List<CfgApplication> applications = pipeline.getApplications();
        Assert.assertNotNull(applications);
        Assert.assertEquals(3, applications.size());
    }

    
    @Test
    public void pipelineApplications1() throws Exception {
        CfgApplication application = cfg.getPipeline().getApplications().get(0);
        Assert.assertEquals("urn:iotope.app:iotope.org:ndef", application.getURN());
        List<CfgFilter> filters = application.getFilters();
        Assert.assertNotNull(filters);
        Assert.assertEquals(2, filters.size());
        CfgFilter filter = filters.get(0);
        Assert.assertEquals("urn:iotope.filter:iotope.org:ndef", filter.getURN());
    }
}
