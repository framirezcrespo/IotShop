package com.iotshop;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.iotshop.exception.DeviceNotFoundException;
import com.iotshop.exception.InvalidStatusException;
import com.iotshop.model.IoTDevice;
import com.iotshop.model.SimCard;
import com.iotshop.repository.IoTDeviceRepository;
import com.iotshop.repository.SimCardRepository;
import com.iotshop.service.IoTShopService;
import com.iotshop.util.Constants;

@SpringBootTest
class IoTShopServiceTest {

	@Mock
	private IoTDeviceRepository iotRepository;

	@Mock
	private SimCardRepository simRepository;

	@InjectMocks
	private IoTShopService shopService;
	
	@Mock
	Page<IoTDevice> iotPage;
	
	@Test
	public void findIotDeviceByIdTest() {
		
		IoTDevice device = new IoTDevice();
		device.setId(55l);
		
		Optional<IoTDevice> deviceOp = Optional.of(device);
		
		Mockito.when(iotRepository.findById(Mockito.anyLong())).thenReturn(deviceOp);
		
		Optional<IoTDevice> devideFound = shopService.findIotDeviceById(55l);
		
		assertTrue(devideFound.isPresent());
		assertTrue(devideFound.get().getId().equals(55l));
		
	}	
	
	@Test
	public void findAllIotDevicesTest() {
				
		Mockito.when(iotRepository.findAll(ArgumentMatchers.isA(Pageable.class))).thenReturn(iotPage);
		shopService.findAllIotDevices(0, 10);
		
	}

	@Test
	public void findAvailableForSaleTest() {
				
		Mockito.when(iotRepository.findByStatusAndTemperatureBetweenAndSimCardIsNotNullOrderById(Mockito.anyString(), Mockito.anyDouble(), Mockito.any(), Mockito.any())).thenReturn(iotPage);
		shopService.findAvailableForSale(0, 10);
		
	}

	@Test
	public void findWaitingForActivationTest() {
				
		Mockito.when(iotRepository.findBySimCard_StatusOrderById(Mockito.anyString(), Mockito.any())).thenReturn(iotPage);
		shopService.findAvailableForSale(0, 10);
		
	}

	@Test
	public void updateDeviceTest() {
				
		IoTDevice device = new IoTDevice();
		device.setId(55l);
		
		SimCard sim = new SimCard();
		sim.setId(11l);
		
		Optional<IoTDevice> deviceOp = Optional.of(device);
		Optional<SimCard> simOp = Optional.of(sim);
		
		Mockito.when(iotRepository.findById(Mockito.anyLong())).thenReturn(deviceOp);		
		Mockito.when(iotRepository.save(Mockito.any())).thenReturn(device);
		Mockito.when(simRepository.findById(Mockito.anyLong())).thenReturn(simOp);
		
		IoTDevice savedDevice = shopService.updateDevice(device);
		
		assertTrue(savedDevice.getId().equals(55L));
	}
	
	
	@Test
	public void updateDeviceNotFoundErrorTest() {
				
		IoTDevice device = new IoTDevice();
		device.setId(66l);
		
		Mockito.doThrow(new DeviceNotFoundException("Device does not exists: 66"))
			.when(iotRepository).findById(Mockito.anyLong());
			
	    Exception exception = assertThrows(DeviceNotFoundException.class, () -> {
	    	IoTDevice savedDevice = shopService.updateDevice(device);
	    });
	    
	    String message = exception.getMessage();
	    assertTrue(message.contains("Device does not exists: 66"));
	    
	}
	
	@Test
	public void updateDeviceSimNotFoundErrorTest() {
			
		SimCard sim = new SimCard();
		sim.setId(11l);

		IoTDevice device = new IoTDevice();
		device.setId(77l);
		device.setSimCard(sim);
					
		Optional<IoTDevice> deviceOp = Optional.of(device);
		
		Mockito.when(iotRepository.findById(Mockito.anyLong())).thenReturn(deviceOp);		
		
		Mockito.doThrow(new DeviceNotFoundException("Sim card does not exists: 11"))
			.when(simRepository).findById(Mockito.anyLong());
			
	    Exception exception = assertThrows(DeviceNotFoundException.class, () -> {
	    	IoTDevice savedDevice = shopService.updateDevice(device);
	    });
	    
	    String message = exception.getMessage();
	    assertTrue(message.contains("Sim card does not exists: 11"));
	    
	}
	
	@Test
	public void updateDeviceInvalidStatusErrorTest() {
			
		IoTDevice device = new IoTDevice();
		device.setStatus(Constants.IOTDEVICE_STATUS_READY);
		device.setId(77l);
		device.setSimCard(null);
					
		Optional<IoTDevice> deviceOp = Optional.of(device);
		
		Mockito.when(iotRepository.findById(Mockito.anyLong())).thenReturn(deviceOp);		
		
		Mockito.doThrow(new DeviceNotFoundException("Sim card does not exists: 11"))
			.when(simRepository).findById(Mockito.anyLong());
			
	    Exception exception = assertThrows(InvalidStatusException.class, () -> {
	    	IoTDevice savedDevice = shopService.updateDevice(device);
	    });
	    
	    String message = exception.getMessage();
	    assertTrue(message.contains("Device cannot have status"));
	    
	}		

	@Test
	public void deleteDeviceTest() {
			
		IoTDevice device = new IoTDevice();
		device.setId(88l);
		
		Optional<IoTDevice> deviceOp = Optional.of(device);
		
		Mockito.when(iotRepository.findById(Mockito.anyLong())).thenReturn(deviceOp);
		Mockito.when(iotRepository.save(Mockito.any())).thenReturn(deviceOp);
		
    	IoTDevice deletedDevice = shopService.deleteDevice(device.getId());
	    
    	assertTrue(deletedDevice.getId().equals(88L));
	}		

	@Test
	public void deleteDeviceNotFoundErrorTest() {
			
		IoTDevice device = new IoTDevice();
		device.setId(99l);
		
		Mockito.doThrow(new DeviceNotFoundException("Device does not exists: 99"))
		.when(iotRepository).findById(Mockito.anyLong());
		
	    Exception exception = assertThrows(DeviceNotFoundException.class, () -> {
	    	IoTDevice deletedDevice = shopService.deleteDevice(device.getId());
	    });
	    
	    String message = exception.getMessage();
	    assertTrue(message.contains("Device does not exists: 99"));
	}		
	
	
}
