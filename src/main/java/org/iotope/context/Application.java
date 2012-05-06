package org.iotope.context;

import java.util.Collection;

import org.iotope.pipeline.model.Field;

public interface Application {

    public void execute(ExecutionContext context);
    
    public MetaData getMetaData();
    
}
