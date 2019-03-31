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

/**
 * Endpoint que contém as funcionalidades de criar, buscar e atualizar objetos da entidade Store. <br><br>
 * 
 * O caminho completo dele é "{server}/api/v1/stores".
 * 
 * @author Daniel
 * @version 1.0
 *
 */
@RestController
public class StoreController {

	private final StoreRepository repository;

	@Autowired
	public StoreController(StoreRepository repository) {
		this.repository = repository;
	}

	/**
	 * Recurso GET que busca e retorna uma Store a partir do seu id.
	 * 
	 * <br><br>
	 * 
	 * Caso o ID seja válido e a Store existir, o código do retorno será 200 e o conteúdo será a Store pesquisada.<br>
	 * Se o ID for inválido, o código será 400 e o retorno será do tipo ApiError.<br>
	 * Se o ID for válido mas a Store não existe, o retorno será 404 e o conteúdo também será do tipo ApiError.
	 * 
	 * @param id - <b>UUID</b> da Store desejada.
	 * @return Store ou ApiError.
	 */
	@GetMapping("/stores/{id}")
	public ResponseEntity<?> retrieveStoreById(@PathVariable String id) {
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
	
	/**
	 * Recurso GET que busca e retorna uma Store a partir das suas propriedades <b>name</b> ou <b>address</b>.
	 * 
	 * <br><br>
	 * 
	 * Retorna código 404 e o tipo ApiError caso nenhuma Store exista com os parâmetros. Se existir ao menos uma,
	 *  retorna uma List&lt;Store&gt; com código 200.
	 * 
	 * @param name - Nome da Store.
	 * @param address - Endereço da Store.
	 * @return List&lt;Store&gt; ou ApiError
	 */
	@GetMapping("/stores")
	public ResponseEntity<?> retrieveStoreByParameters(@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "address", required = false) String address) {
		
		List<Store> stores = null;
		
		// Valida os inputs
		if (isEmpty(name) && isEmpty(address)) {
			return handleError(HttpStatus.BAD_REQUEST, "Nenhum parâmetro válido foi informado. \n"
					+ " Os parâmetros aceitos são 'name' e 'address'. "
					+ "Para consultar pelo ID, utilize o caminho //stores//{id}");
		}
		
		stores = repository.findByParameters(name, address);

		// Se os campos são válidos mas nenhuma Store foi encontrada, retorna 404. 
		if (stores == null || stores.isEmpty()) {
			return handleError(HttpStatus.NOT_FOUND, String.format("Nenhuma Store encontrada para os parâmetros Name [%s] ou Address [%s]", name, address));
		}

		// Caso contrário retorna a lista de Stores encontrada.
		return ResponseEntity.ok(stores);
	}

	/** 
	 * Recurso POST que recebe um JSON do tipo Store e a persiste no banco de dados. <br>
	 * Retorna o código 400 se as informações da Store estiverem incompletas ou malformadas,
	 * e código 201 Created juntamente com os dados da nova Store se a inserção for bem sucedida.
	 * 
	 * @param newStore - Nova Store para persistir.
	 * @return Store ou ApiError
	 * @throws URISyntaxException 
	 */
	@PostMapping("/stores")
	public ResponseEntity<?> createStore(@RequestBody Store newStore) throws URISyntaxException {

		// Validação dos inputs
		if (newStore == null) {
			return handleError(HttpStatus.BAD_REQUEST, "A informação está incompleta ou malformada.");
		}
		else if (isEmpty(newStore.getAddress()) || isEmpty(newStore.getName())) {
			return handleError(HttpStatus.BAD_REQUEST, "Os campos 'name' e 'address' são obrigatórios.");
		}
		
		// Impede que alguém tente utilizar o método POST para atualizar uma Store, informando o UUID no request
		newStore.setId(null);
		
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
	 * Recurso PUT que recebe um JSON do tipo Store e atualiza os dados dela no banco. <br>
	 * Retorna código 200 e os dados da entidade salva em caso de sucesso, ou código 400 e ApiError
	 * em caso de dados inválidos no JSON, ou código 400 se o ID informado não existir.
	 * 
	 * @param newStore - JSON que contém os dados para atualizar da Store.
	 * @param id - UUID da Store que se deseja atualizar.
	 * @return - Store ou ApiError.
	 */
	@PutMapping("/stores/{id}")
	public ResponseEntity<?> updateStore(@RequestBody Store newStore, @PathVariable String id) throws Exception {
		
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
	
	private ResponseEntity<?> handleError(HttpStatus status, String message) {
		return new ResponseEntity<>(new ApiError(status, message), status);
	}

}
