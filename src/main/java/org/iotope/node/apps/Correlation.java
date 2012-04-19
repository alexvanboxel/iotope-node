package org.iotope.node.apps;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.iotope.node.NodeBus;
import org.iotope.node.model.Application;
import org.iotope.node.model.Association;
import org.iotope.node.model.FieldValue;
import org.iotope.node.model.Tag;
import org.iotope.node.model.TagId;
import org.iotope.node.model.WebLink;
import org.iotope.node.reader.TagChange;

@Singleton
public class Correlation {
    
    EntityManagerFactory emf;
    
    public Correlation() {
        super();
        emf = Persistence.createEntityManagerFactory("iotope-node");
        EntityManager em = emf.createEntityManager();
        em.setFlushMode(FlushModeType.COMMIT);
        em.getTransaction().begin();
        
        Application app = em.find(Application.class, Integer.valueOf("1"));
        if (app == null) {
            app = new Application();
            app.setAppId(Integer.valueOf("1"));
            em.persist(app);
        }
        app = em.find(Application.class, Integer.valueOf("2"));
        if (app == null) {
            app = new Application();
            app.setAppId(Integer.valueOf("2"));
            em.persist(app);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public TagChange tagChange(TagChange e) {
        if (!learn) {
            if (TagChange.Event.ADDED == e.getEvent()) {
                
                ///////
                EntityManager em = emf.createEntityManager();
                
                TagId tid = new TagId(e.getRaw());
                Tag tag = em.find(Tag.class, tid);
                if (tag == null) {
                    tag = new Tag(tid);
                    em.getTransaction().begin();
                    em.persist(tag);
                    em.getTransaction().commit();
                }
                
                //TypedQuery<Association> query = em.createQuery("select ass from Association ass where ass.tag = :tag", Association.class);
                TypedQuery<Association> query = em.createNamedQuery("findAssociationByTag", Association.class);
                query.setParameter("tag", tag);
                try {
                    Association ass = query.getSingleResult();
                    Tag t = ass.getTag();
                    if(ass.getApplication().getAppId() == 1) {
                        new WebLinkApp(em).execute(ass.getFields());
                    }
                    
                } catch (NoResultException nre) {
                }
                em.close();
                
                
                /////         //                Association ass = 
                
                
                //                WebLink link = em.find(WebLink.class, e.getRaw());
                //                if (link != null) {
                //                    Desktop desktop = Desktop.getDesktop();
                //                    try {
                //                        desktop.browse(URI.create(link.getUrl()));
                //                    } catch (IOException e1) {
                //                        e1.printStackTrace();
                //                    }
                //                }
            }
        }
        
        return e;
    }
    
    public void setLearn(boolean b) {
        learn = b;
    }
    
    
    /**
     * Prototype: Need to be moved
     * 
     * @param tagId
     * @param fields
     */
    public void associate(String tagId, String appId, Object[] ofs) {
        
        EntityManager em = emf.createEntityManager();
        try {
            em.setFlushMode(FlushModeType.COMMIT);
            em.getTransaction().begin();
            
            TagId tid = new TagId(tagId);
            Tag tag = em.find(Tag.class, tid);
            if (tag == null) {
                tag = new Tag(tid);
                em.persist(tag);
            }
            
            Application app = em.find(Application.class, Integer.valueOf(appId));
            if (app == null) {
                throw new RuntimeException();
            }
            
            Association ass = em.find(Association.class, Long.valueOf(appId));
            if (ass == null) {
                ass = new Association();
                ass.setApplication(app);
                ass.setTag(tag);
                em.persist(ass);
            }
            // UPDATE AND ADD
            Set<FieldValue> handle = new HashSet<FieldValue>();
            Collection<FieldValue> fields = ass.getFields();
            for (Object of : ofs) {
                HashMap<String, String> f = (HashMap<String, String>) of;
                String name = f.get("name");
                String value = f.get("value");
                boolean handled = false;
                for (FieldValue fieldValue : fields) {
                    if (fieldValue.getField().equals(name)) {
                        handled = true;
                        if (!fieldValue.getValue().equals(value)) {
                            fieldValue.setValue(value);
                        }
                    }
                }
                if (!handled) {
                    handle.add(new FieldValue(ass, name, value));
                }
            }
            for (FieldValue add : handle) {
                fields.add(add);
            }
            // DELETE
            handle = new HashSet<FieldValue>();
            for (FieldValue field : ass.getFields()) {
                boolean exist = false;
                for (Object of : ofs) {
                    HashMap<String, String> f = (HashMap<String, String>) of;
                    String name = f.get("name");
                    if (field.getField().equals(name)) {
                        exist = true;
                    }
                }
                if (!exist) {
                    handle.add(field);
                }
            }
            for (FieldValue field : handle) {
                ass.getFields().remove(field);
            }
            em.persist(ass);
            em.getTransaction().commit();
            em.close();
        } catch (Throwable e) {
            em.getTransaction().rollback();
        }
    }
    
    @Inject
    private NodeBus bus;
    
    private boolean learn;
}
