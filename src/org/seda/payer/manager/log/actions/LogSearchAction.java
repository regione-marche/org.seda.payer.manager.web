package org.seda.payer.manager.log.actions;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.dati.LogAccessiRequestType;
import com.seda.payer.pgec.webservice.commons.dati.LogAccessiResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class LogSearchAction extends BaseManagerAction{
	
	public Object service(HttpServletRequest request) throws ActionException {
		//
		HttpSession session = request.getSession();
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order");
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Chiamo il metodo per i settaggi relativi al profilo utente
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) setProfile(request);
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
		//loadPayerApplicationsXml_DDL(request, true, true, true);
		loadPayerApplicationsXml_DDL_properties(request, session, true, true);
		/*
		 *  Ricavo i parametri della request
		 */
		String userName = ((String)request.getAttribute("tx_username") == null ? "" : (String)request.getAttribute("tx_username"));
		String userProfile = ((String)request.getAttribute("tx_userprofile") == null ? "" : (String)request.getAttribute("tx_userprofile"));
		String applicazione = ((String)request.getAttribute("tx_applicazione") == null ? "" : (String)request.getAttribute("tx_applicazione"));
		String indirizzoIP = ((String)request.getAttribute("indirizzo_ip") == null ? "" : (String)request.getAttribute("indirizzo_ip"));
		String canale = ((String)request.getAttribute("tx_canale_pagamento") == null ? "" : (String)request.getAttribute("tx_canale_pagamento"));
		int rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		int pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		String order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");
		String siglaProvincia = ((String)request.getAttribute("tx_provincia") == null) ? "" : (String)request.getAttribute("tx_provincia");
		Calendar dataInizioSessioneDa = (Calendar)request.getAttribute("inizioSessioneDA");
		Calendar dataInizioSessioneA = (Calendar)request.getAttribute("inizioSessioneA");
		/*
		 * Setto l'orario all'inizio della giornata per "dataInizioSessioneDa"
		 */
		if(dataInizioSessioneDa != null)
		{
			dataInizioSessioneDa.set(dataInizioSessioneDa.get(Calendar.YEAR), dataInizioSessioneDa.get(Calendar.MONTH), dataInizioSessioneDa.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		}
		/*
		 * Setto l'orario alla fine della giornata per "dataInizioSessioneA"
		 */
		if(dataInizioSessioneA != null)
		{
			dataInizioSessioneA.set(dataInizioSessioneA.get(Calendar.YEAR), dataInizioSessioneA.get(Calendar.MONTH), dataInizioSessioneA.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
		}
		com.seda.payer.pgec.webservice.commons.dati.PageInfo wsPageInfo = null;
		ResponseType response = new ResponseType();
		/*
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		switch(firedButton)
		{
			case TX_BUTTON_CERCA: 
				try {
					loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
					LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), false);


					LogAccessiRequestType in = new LogAccessiRequestType(pageNumber, rowsPerPage, order, userName, indirizzoIP, applicazione, userProfile, canale, dataInizioSessioneDa, dataInizioSessioneA, getParamCodiceSocieta(), siglaProvincia, getParamCodiceUtente(), getParamCodiceEnte());
					LogAccessiResponseType out = WSCache.commonsServer.logAccessi(in, request);
					response = out.getResponse();
					
					if (response.getRetCode().equals(ResponseTypeRetCode.value1))
					{
						wsPageInfo = out.getPageInfo();
						if(wsPageInfo != null)
						{
							if(wsPageInfo.getNumRows()>0)
							{
								com.seda.data.spi.PageInfo pageInfo = new PageInfo();
								pageInfo.setFirstRow(wsPageInfo.getFirstRow());
								pageInfo.setLastRow(wsPageInfo.getLastRow());
								pageInfo.setNumPages(wsPageInfo.getNumPages());
								pageInfo.setNumRows(wsPageInfo.getNumRows());
								pageInfo.setPageNumber(wsPageInfo.getPageNumber());
								pageInfo.setRowsPerPage(wsPageInfo.getRowsPerPages());
								
								request.setAttribute("listaLog", out.getListaXml());
								request.setAttribute("listaLog.pageInfo", pageInfo);
								request.setAttribute("riepilogoLog", out.getListaRiepilogoXml());
							}
							else setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
						}
						else setFormMessage("form_selezione", Messages.NO_DATA_FOUND.format(), request);
					}
					else
					{
						String messaggio = response.getRetMessage(); 
						setFormMessage("form_selezione", (messaggio == null || messaggio.equals("")) ? "Errore generico" : messaggio, request);
					}
			} catch (FaultType e) {
				setFormMessage("form_selezione", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("form_selezione", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}

				break;
				
			case TX_BUTTON_RESET:
				resetParametri(request);
				request.setAttribute("inizioSessioneDA", null);
				request.setAttribute("inizioSessioneA", null);
				setProfile(request);
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				break;
				
			case TX_BUTTON_SOCIETA_CHANGED:
				String societa = getParamCodiceSocieta();
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, societa, (societa.equals("") ? "" : siglaProvincia), (societa.equals("") ? "" : getParamCodiceEnte()), (societa.equals("") ? "" : getParamCodiceUtente()), true);
				request.setAttribute("tx_provincia","");
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),false);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, (siglaProvincia.equals("") ? "" : getParamCodiceEnte()), (siglaProvincia.equals("") ? "" : getParamCodiceUtente()), true);
				request.setAttribute("tx_UtenteEnte", "");
				break;
				
			case TX_BUTTON_NULL: 
				loadProvinciaXml_DDL(request, session, getParamCodiceSocieta(),true);
				LoadListaUtentiEntiXml_DDL(request, session, getParamCodiceSocieta(), siglaProvincia, getParamCodiceEnte(), getParamCodiceUtente(), true);
				break;
				
		}
		return null;
	}
	
}
