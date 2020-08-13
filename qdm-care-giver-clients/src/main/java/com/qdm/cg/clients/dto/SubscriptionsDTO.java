package com.qdm.cg.clients.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class SubscriptionsDTO {
	private int subscriptionId;
	private String subscriptionName;
}
