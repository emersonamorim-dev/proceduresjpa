package com.proceduresjpa;

import com.proceduresjpa.model.Produto;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


public class StoredProceduresJPA {
	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory = Persistence
				.createEntityManagerFactory("Produto-JPA");
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		//
		updateWithProcedures(entityManager);

		entityManager.close();
		entityManagerFactory.close();
	}

	public static void updateWithProcedures(EntityManager entityManager) {
		StoredProcedureQuery storedProcedureQuery = entityManager
				.createStoredProcedureQuery("update_price_product");

		storedProcedureQuery.registerStoredProcedureParameter(
				"produto_id", Integer.class, ParameterMode.IN);

		storedProcedureQuery.registerStoredProcedureParameter(
				"price_update", BigDecimal.class, ParameterMode.OUT);

		storedProcedureQuery.setParameter("produto_id", 1);
		storedProcedureQuery.setParameter("percentual_update", new BigDecimal(1.1));

		storedProcedureQuery.execute();

		BigDecimal priceupdate = (BigDecimal) storedProcedureQuery
				.getOutputParameterValue("priceupdate");


		String priceUpdate = null;
		log("Price update:" + priceUpdate);

	}

	public static void searchWithProcedures(EntityManager entityManager) {
		StoredProcedureQuery storedProcedureQuery = entityManager
				.createStoredProcedureQuery("pesquisar_produtos", Produto.class);

		storedProcedureQuery.registerStoredProcedureParameter(
				"termo", String.class, ParameterMode.IN);

		storedProcedureQuery.setParameter("termo", "C");

		storedProcedureQuery.execute();

		List<Produto> lista = storedProcedureQuery.getResultList();

		lista.forEach(p -> log("Produto => Id: " + p.getId() + ", Nome: " + p.getNome()));
	}

	private static void consultaComProcedures(EntityManager entityManager) {
		StoredProcedureQuery storedProcedureQuery = entityManager
				.createStoredProcedureQuery("nome_produto");

		storedProcedureQuery.registerStoredProcedureParameter(
				"produto_id", Integer.class, ParameterMode.IN);

		storedProcedureQuery.registerStoredProcedureParameter(
				"produto_nome", String.class, ParameterMode.OUT);

		storedProcedureQuery.setParameter("produto_id", 1);

		storedProcedureQuery.execute();

		String nome = (String) storedProcedureQuery
				.getOutputParameterValue("produto_nome");

		log("Nome produto: " + nome);
	}

	private static void log(Object msg) {
		System.out.println("[LOG " + System.currentTimeMillis() + "] " + msg);
	}

}
