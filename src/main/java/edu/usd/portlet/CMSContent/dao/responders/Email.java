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

	@Autowired
	private InternalDocumentDao internalDocumentDao;

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
			message.setText(formMessage(json),"utf-8", "html");
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
			String[] val = new FormDoc(this.internalDocumentDao.getDocumentById(obj.getString("formId"))).getFields();
			//ret = "The user " + obj.getString("username") + " submitted the form " + obj.getString("formId") + " with the following: \n";
			ret = "<table class=\"table table-striped\">\n<thead><tr><th width=\"200px\">Field</th><th width=\"400px\">Answer</th></tr></thead><tbody>";
			for(String key:val)
			{
				if (key.equals("Submit"))
					continue;
				if (key.equals("replyType"))
					continue;
				if (key.equals("username"))
					continue;
				if (key.equals("formId"))
					continue;
				
				if(key.length() > 20)
					ret += "<tr><td>" + key.substring(0,20) + "</td><td>" + obj.getString(key) + "</td></tr>\n";
				else
					ret += "<tr><td>" + key + "</td><td>" + obj.getString(key) + "</td></tr>\n";
				logger.debug("Key: " + key + " Value: " + obj.getString(key) + "\n");
			}
			ret += "</tbody></table>";
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
