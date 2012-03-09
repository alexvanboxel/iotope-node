package org.iotope.node.web;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.cometd.bayeux.server.BayeuxServer;
import org.iotope.node.NodeBus;
import org.iotope.node.reader.Readers;

@Singleton
public class CometdConfiguration extends GenericServlet {
        
    public CometdConfiguration() {
    }
    
    public void init() throws ServletException {
        BayeuxServer bayeux = (BayeuxServer) getServletContext().getAttribute(BayeuxServer.ATTRIBUTE);
        new CometdNFCTagService(bayeux);
    }
    
    public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
        throw new ServletException();
    }
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private NodeBus bus;
    
    @Inject
    private Readers readers;
    
    
}
