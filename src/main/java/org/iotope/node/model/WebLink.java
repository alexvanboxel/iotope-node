package org.iotope.node.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class WebLink {    
    
    @Id
    @Column(name = "TAG_RAW",length=100)
    private String tagId;
    
    @Column(name = "WL_URL",length=512)
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }
    
}
