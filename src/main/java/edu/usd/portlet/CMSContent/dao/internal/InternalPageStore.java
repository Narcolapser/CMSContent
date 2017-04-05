package edu.usd.portlet.cmscontent.dao;

/**
 * @author Toben Archer
 * @version $Id$
 */

import org.springframework.beans.factory.DisposableBean;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import edu.usd.portlet.cmscontent.dao.CMSPage;

public class InternalPageStore extends HibernateDaoSupport// implements DisposableBean
{
	private Log log = LogFactory.getLog(getClass());

	@Override
	public void storePage(CMSPage Page)
	
	@Override
	public List<CMSPage> getPages()
	
	public List<CMSPage> getPage(FeedbackQueryParameters params)

}
