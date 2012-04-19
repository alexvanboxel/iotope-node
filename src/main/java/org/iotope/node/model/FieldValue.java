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
    
    //    @Id
    //    //@JoinColumn(name = "TAG_RAW")
    //    @ManyToOne
    //    public Application appId;
    
    //    @Id
    //    @ManyToOne
    //    public Tag tag;
    
    
    @Id
    @JoinColumn(name = "ASS_ID")
    @ManyToOne(optional = false)
    private Association association;
    
    @Id
    @Column(name = "FIELD_NAME", length = 25)
    private String field;
    
    @Column(name = "FIELD_VALUE", length = 250)
    private String value;

    public FieldValue(Association ass,String field, String value) {
        this.association = ass;
        this.field = field;
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

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
