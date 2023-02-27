package org.seda.payer.manager.monitoraggiowis.actions;

import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.string.Convert;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaComunicazioniCsvRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaComunicazioniCsvResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaComunicazioniRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaComunicazioniResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.ResponseType;
import com.seda.payer.impostasoggiorno.webservice.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaComuniBySiglaProvinciaRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaComuniBySiglaProvinciaResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaProvinceResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

public class MonitoraggioWisAction extends BaseManagerAction {

	private static final long serialVersionUID = 1L;
	private String siglaProvincia;
	private String codiceBelfiore;
	private String numeroAutorizzazione;
	private String dataInserimentoDa;
	private String dataInserimentoA;
	private String dataComunicazioneDa;
	private String dataComunicazioneA;
	private String tipoComunicazione;
	private String statoComunicazione;
	private String numeroDocumento;
	private String statoPagamento;
	private String usernameOperatore;
	private String codiceTipologiaStruttura;
	private String insegna;
	//PG160210_001 GG 07122016 - inizio
	private String dataConfermaDa;
	private String dataConfermaA;
	//PG160210_001 GG 07122016 - fine
	
	private String templateName;
		
	public Object service(HttpServletRequest request) throws ActionException 
	{
		super.service(request);
		
		/*
		 * Gestione del viewstate per le azioni nei datagrid senza l'utilizzo dei parameter.
		 */
		replyAttributes(false, request,"pageNumber","rowsPerPage","order","validator.message");
		
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Chiamo il metodo per i settaggi relativi al profilo utente
		 */
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET)) 
			setProfile(request);

		HttpSession session = request.getSession();
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		templateName = userBean.getTemplate(applicationName);
		if (templateName.equals("trentrisc"))
			request.setAttribute("ddlProvincia", "TN");
        //inizio LP PG1800XX_016
		if (templateName.equals("sassari")) {
			request.setAttribute("ddlProvincia", "SS");
			request.setAttribute("ddlComune", "I452");
		}
        //fine LP PG1800XX_016
		getParametriForm(request);
		loadListaProvince(request);
		if (codiceBelfiore!=null && codiceBelfiore.length() > 0)
			loadListaComuni(siglaProvincia,request);
		loadListaTipoStrutture(request);
		
		switch(firedButton) {
			case TX_BUTTON_CERCA: 
				ricercaComunicazioni(request);
				break;
				
				
			case TX_BUTTON_RESET:
				resetParametri(request);
				request.setAttribute("tx_data_presentazione_da", null);
				request.setAttribute("tx_data_presentazione_a", null);
				request.setAttribute("tx_data_comunicazione_da", null);
				request.setAttribute("tx_data_comunicazione_a", null);
				//PG160210_001 GG 07122016 - inizio
				request.setAttribute("tx_data_conferma_da", null);
				request.setAttribute("tx_data_conferma_a", null);
				//PG160210_001 GG 07122016 - fine
				setProfile(request);
				if (templateName.equals("trentrisc"))
				{
					siglaProvincia = (String) request.getAttribute("ddlProvincia");
					loadListaComuni(siglaProvincia,request);
				}
		        //inizio LP PG1800XX_016
				if (templateName.equals("sassari"))
				{
					request.setAttribute("ddlProvincia", "SS");
					request.setAttribute("ddlComune", "I452");
					siglaProvincia = "SS";
					codiceBelfiore =  "I452";
					loadListaComuni(siglaProvincia,request);
				}
		        //fine LP PG1800XX_016
				break;
				
			case TX_BUTTON_COMUNE_CHANGED:
				resetParametri(request);
				request.setAttribute("tx_data_presentazione_da", null);
				request.setAttribute("tx_data_presentazione_a", null);
				request.setAttribute("tx_data_comunicazione_da", null);
				request.setAttribute("tx_data_comunicazione_a", null);
				//PG160210_001 GG 07122016 - inizio
				request.setAttribute("tx_data_conferma_da", null);
				request.setAttribute("tx_data_conferma_a", null);
				//PG160210_001 GG 07122016 - fine
				setProfile(request);
				request.setAttribute("ddlProvincia", siglaProvincia);
				request.setAttribute("ddlComune", codiceBelfiore);
				break;

			case TX_BUTTON_PROVINCIA_CHANGED:
				loadListaComuni(siglaProvincia, request);
				break;
			
			case TX_BUTTON_NULL: 
				if (!templateName.equals("trentrisc"))
		        //inizio LP PG1800XX_016
				{
					if (templateName.equals("sassari"))
					{
						siglaProvincia = (String) request.getAttribute("ddlProvincia");
						codiceBelfiore = (String) request.getAttribute("ddlcomune");
						loadListaComuni(siglaProvincia,request);
					} else {
		        //fine LP PG1800XX_016
					loadListaProvince(request);
		        //inizio LP PG1800XX_016
					}
				}
		        //fine LP PG1800XX_016
				else
				{
					siglaProvincia = (String) request.getAttribute("ddlProvincia");
					loadListaComuni(siglaProvincia,request);
				}
				break;
				
			case TX_BUTTON_DOWNLOAD:
				boolean bDownloadOk = true;
				// PG140470
				//if (codiceBelfiore != null && codiceBelfiore.length() > 0)
				if ((!templateName.equals("trentrisc") && (codiceBelfiore != null && codiceBelfiore.length() > 0 )) || templateName.equals("trentrisc") )
				{
					String file="";
					try {
						RecuperaListaComunicazioniCsvResponse resCsv = getListaComunicazioniCsv(request);
						if (resCsv != null && resCsv.getResponse() != null && resCsv.getResponse().getRetCode().getValue().equals("00"))
						{
							file = resCsv.getStringaCsv();
							//aggiustamento carattere \r perso nel webservice 
							file = file.replaceAll("\n", "\r\n");
							Calendar cal = Calendar.getInstance();
							String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
							request.setAttribute("filename",timestamp + "_CcomunicazioniImpostaSoggiorno.csv");
							request.setAttribute("fileCsv", file);
						}
						else
						{
							setFormMessage("monitoraggioWISForm", "Si è verificato un errore durante l'esportazione dei dati" , request);
							bDownloadOk = false;
						}
						
					} catch (FaultType e1) {
						setFormMessage("monitoraggioWISForm", e1.getMessage() , request);
						bDownloadOk = false;
						e1.printStackTrace();
					} catch (RemoteException e1) {
						setFormMessage("monitoraggioWISForm", e1.getMessage() , request);
						bDownloadOk = false;
						e1.printStackTrace();
					}
				}
				else
				{
					setFormMessage("monitoraggioWISForm", "Per poter eseguire il Download è necessario filtrare i dati per Provincia e Comune", request);
					bDownloadOk = false;
				}
				
				if (!bDownloadOk)
				{
					request.setAttribute("download","N");
					ricercaComunicazioni(request);
				}
				break;
							
		}
		request.setAttribute(Field.TX_MESSAGE.format(), getMessage());
		request.setAttribute(Field.TX_ERROR_MESSAGE.format(), getErrorMessage());
		return null;
	}
	
	private void getParametriForm(HttpServletRequest request)
	{
		 siglaProvincia = isNull(request.getAttribute("ddlProvincia"));
		 codiceBelfiore = isNull(request.getAttribute("ddlComune"));
		 numeroAutorizzazione  = isNull(request.getAttribute("txtNumAutorizzazione"));
		 
		 if (request.getAttribute("tx_data_presentazione_da") !=  null)
			 dataInserimentoDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_data_presentazione_da"), "yyyy-MM-dd");
		 else
			 dataInserimentoDa = "";
		 if (request.getAttribute("tx_data_presentazione_a") !=  null)
			 dataInserimentoA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_data_presentazione_a"), "yyyy-MM-dd");
		 else
			 dataInserimentoA = "";
		 
		 if (request.getAttribute("tx_data_comunicazione_da") !=  null)
			 dataComunicazioneDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_data_comunicazione_da"), "yyyy-MM-dd");
		 else
			 dataComunicazioneDa = "";
		 if (request.getAttribute("tx_data_comunicazione_a") !=  null)
			 dataComunicazioneA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_data_comunicazione_a"), "yyyy-MM-dd");
		 else
			 dataComunicazioneA = "";
		 
		 //PG160210_001 GG 07122016 - inizio
		 if (request.getAttribute("tx_data_conferma_da") !=  null)
			 dataConfermaDa = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_data_conferma_da"), "yyyy-MM-dd");
		 else
			 dataConfermaDa = "";
		 if (request.getAttribute("tx_data_conferma_a") !=  null)
			 dataConfermaA = GenericsDateNumbers.formatCalendarData((Calendar)request.getAttribute("tx_data_conferma_a"), "yyyy-MM-dd");
		 else
			 dataConfermaA = "";
		//PG160210_001 GG 07122016 - fine
		 
		 tipoComunicazione = isNull(request.getAttribute("txtTipoComunicazione"));
		 statoComunicazione = isNull(request.getAttribute("txtStatoComunicazione"));
		 numeroDocumento = isNull(request.getAttribute("txtNumeroDocumento"));
		 statoPagamento = isNull(request.getAttribute("txtStatoPagamento"));
		 usernameOperatore = isNull(request.getAttribute("txtUsernameOperatore"));
		 codiceTipologiaStruttura = isNull(request.getAttribute("ddlTipoStruttura"));
		 insegna = isNull(request.getAttribute("txtInsegna"));
		
	}
	
	private void ricercaComunicazioni(HttpServletRequest request)
	{
		try {
			
			RecuperaListaComunicazioniResponse res = getListaComunicazioni(request);
			if (res != null)
			{
				ResponseType response = res.getResponse();
				
				if(response != null && response.getRetCode().getValue().equals("00"))
				{
					PageInfo pageInfo = this.getPageInfo(res.getPageInfo());
					if(pageInfo != null)
					{
						if (pageInfo.getNumRows() > 0)
						{
							String lista = res.getListComunicazioniXml();					
							request.setAttribute("listaComunicazioni", lista);
							request.setAttribute("listaComunicazioni.pageInfo", pageInfo);
							
							request.setAttribute("numcomunicazionitotali", pageInfo.getNumRows());
							request.setAttribute("soggettitotali", res.getTotSoggetti());
							request.setAttribute("pernottamentiimpostatotali", res.getTotPernottamentiSoggettiAdImposta());
							request.setAttribute("importototale", res.getTotImporto());
						}
						else 
							setFormMessage("monitoraggioWISForm", Messages.NO_DATA_FOUND.format(), request);
					}
					else 
						setFormMessage("monitoraggioWISForm", "Errore generico - PageInfo null", request);
				}
				else
					setFormMessage("monitoraggioWISForm", response.getRetMessage() , request);
			}
			else 
				setFormMessage("monitoraggioWISForm", Messages.NO_DATA_FOUND.format() , request);
			
			if (siglaProvincia != null && siglaProvincia.length() > 0)
				loadListaComuni(siglaProvincia, request);
			
		} catch (RemoteException e) {
			setFormMessage("monitoraggioWISForm", e.getMessage() , request);
			e.printStackTrace();
		} catch(Exception e) {
			setFormMessage("monitoraggioWISForm", e.getMessage() , request);
			e.printStackTrace();
		}
	}
	
	private RecuperaListaComunicazioniResponse getListaComunicazioni(HttpServletRequest request) throws com.seda.payer.impostasoggiorno.webservice.srv.FaultType, RemoteException
	{
		int rowsPerPage = request.getAttribute(Field.ROWS_PER_PAGE.format()) == null ? getDefaultListRows(request) : isNullInt(request.getAttribute(Field.ROWS_PER_PAGE.format()));
		int pageNumber = request.getAttribute(Field.PAGE_NUMBER.format()) == null ? 1 : isNullInt(request.getAttribute(Field.PAGE_NUMBER.format()));
		String order = isNull(request.getAttribute(Field.ORDER_BY.format()));
		
		RecuperaListaComunicazioniRequest req = new RecuperaListaComunicazioniRequest();
		req.setSiglaProvincia(siglaProvincia);
		req.setCodiceBelfiore(codiceBelfiore);
		req.setNumeroAutorizzazione(numeroAutorizzazione);
		req.setDataInserimentoDa(dataInserimentoDa);
		req.setDataInserimentoA(dataInserimentoA);
		req.setDataComunicazioneDa(dataComunicazioneDa);
		req.setDataComunicazioneA(dataComunicazioneA);
		req.setTipoComunicazione(tipoComunicazione);
		req.setStatoComunicazione(statoComunicazione);
		req.setNumeroDocumento(numeroDocumento);
		req.setStatoPagamento(statoPagamento);
		req.setUsernameOperatore(usernameOperatore);
		req.setCodiceTipologiaStruttura(codiceTipologiaStruttura);
		req.setInsegna(insegna);
		//PG160210_001 GG 07122016 - inizio
		req.setDataConfermaDa(dataConfermaDa);
		req.setDataConfermaA(dataConfermaA);
		//PG160210_001 GG 07122016 - fine
		
		req.setRowsPerPage(rowsPerPage);
		req.setPageNumber(pageNumber);
		req.setOrder(order);
		 
		return WSCache.impostaSoggiornoConfigServer.recuperaListaComunicazioni(req, request);
	}
	
	private RecuperaListaComunicazioniCsvResponse getListaComunicazioniCsv(HttpServletRequest request) throws com.seda.payer.impostasoggiorno.webservice.srv.FaultType, RemoteException
	{
		if (codiceBelfiore!=null && codiceBelfiore.length() > 0)
		{
			RecuperaListaComunicazioniCsvRequest req = new RecuperaListaComunicazioniCsvRequest();
			req.setSiglaProvincia(siglaProvincia);
			req.setCodiceBelfiore(codiceBelfiore);
			req.setNumeroAutorizzazione(numeroAutorizzazione);
			req.setDataInserimentoDa(dataInserimentoDa);
			req.setDataInserimentoA(dataInserimentoA);
			req.setDataComunicazioneDa(dataComunicazioneDa);
			req.setDataComunicazioneA(dataComunicazioneA);
			req.setTipoComunicazione(tipoComunicazione);
			req.setStatoComunicazione(statoComunicazione);
			req.setNumeroDocumento(numeroDocumento);
			req.setStatoPagamento(statoPagamento);
			req.setUsernameOperatore(usernameOperatore);
			req.setCodiceTipologiaStruttura(codiceTipologiaStruttura);
			req.setInsegna(insegna);
			//PG160210_001 GG 07122016 - inizio
			req.setDataConfermaDa(dataConfermaDa);
			req.setDataConfermaA(dataConfermaA);
			//PG160210_001 GG 07122016 - fine
			req.setTemplate(templateName);
			
			return WSCache.impostaSoggiornoConfigServer.recuperaListaComunicazioniCsv(req, request);
		}
		else
		{
			StringBuffer fileCSV = new StringBuffer();
			ResponseTypeRetCode retCodeCSV = null;
			String retMessageCSV = "";
			RecuperaListaComunicazioniCsvResponse multiFileCsv = new RecuperaListaComunicazioniCsvResponse(); 
			ResponseType responseCSV = new ResponseType();
			loadListaComuni(siglaProvincia,request);
			//inizio LP PG21XX04 Leak
			CachedRowSet ecCached = null;
			//fine LP PG21XX04 Leak
			try {
				//inizio LP PG21XX04 Leak
				//CachedRowSet ecCached = Convert.stringToWebRowSet(request.getAttribute("listcomuni").toString());
				ecCached = Convert.stringToWebRowSet(request.getAttribute("listcomuni").toString());
				//fine LP PG21XX04 Leak
				while (ecCached.next())
				{
					System.out.println("Codice Belfiore : " + ecCached.getString(3) + "-");
					RecuperaListaComunicazioniCsvRequest req = new RecuperaListaComunicazioniCsvRequest();
					req.setSiglaProvincia(siglaProvincia);
					req.setCodiceBelfiore(ecCached.getString(3));
					req.setNumeroAutorizzazione(numeroAutorizzazione);
					req.setDataInserimentoDa(dataInserimentoDa);
					req.setDataInserimentoA(dataInserimentoA);
					req.setDataComunicazioneDa(dataComunicazioneDa);
					req.setDataComunicazioneA(dataComunicazioneA);
					req.setTipoComunicazione(tipoComunicazione);
					req.setStatoComunicazione(statoComunicazione);
					req.setNumeroDocumento(numeroDocumento);
					req.setStatoPagamento(statoPagamento);
					req.setUsernameOperatore(usernameOperatore);
					req.setCodiceTipologiaStruttura(codiceTipologiaStruttura);
					req.setInsegna(insegna);
					//PG160210_001 GG 07122016 - inizio
					req.setDataConfermaDa(dataConfermaDa);
					req.setDataConfermaA(dataConfermaA);
					//PG160210_001 GG 07122016 - fine
					req.setTemplate(templateName);
					
					RecuperaListaComunicazioniCsvResponse recuperaListaComunicazioniCsvResponse = WSCache.impostaSoggiornoConfigServer.recuperaListaComunicazioniCsv(req, request);
					if (recuperaListaComunicazioniCsvResponse.getResponse().getRetCode().getValue().equals("00"))
					//inizio LP PG200230
					{ 
						retCodeCSV = recuperaListaComunicazioniCsvResponse.getResponse().getRetCode();
						retMessageCSV  = recuperaListaComunicazioniCsvResponse.getResponse().getRetMessage();
						boolean bAll = false;
						//Nota. Venivano scaricati anche dati di comuni privi di comunicazioni
						//      se si vuole tornare indietro impostare la variabile bAll = true;
						if(bAll) {
					//fine LP PG200230
							fileCSV.append(recuperaListaComunicazioniCsvResponse.getStringaCsv());
					//inizio LP PG200230
						} else {
							String sCsv = recuperaListaComunicazioniCsvResponse.getStringaCsv();
							String[] sRow = sCsv.split("\n");
							if(sRow.length > 2) {
								fileCSV.append(recuperaListaComunicazioniCsvResponse.getStringaCsv());
							}
						}
					}
					if(retCodeCSV == null) {
					//fine LP PG200230
						retCodeCSV = recuperaListaComunicazioniCsvResponse.getResponse().getRetCode();
						retMessageCSV  = recuperaListaComunicazioniCsvResponse.getResponse().getRetMessage();
					//inizio LP PG200230
					}
					//fine LP PG200230
				}
				responseCSV.setRetCode(retCodeCSV);
				responseCSV.setRetMessage(retMessageCSV);
				multiFileCsv.setStringaCsv(fileCSV.toString());
				multiFileCsv.setResponse(responseCSV);
				return multiFileCsv;
			} catch (SQLException e) {
				setFormMessage("monitoraggioWISForm recuperaListaComunicazioniCsv failed, generic error due to: ", e.getMessage() , request);
				e.printStackTrace();
			} catch (IOException e) {
				setFormMessage("monitoraggioWISForm recuperaListaComunicazioniCsv failed, generic error due to: ", e.getMessage() , request);
				e.printStackTrace();
			}
			//inizio LP PG21XX04 Leak
			finally {
				try {
					if(ecCached != null) {
						ecCached.close();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			//fine LP PG21XX04 Leak
		}
		return null;
			
	}
	
	private PageInfo getPageInfo(com.seda.payer.impostasoggiorno.webservice.dati.PageInfo rpi) {
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rpi.getRowsPerPage());
		return pageInfo;
	}
	
	private void loadListaProvince(HttpServletRequest request)
	{
		try 
		{
			RecuperaProvinceResponse getProvinceRes = WSCache.commonsServer.recuperaProvince(request);
			request.setAttribute("listprovince", getProvinceRes.getListXml());
		} catch (RemoteException e) {
			setFormMessage("monitoraggioWISForm", e.getMessage() , request);
			e.printStackTrace();
		}
	}
	
	private void loadListaComuni(String siglaProvincia, HttpServletRequest request)
	{
		try
		{
			loadListaProvince(request);
			if (siglaProvincia != null && siglaProvincia.length() > 0)
			{
				RecuperaComuniBySiglaProvinciaResponse res = WSCache.commonsServer.recuperaComuni_SiglaProvincia(new RecuperaComuniBySiglaProvinciaRequest(siglaProvincia), request);
				request.setAttribute("listcomuni", res.getListXml());
			}
		} catch (RemoteException e) {
			setFormMessage("monitoraggioWISForm", e.getMessage() , request);
			e.printStackTrace();
		}
	}
	
	private void loadListaTipoStrutture(HttpServletRequest request)
	{
		RecuperaListaTipologiaStrutturaRicettivaRequest in = new RecuperaListaTipologiaStrutturaRicettivaRequest();
		in.setDescrizioneTipologiaStruttura("");
		try 
		{
			RecuperaListaTipologiaStrutturaRicettivaResponse res = WSCache.impostaSoggiornoConfigServer.ricercaTipologiaStrutture(in, request);
			request.setAttribute("listtipologiestrutture", res.getListXml());
		} catch (RemoteException e) {
			setFormMessage("monitoraggioWISForm", e.getMessage() , request);
			e.printStackTrace();
		}
		
	}

}
