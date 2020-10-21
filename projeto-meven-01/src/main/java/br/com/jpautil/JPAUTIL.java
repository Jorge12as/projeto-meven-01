package br.com.jpautil;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUTIL {

	private static EntityManagerFactory factory = null;
/*
 * METODO RESPONSAVEL PELA PERCISTENCIA DO PROJETO
 * PADRÃO SINGLETON, ONDE SÓ PODE EXISTIR UM INSTANCIA DESSA CONEXÃO NO PROJETO ..			
 */
	static {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("projeto-meven-01");
		}
	}

	public static EntityManager getEntityManager() {

		return factory.createEntityManager();
	}

	public static Object getPrimarykey(Object entidade) {
		
		return factory.getPersistenceUnitUtil().getIdentifier(entidade);
	}

}
