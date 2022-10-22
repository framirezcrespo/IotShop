package com.iotshop.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.iotshop.exception.DeviceNotFoundException;
import com.iotshop.model.IoTDevice;
import com.iotshop.service.IoTShopService;

@RestController
public class IoTDeviceController {

	@Autowired
	private IoTShopService shopService;

	@GetMapping(value="/iotdevices/{id}")
	IoTDevice getDevice(@PathVariable Long id, UriComponentsBuilder uriBuilder,
			  HttpServletResponse response) {
		
		return shopService.findIotDeviceById(id)
				.orElseThrow(() -> new DeviceNotFoundException("Device does not exists: " + id));
	}	
	
	@GetMapping(value="/iotdevices",params = { "page", "size" })
	Page<IoTDevice> allPaginated(@RequestParam("page") int page, 
			  @RequestParam("size") int size, UriComponentsBuilder uriBuilder,
			  HttpServletResponse response) {
		
		return shopService.findAllIotDevices(page, size);
	}	
	
	@GetMapping(value="/iotdevices/available",params = { "page", "size" })
	Page<IoTDevice> allAvailableForSale(@RequestParam("page") int page, 
			  @RequestParam("size") int size, UriComponentsBuilder uriBuilder,
			  HttpServletResponse response) {
		
		return shopService.findAvailableForSale(page, size);
	}
	
	@GetMapping(value="/iotdevices/foractivation",params = { "page", "size" })
	Page<IoTDevice> waitingForActivation(@RequestParam("page") int page, 
			  @RequestParam("size") int size, UriComponentsBuilder uriBuilder,
			  HttpServletResponse response) {
				
		return shopService.findWaitingForActivation(page, size);
	}
	
	@PutMapping("/iotdevices")
	public ResponseEntity<IoTDevice> updateDevice(@RequestBody IoTDevice device) {
		
		IoTDevice upDatedDevce = shopService.updateDevice(device);
		return ResponseEntity.ok(upDatedDevce);
	}	

	@DeleteMapping("/iotdevices/{id}")
	public ResponseEntity<IoTDevice> deleteDevice(@PathVariable long id) {
		
		IoTDevice toBeDeletedddevice = shopService.deleteDevice(id);
        return ResponseEntity.ok(toBeDeletedddevice);		
		
	}	
	
}
