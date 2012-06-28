package org.iotope.node.conf;

import java.util.HashMap;
import java.util.Map;

public class CfgFilter {
    
    public enum FilterType {
        UNKNOWN("unknown"),INCLUDE("include"),EXCLUDE("exclude");
        
        private FilterType(String name) {
            this.name = name;
        }
        
        public final String name;
        
        @Override
        public String toString() {
            return name;
        }
    }
    
    static Map<String, FilterType> filterLookup = new HashMap<String, FilterType>();
    static {
        for (FilterType val : FilterType.values()) {
            filterLookup.put(val.name, val);
        }
    }
    
    private String urn;
    private FilterType filterType;
    
    private Map<String,String> properties = new HashMap<String,String>();

    public CfgFilter(String urn,String filterType) {
        this.urn = urn;
        this.filterType = filterLookup.get(filterType);
        if(this.filterType == null) {
            throw new RuntimeException("No match found for "+filterType);
        }
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

    public FilterType getType() {
        return filterType;
    }
}
