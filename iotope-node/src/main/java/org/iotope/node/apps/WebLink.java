package org.iotope.node.apps;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.iotope.IotopeAction;
import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.iotope.context.MetaData;
import org.iotope.pipeline.model.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@IotopeAction(domain = "iotope.org", name = "weblink")
public class WebLink implements Application {
    private static Logger Log = LoggerFactory.getLogger(WebLink.class);
    
    @Override
    public void execute(ExecutionContext context) {
        String url = (String) context.getField("url");
        if (url != null) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(URI.create(url));
            } catch (IOException e) {
                Log.error(e.getMessage());
            }
        } else {
            Log.trace("Nothing for me in the context.");
        }
    }
    
    private static class WebLinkMetaData implements MetaData {
        
        @Override
        public Collection<Field> getFields() {
            List<Field> fields = new ArrayList<Field>();
            fields.add(new Field("url", "xs:string", "URL", "The URL to jump to."));
            return fields;
        }

        @Override
        public String getDisplayName() {
            return "Weblink";
        }

        @Override
        public String getDescription() {
            return "Jump to an URL";
        }
        
    }
    
    @Override
    public MetaData getMetaData() {
        return new WebLinkMetaData();
    }

    @Override
    public void configure(Map<String, String> properties) {
        // Nothing to configure
    }
}
