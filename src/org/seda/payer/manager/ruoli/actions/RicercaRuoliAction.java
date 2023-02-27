package org.seda.payer.manager.ruoli.actions;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.bancadati.webservice.srv.FaultType;
import com.seda.payer.bancadati.webservice.dati.Paginazione;
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaEmissioniRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaEmissioniResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaRuoliRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaRuoliResponse;

public class RicercaRuoliAction extends BaseRuoliAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		salvaStato(request);

		super.service(request);

		HttpSession session = request.getSession();
		
		String exp="0";
        boolean actExp = false;

		switch(getFiredButton(request)) 
			{
			case TX_BUTTON_NULL: 
				resetDDLSession(request);
				break;
			case TX_BUTTON_RESET:
				resetReqEDDL(request);			
				break;
			case TX_BUTTON_CERCA_EXP: 
				if (request.getParameter("ext") != null && request.getParameter("ext").equals("0"))
					exp = "1";
				else
					exp = "0";
				actExp = true;
			case TX_BUTTON_CERCA: 
				if (request.getAttribute("ext")==null)
					request.setAttribute("ext","0");

				if (request.getAttribute("ext")=="" || actExp)
					request.setAttribute("ext",exp);

			try {
				String errore = controlloDate(request);
//				ricercaPagamenti(request);
				if (errore!=null)
					setFormMessage("ricercaRuoliForm", "errore: " + errore, request);
				else
					ricercaRuoli(request);
				}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaRuoliForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaRuoliForm", testoErroreColl, request);					
				}
				catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					e.printStackTrace();
					setFormMessage("ricercaRuoliForm", "errore ricerca: " + testoErrore, request);
				}
				break;
			}

		
		super.service(request);
        aggiornamentoCombo(request, session);				

		return null;
	}

    public void ricercaRuoli(HttpServletRequest request) throws Exception
    {
    	
			RicercaRuoliResponse out;
			RicercaRuoliRequest in;
			in = prepareRicerca(request);
						
			WSCache.logWriter.logDebug("Pagina interrogazione di test");
			out =  WSCache.ruoliBDServer.ricercaRuoli(in, request);
			
			if(out.getPInfo().getNumRows() > 0) 
			{
				request.setAttribute("listaRuoli", out.getListXml());

				request.setAttribute("listaRuoli.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		

				request.setAttribute("totImportoCarico", out.getTotCarico());
				request.setAttribute("totImportoDiminuzioneCarico", out.getTotDimensioneCarico().toString());
				request.setAttribute("totImportoRiscosso", out.getTotRiscosso().toString());
				request.setAttribute("totImportoTotaleRimborso", out.getTotRimborso().toString());
				request.setAttribute("totImportoTotaleResiduo", out.getTotResiduo().toString());
				
			}
			else
			{
				String messaggio="";
				if (request.getAttribute("messaggioRis")!=null)
					messaggio = request.getAttribute("messaggioRis").toString() + " - ";
				setFormMessage("ricercaRuoliForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
			}
		
	
    }

    private void resetReqEDDL(HttpServletRequest request)
    {
		resetParametri(request);
		request.setAttribute("data_consegna_da", null);
		request.setAttribute("data_consegna_a", null);
		setProfile(request);
		resetDDLSession(request);
    }
    
    private String getFormatDate(java.util.Calendar data){
    	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    	return dateformat.format(data.getTime());
    } 
    
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
		String tipoServizio = request.getParameter("tx_tipologia_servizio");
		switch(getFiredButton(request)) 
		{
		   case TX_BUTTON_SOCIETA_CHANGED:
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			case TX_BUTTON_UTENTE_CHANGED:
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			case TX_BUTTON_IMPOSITORE_CHANGED:
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			default:
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);			
		
		}		
	}
	
	private RicercaRuoliRequest prepareRicerca(HttpServletRequest request)
	{
		PropertiesTree configuration; 
		RicercaRuoliRequest ris;

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new RicercaRuoliRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi

		ris.setCodiceSocieta(paramCodiceSocieta);
        ris.setCodiceUtente(paramCodiceUtente);
        ris.setCodiceEnte(paramCodiceEnte);

        ris.setAnnoRuolo(request.getParameter("annoRuolo"));
        ris.setNumeroRuolo(request.getParameter("numRuolo"));
		String ufficioImpositore = request.getParameter("ufficioImpositore");
		if(ufficioImpositore != null && !ufficioImpositore.equals("")){
			ris.setTipoUfficio(ufficioImpositore.substring(0,ufficioImpositore.indexOf('-')));
			ris.setCodiceUfficio(ufficioImpositore.substring(ufficioImpositore.indexOf('-')+1));
		}else{
			ris.setTipoUfficio("");
			ris.setCodiceUfficio("");
		}
		
        ris.setDataConsegnaRuoliDa(getData(request.getParameter("data_consegna_da_day"),request.getParameter("data_consegna_da_month"),request.getParameter("data_consegna_da_year")));
		ris.setDataConsegnaRuoliA(getData(request.getParameter("data_consegna_a_day"),request.getParameter("data_consegna_a_month"),request.getParameter("data_consegna_a_year")));

        return ris;
		
	}

	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
		/*
		 * Salvo le date che richiedono l'utilizzo di un tipo "Calendar"
		 */
		request.setAttribute("data_consegna_da", getCalendar(request, "data_consegna_da"));
		request.setAttribute("data_consegna_a", getCalendar(request, "data_consegna_a"));
	}

	private String controlloDate(HttpServletRequest request)
	{
//		String messaggio = null;
		Calendar dataDa = getCalendar(request, "data_consegna_da");
        Calendar dataA = getCalendar(request, "data_consegna_a");
        
        if (dataDa==null||dataA==null)
        	return null;
        if (dataDa.equals(dataA))
        	return null;
        if (!dataDa.before(dataA))
        	return "La data consegna da deve essere antecedente o uguale alla data consegna a";
        /*
        dataDa.add(Calendar.DAY_OF_MONTH, 30);
        if (dataDa.before(dataA))
        	return "Il massimo range di giorni consentito è di 30 giorni";
        */
        return null;
        
	}
	

}
