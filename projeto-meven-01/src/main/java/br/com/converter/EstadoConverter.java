package br.com.converter;

import java.io.Serializable;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import br.com.entidades.Estados;
import br.com.jpautil.JPAUTIL;


@FacesConverter(forClass = Estados.class, value = "estadoConverter")
public class EstadoConverter implements Converter, Serializable {

	private static final long serialVersionUID = -628943317877875062L;

	@Override
	/* Retorna obejto inteiro */
	public Object getAsObject(FacesContext context, UIComponent component,
			String codigoEstado) {

		EntityManager entityManager = JPAUTIL.getEntityManager();
		EntityTransaction entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();

		Estados estados = (Estados) entityManager.find(Estados.class,
				Long.parseLong(codigoEstado));
		entityManager.close();
		return estados;
	}

	@Override
	/* Retorna apenas o código em String */
	public String getAsString(FacesContext context, UIComponent component,
			Object estado) {
		
		if (estado == null){
			return null;
		}
		
		if (estado instanceof Estados){
			return ((Estados) estado).getId().toString();

		}else {
			return estado.toString();
		}

		
	}
}
