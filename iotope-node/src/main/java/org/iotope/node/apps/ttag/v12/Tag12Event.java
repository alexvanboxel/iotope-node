package org.iotope.node.apps.ttag.v12;

import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import nu.xom.Attribute;
import nu.xom.Element;

import org.iotope.node.apps.ttag.AbstractTagEvent;

/**
 * TagEvent (for ACS SOAP 1.2) wraps the XML representation of a tag that's already embedded in the SOAP request. Every setter on this object will shape the XML request.
 */
public class Tag12Event extends AbstractTagEvent<Tag12Event> {
    
    public Tag12Event() throws Exception {
        super("tagevent.xml", "soap:Envelope/soap:Body", Tag12Event.class, "c12");
    }
    
    
    /**
     * Set the transaction information (1.2 specific)
     * 
     * @param lifecycle
     * @param now
     * @param reference
     * @throws Exception
     */
    public void setTransaction(String lifecycle, Date now, String reference) throws Exception {
        GregorianCalendar calandar = new GregorianCalendar();
        calandar.setTime(now);
        XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(calandar);
        Element tranaction = new Element("transaction");
        tranaction.addAttribute(new Attribute("lifecycle", lifecycle));
        Element element = new Element("timestamp");
        System.out.println(xmlCalendar.toXMLFormat());
        element.appendChild(xmlCalendar.toXMLFormat());
        tranaction.appendChild(element);
        element = new Element("reference");
        element.appendChild(reference);
        tranaction.appendChild(element);
        Element body = getElement(expand("c12:handleTagEvent"));
        body.appendChild(tranaction);
    }
    
}
