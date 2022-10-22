package com.iotshop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.iotshop.exception.DeviceNotFoundException;
import com.iotshop.exception.InvalidStatusException;
import com.iotshop.model.IoTDevice;
import com.iotshop.model.SimCard;
import com.iotshop.repository.IoTDeviceRepository;
import com.iotshop.repository.SimCardRepository;
import com.iotshop.util.Constants;

@Service
public class IoTShopService {

	@Autowired
	private IoTDeviceRepository iotRepository;
	
	@Autowired
	private SimCardRepository simRepository;
	
	public Optional<IoTDevice> findIotDeviceById(Long id) {
		return iotRepository.findById(id);
	}

	public Optional<SimCard> findSimCardById(Long id) {
		return simRepository.findById(id);
	}
	
	public Page<IoTDevice> findAllIotDevices(int page, int size) {
		
		Pageable resultPage = PageRequest.of(page, size);		
		return iotRepository.findAll(resultPage);
		
	}

	public Page<IoTDevice> findAvailableForSale(int page, int size) {
		
		Pageable resultPage = PageRequest.of(page, size);	
		return iotRepository.findByStatusAndTemperatureBetweenAndSimCardIsNotNullOrderById(Constants.IOTDEVICE_STATUS_READY, Constants.MIN_TEMPERATURE, Constants.MAX_TEMPERATURE, resultPage);
		
	}

	public Page<IoTDevice> findWaitingForActivation(int page, int size) {
	
		Pageable resultPage = PageRequest.of(page, size);
		return iotRepository.findBySimCard_StatusOrderById(Constants.SIM_STATUS_WFA, resultPage);
		
	}
	
	public IoTDevice updateDevice(IoTDevice device) {
		
		IoTDevice toBeUpdateddevice = iotRepository.findById(device.getId())
				.orElseThrow(() -> new DeviceNotFoundException("Device does not exists: " + device.getId()));
		
		if (device.getSimCard()!=null && device.getSimCard().getId()!=null) {
			SimCard sim = simRepository.findById(device.getSimCard().getId())
					.orElseThrow(() -> new DeviceNotFoundException("Sim card does not exists: " + device.getSimCard().getId()));
			toBeUpdateddevice.setSimCard(sim);
		} else {
			toBeUpdateddevice.setSimCard(null);
		}
		
		if (toBeUpdateddevice.getSimCard()==null && device.getStatus()!=null && device.getStatus().equals(Constants.IOTDEVICE_STATUS_READY)) {
			throw new InvalidStatusException("Device cannot have status "+Constants.IOTDEVICE_STATUS_READY+" whiout a SIM card assigned");
		}
		
		toBeUpdateddevice.setStatus(device.getStatus());
        
        return iotRepository.save(toBeUpdateddevice);			
		
	}		
	
	public IoTDevice deleteDevice(long id) {
		
		IoTDevice toBeDeletedddevice = iotRepository.findById(id)
				.orElseThrow(() -> new DeviceNotFoundException("Device does not exists: " + id));
		
		iotRepository.delete(toBeDeletedddevice);

        return toBeDeletedddevice;		
		
	}	
	
	public void save(IoTDevice iotDevice) {
		
		iotRepository.save(iotDevice);
		
	}
	
	public void delete(IoTDevice iotDevice) {
		
		iotRepository.delete(iotDevice);
		
	}
	
}
