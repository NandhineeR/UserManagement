package com.qdm.cg.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qdm.cg.clients.entity.Clients;

@Repository
public interface ClientsRepository extends JpaRepository<Clients, Integer>{


}
