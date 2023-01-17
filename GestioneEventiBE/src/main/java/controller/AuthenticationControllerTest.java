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
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import beans.CredentialBean;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AuthenticationControllerTest extends Mockito{
	
	public HttpServletResponse setCookie() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();
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
		//return loginCookie[1];
		HttpServletResponse response = mock(HttpServletResponse.class);
		 Cookie cookie1 = new Cookie("loginCookie", loginCookie[1]);
		 System.out.println(cookie1);
		 response.addCookie(cookie1);
		 return response;
	}
	
	@Test
	public void testCheckToken() throws IOException, InterruptedException, ServletException {
		System.out.println(setCookie());
		HttpServletResponse response = setCookie();
		 HttpServletRequest request = mock(HttpServletRequest.class);
		 System.out.println(request.authenticate(response));
		
		 System.out.println(request.getCookies());
		AuthenticationController auth = new AuthenticationController(request); 
		
		System.out.println(auth.checkToken(request));
	}

	@Test
	public void testIsAdmin() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetIdUser() {
		fail("Not yet implemented");
	}

}
