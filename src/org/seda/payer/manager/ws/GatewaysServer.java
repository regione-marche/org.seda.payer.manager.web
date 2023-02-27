package org.seda.payer.manager.ws;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.gateways.webservice.dati.AllineaManualeTransazioneRequest;
import com.seda.payer.gateways.webservice.dati.AllineaManualeTransazioneResponse;
import com.seda.payer.gateways.webservice.dati.RichiediStornoRequest;
import com.seda.payer.gateways.webservice.dati.RichiediStornoResponse;
import com.seda.payer.gateways.webservice.dati.TransazioneType;
import com.seda.payer.gateways.webservice.source.*;

public class GatewaysServer extends BaseServer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GatewaysSOAPBindingStub gatewaysCaller = null;
	
	private void setCodSocietaHeader(GatewaysSOAPBindingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public GatewaysServer(String endPoint) {
		GatewaysServiceLocator gatewaysService = new GatewaysServiceLocator();
		gatewaysService.setGatewaysPortEndpointAddress(endPoint);
		try {
			gatewaysCaller = (GatewaysSOAPBindingStub) gatewaysService.getGatewaysPort();
		}
		catch (ServiceException e){
			e.printStackTrace();
		}
	} 
	
	public AllineaManualeTransazioneResponse allineaManualeTransazione(String codiceTransazione, String chiaveGateway, HttpServletRequest request) {
		setCodSocietaHeader(gatewaysCaller, request);
		AllineaManualeTransazioneResponse res = null;
		AllineaManualeTransazioneRequest req = new AllineaManualeTransazioneRequest(
				new TransazioneType(codiceTransazione, chiaveGateway, ""));

		try {
			res = gatewaysCaller.allineaManualeTransazione(req);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}
	
	public RichiediStornoResponse richiediStorno (String codiceTransazione, String idOperazione, BigDecimal importoTransazione, String canalePagamento, String chiaveGateway, String operatore, HttpServletRequest request) {
		setCodSocietaHeader(gatewaysCaller, request);
		RichiediStornoResponse res = null;
		RichiediStornoRequest req = new RichiediStornoRequest(idOperazione,codiceTransazione,importoTransazione,canalePagamento,chiaveGateway,operatore);

		try {
			res = gatewaysCaller.richiediStorno(req);
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
		return res;
	}
	
}
