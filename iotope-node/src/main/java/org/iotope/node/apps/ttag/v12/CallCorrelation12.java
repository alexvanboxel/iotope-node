package org.iotope.node.apps.ttag.v12;

import org.apache.http.HttpResponse;
import org.iotope.IotopeApplication;
import org.iotope.context.Application;
import org.iotope.context.ExecutionContext;
import org.iotope.context.MetaData;
import org.iotope.nfc.target.NfcTlv.ContentType;
import org.iotope.nfc.target.TlvBlock;
import org.iotope.nfc.target.TlvByteBlock;
import org.iotope.node.apps.ttag.AbstractCallCorrelation;
import org.iotope.node.apps.ttag.AbstractTagEvent;
import org.iotope.node.apps.ttag.TagType;
import org.iotope.util.IOUtil;

import java.net.URI;
import java.util.Map;


@IotopeApplication(domain = "iotope.org", name = "ttag.c12")
public class CallCorrelation12 extends AbstractCallCorrelation implements Application {

    String user;
    String password;

    @Override
    public void execute(ExecutionContext context) {
        try {
            AbstractTagEvent<?> tagEvent = new Tag12Event();
            
            String id = IOUtil.hexbin(context.getNfcTarget().getNfcId());
            tagEvent.setTag(TagType.ULTRALIGHT, "0x" + id.toUpperCase());
            
            tagEvent.setLocation("iotope");
            URI uri = URI.create("https://acs.touchatag.com/soap/correlation-1.2");
            
            TlvBlock block = context.getTargetContent().getBlock(3);
            if (block.getType() == ContentType.LEGACY_TAGDATA) {
                String tag64 = javax.xml.bind.DatatypeConverter.printBase64Binary(((TlvByteBlock) block).getBlock());
                tagEvent.setTagData(tag64);

                HttpResponse httpResponse = fireTag(uri, user, password, tagEvent);
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

    @Override
    public void configure(Map<String, String> properties) {
        user = properties.get("user");
        password = properties.get("password");
    }
}
