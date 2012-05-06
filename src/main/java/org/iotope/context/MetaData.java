package org.iotope.context;

import java.util.Collection;

import org.iotope.pipeline.model.Field;

public interface MetaData {
    
    
    public Collection<Field> getFields();
    
    public String getDisplayName();
    
    public String getDescription();
    
}
