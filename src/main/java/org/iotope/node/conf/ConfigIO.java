package org.iotope.node.conf;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamReader;

public class ConfigIO {
    protected static final QName IOTOPE_NODE = new QName("iotope-node");
    protected static final QName DISCOVERY = new QName("discovery");
    protected static final QName TECH = new QName("tech");
    protected static final QName PIPELINE = new QName("pipeline");
    protected static final QName APPLICATION = new QName("application");
    protected static final QName FILTER = new QName("filter");
    protected static final QName READER = new QName("reader");
    protected static final QName HARDWARE = new QName("hardware");
    protected static final QName PROPERTY = new QName("property");

    protected static final QName PLACEHOLDER1 = new QName("ph1");
    protected static final QName PLACEHOLDER2 = new QName("ph2");
}
