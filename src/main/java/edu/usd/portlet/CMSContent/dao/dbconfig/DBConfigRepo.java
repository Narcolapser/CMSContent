package edu.usd.portlet.cmscontent.dao;

import java.util.List;
import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Query;
//import javax.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Repository
@Transactional(readOnly = true)
public class DBConfigRepo
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Transactional(readOnly = false)
	public void insertLayout(CMSLayout layout)
	{
		logger.debug("Saving layout.");
		Session session = sessionFactory.openSession();
		logger.debug("1");
		for(CMSSubscription sub:layout.getSubscriptions())
			sub.setLayout(layout);
		layout = (CMSLayout)session.merge(layout);
//		logger.debug("2");
//		if(layout.getLayoutId() == -1)
//		{
//			logger.debug("Layout is not a hibernate object");
//		}
//		else
//		{
//			logger.debug("3");
//			CMSLayout old = this.getCMSLayoutFname(layout.fname);
//			old = (CMSLayout)session.merge(old);
//			//session.delete(old);
//			layout = (CMSLayout)session.merge(layout);
//			logger.debug("4");
//			for(CMSSubscription sub:old.getSubscriptions())
//			{
//				logger.debug("======");
//				logger.debug(sub);
//				logger.debug("======");
//				session.delete(sub);
//			}
//			logger.debug("5");
//			session.flush();
//			logger.debug("5.1");
//			session.delete(layout);
//			logger.debug("6");
//		}

//		session.delete(layout);
//		session.save(layout);
//		for(CMSSubscription sub:layout.getSubscriptions())
//		{
//			sub = (CMSSubscription)session.merge(sub);
//			session.delete(sub);
//		}
		String hql = "FROM CMSSubscription WHERE layout_id = '" + layout.layout_id + "'";
		logger.debug("3");
		Query query = session.createQuery(hql);
		logger.debug("4");
		List subs = session.createQuery(hql).list();
		logger.debug("5");
		boolean no_change = true;
		for(CMSSubscription sub:(List<CMSSubscription>)subs)
		{
			if(!layout.getSubscriptions().contains(sub))
			{
				session.delete(sub);
				no_change = false;
			}
		}
		if(no_change)
		{
			int i = 0;
			for(CMSSubscription sub:layout.getSubscriptions())
			{
				sub.order = i;
				i++;
			}
		}
		//CMSLayout l2 = (CMSLayout)query.uniqueResult();
		//session.delete(l2);
		logger.debug("6");
		session.saveOrUpdate(layout);
		logger.debug("7");
		session.flush();
		logger.debug("8");
	}
	
	@SuppressWarnings("unchecked")
	public CMSLayout getCMSLayoutFname(String fname)
	{
		logger.debug("Getting " + fname + " from database.");
		Session session = sessionFactory.openSession();
		String hql = "FROM CMSLayout WHERE fname = '" + fname + "'";
		Query query = session.createQuery(hql);
		logger.debug(query);
		return (CMSLayout)query.uniqueResult();
	}
}
