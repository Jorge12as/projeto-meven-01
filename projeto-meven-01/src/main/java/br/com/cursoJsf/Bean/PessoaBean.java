package br.com.cursoJsf.Bean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> listPessoas = new ArrayList<Pessoa>();

	public String salvar() {
		pessoa = daoGeneric.mergeSalvar(pessoa);
		carregarPessoas();
		return "";
	}

	public String novo() {
		pessoa = new Pessoa();
		return "";
	}

	@PostConstruct
	public void carregarPessoas() {
		listPessoas = daoGeneric.getListEntity(Pessoa.class);
	}

	public String removeId() {
		daoGeneric.deletePorID(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		return "";
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Pessoa> getListPessoas() {
		return listPessoas;
	}
}
