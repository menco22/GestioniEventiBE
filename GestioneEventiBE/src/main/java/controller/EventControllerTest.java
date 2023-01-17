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
import beans.NewEventBean;

public class EventControllerTest {
	
	@Test 
	public void testDoGetHttpServletRequestHttpServletResponseAdmin() throws IOException, InterruptedException{
		HttpClient client = HttpClient.newBuilder().build();
		
		// request senza login
		HttpRequest request401 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/EventController"))
        		.GET().build();
		HttpResponse<String> response401 = client.send(request401, BodyHandlers.ofString());
		assertEquals(response401.statusCode(), 401);
		
		// request con utente admin
		CredentialBean credential = new CredentialBean();
		credential.setUsername("rinobinachi");
		credential.setPassword("Binachi21");
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
		System.out.println(loginCookie[1]);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/EventController"))
        		.header("Cookie", loginCookie[1]).GET().build();
        System.out.println(request.headers());
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body());       
	}

	@Test
	public void testDoGetHttpServletRequestHttpServletResponseUser() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();
		
		// request senza login
		HttpRequest request401 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/EventController"))
        		.GET().build();
		HttpResponse<String> response401 = client.send(request401, BodyHandlers.ofString());
		assertEquals(response401.statusCode(), 401);
		
		//login con utente user
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
		
		// request con utente user
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/EventController"))
        		.header("Cookie", loginCookie[1]).GET().build();
        System.out.println(request.headers());
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body());
	}

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();
		// request senza login
		HttpRequest request401 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/EventController"))
        		.POST(HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response401 = client.send(request401, BodyHandlers.ofString());
		assertEquals(response401.statusCode(), 401);
		
		// login
		CredentialBean credential = new CredentialBean();
		credential.setUsername("rinobinachi");
		credential.setPassword("Binachi21");
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
		
		// aggiunta evento
		NewEventBean event = new NewEventBean();
		event.setIdLocation(4);
		event.setEventName("nuovo evento");
		event.setDate("2023-03-03 21:00");
		event.setDataScadenza("2023-03-01 14:00");
		event.setStandingPlaces(55);
		JSONObject json2 = new JSONObject(event);
		String eventStr = json2.toString();
		String part0Stringa = String.valueOf(part0);
		String[] loginCookie = part0Stringa.split("/");
		System.out.println(loginCookie[1]);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/EventController"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.ofString(eventStr)).build();
        System.out.println(request.headers());
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.statusCode());
        assertEquals(response.statusCode(), 200);
        
        // eliminazione evento
        HttpRequest requestDlt = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/EventController?id=23&action=delete"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> responseDlt = client.send(requestDlt, BodyHandlers.ofString());
        System.out.println(responseDlt.statusCode());
        assertEquals(responseDlt.statusCode(), 200); 
        
        // modifica evento
		NewEventBean eventUpd = new NewEventBean();
		eventUpd.setIdLocation(5);
		eventUpd.setEventName("nuovo evento43");
		eventUpd.setDate("2023-03-03 21:30");
		eventUpd.setDataScadenza("2023-03-01 12:30");
		eventUpd.setStandingPlaces(45);
		JSONObject json3 = new JSONObject(eventUpd);
		String UpdateStr = json3.toString();
        HttpRequest requestUpd = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/EventController?id=20&action=update"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.ofString(UpdateStr)).build();
        HttpResponse<String> responseUpd = client.send(requestUpd, BodyHandlers.ofString());
        System.out.println(responseUpd.statusCode());
        assertEquals(responseUpd.statusCode(), 200); 
        
        // bad request id e/o action non validi
        HttpRequest request400 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/EventController?action=ciao"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response400 = client.send(request400, BodyHandlers.ofString());
        System.out.println(response400.statusCode());
        assertEquals(response400.statusCode(), 400);
        
	}

}
