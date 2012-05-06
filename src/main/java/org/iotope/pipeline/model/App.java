package org.iotope.pipeline.model;

import org.iotope.node.model.Application;

public class App {

    public App(Application app) {
        appId = app.getAppId();
    }
    
    public int getAppId() {
        return appId;
    }

    private int appId;
}
