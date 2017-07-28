package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import edu.usd.portlet.cmscontent.dao.CMSSubscription;
import org.springframework.web.portlet.ModelAndView;

public class CMSLayout
{
	protected String view;
	protected Map<String,String> properties;
	protected List<CMSSubscription> subscriptions;
	
	public CMSLayout(){}
	
	public CMSLayout(String view, Map<String,String> properties, List<CMSSubscription> subscriptions)
	{
		this.view = view;
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
	
	public Map<String,String> getProperties()
	{
		return this.properties;
	}
	
	public void setProperties(Map<String,String> val)
	{
		this.properties = val;
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
		if (this.subscriptions.size() < index)
			this.subscriptions.add(val);
		else
			this.subscriptions.set(index,val);
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
}
