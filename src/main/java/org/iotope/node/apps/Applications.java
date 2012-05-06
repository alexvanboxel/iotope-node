package org.iotope.node.apps;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.inject.Singleton;

import org.iotope.IotopeAction;
import org.iotope.IotopeApplication;
import org.iotope.context.Application;
import org.reflections.Reflections;

@Singleton
public class Applications {
    
    @SuppressWarnings("unchecked")
    public Applications() {
        Reflections reflections = new Reflections("org.iotope");
        Set<Class<?>> applicationClasses = reflections.getTypesAnnotatedWith(IotopeApplication.class);
        for(Class<?> applicationClass : applicationClasses) {
            IotopeApplication info = applicationClass.getAnnotation(IotopeApplication.class);
            String urn = "urn:iotope.app:"+info.domain() + ":" + info.name();
            
            applications.put(urn, (Class<? extends Application>) applicationClass);
        }
        
        applicationClasses = reflections.getTypesAnnotatedWith(IotopeAction.class);
        for(Class<?> applicationClass : applicationClasses) {
            IotopeAction info = applicationClass.getAnnotation(IotopeAction.class);
            String urn = "urn:iotope.app:"+info.domain() + ":" + info.name();
            
            applications.put(urn, (Class<? extends Application>) applicationClass);
        }
        
        System.out.println();
    }
    
    public Application getApplication(String urn) throws InstantiationException, IllegalAccessException {
        Class<? extends Application> clazz = applications.get(urn);
        return clazz.newInstance();
    }
    
    public Application getAction(String urn) throws InstantiationException, IllegalAccessException {
        Class<? extends Application> clazz = action.get(urn);
        return clazz.newInstance();
    }
    
    Map<String,Class<? extends Application>> applications = new HashMap<String,Class<? extends Application>>();
    Map<String,Class<? extends Application>> action = new HashMap<String,Class<? extends Application>>();
}
