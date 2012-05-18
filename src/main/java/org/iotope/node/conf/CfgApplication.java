package org.iotope.node.conf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CfgApplication {
    
    private String urn;
    private List<CfgFilter> filters = new ArrayList<CfgFilter>();
    
    private Map<String,String> properties = new HashMap<String,String>();
    
    public CfgApplication(String urn) {
        this.urn = urn;
    }
    
    public String getURN() {
        return urn;
    }
    
    public List<CfgFilter> getFilters() {
        return filters;
    }
    
    public void addFilter(CfgFilter filter) {
        this.filters.add(filter);
    }

    public void addProperty(String name, String value) {
        properties.put(name, value);
    }

    public Map<String, String> getProperties() {
        return properties;
    }
}
