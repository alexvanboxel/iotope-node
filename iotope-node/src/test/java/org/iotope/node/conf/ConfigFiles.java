package org.iotope.node.conf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.iotope.util.IOUtil;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConfigFiles {
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    
    private File copyConfig(String resourceName, String targetName) throws IOException {
        File confDir = new File("conf");
        confDir.mkdirs();
        File cfgFile = new File("conf/"+targetName);
        FileOutputStream co = new FileOutputStream(cfgFile);
        IOUtil.copy(getClass().getResourceAsStream(resourceName), co);
        return cfgFile;
    }
    
    @Ignore
    @Test
    public void correctName() throws Exception {
        copyConfig("named1.xml", "named1.xml");
        
        Configuration c = new Configuration();
        c.loadFile("named1");
    }
    
    @Ignore
    @Test
    public void correctIncorrectName() throws Exception {
        thrown.expect(RuntimeException.class);
        
        copyConfig("named1.xml", "named2.xml");
        
        Configuration c = new Configuration();
        c.loadFile("named2");
    }
    
}
