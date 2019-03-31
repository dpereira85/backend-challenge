package com.invillia.acme.repository.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.invillia.acme.model.Store;
import com.invillia.acme.repository.StoreRepositoryCustom;

@Repository
public class StoreRepositoryImpl implements StoreRepositoryCustom {
	
	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public List<Store> findByParameters(String name, String address) {
		String queryStr = "Select s From Store s Where lower(s.name) like lower(:name) or lower(s.address) like lower(:address)";
		
		Query query = entityManager.createQuery(queryStr);
		query.setParameter("name", "%"+name+"%");
		query.setParameter("address", "%"+address+"%");
		
		return query.getResultList();
	}

}
