package org.iotope.node.apps;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.iotope.node.NodeBus;
import org.iotope.node.model.WebLink;
import org.iotope.node.reader.TagChange;

@Singleton
public class Correlation {
    
    public Correlation() {
        super();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("iotope-node");
        em = emf.createEntityManager();
    }
    
    public void tagChange(TagChange e) {
        //        Query query = em.createQuery("SELECT wl FROM WebLink wl");
        //        List<WebLink> resultList = query.getResultList();
        //        for(WebLink l : resultList) {
        //            System.out.println(l.tagId);
        //        }
        
        if (TagChange.Event.ADDED == e.getEvent()) {
            WebLink link = em.find(WebLink.class, e.getRaw());
            if (link != null) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(URI.create(link.url));
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
    
    public void setLearn(boolean b) {
        
    }
    
    private EntityManager em;
    
    @Inject
    private NodeBus bus;
    
    private boolean learn;
}
