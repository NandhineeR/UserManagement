package com.qdm.cg.clients.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.qdm.cg.clients.dto.ClientsDTO;
import com.qdm.cg.clients.entity.Clients;
import com.qdm.cg.clients.response.ResponseInfo;
import com.qdm.cg.clients.response.ResponseType;
import com.qdm.cg.clients.service.ClientsService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/clients" })
@Slf4j
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class ClientsController {

	@Autowired
	ClientsService clientsService;

	@PostMapping(value = "/addClient", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> addClients(@RequestBody ClientsDTO clientsDTO) {

		ResponseEntity response = null;
		try {
			Clients clients = clientsService.addClients(clientsDTO);
			log.info("Client Created Successfully With ClientId : " + clients.getId());
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", null), HttpStatus.CREATED);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At Adding Client : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "Try Again", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}
	}

	@GetMapping(value = "/getClientsList", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getClientsList() {
		ResponseEntity response = null;
		try {
			List<Clients> clients = clientsService.getClientsList();
			List<Object> list = new ArrayList<Object>();
			for (Clients clientsData : clients) {
				Map<String, Object> map = new HashMap();
				map.put("clientName", clientsData.getClientName());
				map.put("gender", clientsData.getGender());
				map.put("age", clientsData.getAge());
				list.add(map);
			}
			log.info("Clients List " + list);
			response = new ResponseEntity(new ResponseInfo(ResponseType.SUCCESS.getResponseMessage(),
					ResponseType.SUCCESS.getResponseCode(), "", list), HttpStatus.OK);
			return response;
		} catch (Exception e) {
			log.error("Error Occured At getClientsLists : " + e.getMessage());
			response = new ResponseEntity(new ResponseInfo(ResponseType.ERROR.getResponseMessage(),
					ResponseType.ERROR.getResponseCode(), "", null), HttpStatus.INTERNAL_SERVER_ERROR);
			return response;
		}

	}

}
