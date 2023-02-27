package org.seda.payer.manager.contogestione.actions;
/*
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;


/*
import com.seda.payer.contogestione.webservice.dati.ContoGestione;
import com.seda.payer.contogestione.webservice.dati.ContoGestioneSearchRequest;
import com.seda.payer.contogestione.webservice.dati.ContoGestioneSearchResponse;
import com.seda.payer.contogestione.webservice.dati.PrendiPathRequest;
import com.seda.payer.contogestione.webservice.dati.PrendiPathResponse;
import com.seda.payer.contogestione.webservice.dati.InviaPECRequest;
import com.seda.payer.contogestione.webservice.dati.InviaPECResponse;
import com.seda.payer.contogestione.webservice.dati.Paginazione;
import com.seda.payer.contogestione.webservice.srv.FaultType;
*//*
import com.seda.payer.impostasoggiorno.webservice.dati.ResponseTypeRetCode;

@SuppressWarnings("serial")
public class ContoGestioneSearchAction extends BaseContoGestioneAction {
	
	public Object service(HttpServletRequest request) throws ActionException 
	{
		super.service(request, "ricercaContoGestione");

		//ContoGestione contoGestione = null;
		
		switch (firedButton)
        {
        	case TX_BUTTON_RESET:
        		resetFormContoGestione(request);
        		setProfile(request);
/*
        		try{	
        		contoGestione = requestToContoGestione(request);
        		esecuzioneRicerca(contoGestione,request);
			}catch (Exception e) {
				WSCache.logWriter.logError("errore: " + e.getMessage(),e);
				setFormMessage("search_assoc_imp_form", "errore: " + testoErrore, request);
			}
*/		/*	
       		break;
        	case TX_BUTTON_DOWNLOAD:
        		getPathPdf(request);
    			break;
        	case TX_BUTTON_STEP1:
        		inviaPec(request);
        		break;
        	case TX_BUTTON_NULL:
        	case TX_BUTTON_DEFAULT:
        		resetGrid(request);
        		break;
        	case TX_BUTTON_CERCA:
        		try{
            		//resetGrid(request);
        			/*
        			contoGestione = requestToContoGestione(request);
	        		esecuzioneRicerca(contoGestione,request);*//*
        		}catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					setFormMessage("search_assoc_imp_form", "errore: " + testoErrore, request);
				}
        	break;
        }
        aggiornamentoCombo(request,request.getSession());
		
		return null;	
	}

	
	private void getPathPdf(HttpServletRequest request)
	{

		try
		{
			String filename = (String)request.getAttribute("filename"); 
			request.setAttribute("filename", filename);
			/*
			PrendiPathRequest prendiPathRequest = new PrendiPathRequest();
			PrendiPathResponse prendiPathResponse = null;
			prendiPathResponse = WSCache.contoGestioneServer.prendiPath(prendiPathRequest, request);
			String path = prendiPathResponse.getPath();
			request.setAttribute("filePath", path + "/" + filename);*//*
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			WSCache.logWriter.logError("errore: " + e.getMessage(),e);
			setFormMessage("search_assoc_imp_form", "Errore nel recupero del file", request);
		}
		
	}

	private void inviaPec(HttpServletRequest request)
	{

		try
		{
			InviaPECRequest inviaPECRequest = new InviaPECRequest();
			inviaPECRequest.setChiave((String)request.getAttribute("chiaveCG"));

			InviaPECResponse inviaPECResponse = null;
			inviaPECResponse = WSCache.contoGestioneServer.inviaPEC(inviaPECRequest, request);

			if (!inviaPECResponse.getRisultato().getRetCode().toString().equals(ResponseTypeRetCode.value1.toString()))
				setFormMessage("search_assoc_imp_form", "Errore nell'invio della mail", request);
			else
				setFormMessage("search_assoc_imp_form", "Mail inviata con successo", request);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			WSCache.logWriter.logError("errore: " + e.getMessage(),e);
			setFormMessage("search_assoc_imp_form", "Errore nel recupero del file", request);
		}
		
	}

	
	private void resetFormContoGestione(HttpServletRequest request)
	{
		resetParametri(request);
		request.setAttribute("codSocieta","");
		request.setAttribute("codProvincia","");
		request.setAttribute("tx_UtenteEnte","");
		request.setAttribute("tipoModello","");
		request.setAttribute("periodo","");
		request.setAttribute("tx_importo_da","");
		request.setAttribute("tx_importo_a","");
		request.setAttribute("listaPeriodi", null);
		resetGrid(request);
	}
	
	/*private TariffaImpostaSoggiorno getAssocBenByRequest(HttpServletRequest request)
	{
		TariffaImpostaSoggiorno tar = new TariffaImpostaSoggiorno();
		
		
		tar.setCodiceBelfiore("");
		tar.setChiaveTariffa("");
		tar.setDescrizioneComune(request.getAttribute("descrizioneComune") == null ? "" : (String)request.getAttribute("descrizioneComune"));
		tar.setDescrizioneTipologiaRicettiva(request.getAttribute("descrizioneStrutturaRicettiva") == null ? "" : (String)request.getAttribute("descrizioneStrutturaRicettiva"));

//gestione data
 		tar.setRicercaDataDa(getDateByTag(request, "tx_data_da")==null?"":sdf.format(getDateByTag(request, "tx_data_da")));
 		tar.setRicercaDataA(getDateByTag(request, "tx_data_a")==null?"":sdf.format(getDateByTag(request, "tx_data_a")));
		
		return tar;
	}*/

	/*private boolean testCampiTariffaImpostaSoggiorno(TariffaImpostaSoggiorno tar, HttpServletRequest request)
	{
		boolean risultato = true;
		String messaggio = "";
		
		if (!tar.getRicercaDataDa().equals("")&&!compareDateStringISO(tar.getRicercaDataDa(), "tx_data_da", request))
		{
			messaggio = "<br>Inserire una data 'Da' valida";
			tar.setRicercaDataDa("");
			request.setAttribute("tx_data_da", null);
		}
		else
		{
				request.setAttribute("tx_data_da", getCalendarByDate(request, getDateByTag(request, "tx_data_da")));
		}
			
		if (!tar.getRicercaDataA().equals("")&&!compareDateStringISO(tar.getRicercaDataA(), "tx_data_a", request))
		{
			messaggio = messaggio + "<br>Inserire una data 'A' valida"; 
			tar.setRicercaDataA("");
			request.setAttribute("tx_data_a", null);
		}
		else
		{
			request.setAttribute("tx_data_a", getCalendarByDate(request, getDateByTag(request, "tx_data_a")));			
		}	
		
		if (messaggio.length()>0)
		{
			messaggio = messaggio.substring(4);
			setFormMessage("search_assoc_imp_form", messaggio, request);
			risultato = false;
		}
		
		return risultato;
	}*/
	
	/*
	private PageInfo getPageInfo(com.seda.payer.contogestione.webservice.dati.PageInfo rpi, int rowsPerPages) 
	{
		PageInfo pageInfo =  new PageInfo(); 
		pageInfo.setFirstRow(rpi.getFirstRow());		
		pageInfo.setLastRow(rpi.getLastRow());
		pageInfo.setNumPages(rpi.getNumPages());
		pageInfo.setNumRows(rpi.getNumRows());
		pageInfo.setPageNumber(rpi.getPageNumber());
		pageInfo.setRowsPerPage(rowsPerPages);
		return pageInfo;
	}
	*/
	
	/*
	public void esecuzioneRicerca(ContoGestione contoGestione,HttpServletRequest request) throws FaultType, Exception
	{
		String order = null;
		int rowsPerPage = 0;
		int pageNumber = 0;

		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");

		try 
		{
			ContoGestioneSearchResponse res = null;
			ContoGestioneSearchRequest in = new ContoGestioneSearchRequest();

			Paginazione pag = new Paginazione();
			
			pag.setPageNumber(pageNumber);
			pag.setRowsPerPage(rowsPerPage);
			pag.setOrder(order);
			
			in.setPagina(pag);
						
			in.setContoGestione(contoGestione);
			
			res = WSCache.contoGestioneServer.getContiGestione(in, request);

			if (res.getPInfo() != null)
			{
				if (res.getPInfo().getNumRows() > 0)
				{
					request.setAttribute("listaContiGestione", res.getListXml());
					request.setAttribute("listaContiGestione.pageInfo", getPageInfo(res.getPInfo(), rowsPerPage));
				}
				else
				{
					request.setAttribute("listaContiGestione", null);
					setFormMessage("search_assoc_imp_form", Messages.NO_DATA_FOUND.format(), request);
				}
			}
			else setFormMessage("search_assoc_imp_form", Messages.NO_DATA_FOUND.format(), request);
		} 
		catch (FaultType e) 
		{
			e.printStackTrace();
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}			
					
		//request.setAttribute("NumRighe", Integer.toString(out.getPInfo().getNumRows()));

	}
	
	
	private ContoGestione requestToContoGestione(HttpServletRequest request) throws Exception
	{
		ContoGestione contoGestione = new ContoGestione();
		contoGestione.setCodSocieta(request.getAttribute("codSocieta")!=null?request.getAttribute("codSocieta").toString():"");
		contoGestione.setCodProvincia(request.getAttribute("codProvincia")!=null?request.getAttribute("codProvincia").toString():"");
     
//        associazione.setCodUtente(request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"");
        String codEnte = request.getAttribute("tx_UtenteEnte")!=null?request.getAttribute("tx_UtenteEnte").toString():"";
        
		if(codEnte.length()>0){
			contoGestione.setCodEnte(codEnte.substring(10));
	        contoGestione.setCodUtente(codEnte.substring(5,10));
			contoGestione.setCodSocieta(codEnte.substring(0,5));
		}else{
			contoGestione.setCodUtente("");
			contoGestione.setCodEnte("");
		}
        
        contoGestione.setTipoModello(request.getAttribute("tipoModello")!=null?request.getAttribute("tipoModello").toString():"");
        contoGestione.setPeriodo(request.getAttribute("periodo")!=null?request.getAttribute("periodo").toString():"");
        String importoDa = request.getAttribute("tx_importo_da")!=null && request.getAttribute("tx_importo_da").toString().length()>0?request.getAttribute("tx_importo_da").toString():"0";
        contoGestione.setImportoDa(getImporto(importoDa));
        String importoA = request.getAttribute("tx_importo_a") !=null && request.getAttribute("tx_importo_a").toString().length()>0?request.getAttribute("tx_importo_a").toString():"0";
        contoGestione.setImportoA(getImporto(importoA));
        
        contoGestione.setOperatorCode(""); 
        
        return contoGestione;
        
	}
	
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
			if (getFiredButton(request)==FiredButton.TX_BUTTON_SOCIETA_CHANGED) 
			{
				loadProvinciaXml_DDL(request, session, codiceSocieta, true);
				LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, null,null,null, true);
//				loadPeriodi_DDL(request);
				resetGrid(request);
			}
			else if (getFiredButton(request)==FiredButton.TX_BUTTON_PROVINCIA_CHANGED) 
			{
				LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia,null,null,true);
				LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia,null,null, true);
//				loadPeriodi_DDL(request);
				resetGrid(request);
			}//MGB-inizio
			else if (getFiredButton(request)==FiredButton.TX_BUTTON_ENTE_CHANGED) 
			{
				loadProvinciaXml_DDL(request, session, codiceSocieta, false);
				LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia,null,null, true);
//				loadPeriodi_DDL(request);
				resetGrid(request);
			}
			//FINE MGB
			else if (getFiredButton(request)==FiredButton.TX_BUTTON_MODELLO_CHANGED) 
			{
				loadProvinciaXml_DDL(request, session, codiceSocieta, false);
				LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia,null,null, false);			
//				loadPeriodi_DDL(request);
				request.setAttribute("listaPeriodi", "");
				request.setAttribute("pageNumber", null);
				request.setAttribute("rowsPerPage", null);
				request.setAttribute("order", null);
//				resetGrid(request);
			}
			else 
			{
				loadProvinciaXml_DDL(request, session, codiceSocieta, true);
				LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, codiceProvincia,null,null, true);
//				loadPeriodi_DDL(request);
			}

			loadPeriodi_DDL(request);

		}

	private BigDecimal getImporto(String importo) throws Exception {
		BigDecimal bImporto;
		if(importo.indexOf(".") != -1)
			bImporto = new BigDecimal((new java.text.DecimalFormat("#,##0.00")).parse(importo).doubleValue());
		else if(importo.indexOf(",") != -1)
			bImporto = new BigDecimal(importo.replace(',', '.'));
		else
			bImporto = new BigDecimal(importo);
		return bImporto;
	}
	

}
*/