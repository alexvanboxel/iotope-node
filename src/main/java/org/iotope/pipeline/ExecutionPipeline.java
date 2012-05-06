package org.iotope.pipeline;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.iotope.context.Application;
import org.iotope.node.apps.Applications;
import org.iotope.pipeline.model.Field;

public class ExecutionPipeline {
    
    public void initPipeline(ExecutionContextImpl executionContext) {
        this.executionContext = executionContext;
        // Fake a pipeline
        try {
            plan.add(applications.getApplication("urn:iotope.app:iotope.org:weblink"));
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void startPipeline() {
        for(Application app : plan) {
            app.execute(executionContext);
        }
        
        
    }
    
    ExecutionContextImpl executionContext;

    @Inject
    Applications applications;
    
    List<Application> plan = new ArrayList<Application>();
}
