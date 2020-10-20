package br.com.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import br.com.jpautil.JPAUTIL;

public class DaoGeneric<T> {

	public void salvar(T entidade) {

		EntityManager entityManager = JPAUTIL.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
		entityManager.persist(entidade);
		entityTransaction.commit();
		entityManager.close();

	}

}
