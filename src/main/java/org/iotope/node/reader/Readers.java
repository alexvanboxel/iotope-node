package org.iotope.node.reader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.TerminalFactory;

import org.iotope.nfc.reader.ReaderChannel;
import org.iotope.nfc.reader.ReaderConnection;
import org.iotope.node.NodeBus;
import org.iotope.node.reader.ReaderChange.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  
 */
@Singleton
@SuppressWarnings("restriction")
public class Readers implements Runnable {
    private static Logger Log = LoggerFactory.getLogger(Readers.class);
    
    public Readers() throws Exception {
        TerminalFactory factory = TerminalFactory.getDefault();
        terminals = factory.terminals();
        knownTerminals = Collections.synchronizedMap(new HashMap<CardTerminal, Reader>());
    }
    
    private Reader addTerminal(CardTerminal terminal) throws CardException {
        Reader reader = new Reader(String.valueOf(terminal.hashCode()), //
                terminal.getName(), 2);
        knownTerminals.put(terminal, reader);
        poll(terminal, reader);
        return reader;
    }
    
    private Reader removeTerminal(CardTerminal terminal) {
        return knownTerminals.remove(terminal);
    }
    
    public List<Reader> getReaders() {
        List<Reader> readers = new ArrayList<Reader>();
        for (Reader r : knownTerminals.values()) {
            readers.add(r);
        }
        return readers;
    }
    
    private void removeAll(List<CardTerminal> toRemove) {
        for (CardTerminal t : toRemove) {
            Reader removed = removeTerminal(t);
            bus.post(new ReaderChange(Event.REMOVED, removed));
        }
    }
    
    @Override
    public void run() {
        List<CardTerminal> tlist;
        
        while (true) {
            try {
                Log.trace("Start sync to search for new Readers");
                tlist = terminals.list();
                for (CardTerminal t : tlist) {
                    Log.trace("Card API returns the current reader: "+t.getName());
                    if (!knownTerminals.containsKey(t)) {
                        bus.post(new ReaderChange(Event.ADDED, addTerminal(t)));
                        Log.info("Found new reader: "+t.getName());
                    }
                }
                List<CardTerminal> toRemove = new ArrayList<CardTerminal>();
                for (CardTerminal t : knownTerminals.keySet()) {
                    if (null == terminals.getTerminal(t.getName())) {
                        toRemove.add(t);
                    }
                }
                removeAll(toRemove);
            } catch (CardException ce) {
                List<CardTerminal> toRemove = new ArrayList<CardTerminal>();
                for (CardTerminal t : knownTerminals.keySet()) {
                    toRemove.add(t);
                }
                removeAll(toRemove);
            }
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                running.set(false);
            }
        }
    }
    
    public void pollAll() throws CardException {
        for (Map.Entry<CardTerminal, Reader> reader : knownTerminals.entrySet()) {
            poll(reader.getKey(), reader.getValue());
        }
    }
    
    private void poll(CardTerminal card, Reader reader) throws CardException {
        ReaderConnection connection = new ReaderConnection();
        ReaderChannel channel = new ReaderChannel(connection, card.connect("*").getBasicChannel());
        new Thread(new PollThread(bus, channel, reader), "Poll Thread " + reader.getTerminalId()).start();
    }
    
    
    private AtomicBoolean running = new AtomicBoolean(true);
    private CardTerminals terminals;
    private Map<CardTerminal, Reader> knownTerminals;
    
    @Inject
    private NodeBus bus;
}
