package org.seda.payer.manager.ws;

/*
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.riversamento.webservice.srv.FaultType;
import com.seda.payer.riversamento.webservice.dati.*;
import com.seda.payer.riversamento.webservice.source.*;


public class RiversamentoServer extends BaseServer
{

	private static final long serialVersionUID = 1L;
	private RiversamentoSOAPBindingStub servizio = null;
	
	private void setCodSocietaHeader(RiversamentoSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}

	public RiversamentoServer(String endPoint) {
		RiversamentoServiceLocator lsService = new RiversamentoServiceLocator();
		lsService.setRiversamentoPortEndpointAddress(endPoint);
		try {
			servizio = (RiversamentoSOAPBindingStub)lsService.getRiversamentoPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
    public RecuperaRiversamentiResponse recuperaRiversamenti(RecuperaRiversamentiRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaRiversamenti(in);    	
    }

    public RecuperaTransazioniRiversateResponse recuperaRiversamenti(RecuperaTransazioniRiversateRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaTransazioniRiversate(in);    	
    }

    public CertificazioneRiversamentiResponse certificazioneRiversamenti(CertificazioneRiversamentiRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.certificazioneRiversamenti(in);    	
    }

    public ElaboraNuoviRiversamentiResponse elaboraNuoviRiversamenti(ElaboraNuoviRiversamentiRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.elaboraNuoviRiversamenti(in);    	
    }

    
    public com.seda.payer.riversamento.webservice.dati.AggiornamentoStatoRiversamentoResponse aggiornamentoStatoRiversamento(com.seda.payer.riversamento.webservice.dati.AggiornamentoStatoRiversamentoRequest in, HttpServletRequest request) throws java.rmi.RemoteException, com.seda.payer.riversamento.webservice.srv.FaultType {
    	setCodSocietaHeader(servizio, request);
    	return servizio.aggiornamentoStatoRiversamento(in);
    }
    
    public com.seda.payer.riversamento.webservice.dati.ElaboraNuoveStampeResponse elaboraNuoveStampe(com.seda.payer.riversamento.webservice.dati.ElaboraNuoveStampeRequest in, HttpServletRequest request) throws java.rmi.RemoteException, com.seda.payer.riversamento.webservice.srv.FaultType {
    	setCodSocietaHeader(servizio, request);
    	return servizio.elaboraNuoveStampe(in);
    }
    
    public com.seda.payer.riversamento.webservice.dati.ElaboraStampaResponse elaboraStampa(com.seda.payer.riversamento.webservice.dati.ElaboraStampaRequest in, HttpServletRequest request) throws java.rmi.RemoteException, com.seda.payer.riversamento.webservice.srv.FaultType {
    	setCodSocietaHeader(servizio, request);
    	return servizio.elaboraStampa(in);
    }
    
    public com.seda.payer.riversamento.webservice.dati.ElaboraNuoviFlussiCBIResponse elaboraNuoviFlussiCBI(com.seda.payer.riversamento.webservice.dati.ElaboraNuoviFlussiCBIRequest in, HttpServletRequest request) throws java.rmi.RemoteException, com.seda.payer.riversamento.webservice.srv.FaultType {
    	setCodSocietaHeader(servizio, request);
    	return servizio.elaboraNuoviFlussiCBI(in);
    }
    
    public com.seda.payer.riversamento.webservice.dati.ElaboraNuoviEsitiCBIResponse elaboraNuoviEsitiCBI(com.seda.payer.riversamento.webservice.dati.ElaboraNuoviEsitiCBIRequest in, HttpServletRequest request) throws java.rmi.RemoteException, com.seda.payer.riversamento.webservice.srv.FaultType {
    	setCodSocietaHeader(servizio, request);
    	return servizio.elaboraNuoviEsitiCBI(in);
    }
    
    public com.seda.payer.riversamento.webservice.dati.AggiornamentoFlagCBIResponse aggiornamentoFlagCBI(com.seda.payer.riversamento.webservice.dati.AggiornamentoFlagCBIRequest in, HttpServletRequest request) throws java.rmi.RemoteException, com.seda.payer.riversamento.webservice.srv.FaultType {
    	setCodSocietaHeader(servizio, request);
    	return servizio.aggiornamentoFlagCBI(in);
    }
    
    public com.seda.payer.riversamento.webservice.dati.DownloadCBIResponse downloadCBI(com.seda.payer.riversamento.webservice.dati.DownloadCBIRequest in, HttpServletRequest request) throws java.rmi.RemoteException, com.seda.payer.riversamento.webservice.srv.FaultType {
    	setCodSocietaHeader(servizio, request);
    	return servizio.downloadCBI(in);
    }

    public com.seda.payer.riversamento.webservice.dati.AvanzamentoStatoResponse avanzamentoStato(com.seda.payer.riversamento.webservice.dati.AvanzamentoStatoRequest in, HttpServletRequest request) throws java.rmi.RemoteException, com.seda.payer.riversamento.webservice.srv.FaultType {
    	setCodSocietaHeader(servizio, request);
    	return servizio.avanzamentoStato(in);
    }

}
*/