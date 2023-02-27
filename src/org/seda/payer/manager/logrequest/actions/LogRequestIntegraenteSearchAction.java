package org.seda.payer.manager.logrequest.actions;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.function.Supplier;

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
import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import com.esed.log.req.dati.CollectionDto;
import com.esed.log.req.dati.LogWin;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;


@SuppressWarnings("serial")
public class LogRequestIntegraenteSearchAction extends BaseManagerAction{
	
	private Calendar dataDa = null;
	private Calendar dataA = null;

	private String codiceSocieta="";
	private String cutecute="";
	private String chiaveEnte="";
	private String chiaveEnteda5="";

	public Object service(HttpServletRequest request) throws ActionException {

		HttpSession session = request.getSession();
		
		Supplier<String> getTipoChiamata = () -> request.getRequestURI();
		
		tx_SalvaStato(request);
		
		FiredButton firedButton = getFiredButton(request);
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) setProfile(request);
		setEstremiDate(request);
		

		loadSocietaUtenteEnteXml_DDL(request, session);
		
		/*
		 * Carico la lista delle applicazioni nell'attributo "listaApplicazioniPayer"
		 * Vencono caricate le applicazioni "Attive" e "Protette" e, come descrizione, viene
		 * utilizzata quella di "MAF-APPLICATION.XML"
		 */
		loadPayerApplicationsXml_DDL_properties(request, session, true, true);


//		String enteda5 = ((String)request.getAttribute("tx_enteda5") == null ? "" : (String)request.getAttribute("tx_enteda5"));
		
		
		String tipoChiamata = 
				(String)request.getAttribute("tx_tipoChiamata") == null ? 
				 ((String)session.getAttribute("tx_tipoChiamata") == null  ? "" : (String)session.getAttribute("tx_tipoChiamata") )
				: (String)request.getAttribute("tx_tipoChiamata") ;
		
		String cuteCute =
				(String)request.getAttribute("tx_cuteCute") == null ? 
				((String)session.getAttribute("tx_cuteCute") == null  ? "" : (String)session.getAttribute("tx_cuteCute"))
				: (String)request.getAttribute("tx_cuteCute");
		
		String bollettino =
				(String)request.getAttribute("tx_bollettino") == null ? 
				((String)session.getAttribute("tx_bollettino") == null  ? "" : (String)session.getAttribute("tx_bollettino"))
				: (String)request.getAttribute("tx_bollettino");

		String idDominio = 
				(String)request.getAttribute("tx_idDominio") == null ? 
				 ( (String)session.getAttribute("tx_idDominio") == null  ? "" : (String)session.getAttribute("tx_idDominio") )
				: (String)request.getAttribute("tx_idDominio") ;
		
		String codfis = 
				(String)request.getAttribute("tx_codfis") == null ? 
				 ( (String)session.getAttribute("tx_codfis") == null  ? "" : (String)session.getAttribute("tx_codfis") )
				: (String)request.getAttribute("tx_codfis") ;
		
		String operatore = 
				(String)request.getAttribute("tx_operatore") == null ? 
				 ( (String)session.getAttribute("tx_operatore") == null  ? "" : (String)session.getAttribute("tx_operatore") )
				: (String)request.getAttribute("tx_operatore") ;
		
		String esito = 
				(String)request.getAttribute("tx_esito") == null ? 
				 ( (String)session.getAttribute("tx_esito") == null  ? "" : (String)session.getAttribute("tx_esito") )
				: (String)request.getAttribute("tx_esito") ;
		
		String error = 
				(String)request.getAttribute("tx_error") == null ? 
				 ( (String)session.getAttribute("tx_error") == null  ? "" : (String)session.getAttribute("tx_error") )
				: (String)request.getAttribute("tx_error") ;
		
		String xmlIn = 
				(String)request.getAttribute("tx_xmlIn") == null ? 
				 ( (String)session.getAttribute("tx_xmlIn") == null  ? "" : (String)session.getAttribute("tx_xmlIn") )
				: (String)request.getAttribute("tx_xmlIn") ;
		
		String xmlOut = 
				(String)request.getAttribute("tx_xmlOut") == null ? 
				 ( (String)session.getAttribute("tx_xmlOut") == null  ? "" : (String)session.getAttribute("tx_xmlOut") )
				: (String)request.getAttribute("tx_xmlOut") ;
		
		Calendar dataChiamataDa =
				(Calendar)request.getAttribute("dataChiamataDA") == null ? (Calendar)session.getAttribute("dataChiamataDA") : (Calendar)request.getAttribute("dataChiamataDA");
		String timeChiamataDa =
				(String)request.getAttribute("dataChiamataTimeDA") == null ? (String)session.getAttribute("dataChiamataTimeDA") : (String)request.getAttribute("dataChiamataTimeDA");

		
		Calendar dataChiamataA =
				(Calendar)request.getAttribute("dataChiamataA") == null ? (Calendar)session.getAttribute("dataChiamataA") : (Calendar)request.getAttribute("dataChiamataA");
		String timeChiamataA =
				(String)request.getAttribute("dataChiamataTimeA") == null ? (String)session.getAttribute("dataChiamataTimeA") : (String)request.getAttribute("dataChiamataTimeA");

		int oraDa = 0;
		int minDa = 0;
		int secDa = 0;
		int oraA = 23;
		int minA = 59;
		int secA = 59;
		
		if(timeChiamataDa != null && timeChiamataDa.length() > 0) {
			String[] appDa =  timeChiamataDa.split(":");
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
		if(timeChiamataA != null && timeChiamataA.length() > 0) {
			String[] appA =  timeChiamataA.split(":");
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

		if(dataChiamataDa != null)
		{
			dataChiamataDa.set(dataChiamataDa.get(Calendar.YEAR), dataChiamataDa.get(Calendar.MONTH), dataChiamataDa.get(Calendar.DAY_OF_MONTH), oraDa, minDa, secDa);
		}
		if(dataChiamataA != null)
		{
			dataChiamataA.set(dataChiamataA.get(Calendar.YEAR), dataChiamataA.get(Calendar.MONTH), dataChiamataA.get(Calendar.DAY_OF_MONTH), oraA, minA, secA);
		}	
		
		int rowsPerPage = getDefaultListRows(request);
		if (request.getAttribute("rowsPerPage") != null && request.getAttribute("rowsPerPage").toString().indexOf(";") == -1)  {
			rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
		}
     
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();
	
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
				
				dividiSocUtenteEnte(request);

				mantieniFiltriRicerca(request);
				
				Client client = null;
				Response responseWS = null;
				if (request.getAttribute("ext") == null)
					request.setAttribute("ext", "0");
				if (request.getAttribute("ext") == "" || actExp)
					request.setAttribute("ext", exp);
				try {

					LogWin logWin = new LogWin();
					logWin.setDbSchemaCodSocieta(getCodSocieta(request));
					logWin.setRowsPerPage(rowsPerPage);
					logWin.setPageNumber(pageNumber);
					
					if(order != null && order.length() > 0)
						logWin.setOrder(order);
					
					if(tipoChiamata != null && tipoChiamata.length() > 0)
						logWin.setTipoChiamata(tipoChiamata);
					
					if(cuteCute != null && cuteCute.length() > 0)
						logWin.setCodiceUtente(cuteCute);
					
					if(bollettino != null && bollettino.length() > 0)
						logWin.setBollettino(bollettino);
					
					if(idDominio != null && idDominio.length() > 0)
						logWin.setIdDominio(idDominio);
				
					if(codfis != null && codfis.length() > 0)
						logWin.setCodiceFiscale(codfis);
					
					if(operatore != null && operatore.length() > 0)
						logWin.setOperatoreInserimento(operatore);
					
					if(esito != null && esito.length() > 0)
						logWin.setEsito(esito);
					
					if(error != null && error.length() > 0)
						logWin.setMessaggioErrore(error);

					if(xmlIn != null && xmlIn.length() > 0)
						logWin.setXmlRequest(xmlIn);
					if(xmlOut != null && xmlOut.length() > 0)
						logWin.setXmlResponse(xmlOut);

					
					if(dataChiamataDa != null && dataChiamataA != null
					   && formatDate(dataChiamataDa, "yyyyMMddHHmmss").compareTo(formatDate(dataChiamataA, "yyyyMMddHHmmss")) > 0) {
						throw new Exception("'Da' non puo' essere maggiore di 'A'"); 
					}
					if(dataChiamataDa != null
					   && formatDate(dataChiamataDa, "yyyyMMddHHmmss").compareTo(formatDate(dataDa, "yyyyMMdd000000")) < 0) {
						throw new Exception("'Da' non puo' essere minore di: " + formatDate(dataDa, "dd/MM/yyyy 00:00:00")); 
					}
					if(dataChiamataA != null
					   && formatDate(dataChiamataA, "yyyyMMddHHmmss").compareTo(formatDate(dataA, "yyyyMMdd235959")) > 0) {
						throw new Exception("'A' non puo' essere maggiore di: " + formatDate(dataA, "dd/MM/yyyy 23:59:59")); 
					}
					
					
					if(dataChiamataDa != null)
						logWin.setDataInizioChiamata(toTimestampFromCalendar(dataChiamataDa, "yyyyMMddHHmmss"));
					if(dataChiamataA != null)
						logWin.setDataFineChiamata(toTimestampFromCalendar(dataChiamataA, "yyyyMMddHHmmss"));
							
//					session.setAttribute("dataChiamataDa", dataChiamataDa);
//					session.setAttribute("dataChiamataTimeDA", timeChiamataDa);
//					session.setAttribute("dataChiamataA", dataChiamataA);
//					session.setAttribute("dataChiamataTimeA", timeChiamataA);
					
					
					if(chiaveEnteda5 != null && chiaveEnteda5.length() > 0)
						logWin.setCodiceEnte(chiaveEnteda5);
					
					
//					if(getParamCodiceSocieta() != null && getParamCodiceSocieta().length() > 0)
//						logWin.setCodiceSocieta(getParamCodiceSocieta());
//
//					if(getParamCodiceUtente()!= null && getParamCodiceUtente().length() > 0)
//						logWin.setCodiceUtente(getParamCodiceUtente());
//					
//					if(getParamCodiceEnte() != null && getParamCodiceEnte().length() > 0)
//						logWin.setCodiceEnte(getParamCodiceEnte());

					Entity<LogWin> entity =  Entity.entity(logWin, MediaType.APPLICATION_JSON);
					
					String uri =  ManagerStarter.configuration.getProperty(PropertiesPath.wsLogRequest.format(PropertiesPath.defaultnode.format()));
					uri += "/searchWin";
					//TOGLIERE
					System.out.println("-uri: " + uri);

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
								
								request.setAttribute("listaLogWin", coll.getListXml());
								request.setAttribute("listaLogWin.pageInfo", pageInfo);
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
				request.setAttribute("dataChiamataDA", null);
				session.setAttribute("dataChiamataDA", null);
				
				request.setAttribute("dataChiamataA", null);
				session.setAttribute("dataChiamataA", null);
				
				request.setAttribute("dataChiamataTimeDA", "");
				request.setAttribute("dataChiamataTimeA", "");
				setProfile(request);
				break;
				
			case TX_BUTTON_NULL: 
				break;
			default:
				break;
				
		}
		return null;
	}



	protected void tx_SalvaStato(HttpServletRequest request) {
		super.tx_SalvaStato(request);
		request.setAttribute("dataChiamataDA", getCalendar(request, "dataChiamataDA"));
		request.setAttribute("dataChiamataA", getCalendar(request, "dataChiamataA"));
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
	
	private void dividiSocUtenteEnte(HttpServletRequest request) {
		// TODO Auto-generated method stub
		if(request.getAttribute("ddlSocietaUtenteEnte")!=null)
		{
			String ddlSocietaUtenteEnte = (String)request.getAttribute("ddlSocietaUtenteEnte");
			String[] codici = ddlSocietaUtenteEnte.split("\\|");
			if (!ddlSocietaUtenteEnte.equals("")&&codici.length>0) 
			{
				
				codiceSocieta = codici[0];
				cutecute = codici[1];
				chiaveEnte = codici[2];
				chiaveEnteda5 = codici[3].substring(1, 6);
				request.setAttribute("tx_societa", codiceSocieta);
				request.setAttribute("tx_utente", cutecute);
				request.setAttribute("tx_ente", chiaveEnte);
				request.setAttribute("tx_enteda5", chiaveEnteda5);
				
				
			}
		
		}
	}
	
	private void mantieniFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		if(request.getAttribute("tx_tipoChiamata")!=null)
			session.setAttribute("tx_tipoChiamata", request.getAttribute("tx_tipoChiamata"));
		
		if(request.getAttribute("tx_bollettino")!=null)
			session.setAttribute("tx_bollettino", request.getAttribute("tx_bollettino"));
		
		if(request.getAttribute("tx_codfis")!=null)
			session.setAttribute("tx_codfis", request.getAttribute("tx_codfis"));
		
		if(request.getAttribute("tx_esito")!=null)
			session.setAttribute("tx_esito", request.getAttribute("tx_esito"));
		
		if(request.getAttribute("tx_operatore")!=null)
			session.setAttribute("tx_operatore", request.getAttribute("tx_operatore"));
		
		if(request.getAttribute("tx_error")!=null)
			session.setAttribute("tx_error", request.getAttribute("tx_error"));

		if(request.getAttribute("dataChiamataDA")!=null)
			session.setAttribute("dataChiamataDA", request.getAttribute("dataChiamataDA"));
		if(request.getAttribute("dataChiamataTimeDA")!=null)
			session.setAttribute("dataChiamataTimeDA", request.getAttribute("dataChiamataTimeDA"));
		
		if(request.getAttribute("dataChiamataA")!=null)
			session.setAttribute("dataChiamataA", request.getAttribute("dataChiamataA"));
		if(request.getAttribute("dataChiamataTimeA")!=null)
			session.setAttribute("dataChiamataTimeA", request.getAttribute("dataChiamataTimeA"));
		
		
	}

	
	
}
