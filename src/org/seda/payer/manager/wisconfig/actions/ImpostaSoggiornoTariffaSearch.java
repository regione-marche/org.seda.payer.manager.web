package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.configurazione.actions.BaseConfigurazioneAction;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTariffeCsvRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTariffeCsvResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.Paginazione;
import com.seda.payer.impostasoggiorno.webservice.dati.TariffaImpostaSoggiorno;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoTariffaSearch extends BaseConfigurazioneAction {

	//inizio LP PG1800XX_016
	String templateName = "";
	//fine LP PG1800XX_016
	
	
	public Object service(HttpServletRequest request) throws ActionException 
	{
		super.service(request, "impostaSoggiornoTariffa_search");

		//inizio LP PG1800XX_016
		HttpSession session = request.getSession();
		MAFRequest mafReq = new MAFRequest(request);
		String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		UserBean userBean = (UserBean)session.getAttribute(SignOnKeys.USER_BEAN);
		templateName = userBean.getTemplate(applicationName);
		
		if (templateName.equals("sassari"))
		{
			request.setAttribute("descrizioneComune","SASSARI");
		}
		//fine LP PG1800XX_016

		TariffaImpostaSoggiorno tar= null;
        switch (firedButton)
        {
        	case TX_BUTTON_AGGIUNGI:
        		request.setAttribute("start","A");
        		request.setAttribute("vista","impostaSoggiornoTariffa_search");
        		break;
        	case TX_BUTTON_RESET:
        		resetFormTariffaRic(request);
        		tar = getTariffaRicByRequest(request);
        		ricercaTariffe(tar, request);
    			break;
        	case TX_BUTTON_CERCA:
        		resetGrid(request);
        		tar = getTariffaRicByRequest(request);
        		if (testCampiTariffaImpostaSoggiorno(tar, request))
        			ricercaTariffe(tar, request);
        		break;
        	case TX_BUTTON_DOWNLOAD:
        		tar = getTariffaRicByRequest(request);
        		downloadCsv(tar, request);
        		break;
        	case TX_BUTTON_NULL:
        	case TX_BUTTON_DEFAULT:
        		tar = getTariffaRicByRequest(request);
        		if (testCampiTariffaImpostaSoggiorno(tar, request))
        			ricercaTariffe(tar, request);
        		break;
        }
		
		return null;	
	}

	
	private void ricercaTariffe(TariffaImpostaSoggiorno tar, HttpServletRequest request)
	{

		String order = null;
		int rowsPerPage = 0;
		int pageNumber = 0;

		

		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));
		order = ((String)request.getAttribute("order") == null) ? "" : (String)request.getAttribute("order");

		try 
		{
			RecuperaListaTariffaResponse res = null;
			RecuperaListaTariffaRequest in = new RecuperaListaTariffaRequest();

			Paginazione pag = new Paginazione();
			
			pag.setPageNumber(pageNumber);
			pag.setRowsPerPage(rowsPerPage);
			pag.setOrder(order);
			
			in.setPagina(pag);
						
			in.setTariffa(tar);
			
			res = WSCache.impostaSoggiornoConfigServer.ricercaTariffe(in, request);

			if (res.getPInfo() != null)
			{
				if (res.getPInfo().getNumRows() > 0)
				{
					request.setAttribute("listaTariffe", res.getListXml());
					request.setAttribute("listaTariffe.pageInfo", getPageInfo(res.getPInfo(), rowsPerPage));
				}
				else
				{
					request.setAttribute("listaTariffe", null);
					setFormMessage("search_tariffa_form", Messages.NO_DATA_FOUND.format(), request);					
				}
			}
			else setFormMessage("search_tariffa_form", Messages.NO_DATA_FOUND.format(), request);
		} 
		catch (FaultType e) 
		{
			e.printStackTrace();
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}				

		
	}
	
	
	private void resetFormTariffaRic(HttpServletRequest request)
	{
		resetParametri(request);
		request.setAttribute("descrizioneComune","");
		//inizio LP PG1800XX_016
		if (templateName.equals("sassari"))
		{
			request.setAttribute("descrizioneComune","SASSARI");
		}
		//fine LP PG1800XX_016
		request.setAttribute("descrizioneStrutturaRicettiva","");

		request.setAttribute("tx_data_da", null);
		request.setAttribute("tx_data_a", null);
		
		resetGrid(request);
	}
	
	private TariffaImpostaSoggiorno getTariffaRicByRequest(HttpServletRequest request)
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
	}

	private boolean testCampiTariffaImpostaSoggiorno(TariffaImpostaSoggiorno tar, HttpServletRequest request)
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
			setFormMessage("search_tariffa_form", messaggio, request);
			risultato = false;
		}
		
		return risultato;
	}
	
	
	private PageInfo getPageInfo(com.seda.payer.impostasoggiorno.webservice.dati.PageInfo rpi, int rowsPerPages) 
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
	
	private void downloadCsv(TariffaImpostaSoggiorno tar, HttpServletRequest request)
	{
		String file="";
		try {
			RecuperaTariffeCsvRequest reqCsv = new RecuperaTariffeCsvRequest();
			reqCsv.setTariffa(tar);
			RecuperaTariffeCsvResponse resCsv = WSCache.impostaSoggiornoConfigServer.recuperaTariffeCsv(reqCsv, request);
			if (resCsv != null && resCsv.getResponse() != null && resCsv.getResponse().getRetCode().getValue().equals("00"))
			{
				file = resCsv.getStringaCsv();
				//aggiustamento carattere \r perso nel webservice 
				file = file.replaceAll("\n", "\r\n");
				Calendar cal = Calendar.getInstance();
				String timestamp = GenericsDateNumbers.formatCalendarData(cal, "yyyyMMdd_HHmmss");
				request.setAttribute("filename",timestamp + "_Tariffe.csv");
				request.setAttribute("fileCsv", file);
			}
			else
			{
				setFormMessage("search_tariffa_form", "Si è verificato un errore durante l'esportazione dei dati" , request);
			}
		} catch (FaultType e1) {
			setFormMessage("search_tariffa_form", e1.getMessage() , request);
			e1.printStackTrace();
		} catch (RemoteException e1) {
			setFormMessage("search_tariffa_form", e1.getMessage() , request);
			e1.printStackTrace();
		}
	}


}
