package ru.yandex.strictweb.ajaxtools.orm;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ORMManager {
	EntityManagerFactory emf;

	ThreadLocal<EntityManager> persistManager = new ThreadLocal<EntityManager>();

	public ORMManager() {
		this("default");
	}
	
	public ORMManager(String name) {
		emf = Persistence.createEntityManagerFactory(name);
	}
	
	public EntityManagerFactory getFactory() {
		return emf;
	}
	
	public void closeFactory() {
		emf.close();		
	}
	
	public EntityManager begin() {
		EntityManager em = get();
		if(null == em) {
			persistManager.set(em = emf.createEntityManager());
		}
		if(!em.getTransaction().isActive()) em.getTransaction().begin();
		return em;
	}
	
	public void commit() {
		EntityManager em = get();
		if(null == em) return;
		if(em.getTransaction().isActive()) em.getTransaction().commit();
	}

	public void rollback() {
		EntityManager em = get();
		if(null == em) return;
		if(em.getTransaction().isActive()) em.getTransaction().rollback();
	}

	public void close() {
		EntityManager em = get();
		if(null == em) return;
		commit();
		if(em.isOpen()) em.close();
		persistManager.set(null);
	}
	
	public EntityManager get() {
		return persistManager.get();
	}	
}
