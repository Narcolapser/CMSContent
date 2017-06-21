package edu.usd.portlet.cmscontent.dao;

import java.util.List;

import edu.usd.portlet.cmscontent.dao.CMSDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InternalDocumentInfoRepository extends CrudRepository<CMSDocument, Long> {
	public List<CMSDocument> findById(String Id);
}
