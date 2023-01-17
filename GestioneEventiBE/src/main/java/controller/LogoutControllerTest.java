package controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Optional;

import javax.net.ssl.SSLSession;

import org.json.JSONObject;
import org.junit.Test;

import beans.CredentialBean;
import jakarta.servlet.http.HttpSession;

public class LogoutControllerTest {

	@Test
	public void testDoGetHttpServletRequestHttpServletResponse() throws IOException, InterruptedException {
		HttpClient client = HttpClient.newBuilder().build();
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
		HttpRequest logout = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/GestioneEventiBE/LogoutController"))
				.header("Cookie", loginCookie[1]).GET().build();
		HttpResponse <String> logoutResponse = client.send(logout, BodyHandlers.ofString());
		System.out.println(logoutResponse.headers());
		System.out.println(logoutResponse.body());
	}

}
