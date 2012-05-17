package org.iotope.pipeline;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.iotope.node.apps.Applications;
import org.iotope.node.conf.Cfg;
import org.iotope.node.conf.CfgApplication;

public class ExecutionPipeline {
    
    public void initPipeline(Cfg cfg, ExecutionContextImpl executionContext) {
        this.executionContext = executionContext;
        // Fake a pipeline
        try {
            List<CfgApplication> cfgApps = cfg.getPipeline().getApplications();
            for(CfgApplication cfgApp : cfgApps) {
                plan.add(applications.getApplication(cfgApp.getURN()));
            }
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
