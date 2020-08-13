package com.qdm.cg.clients.entity;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({ "subscriptions" })
@Table(name="TB_DETAILS")
public class Details {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String email;
	private long contactNumber;
	private long emergencyContactNumber;

	// Vital Signs
	private String bodyTemperature;
	private String bloodPressure;
	private String respirationRate;
	private String pulseRate;

	// Subscriptions
	@ElementCollection
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "subscriptionId")
	@JoinTable(name = "TB_SUBSCRIPTION")
	private Set<Integer> subscriptions;
}
