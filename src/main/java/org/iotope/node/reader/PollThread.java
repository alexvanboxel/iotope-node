package org.iotope.node.reader;


import java.util.List;

import org.cometd.bayeux.client.ClientSessionChannel;
import org.iotope.nfc.reader.ReaderChannel;
import org.iotope.nfc.reader.pn532.PN532InAutoPoll;
import org.iotope.nfc.reader.pn532.PN532InAutoPollResponse;
import org.iotope.nfc.reader.pn532.PN532InAutoPollResponse.TargetData;
import org.iotope.nfc.reader.pn532.PN532RFConfiguration;
import org.iotope.nfc.reader.pn532.PN532RFConfigurationResponse;
import org.iotope.node.model.TagId;
import org.iotope.util.IOUtil;

import com.google.common.eventbus.EventBus;

public class PollThread implements Runnable {
    
    private ReaderChannel channel;
    private Reader reader;
    private EventBus bus;

    private byte[][] oldTags = new byte[2][]; 
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
                    
                    List<TargetData> tags = response.getTargets();
                    for(int r = tags.size() ; r<count;r++) {
                        bus.post(new TagChange(TagChange.Event.REMOVED,reader,r,""));
                    }
                    count = tags.size();
                    
                    int r=0;
                    for(TargetData tag : tags) {
                            bus.post(new TagChange(TagChange.Event.REMOVED,reader,r,""));
                            bus.post(new TagChange(TagChange.Event.ADDED,reader,r,IOUtil.hex(tag.getTargetData())));
                            oldTags[r++] = tag.getTargetData();
                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
}
