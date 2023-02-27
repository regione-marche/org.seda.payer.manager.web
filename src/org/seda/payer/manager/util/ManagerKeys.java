/**
 * 
 */
package org.seda.payer.manager.util;

/**
 * @author Seda Lab
 *
 */
public class ManagerKeys {
	
	public static final String CONFIG_FILE = "configroot";
	public static final String ENV_CONFIG_FILE = "MANAGER_CONFIG_ROOT";	
	public static final String CONTEXT_PROPERTIES_TREE = "org.seda.payer.manager.CONTEXT_PROPERTIES_TREE";
	public static final String CONTEXT_LOGGER = "org.seda.payer.manager.CONTEXT_LOGGER";
	
	
	//variabile request per gestione azioni con il flow
	public static final String REQUEST_ACTION_FLOW = "flowaction";
	
	//variabili di sessione comuni
	public static final String ROWSPERPAGE ="rowsPerPage";
	public static final String DDL_DATE_ANNO_DA = "ddlDateAnnoDa";
	public static final String DDL_DATE_ANNO_A = "ddlDateAnnoA";
	public static final String FEDERA_FILTER = "federa_filter";
	public static final String GLOBAL_RULE_SET_MAP = "GlobalRuleSetMap";
	public static final String NUMERO_PROFILI = "numero_profili";
	public static final String PROFILI_UTENTE = "profili_utente";
	
	public static final String CHIAVE_MULTIPROFILO = "PROF";
	public static final String PROFILO_UTENTEPIVA = "profiloutentePIVA";
	//mega bottone
	public static final String PRESENZA_MEGAB = "presenza_megab";
	public static final String MAXRIGHE_DOWNLOAD = "maxrighe_download";
	public static final String PATH_FILETEMP = "path_filetemp";
	public static final String MAXRIGHE_DOWNLOAD_BORSELLINO = "maxrighe_download_borsellino";

	//web service NOTIFICHE per lista e dettaglio
	public static final String NOTIFICHE_URL = "notifiche_Url";
	public static final String NOTIFICHE_BEARER = "notifiche_Bearer";
	public static final String NOTIFICHE_SOCIETA = "notifiche_Societa";
	public static final String NOTIFICHE_PDF_ROOT = "notifiche_pdf_Root";
	
	public static final String DBSCHEMA_CODSOCIETA = "dbschema_codsocieta";
	
	//Scadenza Password
	public static final String GG_AVVISO_SCADENZA_PASSWORD = "ggAvvisoScadenzaPassword";
	public static final String GG_SCADENZA_PASSWORD = "ggScadenzaPassword";

	public static final String SESSION_RESPONSIVE = "flagResponsive"; //MODIFICHE RESPONSIVE
	
	public static final String NASCONDI_CREDENZIALI = "nascondiCredenziali"; //PG200300
	
}
