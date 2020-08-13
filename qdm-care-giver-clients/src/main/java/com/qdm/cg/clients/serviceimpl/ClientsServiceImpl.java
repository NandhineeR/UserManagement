package com.qdm.cg.clients.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qdm.cg.clients.dto.ClientsDTO;
import com.qdm.cg.clients.entity.Clients;
import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.repository.ClientsRepository;
import com.qdm.cg.clients.repository.SubscriptionsRepository;
import com.qdm.cg.clients.service.ClientsService;

@Service
@Transactional
public class ClientsServiceImpl implements ClientsService {

	ClientsRepository clientsRepository;
	SubscriptionsRepository subscriptionsRepository;
	ModelMapper modelMapper;

	@Autowired
	public ClientsServiceImpl(ClientsRepository clientsRepository, SubscriptionsRepository subscriptionsRepository,
			ModelMapper modelMapper) {
		super();
		this.clientsRepository = clientsRepository;
		this.subscriptionsRepository = subscriptionsRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public Clients addClients(ClientsDTO clientsDTO) {

		Clients careGiver = modelMapper.map(clientsDTO, Clients.class);
		List<Subscriptions> subscriptionsList = new ArrayList<>();
		return clientsRepository.save(careGiver);

	}

	@Override
	public List<Clients> getClientsList() {
		return clientsRepository.findAll();
	}

}
