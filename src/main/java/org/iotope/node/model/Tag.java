package org.iotope.node.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Tag {

    @EmbeddedId
    private TagId id;
    
    
    public Tag() {
    }

    public Tag(TagId id) {
        this.id = id;
    }

    public TagId getId() {
        return id;
    }

    public void setId(TagId id) {
        this.id = id;
    }
    
}
