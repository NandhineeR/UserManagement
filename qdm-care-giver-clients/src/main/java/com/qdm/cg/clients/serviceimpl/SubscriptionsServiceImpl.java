package com.qdm.cg.clients.serviceimpl;

import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qdm.cg.clients.dto.SubscriptionsDTO;
import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.repository.SubscriptionsRepository;
import com.qdm.cg.clients.service.SubscriptionsService;

@Service
@Transactional
public class SubscriptionsServiceImpl implements SubscriptionsService {

	@Autowired
	SubscriptionsRepository subscriptionsRepository;
	
	@Autowired
	ModelMapper modelMapper;

	@Override
	public Subscriptions addSubscriptions(SubscriptionsDTO subscriptionsDTO) {
		Subscriptions subscription = modelMapper.map(subscriptionsDTO, Subscriptions.class);
		return subscriptionsRepository.save(subscription);
	}

	@Override
	public List<Subscriptions> getSubscriptions() {
		return subscriptionsRepository.findAll();
	}

}
