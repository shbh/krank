package com.arcmind.jpa.course;

import javax.naming.InitialContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class UserDaoTest extends EmbeddedEJB3JBossBaseTestCase {

	UserDao userDao;
	EntityManagerFactory entityManagerFactory;
	
	protected void setUp() throws Exception {
		super.setUp();
		entityManagerFactory = Persistence.createEntityManagerFactory("security-domain");
		createTestData();
	}

	protected void tearDown() throws Exception {
		deleteTestData();
		super.tearDown();
	}

	public void testCrudOperations() throws Exception {

		InitialContext ctx = getInitialContext();
		userDao = (UserDao) ctx.lookup("UserDaoImpl/local");
		
		User user = new User();
		Long id = null;
		user.setName("RickHigh");

		/* Overall Objective: add a new User to the database using JPA with EJB. */
		/* Use the EJB3-configured UserDaoImpl. */

		userDao.create(user);
		
		id = user.getId();
		user = null;

		/* Look up the user in the database. */
		user = userDao.read(id);
		user.setName("RicardoTorreAlto");

		/* Test that the object was read from the database. */
		assertNotNull(user);
		assertEquals("RicardoTorreAlto", user.getName());

		/* Overall Objective: Delete the user. */
		/* Try to delete the user. */
		userDao.delete(user);

		/* Overall Objective: Test that the user was deleted. */
		user = userDao.read(id);

		/* Test that the user was not found in the database. */
		assertNull(user);

	}

	private void deleteTestData() throws Exception {
		
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	Query query = entityManager.createQuery("delete User");
        	query.executeUpdate();
        	transaction.commit();        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
		
	}



	private void createTestData() throws Exception {
		String[] userNames = new String[]{"RickHi","BobSmith","Sergey","PaulHix","Taboraz"}; 
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		/* Start a transaction. */
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        try {
        	for (String userName : userNames) {
        		User user = new User();
        		user.setName(userName);
        		entityManager.persist(user);
        	}
        	for (int index = 0; index < 100; index++) {
        		User user = new User();
        		user.setName("user" + index);
        		entityManager.persist(user);
        	}        	
        	transaction.commit();
        	
        } catch (Exception ex) {
        	ex.printStackTrace();
        	transaction.rollback();
        	throw ex;
        }
	}

}
