package org.iotope.node.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Application {
    
    @Id
    @Column(name = "APP_ID", length = 100)
    private int appId;

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }
}
