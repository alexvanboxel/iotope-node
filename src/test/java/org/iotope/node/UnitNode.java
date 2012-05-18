package org.iotope.node;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class UnitNode {
    
    private static Node node;
    
    public static <T> T instance(Class<T> c) {
        if(node == null) {
            WeldContainer cdiContainer = new Weld().initialize();
            Node.setContainer( new Weld().initialize());
            Node node = cdiContainer.instance().select(Node.class).get();
        }
        return Node.instance(c);
    }
}
