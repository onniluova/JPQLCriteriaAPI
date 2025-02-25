package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import entity.*;
import java.util.*;

public class Dao {

	private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("Harj1PU");

	public void addRegister(Register reg) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

        em.persist(reg);

        em.getTransaction().commit();
        em.close();
	}

	public void addEvent(int eventNumber, int regNumber, double amount) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

        Register reg = em.find(Register.class, regNumber);
        SalesEvent evt = new SalesEvent(eventNumber, reg, amount);

        em.persist(evt);

        em.getTransaction().commit();
        em.close();
	}

	public List<SalesEvent> retrieveSmallSales(double limit) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		TypedQuery<SalesEvent> getAllSalesQuery = em.createQuery("SELECT e FROM SalesEvent e", SalesEvent.class);
		List<SalesEvent> result = getAllSalesQuery.getResultList();

		Iterator<SalesEvent> iterator = result.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getAmount() > limit) {
				iterator.remove();
			}
		}

		System.out.println(result);

		em.getTransaction().commit();
		em.close();
		return result;
	}

	public List<SalesEvent> addFees(double feeAmount) {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		TypedQuery<SalesEvent> getAllSalesQuery = em.createQuery("SELECT e FROM SalesEvent e", SalesEvent.class);
		List<SalesEvent> result = getAllSalesQuery.getResultList();

		for (SalesEvent event : result) {
			event.setAmount(event.getAmount() + feeAmount);
			em.merge(event);
		}

		System.out.println(result);

		em.getTransaction().commit();
		em.close();

        return result;
    }

	public void deleteAllEvents() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		Query deleteQuery = em.createQuery("DELETE FROM SalesEvent");
		int deletedCount = deleteQuery.executeUpdate();

		em.getTransaction().commit();
		em.close();

		System.out.println("Deleted " + deletedCount + " sales events.");
	}
}
