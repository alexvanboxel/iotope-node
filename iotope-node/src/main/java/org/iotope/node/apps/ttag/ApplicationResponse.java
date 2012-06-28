package org.iotope.node.apps.ttag;

import nu.xom.Element;
import nu.xom.Node;


public class ApplicationResponse {
    
    private Element root;  
    
    public ApplicationResponse(Element root) {
        this.root = root;
    }
    
    public String getId() {
        return root.getAttribute("id").getValue();
    }
    
    public String getValue() {
        if(root.getChildCount() == 0) {
            return null;
        } else if (root.getChildCount() > 1) {
            throw new RuntimeException("");
        }
        // After the above conditions we are sure there is only 1 child
        Node node = root.getChild(0);
        return node.getValue();
    }

    @Override
    public String toString() {
        return root.toXML();
    }
    
    
}
