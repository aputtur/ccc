package com.copyright.ccc.oue.client.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.copyright.ccc.oue.client.OpenUrlExtensionUtils;

public class OpenUrlExtensionFilter implements Filter
{
	@Override
	public void destroy()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		request.setCharacterEncoding("UTF-8");
		
		HttpServletRequest req = (HttpServletRequest) request;
		
		String uri = req.getRequestURI();
		String op = req.getParameter("operation");
		
		try
		{
			if ((uri.toLowerCase().endsWith("search.do") && (op != null && (op.toLowerCase().equals("go"))))
					|| (uri.toLowerCase().endsWith("cart.do")))
			{
			
				req.getSession().removeAttribute(OpenUrlExtensionUtils.SESSION_KEY);		

			}
		}
		finally
		{
			//Pass control to next filter in chain
			chain.doFilter( request, response );
		}

		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException
	{
		// TODO Auto-generated method stub
		
	}

}
