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
		
	@Test
	public void testCheckToken() throws IOException, InterruptedException, ServletException {
		HttpServletRequest request = mock(HttpServletRequest.class); 
		AuthenticationController auth = new AuthenticationController(request);
		assertFalse(auth.checkToken(request));
	}


}
