package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.portlet.RenderRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;

import edu.usd.portlet.cmscontent.dao.CMSSubscription;
import org.springframework.web.portlet.ModelAndView;

public class CMSLayout
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	public String view;
	public String name;
	public String description;
	public Map<String,String> properties;
	public List<CMSSubscription> subscriptions;
	
	public CMSLayout(){}
	
	public CMSLayout(CMSLayout val)
	{
		this.view = val.view;
		this.name = val.name;
		this.description = val.description;
		this.properties = val.properties;
		this.subscriptions = val.subscriptions;
	}
	
	public CMSLayout(String view, Map<String,String> properties, List<CMSSubscription> subscriptions)
	{
		this.view = view;
		this.name = name;
		this.description = description;
		this.properties = properties;
		this.subscriptions = subscriptions;
	}
	
	public String getView()
	{
		return this.view;
	}
	
	public void setView(String val)
	{
		this.view = val;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setName(String val)
	{
		this.name = val;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public void setDescription(String val)
	{
		this.description = val;
	}

	public Map<String,String> getProperties()
	{
		logger.debug("Layout properties!");
		if(this.properties == null)
			this.properties = new HashMap<String,String>();
		return this.properties;
	}
	
	public void setProperties(Map<String,String> val)
	{
		this.properties = val;
	}
	
	public String getProperty(String key)
	{
		return this.properties.get(key);
	}

	public void setProperty(String key, String value)
	{
		this.properties.put(key,value);
	}
	
	public List<CMSSubscription> getSubscriptions()
	{
		return this.subscriptions;
	}
	
	public void setSubscriptions(List<CMSSubscription> val)
	{
		this.subscriptions = val;
	}
	
	public void updateSubscription(CMSSubscription val, int index)
	{
		logger.info("Index: " + index + " list length: " + this.subscriptions.size());
		if (this.subscriptions.size() <= index)
			if (val != null)
			{
				logger.info("Added subscription");
				this.subscriptions.add(val);
			}
			else
			{
				logger.info("This is the gimp!");
			}
		else
			if (val == null)
			{
				logger.info("Removed subscription");
				this.subscriptions.remove(index);
			}
			else
			{
				logger.info("changed subscription");
				this.subscriptions.set(index,val);
			}
		logger.info("Completeing the update function");
	}
	
	public List<CMSDocument> getSubscriptionsAsDocs()
	{
		ArrayList<CMSDocument> ret = new ArrayList<CMSDocument>();
		for(CMSSubscription sub:this.subscriptions)
		{
			CMSDocument temp = new CMSDocument();
			temp.setSource(sub.getDocSource());
			temp.setId(sub.getDocId());
			ret.add(temp);
		}
		return ret;
	}
	
	public ModelAndView display(Map<String, Object> model)
	{
		return new ModelAndView(this.view,model);
	}
	public CMSLayout copy(CMSLayout val)
	{
		logger.info("Layout Copying is copying");
		return new CMSLayout(val);
	}
	
	public ArrayList<CMSDocument> getContent(RenderRequest request, List<CMSDocumentDao> dataSources)
	{
	//Preparing a the list of page content.
		ArrayList<CMSDocument> content = new ArrayList<CMSDocument>();
		Map<String, Boolean> isForm = new HashMap<String, Boolean>();
		Map<String, ArrayList<JSONObject>> formContent = new HashMap<String, ArrayList<JSONObject>>();
		JSONArray obj;

		for(CMSSubscription sub:this.getSubscriptions())
			for(CMSDocumentDao ds:dataSources)
				if(ds.getDaoName().equals(sub.getDocSource()))
				{
					List<String> groups = sub.getSecurityGroups();
					if (groups == null || groups.size() == 0 || groups.get(0).equals(""))
					{
						content.add(ds.getDocument(sub.getDocId()));
						if(sub.getDocId().contains("form:"))
						{
							isForm.put(sub.getDocId(),true);
							try{
								obj = new JSONArray(ds.getDocument(sub.getDocId()).getContent());
								ArrayList<JSONObject> jobj = new ArrayList<JSONObject>();
								for(int i = 0; i < obj.length(); i++)
									jobj.add(obj.getJSONObject(i));
								formContent.put(sub.getDocId(),jobj);
							}
							catch(JSONException e)
							{
								logger.error("Error loading form data: " + e);
							}
						}
						else
							isForm.put(sub.getDocId(),false);
					}
					else
					{
						for(String role : sub.getSecurityGroups())
							if(request.isUserInRole(role))
							{
								content.add(ds.getDocument(sub.getDocId()));
								if(sub.getDocId().contains("form:"))
								{
									isForm.put(sub.getDocId(),true);
									try{
										obj = new JSONArray(ds.getDocument(sub.getDocId()).getContent());
										ArrayList<JSONObject> jobj = new ArrayList<JSONObject>();
										for(int i = 0; i < obj.length(); i++)
											jobj.add(obj.getJSONObject(i));
										formContent.put(sub.getDocId(),jobj);
									}
									catch(JSONException e)
									{
										logger.error("Error loading form data: " + e);
									}
								}
								else
									isForm.put(sub.getDocId(),false);
							}
					}
				}
		return content;
	}
}
