package br.com.cursoJsf.Bean;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;
import com.google.gson.Gson;
import br.com.dao.DaoGeneric;
import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUTIL;
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
	private List<SelectItem> cidades;
	private Part arquivoFoto;

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

	public void registraLog() {
		System.out.println("método registraLog");
		/* Criar a rotina de gravação de log */
	}

	public boolean permiteAcesso(String acesso) {

		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

		return pessoaUser.getUsuarioPerfil().equals(acesso);
	}

	public String salvar() throws IOException {

		/* Processar imagem */
		byte[] imagemByte = getByte(arquivoFoto.getInputStream());
		pessoa.setFotoIconBase64original(imagemByte); // SALVA IMAGEM ORIGINAL

		/* TRANSFORMA EM BUFFERIMAGE */
		BufferedImage bufferImage = ImageIO.read(new ByteArrayInputStream(imagemByte));

		/* PEGA O TIPO DE IMAGEM */
		int type = bufferImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferImage.getType();

		int largura = 200;
		int altura = 200;

		/* CRIA MINIATURA */
		BufferedImage resizedImage = new BufferedImage(altura, largura, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(bufferImage, 0, 0, largura, altura, null);
		g.dispose();

		/* Escrever novamante a imagem em um tamanho menor */
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extensao = arquivoFoto.getContentType().split("\\/")[1];// PEGANDO A EXTENSAO DO ARQUINVO
		ImageIO.write(resizedImage, extensao, baos);

		/* CRIANDO O CABEÇALHO DA IMAGEM PARA EXIBIR NO NAVEGADOR */
		String miniImagem = "data:" + arquivoFoto.getContentType() + ";base64,"
				+ DatatypeConverter.printBase64Binary(baos.toByteArray());

		/* PROCESSAR IMAGEM */
		pessoa.setFotoIconBase64(miniImagem);
		pessoa.setExtesao(extensao);

		pessoa = daoGeneric.mergeSalvar(pessoa);
		carregarPessoas();
		mostrarMsg("Registro salvo com sucesso!");
		return "";
	}

	/* METODO QUE CONVERTE INPUTSTREAM PARA ARRAY DE BYTES */
	@SuppressWarnings("unused")
	private byte[] getByte(InputStream is) throws IOException {

		int len;
		int size = 1024;
		byte[] buf = null;

		if (is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);

		} else {

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];

			while ((len = is.read(buf, 0, size)) != -1) {

				bos.write(buf, 0, len);
			}

			buf = bos.toByteArray();
		}

		return buf;

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

	@SuppressWarnings("unchecked")
	public void carregaCidades(AjaxBehaviorEvent event) {

		Estados estado = (Estados) ((HtmlSelectOneMenu) event.getSource()).getValue();
		if (estado != null) {

			pessoa.setObjEstadoTemp(estado);

			List<Cidades> cidades = JPAUTIL.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();

			List<SelectItem> selectItemCidade = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {

				selectItemCidade.add(new SelectItem(cidade, cidade.getNome()));
			}

			setCidades(selectItemCidade);
		}
	}

	public void mudancaDeValor(ValueChangeEvent evento) {
		System.out.println("Valor antigo: " + evento.getOldValue());
		System.out.println("Valor Novo: " + evento.getNewValue());
	}

	public void mudancaDeValorSobrenome(ValueChangeEvent evento) {
		System.out.println("Valor antigo: " + evento.getOldValue());
		System.out.println("Valor Novo: " + evento.getNewValue());
	}

	@SuppressWarnings("unchecked")
	public void editar() {
		if (pessoa.getCidades() != null) {
			Estados estado = pessoa.getCidades().getEstados(); // CARREGANDO OS ESTADOS AO COMBO ESTADO
			pessoa.setEstados(estado); // SETANDO O ESTADO DA CONSULTA AO ESTADO DA PESSOA

			// CARREGANDO O COMBO CIDADES
			List<Cidades> cidades = JPAUTIL.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
			}

			setCidades(selectItemsCidade);

		}
	}

	public List<Pessoa> getListPessoas() {
		return listPessoas;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}

	public Part getArquivoFoto() {
		return arquivoFoto;
	}
}
