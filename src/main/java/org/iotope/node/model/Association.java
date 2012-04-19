package org.iotope.node.model;

import static javax.persistence.CascadeType.ALL;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

@Entity
@NamedQuery(name = "findAssociationByTag", query = "select ass from Association ass where ass.tag = :tag")
public class Association {
    
    @Id
    @GeneratedValue
    @Column(name = "ASS_ID")
    private long id;
    
//    @JoinColumn(name = "APP_APP_ID",referencedColumnName="ASS_ID")
    Application app;
    
//    @JoinColumn(name = "TAG",referencedColumnName="TAG_RAW")
    private Tag tag;
    
    @OneToMany(mappedBy = "association", cascade = ALL)
    private//    @ElementCollection(targetClass=FieldValue.class)
    //    @CollectionTable(name="FIELDS",joinColumns=
    //            {
    //                @JoinColumn(name="ASS_ID",referencedColumnName="id"),
    //            }
    //    )
    Collection<FieldValue> fields;
    
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
    
    public//    @ElementCollection(targetClass=FieldValue.class)
    //    @CollectionTable(name="FIELDS",joinColumns=
    //            {
    //                @JoinColumn(name="ASS_ID",referencedColumnName="id"),
    //            }
    //    )
    Collection<FieldValue> getFields() {
        return fields;
    }
    
    public void setFields(//    @ElementCollection(targetClass=FieldValue.class)
            //    @CollectionTable(name="FIELDS",joinColumns=
            //            {
            //                @JoinColumn(name="ASS_ID",referencedColumnName="id"),
            //            }
            //    )
            Collection<FieldValue> fields) {
        this.fields = fields;
    }
    
    public Tag getTag() {
        return tag;
    }
    
    public void setTag(Tag tag) {
        this.tag = tag;
    }
    
    
}
