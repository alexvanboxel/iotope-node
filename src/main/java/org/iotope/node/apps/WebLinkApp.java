package org.iotope.node.apps;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.iotope.node.model.WebLink;

public class WebLinkApp {

    public static void main(String... arg) throws Exception {
        WebLinkApp app = new WebLinkApp();
//        app.singleton();
        app.browse();
    }
    
    public WebLinkApp singleton() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("iotope-node");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        WebLink weblink = new WebLink();
//        weblink.tagId= new byte[] { 0x00, 0x01 } ;
//        weblink.tagType = 0x10;
        weblink.url = "http://alex.vanboxel.be";
        // Create the instance of Student Entity class
//        Student student = new Student(147675, "Afshan", 29);
        // JPA API to store the Student instance on the database.
        em.persist(weblink);
        tx.commit();
        em.close();
                 return null; 
    }

    public void browse() throws IOException {
        Desktop desktop = Desktop.getDesktop();
        
        desktop.browse(URI.create("http://alex.vanboxel.be"));
    }
    

}
