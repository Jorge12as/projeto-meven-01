package br.com.cursoJsf.Bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

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
	private List<SelectItem> estados;

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

	public String deslogar() {

		// Adicionando o usuário na sessão - usuarioLogado
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado"); // REMOVE O USUARIO DA SESSÃO EM USO

		@SuppressWarnings("static-access")
		HttpServletRequest httpServletRequest = (HttpServletRequest) // PEGA A SESSÃO EM USO.
		context.getCurrentInstance().getExternalContext().getRequest();

		httpServletRequest.getSession().invalidate(); // INVÁLIDA A SESSÃO DO USUARIO

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

	public void pesquisarCep(AjaxBehaviorEvent event) {

		try {

			URL url = new URL("http://viacep.com.br/ws/" + pessoa.getcep() + "/json/");
			URLConnection connection = url.openConnection(); // ABRE A CONEXÃO COM O WEBSERVICE
			InputStream is = connection.getInputStream(); // EXECUTA A CONEXÃO DO LADO DO WEBSERVICE
			// CLASSES RESPONSAVEIS POR LER OS DADOS VINDO EM JSON
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String cep = "";
			StringBuilder jsonCep = new StringBuilder(); // CLASSE PARA LER O RESULTADO DO JSON

			while ((cep = br.readLine()) != null) { // ATRIBUINDO OS DADOS DA CONSULTA A VARIAVEL CEP
				jsonCep.append(cep); // ATRIBUINDO OS DADOS DA VARIAVEL CEP A VARIAVEL jsonCep
			}

			Pessoa gsonAux = new Gson().fromJson(jsonCep.toString(), Pessoa.class);

			pessoa.setCep(gsonAux.getCep());
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());
			pessoa.setIbge(gsonAux.getIbge());
			pessoa.setGia(gsonAux.getGia());
			pessoa.setDdd(gsonAux.getDdd());
			pessoa.setSiafi(gsonAux.getSiafi());

			System.out.println(jsonCep);

		} catch (Exception e) {
			e.getStackTrace();
			mostrarMsg("Erro ao consultar o cep");
		}

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

	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listaEstados();
		return estados;
	}

	public List<Pessoa> getListPessoas() {
		return listPessoas;
	}
}
