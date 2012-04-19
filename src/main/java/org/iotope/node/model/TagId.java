package org.iotope.node.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Id;

@Embeddable
public class TagId {
    
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
