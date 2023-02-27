package org.seda.payer.manager.components.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class AuthenticationFilter
 */
public class AuthenticationFilter extends it.cefriel.icar.inf3.web.filters.AuthenticationFilter implements Filter {
       
    /**
     * @see it.cefriel.icar.inf3.web.filters.AuthenticationFilter#it.cefriel.icar.inf3.web.filters.AuthenticationFilter()
     */
    public AuthenticationFilter() {
        super();
        
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		super.destroy();
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpSession session=((HttpServletRequest) request).getSession(false);
		
		if(session!=null){
			
			String tipoAutenticazione= (String) session.getAttribute("pagonetTipoAutenticazione");
	
			if(tipoAutenticazione!=null && tipoAutenticazione.equalsIgnoreCase("F")){
				super.doFilter(request, response, chain);
			} else
				// pass the request along the filter chain
				chain.doFilter(request, response);
		} else
			chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		super.init(fConfig);
	}

}
