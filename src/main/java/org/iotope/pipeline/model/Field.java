package org.iotope.pipeline.model;

import org.iotope.node.model.Application;
import org.iotope.node.model.FieldDefinition;
import org.iotope.node.model.FieldValue;

public class Field {
    
    public Field(String name, String type, String displayName, String description) {
        this(name, type, displayName, description, null);
    }
    
    public Field(String name, String type, String displayName, String description, String value) {
        super();
        this.name = name;
        this.displayName = displayName;
        this.type = type;
        this.description = description;
        this.value = value;
    }
    
    public Field(FieldValue val) {
        Application app = val.getAssociation().getApplication();
        FieldDefinition def = val.getField();
        name = def.getName();
        displayName = def.getDisplayName();
        description = def.getDescription();
        type = def.getType();
        value = val.getValue();
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getName() {
        return name;
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
    
    private String name;
    private String displayName;
    private String type;
    private String description;
    
    private String value;
}
