package org.seda.payer.manager.entrate.actions;



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
import com.seda.payer.bancadati.webservice.dati.RicercaAnagraficheRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaAnagraficheResponse;

public class RicercaAnagAction extends BaseEntrateAction {

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

				request.setAttribute("rowsPerPage", request.getParameter("hRowsPerPage"));
				request.setAttribute("pageNumber", request.getParameter("hPageNumber"));
				request.setAttribute("order", request.getParameter("hOrder"));


			case TX_BUTTON_CERCA: 
				if (request.getAttribute("ext")==null)
					request.setAttribute("ext","0");

				if (request.getAttribute("ext")=="" || actExp)
					request.setAttribute("ext",exp);
				
				try {
					ricercaAnagrafiche(request);
					}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaAnagForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaAnagForm", testoErroreColl, request);					
				}
				catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					e.printStackTrace();
					setFormMessage("ricercaAnagForm", "errore ricerca: " + testoErrore, request);
				}
				break;
			}

		
		super.service(request);
        aggiornamentoCombo(request, session);				

		return null;
	}

	
    public void ricercaAnagrafiche(HttpServletRequest request) throws Exception
    {
    	
			RicercaAnagraficheResponse out;
			RicercaAnagraficheRequest in;
			in = prepareRicerca(request);
						
			WSCache.logWriter.logDebug("Pagina interrogazione Anagrafiche Entrate");
			out =  WSCache.entrateBDServer.ricercaAnagrafiche(in, request);

			
			if(out.getPInfo().getNumRows() > 0) 
			{
				request.setAttribute("listaAnag", out.getListXml());

				request.setAttribute("listaAnag.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		
			}
			else
			{
				String messaggio="";
				if (request.getAttribute("messaggioRis")!=null)
					messaggio = request.getAttribute("messaggioRis").toString() + " - ";
				setFormMessage("ricercaAnagForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
			}
		
	
    }

    private void resetReqEDDL(HttpServletRequest request)
    {
		resetParametri(request);
//		request.setAttribute("data_pagamento_da", null);
//		request.setAttribute("data_pagamento_a", null);
		setProfile(request);
		resetDDLSession(request);
    }
    
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
		switch(getFiredButton(request)) 
		{
			   case TX_BUTTON_SOCIETA_CHANGED:
					loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, true);
					loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
					LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
//					loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
					break;
				case TX_BUTTON_UTENTE_CHANGED:
					loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
					loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
					LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
//					loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
					break;
				case TX_BUTTON_IMPOSITORE_CHANGED:
					loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
					loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
					LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
//					loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
					break;
/*					
				case TX_BUTTON_TIPO_SERVIZIO_CHANGED:
					loadTipologiaServizioXml_DDL(request,session, paramCodiceSocieta, false);
					loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
					loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceTipologiaServizio, true);
					LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
					loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);
					break;
*/					
				default:
					switch (getProfiloUtente(userProfile)) {
					case AMMI: 
						paramCodiceUtente = null;
						paramCodiceEnte = null;
						break;
					case AMUT:
						paramCodiceEnte = null;
						break;
					}
					loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
					loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, false);
					LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
//					loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);			
		}		
	}

	
	private RicercaAnagraficheRequest prepareRicerca(HttpServletRequest request)
	{
		PropertiesTree configuration; 
		RicercaAnagraficheRequest ris;

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new RicercaAnagraficheRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		
	 	ris.setCodiceSocieta(paramCodiceSocieta);
		ris.setCodiceUtente(paramCodiceUtente);
		ris.setCodiceEnte(paramCodiceEnte);			
        String impServ = request.getParameter("impostaServ");
        if(impServ != null && impServ.length() > 0){
        	ris.setCodiceSocieta(impServ.substring(0,5));
        	//ris.setTipologiaServizio(impServ.substring(5,8));
        	ris.setImpostaServizio(impServ.substring(8,impServ.length()));
        }
		//ris.setImpostaServizio(request.getParameter("impostaServ"));
		ris.setCodiceFiscale(request.getParameter("codFiscale"));
		ris.setDenominazione(request.getParameter("denominazione"));
		ris.setTipoRic(request.getParameter("tipoRic"));

		return ris;
		
	}

/*	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
		 // Salvo le date che richiedono l'utilizzo di un tipo "Calendar"

		request.setAttribute("data_pagamento_da", getCalendar(request, "data_pagamento_da"));
		request.setAttribute("data_pagamento_a", getCalendar(request, "data_pagamento_a"));
	}
*/

}
