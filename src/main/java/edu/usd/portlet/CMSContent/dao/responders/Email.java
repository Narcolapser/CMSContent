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
import java.util.Iterator;
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
			message.setSubject(getSubject(json));
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
			ret = "<table style=\"border: 1px solid black\">\n<thead><tr><th width=\"200px\">Field</th><th width=\"400px\">Answer</th></tr></thead><tbody>";
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
				
//				if(key.length() > 20)
//					ret += "<tr><td>" + key.substring(0,20) + "</td><td>" + obj.getString(key) + "</td></tr>\n";
//				else
				if(obj.has(key))
				{
					ret += "<tr><td style=\"border: 1px solid black\">" + key + "</td><td style=\"border: 1px solid black\">" + obj.getString(key) + "</td></tr>\n";
					logger.debug("Key: " + key + " Value: " + obj.getString(key) + "\n");
				}
				else
				{
					ret += "<tr><td style=\"border: 1px solid black\">" + key + "</td><td style=\"border: 1px solid black\">Error processing value. Mismatch between form spec and form response.</td></tr>\n";
					logger.debug("Key: " + key + " Value does not exist.\n");
				}
			}
			ret += "<tr><td style=\"border: 1px solid black\">Extra info</td></tr>\n";
			ret += getOffSpec(val,obj);
			ret += "</tbody></table>";
			ret += "<p>Raw response: " + json + "</p>";
		}
		catch (JSONException e)
		{
			logger.error("Error processing user's response: " + e);
			logger.error(json);
			return "Error processing user's response: " + e + "\n" + json;
		}
		return ret;
	}
	
	private String getSubject(String json)
	{
		String ret = "Form Submission";
		try
		{
			JSONObject obj = new JSONObject(json);
			ret = "Form Submission for: " + new FormDoc(this.internalDocumentDao.getDocumentById(obj.getString("formId"))).getTitle();
		}
		catch (JSONException e)
		{
			logger.error("Error processing user's response: " + e);
			logger.error(json);
			return "Error processing user's response: " + e + "\n" + json;
		}
		return ret;
	}
	
	private String getOffSpec(String[] spec, JSONObject obj)
	{
		String ret = "";
		Iterator<String> iter = obj.keys();
		try
		{
			while (iter.hasNext())
			{
				String key = iter.next();
				boolean contains = false;
				for (String specKey: spec)
					if(specKey.equals(key))
					{
						contains = true;
						break;
					}
				if (!contains)
					ret += "<tr><td style=\"border: 1px solid black\">" + key + "</td><td style=\"border: 1px solid black\">" + obj.getString(key) + "</td></tr>\n";
			}
		}
		catch (JSONException e)
		{
			logger.error("Error processing out of spec: " + e);
		}
		return ret;
	}

	public boolean autoRespond(){return false;}
}
