package org.iotope.node.apps;

import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.util.ajax.JSON.Output;

public class CorrelationModeChange implements JSON.Convertible {
    
    public enum Mode {
        ACTIVE, LEARN
    };
    
    
    public CorrelationModeChange(Mode mode) {
        super();
        this.mode = mode;
    }
    
    public Mode getMode() {
        return mode;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void fromJSON(Map map) {
    }
    
    @Override
    public void toJSON(Output out) {
        out.add("type", "CorrelationModeChange");
        out.add("mode", mode);
    }
    
    private Mode mode;
}
