package org.seda.payer.manager.ws;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;

import com.seda.payer.pgec.webservice.srv.FaultType;
import com.seda.security.webservice.dati.AggiornaUtentePIVARequestType;
import com.seda.security.webservice.dati.AggiornaUtentePIVAResponseType;
import com.seda.security.webservice.dati.CambioPWDRequestType;
import com.seda.security.webservice.dati.CambioPWDResponseType;
import com.seda.security.webservice.dati.ClassificazioneMerceologicaRequestType;
import com.seda.security.webservice.dati.ClassificazioneMerceologicaResponseType;
import com.seda.security.webservice.dati.InserisciUtentePIVARequestType;
import com.seda.security.webservice.dati.InserisciUtentePIVAResponseType;
import com.seda.security.webservice.dati.ListaClassificazioniMerceologicheDDLRequestType;
import com.seda.security.webservice.dati.ListaClassificazioniMerceologicheDDLResponseType;
import com.seda.security.webservice.dati.ListaFamiglieMerceologicheDDLRequestType;
import com.seda.security.webservice.dati.ListaFamiglieMerceologicheDDLResponseType;
import com.seda.security.webservice.dati.ListaUtentiRequestType;
import com.seda.security.webservice.dati.ListaUtentiResponseType;
import com.seda.security.webservice.dati.LoginRequestType;
import com.seda.security.webservice.dati.LoginResponseType;
import com.seda.security.webservice.dati.ResetPWDRequestType;
import com.seda.security.webservice.dati.ResetPWDResponseType;
import com.seda.security.webservice.dati.SelezionaUtentePIVARequestType;
import com.seda.security.webservice.dati.SelezionaUtentePIVAResponseType;
import com.seda.security.webservice.dati.SelezionaUtentePIVAbyCFRequestType;
import com.seda.security.webservice.dati.SendMailCodiciRequestType;
import com.seda.security.webservice.dati.SendMailCodiciResponseType;
import com.seda.security.webservice.source.SecurityPort;
import com.seda.security.webservice.source.SecuritySOAPBinding;

public class SecurityServer extends BaseServer {

	private static final long serialVersionUID = 1L;
	
	private SecuritySOAPBinding securityCaller = null;
	
	public SecurityServer(String endPoint)
	{
		SecurityPort lsService = new SecurityPort();
		lsService.setSecurityPortEndpointAddress(endPoint);
		try {
			securityCaller = (SecuritySOAPBinding)lsService.getSecurityPort();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	
	public LoginResponseType login(LoginRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{
		String codSoc = getCodSocieta(request);
		codSoc = codSoc==null?"":codSoc.trim();
		if(codSoc.equals("000TO")){
			in.setCodSoc(codSoc.concat("|Y"));
		}else{
			in.setCodSoc(getCodSocieta(request));
		}
		return securityCaller.login(in);
	}

	public SendMailCodiciResponseType sendMailCodici(SendMailCodiciRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{
		in.setCodSoc(getCodSocieta(request));
		return securityCaller.sendMailCodici(in);
	}
	
	public ListaUtentiResponseType listaUtenti(ListaUtentiRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{
		in.setCodSoc(getCodSocieta(request));
		return securityCaller.listaUtenti(in);
	}
	
	
	

	
	public InserisciUtentePIVAResponseType inserisciUtentePIVA(InserisciUtentePIVARequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{
		in.setCodSoc(getCodSocieta(request));
		return securityCaller.inserisciUtentePIVA(in);
	}
	
	public SelezionaUtentePIVAResponseType selezionaUtentePIVA(SelezionaUtentePIVARequestType in, HttpServletRequest request)throws RemoteException, FaultType
	{
		in.setCodSoc(getCodSocieta(request));
		return securityCaller.selezionaUtentePIVA(in);
	}
	public SelezionaUtentePIVAResponseType selezionaUtentePIVAbyCF(SelezionaUtentePIVAbyCFRequestType in, HttpServletRequest request)throws RemoteException, FaultType
	{
		in.setCodSoc(getCodSocieta(request));
		return securityCaller.selezionaUtentePIVAbyCF(in);
	}
	
	public AggiornaUtentePIVAResponseType aggiornaUtentePIVA(AggiornaUtentePIVARequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{
		in.setCodSoc(getCodSocieta(request));
		return securityCaller.aggiornaUtentePIVA(in);
	}
	
	public CambioPWDResponseType cambioPWD(CambioPWDRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{
		in.setCodSoc(getCodSocieta(request));
		return securityCaller.cambioPWD(in);
	}
	
	public ResetPWDResponseType resetPWD(ResetPWDRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{
		in.setCodSoc(getCodSocieta(request));
		return securityCaller.resetPWD(in);
	}
	
	
	
	public ListaFamiglieMerceologicheDDLResponseType getListaFamiglieMerceologiche(ListaFamiglieMerceologicheDDLRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{
		return securityCaller.listaFamiglieMerceologiche(in);
	}
	
	public ListaClassificazioniMerceologicheDDLResponseType getListaClassificazioniMerceologiche(ListaClassificazioniMerceologicheDDLRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{
		return securityCaller.listaClassificazioniMerceologiche(in);
	}
	
	public ClassificazioneMerceologicaResponseType getDettaglioClassificazioneMerceologica(ClassificazioneMerceologicaRequestType in, HttpServletRequest request) throws RemoteException, FaultType
	{
		return securityCaller.selezionaClassificazioneMerceologica(in);
	}
}
