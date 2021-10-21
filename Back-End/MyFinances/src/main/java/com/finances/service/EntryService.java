package com.finances.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;

import com.finances.model.Entry;
import com.finances.model.enums.EntryStatus;

public interface EntryService {

	Entry save(Entry entry);
	Entry update(Entry entry);
	
	List<Entry> search(Entry entryFilter);
	
	void delete(Entry entry);
	void updateStatus(Entry entry, EntryStatus status);
	void validate(Entry entry);
	
	Optional<Entry> getEntryById(Long id);
	
	BigDecimal getBalanceByUser(Long id);
} 
