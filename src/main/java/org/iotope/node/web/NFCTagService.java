package org.iotope.node.web;

import java.util.Map;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.LocalSession;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;
import org.cometd.server.ServerMessageImpl;
import org.iotope.node.reader.ReaderChange;
import org.iotope.node.reader.TagChange;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class NFCTagService extends AbstractService {
    
    public NFCTagService(EventBus bus, BayeuxServer bayeuxServer) {
        super(bayeuxServer, "tag");
        this.bus = bus;
        bus.register(this);
        addService("/tag", "processTag");
        
        LocalSession session = bayeuxServer.newLocalSession("poll");
        session.handshake();
        tagChannel = session.getChannel("/tag");
        readerChannel = session.getChannel("/reader");
    }
    
    public void processTag(ServerSession remote, String channelName, Map<String, Object> data, String messageId) {
        for (Map.Entry<String, Object> o : data.entrySet()) {
            System.err.println(o.getKey() + " = " + o.getValue());
        }
        //                remote.deliver(getServerSession(), "/tag", data, null);
    }
    
    @Subscribe
    public void publishReaderChange(ReaderChange e) {
        System.err.println(e);
    }
    
    @Subscribe
    public void publishTagChange(TagChange e) {
        ServerMessageImpl m = new ServerMessageImpl();
        m.setData(e.getRaw());
        tagChannel.publish(m);
    }
    
    private EventBus bus;
    private ClientSessionChannel readerChannel;
    private ClientSessionChannel tagChannel;
}