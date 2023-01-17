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

public class UserProfileControllerTest {

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();
		
		//request senza login
		HttpRequest request401 = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/UserProfileController"))
        		.GET().build();
		HttpResponse<String> response401 = client.send(request401, BodyHandlers.ofString());
		assertEquals(response401.statusCode(), 401);
		
		//login
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
		
		// request corretta
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/UserProfileController"))
        		.header("Cookie", loginCookie[1]).GET().build();
        System.out.println(request.headers()); 
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        System.out.println(response.body());
        assertNotNull(response.body());
	}

}
