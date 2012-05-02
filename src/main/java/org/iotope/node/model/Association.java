package org.iotope.node.model;

import static javax.persistence.CascadeType.ALL;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "findAssociationByTag", query = "select ass from Association ass where ass.tag = :tag")
public class Association {
    
    @Id
    @GeneratedValue
    @Column(name = "ASS_ID")
    private long id;
    
    @ManyToOne
    @JoinColumn(name = "APP_ID")
    Application app;
    
    @ManyToOne
    private Tag tag;
    
    @OneToMany(mappedBy = "association", cascade = ALL)
    private Collection<FieldValue> fields;
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public void setApplication(Application app) {
        this.app = app;
    }
    
    public Application getApplication() {
        return this.app;
    }
    
    public Collection<FieldValue> getFields() {
        return fields;
    }
    
    public void setFields(Collection<FieldValue> fields) {
        this.fields = fields;
    }
    
    public Tag getTag() {
        return tag;
    }
    
    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public FieldValue getFieldValueByName(String name) {
        for (FieldValue fieldValue : fields) {
            if (fieldValue.getField().getName().equals(name)) {
                return fieldValue;
            }
        }
        return null;
    }

    public void addValue(FieldDefinition definition, String value) {
        FieldValue fieldValue = new FieldValue();
        fieldValue.setAssociation(this);
        fieldValue.setField(definition);
        fieldValue.setValue(value);
        fields.add(fieldValue);
    }
    
    
}
