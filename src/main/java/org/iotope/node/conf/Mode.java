package org.iotope.node.conf;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.iotope.node.NodeBus;

@Singleton
public class Mode {
    
    @Inject
    private NodeBus bus;

    private boolean learnMode = false;
    
    public void set(String key, String value) {
        if ("learnMode".equals(key)) {
            setLearnMode(Boolean.valueOf(value));
        } else {
            throw new RuntimeException("Unknown configuration property : " + key);
        }
        bus.post(this);
    }
    
    public String get(String key) {
        if ("learnMode".equals(key)) {
            return String.valueOf(isLearnMode());
        } else {
            throw new RuntimeException("Unknown configuration property : " + key);
        }
    }
    
    public boolean isLearnMode() {
        return learnMode;
    }
    
    public void setLearnMode(boolean learnMode) {
        this.learnMode = learnMode;
    }
}
