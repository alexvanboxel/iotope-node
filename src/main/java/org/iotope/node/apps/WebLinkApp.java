package org.iotope.node.apps;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;

import org.iotope.node.model.FieldValue;

public class WebLinkApp {
    
    private EntityManager em;
    
    public WebLinkApp(EntityManager em) {
        this.em = em;
    }
    
    public void execute(Map<String, FieldValue> values)  {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(URI.create(values.get("url").getValue()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void execute(Collection<FieldValue> values) {
        Map<String, FieldValue> map = new HashMap<String, FieldValue>();
        for (FieldValue value : values) {
            map.put(value.getField(), value);
        }
        execute(map);
    }
    
    
}
