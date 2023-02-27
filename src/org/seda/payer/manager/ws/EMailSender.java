package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.xml.rpc.ServiceException;

import com.seda.emailsender.webservices.dati.EMailSenderRequestType;
import com.seda.emailsender.webservices.dati.EMailSenderResponse;
import com.seda.emailsender.webservices.source.EMailSenderInterface;
import com.seda.emailsender.webservices.source.EMailSenderServiceLocator;
import com.seda.emailsender.webservices.srv.EMailSenderFaultType;

public class EMailSender extends BaseServer 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EMailSenderInterface emsCaller = null;
	
	public EMailSender(String endPoint)
	{
		EMailSenderServiceLocator emsService = new EMailSenderServiceLocator();
		emsService.setEMailSenderPortEndpointAddress(endPoint);
		try 
		{
			emsCaller = (EMailSenderInterface)emsService.getEMailSenderPort();
		}
		catch (ServiceException e)
		{
			e.printStackTrace();
		}
	} 
	
	public EMailSenderResponse sendEMail(String EMailSender,String EMailDataTOList, 
			String EMailDataCCList,
			String EMailDataCCNList,
			String EMailDataSubject,
			String EMailDataText,
			String EMailDataAttacchedFileList)
	{
		EMailSenderResponse emsRes = null;
		EMailSenderRequestType emsBean = new EMailSenderRequestType();
		emsBean.setEMailSender(EMailSender);
		emsBean.setEMailDataTOList(EMailDataTOList);
		emsBean.setEMailDataCCList(EMailDataCCList);
		emsBean.setEMailDataCCNList(EMailDataCCNList);
		emsBean.setEMailDataSubject(EMailDataSubject);
		emsBean.setEMailDataText(EMailDataText);
		emsBean.setEMailDataAttacchedFileList(EMailDataAttacchedFileList);

		try
		{
			emsRes = (EMailSenderResponse)emsCaller.getEMailSender(emsBean);
		}
		catch(EMailSenderFaultType e)
		{
			System.out.println("Remote exception: " + e.getMessage());
			System.out.println("EMailSenderFaultType: " + e.getMessage1());
		}
		catch (RemoteException ex)
		{
			ex.printStackTrace();
		}
		
		return emsRes;
	}
}

