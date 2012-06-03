package org.iotope.node.conf;

import java.util.HashMap;
import java.util.Map;

public class Cfg {
    
    private String name;
    
    private Map<CfgTech.Protocol,CfgTech> tech = new HashMap<CfgTech.Protocol,CfgTech>(); 

    private CfgPipeline pipeline;
    
    public CfgTech getTechnology(CfgTech.Protocol protocol) {
        return tech.get(protocol);
    }
    
    void addTechnology(CfgTech tech) {
        this.tech.put(tech.getProtocol(), tech);
    }

    public CfgPipeline getPipeline() {
        return pipeline;
    }

    void setPipeline(CfgPipeline readPipeline) {
        this.pipeline = readPipeline;        
    }

    void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
