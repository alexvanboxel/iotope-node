package org.iotope.node.model;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Application {
    
    public Application() {
        super();
    }

    public Application(int appId) {
        super();
        this.appId = appId;
        this.definitions = new ArrayList<FieldDefinition>();
    }

    @Id
    @Column(name = "APP_ID", length = 100)
    private int appId;
    
    @OneToMany(mappedBy = "app", cascade = ALL)
    Collection<FieldDefinition> definitions;
    
    public int getAppId() {
        return appId;
    }
    
    public void setAppId(int appId) {
        this.appId = appId;
    }

    public void addDefinition(String fieldName, String displayName, String type, String description) {
        FieldDefinition definition = new FieldDefinition();
        definition.app = this;
        definition.fieldName = fieldName;
        definition.displayName = displayName;
        definition.type = type;
        definition.description = description;
        definitions.add(definition);
    }

    public FieldDefinition getFieldDefinitionByName(String name) {
        for (FieldDefinition fieldDefinition : definitions) {
            if (fieldDefinition.getName().equals(name)) {
                return fieldDefinition;
            }
        }
        return null;
    }
    
}
