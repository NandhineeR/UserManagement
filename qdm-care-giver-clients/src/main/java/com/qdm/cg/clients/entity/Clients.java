package com.qdm.cg.clients.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.qdm.cg.clients.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="TB_CLIENTS")
public class Clients {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String clientName;
	@Enumerated(EnumType.STRING)
	
	private Gender gender;
	
	private int age;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "details_id")
	private Details details;
	

}
