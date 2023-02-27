package org.seda.payer.manager.ruoli.actions;

import java.rmi.RemoteException;

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
//import com.seda.payer.bancadati.webservice.dati.RecuperaRimborsiRequest;
//import com.seda.payer.bancadati.webservice.dati.RecuperaRimborsiResponse;

public class RicercaAnagraficheAction extends BaseRuoliAction {

	private static final long serialVersionUID = 1L;
/*	
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

				request.setAttribute("rowsPerPage", request.getParameter("hRowsPerPage"));
				request.setAttribute("pageNumber", request.getParameter("hPageNumber"));
				request.setAttribute("order", request.getParameter("hOrder"));
			
			case TX_BUTTON_CERCA: 
				if (request.getAttribute("ext")==null)
					request.setAttribute("ext","0");

				if (request.getAttribute("ext")=="" || actExp)
					request.setAttribute("ext",exp);
				
				try {
						ricercaEccedenze(request);
					}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaEccedenzeForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaEccedenzeForm", testoErroreColl, request);					
				}
				catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					e.printStackTrace();
					setFormMessage("ricercaEccedenzeForm", "errore ricerca: " + testoErrore, request);
				}
				break;
			}

		
		super.service(request);
        aggiornamentoCombo(request, session);				

		return null;
	}

    public void ricercaEccedenze(HttpServletRequest request) throws Exception
    {
    	
			RecuperaRimborsiResponse out;
			RecuperaRimborsiRequest in;
			in = prepareRicerca(request);
						
			WSCache.logWriter.logDebug("Pagina interrogazione di test");
			out =  WSCache.ruoliBDServer.recuperaRimborsi(in);

			
			if(out.getPInfo().getNumRows() > 0) 
			{
				request.setAttribute("listaEccedenze", out.getListXml());

				request.setAttribute("listaEccedenze.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		

				request.setAttribute("totImportoRimborsiPos", out.getTotImportoRimborsiPos().toString());
				request.setAttribute("totImportoRimborsiNeg", out.getTotImportoRimborsiNeg().toString());
				request.setAttribute("totImportoAssegniPos", out.getTotImportoAssegniPos().toString());
				request.setAttribute("totImportoAssegniNeg", out.getTotImportoAssegniNeg().toString());
				request.setAttribute("totImportoBonificiPos", out.getTotImportoBonificiPos().toString());
				request.setAttribute("totImportoBonificiNeg", out.getTotImportoBonificiNeg().toString());
			}
			else
			{
				String messaggio="";
				if (request.getAttribute("messaggioRis")!=null)
					messaggio = request.getAttribute("messaggioRis").toString() + " - ";
				setFormMessage("ricercaEccedenzeForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
			}
		
	
    }

    private void resetReqEDDL(HttpServletRequest request)
    {
		resetParametri(request);
		request.setAttribute("data_ricezione_da", null);
		request.setAttribute("data_ricezione_a", null);
		request.setAttribute("data_ordine_da", null);
		request.setAttribute("data_ordine_a", null);
		setProfile(request);
		resetDDLSession(request);
    }
    
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
		switch(getFiredButton(request)) 
		{
  		    case TX_BUTTON_SOCIETA_CHANGED:
				loadProvinciaXml_DDL(request, session, paramCodiceSocieta, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				break;
			case TX_BUTTON_PROVINCIA_CHANGED:
				loadProvinciaXml_DDL(request, session, paramCodiceSocieta, false);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				break;
			default:
				loadProvinciaXml_DDL(request, session, paramCodiceSocieta, false);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);				
		}		
	}
	
	private RecuperaRimborsiRequest prepareRicerca(HttpServletRequest request)
	{
		PropertiesTree configuration; 
		RecuperaRimborsiRequest ris;

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new RecuperaRimborsiRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		
		ris.setCodiceSocieta(paramCodiceSocieta);
        ris.setSiglaProvincia(request.getParameter("tx_provincia"));
        ris.setCodiceUtente(paramCodiceUtente);
        ris.setCodiceEnte(paramCodiceEnte);

        ris.setCodiceFiscale(request.getParameter("codFiscale"));
        ris.setNomeDoc(request.getParameter("documento"));

        ris.setDataRicezioneDa(getData(request.getParameter("data_ricezione_da_day"),request.getParameter("data_ricezione_da_month"),request.getParameter("data_ricezione_da_year")));
		ris.setDataRicezioneA(getData(request.getParameter("data_ricezione_a_day"),request.getParameter("data_ricezione_a_month"),request.getParameter("data_ricezione_a_year")));

		ris.setEsitoRimborso(request.getParameter("esitoRim"));
        ris.setStrumento(request.getParameter("strumento"));

        ris.setDataOrdineDa(getData(request.getParameter("data_ordine_da_day"),request.getParameter("data_ordine_da_month"),request.getParameter("data_ordine_da_year")));
		ris.setDataOrdineA(getData(request.getParameter("data_ordine_a_day"),request.getParameter("data_ordine_a_month"),request.getParameter("data_ordine_a_year")));

		return ris;
		
	}

	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);

		request.setAttribute("data_ricezione_da", getCalendar(request, "data_ricezione_da"));
		request.setAttribute("data_ricezione_a", getCalendar(request, "data_ricezione_a"));
		request.setAttribute("data_ordine_da", getCalendar(request, "data_ordine_da"));
		request.setAttribute("data_ordine_a", getCalendar(request, "data_ordine_a"));
	}

*/

}
