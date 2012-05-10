package org.iotope.node.apps.ttag.v12;

import nu.xom.Document;

import org.apache.http.HttpResponse;
import org.iotope.node.apps.ttag.ACSBase;
import org.iotope.node.apps.ttag.TagResponse;


/**
 * SOAP response representation that is received after a TagEvent.
 */
public class Tag12Response extends ACSBase<Tag12Response> implements TagResponse {
    
    public Tag12Response(String template) throws Exception {
        super(template, "soap:Envelope/soap:Body", Tag12Response.class);
    }
    
    public Tag12Response(Document doc) throws Exception {
        super(doc, "soap:Envelope/soap:Body", Tag12Response.class);
    }
    
    public Tag12Response(Document doc, String root) throws Exception {
        super(doc, root, Tag12Response.class);
    }
    
    public Tag12Response(HttpResponse response) throws Exception {
        super(response, "soap:Envelope/soap:Body", Tag12Response.class);
    }

    public boolean isSoapFault() {
        return existNode("soap:Fault");
    }
    
    public String getSoapFault() {
        return getTextValue("soap:Fault/faultstring");
    }
    
    public String getContainerName() {
        return getAttributeValue("c12:handleTagEventResponse/applicationResponse/ClientAction/container", "name");
    }
    
    public String getContainerAttribute(String name) {
        return getTextValue("c12:handleTagEventResponse/applicationResponse/ClientAction/container/container/attribute[@name='" + name + "']/string");
    }
    
    public String getSystemMessage() {
        return getTextValue("c12:handleTagEventResponse/systemMessage");
    }
    
    public String getMessage() {
        return getContainerAttribute("message");
    }
    
    @Override
    public boolean hasTransactionFeedback() {
        throw new RuntimeException("Not supported");
    }
    
    @Override
    public String getTransactionId() {
        throw new RuntimeException("Not supported");
    }
    
    @Override
    public boolean existNodeTransactionId() {
        throw new RuntimeException("Not supported");
    }
    
    @Override
    public boolean isDuplicate() {
        throw new RuntimeException("Not supported");
    }
    
    @Override
    public String getTransactionErrorCode() {
        throw new RuntimeException("Not supported");
    }
    
    @Override
    public String getSystemErrorCode() {
        throw new RuntimeException("Not supported");
    }
    
}
