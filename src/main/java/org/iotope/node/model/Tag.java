package org.iotope.node.model;

import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import org.eclipse.jetty.util.ajax.JSON;
import org.eclipse.jetty.util.ajax.JSON.Output;

@Entity
public class Tag implements JSON.Convertible {

    @EmbeddedId
    private TagId id;
    
    
    public Tag() {
    }

    public Tag(TagId id) {
        this.id = id;
    }

    @Override
    public void fromJSON(Map arg0) {
    }

    @Override
    public void toJSON(Output arg0) {
    }

    public TagId getId() {
        return id;
    }

    public void setId(TagId id) {
        this.id = id;
    }
    
}
