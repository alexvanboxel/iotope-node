package org.iotope.node.reader;


import java.util.List;

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

    private String[] oldTags = new String[2]; 
    private int count = 0;
    
    private String prev = null;
    
    public PollThread(EventBus bus, ReaderChannel channel, Reader reader) {
        this.bus = bus;
        this.reader = reader;
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
                    
                    List<String> tags = response.getRawTagdata();
                    for(int r = tags.size() ; r<count;r++) {
                        bus.post(new TagChange(TagChange.Event.REMOVED,reader,r,""));
                    }
                    count = tags.size();
                    
                    int r=0;
                    for(String tag : tags) {
                            bus.post(new TagChange(TagChange.Event.REMOVED,reader,r,""));
                            bus.post(new TagChange(TagChange.Event.ADDED,reader,r,tag));
                            oldTags[r++] = tag;
                    }
                    
//                    bus.post(new TagChange(TagChange.Event.ADDED,reader,0,prev));
                }
                Thread.sleep(250);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
