package com.izicap.demo;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.izicap.demo.token.controller.UserController;
import com.izicap.demo.token.controller.dtos.UserAndPassword;
import com.izicap.demo.token.services.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {com.izicap.demo.token.Launcher.class})
public class Creation {
	@Autowired
	private TestRestTemplate testRestTemplate;
	private HttpHeaders headers;
	
	@LocalServerPort
	int randomServerPort;
	
	
	static  String token;
	private String creationDate, expirationDate;
	
	//Login and paswword valide
	private static String userAdmin = "admin";
	private static String userPassword = "admin";
	
	// Login and password invalide
	private static String NewUser = "Test";
	private static String NewPassword = "test";
	
	@Test
	public void testCreationUser() throws URISyntaxException {
		String responseBody;
		final String baseUrl = "http://localhost:"+randomServerPort+"/tokens";
		URI uri = new URI(baseUrl);
		UserAndPassword userAndPassword = new UserAndPassword(userAdmin, userPassword);
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<UserAndPassword> request = new HttpEntity<>(userAndPassword, headers);
		
		ResponseEntity<String> result = this.testRestTemplate.postForEntity(uri, request, String.class);
		
		Assert.assertEquals(201, result.getStatusCodeValue());
		System.out.println("Successful token creation " + result.getStatusCodeValue() + " (Created)");
		responseBody = result.getBody();
		System.out.println("-------------------------------------");
		System.out.println(responseBody);
		
		JSONParser jsonPaser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject)jsonPaser.parse(responseBody);
			Creation.token = (String) jsonObject.get("token");
			creationDate = (String) jsonObject.get("creationDate");
			expirationDate = (String) jsonObject.get("expirationDate");
			
			System.out.println("token : " + token);
			System.out.println("creationDate :" + creationDate);
			System.out.println("expirationDate: " + expirationDate);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void createNewUser() throws URISyntaxException {
		String responseBody;
		String curentToken;
		
		curentToken = Creation.token;
		System.out.println("Test here : " + curentToken);
		final String baseUrl = "http://localhost:"+randomServerPort+"/tokens";
		URI uri = new URI(baseUrl);
		UserAndPassword userAndPassword = new UserAndPassword("toto", "toto");
		UserController userController = new UserController();
		userController.create("Bearer " + curentToken, userAndPassword);
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
	}
}
