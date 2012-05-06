package org.iotope.node.apps;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.iotope.IotopeAction;
import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@IotopeAction(domain="iotope.org",name="weblink")
public class WebLink implements Application {
    private static Logger Log = LoggerFactory.getLogger(WebLink.class);
        
//    public void execute(Map<String, FieldValue> values) {
//        Desktop desktop = Desktop.getDesktop();
//        try {
//            desktop.browse(URI.create(values.get("url").getValue()));
//        } catch (IOException e) {
//            Log.error(e.getMessage());
//        }
//    }
//    
//    public void execute(Collection<FieldValue> values) {
//        Map<String, FieldValue> map = new HashMap<String, FieldValue>();
//        for (FieldValue value : values) {
//            map.put(value.getField().getName(), value);
//        }
//        execute(map);
//    }

    @Override
    public void execute(ExecutionContext context) {
        String url = (String) context.getField("url");
        if(url != null) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(URI.create(url));
            } catch (IOException e) {
                Log.error(e.getMessage());
            }
        }
        else {
            Log.trace("Nothing for me in the context.");
        }
    }
}
