package org.seda.payer.manager.login.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.List;

import javax.crypto.NoSuchPaddingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.filters.TemplateFilter;
import org.seda.payer.manager.components.security.Profilo;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.defaults.actions.SceltaProfiloAction;
import org.seda.payer.manager.login.util.AesEncryptDecrypt;
import org.seda.payer.manager.login.util.CasRequest;
import org.seda.payer.manager.login.util.ExtUserBean;
import org.seda.payer.manager.login.util.ExtUserBeanDecrypt;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seda.commons.properties.tree.PropertiesNodeException;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.security.TokenGenerator;
import com.seda.compatibility.SystemVariable;
import com.seda.j2ee5.maf.core.application.ApplicationBinderFactory;
import com.seda.j2ee5.maf.core.security.SignOnException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;


public class LoginCasAction  implements Filter{
	
	
	private static final long serialVersionUID = 1L;
	public static PropertiesTree configuration;
	private AesEncryptDecrypt encryptDecrypt;
	
	private ServletContext servletContext;
	
	
	public void init(FilterConfig config) throws ServletException {
		 servletContext = config.getServletContext();
	}
	
	public void destroy() {}
	
	protected void processPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		
		HttpSession session = ((HttpServletRequest)request).getSession();
	
		System.out.println("POST - LoginCAS service");
		
		String body = readBodyFromRequest(request);
		System.out.println("Request body: " + body);
		
		
		
		ObjectMapper mapper = new ObjectMapper();
		try{
			//1. Decrypto il token Bearer per recuperare lo userBean e verificare la validità del token stesso
			CasRequest cas = mapper.readValue(body, CasRequest.class);
			
			System.out.println("jwt: " + cas.getJwt());
			ExtUserBean extUserBean = ExtUserBeanDecrypt.decryptBearer(cas.getJwt(), "a10so3kd#2@3è][01]");
			
			//1. genero UserBean
			UserBean userBean = getNewUserBean(extUserBean.getUsername(), request,session);
			
			//2. Se ho recuperato lo userBean, procedo con il controllo del profilo
			
			checkUser(userBean, request, session);
			
			//3. Genero un Token temporaneo per il login in Pagonet
			// token = username|scadenzaToken
			
			
			String urlRedirect = "";
			if(configuration.getProperty(PropertiesPath.urlRedirectCAS.format(getTemplateName(request)))!=null) {
				urlRedirect = configuration.getProperty(PropertiesPath.urlRedirectCAS.format(getTemplateName(request)));
			}
			
			Long scadenzaToken = new Date().getTime()+60000; //Scadenza token dopo un minuto
			System.out.println("Scadenza token: "+scadenzaToken);
			
			//Encrypt del Token
			String token = userBean.getUserName()+"|"+scadenzaToken;
			String cryptedToken256 = encryptDecrypt.encryptString(token);
			String cryptedToken = DatatypeConverter.printBase64Binary(cryptedToken256.getBytes("UTF-8"));
			System.out.println("crypted token: " + cryptedToken);
			
			urlRedirect += "?token="+cryptedToken;
			System.out.println("url redirect: " + urlRedirect);
		
			
			//4. Invio la url come risposta
			response.setStatus(200);
			response.setContentType("application/json");
			PrintWriter writer=response.getWriter();
			writer.append("{\"url\":\""+urlRedirect+"\"}");
			//inizio LP PG21XX04 Leak
			writer.close();
			//fine LP PG21XX04 Leak
		}catch(Exception e){
			try {
				response.sendError(401);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return;
		
	}
	
	
	private String readBodyFromRequest(HttpServletRequest request){
		StringBuilder sb = new StringBuilder();
		String body = "";
		
		try{
			 BufferedReader reader = request.getReader();
			    
			 String line;
			 while ((line = reader.readLine()) != null) {
			    sb.append(line).append('\n');
			 }
			 body = sb.toString();
			 reader.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return body;
	}
	
	
	protected void processGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		System.out.println("GET - LoginCAS service");
		HttpSession session = ((HttpServletRequest)request).getSession();
		//Reinizializzo lo userbean in modo da non avere conflitti di sessione
		UserBean userBean = null; 
		
		String portaleUrl = "";
		
		try{		
			if(session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA) == null || session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA).equals("")){
				String dbSchemaCodSocieta=configuration.getProperty(PropertiesPath.societa.format(getTemplateName(request)));
				session.setAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA, dbSchemaCodSocieta);
			}
			
			//1. Recupero il token e l'url del portale di logout dalla url
			String token = request.getParameter("token");
			portaleUrl = request.getParameter("portaleUrl");
			
			//2. Decrypto il token e controllo la validità di quest'ultimo
			String decryptedToken256 = new String(DatatypeConverter.parseBase64Binary(token));
			String decryptedToken = encryptDecrypt.decryptString(decryptedToken256);
			System.out.println("decryptedToken: " + decryptedToken);
			
			String[] params = decryptedToken.split("\\|");
			Long scadenzaToken = Long.parseLong(params[1]);
			
			Date date = new Date();
			
			if(scadenzaToken < date.getTime()){
				throw new Exception("Token generato da Pagonet scaduto");
			}
			
			userBean = getNewUserBean(params[0], request, session);
				session.setAttribute(SignOnKeys.SIGNED_ON_USER, new Boolean(true));
			
			checkUser(userBean, request, session);
			
			session.setAttribute(ManagerKeys.NASCONDI_CREDENZIALI, "N");
			if(configuration.getProperty(PropertiesPath.nascondiCredenziali.format(getTemplateName(request)))!=null) {
				session.setAttribute(ManagerKeys.NASCONDI_CREDENZIALI, configuration.getProperty(PropertiesPath.nascondiCredenziali.format(getTemplateName(request))));
			}
			
			//5. Redirect verso pagonet manager
				
			session.setAttribute("urlPortale", portaleUrl);
			request.setAttribute(MAFAttributes.ACTIONS,"default.do");
			request.setAttribute(MAFAttributes.CURRENT_APPLICATION,"default");
			RequestDispatcher rd = request.getRequestDispatcher("../default/default.do");
			rd.forward(request, response);
			
		}catch(Exception e){
			response.sendError(403);
			e.printStackTrace();
		}
		return;
		
	}
	
	
	private void checkUser(UserBean userBean, HttpServletRequest request, HttpSession session) throws SignOnException, Exception{
				
		
			//Controllo il profilo Pagonet
			if(UserBean.isValid(userBean))
			{
				//1. Recupero la lista dei profili
				
				List<Profilo> listProfili = SignOnSupport.caricaProfili(session, userBean.getUserName(), request);
				if (listProfili.size() == 1)
				{
					session.setAttribute(ManagerKeys.NUMERO_PROFILI, "1");
					//se ho un solo profilo aggiorno direttamente lo userBean
					SceltaProfiloAction.setUserBeanAndMenu(userBean, session, listProfili.get(0).getChiaveUtente(), true, request);
					
					ApplicationBinderFactory.instance().getApplicationBinder().bind(request, userBean);
				}
				else if (listProfili.size() > 1)
				{
					//setto un profilo fittizio per permettere l'accesso all'applicazione di default
					ApplicationBinderFactory.instance().getApplicationBinder().bind(request, userBean);
					userBean.setUserProfile(ManagerKeys.CHIAVE_MULTIPROFILO);
					session.setAttribute(ManagerKeys.NUMERO_PROFILI, "N");
					session.setAttribute(ManagerKeys.PROFILI_UTENTE, listProfili);
				}
				else 
					throw new SignOnException(Messages.NESSUN_PROFILO_PRESENTE.format(userBean.getUserName()));
			}else{
				throw new SignOnException("UserBean non valido");
			}
	}
	
	
	
	private UserBean getNewUserBean(String username, HttpServletRequest request,HttpSession session){
		
		UserBean userBean = new UserBean();
//		HttpSession session = request.getSession(false);
		
		try{
			userBean.setSession(session.getId());
			userBean.setUserName(username);
			
			TemplateFilter.setTemplateToUserBean(userBean, session, request);

			userBean.setCodiceFiscale(username);
			
			userBean.setNome("NTEST");
			
			userBean.setCognome("CTEST");

			System.out.println("Username = " + username);
			
			//Valorizzo il token 
			String token = null;
			try {
				token = TokenGenerator.generateSecureToken(40);
			} catch (NoSuchAlgorithmException e) {
				token = null;
			}
			
			userBean.setUserToken(token);

			userBean.setProfile("PYCO");

			session.setAttribute(SignOnKeys.USER_BEAN, userBean);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return userBean;
	}
	
	//inizio LP PG21XX04 Bug configuration
	//public void getConfiguration () throws PropertiesNodeException {
	//	SystemVariable sv = new SystemVariable();
	//	String rootPath=sv.getSystemVariableValue(ManagerKeys.ENV_CONFIG_FILE);
	//	sv=null;	 
	//	if (rootPath!=null) {
	//		configuration = new PropertiesTree(rootPath);
	//	}
	//}
	public void getConfiguration(HttpServletRequest request) {
		configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
	}
	//fine LP PG21XX04 Bug configuration
	
	private String getTemplateName(HttpServletRequest request)
	{
		if (configuration != null)
		{
			String templateName = ManagerStarter.configuration.getProperty(PropertiesPath.templateBase.format() + request.getServerName());

			if (templateName != null && !templateName.equals(""))
				return templateName;
		}	
		return "";
	}
    
	    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException 
		{
	    	HttpServletRequest request = (HttpServletRequest) servletRequest;
	    	HttpServletResponse response = (HttpServletResponse) servletResponse;
	    	
	    	 try {
		    	encryptDecrypt = new AesEncryptDecrypt("a10so3kd#2@3è][01]");
		    	//inizio LP PG21XX04 Bug configuration
				//getConfiguration();
				getConfiguration(request);
		    	//fine LP PG21XX04 Bug configuration
				
				if(request.getMethod().equals("POST"))
		    		processPost(request, response);
		    	else
		    		processGet(request, response);	
				
				
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				e.printStackTrace();
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
	    	//inizio LP PG21XX04 Bug configuration
			//} catch (PropertiesNodeException e) {
			//	e.printStackTrace();
	    	//fine LP PG21XX04 Bug configuration
			}
	
	 
	    	
		}

}
