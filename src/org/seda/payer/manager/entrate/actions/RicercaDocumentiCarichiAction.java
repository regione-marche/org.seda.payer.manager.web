package org.seda.payer.manager.entrate.actions;

import java.rmi.RemoteException;
import java.text.SimpleDateFormat;

import org.seda.payer.manager.entrate.actions.GestioneDocumentiCaricoAction.Screen;
import org.seda.payer.manager.entrate.actions.util.EntrateUtils;
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

/**
 * Nota: Luciano 2017-09-19
 * Questa funzione di semplice ricerca penso dovrebbe essere sostituita dalla
 * nuova (ago 2017) e piu` completa funzione di  {@link GestioneDocumentiCaricoAction}.
 * 
 * In fase di sviluppo utilizzo un forward per:
 *  testare la nuova {@link GestioneDocumentiCaricoAction} pur mantenendo funzionante ed intatta 
 *  la presente classe.
 *  
 * Successivamente si potra` decidere se cambiare il puntataore nel menu` in modo da puntare 
 * alla "gestione" piuttosto che "ricerca"... oppure se mantenerle entrambe, oppure se sostituire la
 * vecchia "ricerca" con la nuova "getione". Chiedere istruzioni a chi conosce un migliore
 * visione d'insieme delle logiche di sviluppo/rilascio. 
 *
 */
public class RicercaDocumentiCarichiAction extends BaseEntrateAction {

	private static final long serialVersionUID = 1L;
	
  public Object service(HttpServletRequest request) throws ActionException {
    salvaStato(request);
    super.service(request);
    
    HttpSession session = request.getSession();
    String exp = "0";
    boolean actExp = false;

    String templateCurrentApplication = EntrateUtils.getTemplateCurrentApplication(request);
    boolean aosta = "aosta".equals(templateCurrentApplication);
    boolean regmarche = "regmarche".equals(templateCurrentApplication);
    if(aosta || regmarche){
       request.setAttribute("screen","gestione");
      return null;
    }
    else {
      request.setAttribute("screen","ricerca");
    }
    
    
    switch (getFiredButton(request)) {
    
      case TX_BUTTON_NULL:
        resetDDLSession(request);
        break;
      case TX_BUTTON_RESET:
        resetReqEDDL(request);
        break;
      case TX_BUTTON_CERCA_EXP:
        if (request.getParameter("r2RowsPerPage") != null
            && request.getParameter("r2RowsPerPage").length() > 0) {
          request.setAttribute("rowsPerPage", request.getParameter("r2RowsPerPage"));
          request.setAttribute("pageNumber", request.getParameter("r2PageNumber"));
          request.setAttribute("order", request.getParameter("r2Order"));
          request.setAttribute("ext", request.getParameter("r2Ext"));
        }

        if (request.getAttribute("ext") != null && request.getAttribute("ext").equals("0"))
          exp = "1";
        else
          exp = "0";
        actExp = true;


      case TX_BUTTON_CERCA:
        if (request.getAttribute("ext") == null)
          request.setAttribute("ext", "0");

        if (request.getAttribute("ext") == "" || actExp)
          request.setAttribute("ext", exp);

        /*
        if(request.getParameter("r2RowsPerPage") != null && request.getParameter("r2RowsPerPage").length() >0 ){
        	request.setAttribute("rowsPerPage", request.getParameter("r2RowsPerPage"));
        	request.setAttribute("pageNumber", request.getParameter("r2PageNumber"));
        	request.setAttribute("order", request.getParameter("r2Order"));
        }
        */

        try {
          ricercaDocumenti(request, session);
        } catch (FaultType fte) {
          WSCache.logWriter.logError("errore: " + fte.getMessage1(), fte);
          fte.printStackTrace();
          setFormMessage("ricercaDocumentiForm", "errore: " + decodeMessaggio(fte), request);
        } catch (RemoteException af) {
          WSCache.logWriter.logError("errore: " + af.getMessage(), af);
          af.printStackTrace();
          setFormMessage("ricercaDocumentiForm", testoErroreColl, request);
        } catch (Exception e) {
          WSCache.logWriter.logError("errore: " + e.getMessage(), e);
          e.printStackTrace();
          setFormMessage("ricercaDocumentiForm", "errore ricerca: " + testoErrore, request);
        }
        break;
    }

    super.service(request);
    aggiornamentoCombo(request, session);

    return null;
  }

  public void ricercaDocumenti(HttpServletRequest request, HttpSession session) throws Exception {

    RecuperaDocumentiResponse out;
    RecuperaDocumentiRequest in;
    in = prepareRicerca(request, session);

    WSCache.logWriter.logDebug("Pagina interrogazione di test");
    out = WSCache.entrateBDServer.ricercaDocumenti(in, request);

    if (out.getPInfo().getNumRows() > 0) {
      request.setAttribute("listaDocumenti", out.getListXml());

      request.setAttribute("listaDocumenti.pageInfo", getPageInfo(out.getPInfo(), in.getPagina()
          .getRowsPerPage()));

      request.setAttribute("totImportoCarico", out.getTotCarico());
      request.setAttribute("totImportoRendiconto", out.getTotRendicontato().toString());
      request.setAttribute("totImportoFinCarico", out.getTotFinCarico().toString());
      request.setAttribute("totImportoDiminuzioneCarico", out.getTotDimensioneCarico().toString());
      request.setAttribute("totImportoRiscosso", out.getTotRiscosso().toString());
      request.setAttribute("totImportoTotaleRimborso", out.getTotRimborso().toString());
      request.setAttribute("totImportoTotaleResiduo", out.getTotResiduo().toString());
      request.setAttribute("totImportoResiduoScaduto", out.getTotResScaduto().toString());

      request.setAttribute("percRendicontatoCarico", out.getPercRendicontatoCarico().toString());
      request.setAttribute("percRensiduoCarico", out.getPercResiduoCarico().toString());
      request.setAttribute("percRimborsoCarico", out.getPercRimborsoCarico().toString());
      request.setAttribute("percRiscossoCarico", out.getPercRiscossoCarico().toString());
      request.setAttribute("percSgravatoCarico", out.getPercSgravatoCarico().toString());
      request.setAttribute("percResiduoScadCarico", out.getPercResiduoScadutoCarico().toString());

      //request.setAttribute("dataUltimoAgg", out.getDataUltimoAgg()== null?"":out.getDataUltimoAgg().getTime());

      request.setAttribute("dataUltimoAgg", out.getDataUltimoAgg() == null ? "" : getFormatDate(out
          .getDataUltimoAgg()));
    } else {
      String messaggio = "";
      if (request.getAttribute("messaggioRis") != null)
        messaggio = request.getAttribute("messaggioRis").toString() + " - ";
      setFormMessage("ricercaDocumentiForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
    }
  }

  private void resetReqEDDL(HttpServletRequest request) {
    resetParametri(request);
    /*request.setAttribute("data_ricezione_da", null);
    request.setAttribute("data_ricezione_a", null);
    request.setAttribute("data_ordine_da", null);
    request.setAttribute("data_ordine_a", null);*/
    setProfile(request);
    resetDDLSession(request);
  }

  private String getFormatDate(java.util.Calendar data) {
    SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    return dateformat.format(data.getTime());
  }
    
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
	
		switch(getFiredButton(request)) 
		{
		   case TX_BUTTON_SOCIETA_CHANGED:
				//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
			    loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
			    loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, true);
				loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			case TX_BUTTON_UTENTE_CHANGED:
				//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
				loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, true);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			case TX_BUTTON_IMPOSITORE_CHANGED:
				//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
				loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,true);
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, true);
				break;
			case TX_BUTTON_TIPO_SERVIZIO_CHANGED:
				//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,false);
				loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,false);
				loadDDLUtente(request, session, paramCodiceSocieta, siglaProvincia, false);
				loadListaImpostaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte, paramCodiceTipologiaServizio, true);
				LoadListaUtentiEntiXml_DDL(request, session, paramCodiceSocieta, siglaProvincia, paramCodiceEnte, paramCodiceUtente, false);
				loadListaUffImpositore(request, session, paramCodiceSocieta, paramCodiceEnte, paramCodiceUtente, false);
				break;
			default:
				//loadTipologiaServizioXml_DDL_2(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,false);
				loadListaTipologiaServizio(request, session, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,false);
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
	
	private RecuperaDocumentiRequest prepareRicerca(HttpServletRequest request, HttpSession session)
	{
		PropertiesTree configuration; 
		RecuperaDocumentiRequest ris;

		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

//      if (request.getParameter("rowsPerPage")!=null)	
	      if (request.getAttribute("rowsPerPage")!=null&&!((String)request.getAttribute("rowsPerPage")).equals("")&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)	
	      {
	      	rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
	      }
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

		ris = new RecuperaDocumentiRequest();
		ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));
		
		//set campi
		
		ris.setCodiceSocieta(paramCodiceSocieta);
		//ris.setSiglaProvincia(request.getParameter("tx_provincia"));
        ris.setCodiceUtente(paramCodiceUtente);
        ris.setCodiceEnte(paramCodiceEnte);

        ris.setCodiceFiscale(request.getParameter("codFiscale"));
        String impServ = request.getParameter("impostaServ");
        if(impServ != null && impServ.length() > 0){
        	ris.setCodiceSocieta(impServ.substring(0,5));
        	ris.setTipologiaServizio(impServ.substring(5,8));
        	ris.setImpostaServizio(impServ.substring(8,impServ.length()));
        }

        //ris.setImpostaServizio(request.getParameter("impostaServ"));
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
			ris.setCodiceSocieta(getTipologiaServizio(request,session).substring(4,9));
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
		
		//ris.setTipologiaServizio(getTipologiaServizio(request, session));
		//Giulia 8/05/2014 FINE
        ris.setStatoDocumento(request.getParameter("stato_documento"));

        ris.setStatoSospensione(request.getParameter("stato_sospensione"));
        ris.setNumeroDocumento(request.getParameter("numDocumento"));
        ris.setStatoProcedure(request.getParameter("stato_procedure"));
		
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



}
