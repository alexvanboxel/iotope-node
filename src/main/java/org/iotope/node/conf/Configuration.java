package org.iotope.node.conf;

import javax.inject.Singleton;

@Singleton
public class Configuration {
    
    private boolean readTagContent;
    
    private String nodeName;
    
    private boolean learnMode;
    
    private String nodeGroup;
    
    private String server;
    
    private String defaultApplication;
    
    private boolean cacheTagContent;
    
    private boolean executeAssociated;
    
    private String nodeToken;
    
    public void set(String key, String value) {
        if ("readTagContent".equals(key)) {
            setReadTagContent(Boolean.valueOf(value));
        } else if ("nodeName".equals(key)) {
            setNodeName(value);
        } else if ("learnMode".equals(key)) {
            setLearnMode(Boolean.valueOf(value));
        } else if ("nodeGroup".equals(key)) {
            setNodeGroup(value);
        } else if ("server".equals(key)) {
            setServer(value);
        } else if ("defaultApplication".equals(key)) {
            setDefaultApplication(value);
        } else if ("cacheTagContent".equals(key)) {
            setCacheTagContent(Boolean.valueOf(value));
        } else if ("executeAssociated".equals(key)) {
            setExecuteAssociated(Boolean.valueOf(value));
        } else if ("nodeToken".equals(key)) {
            setNodeToken(value);
        } else {
            throw new RuntimeException("Unknown configuration property : " + key);
        }
    }
    
    public String get(String key) {
        if ("readTagContent".equals(key)) {
            return String.valueOf(isReadTagContent());
        } else if ("nodeName".equals(key)) {
            return getNodeName();
        } else if ("learnMode".equals(key)) {
            return String.valueOf(isLearnMode());
        } else if ("nodeGroup".equals(key)) {
            return getNodeGroup();
        } else if ("server".equals(key)) {
            return getServer();
        } else if ("defaultApplication".equals(key)) {
            return getDefaultApplication();
        } else if ("cacheTagContent".equals(key)) {
            return String.valueOf(isCacheTagContent());
        } else if ("executeAssociated".equals(key)) {
            return String.valueOf(isExecuteAssociated());
        } else if ("nodeToken".equals(key)) {
            return getNodeToken();
        } else {
            throw new RuntimeException("Unknown configuration property : " + key);
        }
    }
    
    public boolean isReadTagContent() {
        return readTagContent;
    }
    
    public void setReadTagContent(boolean readTagContent) {
        this.readTagContent = readTagContent;
    }
    
    public String getNodeName() {
        return nodeName;
    }
    
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }
    
    public boolean isLearnMode() {
        return learnMode;
    }
    
    public void setLearnMode(boolean learnMode) {
        this.learnMode = learnMode;
    }
    
    public String getNodeGroup() {
        return nodeGroup;
    }
    
    public void setNodeGroup(String nodeGroup) {
        this.nodeGroup = nodeGroup;
    }
    
    public String getServer() {
        return server;
    }
    
    public void setServer(String server) {
        this.server = server;
    }
    
    public String getDefaultApplication() {
        return defaultApplication;
    }
    
    public void setDefaultApplication(String defaultApplication) {
        this.defaultApplication = defaultApplication;
    }
    
    public boolean isCacheTagContent() {
        return cacheTagContent;
    }
    
    public void setCacheTagContent(boolean cacheTagContent) {
        this.cacheTagContent = cacheTagContent;
    }
    
    public boolean isExecuteAssociated() {
        return executeAssociated;
    }
    
    public void setExecuteAssociated(boolean executeAssociated) {
        this.executeAssociated = executeAssociated;
    }
    
    public String getNodeToken() {
        return nodeToken;
    }
    
    public void setNodeToken(String nodeToken) {
        this.nodeToken = nodeToken;
    }
    
}
