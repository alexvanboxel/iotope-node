package org.iotope.node.conf;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.iotope.node.NodeBus;

@Singleton
public class Configuration {
    
    @Inject
    private NodeBus bus;
    
    private Cfg configuration;
    
    public void init() {
        try {
            load("/META-INF/config.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void load(String name) throws Exception {
        URL url = getClass().getResource(name);
        URI uri = url.toURI();
        InputStream stream = url.openStream();
        
        ConfigReader reader = new ConfigReader(uri, stream);
        setConfig(reader.read());
        
    }
    
    synchronized public void setConfig(Cfg configuration) {
        this.configuration = configuration;
        if (bus != null) {
            bus.post(configuration);
        }
    }
    
    synchronized public Cfg getConfig() {
        return configuration;
    }
}
