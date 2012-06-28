package org.iotope.pipeline.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Reader {
    
    public Reader(String terminalId, String pcscName, int slots) {
        this.terminalId = terminalId;
        this.pcscName = pcscName;
        this.slots = slots;
    }
    
    public String getTerminalId() {
        return terminalId;
    }
        
    public String getName() {
        return pcscName;
    }
        
    @JsonProperty
    private String terminalId;
    @JsonProperty
    private String pcscName;
    @JsonProperty
    private int slots;
}
