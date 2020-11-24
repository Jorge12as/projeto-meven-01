package br.com.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;
import org.hibernate.validator.constraints.br.TituloEleitoral;



@Entity
public class Pessoa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotEmpty
	@Size(min = 10, max = 50, message = "Nome deve ter entre 10 e 50 letras")	
	private String nome;

	@NotEmpty(message = "Sobrenome deve ser informado")
	@NotNull(message = "Sobrenome deve ser informado")
	private String sobrenome;

	@DecimalMax(value = "50", message = "Idade de ser menor que 50")	
	private Integer idade;

	private String nivelProgramador;

	private Integer linguagens[];

	private String sexo;

	private String[] frameworks;

	private Boolean ativo;

	private String usuarioPerfil;

	private String login;

	private String senha;

	private String cep;

	private String logradouro;

	private String complemento;

	private String bairro;

	private String localidade;

	private String uf;

	private String ibge;

	@TituloEleitoral(message = "Titulo eleitoral inválido")
	private String titEleitoral;
	
	private String unidade;
	
	private String gia;

	private String ddd;

	private String siafi;
	
	@CPF(message = "Cpf Inválido")
	private String cpf;
	
	@Column(columnDefinition = "longblob")/*Tipo longblob Grava arquivos em base 64*/
	private String fotoIconBase64;
	
	private String extesao; //exteção jpeg; jpg, png
				
	
	
	public String getFotoIconBase64() {
		return fotoIconBase64;
	}

	public void setFotoIconBase64(String fotoIconBase64) {
		this.fotoIconBase64 = fotoIconBase64;
	}

	public String getExtesao() {
		return extesao;
	}

	public void setExtesao(String extesao) {
		this.extesao = extesao;
	}

	public byte[] getFotoIconBase64original() {
		return fotoIconBase64original;
	}

	public void setFotoIconBase64original(byte[] fotoIconBase64original) {
		this.fotoIconBase64original = fotoIconBase64original;
	}

	@Lob //Grava arquivos na base de dados
	@Basic(fetch = FetchType.LAZY)
	private byte[] fotoIconBase64original;
	
	@Transient // OBJETO NÃO FICA PERSISTENTE OU NÃO GRAVA NO BANCO
	private Estados objEstadoTemp;

	@ManyToOne // MUITAS PESSOAS EM UMA PESSA
	private Cidades cidades;
	@Transient // OBJETO NÃO FICA PERSISTENTE OU NÃO GRAVA NO BANCO
	private Estados estados;

	
	
	public String getTitEleitoral() {
		return titEleitoral;
	}

	public void setTitEleitoral(String titEleitoral) {
		this.titEleitoral = titEleitoral;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public void setEstados(Estados estados) {
		this.estados = estados;
	}

	public Estados getEstados() {
		return estados;
	}

	public void setCidades(Cidades cidade) {
		this.cidades = cidade;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public String getCpf() {
		return cpf;
	}
	public Cidades getCidades() {
		return cidades;
	}

	public Estados getObjEstadoTemp() {
		return objEstadoTemp;
	}

	public void setObjEstadoTemp(Estados objEstadoTemp) {
		this.objEstadoTemp = objEstadoTemp;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getIbge() {
		return ibge;
	}

	public void setIbge(String ibge) {
		this.ibge = ibge;
	}

	public String getGia() {
		return gia;
	}

	public void setGia(String gia) {
		this.gia = gia;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getSiafi() {
		return siafi;
	}

	public void setSiafi(String siafi) {
		this.siafi = siafi;
	}

	public void setcep(String cep) {
		this.cep = cep;
	}

	public String getcep() {
		return cep;
	}

	@Temporal(TemporalType.DATE)
	private Date dataNascimento = new Date();

	public String getlogin() {
		return login;
	}

	public void setlogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void setNivelProgramador(String nivelProgramador) {
		this.nivelProgramador = nivelProgramador;
	}

	public String getNivelProgramador() {
		return nivelProgramador;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setFrameworks(String[] frameworks) {
		this.frameworks = frameworks;
	}

	public String[] getFrameworks() {
		return frameworks;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getSexo() {
		return sexo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLinguagens(Integer[] linguagens) {
		this.linguagens = linguagens;
	}

	public Integer[] getLinguagens() {
		return linguagens;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getUsuarioPerfil() {
		return usuarioPerfil;
	}

	public void setUsuarioPerfil(String usuarioPerfil) {
		this.usuarioPerfil = usuarioPerfil;
	}

	public Pessoa() {
	}

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
		Pessoa other = (Pessoa) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
