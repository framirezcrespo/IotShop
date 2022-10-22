package com.iotshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iotshop.model.SimCard;

public interface SimCardRepository extends JpaRepository<SimCard, Long> {

}
