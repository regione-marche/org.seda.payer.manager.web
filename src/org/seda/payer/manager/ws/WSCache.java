package org.seda.payer.manager.ws;

public class WSCache {
	public static LogWriter logWriter = null;
	public static CommonsServer commonsServer = null;
	public static EMailSender emailServer = null; //PG170280 CT
	
	public static NotificheServer notificheServer = null;
	//	public static NotificheServerComponent notificheServerComponent = null; MATTEO NOTIFICHE
	public static GatewaysServer gatewaysServer = null;
	public static CompanyServer companyServer = null;
	public static MenuServer menuServer = null;
	public static SecurityServer securityServer = null;
	public static String securityIV;
	public static String securityKey;
	public static String directoryFlussi;
	public static BollettinoServer bollettinoServer= null;
	public static TotemTipologiaImpostaServer totemTipologiaImpostaServer= null;

	public static AnagServiziServer anagServiziServer = null;
	public static AnagProvComServer anagProvComServer = null;
	public static RendicontaEnteServer rendicontaEnteServer = null;
	public static AnagEnteServer anagEnteServer = null;
	public static CanPagamentoServer canPagamentoServer = null;
	public static TipologiaServizioServer tipologiaServizioServer=null;
	public static CartaPagamentoServer cartaPagamentoServer=null;
	public static AdminUsersServer adminUsersServer = null;
	public static UserServer userServer = null;
	public static AbilitaSistemiEsterniSecureSiteServer abilitaSistemiEsterniSecureSiteServer=null;
	public static ImpostaServizioServer impostaServizioServer = null;
	public static ConfigUtenteTipoServizioServer configUtenteTipoServizioServer = null;
	public static GatewayPagamentoServer gatewayPagamentoServer = null;
	public static ConfigSessCarrelloSocCanPagamentoServer configSessCarrelloSocCanPagamentoServer = null;
	public static RangeAbiUtenteTipoServizioServer rangeAbiUtenteTipoServizioServer = null;
	public static ConfigUtenteTipoServizioEnteServer configUtenteTipoServizioEnteServer = null;
	public static RangeAbiUtenteTipoServizioEnteServer rangeAbiUtenteTipoServizioEnteServer = null;
	public static CostiNotificaServer costiNotificaServer = null;
	public static ConfigEstrattoContoServer configEstrattoContoServer = null;
	public static ConfigBancaDatiServer configBancaDatiServer = null;
	public static FunzPagTpServServer funzPagTpServServer = null;
	public static FunzPagTpServEnteServer funzPagTpServEnteServer = null;
	public static EnteServer enteServer = null;
	public static AbilitaCanalePagamentoTipoServizioEnteServer abilitazTributiServer = null;
	public static CostiTransazioneBancaServer costiTransazioneBancaServer = null;
	public static ConfRendUtenteServizioEnteServer confRendUtenteServizioEnteServer = null;
	public static ConfRendUtenteServizioServer confRendUtenteServizioServer = null;
	public static MIPServer mipServer = null;
	//public static RiversamentoServer riversamentoServer = null;
	public static BeneficiarioServer beneficiarioServer = null;
	public static AssocImpBenServer assocImpBenServer = null;
	public static AssocBenServer assocBenServer = null;
	public static ConvenzioneImpServer convenzioneImpServer = null;
	//public static EccedenzeServer eccedenzeServer = null;
	public static EntrateBDServer entrateBDServer = null;
	public static RuoliBDServer ruoliBDServer = null;
	public static OtticoConfigurazioneServer configurazioneServer = null;
	public static OtticoManagerServer managerOtticoServer = null;
	public static ImpostaSoggiornoConfigServer impostaSoggiornoConfigServer = null;
	public static ImpostaSoggiornoServer impostaSoggiornoServer = null;
	//public static ContoGestioneServer contoGestioneServer = null;
	public static FiltriEstrattoContoServer filtriEstrattoContoServer = null;
	//inizio LP PG200060
	public static ConfigRtEnteServer configRtEnteServer = null;
//	fine LP PG200060
	public static IntegraEntePgServer integraEntePgServer= null;
	public static boolean bSetIntegraEntePgServerDynamic = false;
  public static IntegraEcDifferitoServer integraEcDifferitoServer= null;
	//inizio LP PG200360
	public static ConfigTassonomiaServer configTassonomiaServer = null;
	//	fine LP PG200360
	//inizio LP PG210040
	public static ConfigGruppoServer configGruppoServer = null;
	//	fine LP PG210040

	public static boolean initiateConfRendUtenteServizioEnteServer(String address) throws Exception {
		try {
			confRendUtenteServizioEnteServer = new ConfRendUtenteServizioEnteServer(address);
		} catch (Exception e) {
			throw e;
		}

		return (confRendUtenteServizioEnteServer != null);
	}

	public static boolean initiateConfRendUtenteServizioServer(String address) throws Exception {
		try {
			confRendUtenteServizioServer = new ConfRendUtenteServizioServer(address);
		} catch (Exception e) {
			throw e;
		}

		return (confRendUtenteServizioServer != null);
	}

	public static boolean initiateLogger(String address) throws Exception {
		try {
			logWriter = new LogWriter(address);
		} catch (Exception e) {
			throw e;
		}

		return (logWriter != null);
	}


	public static boolean initiateNotificheServer(String address) throws Exception {
		try {
			notificheServer = new NotificheServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (notificheServer != null);	
	}

	//	public static boolean initiateNotificheServerComponent(String address) throws Exception { MATTEO NOTIFICHE
	//		try {
	//			notificheServerComponent = new NotificheServerComponent(address);
	//		} catch (Exception e) {
	//			throw e;
	//		}
	//		return (notificheServerComponent != null);	
	//	}

	public static boolean initiateGatewaysServer(String address) throws Exception {
		try {
			gatewaysServer = new GatewaysServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (gatewaysServer != null);	
	}

	public static boolean initiateCommonsServer(String address) throws Exception {
		try {
			commonsServer = new CommonsServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (commonsServer != null);	
	}
	
	public static boolean initiateEmailServer(String address) throws Exception {
		try {
			emailServer = new EMailSender(address);
		} catch (Exception e) {
			throw e;
		}
		return (emailServer != null);	
	}
		
	public static boolean initiateCompanyServer(String address) throws Exception {
		try {
			companyServer = new CompanyServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (companyServer != null);	
	}

	public static boolean initiateBollettinoServer(String address) throws Exception {
		try {
			bollettinoServer = new BollettinoServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (bollettinoServer != null);	
	}

	public static boolean initiateTotemTipologiaImpostaServer(String address) throws Exception {
		try {
			totemTipologiaImpostaServer = new TotemTipologiaImpostaServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (totemTipologiaImpostaServer != null);	
	}
	
	public static boolean initiateMenuServer(String address) throws Exception {
		try {
			menuServer = new MenuServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (menuServer != null);	
	}

	public static boolean initiateSecurityServer(String address) throws Exception {
		try {
			securityServer = new SecurityServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (securityServer != null);	
	}


	public static boolean initiateAnagServiziServer(String address) throws Exception {
		try {
			anagServiziServer = new AnagServiziServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (anagServiziServer != null);	
	}

	public static boolean initiateAnagProvComServer(String address) throws Exception {
		try {
			anagProvComServer = new AnagProvComServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (anagProvComServer != null);	
	}

	public static boolean initiateRendicontaEnteServer(String address) throws Exception {
		try {
			rendicontaEnteServer = new RendicontaEnteServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (rendicontaEnteServer != null);	
	}

	/*
	public static boolean initiateRiversamentoServer(String address) throws Exception {
		try {
			riversamentoServer = new RiversamentoServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (riversamentoServer != null);	
	}
	*/

	public static boolean initiateBeneficiarioServer(String address) throws Exception {
		try {
			beneficiarioServer = new BeneficiarioServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (beneficiarioServer != null);	
	}


	public static boolean initiateAssocImpBenServer(String address) throws Exception {
		try {
			assocImpBenServer = new AssocImpBenServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (assocImpBenServer != null);	
	}

	public static boolean initiateAssocBenServer(String address) throws Exception {
		try {
			assocBenServer = new AssocBenServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (assocBenServer != null);	
	}

	public static boolean initiateConvenzioneImpServer(String address) throws Exception {
		try {
			convenzioneImpServer = new ConvenzioneImpServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (convenzioneImpServer != null);	
	}

	/*
	public static boolean initiateEccedenzeServer(String address) throws Exception {
		try {
			eccedenzeServer = new EccedenzeServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (eccedenzeServer != null);	
	}*/


	public static boolean initiateEntrateBDServer(String address) throws Exception {
		try {
			entrateBDServer = new EntrateBDServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (entrateBDServer != null);	
	}

	public static boolean initiateRuoliBDServer(String address) throws Exception {
		try {
			ruoliBDServer = new RuoliBDServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (ruoliBDServer != null);	
	}

	public static boolean initiateAnagEnteServer(String address) throws Exception {
		try {
			anagEnteServer = new AnagEnteServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (anagEnteServer != null);	
	}

	public static boolean initiateCanPagamentoServer(String address) throws Exception {
		try {
			canPagamentoServer = new CanPagamentoServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (canPagamentoServer != null);	
	}

	public static boolean initiateTipologiaServizioServer(String address) throws Exception {
		try {
			tipologiaServizioServer = new TipologiaServizioServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (tipologiaServizioServer != null);	
	}

	public static boolean initiateCartaPagamentoServer(String address) throws Exception {
		try {
			cartaPagamentoServer = new CartaPagamentoServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (cartaPagamentoServer != null);	
	}

	public static boolean initiateAdminUsersServer (String address) throws Exception
	{
		try
		{
			adminUsersServer = new AdminUsersServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (adminUsersServer != null);
	}

	public static boolean initiateUserServer (String address) throws Exception
	{
		try
		{
			userServer = new UserServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (userServer != null);
	}

	public static boolean initiateAbilitaSistemiEsterniSecureSiteServer(String address) throws Exception {
		try {
			abilitaSistemiEsterniSecureSiteServer = new AbilitaSistemiEsterniSecureSiteServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (abilitaSistemiEsterniSecureSiteServer != null);	
	}

	public static boolean initiateImpostaServizioServer(String address) throws Exception {
		try {
			impostaServizioServer = new ImpostaServizioServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (impostaServizioServer != null);	
	}

	public static boolean initiateConfigUtenteTipoServizioServer(String address) throws Exception {
		try {
			configUtenteTipoServizioServer = new ConfigUtenteTipoServizioServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (configUtenteTipoServizioServer != null);	
	}

	public static boolean initiateGatewayPagamentoServer (String address) throws Exception
	{
		try
		{
			gatewayPagamentoServer = new GatewayPagamentoServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (gatewayPagamentoServer != null);
	}

	public static boolean initiateConfigSessCarrelloSocCanPagamentoServer (String address) throws Exception
	{
		try
		{
			configSessCarrelloSocCanPagamentoServer = new ConfigSessCarrelloSocCanPagamentoServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (configSessCarrelloSocCanPagamentoServer != null);
	}

	public static boolean initiateRangeAbiUtenteTipoServizioServer (String address) throws Exception
	{
		try
		{
			rangeAbiUtenteTipoServizioServer = new RangeAbiUtenteTipoServizioServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (rangeAbiUtenteTipoServizioServer != null);
	}


	public static boolean initiateRangeAbiUtenteTipoServizioEnteServer (String address) throws Exception
	{
		try
		{
			rangeAbiUtenteTipoServizioEnteServer = new RangeAbiUtenteTipoServizioEnteServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (rangeAbiUtenteTipoServizioEnteServer != null);
	}

	public static boolean initiateConfigUtenteTipoServizioEnteServer (String address) throws Exception
	{
		try
		{
			configUtenteTipoServizioEnteServer = new ConfigUtenteTipoServizioEnteServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (configUtenteTipoServizioEnteServer != null);
	}

	public static boolean initiateCostiNotificaServer (String address) throws Exception
	{
		try
		{
			costiNotificaServer = new CostiNotificaServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (costiNotificaServer != null);
	}
	public static boolean initiateConfigEstrattoContoServer (String address) throws Exception
	{
		try
		{
			configEstrattoContoServer = new ConfigEstrattoContoServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (configEstrattoContoServer != null);
	}
	//inizio LP PG200060
	public static boolean initiateConfigRtEnteServer (String address) throws Exception
	{
		try
		{
			configRtEnteServer = new ConfigRtEnteServer(address) ;
			
		}catch (Exception e)
		{
			throw e;
		}
		return (configRtEnteServer != null);
	}
	//fine LP PG200060
	public static boolean initiateConfigBancaDatiServer (String address) throws Exception
	{
		try
		{
			configBancaDatiServer = new ConfigBancaDatiServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (configBancaDatiServer != null);
	}
	public static boolean initiateFunzPagTpServServer (String address) throws Exception
	{
		try
		{
			funzPagTpServServer = new FunzPagTpServServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (funzPagTpServServer != null);
	}
	public static boolean initiateFunzPagTpServEnteServer (String address) throws Exception
	{
		try
		{
			funzPagTpServEnteServer = new FunzPagTpServEnteServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (funzPagTpServEnteServer != null);
	}

	public static boolean initiateEnteServer (String address) throws Exception
	{
		try
		{
			enteServer = new EnteServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (enteServer != null);
	}

	public static boolean initiateAbilitazTributiServer (String address) throws Exception
	{
		try
		{
			abilitazTributiServer = new AbilitaCanalePagamentoTipoServizioEnteServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (abilitazTributiServer != null);
	}

	public static boolean initiateCostiTransazioneBancaServer (String address) throws Exception
	{
		try
		{
			costiTransazioneBancaServer = new CostiTransazioneBancaServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (costiTransazioneBancaServer != null);
	}

	public static boolean initiateMIPServer (String address) throws Exception
	{
		try
		{
			mipServer = new MIPServer(address) ;
		}catch (Exception e)
		{
			throw e;
		}
		return (mipServer != null);
	}


	public static boolean initiateImpostaSoggiornoConfigServer(String address) throws Exception {
		try {
			impostaSoggiornoConfigServer = new ImpostaSoggiornoConfigServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (impostaSoggiornoConfigServer != null);	
	}

	public static boolean initiateImpostaSoggiornoServer(String address) throws Exception {
		try {
			impostaSoggiornoServer = new ImpostaSoggiornoServer(address);
		} catch (Exception e) {
			throw e;
		}
		return (impostaSoggiornoServer != null);	
	}


	/**
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public static boolean initiateConfigurazioneOtticoServer(String address) throws Exception {
		try { configurazioneServer = new OtticoConfigurazioneServer(address);
		} catch (Exception e) { throw e; }
		return configurazioneServer != null;	
	}
	/**
	 * @param address
	 * @return
	 * @throws Exception
	 */
	public static boolean initiateManagerOtticoServer(String address) throws Exception {
		try { managerOtticoServer = new OtticoManagerServer(address);
		} catch (Exception e) { throw e; }
		return configurazioneServer != null;	
	}

	/**
	 * @param address
	 * @return
	 * @throws Exception
	 */
	/*
	public static boolean initiateContoGestioneServer(String address) throws Exception {
		try { contoGestioneServer = new ContoGestioneServer(address);
		} catch (Exception e) { throw e; }
		return configurazioneServer != null;	
	}
	*/

	public static boolean initiateFiltriEstrattoContoServer(String address) throws Exception {
		try { filtriEstrattoContoServer = new FiltriEstrattoContoServer(address);
		} catch (Exception e) { throw e; }
		return filtriEstrattoContoServer != null;	
	}

	public static boolean initiateIntegraEntePgServer(String address) throws Exception {
		try { integraEntePgServer = new IntegraEntePgServer(address);
		} catch (Exception e) { throw e; }
		return integraEntePgServer != null;
	}

  public static boolean initiateIntegraEcDifferitoServer(String address) throws Exception {
    try { integraEcDifferitoServer = new IntegraEcDifferitoServer(address);
    } catch (Exception e) { throw e; }
    return integraEcDifferitoServer != null;
  }
	//inizio LP PG200360
	public static boolean initiateConfigTassonomia(String address) throws Exception
	{
		try	{
			configTassonomiaServer = new ConfigTassonomiaServer(address) ;
		} catch (Exception e) {
			throw e;
		}
		return (configTassonomiaServer != null);
	}
	//fine LP PG200360

	//inizio LP PG210040
	public static boolean initiateConfigGruppo(String address) throws Exception
	{
		try	{
			configGruppoServer = new ConfigGruppoServer(address) ;
		} catch (Exception e) {
			throw e;
		}
		return (configGruppoServer != null);
	}
	//fine LP PG210040
}