package org.iotope.context;

import java.util.Map;


public interface Application {
    
    public void execute(ExecutionContext context);
    
    public MetaData getMetaData();
    
    public void configure(Map<String, String> properties);
}
