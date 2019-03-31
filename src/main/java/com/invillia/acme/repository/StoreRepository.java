package com.invillia.acme.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.invillia.acme.model.Store;

public interface StoreRepository extends CrudRepository<Store, UUID>, StoreRepositoryCustom {

}
