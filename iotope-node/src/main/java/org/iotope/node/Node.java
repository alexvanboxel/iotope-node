package org.iotope.node;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.cometd.server.CometdServlet;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.iotope.exception.IotopeException;
import org.iotope.node.conf.Configuration;
import org.iotope.node.reader.ReaderChange;
import org.iotope.node.reader.Readers;
import org.iotope.node.web.CometdConfiguration;
import org.iotope.node.web.UIServlet;
import org.iotope.pipeline.ExecutionPipeline;
import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

@Singleton
public class Node {
    
    public static void main(String[] args) {
        try {
            cdiContainer = new Weld().initialize();
            Node node = cdiContainer.instance().select(Node.class).get();
            node.wire();
        } catch (Exception e) {
            // There is no clean shutdown procedure (the exit action also just shoots down)
            // This should be fixed and perform a robust and safe shutdown, but for now, will
            // at least ensure the application terminates if an exception occurs.
            e.printStackTrace(); // To ensure we display what went wrong before we commit suicide
            System.exit(-1);
        }
    }
    
    @Subscribe
    public void test(ReaderChange e) {
        System.err.println(e);
    }
    
    
    @Subscribe
    public void dead(DeadEvent e) {
        System.err.println(e);
    }
    
    /**
     * Wire the application together
     */
    public void wire() {
        bus.register(this);
        bus.register(tray);
        
        configuration.init();
        
        Thread readerThread = new Thread(readers, "Readers Monitor");
        readerThread.start();
        
        Server server = new Server();
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(4242);
        server.addConnector(connector);
        
        ServletHolder holderCometd = new ServletHolder(new CometdServlet());
        holderCometd.setInitOrder(1);
        holderCometd.setInitParameter("jsonContext", "org.cometd.server.JacksonJSONContextServer");
        ServletHolder holderCometdConfig = new ServletHolder(servletCometDConfig);
        holderCometdConfig.setInitOrder(2);
        ServletHolder holderUI = new ServletHolder(servletUI);
        
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(holderCometd, "/cometd/*");
        context.addServlet(holderCometdConfig, "/config");
        context.addServlet(holderUI, "/ui/*");
        
        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[] { context, new DefaultHandler() });
        server.setHandler(handlers);
        
        try {
            server.start();
            server.join();
        } catch (Exception e) {
            throw new IotopeException("Failed to start node instance", e);
        }
    }
    
    
    public static void setContainer(WeldContainer cdiContainer) {
        Node.cdiContainer = cdiContainer;
    }

    @Inject
    private NodeBus bus;
    
    @Inject
    private NodeTray tray;
    
    @Inject
    private UIServlet servletUI;
    
    @Inject
    private Readers readers;
    
    @SuppressWarnings("unused") // Just for Weld to know it should instantiate the Execution Pipeline
    @Inject
    private ExecutionPipeline pipe;
    
    @Inject
    private CometdConfiguration servletCometDConfig;
    
    @Inject
    private Configuration configuration;
    
    private static WeldContainer cdiContainer;
    
    public static <T> T instance(Class<T> c) {
        return cdiContainer.instance().select(c).get();
    }
}
