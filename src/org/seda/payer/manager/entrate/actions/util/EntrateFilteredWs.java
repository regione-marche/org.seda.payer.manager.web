package org.seda.payer.manager.entrate.actions.util;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import javax.sql.rowset.WebRowSet;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.SerializationUtils;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.entrate.exceptions.ConfigurazioneException;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.IntegraEcDifferitoServer;
import org.seda.payer.manager.ws.WSCache;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Anagrafica;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.RichiestaAvvisoPagoPaRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.RichiestaAvvisoPagoPaResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.CancellazioneEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.CancellazioneEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Configurazione;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.ConfigurazioneIUV;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DiscaricoEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.DiscaricoEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Documento;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.InserimentoEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.InserimentoEcResponse;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Ruolo;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Scadenza;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Tributo;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.VariazioneEcRequest;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.VariazioneEcResponse;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocator;
import com.seda.j2ee5.maf.components.servicelocator.ServiceLocatorException;
import com.seda.payer.bancadati.webservice.dati.Paginazione;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLImpServizioRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLImpServizioResponse;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLTipServizioRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDDLTipServizioResponse;
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiRequest;
import com.seda.payer.bancadati.webservice.dati.RecuperaDocumentiResponse;
import com.seda.payer.bancadati.webservice.dati.RicercaTributiRequest;
import com.seda.payer.bancadati.webservice.dati.RicercaTributiResponse;
import com.seda.payer.core.bean.AnagraficaBollettino;
import com.seda.payer.core.bean.ConfigurazioneModello3;
import com.seda.payer.core.bean.EntrateTributiPage;
import com.seda.payer.core.dao.ArchivioCarichiDao;
import com.seda.payer.core.dao.ConfigurazioneModello3DaoImpl;
import com.seda.payer.core.dao.EntrateBancaDatiDao;
import com.seda.payer.core.exception.DaoException;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLRequestType;
import com.seda.payer.pgec.webservice.commons.dati.GetListaUtentiEntiXml_DDLResponseType;
import com.seda.payer.pgec.webservice.commons.dati.GetStaticDDLListsRequest;
import com.seda.payer.pgec.webservice.commons.dati.GetStaticDDLListsResponse;
import com.seda.payer.pgec.webservice.commons.dati.GetUtentiDDLRequest;
import com.seda.payer.pgec.webservice.commons.dati.GetUtentiDDLResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaProvinceResponse;
import com.seda.payer.pgec.webservice.commons.dati.ResponseType;
import com.seda.payer.pgec.webservice.commons.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest2;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchRequest3;
import com.seda.payer.pgec.webservice.configutentetiposervizioente.dati.ConfigUtenteTipoServizioEnteSearchResponse;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailRequest;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailResponse;

/** Webservices con dati filtrati in base alle limitazioni del profilo utente
 * 
 * <p>Funzionalità:
 * <ul>
 * <li> Accesso a un sottoinsieme dei servizi di {@link WSCache} mantenendo lo stesso nome. Man mano
 *      che mi studio quali mi servono e come funzionano.  
 * <li> Alcuni WS non mi sono chiari, ad esemplio la lista degli enti fornita da da due WS
 *      mi ritorna insiemi di Enti Diversi. TODO: Chiedere
 * <li> Nuovi servizi come {@link IntegraEcDifferitoServer} lo definisco quì dentro per poi migrarli
 *      in {@link WSCache}
 * <li> Funzione di <b>lettura simulata</b> in {@link IntegraEcDifferitoServer} utilizzando altri WS e
 *      accesso DAO appositamente implementatai con accesso diretto a DAO/JDBC e modificando.
 *      Ci penserà Roberto Marini perché io non conosco acora DAO/Schema. Questa funzione potrebbe 
 *      essere implementata dentro {@link IntegraEcDifferitoServer} per essere riusata.
 * <li> Profilazione Utente. A livello applicativo  limito i risultati forniti dai vari WS.
 *      Posso farlo sia agendo sui parametri di ricerca verso {@link WSCache} che filtrando i risultati.
 * <li> Codifica alternativa [utenteEnte,tipoUfficio,codUfficio]. Questo contesto lo associo 
 *      all'Ente estraendolo da una stringa descrittiva del {@link WebRowSet}'
 * <li> Adattamento importi BigDecimal. Alcuni importo WS/DAO sono in Euro con due decimali, altri
 *      sono in centesimi di Euro, con i due decimali impostati a 00.Utilizzo
 *      due funzioni di conversione (cetToEuro, euroToCent) che mi permettono di ricercare dove 
 *      avviene questa conversione. Come standard mantengo ai chiamanti un importo in EURO,00.
 *      
 *</ul>
 *  Nota: dovrebbe essere una scollegata dal concetto di request.
 *  Tuttavia alcuni ws hanno bisogno di specificare dati aggiuntivi (codSocieta) come parametro header.
 *  Nota: {@link WSCache} estrae il codSocieta da request: Request.URL==>Template==>codSocieta.
 *
 *
 *@author luciano.dercoli 2017-08-xx
 **/
public class EntrateFilteredWs implements Serializable {
  private static final long serialVersionUID = 1L;

  /** Questa procedura è utilizzabile SOLO da amministratori. TODO:chiedere */
  public static enum ProfiloUtente {
    AMMI, AMSO, AMUT, AMEN
  }


  /** Parametri in input già estratti dalla request. 
   * <p> Alcuni potrebbero essere invalidi/inutilizzabili
   * se per esempio cambio l'Utente... l'Ente diventerà invalido e quindi lo ignoro.
   * <p> La stringa vuota "" rappresenta il valore di default "Tutte le societa", "Tutti gli enti", ecc
   * <p> il valore null significa "lasciare invariato", cioé che il parametro non è nel form: o perché non interessa filtrare,
   * oppure perché il filtro è fissato e la DropdownList è disabled  */
  static public class EventoFiltro {
    public String keySocieta = "";
    public String keyUtente = "";
    public String keyEnte = "";
    public String keyTipologiaServizio = "";
    public String keyImpostaServizio = "";
    // eventi 
    public boolean tx_societa_changed;
    public boolean tx_utente_changed;
    public boolean tx_ente_changed;
    public boolean tx_tipologia_servizio_changed;
    //inizio LP PG200360
    public boolean tx_button_flagstampaavviso_changed;
    //fine LP PG200360
    public boolean filtroReset;
    
    public boolean update;
  }

  static public class Societa implements Serializable {
    private static final long serialVersionUID = 1L;
    String codSocieta;

    public Societa(String txSocieta) {
      codSocieta = txSocieta;
    }

    public String getCodSocieta() {
      return codSocieta;
    }

    public String getKey() {
      return codSocieta;
    }
  }

  static public class Utente implements Serializable {
    private static final long serialVersionUID = 1L;
    Societa societa;
    public String codUtente;

    public Utente() {}

    public Utente(Societa societa, String codUtente) {
      super();
      this.societa = societa;
      this.codUtente = codUtente;
    }

    public String getCodUtente() {
      return codUtente;
    }

    public String getKey() {
      return societa.getKey() + codUtente;
    }
  }

  static public class Ente implements Serializable {
    private static final long serialVersionUID = 1L;
    EntrateFilteredWs.Utente utente;
    String codEnte;

    public Ente(EntrateFilteredWs.Utente utente, String codEnte) {
      super();
      this.utente = utente;
      this.codEnte = codEnte;
    }

    public String getCodEnte() {
      return codEnte;
    }

    public String getKey() {
      return utente.getKey() + codEnte;
    }

    public EntrateFilteredWs.Utente getUtente() {
      return utente;
    }
  }

  /** Non utilizziamo il Servizio erogato per cui si paga una Tassa (es: TARSU)...
   * ma un generico raggrupamento di servizi diversi es: "Entrate per servizi comunali" */
  static public class TipologiaServizio implements Serializable {
    private static final long serialVersionUID = 1L;
    Societa societa;
    public String codTipologiaServizio;

    public TipologiaServizio(Societa societa, String codTipologiaServizio) {
      super();
      this.societa = societa;
      this.codTipologiaServizio = codTipologiaServizio;
    }

    public String getCodTipologiaServizio() {
      return codTipologiaServizio;
    }

    /** */
    public String getKey() {
      return societa.getKey() + codTipologiaServizio;
    }
  }

  static public class ImpostaServizio implements Serializable {
    private static final long serialVersionUID = 1L;
    EntrateFilteredWs.TipologiaServizio tipologiaServizio;
    String codImposta;

    public ImpostaServizio(EntrateFilteredWs.TipologiaServizio tipologiaServizio, String codImposta) {
      super();
      this.tipologiaServizio = tipologiaServizio;
      this.codImposta = codImposta;
    }

    public String getCodImposta() {
      return codImposta;
    }

    /** */
    public String getKey() {
      return tipologiaServizio.getKey() + codImposta;
    }
  }

  /** ID documento, usato per richieste di lettura. */
  static public class IdDocumento implements Serializable {
    private static final long serialVersionUID = 1L;

    public String chiaveFlusso;

    public String chiaveTomb;

    /** codice Societa */
    public String codSocieta;

    /** codice Utente  */
    public String codUtente;

    /** codice ente */
    public String codEnte;

    /** tipo servizio EP = entrate */
    public String codTipologiaServizio;
    /** codice imposta servizio */
    public String codImpostaServizio;

    /** numero documento */
    public String numeroDocumento;

    // Questi due non sono selezionabili direttamante, ma sono fissati dall'Ente selezionato
    /** codifica UtenteEnte */
    public String codUtenteEnte;
    /** tipo ufficio */
    public String codTipoUfficio;
    /** codice ufficio */
    public String codUfficio;
    public String numeroEmissione;

    public String annoEmissione;

  }

  /** controllo se è contenuto della lista. */
  public static boolean isContainedInListaTipologiaServizio(HttpServletRequest request,
      String codSocieta, String codTipologiaServizio) {
    HttpSession session = request.getSession();
    String lista = "listaTipologieServizio";
    String xml = (String) request.getAttribute(lista);

    if (xml != null) {
      //inizio LP PG21XX04 Leak
      WebRowSet webRowset = null;
      //fine LP PG21XX04 Leak
      try {
        //inizio LP PG21XX04 Leak
        //WebRowSet webRowset = Convert.stringToWebRowSet(xml);
        webRowset = Convert.stringToWebRowSet(xml);
        //fine LP PG21XX04 Leak
        while (webRowset.next()) {
          if (codSocieta.equals(webRowset.getString(3))
              && codTipologiaServizio.equals(webRowset.getString(1)))
            return true;
        }
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
	  //inizio LP PG21XX04 Leak
	  finally {
		try {
			if(webRowset != null) {
				webRowset.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	  }
	  //fine LP PG21XX04 Leak
      
    }

    return false;
  }

  // SOCIETA
  /**  CodiceSocieta[char 5] */
  EntrateFilteredWs.Societa societa;
  boolean ddlSocietaDisabled = false;

  // UTENTE
  /**  CodiceSocieta[char 5] + CodiceUtente [char 5] */
  EntrateFilteredWs.Utente utente;
  boolean ddlUtenteDisabled = false;
  /** xml WebRowSet */
  private String listaUtenti;
  // con descrizione più dettagliata
  String listaSocUtenteEnte;

  // ENTE
  /**  CodiceSocieta[char 5] + CodiceUtente [char 5] + CodiceEnte [char 10] */
  public EntrateFilteredWs.Ente ente;
  boolean ddlEnteDisabled = false;
  private String listaEnti;

  // TIPOLOGIA SERVIZIO
  EntrateFilteredWs.TipologiaServizio tipologiaServizio;
  private String listaTipologieServizio;

  // IMPOSTA SERVIZIO
  EntrateFilteredWs.ImpostaServizio impostaServizio;
  boolean ddlImpostaServizioDisabled = false;
  private String listaImpostaServizio;

  // PROFILO UTENTE
  UserBean userBean;
  transient EntrateFilteredWs.ProfiloUtente userProfile;


  // codifica aggiuntiva del contesto, determanita della selezione Ente
  private String codUtenteEnte = "";
  private String tipoUfficio = "";
  private String codUfficio = "";
  
  //31102017 GG - inizio
  // IDENTIFICATIVO DOMINIO
  private String idDominio = "";
  private String auxDigit = "";
  private String applicationCode = "";
  private String segregationCode = "";
  public String identificativoFlusso = "";
  //31102017 GG - fine
  //PG200140 - inizio
  private String carattereServizio = "";
  //PG200140 - fine

  private transient HttpServletRequest request;

  /** La profilazione utente impone dei vincoli ai dati visibili/modificabili.
   *  <p>--- Restrizioni di Profilo ---
   *  <ul>
   *  <li> AMMI (AMMInistratore del sistema informatico)<br>
   *        TUTTO LIBERO
   *        
   *  <li> AMSO (AMministratore SOcietà)<br>
   *   SOCIETA FISSA<br>
   *   UTENTE LIBERO<br>
   *   ENTE LIBERO<br>
   * 
   *  <li> AMUT (AMministratore UTente)<br>
   *   SOCIETA FISSA<br>
   *   UTENTE FISSO<br>
   *   ENTE LIBERO<br>
   * 
   *  <li> AMEN (AMministratore ENte)<br>
   *   SOCIETA FISSA<br>
   *   UTENTE FISSO<br>
   *       multiutente--> ENTE VARIABILE<br> 
   *     no multiutente --> ENTE FISSO<br>
   *     
   *   <p>    TODO:LUCIANO: AMEN può essere "Multi-Ente"? Oppure "Multi-Utente"? Mi pare la seconda
   *   cioè più Enti anche di diversi Utenti!
   **/
  public void setUserContext(UserBean userBean) {
    setUserBean(userBean);

    // Tutti i profili diversi da AMMI vincolano una unica "Società" 
    if (userProfile == EntrateFilteredWs.ProfiloUtente.AMSO
        || userProfile == EntrateFilteredWs.ProfiloUtente.AMUT
        || userProfile == EntrateFilteredWs.ProfiloUtente.AMEN) {
      societa = new EntrateFilteredWs.Societa(userBean.getCodiceSocieta());
      ddlSocietaDisabled = true;
    }

    // Amministratore Utente o Ente-SingoloUtente ... vincola anche l'Utente
    if (userProfile == EntrateFilteredWs.ProfiloUtente.AMUT
        || (userProfile == EntrateFilteredWs.ProfiloUtente.AMEN && !userBean
            .getMultiUtenteEnabled())) {
      utente = new EntrateFilteredWs.Utente(societa, userBean.getCodiceUtente());
      ddlUtenteDisabled = true;
    }

    // La DDL "Utente/Ente" è disabilitata solo per il ruolo "AMEN" senza l'opzione multiutente.    
    if (userProfile == EntrateFilteredWs.ProfiloUtente.AMEN && !userBean.getMultiUtenteEnabled()) {
      ente = new EntrateFilteredWs.Ente(utente, userBean.getChiaveEnteConsorzio());
      ddlEnteDisabled = true;
    }

  }

  /** Serve per accedere a librerie che utilizzano oggetti salvati in application/sessione/request */
  public void setRequestContext(HttpServletRequest request) {
    this.request = request;
  }

  public void trySetFilter(EventoFiltro ev) throws ConfigurazioneException {

    if (ev.filtroReset) {
    	if(userProfile == EntrateFilteredWs.ProfiloUtente.AMMI){
    		 ev.keySocieta = "";
    	     ev.keyUtente = "";
    	     ev.keyEnte = "";
    	}else if(userProfile == EntrateFilteredWs.ProfiloUtente.AMSO){
   	     	ev.keyUtente = "";
   	     	ev.keyEnte = "";
    	}else if(userProfile == EntrateFilteredWs.ProfiloUtente.AMUT){
   	     	ev.keyEnte = "";
    	}
      ev.keyTipologiaServizio = "";
      ev.keyImpostaServizio = "";
    }

//    // questo potrebbe essere assente -> lo considero "tutte le tipologie" 
//    if (ev.keyTipologiaServizio == null)
//      ev.keyTipologiaServizio = "";

    // la lista Società è statica... ed in genere bloccata tranne per AMMI
    if (ev.keySocieta != null) {
      if (userProfile == EntrateFilteredWs.ProfiloUtente.AMMI) {
        societa = ev.keySocieta.equals("") ? null : new EntrateFilteredWs.Societa(ev.keySocieta);
      }
    }

    boolean listaUtenteDirty = false;
    boolean listaEnteDirty = false;
    boolean listaTipologiaServizioDirty = false;
    boolean listaImpostaServizioDirty = false;

    if (ev.tx_societa_changed || ev.filtroReset) {
      // occorre caricare / ricaricare tutte le liste...
      listaUtenteDirty = true;
      listaEnteDirty = true;
      listaTipologiaServizioDirty = true;
      listaImpostaServizioDirty = true;
    }

    if (ev.tx_utente_changed) {
      listaEnteDirty = true;
      listaTipologiaServizioDirty = true;
      listaImpostaServizioDirty = true;
    }

    if (ev.tx_ente_changed) {
      /** Come chiave TipologiaServizio è interno all'Utente... 
       * tuttavia se mi specificano un Ente... la
       * lista dei servizi pertinenti che ritorno dovrebbe essere ridotta di conseguenza! */
      listaTipologiaServizioDirty = true;
      listaImpostaServizioDirty = true;
    }

    if (ev.tx_tipologia_servizio_changed) {
      listaImpostaServizioDirty = true;
    }


    // ricarico lista Utenti?
    if (listaUtenteDirty) {
      String paramCodiceSocieta = societa == null ? "" : societa.codSocieta;
      loadListaUtente(request, paramCodiceSocieta, listaUtenteDirty);
    }

    // Utente...
    if (ev.keyUtente != null) {
      if (ev.keyUtente.length() == 10) {
        String codSocieta = ev.keyUtente.substring(0, 5);
        String codUtente = ev.keyUtente.substring(5);
        if (societa != null) {
          if (societa.codSocieta.equals(codSocieta)) {
            utente = new EntrateFilteredWs.Utente(societa, codUtente);
          } else {
            // il vecchio Utente non appartine alla nuova società selezionata... lo annullo
            utente = null;
          }
        } else {
          // società == tutte... la DDL dell'Utente seleziona un Utente+Società 
          EntrateFilteredWs.Societa s = new EntrateFilteredWs.Societa(codSocieta);
          utente = new EntrateFilteredWs.Utente(s, codUtente);
        }
      } else {
        utente = null;
      }
    }


    // Ricarico Lista Enti?
    if (listaEnteDirty) {
      String paramCodiceSocieta = "";
      String paramCodiceUtente = "";
      if (utente != null) {
        paramCodiceSocieta = utente.societa.codSocieta;
        paramCodiceUtente = utente.codUtente;
      } else if (societa != null) {
        paramCodiceSocieta = societa.codSocieta;
      }
      loadListaEnti(request, paramCodiceSocieta, paramCodiceUtente, "" /*paramCodiceEnte*/,
          listaEnteDirty);

      loadListaSocUtentiEnti(request, paramCodiceSocieta, paramCodiceUtente,
          "" /*paramCodiceEnte*/, listaEnteDirty);

    }

    // Ente...
    if (ev.keyEnte != null) {
      Ente enteBefore = ente;
      if (ev.keyEnte.length() == 20) {
        String codSocieta = ev.keyEnte.substring(0, 5);
        String codUtente = ev.keyEnte.substring(5, 10);
        String codEnte = ev.keyEnte.substring(10);

        if (utente != null) {
          // Ente interno all'utente selezionato...
          if (utente.codUtente.equals(codUtente) && utente.societa.codSocieta.equals(codSocieta)) {
            ente = new EntrateFilteredWs.Ente(utente, codEnte);
          } else {
            ente = null;
          }
        } else if (societa != null) {
          // Ente interno alla Società selezionata...
          if (societa.codSocieta.equals(codSocieta)) {
            ente = new EntrateFilteredWs.Ente(new EntrateFilteredWs.Utente(societa, codUtente),
                codEnte);
          } else {
            ente = null;
          }
        } else {
          // Società ed Utente non specificati...  Ente selezionato direttamente
          EntrateFilteredWs.Societa s = new EntrateFilteredWs.Societa(codSocieta);
          EntrateFilteredWs.Utente u = new EntrateFilteredWs.Utente(s, codUtente);
          ente = new EntrateFilteredWs.Ente(u, codEnte);
        }
      } else {
        ente = null;
      }
      // Ente variato? aggiorno UtenteEnte,tipoUffico,codUfficio
      if (ente != enteBefore) {
        updateIdSecondario();
      }
    }

    // Societa/Utente/Ente selezionati direttamante o implicitamente
    String paramCodiceSocieta = findSocietaSel() == null ? "" : findSocietaSel().codSocieta;
    String paramCodiceUtente = findUtenteSel() == null ? "" : findUtenteSel().codUtente;
    String paramCodiceEnte = ente == null ? "" : ente.codEnte;
    
    //31102017 GG - inizio gestione idDominio
    idDominio = "";
	auxDigit = "";
	applicationCode = "";
	segregationCode = "";
	//PG200140 - inizio
	carattereServizio = "";
	//PG200140 - fine
    if(ente!=null) {
	    getIdDominio(request, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte);
	    getConfigurazioneIuv(request, paramCodiceUtente, idDominio);
	    //Gestione identificativo flusso default - inizio
	    Calendar calCurrentDate = Calendar.getInstance();
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	    if(!ev.update){
	    	String identificativoFlussoNew = "EH" + paramCodiceUtente + dateFormat.format(calCurrentDate.getTime()) + ".txt";	//maxlength 50 
		    if (ev.tx_ente_changed) {	//Cambio l'identificativo flusso solo se cambia l'ente	
		    	identificativoFlusso = identificativoFlussoNew;
		    } else {
		    	if (request.getParameter("configurazione_identificativoFlusso")!=null && ((String)request.getParameter("configurazione_identificativoFlusso")).trim().length()>0) 
		    		identificativoFlusso = request.getParameter("configurazione_identificativoFlusso");
		    	else 
		    		identificativoFlusso = identificativoFlussoNew;
		    }
	    }
	    //Gestione identificativo flusso default - fine
    }
    //31102017 GG - fine gestione idDominio

    //        lista Tipologia Servizio ...
    if (listaTipologiaServizioDirty) {

      // AMEN ha una lista di getListaTipologiaServizioString()
      // a cui può accedere... ma siccome il filtro non prevede una sottoselezione di elementi
      // obbligo AMEN a selezionare PER FORZA UNA tipologiaServizio PER VOLTA!
      // Quindi... AMEN non può specificare "tutte le tipologie servizio"...
      boolean allowEmptySelection = userProfile != EntrateFilteredWs.ProfiloUtente.AMEN;
      loadListaTipologiaServizio(request, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,
          allowEmptySelection, listaTipologiaServizioDirty);
    }
    
    if (ev.keyTipologiaServizio != null) {
      if (ev.keyTipologiaServizio.length() >= 8) {
        String codSocieta = ev.keyTipologiaServizio.substring(0, 5);
        String codTipologiaServizio = ev.keyTipologiaServizio.substring(5, 8);

        if (listaTipologiaServizioDirty
            && !isContainedInListaTipologiaServizio(request, codSocieta, codTipologiaServizio)) {
          // non è più presente nella lista... perché è stata ricaricata
          tipologiaServizio = null;
        } else {
          EntrateFilteredWs.Societa societaSel = findSocietaSel();
          if (societaSel != null) {
            if (!societaSel.codSocieta.equals(codSocieta)) {
              tipologiaServizio = null;
            } else {
              tipologiaServizio = new EntrateFilteredWs.TipologiaServizio(societaSel,
                  codTipologiaServizio);
            }
          } else {
            // società specificata implicitamnete dalla selezione tipologiaServizio?
            // Mi pare esagerato... comunque è una cosa possibile

            tipologiaServizio = new EntrateFilteredWs.TipologiaServizio(
                new EntrateFilteredWs.Societa(codSocieta), codTipologiaServizio);
            paramCodiceSocieta = findSocietaSel().codSocieta;
          }
        }
      } else
        tipologiaServizio = null;
    }

    if (listaImpostaServizioDirty) {
      String paramCodiceTipologiaServizio = tipologiaServizio == null ? ""
          : tipologiaServizio.codTipologiaServizio;
      loadListaImpostaServizio(request, paramCodiceSocieta, paramCodiceUtente, paramCodiceEnte,
          paramCodiceTipologiaServizio, listaImpostaServizioDirty);
    }

    // Imposta Servizio
    if (ev.keyImpostaServizio != null) {
      if (ev.keyImpostaServizio.equals("")) {
        impostaServizio = null;
      } else {
        String codiceSocieta = ev.keyImpostaServizio.substring(0, 5);
        String codTipologiaServizio = ev.keyImpostaServizio.substring(5, 8);
        String codImpostaServizio = ev.keyImpostaServizio.substring(8, 10);
        //String codImpostaServizio = ev.keyImpostaServizio.substring(8, ev.keyImpostaServizio
        //    .length());

        if (tipologiaServizio != null) {
          if (codiceSocieta.equals(tipologiaServizio.societa.codSocieta)
              && codTipologiaServizio.equals(tipologiaServizio.codTipologiaServizio)) {
            impostaServizio = new EntrateFilteredWs.ImpostaServizio(tipologiaServizio,
                codImpostaServizio);
          } else {
            impostaServizio = null;
          }
        } else if (societa != null) {
          if (codiceSocieta.equals(societa.codSocieta)) {
            impostaServizio = new EntrateFilteredWs.ImpostaServizio(
                new EntrateFilteredWs.TipologiaServizio(societa, codTipologiaServizio),
                codImpostaServizio);
          } else {
            impostaServizio = null;
          }
        } else {
          impostaServizio = new EntrateFilteredWs.ImpostaServizio(
              new EntrateFilteredWs.TipologiaServizio(new EntrateFilteredWs.Societa(codiceSocieta),
                  codTipologiaServizio), codImpostaServizio);
        }
      }
    }
  }

  /** aggiorno UtenteEnte, TipoUffcio, CodUfficio estraendoli dal webRowSet */
  private void updateIdSecondario() {
    codUtenteEnte = "";
    tipoUfficio = "";
    codUfficio = "";

    if (ente == null) {
    } else {
      // ricerca riga per chiave, ed estrazione con regex
      //inizio LP PG21XX04 Leak
      WebRowSet wrs = null;
      //fine LP PG21XX04 Leak
      try {
        //inizio LP PG21XX04 Leak
        //WebRowSet wrs = Convert.stringToWebRowSet(getListaSocUtenteEnte());
        wrs = Convert.stringToWebRowSet(getListaSocUtenteEnte());
        //fine LP PG21XX04 Leak
        while (wrs.next()) {
          String rowKey = wrs.getString(1) + wrs.getString(2) + wrs.getString(3);
          if (rowKey.equals(ente.getKey())) {
            // trovata striga formato "[06954-F-1] Comune di TEST N1" 
            String descrizione = wrs.getString(4);
            Pattern p = Pattern.compile("\\[(\\w*)-(\\w*)-(\\w*)\\]");
            Matcher m = p.matcher(descrizione);

            if (m.find()) {
              codUtenteEnte = m.group(1);
              tipoUfficio = m.group(2);
              codUfficio = m.group(3);
            } else
              throw new RuntimeException("non riconosco utenteEnte-tipoUfficio-codUfficio, descr="
                  + descrizione);
            break;
          }
        }
        wrs.close();
      } catch (Exception ex) {
        throw new RuntimeException(ex);
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
    // TODO: Se non trovo nulla va bene il blank o meglio Exception?
  }

  /**   Ccodice valori selezionati anche in modo implicito (es: selezionando un Ente specifico, implicitamente
   ho selezionato anche società/utente anche se nel filtro sono impostati a "tutti")
  */
  public EntrateFilteredWs.Societa findSocietaSel() {
    if (ente != null)
      return ente.utente.societa;
    if (utente != null)
      return utente.societa;
    if (impostaServizio != null)
      return impostaServizio.tipologiaServizio.societa;
    if (tipologiaServizio != null)
      return tipologiaServizio.societa;
    return societa;
  }

  public EntrateFilteredWs.TipologiaServizio findTipologiaServizioSel() {
    if (impostaServizio != null)
      return impostaServizio.tipologiaServizio;
    return tipologiaServizio;
  }

  public EntrateFilteredWs.Utente findUtenteSel() {
    if (ente != null)
      return ente.utente;
    return utente;
  }

  public EntrateFilteredWs.Ente getEnte() {
    return ente;
  }

  public EntrateFilteredWs.ImpostaServizio getImpostaServizio() {
    return impostaServizio;
  }

  public String getKeyEnte() {
    return ente == null ? "" : ente.getKey();
  }

  public String getKeyImpostaServizio() {
    return impostaServizio == null ? "" : impostaServizio.getKey();
  }

  public String getKeySocieta() {
    return societa == null ? "" : societa.getKey();
  }

  public String getKeyTipologiaServizio() {
    return tipologiaServizio == null ? "**" : tipologiaServizio.getKey();
  }

  public String getKeyUtente() {
    return utente == null ? "" : utente.getKey();
  }

  public String getListaUtenti() {
    return listaUtenti;
  }

  public String getListaEnti() {
    return listaEnti;
  }

  public String getListaSocUtenteEnte() {
    return listaSocUtenteEnte;
  }

  public String getListaTipologieServizio() {
    return listaTipologieServizio;
  }

  public String getListaImpostaServizio() {
    return listaImpostaServizio;
  }

  public UserBean getUserBean() {
    return userBean;
  }

  public boolean isDdlSocietaDisabled() {
    return ddlSocietaDisabled;
  }

  public boolean isDdlUtenteDisabled() {
    return ddlUtenteDisabled;
  }

  public boolean isDdlEnteDisabled() {
    return ddlEnteDisabled;
  }

  public boolean isDdlImpostaServizioDisabled() {
    return ddlImpostaServizioDisabled;
  }

  public void reset() {
    EventoFiltro testataEvent = new EventoFiltro();
    if(userProfile == EntrateFilteredWs.ProfiloUtente.AMEN){
    	//SVILUPPO_001_LP
    	if(utente!= null) {
	    	 testataEvent.keySocieta = societa.codSocieta;
	    	 testataEvent.keyUtente = societa.codSocieta+utente.codUtente;
	    	 testataEvent.keyEnte = societa.codSocieta+utente.codUtente+ente.codEnte;
    	} else {
	    	 testataEvent.keySocieta = societa.codSocieta;
	    	 testataEvent.keyUtente = societa.codSocieta+userBean.getCodiceUtente();
	    	 testataEvent.keyEnte = societa.codSocieta+userBean.getCodiceUtente()+ente.codEnte;
    	}
    	//FINE SVILUPPO_001_LP
    }else if(userProfile == EntrateFilteredWs.ProfiloUtente.AMUT){
    	//SVILUPPO_001_LP
    	if(utente!= null) {
    		testataEvent.keySocieta = societa.codSocieta;
       	 	testataEvent.keyUtente = societa.codSocieta+utente.codUtente;
    	} else {
    		testataEvent.keySocieta = societa.codSocieta;
       	 	testataEvent.keyUtente = societa.codSocieta+userBean.getCodiceUtente();
    	}
    	//FINE SVILUPPO_001_LP
    }else if(userProfile == EntrateFilteredWs.ProfiloUtente.AMSO){
   	 	testataEvent.keySocieta = societa.codSocieta;
    }

    testataEvent.filtroReset = true;
    try {
		trySetFilter(testataEvent);
	} catch (ConfigurazioneException e) {
		e.printStackTrace();
	}
  }

  public void setDdlEnteDisabled(boolean ddlEnteDisabled) {
    this.ddlEnteDisabled = ddlEnteDisabled;
  }

  public void setDdlImpostaServizioDisabled(boolean ddlImpostaServizioDisabled) {
    this.ddlImpostaServizioDisabled = ddlImpostaServizioDisabled;
  }

  public void setDdlSocietaDisabled(boolean ddlSocietaDisabled) {
    this.ddlSocietaDisabled = ddlSocietaDisabled;
  }

  public void setDdlUtenteDisabled(boolean ddlUtenteDisabled) {
    this.ddlUtenteDisabled = ddlUtenteDisabled;
  }

  public void setListaEnti(String listaUtentiEnti) {
    this.listaEnti = listaUtentiEnti;
  }

  public void setListaImpostaServizio(String listaImpostaServizio) {
    this.listaImpostaServizio = listaImpostaServizio;
  }

  public void setListaTipologieServizio(String listaTipologieServizio) {
    this.listaTipologieServizio = listaTipologieServizio;
  }

  public void setListaUtenti(String listaUtenti) {
    this.listaUtenti = listaUtenti;
  }

  public void setUserBean(UserBean userBean) {
    this.userBean = userBean;
    this.userProfile = EntrateFilteredWs.getProfiloUtente(userBean.getUserProfile());
  }

  void loadListaUtente(HttpServletRequest request, String codiceSocieta, boolean forceReload) {
    String codiceProvincia = null;
    if (getListaUtenti() == null || forceReload) {
      try {
        GetUtentiDDLRequest in = new GetUtentiDDLRequest();
        in.setCodiceSocieta(codiceSocieta);
        in.setCodiceProvincia(codiceProvincia);
        GetUtentiDDLResponse res = WSCache.commonsServer.getUtentiDDL(in, request);
        setListaUtenti(res.getListXml());
      } catch (Exception ex) {
        throw new RuntimeException(String.format("Errore nel caricamento Utenti (societa=%s)",
            codiceSocieta), ex);
      }
    }
  }

  /**
   * @throws RuntimeException in caso di difetti di programmazione, configurazione, connessione, runtime
   *  C'è poco da gestire in modo automatico... solo Messaggio Utente/LOG amministratore
   *  con informazioni aggiuntive.   
   */
  void loadListaEnti(HttpServletRequest request, String codiceSocieta, String codiceUtente,
      String codiceEnte, boolean forceReload) {
    String siglaProvincia = null;

    if (listaEnti == null || forceReload) {
      try {
        GetListaUtentiEntiXml_DDLRequestType in = new GetListaUtentiEntiXml_DDLRequestType();
        in.setCodiceSocieta(codiceSocieta);
        in.setCodiceUtente(codiceUtente);
        in.setSiglaProvincia(siglaProvincia);
        in.setCodiceEnte(codiceEnte);

        GetListaUtentiEntiXml_DDLResponseType res = WSCache.commonsServer
            .getListaUtentiEntiXml_DDL(in, request);
        ResponseType response = res.getResponse();

        if (response.getRetCode().equals(ResponseTypeRetCode.value1)) {
          setListaEnti(res.getXml());
        } else {
          setListaEnti(null);
          throw new RuntimeException("Errore risposta WS nel caricamento Enti: "
              + response.getRetMessage());
        }
      } catch (Exception ex) {
        throw new RuntimeException(String.format("Errore nel caricamento Enti "
            + "(societa=%s, utente=%s)", codiceSocieta, codiceUtente), ex);
      }
    }
  }

  /** Lista Enti con descrizione completa dei context superiori Società/Utente/Ente */
  void loadListaSocUtentiEnti(HttpServletRequest request, String codiceSocieta,
      String codiceUtente, String codiceEnte, boolean forceReload) {
    if (listaSocUtenteEnte == null || forceReload) {
      try {
        ConfigUtenteTipoServizioEnteSearchResponse res = EntrateFilteredWs
            .getConfigUtenteTipoServizioEntes2(codiceSocieta, "", "", "", request);
        listaSocUtenteEnte = res.getResponse().getListXml();
//        System.out.println("Lista ENTI\n"+ LucioUtils.webrowsetToCsv(listaSocUtenteEnte));
      } catch (Exception ex) {
        throw new RuntimeException(String.format("Errore nel caricamento Enti "
            + "(societa=%s, utente=%s)", codiceSocieta, codiceUtente), ex);
      }
    }
  }


  void loadListaTipologiaServizio(HttpServletRequest request, String codiceSocieta,
      String codiceUtente, String codiceEnte, boolean allowEmptySelection, boolean forceReload) {

    /* Nel caso di un utente di tipologia AMEN*/
    String listaTipologiaServizioAmen = "";
    if (userProfile == ProfiloUtente.AMEN) {
      listaTipologiaServizioAmen = userBean.getListaTipologiaServizioString();
    }

    if (listaTipologieServizio == null || forceReload) {
      try {
        RecuperaDDLTipServizioRequest in = new RecuperaDDLTipServizioRequest();
        in.setCodiceSocieta(codiceSocieta);
        in.setCodiceUtente(codiceUtente);
        in.setCodiceEnte(codiceEnte);
        in.setTipologiaServizio(listaTipologiaServizioAmen);

        RecuperaDDLTipServizioResponse res;
        //        res = WSCache.entrateBDServer.recuperaDDLTipologiaServizio(in, request);
        res = EntrateFilteredWs.recuperaDDLTipologiaServizio(in, request);


        com.seda.payer.bancadati.webservice.dati.ResponseType response = res.getRisultato();
        if (response.getRetCode() == com.seda.payer.bancadati.webservice.dati.ResponseTypeRetCode.value1) {
          setListaTipologieServizio(res.getListXml());
          if (allowEmptySelection) {
            // Aggiungo Riga fittizia valutata "**" per dire tutte le tipologie.
  	      	//inizio LP PG21XX04 Leak
              WebRowSet webRowset = null;
	      	//fine LP PG21XX04 Leak
            try {
      	      //inizio LP PG21XX04 Leak
              //WebRowSet webRowset = Convert.stringToWebRowSet(getListaTipologieServizio());
              webRowset = Convert.stringToWebRowSet(getListaTipologieServizio());
  	      	  //fine LP PG21XX04 Leak
              webRowset.moveToInsertRow();
              webRowset.updateString(1, "*");
              webRowset.updateString(2, "Tutte le tipologie");
              webRowset.updateString(3, "*");
              webRowset.insertRow();
              webRowset.moveToCurrentRow();
              setListaTipologieServizio(Convert.webRowSetToString(webRowset));
            } catch (Exception ex) {
              throw new RuntimeException(ex);
            }
	      	//inizio LP PG21XX04 Leak
	      	finally {
	      		try {
	      			if(webRowset != null) {
	      				webRowset.close();
	      			}
	      		} catch (SQLException e) {
	      			e.printStackTrace();
	      		}
	      	}
	      	//fine LP PG21XX04 Leak
          }
        } else {
          setListaTipologieServizio(null);

          throw new RuntimeException("Errore WS nel caricamento delle Tipologie Servizio: "
              + response.getRetMessage());
        }
      } catch (Exception ex) {
        throw new RuntimeException(String.format("Errore nel caricamento delle Tipologie Servizio"
            + "(societa=%s, utente=%s, ente=%s, listaTipologia=%s)", codiceSocieta, codiceUtente,
            codiceEnte, listaTipologiaServizioAmen), ex);
      }
    }
  }

  void loadListaImpostaServizio(HttpServletRequest request, String codiceSocieta,
      String codiceUtente, String codiceEnte, String tipoServizio, boolean forceReload) {
    if (listaImpostaServizio == null || forceReload) {
      try {
        RecuperaDDLImpServizioRequest in = null;
        in = new RecuperaDDLImpServizioRequest();
        in.setCodiceSocieta(codiceSocieta);
        in.setCodiceUtente(codiceUtente);
        in.setCodiceEnte(codiceEnte);
        in.setTipologiaServizio(tipoServizio);

        RecuperaDDLImpServizioResponse res = null;
        res = WSCache.entrateBDServer.recuperaDDLImpostaServizio(in, request);
        com.seda.payer.bancadati.webservice.dati.ResponseType response = res.getRisultato();
        if (response.getRetMessage().equals("OK")) {
          setListaImpostaServizio(res.getListXml());
        } else {
          setListaImpostaServizio(null);
          throw new RuntimeException("Errore WS nel caricamento delle Imposte Servizio: "
              + response.getRetMessage());
        }
      } catch (Exception ex) {
        throw new RuntimeException(String.format("Errore nel caricamento delle Imposte Servizi "
            + "(societa=%s, utente=%s, ente=%s, tipologiaServizio=%s)", codiceSocieta,
            codiceUtente, codiceEnte, tipologiaServizio), ex);

      }
    }
  }

  /** Ricerca una descrizione per codice. La lista deve essere già caricata. */
  public String findDescrizioneTipologiaServizio(HttpServletRequest request, String key) {
    if (key == null || key.equals(""))
      return "";
  	//inizio LP PG21XX04 Leak
    WebRowSet wrs = null;
  	//fine LP PG21XX04 Leak
    try {
      //inizio LP PG21XX04 Leak
      //WebRowSet wrs = Convert.stringToWebRowSet(listaTipologieServizio);
      wrs = Convert.stringToWebRowSet(listaTipologieServizio);
      //fine LP PG21XX04 Leak
      // wrs deve essere già caricata , altrimenti NullPointerException
      while (wrs.next()) {
        String rowKey = wrs.getString(3) + wrs.getString(1);
        if (rowKey.equals(key))
          return wrs.getString(2);
      }
      throw new IllegalArgumentException("Non trovo TipologiaServizio in lista, key=" + key);
    } catch (Exception ex) {
      throw new RuntimeException(String.format("Errore ricerca TipologiaServizio, key=%s", key), ex);
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

  /** Ricerca una descrizione per codice. La lista deve essere già caricata. */
  public String findDescrizioneImpostaServizio(HttpServletRequest request, String key) {
    if (key == null || key.equals(""))
      return "";
  	//inizio LP PG21XX04 Leak
    WebRowSet wrs = null;
  	//fine LP PG21XX04 Leak
    try {
      //inizio LP PG21XX04 Leak
      //WebRowSet wrs = Convert.stringToWebRowSet(listaImpostaServizio);
      wrs = Convert.stringToWebRowSet(listaImpostaServizio);
      //fine LP PG21XX04 Leak
      // wrs deve essere già caricata , altrimenti NullPointerException
      while (wrs.next()) {
        String rowKey = wrs.getString(1) + wrs.getString(2) + wrs.getString(3);

        if (rowKey.equals(key))
          return wrs.getString(4);
      }
      throw new IllegalArgumentException("Non trovo ImpostaServizio in lista, key=" + key);
    } catch (Exception ex) {
      throw new RuntimeException(String.format("Errore ricerca ImpostaServizio, key=%s", key), ex);
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

  /**
   * Accesso WebService, compilando Header WS con "codSocieta".
   * Moltiplico importi per 100.00 e li salvo in centesimi .
   * @return risposta con eventuale esito negativo
  */
  public static InserimentoEcResponse addDocumentoCarico(InserimentoEcRequest rq,
      HttpServletRequest request) throws FaultType, RemoteException {
    // deep copy, non funziona per problemi di classloader, moltiplico e divido per 100
    //    rq = (InserimentoEcRequest) SerializationUtils.clone(rq);

    BigDecimal cent = euroToCent(rq.getDocumento().getImpBollettinoTotaleDocumento());
    rq.getDocumento().setImpBollettinoTotaleDocumento(cent);

    for (Tributo trib : rq.getListTributi()) {
      cent = euroToCent(trib.getImpTributo());
      trib.setImpTributo(cent);
    }

    for (Scadenza sca : rq.getListScadenze()) {
      cent = euroToCent(sca.getImpBollettinoRata());
      sca.setImpBollettinoRata(cent);
    }

    InserimentoEcResponse wsResponse = WSCache.integraEcDifferitoServer.addDocumentoCarico(rq,
        request, EntrateUtils.getCodSocieta(request));

    BigDecimal euro = centToEuro(rq.getDocumento().getImpBollettinoTotaleDocumento());
    rq.getDocumento().setImpBollettinoTotaleDocumento(euro);

    for (Tributo trib : rq.getListTributi()) {
      euro = centToEuro(trib.getImpTributo());
      trib.setImpTributo(euro);
    }

    for (Scadenza sca : rq.getListScadenze()) {
      euro = centToEuro(sca.getImpBollettinoRata());
      sca.setImpBollettinoRata(euro);
    }


    return wsResponse;
  }

  public VariazioneEcResponse variazioneEC(VariazioneEcRequest rq, HttpServletRequest request)
      throws FaultType, RemoteException {
    BigDecimal cent = euroToCent(rq.getDocumento().getImpBollettinoTotaleDocumento());
    rq.getDocumento().setImpBollettinoTotaleDocumento(cent);

    for (Tributo trib : rq.getListTributi()) {
      cent = euroToCent(trib.getImpTributo());
      trib.setImpTributo(cent);
    }

    for (Scadenza sca : rq.getListScadenze()) {
      cent = euroToCent(sca.getImpBollettinoRata());
      sca.setImpBollettinoRata(cent);
    }

    VariazioneEcResponse wsResponse = WSCache.integraEcDifferitoServer.variazioneEC(rq, request,
        EntrateUtils.getCodSocieta(request));

    BigDecimal euro = centToEuro(rq.getDocumento().getImpBollettinoTotaleDocumento());
    rq.getDocumento().setImpBollettinoTotaleDocumento(euro);

    for (Tributo trib : rq.getListTributi()) {
      euro = centToEuro(trib.getImpTributo());
      trib.setImpTributo(euro);
    }

    for (Scadenza sca : rq.getListScadenze()) {
      euro = centToEuro(sca.getImpBollettinoRata());
      sca.setImpBollettinoRata(euro);
    }


    return wsResponse;

  }

  public static CancellazioneEcResponse deleteDocumentoCarico(
      CancellazioneEcRequest cancellazioneEcRequest, HttpServletRequest request)
      throws com.esed.payer.archiviocarichi.webservice.srv.FaultType, IOException {

    CancellazioneEcResponse wsResponse = WSCache.integraEcDifferitoServer.deleteDocumentoCarico(
        cancellazioneEcRequest, request, EntrateUtils.getCodSocieta(request));
    return wsResponse;
  }

  public static DiscaricoEcResponse discaricoTributi(DiscaricoEcRequest discaricoEcRequest,
      HttpServletRequest request) throws com.seda.payer.integraente.webservice.srv.FaultType,
      RemoteException {
    discaricoEcRequest = (DiscaricoEcRequest) SerializationUtils.clone(discaricoEcRequest);
    for (Tributo trib : discaricoEcRequest.getListTributi()) {
      BigDecimal cent = euroToCent(trib.getImpTributo());
      trib.setImpTributo(cent);
    }
    DiscaricoEcResponse wsResponse = WSCache.integraEcDifferitoServer.discaricoTributi(
        discaricoEcRequest, request, EntrateUtils.getCodSocieta(request));
    return wsResponse;
  }
  
  //PG200450 GG 09032021 - inizio
  public static RichiestaAvvisoPagoPaResponse stampaAvvisoPagoPA (
		  RichiestaAvvisoPagoPaRequest richiestaAvvisoPagoPaRequest, HttpServletRequest request)
	      throws com.esed.payer.archiviocarichi.webservice.srv.FaultType, RemoteException {

	  RichiestaAvvisoPagoPaResponse wsResponse = WSCache.integraEcDifferitoServer.stampaAvvisoPagoPA(
			  richiestaAvvisoPagoPaRequest, request, EntrateUtils.getCodSocieta(request));
	    return wsResponse;
  }
  //PG200450 GG 09032021 - fine


  private DataSource anagraficaDataSource;
  private String dbSchemaCodSocieta;
  private String anagraficaDbSchema;

  protected DataSource getAnagraficaDataSource() {
    if (anagraficaDataSource == null) {
      dbSchemaCodSocieta = (String) request.getSession().getAttribute(
          ManagerKeys.DBSCHEMA_CODSOCIETA);
      PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext()
          .getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
      String dataSourceName = configuration.getProperty(PropertiesPath.dataSourceWallet
          .format(dbSchemaCodSocieta));
      try {
        this.anagraficaDataSource = ServiceLocator.getInstance().getDataSource(
            "java:comp/env/".concat(dataSourceName));
      } catch (ServiceLocatorException e) {
    	  //inizio LP PG200360
          //throw new RuntimeException(e);
    	  try {
    		  this.anagraficaDataSource = ServiceLocator.getInstance().getDataSource("java:/".concat(dataSourceName));
    	  } catch (ServiceLocatorException e1) {
    		  throw new RuntimeException(e1);
    	  }
    	  //fine LP PG200360
      }
    }
    return this.anagraficaDataSource;
  }

  protected String getAnagraficaDbSchema() {
    if (anagraficaDbSchema == null) {
      PropertiesTree configuration = (PropertiesTree) (request.getSession().getServletContext()
          .getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
      this.anagraficaDbSchema = configuration.getProperty(PropertiesPath.dataSourceSchemaWallet
          .format(dbSchemaCodSocieta));
    }

    return this.anagraficaDbSchema;
  }

  public String getDbSchemaCodSocieta() {
    if (dbSchemaCodSocieta == null) {
      HttpSession session = request.getSession();
      dbSchemaCodSocieta = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
    }

    return dbSchemaCodSocieta;
  }

  /**
   * Accesso WebService, "simulato" workaround con accesso diretto DAO/JDBC
   * 
   *Nota: aggiorna anche il campo ES: idDoc.codEnte="ANE000123" con il valore letto dal DB.
  */
  public LetturaEcResponse readDocumentoCarico(IdDocumento idDoc, HttpServletRequest request)
      throws SQLException, DaoException, RemoteException, FaultType {
    LetturaEcResponse letturaEcResponse = new LetturaEcResponse();
    letturaEcResponse.setCodiceUtente(idDoc.codUtente);
    letturaEcResponse.setCodiceEnte(idDoc.codEnte);
    letturaEcResponse.setTipoUfficio(idDoc.codTipoUfficio);
    letturaEcResponse.setCodiceUfficio(idDoc.codUfficio);
    letturaEcResponse.setTipoServizio("EP");

    Ruolo ruo = new Ruolo();
    Documento doc = new Documento();
    Anagrafica anag = new Anagrafica();
    ConfigurazioneIUV confIuv = new ConfigurazioneIUV();
    Configurazione conf = new Configurazione();
    letturaEcResponse.setRuolo(ruo);
    letturaEcResponse.setConfigurazione(conf);
    letturaEcResponse.setDocumento(doc);
    letturaEcResponse.setAnagrafica(anag);

    // dati documento e anagrafica estratti dal DAO
    EntrateBancaDatiDao controller;
    AnagraficaBollettino res = null;
  	//inizio LP PG21XX04 Leak
    //controller = new EntrateBancaDatiDao(getAnagraficaDataSource().getConnection(),
    //        getAnagraficaDbSchema());
    Connection connection = null; 
    try {		
    	connection = getAnagraficaDataSource().getConnection();
    	controller = new EntrateBancaDatiDao(connection, getAnagraficaDbSchema());
  	//fine LP PG21XX04 Leak
    com.seda.payer.core.bean.Documento docDAO = controller.getDettaglioDocumento2(idDoc.codSocieta,
        idDoc.numeroDocumento, idDoc.codUtente, idDoc.chiaveFlusso, idDoc.codTipologiaServizio,
        idDoc.codUtenteEnte, idDoc.codTipoUfficio, idDoc.codUfficio, idDoc.codImpostaServizio,
        idDoc.chiaveTomb);
    doc.setAnnoEmissione(docDAO.getAnnoEmissione());
    doc.setDataNotifica(dateAjust(docDAO.getDataNotifica()));
    doc.setFlagFatturazioneElettronica(docDAO.getFlagFatturazioneElettronica());
    doc.setIbanAccredito(docDAO.getIbanAccredito());
    doc.setIdentificativoUnivocoVersamento(docDAO.getIdentificativoUnivocoVersamento());
    doc.setImpBollettinoTotaleDocumento(docDAO.getImpBollettinoTotaleDocumento());
    doc.setNumeroBollettinoPagoPA(trimOrNull(docDAO.getNumeroBollettinoPagoPA()));
    doc.setNumeroDocumento(docDAO.getNumeroDocumento());
    doc.setNumeroEmissione(trimOrNull(docDAO.getNumeroEmissione()));
    doc.setIbanAppoggio(docDAO.getIbanAppoggio()); // PG200140
    //PRE_JAVA1.8 SB - inizio
    doc.setCausale(docDAO.getCausale());
    //PRE_JAVA1.8 SB - fine
    //inizio LP PG200360
    doc.setTassonomia(docDAO.getTassonomia());
    //fine LP PG200360
    
    idDoc.codEnte = docDAO.getCodEnte();
    conf.setFlagGenerazioneIUV(docDAO.getFlagGenerazioneIUV());
    conf.setFlagStampaAvviso(docDAO.getFlagStampaAvviso());
    conf.setIdentificativoFlusso(docDAO.getNomeFlusso());
//  23.04.2018 INIZIO - MARINI: aggiunti campi mancanti da visualizzare in jsp 
    confIuv.setIdentificativoDominio(docDAO.getIdDominio());
    confIuv.setAuxDigit(docDAO.getAuxDigit());
    confIuv.setApplicationCode(docDAO.getApplCode());
    confIuv.setSegregationCode(docDAO.getSegrCode());
    // PG200140 - inizio
    confIuv.setCarattereServizio(docDAO.getCarattServizio());
    // PG200140 - fine
    conf.setConfigurazioneIUV(confIuv);
//  23.04.2018 FINE

    anag.setCodiceFiscale(docDAO.getAnaFiscale());
    anag.setDenominazione(docDAO.getAnaDenom());
    anag.setTipoAnagrafica(docDAO.getAnaTipoAnag());
    anag.setCodiceBelfioreComuneNascita(trimOrNull(docDAO.getAnBelfNascita()));

    anag.setDataNascita(dateAjust(docDAO.getAnaDataNascita()));
    anag.setIndirizzoFiscale(docDAO.getAnaIndirizzo());
    anag.setCodiceBelfioreComuneFiscale(trimOrNull(docDAO.getAnaFiscaleAlt()));
    anag.setEmail(docDAO.getAnaMail());
    anag.setEmailPec(docDAO.getAnaMailPec());
    letturaEcResponse.setProvinciaNascita(docDAO.getProvinciaNascita());
    letturaEcResponse.setProvinciaFiscale(docDAO.getProvinciaFiscale());

    ruo.setDescrizioneImpostaServizio(docDAO.getDescImpostaServizio());
    ruo.setCodiceTipologiaServizio(docDAO.getCodTipologiaServizio());
    ruo.setDescrizioneTipologiaServizio(docDAO.getDescTipologiaServizio());

    // copio array scadenze
    Scadenza[] scadenze = new Scadenza[docDAO.getScad().size()];
    int i = 0;
    for (com.seda.payer.core.bean.Scadenza sca : docDAO.getScad()) {
      Scadenza scadenza = new Scadenza();
      scadenza.setDataScadenzaRata(dateAjust(sca.getDataScadenzaRata()));
      scadenza.setIdentificativoUnivocoVersamento(trimOrNull(sca.getIdentificativoUnivocoVersamento()));
      scadenza.setImpBollettinoRata(sca.getImpBollettinoRata());
      scadenza.setNumeroBollettinoPagoPA(sca.getNumeroBollettinoPagoPA());
      scadenza.setNumeroRata(sca.getNumeroRata());

      scadenze[i++] = scadenza;
    }
    letturaEcResponse.setListScadenze(scadenze);


//      // tributi
    RicercaTributiRequest rt = new RicercaTributiRequest();
    rt.setPagina(new Paginazione(100, 1, ""));
    rt.setCodiceSocieta(idDoc.codSocieta);
    rt.setCodiceUtente(idDoc.codUtente);
    rt.setCodiceEnte(idDoc.codUtenteEnte);
    rt.setTipoUfficio(idDoc.codTipoUfficio);
    rt.setCodiceUfficio(idDoc.codUfficio);
    rt.setImpostaServizio(idDoc.codImpostaServizio);
    rt.setNumeroDocumento(idDoc.numeroDocumento);
    
    //inizio LP PG200360
    String listaxml = "";
    boolean bDaoDiretto = true;
    if(bDaoDiretto) {
	    EntrateTributiPage dto = new EntrateTributiPage();
	    
	    dto.setCodiceEnte(rt.getCodiceEnte());
	    dto.setCodiceSocieta(rt.getCodiceSocieta());
	    dto.setCodiceUfficio(rt.getCodiceUfficio());
	    dto.setCodiceUtente(rt.getCodiceUtente());
	    dto.setImpostaServizio(rt.getImpostaServizio());
	    dto.setNumeroDocumento(rt.getNumeroDocumento());
	    dto.setTipoUfficio(rt.getTipoUfficio());
		dto = controller.getListaTributi(dto, "", 0, 0);
		listaxml = dto.getListXml();
	  } else {
    //fine LP PG200360
//    WSCache.logWriter.logDebug("Ricerca Elenco tributi Documento Entrate");
    RicercaTributiResponse tri = WSCache.entrateBDServer.ricercaTributi(rt, request);
//    String csvListaTrib = EntrateUtils.webrowsetToCsv(tri.getListXml());
    //inizio LP PG200360
    	listaxml = tri.getListXml();
	  }
    //fine LP PG200360
    ArrayList<Tributo> tributi = new ArrayList<Tributo>();
	//inizio LP PG21XX04 Leak
    WebRowSet wrsTrib = null;
	//fine LP PG21XX04 Leak
    try {
    //inizio LP PG200360
      //WebRowSet wrsTrib = Convert.stringToWebRowSet(tri.getListXml());
      //inizio LP PG21XX04 Leak
      //WebRowSet wrsTrib = Convert.stringToWebRowSet(listaxml);
      wrsTrib = Convert.stringToWebRowSet(listaxml);
  	  //fine LP PG21XX04 Leak
    //fine LP PG200360
      while (wrsTrib.next()) {
        Tributo tributo = new Tributo();
        tributo.setProgressivoTributo(wrsTrib.getInt(1));
        tributo.setCodiceTributo(wrsTrib.getString(2));
        tributo.setAnnoTributo(wrsTrib.getString(3));
        tributo.setImpTributo(wrsTrib.getBigDecimal(4));
        tributo.setNoteTributo(wrsTrib.getString(7));
        tributo.setCodiceCapitolo(wrsTrib.getString(14));
        tributo.setAccertamento(wrsTrib.getString(15));
        //inizio LP PG210130
        tributo.setArticolo(wrsTrib.getString(16));
        tributo.setIdentificativoDominio(wrsTrib.getString(17));
        tributo.setIBANBancario(wrsTrib.getString(18));
        tributo.setIBANPostale(wrsTrib.getString(19));
        //fine LP PG210130
        tributo.setCodiceTipologiaServizio(wrsTrib.getString(20)); //LP PG22XX05
        tributi.add(tributo);
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
	//inizio LP PG21XX04 Leak
	finally {
		try {
			if(wrsTrib != null) {
				wrsTrib.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//fine LP PG21XX04 Leak
    letturaEcResponse.setListTributi(tributi.toArray(new Tributo[tributi.size()]));
    
    //inizio LP PG200360
  	//inizio LP PG21XX04 Leak
    //ArchivioCarichiDao archivioCarichiDao = new ArchivioCarichiDao(getAnagraficaDataSource().getConnection(), getAnagraficaDbSchema()); 
    ArchivioCarichiDao archivioCarichiDao = new ArchivioCarichiDao(connection, getAnagraficaDbSchema()); 
  	//fine LP PG21XX04 Leak
    
    letturaEcResponse.stampaAvvisoEseguita = false;
	String flagElabStampaAvviso = archivioCarichiDao.getFlagElabStampaAvviso(docDAO.getNomeFlusso());
	if (!flagElabStampaAvviso.equals("") && !flagElabStampaAvviso.equals("N")) {
		letturaEcResponse.stampaAvvisoEseguita = true;
	}
	
	letturaEcResponse.modalitaAggiornamento = false;
	if(letturaEcResponse.stampaAvvisoEseguita) {
		conf.setFlagStampaAvviso("N");
		letturaEcResponse.modalitaAggiornamento = true;
	}
    //fine LP PG200360

    return letturaEcResponse;
  	//inizio LP PG21XX04 Leak
    } catch (DaoException e) {
    	throw new DaoException(e);
    } catch (SQLException e) {
    	throw new SQLException(e);
	} catch (RuntimeException e) {
    	throw new RuntimeException(e);
	} catch (FaultType e) {
    	throw new RuntimeException(e);
	} finally {
		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
  	//fine LP PG21XX04 Leak
  }

  /** adatto formato col punto e 01/01/1900==>null */
  private String dateAjust(String data) {
    if (data == null)
      return null;

    data = data.replace(".", "/");
    if (data.equals("01/01/1900"))
      data = null;
    return data;
  }

  private String trimOrNull(String s) {
    if (s != null)
      return s.trim();
    return null;
  }

  /** Rensponse ad immaggine di quelle generate in risposta da {@link IntegraEcDifferitoServer}*/
  public static class LetturaEcResponse extends
      com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.RequestBase implements
      java.io.Serializable {
    /* dati configurazione per generazione I.U.V. */
    private com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Configurazione configurazione;

    /* dati imposta servizio/tipologia servizio */
    private com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Ruolo ruolo;

    /* dati del documento */
    private com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Documento documento;

    /* dati anagrafica associata al documento */
    private com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Anagrafica anagrafica;

    /* lista scadenze del documento */
    private com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Scadenza[] listScadenze;

    /* lista tributi del documento */
    private com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Tributo[] listTributi;

    String provinciaNascita;
    String provinciaFiscale;
    //inizio LP PG200360
    public boolean stampaAvvisoEseguita = false;
    public boolean modalitaAggiornamento = false;
    //fine LP PG200360

    public String getProvinciaFiscale() {
      return provinciaFiscale;
    }

    public void setProvinciaFiscale(String provinciaFiscale) {
      this.provinciaFiscale = provinciaFiscale;
    }

    public String getProvinciaNascita() {
      return provinciaNascita;
    }

    public void setProvinciaNascita(String provinciaNascita) {
      this.provinciaNascita = provinciaNascita;
    }

    public com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Configurazione getConfigurazione() {
      return configurazione;
    }

    public void setConfigurazione(
        com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Configurazione configurazione) {
      this.configurazione = configurazione;
    }

    public com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Ruolo getRuolo() {
      return ruolo;
    }

    public void setRuolo(
        com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Ruolo ruolo) {
      this.ruolo = ruolo;
    }

    public com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Documento getDocumento() {
      return documento;
    }

    public void setDocumento(
        com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Documento documento) {
      this.documento = documento;
    }

    public com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Anagrafica getAnagrafica() {
      return anagrafica;
    }

    public void setAnagrafica(
        com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Anagrafica anagrafica) {
      this.anagrafica = anagrafica;
    }

    public com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Scadenza[] getListScadenze() {
      return listScadenze;
    }

    public void setListScadenze(
        com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Scadenza[] listScadenze) {
      this.listScadenze = listScadenze;
    }

    public com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Tributo[] getListTributi() {
      return listTributi;
    }

    public void setListTributi(
        com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Tributo[] listTributi) {
      this.listTributi = listTributi;
    }

  }

  public static RecuperaDDLTipServizioResponse recuperaDDLTipologiaServizio(
      RecuperaDDLTipServizioRequest in, HttpServletRequest request) throws RemoteException,
      FaultType {

    RecuperaDDLTipServizioResponse res = WSCache.entrateBDServer.recuperaDDLTipologiaServizio(in,
        request);

    return res;
  }

  static public void loadListaProvince(HttpServletRequest request)
      throws com.seda.payer.pgec.webservice.commons.srv.FaultType, RemoteException {
    RecuperaProvinceResponse getProvinceRes = WSCache.commonsServer.recuperaProvince(request);

    // lista province in sessione
//    String listaProvince = (String) request.getSession().getAttribute("listaProvince");
//    if (listaProvince == null) {
//      listaProvince = getProvinceRes.getListXml();
//      request.getSession().setAttribute("listaProvince", listaProvince);
//    }
    String listaProvince = getProvinceRes.getListXml();
    request.getSession().setAttribute("listaProvince", listaProvince);
  }

  /**
     * Carica le drop-down-list che non cambiano in base alle selezioni
     * dell'utente.
     * 
     * 1) Società - 
     */
  public static void loadStaticXml_DDL(HttpServletRequest request, HttpSession session) {
    if (session.getAttribute("listaSocieta") == null) {
      /*
       * Se non ci sono le carico tramite il WS e poi le metto in sessione
       */
      try {
        GetStaticDDLListsResponse res = WSCache.commonsServer.getStaticDDLLists(
            new GetStaticDDLListsRequest(), request);

        session.setAttribute("listaSocieta", res.getListaSocieta());
        //        session.setAttribute("listaCanaliPagamento", res.getListaCanali());
        //        session.setAttribute("listaStrumenti", res.getListaStrumenti());
        //        session.setAttribute("listaBollettini", res.getListaBollettini());
      } catch (Exception e) {
        throw new RuntimeException("Errore nel caricamento delle DDL statiche", e);
      }
    }
  }

  public static String trovaRegioneComune(String codiceBelfiore) {
    new NotImplementedException("trovaRegioneComune").printStackTrace();

    return "AP";
  }

  public static ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEntes2(
      String codiceSocieta, String codiceUtente, String chiaveEnte, String procName,
      HttpServletRequest request) throws FaultType, RemoteException {
    ConfigUtenteTipoServizioEnteSearchRequest2 in = new ConfigUtenteTipoServizioEnteSearchRequest2();

    in.setCompanyCode(codiceSocieta == null ? "" : codiceSocieta);
    in.setCodiceUtente(codiceUtente == null ? "" : codiceUtente);
    in.setChiaveEnte(chiaveEnte == null ? "" : chiaveEnte);
    in.setProcName(procName == null ? "" : procName);

    ConfigUtenteTipoServizioEnteSearchResponse res = WSCache.configUtenteTipoServizioEnteServer
        .getConfigUtenteTipoServizioEntes2(in, request);

    return res;
  }

  /** Nota: Utilizzo un nuovo servizio anziché  WSCache.commonsServer */
  public static ConfigUtenteTipoServizioEnteSearchResponse getConfigUtenteTipoServizioEnteSearchResponse3(
      ConfigUtenteTipoServizioEnteSearchRequest3 configUtenteTipoServizioEnteSearchRequest3,
      HttpServletRequest request) throws FaultType, RemoteException {

    ConfigUtenteTipoServizioEnteSearchRequest3 in = new ConfigUtenteTipoServizioEnteSearchRequest3();
    in.setCompanyCode(configUtenteTipoServizioEnteSearchRequest3.getCompanyCode() == null ? ""
        : configUtenteTipoServizioEnteSearchRequest3.getCompanyCode());

    ConfigUtenteTipoServizioEnteSearchResponse res = WSCache.configUtenteTipoServizioEnteServer
        .getConfigUtenteTipoServizioEntes3(in, request);
    return res;
  }
  
  //31102017 GG - inizio gestione iddominio
  public static EnteDetailResponse getEnte (EnteDetailRequest detailRequest, HttpServletRequest request) throws FaultType, RemoteException
  {
	return WSCache.enteServer.getEnte(detailRequest, request);
  }
  
  void getIdDominio(HttpServletRequest request, String codiceSocieta, String codiceUtente, String chiaveEnte) throws ConfigurazioneException {
     try {
    	 EnteDetailRequest enteDetailRequest = new EnteDetailRequest(codiceSocieta, codiceUtente, chiaveEnte);
    	 EnteDetailResponse res = EntrateFilteredWs.getEnte(enteDetailRequest, request);
         idDominio = res.getEnte().getCFiscalePIva()==null?"":res.getEnte().getCFiscalePIva().trim();
     } catch (Exception ex) {
        throw new ConfigurazioneException(String.format("Errore nel recupero identificativo dominio per ente "
            + "(societa=%s, utente=%s, ente=%s)", codiceSocieta, codiceUtente, chiaveEnte), ex);
     }
  }
  
  void getConfigurazioneIuv(HttpServletRequest request, String codUtente, String idDominio) throws ConfigurazioneException {
	     try {
	    	//Leggo informazioni da file di configurazione del ws archivio carichi
			PropertiesTree configuration;
//			try {
//				String rootPath = System.getenv("ARCHIVIOCARICHI_WSROOT");
//				if (rootPath == null){
//					throw new Exception("Variabile di sistema ARCHIVIOCARICHI_WSROOT non definita");
//				}
//				configuration = new PropertiesTree(rootPath);
//			} catch (Exception e) {
//				throw new ConfigurazioneException("Errore durante il caricamento della configurazione dalla variabile di sistema ARCHIVIOCARICHI_WSROOT. " + e.getMessage(),e);
//			}
			configuration = (PropertiesTree) (request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));
			
			if (configuration != null){
				ConfigurazioneModello3 configurazioneModello3 = getConfigurazioneIuvFromDB(codUtente, idDominio);
				//archivioCarichiWs.000TO.auxDigit.QRSFRZ79L22E388S
				if (configuration.getProperty(PropertiesPath.auxDigit.format(codUtente,idDominio))!=null) {
					auxDigit = configuration.getProperty(PropertiesPath.auxDigit.format(codUtente,idDominio)).trim();
				} else {
					//se sul file di configurazione non c'è l'aux digit per codUtente e idDominio lo prendo dal DB
					System.out.println("AuxDigit non impostato su file di configurazione per codUtente "+codUtente+" e idDominio "+idDominio);
					System.out.println("Recupero AuxDigit dal DB");
					if(configurazioneModello3.getAuxDigit() != null && !configurazioneModello3.getAuxDigit().equals("")) {
						auxDigit = configurazioneModello3.getAuxDigit();
					}
				}
				String applicationCodeConfig = "";
				if (configuration.getProperty(PropertiesPath.applicationCode.format(codUtente,idDominio, auxDigit))!=null) {
					applicationCodeConfig = configuration.getProperty(PropertiesPath.applicationCode.format(codUtente,idDominio, auxDigit)).trim();
				} else {
					//se sul file di configurazione non c'è l'applicationCode per codUtente, idDominio e auxDigit lo prendo dal DB
					System.out.println("applicationCode non impostato su file di configurazione per codUtente "+codUtente+", idDominio "+idDominio+"e auxDigit "+auxDigit);
					System.out.println("Recupero applicationCode dal DB");
					if(configurazioneModello3.getCodiceSegregazione() != null && !configurazioneModello3.getCodiceSegregazione().equals("")) {
						applicationCodeConfig = configurazioneModello3.getCodiceSegregazione();
					}
				}
				if(auxDigit.equals("0"))
					applicationCode=applicationCodeConfig;
				else if (auxDigit.equals("3"))
					segregationCode=applicationCodeConfig;
			}
	     } catch (Exception ex) {
	        throw new RuntimeException("Errore nel recupero configurazioni iuv per identificativo dominio "
	            + idDominio, ex);
	     }
 }
 //31102017 GG - fine gestione iddominio
  
  ConfigurazioneModello3 getConfigurazioneIuvFromDB(String codiceUtente, String idDominio) {
		ConfigurazioneModello3DaoImpl configurazioneModello3DaoImpl;
		ConfigurazioneModello3 configurazioneModello3 = new ConfigurazioneModello3();
		configurazioneModello3.setCodiceSocieta("");
		configurazioneModello3.setCodiceUtente(codiceUtente);
		configurazioneModello3.setChiaveEnte("");
		configurazioneModello3.setCodiceIdentificativoDominio(idDominio);
		configurazioneModello3.setAuxDigit(auxDigit);
		configurazioneModello3.setCodiceSegregazione("");
		configurazioneModello3.setCarattereDiServizio("");

		//inizio LP PG21XX04 Leak
		Connection conn = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//configurazioneModello3DaoImpl = new ConfigurazioneModello3DaoImpl(getAnagraficaDataSource().getConnection(), getAnagraficaDbSchema());
			conn = getAnagraficaDataSource().getConnection();
			configurazioneModello3DaoImpl = new ConfigurazioneModello3DaoImpl(conn, getAnagraficaDbSchema());
			//fine LP PG21XX04 Leak
			configurazioneModello3 = configurazioneModello3DaoImpl.select(configurazioneModello3);
		} catch (SQLException sqlE) {
			sqlE.printStackTrace();
		} catch (DaoException daoE) {
			daoE.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		//inizio LP PG21XX04 Leak
		finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		//fine LP PG21XX04 Leak
		return configurazioneModello3;
	  }
  


  public static RecuperaDocumentiResponse ricercaDocumenti(RecuperaDocumentiRequest in,
      HttpServletRequest request) throws RemoteException, FaultType {
    RecuperaDocumentiResponse out = WSCache.entrateBDServer.ricercaDocumenti(in, request);
    return out;
  }

  public static ProfiloUtente getProfiloUtente(String profilo) {
    if (profilo.equals("AMMI"))
      return ProfiloUtente.AMMI;
    if (profilo.equals("AMSO"))
      return ProfiloUtente.AMSO;
    if (profilo.equals("AMUT"))
      return ProfiloUtente.AMUT;
    if (profilo.equals("AMEN"))
      return ProfiloUtente.AMEN;
    return null;
  }

  public String getCodUtenteEnte() {
    return codUtenteEnte;
  }

  public String getTipoUfficio() {
    return tipoUfficio;
  }

  public String getCodUfficio() {
    return codUfficio;
  }
  
  public String getIdDominio() {
	    return idDominio;
  }

  public String getAuxDigit() {
	    return auxDigit;
  }
  
  public String getApplicationCode() {
	    return applicationCode;
  }
  
  public String getSegregationCode() {
	    return segregationCode;
  }
  
  public String getIdentificativoFlusso() {
	    return identificativoFlusso;
  }
//PG200140 - inizio
  public String getCarattereServizio() {
	    return carattereServizio;
  }
//PG200140 - fine
  
/** moltiplico per 100.0 */
  static BigDecimal euroToCent(BigDecimal euro) {
    BigDecimal centesimi = euro.multiply(new BigDecimal("100.0"));
    return centesimi;
  }

  /** divido per 100.0 */
  static BigDecimal centToEuro(BigDecimal centesimi) {
    BigDecimal euro = centesimi.divide(new BigDecimal("100.0"));
    return euro;
  }
}
