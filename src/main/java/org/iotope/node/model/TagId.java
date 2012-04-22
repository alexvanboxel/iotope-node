package org.iotope.node.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TagId implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TagId() {
    }
    
    public TagId(String tagId) {
        this.setTagId(tagId);
    }
    
    public String getTagId() {
        return tagId;
    }
    
    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
    
    @Column(name = "TAG_RAW", length = 100)
    private String tagId;
}
