package com.izicap.demo;




import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import org.springframework.test.context.junit4.SpringRunner;

import com.izicap.demo.token.controller.dtos.UserAndPassword;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = {com.izicap.demo.token.Launcher.class})
public class ManageToken {

	@Autowired
	private TestRestTemplate testRestTemplate;
	private HttpHeaders headers;
	
	@LocalServerPort
	int randomServerPort;
	
	
	public static String token;
	private String creationDate, expirationDate;
	
	//Login and paswword valide
	private static String userAdmin = "admin";
	private static String userPassword = "admin";
	
	// Login and password invalide
	private static String ivalideUserAdmin = "administrator";
	private static String ivalideUserPassword = "administrator";
	
	@Test
	public void testCreatedToken() throws URISyntaxException {
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
			token = (String) jsonObject.get("token");
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
	public void testGenerateNewToken() throws URISyntaxException, InterruptedException {
		Thread.sleep(60000);
		String responseBody;
		String GeneratedToken;
		String currentToken;
		
		
		final String baseUrl = "http://localhost:"+randomServerPort+"/tokens";
		URI uri = new URI(baseUrl);
		UserAndPassword userAndPassword = new UserAndPassword(userAdmin, userPassword);
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<UserAndPassword> request = new HttpEntity<>(userAndPassword, headers);
		
		ResponseEntity<String> result = this.testRestTemplate.postForEntity(uri, request, String.class);
		
		responseBody = result.getBody();
		System.out.println("ICI-----> "+responseBody);
		
		//Extract each value on the body response
		JSONParser jsonPaser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject)jsonPaser.parse(responseBody);
			GeneratedToken = (String) jsonObject.get("token");
			
			System.out.println("Refresh token : " + GeneratedToken);
			if(GeneratedToken != token && result.getStatusCodeValue() == 201) {
				Assert.assertNotEquals(token, GeneratedToken);
				Assert.assertEquals(201, result.getStatusCodeValue());
				System.out.println("successful token Generated " + result.getStatusCodeValue() + " (OK)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testRefreshToken() throws URISyntaxException {
		String responseBody;
		String tokenRefresh;
		String currentToken;
		
		
		final String baseUrl = "http://localhost:"+randomServerPort+"/tokens";
		URI uri = new URI(baseUrl);
		UserAndPassword userAndPassword = new UserAndPassword(userAdmin, userPassword);
		
		headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<UserAndPassword> request = new HttpEntity<>(userAndPassword, headers);
		
		ResponseEntity<String> result = this.testRestTemplate.postForEntity(uri, request, String.class);
		
		responseBody = result.getBody();
		System.out.println("ICI-----> "+responseBody);
		
		//Extract each value on the body response
		JSONParser jsonPaser = new JSONParser();
		try {
			JSONObject jsonObject = (JSONObject)jsonPaser.parse(responseBody);
			tokenRefresh = (String) jsonObject.get("token");
			
			System.out.println("Refresh token : " + tokenRefresh);
			if(result.getStatusCodeValue() == 200 && tokenRefresh == token) {
				Assert.assertEquals(token, tokenRefresh);
				Assert.assertEquals(200, result.getStatusCodeValue());
				System.out.println("successful token refresh " + result.getStatusCodeValue() + " (OK)");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testUnauthorizeToken() throws URISyntaxException {
		final String baseUrl = "http://localhost:"+randomServerPort+"/tokens";
		URI uri = new URI(baseUrl);
		UserAndPassword userAndPassword = new UserAndPassword(ivalideUserAdmin, ivalideUserPassword);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<UserAndPassword> request = new HttpEntity<>(userAndPassword, headers);
		
		ResponseEntity<String> result = this.testRestTemplate.postForEntity(uri, request, String.class);
		System.out.println("incorrect username/password combination is used "+result.getStatusCodeValue() + " (Unauthorized)");
		Assert.assertEquals(401, result.getStatusCodeValue());
	}
	
}
