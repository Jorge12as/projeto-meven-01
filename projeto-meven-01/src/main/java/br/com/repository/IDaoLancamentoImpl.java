package br.com.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Lancamento;
import br.com.jpautil.JPAUTIL;

public class IDaoLancamentoImpl implements IDaoLancamento {

	@SuppressWarnings("unchecked")
	@Override
	public List<Lancamento> consultarLancamentos(Long codUser) {

		
		List<Lancamento> listarLancamentos = null;
		EntityManager entytiManeger = JPAUTIL.getEntityManager();
		EntityTransaction trasaction = entytiManeger.getTransaction();
		trasaction.begin();

		listarLancamentos = entytiManeger.createQuery("from Lancamento where usuario.id = " + codUser)
				.getResultList();

		trasaction.commit();
		entytiManeger.close();
		
		return listarLancamentos;
		
	}

}
