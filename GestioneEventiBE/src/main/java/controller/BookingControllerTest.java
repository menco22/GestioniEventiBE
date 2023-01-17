package controller;

import static org.junit.Assert.*;

import java.util.Optional;
import java.io.IOException;
import java.net.Authenticator;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import beans.CredentialBean;
import beans.NewBookingBean;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;


public class BookingControllerTest{
	
	@Test 
	public void testDoGetHttpServletRequestHttpServletResponseAdmin() throws IOException, InterruptedException{
		HttpClient client = HttpClient.newBuilder().build();
		
		// request senza login
		HttpRequest request401 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/TableController?idEvent=10"))
        		.GET().build();
		HttpResponse<String> response401 = client.send(request401, BodyHandlers.ofString());
		assertEquals(response401.statusCode(), 401);
		
		// login admin
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
		
		// request con utente admin
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/BookingController?idEvent=10"))
        		.header("Cookie", loginCookie[1]).GET().build();
        System.out.println(request.headers());
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body());       
		
        // bad request con utente admin
        HttpRequest request400 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/BookingController"))
        		.header("Cookie", loginCookie[1]).GET().build();
        System.out.println(request.headers());
        HttpResponse<String> response400 = client.send(request400, BodyHandlers.ofString());
        System.out.println(response400.statusCode());
        assertEquals(response400.statusCode(), 400);
	}
		
	@Test
	public void testDoGetHttpServletRequestHttpServletResponseUser() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();
		
		// request senza login
		HttpRequest request401 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/TableController?idEvent=10"))
        		.GET().build();
		HttpResponse<String> response401 = client.send(request401, BodyHandlers.ofString());
		assertEquals(response401.statusCode(), 401);
		
		// login utente user
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
		System.out.println(loginCookie[1]);
		
		// request con utente user
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/BookingController"))
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
		HttpRequest request401 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/BookingController"))
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
		// aggiunta prenotazione
		NewBookingBean booking = new NewBookingBean();
		booking.setCode("iaghig6t6rjhvc");
		booking.setBookingType("singolo");
		booking.setIdEvent(10);
		booking.setIdTable(6);
		JSONObject json2 = new JSONObject(booking);
		String bookingStr = json2.toString();
		System.out.println(bookingStr);
		String part0Stringa = String.valueOf(part0);
		String[] loginCookie = part0Stringa.split("/");
		System.out.println(loginCookie[1]);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/BookingController"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.ofString(bookingStr)).build();
        System.out.println(request.headers());
        
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.statusCode());
        assertEquals(response.statusCode(), 200);
        
        // eliminazione prenotazione
        HttpRequest requestDlt = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/BookingController?id=7&action=delete"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> responseDlt = client.send(requestDlt, BodyHandlers.ofString());
        System.out.println(responseDlt.statusCode());
        assertEquals(responseDlt.statusCode(), 200);   
        
        // modifica prenotazione
        NewBookingBean bookingUpd = new NewBookingBean();
        bookingUpd.setBookingType("tavolo grande");
        bookingUpd.setCode("ohciòfdx");
        bookingUpd.setIdEvent(10);
        bookingUpd.setIdTable(7);
		JSONObject json3 = new JSONObject(bookingUpd);
		String updateStr = json3.toString();
        HttpRequest requestUpd = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/BookingController?id=1&action=update"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.ofString(updateStr)).build();
        HttpResponse<String> responseUpd = client.send(requestUpd, BodyHandlers.ofString());
        System.out.println(responseUpd.statusCode());
        assertEquals(responseUpd.statusCode(), 200);   
        
        // bad request id e/azione non validi
        HttpRequest request400 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/BookingController?action=ciao"))
        		.header("Cookie", loginCookie[1]).POST(HttpRequest.BodyPublishers.noBody()).build();
        HttpResponse<String> response400 = client.send(request400, BodyHandlers.ofString());
        System.out.println(response400.statusCode());
        assertEquals(response400.statusCode(), 400);
        
	}

}
