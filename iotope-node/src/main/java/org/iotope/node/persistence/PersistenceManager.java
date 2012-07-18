package org.iotope.node.persistence;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Singleton
public class PersistenceManager {
    
    EntityManagerFactory emf;
    
    public PersistenceManager() {
        emf = Persistence.createEntityManagerFactory("iotope-node");
    }
    
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
}
