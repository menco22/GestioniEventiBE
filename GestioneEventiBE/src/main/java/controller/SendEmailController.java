package controller;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Authenticator;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import com.google.gson.Gson;

import beans.RegistrationBean;
import dao.UserDao;

/**
 * Servlet implementation class SendEmailController
 */
public class SendEmailController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
   public SendEmailController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
   
   // utilizzata per inviare la richiesta di account da creator
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		StringBuilder buffer = new StringBuilder();
	    BufferedReader reader = request.getReader();
	    String line;
	    while ((line = reader.readLine()) != null) {
	        buffer.append(line);
	        buffer.append(System.lineSeparator());
	    }
	    String data = buffer.toString();
	    //System.out.println("i parametri sono:" +data);
		RegistrationBean registration = null;
		Gson datas = new Gson();
		try {
			registration = datas.fromJson(data, RegistrationBean.class );
			String from	 = "event.drop@outlook.it";
			String to= "event.drop23@gmail.com";
			Properties prop = new Properties();
			prop.put("mail.smtp.socketFactory.port", "587");
			prop.put("mail.smtp.socketFcatory.class", "jakarta.net.ssl.SSLSocketFactory");
			prop.put("mail.smtp.socketFactory.fallback", "true");
			prop.put("mail.smtp.host", "smtp-mail.outlook.com");
			prop.put("mail.smtp.port", "587");
			prop.put("mail.smtp.ssl.trust", "smtp-mail.outlook.com");
			prop.put("mail.smtp.starttls.enable", "true");
			prop.put("mail.smtp.auth", "true");
			
			Session session = Session.getInstance(prop, new jakarta.mail.Authenticator(){
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("event.drop@outlook.it", "DaveriMencucci22!@");
				}
			});
		
			//Session emailSession = Session.getDefaultInstance(prop, null);
			String msgBody = "Si chiede account come creator del seguente utente: "+ registration.getName() +" "
					+ registration.getSurname() + " " + registration.getUsername();
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			msg.setSubject("Richiesta account come admin da " + registration.getUsername());
			msg.setText(msgBody);
			Transport.send(msg);
		}catch (Exception e) {
			e.printStackTrace();
		}
    }

}
