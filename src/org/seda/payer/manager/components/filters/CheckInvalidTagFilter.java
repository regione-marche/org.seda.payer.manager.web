package org.seda.payer.manager.components.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.commons.validator.Validation;
import com.seda.commons.validator.ValidationException;
import com.seda.commons.validator.ValidationMessage;
import com.seda.j2ee5.maf.components.encoding.EncodingContext;
import com.seda.j2ee5.maf.components.validation.ValidationContext;
import com.seda.j2ee5.maf.components.validation.ValidationErrorMap;
import com.seda.j2ee5.maf.components.validation.ValidationForm;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.j2ee5.maf.util.MAFUtil;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;


public class CheckInvalidTagFilter implements Filter {
      
	
	private static PolicyFactory policy;
	
//	 private static Pattern[] patterns = new Pattern[]{
//		        // Script fragments
//		        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
//		        // src='...'
//		        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
//		        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
//		        // lonely script tags
//		        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
//		        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
//		        // eval(...)
//		        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
//		        // expression(...)
//		        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
//		        // javascript:...
//		        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
//		        // vbscript:...
//		        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
//		        // onload(...)=...
//		        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
//		        //<qss...>
//		        Pattern.compile("<qss(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
//		    };
//	

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		
		
		PropertiesTree configuration=(PropertiesTree)request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		String allowElementsString = configuration.getProperty(PropertiesPath.allowElementsOWASP.format())!=null ? configuration.getProperty(PropertiesPath.allowElementsOWASP.format()) : "";
		
		String[] allowElements = {""};
		
		if(allowElementsString.length()>0)
			allowElements = allowElementsString.split(",");
		
		policy = new HtmlPolicyBuilder()
			    .allowElements(allowElements)
			    .toFactory();

//		String servername = request.getServerName();
//		String tagString = null;
//		String templateName = null;
//		if (ManagerStarter.configuration != null) {
//			System.out.println("ManagerStarter");
//			templateName = ManagerStarter.configuration.getProperty(PropertiesPath.templateBase.format() + servername);
//			if(templateName!=null) {
//				tagString = ManagerStarter.configuration.getProperty(PropertiesPath.tagHtmlNonAccettati.format(templateName));
//				if(tagString == null)
//					chain.doFilter(request, response);
//			}else {
//				chain.doFilter(request, response);
//			}
//			
//		}
		
		Map<String, String> extraP = request.getParameterMap();		
		
		String matchValue="";
		
		System.out.println("Parameters");
		boolean trovato = false;
		Enumeration keys = request.getParameterNames();
		   while (keys.hasMoreElements() )
		   {
		      String key = (String)keys.nextElement();
		  
		      String value = (String) request.getParameter(key);
		      if(value != null) {
		    	
//		    	  for (Pattern scriptPattern : patterns){
//		                matchValue = scriptPattern.matcher(value).replaceAll("");
//		            
//		    		  //int idx = StringUtils.indexOf(value, s);
//				      if(!matchValue.equals(value)) {
//				    	  System.out.println("key: " + key);
//				    	  System.out.println("Value P: " + value);
//				    	  System.out.println("matchValue P: " + matchValue);
//				    	  
//				    	  extraP.replace(key,"");
//				    	  if(request.getAttribute(key)!=null) {
//				    		  request.setAttribute(key, "");
//				    	  }
//				    	  trovato = true;
//				    	  break;
//				      }
//		    	  }
		    	  
		    	  String safeHTML = policy.sanitize(value);
		    	  
		    	  System.out.println("safeHTML: " + safeHTML);
		    	  
		    	  extraP.replace(key, safeHTML);
		    	  
		      }
		   }
		  
		   HttpServletRequest wrappedRequest = new WrappedRequest(request, extraP);  	
		   
		   System.out.println("Attributes");
		   keys = request.getAttributeNames();
		   while (keys.hasMoreElements() )
		   {
		      String key = (String)keys.nextElement();
		      
		      try {
		    	  String value = (String)request.getAttribute(key);
			      if(value != null) {
//			    	  for (Pattern scriptPattern : patterns){
//			                matchValue = scriptPattern.matcher(value).replaceAll("");
//			            
//			    		  //int idx = StringUtils.indexOf(value, s);
//						      if(!matchValue.equals(value)) {
//						    	  System.out.println("key: " + key);
//						    	  System.out.println("Value A: " + value);
//						    	  System.out.println("matchValue A: " + matchValue);
//						    	  
//						    	  extraP.replace(key,"");
//						    	  if(request.getAttribute(key)!=null) {
//						    		  request.setAttribute(key, "");
//						    	  }
//						    	  trovato = true;
//						    	  break;
//						      }
//			    	  }
			    	  String safeHTML = policy.sanitize(value);
			    	  
			    	  System.out.println("safeHTML: " + safeHTML);
			    	  
			    	  if(wrappedRequest.getAttribute(key)!=null) {
			    		  wrappedRequest.setAttribute(key, safeHTML);
			    	  }
			    	  
//			    	 if(trovato) {
//				    		 System.out.println("Trovato carattere non ammesso: " + value);
//				    		 setError(request);  
//				    		 MAFRequest mafRequest = new MAFRequest((HttpServletRequest)request);
//				    		 MAFUtil.rejectRequest(mafRequest.getHttpServletRequest(), (HttpServletResponse)response, true, true);
//				    	 }
			      }
			      
//			      if(trovato) {
//					   
			    		 
//			    		 setError(wrappedRequest);   		
//			    		 MAFRequest mafRequest = new MAFRequest((HttpServletRequest)wrappedRequest);
//			    		 MAFUtil.rejectRequest(mafRequest.getHttpServletRequest(), (HttpServletResponse)response, true, true);
//			    	 }
		      }catch(Exception e) {
		    	  continue;
		      }
		   }
		   
		 	 
  		
		chain.doFilter(wrappedRequest, response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("-----------CheckInvalidTagFilter-----------");
	}
//	@SuppressWarnings("unchecked")
//	private Map<String, String> getValidatorMap(Map parametersMap) {
//		Map<String, String> validatorMap = new HashMap<String, String>();
//		Iterator<String> it = parametersMap.keySet().iterator();
//		while (it.hasNext()) {
//			String key = (String)it.next();
//			String value = null;
//			Object o = parametersMap.get(key);
//			if (o instanceof String) {
//				value = (String)o;
//			} else if (o.getClass().isArray()) {
//				String[] values = (String[])o;
//				value = Convert.arrayToString(values);
//			}
//			if (value!=null)
//				validatorMap.put(key, value);
//		}
//		return validatorMap;
//	}
	
//	private void setError(HttpServletRequest request) {
//		Validation validation = new Validation();
//		 
//		 MAFRequest mafRequest = new MAFRequest((HttpServletRequest)request);
//		 HttpSession session = mafRequest.getHttpSession();
//		 try {
//			 validation.setLocale((Locale)session.getAttribute(MAFAttributes.LOCALE));	//04082016 GG
//			
//			 String formName = "";
//			 boolean encoding=EncodingContext.getInstance().isEncodeParameter();
//			 boolean encodingDelete=EncodingContext.getInstance().isEncodeDelete();
//	
//			 if (encoding && encodingDelete) 
//					formName = (String)mafRequest.getAttribute(ValidationContext.getInstance().getValidationFormName());
//				else 
//					formName = mafRequest.getParameter(ValidationContext.getInstance().getValidationFormName());
//	
//			 Map<String, String> validatorMap;
//			if (encoding && encodingDelete) validatorMap = getValidatorMap(mafRequest.getAttributeMap());
//			else  validatorMap = getValidatorMap(request.getParameterMap());
//			 
//			 String formKey = ValidationContext.getInstance().getValidationKey();
//			 ValidationForm validationForm = (ValidationForm)session.getAttribute(formName.concat(formKey));
//			if(validationForm!=null) {
//				 validation.setUserMessages(validationForm.getUserMessages());
//				 String validationMessage = ValidationContext.getInstance().getValidationMessage();
//				 ArrayList<ValidationMessage> messages = validation.validateBuffer(validationForm.getValidationBuffer(), validatorMap);
//				 messages.add(new ValidationMessage("", "Internal", "Tag HTML non ammessi"));
//				 request.setAttribute(validationMessage, new ValidationErrorMap(formName, messages));
//			}
//		} catch (ValidationException e) {
//			e.printStackTrace();
//		}
//	}
	
}
