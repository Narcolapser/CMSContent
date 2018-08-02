package edu.usd.portlet.cmscontent.rest;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.lang.Thread;
import java.lang.Runnable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import edu.usd.portlet.cmscontent.dao.FormDaoImpl;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSResponder;
/**
 * This class provides a REST API for the CMS Forms. There are two functions
 * that are exposed to allow the CMS Forms to function correctly:
 *   response: Responds to a form submission.
 *   listResponders: Returns the available responders. For editor usage.
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */
@RestController
@RequestMapping("/v2/form")
public final class FormApi {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	List<CMSDocumentDao> dataSources;
	
	@Autowired
	List<CMSResponder> responders;

	public CMSDocumentDao getDbo(String name)
	{
		CMSDocumentDao dbo = dataSources.get(0);
		for(CMSDocumentDao ds:dataSources)
			if (ds.getDaoName().equals(name))
				dbo = ds;
		return dbo;
	}

	@RequestMapping("response")
	public String formResponse(
		@RequestParam(value="form", defaultValue = "") String form
		)
	{
		try
		{
			logger.debug("Recieved form response: " + form);
			
			//There can be only one dbo we are interested in. Lets interact with it specifically
			FormDaoImpl dbo = (FormDaoImpl)getDbo("InternalForms");
			
			//convert the form response up into a proper JSON object to work with.
			JSONObject obj = new JSONObject(form);
			logger.debug(obj.getString("formId"));
			
			//In order to hide sensative information in responder options from being in the client
			//side html, we leave it out and then load the document from the database. Then we get
			//the json document from the form dbo. 
			CMSDocument doc = dbo.getDocument(obj.getString("formId"));
			ArrayList<JSONObject> jform = dbo.getDocJson(doc);

			//get what responder type it is.
			for(CMSResponder resp: getAutoResponders())
			{
				logger.debug("Responding with: " + resp.getName());
				//ask the responder to to respond to this form.
				boolean result = resp.respond(form,"");
				
				//if the responder returns false, something went wrong.
				if(result == false)
				{
					logger.error("Something went wrong when submitting form: " + form + " to responder: " + resp.getName());
					return "{\"result\":\"failure\"}";
				}
			}
			ResponderRunner rr = new ResponderRunner(form,jform,responders);
			new Thread(rr).start();
			logger.debug("End of responds reached.");
		}
		catch(JSONException e)
		{
			logger.error("Failure to decode json: " + form + " error: " + e);
			return "{\"result\":\"failure\"}";
		}
		return "{\"result\":\"success\"}";
	}

	private CMSResponder getCMSResponder(String name)
	{
		for(CMSResponder re:responders)
			if (name.equals(re.getName()))
				return re;
		return null;
	}

	@RequestMapping("listResponders")
	public String listResponder()
	{
		String ret = "[";
		for (CMSResponder resp:responders)
			if (resp.autoRespond() == false) //auto responders don't get displayed.
				ret += responderAsJson(resp) + ",";
		
		//remove the trailing comma and return with the closing bracket.
		return ret.substring(0,ret.length()-1) + "]";
	}
	
	
	public List<CMSResponder> getAutoResponders()
	{
		List<CMSResponder> ret = new ArrayList<>();
		for (CMSResponder resp:responders)
			if (resp.autoRespond() == true)
				ret.add(resp);
		return ret;
	}

	//This is probably going to be removed, I don't blieve it is necessary.
	@RequestMapping("getResponder")
	public String getResponder(@RequestParam(value="name") String name)
	{
		for (CMSResponder resp:responders)
			if(resp.getName().equals(name))
				return responderAsJson(resp);
		return "";
	}
	
	private String responderAsJson(CMSResponder val)
	{
		try
		{
			JSONObject obj = new JSONObject();
			obj.put("name",val.getName());
			obj.put("optionInfo",val.getOptionInfo());
			return obj.toString();
		}
		catch(JSONException e)
		{
			return "";
		}
	}
	
	private class ResponderRunner implements Runnable
	{
		private String form;
		private ArrayList<JSONObject> jform;
		private List<CMSResponder> responders;
		
		public ResponderRunner(String form, ArrayList<JSONObject> jform, List<CMSResponder> responders)
		{
			this.form = form;
			this.jform = jform;
			this.responders = responders;
		}
		public void run()
		{
			try
			{
				logger.info("Logging from responder thread!");
				
				//loop over the objects till we find the responders, and then call each responder.
				for(JSONObject entry: jform)
					if(entry.getString("type").equals("respType"))
					{
						//get what responder type it is.
						CMSResponder resp = getCMSResponder(entry.getString("label"));
						
						//ask the responder to to respond to this form.
						boolean result = resp.respond(form,entry.getString("options"));
						
						//if the responder returns false, something went wrong.
						if(result == false)
						{
							logger.error("Something went wrong when submitting form: " + form + " to responder: " + entry.getString("label"));
						}
						logger.debug("responded with: " + entry.getString("label"));
					}
			}
			catch (Exception e)
			{
				logger.error("Error in responder thread: " + e);
			}
		}
		private CMSResponder getCMSResponder(String name)
		{
			for(CMSResponder re:responders)
				if (name.equals(re.getName()))
					return re;
			return null;
		}
	}

}

