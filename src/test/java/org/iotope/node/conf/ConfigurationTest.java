package org.iotope.node.conf;

import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void configtest1() throws Exception {
        Configuration c = new Configuration();
        c.loadResource("configtest1.xml");

        Cfg cfg = c.getConfig();
        
        CfgTech tech = cfg.getTechnology(CfgTech.Protocol.MIFARE_CLASSIC);
        
        System.out.println(c);
    }
    
}
