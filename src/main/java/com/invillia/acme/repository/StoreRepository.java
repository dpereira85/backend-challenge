package com.invillia.acme.repository;

import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.invillia.acme.model.Store;

@RepositoryRestResource(exported=false)
public interface StoreRepository extends PagingAndSortingRepository<Store, UUID> {

}
