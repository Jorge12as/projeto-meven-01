package br.com.repository;

import br.com.entidades.Pessoa;

public interface IDaoPessoa {

	Pessoa consultarUsuario(String usuario, String senha);
	
}
