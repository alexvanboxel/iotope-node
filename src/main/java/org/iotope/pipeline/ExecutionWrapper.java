package org.iotope.pipeline;

import java.util.ArrayList;
import java.util.List;

import org.iotope.context.Application;
import org.iotope.context.Filter;
import org.iotope.node.conf.CfgApplication;

public class ExecutionWrapper implements Cloneable {
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    public ExecutionWrapper(String domain, String name, Class<? extends Application> applicationClass) {
        this.domain = domain;
        this.name = name;
        this.applicationClass = applicationClass;
    }
    
    public Class<? extends Application> getApplicationClass() {
        return applicationClass;
    }
    
    public void configure(CfgApplication cfg,Application application) {
        this.cfg = cfg;
        this.application = application;
    }

    /**
     * @return true if the application is executable in the current context  
     */
    public boolean isExecutable(ExecutionContextImpl executionContext) {
//        Application application = Node.instance(applicationClass);
//        executionContext.switchContext(domain, name);
//        application.execute(executionContext);
        return true;
    }
    
    public void execute(ExecutionContextImpl executionContext) {
        executionContext.switchContext(domain, name);
        application.execute(executionContext);
    }
    
    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    Application application;
    List<Filter> filters = new ArrayList<Filter>();
    
    String domain;
    String name;
    Class<? extends Application> applicationClass;
    
    CfgApplication cfg;
}
