package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.rpc.ServiceException;

import org.seda.payer.manager.util.ManagerKeys;

//PG150180_001 GG - inizio
//PG150180_001 GG - fine
//PG150180_001 GG - inizio
//PG150180_001 GG - fine
import com.seda.payer.pgec.webservice.commons.dati.*;
import com.seda.payer.pgec.webservice.commons.source.CommonsSOAPBindingStub;
import com.seda.payer.pgec.webservice.commons.source.CommonsServiceLocator;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;


public class CommonsServer extends BaseServer 
{
	private static final long serialVersionUID = 1L;
	private CommonsSOAPBindingStub commonsCaller = null;
	

	private void setCodSocietaHeader(CommonsSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public CommonsServer(String endPoint) {
		CommonsServiceLocator lsService = new CommonsServiceLocator();
		lsService.setCommonsPortEndpointAddress(endPoint);
		try {
			commonsCaller = (CommonsSOAPBindingStub)lsService.getCommonsPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
    public RecuperaTransazioniResponseType recuperaTransazioni(RecuperaTransazioniRequestType in, HttpServletRequest request) throws FaultType, RemoteException {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaTransazioni(in);
	}
    
    public RecuperaTransazioniGroupedResponse recuperaTransazioniGrouped(RecuperaTransazioniGroupedRequest in, HttpServletRequest request) throws FaultType, RemoteException {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaTransazioniGrouped(in);
	}
    
    public RecuperaTransazioniGroupedOneriResponse recuperaTransazioniGroupedOneri(RecuperaTransazioniGroupedOneriRequest in, HttpServletRequest request) throws FaultType, RemoteException {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaTransazioniGroupedOneri(in);
	}

    
    public RecuperaTransazioniGroupedSuccessResponse recuperaTransazioniGroupedSuccess(RecuperaTransazioniGroupedSuccessRequest in, HttpServletRequest request) throws FaultType, RemoteException {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaTransazioniGroupedSuccess(in);
	}
    public RecuperaTransazioneXmlResponse recuperaTransazioneXml(RecuperaTransazioneXmlRequest in, HttpServletRequest request) throws FaultType, RemoteException {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaTransazioneXml(in);
	}
    public RecuperaTransazioniCsvResponse recuperaTransazioniCsv(RecuperaTransazioniCsvRequest in, HttpServletRequest request) throws FaultType, RemoteException {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaTransazioniCsv(in);
	}
    public GeneraPdfTransazioniResponse generaPdfTransazioni(GeneraPdfTransazioniRequest in, HttpServletRequest request) throws FaultType, RemoteException {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.generaPdfTransazioni(in);
	}
    /**
     * Restituisce la lista delle province (sigla, descrizione) in funzione della società selezionata --- 
     * NOTA: Sostituirà "getProvinceDDL" perchè utiilizza il codice Belfiore invece della sigla della provincia
     * @param in (Codice della Società)
     * @return
     * @throws RemoteException
     * @throws FaultType
     */
    public GetListaProvinceXml_DDLResponseType getListaProvinceXml_DDL(GetListaProvinceXml_DDLRequestType in, HttpServletRequest request) throws RemoteException, FaultType
    {setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaProvinceXml_DDL(in);
    }
    public GetListaTipologiaServizioXml_DDLResponseType getListaTipologiaServizioXml_DDL(GetListaTipologiaServizioXml_DDLRequestType in, HttpServletRequest request) throws RemoteException, FaultType
    {setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaTipologiaServizioXml_DDL(in);
    }
    public GetListaTipologiaServizioXml_DDL_2ResponseType getListaTipologiaServizioXml_DDL_2(GetListaTipologiaServizioXml_DDL_2RequestType in, HttpServletRequest request) throws RemoteException, FaultType
    {setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaTipologiaServizioXml_DDL_2(in);
    }
    public GetListaUtentiEntiXml_DDLResponseType getListaUtentiEntiXml_DDL(GetListaUtentiEntiXml_DDLRequestType in, HttpServletRequest request)throws RemoteException, FaultType
    {setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaUtentiEntiXml_DDL(in);
    }
    public GetListaUtentiEntiGenericiXml_DDLResponse getListaUtentiEntiGenericiXml_DDL(GetListaUtentiEntiGenericiXml_DDLRequest in, HttpServletRequest request)throws RemoteException, FaultType
    {setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaUtentiEntiGenericiXml_DDL(in);
    }
    public GetListaUtentiEntiXml_DDLResponseType getListaUtentiEntiAllXml_DDL(GetListaUtentiEntiXml_DDLRequestType in, HttpServletRequest request)throws RemoteException, FaultType
    {setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaUtentiImpositoriXml_DDL(in);
    }

    public GetListaTipoCartaXml_DDLResponseType getListaTipoCartaXml_DDL(GetListaTipoCartaXml_DDLRequestType in, HttpServletRequest request) throws RemoteException, FaultType
    {setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaTipoCartaXml_DDL(in);
    }
    public GetListaSocietaXml_DDLResponseType getListaSocietaXml_DDL(GetListaSocietaXml_DDLRequestType in, HttpServletRequest request)throws RemoteException, FaultType{
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaSocietaXml_DDL(in);
    }
    
    public GetProvinceDDLResponse getProvinceDDL(GetProvinceDDLRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getProvinceDDL(in);
	} 
    public GetUtentiDDLResponse getUtentiDDL(GetUtentiDDLRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getUtentiDDL(in);
	} 
    public GetEntiDDLResponse getEntiDDL(GetEntiDDLRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getEntiDDL(in);
	} 
    public GetStaticDDLListsResponse getStaticDDLLists(GetStaticDDLListsRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getStaticDDLLists(in);
	} 
    public ModificaEmailSmsResponse modificaEmailSms(ModificaEmailSmsRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.modificaEmailSms(in);
	}  
    public RecuperaTransazioneResponseType recuperaTransazione(RecuperaTransazioneRequestType in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaTransazione(in);
	}
    public RecuperaTransazioneFiltrataResponseType recuperaTransazioneFiltrata(RecuperaTransazioneFiltrataRequestType in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaTransazioneFiltrata(in);
	}
    public GetDettaglioTransazioneResponse getDettaglioTransazione(GetDettaglioTransazioneRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getDettaglioTransazione(in);
    }
    public ForzaAllineamentoTransazioneResponse forzaAllineamentoTransazione(ForzaAllineamentoTransazioneRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.forzaAllineamentoTransazione(in);
    }
    public AggiornaRiconciliazioneResponse aggiornaRiconciliazione(AggiornaRiconciliazioneRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.aggiornaRiconciliazione(in);
    }
    public RecuperaMovimentiCBIResponse recuperaMovimentiCBI(RecuperaMovimentiCBIRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaMovimentiCBI(in);
    }
    //PG150180_001 GG
    public RecuperaMovimentiNodoResponse recuperaMovimentiNodo(RecuperaMovimentiNodoRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaMovimentiNodo(in);
    }
    //inizio LP PG200200
    public RecuperaIUVFlussoNodoResponse recuperaIUVFlussoNodo(RecuperaIUVFlussoNodoRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaIUVFlussoNodo(in);
    }
    public RitentaQuadraturaFlussoNodoResponse ritentaQuadraturaFlussoNodo(RitentaQuadraturaFlussoNodoRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.ritentaQuadraturaFlussoNodo(in);
    }
    //fine LP PG200200
    public StampaMovimentiPdfResponse stampaMovimentiPdf(StampaMovimentiPdfRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.stampaMovimentiPdf(in);
    }
    public ScaricaMovimentoResponse downloadFlussoQuadrature(ScaricaMovimentoRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.scaricaMovimento(in);
    }
    public ForzaChiusuraQuadraturaResponse forzaChiusuraQuadratura(ForzaChiusuraQuadraturaRequest in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.forzaChiusuraQuadratura(in);
    }
    public RecuperaFlussiRendCsvResponseType recuperaFlussiRendCsv(RecuperaFlussiRendCsvRequestType in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaFlussiRendCsv(in);
    }
    public RecuperaFlussiRendResponseType recuperaFlussiRend(RecuperaFlussiRendRequestType in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaFlussiRend(in);
    }
    public RecuperaFlussiRendPdfResponseType recuperaFlussiRendPdf(RecuperaFlussiRendPdfRequestType in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaFlussiRendPdf(in);
    }
    public RecuperaGatewayRendResponseType recuperaGatewayRend(RecuperaGatewayRendRequestType in, HttpServletRequest request) throws RemoteException, FaultType {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaGatewayRend(in);
    }
	public RecuperaTransazioniNonQuadrateResponse recuperaTransazioniNonQuadrate(RecuperaTransazioniNonQuadrateRequest in, HttpServletRequest request) throws RemoteException, FaultType {
		setCodSocietaHeader(commonsCaller, request);
		return commonsCaller.recuperaTransazioniNonQuadrate(in);
	}
	public RecuperaMovimentiApertiResponse recuperaMovimentiAperti(RecuperaMovimentiApertiRequest in, HttpServletRequest request) throws RemoteException, FaultType {
		setCodSocietaHeader(commonsCaller, request);
		return commonsCaller.recuperaMovimentiAperti(in);
	}
    //PG150180_001 GG
	public RecuperaMovimentiApertiNodoResponse recuperaMovimentiApertiNodo(RecuperaMovimentiApertiNodoRequest in, HttpServletRequest request) throws RemoteException, FaultType {
		setCodSocietaHeader(commonsCaller, request);
		return commonsCaller.recuperaMovimentiApertiNodo(in);
	}
	public EliminaTransazioneResponse eliminaTransazione(EliminaTransazioneRequest in, HttpServletRequest request) throws RemoteException, FaultType {
		setCodSocietaHeader(commonsCaller, request);
		return commonsCaller.eliminaTransazione(in);
	}
	
	public ManagerLogResponseType managerLog (ManagerLogRequestType in, HttpServletRequest request) throws FaultType, RemoteException {
		setCodSocietaHeader(commonsCaller, request);
		return commonsCaller.managerLog(in);
	}
	public ManagerLogResponseType managerLog(ManagerLogRequestType in, HttpSession session) throws FaultType, RemoteException 
	{
		String dbSchemaCodSocieta = "";
		if (session != null && session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA) != null)
		{
			dbSchemaCodSocieta = (String)session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
		
			commonsCaller.clearHeaders();
			commonsCaller.setHeader("",DBSCHEMACODSOCIETA, dbSchemaCodSocieta);		
		
			return commonsCaller.managerLog(in);
		}
		
		return null;
	}
	public LogAccessiResponseType logAccessi (LogAccessiRequestType in, HttpServletRequest request) throws FaultType, RemoteException {
		setCodSocietaHeader(commonsCaller, request);
		return commonsCaller.logAccessi(in);
	}
	public GetCanaliPagamentoDDLResponse getCanaliPagamentoDDL (GetCanaliPagamentoDDLRequest in, HttpServletRequest request)throws FaultType, RemoteException {
		setCodSocietaHeader(commonsCaller, request);
		return commonsCaller.getCanaliPagamentoDDL(in);
	}

	//dom
    public DDLResponseType getListaBeneficiariByImpDDL(ListaBeneficiariByImpDDLRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaBeneficiariByImpDDL(in);
    }

    public DDLResponseType getListaBeneficiariBollDDL(ListaBeneficiariBollDDLRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaBeneficiariBollDDL(in);
    }


    public DDLResponseType getListaBeneficiariDDL(ListaBeneficiariDDLRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaBeneficiariDDL(in);
    }
//new
    public DDLResponseType getListaBeneficiariByConvDDL(ListaBeneficiariByConvDDLRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaBeneficiariByConvDDL(in);
    }

    public DDLResponseType getListaProvinceBenDDL(ListaProvinceBenDDLRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaProvinceBenDDL(in);
    }
//new
    public DDLResponseType getListaProvinceByConvDDL(ListaProvinceByConvDDLRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaProvinceByConvDDL(in);
    }
    
    public DDLResponseType getListaGatewayCanDDL(ListaGatewayCanDDLRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.getListaGatewayCanDDL(in);
    }

    //flusso fittizio
	public AssegnaTranAFittizioResponse assegnaTranAFittizio (AssegnaTranAFittizioRequest in, HttpServletRequest request)throws FaultType, RemoteException {
		setCodSocietaHeader(commonsCaller, request);
		return commonsCaller.assegnaTranAFittizio(in);
	}

	public RecuperaProvinceResponse recuperaProvince(HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(commonsCaller, request);
		return commonsCaller.recuperaProvince(new RecuperaProvinceRequest());
	}
	
	public RecuperaBelfioreResponse recuperaBelfiore(RecuperaBelfioreRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaBelfiore(in);
    }
	
	public RecuperaComuniBySiglaProvinciaResponse recuperaComuni_SiglaProvincia(RecuperaComuniBySiglaProvinciaRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(commonsCaller, request);
    	//stored PYAPCSP_LST_SIGLA
    	return commonsCaller.recuperaComuni_SiglaProvincia(in);
	}
	
	public GetKeyResponse getKey(GetKeyRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(commonsCaller, request);
    	//stored PYKEYSP_BIGINT
    	return commonsCaller.getKey(in);
	}
	public RptNodoSpcResponse recuperaRPT(RptNodoSpcRequest in, HttpServletRequest request) throws FaultType, RemoteException
    {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.recuperaRPT(in);
    }
	public AggiornaEsitoQuadraturaNodoResponse aggiornaEsitoQuadratura(AggiornaEsitoQuadraturaNodoRequest in, HttpServletRequest request) throws FaultType, RemoteException
    {
    	setCodSocietaHeader(commonsCaller, request);
    	return commonsCaller.aggiornaEsitoQuadratura(in);
    }

	//inizio LP PG21X007
    public RecuperaEnteUtenteSocietaAneResponse recuperaEnteUtenteSocieta_Ane(RecuperaEnteUtenteSocietaAneRequest in, HttpServletRequest request) throws FaultType, RemoteException
    {
    	setCodSocietaHeader(commonsCaller, request);
    	//stored PYANESP_SEL_ENT2
    	return commonsCaller.recuperaEnteUtenteSocietaAne(in);
    }

    public RecuperaEnteUtenteSocietaProvinciaResponse recuperaEnteUtenteSocietaProvincia(RecuperaEnteUtenteSocietaProvinciaRequest in, HttpServletRequest request) throws FaultType, RemoteException
    {
    	setCodSocietaHeader(commonsCaller, request);
    	//stored PYANESP_SEL_ENT
    	return commonsCaller.recuperaEnteUtenteSocietaProvincia(in);
    }

    public RecuperaEnteBelfResponse recuperaEnteBelf(RecuperaEnteBelfRequest in, HttpServletRequest request) throws FaultType, RemoteException
    {
    	setCodSocietaHeader(commonsCaller, request);
    	//stored PYANESP_SEL_BELF
    	return commonsCaller.recuperaEnteBelf(in);
    }
	//fine LP PG21X007

	 public InvalidateLogResponseType invalidateLog (InvalidateLogRequestType in, HttpServletRequest request) throws FaultType, RemoteException {
			setCodSocietaHeader(commonsCaller, request);
			return commonsCaller.invalidateLog(in);
		}
	
	 public GetLogResponseType getLogBySessionId (GetLogRequestType in, HttpServletRequest request) throws FaultType, RemoteException {
			setCodSocietaHeader(commonsCaller, request);
			return commonsCaller.getLogBySessionId(in);
	}

	 
	public ConfigPagamentoSingleResponse recuperaFunzioneEnte(ConfigPagamentoSingleRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{
		setCodSocietaHeader(commonsCaller, request);
		//stored PYCESSP_SEL_CONFIG
		return commonsCaller.recuperaFunzioneEnte(in);
	}
	 // YLM PG22XX06 INI
	 public GetListaTipologiaServizioAPPIOXml_DDL_ResponseType getListaTipologiaServizioAPPIOXml_DDL(GetListaTipologiaServizioAPPIOXml_DDL_RequestType in, HttpServletRequest request) throws RemoteException, FaultType
	    {setCodSocietaHeader(commonsCaller, request);
	    	return commonsCaller.getListaTipologiaServizioAPPIOXml_DDL(in);
	    }
	// YLM PG22XX06 FINE
}
