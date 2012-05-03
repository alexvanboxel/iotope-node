package org.iotope.node.apps;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.iotope.node.model.FieldValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebLinkApp {
    private static Logger Log = LoggerFactory.getLogger(WebLinkApp.class);
    
    public WebLinkApp() {
    }
    
    public void execute(Map<String, FieldValue> values) {
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(URI.create(values.get("url").getValue()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.error(e.getMessage());
        }
    }
    
    public void execute(Collection<FieldValue> values) {
        Map<String, FieldValue> map = new HashMap<String, FieldValue>();
        for (FieldValue value : values) {
            map.put(value.getField().getName(), value);
        }
        execute(map);
    }
    
    
}
