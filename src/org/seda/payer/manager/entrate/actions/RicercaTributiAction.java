package org.seda.payer.manager.entrate.actions;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.entrate.actions.util.EntrateUtils;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DiscaricoEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DiscaricoEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Documento;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Tributo;
//import com.google.gson.Gson;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.bancadati.webservice.dati.Paginazione;
import com.seda.payer.bancadati.webservice.dati.RicercaTributiRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaTributiResponse;
import com.seda.payer.bancadati.webservice.srv.FaultType;

/**
 * Ricerca tributi e discarico.
 */
public class RicercaTributiAction extends BaseEntrateAction {

  private static final long serialVersionUID = 1L;
  //costanti pagina chiamante per INDIETRO
  private final String RICERCA_DOCUMENTI = "ricercaDocumenti";
  private int rowsPerPage = 5;
  private int pageNumber = 1;
  private String order = "";
  static final DecimalFormat df = new DecimalFormat("0.00");
  private ArrayList<ArchivioCarichiTributoDiscarico> arrTribForm = new ArrayList<ArchivioCarichiTributoDiscarico>();
  private ArrayList<Tributo> tribWS = new ArrayList<Tributo>();

  public Object service(HttpServletRequest request) throws ActionException {

    salvaStato(request);

    super.service(request);
    switch (getFiredButton(request)) {
      case TX_BUTTON_CERCA:
        /*
        String chiamante = request.getParameter("pagPrec");
        if(chiamante == null )
        	//errore
        if(chiamante.equals(RICERCA_DOCUMENTI))	{
        	request.setAttribute("rowsPerPage", request.getParameter("r2RowsPerPage"));
        	request.setAttribute("pageNumber", request.getParameter("r2PageNumber"));
        	request.setAttribute("order", request.getParameter("r2Order"));
        }
        */
        break;
      case TX_BUTTON_CERCA_EXP:
        try {
/*
				DettaglioDocumentoAction dettAction = new DettaglioDocumentoAction();
				
				dettAction.ricercaDettaglioDocumento(request);
*/
          //CHIAMO LA LISTA DEI TRIBUTI
          ricercaTributi(request);
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
          //setErrorMessage(request, e.getMessage());
          setFormMessage("ricercaDocumentiForm", "errore: " + testoErrore, request);
        }
        break;

      case TX_BUTTON_SALVA:
      case TX_BUTTON_DISCARICO:
  
        Tributo trib = null;
        loadTribToJsp(request);            

        request.setAttribute("salvaDisabled",false);
        request.setAttribute("impDiscaricoDisabledAll",false);
       
        String impDiscarico = "";
        boolean aggiorna = true;
        try {
        
            if (getFiredButton(request).equals(FiredButton.TX_BUTTON_SALVA)){
                for (int i=0;i<arrTribForm.size();i++ ){
                	if (request.getParameter("trib_"+i+"_impDiscarico")==null){
                		impDiscarico="0,00";
                	} else {
                		impDiscarico=request.getParameter("trib_"+i+"_impDiscarico").trim().equals("")?"0,00":request.getParameter("trib_"+i+"_impDiscarico");
                	}
                  arrTribForm.get(i).setImpDiscarico(impDiscarico);
                  if (convStringToBD(impDiscarico).compareTo(convStringToBD(arrTribForm.get(i).getImpResiduo()))>0){
//                      setErrorMessage("Errore, Importo Discarico maggiore del residuo");
                      setFormMessage("discaricoForm", "Errore, Importo Discarico maggiore del residuo", request);
                      aggiorna=false;
                  }
    
               // carica la lista tributi per web service---------------------------------------
                  trib = new Tributo();
                  trib.setCodiceTributo(arrTribForm.get(i).getCodiceTributo());
                  trib.setAnnoTributo(arrTribForm.get(i).getAnnoTributo());
                  trib.setProgressivoTributo(arrTribForm.get(i).getProgressivoTributo());
                  trib.setImpTributo(convStringToBD(arrTribForm.get(i).getImpDiscarico()).multiply(new BigDecimal("100"))); 
                  tribWS.add(trib);
                  
                }
                for (int i=0;i<arrTribForm.size()&&aggiorna;i++ ){
                    arrTribForm.get(i).setImpDiscaricoDisabled(true);
                }
    
                if (aggiorna && callWsSaveDiscarico(request,tribWS)) {
                  loadTribToJsp(request);       
                  request.setAttribute("salvaDisabled",true);
                  request.setAttribute("impDiscaricoDisabledAll",true);
                }
            }
        } catch (ParseException e) {
          setMessage("Errore generico in WS Discarico ...");
          throw new RuntimeException(e);
        } 

//        request.setAttribute("listaTributiDiscarico", editDocumento.listaTributi);
        request.setAttribute("listaTributiDiscarico", arrTribForm);
        request.setAttribute("message", getMessage());
        request.setAttribute("errorMessage", getErrorMessage());
        break;
    }
    return null;
  }

  public BigDecimal convStringToBD(String importo) throws ParseException {

    df.setParseBigDecimal(true);
    return (BigDecimal) df.parse(importo);
    
  }

  public void loadTribToJsp(HttpServletRequest request) {

    ArchivioCarichiTributoDiscarico tribJsp = null;
	//inizio LP PG21XX04 Leak
	WebRowSet wrs = null;
	//fine LP PG21XX04 Leak
    try {
        new RicercaDocumentiCarichiAction().ricercaDocumenti(request, request.getSession());
        ricercaTributi(request);
        String lst = (String) request.getAttribute("listaTributi");
    	//inizio LP PG21XX04 Leak
        //WebRowSet wrs = Convert.stringToWebRowSet(lst);
        wrs = Convert.stringToWebRowSet(lst);
    	//fine LP PG21XX04 Leak
//        ResultSetMetaData meta = wrs.getMetaData();
        int i=0;
        arrTribForm.clear();
        while (wrs.next()) {
          i++;
          tribJsp = new ArchivioCarichiTributoDiscarico();
          tribJsp.setProgressivoTributo(i); 
          tribJsp.setCodiceTributo(wrs.getString(2));
          tribJsp.setAnnoTributo(wrs.getString(3));
          tribJsp.setImpTributo(df.format(wrs.getBigDecimal(4).doubleValue()));
          tribJsp.setImpPagatoCompresiSgravi(df.format(wrs.getBigDecimal(5).doubleValue()));
          BigDecimal diff = wrs.getBigDecimal(4).subtract(wrs.getBigDecimal(5));
          tribJsp.setImpResiduo(df.format(diff.doubleValue()));
          if (diff.doubleValue() == 0.00) {
            tribJsp.setImpDiscaricoDisabled(true); 
          }
          tribJsp.setImpDiscarico(tribJsp.getImpResiduo());
          //inizio LP PG22XX05
          try {
        	  if(wrs.getString(17) != null)
        		  tribJsp.setIdDominio(wrs.getString(17));
          } catch (Exception e) {
        	  tribJsp.setIdDominio("");
          }
          //fine LP PG22XX05
          arrTribForm.add(tribJsp);
        }
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
        //setErrorMessage(request, e.getMessage());
        setFormMessage("ricercaDocumentiForm", "errore: " + testoErrore, request);
      }   
	  //inizio LP PG21XX04 Leak
	  finally {
		try {
			if(wrs != null) {
				wrs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  }
	  //fine LP PG21XX04 Leak
  }


  public boolean callWsSaveDiscarico(HttpServletRequest request, ArrayList<Tributo> listTributi) {
    
    try {
      // Questa è la request da inviare al webservice 
      DiscaricoEcRequest discaricoEcRequest = new DiscaricoEcRequest();

      discaricoEcRequest.setCodiceUtente(request.getParameter("chiaveCodUte"));
      discaricoEcRequest.setTipoServizio("EP");
      discaricoEcRequest.setCodiceEnte(request.getParameter("chiaveCodEnte"));
      discaricoEcRequest.setTipoUfficio(request.getParameter("chiaveTipoUff"));
      discaricoEcRequest.setCodiceUfficio(request.getParameter("chiaveCodUff"));
      discaricoEcRequest.setImpostaServizio(request.getParameter("chiaveIS"));
      
      Documento doc = new Documento();
      doc.setAnnoEmissione("");
      doc.setNumeroEmissione("");
      doc.setDataNotifica("");
      doc.setNumeroBollettinoPagoPA("");
      doc.setImpBollettinoTotaleDocumento(new BigDecimal(0.00));
      doc.setIbanAccredito("");
      doc.setFlagFatturazioneElettronica("");
      doc.setIdentificativoUnivocoVersamento("");
      
      doc.setNumeroDocumento(request.getParameter("chiaveDoc"));
      discaricoEcRequest.setDocumento(doc);
      discaricoEcRequest.setListTributi(listTributi.toArray(new Tributo[]{}));

//      Gson gson = EntrateUtils.getGson();
//      System.out.println("discaricoEcRequest: " + gson.toJson(discaricoEcRequest));

      //      logger.debug("inserimentoEcRequest: " + gson.toJson(inserimentoEcRequest));
      DiscaricoEcResponse wsResponse = WSCache.integraEcDifferitoServer.discaricoTributi(discaricoEcRequest,request,(String)request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA));
      
//      System.out.println("wsResponse: " + gson.toJson(wsResponse));

      String esito = wsResponse.getCodiceEsito();
      final String RESP_ESITO_POSITIVO = "00";
      final String RESP_ERRORE_GENERICO = "01";
      final String RESP_ERRORE_CONFIGURAZIONE = "02";
      final String RESP_ERRORE_POSIZIONE_DEBITORIA_GIA_PRESENTE = "03";
      final String RESP_ERRORE_VALIDAZIONE = "04";

      if (RESP_ESITO_POSITIVO.equals(esito)) {
          setMessage("Discarico avvenuto correttamente");
      } else {
        setErrorMessage(wsResponse.getMessaggioEsito());
        return false;
      }

      } catch (Exception e) {
        WSCache.logWriter.logError("errore: " + e.getMessage(), e);
        return false;
      }

    return true;
    
    
  }
    public void ricercaTributi(HttpServletRequest request) throws Exception {

    RicercaTributiResponse out;
    RicercaTributiRequest in;
    in = prepareRicercaTributi(request);

    WSCache.logWriter.logDebug("Pagina interrogazione di test");
    out = WSCache.entrateBDServer.ricercaTributi(in, request);

    if (out.getPInfo().getNumRows() > 0) {
      request.setAttribute("listaTributi", out.getListXml());

      request.setAttribute("listaTributi.pageInfo", getPageInfo(out.getPInfo(), in.getPagina()
          .getRowsPerPage()));

    } else {
      String messaggio = "";
      if (request.getAttribute("messaggioRis") != null)
        messaggio = request.getAttribute("messaggioRis").toString() + " - ";
      setFormMessage("ricercaTributiForm", messaggio + Messages.NO_DATA_FOUND.format(), request);
    }
  }


  private String getFormatDate(java.util.Calendar data) {
    SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    return dateformat.format(data.getTime());
  }

  private void resetPage(HttpServletRequest request) {
    PropertiesTree configuration;

    configuration = (PropertiesTree) (request.getSession().getServletContext()
        .getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

    rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows
        .format()));

    if (request.getAttribute("rowsPerPage") != null
        && !((String) request.getAttribute("rowsPerPage")).equals("")
        && request.getAttribute("rowsPerPage").toString().indexOf(";") == -1) {
      rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
    }

    pageNumber = (request.getAttribute("pageNumber") == null)
        || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request
        .getAttribute("pageNumber").toString());

    order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();

  }



  private RicercaTributiRequest prepareRicercaTributi(HttpServletRequest request) {
    resetPage(request);

    RicercaTributiRequest ris;

    ris = new RicercaTributiRequest();
    ris.setPagina(new Paginazione(rowsPerPage, pageNumber, order));

    //set campi
    /*
    <s:textbox name="chiaveIS" label="chiaveISDett" bmodify="true" text="${chiaveIS}" cssclass="display_none" cssclasslabel="display_none"  />
    <!-- NUMERO DOCUMENTO EH1_CEH1NDOC -->
    <s:textbox name="chiaveDoc" label="chiaveDocDett" bmodify="true" text="${chiaveDoc}" cssclass="display_none" cssclasslabel="display_none"  />
    <!-- TIPO UFFICIO EH1_TANETUFF -->
    <s:textbox name="chiaveTipoUff" label="chiaveTipoUffDett" bmodify="true" text="${chiaveTipoUff}" cssclass="display_none" cssclasslabel="display_none"  />
    <!-- CODICE UFFICIO EH1_CANECUFF -->
    <s:textbox name="chiaveCodUff" label="chiaveCodUffDett" bmodify="true" text="${chiaveCodUff}" cssclass="display_none" cssclasslabel="display_none"  />
    <!-- CODICE UTENTE EH1_CUTECUTE -->
    <s:textbox name="chiaveCodUte" label="chiaveCodUteDett" bmodify="true" text="${chiaveCodUte}" cssclass="display_none" cssclasslabel="display_none"  />
    <!-- CODICE ENTE EH1_CANECENT -->
    <s:textbox name="chiaveCodEnte" label="chiaveCodEnteDett" bmodify="true" text="${chiaveCodEnte}" cssclass="display_none" cssclasslabel="display_none"  />
    <!-- CODICE SERVIZIO EH1_TEH1SERV -->
    <s:textbox name="chiaveServ" label="chiaveServDett" bmodify="true" text="${chiaveServ}" cssclass="display_none" cssclasslabel="display_none"  />
    <!-- CODICE TOMBSTONE EH1_CEH1TOMB -->
    <s:textbox name="chiaveTomb" label="chiaveTombDett" bmodify="true" text="${chiaveTomb}" cssclass="display_none" cssclasslabel="display_none"  />
    <!-- PROGR.FLUSSO EH1_PEH1FLUS -->
    <s:textbox name="chiaveFlusso" label="chiaveFlussoDett" bmodify="true" text="${chiaveFlusso}" cssclass="display_none" cssclasslabel="display_none"  />

    
    */
    ris.setCodiceSocieta(request.getParameter(""));
    ris.setCodiceUtente(request.getParameter("chiaveCodUte"));
    ris.setCodiceEnte(request.getParameter("chiaveCodEnte"));
    ris.setCodiceUfficio(request.getParameter("chiaveCodUff"));
    ris.setTipoUfficio(request.getParameter("chiaveTipoUff"));

/*
        String impostaServ = request.getParameter("dettImpostaServ");
        
        if(impostaServ== null) impostaServ = "";
        if( impostaServ.length()> 0 )
        	impostaServ = (impostaServ.substring(0,impostaServ.indexOf('-')).trim());
        ris.setImpostaServizio(impostaServ);
*/
    ris.setImpostaServizio(request.getParameter("chiaveIS"));
    ris.setNumeroDocumento(request.getParameter("chiaveDoc"));

    return ris;

  }




  protected void salvaStato(HttpServletRequest request) {
    super.salvaStato(request);
    /*
     * Salvo le date che richiedono l'utilizzo di un tipo "Calendar"
     */
  }




}
