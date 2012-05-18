package org.iotope.pipeline.app;

import java.util.Map;

import org.iotope.IotopeApplication;
import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.iotope.context.MetaData;

@IotopeApplication(domain = "test.iotope.org", name = "test")
public class TestApplication implements Application {
 
    @Override
    public void execute(ExecutionContext context) {
        context.setField("result", "true");
    }
    
    @Override
    public MetaData getMetaData() {
        return null;
    }

    @Override
    public void configure(Map<String, String> properties) {
    }
}
