package org.seda.payer.manager.logrequest.actions;

import java.util.Calendar;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.adminusers.util.ProfiloUtil;
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import com.esed.log.req.dati.CollectionDto;
import com.esed.log.req.dati.LogRequest;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;


@SuppressWarnings("serial")
public class LogRequestSearchAction extends BaseManagerAction{
	
	private Calendar dataDa = null;
	private Calendar dataA = null;

	public Object service(HttpServletRequest request) throws ActionException {
		//
		HttpSession session = request.getSession();
		tx_SalvaStato(request);
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Chiamo il metodo per i settaggi relativi al profilo utente
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) setProfile(request);
		setEstremiDate(request);
		/*
		 * Carico la lista delle società e dei canali di pagamento
		 */
		loadSocietaXml_DDL(request);
		loadCanaliPagamentoXml_DDL(request);
		/*
		 * Carico la lista delle applicazioni nell'attributo "listaApplicazioniPayer"
		 * Vencono caricate le applicazioni "Attive" e "Protette" e, come descrizione, viene
		 * utilizzata quella di "MAF-APPLICATION.XML"
		 */
		loadPayerApplicationsXml_DDL_properties(request, session, true, true);
		/*
		 *  Ricavo i parametri della request se non presenti li prendo dalla session
		 */

		String userName = (String)request.getAttribute("tx_username") != null ? (String)request.getAttribute("tx_username") : ((String)session.getAttribute("tx_username") != null ? (String)session.getAttribute("tx_username") : "");
		String userProfile = (String)request.getAttribute("tx_userprofile") != null ? (String)request.getAttribute("tx_userprofile") : ((String)session.getAttribute("tx_userprofile") != null ? (String)session.getAttribute("tx_userprofile") : "");
		String applicazione = (String)request.getAttribute("tx_applicazione") != null ? (String)request.getAttribute("tx_applicazione") : ((String)session.getAttribute("tx_applicazione") != null ? (String)session.getAttribute("tx_applicazione") : "");
		String indirizzoIP = (String)request.getAttribute("indirizzo_ip") != null ? (String)request.getAttribute("indirizzo_ip") : ((String)session.getAttribute("indirizzo_ip") != null ? (String)session.getAttribute("indirizzo_ip") : "");
		String applicativo = (String)request.getAttribute("tx_app") != null ? (String)request.getAttribute("tx_app") : ((String)session.getAttribute("tx_app") != null ? (String)session.getAttribute("tx_app") : "");
		String canale = (String)request.getAttribute("tx_canale_pagamento") != null ? (String)request.getAttribute("tx_canale_pagamento") : ((String)session.getAttribute("tx_canale_pagamento") != null ? (String)session.getAttribute("tx_canale_pagamento") : "");
		String metodo = (String)request.getAttribute("tx_metodo") != null ? (String)request.getAttribute("tx_metodo") : ((String)session.getAttribute("tx_metodo") != null ? (String)session.getAttribute("tx_metodo") : "");
		String azione = (String)request.getAttribute("tx_azione") != null ? (String)request.getAttribute("tx_azione") : ((String)session.getAttribute("tx_azione") != null ? (String)session.getAttribute("tx_azione") : "");
		String codfis = (String)request.getAttribute("tx_codfis") != null ? (String)request.getAttribute("tx_codfis") : ((String)session.getAttribute("tx_codfis") != null ? (String)session.getAttribute("tx_codfis") : "");
		String numdoc = (String)request.getAttribute("tx_numdoc") != null ? (String)request.getAttribute("tx_numdoc") : ((String)session.getAttribute("tx_numdoc") != null ? (String)session.getAttribute("tx_numdoc") : "");
		String numbol = (String)request.getAttribute("tx_numbol") != null ? (String)request.getAttribute("tx_numbol") : ((String)session.getAttribute("tx_numbol") != null ? (String)session.getAttribute("tx_numbol") : "");
		String siglaProvinciaEnte = (String)request.getAttribute("tx_provincia") != null ? (String)request.getAttribute("tx_provincia") : ((String)session.getAttribute("tx_provincia") != null ? (String)session.getAttribute("tx_provincia") : "");
		String siglaProvinciaRequest = (String)request.getAttribute("tx_provres") != null ? (String)request.getAttribute("tx_provres") : ((String)session.getAttribute("tx_provres") != null ? (String)session.getAttribute("tx_provres") : "");
		String tx_comures = (String)request.getAttribute("tx_comures") != null ? (String)request.getAttribute("tx_comures") : ((String)session.getAttribute("tx_comures") != null ? (String)session.getAttribute("tx_comures") : "");
//		String comures = "";
		String belfres = "";
		if(tx_comures != null) {
			String[] appo = GenericsDateNumbers.getSplit_NString(2, tx_comures);
//			comures = appo[0];
			belfres = appo[1];
		}
		String numeroIUV = (String)request.getAttribute("tx_numiuv") != null ? (String)request.getAttribute("tx_numiuv") : ((String)session.getAttribute("tx_numiuv") != null ? (String)session.getAttribute("tx_numiuv") : "");
		String chiaveTransazione = (String)request.getAttribute("tx_keytra") != null ? (String)request.getAttribute("tx_keytra") : ((String)session.getAttribute("tx_keytra") != null ? (String)session.getAttribute("tx_keytra") : "");
		String tx_request = (String)request.getAttribute("tx_request") != null ? (String)request.getAttribute("tx_request") : ((String)session.getAttribute("tx_request") != null ? (String)session.getAttribute("tx_request") : "");
		String tx_sessione = (String)request.getAttribute("tx_sessione") != null ? (String)request.getAttribute("tx_sessione") : ((String)session.getAttribute("tx_sessione") != null ? (String)session.getAttribute("tx_sessione") : "");
		String tx_error = (String)request.getAttribute("tx_error") != null ? (String)request.getAttribute("tx_error") : ((String)session.getAttribute("tx_error") != null ? (String)session.getAttribute("tx_error") : "");
		String tx_ope = (String)request.getAttribute("tx_ope") != null ? (String)request.getAttribute("tx_ope") : ((String)session.getAttribute("tx_ope") != null ? (String)session.getAttribute("tx_ope") : "");
			
		Calendar dataInizioSessioneDa =
				(Calendar)request.getAttribute("inizioSessioneDA") == null ? (Calendar)session.getAttribute("inizioSessioneDA") : (Calendar)request.getAttribute("inizioSessioneDA");
		String timeInizioSessioneDa =
				(String)request.getAttribute("inizioSessioneTimeDA") == null ? (String)session.getAttribute("inizioSessioneTimeDA") : (String)request.getAttribute("inizioSessioneTimeDA");
		
		Calendar dataInizioSessioneA =
				(Calendar)request.getAttribute("inizioSessioneA") == null ? (Calendar)session.getAttribute("inizioSessioneA") : (Calendar)request.getAttribute("inizioSessioneA");
		String timeInizioSessioneA =
				(String)request.getAttribute("inizioSessioneTimeA") == null ? (String)session.getAttribute("inizioSessioneTimeA") : (String)request.getAttribute("inizioSessioneTimeA");
		int oraDa = 0;
		int minDa = 0;
		int secDa = 0;
		int oraA = 23;
		int minA = 59;
		int secA = 59;
		if(timeInizioSessioneDa != null && timeInizioSessioneDa.length() > 0) {
			String[] appDa =  timeInizioSessioneDa.split(":");
			oraDa = Integer.parseInt(appDa[0]);
			if(appDa.length > 1)
				minDa = Integer.parseInt(appDa[1]);
			else
				minDa = 0;
			if(appDa.length > 2)
				secDa = Integer.parseInt(appDa[2]);
			else
				secDa = 0;
		}
		if(timeInizioSessioneA != null && timeInizioSessioneA.length() > 0) {
			String[] appA =  timeInizioSessioneA.split(":");
			oraA = Integer.parseInt(appA[0]);
			if(appA.length > 1)
				minA = Integer.parseInt(appA[1]);
			else
				minA = 0;
			if(appA.length > 2)
				secA = Integer.parseInt(appA[2]);
			else
				secA = 0;
		}
		/*Setto l'orario all'inizio della giornata per "dataDa"*/
		if(dataInizioSessioneDa != null)
		{
			//dataInizioSessioneDa.set(dataInizioSessioneDa.get(Calendar.YEAR), dataInizioSessioneDa.get(Calendar.MONTH), dataInizioSessioneDa.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
			dataInizioSessioneDa.set(dataInizioSessioneDa.get(Calendar.YEAR), dataInizioSessioneDa.get(Calendar.MONTH), dataInizioSessioneDa.get(Calendar.DAY_OF_MONTH), oraDa, minDa, secDa);
		}
		/*Setto l'orario alla fine della giornata per "dataA"*/
		if(dataInizioSessioneA != null)
		{
			//dataInizioSessioneA.set(dataInizioSessioneA.get(Calendar.YEAR), dataInizioSessioneA.get(Calendar.MONTH), dataInizioSessioneA.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
			dataInizioSessioneA.set(dataInizioSessioneA.get(Calendar.YEAR), dataInizioSessioneA.get(Calendar.MONTH), dataInizioSessioneA.get(Calendar.DAY_OF_MONTH), oraA, minA, secA);
		}
		
		int rowsPerPage = getDefaultListRows(request);
		if (request.getAttribute("rowsPerPage") != null && request.getAttribute("rowsPerPage").toString().indexOf(";") == -1)  {
			rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
		}
     
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();
		/*
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		String exp = "0";
        boolean actExp = false;
        
		switch(firedButton) {
			case TX_BUTTON_CERCA_EXP: 
				if (request.getParameter("ext") != null && !request.getParameter("ext").equals("1"))
					exp = "1";
				else
					exp = "0";
				actExp = true;
				request.setAttribute("rowsPerPage", request.getParameter("hRowsPerPage"));
				request.setAttribute("pageNumber", request.getParameter("hPageNumber"));
				request.setAttribute("order", request.getParameter("hOrder"));
				String appor = ((String) request.getAttribute("rowsPerPage") == null) ? "" + getDefaultListRows(request) : (String) request.getAttribute("rowsPerPage");
				if(appor != null && appor.indexOf(";") != -1) {
					rowsPerPage = getDefaultListRows(request);
				} else {
					rowsPerPage = Integer.parseInt(appor);
				}
				pageNumber = ((String) request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
				order = ((String) request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
			case TX_BUTTON_CERCA:
				mantieniFiltriRicerca(request);
				
				Client client = null;
				Response responseWS = null;
				if (request.getAttribute("ext") == null)
					request.setAttribute("ext", "0");
				if (request.getAttribute("ext") == "" || actExp)
					request.setAttribute("ext", exp);
				try {
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvinciaEnte, getParamCodiceEnte(), getParamCodiceUtente(), false);
					loadProvinciaComune(request, siglaProvinciaRequest);

					LogRequest logRequest = new LogRequest();
					logRequest.setDbSchemaCodSocieta(getCodSocieta(request));
					logRequest.setApplicativo(applicativo);
					logRequest.setRowsPerPage(rowsPerPage);
					logRequest.setPageNumber(pageNumber);
					if(order != null && order.length() > 0)
						logRequest.setOrder(order);
					if(userName != null && userName.length() > 0)
						logRequest.setUserName(userName);
					if(indirizzoIP != null && indirizzoIP.length() > 0)
						logRequest.setIndirizzoIP(indirizzoIP);
					if(applicazione != null && applicazione.length() > 0)
						logRequest.setSezioneApplicativa(applicazione);
					if(userProfile != null && userProfile.length() > 0)
						logRequest.setUserProfile(userProfile);
					if(canale != null && canale.length() > 0)
						logRequest.setCanalePagamento(canale);
					if(metodo != null && metodo.length() > 0)
						logRequest.setTipoRequest(metodo);
					if(azione != null && azione.length() > 0)
						logRequest.setAction(azione);
					if(codfis != null && codfis.length() > 0)
						logRequest.setCodiceFiscale(codfis);
					if(numdoc != null && numdoc.length() > 0)
						logRequest.setNumeroDocumento(numdoc);
					if(numbol != null && numbol.length() > 0)
						logRequest.setNumeroBollettino(numbol);
					if(belfres != null && belfres.length() > 0) {
						logRequest.setBelfioreRequest(belfres);
					} else if(siglaProvinciaRequest != null && siglaProvinciaRequest.length() > 0) {
						logRequest.setSiglaProvinciaRequest(siglaProvinciaRequest);
					}
					if(numeroIUV != null && numeroIUV.length() > 0) {
						logRequest.setNumeroIUV(numeroIUV);
					}
					if(chiaveTransazione != null && chiaveTransazione.length() > 0) {
						logRequest.setChiaveTransazione(chiaveTransazione);
					}
					if(tx_request != null && tx_request.length() > 0)
						logRequest.setQueryString(tx_request);
					//inizio LP PG21X007 - 20211230
					if(tx_sessione != null && tx_sessione.length() > 0)
						logRequest.setSessionID(tx_sessione);
					//fine LP PG21X007 - 20211230
					if(tx_error != null && tx_error.length() > 0)
						logRequest.setError(tx_error);
					if(tx_ope != null && tx_ope.length() > 0)
						logRequest.setOperatoreRequest(tx_ope);
					
					if(dataInizioSessioneDa != null && dataInizioSessioneA != null
					   && formatDate(dataInizioSessioneDa, "yyyyMMddHHmmss").compareTo(formatDate(dataInizioSessioneA, "yyyyMMddHHmmss")) > 0) {
						throw new Exception("'Da' non puo' essere maggiore di 'A'"); 
					}
					if(dataInizioSessioneDa != null
					   && formatDate(dataInizioSessioneDa, "yyyyMMddHHmmss").compareTo(formatDate(dataDa, "yyyyMMdd000000")) < 0) {
						throw new Exception("'Da' non puo' essere minore di: " + formatDate(dataDa, "dd/MM/yyyy 00:00:00")); 
					}
					if(dataInizioSessioneA != null
					   && formatDate(dataInizioSessioneA, "yyyyMMddHHmmss").compareTo(formatDate(dataA, "yyyyMMdd235959")) > 0) {
						throw new Exception("'A' non puo' essere maggiore di: " + formatDate(dataA, "dd/MM/yyyy 23:59:59")); 
					}
					if(dataInizioSessioneDa != null)
						logRequest.setDataInizio_da(formatDate(dataInizioSessioneDa, "yyyy-MM-dd HH:mm:ss"));
					if(dataInizioSessioneA != null)
						logRequest.setDataFine_a(formatDate(dataInizioSessioneA, "yyyy-MM-dd HH:mm:ss"));
					if(getParamCodiceSocieta() != null && getParamCodiceSocieta().length() > 0)
						logRequest.setCodiceSocieta(getParamCodiceSocieta());
					if(siglaProvinciaEnte != null && siglaProvinciaEnte.length() > 0)
						logRequest.setSiglaProvinciaEnte(siglaProvinciaEnte);
					if(getParamCodiceUtente()!= null && getParamCodiceUtente().length() > 0)
						logRequest.setCodiceUtente(getParamCodiceUtente());
					if(getParamCodiceEnte() != null && getParamCodiceEnte().length() > 0)
						logRequest.setChiaveEnte(getParamCodiceEnte());
					
					Entity<LogRequest> entity =  Entity.entity(logRequest, MediaType.APPLICATION_JSON);
					
					String uri =  ManagerStarter.configuration.getProperty(PropertiesPath.wsLogRequest.format(PropertiesPath.defaultnode.format()));
					uri += "/search";

					client = ClientBuilder.newClient();
					WebTarget target = client.target(uri);
					Builder builder = target.request(MediaType.APPLICATION_JSON);
					responseWS = builder.post(entity);
					if(responseWS != null) {
						if(responseWS.getStatus() == Status.OK.getStatusCode()) {
							CollectionDto coll = responseWS.readEntity(CollectionDto.class);
							com.seda.data.spi.PageInfo pageInfoWS = coll.getPageInfo();
							if(pageInfoWS.getNumRows() > 0) {
								com.seda.data.spi.PageInfo pageInfo = new PageInfo();
								pageInfo.setFirstRow(pageInfoWS.getFirstRow());
								pageInfo.setLastRow(pageInfoWS.getLastRow());
								pageInfo.setNumPages(pageInfoWS.getNumPages());
								pageInfo.setNumRows(pageInfoWS.getNumRows());
								pageInfo.setPageNumber(pageInfoWS.getPageNumber());
								pageInfo.setRowsPerPage(pageInfoWS.getRowsPerPage());
								
								request.setAttribute("listaLogRequest", coll.getListXml());
								request.setAttribute("listaLogRequest.pageInfo", pageInfo);
							} else {
								setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
							}
						} else { 
							setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
						}
						responseWS.close();
						responseWS = null;
					}
					client.close();
					client = null;
				} catch (Exception e) {
					setFormMessage("form_selezione", "Errore: " + e.getMessage(), request);
					e.printStackTrace();
				} finally {
					if(responseWS != null)
						responseWS.close();
					if(client != null)
						client.close();
				}

				break;
				
			case TX_BUTTON_RESET:
				resetParametri(request);
				request.setAttribute("inizioSessioneDA", null);
				session.setAttribute("inizioSessioneDA", null);
				request.setAttribute("inizioSessioneA", null);
				session.setAttribute("inizioSessioneA", null);
				request.setAttribute("inizioSessioneTimeDA", "");
				request.setAttribute("inizioSessioneTimeA", "");
				setProfile(request);
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvinciaEnte, getParamCodiceEnte(), getParamCodiceUtente(), true);
				loadProvinciaComune(request, "");
				break;
				
			case TX_BUTTON_SOCIETA_CHANGED:
				String societa = getParamCodiceSocieta();
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvinciaEnte), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : getParamCodiceUtente()), true);
				loadProvinciaComune(request, siglaProvinciaRequest);
				request.setAttribute("tx_provincia","");
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvinciaEnte, (siglaProvinciaEnte.equals("") ? "" : getParamCodiceEnte()), (siglaProvinciaEnte.equals("") ? "" : getParamCodiceUtente()), true);
				loadProvinciaComune(request, siglaProvinciaRequest);
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_PROVINCIA_BEN_CHANGED:
				loadProvinciaXml_DDL(request, session, null, false);
				LoadListaUtentiEntiXml_DDL(request, session, null, null, null, null, false);
				loadProvinciaComune(request, siglaProvinciaRequest);
				break;
				
			case TX_BUTTON_NULL: 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvinciaEnte, getParamCodiceEnte(), getParamCodiceUtente(), true);
				loadProvinciaComune(request, "");
				break;
			default:
				break;
				
		}
		return null;
	}

	protected void tx_SalvaStato(HttpServletRequest request) {
		super.tx_SalvaStato(request);
		request.setAttribute("inizioSessioneDA", getCalendar(request, "inizioSessioneDA"));
		request.setAttribute("inizioSessioneA", getCalendar(request, "inizioSessioneA"));
	}
	
	private String getCodSocieta(HttpServletRequest request)
	{
		HttpSession session = request.getSession(false);
		String dbSchemaCodSocieta = null;
		if (session != null && session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA) != null)
			dbSchemaCodSocieta = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		else
		{
			PropertiesTree configuration = (PropertiesTree) session.getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
			dbSchemaCodSocieta=configuration.getProperty(PropertiesPath.societa.format(getTemplateCurrentApplication(request, session)));
			session.setAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA,dbSchemaCodSocieta);
		}
		return dbSchemaCodSocieta;
	}

	public void loadProvinciaComune(HttpServletRequest request, String siglaProvincia)
	{
		
		if (request.getSession().getAttribute("listprovincia") != null) {
			request.setAttribute("listprovincia", (String) request.getSession().getAttribute("listprovincia"));
		} else {
			ProfiloUtil.loadProvince(request);
			request.getSession().setAttribute("listprovincia", (String) request.getAttribute("listprovincia"));
		}
		
		if (siglaProvincia != null) {
			//if (bollTemp.getResidenzaEstero().equals("Y"))
			//	bollTemp.setCittaProvincia("EE");
			if (!siglaProvincia.equals("")) {
				//String nomeTemplate = Generics.getTemplateCurrentApplicationStr(request, request.getSession());
				//if (nomeTemplate.equalsIgnoreCase("bolzanoDE") || nomeTemplate.equalsIgnoreCase("bolzano")) {
				//	String flagDescAlternativa = "";
				//	if (nomeTemplate.equalsIgnoreCase("bolzanoDE")) {
				//		flagDescAlternativa = "Y";
				//	}	
				//	loadComuni2(request, bollTemp.getCittaProvincia(), false, "ricercacomunelistcomuni", flagDescAlternativa);
				//} else {
				ProfiloUtil.loadComuni(request, siglaProvincia, "ricercacomunelistcomuni");
				//}
			}
		}
	}

	private void setEstremiDate(HttpServletRequest request)
	{
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
		Calendar cal0 = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
		int cheGiornoE = cal.get(Calendar.DAY_OF_WEEK);
		int fine = -1;
		int inizio = -6;
		if(cheGiornoE != 2) {
			//se non è lunedì
			if(cheGiornoE > 2)
				fine = 1 - cheGiornoE;
			else
				fine = -7;
		}
		System.out.println("setEstremiDate oggi data: " + formatDate(cal, "yyyyMMdd"));
		cal.add(Calendar.DAY_OF_YEAR, fine); //imposto alla domenica di una settimana prima
		cal.add(Calendar.DAY_OF_YEAR, inizio); //imposto al lunedì di una settimana prima
		String dataInizio = formatDate(cal, "yyyyMMdd"); //tag della data iniziale
		System.out.println("setEstremiDate settimana precedente dataInizio: " + dataInizio);
		dataDa = cal;
		cheGiornoE = cal0.get(Calendar.DAY_OF_WEEK);
		String dataFineCorrente = ""; //tag della data di fine della settimana corrente
		if(cheGiornoE != 2) {
			if(cheGiornoE > 2)
				inizio = 2 - cheGiornoE;
			else
				inizio = -6;
			cal0.add(Calendar.DAY_OF_YEAR, inizio); //imposto al lunedì della settimana corrente
		}
		cal0.add(Calendar.DAY_OF_YEAR, 6); //imposto alla domenica della settimana corrente
		dataFineCorrente = formatDate(cal0, "yyyyMMdd"); //tag della data finale della settimana corrente
		dataA = cal0;
		System.out.println("setEstremiDate settimana corrente dataFine: " + dataFineCorrente);
		request.setAttribute("logrequestAnnoDa",  Integer.parseInt(dataInizio.substring(0, 4)));
		request.setAttribute("logrequestMeseDa", Integer.parseInt(dataInizio.substring(4, 6)));
		request.setAttribute("logrequestGiornoDa", Integer.parseInt(dataInizio.substring(6)));
		request.setAttribute("logrequestAnnoA", Integer.parseInt(dataFineCorrente.substring(0, 4)));
		request.setAttribute("logrequestMeseA", Integer.parseInt(dataFineCorrente.substring(4, 6)));
		request.setAttribute("logrequestGiornoA", Integer.parseInt(dataFineCorrente.substring(6)));
	}
	
	

	private void mantieniFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		

		if(request.getAttribute("tx_username")!=null)
			session.setAttribute("tx_username", request.getAttribute("tx_username"));
		
		if(request.getAttribute("tx_userprofile")!=null)
			session.setAttribute("tx_userprofile", request.getAttribute("tx_userprofile"));
		
		if(request.getAttribute("tx_applicazione")!=null)
			session.setAttribute("tx_applicazione", request.getAttribute("tx_applicazione"));
		
		if(request.getAttribute("indirizzo_ip")!=null)
			session.setAttribute("indirizzo_ip", request.getAttribute("indirizzo_ip"));
		
		if(request.getAttribute("tx_app")!=null)
			session.setAttribute("tx_app", request.getAttribute("tx_app"));
		
		if(request.getAttribute("tx_canale_pagamento")!=null)
			session.setAttribute("tx_canale_pagamento", request.getAttribute("tx_canale_pagamento"));
		
		if(request.getAttribute("tx_metodo")!=null)
			session.setAttribute("tx_metodo", request.getAttribute("tx_metodo"));
		
		if(request.getAttribute("tx_azione")!=null)
			session.setAttribute("tx_azione", request.getAttribute("tx_azione"));
		
		if(request.getAttribute("tx_codfis")!=null)
			session.setAttribute("tx_codfis", request.getAttribute("tx_codfis"));
		
		if(request.getAttribute("tx_numdoc")!=null)
			session.setAttribute("tx_numdoc", request.getAttribute("tx_numdoc"));
		
		if(request.getAttribute("tx_numbol")!=null)
			session.setAttribute("tx_numbol", request.getAttribute("tx_numbol"));
		
		if(request.getAttribute("tx_provincia")!=null)
			session.setAttribute("tx_provincia", request.getAttribute("tx_provincia"));
		
		if(request.getAttribute("tx_provres")!=null)
			session.setAttribute("tx_provres", request.getAttribute("tx_provres"));
		
		if(request.getAttribute("tx_comures")!=null)
			session.setAttribute("tx_comures", request.getAttribute("tx_comures"));
		
		if(request.getAttribute("tx_numiuv")!=null)
			session.setAttribute("tx_numiuv", request.getAttribute("tx_numiuv"));
		
		if(request.getAttribute("tx_keytra")!=null)
			session.setAttribute("tx_keytra", request.getAttribute("tx_keytra"));
		
		if(request.getAttribute("tx_request")!=null)
			session.setAttribute("tx_request", request.getAttribute("tx_request"));
		
		if(request.getAttribute("tx_sessione")!=null)
			session.setAttribute("tx_sessione", request.getAttribute("tx_sessione"));
		
		if(request.getAttribute("tx_error")!=null)
			session.setAttribute("tx_error", request.getAttribute("tx_error"));

		if(request.getAttribute("tx_ope")!=null)
			session.setAttribute("tx_ope", request.getAttribute("tx_ope"));
		
		if(request.getAttribute("inizioSessioneDA")!=null)
			session.setAttribute("inizioSessioneDA", request.getAttribute("inizioSessioneDA"));
		if(request.getAttribute("inizioSessioneTimeDA")!=null)
			session.setAttribute("inizioSessioneTimeDA", request.getAttribute("inizioSessioneTimeDA"));
		
		if(request.getAttribute("inizioSessioneA")!=null)
			session.setAttribute("inizioSessioneA", request.getAttribute("inizioSessioneA"));
		if(request.getAttribute("inizioSessioneTimeA")!=null)
			session.setAttribute("inizioSessioneTimeA", request.getAttribute("inizioSessioneTimeA"));
		
	}
}
