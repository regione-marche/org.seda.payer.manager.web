package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.bancadati.webservice.srv.FaultType;
import com.seda.payer.bancadati.webservice.dati.*;
import com.seda.payer.bancadati.webservice.source.*;


public class RuoliBDServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RuoliBancaDatiSOAPBindingStub servizio = null;
	
	private void setCodSocietaHeader(RuoliBancaDatiSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}

	public RuoliBDServer(String endPoint) {
		RuoliBancaDatiServiceLocator lsService = new RuoliBancaDatiServiceLocator();
		lsService.setRuoliBancaDatiPortEndpointAddress(endPoint);
		try {
			servizio = (RuoliBancaDatiSOAPBindingStub)lsService.getRuoliBancaDatiPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
/*
    public RecuperaRimborsiResponse recuperaRimborsi(RecuperaRimborsiRequest in) throws RemoteException, FaultType
    {
    	return servizio.recuperaRimborsi(in);    	
    }
*/    
    public RicercaRuoliResponse ricercaRuoli(RicercaRuoliRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaRuoli(in);    	
    }

    public RicercaDettaglioRuoloResponse ricercaDettaglioRuolo(RicercaDettaglioRuoloRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaDettaglioRuolo(in);    	
    }

    public RicercaDettaglioSRuoloResponse ricercaDettaglioXmlRuolo(RicercaDettaglioSRuoloRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaDettaglioSRuolo(in);    	
    }
    public RicercaPartiteRuoliResponse ricercaPartiteRuoli(RicercaPartiteRuoliRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaPartiteRuoli(in);    	
    }
    public RicercaDettaglioPartitaResponse ricercaDettaglioPartita(RicercaDettaglioPartitaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaDettaglioPartita(in);    	
    }

    public RicercaDettaglioSPartitaResponse ricercaDettaglioXmlPartita(RicercaDettaglioSPartitaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaDettaglioSPartita(in);    	
    }
    public RicercaArticoliResponse ricercaArticoli(RicercaArticoliRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaArticoli(in);    	
    }
    public RicercaPagamentiDettResponse ricercaPagamentiDett(RicercaPagamentiDettRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaPagamentiDett(in);    	
    }
    public RicercaPartiteResponse ricercaPartite(RicercaPartiteRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaPartite(in);    	
    }
    public RicercaAnagraficheRuoliResponse ricercaAnagrafiche(RicercaAnagraficheRuoliRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaAnagrafiche(in);    	
    }
    
    public RicercaDettaglioSAnagraficaResponse ricercaDettaglioXmlAnagrafica(RicercaDettaglioSAnagraficaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaDettaglioSAnagrafica(in);    	
    }

    public RicercaPartiteAnagraficheResponse ricercaPartiteAnagrafiche(RicercaPartiteAnagraficheRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaPartiteAnagrafiche(in);    	
    }
    
    public RicercaPagamentiRuoliResponse ricercaPagamenti(RicercaPagamentiRuoliRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaPagamenti(in);    	
    }
    public EstraiPagamentiRuoliResponse estraiPagamenti(EstraiPagamentiRuoliRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.estraiPagamenti(in);    	
    }
    public RecuperaRuoliDataUltAggResponse getDataUltimoAggiornamento(RecuperaRuoliDataUltAggRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaDataUltimoAggiornamento(in);    	
    }



}
