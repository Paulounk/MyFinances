package com.finances.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finances.dto.EntryDTO;
import com.finances.dto.UpdateStatusDTO;
import com.finances.exception.BusinessRuleException;
import com.finances.model.Entry;
import com.finances.model.User;
import com.finances.model.enums.EntryStatus;
import com.finances.model.enums.EntryType;
import com.finances.service.EntryService;
import com.finances.service.UserService;

@RestController
@RequestMapping("/entries")
@CrossOrigin("*")
public class EntryController {

	@Autowired
	private EntryService service;
	
	@Autowired
	private UserService userService;

	/*@GetMapping
	public ResponseEntity<List<Entry>> getAll(){
		return ResponseEntity.ok(repository.findAll());
	}*/
	
	@GetMapping
	public ResponseEntity<List<Entry>> search(
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "month", required = false) Integer month,
			@RequestParam(value = "year", required = false) Integer year,
			@RequestParam("user") Long idUser
			){
		
		Entry entryFilter = new Entry();

		entryFilter.setDescription(description);
		entryFilter.setMonth(month);
		entryFilter.setYear(year);
		entryFilter.setDateRegister(null);

		Optional<User> user = userService.getUserById(idUser);
		
		if(!user.isPresent()) {
			return new ResponseEntity("It was not possible to perform a query. User not found.",HttpStatus.BAD_REQUEST);
		}else {
			entryFilter.setUser(user.get());
		}
		
		List<Entry> entries = service.search(entryFilter);

		return ResponseEntity.ok(entries);
		
	}
	
	
	@GetMapping("{id}")
	public ResponseEntity<Entry> getById(@PathVariable Long id){
		
		return service.getEntryById(id)
				.map(entry -> new ResponseEntity(convertDTO(entry), HttpStatus.OK))
				.orElseGet( () -> new ResponseEntity<Entry>(HttpStatus.NOT_FOUND));
		
	}
	
	@PostMapping
	public ResponseEntity<?> saveEntry(@RequestBody EntryDTO dto) {
		
		try {
			Entry entity = convert(dto);
			entity = service.save(entity);
			return new ResponseEntity(entity, HttpStatus.CREATED);
		}catch (BusinessRuleException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	} 
	
	
	@PutMapping("{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody EntryDTO dto) {
		
		return service.getEntryById(id).map(resp -> {
			try {
				Entry entry = convert(dto);
				entry.setId(resp.getId());
				service.update(entry);
				return ResponseEntity.ok(entry);
			}catch(BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> 
			new ResponseEntity("Entry not found in database for update", HttpStatus.BAD_REQUEST));
	}
	
	@PutMapping("{id}/update-status")
	public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody UpdateStatusDTO dto) {
		
		return service.getEntryById(id).map(resp -> {
			
			EntryStatus selectStatus = EntryStatus.valueOf(dto.getStatus());
			
			if(selectStatus == null) {
				return ResponseEntity.badRequest().body("Unable to update release status. Enter a valid status!");
			}
			
			try {
				resp.setStatus(selectStatus);
				service.update(resp);
				return ResponseEntity.ok(resp);
			}catch(BusinessRuleException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
			
		}).orElseGet(() -> 
			new ResponseEntity("Entry not found in database for update.", HttpStatus.BAD_REQUEST));
		
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		
		return service.getEntryById(id).map(resp -> {
			service.delete(resp);
			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(() -> 
			new ResponseEntity("Entry not found in database for update", HttpStatus.BAD_REQUEST));
	}
	
	private Entry convert(EntryDTO dto) {
		
		Entry entry = new Entry();
		
		entry.setId(dto.getId());
		entry.setDescription(dto.getDescription());
		entry.setYear(dto.getYear());
		entry.setMonth(dto.getMonth());
		entry.setValue(dto.getValue());
		
		User user = userService.getUserById(dto.getUser())
		.orElseThrow(() -> new BusinessRuleException("User not found for entered ID."));
		
		entry.setUser(user);
		
		if(dto.getType() != null){
			entry.setType(EntryType.valueOf(dto.getType()));
		}
		if(dto.getStatus() != null) {
			entry.setStatus(EntryStatus.valueOf(dto.getStatus()));
		}
		return entry;
	}
	
	
	private EntryDTO convertDTO(Entry entry) {
		
		return EntryDTO.builder()
				.id(entry.getId())
				.description(entry.getDescription())
				.value(entry.getValue())
				.month(entry.getMonth())
				.year(entry.getYear())
				.status(entry.getStatus().name())
				.type(entry.getType().name())
				.build();
		
	}
}
