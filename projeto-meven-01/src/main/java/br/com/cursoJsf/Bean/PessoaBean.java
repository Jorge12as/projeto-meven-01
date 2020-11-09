package br.com.cursoJsf.Bean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import br.com.dao.DaoGeneric;
import br.com.entidades.Pessoa;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;


@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> listPessoas = new ArrayList<Pessoa>();
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();

	public String logar() {

		Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getlogin(), pessoa.getSenha());

		if (pessoaUser != null) {// achou o usuario

			// Adicionando o usuário na sessão - usuarioLogado
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", pessoaUser);

			return "primeirapagina.jsf";
		}

		return "index.jsf";
	}

	public boolean permiteAcesso(String acesso) {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

		return pessoaUser.getUsuarioPerfil().equals(acesso);
	}

	public String salvar() {
		pessoa = daoGeneric.mergeSalvar(pessoa);
		carregarPessoas();
		mostrarMsg("Registro salvo com sucesso!");
		return "";
	}

	public String novo() {
		pessoa = new Pessoa();
		return "";
	}
	public String Limpar() {
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
		mostrarMsg("Registro excluido com sucesso!");
		return "";
	}

	@SuppressWarnings("unused")
	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}


	public void pesquisaCep(AjaxBehaviorEvent event) {
		
		System.out.println(" Metodo chamado com sucesso!" + pessoa.getcep());
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
