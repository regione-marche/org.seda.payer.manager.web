package org.seda.payer.manager.components.filters;

import java.io.IOException;
import java.rmi.RemoteException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.MenuRequestParams;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.ws.WSCache;
//inizio LP PG22XX01
import org.seda.payer.manager.ws.WsLogRequestThread;
//fine LP PG22XX01

import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.pgec.webservice.commons.dati.ManagerLogRequestType;
import com.seda.payer.pgec.webservice.commons.dati.ManagerLogResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

//inizio LP PG21X007
import com.esed.log.req.dati.LogRequest;
import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.defender.http.HttpDefenseRequest;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteDetailRequest;
import com.seda.payer.pgec.webservice.anagente.dati.AnagEnteDetailResponse;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComDetailRequest;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComDetailResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaEnteUtenteSocietaAneRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaEnteUtenteSocietaAneResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaEnteUtenteSocietaProvinciaRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaEnteUtenteSocietaProvinciaResponse;
import java.sql.SQLException;
import javax.sql.rowset.WebRowSet;
//inizio LP PG22XX01
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//fine LP PG22XX01
import javax.ws.rs.client.Entity;
//inizio LP PG22XX01
//import javax.ws.rs.client.Invocation.Builder;
//import javax.ws.rs.client.WebTarget;
//fine LP PG22XX01
import javax.ws.rs.core.MediaType;
//inizio LP PG22XX01
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;
//fine LP PG22XX01

import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

//fine LP PG21X007

public class LoggingManager implements Filter {
	static final String loginApplName = "default";
	static final String logoutApplName = "login";

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException, FaultType, RemoteException {
		UserBean userBean = null;
		String userToken = null;
		String sessionId = null;
		String userName = null;
		String userProfile = null;
		String codiceSocieta = null;
		String codiceUtente = null;
		String chiaveEnte = null;
		String indirizzoIP = null;
		String currentApplication = null;
		ManagerLogResponseType logResponse = null;
		ManagerLogRequestType in = null;
		ResponseType res = null;

		HttpServletRequest hreq = (HttpServletRequest) request;
		HttpSession session = hreq.getSession();
		userBean = (UserBean) session.getAttribute(SignOnKeys.USER_BEAN);
		currentApplication = (String) hreq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		// System.out.println("LoggingManager fired - " + hreq.getRequestURL());
		if (UserBean.isValid(userBean) && currentApplication != null) {
			if (userBean.getApplicazioni() != null) {
				/*
				 * Richiamo il metodo che aggiunge alla request, se non presenti, i parametri
				 * necessari alla costruzione dei tre livelli del menu.
				 */
				MenuRequestParams.set(hreq);
			}
			/*
			 * Cambio il nome dell'applicazione per individuare in modo più chiaro il
			 * "login" ed il "logout"
			 */
			if (currentApplication.equals(loginApplName)) {
				currentApplication = "login";
			} else {
				if (currentApplication.equals(logoutApplName))
					currentApplication = "logout";
			}
			/*
			 * Faccio il log dell'attività dell'utente
			 */
			userToken = userBean.getUserToken();
			sessionId = session.getId();
			userName = userBean.getUserName();
			userProfile = userBean.getUserProfile();
			codiceSocieta = userBean.getCodiceSocieta() == null ? "" : userBean.getCodiceSocieta();
			codiceUtente = userBean.getCodiceUtente() == null ? "" : userBean.getCodiceUtente();
			chiaveEnte = userBean.getChiaveEnteConsorzio() == null ? "" : userBean.getChiaveEnteConsorzio();
			indirizzoIP = hreq.getRemoteAddr();
			in = new ManagerLogRequestType();
			in.setUserToken(userToken);
			in.setSessionId(sessionId);
			in.setUserName(userName);
			in.setUserProfile(userProfile);
			in.setCodiceSocieta(codiceSocieta);
			in.setCodiceUtente(codiceUtente);
			in.setChiaveEnte(chiaveEnte);
			in.setCanalePagamento("WEB");
			in.setIndirizzoIP(indirizzoIP);
			in.setApplicazione(currentApplication);
			in.setEndSession("N");
			logResponse = WSCache.commonsServer.managerLog(in, hreq);
			res = logResponse.getResponse();
			if (!res.getRetCode().equals(ResponseTypeRetCode.value1))
				System.out.println(" LoggingManager: ERROR - " + res.getRetMessage());
			/*
			 * else { System.out.println(" LoggingManager: sessionID:" + sessionId +
			 * " Applicazione:" + currentApplication + " record:" + (logResponse.isEsito() ?
			 * "1" : "0")); System.out.println(""); }
			 */ }
		// inizio LP PG21X007
		else if (userBean != null) {
			userName = userBean.getUserName();
			userProfile = userBean.getUserProfile();
			codiceSocieta = userBean.getCodiceSocieta();
			codiceUtente = userBean.getCodiceUtente();
			chiaveEnte = userBean.getChiaveEnteConsorzio();
		}
		LogRequest logRequest = makeLogRequest(request, session, "manager", "ManagerLog");
		if (logRequest != null) {
			logRequest.setUserNameCheckEmpty(userName);
			logRequest.setUserProfileCheckEmpty(userProfile);
			logRequest.setCodiceSocietaCheckEmpty(codiceSocieta);
			logRequest.setCodiceUtenteCheckEmpty(codiceUtente);
			if (logRequest.setChiaveEnteCheckEmpty(chiaveEnte)) {
				setInfoEnte(chiaveEnte, logRequest, hreq);
			}
			logRequest.setCanalePagamento("WEB"); // per uniformita' con logAccessi
			logRequest.setSezioneApplicativaCheckEmpty(currentApplication);
			parseAttributiRequest(logRequest, hreq);
			// LP PG21XX07 - 20211230 System.out.println("logRequest = " +
			// logRequest.toString());
			Entity<LogRequest> entity = Entity.entity(logRequest, MediaType.APPLICATION_JSON);
			// LP PG21XX07 - 20211230 System.out.println("entity = " + entity.toString());

			String uri = ManagerStarter.configuration
					.getProperty(PropertiesPath.wsLogRequest.format(PropertiesPath.defaultnode.format()));
			uri += "/save";
			// LP PG21XX07 - 20211230 System.out.println("-uri: " + uri);
//inizio LP PG22XX01
//			Client client = ClientBuilder.newClient();
//			// LP PG21XX07 - 20211230 System.out.println("inizio chiamata prova log request WS");
//			WebTarget target = client.target(uri);
//			// LP PG21XX07 - 20211230 System.out.println("-target uri= " + target.getUri());
//			Builder builder = target.request(MediaType.APPLICATION_JSON);
//			Response responseWS = builder.post(entity);
//			if(responseWS != null) {
//				// LP PG21XX07 - 20211230 System.out.println("-response = " + responseWS.getStatus());
//				// LP PG21XX07 - 20211230 System.out.println("-response info = " + responseWS.getStatusInfo());
//				if (responseWS.getStatus() == Status.OK.getStatusCode()) {
//					// LP PG21XX07 - 20211230 System.out.println("status ok");
//				}
//				responseWS.close();
//			}
			WsLogRequestThread wsLogRequestThread = new WsLogRequestThread(uri, entity);
			Thread thread = new Thread(wsLogRequestThread);
			thread.start();			
//fine LP PG22XX01

		}
		// fine LP PG21X007
		chain.doFilter(request, response);
		return;
	}

	public void init(FilterConfig arg0) throws ServletException {

	}

	// inizio LP PG21X007
	private LogRequest makeLogRequest(ServletRequest request, HttpSession session, String app, String operatore) {
		LogRequest logRequest = new LogRequest();

		String dbSchemaCodSocieta = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		// LP PG21XX07 - 20211230 System.out.println("dbSchemaCodSocieta = " +
		// dbSchemaCodSocieta);
		String template = ManagerStarter.configuration
				.getProperty(PropertiesPath.templateBase.format() + request.getServerName());
		// LP PG21XX07 - 20211230 System.out.println("template = " + template);
		if (dbSchemaCodSocieta == null && template != null) {
			dbSchemaCodSocieta = ManagerStarter.configuration.getProperty(PropertiesPath.societa.format(template));
			// LP PG21XX07 - 20211230 System.out.println("dbSchemaCodSocieta(template) = " +
			// dbSchemaCodSocieta);
		}
		HttpDefenseRequest reqLoc = null;
		MAFRequest mafReq = null;
//		YLM PG22XX06 INIZIO MODIFICA PER ERRORE CAST
		
//		try {
//			reqLoc = (HttpDefenseRequest) request;
//		} catch (ClassCastException e) {
//			e.printStackTrace();
//			System.out.println(
//					"============================ makeLogRequest ClassCastException (HttpDefenseRequest) ==========================================> Errore: "
//							+ e.getMessage());
//			try {
//				mafReq = new MAFRequest(request);
//			} catch (ClassCastException e1) {
//				e.printStackTrace();
//				System.out.println(
//						"============================ makeLogRequest ClassCastException (MAFRequest) =================================================> Errore: "
//								+ e.getMessage());
//				return null;
//			} catch (Exception e1) {
//				e.printStackTrace();
//				System.out.println(
//						"============================ makeLogRequest ClassCastException (MAFRequest) =================================================> Errore: "
//								+ e.getMessage());
//				return null;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println(
//					"============================ makeLogRequest ClassCastException (HttpDefenseRequest) ==========================================> Errore: "
//							+ e.getMessage());
//			return null;
		
//		}
		try {
			mafReq = new MAFRequest(request);
		} catch (ClassCastException e1) {
			e1.printStackTrace();
			System.out.println(
					"============================ makeLogRequest ClassCastException (MAFRequest) =================================================> Errore: "
							+ e1.getMessage());
			return null;
		} catch (Exception e1) {
			e1.printStackTrace();
			System.out.println(
					"============================ makeLogRequest Exception (MAFRequest) =================================================> Errore: "
							+ e1.getMessage());
			return null;
		}
		
		reqLoc = mafReq.getHttpDefenseRequest();
		if (reqLoc == null) {
			System.out.println("=== makeLogRequest (reqLoc->mafReq.getHttpDefenseRequest() ) ===> Warning: reqLoc NULL");
			
		}
		
//		if (mafReq == null) {
//			mafReq = new MAFRequest(request);
//		}
//		// System.out.println("mafReq.getCurrentURL() = " + mafReq.getCurrentURL());
//		// System.out.println("mafReq.getAttributeMap() = " +
//		// mafReq.getAttributeMap().toString());
//		// System.out.println("mafReq.getTargetURL() = " + mafReq.getTargetURL());
//		if (reqLoc == null) {
//			reqLoc = mafReq.getHttpDefenseRequest();
//		}

//		YLM PG22XX06 FINE
		
		String indirizzoIP = reqLoc.getRemoteAddr();
//inizio LP PG21X007 - 20211230
//		System.out.println(" ================================================================================================================================================ ");
//		System.out.println(" ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ ");
//		System.out.println(" ================================================================================================================================================ ");
//		System.out.println("getRemoteAddr = " + indirizzoIP);
//		//System.out.println("getEncodedParameters = " + reqLoc.getEncodedParameters());
//		System.out.println("getLocalAddr() = " + reqLoc.getLocalAddr());
//		System.out.println("getLocalName() = " + reqLoc.getLocalName());
//		System.out.println("getLocalPort() = " + reqLoc.getLocalPort());
//		//System.out.println("getRequest() = " + reqLoc.getRequest());
//		System.out.println("getCharacterEncoding() = " + reqLoc.getCharacterEncoding());
//		System.out.println("getContextPath() = " + reqLoc.getContextPath());
//		System.out.println("getPathInfo() = " + reqLoc.getPathInfo());
//		System.out.println("getPathTranslated() = " + reqLoc.getPathTranslated());
//		System.out.println("getRemoteHost() = " + reqLoc.getRemoteHost());
//		System.out.println("getRemotePort() = " + reqLoc.getRemotePort());
//		System.out.println("getRemoteUser() = " + reqLoc.getRemoteUser());
//		System.out.println("getServerName() = " + reqLoc.getServerName());
//		System.out.println("getServerPort() = " + reqLoc.getServerPort());
//		System.out.println("getServletPath() = " + reqLoc.getServletPath());
//		System.out.println("getLocale() = " + (reqLoc.getLocale() != null ? reqLoc.getLocale().toString() : "N.D."));
//		System.out.println("getUserPrincipal()) = " + (reqLoc.getUserPrincipal() != null ? reqLoc.getUserPrincipal().toString() : "N.D"));
//		System.out.println(" ================================================================================================================================================ ");
//		System.out.println(" ------------------------------------------------------------------------------------------------------------------------------------------------ ");
//		System.out.println(" ================================================================================================================================================ ");	
//		String sessionId = reqLoc.getRequestedSessionId();
//		System.out.println("getRequestedSessionId = " + sessionId);
		String sessionId = session.getId();
//fine LP PG21X007 - 20211230
		String tipoRequest = reqLoc.getMethod();
		// LP PG21XX07 - 20211230 System.out.println("getMethod = " + tipoRequest);
		// String action = reqLoc.getRequestURI();
		// LP PG21XX07 - 20211230 System.out.println("getRequestURI = " +
		// reqLoc.getRequestURI());
		String action = mafReq.getTargetURL();
		if (action != null) {
			action = action.replace(".do", "");
		}
		// LP PG21XX07 - 20211230 System.out.println("action = " + action);
		String queryString = reqLoc.getQueryString();
		// LP PG21XX07 - 20211230 System.out.println("getQueryString = " + queryString);
		String mapParam = reqLoc.getParameterMap().toString();
		// LP PG21XX07 - 20211230 System.out.println("getParameterMap = " + mapParam);
		String sRequest = reqLoc.getRequestURL().toString();
		// LP PG21XX07 - 20211230 System.out.println("getRequestURL = " + sRequest);

		logRequest.setDbSchemaCodSocieta(dbSchemaCodSocieta);
		logRequest.setTemplate(template);
		if (indirizzoIP != null)
			logRequest.setIndirizzoIP(indirizzoIP);
		logRequest.setSessionID(sessionId);
		logRequest.setTipoRequest(tipoRequest);
		logRequest.setAction(action);
		logRequest.setApplicativo(app);
		// logRequest.setCodiceFiscale("");
		// logRequest.setNumeroBollettino("");
		// logRequest.setNumeroDocumento("");
		if (queryString == null) {
			queryString = "";
		}
		logRequest.setQueryString(queryString);
		if (mapParam == null || mapParam.equals("{}")) {
			mapParam = "";
		} else {
			mapParam = mapParam.substring(1, mapParam.length() - 1);
		}
		logRequest.setParam(mapParam);
		if (sRequest == null) {
			sRequest = "";
		}
		logRequest.setRequest(sRequest);
		logRequest.setOperatoreRequest(operatore);
		return logRequest;
	}

	private String getNomeEnte(String chiaveEnte, HttpServletRequest request) {
		if (chiaveEnte != null && chiaveEnte.trim().length() > 0) {
			try {
				AnagEnteDetailRequest detailRequest = new AnagEnteDetailRequest();
				detailRequest.setChiaveEnte(chiaveEnte.trim());
				AnagEnteDetailResponse res = WSCache.anagEnteServer.getAnagEnte(detailRequest, request);
				return res.getAnagente().getDescrizioneEnte();
			} catch (Exception e) {
			}
		}
		return null;
	}

	private boolean setInfoEnte(String chiaveEnte, LogRequest logRequest, HttpServletRequest request) {
		boolean bOk = false;
		String codiceSocieta = null;
		String codiceEnte = null;
		String descrizioneEnte = null;
		String codiceUtente = null;
		String codiceUfficio = null;
		String tipoUfficio = null;
		WebRowSet wrs = null;
		if (chiaveEnte != null && chiaveEnte.trim().length() > 0) {
			try {
				// LP PG21XX07 - 20211230 System.out.println("============================
				// inizio recuperaEnteUtenteSocieta_Ane
				// =================================================>");
				RecuperaEnteUtenteSocietaAneResponse res = WSCache.commonsServer
						.recuperaEnteUtenteSocieta_Ane(new RecuperaEnteUtenteSocietaAneRequest(chiaveEnte), request);
				if (res != null) {
					if (res.getRetCode().equals("00") && !res.getListXml().equals("")) {
						wrs = Convert.stringToWebRowSet(res.getListXml());
						if (wrs != null) {
							if (wrs.next()) {
								codiceSocieta = wrs.getString(1).trim();
								codiceUtente = wrs.getString(2).trim();
								codiceEnte = wrs.getString(4).trim();
								tipoUfficio = wrs.getString(5).trim();
								codiceUfficio = wrs.getString(6).trim();
								descrizioneEnte = wrs.getString(7).trim();
								if (logRequest.setCodiceSocietaCheckEmpty(codiceSocieta)) {
									// LP PG21XX07 - 20211230 System.out.println("============================
									// setInfoEnte =================================================> codiceSocieta:
									// " + codiceSocieta);
								}
								if (logRequest.setCodiceUtenteCheckEmpty(codiceUtente)) {
									// LP PG21XX07 - 20211230 System.out.println("============================
									// setInfoEnte =================================================> codiceUtente:
									// " + codiceUtente);
								}
								if (logRequest.setDescrizioneEnteCheckEmpty(descrizioneEnte)) {
									// LP PG21XX07 - 20211230 System.out.println("============================
									// setInfoEnte =================================================>
									// descrizioneEnte: " + descrizioneEnte);
								}
								if (logRequest.getSiglaProvinciaEnte() == null
										|| logRequest.getSiglaProvinciaEnte().trim().length() == 0) {
									setInfoSiglaProvincia(codiceUtente, codiceEnte, tipoUfficio, codiceUfficio,
											logRequest, request);
								}
								bOk = true;
							} else {
								// LP PG21XX07 - 20211230 System.out.println("Chiave ente non configurata");
							}
						}
					} else {
						// LP PG21XX07 - 20211230 System.out.println("Errore setInfoEnte: " +
						// res.getRetMessage());
					}
					// LP PG21XX07 - 20211230 System.out.println("============================ fine
					// recuperaEnteUtenteSocieta_Ane
					// =================================================>");
				}
			} catch (Exception e) {
				System.out.println("Errore setInfoEnte exception: " + e.getMessage());
			} finally {
				if (wrs != null) {
					try {
						wrs.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return bOk;
	}

	private void setInfoSiglaProvincia(String codiceUtente, String codiceEnte, String tipoUfficio, String codiceUfficio,
			LogRequest logRequest, HttpServletRequest request) {
		WebRowSet wrs = null;
		//inizio LP PG22XX04
		if(codiceUtente == null || codiceEnte == null || tipoUfficio == null || codiceUfficio == null)
			return;
		//fine LP PG22XX04
		// LP PG21XX07 - 20211230 System.out.println("============================
		// setInfoSiglaProvincia =================================================>
		// codiceUtente: " + codiceUtente);
		// LP PG21XX07 - 20211230 System.out.println("============================
		// setInfoSiglaProvincia =================================================>
		// codiceEnte: " + codiceEnte);
		// LP PG21XX07 - 20211230 System.out.println("============================
		// setInfoSiglaProvincia =================================================>
		// tipoUfficio: " + tipoUfficio);
		// LP PG21XX07 - 20211230 System.out.println("============================
		// setInfoSiglaProvincia =================================================>
		// codiceUfficio: " + codiceUfficio);
		try {
			RecuperaEnteUtenteSocietaProvinciaRequest req = new RecuperaEnteUtenteSocietaProvinciaRequest();
			req.setCodiceUtente(codiceUtente);
			req.setCodiceEnte(codiceEnte);
			req.setTipoUfficio(tipoUfficio);
			req.setCodiceUfficio(codiceUfficio);
			// LP PG21XX07 - 20211230 System.out.println("============================
			// inizio recuperaEnteUtenteSocietaProvincia
			// =================================================>");
			RecuperaEnteUtenteSocietaProvinciaResponse res1 = WSCache.commonsServer
					.recuperaEnteUtenteSocietaProvincia(req, request);

			if (res1 != null) {
				if (res1.getRetCode().equals("00") && !res1.getListXml().equals("")) {
					wrs = Convert.stringToWebRowSet(res1.getListXml());
					if (wrs != null) {
						if (wrs.next()) {
							// codSocieta = wrs.getString(1).trim();
							// chiaveEnte = wrs.getString(2).trim();
							// descrProvincia = wrs.getString(3).trim();
							logRequest.setSiglaProvinciaEnteCheckEmpty(wrs.getString(4));
							// LP PG21XX07 - 20211230 System.out.println("============================
							// setInfoSiglaProvincia =================================================>
							// siglaProvincia: " + logRequest.getSiglaProvinciaEnte());
						}
					}
				}
			}
			// LP PG21XX07 - 20211230 System.out.println("============================ fine
			// recuperaEnteUtenteSocietaProvincia
			// =================================================>");
		} catch (Exception e) {
			System.out.println("Errore setInfoSiglaProvincia exception: " + e.getMessage());
		} finally {
			if (wrs != null) {
				try {
					wrs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return;
	}

	private void parseAttributiRequest(LogRequest logRequest, HttpServletRequest request) {
		boolean bAncheParam = true;
		if (logRequest.getChiaveEnte() == null || logRequest.getChiaveEnte().trim().length() == 0) {
			String[] tag = { "ddlSocietaUtenteEnte", "configutentetiposervizioente_strEntetiposervizios", "ddlEnteJS",
					"tx_UtenteEnte", "elaborazioni_searchchiaveEnte", "codente", "chiaveEnte", "anagente_chiaveEnte",
					"configutentetiposervizioente_chiaveEnte", "payerCodiceEnte" };
			for (int i = 0; i < tag.length; i++) {
				if (setInfoEnteRequest(tag[i], bAncheParam, logRequest, request)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// parseAttributiRequest =================================================>
					// chiaveEnte: " + logRequest.getChiaveEnte());
					break;
				}
			}
		}

		if (logRequest.getSiglaProvinciaEnte() == null || logRequest.getSiglaProvinciaEnte().trim().length() == 0) {
			String[] tag = { "tx_provincia", "elaborazioni_siglaProvincia", "prov" };
			for (int i = 0; i < tag.length; i++) {
				if (setSiglaProvinciaEnteRequest(tag[i], bAncheParam, logRequest, request)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// parseAttributiRequest =================================================>
					// siglaProvincia: " + logRequest.getSiglaProvinciaEnte());
					break;
				}
			}
		}

		if (logRequest.getSiglaProvinciaRequest() == null
				|| logRequest.getSiglaProvinciaRequest().trim().length() == 0) {
			String[] tag = { "ddlProvinciaRequest", "ddlProvincia", "ddlProvinciaResidenza", "tx_provres" };
			for (int i = 0; i < tag.length; i++) {
				if (setSiglaProvinciaRequestRequest(tag[i], bAncheParam, logRequest, request)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// parseAttributiRequest =================================================>
					// siglaProvinciaRequest: " + logRequest.getSiglaProvinciaRequest());
					break;
				}
			}
		}
		if (logRequest.getComuneRequest() == null || logRequest.getComuneRequest().trim().length() == 0) {
			String[] tag = { "ddlComuneRequest", "codEnteComuneDomicilioFiscale", "ddlComune",
					"anagprovcom_codiceBelfiore", "txtCodBelfiore", "ddlComuneResidenza", "tx_comures" };
			for (int i = 0; i < tag.length; i++) {
				if (setComuneRequestRequest(tag[i], bAncheParam, logRequest, request)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// parseAttributiRequest =================================================>
					// comuneRequest: " + logRequest.getComuneRequest());
					break;
				}
			}
		}
		if (logRequest.getCodiceUtente() == null || logRequest.getCodiceUtente().trim().length() == 0) {
			String[] tag = { "codute", "codutente", "tx_utente", "elaborazioni_searchuserCode",
					"configutentetiposervizioente_codiceUtente", "configutentetiposervizio_strUtentetiposervizios",
					"configutentetiposervizio_codiceUtente" };
			for (int i = 0; i < tag.length; i++) {
				if (setCodiceUtenteRequest(tag[i], bAncheParam, logRequest, request)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// parseAttributiRequest =================================================>
					// codiceUtente: " + logRequest.getCodiceUtente());
					break;
				}
			}
		}
		if (logRequest.getCodiceSocieta() == null || logRequest.getCodiceSocieta().trim().length() == 0) {
			String[] tag = { "tx_societa", "codsoc", "tx_utente", "codiceSocieta", "elaborazioni_searchuserCode",
					"configutentetiposervizioente_companyCode", "configutentetiposervizio_companyCode" };
			for (int i = 0; i < tag.length; i++) {
				if (setCodiceSocietaRequest(tag[i], bAncheParam, logRequest, request)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// parseAttributiRequest =================================================>
					// codiceSocieta: " + logRequest.getCodiceSocieta());
					break;
				}
			}
		}
		if (logRequest.getCodiceFiscale() == null || logRequest.getCodiceFiscale().trim().length() == 0) {
			String[] tag = { "codFiscale", "codFisc", "tbEleCodFiscale", "txtCodFiscale", "tx_codFisc", "txtPartitaIVA",
					"tx_codfis", "CF" };
			for (int i = 0; i < tag.length; i++) {
				if (setCodiceFiscaleRequest(tag[i], bAncheParam, logRequest, request)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// parseAttributiRequest =================================================>
					// codiceFiscale: " + logRequest.getCodiceFiscale());
					break;
				}
			}
		}
		if (logRequest.getNumeroBollettino() == null || logRequest.getNumeroBollettino().trim().length() == 0) {
			String[] tag = { "numeroBollettino", "tx_id_bollettino", "tbEleBollettino", "txtNumBollettino", "numboll",
					"idBoll", "tx_numbol", "tbEleBollettinoFreccia", "payerNumeroBollettino" };
			for (int i = 0; i < tag.length; i++) {
				if (setNumBollettinoRequest(tag[i], bAncheParam, logRequest, request)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// parseAttributiRequest =================================================>
					// numeroBollettino: " + logRequest.getNumeroBollettino());
					break;
				}
			}
		}
		if (logRequest.getNumeroDocumento() == null || logRequest.getNumeroDocumento().trim().length() == 0) {
			String[] tag = { "numDocumento", "txtNumeroDocumento", "tx_idDocumento", "tbEleDocumento", "tbNumDoc",
					"numdoc", "numDoc", "id_documento", "tx_numdoc", "payerNumeroDocumento" };
			for (int i = 0; i < tag.length; i++) {
				if (setNumDocumentoRequest(tag[i], bAncheParam, logRequest, request)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// parseAttributiRequest =================================================>
					// numeroDocumento: " + logRequest.getNumeroDocumento());
					break;
				}
			}
		}
		if (logRequest.getChiaveTransazione() == null || logRequest.getChiaveTransazione().trim().length() == 0) {
			String[] tag = { "tx_codice_transazione", "tx_codice_transazione_hidden", "txtIdTransazione",
					"chiaveTransazione", "tx_keytra" };
			for (int i = 0; i < tag.length; i++) {
				if (setChiaveTransazioneRequest(tag[i], bAncheParam, logRequest, request)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// parseAttributiRequest =================================================>
					// numeroDocumento: " + logRequest.getChiaveTransazione());
					break;
				}
			}
		}
		if (logRequest.getNumeroIUV() == null || logRequest.getNumeroIUV().trim().length() == 0) {
			String[] tag = { "tx_codice_IUV", "numeroIUV", "CodiceIUV", "tx_idIuv_hidden", "iuvnodospc", "tx_numiuv",
					"payerNumBollettino" };
			for (int i = 0; i < tag.length; i++) {
				if (setNumeroIUVRequest(tag[i], bAncheParam, logRequest, request)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// parseAttributiRequest =================================================>
					// numeroDocumento: " + logRequest.getNumeroIUV());
					break;
				}
			}
		}
	}

	private String getAttribute(String nomeAtt, boolean bAncheParam, HttpServletRequest request) {
		String appo = (String) request.getAttribute(nomeAtt);
		if (appo != null && appo.trim().length() > 0) {
			return appo.trim();
		}
		if (bAncheParam) {
			appo = (String) request.getParameter(nomeAtt);
			if (appo != null && appo.trim().length() > 0) {
				return appo.trim();
			}
		}
		return null;
	}

	private boolean setNumBollettinoRequest(String nomeAtt, boolean bAncheParam, LogRequest logRequest,
			HttpServletRequest request) {
		String appo = getAttribute(nomeAtt, bAncheParam, request);
		if (appo != null) {
			logRequest.setNumeroBollettinoCheckEmpty(appo);
			return true;
		}
		return false;
	}

	private boolean setCodiceFiscaleRequest(String nomeAtt, boolean bAncheParam, LogRequest logRequest,
			HttpServletRequest request) {
		String appo = getAttribute(nomeAtt, bAncheParam, request);
		if (appo != null) {
			logRequest.setCodiceFiscaleCheckEmpty(appo);
			return true;
		}
		return false;
	}

	private boolean setCodiceUtenteRequest(String nomeAtt, boolean bAncheParam, LogRequest logRequest,
			HttpServletRequest request) {
		String appo = getAttribute(nomeAtt, bAncheParam, request);
		if (appo != null) {
			if (nomeAtt.equals("tx_utente") || nomeAtt.equals("elaborazioni_searchuserCode")) {
				// tx_utente=00004000OP
				if (appo.length() == 10) {
					logRequest.setCodiceSocietaCheckEmpty(appo.substring(0, 5));
					return logRequest.setCodiceUtenteCheckEmpty(appo.substring(5));
				}
			} else if (nomeAtt.equals("configutentetiposervizio_strUtentetiposervizios")) {
				// configutentetiposervizio_strUtentetiposervizios=00004|000OP
				if (appo.length() == 11) {
					logRequest.setCodiceSocietaCheckEmpty(appo.substring(0, 5));
					return logRequest.setCodiceUtenteCheckEmpty(appo.substring(6));
				}
			} else
				return logRequest.setCodiceUtenteCheckEmpty(appo);
		}
		return false;
	}

	private boolean setCodiceSocietaRequest(String nomeAtt, boolean bAncheParam, LogRequest logRequest,
			HttpServletRequest request) {
		String appo = getAttribute(nomeAtt, bAncheParam, request);
		if (appo != null) {
			if (nomeAtt.equals("tx_utente") || nomeAtt.equals("elaborazioni_searchuserCode")) {
				// tx_utente=00004000OP
				if (appo.length() == 10) {
					logRequest.setCodiceUtenteCheckEmpty(appo.substring(5));
					return logRequest.setCodiceSocietaCheckEmpty(appo.substring(0, 5));
				}
			} else
				return logRequest.setCodiceSocietaCheckEmpty(appo);
		}
		return false;
	}

	private boolean setNumDocumentoRequest(String nomeAtt, boolean bAncheParam, LogRequest logRequest,
			HttpServletRequest request) {
		String appo = getAttribute(nomeAtt, bAncheParam, request);
		if (appo != null) {
			logRequest.setNumeroDocumentoCheckEmpty(appo);
			return true;
		}
		return false;
	}

	private boolean setChiaveTransazioneRequest(String nomeAtt, boolean bAncheParam, LogRequest logRequest,
			HttpServletRequest request) {
		String appo = getAttribute(nomeAtt, bAncheParam, request);
		if (appo != null) {
			logRequest.setChiaveTransazioneCheckEmpty(appo);
			return true;
		}
		return false;
	}

	private boolean setNumeroIUVRequest(String nomeAtt, boolean bAncheParam, LogRequest logRequest,
			HttpServletRequest request) {
		String appo = getAttribute(nomeAtt, bAncheParam, request);
		if (appo != null) {
			logRequest.setNumeroIUVCheckEmpty(appo);
			return true;
		}
		return false;
	}

	private boolean setSiglaProvinciaEnteRequest(String nomeAtt, boolean bAncheParam, LogRequest logRequest,
			HttpServletRequest request) {
		String appo = getAttribute(nomeAtt, bAncheParam, request);
		if (appo != null) {
			logRequest.setSiglaProvinciaEnteCheckEmpty(appo);
			return true;
		}
		return false;
	}

	/*
	 * private boolean setDescrizioneEnteRequest(String nomeAtt, boolean
	 * bAncheParam, LogRequest logRequest, HttpServletRequest request) { String appo
	 * = getAttribute(nomeAtt, bAncheParam, request); if(appo != null) { String[]
	 * sCodeEnte_Desc = GenericsDateNumbers.getSplit_NString(3, appo);
	 * if(sCodeEnte_Desc[0].trim().length() > 0) {
	 * logRequest.setDescrizioneEnteCheckEmpty(sCodeEnte_Desc[0].trim());
	 * if(sCodeEnte_Desc[2].trim().length() > 0 && logRequest.getChiaveEnte() ==
	 * null) { setInfoEnteBelf(sCodeEnte_Desc[2].trim(), logRequest, request); }
	 * return true; } } return false; }
	 */
	private boolean setComuneRequestRequest(String nomeAtt, boolean bAncheParam, LogRequest logRequest,
			HttpServletRequest request) {
		String appo = getAttribute(nomeAtt, bAncheParam, request);
		if (appo != null) {
			if (nomeAtt.equals("codEnteComuneDomicilioFiscale") || nomeAtt.equals("ddlComune")
					|| nomeAtt.equals("anagprovcom_codiceBelfiore") || nomeAtt.equals("txtCodBelfiore")) {
				return setInfoComuneDaBelf(appo, logRequest, request);
			}
			if (nomeAtt.equals("ddlComuneRequest") || nomeAtt.equals("ddlComuneResidenza")) {
				String[] sCodeEnte_Desc = GenericsDateNumbers.getSplit_NString(3, appo);
				appo = sCodeEnte_Desc[2];
				return setInfoComuneDaBelf(appo, logRequest, request);
			}
			if (nomeAtt.equals("tx_comures")) {
				String[] sCodeEnte_Desc = GenericsDateNumbers.getSplit_NString(2, appo);
				appo = sCodeEnte_Desc[1];
				return setInfoComuneDaBelf(appo, logRequest, request);
			}
		}
		return false;
	}

	private boolean setSiglaProvinciaRequestRequest(String nomeAtt, boolean bAncheParam, LogRequest logRequest,
			HttpServletRequest request) {
		String appo = getAttribute(nomeAtt, bAncheParam, request);
		if (appo != null) {
			logRequest.setSiglaProvinciaRequestCheckEmpty(appo);
			return true;
		}
		return false;
	}

	private boolean setInfoEnteRequest(String nomeAtt, boolean bAncheParam, LogRequest logRequest,
			HttpServletRequest request) {
		String appo = getAttribute(nomeAtt, bAncheParam, request);
		if (appo != null) {
			appo = appo.trim();
			if (nomeAtt.equals("ddlSocietaUtenteEnte")
					|| nomeAtt.equals("configutentetiposervizioente_strEntetiposervizios")) {
				// ddlSocietaUtenteEnte=00004|000TO|ANE0000409|[99225--] AGENZIA FORESTE
				// DEMANIALI
				// ddlSocietaUtenteEnte=00004|000TO|ANE0000407,
				// configutentetiposervizioente_strEntetiposervizios=00001|00058|ANE0000005
				// LP PG21XX07 - 20211230 System.out.println("============================
				// setInfoEnteRequest =================================================>
				// ddlSocietaUtenteEnte: " + appo);
				String[] sCodeEnte_Desc = appo.split("\\|", -1);
				if (sCodeEnte_Desc.length == 4 || sCodeEnte_Desc.length == 3) {
					String codiceSocieta = sCodeEnte_Desc[0].trim();
					if (logRequest.setCodiceSocietaCheckEmpty(codiceSocieta)) {
						// LP PG21XX07 - 20211230 System.out.println("============================
						// setInfoEnteRequest =================================================>
						// codiceSocieta: " + codiceSocieta);
					}
					String codiceUtente = sCodeEnte_Desc[1].trim();
					if (logRequest.setCodiceUtenteCheckEmpty(codiceUtente)) {
						// LP PG21XX07 - 20211230 System.out.println("============================
						// setInfoEnteRequest =================================================>
						// codiceUtente: " + codiceUtente);
					}
					if (sCodeEnte_Desc.length == 4
							&& logRequest.setDescrizioneEnteCheckEmpty(sCodeEnte_Desc[3].trim())) {
						// LP PG21XX07 - 20211230 System.out.println("============================
						// setInfoEnteRequest =================================================>
						// descrizioneEnte: " + logRequest.getDescrizioneEnte());
					}
					String chiaveEnte = sCodeEnte_Desc[2];
					if (logRequest.setChiaveEnteCheckEmpty(chiaveEnte)) {
						return setInfoEnte(chiaveEnte, logRequest, request);
					}
				}
			}
			if (nomeAtt.equals("ddlEnteJS")) {
				// LP PG21XX07 - 20211230 System.out.println("============================
				// setInfoEnteRequest =================================================>
				// ddlEnteJS: " + appo);
				String[] sCodeEnte_Desc = appo.split("\\|", -1);
				if (sCodeEnte_Desc.length == 7 || sCodeEnte_Desc.length == 6) {
					String codiceSocieta = sCodeEnte_Desc[0].trim();
					if (logRequest.setCodiceSocietaCheckEmpty(codiceSocieta)) {
						// LP PG21XX07 - 20211230 System.out.println("============================
						// setInfoEnteRequest =================================================>
						// codiceSocieta: " + codiceSocieta);
					}
					String codiceUtente = sCodeEnte_Desc[1].trim();
					if (logRequest.setCodiceUtenteCheckEmpty(codiceUtente)) {
						// LP PG21XX07 - 20211230 System.out.println("============================
						// setInfoEnteRequest =================================================>
						// codiceUtente: " + codiceUtente);
					}
					int lenDesc2 = sCodeEnte_Desc[2].trim().length();
					if (sCodeEnte_Desc.length == 7 && lenDesc2 == 5) {
						// ddlEnteJS=00004|000TO|99666| | |Comune di Cornedo |014118
						if (logRequest.setDescrizioneEnteCheckEmpty(sCodeEnte_Desc[5])) {
							// LP PG21XX07 - 20211230 System.out.println("============================
							// setInfoEnteRequest =================================================>
							// descrizioneEnte: " + logRequest.getDescrizioneEnte());
						}
						String codiceEnte = sCodeEnte_Desc[2].trim();
						String tipoUfficio = sCodeEnte_Desc[3].trim();
						String codiceUfficio = sCodeEnte_Desc[4].trim();
						return setInfoEnteDaCodice(codiceUtente, codiceEnte, tipoUfficio, codiceUfficio, logRequest,
								request);
					} else if (sCodeEnte_Desc.length == 6 && lenDesc2 == 10) {
						// ddlEnteJS=00004|000TO|ANE0000002|Comune di Aldino / Comune di Aldino
						// DE|39040|55551
						String chiaveEnte = sCodeEnte_Desc[2];
						if (logRequest.setChiaveEnteCheckEmpty(chiaveEnte)) {
							return setInfoEnte(chiaveEnte, logRequest, request);
						}
					}
				}
			}
			if ((nomeAtt.equals("tx_UtenteEnte") || nomeAtt.equals("elaborazioni_searchchiaveEnte"))
					&& appo.length() == 20) {
				// tx_UtenteEnte=0000100058ANE0000039
				// LP PG21XX07 - 20211230 System.out.println("============================
				// setInfoEnteRequest =================================================> " +
				// nomeAtt + ": " + appo);
				String codiceSocieta = appo.substring(0, 5);
				if (logRequest.setCodiceSocietaCheckEmpty(codiceSocieta)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// setInfoEnteRequest =================================================>
					// codiceSocieta: " + codiceSocieta);
				}
				String codiceUtente = appo.substring(5, 10);
				if (logRequest.setCodiceUtenteCheckEmpty(codiceUtente)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// setInfoEnteRequest =================================================>
					// codiceUtente: " + codiceUtente);
				}
				String chiaveEnte = appo.substring(10);
				if (logRequest.setChiaveEnteCheckEmpty(chiaveEnte)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// setInfoEnteRequest =================================================>
					// chiaveEnte: " + chiaveEnte);
					return setInfoEnte(chiaveEnte, logRequest, request);
				}
			}
			if (nomeAtt.equals("codente")) {
				// LP PG21XX07 - 20211230 System.out.println("============================
				// setInfoEnteRequest =================================================>
				// codente: " + appo);
				if (logRequest.setChiaveEnteCheckEmpty(appo)) {
					return setInfoEnte(appo, logRequest, request);
				} else if (!(appo.length() == 10 && appo.substring(0, 3).equals("ANE"))) {
					String codiceUtente = getAttribute("codutente", bAncheParam, request);
					String codiceEnte = appo;
					String tipoUfficio = getAttribute("tipoufficio", bAncheParam, request);
					String codiceUfficio = getAttribute("codufficio", bAncheParam, request);
					return setInfoEnteDaCodice(codiceUtente, codiceEnte, tipoUfficio, codiceUfficio, logRequest,
							request);
				}
			}
			if (nomeAtt.equals("payerCodiceEnte")) {
				// LP PG21XX07 - 20211230 System.out.println("============================
				// setInfoEnteRequest =================================================>
				// codente: " + appo);
				if (logRequest.setChiaveEnteCheckEmpty(appo)) {
					return setInfoEnte(appo, logRequest, request);
				} else if (!(appo.length() == 10 && appo.substring(0, 3).equals("ANE"))) {
					String codiceUtente = getAttribute("payerCodiceUtente", bAncheParam, request);
					String codiceEnte = appo;
					String tipoUfficio = getAttribute("payerTipoUfficio", bAncheParam, request);
					String codiceUfficio = getAttribute("payerCodiceUfficio", bAncheParam, request);
					return setInfoEnteDaCodice(codiceUtente, codiceEnte, tipoUfficio, codiceUfficio, logRequest,
							request);
				}
			}
			if (nomeAtt.equals("chiaveEnte") || nomeAtt.equals("anagente_chiaveEnte")
					|| nomeAtt.equals("configutentetiposervizioente_chiaveEnte")) {
				if (logRequest.setChiaveEnteCheckEmpty(appo)) {
					// LP PG21XX07 - 20211230 System.out.println("============================
					// setInfoEnteRequest =================================================> " +
					// nomeAtt + ": " + appo);
					return setInfoEnte(appo, logRequest, request);
				}
			}
		}
		return false;
	}

	private boolean setInfoEnteDaCodice(String codiceUtente, String codiceEnte, String tipoUfficio,
			String codiceUfficio, LogRequest logRequest, HttpServletRequest request) {
		// LP PG21XX07 - 20211230 System.out.println("============================
		// inizio setInfoEnteDaCodice
		// =================================================>");
		boolean bOk = false;
		WebRowSet wrs = null;
		//inizio LP PG22XX04
		if(codiceUtente == null || codiceEnte == null || tipoUfficio == null || codiceUfficio == null)
			return false;
		//fine LP PG22XX04
		// LP PG21XX07 - 20211230 System.out.println("============================
		// setInfoEnteDaCodice =================================================>
		// codiceUtente: " + codiceUtente);
		// LP PG21XX07 - 20211230 System.out.println("============================
		// setInfoEnteDaCodice =================================================>
		// codiceEnte: " + codiceEnte);
		// LP PG21XX07 - 20211230 System.out.println("============================
		// setInfoEnteDaCodice =================================================>
		// tipoUfficio: " + tipoUfficio);
		// LP PG21XX07 - 20211230 System.out.println("============================
		// setInfoEnteDaCodice =================================================>
		// codiceUfficio: " + codiceUfficio);
		try {
			if (logRequest.setCodiceUtenteCheckEmpty(codiceUtente)) {
				// LP PG21XX07 - 20211230 System.out.println("============================
				// setInfoEnteDaCodice =================================================>
				// codiceUtente: " + codiceUtente);
			}
			RecuperaEnteUtenteSocietaProvinciaRequest req = new RecuperaEnteUtenteSocietaProvinciaRequest();
			req.setCodiceUtente(codiceUtente);
			req.setCodiceEnte(codiceEnte);
			req.setTipoUfficio(tipoUfficio);
			req.setCodiceUfficio(codiceUfficio);
			// LP PG21XX07 - 20211230 System.out.println("============================
			// inizio recuperaEnteUtenteSocietaProvincia
			// =================================================>");
			RecuperaEnteUtenteSocietaProvinciaResponse res = WSCache.commonsServer
					.recuperaEnteUtenteSocietaProvincia(req, request);

			if (res != null) {
				if (res.getRetCode().equals("00") && !res.getListXml().equals("")) {
					wrs = Convert.stringToWebRowSet(res.getListXml());
					if (wrs != null) {
						if (wrs.next()) {
							// codSocieta = wrs.getString(1).trim();
							if (logRequest.setChiaveEnteCheckEmpty(wrs.getString(2))) {
								// LP PG21XX07 - 20211230 System.out.println("============================
								// setInfoEnteDaCodice =================================================>
								// chiaveEnte: " + logRequest.getChiaveEnte());
							}
							if (logRequest.setSiglaProvinciaEnteCheckEmpty(wrs.getString(4))) {
								// LP PG21XX07 - 20211230 System.out.println("============================
								// setInfoEnteDaCodice =================================================>
								// siglaProvincia: " + logRequest.getSiglaProvinciaEnte());
							}
							if (logRequest.setDescrizioneEnteCheckEmpty(wrs.getString(5))) {
								// LP PG21XX07 - 20211230 System.out.println("============================
								// setInfoEnteDaCodice =================================================>
								// descrizioneEnte: " + logRequest.getDescrizioneEnte());
							}
							// LP PG21XX07 - 20211230 System.out.println("============================ fine
							// recuperaEnteUtenteSocietaProvincia
							// =================================================>");
							bOk = true;
						}
					}
				} else {
					// LP PG21XX07 - 20211230 System.out.println("recuperaEnteUtenteSocietaProvincia
					// res: " + res.getRetMessage());
				}
			} else {
				// LP PG21XX07 - 20211230 System.out.println("recuperaEnteUtenteSocietaProvincia
				// == null");
			}
			// LP PG21XX07 - 20211230 System.out.println("============================ fine
			// recuperaEnteUtenteSocietaProvincia
			// =================================================>");
		} catch (Exception e) {
		} finally {
			if (wrs != null) {
				try {
					wrs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		// LP PG21XX07 - 20211230 System.out.println("============================ fine
		// setInfoEnteDaCodice =================================================>");
		return bOk;
	}

	/*
	 * private boolean setInfoEnteBelf(String codBelf, LogRequest logRequest,
	 * HttpServletRequest request) { WebRowSet wrs = null; System.out.
	 * println("============================ inizio recuperaEnteBelf  =================================================>"
	 * ); try { RecuperaEnteBelfRequest in = new RecuperaEnteBelfRequest();
	 * in.setCodiceBelf(codBelf); RecuperaEnteBelfResponse res =
	 * WSCache.commonsServer.recuperaEnteBelf(in, request); if (res != null) { if
	 * (res.getRetCode().equals("00") && !res.getListXml().equals("")) { wrs =
	 * Convert.stringToWebRowSet(res.getListXml()); if (wrs != null) { if
	 * (wrs.next()) { //ENT_CSOCCSOC, ENT_CUTECUTE, ANE_KANEKENT String
	 * codiceSocieta = wrs.getString(1).trim(); if(logRequest.getCodiceSocieta() ==
	 * null || logRequest.getCodiceSocieta().trim().length() == 0) { System.out.
	 * println("============================ setInfoEnteBelf  =================================================> codiceSocieta: "
	 * + codiceSocieta); logRequest.setCodiceSocieta(codiceSocieta); } String
	 * codiceUtente = wrs.getString(2).trim(); if(logRequest.getCodiceUtente() ==
	 * null || logRequest.getCodiceUtente().trim().length() == 0) { System.out.
	 * println("============================ setInfoEnteBelf  =================================================> codiceUtente: "
	 * + codiceUtente); logRequest.setCodiceUtente(codiceUtente); }
	 * if(logRequest.getChiaveEnte() == null ||
	 * logRequest.getChiaveEnte().trim().length() == 0) {
	 * logRequest.setChiaveEnte(wrs.getString(3).trim()); System.out.
	 * println("============================ setInfoEnteBelf  =================================================> chiaveEnte: "
	 * + logRequest.getChiaveEnte()); } System.out.
	 * println("============================ fine setInfoEnteBelf  =================================================>"
	 * ); return true; } } } else { System.out.println("recuperaEnteBelf res: " +
	 * res.getRetMessage()); } } else {
	 * System.out.println("recuperaEnteBelf == null"); } } catch (Exception e) { }
	 * finally { if(wrs != null) { try { wrs.close(); } catch (SQLException e) {
	 * e.printStackTrace(); } } } System.out.
	 * println("============================ fine setInfoEnteBelf  =================================================>"
	 * ); return false; }
	 */
	private boolean setInfoComuneDaBelf(String codiceBelfiore, LogRequest logRequest, HttpServletRequest request) {
		// LP PG21XX07 - 20211230 System.out.println("============================
		// inizio setInfoComuneDaBelf
		// =================================================>");
		if (codiceBelfiore.trim().length() > 0) {
			codiceBelfiore = codiceBelfiore.trim();
			try {
				AnagProvComDetailRequest req = new AnagProvComDetailRequest();
				req.setCodiceBelfiore(codiceBelfiore);

				AnagProvComDetailResponse res = WSCache.anagProvComServer.getAnagProvCom(req, request);
				if (res != null && res.getAnagprovcom() != null) {
					logRequest.setBelfioreRequest(codiceBelfiore);
					logRequest.setComuneRequestCheckEmpty(res.getAnagprovcom().getDescrizioneComune());
					logRequest.setSiglaProvinciaRequestCheckEmpty(res.getAnagprovcom().getSiglaProvincia());
					return true;
				}
			} catch (Exception e) {
				System.out.println("setInfoComuneDaBelf res: " + e.getMessage());
			}
		}
		// LP PG21XX07 - 20211230 System.out.println("============================ fine
		// setInfoComuneDaBelf =================================================>");
		return false;
	}
	// fine LP PG21X007
}
