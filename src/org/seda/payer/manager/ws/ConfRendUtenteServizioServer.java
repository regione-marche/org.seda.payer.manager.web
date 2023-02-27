package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioCancelRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioCancelResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioGetListaXmlRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioGetListaXmlResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioGetRecordRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioGetRecordResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioSaveRequestType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.dati.ConfRendUtenteServizioSaveResponseType;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.source.ConfRendUtenteServizioImplementationStub;
import com.seda.payer.pgec.webservice.ConfRendUtenteServizio.source.ConfRendUtenteServizioServiceLocator;
import com.seda.payer.pgec.webservice.srv.FaultType;

public class ConfRendUtenteServizioServer extends BaseServer{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ConfRendUtenteServizioImplementationStub confRendUtenteServizioServerCaller = null;
	
	private void setCodSocietaHeader(ConfRendUtenteServizioImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public ConfRendUtenteServizioServer(String endPoint)
	{
		ConfRendUtenteServizioServiceLocator lsService = new ConfRendUtenteServizioServiceLocator();
		lsService.setConfRendUtenteServizioPortEndpointAddress(endPoint);
		try {
			confRendUtenteServizioServerCaller = (ConfRendUtenteServizioImplementationStub)lsService.getConfRendUtenteServizioPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	public ConfRendUtenteServizioGetRecordResponseType getRecord(ConfRendUtenteServizioGetRecordRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(confRendUtenteServizioServerCaller, request);
		return confRendUtenteServizioServerCaller.getRecord(in);
	}

	public ConfRendUtenteServizioCancelResponseType cancel(ConfRendUtenteServizioCancelRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(confRendUtenteServizioServerCaller, request);
		return confRendUtenteServizioServerCaller.cancel(in);
	}

	public ConfRendUtenteServizioSaveResponseType save(ConfRendUtenteServizioSaveRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(confRendUtenteServizioServerCaller, request);
		return confRendUtenteServizioServerCaller.save(in);
	}

	public ConfRendUtenteServizioGetListaXmlResponseType getListaXml(ConfRendUtenteServizioGetListaXmlRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(confRendUtenteServizioServerCaller, request);
		return confRendUtenteServizioServerCaller.getListaXml(in);
	}
}
