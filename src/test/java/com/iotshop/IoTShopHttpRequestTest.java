package com.iotshop;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import com.iotshop.model.IoTDevice;
import com.iotshop.model.SimCard;
import com.iotshop.util.Constants;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class IoTShopHttpRequestTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;	
	
	@Test
	public void getByIdOK() throws Exception {
		assertThat(restTemplate.getForObject("http://localhost:" + port + "/iotdevices/32",
				String.class)).contains("{\"id\":32,\"simCard\":{\"id\":550,\"operatorCode\":3,\"country\":\"Netherlands\",\"status\":\" Deactivated\"},\"status\":\"READY\",\"temperature\":81.0}");
	}

	@Test
	public void getByIdNotFoud() throws Exception {
		assertThat(restTemplate.getForObject("http://localhost:" + port + "/iotdevices/9999",
				String.class)).contains("Device does not exists");
	}

	@Test
	public void getAllPaginatedOk() throws Exception {
		assertThat(restTemplate.getForObject("http://localhost:" + port + "/iotdevices?page=0&size=5",
				String.class)).contains("\"numberOfElements\":5");
	}

	@Test
	public void getAvailableOk() throws Exception {

		String page = restTemplate.getForObject("http://localhost:" + port + "/iotdevices/available?page=0&size=5",String.class);
		int pos=0;
		Integer count=0;
		while (pos>=0) {
			pos=page.indexOf(Constants.IOTDEVICE_STATUS_READY,pos+1);
			if (pos>=0) count++;
		}
		assertThat(count).isEqualTo(5);

	}

	@Test
	public void waitingForActivationOk() throws Exception {
		
		String page = restTemplate.getForObject("http://localhost:" + port + "/iotdevices/foractivation?page=0&size=5",String.class);
		int pos=0;
		int count=0;
		while (pos>=0) {
			pos=page.indexOf(Constants.SIM_STATUS_WFA,pos+1);
			if (pos>=0) count++;
		}
		assertThat(count).isEqualTo(5);
		
	}

	@Test
	public void updateDeviceOk() throws Exception {
		
		SimCard simCard = new SimCard();
		simCard.setId(55L);
		
		IoTDevice testDevice = new IoTDevice();
		testDevice.setId(33l);
		testDevice.setStatus(Constants.IOTDEVICE_STATUS_READY);
		testDevice.setSimCard(simCard);
		
		String deviceBefore = restTemplate.getForObject("http://localhost:" + port + "/iotdevices/33",
				String.class);
		
		restTemplate.put("http://localhost:" + port + "/iotdevices", testDevice);
		
		String deviceAfter = restTemplate.getForObject("http://localhost:" + port + "/iotdevices/33",
				String.class);

		assertThat(deviceBefore).contains("\"status\":\"null\"");
		assertThat(deviceAfter).contains("\"status\":\"READY\"");
	}

	@Test
	public void updateDeviceInvalidSim() throws Exception {
		
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    Map<String, String> param = new HashMap<String, String>();
	    param.put("id","55");
	    HttpEntity<String> requestEntity = new HttpEntity<String>("{\"id\":55,\"simCard\":	{\"id\":5555},\"status\":\"READY\",\"temperature\":76.0}", headers);
	    
	    HttpEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/iotdevices", HttpMethod.PUT, requestEntity, String.class, param);
			
	    assertThat(response.toString()).contains("Sim card does not exists");
	}	
	
	@Test
	public void updateNotFound() throws Exception {
		
	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    Map<String, String> param = new HashMap<String, String>();
	    param.put("id","3300");
	    HttpEntity<String> requestEntity = new HttpEntity<String>("{\"id\":3300,\"status\":\"READY\",\"temperature\":76.0}", headers);
	    
	    HttpEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/iotdevices", HttpMethod.PUT, requestEntity, String.class, param);
			
	    assertThat(response.toString()).contains("Device does not exists");
	}	

	@Test
	public void updateInvalidStatus() throws Exception {
		

	    HttpHeaders headers = new HttpHeaders();
	    headers.set("Content-Type", "application/json");
	    Map<String, String> param = new HashMap<String, String>();
	    param.put("id","33");
	    HttpEntity<String> requestEntity = new HttpEntity<String>("{\"id\":33,\"status\":\"READY\",\"temperature\":76.0}", headers);
	    
	    HttpEntity<String> response = restTemplate.exchange("http://localhost:" + port + "/iotdevices", HttpMethod.PUT, requestEntity, String.class, param);
			
	    assertThat(response.toString()).contains("Device cannot have status READY whiout a SIM card assigned");
	}	
	
	
	@Test
	public void updateDeviceNoSimError() throws Exception {
		
		IoTDevice testDevice = new IoTDevice();
		testDevice.setStatus(Constants.IOTDEVICE_STATUS_READY);
		
		String deviceBefore = restTemplate.getForObject("http://localhost:" + port + "/iotdevices/33",
				String.class);
		
		restTemplate.put("http://localhost:" + port + "/iotdevices", testDevice);
		
		String deviceAfter = restTemplate.getForObject("http://localhost:" + port + "/iotdevices/33",
				String.class);

		assertThat(deviceBefore).contains("\"status\":\"null\"");
		assertThat(deviceAfter).contains("\"status\":\"null\"");
	}	
	
	@Test
	public void deleteDeviceOk() throws Exception {
		
		String deviceBefore = restTemplate.getForObject("http://localhost:" + port + "/iotdevices/44",
				String.class);
		
		restTemplate.delete("http://localhost:" + port + "/iotdevices/44");
		
		String deviceAfter = restTemplate.getForObject("http://localhost:" + port + "/iotdevices/44",
				String.class);

		assertThat(deviceBefore).contains("\"id\":44");
		assertThat(deviceAfter).contains("Device does not exists");
	}	
	
}
