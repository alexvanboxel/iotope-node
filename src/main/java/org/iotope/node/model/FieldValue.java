package org.iotope.node.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(FieldValuePK.class)
public class FieldValue {
    
    @Id
    @JoinColumn(name = "ASS_ID")
    @ManyToOne(optional = false)
    private Association association;
    
    @Id
    @ManyToOne
//    @Column(name = "DEF_ID", length = 250)
    private FieldDefinition definition;
    
    @Column(name = "VAL_VALUE", length = 250)
    private String value;

    public FieldValue(Association ass,FieldDefinition field, String value) {
        this.association = ass;
        this.definition = field;
        this.value = value;
    }

    public FieldValue() {
    }

    public Association getAssociation() {
        return association;
    }

    public void setAssociation(Association association) {
        this.association = association;
    }

    public FieldDefinition getField() {
        return definition;
    }

    public void setField(FieldDefinition definition) {
        this.definition = definition;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
