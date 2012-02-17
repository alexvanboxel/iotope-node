package org.iotope.node;


import org.cometd.bayeux.client.ClientSession;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.LocalSession;
import org.cometd.server.ServerMessageImpl;
import org.iotope.nfc.reader.ReaderChannel;
import org.iotope.nfc.reader.pn532.PN532InAutoPoll;
import org.iotope.nfc.reader.pn532.PN532InAutoPollResponse;
import org.iotope.nfc.reader.pn532.PN532RFConfiguration;
import org.iotope.nfc.reader.pn532.PN532RFConfigurationResponse;

public class PollThread implements Runnable {
    
    ReaderChannel channel;
    BayeuxServer cometd;
    
    private String prev = null;
    
    public PollThread(BayeuxServer cometd, ReaderChannel channel) {
        this.channel = channel;
        this.cometd = cometd;
        LocalSession session = cometd.newLocalSession("poll");
        session.handshake();
        cometdChannel=session.getChannel("/tag");
    }
    
    ClientSessionChannel cometdChannel;
    
    @Override
    public void run() {
        try {
            PN532RFConfigurationResponse initResponse = channel.transmit(new PN532RFConfiguration());
            while(true) {
                PN532InAutoPollResponse response = channel.transmit(new PN532InAutoPoll());
                if (!response.toString().equals(prev)) {
                    prev = response.toString();
                    ServerMessageImpl m = new ServerMessageImpl();
                    m.setData(prev);
                    if (cometdChannel != null) {
                        cometdChannel.publish(m);
                    }
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
