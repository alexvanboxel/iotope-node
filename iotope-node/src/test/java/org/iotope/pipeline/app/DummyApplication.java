package org.iotope.pipeline.app;

import java.util.Map;

import org.iotope.IotopeApplication;
import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.iotope.context.MetaData;

@IotopeApplication(domain = "test.iotope.org", name = "test")
public class DummyApplication implements Application {
 
    @Override
    public void execute(ExecutionContext context) {
        context.setField("result", "executed");
    }
    
    @Override
    public MetaData getMetaData() {
        return null;
    }

    @Override
    public void configure(Map<String, String> properties) {
    }
}
