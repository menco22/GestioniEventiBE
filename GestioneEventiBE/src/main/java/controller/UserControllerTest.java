package controller;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter.Config;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Optional;

import org.json.JSONML;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;

import com.google.gson.Gson;

import beans.CredentialBean;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class UserControllerTest {	
	UserController uc = new UserController();

	
	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() throws IOException, InterruptedException, ServletException {
		HttpClient client = HttpClient.newBuilder().build();
		HttpRequest request = (HttpRequest) HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/UserController")).GET().build();
		HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
		System.out.println(response.body());
		assertNotNull(response.body());
		
		// request per avere l'utente relativo ad un certo id
		HttpRequest requestId = (HttpRequest) HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/UserController?id=9")).GET().build();
		HttpResponse<String> responseId = client.send(requestId, BodyHandlers.ofString());
		System.out.println(responseId.body());
		assertNotNull(responseId.body());
		
		// request per avere l'utente relativo ad un certo username
		HttpRequest requestUsername = (HttpRequest) HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/UserController?username=rinogialli")).GET().build();
		HttpResponse<String> responseUsername = client.send(requestUsername, BodyHandlers.ofString());
		System.out.println(responseUsername.body());
		assertNotNull(responseUsername.body());
	}

	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() throws IOException, InterruptedException {
		// User controller con doPost gestisce il login e viene utilizzata in tutti gli altri test
		CredentialBean credential = new CredentialBean();
		credential.setUsername("pro22");
		credential.setPassword("pro22");
		JSONObject json = new JSONObject(credential);
		String credString = json.toString();
		HttpClient client = HttpClient.newBuilder().build();
		HttpRequest request = (HttpRequest) HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/UserController"))
				.POST(HttpRequest.BodyPublishers.ofString(credString)).build();
        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        Optional<String> cookie = response.headers().firstValue("set-cookie");
        System.out.println(cookie.get());
        assertNotNull(cookie.get());
	}

}
