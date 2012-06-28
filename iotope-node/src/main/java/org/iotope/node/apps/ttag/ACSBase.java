package org.iotope.node.apps.ttag;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.Serializer;
import nu.xom.XPathContext;

import org.apache.http.HttpResponse;

public abstract class ACSBase<T> {
    
    protected Document _doc;
    protected Element _element;
    protected String _root;
    protected static final XPathContext _xpathContext = new XPathContext();
    protected Class<T> clazz;
    static {
        _xpathContext.addNamespace("soap", "http://schemas.xmlsoap.org/soap/envelope/");
        _xpathContext.addNamespace("tag", "http://www.touchatag.com/acs/api/correlation-1.1");
        _xpathContext.addNamespace("c12", "http://www.touchatag.com/acs/api/correlation-1.2");
        _xpathContext.addNamespace("c13", "http://www.touchatag.com/acs/api/correlation-1.3");
        _xpathContext.addNamespace("metadatalabel", "http://acs.touchatag.com/schema/metadataLabel-1.0");
        _xpathContext.addNamespace("metadataitem", "http://acs.touchatag.com/schema/metadataItem-1.0");
        _xpathContext.addNamespace("resttag", "http://acs.touchatag.com/schema/tag-1.0");
        _xpathContext.addNamespace("error", "http://acs.touchatag.com/schema/error-1.0");
    }
    
    protected ACSBase(String template, String root, Class<T> clazz) throws Exception {
        _root = root;
        this.clazz = clazz;
        loadTemplate(template);
    }
    
    protected ACSBase(Document doc, String root, Class<T> clazz) throws Exception {
        _root = root;
        this.clazz = clazz;
        this._doc = doc;
    }
    
    protected ACSBase(HttpResponse response, String root, Class<T> clazz) throws Exception {
        this(xomFrom(response), root, clazz);
    }
    
    protected ACSBase(Element element, String root, Class<T> clazz) throws Exception {
        _doc = element.getDocument();
        _element = element;
        _root = root;
        this.clazz = clazz;
    }
    
    public boolean isConnected() {
        if (_element != null && _doc == null) {
            return true;
        }
        return false;
    }
    
    private void loadTemplate(String template) throws Exception {
        Builder parser = new Builder(false);
        
        InputStream in = null;
        try {
            in = getClass().getResourceAsStream(template);
            _doc = parser.build(in);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
        
    public static Document xomFrom(HttpResponse response) throws Exception {
        Builder parser = new Builder(false);
        Document build = parser.build(response.getEntity().getContent());
        return build;
    }
    
    /**
     * Returns a single node from the dom or null. Multiple nodes will result in an exception.
     * 
     * @param xpath
     *            expression from the root
     * @return
     */
    private Node getNode(String xpath) {
        Nodes nodes = _doc.query(xpath, _xpathContext);
        if (nodes.size() == 0) {
            return null;
        }
        if (nodes.size() > 1) {
            //            Assert.fail("XPath expression '" + xpath + "' should result in a single node.\n\n" + _doc.toXML());
        }
        return nodes.get(0);
    }
    
    /**
     * Returns a list of nodes from the dom.
     * 
     * @param xpath
     *            expression from the root
     * @return
     */
    private List<Node> getNodes(String xpath) {
        Nodes nodes = _doc.query(xpath, _xpathContext);
        if (nodes.size() == 0) {
            return null;
        }
        ArrayList<Node> nodesList = new ArrayList<Node>(nodes.size());
        for (int i = 0; i < nodes.size(); ++i) {
            nodesList.add(nodes.get(i));
        }
        return nodesList;
    }
    
    protected Element getElement(String xpath) {
        Node node = getNode(xpath);
        if (!(node instanceof Element)) {
            //            Assert.fail("XPath expression '" + xpath + "' should result in an Element node.\n\n" + _doc.toXML());
        }
        return (Element) node;
    }
    
    protected List<Element> getElements(String xpath) {
        List<Node> nodes = getNodes(xpath);
        List<Element> elements = new ArrayList<Element>(nodes.size());
        for (Node node : nodes) {
            if (!(node instanceof Element)) {
                //                Assert.fail("XPath expression '" + xpath + "' should result in an Element node.\n\n" + _doc.toXML());
            }
            elements.add((Element) node);
        }
        return elements;
    }
    
    /**
     * Resolves the XPath expression against the root of the entity
     * 
     * @param xpath
     *            the XPath expression starting from the root
     * @return the resolved XPath expression
     */
    protected String expand(String xpath) {
        if (xpath == null || "".equals(xpath)) {
            return _root;
        }
        if (xpath.startsWith("/")) {
            return _root + xpath;
        }
        return _root + "/" + xpath;
    }
    
    public void removeNode(String xpath) {
        Element element = getElement(expand(xpath));
        element.getParent().removeChild(element);
    }
    
    public boolean existNode(String xpath) {
        Node node = getNode(expand(xpath));
        if (node == null) {
            return false;
        }
        return true;
    }
    
    protected void removeChildWhenNoGrandChilderen(Element element) {
        
        
        Elements elements = element.getChildElements();
        for (int ix = 0; ix < elements.size(); ix++) {
            Element child = elements.get(ix);
            if (child.getChildCount() == 0) {
                element.removeChild(child);
            }
        }
    }
    
    public final String getTextValue(String xpath) {
        return getElement(expand(xpath)).getValue();
    }
    
    public final List<String> getTextValues(String xpath) {
        List<Element> elements = getElements(expand(xpath));
        List<String> textValues = new ArrayList<String>(elements.size());
        for (Element element : elements) {
            textValues.add(element.getValue());
        }
        return textValues;
    }
    
    public final void setTextValue(String xpath, String value) {
        Element element = getElement(expand(xpath));
        element.removeChildren();
        element.appendChild(value);
    }
    
    public void setNodeValue(String xpath, Node value) {
        Element element = getElement(expand(xpath));
        element.getParent().replaceChild(element, value);
    }
    
    public String getAttributeValue(String attributeName) {
        return getAttributeValue(null, attributeName);
    }
    
    public String getAttributeValue(String xpath, String attributeName) {
        Element element = getElement(expand(xpath));
        if (element == null) {
            return null;
        }
        Attribute attribute = element.getAttribute(attributeName);
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }
    
    public final String toXML() {
        return _doc.toXML();
    }
    
    public final void toXML(OutputStream outputStream, String encoding) throws IOException {
        Serializer serializer = new Serializer(outputStream, encoding);
        serializer.write(_doc);
        serializer.flush();
    }
    
    @Override
    public String toString() {
        return _doc.toXML();
    }
}
