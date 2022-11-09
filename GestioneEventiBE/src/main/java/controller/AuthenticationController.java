package controller;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import com.auth0.jwt.JWT;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.*;  

public class AuthenticationController {
	public AuthenticationController(HttpServletRequest request) {
		super();
		this.request = request;
	}

	HttpServletRequest request; 
	HttpServletResponse response;
	
	public boolean checkToken( HttpServletRequest request ) {
		Cookie[]cookie = request.getCookies();
		ArrayList <Cookie> cookies = new ArrayList();
		for(int i=0; i<cookie.length; i++) {
			cookies.add((Cookie) Array.get(cookie, i));
		}
		//Cookie[] cookies = request.getCookies();
		if(cookies!=null) {
			for(int i=0; i<cookies.size(); i++) {
				if(cookies.get(i).getName().equals("loginCookie")){
					String jwt = cookies.get(i).getValue();
					String[] parts = jwt.split("\\.");
					String body = new String (Base64.getUrlDecoder().decode(parts[1]));
					//System.out.println(body);
					JSONObject payload;
					try {
						payload = new JSONObject(body);
						System.out.println(payload);
						System.out.println(payload.getLong("exp"));
						System.out.println((LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)));
						if(payload.getLong("exp")>(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC))) {
							System.out.println("Token valido");
							return true;
						}else {
							return false;
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				i++;
			}
		}return false;
	}

}
