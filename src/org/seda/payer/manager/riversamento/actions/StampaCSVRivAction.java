package org.seda.payer.manager.riversamento.actions;

/*
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
//import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.riversamento.webservice.srv.FaultType;
import com.seda.payer.riversamento.webservice.dati.ElaboraStampaRequest;
import com.seda.payer.riversamento.webservice.dati.ElaboraStampaResponse;


public class StampaCSVRivAction extends BaseRiversamentoAction {


	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		try {
			String path = getNomeFileCsv(request);
			request.setAttribute("csvRiv", path);
			//request.setAttribute("csvRivD", "flussoRiversamento.zip");
			request.setAttribute("csvRivD", getFileNameFromPath(path));
		} 
		catch (FaultType fte) 
		{
			WSCache.logWriter.logError("errore: " + fte.getMessage1(),fte);
			fte.printStackTrace();
			setFormMessage("ricercaRiversamentiForm", "errore: " + decodeMessaggio(fte), request);
		}
		catch (RemoteException re)
		{
			WSCache.logWriter.logError("errore: " + re.getMessage(),re);
			re.printStackTrace();
			setFormMessage("ricercaRiversamentiForm", testoErroreColl, request);
			
		}
		catch (Exception e) {
//			setErrorMessage(e.getMessage());
			WSCache.logWriter.logError("download CSV Riversamento fallita", e);
			setFormMessage("ricercaRiversamentiForm", "download CSV Riversamento fallita", request);

		}
		return null;
	}

	private String getNomeFileCsv(HttpServletRequest request) throws Exception
	{
		String nomeFileReportCsv = "";
		String tipoDocumento = "csv";
		String codiceSocieta = request.getParameter("codSoc");
		String codiceUtente = request.getParameter("codUte");
		String dataRiversamento = request.getParameter("dataRiv");
		String enteBeneficiario = request.getParameter("codEnte");
		String tipoRiversamento = request.getParameter("tipoRend");
		String tipoRendicontazione = request.getParameter("tipoRiv");
		ElaboraStampaRequest elaboraStampaRequest = new ElaboraStampaRequest(tipoDocumento,codiceSocieta, codiceUtente, dataRiversamento, enteBeneficiario, tipoRiversamento, tipoRendicontazione);
		ElaboraStampaResponse elaboraStampaResponse = null;
		elaboraStampaResponse = WSCache.riversamentoServer.elaboraStampa(elaboraStampaRequest, request);
		request.setAttribute("retCode", elaboraStampaResponse.getRisultato().getRetCode());
		request.setAttribute("retMessage", elaboraStampaResponse.getRisultato().getRetMessage());
		nomeFileReportCsv = elaboraStampaResponse.getRisultato().getRetMessage().replace(".csv", ".zip");
		
		return nomeFileReportCsv;
	}
	
	private String getFileNameFromPath(String sPath)
	{
		int iIndexLinux = sPath.lastIndexOf("/");
		int iIndexWindows = sPath.lastIndexOf("\\");
		int iIndex = iIndexLinux > iIndexWindows ? iIndexLinux : iIndexWindows;
		
		return sPath.substring(iIndex + 1, sPath.length());
	}

}
*/