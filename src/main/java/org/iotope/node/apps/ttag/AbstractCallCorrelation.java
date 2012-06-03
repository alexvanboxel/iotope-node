package org.iotope.node.apps.ttag;

import java.net.URI;
import java.nio.charset.Charset;

import javax.inject.Inject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.Header;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.iotope.context.ExecutionContext;
import org.iotope.gateway.http.HttpCredentials;
import org.iotope.gateway.http.HttpGateway;


public abstract class AbstractCallCorrelation {
    
    
    @Inject
    HttpGateway http;
    
    protected HttpResponse fireTag(URI uri, String user, String password, AbstractTagEvent<?> event, TagEventAttachment... attachments) throws Exception {
        
        HttpPost post = new HttpPost(uri);
        
        String boundary = "uuid:51f9e439-fc10-4991-af14-99ec1ab3519e";
        String attid = "7bbd9c3d-eb36-49dc-aea0-6e79ba65c12f@iotope.org";
        
        if (attachments != null && attachments.length > 0) {
            TagEventAttachment workflowAttachment = attachments[0];
            event.addAttachment(workflowAttachment.getUri(), "cid:" + attid);
        }
        MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.STRICT, boundary, Charset.forName("utf-8"));
        StringBody soap = StringBody.create(event.toXML(), "application/xop+xml", Charset.forName("utf-8"));
        FormBodyPart soapBodyPart = new FormBodyPart("soap", soap);
        Header header = soapBodyPart.getHeader();
        soapBodyPart.addField("Content-Id", "<rootpart*51f9e439-fc10-4991-af14-99ec1ab3519e@iotope.org>");
        header.removeFields("Content-Disposition");
        multipart.addPart(soapBodyPart);
        
        StringBody attach = null;
        for (TagEventAttachment attachment : attachments) {
            Object content = attachment.getContent();
            if (content instanceof String) {
                attach = StringBody.create((String) content, "plain/text", Charset.forName("utf-8"));
            } else {
                throw new RuntimeException("Unknown attachment type");
            }
            
            FormBodyPart attachBodyPart = new FormBodyPart("attachment", attach);
            header = attachBodyPart.getHeader();
            attachBodyPart.addField("Content-Id", "<7bbd9c3d-eb36-49dc-aea0-6e79ba65c12f@iotope.org>");
            header.removeFields("Content-Disposition");
            multipart.addPart(attachBodyPart);
        }
        
        post.setHeader("Content-Type", "multipart/related;start=\"<rootpart*51f9e439-fc10-4991-af14-99ec1ab3519e@iotope.org>\";type=\"application/xop+xml\";boundary=\"" + boundary + "\";start-info=\"text/xml\"");
        post.setEntity(multipart);
        
        
        HttpResponse response = http.execute(post, new HttpCredentials(user, password));
        return response;
    }
    
    protected void detectApplications(ExecutionContext context, TagResponse response) throws Exception {
        String name = response.getContainerName();
        if(response.isSoapFault()) {
            context.executeNext("iotope.org", "notify", "caption", "System Message");
            context.executeNext("iotope.org", "notify", "message", response.getSoapFault());
            context.executeNext("iotope.org", "notify", "type", "ERROR");
        }
        else if ("tikitag.standard.url".equals(name)) {
            context.executeNext("iotope.org", "weblink", "url", response.getContainerAttribute("url"));
        } else if ("tikitag.standard.tagManagement".equals(name)) {
            context.executeNext("iotope.org", "notify", "caption", "Legacy Tag Management");
            context.executeNext("iotope.org", "notify", "message", response.getContainerAttribute("message"));
            context.executeNext("iotope.org", "notify", "type", "WARNING");
        } else {
            String message = response.getSystemMessage();
            if (message != null) {
                context.executeNext("iotope.org", "notify", "caption", "System Message");
                context.executeNext("iotope.org", "notify", "message", message);
                context.executeNext("iotope.org", "notify", "type", "WARNING");
            }
        }
    }
    
}
