package test;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class RDE implements Filter {

	private String loginInfoPath;
	

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filter) throws IOException,
			ServletException {
		
	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}
	
	public void destroy() {

	}

}
