package com.invillia.acme.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.invillia.acme.model.Store;

public interface StoreRepository extends CrudRepository<Store, UUID> {

	List<Store> findByNameIgnoreCaseContaining(String name);
	List<Store> findByAddressIgnoreCaseContaining(String address);
	List<Store> findByNameIgnoreCaseContainingOrAddressIgnoreCaseContaining(String name, String address);
}
