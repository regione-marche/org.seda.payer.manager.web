package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteCancelRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteCancelResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteGetListaXmlRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteGetListaXmlResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteGetRecordRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteGetRecordResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteSaveRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.dati.ConfRendUtenteServizioEnteSaveResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.source.ConfRendUtenteServizioEnteImplementationStub;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizioEnte.source.ConfRendUtenteServizioEnteServiceLocator;
import com.seda.payer.pgec.webservice.srv.FaultType;

public class ConfRendUtenteServizioEnteServer extends BaseServer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfRendUtenteServizioEnteImplementationStub confRendUtenteServizioEnteServerCaller = null;
	
	private void setCodSocietaHeader(ConfRendUtenteServizioEnteImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public ConfRendUtenteServizioEnteServer(String endPoint)
	{
		ConfRendUtenteServizioEnteServiceLocator lsService = new ConfRendUtenteServizioEnteServiceLocator();
		lsService.setConfRendUtenteServizioEntePortEndpointAddress(endPoint);
		try {
			confRendUtenteServizioEnteServerCaller = (ConfRendUtenteServizioEnteImplementationStub)lsService.getConfRendUtenteServizioEntePort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public ConfRendUtenteServizioEnteGetRecordResponseType getRecord(ConfRendUtenteServizioEnteGetRecordRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(confRendUtenteServizioEnteServerCaller, request);
		return confRendUtenteServizioEnteServerCaller.getRecord(in);
	}

	public ConfRendUtenteServizioEnteCancelResponseType cancel(ConfRendUtenteServizioEnteCancelRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(confRendUtenteServizioEnteServerCaller, request);
		return confRendUtenteServizioEnteServerCaller.cancel(in);
	}

	public ConfRendUtenteServizioEnteSaveResponseType save(ConfRendUtenteServizioEnteSaveRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(confRendUtenteServizioEnteServerCaller, request);
		return confRendUtenteServizioEnteServerCaller.save(in);
	}

	public ConfRendUtenteServizioEnteGetListaXmlResponseType getListaXml(ConfRendUtenteServizioEnteGetListaXmlRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(confRendUtenteServizioEnteServerCaller, request);
		return confRendUtenteServizioEnteServerCaller.getListaXml(in);
	}
}
