package org.iotope.node.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WebLink {    
    
    @Id
    @Column(name = "TAG_RAW",length=100)
    public String tagId;
    
    @Column(name = "WL_URL",length=512)
    public String url;
    
}
