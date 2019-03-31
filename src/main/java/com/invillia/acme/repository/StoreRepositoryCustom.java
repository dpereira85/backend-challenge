package com.invillia.acme.repository;

import java.util.List;

import com.invillia.acme.model.Store;

public interface StoreRepositoryCustom {

	List<Store> findByParameters(String name, String address);

}
