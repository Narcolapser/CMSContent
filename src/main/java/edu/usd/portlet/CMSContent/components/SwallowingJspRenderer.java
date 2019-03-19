package edu.usd.portlet.cmscontent.components;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.jsp.jstl.fmt.LocalizationContext;

import org.directwebremoting.util.SwallowingHttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class provides the facility to render a JSP document into a string which
 * can be used by a document source to provide more complicated personalization
 * of a document. It was originally created to allow CMSForms to embed username
 * and email address in the form to save them as valueable bits of information
 * and allow for richer functionality. But also allows for document sources to
 * store their document as a data structure rather than a string and then render
 * it using JSP files.
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */

@Component
public class SwallowingJspRenderer implements ServletContextAware
{
	protected final Log logger = LogFactory.getLog(this.getClass());

	@Autowired
	private CMSViewResolver viewResolver;

	private ServletContext servletContext;

	public String render(String viewName, Model model) throws IOException
	{
		return render(viewName, model.asMap(), null);
	}

	public String render(String viewName, Model model, 
	Locale locale) throws IOException
	{
		return render(viewName, model.asMap(), locale);
	}

	public String render(String viewName, Map<String,Object> modelMap) throws IOException
	{
		return render(viewName, modelMap, null);
	}

	public String render(String viewName, Map<String,Object> modelMap, Locale locale) throws IOException
	{
		String result = null;

		if(locale == null)
		{
			locale = Locale.getDefault();
		}

		// These String objects are used to capture the output
		// of SwallowingHttpServletResponse
		StringWriter sout = new StringWriter();
		StringBuffer sbuffer = sout.getBuffer();

		// Set up a fake request and response.  We need the mock response
		// so that we can create the Swallowing response
		HttpServletRequest request = new MockIncludedHttpServletRequest();
		logger.trace("Request type: ");
		logger.trace(request.getMethod());
		HttpServletResponse response = new MockHttpServletResponse();
		HttpServletResponse swallowingResponse = new SwallowingHttpServletResponse(response, sout, "UTF-8");

//		// Use our own LocaleResolver here, or Spring will try to meddle with it
//		LocaleResolver localeResolver = new JspLocaleResolver();
//		localeResolver.setLocale(request, swallowingResponse, locale);

		try
		{
			//Add the modelMap to the request as attributes
			addModelAsRequestAttributes(request, modelMap);

			// Push our LocaleResolver into the request
//			request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE, localeResolver);			

			// Push our Locale into the request
//			LocalizationContext localizationContext = new LocalizationContext(null, locale);
//			request.setAttribute(Config.FMT_LOCALIZATION_CONTEXT+".request", localizationContext);
//			request.setAttribute(Config.FMT_LOCALE, locale);

			// Make sure we are using UTF-8 for the rendered JSP
			swallowingResponse.setContentType("text/html; charset=utf-8");

			// "include" the file (but not really an include) with the dispatcher
			// The resulting rendering will come out in swallowing response,
			// via sbuffer
			RequestDispatcher dispatcher = servletContext.getRequestDispatcher(viewResolver.urlForView(viewName));

			dispatcher.include(request, swallowingResponse);

			result = sbuffer.toString();
		}
		catch(Exception e)
		{
			throw new IOException(e);
		}

		return result;
	}

	/*
	 * Moves the items in the map to be request.attributes
	 */
	private void addModelAsRequestAttributes(ServletRequest request, Map<String,Object> modelMap)
	{
		if(modelMap != null && request != null)
		{
			for (Map.Entry<String, Object> entry : modelMap.entrySet())
			{
				String modelName = entry.getKey();
				Object modelValue = entry.getValue();
				if(modelValue != null)
				{
					request.setAttribute(modelName, modelValue);
				}
				else
				{
					request.removeAttribute(modelName);
				}
			}
		}
	}

	@Override
	public void setServletContext(ServletContext servletContext)
	{
		this.servletContext = servletContext;
	}
}
