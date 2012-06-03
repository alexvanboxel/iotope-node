package org.iotope.node.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.iotope.node.NodeBus;
import org.iotope.util.IOUtil;

@Singleton
public class Configuration {
    
    @Inject
    private NodeBus bus;
    
    private Cfg configuration;
    
    private String home;
    
    public void init() {
        try {
            String homePath = System.getenv("IOTOPE-NODE-HOME");
            if (homePath != null) {
                home = new File(homePath).getAbsolutePath();
            }
            else {
                home = ".";
            }
            
            
            Properties activeProperties = new Properties();
            activeProperties.load(new FileReader(home+"/conf/active-config.properties"));
            String active = (String) activeProperties.get("ACTIVE");
            //loadResource("/META-INF/config.xml");
            loadFile(active);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loadResource(String name) throws Exception {
        URL url = getClass().getResource(name);
        URI uri = url.toURI();
        InputStream stream = url.openStream();
        
        ConfigReader reader = new ConfigReader(uri, stream);
        setConfig(reader.read());
    }
    
    
    public void loadFile(String name) throws Exception {
        File conf = new File(home+"/conf/" + name + ".xml");
        URL url = conf.toURI().toURL();
        URI uri = url.toURI();
        InputStream stream = url.openStream();
        
        ConfigReader reader = new ConfigReader(uri, stream);
        Cfg cfg = reader.read();
        
        if (!name.equals(cfg.getName())) {
            throw new RuntimeException("Config file: Name " + cfg.getName() + " doesn't match the filename " + name);
        }
        
        long time = conf.lastModified();
        File backup = new File(home+"/conf/" + name + "~" + time + ".xml");
        if (!backup.exists()) {
            FileInputStream in = new FileInputStream(conf);
            FileOutputStream out = new FileOutputStream(backup);
            IOUtil.copy(in, out);
        }
        setConfig(cfg);
    }
    
    synchronized private void setConfig(Cfg configuration) {
        this.configuration = configuration;
        if (bus != null) {
            bus.post(configuration);
        }
    }
    
    synchronized public Cfg getConfig() {
        return configuration;
    }
}
