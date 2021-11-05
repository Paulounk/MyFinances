package com.finances.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finances.model.Entry;
import com.finances.model.enums.EntryStatus;
import com.finances.model.enums.EntryType;

@Repository
public interface EntryRepository extends JpaRepository<Entry, Long>{

	/* select sum(tb_entries.value) from tb_entries inner join tb_user on tb_user.id = tb_entries.user_id 
	   where tb_entries.type = 'EXPENDITURE' group by tb_user; */
	
	@Query(value = 
				" select sum(l.value) from Entry l join l.user u "
			  + " where u.id = :idUser and l.type = :type and l.status = :status group by u " )
	BigDecimal getBalanceByTypeEntryAndUserAndStatus(
			@Param("idUser") Long idUser, 
			@Param("type") EntryType type,
			@Param("status") EntryStatus status);
	

}
