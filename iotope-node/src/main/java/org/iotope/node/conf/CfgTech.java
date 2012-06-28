package org.iotope.node.conf;

import java.util.HashMap;
import java.util.Map;

public class CfgTech {
    
    public enum Protocol {
        MIFARE_CLASSIC("mifare-classic"), MIFARE_ULTRALIGHT("mifare-ultralight");
        
        private Protocol(String name) {
            this.name = name;
        }
        
        public final String name;
        
        @Override
        public String toString() {
            return name;
        }
    }
    
    static Map<String, Protocol> protocolLookup = new HashMap<String, Protocol>();
    static {
        for (Protocol val : Protocol.values()) {
            protocolLookup.put(val.name, val);
        }
    }
    
    int type;
    Protocol protocol;
    boolean detect;
    boolean ndef;
    boolean cache;
    boolean meta;
    
    CfgTech(String type, String protocol, String detect, String ndef, String cache, String meta) {
        this.type = Integer.valueOf(type);
        this.protocol = protocolLookup.get(protocol);
        this.detect = Boolean.valueOf(detect);
        this.ndef = Boolean.valueOf(ndef);
        this.cache = Boolean.valueOf(cache);
        this.meta = Boolean.valueOf(meta);
    }

    public static Map<String, Protocol> getProtocolLookup() {
        return protocolLookup;
    }

    public int getType() {
        return type;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public boolean isDetect() {
        return detect;
    }

    public boolean isNdef() {
        return ndef;
    }

    public boolean isCache() {
        return cache;
    }

    public boolean isMeta() {
        return meta;
    }
    
}
