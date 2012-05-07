package org.iotope.node.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.LocalSession;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;
import org.iotope.node.Node;
import org.iotope.node.NodeBus;
import org.iotope.node.apps.Correlation;
import org.iotope.node.conf.Configuration;
import org.iotope.node.model.FieldDefinition;
import org.iotope.node.reader.ReaderChange;
import org.iotope.node.reader.Readers;
import org.iotope.node.reader.TagChange;
import org.iotope.pipeline.model.Field;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class CometdNFCTagService extends AbstractService {
    
    public CometdNFCTagService(BayeuxServer bayeuxServer) {
        super(bayeuxServer, "tag");
        // Annoying, we need this hack to get our instances from CDI
        this.readers = Node.instance(Readers.class);
        this.bus = Node.instance(NodeBus.class);
        this.correlation = Node.instance(Correlation.class);
        this.configuration = Node.instance(Configuration.class);
        
        bus.register(this);
        addService("/info", "processInfoRequest");
        addService("/service/rest/**", "processRESTRequest");
        LocalSession session = bayeuxServer.newLocalSession("poll");
        session.handshake();
        tagChannel = session.getChannel("/tag");
        readerChannel = session.getChannel("/reader");
    }
    
    public void processInfoRequest(ServerSession remote, String channelName, Map<String, Object> data, String messageId) {
        String type = (String) data.get("type");
        if ("ReadersInfo".equals(type)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", "ReadersInfo");
            map.put("readers", readers.getReaders());
            remote.deliver(getServerSession(), "/info", map, null);
        }
    }
    
    /**
     * Temporary REST channel
     * 
     * @param remote
     * @param channelName
     * @param data
     * @param messageId
     */
    public void processRESTRequest(ServerSession remote, String channelName, Map<String, Object> data, String messageId) {
        String[] path = channelName.split("/");
        String type = (String) data.get("type");
        if ("setBooleanOption".equals(type)) {
            configuration.set((String)data.get("name"), ((Boolean)data.get("value")).toString());
        } else if ("assignApplication".equals(type)) {
            String tagId = (String) data.get("tagId");
            String readerId = (String) data.get("readerId");
            String appId = (String) data.get("appId");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", "assignApplication");
            map.put("readerId", readerId);
            map.put("tagId", tagId);
            map.put("appId", appId);
            map.put("fields", correlation.getFieldsForApplications(appId));
            remote.deliver(getServerSession(), channelName, map, messageId);
        } else if ("saveApplication".equals(type)) {
            String tagId = (String) data.get("tagId");
//            String readerId = (String) data.get("readerId");
            String appId = (String) data.get("appId");
            List<?> fields = (List<?>) data.get("fields");
            correlation.associate(tagId, appId, fields);
        } else if ("getApplications".equals(type)) {
            remote.deliver(getServerSession(), channelName, correlation.getApplications(), messageId);
        }
    }
    
    @Subscribe
    public void publishReaderChange(ReaderChange e) {
        readerChannel.publish(e);
    }
    
    @Subscribe
    public void publishTagChange(TagChange e) {
//        correlation.tagChange(e);
        tagChannel.publish(e);
    }
    
    private ClientSessionChannel readerChannel;
    private ClientSessionChannel tagChannel;
    
    private Correlation correlation;
    private Configuration configuration;
    private Readers readers;
    private EventBus bus;
}