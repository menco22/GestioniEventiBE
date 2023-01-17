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
import beans.NewTypeBean;

public class LocationTypeControllerTest {

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();
		
		// request senza login
		HttpRequest request401 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationTypeController"))
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
		// request per lista tipi location
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationTypeController"))
        		.header("Cookie", loginCookie[1]).GET().build();
        System.out.println(request.headers());
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body());
        
        // request per tipo specifico
        HttpRequest requestId = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationTypeController?id=5"))
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
		HttpRequest request401 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationTypeController"))
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
		// aggiunta tipo location
		NewTypeBean type = new NewTypeBean();
		type.setDescription("ristorante");
		JSONObject json2 = new JSONObject(type);
		String typeStr = json2.toString();
		String part0Stringa = String.valueOf(part0);
		String[] loginCookie = part0Stringa.split("/");
		System.out.println(loginCookie[1]);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationTypeController"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.ofString(typeStr)).build();
        System.out.println(request.headers());
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.statusCode());
        assertEquals(response.statusCode(), 200);
        
        // delete tipo location
        HttpRequest requestDlt = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationTypeController?id=6&action=delete"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> responseDlt = client.send(requestDlt, BodyHandlers.ofString());
        System.out.println(responseDlt.statusCode());
        assertEquals(responseDlt.statusCode(), 200); 
        
        //update tipo location
		NewTypeBean typeUpd = new NewTypeBean();
		typeUpd.setDescription("ristorante stellato");
		JSONObject json3 = new JSONObject(typeUpd);
		String updateStr = json3.toString(); 
        HttpRequest requestUpd = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationTypeController?id=5&action=update"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.ofString(updateStr)).build();
        HttpResponse<String> responseUpd = client.send(requestUpd, BodyHandlers.ofString());
        System.out.println(responseUpd.statusCode());
        assertEquals(responseUpd.statusCode(), 200); 
        
        // bad request id e/o action non validi
        HttpRequest request400 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LocationTypeController?action=ciao"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response400 = client.send(request400, BodyHandlers.ofString());
        System.out.println(response400.statusCode());
        assertEquals(response400.statusCode(), 400);
	}

}
