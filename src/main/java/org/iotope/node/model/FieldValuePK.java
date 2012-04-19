package org.iotope.node.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


public class FieldValuePK {
    
    private Long association;
    
    private String field;

    public Long getAssociation() {
        return association;
    }

    public String getField() {
        return field;
    }

//    
//    @Id
////    //@JoinColumn(name = "TAG_RAW")
////    @ManyToOne
////    public Application appId;
//    
////    @Id
////    @ManyToOne
////    public Tag tag;
//    
//    @Id
//    @Column(name = "FIELD_NAME", length = 25)
//    public String field;
//    
//    @Column(name = "FIELD_VALUE", length = 250)
//    public String value;
//    
}
