package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.usd.portlet.cmscontent.dao.CMSDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.usd.portlet.cmscontent.dao.InternalDocumentInfoRepository;


@Repository
public class InternalDocumentInfoRepositoryImpl implements InternalDocumentInfoRepository {

	private Log logger = LogFactory.getLog(getClass());
	private InternalDocumentStore IDS = new InternalDocumentStore();
	
	public void setIDS(InternalDocumentStore ids)
	{
		InternalDocumentStore IDS;
	}
	public List<CMSDocument> findById(String Id)
	{
		ArrayList<CMSDocument> ret = new ArrayList<CMSDocument>();
		return ret;
	}
	public List<CMSDocument> getAll()
	{
		logger.info("Getting all documents");
		return IDS.getAll();
		
	}
	public void deleteAll(){}
	public void delete(){}
}
