package com.finances.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finances.model.Entry;
import com.finances.model.enums.EntryType;

public interface EntryRepository extends JpaRepository<Entry, Long>{

	/* select sum(tb_entries.value) from tb_entries inner join tb_user on tb_user.id = tb_entries.user_id 
	   where tb_entries.type = 'EXPENDITURE' group by tb_user; */
	
	
	@Query(value = 
				" select sum(l.value) from Entry l join l.user u "
			  + " where u.id = :idUser and l.type = :type group by u " )
	BigDecimal getBalanceByTypeEntryAndUser(
			@Param("idUser") Long idUser, 
			@Param("type") EntryType type);
}
