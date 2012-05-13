package org.iotope.node.conf;

import java.io.InputStream;
import java.net.URI;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;


public class ConfigReader extends ConfigIO {
    
    private XMLStreamReader reader;
    private Cfg config;
    
    public ConfigReader(URI uri, InputStream stream) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        reader = xmlInputFactory.createXMLStreamReader(new StreamSource(stream));
    }
    
    public Cfg read() throws Exception {
        config = new Cfg();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamReader.START_ELEMENT) {
                QName name = reader.getName();
                if (IOTOPE_NODE.equals(name)) {
                } else if (PIPELINE.equals(name)) {
                    CfgPipeline pipeline = readPipeline();
                } else if (TECH.equals(name)) {
                    CfgTech tech = readTech();
                } else if (READER.equals(name)) {
                    readReader();
                } else {
                    throwUnexpectedElement(name);
                }
                
            } else if (event == XMLStreamReader.END_ELEMENT) {
                QName name = reader.getName();
                if (IOTOPE_NODE.equals(name))
                    return config;
            }
        }
        return null;
    }
    
    private CfgPipeline readPipeline() throws Exception {
        String inherit = reader.getAttributeValue(null, "inherit");
        CfgPipeline cfg = new CfgPipeline();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamReader.START_ELEMENT) {
                QName name = reader.getName();
                if (APPLICATION.equals(name)) {
                    readApplication();
                } else if (PLACEHOLDER2.equals(name)) {
                } else {
                    throwUnexpectedElement(name);
                }
            } else if (event == XMLStreamReader.END_ELEMENT) {
                QName name = reader.getName();
                if (PIPELINE.equals(name))
                    return cfg;
            }
        }
        throw new RuntimeException("Unexpected end");
    }
    
    private CfgApplication readApplication() throws Exception {
        String inherit = reader.getAttributeValue(null, "inherit");
        CfgApplication cfg = new CfgApplication();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamReader.START_ELEMENT) {
                QName name = reader.getName();
                if (FILTER.equals(name)) {
                    readFilter();
                } else if (PLACEHOLDER2.equals(name)) {
                } else {
                    throwUnexpectedElement(name);
                }
                
            } else if (event == XMLStreamReader.END_ELEMENT) {
                QName name = reader.getName();
                if (APPLICATION.equals(name))
                    return cfg;
            }
        }
        throw new RuntimeException("Unexpected end");
    }
    
    private CfgFilter readFilter() throws Exception {
        String inherit = reader.getAttributeValue(null, "inherit");
        CfgFilter cfg = new CfgFilter();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamReader.START_ELEMENT) {
                QName name = reader.getName();
                if (PLACEHOLDER1.equals(name)) {
                } else if (PLACEHOLDER2.equals(name)) {
                } else {
                    throwUnexpectedElement(name);
                }
            } else if (event == XMLStreamReader.END_ELEMENT) {
                QName name = reader.getName();
                if (FILTER.equals(name))
                    return cfg;
            }
        }
        throw new RuntimeException("Unexpected end");
    }
    
    private CfgReader readReader() throws Exception {
        String inherit = reader.getAttributeValue(null, "inherit");
        CfgReader cfg = new CfgReader();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamReader.START_ELEMENT) {
                QName name = reader.getName();
                if (HARDWARE.equals(name)) {
                    readHardware();
                } else if (PLACEHOLDER2.equals(name)) {
                } else {
                    throwUnexpectedElement(name);
                }
            } else if (event == XMLStreamReader.END_ELEMENT) {
                QName name = reader.getName();
                if (READER.equals(name))
                    return cfg;
            }
        }
        throw new RuntimeException("Unexpected end");
    }
    
    private CfgFilter readHardware() throws Exception {
        String inherit = reader.getAttributeValue(null, "inherit");
        CfgFilter cfg = new CfgFilter();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamReader.START_ELEMENT) {
                QName name = reader.getName();
                if (PROPERTY.equals(name)) {
                    readProperty();
                } else if (PLACEHOLDER2.equals(name)) {
                } else {
                    throwUnexpectedElement(name);
                }
            } else if (event == XMLStreamReader.END_ELEMENT) {
                QName name = reader.getName();
                if (HARDWARE.equals(name))
                    return cfg;
            }
        }
        throw new RuntimeException("Unexpected end");
    }
    
    private CfgTech readTech() throws Exception {
        return new CfgTech(getAttr("tech"), getAttr("protocol"), getAttr("detect"), getAttr("ndef"), getAttr("cache"), getAttr("meta"));
    }
    
    private CfgProperty readProperty() throws Exception {
        return new CfgProperty(getAttr("name"), getAttr("value"));
    }
    
    //    private CfgTech readTech() throws Exception {
    //        return new CfgTech(getAttr("tech"), getAttr("protocol"), getAttr("detect"), getAttr("ndef"), getAttr("cache"), getAttr("meta"));
    //    }
    
    protected void throwUnexpectedElement(QName name) {
        throw new RuntimeException("Unexpected element: " + name.toString());
    }
    
    protected String getAttr(String localName) {
        return reader.getAttributeValue(null, localName);
    }
    
}
