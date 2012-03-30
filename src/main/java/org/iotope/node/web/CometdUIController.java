package org.iotope.node.web;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.iotope.node.apps.Correlation;

@Singleton
public class CometdUIController {
    
    public void setLearnMode(boolean b) {
        correlation.setLearn(b);
    }
    
    
    @Inject
    Correlation correlation;
}
