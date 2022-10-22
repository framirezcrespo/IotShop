package com.iotshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.iotshop.model.IoTDevice;

public interface IoTDeviceRepository extends PagingAndSortingRepository<IoTDevice, Long> {

	Page<IoTDevice> findByStatusOrderById(String status, Pageable resultPage);
	
	Page<IoTDevice> findBySimCard_StatusOrderById(String status, Pageable resultPage);
	
	Page<IoTDevice> findByStatusAndTemperatureBetweenAndSimCardIsNotNullOrderById(String status, Double startTemperature, Double endTemperature, Pageable resultPage);
}
