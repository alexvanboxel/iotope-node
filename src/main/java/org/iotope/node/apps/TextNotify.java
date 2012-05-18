package org.iotope.node.apps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.iotope.IotopeAction;
import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.iotope.context.MetaData;
import org.iotope.node.NodeTray;
import org.iotope.pipeline.model.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@IotopeAction(domain = "iotope.org", name = "notify")
public class TextNotify implements Application {
    private static Logger Log = LoggerFactory.getLogger(TextNotify.class);
    
    @Inject
    private NodeTray nodeTray;
    
    @Override
    public void execute(ExecutionContext context) {
        String caption = (String) context.getField("caption");
        String message = (String) context.getField("message");
        String type = (String) context.getField("type");
        if (message != null || caption != null) {
            nodeTray.message(caption, message, type);
        } else {
            Log.trace("Nothing for me in the context.");
        }
    }
    
    private static class TextNotifyMetaData implements MetaData {
        
        @Override
        public Collection<Field> getFields() {
            List<Field> fields = new ArrayList<Field>();
            fields.add(new Field("caption", "xs:string", "Caption", "Caption above the message."));
            fields.add(new Field("message", "xs:string", "Message", "Message to display."));
            fields.add(new Field("type", "xs:string", "Type", "The notification type."));
            return fields;
        }
        
        @Override
        public String getDisplayName() {
            return "Text Notify";
        }
        
        @Override
        public String getDescription() {
            return "Display a notification.";
        }
        
    }
    
    @Override
    public MetaData getMetaData() {
        return new TextNotifyMetaData();
    }
    
    @Override
    public void configure(Map<String, String> properties) {
        // Nothing to configure
    }
}
