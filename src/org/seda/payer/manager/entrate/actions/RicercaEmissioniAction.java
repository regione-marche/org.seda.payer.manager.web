package org.seda.payer.manager.entrate.actions;

import java.rmi.RemoteException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.bancadati.webservice.dati.Paginazione;
import com.seda.payer.bancadati.webservice.dati.RicercaEmissioniRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaEmissioniResponse;
import com.seda.payer.bancadati.webservice.srv.FaultType;

public class RicercaEmissioniAction extends BaseEntrateAction {

	private static final long serialVersionUID = 1L;
	
	public Object service(HttpServletRequest request) throws ActionException {
		
		salvaStato(request);

		super.service(request);

		HttpSession session = request.getSession();
		
		String exp="0";
        boolean actExp = false;

		switch(getFiredButton(request)) 
			{
			case TX_BUTTON_NULL: {
				resetDDLSession(request);
				resetFiltri(request); //TK2020121188000041
				}; break;
			case TX_BUTTON_RESET: {
				resetReqEDDL(request);		
				resetFiltri(request); //TK2020121188000041
				}; break;
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
						salvaFiltriRicerca(request); //TK2020121188000041
						recuperaFiltriRicerca(request); //TK2020121188000041
						ricercaEmissioni(request,session);
						
					}
				catch (FaultType fte) 
				{
					WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
					fte.printStackTrace();
					setFormMessage("ricercaDocumentiForm", "errore: " + decodeMessaggio(fte), request);
				}
				catch (RemoteException af)
				{
					WSCache.logWriter.logError("errore: " + af.getMessage(),af);
					af.printStackTrace();
					setFormMessage("ricercaDocumentiForm", testoErroreColl, request);					
				}
				catch (Exception e) {
					WSCache.logWriter.logError("errore: " + e.getMessage(),e);
					e.printStackTrace();
					setFormMessage("ricercaDocumentiForm", "errore ricerca: " + testoErrore, request);
				}
				break;
				
				//TK2020121188000041
				case TX_BUTTON_TIPO_SERVIZIO_CHANGED: {
			    	salvaFiltriRicerca(request);
			    	recuperaFiltriRicerca(request);
				}
			    //FINE TK2020121188000041
			
			}

		
		//super.service(request);
        aggiornamentoCombo(request, session);				
        
		return null;
	}

    public void ricercaEmissioni(HttpServletRequest request, HttpSession session) throws Exception
    {
    	
			RicercaEmissioniResponse out;
			RicercaEmissioniRequest in;
			in = prepareRicerca(request,session);
						
			WSCache.logWriter.logDebug("Pagina interrogazione di test");
			out =  WSCache.entrateBDServer.ricercaEmissioni(in, request);
			
			if(out.getPInfo().getNumRows() > 0) 
			{
				
				request.setAttribute("listaEmissioni", out.getListXml());
				request.setAttribute("listaEmissioni.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));		
				request.setAttribute("totImportoCarico", out.getTotCarico());
				request.setAttribute("totImportoRendiconto", out.getTotRendicontato().toString());
				request.setAttribute("totImportoDiminuzioneCarico", out.getTotDimensioneCarico().toString());
				request.setAttribute("totImportoRiscosso", out.getTotRiscosso().toString());
				request.setAttribute("totImportoTotaleRimborso", out.getTotRimborso().toString());
				request.setAttribute("totImportoTotaleResiduo", out.getTotResiduo().toString());
				request.setAttribute("totImportoResiduoScaduto", out.getTotResiduoScaduto().toString());
				
				//request.setAttribute("dataUltimoAgg", out.getDataUltimoAgg()== null?"":out.getDataUltimoAgg().getTime());				
			}
			else
			{
				String messaggio="";
				if (request.getAttribute("messaggioRis")!=null)
					messaggio = request.getAttribute("messaggioRis").toString() + " - ";
				setFormMessage("ricercaEmissioniForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
			}
		
	
    }

    private void resetReqEDDL(HttpServletRequest request)
    {
		resetParametri(request);
		/*request.setAttribute("data_ricezione_da", null);
		request.setAttribute("data_ricezione_a", null);
		request.setAttribute("data_ordine_da", null);
		request.setAttribute("data_ordine_a", null);*/	
		setProfile(request);
		resetDDLSession(request);
    }
    /*
    private String getFormatDate(java.util.Calendar data){
    	SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    	return dateformat.format(data.getTime());
    }*/ 
    
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
		//String tipoServizio = request.getParameter("tx_tipologia_servizio");
		switch(getFiredButton(request)) 
		{
		   case TX_BUTTON_SOCIETA_CHANGED:
			    //loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
			    loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, true);
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, true);
				loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			case TX_BUTTON_UTENTE_CHANGED:
				//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
				loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, true);
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			case TX_BUTTON_IMPOSITORE_CHANGED:
				//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
				loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, true);
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			case TX_BUTTON_TIPO_SERVIZIO_CHANGED:
				//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,false);
				loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, false);
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);
				break;
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
				//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,false);
				loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, false);
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, false);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);			
	
		
		/*
		
			case TX_BUTTON_SOCIETA_CHANGED:
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, true);
  		    	//loadProvinciaXml_DDL(request, session, paramCodiceSocieta, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				loadListaImpostaServizio(request, session,paramCodiceSocieta,tipoServizio,true);
				loadTipologiaServizioXml_DDL(request, session, paramCodiceSocieta, false);
				break;
			case TX_BUTTON_UTENTE_CHANGED:
				loadDDLUtente(request, session, paramCodiceSocieta, null, false);
				//paramCodiceUtente = request.getParameter("tx_utent");
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);
				break;
			default:
				loadListaImpostaServizio(request, session,paramCodiceSocieta,tipoServizio,false);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);
				loadDDLUtente(request, session, paramCodiceSocieta, null, false);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
				loadTipologiaServizioXml_DDL(request, session, paramCodiceSocieta, false);
		*/
		
		}		
	}
	
	private RicercaEmissioniRequest prepareRicerca(HttpServletRequest request, HttpSession session)
	{
		PropertiesTree configuration; 
		RicercaEmissioniRequest ris;

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new RicercaEmissioniRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		
		ris.setCodiceSocieta(paramCodiceSocieta);
		//ris.setSiglaProvincia(request.getParameter("tx_provincia"));
        ris.setCodiceUtente(paramCodiceUtente);
        ris.setCodiceEnte(paramCodiceEnte);
       
        //TKTK2020121188000041
        String impServ = "";
        if(request.getParameter("impostaServ")!=null) {
        	impServ = request.getParameter("impostaServ");
        } else if (session.getAttribute("tx_imposta_servizio_value")!=null) {
        	impServ = session.getAttribute("tx_imposta_servizio_value").toString();
        }
        //FINE TKTK2020121188000041
        
        if(impServ != null && impServ.length() > 0){
        	ris.setCodiceSocieta(impServ.substring(0,5));
        	ris.setTipologiaServizio(impServ.substring(5,8));
        	ris.setImpostaServizio(impServ.substring(8,impServ.length()));
        } 
        
        ris.setAnnoEmissione(request.getParameter("annoEmissione"));

        ris.setNumeroEmissione(request.getParameter("numEmissione"));
		String ufficioImpositore = request.getParameter("ufficioImpositore");
		if(ufficioImpositore != null && !ufficioImpositore.equals("")){
			ris.setTipoUfficio(ufficioImpositore.substring(0,ufficioImpositore.indexOf('-')));
			ris.setCodiceUfficio(ufficioImpositore.substring(ufficioImpositore.indexOf('-')+1));
		}else{
			ris.setTipoUfficio("");
			ris.setCodiceUfficio("");
		}

		//ris.setTipologiaServizio(request.getParameter("tx_tipologia_servizio"));
		//Giulia 8/05/2014 INIZIO
		
		if(getTipologiaServizio(request,session)!=null && !getTipologiaServizio(request,session).equals("") && userBean.getProfile().equals("AMMI")){
		ris.setTipologiaServizio(getTipologiaServizio(request,session).substring(0, 3));
		if((getTipologiaServizio(request,session)).length()>0){
			ris.setCodiceSocieta(getTipologiaServizio(request,session).substring(4));
		}
		}
		else {
			if (getTipologiaServizio(request,session)!=null && !getTipologiaServizio(request,session).equals("") && getTipologiaServizio(request,session).indexOf("_", 0) != -1) {
				String serv = getTipologiaServizio(request,session);
				serv= serv.replace("'", "");
				ris.setTipologiaServizio(serv.substring(0, 3));
			}
			else
				ris.setTipologiaServizio(getTipologiaServizio(request,session));
		}
		//Giulia 29/04/2014 FINE
		//ris.setTipologiaServizio(getTipologiaServizio(request, session));
		
        return ris;
		
	}

	
	protected void salvaStato(HttpServletRequest request)
	{
		super.salvaStato(request);
		/*
		 * Salvo le date che richiedono l'utilizzo di un tipo "Calendar"
		 */
		request.setAttribute("data_ricezione_da", getCalendar(request, "data_ricezione_da"));
		request.setAttribute("data_ricezione_a", getCalendar(request, "data_ricezione_a"));
		request.setAttribute("data_ordine_da", getCalendar(request, "data_ordine_da"));
		request.setAttribute("data_ordine_a", getCalendar(request, "data_ordine_a"));
		
	}
	
	//TK2020121188000041
	
	private void salvaFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("tx_societa", request.getAttribute("tx_societa")!=null?request.getAttribute("tx_societa"):"");
		session.setAttribute("tx_utente", request.getAttribute("tx_utente")!=null?request.getAttribute("tx_utente"):"");
		session.setAttribute("tx_UtenteEnte", request.getAttribute("tx_UtenteEnte")!=null?request.getAttribute("tx_UtenteEnte"):"");
		session.setAttribute("annoEmissione", request.getAttribute("annoEmissione")!=null?request.getAttribute("annoEmissione"):"");
		session.setAttribute("numEmissione", request.getAttribute("numEmissione")!=null?request.getAttribute("numEmissione"):"");
		if(request.getParameter("impostaServVal")!=null) {
			session.setAttribute("tx_imposta_servizio_value", request.getParameter("impostaServVal"));
		}
		if(request.getParameter("tipServVal")!=null) {
			session.setAttribute("tx_tipologia_servizio_value", request.getParameter("tipServVal"));
		}
	}
	
	private void recuperaFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		request.setAttribute("tx_societa", session.getAttribute("tx_societa")!=null?session.getAttribute("tx_societa").toString():"");
		request.setAttribute("tx_utente", session.getAttribute("tx_utente")!=null?session.getAttribute("tx_utente").toString():"");
		request.setAttribute("tx_UtenteEnte", session.getAttribute("tx_UtenteEnte")!=null?session.getAttribute("tx_UtenteEnte").toString():"");
		request.setAttribute("annoEmissione", session.getAttribute("annoEmissione")!=null?session.getAttribute("annoEmissione").toString():"");
		request.setAttribute("numEmissione", session.getAttribute("numEmissione")!=null?session.getAttribute("numEmissione").toString():"");
		request.setAttribute("tx_tipologia_servizio_value", session.getAttribute("tx_tipologia_servizio_value")!=null?session.getAttribute("tx_tipologia_servizio_value"):"");
		request.setAttribute("tx_imposta_servizio_value", session.getAttribute("tx_imposta_servizio_value")!=null?session.getAttribute("tx_imposta_servizio_value"):"");
	}
	
	private void resetFiltri(HttpServletRequest request) {
		HttpSession session = request.getSession();
		request.setAttribute("tx_societa", "");
		request.setAttribute("tx_utente", "");
		request.setAttribute("tx_UtenteEnte", "");
		request.setAttribute("annoEmissione", "");
		request.setAttribute("numEmissione", "");
		request.setAttribute("tx_imposta_servizio_value", "");
		request.setAttribute("tx_tipologia_servizio_value", "");
		session.setAttribute("tx_societa", "");
		session.setAttribute("tx_utente", "");
		session.setAttribute("tx_UtenteEnte", "");
		session.setAttribute("annoEmissione", "");
		session.setAttribute("numEmissione", "");
		session.setAttribute("tx_imposta_servizio_value", "");
		session.setAttribute("tx_tipologia_servizio_value", "");
	}
	
	//FINE TK2020121188000041



}
