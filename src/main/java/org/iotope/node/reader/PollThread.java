package org.iotope.node.reader;


import org.cometd.bayeux.client.ClientSessionChannel;
import org.iotope.nfc.reader.ReaderChannel;
import org.iotope.nfc.reader.pn532.PN532InAutoPoll;
import org.iotope.nfc.reader.pn532.PN532InAutoPollResponse;
import org.iotope.nfc.reader.pn532.PN532RFConfiguration;
import org.iotope.nfc.reader.pn532.PN532RFConfigurationResponse;

import com.google.common.eventbus.EventBus;

public class PollThread implements Runnable {
    
    private ReaderChannel channel;
    private Reader reader;
    private EventBus bus;
    
    
    private String prev = null;
    
    public PollThread(EventBus bus, ReaderChannel channel, Reader reader) {
        this.bus = bus;
        this.channel = channel;
    }
    
    ClientSessionChannel cometdChannel;
    
    @Override
    public void run() {
        try {
            PN532RFConfigurationResponse initResponse = channel.transmit(new PN532RFConfiguration());
            while (true) {
                PN532InAutoPollResponse response = channel.transmit(new PN532InAutoPoll());
                if (!response.toString().equals(prev)) {
                    prev = response.toString();
                    bus.post(new TagChange(prev));
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
