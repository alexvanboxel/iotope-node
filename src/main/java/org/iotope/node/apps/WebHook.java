package org.iotope.node.apps;

import java.io.IOException;
import java.net.URI;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.inject.Inject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.iotope.IotopeApplication;
import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.iotope.context.MetaData;
import org.iotope.gateway.http.HttpGateway;

@IotopeApplication(domain = "iotope.org", name = "webhook")
public class WebHook implements Application {
 
    @Inject
    HttpGateway http;
    

    @Override
    public void execute(ExecutionContext context) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String json = mapper.writeValueAsString(context);

            System.out.println(json);

            URI uri = URI.create("http://localhost:8811/iotopehook");
            
            HttpPost post = new HttpPost(uri);
            post.setHeader("Accept", "application/vnd.iotope-0.1+json");
            StringEntity stringEntity = new StringEntity(json);
            stringEntity.setContentType("application/vnd.iotope-0.1+json");
            post.setEntity(stringEntity);
            
            HttpResponse response = http.execute(post, null);
            String out = EntityUtils.toString(response.getEntity());
            
            
            JsonNode rootNode = mapper.readValue(out, JsonNode.class);
            System.out.println(rootNode);
            
            
            Iterator<Entry<String, JsonNode>> apps = rootNode.getFields();
            while(apps.hasNext()) {
                Entry<String, JsonNode> app = apps.next();
                String appURN = app.getKey();

                String[] parts = appURN.split(":");
                String domainName = parts[2];
                String appName = parts[3];
                
                Iterator<Entry<String, JsonNode>> fields  = app.getValue().getFields();
                while(fields.hasNext()) {
                    Entry<String, JsonNode> field = fields.next();
                    String fieldName = field.getKey();
                    String fieldValue = field.getValue().asText();
                    context.setField(domainName, appName, fieldName, fieldValue);
                }

            }
            
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public MetaData getMetaData() {
        return null;
    }
}
