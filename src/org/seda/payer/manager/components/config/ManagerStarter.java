package org.seda.payer.manager.components.config;

import javax.servlet.ServletContext;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.util.ValidationMessages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesNodeException;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.compatibility.SystemVariable;
import com.seda.j2ee5.maf.core.application.ApplicationStarter;
import com.seda.j2ee5.maf.core.application.ApplicationStarterException;

public class ManagerStarter implements ApplicationStarter {
	/* (non-Javadoc)
	 * @see com.seda.j2ee5.maf.core.application.ApplicationStarter#start(javax.servlet.ServletContext)
	 */
	
	public static PropertiesTree configuration;
	
	public Object start(ServletContext context) throws ApplicationStarterException {
		
		String rootPath = context.getInitParameter(ManagerKeys.CONFIG_FILE);
		if (rootPath==null) {
			SystemVariable sv = new SystemVariable();
			rootPath=sv.getSystemVariableValue(ManagerKeys.ENV_CONFIG_FILE);
			sv=null;
		} 
		if (rootPath!=null) {
			
			try {
				// caricamento configurazioni esterne
				configuration = new PropertiesTree(rootPath);
				context.setAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE, configuration);
				if (configuration!=null) {
					// Imposta nel contesto il numero di righe per pagina da utilizzare
					if (configuration.getProperty(PropertiesPath.rowsPerPage.format())==null)
						context.setAttribute(ManagerKeys.ROWSPERPAGE,"5;10;15");
					else
						context.setAttribute(ManagerKeys.ROWSPERPAGE,configuration.getProperty(PropertiesPath.rowsPerPage.format()));
					// Imposta gli anni per popolare le dropdownlist delle date
					if(configuration.getProperty(PropertiesPath.ddlDateAnnoDa.format()) == null)
						context.setAttribute(ManagerKeys.DDL_DATE_ANNO_DA,"2009");
					else
						context.setAttribute(ManagerKeys.DDL_DATE_ANNO_DA,configuration.getProperty(PropertiesPath.ddlDateAnnoDa.format()));
					if(configuration.getProperty(PropertiesPath.ddlDateAnnoA.format()) == null)
						context.setAttribute(ManagerKeys.DDL_DATE_ANNO_A,"2020");
					else
						context.setAttribute(ManagerKeys.DDL_DATE_ANNO_A,configuration.getProperty(PropertiesPath.ddlDateAnnoA.format()));

					//mega bottone
					if(configuration.getProperty(PropertiesPath.presenza_megab.format()) == null)
						context.setAttribute(ManagerKeys.PRESENZA_MEGAB,"N");
					else
						context.setAttribute(ManagerKeys.PRESENZA_MEGAB,configuration.getProperty(PropertiesPath.presenza_megab.format()));
					
					//parametro max numero righe per download
					if(configuration.getProperty(PropertiesPath.max_righe_download.format()) == null)
						context.setAttribute(ManagerKeys.MAXRIGHE_DOWNLOAD, new Integer(0));
					else
						context.setAttribute(ManagerKeys.MAXRIGHE_DOWNLOAD, new Integer(configuration.getProperty(PropertiesPath.max_righe_download.format())));
					
					
					//parametro max numero righe per download
					if(configuration.getProperty(PropertiesPath.max_righe_download_borsellino.format()) == null)
						context.setAttribute(ManagerKeys.MAXRIGHE_DOWNLOAD_BORSELLINO, new Integer(0));
					else
						context.setAttribute(ManagerKeys.MAXRIGHE_DOWNLOAD_BORSELLINO, new Integer(configuration.getProperty(PropertiesPath.max_righe_download_borsellino.format())));
					
					//parametri PER chiamata al web service NOTIFICHE
					if(configuration.getProperty(PropertiesPath.notifiche_Url.format()) == null)
						context.setAttribute(ManagerKeys.NOTIFICHE_URL, new String(""));
					else
						context.setAttribute(ManagerKeys.NOTIFICHE_URL, new String(configuration.getProperty(PropertiesPath.notifiche_Url.format())));
					
					if(configuration.getProperty(PropertiesPath.notifiche_Bearer.format()) == null)
						context.setAttribute(ManagerKeys.NOTIFICHE_BEARER, new String(""));
					else
						context.setAttribute(ManagerKeys.NOTIFICHE_BEARER, new String(configuration.getProperty(PropertiesPath.notifiche_Bearer.format())));
					
					if(configuration.getProperty(PropertiesPath.notifiche_Societa.format()) == null)
						context.setAttribute(ManagerKeys.NOTIFICHE_SOCIETA, new String(""));
					else
						context.setAttribute(ManagerKeys.NOTIFICHE_PDF_ROOT, new String(configuration.getProperty(PropertiesPath.notifiche_pdf_Root.format())));
					if(configuration.getProperty(PropertiesPath.notifiche_pdf_Root.format()) == null)
						context.setAttribute(ManagerKeys.NOTIFICHE_PDF_ROOT, new String(""));
					else
						context.setAttribute(ManagerKeys.NOTIFICHE_PDF_ROOT, new String(configuration.getProperty(PropertiesPath.notifiche_pdf_Root.format())));
					
					
					//path temp download csv monitoraggio transazioni
					if(configuration.getProperty(PropertiesPath.path_filetemp.format()) == null)
						context.setAttribute(ManagerKeys.PATH_FILETEMP, new String(""));
					else
						context.setAttribute(ManagerKeys.PATH_FILETEMP, new String(configuration.getProperty(PropertiesPath.path_filetemp.format())));
					


					/*
					 * Salva nel contesto tutti i messaggi di validazione configurati in ValidationMessages.
					 */
					loadValidationMessages_context(context);
					System.out.println("inizio lettura configuraziuone WS");
					WSCache.initiateLogger(configuration.getProperty(PropertiesPath.wsLogger.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateCommonsServer(configuration.getProperty(PropertiesPath.wsCommonsServer.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateNotificheServer(configuration.getProperty(PropertiesPath.wsNotificheServer.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateGatewaysServer(configuration.getProperty(PropertiesPath.wsGatewaysServer.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateCompanyServer(configuration.getProperty(PropertiesPath.wsCompany.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateBollettinoServer(configuration.getProperty(PropertiesPath.wsBollettino.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateTotemTipologiaImpostaServer(configuration.getProperty(PropertiesPath.wsTotemTipologiaImposta.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateMenuServer(configuration.getProperty(PropertiesPath.wsMenu.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateSecurityServer(configuration.getProperty(PropertiesPath.wsSecurity.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateAnagServiziServer(configuration.getProperty(PropertiesPath.wsAnagServizi.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateAnagProvComServer(configuration.getProperty(PropertiesPath.wsAnagProvCom.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateRendicontaEnteServer(configuration.getProperty(PropertiesPath.wsInviaFlussorend.format(PropertiesPath.defaultnode.format())));
					WSCache.securityIV = configuration.getProperty(PropertiesPath.webEncryptionIv.format());
					WSCache.securityKey = configuration.getProperty(PropertiesPath.webEncryptionKey.format());
					WSCache.directoryFlussi = configuration.getProperty(PropertiesPath.directoryFlussi.format());

					//WSCache.initiateRiversamentoServer(configuration.getProperty(PropertiesPath.wsRiversamento.format(PropertiesPath.defaultnode.format())));				
					WSCache.initiateBeneficiarioServer(configuration.getProperty(PropertiesPath.wsBeneficiario.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateAssocImpBenServer(configuration.getProperty(PropertiesPath.wsAssocImpBen.format(PropertiesPath.defaultnode.format())));					
					WSCache.initiateConvenzioneImpServer(configuration.getProperty(PropertiesPath.wsConvenzioneImp.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateAssocBenServer(configuration.getProperty(PropertiesPath.wsAssocBen.format(PropertiesPath.defaultnode.format())));
					
					//WSCache.initiateEccedenzeServer(configuration.getProperty(PropertiesPath.wsEccedenze.format(PropertiesPath.defaultnode.format())));

					WSCache.initiateEntrateBDServer(configuration.getProperty(PropertiesPath.wsEntrateBD.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateRuoliBDServer(configuration.getProperty(PropertiesPath.wsRuoliBD.format(PropertiesPath.defaultnode.format())));
 
					WSCache.initiateAnagEnteServer(configuration.getProperty(PropertiesPath.wsAnagEnte.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateCanPagamentoServer(configuration.getProperty(PropertiesPath.wsCanPagamento.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateTipologiaServizioServer(configuration.getProperty(PropertiesPath.wsTipologiaServizio.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateCartaPagamentoServer(configuration.getProperty(PropertiesPath.wsCartaPagamento.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateAdminUsersServer(configuration.getProperty(PropertiesPath.wsAdminusers.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateUserServer(configuration.getProperty(PropertiesPath.wsUser.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateAbilitaSistemiEsterniSecureSiteServer(configuration.getProperty(PropertiesPath.wsAbilitaSistemiEsterniSecureSite.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateImpostaServizioServer(configuration.getProperty(PropertiesPath.wsImpostaServizio.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateConfigUtenteTipoServizioServer(configuration.getProperty(PropertiesPath.wsConfigUtenteTipoServizio.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateGatewayPagamentoServer(configuration.getProperty(PropertiesPath.wsGatewayPagamento.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateConfigSessCarrelloSocCanPagamentoServer(configuration.getProperty(PropertiesPath.wsConfigSessCarrelloSocCanPagamento.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateRangeAbiUtenteTipoServizioServer(configuration.getProperty(PropertiesPath.wsRangeAbiUtenteTipoServizio.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateRangeAbiUtenteTipoServizioEnteServer(configuration.getProperty(PropertiesPath.wsRangeAbiUtenteTipoServizioEnte.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateConfigUtenteTipoServizioEnteServer(configuration.getProperty(PropertiesPath.wsConfigUtenteTipoServizioEnte.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateEnteServer(configuration.getProperty(PropertiesPath.wsEnte.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateAbilitazTributiServer(configuration.getProperty(PropertiesPath.wsAbilitazTributi.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateFunzPagTpServEnteServer(configuration.getProperty(PropertiesPath.wsFunzPagTpServEnte.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateFunzPagTpServServer(configuration.getProperty(PropertiesPath.wsFunzPagTpServ.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateCostiNotificaServer(configuration.getProperty(PropertiesPath.wsCostiNotifica.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateConfigEstrattoContoServer(configuration.getProperty(PropertiesPath.wsConfigEstrattoConto.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateConfigBancaDatiServer(configuration.getProperty(PropertiesPath.wsConfigBancaDati.format(PropertiesPath.defaultnode.format())));
					//inizio LP PG200060
					WSCache.initiateConfigRtEnteServer(configuration.getProperty(PropertiesPath.wsConfigRtEnte.format(PropertiesPath.defaultnode.format())));
					//fine LP PG200060
					WSCache.initiateCostiTransazioneBancaServer(configuration.getProperty(PropertiesPath.wsCostiTransazioneBanca.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateConfRendUtenteServizioEnteServer(configuration.getProperty(PropertiesPath.wsConfRendUtenteServizioEnte.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateConfRendUtenteServizioServer(configuration.getProperty(PropertiesPath.wsConfRendUtenteServizio.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateMIPServer(configuration.getProperty(PropertiesPath.wsMIP.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateConfigurazioneOtticoServer(configuration.getProperty(PropertiesPath.wsConfigurazioneOttico.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateManagerOtticoServer(configuration.getProperty(PropertiesPath.wsManagerOttico.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateImpostaSoggiornoConfigServer(configuration.getProperty(PropertiesPath.wsImpostaSoggiornoConfigServer.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateImpostaSoggiornoServer(configuration.getProperty(PropertiesPath.wsImpostaSoggiornoServer.format(PropertiesPath.defaultnode.format())));
					//WSCache.initiateContoGestioneServer(configuration.getProperty(PropertiesPath.wsContoGestioneServer.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateFiltriEstrattoContoServer(configuration.getProperty(PropertiesPath.wsFiltriEstrattoContoServer.format(PropertiesPath.defaultnode.format())));
					WSCache.initiateIntegraEntePgServer(configuration.getProperty(PropertiesPath.wsIntegraente.format(PropertiesPath.defaultnode.format())));
					if(configuration.getProperty(PropertiesPath.wsIntegraEcDifferito.format(PropertiesPath.defaultnode.format()))!=null)
						WSCache.initiateIntegraEcDifferitoServer(configuration.getProperty(PropertiesPath.wsIntegraEcDifferito.format(PropertiesPath.defaultnode.format())));

					WSCache.initiateEmailServer(configuration.getProperty(PropertiesPath.wsEmail.format(PropertiesPath.defaultnode.format())));
					//inizio LP PG200360
					WSCache.initiateConfigTassonomia(configuration.getProperty(PropertiesPath.wsConfigTassonomia.format(PropertiesPath.defaultnode.format())));
					//inizio LP PG210040
					WSCache.initiateConfigGruppo(configuration.getProperty(PropertiesPath.wsConfigGruppo.format(PropertiesPath.defaultnode.format())));
					//fine LP PG210040
					System.out.println("Fine lettura Configurazione WS");
					//fine LP PG200360
				}

			} catch (PropertiesNodeException x) {
				throw new ApplicationStarterException(x);
			} catch (Exception x) {
				throw new ApplicationStarterException(x);
			}
		} else {
			System.out.println("Errore durante il caricamento della variabile d'ambiente");
			throw new ApplicationStarterException("");
		}
		return null;
	}
	
	private void loadValidationMessages_context(ServletContext context)
	{
		/*
		 * Salvo nel contesto i messaggi di validazione configurati in ValidationMessages.
		 */
		try
		{
			for (ValidationMessages msg : ValidationMessages.values())
			{
				String key = msg.name();
				String propValue = msg.format();
				context.setAttribute(key, propValue);	
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERRORE loadValidationMessages_context: Caricamento ValidationMessages FALLITO");
		}
	}
}