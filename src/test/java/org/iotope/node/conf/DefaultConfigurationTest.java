package org.iotope.node.conf;

import junit.framework.Assert;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DefaultConfigurationTest {
    
    private static Cfg cfg;
    
    @BeforeClass
    public static void before() throws Exception {
        Configuration c = new Configuration();
        c.load("/META-INF/config.xml");
        cfg = c.getConfig();
    }
    
    @AfterClass
    public static void after() {
        cfg = null;
    }
    
    @Test
    public void techMifareUltralight() throws Exception {
        CfgTech tech = cfg.getTechnology(CfgTech.Protocol.MIFARE_ULTRALIGHT);
        Assert.assertTrue(tech.isDetect());
    }
    
    @Test
    public void techMifareClassic() throws Exception {
        CfgTech tech = cfg.getTechnology(CfgTech.Protocol.MIFARE_CLASSIC);
        Assert.assertTrue(tech.isDetect());
    }
    
}
