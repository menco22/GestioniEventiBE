package controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.json.JSONObject;
import org.junit.Test;

import beans.RegistrationBean;

public class RegistrationControllerTest {

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() throws IOException, InterruptedException {
		RegistrationBean registration = new RegistrationBean();
		registration.setName("Silvio");
		registration.setSurname("Daveri");
		registration.setEmail("silviodaveri@gmail.com");
		registration.setUsername("silviodave");
		registration.setPassword("silviodave81");
		registration.setIdRole(2);
		JSONObject json = new JSONObject(registration);
		String registrationStr = json.toString();
		HttpClient client = HttpClient.newBuilder().build();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/RegistrationController"))
				.POST(HttpRequest.BodyPublishers.ofString(registrationStr)).build();
		HttpResponse <String> response = client.send(request, BodyHandlers.ofString());
		System.out.println(response.headers());
		assertEquals(response.statusCode(), 200);
	}

}
