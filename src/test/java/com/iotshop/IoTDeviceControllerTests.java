package com.iotshop;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import com.iotshop.controller.IoTDeviceController;

@SpringBootTest
class IoTDeviceControllerTests {

	@InjectMocks
	private IoTDeviceController iotController;
	
	@Test
	public void contextLoads() throws Exception {
		assertThat(iotController).isNotNull();
	}	

}
