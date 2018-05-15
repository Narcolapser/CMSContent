package edu.usd.portlet.cmscontent.components;

import javax.servlet.DispatcherType;

import org.springframework.mock.web.MockHttpServletRequest;

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
