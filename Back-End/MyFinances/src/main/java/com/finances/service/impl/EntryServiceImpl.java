package com.finances.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finances.exception.BusinessRuleException;
import com.finances.model.Entry;
import com.finances.model.enums.EntryStatus;
import com.finances.repository.EntryRepository;
import com.finances.service.EntryService;

@Service
public class EntryServiceImpl implements EntryService{

	private EntryRepository repository;
	
	public EntryServiceImpl(EntryRepository repository) {
		this.repository = repository;
	}
	
	@Override
	@Transactional
	public Entry save(Entry entry) {

		validate(entry);
		entry.setStatus(EntryStatus.PENDING);
		return repository.save(entry);
	}

	@Override
	@Transactional
	public Entry update(Entry entry) {

		Objects.requireNonNull(entry.getId());
		validate(entry);
		entry.setStatus(EntryStatus.PENDING);
		return repository.save(entry);
	}
	
	@Override
	@Transactional
	public void delete(Entry entry) {
		 
		Objects.requireNonNull(entry.getId());
		repository.delete(entry);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Entry> search(Entry entryFilter) {
		
		Example<Entry> example = Example.of(entryFilter,
			ExampleMatcher.matching()
			.withIgnoreCase()
			.withStringMatcher(StringMatcher.CONTAINING));
		
		return repository.findAll(example);

	}

	@Override
	public void updateStatus(Entry entry, EntryStatus status) {
		entry.setStatus(status);
		update(entry);
	}

	@Override
	public void validate(Entry entry) {
		
		if(entry.getDescription() == null || entry.getDescription().trim().equals("")) {
			throw new BusinessRuleException("Enter a valid description.");
		}
		
		if(entry.getMonth() == null || entry.getMonth() < 1 ||  entry.getMonth() > 12) {
			throw new BusinessRuleException("Enter a valid month.");
		}
		
		if(entry.getYear() == null || entry.getYear().toString().length() != 4) {
			throw new BusinessRuleException("Enter a valid year.");
		}
		
		if(entry.getUser() == null || entry.getUser().getId() == null) {
			throw new BusinessRuleException("Enter a valid user.");
		}
		
		if(entry.getValue() == null || entry.getValue().compareTo(BigDecimal.ZERO) < 1) {
			throw new BusinessRuleException("Enter a value greater than zero.");
		}
		
		if(entry.getType() == null) {
			throw new BusinessRuleException("Enter a release type.");
		}
		
	}

	@Override
	public Optional<Entry> getEntryById(Long id) {
		
		return repository.findById(id);
	}

}
