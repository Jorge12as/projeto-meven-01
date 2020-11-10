package br.com.repository;

import java.util.List;

import javax.faces.model.SelectItem;

import br.com.entidades.Pessoa;

public interface IDaoPessoa {

	Pessoa consultarUsuario(String usuario, String senha);
	
	List<SelectItem>listaEstados();
}
