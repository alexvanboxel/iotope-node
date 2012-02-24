package org.iotope.node.web;

import java.util.HashMap;
import java.util.Map;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.LocalSession;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;
import org.iotope.node.reader.ReaderChange;
import org.iotope.node.reader.Readers;
import org.iotope.node.reader.TagChange;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class NFCTagService extends AbstractService {
    
    public NFCTagService(EventBus bus, Readers readers, BayeuxServer bayeuxServer) {
        super(bayeuxServer, "tag");
        this.readers = readers;
        bus.register(this);
        addService("/info", "processInfoRequest");
        LocalSession session = bayeuxServer.newLocalSession("poll");
        session.handshake();
        tagChannel = session.getChannel("/tag");
        readerChannel = session.getChannel("/reader");
    }
    
    public void processInfoRequest(ServerSession remote, String channelName, Map<String, Object> data, String messageId) {
        String type = (String) data.get("type");
        if("ReadersInfo".equals(type)) {
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("type", "ReadersInfo");
            map.put("readers", readers.getReaders());
            remote.deliver(getServerSession(), "/info", map, null);
        }
//        for (Map.Entry<String, Object> o : data.entrySet()) {
//            System.err.println(o.getKey() + " = " + o.getValue());
//        }
        //                remote.deliver(getServerSession(), "/tag", data, null);
    }
    
    @Subscribe
    public void publishReaderChange(ReaderChange e) {
        readerChannel.publish(e);
    }
    
    @Subscribe
    public void publishTagChange(TagChange e) {
        tagChannel.publish(e);
    }
    
    private ClientSessionChannel readerChannel;
    private ClientSessionChannel tagChannel;
    private Readers readers;
}