package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.payer.pgec.webservice.user.dati.*;
import com.seda.payer.pgec.webservice.user.source.*;


public class UserServer extends BaseServer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UserImplementationStub userCaller = null;
	
	private void setCodSocietaHeader(UserImplementationStub stub, HttpServletRequest request) {
		stub.clearHeaders();
		stub.setHeader("",DBSCHEMACODSOCIETA,getCodSocieta(request));		
	}

	public UserServer(String endPoint) {
		UserServiceLocator lsService = new UserServiceLocator();
		lsService.setUserPortEndpointAddress(endPoint);
		try {
			userCaller = (UserImplementationStub)lsService.getUserPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public UserSearchResponse getUsers(UserSearchRequest in, HttpServletRequest request) throws RemoteException, FaultType
	{	setCodSocietaHeader(userCaller, request);
		return userCaller.getUsers(in);
	}
    public UserDetailResponse getUser(UserDetailRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(userCaller, request);
    	return userCaller.getUser(in);
    }
    public StatusResponse save(UserSaveRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(userCaller, request);
    	return userCaller.save(in);
    }
    public StatusResponse cancel(UserCancelRequest in, HttpServletRequest request) throws RemoteException, FaultType
    {	setCodSocietaHeader(userCaller, request);
    	return userCaller.cancel(in);
    }
}
