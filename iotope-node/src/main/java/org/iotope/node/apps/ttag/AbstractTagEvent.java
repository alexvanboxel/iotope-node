package org.iotope.node.apps.ttag;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;

import org.apache.http.HttpResponse;



/**
 * TagEvent wraps the XML representation of a tag that's already embedded in the SOAP 
 * request. Every setter on this object will shape the XML request.
 */
public abstract class AbstractTagEvent<T> extends ACSBase<T>{
    
    private String ns;

    protected AbstractTagEvent(String template, String root,Class<T> clazz,String ns) throws Exception {
        super(template, root,clazz);
        this.ns = ns;
    }
    
    protected AbstractTagEvent(Document doc, String root,Class<T> clazz,String ns) throws Exception {
        super(doc, root,clazz);
        this.ns = ns;
    }
    
    protected AbstractTagEvent(HttpResponse response, String root,Class<T> clazz,String ns) throws Exception {
        super(response, root,clazz);
        this.ns = ns;
    }
    
    protected AbstractTagEvent(Element element, String root,Class<T> clazz,String ns) throws Exception {
        super(element, root,clazz);
        this.ns = ns;
    }

    /**
     * Set the tag part in the request.
     * 
     * @type type of the tag (to determine the URN)
     * @param tagId
     */
    public void setTag(TagType type,String tagId) {
        setTextValue(ns+":handleTagEvent/actionTag/tagId", type.getURN() + tagId);
    }
    
    public void setLocation(String location) {
        setTextValue(ns+":handleTagEvent/clientId/name", location);
    }
    
    public void setClientIdId(String clientIdId) {
        setTextValue("c12:handleTagEvent/clientId/id", clientIdId);
    }
    
    public void setTagData(String tagData) {
        setTextValue(ns+":handleTagEvent/actionTag/tagData", tagData);
    }
    
    public void addAttachment(String uri,String value) {
        Element element = new Element("attachments");
        element.addAttribute(new Attribute("identifier",uri));
        element.appendChild(value);
        Element body = getElement(expand(ns+":handleTagEvent"));
        body.insertChild(element, 6);
    }

}
