package com.finances.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finances.model.enums.EntryType;
import com.finances.model.enums.EntryStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "tb_entries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Entry {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	
	@Size(max = 150)
	private String description;
	
	private Integer month;

	private Integer year;
	
	private BigDecimal value;
	
	@Enumerated(value = EnumType.STRING)
	private EntryType type;
	
	@Enumerated(value = EnumType.STRING)
	private EntryStatus status;
	
	@Builder.Default
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateRegister = new java.sql.Date(System.currentTimeMillis());
	
	@ManyToOne
    @JsonIgnoreProperties("entries")
    private User user;
	
	

}
