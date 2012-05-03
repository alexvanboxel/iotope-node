package org.iotope.node.web;

import java.util.HashMap;
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
import org.iotope.node.reader.ReaderChange;
import org.iotope.node.reader.Readers;
import org.iotope.node.reader.TagChange;

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
        if ("setLearnMode".equals(type)) {
            if (Boolean.TRUE.equals(data.get("learn"))) {
                configuration.setLearnMode(true);
            } else {
                configuration.setLearnMode(false);
            }
        } else if ("assignApplication".equals(type)) {
            String tagId = (String) data.get("tagId");
            String readerId = (String) data.get("readerId");
            String appId = (String) data.get("appId");
            
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", "assignApplication");
            map.put("readerId", readerId);
            map.put("tagId", tagId);
            map.put("appId", appId);
            Map<String, Object>[] fields = null;
            if ("1".equals(appId)) {
                fields = new HashMap[1];
                fields[0] = new HashMap<String, Object>();
                fields[0].put("name", "url");
                fields[0].put("displayName", "URL");
                fields[0].put("type", "xs:string");
                fields[0].put("description", "The URL to jump to.");
            } else if ("2".equals(appId)) {
                fields = new HashMap[3];
                fields[0] = new HashMap<String, Object>();
                fields[0].put("name", "url");
                fields[0].put("displayName", "URL");
                fields[0].put("type", "xs:string");
                fields[0].put("description", "The URL to call.");
                fields[1] = new HashMap<String, Object>();
                fields[1].put("name", "method");
                fields[1].put("displayName", "Method");
                fields[1].put("type", "xs:string");
                fields[1].put("description", "How to call (GET, POST, PUT, ...)");
                fields[2] = new HashMap<String, Object>();
                fields[2].put("name", "accept");
                fields[2].put("displayName", "Accept");
                fields[2].put("type", "xs:string");
                fields[2].put("description", "The Accept header.");
            } else {
            }
            map.put("fields", fields);
            remote.deliver(getServerSession(), channelName, map, messageId);
        } else if ("saveApplication".equals(type)) {
            String tagId = (String) data.get("tagId");
            String readerId = (String) data.get("readerId");
            String appId = (String) data.get("appId");
            Object[] fields = (Object[]) data.get("fields");
            correlation.associate(tagId, appId, fields);
            //correlation.associate(tagId, appId, fields);
            //            Map<String,String> fields = Map<String,String> 
            //          if (Boolean.TRUE.equals(data.get("learn"))) {
            //              correlation.setLearn(true);
            //          } else {
            //              correlation.setLearn(false);
            //          }
        }
    }
    
    @Subscribe
    public void publishReaderChange(ReaderChange e) {
        readerChannel.publish(e);
    }
    
    @Subscribe
    public void publishTagChange(TagChange e) {
        correlation.tagChange(e);
        tagChannel.publish(e);
    }
    
    private ClientSessionChannel readerChannel;
    private ClientSessionChannel tagChannel;
    
    private Correlation correlation;
    private Configuration configuration;
    private Readers readers;
    private EventBus bus;
}