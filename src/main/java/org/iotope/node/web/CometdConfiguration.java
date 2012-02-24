package org.iotope.node.web;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.cometd.bayeux.server.BayeuxServer;
import org.iotope.node.reader.Readers;

import com.google.common.eventbus.EventBus;

public class CometdConfiguration extends GenericServlet {
        
    public CometdConfiguration(EventBus bus,Readers readers) {
        this.bus = bus;
        this.readers = readers;
    }
    
    public void init() throws ServletException {
        BayeuxServer bayeux = (BayeuxServer) getServletContext().getAttribute(BayeuxServer.ATTRIBUTE);
        new NFCTagService(bus,readers,bayeux);
    }
    
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        throw new ServletException();
    }
    
    private static final long serialVersionUID = 1L;
    private EventBus bus;
    private Readers readers;
}
