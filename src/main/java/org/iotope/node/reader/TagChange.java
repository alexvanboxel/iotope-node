package org.iotope.node.reader;

import java.util.Map;

import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.util.ajax.JSON.Output;

public class TagChange implements JSON.Convertible {
    
    public enum Event {
        ADDED, REMOVED
    };
    
    
    public TagChange(Event event,Reader reader, int slot,String raw) {
        super();
        this.event = event;
        this.reader = reader;
        this.slot = slot;
        this.raw = raw;
    }
    
    public String getRaw() {
        return raw;
    }
    
    public Event getEvent() {
        return event;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void fromJSON(Map map) {
    }
    
    @Override
    public void toJSON(Output out) {
        out.add("type", "TagChange");
        out.add("event", event);
        out.add("reader", reader);
        out.add("slot", slot);
        out.add("tag", raw);
    }
    
    private Event event;
    private Reader reader;
    private int slot;
    private String raw;
}
