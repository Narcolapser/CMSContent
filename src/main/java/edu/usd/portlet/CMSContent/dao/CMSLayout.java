package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.usd.portlet.cmscontent.dao.CMSSubscription;
import org.springframework.web.portlet.ModelAndView;

/**
 * The base layout class on which other layouts are built. 
 *
 * @author Toben Archer
 * @version $Id$
 */

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
		//logger.debug("Setting my view to: " + val + " I currently am " + this.view);
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
		logger.debug("Index: " + index + " list length: " + this.subscriptions.size());
		if (this.subscriptions.size() <= index)
			if (val != null)
			{
				logger.debug("Added subscription");
				this.subscriptions.add(val);
			}
			else
			{
				logger.debug("This is the gimp!");
			}
		else
			if (val == null)
			{
				logger.debug("Removed subscription");
				this.subscriptions.remove(index);
			}
			else
			{
				logger.debug("changed subscription");
				this.subscriptions.set(index,val);
			}
		logger.debug("Completeing the update function");
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
		for(CMSDocument doc:(List<CMSDocument>)model.get("content"))
			doc.personalize(model);
			
		return new ModelAndView(this.view,model);
	}
	public CMSLayout copy(CMSLayout val)
	{
		logger.trace("Layout Copying is copying");
		return new CMSLayout(val);
	}
	
	public ArrayList<CMSDocument> getContent(PortletRequest request, List<CMSDocumentDao> dataSources)
	{
		//Preparing a the list of page content.
		ArrayList<CMSDocument> content = new ArrayList<CMSDocument>();

		//Get the content, contingent on it existing and the user having permission.
		for(CMSSubscription sub:this.getSubscriptions()) //step through each subscription
		{
			//get the data source for the subscription.
			CMSDocumentDao ds = sub.getDao(dataSources);
			
			//if the datasource is null, there is a problem, don't render this.
			if (ds == null)
				continue;
				
			//get the security groups for this subscription.
			List<String> groups = sub.getSecurityGroups();
			
			// if the groups is empty in some way, then everyone has access.
			if (groups == null || groups.size() == 0 || groups.get(0).equals(""))
				content.add(ds.getDocument(sub.getDocId()));

			else //iterate over each security group and see if the user is in one.
				for(String role : sub.getSecurityGroups())
					if(request.isUserInRole(role))
					{// if the user is in one, then add the content and break the loop to avoid duplicates.
						content.add(ds.getDocument(sub.getDocId()));
						break;
					}
		}
		return content;
	}
}
