package org.iotope.context;

import org.iotope.nfc.tag.NfcTarget;
import org.iotope.nfc.target.NfcTlv;


public interface ExecutionContext {
    
    public Object getField(String variable);
    
    public Object getField(String domain, String application, String variable);
    
    public void setField(String variable, String value);

    public void setField(String domain, String application, String variable, String value);

    public void executeNext(String domain, String application);
    
    public void executeNext(String domain, String application, String variable, String value);
    
    public void executeLast(String domain, String application);
    
    public void executeLast(String domain, String application, String variable, String value);

    public NfcTlv getTargetContent();
    
    public NfcTarget getNfcTarget();
}
