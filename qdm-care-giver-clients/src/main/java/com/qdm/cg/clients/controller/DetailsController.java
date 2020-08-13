package com.qdm.cg.clients.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qdm.cg.clients.entity.Clients;
import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.repository.SubscriptionsRepository;
import com.qdm.cg.clients.response.ResponseInfo;
import com.qdm.cg.clients.response.ResponseType;
import com.qdm.cg.clients.service.DetailsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/details" })
@Slf4j
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class DetailsController {
	@Autowired
	DetailsService detailsService;

	@Autowired
	SubscriptionsRepository subscriptionsRepository;

	@GetMapping(value = "/getClientByClientId/{clientid}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getClientByClientId(@PathVariable("clientid") Integer clientid) {
		ResponseEntity response = null;
		try {
			Clients clientsData = detailsService.getClientByClientId(clientid);
			if (clientsData != null) {
				List<Object> subscriptionList = new ArrayList<Object>();
				for (Integer subId : clientsData.getDetails().getSubscriptions()) {
					Subscriptions subscription = subscriptionsRepository.findBySubscriptionId(subId);
					subscriptionList.add(subscription);
				}
				Map<String, Object> clientDetails = new HashMap();
				clientDetails.put("clientName", clientsData.getClientName());
				clientDetails.put("gender", clientsData.getGender());
				clientDetails.put("age", clientsData.getAge());
				clientDetails.put("details", clientsData.getDetails());
				clientDetails.put("subscription", subscriptionList);

				log.info("ClientById " + clientDetails);
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", clientDetails), HttpStatus.OK);
				return response;
			} else {
				log.info("No Clients Found with Id :  " + clientid);
				response = new ResponseEntity(new ResponseInfo(ResponseType.NOT_FOUND.getResponseMessage(),
						ResponseType.NOT_FOUND.getResponseCode(), "", null), HttpStatus.NOT_FOUND);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At getClientByClientId : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@PutMapping(value = "/updateClientSubscrptions", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> updateClientSubscriptions(@RequestParam("clientId") Integer clientId,
			@RequestParam("subscriptionList") Set<Integer> subscriptionList) {

		ResponseEntity response = null;
		if (clientId == 0) {
			log.error("Client Id is Empty");
			response = new ResponseEntity(new ResponseInfo(ResponseType.BAD_REQUEST.getResponseMessage(),
					ResponseType.BAD_REQUEST.getResponseCode(), "", null), HttpStatus.BAD_REQUEST);
			return response;
		}
		try {
			Clients clients = detailsService.updateClientSubscriptions(clientId, subscriptionList);
			if (clients != null) {
				log.info("Client Updated Successfully with Id " + clients.getId());
				response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
						ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.OK);
				return response;
			} else {
				log.info("No Clients Found with Id :  " + clientId);
				response = new ResponseEntity(new ResponseInfo(ResponseType.NOT_FOUND.getResponseMessage(),
						ResponseType.NOT_FOUND.getResponseCode(), "", null), HttpStatus.NOT_FOUND);
				return response;
			}
		} catch (Exception e) {
			log.error("Error Occured At UpdateClientSubscriptions : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

}
