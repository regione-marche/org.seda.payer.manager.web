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
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.util.MAFRequest;

public class CheckInvalidTagFilter implements Filter {
      

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		
		
		MAFRequest mafReq = new MAFRequest(request);
		String target = mafReq.getTargetURL();
		String skipTarget = ManagerStarter.configuration.getProperty(PropertiesPath.skipTarget.format())!=null ? ManagerStarter.configuration.getProperty(PropertiesPath.skipTarget.format()) : "";
		
		if(!skipTarget.equals("") && skipTarget.contains(target)) {
			chain.doFilter(request, response);
			return;
		}
		
		String allowElementsString = ManagerStarter.configuration.getProperty(PropertiesPath.allowElementsOWASP.format())!=null ? ManagerStarter.configuration.getProperty(PropertiesPath.allowElementsOWASP.format()) : "";
		
		String[] allowElements = {""};
		
		if(allowElementsString.length()>0)
			allowElements = allowElementsString.split(";");
		
		
		chain.doFilter(new FilteredRequest(request, allowElements), response);
		return;
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

            result = policy.sanitize(input)	            		
            		.replace("&#64;", "@")
            		.replace("&#33;", "!")
            		.replace("&#35;", "#")
            		.replace("&#36;", "$")
            		.replace("&#37;", "%")
            		.replace("&#38;", "&")
            		.replace("&#42;", "*")
            		.replace("&#39;", "'")
            		.replace("&#43;", "+");
            
            
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
