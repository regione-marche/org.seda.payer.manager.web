package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaAnagraficaStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaAnagraficaStrutturaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaAnagraficaStrutturaToHostRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaAnagraficaStrutturaToHostResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaConfigurazioneImpSoggToHostRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaConfigurazioneImpSoggToHostResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaFasciaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaFasciaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTipologiaSoggettoRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTipologiaSoggettoResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaAnagraficaStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaAnagraficaStrutturaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaConfigurazioneImpSoggRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaConfigurazioneImpSoggResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaFasciaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaFasciaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaTipologiaSoggettoRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaTipologiaSoggettoResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.CancellaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.GetProgressivoComunicazioneRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.GetProgressivoComunicazioneResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciAnagraficaStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciAnagraficaStrutturaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciConfigurazioneImpSoggToHostRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciConfigurazioneImpSoggToHostResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciFasciaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciFasciaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTipologiaSoggettoRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTipologiaSoggettoResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaCatMerceologicaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaCatMerceologicaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaCsvRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaCsvResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaAnagraficaStrutturaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaConfigurazioneImpostaSoggiornoRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaConfigurazioneImpostaSoggiornoResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaFasciaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaFasciaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaAnagraficaStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaAnagraficaStrutturaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaComunicazioniCsvRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaComunicazioniCsvResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaComunicazioniRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaComunicazioniResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaConfigurazioneImpSoggRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaConfigurazioneImpSoggResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaEntiImpostaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaEntiImpostaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaFasceTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaFasceTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaSoggettoRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaSoggettoResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTariffeCsvRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTariffeCsvResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaSoggettiCsvRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaSoggettiCsvResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaSoggettoRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaSoggettoResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaStrutturaRicettivaCsvRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaStrutturaRicettivaCsvResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaStrutturaRicettivaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTipologiaStrutturaRicettivaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.VerificaAnagraficaContribuentiRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.VerificaAnagraficaContribuentiResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.VerificaTariffaComunicazioniRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.VerificaTariffaComunicazioniResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.source.ImpostaSoggiornoConfigSOAPBindingStub;
import com.seda.payer.impostasoggiorno.webservice.configurazione.source.ImpostaSoggiornoConfigServiceLocator;
import com.seda.payer.impostasoggiorno.webservice.srv.FaultType;

public class ImpostaSoggiornoConfigServer extends BaseServer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static ImpostaSoggiornoConfigSOAPBindingStub servizio = null;
	
	private void setCodSocietaHeader(ImpostaSoggiornoConfigSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	/**
	 * Default constructor	
	 * @param endPoint
	 */
	public ImpostaSoggiornoConfigServer(String endPoint) {
		ImpostaSoggiornoConfigServiceLocator serviceLocator = new ImpostaSoggiornoConfigServiceLocator();
		serviceLocator.setImpostaSoggiornoConfigPortEndpointAddress(endPoint);
		try {
			servizio = (ImpostaSoggiornoConfigSOAPBindingStub)serviceLocator.getImpostaSoggiornoConfigPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public RecuperaAnagraficaStrutturaResponse recuperaAnagraficaStruttura(RecuperaAnagraficaStrutturaRequest in, HttpServletRequest request) throws java.rmi.RemoteException,FaultType 
	{	
		setCodSocietaHeader(servizio, request);
		return servizio.recuperaAnagraficaStruttura(in);
	}
	
	public AggiornaAnagraficaStrutturaResponse aggiornaAnagraficaStruttura(AggiornaAnagraficaStrutturaRequest in, HttpServletRequest request) throws java.rmi.RemoteException,FaultType 
	{	
		setCodSocietaHeader(servizio, request);
		return servizio.aggiornaAnagraficaStruttura(in);
	}
	
	public RecuperaConfigurazioneImpostaSoggiornoResponse recuperaConfigurazioneImpostaSoggiorno(RecuperaConfigurazioneImpostaSoggiornoRequest in, HttpServletRequest request) throws java.rmi.RemoteException, FaultType 
	{	
		setCodSocietaHeader(servizio, request);
		return servizio.recuperaConfigurazioneImpostaSoggiorno(in);
	}
	
	public RecuperaTipologiaStrutturaRicettivaResponse recuperaTipologiaStrutturaRicettiva(RecuperaTipologiaStrutturaRicettivaRequest in, HttpServletRequest request) throws java.rmi.RemoteException, FaultType 
	{	
		setCodSocietaHeader(servizio, request);
		return servizio.recuperaTipologiaStrutturaRicettiva(in);
	}
	
    public RecuperaListaTipologiaSoggettoResponse ricercaTipologiaSoggetti(RecuperaListaTipologiaSoggettoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaTipologiaSoggetto(in);   	
    }

    public RecuperaListaTipologiaStrutturaRicettivaResponse ricercaTipologiaStrutture(RecuperaListaTipologiaStrutturaRicettivaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaTipologiaStrutturaRicettiva(in);   	
    }
    
    public AggiornaTipologiaStrutturaRicettivaResponse aggiornaTipologiaStrutturaRicettiva(AggiornaTipologiaStrutturaRicettivaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.aggiornaTipologiaStrutturaRicettiva(in);
    }
    
    public InserisciTipologiaStrutturaRicettivaResponse inserisciTipologiaStrutturaRicettiva(InserisciTipologiaStrutturaRicettivaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.inserisciTipologiaStrutturaRicettiva(in);
    }

    public CancellaTipologiaStrutturaRicettivaResponse cancellaTipologiaStrutturaRicettiva(CancellaTipologiaStrutturaRicettivaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.cancellaTipologiaStrutturaRicettiva(in);
    }    
    
    public AggiornaTipologiaSoggettoResponse aggiornaTipologiaSoggetti(AggiornaTipologiaSoggettoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.aggiornaTipologiaSoggetto(in);
    }
    
    public InserisciTipologiaSoggettoResponse inserisciTipologiaSoggetti(InserisciTipologiaSoggettoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.inserisciTipologiaSoggetto(in);
    }

    public CancellaTipologiaSoggettoResponse cancellaTipologiaSoggetti(CancellaTipologiaSoggettoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.cancellaTipologiaSoggetto(in);
    }
	
    public RecuperaTipologiaSoggettoResponse recuperaTipologiaSoggetto(RecuperaTipologiaSoggettoRequest in, HttpServletRequest request) throws java.rmi.RemoteException,FaultType 
	{	
    	setCodSocietaHeader(servizio, request);
		return servizio.recuperaTipologiaSoggetto(in);
	}
    
    public RecuperaTipologiaSoggettiCsvResponse recuperaTipologiaSoggettiCsv(RecuperaTipologiaSoggettiCsvRequest in, HttpServletRequest request) throws RemoteException,FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaTipologiaSoggettiCsv(in);
    }
    
    public RecuperaListaAnagraficaStrutturaResponse ricercaAnagraficheStrutture(RecuperaListaAnagraficaStrutturaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaAnagraficaStruttura(in);   	
    }
    
    public RecuperaAnagraficaStrutturaCsvResponse recuperaAnagraficaStrutturaCsv(RecuperaAnagraficaStrutturaCsvRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaAnagraficaStrutturaCsv(in);	
    }

    public InserisciAnagraficaStrutturaResponse inserisciAnagraficaStruttura(InserisciAnagraficaStrutturaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.inserisciAnagraficaStruttura(in);
    }

    public CancellaAnagraficaStrutturaResponse cancellaAnagraficaStruttura(CancellaAnagraficaStrutturaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.cancellaAnagraficaStruttura(in);
    }
    
    public RecuperaTipologiaStrutturaRicettivaCsvResponse recuperaTipologiaStrutturaRicettivaCsv(RecuperaTipologiaStrutturaRicettivaCsvRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaTipologiaStrutturaRicettivaCsv(in);
    }
    
    public RecuperaListaConfigurazioneImpSoggResponse ricercaConfigurazioneImposta(RecuperaListaConfigurazioneImpSoggRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaConfigurazioneImpSogg(in);   	
    }

    public InserisciConfigurazioneImpSoggToHostResponse inserisciConfigurazioneImposta(InserisciConfigurazioneImpSoggToHostRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.inserisciConfigurazioneImpSoggToHost(in);
    }

    public RecuperaListaEntiImpostaResponse recuperaListaEntiImposta(RecuperaListaEntiImpostaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaEntiImposta(in);
    }
    
    public VerificaAnagraficaContribuentiResponse verificaAnagraficaContribuenti(VerificaAnagraficaContribuentiRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.verificaAnagraficaContribuenti(in);
    }

    public CancellaConfigurazioneImpSoggResponse cancellaConfigurazioneImposta(CancellaConfigurazioneImpSoggRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.cancellaConfigurazioneImpSogg(in);
    }

    public AggiornaConfigurazioneImpSoggToHostResponse aggiornaConfigurazioneImposta(AggiornaConfigurazioneImpSoggToHostRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.aggiornaConfigurazioneImpSoggToHost(in);
    }
    
    public AggiornaAnagraficaStrutturaToHostResponse aggiornaAnagraficaStrutturaHost(AggiornaAnagraficaStrutturaToHostRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.aggiornaAnagraficaStrutturaToHost(in);
    }
    
    //tariffa
    
    public RecuperaListaTariffaResponse ricercaTariffe(RecuperaListaTariffaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaTariffa(in);   	
    }

    public RecuperaTariffaResponse recuperaTariffa(RecuperaTariffaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaTariffa(in);
    }

    public InserisciTariffaResponse inserisciTariffa(InserisciTariffaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.inserisciTariffa(in);
    }
    
    public AggiornaTariffaResponse aggiornaTariffa(AggiornaTariffaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.aggiornaTariffa(in);
    }
    

    public CancellaTariffaResponse cancellaTariffa(CancellaTariffaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.cancellaTariffa(in);
    }    
    
    public VerificaTariffaComunicazioniResponse verificaTariffaComunicazioni(VerificaTariffaComunicazioniRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.verificaTariffaComunicazioni(in);
    }    
    
    public RecuperaTariffeCsvResponse recuperaTariffeCsv(RecuperaTariffeCsvRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaTariffeCsv(in);
    }
    
    public RecuperaListaComunicazioniResponse recuperaListaComunicazioni(RecuperaListaComunicazioniRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaComunicazioni(in);
    }
    
    public RecuperaListaComunicazioniCsvResponse recuperaListaComunicazioniCsv(RecuperaListaComunicazioniCsvRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaComunicazioniCsv(in);
    }
    
    //PG180050_001 GG - inizio
	public RecuperaAnagraficaStrutturaCatMerceologicaResponse recuperaAnagraficaStrutturaCatMerceologica(RecuperaAnagraficaStrutturaCatMerceologicaRequest in, HttpServletRequest request) throws java.rmi.RemoteException,FaultType 
	{
		setCodSocietaHeader(servizio, request);
		return servizio.recuperaAnagraficaStrutturaCatMerceologica(in);
	}
	
	public GetProgressivoComunicazioneResponse getProgressivoComunicazione(GetProgressivoComunicazioneRequest in, HttpServletRequest request) throws java.rmi.RemoteException,FaultType 
	{
		setCodSocietaHeader(servizio, request);
		return servizio.getProgressivoComunicazione(in);
	}
	//PG180050_001 GG - fine
	
	//inizio LP PG1800XX_016
    public RecuperaListaFasceTariffaResponse ricercaFasceTariffa(RecuperaListaFasceTariffaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaFasceTariffa(in);   	
    }

    public RecuperaFasciaTariffaResponse recuperaFasciaTariffa(RecuperaFasciaTariffaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaFasciaTariffa(in);
    }

    public InserisciFasciaTariffaResponse inserisciFasciaTariffa(InserisciFasciaTariffaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.inserisciFasciaTariffa(in);
    }
    
    public AggiornaFasciaTariffaResponse aggiornaFasciaTariffa(AggiornaFasciaTariffaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.aggiornaFasciaTariffa(in);
    }

    public CancellaFasciaTariffaResponse cancellaFasciaTariffa(CancellaFasciaTariffaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	
    	setCodSocietaHeader(servizio, request);
    	return servizio.cancellaFasciaTariffa(in);
    }    
    //fine LP PG1800XX_016
    
}
