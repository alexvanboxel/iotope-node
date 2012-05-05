package org.iotope.node.apps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.iotope.node.conf.Configuration;
import org.iotope.node.model.Application;
import org.iotope.node.model.Association;
import org.iotope.node.model.FieldDefinition;
import org.iotope.node.model.FieldValue;
import org.iotope.node.model.Tag;
import org.iotope.node.model.TagId;
import org.iotope.node.reader.TagChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class Correlation {
    private static Logger Log = LoggerFactory.getLogger(Correlation.class);
    
    EntityManagerFactory emf;
    
    @Inject
    Configuration configuration;
    
    public Correlation() {
        super();
        emf = Persistence.createEntityManagerFactory("iotope-node");
        EntityManager em = emf.createEntityManager();
        em.setFlushMode(FlushModeType.COMMIT);
        em.getTransaction().begin();
        
        Application app = em.find(Application.class, Integer.valueOf("1"));
        if (app == null) {
            app = new Application(1);
            app.addDefinition("url", "URL", "xs:string", "The URL to jump to.");
            em.persist(app);
        }
        app = em.find(Application.class, Integer.valueOf("2"));
        if (app == null) {
            app = new Application(2);
            app.addDefinition("url", "URL", "xs:string", "The URL to jump to.");
            app.addDefinition("method", "Method", "xs:string", "How to call (GET, POST, PUT, ...).");
            app.addDefinition("accept", "Accept", "xs:string", "The Accept header.");
            em.persist(app);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    /**
     * 
     * 
     * @param e
     * @return
     */
    public TagChange getAssociateDataForTag(TagChange e) {
        EntityManager em = emf.createEntityManager();
        try {
            TagId tid = new TagId(e.getNfcId());
            Tag tag = em.find(Tag.class, tid);
            if (tag == null) {
                return e;
            }
            
            TypedQuery<Association> query = em.createNamedQuery("findAssociationByTag", Association.class);
            query.setParameter("tag", tag);
            Association ass;
            try {
                ass = query.getSingleResult();
            } catch (NoResultException nre) {
                return e;
            }
            
            e.setApplication(ass.getApplication());
            for (FieldValue value : ass.getFields()) {
                e.addField(value);
            }
        } catch (Throwable ex) {
        }
        return e;
    }
    
    public void tagChange(TagChange e) {
        if (!configuration.isLearnMode() && configuration.isExecuteAssociated()) {
            if (TagChange.Event.ADDED == e.getEvent()) {
                
                Application application = e.getApplication();
                List<FieldValue> fields = e.getFields();
                
                switch (application.getAppId()) {
                case 1:
                    new WebLinkApp().execute(fields);
                    break;
                default:
                }
            }
        }
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
                ass.setFields(new ArrayList<FieldValue>());
                em.persist(ass);
            }
            // UPDATE AND ADD
            for (Object of : ofs) {
                @SuppressWarnings("unchecked")
                HashMap<String, String> f = (HashMap<String, String>) of;
                String name = f.get("name");
                String value = f.get("value");
                FieldValue fieldValue = ass.getFieldValueByName(name);
                if (fieldValue == null) {
                    FieldDefinition definition = app.getFieldDefinitionByName(name);
                    ass.addValue(definition, value);
                } else {
                    fieldValue.setValue(value);
                }
            }
            em.persist(ass);
            em.getTransaction().commit();
            em.close();
        } catch (Throwable e) {
            Log.error(e.getMessage());
            em.getTransaction().rollback();
        }
    }
}