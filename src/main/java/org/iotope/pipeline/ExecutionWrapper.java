package org.iotope.pipeline;

import org.iotope.context.Application;
import org.iotope.node.Node;

public class ExecutionWrapper {
    
    public ExecutionWrapper(String domain, String name, Class<? extends Application> applicationClass) {
        this.domain = domain;
        this.name = name;
        this.applicationClass = applicationClass;
    }
    
    public void execute(ExecutionContextImpl executionContext) {
        Application application = Node.instance(applicationClass);
        executionContext.switchContext(domain, name);
        application.execute(executionContext);
    }
    
    String domain;
    String name;
    Class<? extends Application> applicationClass;
    
}
