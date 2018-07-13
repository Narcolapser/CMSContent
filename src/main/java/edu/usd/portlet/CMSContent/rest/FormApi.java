package edu.usd.portlet.cmscontent.rest;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
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
import edu.usd.portlet.cmscontent.dao.InternalDao;
import edu.usd.portlet.cmscontent.dao.CMSDocumentDao;
import edu.usd.portlet.cmscontent.dao.CMSDocument;
import edu.usd.portlet.cmscontent.dao.CMSResponder;

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
			FormDaoImpl dbo = (FormDaoImpl)getDbo("InternalForms");
			JSONObject obj = new JSONObject(form);
			logger.debug(obj.getString("formId"));
			CMSDocument doc = dbo.getDocument(obj.getString("formId"));
			ArrayList<JSONObject> jform = dbo.getDocJson(doc);
			String options = "";
			for(JSONObject entry: jform)
				if(entry.getString("type").equals("respType"))
					if(!getCMSResponder(entry.getString("label")).respond(form,entry.getString("options")))
					{
						logger.error("Something went wrong when submitting form: " + form + " to responder: " + entry.getString("label"));
						return "{\"result\":\"failure\"}";
					}
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
			ret += responderAsJson(resp) + ",";
		
		//remove the trailing comma and return with the closing bracket.
		return ret.substring(0,ret.length()-1) + "]";
	}

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

}

