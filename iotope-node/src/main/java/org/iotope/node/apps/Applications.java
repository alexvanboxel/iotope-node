package org.iotope.node.apps;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.iotope.IotopeAction;
import org.iotope.IotopeApplication;
import org.iotope.IotopeFilter;
import org.iotope.context.Application;
import org.iotope.context.Filter;
import org.iotope.context.MetaData;
import org.iotope.node.Node;
import org.iotope.node.conf.CfgApplication;
import org.iotope.node.conf.CfgFilter;
import org.iotope.node.persistence.Correlation;
import org.iotope.pipeline.ExecutionWrapper;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Applications {
    private static Logger Log = LoggerFactory.getLogger(Applications.class);
    
    @SuppressWarnings("unchecked")
    public Applications() {
        Reflections reflections = new Reflections("org.iotope","com.iotope","test.iotope.org","ext.iotope");
        Set<Class<?>> applicationClasses = reflections.getTypesAnnotatedWith(IotopeApplication.class);
        for (Class<?> applicationClass : applicationClasses) {
            IotopeApplication info = applicationClass.getAnnotation(IotopeApplication.class);
            String urn = "urn:iotope.app:" + info.domain() + ":" + info.name();
            Log.info("Registered application: " + urn);
            persistApplication(info.domain(), info.name(), (Class<? extends Application>) applicationClass);
            applications.put(urn, new ExecutionWrapper.Description(info.domain(), info.name(), (Class<? extends Application>) applicationClass));
        }
        
        applicationClasses = reflections.getTypesAnnotatedWith(IotopeAction.class);
        for (Class<?> applicationClass : applicationClasses) {
            IotopeAction info = applicationClass.getAnnotation(IotopeAction.class);
            String urn = "urn:iotope.app:" + info.domain() + ":" + info.name();
            Log.info("Registered action: " + urn);
            persistApplication(info.domain(), info.name(), (Class<? extends Application>) applicationClass);
            applications.put(urn, new ExecutionWrapper.Description(info.domain(), info.name(), (Class<? extends Application>) applicationClass));
        }
        
        Set<Class<?>> filterClasses = reflections.getTypesAnnotatedWith(IotopeFilter.class);
        for (Class<?> filterClass : filterClasses) {
            IotopeFilter info = filterClass.getAnnotation(IotopeFilter.class);
            String urn = "urn:iotope.filter:" + info.domain() + ":" + info.name();
            Log.info("Registered filter: " + urn);
            filters.put(urn, (Class<? extends Filter>) filterClass);
        }
        
    }
    
    private void persistApplication(String domain, String name, Class<? extends Application> applicationClass) {
        try {
            Application application = applicationClass.newInstance();
            MetaData metadata = application.getMetaData();
            if (metadata != null) {
                Correlation correlation = Node.instance(Correlation.class);
                correlation.addApplication(domain, name, metadata.getDisplayName(), metadata.getDescription(), metadata.getFields());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Class<? extends Filter> createFilter(CfgFilter cfgFilter) {
        return filters.get(cfgFilter.getURN());
    }
    
    public ExecutionWrapper createWrappedApplication(CfgApplication cfgApp) throws InstantiationException, IllegalAccessException {
        ExecutionWrapper wrapper = null;
        try {
            wrapper = new ExecutionWrapper(applications.get(cfgApp.getURN()));
            Application application = Node.instance(wrapper.getApplicationClass());
            wrapper.configure(cfgApp,application);
            for(CfgFilter f : cfgApp.getFilters()) {
                Class<? extends Filter> cf = createFilter(f);
                Filter filter = Node.instance(cf);
                filter.configure(f.getProperties());
                wrapper.addFilter(f,filter);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return wrapper;
    }
    
    //    public Application getAction(String urn) throws InstantiationException, IllegalAccessException {
    //        return Node.instance(applications.get(urn));
    //    }
    
    Map<String, ExecutionWrapper.Description> applications = new HashMap<String, ExecutionWrapper.Description>();
    Map<String, ExecutionWrapper> action = new HashMap<String, ExecutionWrapper>();
    Map<String, Class<? extends Filter>> filters = new HashMap<String, Class<? extends Filter>>();
}
