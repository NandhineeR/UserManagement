package com.qdm.cg.clients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qdm.cg.clients.entity.Subscriptions;


@Repository
public interface SubscriptionsRepository extends JpaRepository<Subscriptions, Integer>{

	Subscriptions findBySubscriptionId(Integer subscriptionListId);

	
}
