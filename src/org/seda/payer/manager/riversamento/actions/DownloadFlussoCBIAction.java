package org.seda.payer.manager.riversamento.actions;

/*
import javax.servlet.http.HttpServletRequest;
import java.rmi.RemoteException;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
//import com.seda.payer.pgec.webservice.commons.srv.FaultType;
import com.seda.payer.riversamento.webservice.srv.FaultType;
import com.seda.payer.riversamento.webservice.dati.DownloadCBIRequest;
import com.seda.payer.riversamento.webservice.dati.DownloadCBIResponse;


public class DownloadFlussoCBIAction extends BaseRiversamentoAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		try {
			String filename = getNomeFileFlussoCBI(request);
			request.setAttribute("CBIRiv", filename);
			request.setAttribute("CBIRivD", "flussoCBI");
			setFormMessage("ricercaRiversamentiForm", "File scaricato", request);
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

	private String getNomeFileFlussoCBI(HttpServletRequest request) throws Exception
	{
		String nomeFileFlussoCBI = "";

		String codiceSocieta = request.getParameter("codSoc");
		String codiceUtente = request.getParameter("codUte");
		String dataRiversamento = request.getParameter("dataRiv");
		String enteBeneficiario = request.getParameter("codEnte");
		String tipoRiversamento = request.getParameter("tipoRend");
		String tipoRendicontazione = request.getParameter("tipoRiv");
		DownloadCBIRequest in = new DownloadCBIRequest(codiceSocieta, codiceUtente, dataRiversamento,tipoRiversamento, tipoRendicontazione, enteBeneficiario);
		DownloadCBIResponse out = null;
		out = WSCache.riversamentoServer.downloadCBI(in, request);
		request.setAttribute("retCode", out.getRisultato().getRetCode());
		request.setAttribute("retMessage", out.getRisultato().getRetMessage());
		nomeFileFlussoCBI = out.getNomeFlussoCBI();
		return nomeFileFlussoCBI;
	}

}
*/