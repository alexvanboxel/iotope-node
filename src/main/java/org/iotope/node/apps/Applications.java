package org.iotope.node.apps;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.iotope.IotopeAction;
import org.iotope.IotopeApplication;
import org.iotope.context.Application;
import org.iotope.context.MetaData;
import org.iotope.node.Node;
import org.iotope.pipeline.ExecutionWrapper;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Applications {
    private static Logger Log = LoggerFactory.getLogger(Applications.class);
    
    @SuppressWarnings("unchecked")
    public Applications() {
        Reflections reflections = new Reflections("");
        Set<Class<?>> applicationClasses = reflections.getTypesAnnotatedWith(IotopeApplication.class);
        for(Class<?> applicationClass : applicationClasses) {
            IotopeApplication info = applicationClass.getAnnotation(IotopeApplication.class);
            String urn = "urn:iotope.app:"+info.domain() + ":" + info.name();
            Log.info("Registered application: "+urn);
            persistApplication(info.domain(),info.name(),(Class<? extends Application>)applicationClass);
            applications.put(urn, new ExecutionWrapper(info.domain(),info.name(),(Class<? extends Application>) applicationClass));
        }
        
        applicationClasses = reflections.getTypesAnnotatedWith(IotopeAction.class);
        for(Class<?> applicationClass : applicationClasses) {
            IotopeAction info = applicationClass.getAnnotation(IotopeAction.class);
            String urn = "urn:iotope.app:"+info.domain() + ":" + info.name();
            Log.info("Registered action: "+urn);
            persistApplication(info.domain(),info.name(),(Class<? extends Application>)applicationClass);
            applications.put(urn, new ExecutionWrapper(info.domain(),info.name(),(Class<? extends Application>) applicationClass));
        }
        
        System.out.println();
    }
    
    private void persistApplication(String domain, String name, Class<? extends Application> applicationClass) {
        try {
            Application application = applicationClass.newInstance();
            MetaData metadata = application.getMetaData();
            if(metadata != null) {
                Correlation correlation = Node.instance(Correlation.class);
                correlation.addApplication(domain,name,metadata.getDisplayName(),metadata.getDescription(),metadata.getFields());
            }
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public ExecutionWrapper getApplication(String urn) throws InstantiationException, IllegalAccessException {
        return applications.get(urn);
    }
    
//    public Application getAction(String urn) throws InstantiationException, IllegalAccessException {
//        return Node.instance(applications.get(urn));
//    }
    
    Map<String,ExecutionWrapper> applications = new HashMap<String,ExecutionWrapper>();
    Map<String,ExecutionWrapper> action = new HashMap<String,ExecutionWrapper>();
}

