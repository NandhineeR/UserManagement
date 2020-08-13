package com.qdm.cg.clients.service;

import java.util.Set;

import com.qdm.cg.clients.entity.Clients;

public interface DetailsService {
	Clients getClientByClientId(Integer clientid);

	Clients updateClientSubscriptions(Integer clientId, Set<Integer> subscriptionList);

}
