package org.iotope.node.apps.ttag.v12;

import java.net.URI;

import org.apache.http.HttpResponse;
import org.iotope.IotopeApplication;
import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.iotope.context.MetaData;
import org.iotope.nfc.target.Block;
import org.iotope.nfc.target.ByteBlock;
import org.iotope.nfc.target.TargetContent.ContentType;
import org.iotope.node.apps.ttag.AbstractCallCorrelation;
import org.iotope.node.apps.ttag.AbstractTagEvent;
import org.iotope.node.apps.ttag.TagType;
import org.iotope.util.IOUtil;


@IotopeApplication(domain = "iotope.org", name = "ttag.c12")
public class CallCorrelation12 extends AbstractCallCorrelation implements Application {
    
    @Override
    public void execute(ExecutionContext context) {
        try {
            AbstractTagEvent<?> tagEvent = new Tag12Event();
            
            String id = IOUtil.hexbin(context.getNfcTarget().getNfcId());
            tagEvent.setTag(TagType.ULTRALIGHT, "0x" + id.toUpperCase());
            
            tagEvent.setLocation("iotope");
            URI uri = URI.create("https://acs.touchatag.com/soap/correlation-1.2");
            
            Block block = context.getTargetContent().getBlock(3);
            if (block.getType() == ContentType.LEGACY_TAGDATA) {
                String tag64 = javax.xml.bind.DatatypeConverter.printBase64Binary(((ByteBlock) block).getBlock());
                tagEvent.setTagData(tag64);
                HttpResponse httpResponse = fireTag(uri, "user", "password", tagEvent);
                Tag12Response response = new Tag12Response(httpResponse);
                System.out.println(response.toXML());
                detectApplications(context, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public MetaData getMetaData() {
        return null;
    }
}
