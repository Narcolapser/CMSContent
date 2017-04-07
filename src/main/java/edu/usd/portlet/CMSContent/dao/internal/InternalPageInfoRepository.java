package edu.usd.portlet.cmscontent.dao;

import java.util.List;

import edu.usd.portlet.cmscontent.dao.CMSPageInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface InternalPageInfoRepository extends CrudRepository<CMSPageInfo, Long> {
//	public List<CMSPageInfo> findByPath(String path);
}
