package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import org.springframework.web.portlet.ModelAndView;

@Component
public class Email implements CMSResponder
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	//get the name of the responder.
	public String getName(){return "Email";}
	
	public String getOptionInfo(){return "Sending addres, Receiving address:";}

	//respond to the form returned to the system.
	public boolean respond(String json, String options)
	{
		logger.debug("Processing response: " + json);
		
		
		String to=options.split(",")[1];
		String from=options.split(",")[0];
		String host = "exchange.usd.edu";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host",host);
		Session session = Session.getDefaultInstance(properties);
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("This is the subject Line!");
			message.setText(json);
			Transport.send(message);
			logger.debug("Message sent sucessfully");
		}
		catch (MessagingException mex)
		{
			logger.debug("Error sending message: " + mex);
			return false;
		}
		return true;
	}
}
