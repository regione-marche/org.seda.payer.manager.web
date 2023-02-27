package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.impostasoggiorno.webservice.srv.FaultType;
import com.seda.payer.impostasoggiorno.webservice.wis.dati.RecuperaDettaglioComunicazioneAggregatoRequest;
import com.seda.payer.impostasoggiorno.webservice.wis.dati.RecuperaDettaglioComunicazioneAggregatoResponse;
import com.seda.payer.impostasoggiorno.webservice.wis.dati.VerificaAutorizzazioneStrutturaRequest;
import com.seda.payer.impostasoggiorno.webservice.wis.dati.VerificaAutorizzazioneStrutturaResponse;
import com.seda.payer.impostasoggiorno.webservice.wis.source.ImpostaSoggiornoSOAPBindingStub;
import com.seda.payer.impostasoggiorno.webservice.wis.source.ImpostaSoggiornoServiceLocator;

public class ImpostaSoggiornoServer extends BaseServer {

	private static final long serialVersionUID = 1L;
	public static ImpostaSoggiornoSOAPBindingStub impostaSoggiornoCaller = null;
	
	private void setCodSocietaHeader(ImpostaSoggiornoSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	
	public ImpostaSoggiornoServer(String endPoint) {
		ImpostaSoggiornoServiceLocator serviceLocator = new ImpostaSoggiornoServiceLocator();
		serviceLocator.setImpostaSoggiornoPortEndpointAddress(endPoint);
		try {
			impostaSoggiornoCaller = (ImpostaSoggiornoSOAPBindingStub)serviceLocator.getImpostaSoggiornoPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	
	public RecuperaDettaglioComunicazioneAggregatoResponse recuperaDettaglioComunicazione(RecuperaDettaglioComunicazioneAggregatoRequest in, HttpServletRequest request) throws RemoteException, FaultType 
	{
		setCodSocietaHeader(impostaSoggiornoCaller, request);
		return impostaSoggiornoCaller.recuperaDettaglioComunicazioneAggregato(in);
	}

	public VerificaAutorizzazioneStrutturaResponse verificaAutorizzazioneStruttura(VerificaAutorizzazioneStrutturaRequest in, HttpServletRequest request) throws java.rmi.RemoteException, FaultType 
	{
		setCodSocietaHeader(impostaSoggiornoCaller, request);
		return impostaSoggiornoCaller.verificaAutorizzazioneStruttura(in);
	}
	
}
