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
	public Token getToken(String permissions)
	{
		Session session = sessionFactory.openSession();
		Token token = new Token();
		token.setTime(System.currentTimeMillis() + 3600000);
		token.setHash(getHash());
		token.setPermissions(permissions);
		session.saveOrUpdate(token);
		session.flush();
		return token;
	}
	
	@Transactional(readOnly = true)
	public boolean validateToken(String token_hash, String item)
	{
		Session session = sessionFactory.openSession();
		String hql = "FROM Token WHERE token_hash = '" + token_hash + "'";
		try
		{
			Query query = session.createQuery(hql);
			//long token_time = (long)query.uniqueResult();
			Token token = (Token)query.uniqueResult();
			boolean valid = true;
			valid &= token.getTime() > System.currentTimeMillis();
			valid &= token.getPermissions().contains(item);
			return token.getTime() > System.currentTimeMillis();
		}
		catch(NullPointerException e)
		{
			return false;
		}
		catch(Exception e)
		{
			logger.error(e);
			return false;
		}
	}
	
	public static String getHash()
	{
		return "reportApi" + System.currentTimeMillis();
	}
}
