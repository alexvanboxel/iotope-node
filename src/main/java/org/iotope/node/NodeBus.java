package org.iotope.node;

import javax.inject.Singleton;

import com.google.common.eventbus.EventBus;

@Singleton
public class NodeBus extends EventBus {
    
    public NodeBus() {
        super("nodebus");
    }
}
