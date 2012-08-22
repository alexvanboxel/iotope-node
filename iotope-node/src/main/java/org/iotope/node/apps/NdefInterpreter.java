package org.iotope.node.apps;

import java.util.List;
import java.util.Map;

import org.iotope.IotopeApplication;
import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.iotope.context.MetaData;
import org.iotope.nfc.ndef.NdefParsedMessage;
import org.iotope.nfc.ndef.NdefParsedRecord;
import org.iotope.nfc.target.Block;
import org.iotope.nfc.target.NdefBlock;
import org.iotope.nfc.target.TargetContent;
import org.iotope.nfc.target.TargetContent.ContentType;

@IotopeApplication(domain = "iotope.org", name = "ndef")
public class NdefInterpreter implements Application {
    
    @Override
    public void execute(ExecutionContext context) {
        TargetContent content = context.getTargetContent();
        if (content != null) {
            List<Block> blocks = content.getBlocks();
            for (Block block : blocks) {
                if (block.getType() == ContentType.NDEF) {
                    NdefBlock ndef = (NdefBlock) block;
                    NdefParsedMessage message = ndef.getNdef();
                    for (NdefParsedRecord record : message.getRecords()) {
                        String rtd = record.getRTD();
                        if ("urn:nfc:wkt:U".equals(rtd)) {
                            context.executeNext("iotope.org", "weblink", "url", record.getContent());
                        } else if ("urn:nfc:wkt:T".equals(rtd)) {
                            context.executeNext("iotope.org", "weblink", "caption", null);
                            context.executeNext("iotope.org", "weblink", "message", record.getContent());
                            context.executeNext("iotope.org", "weblink", "message", "INFO");
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public MetaData getMetaData() {
        return null;
    }

    @Override
    public void configure(Map<String, String> properties) {
        // Nothing to configure
    }
}
