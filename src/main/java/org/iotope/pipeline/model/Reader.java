package org.iotope.pipeline.model;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.util.ajax.JSON.Output;

public class Reader implements JSON.Convertible {
    
    public Reader(String terminalId, String pcscName, int slots) {
        this.terminalId = terminalId;
        this.pcscName = pcscName;
        this.slots = slots;
    }
    
    public String getTerminalId() {
        return terminalId;
    }
    
    @Override
    @SuppressWarnings("rawtypes")
    public void fromJSON(Map map) {
    }
    
    @Override
    public void toJSON(Output out) {
        out.add("type", "Reader");
        out.add("terminalId", terminalId);
        out.add("pcscName", pcscName);
        out.add("slots", slots);
    }
    
    @JsonProperty
    private String terminalId;
    @JsonProperty
    private String pcscName;
    @JsonProperty
    private int slots;
}
