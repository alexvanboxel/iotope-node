package org.iotope.node;

import org.cometd.server.CometdServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.iotope.node.reader.ReaderChange;
import org.iotope.node.reader.Readers;
import org.iotope.node.web.CometdConfiguration;
import org.iotope.node.web.UIServlet;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

public class NodeTest {
    
    public static void main(String[] args) throws Exception {
        new NodeTest();
    }
    
    @Subscribe public void test(ReaderChange e) {
        System.err.println(e);
      }

    
    @Subscribe public void dead(DeadEvent e) {
        System.err.println(e);
      }

    
    /**
     * @param args
     */
    public NodeTest() throws Exception {
        
        EventBus bus = new EventBus();
        bus.register(this);
        
        Readers readers = new Readers(bus);
        Thread readerThread = new Thread(readers, "Readers Monitor");
        readerThread.start();
        
        Server server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(4242);
        server.addConnector(connector);
        
        ServletHolder holderCometd = new ServletHolder(new CometdServlet());
        holderCometd.setInitOrder(1);
        ServletHolder holderCometdConfig = new ServletHolder(new CometdConfiguration(bus));
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
        
        readers.pollAll();
        server.join();
    }
    
}
