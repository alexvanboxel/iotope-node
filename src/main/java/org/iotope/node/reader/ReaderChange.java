package org.iotope.node.reader;

import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.util.ajax.JSON.Output;

public class ReaderChange implements JSON.Convertible {
    
    enum Event {
        ADDED, REMOVED
    };
    
    public ReaderChange(Event event,Reader reader) {
        this.event = event;
        this.reader = reader;
    }
    
    public Event getType() {
        return event;
    }
    
    @Override
    @SuppressWarnings("rawtypes")
    public void fromJSON(Map map) {
    }
    
    @Override
    public void toJSON(Output out) {
        out.add("type", "ReaderChange");
        out.add("event", event);
        out.add("reader", reader);
    }
    
    private Event event;
    private Reader reader;
}
