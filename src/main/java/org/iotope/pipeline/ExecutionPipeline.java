package org.iotope.pipeline;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.iotope.context.Application;
import org.iotope.node.apps.Applications;

public class ExecutionPipeline {
    
    public void initPipeline(ExecutionContextImpl executionContext) {
        this.executionContext = executionContext;
        // Fake a pipeline
        try {
            plan.add(applications.getApplication("urn:iotope.app:iotope.org:ndef"));
//            plan.add(applications.getApplication("urn:iotope.app:iotope.org:ttag.c12"));
//            plan.add(applications.getApplication("urn:iotope.app:iotope.org:webhook"));
            plan.add(applications.getApplication("urn:iotope.app:iotope.org:weblink"));
            plan.add(applications.getApplication("urn:iotope.app:iotope.org:notify"));
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void startPipeline() {
        for(ExecutionWrapper app : plan) {
            
            
            app.execute(executionContext);
        }
        
        
    }
    
    ExecutionContextImpl executionContext;

    @Inject
    Applications applications;
    
    List<ExecutionWrapper> plan = new ArrayList<ExecutionWrapper>();
}
