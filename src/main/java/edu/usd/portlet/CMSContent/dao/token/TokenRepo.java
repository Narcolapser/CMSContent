package edu.usd.portlet.cmscontent.dao;

import java.lang.Math;
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

import edu.usd.portlet.cmscontent.dao.Token;

@Repository
@Transactional(readOnly = true)
public class TokenRepo
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
	public Token getToken(String username)
	{
		Session session = sessionFactory.openSession();
		Token token = new Token();
		token.setTime(System.currentTimeMillis() + 3600000);
		token.setHash(username + System.currentTimeMillis());
		session.saveOrUpdate(token);
		session.flush();
		logger.debug("Inserted a new token.============================================================");
		return token;
	}
	
	@Transactional(readOnly = true)
	public boolean validateToken(String token_hash)
	{
		Session session = sessionFactory.openSession();
		String hql = "SELECT token_time FROM Token WHERE token_hash = '" + token_hash + "'";
		Query query = session.createQuery(hql);
		long token_time = (long)query.uniqueResult();
		logger.info("token time is: " + token_time);
		return token_time > System.currentTimeMillis();
	}
}
