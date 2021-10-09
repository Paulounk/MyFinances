package com.finances.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finances.model.Entries;

public interface EntriesRepository extends JpaRepository<Entries, Long>{

}
