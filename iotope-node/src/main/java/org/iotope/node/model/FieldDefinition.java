package org.iotope.node.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class FieldDefinition {
    
    @Id
    @GeneratedValue
    @Column(name = "DEF_ID")
    private long id;
    
    @ManyToOne
    @JoinColumn(name="APP_ID")
    Application app;

    @Column(name = "DEF_FIELDNAME", length = 25)
    String fieldName;

    @Column(name = "DEF_DISPLAYNAME", length = 25)
    String displayName;
    
    @Column(name = "DEF_TYPE", length = 25)
    String type;
    
    @Column(name = "DEF_DESCRIPTION", length = 250)
    String description;
    
    public long getId() {
        return id;
    }

    public Application getApp() {
        return app;
    }

    public String getName() {
        return fieldName;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return fieldName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }
}
