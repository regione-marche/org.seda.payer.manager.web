package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;


import com.seda.payer.pgec.webservice.assocben.dati.AssocBen;
import com.seda.payer.pgec.webservice.assocben.dati.AssocBenSearchRequest;
import com.seda.payer.pgec.webservice.assocben.dati.AssocBenSearchResponse;
import com.seda.payer.pgec.webservice.assocben.dati.Paginazione;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class AssocBenActionSearch extends BaseRiversamento2Action {
	
	
	public Object service(HttpServletRequest request) throws ActionException 
	{
		super.service(request, "ricercaAssocBen");

		AssocBen associazione = null;
		switch (firedButton)
        {
        	case TX_BUTTON_AGGIUNGI:
        		request.setAttribute("start","A");
        		request.setAttribute("vista","ricercaAssocBen");
        		request.setAttribute("codop","add");
        		break;
        	case TX_BUTTON_RESET:
        		resetFormAssocBen(request);
        		setProfile(request);
        	try{	
        		associazione = requestToAssociazione(request);
        		esecuzioneRicerca(associazione,request);
			}catch (Exception e) {
				WSCache.logWriter.logError("errore: " + e.getMessage(),e);
				setFormMessage("search_assoc_imp_form", "errore: " + testoErrore, request);
			}
       		break;
        	case TX_BUTTON_CERCA:
        		resetGrid(request);
        	case TX_BUTTON_NULL:
        	case TX_BUTTON_DEFAULT:
        		try{
        			associazione = requestToAssociazione(request);
	        		//if (testCampiTariffaImpostaSoggiorno(tar, request))
	        		esecuzioneRicerca(associazione,request);
        		}catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					setFormMessage("search_assoc_imp_form", "errore: " + testoErrore, request);
				}
        	break;
        }
        aggiornamentoCombo(request,request.getSession());
		
		return null;	
	}

	
	private void resetFormAssocBen(HttpServletRequest request)
	{
		resetParametri(request);
		request.setAttribute("codSocieta","");
		request.setAttribute("codProvincia","");
		request.setAttribute("codUtente","");
		request.setAttribute("codBeneficiario","");
		request.setAttribute("annoRifDaS","");
		request.setAttribute("annoRifAS","");
		

		request.setAttribute("tx_data_validitaS", null);
		
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
	
	
	private PageInfo getPageInfo(com.seda.payer.pgec.webservice.assocben.dati.PageInfo rpi, int rowsPerPages) 
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
	
	
	
	public void esecuzioneRicerca(AssocBen associazione,HttpServletRequest request) throws FaultType, Exception
	{
		String order = null;
		int rowsPerPage = 0;
		int pageNumber = 0;

		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");

		try 
		{
			AssocBenSearchResponse res = null;
			AssocBenSearchRequest in = new AssocBenSearchRequest();

			Paginazione pag = new Paginazione();
			
			pag.setPageNumber(pageNumber);
			pag.setRowsPerPage(rowsPerPage);
			pag.setOrder(order);
			
			in.setPagina(pag);
						
			in.setAssocBen(associazione);
			
			res = WSCache.assocBenServer.getAssociazioniBen(in, request);

			if (res.getPInfo() != null)
			{
				if (res.getPInfo().getNumRows() > 0)
				{
					request.setAttribute("listaAssociazioni", res.getListXml());
					request.setAttribute("listaAssociazioni.pageInfo", getPageInfo(res.getPInfo(), rowsPerPage));
				}
				else
				{
					request.setAttribute("listaAssociazioni", null);
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
	
	
	private AssocBen requestToAssociazione(HttpServletRequest request) throws Exception
	{
		AssocBen associazione = new AssocBen();
		String dataA = null;
		String dataDa = null;
		associazione.setCodSocieta(request.getAttribute("codSocieta")!=null?request.getAttribute("codSocieta").toString():"");
        associazione.setCodProvincia(request.getAttribute("codProvincia")!=null?request.getAttribute("codProvincia").toString():"");
     
//        associazione.setCodUtente(request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"");
        String codUtente = request.getAttribute("codUtente")!=null?request.getAttribute("codUtente").toString():"";
        if (codUtente.length()==10)
        	codUtente = codUtente.substring(5);        	
        associazione.setCodUtente(codUtente);
        
/*		String codImpositore = request.getAttribute("codImpositore")!=null?request.getAttribute("codImpositore").toString():"";
//agg
		if (codImpositore.length()==20) 
			codImpositore = codImpositore.substring(10);
*/
		String codBeneficiario = request.getAttribute("codBeneficiario")!=null?request.getAttribute("codBeneficiario").toString():"";
		//agg
		if (codBeneficiario.length()==20) 
			codBeneficiario = codBeneficiario.substring(10);
        
        associazione.setCodBeneficiario(codBeneficiario);

        dataDa = request.getAttribute("annoRifDaS")!=null?request.getAttribute("annoRifDaS").toString():"";
        dataA = request.getAttribute("annoRifAS")!=null?request.getAttribute("annoRifAS").toString():"";
        associazione.setAnnoRifDa(dataDa);
        associazione.setAnnoRifA(dataA);
        if (!dataDa.equals("")&&!dataA.equals("")&&dataDa.compareTo(dataA)>0)
        {       	
        	throw new FaultType(100, "l'intervallo settato per l'anno di riferimento non è corretto");
        }
  
        
        associazione.setDataValidita(getDateByTag(request, "tx_data_validitaS")==null?"":sdf.format(getDateByTag(request, "tx_data_validitaS")));
        request.setAttribute("tx_data_validitaS", getCalendarByDate(request, getDateByTag(request, "tx_data_validitaS")));
        /*if (request.getAttribute("dataValidita_day")!=null&&request.getAttribute("dataValidita_day").toString().length()>0){
        	associazione.setDataValidita(request.getAttribute("dataValidita_day")!=null?getData(request.getAttribute("dataValidita_day").toString(),request.getAttribute("dataValidita_month").toString(),request.getAttribute("dataValidita_year").toString()):"");
        	//reimposto la data validità in request
        	request.setAttribute("dataValidita", getCalendarByDate(request, getDateByTag(request, "dataValidita")));
        }else{
        	if (request.getAttribute("dataValidita")!=null&&request.getAttribute("dataValidita").toString().length()>10){
                 associazione.setDataValidita(request.getAttribute("dataValidita").toString().substring(0,10));
	        	//reimposto la data validità in request
	        	request.setAttribute("dataValidita", getCalendarByDate(request, getDateByTag(request, "dataValidita")));

        	}else{ 
        		associazione.setDataValidita("");
	        	//reimposto la data validità in request
	        	request.setAttribute("dataValidita","");
        	}
        }*/
        
        associazione.setTipoRendicontazione(request.getAttribute("tipoRendicontazione")!=null?request.getAttribute("tipoRendicontazione").toString():"");
        associazione.setStrumRiversamento(request.getAttribute("strumRiversamento")!=null?request.getAttribute("strumRiversamento").toString():"");
        associazione.setMetodoRend(request.getAttribute("metodoRend")!=null?request.getAttribute("metodoRend").toString():"");

        associazione.setOperatorCode((String)request.getAttribute("operatore"));
        
        
        
        return associazione;
        
	}
	
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
			if (getFiredButton(request)==FiredButton.TX_BUTTON_SOCIETA_CHANGED) 
			{
				loadProvinciaXml_DDL(request, session, codiceSocieta, true);
				loadDDLUtente(request, session, codiceSocieta, null, true);
				loadDDLBeneficiarioBoll(request, session, codiceSocieta, null, null, true);
				resetGrid(request);
			}
			else if (getFiredButton(request)==FiredButton.TX_BUTTON_PROVINCIA_CHANGED) 
			{
				loadProvinciaXml_DDL(request, session, codiceSocieta, false);
				loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
				loadDDLBeneficiarioBoll(request, session, codiceSocieta, codiceProvincia, null,true);
				resetGrid(request);
			}
			else if (getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_CHANGED) 
			{
				loadProvinciaXml_DDL(request, session, codiceSocieta, false);
				loadDDLUtente(request, session, codiceSocieta, codiceProvincia,false);			
				loadDDLBeneficiarioBoll(request, session, codiceSocieta, codiceProvincia, codiceUtente, true);
				resetGrid(request);
			}
			else 
			{
				loadProvinciaXml_DDL(request, session, codiceSocieta, true);
				loadDDLUtente(request, session, codiceSocieta, codiceProvincia,true);
				//LoadListaUtentiEntiXml_DDL(request, session, codiceSocieta, codiceProvincia, null, codiceUtente,false);
				loadDDLBeneficiarioBoll(request, session, codiceSocieta, codiceProvincia, codiceUtente, true);
			}
		}

	

}
