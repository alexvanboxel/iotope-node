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
                    config.setPipeline(readPipeline());
                } else if (TECH.equals(name)) {
                    config.addTechnology(readTech());
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
                    cfg.addApplications(readApplication());
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
        CfgApplication cfg = new CfgApplication(getAttr("urn"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamReader.START_ELEMENT) {
                QName name = reader.getName();
                if (FILTER.equals(name)) {
                    cfg.addFilter(readFilter());
                } else if (PROPERTY.equals(name)) {
                    cfg.addProperty(getAttr("name"), readProperty());
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
        CfgFilter cfg = new CfgFilter(getAttr("urn"),getAttr("type"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamReader.START_ELEMENT) {
                QName name = reader.getName();
                if (PLACEHOLDER1.equals(name)) {
                } else if (PROPERTY.equals(name)) {
                    cfg.addProperty(getAttr("name"), readProperty());
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
    
    private CfgHardware readHardware() throws Exception {
        String inherit = reader.getAttributeValue(null, "inherit");
        CfgHardware cfg = new CfgHardware();
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
        return new CfgTech(getAttr("type"), getAttr("protocol"), getAttr("detect"), getAttr("ndef"), getAttr("cache"), getAttr("meta"));
    }
    
    private String readProperty() throws Exception {
        String text = "";
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamReader.CHARACTERS) {
                text += reader.getText();
            } else if (event == XMLStreamReader.END_ELEMENT) {
                QName name = reader.getName();
                if (PROPERTY.equals(name))
                    return text.trim();
            }
        }
        throw new RuntimeException("Unexpected end");
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
