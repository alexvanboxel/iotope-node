package org.iotope.node.web;

import java.util.Map;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;

public class NFCTagService extends AbstractService {
    
    public NFCTagService(BayeuxServer bayeuxServer) {
        super(bayeuxServer, "tag");
        addService("/tag", "processTag");
    }
    
    public void processTag(ServerSession remote, String channelName, Map<String, Object> data, String messageId) {
        for (Map.Entry<String, Object> o : data.entrySet()) {
            System.err.println(o.getKey() + " = " + o.getValue());
        }
        remote.deliver(getServerSession(), "/tag", data, null);
    }
}