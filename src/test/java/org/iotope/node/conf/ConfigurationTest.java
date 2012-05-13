package org.iotope.node.conf;

import org.junit.Test;

public class ConfigurationTest {

    @Test
    public void configtest1() throws Exception {
        Configuration c = new Configuration();
        c.load("configtest1.xml");
        
        System.out.println(c);
    }
    
}
