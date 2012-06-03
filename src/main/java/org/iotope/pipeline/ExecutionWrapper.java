package org.iotope.pipeline;

import java.util.ArrayList;
import java.util.List;

import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.iotope.context.Filter;
import org.iotope.node.conf.CfgApplication;
import org.iotope.node.conf.CfgFilter;
import org.iotope.node.conf.CfgFilter.FilterType;

public class ExecutionWrapper {
    
    public static class Description {
        String domain;
        String name;
        Class<? extends Application> applicationClass;
        
        public Description(String domain, String name, Class<? extends Application> applicationClass) {
            this.domain = domain;
            this.name = name;
            this.applicationClass = applicationClass;
        }
    }
    
    public ExecutionWrapper(Description description) {
        this.description = description;
    }
    
    private class FilterWrapper {
        
        private FilterWrapper(CfgFilter cfgFilter, Filter filter) {
            super();
            this.filter = filter;
            this.cfgFilter = cfgFilter;
        }
        
        private Filter filter;
        private CfgFilter cfgFilter;
        
        public boolean match(ExecutionContext context) {
            return filter.match(context);
        }
        
        public FilterType getFilterType() {
            return cfgFilter.getType();
        }
    }
    
    public Class<? extends Application> getApplicationClass() {
        return description.applicationClass;
    }
    
    public void configure(CfgApplication cfg, Application application) {
        this.cfg = cfg;
        this.application = application;
    }
    
    /**
     * @return true if the application is executable in the current context  
     */
    public boolean isExecutable(ExecutionContextImpl executionContext) {
        if (filters.size() == 0) {
            return true;
        }
        
        int cInclude = 0, cExclude = 0;
        for (FilterWrapper filter : filters) {
            FilterType type = filter.getFilterType();
            switch (type) {
            case INCLUDE:
                cInclude++;
                break;
            case EXCLUDE:
                cExclude++;
                break;
            }
        }
        if (cInclude == filters.size()) {
            for (FilterWrapper filter : filters) {
                if (filter.match(executionContext)) {
                    return true;
                }
            }
            return false;
        } else if (cExclude == filters.size()) {
            for (FilterWrapper filter : filters) {
                if (filter.match(executionContext)) {
                    return false;
                }
            }
            return true;
        }
        
        CfgFilter.FilterType state = CfgFilter.FilterType.UNKNOWN;
        for (FilterWrapper filter : filters) {
            if (filter.match(executionContext)) {
                CfgFilter.FilterType current = filter.getFilterType();
                if (current == CfgFilter.FilterType.INCLUDE) {
                    state = CfgFilter.FilterType.INCLUDE;
                } else if (current == CfgFilter.FilterType.EXCLUDE) {
                    return false;
                }
            }
        }
        
        if (state == CfgFilter.FilterType.INCLUDE) {
            return true;
        }
        
        return false;
    }
    
    public void execute(ExecutionContextImpl executionContext) {
        executionContext.switchContext(description.domain, description.name);
        application.configure(cfg.getProperties());
        application.execute(executionContext);
    }
    
    public void addFilter(CfgFilter f, Filter filter) {
        filters.add(new FilterWrapper(f, filter));
    }
    
    Application application;
    List<FilterWrapper> filters = new ArrayList<FilterWrapper>();
    
    CfgApplication cfg;
    
    Description description;
}
