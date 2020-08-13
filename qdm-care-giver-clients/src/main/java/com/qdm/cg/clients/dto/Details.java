package com.qdm.cg.clients.dto;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Details {
	private String email;
	private long contactNumber;
	private long emergencyContactNumber;

	// Vital Signs
	private String bodyTemperature;
	private String bloodPressure;
	private String respirationRate;
	private String pulseRate;

	// Subscriptions
	private List<Integer> subscriptions;

}
