package com.qdm.cg.clients.serviceimpl;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qdm.cg.clients.entity.Clients;
import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.repository.ClientsRepository;
import com.qdm.cg.clients.repository.SubscriptionsRepository;
import com.qdm.cg.clients.service.DetailsService;

@Service
@Transactional
public class DetailsServiceImpl implements DetailsService {

	ClientsRepository clientsRepository;
	SubscriptionsRepository subscriptionsRepository;
	ModelMapper modelMapper;

	@Autowired
	public DetailsServiceImpl(ClientsRepository clientsRepository, SubscriptionsRepository subscriptionsRepository,
			ModelMapper modelMapper) {
		super();
		this.clientsRepository = clientsRepository;
		this.subscriptionsRepository = subscriptionsRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public Clients getClientByClientId(Integer clientid) {
		Optional<Clients> client = clientsRepository.findById(clientid);
		if (client.isPresent()) {
			return client.get();
		} else {
			return null;
		}
	}

	@Override
	public Clients updateClientSubscriptions(Integer clientId, Set<Integer> subscriptionList) {
		Optional<Clients> client = clientsRepository.findById(clientId);
		Set<Integer> subscriptionsList = new HashSet<>();
		if (client.isPresent()) {
			for (Integer subscriptions : client.get().getDetails().getSubscriptions()) {
				subscriptionsList.add(subscriptions);
			}
		} else {
			return null;
		}
		for (Integer subscriptionListId : subscriptionList) {
			Subscriptions sub = subscriptionsRepository.findBySubscriptionId(subscriptionListId);
			subscriptionsList.add(sub.getSubscriptionId());
		}
		client.get().getDetails().setSubscriptions(subscriptionsList);
		return clientsRepository.save(client.get());
	}

}
