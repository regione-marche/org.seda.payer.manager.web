package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.bancadati.webservice.srv.FaultType;
import com.seda.payer.bancadati.webservice.dati.*;
import com.seda.payer.bancadati.webservice.source.*;


public class EntrateBDServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EntrateBancaDatiSOAPBindingStub servizio = null;
	
	private void setCodSocietaHeader(EntrateBancaDatiSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public EntrateBDServer(String endPoint) {
		EntrateBancaDatiServiceLocator lsService = new EntrateBancaDatiServiceLocator();
		lsService.setEntrateBancaDatiPortEndpointAddress(endPoint);
		try {
			servizio = (EntrateBancaDatiSOAPBindingStub) lsService.getEntrateBancaDatiPort();
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
    // pagamenti
    public RicercaPagamentiResponse ricercaPagamenti(RicercaPagamentiRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaPagamenti(in);    	
    }

    public EstraiPagamentiResponse estraiPagamenti(EstraiPagamentiRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.estraiPagamenti(in);    	
    }

    // anagrafiche
    public RicercaAnagraficheResponse ricercaAnagrafiche(RicercaAnagraficheRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {  	setCodSocietaHeader(servizio, request);  	
    	return servizio.ricercaAnagrafiche(in);    	
    }

    public RicercaDettaglioAnagraficaResponse ricercaDettaglioAnagrafica(RicercaDettaglioAnagraficaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaDettaglioAnagrafica(in);    	
    }

    public RicercaDocumentiAnagraficaResponse ricercaDocumentiAnagrafica(RicercaDocumentiAnagraficaRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaDocumentiAnagrafica(in);    	
    }

    // fine anagrafiche

    //documenti emissioni
    public RicercaDettaglioEmissioneSResponse ricercaDettaglioEmissioneS(RicercaDettaglioEmissioneSRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaDettaglioEmissioneS(in);    	
    }

    public RicercaDocumentiEmissioneResponse ricercaDocumentiEmissione(RicercaDocumentiEmissioneRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {
    	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaDocumentiEmissione(in);    	
    }

    //fine documenti emissioni

    public RicercaTributiResponse ricercaTributi(RicercaTributiRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaTributi(in);    	
    }

    public RicercaScadenzeResponse ricercaScadenze(RicercaScadenzeRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaScadenze(in);    	
    }
    
    public RecuperaDDLImpServizioResponse recuperaDDLImpostaServizio (RecuperaDDLImpServizioRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaImpServizio(in);  	
    }
    public RecuperaDDLUffImpositoreResponse recuperaDDLUffImpositore(RecuperaDDLUffImpositoreRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaUffImpositore(in);  	
    }
      public RecuperaDocumentiResponse ricercaDocumenti(RecuperaDocumentiRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaDocumenti(in);    	
    }

      public RicercaEmissioniResponse ricercaEmissioni(RicercaEmissioniRequest in, HttpServletRequest request) throws RemoteException, FaultType
      {	setCodSocietaHeader(servizio, request);
      	return servizio.ricercaEmissioni(in);    	
      }

      public RicercaPagamentiDocumentiResponse ricercaPagamentiDocumenti(RicercaPagamentiDocumentiRequest in, HttpServletRequest request) throws RemoteException, FaultType
      {	setCodSocietaHeader(servizio, request);
      	return servizio.ricercaPagamentiDocumenti(in);    	
      }

    public RecuperaDettaglioDocumentiResponse dettaglioDocumento(RecuperaDettaglioDocumentiRequest in, HttpServletRequest request) throws RemoteException, FaultType
      {	setCodSocietaHeader(servizio, request);
      	return servizio.recuperaDettaglioDocumento(in);    	
      }

    public RicercaNoteResponse ricercaNote(RicercaNoteRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.ricercaNote(in);    	
    }

    public RecuperaDettaglioTributiResponse dettaglioTributo(RecuperaDettaglioTributiRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaDettaglioTributo(in);    	
    }
   
    public RecuperaDettaglioEmissioneResponse dettaglioEmissione(RecuperaDettaglioEmissioneRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaDettaglioEmissione(in);    	
    }

    public RecuperaDataUltAggiornamentoResponse getDataUltimoAggiornamento(RecuperaDataUltAggiornamentoRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaDataUltAggiornamento(in);    	
    }
    //PG170070 GG 20170530 - inizio
    public RecuperaDDLTipServizioResponse recuperaDDLTipologiaServizio(RecuperaDDLTipServizioRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(servizio, request);
    	return servizio.recuperaListaTipServizio(in);  	
    }
    //PG170070 GG 20170530 - fine

}
