package com.qdm.cg.clients.dto;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.springframework.stereotype.Component;

import com.qdm.cg.clients.enums.Gender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ClientsDTO {

	private String clientName;
	private Gender gender;
	private int age;
	Details details;

}
