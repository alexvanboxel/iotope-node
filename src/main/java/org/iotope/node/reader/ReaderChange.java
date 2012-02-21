package org.iotope.node.reader;

public class ReaderChange {
    
    enum Event {
        ADDED, REMOVED
    };
    
    public ReaderChange(Event type) {
        this.type = type;
    }
    
    public Event getType() {
        return type;
    }
    
    private Event type;
    
}
