package org.iotope.node.conf;

import java.util.HashMap;
import java.util.Map;

public class Cfg {
    
    private Map<CfgTech.Protocol,CfgTech> tech = new HashMap<CfgTech.Protocol,CfgTech>(); 

    public CfgTech getTechnology(CfgTech.Protocol protocol) {
        return tech.get(protocol);
    }
    
    void addTechnology(CfgTech tech) {
        this.tech.put(tech.getProtocol(), tech);
    }
}
