package org.seda.payer.manager.components.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;

public class CheckInvalidTagFilter implements Filter {
      

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		
		PropertiesTree configuration=(PropertiesTree)request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		String allowElementsString = configuration.getProperty(PropertiesPath.allowElementsOWASP.format())!=null ? configuration.getProperty(PropertiesPath.allowElementsOWASP.format()) : "";
		
		String[] allowElements = {""};
		
		if(allowElementsString.length()>0)
			allowElements = allowElementsString.split(",");
		
		chain.doFilter(new FilteredRequest(request, allowElements), response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("-----------CheckInvalidTagFilter-----------");
	}
	
	static class FilteredRequest extends HttpServletRequestWrapper {

	 	private static PolicyFactory policy;
	 	
        public FilteredRequest(ServletRequest request, String[] allowedElements) {
            super((HttpServletRequest)request);
            
            policy = new HtmlPolicyBuilder()
    			    .allowElements(allowedElements)
    			    .toFactory();
        }

        public String sanitize(String input) {
            String result = "";

            result = policy.sanitize(input);
            
            return result;
        }

        public String getParameter(String paramName) {
            String value = super.getParameter(paramName);
            if(value!=null)
                value = sanitize(value);
            return value;
        }

        public String[] getParameterValues(String paramName) {
            String values[] = super.getParameterValues(paramName);
                for (int index = 0; index < values.length; index++) {
                	if(values[index]!=null)
                    values[index] = sanitize(values[index]);
                }
            return values;
        }
    }
	
}
