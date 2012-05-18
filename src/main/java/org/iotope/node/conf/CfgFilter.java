package org.iotope.node.conf;

import java.util.HashMap;
import java.util.Map;

public class CfgFilter {
    
    private String urn;
    
    private Map<String,String> properties = new HashMap<String,String>();

    public CfgFilter(String urn) {
        this.urn = urn;
    }

    public String getURN() {
        return urn;
    }

    public Map<String,String> getProperties() {
        return properties;
    }

    public void addProperty(String name, String value) {
        properties.put(name, value);
    }
}
