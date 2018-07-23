package edu.usd.portlet.cmscontent.components;

import javax.servlet.DispatcherType;

import org.springframework.mock.web.MockHttpServletRequest;

/**
 * This class provides a mock Http request for the swallowing JSP renderer to
 * work off of. s
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */
 
public class MockIncludedHttpServletRequest extends MockHttpServletRequest
{
	public MockIncludedHttpServletRequest()
	{
		super();
		this.setMethod("GET");
	}

	public DispatcherType getDispatcherType()
	{
		return DispatcherType.INCLUDE;
	}

	public boolean isAsyncSupported()
	{
		return false;
	}
}
