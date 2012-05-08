package org.iotope.node.apps.filter;

import java.net.URI;

import org.iotope.IotopeFilter;
import org.iotope.context.ExecutionContext;
import org.iotope.context.Filter;
import org.iotope.nfc.ndef.NdefMessage;
import org.iotope.nfc.ndef.NdefRecord;
import org.iotope.nfc.target.Block;
import org.iotope.nfc.target.NdefBlock;
import org.iotope.nfc.target.TargetContent;
import org.iotope.nfc.target.TargetContent.ContentType;

@IotopeFilter(domain = "iotope.org", name = "touchatag")
public class TouchatagFilter implements Filter {
    
    @Override
    public boolean match(ExecutionContext context) {
        TargetContent content = context.getTargetContent();
        if (content.size() != 3) {
            return false;
        }
        Block block = content.getBlock(1);
        if (block.getType() != ContentType.LEGACY_HASH) {
            return false;
        }
        block = content.getBlock(0);
        if (block.getType() != ContentType.NDEF) {
            return false;
        }
        NdefBlock ndef = (NdefBlock) block;
        NdefMessage message = ndef.getNdef();
        if (message.size() != 1) {
            return false;
        }
        NdefRecord record = message.getRecord(0);
        if (!"urn:nfc:wkt:U".equals(record.getRTD())) {
            return false;
        }
        URI uri = null;
        try {
            uri = URI.create(record.getContent());
            if (!"www.ttag.be".equals(uri.getHost())) {
                return false;
            }
        } catch (RuntimeException e) {
            return false;
        }
        return true;
    }
    
}
