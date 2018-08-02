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
 * This responder is used to send an email to a specific user with the result of
 * the form. It only works with systems that allow annonymous stmp requests.
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */

@Component
public class Email implements CMSResponder
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	//get the name of the responder.
	public String getName(){return "Email";}
	
	public String getOptionInfo(){return "Sending address, Receiving address:";}

	//respond to the form returned to the system.
	public boolean respond(String json, String options)
	{
		logger.debug("Processing response: " + json);
		
		
		String to=options.split(",")[1];
		String from=options.split(",")[0];
		String host = "smtp.usd.edu";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host",host);
		Session session = Session.getDefaultInstance(properties);
		
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject("Form response.");
			message.setText(formMessage(json));
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
	
	private String formMessage(String json)
	{
		String ret = "Error processing form.";
		try
		{
			JSONObject obj = new JSONObject(json);
			ret = "The user " + obj.getString("username") + " submitted the form " + obj.getString("formId") + " with the following: \n";
			for(String key:obj.getNames(obj))
			{
				if (key.equals("Submit"))
					continue;
				if (key.equals("replyType"))
					continue;
				if (key.equals("username"))
					continue;
				if (key.equals("formId"))
					continue;
					
				ret += key + "\t: " + obj.getString(key) + "\n";
				logger.debug("Key: " + key + " Value: " + obj.getString(key) + "\n");
			}
		}
		catch (JSONException e)
		{
			logger.error("Error processing user's response: " + e);
			return "Error processing form.";
		}
		return ret;
	}
	
	public boolean autoRespond(){return false;}
}
