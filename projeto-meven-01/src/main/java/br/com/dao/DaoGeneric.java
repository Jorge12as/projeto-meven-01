package br.com.dao;

import java.util.List;
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

	public T mergeSalvar(T entidade) {

		EntityManager entityManager = JPAUTIL.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		T retorno = entityManager.merge(entidade);
		entityTransaction.commit();
		entityManager.close();

		return retorno;
	}

	@SuppressWarnings("unchecked")
	public List<T> getListEntity(Class<T> entidade) {

		EntityManager entityManager = JPAUTIL.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		List<T> retorno = entityManager.createQuery("from " + entidade.getName()).getResultList();
		entityTransaction.commit();
		entityManager.close();

		return retorno;

	}

	public void deletePorID(T entidade) {

		EntityManager entityManager = JPAUTIL.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		Object id = JPAUTIL.getPrimarykey(entidade);
		entityManager.createQuery("delete from " + entidade.getClass().getCanonicalName() + " where id = " + id)
				.executeUpdate();
		entityTransaction.commit();
		entityManager.close();

	}

	public T consultar(Class<T> entidade, String codigo) {

		EntityManager entityManager = JPAUTIL.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		T objeto = (T) entityManager.find(entidade, Long.parseLong(codigo));

		entityTransaction.commit();

		return objeto;
	}
}
