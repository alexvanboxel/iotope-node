package org.iotope.node;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class UnitNode {
    
    private static Node node;
    
    @SuppressWarnings("static-access")
    public static <T> T instance(Class<T> c) {
        if(node == null) {
            WeldContainer cdiContainer = new Weld().initialize();
            Node.setContainer( new Weld().initialize());
            node = cdiContainer.instance().select(Node.class).get();
        }
        return node.instance(c);
    }
}
