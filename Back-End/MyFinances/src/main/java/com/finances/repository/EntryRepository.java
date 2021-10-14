package com.finances.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finances.model.Entry;

public interface EntryRepository extends JpaRepository<Entry, Long>{

}
