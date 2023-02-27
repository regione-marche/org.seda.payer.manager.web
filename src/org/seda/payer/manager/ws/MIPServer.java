package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.commons.srv.FaultType;
//PG150180_001 GG - inizio
import com.seda.payer.pgec.webservice.mip.dati.MCSDetailRequest;
import com.seda.payer.pgec.webservice.mip.dati.MCSDetailResponse;
import com.seda.payer.pgec.webservice.mip.dati.MICDetailRequest;
import com.seda.payer.pgec.webservice.mip.dati.MICDetailResponse;
import com.seda.payer.pgec.webservice.mip.dati.MICSaveCupStatusRequest;
import com.seda.payer.pgec.webservice.mip.dati.MICSaveCupStatusResponse;
import com.seda.payer.pgec.webservice.mip.dati.MINDetailRequest;
import com.seda.payer.pgec.webservice.mip.dati.MINDetailResponse;
import com.seda.payer.pgec.webservice.mip.dati.MINSavePaymentStatusRequest;
import com.seda.payer.pgec.webservice.mip.dati.MINSavePaymentStatusResponse;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMCSRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMCSResponse;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMICRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMICResponse;
//PG150180_001 GG - fine
import com.seda.payer.pgec.webservice.mip.dati.MIPDetailRequest;
import com.seda.payer.pgec.webservice.mip.dati.MIPDetailResponse;
import com.seda.payer.pgec.webservice.mip.dati.MIPSavePaymentStatusRequest;
import com.seda.payer.pgec.webservice.mip.dati.MIPSavePaymentStatusResponse;
//PG150180_001 GG - inizio
import com.seda.payer.pgec.webservice.mip.dati.MPNDetailRequest;
import com.seda.payer.pgec.webservice.mip.dati.MPNDetailResponse;
//PG150180_001 GG - fine
import com.seda.payer.pgec.webservice.mip.dati.MPSDetailRequest;
import com.seda.payer.pgec.webservice.mip.dati.MPSDetailResponse;
//PG150180_001 GG - inizio
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMINRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMINResponse;
//PG150180_001 GG - fine
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMIPRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMIPResponse;
//PG150180_001 GG - inizio
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMPNRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMPNResponse;
//PG150180_001 GG - fine
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMPSRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaMPSResponse;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaOneriRequest;
import com.seda.payer.pgec.webservice.mip.dati.RecuperaListaOneriResponse;
import com.seda.payer.pgec.webservice.mip.source.MIPSOAPBidingStub;
import com.seda.payer.pgec.webservice.mip.source.MIPServiceLocator;


public class MIPServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MIPSOAPBidingStub MIPCaller = null;

	private void setCodSocietaHeader(MIPSOAPBidingStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public MIPServer(String endPoint)
	{
		MIPServiceLocator lsService = new MIPServiceLocator();
		lsService.setMIPPortEndpointAddress(endPoint);
		try {
			MIPCaller = (MIPSOAPBidingStub)lsService.getMIPPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public RecuperaListaMIPResponse recuperaListaMIP(RecuperaListaMIPRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.recuperaListaMIP(in);
	}
	
	public RecuperaListaMPSResponse recuperaListaMPS(RecuperaListaMPSRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.recuperaListaMPS(in);
	}
	
	public MIPDetailResponse getModuloIntegrazionePagamenti(MIPDetailRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.getModuloIntegrazionePagamenti(in);
	}
	
	public MPSDetailResponse getModuloIntegrazionePagamentiPaymentStatus(MPSDetailRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.getModuloIntegrazionePagamentiPaymentStatus(in);
	}
	
	public MIPSavePaymentStatusResponse savePaymentStatus(MIPSavePaymentStatusRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.savePaymentStatus(in);
	}
	
	public RecuperaListaOneriResponse recuperaListaOneri(RecuperaListaOneriRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.recuperaListaOneri(in);
	}
	
	//PG150180_001 GG - inizio
	public RecuperaListaMINResponse recuperaListaMIN(RecuperaListaMINRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.recuperaListaMIN(in);
	}
	
	public RecuperaListaMPNResponse recuperaListaMPN(RecuperaListaMPNRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.recuperaListaMPN(in);
	}
	
	public MINDetailResponse getModuloIntegrazionePagamentiNodo(MINDetailRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.getModuloIntegrazionePagamentiNodo(in);
	}
	
	public MPNDetailResponse getModuloIntegrazionePagamentiNodoPaymentStatus(MPNDetailRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.getModuloIntegrazionePagamentiNodoPaymentStatus(in);
	}
	
	public MINSavePaymentStatusResponse savePaymentNodoStatus(MINSavePaymentStatusRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.savePaymentNodoStatus(in);
	}
	//PG150180_001 GG - fine
	//inizio LP PG200060
	//PG14001X_001 GG 25112014 - inizio
	//CUP
	public RecuperaListaMICResponse recuperaListaMIC(RecuperaListaMICRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.recuperaListaMIC(in);
	}
	
	public RecuperaListaMCSResponse recuperaListaMCS(RecuperaListaMCSRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.recuperaListaMCS(in);
	}
	
	public MICDetailResponse getModuloIntegrazioneCup(MICDetailRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.getModuloIntegrazioneCup(in);
	}
	
	public MCSDetailResponse getModuloIntegrazioneCupStatus(MCSDetailRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.getModuloIntegrazioneCupStatus(in);
	}
	
	public MICSaveCupStatusResponse saveCupStatus(MICSaveCupStatusRequest in, HttpServletRequest request) throws FaultType, RemoteException
	{	setCodSocietaHeader(MIPCaller, request);
		return MIPCaller.saveCupStatus(in);
	}
	//PG14001X_001 GG 25112014 - fine
	//fine LP PG200060
}
