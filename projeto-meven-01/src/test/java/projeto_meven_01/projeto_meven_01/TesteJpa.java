package projeto_meven_01.projeto_meven_01;

import javax.persistence.Persistence;

public class TesteJpa {

	public static void main(String[] args) {
		Persistence.createEntityManagerFactory("projeto-meven-01");
	}

}
