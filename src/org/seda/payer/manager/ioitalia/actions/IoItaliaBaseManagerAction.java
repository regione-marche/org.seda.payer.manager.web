package org.seda.payer.manager.ioitalia.actions;

import java.io.IOException;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import javax.net.ssl.SSLContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.tools.ant.filters.StringInputStream;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.ioitalia.rest.Content;
import org.seda.payer.manager.ioitalia.rest.CustomRestHeaderFilter;
import org.seda.payer.manager.ioitalia.rest.DefaultAddresses;
import org.seda.payer.manager.ioitalia.rest.InviaMessaggiAppIOInterface;
import org.seda.payer.manager.ioitalia.rest.MessagesRequest;
import org.seda.payer.manager.ioitalia.rest.MessagesResponse;
import org.seda.payer.manager.ioitalia.rest.PaymentData;
import org.seda.payer.manager.ioitalia.rest.ProfilesRequest;
import org.seda.payer.manager.ioitalia.rest.ProfilesResponse;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLImpServizioRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLImpServizioResponse;
import com.seda.payer.bancadati.webservice.srv.FaultType;
import com.seda.payer.core.bean.IoItaliaConfigurazione;
import com.seda.payer.core.bean.IoItaliaFornitura;
import com.seda.payer.core.bean.IoItaliaMessaggio;
import com.seda.payer.core.dao.ArchivioCarichiDao;
import com.seda.payer.core.dao.IoItaliaDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailRequest;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailResponse;

public class IoItaliaBaseManagerAction extends BaseManagerAction {

	/*
	 * *********************
	 * TODO CONNESSIONE DB *
	 * *********************
	 */
	private static final long serialVersionUID = 1L;
	
	protected String payerDbSchema = null;
	protected DataSource payerDataSource = null;
	protected int rowsPerPage = 0;
	protected int pageNumber = 0;
	protected String order = "";
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		
		PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
		String dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		String dataSourceName =  configuration.getProperty(PropertiesPath.dataSourceWallet.format(dbSchemaCodSocieta));
		this.payerDbSchema =  configuration.getProperty(PropertiesPath.dataSourceSchemaWallet.format(dbSchemaCodSocieta));
		try {	
			this.payerDataSource = ServiceLocator.getInstance().getDataSource("java:comp/env/".concat(dataSourceName));
		} catch (ServiceLocatorException e) {
			throw new ActionException("ServiceLocator error " + e.getMessage(),e);
		}

		this.rowsPerPage = ((String) request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String) request.getAttribute("rowsPerPage"));
		this.pageNumber = ((String) request.getAttribute("pageNumber") == null) || (((String) request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String) request.getAttribute("pageNumber"));
		this.order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		/***
		 * TEST DA CANCELLARE
		 */
		/*
		String idDominio = getIdDominio("00004", dbSchemaCodSocieta, "ANE0000619", request);
		
		try (Connection conn = this.payerDataSource.getConnection()) {
			
			if (conn.getAutoCommit())
				conn.setAutoCommit(false);
			
			IoItaliaDao dao = new IoItaliaDao(conn, this.payerDbSchema);
			
			long idFornitura = dao.insertFornitura("00004", dbSchemaCodSocieta, "ANE0000619", "TAR", "01", "MAN-20211213");
			
			IoItaliaMessaggio messaggio = new IoItaliaMessaggio();
			
			messaggio.setCutecute(dbSchemaCodSocieta);
			messaggio.setIdDominio("012345678912");
			messaggio.setTipologiaServizio("TAR");
			messaggio.setTimestampParsingFile(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
			messaggio.setPosizione(1);
			messaggio.setCodiceFiscale("DRNSMN80B13A269W");
			messaggio.setOggettoMessaggio("Oggetto");
			messaggio.setCorpoMessaggio("Corpo del messaggio");
			messaggio.setDataScadenzaMessaggio(Date.valueOf(LocalDate.now()));
			messaggio.setImporto(new BigDecimal(15.0));
			messaggio.setAvvisoPagoPa("012345678912345678");
			messaggio.setScadenzaPagamento("1");
			messaggio.setEmail("mail@tim.it");
			messaggio.setStato("0");
			messaggio.setIdFornitura(idFornitura);
			messaggio.setImpostaServizio("01");
			
			dao.insertMessaggio(messaggio);
			
			conn.commit();
			
		} catch (SQLException | DaoException e) {
			throw new ActionException(e);
		}
		*/
		/***
		 * TEST DA CANCELLARE
		 */
		
		return null;
	}
	
	public void init(ServletContext context) throws ActionException{
		super.init(context);
	}
	
	protected void loadListaImpostaServizio(HttpServletRequest request,
			HttpSession session, String codiceSocieta, String codiceUtente, String codiceEnte, String tipoServizio,
			boolean forceReload) {
		RecuperaDDLImpServizioRequest in = null;
		RecuperaDDLImpServizioResponse res = null;
		String xml = null;
		String lista = "listaImpostaServizio";
		/*
		 * Cerco la stringa XML in sessions
		 */
		if (session.getAttribute(lista) != null && !forceReload) {
			request.setAttribute(lista, (String) session.getAttribute(lista));
		} else {
			try {
				in = new RecuperaDDLImpServizioRequest();
				in.setCodiceSocieta(codiceSocieta);
				in.setCodiceUtente(codiceUtente);
				in.setCodiceEnte(codiceEnte);
				//Giulia 8/05/2014 INIZIO
				//KD - Aggiunto controllo NullPointer
				if(tipoServizio != null && tipoServizio.length()>3)
					tipoServizio = tipoServizio.substring(0,3);
				//Giulia 8/05/2014 FINE
				in.setTipologiaServizio(tipoServizio);
				res =  WSCache.entrateBDServer.recuperaDDLImpostaServizio(in, request);
				com.seda.payer.bancadati.webservice.dati.ResponseType response = res.getRisultato();
				if (response.getRetMessage().equals("OK")) {
					xml = res.getListXml();
					request.setAttribute(lista, xml);
					session.setAttribute(lista, xml);
				} else {
					session.setAttribute(lista, null);
					setErrorMessage("Errore nel caricamento delle Imposte Servizio: "
							+ response.getRetMessage());
				}
			} catch (FaultType e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Imposte Servizio: FaultType - "
						+ e.getLocalizedMessage());
			} catch (RemoteException e) {
				e.printStackTrace();
				setErrorMessage("Errore nel caricamento delle Imposte Servizio: RemoteException - "
						+ e.getLocalizedMessage());
			}
		}
	}
	
	protected String getChiaveEnte(String idDominio) throws Exception {
		
		// codiceEnte da 10
		String chiaveEnte = null;
		
		try (Connection conn = this.payerDataSource.getConnection()) {
			
			ArchivioCarichiDao archivioCarichiDao = new ArchivioCarichiDao(conn, this.payerDbSchema);
			chiaveEnte = archivioCarichiDao.getKeyEnteEC(idDominio);
		}
		
		return chiaveEnte;
	}
	
	@SuppressWarnings("restriction")
	protected String getCodiceSocieta(String codiceUtente, String chiaveEnte, String tipologiaServizio, HttpServletRequest request) throws SQLException, IOException {
		
		String codiceSocieta = null;
		
		ConfigUtenteTipoServizioEnteSearchRequest in = new ConfigUtenteTipoServizioEnteSearchRequest();
		in.setCodiceUtente(codiceUtente);
		in.setChiaveEnte(chiaveEnte);
		in.setCodiceTipologiaServizio(tipologiaServizio);
		ConfigUtenteTipoServizioEnteSearchResponse res = WSCache.configUtenteTipoServizioEnteServer.getConfigUtenteTipoServizioEntes(in, request);
		
		try (com.sun.rowset.WebRowSetImpl wrs = new com.sun.rowset.WebRowSetImpl()) {
			
			wrs.readXml(new StringInputStream(res.getResponse().getListXml(), "UTF-8"));
			
			if (wrs.next()) {
				
				codiceSocieta = wrs.getString("CFE_CSOCCSOC");
			}
		}
		
		return codiceSocieta;
	}
	
	protected String getIdDominio(String codiceSocieta, String codiceUtente, String chiaveEnte, HttpServletRequest request) {
		
		String idDominio = null;
		
		EnteDetailRequest in = new EnteDetailRequest();
		in.setCompanyCode(codiceSocieta);
		in.setUserCode(codiceUtente);
		in.setChiaveEnte(chiaveEnte);
		
		EnteDetailResponse res = null;
		
		try {
			res = WSCache.enteServer.getEnte(in, request);
		} catch (com.seda.payer.pgec.webservice.srv.FaultType e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		
		idDominio = res.getEnte().getCFiscalePIva();
		
		return idDominio;
	}
//	YLM PG22XX06 INI
	protected void inviaMessaggioAppIO(String idMessaggio, HttpServletRequest request) {
		inviaMessaggioAppIOTail (idMessaggio, false , request);
	}
//	YLM PG22XX06 FINE
	protected void inviaMessaggioAppIOTail(String idMessaggio, boolean evol , HttpServletRequest request) {
				
		String messaggioErrore = null;
		Connection connection = null;
		
		try{
			
			connection = this.payerDataSource.getConnection();
			if(connection.getAutoCommit()) connection.setAutoCommit(false);
			
			IoItaliaDao italiaDao = new IoItaliaDao(connection,this.payerDbSchema);
			
			IoItaliaMessaggio messaggio = italiaDao.selectMessaggio(Long.parseLong(idMessaggio));
    		messaggio.setDataInvioMessaggio(Date.valueOf(LocalDate.now()));
    		
    		//PAGONET-392 - inizio
    		String template = getTemplateCurrentApplication(request, request.getSession());
    		IoItaliaFornitura fornitura = new IoItaliaFornitura();
			if ( template.equals("aosta")) {
				fornitura = italiaDao.selectFornitura(messaggio.getIdFornitura());
			} else {
				fornitura = italiaDao.selectFornituraTail(messaggio.getIdFornitura(), true);
			}
			//PAGONET-392 - fine
    		
    		String idDominio = messaggio.getIdDominio();
    		String tipologiaServizio = messaggio.getTipologiaServizio();
    		String codFiscale = messaggio.getCodiceFiscale();
    		String impostaServizio = messaggio.getImpostaServizio();
    		String chiaveEnte = getChiaveEnte(idDominio);

//    		YLM PG22XX06 INI
    		//String codiceUtente = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
    		String codiceUtente = fornitura.getCodiceUtente();	//PAGONET-392
			   		
    		
    		String codSocieta = "";
    		if (!evol) {
    			codSocieta = getCodiceSocieta(codiceUtente, chiaveEnte, tipologiaServizio, request);
    		} 
    		//PAGONET-392 - inizio
    		else {
    			codSocieta = fornitura.getCodiceSocieta();
    		}
    		//PAGONET-392 - fine
    		
    		IoItaliaConfigurazione configurazione = new IoItaliaConfigurazione();
    		if (evol) {
    			configurazione = italiaDao.selectConfigurazioneTail(codSocieta, codiceUtente, chiaveEnte, tipologiaServizio, impostaServizio, true);
    		} else {
    			configurazione = italiaDao.selectConfigurazione(codSocieta, codiceUtente, chiaveEnte, tipologiaServizio, impostaServizio);
        		
    		}
    		if (configurazione == null ) {
    			throw new Exception("Errore: Configurazione Mancante durante inviaMessaggioAppIO");
    		}
//    		YLM PG22XX06 FINE
    		String primaryKey = configurazione.getIoKey1();
    		
    		if(primaryKey != null) {
    			
    			InviaMessaggiAppIOInterface proxy = getProxy(primaryKey, request);
    			ProfilesRequest messaggi = new ProfilesRequest(codFiscale);
//    			System.out.println("inviaMessaggioAppIOTail - profilesRequest - fiscal_code: " + messaggi.getFiscal_code());
    			ObjectMapper obj = new ObjectMapper();  
	            try {  
	                String jsonStr = obj.writeValueAsString(messaggi);  
	                System.out.println("inviaMessaggioAppIOTail - profiles - profilesRequest : " + jsonStr);  
	            }  
	            catch (IOException e) {}
		    	Response response = proxy.profiles(messaggi);
		    	System.out.println("inviaMessaggioAppIOTail - profiles - response = " + response.getStatus());
				System.out.println("inviaMessaggioAppIOTail - profiles - response info = " + response.getStatusInfo());
		    	ProfilesResponse profilesResponse = response.readEntity(ProfilesResponse.class);
//		    	sostituito con stampa json sottostante
//		    	System.out.println("inviaMessaggioAppIOTail - profilesResponse - statusCode: " + profilesResponse.getStatusCode());
//		    	System.out.println("inviaMessaggioAppIOTail - profilesResponse - message: " + profilesResponse.getMessage());
//		    	System.out.println("inviaMessaggioAppIOTail - profilesResponse - detail: " + profilesResponse.getDetail());
//		    	System.out.println("inviaMessaggioAppIOTail - profilesResponse - status: " + profilesResponse.getStatus());
//		    	System.out.println("inviaMessaggioAppIOTail - profilesResponse - title: " + profilesResponse.getTitle());
//		    	System.out.println("inviaMessaggioAppIOTail - profilesResponse - sender_allowed: " + profilesResponse.getSender_allowed());
		    	
		    	obj = new ObjectMapper();  
	            try {  
	                String jsonStr = obj.writeValueAsString(profilesResponse);  
	                System.out.println("inviaMessaggioAppIOTail - profiles - profilesResponse : " + jsonStr);  
	            }  
	            catch (IOException e) {}
	            
		    	if(response.getStatus() == 200) {
		    		if(profilesResponse.getSender_allowed() == true) {
		    			
		    			MessagesRequest messagesRequest = new MessagesRequest();
		    			Content content = new Content();
		    			content.setSubject(messaggio.getOggettoMessaggio());
		    			content.setMarkdown(messaggio.getCorpoMessaggio());
		    			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		    			String dueDate = sdf.format(messaggio.getDataScadenzaMessaggio());
		    			content.setDue_date(dueDate);
		    	
		    			if(messaggio.getAvvisoPagoPa() != null && !messaggio.getAvvisoPagoPa().contentEquals("") && 
		    					messaggio.getImporto() != null && messaggio.getImporto().compareTo(new BigDecimal(1)) >= 0) {
		    				PaymentData paymentData = new PaymentData();
		    				//il campo amount è espresso in centesimi
		    				long amount = 0;
		    				try {
		    					BigDecimal importo = messaggio.getImporto().multiply(new BigDecimal(100));
		    					amount = importo.longValue();
		    				}catch(Exception e) {		
		    				}
		    				if(amount > 0) {
		    					paymentData.setAmount(amount);
		    					paymentData.setNotice_number(messaggio.getAvvisoPagoPa());
		    					if(messaggio.getScadenzaPagamento().equalsIgnoreCase("Y")) {
		    						paymentData.setInvalid_after_due_date(true);
		    					}
		    					content.setPayment_data(paymentData);
		    				}
		    			}
		    			messagesRequest.setContent(content);
		    			messagesRequest.setFiscal_code(codFiscale);
		    			if(messaggio.getEmail() != null && !messaggio.getEmail().equals("")) {
		    				DefaultAddresses defaultAddresses = new DefaultAddresses();
		    				defaultAddresses.setEmail(messaggio.getEmail());
		    				messagesRequest.setDefault_addresses(defaultAddresses);
		    			}
		    			// json della request messages
			            obj = new ObjectMapper();  
			            try {  
			                String jsonStr = obj.writeValueAsString(messagesRequest);  
			                System.out.println("inviaMessaggioAppIOTail - messages - messagesRequest : " + jsonStr);  
			            }  
			            catch (IOException e) {}
		    			
		    			Response response2 = proxy.messages(messagesRequest);
		    			MessagesResponse messagesResponse = response2.readEntity(MessagesResponse.class);
		    			// json della response messages
		    			obj = new ObjectMapper();  
			            try {  
			                String jsonStr = obj.writeValueAsString(messagesResponse);  
			                System.out.println("inviaMessaggioAppIOTail - messages - messagesResponse : " + jsonStr);  
			            }  
			            catch (IOException e) {}  

			            
		    			if(response2.getStatus() == 201) {
		    				messaggio.setIdInvioMessaggio(messagesResponse.getId());
		    				messaggio.setStato("1");
		    				
		    			}else if(response2.getStatus() == 400) {
				    		messaggioErrore = "400 - Invalid payload - "+messagesResponse.getDetail();
				    	}else if(response2.getStatus() == 401) {
				    		messaggioErrore = "401 - Non autorizzato con la chiave specificata";
				    	}else if(response2.getStatus() == 429) {
				    		messaggioErrore = "429 - Troppe richieste";
				    	}else if(response2.getStatus() == 500) {
				    		messaggioErrore = "500 - Il messaggio non può essere inviato - " + messagesResponse.getDetail();
				    	}else{
				    		messaggioErrore = response.getStatus()+ "";
				    		if(messagesResponse.getMessage()!=null) messaggioErrore+=" - "+messagesResponse.getMessage();
				    		if(messagesResponse.getDetail()!=null) messaggioErrore+=" - "+messagesResponse.getDetail();
				    	}
		    			
		    		}else {
		    			//se la risposta è sender_allowed = false allora non posso inviare il messaggio e aggiorno il messaggio sul db
		    			messaggioErrore = "Utente non abilitato a ricevere messaggi.";
		    		}	
		    		
		    	}else if(response.getStatus() == 401) {
		    		messaggioErrore = "401 - Non autorizzato con la chiave specificata";
		    	}else if(response.getStatus() == 404) {
		    		messaggioErrore = "404 - Nessun utente trovato con il codice fiscale specificato";
		    	}else if(response.getStatus() == 429) {
		    		messaggioErrore = "429 - Troppe richieste";
		    	}else {
		    		messaggioErrore = response.getStatus()+ "";
		    		if(profilesResponse.getMessage()!=null) messaggioErrore+=" - "+profilesResponse.getMessage();
		    		if(profilesResponse.getDetail()!=null) messaggioErrore+=" - "+profilesResponse.getDetail();
		    	}
    			
    		}else {
				messaggioErrore = "PrimaryKey non specificata";
			}
    		
    		if(messaggioErrore != null) {
    			messaggio.setMessaggioErrore(messaggioErrore);
	    		messaggio.setStato("2");
    	    	messaggioErrore = idDominio+" - "+messaggio.getTipologiaServizio()+" - "+messaggio.getTimestampParsingFile()+" - "+
    	    	messaggio.getPosizione()+" - Motivo: "+messaggioErrore;
    	    	request.setAttribute("tx_error_message", messaggioErrore);
    		}else {
    			request.setAttribute("tx_message", "Messaggio inviato correttamente");
    		}
    		
    		italiaDao.updateStatoInvioMessaggio(messaggio);

			connection.commit();
    		
		} catch (SQLException | DaoException e) {
			
			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			messaggioErrore = e.getMessage();
//			YLM PG22XX06 INI
			request.setAttribute("tx_error_message", messaggioErrore);
//			YLM PG22XX06 FINE

		} catch (Exception e) {

			try {
				connection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			messaggioErrore = e.getMessage();
			if (messaggioErrore == null) {
				
				messaggioErrore = "Errore Generico IoItaliaBaseManagerAction";
			}
//			YLM PG22XX06 INI
			request.setAttribute("tx_error_message", messaggioErrore);
//			YLM PG22XX06 FINE
		} finally {
			
    		try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private InviaMessaggiAppIOInterface getProxy(String apiPrimaryKey,HttpServletRequest request) {
			
		InviaMessaggiAppIOInterface proxy = null;
				
				
		PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		MAFRequest mafReq = new MAFRequest(request);
				
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		UserBean userBean = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
				
		String templateName = userBean.getTemplate(applicationName);
//		String wsAppIOEndpointURL = configuration.getProperty(PropertiesPath.wsAppIOEndpointURL.format(templateName));
		String wsAppIOEndpointURL = "https://api.io.italia.it/api/v1";		
		try {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
			ResteasyClient client = new ResteasyClientBuilder().sslContext(sslContext).build();
			System.out.println("InviaMessaggiAppIOInterface - getProxy - Ocp-Apim-Subscription-Key: " + apiPrimaryKey);
			client.register(new CustomRestHeaderFilter("Ocp-Apim-Subscription-Key", apiPrimaryKey));
			ResteasyWebTarget target = client.target(UriBuilder.fromPath(wsAppIOEndpointURL));
			System.out.println("InviaMessaggiAppIOInterface - getProxy - uriTarget: " + target.getUri().toString());
			proxy = target.proxy(InviaMessaggiAppIOInterface.class);
		} catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return proxy;
		}
	
	protected String isNull(Object object){
		if(object!=null)
		{
			return (String) object;
		} 
		else
			return "";
	}
	
}
