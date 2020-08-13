package com.qdm.cg.clients.service;

import java.util.List;

import com.qdm.cg.clients.dto.ClientsDTO;
import com.qdm.cg.clients.entity.Clients;

public interface ClientsService {

	Clients addClients(ClientsDTO clientsDTO);

	List<Clients> getClientsList();

}
