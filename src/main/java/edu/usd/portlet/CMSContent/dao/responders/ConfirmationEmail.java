package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import org.springframework.web.portlet.ModelAndView;

/**
 * This responder is used to automatically send a confirmation email to a user
 * when responds to an email. The option set here allows the form creator
 * to give some sort of message to the user in response. 
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */

@Component
public class ConfirmationEmail implements CMSResponder
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	//get the name of the responder.
	public String getName(){return "Confirmation Email";}
	
	public String getOptionInfo(){return "Message body:";}

	//respond to the form returned to the system.
	public boolean respond(String json, String options)
	{
		logger.debug("Processing response: " + json);
		
		
		String to=options.split(",")[1];
		String from="noreply@usd.edu";
		String host = "smtp.usd.edu";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host",host);
		Session session = Session.getDefaultInstance(properties);
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Your submission has been recieved.");
			message.setText(options);
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
	
	private String getUserEmail(String json)
	{
		try
		{
			JSONObject obj = new JSONObject(json);
			return obj.getString("useremail");
		}
		catch (JSONException e)
		{
			logger.error("Error processing user's response: " + e);
			return "Error processing form.";
		}
	}
}
