package org.iotope.node;

import java.util.List;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.server.CometdServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.iotope.nfc.reader.ReaderChannel;
import org.iotope.nfc.reader.ReaderConnection;
import org.iotope.node.web.CometdConfiguration;
import org.iotope.node.web.UIServlet;

public class NodeTest {
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        /* the card terminals */
        List<CardTerminal> terminals;
        TerminalFactory factory = TerminalFactory.getDefault();
        terminals = factory.terminals().list();
        if (terminals.size() < 1)
            throw new Exception("Need 1 terminals");
        
        CardTerminal t = terminals.get(0);
        
        ReaderConnection connection = new ReaderConnection();
        ReaderChannel channel = new ReaderChannel(connection, t.connect("T=0").getBasicChannel());
        
        Server server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(4242);
        server.addConnector(connector);
        
        ServletHolder holderCometd = new ServletHolder(new CometdServlet());
        holderCometd.setInitOrder(1);
        ServletHolder holderCometdConfig = new ServletHolder(new CometdConfiguration());
        holderCometdConfig.setInitOrder(2);
        ServletHolder holderUI = new ServletHolder(new UIServlet());
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(holderCometd, "/cometd/*");
        context.addServlet(holderCometdConfig, "/config");
        context.addServlet(holderUI, "/ui/*");
        
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
        server.setHandler(handlers);
        
        server.start();
        BayeuxServer cometd = ((CometdConfiguration) holderCometdConfig.getServlet()).getCometD();
        new Thread(new PollThread(cometd, channel)).start();
        server.join();
    }
    
}
