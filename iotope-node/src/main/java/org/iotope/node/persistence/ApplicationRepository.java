package org.iotope.node.persistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.iotope.node.model.Application;
import org.iotope.node.model.Association;
import org.iotope.node.model.FieldDefinition;
import org.iotope.node.model.FieldValue;
import org.iotope.node.model.Tag;
import org.iotope.node.model.TagId;
import org.iotope.pipeline.ExecutionContextImpl;
import org.iotope.pipeline.model.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApplicationRepository {
    private static Logger Log = LoggerFactory.getLogger(ApplicationRepository.class);
    
    @Inject
    PersistenceManager manager;
    
    public ApplicationRepository() {
        super();
    }
    
    public void addApplication(String domain, String name, String displayName, String description, Collection<Field> fields) {
        EntityManager em = manager.createEntityManager();
        try {
            em.setFlushMode(FlushModeType.COMMIT);
            em.getTransaction().begin();
            
            TypedQuery<Application> query = em.createNamedQuery("findApplicationByName", Application.class);
            query.setParameter("domain", domain);
            query.setParameter("name", name);
            Application app;
            try {
                app = query.getSingleResult();
            } catch (NoResultException nre) {
                app = new Application(domain, name, displayName, description);
                for (Field field : fields) {
                    app.addDefinition(field.getName(), field.getType(), field.getDisplayName(), field.getDescription());
                }
                em.persist(app);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    /**
     * 
     * 
     * @param string 
     * @param e
     * @return
     */
    public void getAssociateDataForTag(String nfcid, ExecutionContextImpl ec) {
        EntityManager em = manager.createEntityManager();
        try {
            TagId tid = new TagId(nfcid);
            Tag tag = em.find(Tag.class, tid);
            if (tag == null) {
                return;
            }
            
            TypedQuery<Association> query = em.createNamedQuery("findAssociationByTag", Association.class);
            query.setParameter("tag", tag);
            Association ass;
            try {
                ass = query.getSingleResult();
            } catch (NoResultException nre) {
                return;
            }
            
            ec.setFields(ass.getApplication(), ass.getFields());
        } finally {
            em.close();
        }
    }
    
    /**
     * Prototype: Need to be moved
     * 
     * @param tagId
     * @param fields
     */
    public void associate(String tagId, int appId, List<?> fields) {
        EntityManager em = manager.createEntityManager();
        try {
            em.setFlushMode(FlushModeType.COMMIT);
            em.getTransaction().begin();
            
            TagId tid = new TagId(tagId);
            Tag tag = em.find(Tag.class, tid);
            if (tag == null) {
                tag = new Tag(tid);
                em.persist(tag);
            }
            
            Application app = em.find(Application.class, appId);
            if (app == null) {
                throw new RuntimeException();
            }
            
            TypedQuery<Association> query = em.createNamedQuery("findAssociationByTag", Association.class);
            query.setParameter("tag", tag);
            Association ass;
            try {
                ass = query.getSingleResult();
                if (!ass.getApplication().equals(app)) {
                    ass.setApplication(app);
                    em.persist(ass);
                }
            } catch (NoResultException nre) {
                ass = new Association();
                ass.setApplication(app);
                ass.setTag(tag);
                ass.setFields(new ArrayList<FieldValue>());
                em.persist(ass);
            }
            // Remove old
            List<FieldValue> toRemove = new ArrayList<FieldValue>();
            for (FieldValue val : ass.getFields()) {
                if (!val.getField().getApp().equals(app)) {
                    toRemove.add(val);
                }
            }
            for (FieldValue val : toRemove) {
                em.remove(val);
                ass.getFields().remove(val);
            }
            // UPDATE AND ADD
            for (Object of : fields) {
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
        } catch (Throwable e) {
            Log.error(e.getMessage());
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    public void removeAssociation(String tagId, int appId) {
        
        EntityManager em = manager.createEntityManager();
        try {
            em.setFlushMode(FlushModeType.COMMIT);
            em.getTransaction().begin();
            
            TagId tid = new TagId(tagId);
            Tag tag = em.find(Tag.class, tid);
            if (tag == null) {
                throw new RuntimeException();
            }
            
            TypedQuery<Association> query = em.createNamedQuery("findAssociationByTag", Association.class);
            query.setParameter("tag", tag);
            Association ass;
            try {
                ass = query.getSingleResult();
                em.remove(ass);
            } catch (NoResultException nre) {
            }
            em.getTransaction().commit();
            em.close();
        } catch (Throwable e) {
            Log.error(e.getMessage());
            em.getTransaction().rollback();
        }
    }
    
    public List<Application> getApplications() {
        EntityManager em = manager.createEntityManager();
        em.setFlushMode(FlushModeType.COMMIT);
        TypedQuery<Application> query = em.createQuery("select app from Application app", Application.class);
        try {
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    public List<Field> getFieldsForApplications(String appId) {
        EntityManager em = manager.createEntityManager();
        try {
            Application app = em.find(Application.class, Integer.valueOf(appId));
            if (app == null) {
                throw new RuntimeException();
            }
            List<Field> fields = new ArrayList<Field>();
            for (FieldDefinition def : app.getFieldDefinitions()) {
                fields.add(new Field(def));
            }
            return fields;
        } finally {
            em.close();
        }
    }
}
