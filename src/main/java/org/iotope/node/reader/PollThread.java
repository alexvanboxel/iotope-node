package org.iotope.node.reader;


import org.cometd.bayeux.client.ClientSessionChannel;
import org.iotope.nfc.reader.ReaderChannel;
import org.iotope.nfc.reader.pn532.PN532InAutoPoll;
import org.iotope.nfc.reader.pn532.PN532InAutoPollResponse;
import org.iotope.nfc.reader.pn532.PN532RFConfiguration;
import org.iotope.nfc.reader.pn532.PN532RFConfigurationResponse;
import org.iotope.nfc.tag.NfcTarget;
import org.iotope.nfc.tech.NfcType2;
import org.iotope.node.Node;
import org.iotope.node.apps.Correlation;
import org.iotope.node.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

public class PollThread implements Runnable {
    private static Logger Log = LoggerFactory.getLogger(PollThread.class);
    
    private ReaderChannel channel;
    private Reader reader;
    private EventBus bus;
    
    Correlation correlation = Node.instance(Correlation.class);
    Configuration configuration = Node.instance(Configuration.class);
    
    ClientSessionChannel cometdChannel;
    NfcTarget[] previousTargets = new NfcTarget[2];
    
    public PollThread(EventBus bus, ReaderChannel channel, Reader reader) {
        this.bus = bus;
        this.reader = reader;
        this.channel = channel;
    }
    
    @Override
    public void run() {
        try {
            PN532RFConfigurationResponse initResponse = channel.transmit(new PN532RFConfiguration());
            while (true) {
                PN532InAutoPollResponse response = channel.transmit(new PN532InAutoPoll());
                NfcTarget[] currentTargets = response.getTags();
                // After detection, remove all the old targets from our list
                // and broadcast the removal on the bus
                removeTargetsFromSlots(currentTargets);
                // Handle new targets that are detected as well as broadcast the new 
                // targets
                handleNewTargets(currentTargets);
                // Wait for next auto poll request
                Thread.sleep(500);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private void handleNewTargets(NfcTarget[] currentTargets) {
        // 
        for (int ix = 0; ix < currentTargets.length; ix++) {
            if (previousTargets[ix] == null) {
                NfcTarget nfcTarget = currentTargets[ix];
                if (nfcTarget != null) {
                    TagChange tagChange = new TagChange(TagChange.Event.ADDED, reader, ix, nfcTarget);
                    if (nfcTarget.isDEP()) {
                        // DEP
                        Log.trace("Handling new DEP target: " + nfcTarget.toString());
                    } else {
                        // TAG
                        if(configuration.isReadTagContent()) {
                            Log.debug("Handling new TAG target: " + nfcTarget.toString());
                            try {
                                switch (nfcTarget.getType()) {
                                case MIFARE_1K:
                                    //readClassicAll(nfcTag);
                                    break;
                                case MIFARE_ULTRALIGHT:
                                    NfcType2 ultraLight = new NfcType2(channel);
                                    tagChange.addTagContent(ultraLight.readNDEF(nfcTarget));
                                    //writeTest(nfcTag);
                                    break;
                                default:
                                    Log.error("Can't handle unsupported target type: " + nfcTarget.getType());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        else {
                            Log.debug("New TAG target: " + nfcTarget.toString() + " but content is not read due to configuration.");
                        }
                    }
                    if (configuration.isExecuteAssociated()) {
                        tagChange = correlation.getAssociateDataForTag(tagChange);
                    }
                    bus.post(tagChange);
                    previousTargets[ix] = nfcTarget;
                } else {
                    Log.error("Trying to handle nfcTarget but it has NULL");
                }
            }
        }
    }
    
    private void removeTargetsFromSlots(NfcTarget[] currentTargets) {
        // remove previously different tags slots
        for (int ix = 0; ix < previousTargets.length; ix++) {
            NfcTarget nfcTarget = null;
            if (ix < currentTargets.length) {
                nfcTarget = currentTargets[ix];
            }
            if ((previousTargets[ix] != null) && !(previousTargets[ix].equals(nfcTarget))) {
                bus.post(new TagChange(TagChange.Event.REMOVED, reader, ix, null));
                previousTargets[ix] = null;
            }
        }
    }
    
}
