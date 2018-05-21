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

import edu.usd.portlet.cmscontent.dao.DatabaseResponse;
import edu.usd.portlet.cmscontent.dao.DatabaseAnswer;

@Repository
@Transactional(readOnly = true)
public class DatabaseRepo
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
	public void insertResponse(DatabaseResponse resp)
	{
		Session session = sessionFactory.openSession();
		session.saveOrUpdate(resp);
		session.flush();
	}
	
	@Transactional(readOnly = false)
	public void insertAnswer(DatabaseAnswer ans)
	{
		Session session = sessionFactory.openSession();
		session.saveOrUpdate(ans);
		session.flush();
	}

}
