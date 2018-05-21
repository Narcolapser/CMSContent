package edu.usd.portlet.cmscontent.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import org.springframework.web.portlet.ModelAndView;

import edu.usd.portlet.cmscontent.dao.DatabaseResponse;

@Component
public class Database implements CMSResponder
{
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@Autowired
	private DatabaseRepo databaseRepo;

	//get the name of the responder.
	public String getName(){return "Database";}
	
	public String getOptionInfo(){return "Connection String:";}
	
	//respond to the form returned to the system.
	public boolean respond(String json, String options)
	{
		logger.debug("Processing response: " + json);
		try
		{
			JSONObject obj = new JSONObject(json);
			logger.debug(obj);
			String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new java.util.Date());
//			String id = obj.getString("username") + "-" + obj.getString("formId") + "-" + timeStamp;
			DatabaseResponse resp = new DatabaseResponse();
			resp.setUserName(obj.getString("username"));
			resp.setForm(obj.getString("formId"));
			resp.setResponseTime(timeStamp);
			databaseRepo.insertResponse(resp);
			logger.debug(resp.getId());
			for(String key:obj.getNames(obj))
			{
				DatabaseAnswer ans = new DatabaseAnswer();
				if (key.equals("Submit"))
					continue;
				if (key.equals("replyType"))
					continue;
				if (key.equals("username"))
					continue;
				if (key.equals("formId"))
					continue;
					
				ans.setField(key);
				ans.setAnswer(obj.getString(key));
				ans.setResp(resp);
				databaseRepo.insertAnswer(ans);
				logger.debug("Key: " + key + " Value: " + obj.getString(key));
			}
		}
		catch(Exception e)
		{
			logger.error("Error processing response: " + e);
			return false;
		}
		return true;
	}
}
