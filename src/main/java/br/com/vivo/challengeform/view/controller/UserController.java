package br.com.vivo.challengeform.view.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import br.com.vivo.challengeform.dto.LevelDTO;
import br.com.vivo.challengeform.dto.TechnologyDTO;
import br.com.vivo.challengeform.entities.Level;
import br.com.vivo.challengeform.service.UserService;
import br.com.vivo.challengeform.view.model.LevelRequest;
import br.com.vivo.challengeform.view.model.UserRequest;
import br.com.vivo.challengeform.view.model.UserResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.vivo.challengeform.dto.UserDTO;

@RestController
@RequestMapping("/api")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * Retorna a lista de usuários completa.
	 * @return Lista de usuários.
	 */
	@GetMapping("/users")
	public ResponseEntity<List<UserResponse>> getUsers(){

		List<UserDTO> userDTOS = userService.getUsers();

		ModelMapper mapper = new ModelMapper();

		List<UserResponse> userResponses = userDTOS.stream()
				.map(userDTO -> mapper.map(userDTO,UserResponse.class))
				.collect(Collectors.toList());

		return new ResponseEntity<>(userResponses, HttpStatus.OK);
	}
	
	/**
	 * Retorna um usuário pelo seu Id.
	 * @param id do usuário que será localizado.
	 * @return Retorna um usuário pelo seu Id, caso sejá encontrado.
	 */
	@GetMapping("/user/{id}")
	public ResponseEntity<Optional<UserResponse>> getUserById(@PathVariable long id) {
		Optional<UserDTO> userDTO = userService.getUserById(id);

		UserResponse user = new ModelMapper().map(userDTO.get(), UserResponse.class);

		return new ResponseEntity<>(Optional.of(user), HttpStatus.OK);
	}
	
	/**
	 * Adiciona um usuário no banco de dados, gerando um novo Id para ele.
	 *
	 * @param userRequest Usuário que será adicionado no banco de dados.
	 * @return Usuário que foi adicionado.
	 */
	@PostMapping("/add-user")
	@ResponseBody
	public ResponseEntity<UserResponse> addUser(@Valid @RequestBody UserRequest userRequest) {

		ModelMapper mapper =new ModelMapper();
		UserDTO userDTO = mapper.map(userRequest, UserDTO.class);

		userDTO =  userService.addUser(userDTO);

		return new ResponseEntity<>(mapper.map(userDTO, UserResponse.class), HttpStatus.CREATED);
	}

	/**
	 * Envia um email para um usuário.
	 * @param user Usuário que receberá o email.
	 * @return Retorna um ResponseEntity ok, com a mensagem "Email enviado com sucesso para: EmailDoUsuário", indicando que o email foi enviado com sucesso para o usuário.
	 */
	@PostMapping("/send-email")
	@ResponseBody
	public ResponseEntity<?> sendEmail(@Valid @RequestBody UserDTO user) {
		userService.sendEmailToUser(user);
		return ResponseEntity.ok("Email enviado com sucesso para: " + user.getEmail());
	}
	
	/**
	 * Remove um usuário do banco de dados pelo Id.
	 * @param id do usuário que será removido.
	 */
	@DeleteMapping("/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable long id) {
		userService.deleteUser(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
}
