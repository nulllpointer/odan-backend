package com.odan.common.application;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CorsFilter implements Filter {

	public CorsFilter() {
		
	}

	public void init(FilterConfig fConfig) throws ServletException {
		
	}

	public void destroy() {
		
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
		res.addHeader("Access-Control-Allow-Origin", "*");
		if (res.getHeader("Access-Control-Allow-Headers") == null) {
			res.addHeader("Access-Control-Allow-Headers",
					"Authorization, Origin, X-Requested-With, X-Range, Range, Content-Type, Accept");
		} else {
			res.setHeader("Access-Control-Allow-Headers",
					"Authorization, Origin, X-Requested-With, X-Range, Range, Content-Type, Accept,"
							+ res.getHeader("Access-Control-Allow-Headers"));
		}
		res.addHeader("Access-Control-Expose-Headers",
				"Accept-Ranges, Content-Encoding, Content-Length, Content-Range");
		// res.addHeader("Access-Control-Allow-Credentials", "true");
		chain.doFilter(request, response);
	}
}
