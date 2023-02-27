package org.seda.payer.manager.riversamento.actions;
/*
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
//import com.seda.payer.pgec.webservice.commons.srv.FaultType;
//import com.seda.payer.eccedenze.webservice.dati.ResponseTypeRetCode;
import com.seda.payer.riversamento.webservice.srv.FaultType;
import com.seda.payer.riversamento.webservice.dati.ElaboraStampaRequest;
import com.seda.payer.riversamento.webservice.dati.ElaboraStampaResponse;
import com.seda.payer.riversamento.webservice.dati.ResponseType;

public class StampaPDFRivAction extends BaseRiversamentoAction {
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		try {
			String filename = getNomeFilePdf(request);
			request.setAttribute("pdfRiv", filename);
			request.setAttribute("pdfRivD", "reportRiversamento.pdf");
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
			WSCache.logWriter.logError("download PDF Riversamento fallita", e);
			setFormMessage("ricercaRiversamentiForm", "download PDF Riversamento fallita", request);
		}
		return null;
	}

	private String getNomeFilePdf(HttpServletRequest request) throws Exception
	{
		String nomeFileReportPdf = "";
		String tipoDocumento = "pdf";
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
		if (!elaboraStampaResponse.getRisultato().getRetCode().getValue().equals("00"))
			{
			 setFormMessage("ricercaRiversamentiForm", elaboraStampaResponse.getRisultato().getRetMessage(), request);
			 nomeFileReportPdf = "";
			}
		else
			nomeFileReportPdf = elaboraStampaResponse.getRisultato().getRetMessage();
		return nomeFileReportPdf;
	}
}
*/