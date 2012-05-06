package org.iotope.node.model;

import static javax.persistence.CascadeType.ALL;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "findApplicationByName", query = "select app from Application app where app.domain = :domain and app.name = :name")
public class Application {
    
    public Application() {
        super();
    }
    
    public Application(String domain, String name, String displayName, String description) {
        super();
        this.domain = domain;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.definitions = new ArrayList<FieldDefinition>();
    }
    
    @Id
    @GeneratedValue
    @Column(name = "APP_ID")
    private int appId;
    
    @Column(name = "APP_DOMAIN", length = 50)
    private String domain;
    
    @Column(name = "APP_NAME", length = 50)
    private String name;
    
    @Column(name = "APP_DISPLAYNAME", length = 100)
    private String displayName;
    
    @Column(name = "APP_DESCRIPTION", length = 250)
    private String description;
    
    @OneToMany(mappedBy = "app", cascade = ALL)
    Collection<FieldDefinition> definitions;
    
    public int getAppId() {
        return appId;
    }
    
    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }
    
    public void addDefinition(String fieldName, String type, String displayName, String description) {
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
    
    public String getDomain() {
        return this.domain;
    }
    
    public String getName() {
        return this.name;
    }
    
}
