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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.finances.model.enums.EntryType;
import com.finances.model.enums.EntryStatus;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name = "tb_entries")
@Data
@Builder
public class Entries {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;
	
	@NotBlank
	@Size(max = 150)
	private String description;
	
	@NotBlank
	private Integer month;
	
	@NotBlank
	private Integer year;
	
	@NotBlank
	private BigDecimal value;
	
	@NotBlank
	@Enumerated(value = EnumType.STRING)
	private EntryType type;
	
	@NotBlank
	@Enumerated(value = EnumType.STRING)
	private EntryStatus status;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateRegister = new java.sql.Date(System.currentTimeMillis());
	
	@ManyToOne
    @JsonIgnoreProperties("entries")
    private User user;

}
