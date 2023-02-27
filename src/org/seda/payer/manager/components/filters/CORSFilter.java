package org.seda.payer.manager.components.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;


@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {

	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) 
	throws IOException, ServletException{
		
		try {
			
			HttpServletRequest request = (HttpServletRequest) servletRequest;
			HttpServletResponse response = (HttpServletResponse) servletResponse;
			
			String accessControllAllowHeadersValue = getHeaderInfo(request);
			System.out.println("accessControllAllowHeadersValue: "+accessControllAllowHeadersValue);
			String origin = getOriginInfo(request);
			System.out.println("origin: "+origin);
			
			response.setHeader("Access-Control-Allow-Origin", origin);
			response.setHeader("Access-Control-Allow-Credentials", "true");
			response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
			response.setHeader("Access-Control-Max-Age", "3600");
			response.setHeader("Access-Control-Allow-Headers", accessControllAllowHeadersValue);
			response.setHeader("Access-Control-Expose-Headers", "X_JWT,Id_Request,X-CSRF-TOKEN");
			if(request.getMethod().equals("OPTIONS")){ 
				System.out.println("Passo al prossimo filtro...");
				response.setStatus(HttpServletResponse.SC_ACCEPTED);
				return;
			}

			chain.doFilter(servletRequest, servletResponse);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	public void init(FilterConfig config){
		//this.domains = config.getInitParameter("domains");
		//System.out.println("Domains: " + domains);
		System.out.println("Inizio CORSFilter");
	}
	
	public void destroy(){}
	
	private String getHeaderInfo(HttpServletRequest request){
		String hd = request.getHeader("Access-Control-Request-Headers"); 
		return hd;
	}
	private String getOriginInfo(HttpServletRequest request){
		String hd = request.getHeader("Origin"); 
		return hd;
	}
	
}
