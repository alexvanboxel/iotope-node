package org.iotope.node.apps.filter;

import java.net.URI;
import java.util.Map;

import org.iotope.IotopeFilter;
import org.iotope.context.ExecutionContext;
import org.iotope.context.Filter;
import org.iotope.nfc.ndef.NdefMessage;
import org.iotope.nfc.ndef.NdefRecord;
import org.iotope.nfc.target.Block;
import org.iotope.nfc.target.NdefBlock;
import org.iotope.nfc.target.TargetContent;
import org.iotope.nfc.target.TargetContent.ContentType;

@IotopeFilter(domain = "iotope.org", name = "ndef")
public class NdefFilter implements Filter {
    
    @Override
    public boolean match(ExecutionContext context) {
        TargetContent content = context.getTargetContent();
        if (content.size() == 0) {
            return false;
        }
        Block block = content.getBlock(0);
        if (block.getType() != ContentType.NDEF) {
            return false;
        }
        return true;
    }
    
    @Override
    public void configure(Map<String, String> properties) {
    }
    
}
