package org.iotope.node.model;

import java.io.Serializable;


public class FieldValuePK implements Serializable{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private Long association;
    
    private Long definition;
    
    public Long getAssociation() {
        return association;
    }

    public Long getField() {
        return definition;
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
