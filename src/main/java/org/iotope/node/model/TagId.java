package org.iotope.node.model;

import javax.persistence.Embeddable;

@Embeddable
public class TagId {

    
    public int type;

    public byte[] id;
    
}
