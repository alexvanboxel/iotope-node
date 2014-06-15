package org.iotope.node.apps.filter;

import org.iotope.IotopeFilter;
import org.iotope.context.ExecutionContext;
import org.iotope.context.Filter;
import org.iotope.nfc.target.NfcTlv;
import org.iotope.nfc.target.NfcTlv.ContentType;
import org.iotope.nfc.target.TlvBlock;

import java.util.Map;

@IotopeFilter(domain = "iotope.org", name = "ndef")
public class NdefFilter implements Filter {
    
    @Override
    public boolean match(ExecutionContext context) {
        NfcTlv content = context.getTargetContent();
        if (content == null || content.size() == 0) {
            return false;
        }
        TlvBlock block = content.getBlock(0);
        if (block.getType() != ContentType.NDEF) {
            return false;
        }
        return true;
    }
    
    @Override
    public void configure(Map<String, String> properties) {
    }
    
}
