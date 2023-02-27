package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.adminusers.dati.AggiornaUtenteRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.AggiornaUtenteResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.EliminaUtenteRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.EliminaUtenteResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.InserisciUtenteRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.InserisciUtenteResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.ListaUtentiRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.ListaUtentiResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.ListaUtenzeCsvRequest;
import com.seda.payer.pgec.webservice.adminusers.dati.ListaUtenzeCsvResponse;
import com.seda.payer.pgec.webservice.adminusers.dati.SelezionaUtenteRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.SelezionaUtenteResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.UserInfoRequestType;
import com.seda.payer.pgec.webservice.adminusers.dati.UserInfoResponseType;
import com.seda.payer.pgec.webservice.adminusers.dati.UserProfileListRequest;
import com.seda.payer.pgec.webservice.adminusers.dati.UserProfileListResponse;
import com.seda.payer.pgec.webservice.adminusers.source.AdminusersImplementationStub;
import com.seda.payer.pgec.webservice.adminusers.source.AdminusersServiceLocator;
import com.seda.payer.pgec.webservice.srv.FaultType;

public class AdminUsersServer extends BaseServer {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AdminusersImplementationStub adminusersCaller = null;
	
	private void setCodSocietaHeader (AdminusersImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}
	public AdminUsersServer (String endPoint)
	{
		AdminusersServiceLocator lsService = new AdminusersServiceLocator();
		lsService.setadminusersPortEndpointAddress(endPoint);
		try {
			adminusersCaller = (AdminusersImplementationStub)lsService.getadminusersPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public AggiornaUtenteResponseType aggiornaUtente(AggiornaUtenteRequestType in, HttpServletRequest request)throws RemoteException, FaultType
	{	setCodSocietaHeader(adminusersCaller, request);
		return adminusersCaller.aggiornaUtente(in);
	}
	
	public EliminaUtenteResponseType eliminaUtente(EliminaUtenteRequestType in, HttpServletRequest request )throws RemoteException, FaultType
	{	setCodSocietaHeader(adminusersCaller, request);
		return adminusersCaller.eliminaUtente(in);
	}
	
	public InserisciUtenteResponseType inserisciUtente(InserisciUtenteRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(adminusersCaller, request);
		return adminusersCaller.inserisciUtente(in);
	}
	
	public SelezionaUtenteResponseType selezionaUtente(SelezionaUtenteRequestType in, HttpServletRequest request)throws RemoteException, FaultType
	{	setCodSocietaHeader(adminusersCaller, request);
		return adminusersCaller.selezionaUtente(in);
	}
	
	public ListaUtentiResponseType listaUtenti(ListaUtentiRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{
		setCodSocietaHeader(adminusersCaller, request);
		return adminusersCaller.listaUtenti(in);
	}
	
	public UserInfoResponseType getUserInfo (UserInfoRequestType in, HttpServletRequest request) throws FaultType, RemoteException 
	{
		setCodSocietaHeader(adminusersCaller, request);
		return adminusersCaller.getUserInfo(in);
	}
	public UserProfileListResponse getUserProfileListByUserName(UserProfileListRequest in, HttpServletRequest request) throws FaultType, RemoteException 
	{
		setCodSocietaHeader(adminusersCaller, request);
		return adminusersCaller.getUserProfileListByUserName(in);
	}
	
	public ListaUtenzeCsvResponse listaUtenzeCsv(ListaUtenzeCsvRequest in,  HttpServletRequest request) throws FaultType, RemoteException 
	{
		setCodSocietaHeader(adminusersCaller, request);
		return adminusersCaller.listaUtenzeCsv(in);
	}
	
}
