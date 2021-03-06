package edu.usd.portlet.cmscontent.components;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * This class provides the swallowing JSP renderer a jsp view resolver so that
 * the jsp pages can be accessed and processed internally.
 *
 * @author Toben Archer (Toben.Archer@usd.edu)
 */

@Component
public class CMSViewResolver extends UrlBasedViewResolver
{
	@Override
	protected Class<JstlView> getViewClass()
	{
		return JstlView.class;
	}

	@Override
	protected String getSuffix()
	{
		return ".jsp";
	}

	@Override
	protected String getPrefix()
	{
		return "/WEB-INF/jsp/";
	}

	public String urlForView(String view)
	{
		String result = view;
		if(getPrefix() != null) {
			result = getPrefix() + result;
		}
		if(getSuffix() != null) {
			result = result + getSuffix();
		}
		return result;
	}
}
