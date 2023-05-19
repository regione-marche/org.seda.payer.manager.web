package org.seda.payer.manager.entrate.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.seda.payer.manager.adminusers.actions.SeUsersEditAction;
import org.seda.payer.manager.adminusers.util.ProfiloUtil;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.configurazione.actions.AnagEnteAction;
import org.seda.payer.manager.entrate.actions.GestioneDocumentiCaricoAction.StatoEditDocumento.Mode;
import org.seda.payer.manager.entrate.actions.util.EntrateFilteredWs;
import org.seda.payer.manager.entrate.actions.util.EntrateUtils;
import org.seda.payer.manager.entrate.actions.util.EntrateFilteredWs.EventoFiltro;
import org.seda.payer.manager.entrate.actions.util.EntrateFilteredWs.LetturaEcResponse;
import org.seda.payer.manager.entrate.exceptions.ConfigurazioneException;
import org.seda.payer.manager.entrate.flows.GestioneDocumentiCaricoFlow;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.util.ValidatoreHelper;
import org.seda.payer.manager.ws.WSCache;

import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Anagrafica;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.CancellazioneEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.CancellazioneEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Configurazione;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.ConfigurazioneIUV;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Documento;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.InserimentoEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.InserimentoEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.RichiestaAvvisoPagoPaRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.RichiestaAvvisoPagoPaResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Ruolo;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Scadenza;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.StampaDocumento;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Tributo;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.VariazioneEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.VariazioneEcResponse;
//import com.google.gson.Gson;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.validator.ValidationMessage;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.components.validation.ValidationContext;
import com.seda.j2ee5.maf.components.validation.ValidationErrorMap;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.ActionManager;
import com.seda.j2ee5.maf.core.action.HtmlAction;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.payer.bancadati.webservice.dati.Paginazione;
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiResponse;
import com.seda.payer.bancadati.webservice.dati.ResponseTypeRetCode;
import com.seda.payer.bancadati.webservice.srv.FaultType;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.pgec.webservice.commons.dati.GetStaticDDLListsRequest;
import com.seda.payer.pgec.webservice.commons.dati.GetStaticDDLListsResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaBelfioreResponse;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaRequest;
import com.seda.payer.pgec.webservice.configtassonomia.dati.ConfigTassonomiaListaResponse;

import org.seda.payer.manager.adminusers.util.Error;

/**
 TODO:LUCIANO: CFunzionalità CRUD per DocumentiCarico.
 A partire da esempio di:
  {@link RicercaDocumentiCarichiAction} ( che verrà quindi eliminata )
  {@link BaseEntrateAction}
  {@link AnagEnteAction}
 
 Nota: la {@link ActionManager} istanzia questa action ad ogni request... pertanto l'elaborazione 
 avviene su un singolo thread. Posso utilizzare variabili istanza.

 
 
 Funzionalità implementate (stato)[eventi]:
 >ListaDocumenti
 >>paginazione (ordinamento, numero pagine, pagina corrente)
      [avanzamento pagina, pagina diretta changed, num righe changed]
 >>FiltroDocumenti (valori filtro)
      [reset filtro, filtro changed]

 >Edit Nuovo Documento (valori campi: oggetti anagrafica, documento e liste scadenze e tributi,
      messaggi errore validazione  )
      [Indietro senza salvare, salva creando nuovo documento, aggiunta/eliminazione scadenza o tributo,
       combo changed... "ricompila campi calcolati"   ]
 >> messaggio avvenuto salvataggio add
      ["evento messaggio letto e torna indietro"]
 >Edit Documento Esistente...
 >> campi bloccati in modifica, salvataggio edit
 >> messaggio avvenuto salvataggio edit
 
 Nota: TODO:PROVINCIA
 La provincia non può essere anche estera? Non dispongo della sigla/descrizione provincia.
 In caso di comune estero EE, i campi nazione,provincia,comune, dovrebbero essere gestiti in modo testo? 
 Per esempio vedi: {@link SeUsersEditAction} e {@link ProfiloUtil}
 */

public class GestioneDocumentiCaricoAction extends HtmlAction {
  static Logger logger = Logger.getLogger(GestioneDocumentiCaricoAction.class);

  private static final long serialVersionUID = 1L;

  static final DecimalFormat df = new DecimalFormat("0.00");
  
  static SimpleDateFormat dateFormatDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");

  /** Questa Action "vive" all'interno di una Request. */
  HttpServletRequest request;
  UserBean userBean;

  /** Screen attualmente visualizzato nella sessione/sottosessione client.*/
  String screen;

  public EntrateFilteredWs filteredDsListaDocumenti;
  StatoFiltroDocumenti filtroDocumenti;

  StatoPagineDocumenti pagineDocumenti;

  public EntrateFilteredWs filteredDsDocumento;
  StatoEditDocumento editDocumento;

  // messaggi del tipo "status bar"... relativi alla operazione svolta in questa request.
  // Sono diversi dal "validation messages".
  private String message = "";
  private String errorMessage = "";
  //inizio LP PG200360
  private String messaggiosostituisci = null;
  //fine LP PG200360

  /** inizializzazione o reset dal button del form*/
  boolean initialize;

  boolean filtroReset;

  private String anagrafica_siglaProvinciaFiscale;
  private String anagrafica_siglaProvinciaNascita;

  public Object service(HttpServletRequest request) throws ActionException {
	  
	  
	df.setRoundingMode(RoundingMode.UNNECESSARY); //PREJAVA18_LUCAP_15092020_TK2020091588000101  
	
    this.request = request;
    EntrateUtils.removeParametriFromAttributes(request);

    logger.info("Inizio service, \n URL=" + request.getRequestURL() + "\n"
        + " REQUEST PARAMETERS:\n" + EntrateUtils.parametersToString(request)
        + "----\n REQUEST ATTRIBUTES:\n" + EntrateUtils.attributesToString(request));

    
    SessionState sessionState = restoreStateFromSession();

    if (screen == null) {
      screen = Screen.SEARCH; // default
      initialize = true;
    }
    
    if (screen == Screen.STAMPA && !isFiredButton(request, "button_stampa"))
    	screen = Screen.SEARCH;

//    pagineDocumenti.DDLType = request.getParameter("DDLType");
    filtroReset = initialize || isFiredButton(request, "tx_button_reset");

    workaroundMenu();
    
    if (isFiredButton(request, "tx_button_indietro")) {
    	//marini -ripristina i combo
//    	anagrafica_siglaProvinciaFiscale = "";
//        anagrafica_siglaProvinciaNascita = "";
//        editDocumento.keyEnte = "";
//        filteredDsDocumento.reset();
    	screen = Screen.SEARCH;
    }
        
    if (screen == Screen.SEARCH || initialize) {
      onGestioneDocumentiScreen(sessionState);
    } else if (screen == Screen.ADD) {
      onAddOrEditDocumentoScreen(false);
    } else if (screen == Screen.EDIT) {
      onAddOrEditDocumentoScreen(true);
    } else if (screen == Screen.DELETE) {
      onDeleteDocumentoScreen();
    } else if (screen == Screen.STAMPA) {
    	onGestioneDocumentiScreen(sessionState);
    }

    saveStateToSession(sessionState);

    saveRequestAttributes();

    logger.info("fine service, \n" + " REQUEST PARAMETERS:\n"
        + EntrateUtils.parametersToString(request) + "----\n REQUEST ATTRIBUTES:\n"
        + EntrateUtils.attributesToString(request));
    return null;
  }

  /** Quando viene invocata da forward del MAF si perde nella prima invocazione la configurazione 
   * del menu... e la request viene invocata con "http://10.10.80.6:9080/manager/entrate/ricercaDocumenti.do?mnId=64&mnLivello=3&mnId1=3&mnId2=21" */
  private void workaroundMenu() {
    String forwardedFrom = (String) request.getAttribute("javax.servlet.forward.servlet_path");
    if(forwardedFrom !=null && forwardedFrom.endsWith("ricercaDocumenti.do")) {
      request.setAttribute("workaroundMenu", true);
      screen = Screen.SEARCH;
    }
  }

  private void saveStateToSession(SessionState sessionState) {
    // Salvo tutto nel mio oggetto in sessione...
    sessionState.screen = screen;

    sessionState.filteredDsListaDocumenti = filteredDsListaDocumenti;
    sessionState.filtroDocumenti = filtroDocumenti;
    sessionState.pagineDocumenti = pagineDocumenti;

    sessionState.filteredDsDocumento = filteredDsDocumento;
    sessionState.editDocumento = editDocumento;

    sessionState.anagrafica_siglaProvinciaFiscale = anagrafica_siglaProvinciaFiscale;
    sessionState.anagrafica_siglaProvinciaNascita = anagrafica_siglaProvinciaNascita;

    request.getSession().setAttribute(SessionState.KEY, sessionState);
  }

  private SessionState restoreStateFromSession() {
    // Inizializzo variabili della Action/Request/Session
    userBean = (UserBean) request.getSession().getAttribute(SignOnKeys.USER_BEAN);
    if (userBean != null) {
      request.setAttribute("operatore", userBean.getUserName());
      // Da modificare col DEBUGGER
//      userBean.setUserProfile(ProfiloUtente.AMEN.toString());
//      userBean.setCodiceSocieta("");
//      userBean.setCodiceUtente("");
//      userBean.setChiaveEnteConsorzio();
    } else {
      request.setAttribute("operatore", "Sconosciuto");
    }

    SessionState sessionState = (SessionState) request.getSession().getAttribute(SessionState.KEY);
    if (sessionState == null) {
       	System.out.println("RestoreStateFron sessione = null");
      sessionState = new SessionState();

      //  Filtro Documenti
      filteredDsListaDocumenti = new EntrateFilteredWs();
      filteredDsListaDocumenti.setUserContext(userBean);

      //  Filtro Documenti2
      filtroDocumenti = new StatoFiltroDocumenti();

      //  Paginazione Documenti
      pagineDocumenti = new StatoPagineDocumenti();

      filteredDsDocumento = new EntrateFilteredWs();
      filteredDsDocumento.setUserContext(userBean);
      filteredDsDocumento.setRequestContext(request);
      filteredDsDocumento.reset();
      editDocumento = new StatoEditDocumento();

    } else {
       	System.out.println("RestoreStateFron sessione != null");
      filteredDsListaDocumenti = sessionState.filteredDsListaDocumenti;
      filtroDocumenti = sessionState.filtroDocumenti;
      pagineDocumenti = sessionState.pagineDocumenti;
      filteredDsDocumento = sessionState.filteredDsDocumento;
      editDocumento = sessionState.editDocumento;
      anagrafica_siglaProvinciaFiscale = sessionState.anagrafica_siglaProvinciaFiscale;
      anagrafica_siglaProvinciaNascita = sessionState.anagrafica_siglaProvinciaNascita;
    }

    // su quale screen del flow mi trovo? (mittente della request/evento)
    screen = Screen.fromString(sessionState.screen);
    return sessionState;
  }

  
  //inizio LP PG200360
  /*
  void loadTassomie(HttpServletRequest request, HttpSession session) {
	if (session.getAttribute("tassonomie") == null) {
		try {
			ConfigTassonomiaListaResponse configTassonomiaListaResponse = getTassonomiaListaResponse(request);
			session.setAttribute("tassonomie", configTassonomiaListaResponse.getListaTassonomia().getListXml());
		} catch (Exception e) {
			throw new RuntimeException("Errore nel caricamento delle tassonomie", e);
		}
	}
  }
  */
  //fine LP PG200360

  /** Imposto attributi  nella request, per rendering tramite JSP/templates. */
  void saveRequestAttributes() {
    // copia da session a request Attribute, per essere utilizzata dai tag seda. 

    // tabelle statiche salvate in sessione copiate su request
    EntrateFilteredWs.loadStaticXml_DDL(request, request.getSession());
    //inizio LP PG200360
    //loadTassomie(request, request.getSession());
    //fine LP PG200360

    request.setAttribute("pagineDocumenti", pagineDocumenti);

    // lista campi data/calendar
    ArrayList<String> dateFieldList = new ArrayList<String>();


    request.setAttribute("ext", pagineDocumenti.ext ? "1" : "0");

    // mantengo lo scroll verticale tra una pagina e la successiva tramite il parametro scrollY
    // param:scrollY
    String scrollY = request.getParameter("scrollY");
    if (Strings.isNullOrEmpty(scrollY))
      scrollY = "0";
    request.setAttribute("scrollY", scrollY);

    request.setAttribute("listaProvince", request.getSession().getAttribute("listaProvince"));

    if (editDocumento != null) {

      // campi aggiuntivi non presenti nel bean o di tipo diverso
      request.setAttribute("documento_dataNotifica", EntrateUtils
          .calendarFromDDMMYYYY(editDocumento.documento.getDataNotifica()));
      dateFieldList.add("documento_dataNotifica");

      request.setAttribute("anagrafica_dataNascita", EntrateUtils
          .calendarFromDDMMYYYY(editDocumento.anagrafica.getDataNascita()));
      dateFieldList.add("anagrafica_dataNascita");

      request.setAttribute("anagrafica_siglaProvinciaFiscale", anagrafica_siglaProvinciaFiscale);
      request.setAttribute("anagrafica_siglaProvinciaNascita", anagrafica_siglaProvinciaNascita);

      request.setAttribute("listBelfioreFiscale", request.getSession().getAttribute(
          "listBelfioreFiscale"));
      request.setAttribute("listBelfioreNascita", request.getSession().getAttribute(
          "listBelfioreNascita"));


      // importo Bollettino totale doc.
      String documento_impBollettinoTotaleDocumento = "";
      BigDecimal value = editDocumento.documento.getImpBollettinoTotaleDocumento();
      if (value != null) {
        documento_impBollettinoTotaleDocumento = df.format(value);
      }
      //marini per replace  
      	request.setAttribute("documento_impBollettinoTotaleDocumento",documento_impBollettinoTotaleDocumento.replace(".", ","));

      // importi formattati tributi...
      TributoExtra[] trib = new TributoExtra[editDocumento.listaTributi.size()];
      for (int i = 0; i < trib.length; i++) {
        trib[i] = new TributoExtra();

        BigDecimal importo = editDocumento.listaTributi.get(i).getImpTributo();
        if (importo == null)
          trib[i].impTributo = "";
        else
          //marini per replace  
          trib[i].impTributo = df.format(importo).replace(".",",");
        trib[i].discaricoTributo = "12,34";
        trib[i].residuoTributo = "0,45";
      }
      request.setAttribute("trib", trib);

      // importi e calendar nelle scadenze...
      ScadenzaExtra[] scad = new ScadenzaExtra[editDocumento.listaScadenze.size()];
      for (int i = 0; i < scad.length; i++) {
        scad[i] = new ScadenzaExtra();
        scad[i].dataScadenzaRata = EntrateUtils.calendarFromDDMMYYYY(editDocumento.listaScadenze
            .get(i).getDataScadenzaRata());
        dateFieldList.add(String.format("scad_%d_dataScadenzaRata", i));

        BigDecimal importo = editDocumento.listaScadenze.get(i).getImpBollettinoRata();
        if (importo == null)
          scad[i].importoBollettinoRata = "";
        else
          //marini per replace  
          scad[i].importoBollettinoRata = df.format(importo).replace(".",",");
      }
      request.setAttribute("scad", scad);

      // formato ['field1','field2']
      String dateFieldListJSArray = "[";
      int i = 0;
      for (String s : dateFieldList) {
        if (i++ > 0)
          dateFieldListJSArray += ",";
        dateFieldListJSArray += "'" + s + "'";
      }
      dateFieldListJSArray += "]";
      request.setAttribute("dateFieldListJSArray", dateFieldListJSArray);
    }

    // ddl lists

    if (screen == Screen.SEARCH) {
      request.setAttribute("listaSocieta", request.getSession().getAttribute("listaSocieta"));
      request.setAttribute("listaUtenti", filteredDsListaDocumenti.getListaUtenti());
      request.setAttribute("listaEnti", filteredDsListaDocumenti.getListaEnti());
      request.setAttribute("listaTipologieServizio", filteredDsListaDocumenti
          .getListaTipologieServizio());
      request.setAttribute("listaImpostaServizio", filteredDsListaDocumenti
          .getListaImpostaServizio());
    } else if (screen == Screen.ADD || screen == Screen.EDIT || screen == Screen.DELETE) {
      // TODO:LUCIANO: può essere utile cambiare i nomi? es: listaUtentiTes
      request.setAttribute("listaSocUtenteEnte", filteredDsDocumento.getListaSocUtenteEnte());
      request.setAttribute("listaTipologieServizio", filteredDsDocumento
          .getListaTipologieServizio());
      request.setAttribute("listaImpostaServizio", filteredDsDocumento.getListaImpostaServizio());
      //inizio LP PG200360
      //request.setAttribute("tassonomie", request.getSession().getAttribute("tassonomie"));
	  try {
		if(request.getAttribute("tassonomie") == null) {
			ConfigTassonomiaListaResponse configTassonomiaListaResponse = getTassonomiaListaResponse(request);
			request.setAttribute("tassonomie", configTassonomiaListaResponse.getListaTassonomia().getListXml());
		}
	  } catch (Exception e) {
		throw new RuntimeException("Errore nel caricamento delle tassonomie", e);
	  }
      //inizio LP PG200360
    }

    request.setAttribute("screen", screen);
    request.setAttribute("filtroDocumenti", filtroDocumenti);
    request.setAttribute("pagineDocumenti", pagineDocumenti);
    request.setAttribute("editDocumento", editDocumento);

//    // messages.jsp
    request.setAttribute("message", getMessage());
    request.setAttribute("errorMessage", getErrorMessage());
    //inizio LP PG200360
    if(messaggiosostituisci == null) {
    	messaggiosostituisci = (String) request.getSession().getAttribute("messaggiosostituisci");
        if(messaggiosostituisci == null) {
			String dbSchemaCodSocieta = (String) request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
			PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
			if (configuration != null){
				String appo = configuration.getProperty("gestionedocumenticarico.messaggiosostituisci." + dbSchemaCodSocieta);
				messaggiosostituisci = (appo != null ? appo.trim() : "<br />Vuoi procedere con il salvataggio ?<br />");
			}
		    request.getSession().setAttribute("messaggiosostituisci", messaggiosostituisci);
        }
    }
    request.setAttribute("messaggiosostituisci", messaggiosostituisci);
    //fine LP PG200360
    
  }

  /** mi trovo nella schermata di filtro/lista documenti...*/
  void onGestioneDocumentiScreen(SessionState sessionState) {
	//inizio LP PG200360
	if(screen != Screen.EDIT) {  
	//fine LP PG200360
    if (request.getParameter("tx_anno_emissione") != null || filtroReset) {
    	try {
    	  onFiltroDocumentiRequest();
    	  resetFiltri(request); //TK2020121188000041
		} catch (ConfigurazioneException e) {
			e.printStackTrace();
		}
    }

    if (request.getParameter("pageNumber") != null || filtroReset) {
      onPaginationRequest();
    }

    if (pagineDocumenti.changed) {
      // ricarico pagina risultato (o per variazione filtro o paginazione...)
      try {
        pagineDocumenti.changed = false;
        
        
        salvaFiltriRicerca(request); //TK2020121188000041
        recuperaFiltriRicerca(request); //TK2020121188000041
        ricercaDocumenti(request, request.getSession());
        
        
      } catch (Exception e) {
        logger.warn("Errore ricerca documenti", e);
        WSCache.logWriter.logError("errore: " + e.getMessage(), e);
        setFormValidationMessage("gestioneDocumentiCaricoForm", "errore ricerca: "
            + BaseEntrateAction.testoErrore);
      }
    }
	//inizio LP PG200360
	}
	//fine LP PG200360
    
    //TK2020121188000041
    if (isFiredButton(request, "tx_button_tipologia_servizio_changed")) {
    	salvaFiltriRicerca(request);
    	recuperaFiltriRicerca(request);
    }
    //FINE TK2020121188000041

    if (isFiredButton(request, "button_nuovo")) {
      onNewDocumentRequest(sessionState);
    }

    if (isFiredButton(request, "button_modifica")) {
      onEditDocumentRequest();
    }

    if (isFiredButton(request, "button_elimina")) {
      onDeleteDocumentRequest();
    }
    
    if (isFiredButton(request, "button_stampa")) {
        onStampaDocumentRequest();
      }

  }

  /** La request contiene dati del "Filtro Documenti" oppure filtroReset.
   * Se filtroReset tutti i parametri vanno ignorati... 
   * Analizzo eventi di "reload lista" che potrebbero invalidare
   * alcuni valori a valle, es: ricarico lista Utenti Società -> Utente ed Ente potrebbero
   * non appartenere alla lista ricaricata. */
  void onFiltroDocumentiRequest() throws ConfigurazioneException {

    // parametri in input dalla request

    // valori richiesti...alcuni potranno essere invalidati/scartati dal "reload lista"
    EventoFiltro filtroEvent = new EventoFiltro();
    filtroEvent.keySocieta = request.getParameter("tx_societa");
    filtroEvent.keyUtente = request.getParameter("tx_utente");
    filtroEvent.keyEnte = request.getParameter("tx_ente");
    filtroEvent.keyTipologiaServizio = request.getParameter("tx_tipologia_servizio");
    filtroEvent.keyImpostaServizio = request.getParameter("tx_imposta_servizio");

    filtroEvent.tx_societa_changed = isFiredButton(request, "tx_button_societa_changed");
    filtroEvent.tx_utente_changed = isFiredButton(request, "tx_button_utente_changed");
    filtroEvent.tx_ente_changed = isFiredButton(request, "tx_button_ente_changed");
    filtroEvent.tx_tipologia_servizio_changed = isFiredButton(request,
        "tx_button_tipologia_servizio_changed");
    //inizio LP PG200360
    filtroEvent.tx_button_flagstampaavviso_changed = isFiredButton(request, "tx_button_flagstampaavviso_changed");
    //fine LP PG200360
    filtroEvent.filtroReset = filtroReset;

    filteredDsListaDocumenti.setRequestContext(request);
    filteredDsListaDocumenti.trySetFilter(filtroEvent);

    // campi validati... per rendering JSP client
    filtroDocumenti.ddlSocietaDisabled = filteredDsListaDocumenti.isDdlSocietaDisabled();
    filtroDocumenti.keySocieta = filteredDsListaDocumenti.getKeySocieta();

    filtroDocumenti.ddlUtenteDisabled = filteredDsListaDocumenti.isDdlUtenteDisabled();
    filtroDocumenti.keyUtente = filteredDsListaDocumenti.getKeyUtente();

    filtroDocumenti.ddlEnteDisabled = filteredDsListaDocumenti.isDdlEnteDisabled();
    filtroDocumenti.keyEnte = filteredDsListaDocumenti.getKeyEnte();

    filtroDocumenti.ddlImpostaServizioDisabled = filteredDsListaDocumenti
        .isDdlImpostaServizioDisabled();
    filtroDocumenti.keyImpostaServizio = filteredDsListaDocumenti.getKeyImpostaServizio();

    filtroDocumenti.keyTipologiaServizio = filteredDsListaDocumenti.getKeyTipologiaServizio();

    // analizzo la seconda parte del filtro... 
    filtroDocumenti.codFiscale = filtroReset ? "" : request.getParameter("codFiscale");
    filtroDocumenti.annoEmissione = filtroReset ? "" : request.getParameter("tx_anno_emissione");
    filtroDocumenti.numEmissione = filtroReset ? "" : request.getParameter("numEmissione");
    filtroDocumenti.numDocumento = filtroReset ? "" : request.getParameter("numDocumento");
    filtroDocumenti.stato_documento = filtroReset ? "" : request.getParameter("stato_documento");
    
    filtroDocumenti.numeroBollettino = filtroReset ? "" : request.getParameter("numeroBollettino");
    filtroDocumenti.numeroIUV = filtroReset ? "" : request.getParameter("numeroIUV");
    
    

    if (isFiredButton(request, "tx_button_cerca_exp")) {
      /* Toggle ricerca espansa... rieffettuo la search */
      pagineDocumenti.ext = !pagineDocumenti.ext;
    }

    if (isFiredButton(request, "tx_button_cerca_exp") || isFiredButton(request, "tx_button_cerca")) {
      pagineDocumenti.pageNumber = 1;
      pagineDocumenti.changed = true;
    }
  }

  void onPaginationRequest() {
    if (filtroReset) {
      // inizializzo da configuration
      PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext()
          .getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
      pagineDocumenti.rowsPerPage = Integer.parseInt(configuration
          .getProperty(PropertiesPath.defaultListRows.format()));
      pagineDocumenti.pageNumber = 1;
      pagineDocumenti.order = "";
      pagineDocumenti.ext = false;
    } else {
      pagineDocumenti.changed = true;

      String rowsParam = request.getParameter("rowsPerPage");
      if (rowsParam != null && !rowsParam.equals("") && rowsParam.indexOf(";") == -1) {
        pagineDocumenti.rowsPerPage = Integer.parseInt(rowsParam);
      }

      String pageParam = request.getParameter("pageNumber");
      if (pageParam != null && !pageParam.equals(""))
        pagineDocumenti.pageNumber = Integer.parseInt(pageParam);

      String orderParam = request.getParameter("order");
      if (orderParam != null)
        pagineDocumenti.order = orderParam;

      pagineDocumenti.ext = "1".equals(request.getParameter("ext"));
    }
  }


  /** Per la richiesta web-service le codifiche vanno estrapolate in vari modi... */
  private RecuperaDocumentiRequest prepareRicerca(HttpServletRequest request, HttpSession session) {

    String codiceSocietaRis = filteredDsListaDocumenti.findSocietaSel() == null ? ""
        : filteredDsListaDocumenti.findSocietaSel().getCodSocieta();
    String codiceUtenteRis = filteredDsListaDocumenti.findUtenteSel() == null ? ""
        : filteredDsListaDocumenti.findUtenteSel().codUtente;
    String codiceEnteRis = filteredDsListaDocumenti.getEnte() == null ? ""
        : filteredDsListaDocumenti.getEnte().getCodEnte();

    String tipologiaServizioRis = filteredDsListaDocumenti.findTipologiaServizioSel() == null ? ""
        : filteredDsListaDocumenti.findTipologiaServizioSel().codTipologiaServizio;
    String impostaServizioRis = filteredDsListaDocumenti.getImpostaServizio() == null ? ""
        : filteredDsListaDocumenti.getImpostaServizio().getCodImposta();

    RecuperaDocumentiRequest ris = new RecuperaDocumentiRequest();

    ris.setPagina(new Paginazione(pagineDocumenti.rowsPerPage, pagineDocumenti.pageNumber,
        pagineDocumenti.order));

    ris.setCodiceSocieta(codiceSocietaRis);
    ris.setCodiceUtente(codiceUtenteRis);
    ris.setCodiceEnte(codiceEnteRis);

    ris.setCodiceFiscale(filtroDocumenti.codFiscale);

    ris.setAnnoEmissione(filtroDocumenti.annoEmissione);
    ris.setNumeroEmissione(filtroDocumenti.numEmissione);
    ris.setTipoUfficio("");
    ris.setCodiceUfficio("");
    
    //TK2020121188000041
    //impostaServizioRis = request.getParameter("impostaServVal")!=null?request.getParameter("impostaServVal").toString():impostaServizioRis;
    //tipologiaServizioRis = request.getParameter("tipServVal")!=null?request.getParameter("tipServVal").toString():tipologiaServizioRis;
    //FINE TK2020121188000041
    
    ris.setTipologiaServizio(tipologiaServizioRis);
    ris.setImpostaServizio(impostaServizioRis);

    ris.setStatoDocumento(filtroDocumenti.stato_documento);
    ris.setNumeroDocumento(filtroDocumenti.numDocumento);
    
    ris.setNumeroBollettino(filtroDocumenti.numeroBollettino);
    ris.setNumeroIUV(filtroDocumenti.numeroIUV);

    //    ris.setStatoSospensione(request.getParameter("stato_sospensione"));
    //    ris.setStatoProcedure(request.getParameter("stato_procedure"));

    return ris;
  }

  /** Ricerca al web-service */
  private void ricercaDocumenti(HttpServletRequest request, HttpSession session) throws Exception {
    RecuperaDocumentiRequest in = prepareRicerca(request, session);

    //   WSCache.logWriter.logDebug("Pagina interrogazione di test");
    RecuperaDocumentiResponse out = EntrateFilteredWs.ricercaDocumenti(in, request);

    if (out.getPInfo().getNumRows() > 0) {
      String listaDocumenti = out.getListXml();
      String csv = EntrateUtils.webrowsetToCsv(listaDocumenti);
      System.out.println(csv);
      request.setAttribute("listaDocumenti", listaDocumenti);

      request.setAttribute("listaDocumenti.pageInfo", getPageInfo(out.getPInfo(), in.getPagina().getRowsPerPage()));

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
      
      
      

      request.setAttribute("dataUltimoAgg", out.getDataUltimoAgg() == null ? "" : formatDate(out
          .getDataUltimoAgg()));
    } else if (out.getRisultato().getRetCode().equals(ResponseTypeRetCode.value1)) {
      // nessun risultato
      setFormValidationMessage("gestioneDocumentiCaricoForm", Messages.NO_DATA_FOUND.format());
    } else {
      // errore servizio 
      String messaggio = out.getRisultato().getRetMessage();
      setFormValidationMessage("gestioneDocumentiCaricoForm", messaggio);
    }
  }

  /** inizializzo lista Province e lista Societa/Utente/Ente */
  private void onFormDocumentoConstantsLoad() throws ConfigurazioneException {

    try {
      EntrateFilteredWs.loadListaProvince(request);
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    // lo chiamo solo per inizializzazione...
    onEditDocumentoTestataChanged(false);
  }

  /** carico i dati dal WS negli oggetti di edit */
  private void onFormDocumentoDataLoad(EntrateFilteredWs.IdDocumento idDoc) throws SQLException,
      DaoException, RemoteException, com.seda.payer.pgec.webservice.commons.srv.FaultType, ConfigurazioneException {
    // READ
	 LetturaEcResponse letturaEcResponse = filteredDsDocumento.readDocumentoCarico(idDoc, request);

    // imposto l'Ente di pertinenza
    String keyEnte = idDoc.codSocieta + idDoc.codUtente + idDoc.codEnte;
	System.out.println("Documento ENTE = "+keyEnte);

    filteredDsDocumento.reset();
    EventoFiltro testataEvent = new EventoFiltro();
    testataEvent.keyEnte = keyEnte;
    testataEvent.tx_ente_changed = true;
    testataEvent.update = true;
    filteredDsDocumento.setRequestContext(request);
    filteredDsDocumento.trySetFilter(testataEvent);

    editDocumento.keyEnte = filteredDsDocumento.getKeyEnte();
    editDocumento.idDoc = idDoc;
    editDocumento.codImpostaServizio = idDoc.codImpostaServizio;
    editDocumento.codUtenteEnte = filteredDsDocumento.getCodUtenteEnte();
    editDocumento.tipoUfficio = filteredDsDocumento.getTipoUfficio();
    editDocumento.codiceUfficio = filteredDsDocumento.getCodUfficio();
    editDocumento.chiaveFlusso = idDoc.chiaveFlusso;
    editDocumento.codTipologiaServizio = idDoc.codTipologiaServizio;
    
    anagrafica_siglaProvinciaFiscale = letturaEcResponse.getProvinciaFiscale();
    onUpdateListaProvinciaFiscale();
    anagrafica_siglaProvinciaNascita = letturaEcResponse.getProvinciaNascita();
    onUpdateListaProvinciaNascita();

    editDocumento.ruolo = letturaEcResponse.getRuolo();
    //marini
    editDocumento.configurazione = letturaEcResponse.getConfigurazione();
    
    editDocumento.documento = letturaEcResponse.getDocumento();
    editDocumento.anagrafica = letturaEcResponse.getAnagrafica();
    editDocumento.listaTributi.clear();
    for (Tributo tributo : letturaEcResponse.getListTributi())
      editDocumento.listaTributi.add(tributo);
    editDocumento.listaScadenze.clear();
    for (Scadenza scadenza : letturaEcResponse.getListScadenze())
      editDocumento.listaScadenze.add(scadenza);
    //inizio LP PG200360
    editDocumento.stampaAvvisoEseguita = letturaEcResponse.stampaAvvisoEseguita;
    editDocumento.modalitaAggiornamento = letturaEcResponse.modalitaAggiornamento;
    StatoEditDocumento editDocumentoOLD = new StatoEditDocumento();
    Documento docOLD = new Documento();
    docOLD.setNumeroBollettinoPagoPA(editDocumento.documento.getNumeroBollettinoPagoPA());
    docOLD.setIdentificativoUnivocoVersamento(editDocumento.documento.getIdentificativoUnivocoVersamento());
    editDocumentoOLD.documento = docOLD;
    ArrayList<Scadenza> lscadOLD = new ArrayList<Scadenza>(); 
    for (Scadenza scad : editDocumento.listaScadenze) {
	   String numeroBollettinoPagoPA = scad.getNumeroBollettinoPagoPA();
	   String identificativoUnivocoVersamento = scad.getIdentificativoUnivocoVersamento();
	   Scadenza scadOLD = new Scadenza(); 
	   scadOLD.setNumeroBollettinoPagoPA(numeroBollettinoPagoPA);
	   scadOLD.setIdentificativoUnivocoVersamento(identificativoUnivocoVersamento);
	   lscadOLD.add(scadOLD);
    }
    editDocumentoOLD.listaScadenze = lscadOLD;
    request.getSession().setAttribute("editDocumentoOLD", editDocumentoOLD);
    //fine LP PG200360
  }

  /** Mi chiedono di aggiugere un nuovo documento...*/
  private void onNewDocumentRequest(SessionState sessionState) {
    editDocumento = EntrateUtils.createEmptyStatoEditDocumento(sessionState);
    anagrafica_siglaProvinciaFiscale="";
    anagrafica_siglaProvinciaNascita="";
    //marini
    //filteredDsDocumento.ente=null;    ///////////////////<-------------------------------
    filteredDsDocumento.identificativoFlusso="";
    //marini
    
    try {
    	onFormDocumentoConstantsLoad();
    } catch (ConfigurazioneException ex) {
    	ex.printStackTrace();
    	setFormValidationMessage("frmAction", "Impossibile recuperare Id Dominio/AuxDigit/Application Code/Segregation Code");
    	//return;
    }
    String retMessage = "";
    retMessage = checkConfigurazioneIUV(editDocumento);
    if (retMessage.trim().length()>0) {
    	setFormValidationMessage("frmAction", retMessage);
    }
    
    editDocumento.mode = Mode.CREATE;
    screen = Screen.ADD;
  }


  /** Mi chiedono di entrare in EDIT di un documento*/
  private void onEditDocumentRequest() {
    try {
      onFormDocumentoConstantsLoad();

      EntrateFilteredWs.IdDocumento idDoc = parseIdDocumentoFromRequest();
      onFormDocumentoDataLoad(idDoc);

      String retMessage = "";
      retMessage = checkConfigurazioneIUV(editDocumento);
      if (retMessage.trim().length()>0) {
     	  setFormValidationMessage("frmAction", retMessage);
      }
      //inizio LP PG200360
      request.getSession().setAttribute("idDocOLD", idDoc);
      //fine LP PG200360
      logger.debug("avviato EDIT documento:" + idDoc);
    } catch (ConfigurazioneException ex) {
      	ex.printStackTrace();
      	setFormValidationMessage("frmAction", "Impossibile recuperare Id Dominio/AuxDigit/Application Code/Segregation Code");
      	//return;
    } catch (Exception e) {
    	e.printStackTrace();
    	throw new RuntimeException(e);
    }

    editDocumento.mode = Mode.UPDATE;
    screen = Screen.EDIT;
  }



  /** Mi chiedono di ELIMINARE un documento, chiedo conferma*/
  private void onDeleteDocumentRequest() {
    try {
      onFormDocumentoConstantsLoad();

      EntrateFilteredWs.IdDocumento idDoc = parseIdDocumentoFromRequest();
      onFormDocumentoDataLoad(idDoc);
      setMessage("Attenzione! Conferma Eliminazione Documento");

      editDocumento.mode = Mode.DELETE;
      screen = Screen.DELETE;
      
      logger.debug("avviato richiesta CONFERMA DI CANCELLAZIONE documento:" + idDoc);
    } catch (ConfigurazioneException ex) {
    	ex.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /** Mi CONFERMANO di ELIMINARE un documento*/
  private void onDeleteDocumentCommit() {
    CancellazioneEcRequest req = new CancellazioneEcRequest();

    String codUtente = "";
    if (filteredDsDocumento.findUtenteSel() != null)
      codUtente = filteredDsDocumento.findUtenteSel().getCodUtente();
    req.setCodiceUtente(codUtente);
    // Nota: questo è il codUtenteEnte es: "99999" e non il codUtente="ANE00000123"
    req.setCodiceEnte(editDocumento.getCodUtenteEnte());

    req.setTipoUfficio(editDocumento.getTipoUfficio());
    req.setCodiceUfficio(editDocumento.getCodiceUfficio());

    req.setImpostaServizio(editDocumento.getCodImpostaServizio());
    req.setNumeroDocumento(editDocumento.getDocumento().getNumeroDocumento());

    //  Fisso: "Entrate Patrimoniali"
    String tipoServizio = "EP";
    req.setTipoServizio(tipoServizio);
    req.setImpostaServizio(editDocumento.codImpostaServizio);
  

//    Gson gson = EntrateUtils.getGson();
//    System.out.println("CancellazioneEcRequest: " + gson.toJson(req));
    
    try {
      CancellazioneEcResponse wsResponse = EntrateFilteredWs
          .deleteDocumentoCarico(req, request);
      logger.setLevel(Level.ALL);
      logger.debug(wsResponse.getMessaggioEsito());

      String esito = wsResponse.getCodiceEsito();
      final String RESP_ESITO_POSITIVO = "00";

      String messEsito = wsResponse.getMessaggioEsito();
      if (RESP_ESITO_POSITIVO.equals(esito)) {
        // Esito Positivo senza errori/segnalazioni
        logger.debug(String.format("cancellato documento: esito=%s, mess=%s",  esito,
            messEsito));
        setMessage(messEsito);

        // nella JSP il button ELIMINA non lo visualizzo più 
        editDocumento.mode = Mode.READONLY;
      } else {
        setErrorMessage(wsResponse.getMessaggioEsito());
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /** Mi trovo nello screen di ADD o EDIT documento.*/
  void onAddOrEditDocumentoScreen(boolean update) {

    try {
      EntrateFilteredWs.loadListaProvince(request);

      // se nella request ci sono dati di Edit Documento...
      //inizio LP PG200360
      //if (request.getParameter("anagrafica_codiceFiscale") != null) {
      if (request.getParameter("anagrafica_codiceFiscale") != null || editDocumento.modalitaAggiornamento) {
      //fine LP PG200360
        try {
        	onEditDocumentoTestataChanged(update);
            onEditDocumentoDataChanged();
        } catch (ConfigurazioneException ex) {
        	ex.printStackTrace();
        	setFormValidationMessage("frmAction", "Impossibile recuperare Id Dominio/AuxDigit/Application Code/Segregation Code");
        	//return;
        }
       
        String retMessage = "";
        retMessage = checkConfigurazioneIUV(editDocumento);
        if (retMessage.trim().length()>0) {
        	setFormValidationMessage("frmAction", retMessage);
        }
      }

      if(request.getAttribute(ValidationContext.getInstance().getValidationMessage())!=null && 
    		  !request.getAttribute(ValidationContext.getInstance().getValidationMessage()).equals("")){
    	  return;
      }
      
      // aggiungo/elimino Scadenza
      if (isFiredButton(request, "button_add_scadenza")) {
        editDocumento.listaScadenze.add(new Scadenza());
      }
      if (isFiredButton(request, "button_delete_scadenza")) {
        int scad_delete_row = -1;
        scad_delete_row = Integer.parseInt(request.getParameter("button_delete_scadenza"));
        editDocumento.listaScadenze.remove(scad_delete_row);
      }

      // aggiungo/elimino Tributo
      if (isFiredButton(request, "button_add_tributo")) {
        editDocumento.listaTributi.add(new Tributo());
      }
      if (isFiredButton(request, "button_delete_tributo")) {
        int tributo_delete_row = -1;
        tributo_delete_row = Integer.parseInt(request.getParameter("button_delete_tributo"));
        editDocumento.listaTributi.remove(tributo_delete_row);
      }

      if (isFiredButton(request, "tx_button_indietro")) {
        screen = Screen.SEARCH;
      }

    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

    if (screen == Screen.ADD && isFiredButton(request, "tx_button_save")) {
      onSaveDocumentoCommit();
    } else if (screen == Screen.EDIT && isFiredButton(request, "tx_button_save")) {
      onUpdateDocumentoCommit();
    } 
    //inizio LP PG200360
    else {
	   if(isFiredButton(request, "tx_button_flagstampaavviso_changed")) {
		   String flagStampaAvviso = request.getParameter("configurazione_flagStampaAvviso");
		   editDocumento.configurazione.setFlagStampaAvviso(flagStampaAvviso);
		   if(editDocumento.mode != Mode.CREATE) {
			   if(flagStampaAvviso.equals("N")) {
				   if(!editDocumento.modalitaAggiornamento) {
					   if(editDocumento.stampaAvvisoEseguita) {
						   //TODO: se passo da Y a N eseguo reset e ripristino dati da DB solo qui?
						   Reset();
						   editDocumento.modalitaAggiornamento = true;
					   }
				   }
			   } else {
				   if(editDocumento.modalitaAggiornamento) {
					   if(editDocumento.stampaAvvisoEseguita) {
						   editDocumento.modalitaAggiornamento = false;
						   String flagGenerazioneIUV = editDocumento.configurazione.getFlagGenerazioneIUV();
						   if(!flagGenerazioneIUV.equals("N")) {
							   editDocumento.documento.setNumeroBollettinoPagoPA("");
							   editDocumento.documento.setIdentificativoUnivocoVersamento("");
							   for (Scadenza scad : editDocumento.listaScadenze) {
								   scad.setNumeroBollettinoPagoPA("");
								   scad.setIdentificativoUnivocoVersamento("");
							   }
						   }
					   }
				   }
			   }
		   }
	   }
	   if(isFiredButton(request, "tx_button_flagGenerazioneIUV_changed")) {
		   String flagGenerazioneIUV = request.getParameter("configurazione_flagGenerazioneIUV");
		   editDocumento.configurazione.setFlagGenerazioneIUV(flagGenerazioneIUV);
		   if(editDocumento.mode != Mode.CREATE) {
			   if(flagGenerazioneIUV.equals("N")) {
				   StatoEditDocumento docOLD = (StatoEditDocumento) request.getSession().getAttribute("editDocumentoOLD");
				   String numeroBollettinoPagoPA = docOLD.documento.getNumeroBollettinoPagoPA();
				   String identificativoUnivocoVersamento = docOLD.documento.getIdentificativoUnivocoVersamento();
				   editDocumento.documento.setNumeroBollettinoPagoPA(numeroBollettinoPagoPA);
				   editDocumento.documento.setIdentificativoUnivocoVersamento(identificativoUnivocoVersamento);
				   int i = 0;
				   ArrayList<Scadenza> scadOLD = docOLD.getListaScadenze();
				   for (Scadenza scad : editDocumento.listaScadenze) {
					   numeroBollettinoPagoPA = scadOLD.get(i).getNumeroBollettinoPagoPA();
					   identificativoUnivocoVersamento = scadOLD.get(i).getIdentificativoUnivocoVersamento();
					   scad.setNumeroBollettinoPagoPA(numeroBollettinoPagoPA);
					   scad.setIdentificativoUnivocoVersamento(identificativoUnivocoVersamento);
					   i++;
				   }
			   } else {
				   editDocumento.documento.setNumeroBollettinoPagoPA("");
				   editDocumento.documento.setIdentificativoUnivocoVersamento("");
				   for (Scadenza scad : editDocumento.listaScadenze) {
					   scad.setNumeroBollettinoPagoPA("");
					   scad.setIdentificativoUnivocoVersamento("");
				   }
			   }
		   }
	   }
    }
   //fine LP PG200360
    
  }

  /** Mi trovo nello screen di CONFERMA CANCELAZIONE documento */
  void onDeleteDocumentoScreen() {

    try {
      EntrateFilteredWs.loadListaProvince(request);

      if (isFiredButton(request, "tx_button_indietro")) {
        screen = Screen.SEARCH;
      }

      if (isFiredButton(request, "tx_button_delete")) {
        onDeleteDocumentCommit();
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }

  }
  
  //PG200450 GG 10032021 - inizio
  /** Mi chiedono di entrare in EDIT di un documento*/
  private void onStampaDocumentRequest() {
    	try {
		
			RichiestaAvvisoPagoPaResponse res;
			RichiestaAvvisoPagoPaRequest req = new RichiestaAvvisoPagoPaRequest();
			req.setCodiceUtente(request.getParameter("codUtente"));
			req.setTipoServizio(request.getParameter("tipoServizio"));
			req.setCodiceEnte(request.getParameter("codUtenteEnte"));
			req.setTipoUfficio(request.getParameter("codTipoUfficio"));
			req.setCodiceUfficio(request.getParameter("codUfficio"));
			req.setImpostaServizio(request.getParameter("codImpostaServizio"));
			
			StampaDocumento stampaDocumento = new StampaDocumento();
			stampaDocumento.setCodiceFiscale(request.getParameter("codiceFiscale"));
			stampaDocumento.setNumeroDocumento(request.getParameter("numeroDocumento"));
			String impResiduo = (String) request.getParameter("impResiduo");
			if (!impResiduo.contains("."))
				impResiduo = impResiduo.concat(".00");
			stampaDocumento.setImportoTotaleDocumento(impResiduo.replace(".",""));	//TODO da verificare come fare formattazione
			stampaDocumento.setFlagDatiAttualizzati("Y");
			req.setStampaDocumento(stampaDocumento);
	
			WSCache.logWriter.logDebug("Pagina interrogazione Richiesta Stampa Avviso PagoPA");
			res = WSCache.integraEcDifferitoServer.stampaAvvisoPagoPA(req, request, EntrateUtils.getCodSocieta(request));
			WSCache.logWriter.logDebug("Fine Richiesta Stampa Avviso PagoPA");
			if (res!=null) {
				if (res.getCodiceEsito().equals("00")) {
//					String targetPdfNome=EntrateUtils.getCodSocieta(request)+"_"+res.getStampaDocumento().getNumeroDocumento()+".pdf";
//					String targetPdfPath = getPathStampaAvviso(EntrateUtils.getCodSocieta(request));
//					String targetPdf = targetPdfPath + File.separator + targetPdfNome;
					String targetPdf=null;
					if (req.getStampaDocumento().getPathStampaDocumento()!=null && req.getStampaDocumento().getPathStampaDocumento().trim().length()>0)
						targetPdf = req.getStampaDocumento().getPathStampaDocumento();
					else {
						if (res.getStampaDocumento()!=null) 
							if (res.getStampaDocumento().getPathStampaDocumento()!=null && res.getStampaDocumento().getPathStampaDocumento().trim().length()>0) 
								targetPdf = res.getStampaDocumento().getPathStampaDocumento();
					}
					if (targetPdf !=null) {
						File targetFile = new File(targetPdf);
						if (targetFile.exists())
							targetFile.delete();
									
						FileOutputStream fos = new FileOutputStream(targetFile);
						fos.write(res.getStampaPDFDocumentoPagoPA());
						fos.close();
						screen=Screen.STAMPA;
						request.setAttribute("pdfAvviso", targetPdf);
					} else {
						setFormValidationMessage("gestioneDocumentiCaricoForm", "Non è stato possibile produrre la stampa dell'avviso per documento " + stampaDocumento.getNumeroDocumento() + " codice fiscale " + stampaDocumento.getCodiceFiscale() + ".");
					}
				} else {
					//Visualizzo a video il messaggio
					setFormValidationMessage("gestioneDocumentiCaricoForm", "Si sono riscontrati problemi nella stampa dell'avviso per documento " + stampaDocumento.getNumeroDocumento() + " codice fiscale " + stampaDocumento.getCodiceFiscale() + ": "
				            + res.getMessaggioEsito()==null?"":res.getMessaggioEsito());
				}
			} else {
				setFormValidationMessage("gestioneDocumentiCaricoForm", "Si sono riscontrati problemi nella richiesta di stampa dell'avviso per documento " + stampaDocumento.getNumeroDocumento() + " codice fiscale " + stampaDocumento.getCodiceFiscale() + ".");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			WSCache.logWriter.logError("errore stampaAvvisoPagoPA: " + ex.getMessage(), ex);
			setFormValidationMessage("gestioneDocumentiCaricoForm", "Si sono verificati errori nella stampa dell'avviso.");
		}
  }
  //PG200450 GG 10032021 - fine

  /** inizializzo oppure aggiorno i dropdown in testata documento (selezione societa/utente/ente)
   * che determinano anche UtenteEnte, codiceUfficio,tipoUfficio.
   * Nota i campi disabled non tornano indietro nel form, me li trovo a null */
  private void onEditDocumentoTestataChanged(boolean update) throws ConfigurazioneException {

    EventoFiltro testataEvent = new EventoFiltro();
    testataEvent.keyEnte = request.getParameter("tx_soc_utente_ente");
    testataEvent.keyTipologiaServizio = request.getParameter("tx_tipologia_servizio");
    testataEvent.keyImpostaServizio = request.getParameter("tx_imposta_servizio");

    testataEvent.tx_ente_changed = isFiredButton(request, "tx_soc_utente_ente_changed");
    testataEvent.tx_tipologia_servizio_changed = isFiredButton(request,
        "tx_button_tipologia_servizio_changed");
    //inizio LP PG200360
    testataEvent.tx_button_flagstampaavviso_changed = isFiredButton(request, "tx_button_flagstampaavviso_changed");
    //fine LP PG200360
    testataEvent.update = update;

    testataEvent.filtroReset = filtroReset;

    filteredDsDocumento.setRequestContext(request);
    filteredDsDocumento.trySetFilter(testataEvent);

    // valori validati per il rendering client JSP
    //editDocumento.keyEnte = filteredDsDocumento.getKeyEnte();
    if(filtroDocumenti.getKeyEnte().trim().length()>0) {
    	editDocumento.keyEnte = filtroDocumenti.getKeyEnte();
    } else {
    	//inizio LP PG200360
    	if((screen == Screen.SEARCH) || (testataEvent.keyEnte != null)) {
    	//fine LP PG200360
    	editDocumento.keyEnte = testataEvent.keyEnte;
    	//inizio LP PG200360
    	}
    	//fine LP PG200360
    }
    editDocumento.codUtenteEnte = filteredDsDocumento.getCodUtenteEnte();
    editDocumento.tipoUfficio = filteredDsDocumento.getTipoUfficio();
    editDocumento.codiceUfficio = filteredDsDocumento.getCodUfficio();
    editDocumento.configurazioneIUV.setIdentificativoDominio(filteredDsDocumento.getIdDominio());
    editDocumento.configurazioneIUV.setAuxDigit(filteredDsDocumento.getAuxDigit());
    editDocumento.configurazioneIUV.setApplicationCode(filteredDsDocumento.getApplicationCode());
    editDocumento.configurazioneIUV.setSegregationCode(filteredDsDocumento.getSegregationCode());
    editDocumento.configurazioneIUV.setCarattereServizio(filteredDsDocumento.getCarattereServizio());
    if(!update)
    	editDocumento.configurazione.setIdentificativoFlusso(filteredDsDocumento.getIdentificativoFlusso());
  }

  private void onUpdateListaProvinciaFiscale() throws FaultType, RemoteException {

    if (Strings.isNullOrEmpty(anagrafica_siglaProvinciaFiscale)) {
      request.getSession().removeAttribute("listBelfioreFiscale");
    } else {
      loadBelfiore(anagrafica_siglaProvinciaFiscale, "listBelfioreFiscale", request);
      editDocumento.anagrafica.setCodiceBelfioreComuneFiscale(null);
      anagrafica_codiceBelfioreComuneFiscaleInvalid = true;
    }
  }

  private void onUpdateListaProvinciaNascita() throws FaultType, RemoteException {
    if (Strings.isNullOrEmpty(anagrafica_siglaProvinciaNascita))
      request.getSession().removeAttribute("listBelfioreNascita");
    else {
      loadBelfiore(anagrafica_siglaProvinciaNascita, "listBelfioreNascita", request);
      editDocumento.anagrafica.setCodiceBelfioreComuneNascita(null);
      anagrafica_codiceBelfioreComuneNascitaInvalid = true;
    }
  }

  boolean anagrafica_codiceBelfioreComuneFiscaleInvalid = false;
  boolean anagrafica_codiceBelfioreComuneNascitaInvalid = false;

  /** mi sono arivati i nuovi dati da una post del form. Nota: Se ci sono campi Disabled (es: in modifica)
   * questi non mi vengono postati come attributi (quindi null) */
  private void onEditDocumentoDataChanged() {

    try {
      // lista comuni provincia di nascita
      if (isFiredButton(request, "tx_button_provincia_nascita_changed")) {
        anagrafica_siglaProvinciaNascita = request.getParameter("anagrafica_siglaProvinciaNascita");
        onUpdateListaProvinciaNascita();
      }

      // lista comuni provincia domicilio fiscale
      if (isFiredButton(request, "tx_button_provincia_fiscale_changed")) {
        anagrafica_siglaProvinciaFiscale = request.getParameter("anagrafica_siglaProvinciaFiscale");
        onUpdateListaProvinciaFiscale();
      }
    } catch (Exception e1) {
      throw new RuntimeException(e1);
    }

    //inizio LP PG200360
    if(!editDocumento.modalitaAggiornamento) {
    //fine LP PG200360
    // Ruolo tipolofiaServizio / impostaServizio
    if (request.getParameter("tx_imposta_servizio_codice") != null)
      editDocumento.codImpostaServizio = request.getParameter("tx_imposta_servizio_codice");
    else
      ; // invariato

    editDocumento.ruolo.setDescrizioneImpostaServizio(request
        .getParameter("tx_imposta_servizio_desc"));

    if (request.getParameter("tx_tipologia_servizio_codice") != null)
      editDocumento.ruolo.setCodiceTipologiaServizio(request
          .getParameter("tx_tipologia_servizio_codice"));

    editDocumento.ruolo.setDescrizioneTipologiaServizio(request
        .getParameter("tx_tipologia_servizio_desc"));
    //inizio LP PG200360
    }
    //fine LP PG200360


    // Configurazione
    Configurazione configurazione = editDocumento.configurazione;
    //inizio LP PG200360
    //inizio LP PG210130
    String idDominioTestata = "";
    //fine LP PG210130
    if(!editDocumento.modalitaAggiornamento) {
    //fine LP PG200360
    configurazione.setFlagGenerazioneIUV(request.getParameter("configurazione_flagGenerazioneIUV"));
    configurazione.setFlagStampaAvviso(request.getParameter("configurazione_flagStampaAvviso"));
//    if (request.getParameter("configurazione_identificativoFlusso")!=null && request.getParameter("configurazione_identificativoFlusso").trim().length()>0) 
    //configurazione.setIdentificativoFlusso(request.getParameter("configurazione_identificativoFlusso"));

    // ConfigurazioneIUV
    if (!configurazione.getFlagGenerazioneIUV().equals("Y")) {
    	ConfigurazioneIUV configurazioneIUV = editDocumento.configurazioneIUV;
        //inizio LP PG210130
    	idDominioTestata = configurazioneIUV.getIdentificativoDominio();
        //fine LP PG210130
//    	configurazioneIUV.setIdentificativoDominio("");
//    	configurazioneIUV.setAuxDigit("");
//    	configurazioneIUV.setApplicationCode("");
//    	configurazioneIUV.setSegregationCode("");
    	
    	configurazione.setConfigurazioneIUV(null);
    	
    } else {
    	ConfigurazioneIUV configurazioneIUV = editDocumento.configurazioneIUV;
        //inizio LP PG210130
    	idDominioTestata = configurazioneIUV.getIdentificativoDominio();
        //fine LP PG210130
    	System.out.println("caratt servizio: "+configurazioneIUV.getCarattereServizio());
//    	configurazioneIUV.setIdentificativoDominio(request.getParameter("iuv_identificativoDominio"));
//    	configurazioneIUV.setAuxDigit(request.getParameter("iuv_auxDigit"));
//    	configurazioneIUV.setApplicationCode(request.getParameter("iuv_applicationCode"));
//    	configurazioneIUV.setSegregationCode(request.getParameter("iuv_segregationCode"));
    	configurazioneIUV.setCarattereServizio(request.getParameter("iuv_carattereServizio"));
    	configurazione.setConfigurazioneIUV(configurazioneIUV);
    }
    //inizio LP PG200360
    }
    //fine LP PG200360

    // Documento
    Documento documento = editDocumento.documento;
    //inizio LP PG200360
    if(!editDocumento.modalitaAggiornamento) {
    //fine LP PG200360
    if (request.getParameter("documento_numeroDocumento") != null)
      documento.setNumeroDocumento(request.getParameter("documento_numeroDocumento"));
    documento.setAnnoEmissione(request.getParameter("documento_annoEmissione"));
    documento.setNumeroEmissione(request.getParameter("documento_numeroEmissione"));
    documento.setCausale(request.getParameter("documento_causale")); //PG180020 CT
  
    documento.setDataNotifica(EntrateUtils.dateDDMMYYYYFromFields(request, "documento_dataNotifica"));

    //marini
    if (!configurazione.getFlagGenerazioneIUV().equals("Y")) {
    	documento.setNumeroBollettinoPagoPA(request.getParameter("documento_numeroBollettinoPagoPA"));
    } else {
    	documento.setNumeroBollettinoPagoPA("");
    }
//	documento.setNumeroBollettinoPagoPA(request.getParameter("documento_numeroBollettinoPagoPA"));
    //inizio LP PG200360
    }
    //fine LP PG200360


    String impBollettino = request.getParameter("documento_impBollettinoTotaleDocumento");
    if (impBollettino.equals(""))
      documento.setImpBollettinoTotaleDocumento(null);
    else {
//      try {
//        double d = df.parse(impBollettino.replace(".", ",")).doubleValue();
//        documento.setImpBollettinoTotaleDocumento(BigDecimal.valueOf(d));
////    	documento.setImpBollettinoTotaleDocumento(new BigDecimal(Double.toString(d)));	//GG probabilmente più preciso
//      } catch (ParseException e) {
//        documento.setImpBollettinoTotaleDocumento(null);
//      }
    	try {
    	  BigDecimal bd = new BigDecimal(impBollettino.replace(",", ".")).setScale(2);
          documento.setImpBollettinoTotaleDocumento(bd);
        } catch (NumberFormatException e) {
          documento.setImpBollettinoTotaleDocumento(null);
        }
    }
    //inizio LP PG200360
    if(!editDocumento.modalitaAggiornamento) {
    //fine LP PG200360
    documento.setIbanAccredito(request.getParameter("documento_ibanAccredito"));
    documento.setIbanAppoggio(request.getParameter("documento_ibanAppoggio"));
    documento.setFlagFatturazioneElettronica(request
        .getParameter("documento_flagFatturazioneElettronica"));

    //marini
    if (!configurazione.getFlagGenerazioneIUV().equals("Y")) {
    	documento.setIdentificativoUnivocoVersamento(request.getParameter("documento_identificativoUnivocoVersamento"));
    } else {
    	documento.setIdentificativoUnivocoVersamento("");
    }
//    documento.setIdentificativoUnivocoVersamento(request
//        .getParameter("documento_identificativoUnivocoVersamento"));
    //inizio LP PG200360
    }
    //fine LP PG200360

    //inizio LP PG200360
    documento.setTassonomia(request.getParameter("documento_tassonomia"));
    //fine LP PG200360

    // Anagrafica
    Anagrafica anagrafica = editDocumento.anagrafica;

    //inizio LP PG200360
    if(!editDocumento.modalitaAggiornamento) {
    //fine LP PG200360
    if (!anagrafica_codiceBelfioreComuneFiscaleInvalid)
      anagrafica.setCodiceBelfioreComuneFiscale(request
          .getParameter("anagrafica_codiceBelfioreComuneFiscale"));

    if (!anagrafica_codiceBelfioreComuneNascitaInvalid)
      anagrafica.setCodiceBelfioreComuneNascita(request
          .getParameter("anagrafica_codiceBelfioreComuneNascita"));

    anagrafica.setCodiceFiscale(request.getParameter("anagrafica_codiceFiscale"));

    anagrafica.setDataNascita(EntrateUtils
        .dateDDMMYYYYFromFields(request, "anagrafica_dataNascita"));

    anagrafica.setDenominazione(request.getParameter("anagrafica_denominazione"));
    
    //PRE_JAVA1.8 SB - inizio
    boolean bValid = false;
    Error error = new Error();
    if(request.getParameter("anagrafica_email")!=null && !request.getParameter("anagrafica_email").equals("")){
    	bValid = checkValiditaMail(request,request.getParameter("anagrafica_email"),error);
		if(!bValid && !error.isErrorvisible()){
			error.addErrorvalue("L'email "+request.getParameter("anagrafica_email")+" non è valida");
			setFormValidationMessage("frmAction", "L'email "+request.getParameter("anagrafica_email")+" non è valida");
		}else if(error.isErrorvisible()){
			setFormValidationMessage("frmAction", error.getErrorvalue());
		}
    }
    
    if(request.getParameter("anagrafica_emailPec")!=null && !request.getParameter("anagrafica_emailPec").equals("")){
    	bValid = checkValiditaMail(request,request.getParameter("anagrafica_emailPec"),error);
		if(!bValid && !error.isErrorvisible()){
			error.addErrorvalue("L'email "+request.getParameter("anagrafica_emailPec")+" non è valida");
			setFormValidationMessage("frmAction", "L'email PEC "+request.getParameter("anagrafica_emailPec")+" non è valida");
		}else if(error.isErrorvisible()){
			setFormValidationMessage("frmAction", error.getErrorvalue());
		}
    }
    //PRE_JAVA1.8 SB - fine
    
    anagrafica.setEmail(request.getParameter("anagrafica_email"));
    anagrafica.setEmailPec(request.getParameter("anagrafica_emailPec"));

    anagrafica.setIndirizzoFiscale(request.getParameter("anagrafica_indirizzoFiscale"));
    anagrafica.setTipoAnagrafica(request.getParameter("anagrafica_tipoAnagrafica"));
    //inizio LP PG200360
    }
    //fine LP PG200360

    // righe tributi
    int i = 0;
    for (Tributo trib : editDocumento.listaTributi) {
      //inizio LP PG200360
      if(!editDocumento.modalitaAggiornamento) {
      //fine LP PG200360
      trib.setAnnoTributo(request.getParameter(String.format("trib_%d_annoTributo", i)));
      String progressivoTributo = request.getParameter(String.format("trib_%d_progressivoTributo",
          i));
      if (progressivoTributo.equals("")) {
        trib.setProgressivoTributo(0);
      } else { // gestione parseEception? non serve, convalida il framework
        trib.setProgressivoTributo(Integer.parseInt(progressivoTributo));
      }
      trib.setCodiceTributo(request.getParameter(String.format("trib_%d_codiceTributo", i)));
      //inizio LP PG200360
      }
      //fine LP PG200360

      String impTributo = request.getParameter(String.format("trib_%d_impTributo", i));
      if (impTributo.equals(""))
        trib.setImpTributo(null);
      else {
//        try {
//          double d = df.parse(impTributo.replace(".", ",")).doubleValue();
//          trib.setImpTributo(BigDecimal.valueOf(d));
//        } catch (ParseException e) {
//          trib.setImpTributo(null);
//        }
    	  try {
    		BigDecimal bd = new BigDecimal(impTributo.replace(",", ".")).setScale(2);
    	  	trib.setImpTributo(bd);
	      } catch (NumberFormatException e) {
	       	trib.setImpTributo(null);
	      }
      }
      //inizio LP PG200360
      if(!editDocumento.modalitaAggiornamento) {
      //fine LP PG200360
      trib.setCodiceCapitolo(request.getParameter(String.format("trib_%d_codiceCapitolo", i)));
      trib.setAccertamento(request.getParameter(String.format("trib_%d_accertamento", i)));
      //inizio LP PG210130
      trib.setArticolo(request.getParameter(String.format("trib_%d_articolo", i)));
      String idDominio = request.getParameter(String.format("trib_%d_identificativoDominio", i));
      boolean bEnteDiverso = false;
      if(idDominio != null) {
    	  idDominio = idDominio.trim();
          if(idDominio.length() > 0)
        	  bEnteDiverso = !idDominio.equals(idDominioTestata);
      }
      trib.setIdentificativoDominio(idDominio);
      String ibanBancario = request.getParameter(String.format("trib_%d_ibanBancario", i)); 
      if(ibanBancario != null)
    	  ibanBancario = ibanBancario.trim();
      trib.setIBANBancario(ibanBancario);
      if(bEnteDiverso && (ibanBancario == null || ibanBancario.length() == 0)) {
		setFormValidationMessage("frmAction", "Non è stato valorizzato l'iban bancario per il tributo #" + (i + 1));
      }
      trib.setIBANPostale(request.getParameter(String.format("trib_%d_ibanPostale", i)));
      //inizio SB PGNTMGR-8
      trib.setMetadatiPagoPATariTefaKey(request.getParameter(String.format("trib_%d_tariTefaKey", i)));
      trib.setMetadatiPagoPATariTefaValue(request.getParameter(String.format("trib_%d_tariTefaValue", i)));
      //fine SB PGNTMGR-8
      //fine LP PG210130
      trib.setNoteTributo(request.getParameter(String.format("trib_%d_noteTributo", i)));
      //inizio LP PG200360
      //inizio LP PG22XX05
      String codiceTipologiaServizio = request.getParameter(String.format("trib_%d_codiceTipologiaServizio", i));
      if(codiceTipologiaServizio != null)
    	  codiceTipologiaServizio = codiceTipologiaServizio.trim();
      trib.setCodiceTipologiaServizio(codiceTipologiaServizio);
      if(bEnteDiverso && (codiceTipologiaServizio == null || codiceTipologiaServizio.length() == 0)) {
		setFormValidationMessage("frmAction", "Non è stato valorizzato il codice tipologia servizio per il tributo #" + (i + 1));
      }
      //fine LP PG22XX05
      }
      //fine LP PG200360

      i++;
    }

    // righe scadenze
    i = 0;
    for (Scadenza scad : editDocumento.listaScadenze) {
      //inizio LP PG200360
      if(!editDocumento.modalitaAggiornamento) {
      //fine LP PG200360
      scad.setDataScadenzaRata(EntrateUtils.dateDDMMYYYYFromFields(request, String.format(
          "scad_%d_dataScadenzaRata", i)));
      //marini
      if (!configurazione.getFlagGenerazioneIUV().equals("Y")) {
          scad.setNumeroBollettinoPagoPA(request.getParameter(String.format("scad_%d_numeroBollettinoPagoPA", i)));
      } else {
    	  scad.setNumeroBollettinoPagoPA("");
      }
//      scad.setNumeroBollettinoPagoPA(request.getParameter(String.format(
//          "scad_%d_numeroBollettinoPagoPA", i)));
      //inizio LP PG200360
      }
      //fine LP PG200360

      String impBollettinoRata = request
          .getParameter(String.format("scad_%d_impBollettinoRata", i));
      if (impBollettinoRata.equals(""))
        scad.setImpBollettinoRata(null);
      else {
//        try {
//          double d = df.parse(impBollettinoRata.replace(".", ",")).doubleValue();
//          scad.setImpBollettinoRata(BigDecimal.valueOf(d));
//        } catch (ParseException e) {
//          scad.setImpBollettinoRata(null);
//        }
    	  try {
      		BigDecimal bd = new BigDecimal(impBollettinoRata.replace(",", ".")).setScale(2);
      		scad.setImpBollettinoRata(bd);
  	      } catch (NumberFormatException e) {
  	    	scad.setImpBollettinoRata(null);
  	      }
      }
      //inizio LP PG200360
      if(!editDocumento.modalitaAggiornamento) {
      //fine LP PG200360
      //marini
      if (!configurazione.getFlagGenerazioneIUV().equals("Y")) {
          scad.setIdentificativoUnivocoVersamento(request.getParameter(String.format("scad_%d_identificativoUnivocoVersamento", i)));
      } else {
    	  scad.setIdentificativoUnivocoVersamento("");
      }
//      scad.setIdentificativoUnivocoVersamento(request.getParameter(String.format(
//          "scad_%d_identificativoUnivocoVersamento", i)));
      scad.setNumeroRata(i + 1);
      //inizio LP PG200360
      }
      //fine LP PG200360
      i++;
    }
  }

  /** Mi ha chiesto di salvare i dati sul DB... ci provo direttamente... senza intromettermi con una mia
   * validazone dei dati.
   * Riporto il messaggio di errore sul web client. */
  void onSaveDocumentoCommit() {

    try {
      // Questa è la request da inviare al webservice 
      InserimentoEcRequest inserimentoEcRequest = new InserimentoEcRequest();
      String codUtente = "";
      if (filteredDsDocumento.findUtenteSel() != null)
        codUtente = filteredDsDocumento.findUtenteSel().getCodUtente();
      inserimentoEcRequest.setCodiceUtente(codUtente);
      // Nota: questo è il codUtenteEnte es: "99999" e non il codUtente="ANE00000123"
      inserimentoEcRequest.setCodiceEnte(editDocumento.getCodUtenteEnte());

      inserimentoEcRequest.setTipoUfficio(editDocumento.getTipoUfficio());
      inserimentoEcRequest.setCodiceUfficio(editDocumento.getCodiceUfficio());

      //  Fisso: "Entrate Patrimoniali"
      String tipoServizio = "EP";
      inserimentoEcRequest.setTipoServizio(tipoServizio);
      inserimentoEcRequest.setImpostaServizio(editDocumento.codImpostaServizio);
      inserimentoEcRequest.setConfigurazione(editDocumento.getConfigurazione());

      inserimentoEcRequest.setRuolo(editDocumento.getRuolo());
      inserimentoEcRequest.setDocumento(editDocumento.getDocumento());
      inserimentoEcRequest.setAnagrafica(editDocumento.getAnagrafica());
      inserimentoEcRequest.setListTributi(editDocumento.getListaTributi().toArray(new Tributo[] {}));
      inserimentoEcRequest.setListScadenze(editDocumento.getListaScadenze().toArray(new Scadenza[] {}));

//      Gson gson = EntrateUtils.getGson();
//      System.out.println("inserimentoEcRequest: " + gson.toJson(inserimentoEcRequest));
      
      InserimentoEcResponse wsResponse = EntrateFilteredWs.addDocumentoCarico(inserimentoEcRequest,request);
//      System.out.println("wsResponse: " + gson.toJson(wsResponse));

      String esito = wsResponse.getCodiceEsito();
      final String RESP_ESITO_POSITIVO = "00";
      final String RESP_ERRORE_GENERICO = "01";
      final String RESP_ERRORE_CONFIGURAZIONE = "02";
      final String RESP_ERRORE_POSIZIONE_DEBITORIA_GIA_PRESENTE = "03";
      final String RESP_ERRORE_VALIDAZIONE = "04";

      if (RESP_ESITO_POSITIVO.equals(esito)) {
        // Esito Positivo...
        setMessage("Documento aggiunto con successo");

        editDocumento.mode = Mode.READONLY;
      } else {
        // volendo si potrebbe diversificare in base al tipo di errore
        setFormValidationMessage("frmAction", wsResponse.getMessaggioEsito());
      }

    } catch (Exception e) {
    	e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /** mi chiedono di aggiornare il documento */
  void onUpdateDocumentoCommit() {
    try {
      // Questa è la request da inviare al webservice 
      VariazioneEcRequest req = new VariazioneEcRequest();
      String codUtente = "";
      if (filteredDsDocumento.findUtenteSel() != null)
        codUtente = filteredDsDocumento.findUtenteSel().getCodUtente();
      req.setCodiceUtente(codUtente);
      req.setCodiceEnte(editDocumento.getCodUtenteEnte());

      req.setTipoUfficio(editDocumento.getTipoUfficio());
      req.setCodiceUfficio(editDocumento.getCodiceUfficio());


      //  Fisso: "Entrate Patrimoniali"
      String tipoServizio = "EP";
      req.setTipoServizio(tipoServizio);
      req.setImpostaServizio(editDocumento.codImpostaServizio);
      req.setConfigurazione(editDocumento.getConfigurazione());

      req.setRuolo(editDocumento.getRuolo());
      req.setDocumento(editDocumento.getDocumento());
      req.setAnagrafica(editDocumento.getAnagrafica());
      req.setListTributi(editDocumento.getListaTributi().toArray(new Tributo[] {}));
      req.setListScadenze(editDocumento.getListaScadenze().toArray(new Scadenza[] {}));

//      Gson gson = EntrateUtils.getGson();
//      System.out.println("inserimentoEcRequest: " + gson.toJson(req));

      //marini Prima di richiamare il ws si accerta che i campi della tabella tributi e scadenze siano valorizzati correttamente
      int i = 0;
      boolean valid=true;
      //inizio LP PG22XX05
      String idDominioPrincipale = filteredDsDocumento.getIdDominio();
      //fine LP PG22XX05
      for (Tributo trib : editDocumento.listaTributi) {
        if (trib.getAnnoTributo().trim().equals("")){
            setFormValidationMessage("frmAction", "Anno tributo: Questo valore è obbligatorio");
            valid=false;
        }
        if (trib.getCodiceTributo().trim().equals("")){
            setFormValidationMessage("frmAction", "Codice Tributo: Questo valore è obbligatorio");
            valid=false;
        }
        if (trib.getImpTributo()==null){
            setFormValidationMessage("frmAction", "Importo Tributo: Questo valore è obbligatorio");
            valid=false;
        }
        //inizio LP PG210130
        if(trib.getIdentificativoDominio() != null && trib.getIdentificativoDominio().trim().length() > 0) {
        	if(trib.getIBANBancario() == null || trib.getIBANBancario().trim().length() == 0) {
    	        //inizio LP PG22XX05
        		if(!idDominioPrincipale.equals(trib.getIdentificativoDominio().trim())) {
            	//fine LP PG22XX05
	                setFormValidationMessage("frmAction", "Iban Bancario: Questo valore è obbligatorio");
	                valid=false;
	    	    //inizio LP PG22XX05
        		}
        	    //fine LP PG22XX05
        	}
        }
        //fine LP PG210130
        //inizio LP PG22XX05
        if(trib.getIdentificativoDominio() != null && trib.getIdentificativoDominio().trim().length() > 0) {
        	if(trib.getCodiceTipologiaServizio() == null || trib.getCodiceTipologiaServizio().trim().length() == 0) {
    	        //inizio LP PG22XX05
        		if(!idDominioPrincipale.equals(trib.getIdentificativoDominio().trim())) {
            	//fine LP PG22XX05
	                setFormValidationMessage("frmAction", "Codice Tipologia Servizio: Questo valore è obbligatorio");
	                valid=false;
	    	    //inizio LP PG22XX05
        		}
        	    //fine LP PG22XX05
        	}
        }
        //fine LP PG22XX05
        i++;
      }
      if (!valid){
    	  return;
      }
      i = 0;
      for (Scadenza scad : editDocumento.listaScadenze) {
          if (scad.getDataScadenzaRata()==null){
              setFormValidationMessage("frmAction", "Data Scadenza: Questo valore è obbligatorio");
              valid=false;
          }
          if (scad.getImpBollettinoRata()==null){
              setFormValidationMessage("frmAction", "Importo Boll. Rata: Questo valore è obbligatorio");
              valid=false;
          }
        i++;
      }
      
      //inizio LP PG210130
      //Nota. Mancava il return se valid == false
      if (!valid) {
    	  return;
      }
      //fine LP PG210130
      
      
      VariazioneEcResponse wsResponse = filteredDsDocumento.variazioneEC(req, request);
//      System.out.println("wsResponse: " + gson.toJson(wsResponse));

      String esito = wsResponse.getCodiceEsito();
      final String RESP_ESITO_POSITIVO = "00";

      if (RESP_ESITO_POSITIVO.equals(esito)) {
        // Esito Positivo...
        setMessage("Documento sostituito con successo");
        
        //marini
        EntrateFilteredWs.IdDocumento idDoc = parseIdDocumentoFromMarini();
    	onFormDocumentoDataLoad(idDoc);
  		screen = Screen.EDIT;
  		 
        editDocumento.mode = Mode.READONLY;
      } else {
        setFormValidationMessage("frmAction", wsResponse.getMessaggioEsito());
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  /** Imposto un messaggio nella lista degli errori di validazione. Es. a seguito
   * di una ricerca con esito vuoto. */
  void setFormValidationMessage(String formName, String message) {
    if (ValidationContext.getInstance().getValidationMessage() != null) {
      ValidationErrorMap vem = new ValidationErrorMap();
      vem.setForm(formName);

      ArrayList<ValidationMessage> messages = new ArrayList<ValidationMessage>();
      ValidationMessage validationMessage = new ValidationMessage("", "", message);
      messages.add(validationMessage);
      vem.setMessages(messages);

      request.setAttribute(ValidationContext.getInstance().getValidationMessage(), vem);
    }
  }

  void resetFormValidationMessage(HttpServletRequest request) {
    if (request.getAttribute(ValidationContext.getInstance().getValidationMessage()) != null)
      request.setAttribute(ValidationContext.getInstance().getValidationMessage(), null);
  }

  private void loadBelfiore(String siglaProvincia, String listbelfiore, HttpServletRequest request)
      throws FaultType, RemoteException {

    RecuperaBelfioreResponse getBelfioreRes = WSCache.commonsServer.recuperaBelfiore(
        new RecuperaBelfioreRequest(siglaProvincia), request);
    request.getSession().setAttribute(listbelfiore, getBelfioreRes.getListXml());
  }

  static PageInfo getPageInfo(com.seda.payer.bancadati.webservice.dati.PageInfo rpi,
      int rowsPerPages) {
    PageInfo pageInfo = new PageInfo();
    pageInfo.setFirstRow(rpi.getFirstRow());
    pageInfo.setLastRow(rpi.getLastRow());
    pageInfo.setNumPages(rpi.getNumPages());
    pageInfo.setNumRows(rpi.getNumRows());
    pageInfo.setPageNumber(rpi.getPageNumber());
    pageInfo.setRowsPerPage(rowsPerPages);
    return pageInfo;
  }


  private static String formatDate(java.util.Calendar data) {
    SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy");
    return dateformat.format(data.getTime());
  }

  private EntrateFilteredWs.IdDocumento parseIdDocumentoFromRequest() {
    EntrateFilteredWs.IdDocumento id = new EntrateFilteredWs.IdDocumento();

    // campi forniti dall'elenco documenti: link documento per edit/delete 
    id.codSocieta = request.getParameter("codSocieta");
    id.codUtente = request.getParameter("codUtente");
    // non è chiame primaria, lo ottengo in seguito dalla lettura es:"ANE00000123
//    id.codEnte = request.getParameter("codEnte");

    // fa parte della chiave del documento es:"99999""
    id.codUtenteEnte = request.getParameter("codUtenteEnte");

    id.codTipologiaServizio = request.getParameter("codTipologiaServizio");
    id.codImpostaServizio = request.getParameter("codImpostaServizio");

    id.codTipoUfficio = request.getParameter("codTipoUfficio");
    id.codUfficio = request.getParameter("codUfficio");

    id.numeroDocumento = request.getParameter("numeroDocumento");
    id.chiaveFlusso = request.getParameter("chiaveFlusso");

    id.chiaveTomb = request.getParameter("chiaveTomb");
    id.annoEmissione = request.getParameter("annoEmissione");
    id.numeroEmissione = request.getParameter("numeroEmissione");
    return id;
  }

  private EntrateFilteredWs.IdDocumento parseIdDocumentoFromMarini() {
	    EntrateFilteredWs.IdDocumento id = new EntrateFilteredWs.IdDocumento();

	    // campi forniti dall'elenco documenti: link documento per edit/delete 
	    String codUtente = "";
	    if (filteredDsDocumento.findUtenteSel() != null)
	    	codUtente = filteredDsDocumento.findUtenteSel().getCodUtente();
	    id.codUtente = codUtente;
	    id.codUtenteEnte = editDocumento.getCodUtenteEnte();

	    id.codSocieta = filteredDsDocumento.getKeyEnte().substring(0,5);

	    id.codTipologiaServizio = editDocumento.getCodTipologiaServizio();
	    id.codImpostaServizio = editDocumento.getCodImpostaServizio();

	    id.codTipoUfficio = editDocumento.getTipoUfficio();
	    id.codUfficio = editDocumento.getCodiceUfficio();

	    id.numeroDocumento = editDocumento.getDocumento().getNumeroDocumento();
	    id.chiaveFlusso = editDocumento.getChiaveFlusso();

	    id.chiaveTomb = "";
	    id.annoEmissione = editDocumento.getDocumento().getAnnoEmissione();
	    id.numeroEmissione = editDocumento.getDocumento().getNumeroEmissione();

	    return id;
	  }

  private String checkConfigurazioneIUV(StatoEditDocumento editDocumento) {
	  String retMessage = "";
	  
	  if(editDocumento.getKeyEnte()!=null && editDocumento.getKeyEnte().trim().length()>0) {
		  Configurazione configurazione = editDocumento.getConfigurazione();
		  String flagGenerazioneIUV = configurazione==null?"":configurazione.getFlagGenerazioneIUV();
		  if (flagGenerazioneIUV!=null && flagGenerazioneIUV.trim().equals("Y")) {
			  if (editDocumento.getConfigurazione().getConfigurazioneIUV().getIdentificativoDominio()!=null &&
				  !editDocumento.getConfigurazione().getConfigurazioneIUV().getIdentificativoDominio().trim().equals("")){
				  editDocumento.configurazioneIUV.setIdentificativoDominio(editDocumento.getConfigurazione().getConfigurazioneIUV().getIdentificativoDominio());
				  editDocumento.configurazioneIUV.setAuxDigit(editDocumento.getConfigurazione().getConfigurazioneIUV().getAuxDigit());
				  editDocumento.configurazioneIUV.setApplicationCode(editDocumento.getConfigurazione().getConfigurazioneIUV().getApplicationCode());
				  editDocumento.configurazioneIUV.setSegregationCode(editDocumento.getConfigurazione().getConfigurazioneIUV().getSegregationCode());
				  editDocumento.configurazioneIUV.setCarattereServizio(editDocumento.getConfigurazione().getConfigurazioneIUV().getCarattereServizio());
			  }
			  
			  ConfigurazioneIUV configurazioneIUV = editDocumento.getConfigurazioneIUV();
			  if (configurazioneIUV!=null) {
				  String identificativoDominio = configurazioneIUV.getIdentificativoDominio()==null?"":configurazioneIUV.getIdentificativoDominio().trim();
				  String auxDigit = configurazioneIUV.getAuxDigit()==null?"":configurazioneIUV.getAuxDigit().trim();
				  String applicationCode = configurazioneIUV.getApplicationCode()==null?"":configurazioneIUV.getApplicationCode().trim();
				  String segregationCode = configurazioneIUV.getSegregationCode()==null?"":configurazioneIUV.getSegregationCode().trim();
				  String carattereServizio = configurazioneIUV.getCarattereServizio()==null?"":configurazioneIUV.getCarattereServizio().trim();
				  if (identificativoDominio.length()==0)
					  retMessage = "Id Dominio non configurato per ente selezionato";
				  else if (auxDigit.length()==0)
					  retMessage = "AuxDigit non configurato per Id Dominio per ente selezionato";
				  else {
					  if (auxDigit.equals("0")) 
						  if (applicationCode.length()==0)
							  retMessage = "Application Code non configurato per Id Dominio e AuxDigit per ente selezionato";
					  else if (auxDigit.equals("3")) 
						  if (segregationCode.length()==0)
							  retMessage = "Segregation Code non configurato per Id Dominio e AuxDigit per ente selezionato";
				  }
			  } else {
				  retMessage = "Impossibile recuperare la configurazione IUV per ente selezionato";
			  }
		  }
	  }
	  return retMessage;
  }
  
  /** button cliccato direttamente o simulato da javascript su DDL changed */
  static boolean isFiredButton(HttpServletRequest request, String buttonName) {
    return request.getParameter(buttonName) != null
        || buttonName.equals(request.getParameter("fired_button_hidden"));
  }


  /** colonne e info extra per ogni tributo */
  static public class TributoExtra implements Serializable {
    private static final long serialVersionUID = 1L;
    public String impTributo;
    public String discaricoTributo;
    public String residuoTributo;

    public String getImpTributo() {
      return impTributo;
    }

    public String getDiscaricoTributo() {
      return discaricoTributo;
    }

    public String getResiduoTributo() {
      return residuoTributo;
    }

    public void setImpTributo(String impTributo) {
      this.impTributo = impTributo;
    }

    public void setDiscaricoTributo(String discaricoTributo) {
      this.discaricoTributo = discaricoTributo;
    }

    public void setResiduoTributo(String residuoTributo) {
      this.residuoTributo = residuoTributo;
    }
  }

  /** colonne e info extra per ogni scadenza */
  static public class ScadenzaExtra implements Serializable {
    private static final long serialVersionUID = 1L;
    String importoBollettinoRata;
    Calendar dataScadenzaRata;

    public String getImportoBollettinoRata() {
      return importoBollettinoRata;
    }

    public void setImportoBollettinoRata(String importoBollettinoRata) {
      this.importoBollettinoRata = importoBollettinoRata;
    }

    public Calendar getDataScadenzaRata() {
      return dataScadenzaRata;
    }

    public void setDataScadenzaRata(Calendar dataScadenzaRata) {
      this.dataScadenzaRata = dataScadenzaRata;
    }
  }

  /** Stato da salvare in sessione. Incapsula i mei dati. */
  static public class SessionState implements Serializable {
    static public final String KEY = "org.seda.payer.manager.entrate.actions.GestioneDocumentiCaricoAction.SessionState";
    private static final long serialVersionUID = 1L;
    String screen;

    EntrateFilteredWs filteredDsListaDocumenti;
    StatoFiltroDocumenti filtroDocumenti;
    StatoPagineDocumenti pagineDocumenti;

    public EntrateFilteredWs filteredDsDocumento;
    public StatoEditDocumento editDocumento;

    public String anagrafica_siglaProvinciaFiscale;
    public String anagrafica_siglaProvinciaNascita;
  }

  static public class StatoFiltroDocumenti implements Serializable {
    private static final long serialVersionUID = 1L;
    String codFiscale;
    String annoEmissione;
    String numEmissione;
    String numDocumento;
    String stato_documento;
    String numeroBollettino;
    String numeroIUV;

    boolean ddlSocietaDisabled;
    String keySocieta;
    boolean ddlUtenteDisabled;
    String keyUtente;

    boolean ddlEnteDisabled;
    public String getNumeroBollettino() {
		return numeroBollettino;
	}

	public void setNumeroBollettino(String numeroBollettino) {
		this.numeroBollettino = numeroBollettino;
	}

	public String getNumeroIUV() {
		return numeroIUV;
	}

	public void setNumeroIUV(String numeroIUV) {
		this.numeroIUV = numeroIUV;
	}

	String keyEnte;

    boolean ddlImpostaServizioDisabled;
    String keyImpostaServizio;

    String keyTipologiaServizio;

    public String getCodFiscale() {
      return codFiscale;
    }

    public void setCodFiscale(String codFiscale) {
      this.codFiscale = codFiscale;
    }


    public String getAnnoEmissione() {
      return annoEmissione;
    }

    public void setAnnoEmissione(String annoEmissione) {
      this.annoEmissione = annoEmissione;
    }

    public String getNumEmissione() {
      return numEmissione;
    }

    public void setNumEmissione(String numEmissione) {
      this.numEmissione = numEmissione;
    }


    public String getStato_documento() {
      return stato_documento;
    }

    public void setStato_documento(String statoDocumento) {
      stato_documento = statoDocumento;
    }

    public String getNumDocumento() {
      return numDocumento;
    }

    public void setNumDocumento(String numDocumento) {
      this.numDocumento = numDocumento;
    }

    public boolean isDdlSocietaDisabled() {
      return ddlSocietaDisabled;
    }

    public void setDdlSocietaDisabled(boolean ddlSocietaDisabled) {
      this.ddlSocietaDisabled = ddlSocietaDisabled;
    }

    public String getKeySocieta() {
      return keySocieta;
    }

    public void setKeySocieta(String keySocieta) {
      this.keySocieta = keySocieta;
    }

    public boolean isDdlUtenteDisabled() {
      return ddlUtenteDisabled;
    }

    public void setDdlUtenteDisabled(boolean ddlUtenteDisabled) {
      this.ddlUtenteDisabled = ddlUtenteDisabled;
    }

    public String getKeyUtente() {
      return keyUtente;
    }

    public void setKeyUtente(String keyUtente) {
      this.keyUtente = keyUtente;
    }

    public boolean isDdlEnteDisabled() {
      return ddlEnteDisabled;
    }

    public void setDdlEnteDisabled(boolean ddlEnteDisabled) {
      this.ddlEnteDisabled = ddlEnteDisabled;
    }

    public String getKeyEnte() {
      return keyEnte;
    }

    public void setKeyEnte(String keyEnte) {
      this.keyEnte = keyEnte;
    }

    public boolean isDdlImpostaServizioDisabled() {
      return ddlImpostaServizioDisabled;
    }

    public void setDdlImpostaServizioDisabled(boolean ddlImpostaServizioDisabled) {
      this.ddlImpostaServizioDisabled = ddlImpostaServizioDisabled;
    }

    public String getKeyImpostaServizio() {
      return keyImpostaServizio;
    }

    public void setKeyImpostaServizio(String keyImpostaServizio) {
      this.keyImpostaServizio = keyImpostaServizio;
    }

    public String getKeyTipologiaServizio() {
      return keyTipologiaServizio;
    }

    public void setKeyTipologiaServizio(String keyTipologiaServizio) {
      this.keyTipologiaServizio = keyTipologiaServizio;
    }

  }
  /** Paginazione documenti. Salvato in sessione. */
  static public class StatoPagineDocumenti implements Serializable {
    public boolean changed;
    private static final long serialVersionUID = 1L;

    public StatoPagineDocumenti() {}

    // paginazione
    int rowsPerPage;
    int pageNumber;
    String order;
    // vista espansa/compressa valori codificati: "0","1";
    boolean ext = false;

    String DDLType = "11"; // ???

    public int getRowsPerPage() {
      return rowsPerPage;
    }

    public String getExt() {
      return ext ? "1" : "0";
    }

    public String getDDLType() {
      return DDLType;
    }

    public void setDDLType(String dDLType) {
      DDLType = dDLType;
    }

  }
  /** Lascio perdere il seguente discorso... complicazione non richesta!
   *<p> Questo viene memorizzato nella sessione e quindi persistente tra una request e la successiva.
   * Posso editare due documenti diversi in due pagine diverse con lo stesso browser/sessione? 
   * Boh!? Di sicuro se sto editando lo STESSO documento in due pagine
   * diverse... rischio di fare pasticci ed una delle due pagine dovrebbe rifiutarsi di apportare
   * modifiche con un messaggio del tipo "pagina scaduta". 
   * 
   * <p> Non esiste neanche un comando per dire "refresh"
   * dei dati che io sto modificando per verificare che non ci siano state altre modifiche "concorrenti"
   * al DB. Infatti il web service mi sembra stateless e non ci sono LOCK/garanzie che non ci siano
   * modifiche fatte in contemporanea da due utenti. Anzi...il secondo probabilmente sovrascrive il 
   * primo.
   * 
   * <p> Invece il secondo che scrive dovrebbe essere messo in condizione di scegliere:
   * "è stata effettuata una modifica in contemporanea da un altro utente, come procedo? 
   * ricarica dal DB, sovrascrivi DB, merge intelligente."
   *
   * Nota: se serve posso Aggiungere campi a questi bean specifici per l'edit (es: info di valdazione) 
   **/
  static public class StatoEditDocumento implements Serializable {
    public BigDecimal importoTotaleResiduo;

    public String codTipologiaServizio;

    private static final long serialVersionUID = 1L;

    public enum Mode {
      CREATE, READONLY, UPDATE, DELETE
    };

    public StatoEditDocumento() {
      configurazione.setFlagGenerazioneIUV("N");
      configurazione.setFlagStampaAvviso("N");
      configurazione.setConfigurazioneIUV(configurazioneIUV);
//      //09112017 GG  - inizio Come da accordi con Polenta viene proposto un identificativo flusso di default modificabile dall'utente
//      //In giornate differenti non sono ammessi identificativiflusso uguali
//      Calendar calCurrentDate = Calendar.getInstance();
//      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//      String identificativoFlusso = "EH" + dateFormat.format(calCurrentDate.getTime()) + ".txt";	//maxlength 50
//      configurazione.setIdentificativoFlusso(identificativoFlusso);
//      //09112017 GG  - fine 
    }

    Mode mode = Mode.CREATE;

    EntrateFilteredWs.IdDocumento idDoc = new EntrateFilteredWs.IdDocumento();
    public Configurazione configurazione = new Configurazione();
    public ConfigurazioneIUV configurazioneIUV = new ConfigurazioneIUV();
    public Ruolo ruolo = new Ruolo();
    public Documento documento = new Documento();
    public Anagrafica anagrafica = new Anagrafica();
    public ArrayList<Scadenza> listaScadenze = new ArrayList<Scadenza>();
    public ArrayList<Tributo> listaTributi = new ArrayList<Tributo>();

    public String keyImpostaServizio;
    public String codImpostaServizio;
    public String chiaveFlusso;

    public boolean ddlImpostaServizioDisabled;

    public String keyTipologiaServizio;

    String keyEnte;

    /** Questi tre campi sono determinati dall'ente "keyEnte" e reperibili come descrizione */
    String codUtenteEnte;
    String tipoUfficio;
    String codiceUfficio;

    //inizio LP PG200360
    public boolean stampaAvvisoEseguita = false;
    public boolean modalitaAggiornamento = false;
    //fine LP PG200360
    

    /** alcuni campi sono disabilitati anche in CREATE... es:in base al 
     * profilo utente che non può modificare il codSocieta. */
    public boolean isCreate() {
      return mode == Mode.CREATE;
    }

    public boolean isUpdate() {
      return mode == Mode.UPDATE;
    }

    public boolean isReadonly() {
      return mode == Mode.READONLY;
    }

    public String getChiaveFlusso() {
		return chiaveFlusso;
	}

	public void setChiaveFlusso(String chiaveFlusso) {
		this.chiaveFlusso = chiaveFlusso;
	}

	public boolean isDelete() {
      return mode == Mode.DELETE;
    }

    public boolean isConfigurazioneIuvDisable() {
      //La configurazione IUV è recuperata dalla configurazione del ws archivio carichi e non è editabile
      //return configurazione.getFlagGenerazioneIUV().equals("N");
      return true;
    }

    public String getCodTipologiaServizio() {
      return codTipologiaServizio;
    }

    public Configurazione getConfigurazione() {
      return configurazione;
    }

    public ConfigurazioneIUV getConfigurazioneIUV() {
      return configurazioneIUV;
    }

    public Ruolo getRuolo() {
      return ruolo;
    }

    public Documento getDocumento() {
      return documento;
    }

    public Anagrafica getAnagrafica() {
      return anagrafica;
    }

    public ArrayList<Scadenza> getListaScadenze() {
      return listaScadenze;
    }

    public ArrayList<Tributo> getListaTributi() {
      return listaTributi;
    }

    public Mode getMode() {
      return mode;
    }

    public void setMode(Mode mode) {
      this.mode = mode;
    }

    public String getKeyImpostaServizio() {
      return keyImpostaServizio;
    }

    public String getCodImpostaServizio() {
      return codImpostaServizio;
    }

    public boolean isDdlImpostaServizioDisabled() {
      return ddlImpostaServizioDisabled;
    }

    public String getKeyTipologiaServizio() {
      return keyTipologiaServizio;
    }

    public String getKeyEnte() {
      return keyEnte;
    }

    public void setKeyEnte(String keyEnte) {
      this.keyEnte = keyEnte;
    }

    public String getCodiceUfficio() {
      return codiceUfficio;
    }

    public String getTipoUfficio() {
      return tipoUfficio;
    }

    public String getCodUtenteEnte() {
      return codUtenteEnte;
    }
    //inizio LP PG200360
    public boolean isStampaAvvisoEseguita() {
    	return stampaAvvisoEseguita;
    }
    
    public boolean isModalitaAggiornamento() {
    	return modalitaAggiornamento;
    }
    //fine LP PG200360
  }


  /** Screen relativo a questa Action... È lo screen che sarà utilizzato come view dello stato attuale,
   * (quindi result della {@link GestioneDocumentiCaricoFlow}). Come parametro di input
   * mi dice ache lo screen dal quale arriva la richiesta/evento client.*/
  static public class Screen {
    static public final String SEARCH = "search";
    static public final String ADD = "add";
    static public final String EDIT = "edit";
    static public final String DELETE = "delete";
    static public final String STAMPA = "stampa";

    /** ristorna una costante utilizzabile con == */
    static public final String fromString(String s) {
      if (s == null)
        return null;

      for (String screen : new String[] {SEARCH, ADD, EDIT, DELETE, STAMPA}) {
        if (screen.equals(s)) {
          return screen;
        }
      }
      throw new IllegalArgumentException("screen=" + s);
    }
  };

  static public class Strings {
    static public boolean isNullOrEmpty(String s) {
      return s == null || s.equals("");
    }
  }
  
  
//PRE_JAVA1.8 SB - inizio
	public boolean checkValiditaMail(HttpServletRequest request, String email, Error error){
		  
		  
		  try{
			  String dbSchemaCodSocieta=(String)request.getSession().getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
			  ValidatoreHelper validatore = new ValidatoreHelper(dbSchemaCodSocieta, request, false, "", 0);
			  
			  return validatore.validaMail("",email, request);
			  
		  }catch(Exception e){
			  error.addErrorvalue("Si è verificato un problema durante la verifica della mail. Riprovare più tardi");		
			  e.printStackTrace();
		  }
		  return false;
		  
	  }
	//PRE_JAVA1.8 SB - fine

	//inizio LP PG200360
	private ConfigTassonomiaListaResponse getTassonomiaListaResponse(HttpServletRequest request) throws FaultType, RemoteException
	{
		ConfigTassonomiaListaResponse res = null;
		ConfigTassonomiaListaRequest in = new ConfigTassonomiaListaRequest();
		in.setRowsPerPage(0);
		in.setPageNumber(0);
		in.setOrder("SPIA"); // ordina per "dati specifici incasso" 
		Date oggi = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dataValidita = df.format(oggi);
		in.setDataInizioValidita_da(dataValidita);
		in.setDataFineValidita_a(dataValidita);
		
		res = WSCache.configTassonomiaServer.lista(in, request);
		return res;
	}
	//fine LP PG200360
  
	//TK2020121188000041
	private void salvaFiltriRicerca(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute("tx_societa", request.getAttribute("tx_societa")!=null?request.getAttribute("tx_societa"):"");
		session.setAttribute("tx_utente", request.getAttribute("tx_utente")!=null?request.getAttribute("tx_utente"):"");
		session.setAttribute("tx_UtenteEnte", request.getAttribute("tx_UtenteEnte")!=null?request.getAttribute("tx_UtenteEnte"):"");
		session.setAttribute("annoEmissione", request.getAttribute("annoEmissione")!=null?request.getAttribute("annoEmissione"):"");
		session.setAttribute("numEmissione", request.getAttribute("numEmissione")!=null?request.getAttribute("numEmissione"):"");
		session.setAttribute("numeroBollettino", request.getAttribute("numeroBollettino")!=null?request.getAttribute("numeroBollettino"):"");
		session.setAttribute("codFiscale", request.getAttribute("codFiscale")!=null?request.getAttribute("codFiscale"):"");
		session.setAttribute("numDocumento", request.getAttribute("numDocumento")!=null?request.getAttribute("numDocumento"):"");
		session.setAttribute("numeroIUV", request.getAttribute("numeroIUV")!=null?request.getAttribute("numeroIUV"):"");
		if(request.getParameter("impostaServVal")!=null) {
			session.setAttribute("tx_imposta_servizio_value", request.getParameter("impostaServVal"));
		}
		if(request.getParameter("tipServVal")!=null) {
			session.setAttribute("tx_tipologia_servizio_value", request.getParameter("tipServVal"));
		}
		if(request.getParameter("statoDocVal")!=null) {
			session.setAttribute("stato_documento_value", request.getParameter("statoDocVal"));
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
		request.setAttribute("numeroBollettino", session.getAttribute("numeroBollettino")!=null?session.getAttribute("numeroBollettino"):"");
		request.setAttribute("codFiscale", session.getAttribute("codFiscale")!=null?session.getAttribute("codFiscale"):"");
		request.setAttribute("numDocumento", session.getAttribute("numDocumento")!=null?session.getAttribute("numDocumento"):"");
		request.setAttribute("numeroIUV", session.getAttribute("numeroIUV")!=null?session.getAttribute("numeroIUV"):"");
		request.setAttribute("stato_documento_value", session.getAttribute("stato_documento_value")!=null?session.getAttribute("stato_documento_value"):"");
		request.setAttribute("tx_imposta_servizio_value", session.getAttribute("tx_imposta_servizio_value")!=null?session.getAttribute("tx_imposta_servizio_value"):"");
	}
	
	private void resetFiltri(HttpServletRequest request) {
		HttpSession session = request.getSession();
		request.setAttribute("tx_societa", "");
		request.setAttribute("tx_utente", "");
		request.setAttribute("tx_UtenteEnte", "");
		request.setAttribute("annoEmissione", "");
		request.setAttribute("numEmissione", "");
		request.setAttribute("tx_tipologia_servizio_value", "");
		request.setAttribute("numeroBollettino", "");
		request.setAttribute("codFiscale", "");
		request.setAttribute("numDocumento", "");
		request.setAttribute("numeroIUV", "");
		request.setAttribute("stato_documento_value", "");
		request.setAttribute("tx_imposta_servizio_value", "");
		session.setAttribute("tx_societa", "");
		session.setAttribute("tx_utente", "");
		session.setAttribute("tx_UtenteEnte", "");
		session.setAttribute("annoEmissione", "");
		session.setAttribute("numEmissione", "");
		session.setAttribute("tx_tipologia_servizio_value", "");
		session.setAttribute("numeroBollettino", "");
		session.setAttribute("codFiscale", "");
		session.setAttribute("numDocumento", "");
		session.setAttribute("numeroIUV", "");
		session.setAttribute("stato_documento_value", "");
		session.setAttribute("tx_imposta_servizio_value", "");
	}
	//FINE TK2020121188000041

	//inizio LP PG200360
	private void Reset()
	{
	    try {
	        onFormDocumentoConstantsLoad();
	
	        EntrateFilteredWs.IdDocumento idDocOLD = (EntrateFilteredWs.IdDocumento) request.getSession().getAttribute("idDocOLD");
	        if(idDocOLD != null) {
		        onFormDocumentoDataLoad(idDocOLD);
		
		        String retMessage = "";
		        retMessage = checkConfigurazioneIUV(editDocumento);
		        if (retMessage.trim().length()>0) {
		       	  	setFormValidationMessage("frmAction", retMessage);
		        }
	        }
	        logger.debug("eseguito RESET documento:" + idDocOLD);
	      } catch (ConfigurazioneException ex) {
	        	ex.printStackTrace();
	        	setFormValidationMessage("frmAction", "Impossibile recuperare Id Dominio/AuxDigit/Application Code/Segregation Code");
	      } catch (Exception e) {
	      	e.printStackTrace();
	      	throw new RuntimeException(e);
	      }
	}
	//fine LP PG200360
	
//	private String getPathStampaAvviso(String dbSchemaCodSocieta) {
//		PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext()
//		          .getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
//		return configuration.getProperty(PropertiesPath.pathStampaAvviso.format(dbSchemaCodSocieta)).trim();
//	}
}
