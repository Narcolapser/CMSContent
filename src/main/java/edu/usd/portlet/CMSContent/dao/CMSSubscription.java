package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.MapKey;
import javax.persistence.ElementCollection;
import javax.persistence.MapKeyColumn;
import javax.persistence.CollectionTable;


/**
 * The many-to-many connector between CMSDocuments and CMSLayouts.
 * 
 * @author Toben Archer
 * @version $Id$
 */

@Entity
@Table(name = "CMSSubscription")
public class CMSSubscription
{
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "cms_subscription_id")
	protected int id;

	@Column(name = "doc_id")
	private String docId;
	
	@Column(name = "doc_source")
	private String docSource;
	
	@Column(name = "doc_title")
	private String docTitle;
	
	@Column(name = "doc_order")
	public int order;
	
	@ManyToOne
	@JoinColumn(name="layout_id", nullable=false)
	private CMSLayout layout;
	
	@ElementCollection
	@CollectionTable(name="CMSSecurityGroups", joinColumns=@JoinColumn(name="cms_subscription_id"))
	@Column(name="securityGroups")
	private List<String> securityGroups;
	
	public CMSSubscription()
	{
		this.docId = "";
		this.docSource = "";
		this.securityGroups = new ArrayList<String>();
	}
	
	public CMSSubscription(String DocId, String DocSource, List<String> secGroups)
	{
		this.docId = DocId;
		this.docSource = DocSource;
		this.securityGroups = secGroups;
	}
	
	public String getDocId()
	{
		return this.docId;
	}
	
	public void setDocId(String val)
	{
		this.docId = val;
	}
	
	public String getDocSource()
	{
		return this.docSource;
	}
	
	public void setDocSource(String val)
	{
		this.docSource = val;
	}
	
	public List<String> getSecurityGroups()
	{
		return this.securityGroups;
	}
	
	public void setSecurityGroups(List<String> val)
	{
		this.securityGroups = val;
	}
	public String getDocTitle()
	{
		return this.docTitle;
	}
	
	public void setDocTitle(String val)
	{
		this.docTitle = val;
	}
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int val)
	{
		this.id = val;
	}
	public CMSLayout getLayout()
	{
		return this.layout;
	}
	
	public void setLayout(CMSLayout val)
	{
		this.layout = val;
	}
	public CMSDocumentDao getDao(List<CMSDocumentDao> dataSources)
	{
		for(CMSDocumentDao ds:dataSources)
			if(ds.getDaoName().equals(this.docSource))
				return ds;
		return null;
	}
	public String toString()
	{
		return "Doc ID: " + this.docId + " Source: " + this.docSource + " Title: " + this.docTitle + " Number of security groups: " + this.securityGroups.size();
	}
}
