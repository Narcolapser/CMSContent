package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import javax.portlet.PortletRequest;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import javax.persistence.OneToMany;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.MapKey;
import javax.persistence.ElementCollection;
import javax.persistence.MapKeyColumn;
import javax.persistence.CollectionTable;
import javax.persistence.UniqueConstraint;


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

@Entity
@Table(name = "CMSLayout", uniqueConstraints= @UniqueConstraint(columnNames = {"fname"}))
public class CMSLayout
{
	@Transient
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "layout_id")
	public int layout_id = -1;
	
	@Column(name = "fname")
	public String fname;

	@Column(name = "cms_view")
	public String view;
	
	@Column(name = "name")
	public String name;
	
	//@Column(name = "description")
	public String description;
	
	@OneToMany(mappedBy="layout", cascade = CascadeType.ALL)
	public List<CMSSubscription> subscriptions;

//	@ElementCollection // this is a collection of primitives
//	@MapKeyColumn(name="key") // column name for map "key"
//	@Column(name="value") // column name for map "value"
	@ElementCollection(targetClass = String.class)
	@CollectionTable(name = "CMSProperties")
	@MapKeyColumn(name="CMSPropKey")
	@Column(name="CMSPropValue")
	public Map<String,String> properties;
	
	public CMSLayout(){this.view = "layouts/single";}
	
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
	
	public int getLayoutId()
	{
		return this.layout_id;
	}
	
	public void setLayoutId(int val)
	{
		this.layout_id = val;
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
			if (doc != null)//if the document has been deleted you'll get a null value here.
				doc.personalize(model);
		
		if(this.view.equals("view_single"))
			this.setView("layouts/single");
		if(this.view.equals("view_tabbed"))
			this.setView("layouts/tabbed");
		if(this.view.equals("view_tabbed_old"))
			this.setView("layouts/tabbed_old");
		if(this.view.equals("view_vertical_tabs"))
			this.setView("layouts/vertical_tabs");
		if(this.view.equals("view_expanding"))
			this.setView("layouts/expanding");

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
			
			//if the document has been removed, we shouldn't add it to the content.
			CMSDocument doc = ds.getDocument(sub.getDocId());
			if (doc == null)
				continue;
			
			// if the groups is empty in some way, then everyone has access.
			if (groups == null || groups.size() == 0 || groups.get(0).equals(""))
				content.add(doc);

			else //iterate over each security group and see if the user is in one.
				for(String role : sub.getSecurityGroups())
					if(request.isUserInRole(role))
					{// if the user is in one, then add the content and break the loop to avoid duplicates.
						content.add(doc);
						break;
					}
		}
		return content;
	}
}
