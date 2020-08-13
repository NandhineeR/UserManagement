package com.qdm.cg.clients.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qdm.cg.clients.dto.SubscriptionsDTO;
import com.qdm.cg.clients.entity.Subscriptions;
import com.qdm.cg.clients.response.ResponseInfo;
import com.qdm.cg.clients.response.ResponseType;
import com.qdm.cg.clients.service.SubscriptionsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/subscription" })

@Slf4j
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class SubscriptionsController {
	@Autowired
	SubscriptionsService subscriptionsService;

	@PostMapping(value = "/addSubscriptions", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> addSubscriptions(@RequestBody SubscriptionsDTO subscriptionsDTO) {

		ResponseEntity response = null;
		try {
			Subscriptions subscription = subscriptionsService.addSubscriptions(subscriptionsDTO);
			log.info("Subscription Created Successfully With subscriptionId : " + subscription.getSubscriptionId());
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.CREATED);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At AddSubscription : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/getSubscription", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getSubscriptions() {
		ResponseEntity response = null;
		try {
			List<Subscriptions> subscriptions = subscriptionsService.getSubscriptions();
			log.info("Care Givers List " + subscriptions);
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", subscriptions), HttpStatus.OK);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At getSubscriptionsList : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

}
