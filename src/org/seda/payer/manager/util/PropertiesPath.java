/**
 * 
 */
package org.seda.payer.manager.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * Classe per la gestione della richiesta di chiavi per la ricerca delle configurazioni
 * nell'albero delle proprieta
 *  
 * @author Seda Lab
 *
 */
public enum PropertiesPath {
	webEncryptionIv,
	webEncryptionKey,
	ddlDateAnnoDa,
	ddlDateAnnoA,
	applicazioniDaAbilitare,
	applicazioniAbilitate,
	webLogger,
	wsSecurity,
	wsAdminusers,
	wsEmail,
	wsSms,
	wsLogger,	
	wsCommonsServer,
	wsNotificheServer,
	wsGatewaysServer,
	wsInviaFlussorend,
	directoryFlussi,
	defaultListRows,
	wsCompany,
	wsBollettino,
	wsTotemTipologiaImposta,
	urlOnline,
	defaultnode,
	wsMenu,
	wsAnagServizi,
	wsAnagProvCom,  

	wsRiversamento,
	wsBeneficiario,
	wsAssocImpBen,
	wsConvenzioneImp,
	wsAssocBen,

	wsEccedenze,
	
	wsEntrateBD,
	wsRuoliBD,
		
	wsAnagEnte,
	wsCanPagamento,
	wsTipologiaServizio,
	wsCartaPagamento,
	wsUser,
	wsAbilitaSistemiEsterniSecureSite,
	wsImpostaServizio,
	wsConfigUtenteTipoServizio,
	wsGatewayPagamento,
	wsConfigSessCarrelloSocCanPagamento,
	wsRangeAbiUtenteTipoServizio,
	rowsPerPage,
	wsConfigUtenteTipoServizioEnte,
	wsRangeAbiUtenteTipoServizioEnte,
	wsFunzPagTpServEnte,
	wsFunzPagTpServ,
	wsConfigBancaDati,
	wsConfigEstrattoConto,
	wsCostiNotifica,
	wsEnte,
	wsAbilitazTributi,
	wsCostiTransazioneBanca,
	wsConfRendUtenteServizioEnte,
	wsConfRendUtenteServizio,
	wsMIP,
	wsFiltriEstrattoContoServer,
	wsConfigurazioneOttico,
	wsManagerOttico,
	wsImpostaSoggiornoConfigServer,
	wsImpostaSoggiornoServer,
	wsContoGestioneServer,
	wsIntegraente,
  wsIntegraEcDifferito,
	templateBase,
	templateActions,
	dataSource,
	path_cubo,
	
	presenza_megab, 
	max_righe_download,
	path_filetemp,
	servizimanager,
	serviziweb,
	max_righe_download_borsellino,
	
	societa,
	dataSourceWallet,
	dataSourceSchemaWallet,
	dataSourceSepa,
	dataSourceSchemaSepa,
	dataSourceBlackBox,
	dataSourceSchemaBlackBox,
	dataSourceEasyBridge,
	dataSourceSchemaEasyBridge,
	scriptGoogle,
	bolzanoURL,
	birthomeengine,
	birthomelogs,
	birtImagePath,
	birtOutputPath,
	birtDesignPath,
	birtImagePathRelative,
	pathLogoPAReport,
	paramPathLogoSocieta,
	auxDigit,
	applicationCode,
	notifiche_Url,
	notifiche_Bearer,
	notifiche_Societa,
	notifiche_pdf_Root,
	
	welcomekitTemplatePath,
	welcomekitOutputPath,
	emailWelcomekitFrom,
	emailWelcomekitSubject,
	emailWelcomekitBody,
	
	ggAvvisoScadenzaPassword,
	
	directoryGDC,
	
	directoryCSV, 				//SVILUPPO_001_LP_26.08.2019
	flagResponsive,				//Modifiche Responsive
	quadraturaPSPInputPath,		//PG190190
	quadraturaPSPOutputPath		//PG190190
	
	//inizio LP PG200060
	,
	//Login Federa Regione Marche
	urlCheck,		
	urlLogin,
	urlValidate,
	urlRichiesta,
	keystorePath,
	keystoreType,
	pwdKeystore,
	aliasCertificate,
	pwdCertificate,
	wsSso,
	idsito,
	returnUrl,
	codiceSocieta,
	
	//Login Federa Regione Marche
	flagFedera,
	//PG180160
	wsConfigRtEnte,
	//fine LP PG200060
	//PG190480_001 SB - inizio
	urlSito,
	urlDefault,
	urlSceltaAut,
	//PG190480_001 SB - fine
	dataSourceUffici,
	dataSourceSchemaUffici

	//PG2000XX_007
	, dataSourceLog				
	, dataSourceSchemaLog
	//FINE PG2000XX_007
	
	, wsValidatoreMailUrl
	, wsValidatoreMailBearer
	, wsValidatoreMailOperation
	, nascondiCredenziali	//PG200300
	, urlRedirectCAS  //EP200510
	//inizio PG200360
	, wsConfigTassonomia
	//fine PG200360
	, pathStampaAvviso	//PG200450
	//inizio PG210040
	, wsConfigGruppo
	//fine PG210040
	, tagHtmlNonAccettati

	, listaVisibileProfili //LUCAP_28092021

	//inizio LP PG21X007
	, wsLogRequest
	//fine LP PG21X007

	, directoryIoItaliaCSV  //PG210160
	, directoryIoItaliaCSVScartati
	, directoryIoItaliaCSVValidi
	, wsAppIOEndpointURL
	, wsAppIOEndpointDefaultURL
	, allowElementsOWASP
	, skipTarget

	;
	
	private static ResourceBundle rb;
    /**
     * @param args - Gli argomenti da passare come variabile al messaggio.
     * @return il messaggio formattato
     */
    public String format(Object... args ) {
        synchronized(PropertiesPath.class) {
            if(rb==null)
                rb = ResourceBundle.getBundle(PropertiesPath.class.getName());
            return MessageFormat.format(rb.getString(name()),args);
        }
    }
	
	
}