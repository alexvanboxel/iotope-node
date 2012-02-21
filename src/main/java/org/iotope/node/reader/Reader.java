package org.iotope.node.reader;

public class Reader {
    
    public Reader(String terminalId) {
        this.terminalId = terminalId;
    }
    
    public String getTerminalId() {
        return terminalId;
    }
    
    private String terminalId;
}
