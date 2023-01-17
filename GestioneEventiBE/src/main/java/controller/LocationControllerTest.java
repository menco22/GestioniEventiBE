package controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.Test;

import beans.CredentialBean;
import beans.NewLocationBean;

public class LocationControllerTest {

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();
		
		// request senza login
		HttpRequest request401 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationController"))
        		.GET().build();
		HttpResponse<String> response401 = client.send(request401, BodyHandlers.ofString());
		assertEquals(response401.statusCode(), 401);
		
		// login
		CredentialBean credential = new CredentialBean();
		credential.setUsername("pro22");
		credential.setPassword("pro22");
		JSONObject json = new JSONObject(credential);
		String credString = json.toString();
		HttpRequest login = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/UserController"))
				.POST(HttpRequest.BodyPublishers.ofString(credString)).build();
		HttpResponse <String> loginResponse = client.send(login, BodyHandlers.ofString());
		Optional<String> cookie = loginResponse.headers().firstValue("set-cookie");
		String parts[]= cookie.toString().split(";");
		char[]part0 = parts[0].toCharArray();
		for(int i=0; i< part0.length;i++) {
			if(part0[i]=='[') {
				part0[i] = '/';
				//System.out.println(part0[i]);
			}
		}
		String part0Stringa = String.valueOf(part0);
		String[] loginCookie = part0Stringa.split("/");
		// request per elenco locations
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationController"))
        		.header("Cookie", loginCookie[1]).GET().build();
        System.out.println(request.headers());
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body());
        
        // request per location specifica
        HttpRequest requestId = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationController?id=5"))
        		.header("Cookie", loginCookie[1]).GET().build();
        System.out.println(requestId.headers());
        HttpResponse<String> responseId = client.send(requestId, BodyHandlers.ofString());
        System.out.println(responseId.body());
        assertNotNull(responseId.body());
	}

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();
		// request senza login
		HttpRequest request401 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationController"))
        		.POST(HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response401 = client.send(request401, BodyHandlers.ofString());
		assertEquals(response401.statusCode(), 401);
		 // login
		CredentialBean credential = new CredentialBean();
		credential.setUsername("pro22");
		credential.setPassword("pro22");
		JSONObject json = new JSONObject(credential);
		String credString = json.toString();
		HttpRequest login = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/UserController"))
				.POST(HttpRequest.BodyPublishers.ofString(credString)).build();
		HttpResponse <String> loginResponse = client.send(login, BodyHandlers.ofString());
		Optional<String> cookie = loginResponse.headers().firstValue("set-cookie");
		String parts[]= cookie.toString().split(";");
		char[]part0 = parts[0].toCharArray();
		for(int i=0; i< part0.length;i++) {
			if(part0[i]=='[') {
				part0[i] = '/';
				//System.out.println(part0[i]);
			}
		}
		// aggiunta location
		NewLocationBean location = new NewLocationBean();
		location.setLocationName("ciao");
		location.setAddress("via arno 3");
		location.setLocationType(1);
		JSONObject json2 = new JSONObject(location);
		String locationStr = json2.toString();
		String part0Stringa = String.valueOf(part0);
		String[] loginCookie = part0Stringa.split("/");
		System.out.println(loginCookie[1]);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationController"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.ofString(locationStr)).build();
        System.out.println(request.headers());
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.statusCode());
        assertEquals(response.statusCode(), 200);
        
        // delete location
        HttpRequest requestDlt = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationController?id=15&action=delete"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> responseDlt = client.send(requestDlt, BodyHandlers.ofString());
        System.out.println(responseDlt.statusCode());
        assertEquals(responseDlt.statusCode(), 200); 
        
        // update location
		NewLocationBean locationUpd = new NewLocationBean();
		locationUpd.setLocationName("ciao!!");
		locationUpd.setAddress("via arno 2");
		locationUpd.setLocationType(1);
		JSONObject json3 = new JSONObject(locationUpd);
		String updateStr = json3.toString();
        HttpRequest requestUpd = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationController?id=14&action=update"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.ofString(updateStr)).build();
        HttpResponse<String> responseUpd = client.send(requestUpd, BodyHandlers.ofString());
        System.out.println(responseUpd.statusCode());
        assertEquals(responseUpd.statusCode(), 200); 
        
        // bad request id e/o action non validi
        HttpRequest request400 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationController?action=ciao"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response400 = client.send(request400, BodyHandlers.ofString());
        System.out.println(response400.statusCode());
        assertEquals(response400.statusCode(), 400);
	}

}
