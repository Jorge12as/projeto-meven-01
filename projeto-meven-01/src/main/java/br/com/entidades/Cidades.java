package br.com.entidades;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Cidades implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cidades other = (Cidades) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Estados getEstados() {
		return estados;
	}

	public void setEstados(Estados estados) {
		this.estados = estados;
	}

	private String nome;

	// RELACIONAMENTO ENTRE AS TABELAS DIDADES E ESTADOS,(MUITOS CIDADES PARA UM
	// ESTADO)
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH) // para que aconteça o carregamento dos
	private Estados estados;																	// relacionamentos usa-se as clausulas(fetch =
																		// FetchType.EAGER,) e para que seja feita
																	    // alguma atualização ou refreshe no banco
																		// usa-se (cascade = CascadeType.REFRESH)para
																		// que seja carregado em cascata..
	
}
