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
import com.seda.payer.bancadati.webservice.dati.RicercaPartiteRuoliRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaPartiteRuoliResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaRuoliRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaRuoliResponse;

public class RicercaPartiteRuoliAction extends BaseRuoliAction {

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
			/*
			case TX_BUTTON_CERCA_EXP: 
				if (request.getParameter("ext") != null && request.getParameter("ext").equals("0"))
					exp = "1";
				else
					exp = "0";
				actExp = true;
			*/
			case TX_BUTTON_CERCA: 
			try {
					DettaglioRuoloAction dettaglioRuolo = new DettaglioRuoloAction();
					dettaglioRuolo.ricercaDettaglioXmlRuolo(request);
					ricercaPartite(request);
				}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaPartiteRuoliForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaPartiteRuoliForm", testoErroreColl, request);					
				}
				catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					e.printStackTrace();
					setFormMessage("ricercaPartiteRuoliForm", "errore ricerca: " + testoErrore, request);
				}
				break;
			}
		
		super.service(request);

		return null;
	}

    public void ricercaPartite(HttpServletRequest request) throws Exception
    {
    	
			RicercaPartiteRuoliResponse out;
			RicercaPartiteRuoliRequest in;
			in = prepareRicerca(request);
						
			WSCache.logWriter.logDebug("Pagina interrogazione di test");
			out =  WSCache.ruoliBDServer.ricercaPartiteRuoli(in, request);
			
			if(out.getPInfo().getNumRows() > 0) 
			{
				request.setAttribute("listaPartite", out.getListXml());

				request.setAttribute("listaPartite.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		

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
				setFormMessage("ricercaPartiteRuoliForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
			}
		
	
    }

	
	private RicercaPartiteRuoliRequest prepareRicerca(HttpServletRequest request)
	{
		PropertiesTree configuration; 
		RicercaPartiteRuoliRequest ris;

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new RicercaPartiteRuoliRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		//&tx_button_cerca=OK&chiaveCodSoc={1}&chiaveCodUte={2}&chiaveCodEnte={18}&chiaveAnnoRuo={4}&chiaveNumRuo={5}&chiaveFlusso={15}&chiaveConc={16}&chiaveTomb={17}&rRowsPerPage=${param.rowsPerPage}&rPageNumber=${param.pageNumber}&rOrder=${param.order}
		
		ris.setCodiceSocieta(request.getParameter("chiaveCodSoc"));
        ris.setCodiceUtente(request.getParameter("chiaveCodUte"));
        ris.setCodiceEnte(request.getParameter("chiaveCodEnte"));

        ris.setAnnoRuolo(request.getParameter("chiaveAnnoRuo"));
        ris.setNumeroRuolo(request.getParameter("chiaveNumRuo"));
        
        ris.setCodiceTombRuolo(request.getParameter("chiaveTomb"));
        ris.setProgrFlussoRuolo(request.getParameter("chiaveFlusso"));
        ris.setCodiceConcessione(request.getParameter("chiaveConc"));

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
        dataDa.add(Calendar.DAY_OF_MONTH, 30);
        if (dataDa.before(dataA))
        	return "Il massimo range di giorni consentito è di 30 giorni";
        return null;
        
	}
	

}
