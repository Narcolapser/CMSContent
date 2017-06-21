package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;

import edu.usd.portlet.cmscontent.dao.CMSDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.usd.portlet.cmscontent.dao.InternalDocumentInfoRepository;


@Repository
public class InternalDocumentInfoRepositoryImpl implements InternalDocumentInfoRepository {
	public List<CMSDocument> findById(String Id)
	{
		ArrayList<CMSDocument> ret = new ArrayList<CMSDocument>();
		return ret;
	}
	public void deleteAll(){}
}
