package com.invillia.acme.controller;

import static com.invillia.acme.utils.StringUtils.isEmpty;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.invillia.acme.exception.ApiError;
import com.invillia.acme.model.Store;
import com.invillia.acme.repository.StoreRepository;

@RestController
public class StoreController {

	private final StoreRepository repository;

	@Autowired
	public StoreController(StoreRepository repository) {
		this.repository = repository;
	}

	/** TODO
	 * @param id
	 * @return
	 */
	@GetMapping("/stores/{id}")
	public ResponseEntity<Object> retrieveStoreById(@PathVariable String id) {
		if (isEmpty(id)) {
			return handleError(HttpStatus.BAD_REQUEST, "O ID não foi informado");
		}
		try {
			UUID uuid = UUID.fromString(id);
			Store store = repository.findById(uuid).get();
			return ResponseEntity.ok(store);
		}
		catch (IllegalArgumentException ex) {
			return handleError(HttpStatus.BAD_REQUEST, String.format("O ID [%s] não é válido.", id));
		}
		catch (NoSuchElementException ex) {
			return handleError(HttpStatus.NOT_FOUND, String.format("Nenhuma Store encontrada para o ID [%s]", id));
		}
	}
	
	@GetMapping("/stores")
	public ResponseEntity<Object> retrieveStoreByParameters(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "address", required = false) String address) {
		
		List<Store> stores = null;
		
		// Valida os inputs e decide qual query utilizar dependendo de quais campos foram preenchidos.
		if (isEmpty(name) && isEmpty(address)) {
			return handleError(HttpStatus.BAD_REQUEST, "Nenhum parâmetro válido foi informado. \n Os parâmetros aceitos são 'name' e 'address'. "
					+ "Para consultar pelo ID, utilize o caminho //stores//{id}");
		}
		else if (!isEmpty(name) && !isEmpty(address)) {
			stores = repository.findByNameIgnoreCaseContainingOrAddressIgnoreCaseContaining(name, address);
		}
		else if (!isEmpty(name)) {
			stores = repository.findByNameIgnoreCaseContaining(name);
		}
		else if (!isEmpty(address)){
			stores = repository.findByAddressIgnoreCaseContaining(address);
		}

		// Se os campos são válidos mas nenhuma Store foi encontrada, retorna 404. 
		if (stores == null || stores.isEmpty()) {
			return handleError(HttpStatus.NOT_FOUND, String.format("Nenhuma Store encontrada para os parâmetros Name [%s] ou Address [%s]", name, address));
		}

		// Caso contrário retorna a lista de Stores encontrada.
		return ResponseEntity.ok(stores);
	}

	/** 
	 * TODO
	 * @param newStore
	 * @return
	 * @throws URISyntaxException 
	 */
	@PostMapping("/stores")
	public ResponseEntity<Object> createStore(@RequestBody Store newStore) throws URISyntaxException {

		// Validação dos inputs
		if (newStore == null) {
			return handleError(HttpStatus.BAD_REQUEST, "A informação está incompleta ou malformada.");
		}
		else if (isEmpty(newStore.getAddress()) || isEmpty(newStore.getName())) {
			return handleError(HttpStatus.BAD_REQUEST, "Os campos 'name' e 'address' são obrigatórios.");
		}
		
		// Persiste a Store e retorna o objeto para consulta (incluindo o novo ID)
		Store store = repository.save(newStore);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(newStore.getId())
				.toUri();
		
		return ResponseEntity.created(location).body(store);
	}

	/**
	 * TODO
	 * 
	 * @param newStore
	 * @param id
	 * @return
	 */
	@PutMapping("/stores/{id}")
	public ResponseEntity<Object> updateStore(@RequestBody Store newStore, @PathVariable String id) throws Exception {
		
		// Validação dos inputs
		if (newStore == null) {
			return handleError(HttpStatus.BAD_REQUEST, "A informação está incompleta ou malformada.");
		}
		else if (isEmpty(id)) {
			return handleError(HttpStatus.BAD_REQUEST, "É necessário informar o ID.");
		}
		else if (isEmpty(newStore.getAddress()) && isEmpty(newStore.getName())) {
			return handleError(HttpStatus.BAD_REQUEST, "É necessário informar ao menos um dos campos para atualizar a Store.");
		}
		
		// Somente os campos informados serão atualizados. Caso o request venha apenas com 'name' ou 'address', o valor
		// antigo do campo omitido é preservado.
		try {
			UUID uuid = UUID.fromString(id);
			newStore.setId(uuid);
			Store store = updateAndSave(uuid, newStore);
			return ResponseEntity.ok(store);
		}
		catch (IllegalArgumentException ex) {
			return handleError(HttpStatus.BAD_REQUEST, String.format("O ID [%s] não é válido.", id));
		}
		catch (NoSuchElementException ex) {
			return handleError(HttpStatus.NOT_FOUND, String.format("Nenhuma Store encontrada para o ID [%s]", id));
		}
	}
		

	private Store updateAndSave(UUID uuid, Store newStore) {
		
		Store store = repository.findById(uuid).get();
		
		if (!isEmpty(newStore.getName())) {
			store.setName(newStore.getName());
		}
		
		if (!isEmpty(newStore.getAddress())) {
			store.setAddress(newStore.getAddress());
		}
		
		repository.save(store);
		return store;
	}
	
	private ResponseEntity<Object> handleError(HttpStatus status, String message) {
		return new ResponseEntity<Object>(new ApiError(status, message), status);
	}

}
