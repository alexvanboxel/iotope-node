package org.iotope.node.conf;

import java.util.ArrayList;
import java.util.List;

public class CfgApplication {
    
    private String urn;
    private List<CfgFilter> filters = new ArrayList<CfgFilter>();
    
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
}
