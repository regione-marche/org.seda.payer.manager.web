package org.seda.payer.manager.federa.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.rmi.RemoteException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.filters.TemplateFilter;
import org.seda.payer.manager.components.security.Profilo;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.defaults.actions.SceltaProfiloAction;
import org.seda.payer.manager.login.actions.SignOnSupport;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.MySOAPClient;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.util.XMLUtility;
import org.seda.payer.manager.ws.WSCache;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.security.TokenGenerator;
import com.seda.j2ee5.maf.core.application.ApplicationBinderFactory;
import com.seda.j2ee5.maf.core.security.SignOnException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.payer.pgec.webservice.srv.FaultType;


/**
 * Servlet Filter implementation class CSIFilter
 */
public class ControlAccessLoginCohesion implements Filter {
	public static PropertiesTree configuration;
	public String applicazione="";
	public String urlCheck = "";		
	public String urlLogin = "";
	public String urlValidate = "";
	public String additionalData="";
	public String urlRichiesta = "";
	public String keystorePath = "";
	public String keystoreType = "";
	public String pwdKeystore = "";
	public String aliasCertificate = "";
	public String pwdCertificate = "";
	public String wsSso = "";
	public String idsito = "";
	public String flagFedera = "";
	public String validateXML="";
	public String validateXML64="";
	public String encodedValidateXML64="";
	public String ssoAuth="";
	public String encryptionKey = "";
	public String codiceSocieta = "";

	/**
	 * Default constructor. 
	 */
	public ControlAccessLoginCohesion() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest servletRequest, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		UserBean userBean = null;

		HttpSession session = ((HttpServletRequest)request).getSession();
		String application = "default";
		String action = "default.do";
		String secureIpAsp="";
		if (session == null)
		{
			action = "sceltaaut";
			request.setAttribute(ManagerKeys.REQUEST_ACTION_FLOW, action);
			request.getRequestDispatcher("default.do").forward(servletRequest, response);

		}
 		userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		if(!isValid(userBean))
		{
			// controllo indirizzo IP
			//inizio LP PG21XX04 Bug configuration
			//try {
			//	getConfiguration ();
			//} catch (PropertiesNodeException e) {
			//	// TODO Auto-generated catch block
			//	e.printStackTrace();
			//}
			getConfiguration(request);
			//fine LP PG21XX04 Bug configuration
//			String template=Generics.getTemplateCurrentApplicationStr(request, session);
			String indirizzoIP = request.getRemoteAddr();

			//inizio LP PG21XX04 Leak
			//PrintWriter out = response.getWriter();
			PrintWriter out = null;
			//fine LP PG21XX04 Leak


			if(configuration.getProperty(PropertiesPath.urlCheck.format()) !=null)
				urlCheck=configuration.getProperty(PropertiesPath.urlCheck.format()).trim();

			if(configuration.getProperty(PropertiesPath.urlLogin.format()) !=null)
				urlLogin=configuration.getProperty(PropertiesPath.urlLogin.format()).trim();

			if(configuration.getProperty(PropertiesPath.urlValidate.format()) !=null)
				urlValidate=configuration.getProperty(PropertiesPath.urlValidate.format()).trim();

			if(configuration.getProperty(PropertiesPath.urlRichiesta.format()) !=null)
				urlRichiesta=configuration.getProperty(PropertiesPath.urlRichiesta.format()).trim();

			if(configuration.getProperty(PropertiesPath.keystorePath.format()) !=null)
				keystorePath=configuration.getProperty(PropertiesPath.keystorePath.format()).trim();

			if(configuration.getProperty(PropertiesPath.keystoreType.format()) !=null)
				keystoreType=configuration.getProperty(PropertiesPath.keystoreType.format()).trim();

			if(configuration.getProperty(PropertiesPath.pwdKeystore.format()) !=null)
				pwdKeystore=configuration.getProperty(PropertiesPath.pwdKeystore.format()).trim();

			if(configuration.getProperty(PropertiesPath.aliasCertificate.format()) !=null)
				aliasCertificate=configuration.getProperty(PropertiesPath.aliasCertificate.format()).trim();

			if(configuration.getProperty(PropertiesPath.pwdCertificate.format()) !=null)
				pwdCertificate=configuration.getProperty(PropertiesPath.pwdCertificate.format()).trim();

			if(configuration.getProperty(PropertiesPath.wsSso.format()) !=null)
				wsSso=configuration.getProperty(PropertiesPath.wsSso.format()).trim();

			if(configuration.getProperty(PropertiesPath.idsito.format()) !=null)
				idsito=configuration.getProperty(PropertiesPath.idsito.format()).trim();
			
			if(configuration.getProperty(PropertiesPath.codiceSocieta.format()) !=null)
				codiceSocieta=configuration.getProperty(PropertiesPath.codiceSocieta.format()).trim();

			String auth = request.getParameter("auth");

			if (auth != null && auth.length() > 0) {
				try {
					//inizio LP PG21XX04 Leak
					out = response.getWriter();
					//fine LP PG21XX04 Leak

					//prende il parametro auth
					validateXML64 = auth;

					//lo decodifica da base64

					validateXML = new String(XMLUtility.base64Decode(validateXML64));

					//Legge documento xml
					Document doc = XMLUtility.getXmlDocFromString(validateXML);

					//Prende L'esito dell'autenticazione
					ssoAuth=doc.getElementsByTagName("esito_auth_sso").item(0).getFirstChild().getNodeValue();

					//Controlla se l'esito e' OK
					if (ssoAuth.equals("OK")) {

						//Prende l'id sessione sso e l'id sessione asp contenuti nel file xml di auth
						String idSessioneSSO = doc.getElementsByTagName("id_sessione_sso").item(0).getFirstChild().getNodeValue();
						String idSessioneASP = doc.getElementsByTagName("id_sessione_aspnet_sso").item(0).getFirstChild().getNodeValue();

						//Chiama un web services per recuparare lo username 
						String tokenAuth = wsCheckSessionSSO(idSessioneSSO,idSessioneASP);
						
						System.out.println("wsCheckSessionSSO eseguito");
						
						//Controlla se la username e' valida
						if (tokenAuth != null && tokenAuth.length() > 0 && !tokenAuth.equals("<AUTH>NO</AUTH>")) {

							request.getSession().setAttribute("TOKEN", tokenAuth);
							request.getSession().setAttribute("idSessioneSSO", idSessioneSSO);
							request.getSession().setAttribute("idSessioneASP", idSessioneASP);

							String token = XMLUtility.base64Encode(tokenAuth.getBytes());




							// ora valoriziamo l'userbean

							if(!encryptionKey.equals("")){
								while(tokenAuth.length()%8 != 0 )
									tokenAuth += " ";
								token = XMLUtility.base64Encode(XMLUtility.cipher3DES(true,tokenAuth.getBytes(), encryptionKey.getBytes(), null, true));
							}
							
							String username = doc.getElementsByTagName("user").item(0).getFirstChild().getNodeValue();

							userBean = createNewUserBean((HttpServletRequest)request, session,username);
							
							System.out.println("userben valorizzato");
							System.out.println("usrname =  " + username);
							
							//payerUserCheck(username, userBean, request, session);
							String esito = payerUserCheck(username, userBean, request, session);
							if (esito.equals("OK"))
							{
								
								System.out.println("payerUserCheck eseguita con esito positivo");
							
								application = "default";
								action="default.do";
								request.setAttribute(MAFAttributes.CURRENT_APPLICATION,application);
								request.getRequestDispatcher(action).forward(request, response);
							}
							else
							{
								request.setAttribute("tx_message", esito);
								request.setAttribute(MAFAttributes.CURRENT_APPLICATION,"login");
								request.getRequestDispatcher("logoff.do").forward(request, response);
								return;
							}
														
							
							
							
							
							return;

						} else {
							System.out.println("tokenAuth non valorizzato");
							out.write("Errore Autenticazione!");
						}
					} else {

						//url encoda l'xml
						encodedValidateXML64 = URLEncoder.encode(validateXML64,"UTF-8");

						//Redirige l'utente alla pagina di login
						((HttpServletResponse) response).sendRedirect(urlLogin+"?auth="+encodedValidateXML64);

						return;
					}
				} catch (Exception e) {
					System.out.println("errore catch");
					out.write("Errore Autenticazione!\n" + e.getMessage());
					e.printStackTrace();
				}
				//inizio LP PG21XX04 Leak
				finally {
					if(out != null) {
						out.close();
					}
				}
				//fine LP PG21XX04 Leak
			}
			else
			{

				//Compone l'xml
				validateXML = "<dsAuth xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://tempuri.org/Auth.xsd\"><auth><user /><id_sa /><id_sito>"+idsito+"</id_sito><esito_auth_sa /><id_sessione_sa /><id_sessione_aspnet_sa /><url_validate>"+urlValidate+"</url_validate><url_richiesta>"+urlRichiesta+"</url_richiesta><esito_auth_sso /><id_sessione_sso /><id_sessione_aspnet_sso /><stilesheet>"+additionalData+"</stilesheet></auth></dsAuth>"	;
				//Codifica l'xml in base64
				validateXML64=XMLUtility.base64Encode(validateXML.getBytes());

				//Codifica il parametro come url
				encodedValidateXML64 = URLEncoder.encode(validateXML64,"UTF-8");

				//Redirige verso la pagina di autenticazione
				((HttpServletResponse) response).sendRedirect(urlCheck+"?auth="+encodedValidateXML64);
			}

		} else {
			System.out.println("se userbean valorizzato passo qui");
			application = "default";
			action="default.do";
			
			
			request.setAttribute(MAFAttributes.CURRENT_APPLICATION,application);
			request.setAttribute(MAFAttributes.ACTIONS,action);
			request.getRequestDispatcher("default.do").forward(request, response);
		}
	}

	public static void initializeApplication(HttpSession session)
	{
//		session.setAttribute(ManagerKeys.SESSION_CHECK, "ok");
//
//		//inizializzazione carrello
//		CartManager.initializeCart(session);
//
//		//ripulisco l'area selezionata
//		Generics.setAreaSelected(session, AREA_PORTALE.NONE);
//		session.setAttribute(ManagerKeys.SESSION_EC_SUBAREA_SELECTED,"");
//		session.setAttribute(ManagerKeys.SESSION_AREA_IS_CART, null);
//
//		//ripulisco le variabili di sessione del sito istituzionale
//		session.setAttribute(ManagerKeys.SESSION_MENU_ISTITUZIONALE_SELECTED, null);
//		session.setAttribute(ManagerKeys.SESSION_MENU_APPLICAZIONE, null);
//		session.setAttribute(ManagerKeys.SESSION_MENU_APPLICAZIONE_SELECTED, null);
//
//		//ripulisco le variabili di sessione dei portali esterni
//		session.setAttribute(ManagerKeys.SESSION_IS_PEOPLE, null);
//		session.setAttribute(ManagerKeys.SESSION_IS_CUP, null);
//		session.setAttribute(ManagerKeys.SESSION_IS_EXT, null);
//
//		//ripulisco le variabili di sessione dei ritentativi di pagamento
//		session.setAttribute(ManagerKeys.SESSION_NUM_TENTATIVO_PAGAMENTO, null);
//		session.setAttribute(ManagerKeys.SESSION_CHIAVE_TRANS_RITENTA_PAGAMENTO, null);
//
//		//ripulisco le variabili di sessione della registrazione / profilo
//		session.setAttribute(ManagerKeys.PROFILO_REGISTRAZIONE, null);
//
//		session.setAttribute(ManagerKeys.SESSION_USER_LOCKED, null);
//
//		//PER DEBUG OPERATORE POS: SIMULAZIONE OPERATORE
//		if (PortalManagerStarter.configuration.getProperty(PropertiesPath.simulaoperatore.format()) != null)
//			session.setAttribute(ManagerKeys.SESSION_IS_OPERATORE, "Y");
	}
	//inizio LP PG21XX04 Bug configuration
	//public void getConfiguration () throws PropertiesNodeException {
	//	SystemVariable sv = new SystemVariable();
	//	String rootPath=sv.getSystemVariableValue(ManagerKeys.ENV_CONFIG_FILE);
	//	sv=null;	 
	//	if (rootPath!=null) {
	//		// caricamento configurazioni esterne
	//		configuration = new PropertiesTree(rootPath);
	//	}
	//}
	public void getConfiguration(HttpServletRequest request) {
		configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
	}
	//fine LP PG21XX04 Bug configuration

	public static boolean isValid(UserBean userBean)
	{
		if(userBean == null) 
			return false;
		return (userBean.getUserName() != null && userBean.getCodiceFiscale() != null); //&& userBean.getUserToken() != null);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	private UserBean getNewUserBean()
	{
		UserBean userBean = new UserBean();
		return userBean;
	}
	private UserBean createNewUserBean(HttpServletRequest request, HttpSession session,String username) throws Exception
	{
		try
		{
			UserBean userBean = getNewUserBean();
			userBean.setSession(session.getId());

			TemplateFilter.setTemplateToUserBean(userBean, session, request);
			
//			String nome;
//			String cognome;
//			String email = "";
//			String cell = "";
		
			//String societa = "000LP";
			String societa = codiceSocieta;

//			TripleDESChryptoService desChrypto = new TripleDESChryptoService();
//			try {
//				desChrypto.setIv("12345678");
//				desChrypto.setKeyValue("123456789987654321123456");
//
//			} catch (UnsupportedEncodingException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}

		



			userBean.setCodiceFiscale(username);
			userBean.setUserName(username);
			
			userBean.setNome("MARTINO");
			
			userBean.setCognome("PACINTI");

			System.out.println("Username = " + username);
			

			//Valorizzo il token che serve per il log degli accessi
			String token = null;
			try {
				token = TokenGenerator.generateSecureToken(40);
			} catch (NoSuchAlgorithmException e) {
				token = null;
			}
			userBean.setUserToken(token);

			userBean.setProfile("PYCO");
//			userBean.setIsAnonimo(false);
			session.setAttribute("regmarcheTipoAutenticazione", "F");
			session.setAttribute("dbschema_codsocieta",societa);

			session.setAttribute(SignOnKeys.USER_BEAN, userBean);
			session.setAttribute(SignOnKeys.SIGNED_ON_USER, new Boolean(true));
			return userBean;
		}catch (Exception e) {
			throw e;
		}
	}

	private String wsCheckSessionSSO(String idSessioneSSO, String idSessioneASP){
		String ret = "";
		try{
			MySOAPClient mySOAPClient = new MySOAPClient(wsSso);

			ArrayList<String[]> parameterList= new ArrayList<String[]>();
			parameterList.add(new String[]{"IdSessioneSSO",idSessioneSSO});
			parameterList.add(new String[]{"IdSessioneASPNET",idSessioneASP});

			mySOAPClient.createEnvelope("http://tempuri.org/", "GetCredential", parameterList);
			mySOAPClient.setSOAPAction("http://tempuri.org/GetCredential");

			//Aggiungo Header Cohesion
			Element cohesionHeader = mySOAPClient.createEnvelopeElement("http://uddi.regione.marche.it/Cohesion","Cohesion");
			Element enteIdHeader = mySOAPClient.createEnvelopeElement("http://uddi.regione.marche.it/Cohesion", "enteId");
			Element userProfileHeader = mySOAPClient.createEnvelopeElement("http://uddi.regione.marche.it/Cohesion", "userProfile");
			mySOAPClient.setTextToEnvelopeElement(enteIdHeader, idsito);
			cohesionHeader.appendChild(enteIdHeader);
			cohesionHeader.appendChild(userProfileHeader);
			mySOAPClient.addEnvelopeHeader(cohesionHeader);

			mySOAPClient.signEnvelope(keystorePath, keystoreType, pwdKeystore, aliasCertificate, pwdCertificate);

			mySOAPClient.callWS(true, true);

			ret = XMLUtility.getXmlDocFromString(mySOAPClient.getWsResponseBodyEnvelope()).getElementsByTagNameNS("http://tempuri.org/", "GetCredentialResult").item(0).getTextContent();
		}catch(Exception e){e.printStackTrace();}
		return ret;
	}
	
	private String payerUserCheck(String username, UserBean userBean, HttpServletRequest request, HttpSession session)
	{
		String esito = "OK";
		Calendar now = null;
		/*
		 * Recupero i dati dell'utente censito in PAYER
		 */
		try {
			//verifico che lo username appartenga ad utente esistente nella tabella SEUSRTB
			
			/*
			com.seda.security.webservice.dati.SelezionaUtenteRequestType req = new com.seda.security.webservice.dati.SelezionaUtenteRequestType(username);		
			com.seda.security.webservice.dati.SelezionaUtenteResponseType res = WSCache.securityServer.selezionaUtente(req);
			com.seda.security.webservice.dati.ResponseType response = null;
			*/
			
			com.seda.security.webservice.dati.SelezionaUtentePIVARequestType req = new com.seda.security.webservice.dati.SelezionaUtentePIVARequestType();
			req.setUserName(username);
			com.seda.security.webservice.dati.SelezionaUtentePIVAResponseType res = WSCache.securityServer.selezionaUtentePIVA(req, request);
			com.seda.security.webservice.dati.ResponseType response = null;
			
			if (res != null && res.getResponse() != null)
			{
				response = res.getResponse();
				String retCode = response.getRetCode();
				String retMessage = response.getRetMessage();
				if (retCode.equals("00"))
				{
					if (res.getSelezionaUtentePIVAResponse() == null)
						esito = Messages.UTENZA_NON_VALIDA.format();
					else
					{
						System.out.println("utenza valida");
						now = Calendar.getInstance();
						Calendar dataFineValiditaUtenza = res.getSelezionaUtentePIVAResponse().getDataFineValiditaUtenza();
						if (dataFineValiditaUtenza.before(now))
							esito = Messages.UTENZA_SCADUTA.format(username);
						else
						{
							if (res.getSelezionaUtentePIVAResponse().getUtenzaAttiva().getValue().equalsIgnoreCase("N"))
								esito = Messages.UTENZA_NON_ATTIVA.format(username);
							else
							{
								//inizializzo lo userbean con la parte dei dati relativa all'utenza definita nella SEUSRTB
								//userBean = new UserBean(res.getSecurityUserBean());
								userBean.setSeUserBean(res.getSelezionaUtentePIVAResponse());
								
								//verifico nella tabella PYUSRTB
								/*
								 * Chiamo il metodo per recuperare la lista dei profili legata allo username dell'utente
								 * e la metto in sessione
								 */
								List<Profilo> listProfili = SignOnSupport.caricaProfili(session, username, request);
								if (listProfili.size() == 1)
								{
									session.setAttribute(ManagerKeys.NUMERO_PROFILI, "1");
									System.out.println("NUMERO_PROFILI = 1");
									//se ho un solo profilo aggiorno direttamente lo userBean
									SceltaProfiloAction.setUserBeanAndMenu(userBean, session, listProfili.get(0).getChiaveUtente(), true, request);
									ApplicationBinderFactory.instance().getApplicationBinder().bind(request, userBean);
								}
								else if (listProfili.size() > 1)
								{
									//setto un profilo fittizio per permettere l'accesso all'applicazione di default
									userBean.setUserProfile(ManagerKeys.CHIAVE_MULTIPROFILO);
									session.setAttribute(ManagerKeys.NUMERO_PROFILI, "N");
									System.out.println("NUMERO_PROFILI = N");
									session.setAttribute(ManagerKeys.PROFILI_UTENTE, listProfili);
								}
								else 
									esito = Messages.NESSUN_PROFILO_PRESENTE.format(username);
							}
						}
					}
				}
				else
					esito = retCode + " - " + retMessage;
			}
			else
				Messages.ERRORE_DI_COMUNICAZIONE.format(" - Impossibile accedere ai dati utente");

		} catch (FaultType e) {
			esito = Messages.ERRORE_DI_COMUNICAZIONE.format(" - Impossibile accedere ai dati utente");
			e.printStackTrace();
		} catch (RemoteException e) {
			esito = Messages.ERRORE_DI_COMUNICAZIONE.format(" - Impossibile accedere ai dati utente");
			e.printStackTrace();
		} catch (SignOnException e) {
			esito = e.getMessage();
			e.printStackTrace();
		}
		return esito;
	}
	
}


