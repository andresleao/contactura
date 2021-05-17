package com.contactura.contactura.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.contactura.contactura.model.Contactura;
import com.contactura.contactura.repository.ContacturaRepository;

@RestController
@RequestMapping({"/contactura"})
public class ContacturaController {
	
	@Autowired // Possibilita o acesso aos métodos da interface repository
	private ContacturaRepository repository;
	
	// Listar todos os contatos - http://localhost:8000/contactura
	@GetMapping
	public List findAll() {
		return repository.findAll();
	}
	
	// Pesquisar pelo ID - http://localhost:8090/contactura/{id}
	@GetMapping(value = "/{id}")
	public ResponseEntity findById(@PathVariable long id) {
		return repository.findById(id)
			.map(record -> ResponseEntity.ok().body(record))
			.orElse(ResponseEntity.notFound().build());
	}
	
	// Criar um novo contato - http://localhost:8090/contactura
	@PostMapping
	public Contactura create(@RequestBody Contactura contactura) {
		return repository.save(contactura);
	}
	
	// Atualizar contato  - http://localhost:8090/contactura/id
	@PutMapping(value = "/{id}")
	public ResponseEntity update(@PathVariable long id, @RequestBody Contactura contactura) {
		return repository.findById(id)
				.map(record ->{
					record.setName(contactura.getName());
					record.setEmail(contactura.getEmail());
					record.setPhone(contactura.getPhone());
					Contactura update = repository.save(record);
					return ResponseEntity.ok().body(update);
				}).orElse(ResponseEntity.notFound().build());
	}
	
	// Deletar contato - http://localhost:8090/contactura/id
	@DeleteMapping(path = {"/{id}"})
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity delete(@PathVariable long id) {
		return repository.findById(id)
				.map(record->{
					repository.deleteById(id);
					return ResponseEntity.ok().body("Deletado com sucesso");
				}).orElse(ResponseEntity.notFound().build());
	}
	
	
	
}
