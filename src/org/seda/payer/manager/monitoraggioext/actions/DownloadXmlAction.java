package org.seda.payer.manager.monitoraggioext.actions;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.actions.BaseManagerAction;
import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.mip.dati.MIPDetailRequest;
import com.seda.payer.pgec.webservice.mip.dati.MIPDetailResponse;
import com.seda.payer.pgec.webservice.mip.dati.MPSDetailRequest;
import com.seda.payer.pgec.webservice.mip.dati.MPSDetailResponse;
import com.seda.payer.pgec.webservice.mip.dati.ModuloIntegrazionePagamenti;
import com.seda.payer.pgec.webservice.mip.dati.ModuloIntegrazionePagamentiPaymentStatus;

public class DownloadXmlAction extends BaseManagerAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException 
	{
		String nomeForm = "";
		if (request.getAttribute("form") != null)
			nomeForm = (String)request.getAttribute("form");
		
		String sXml = "";
		String nomeFile = "error.txt";
		
		try
		{
			if (request.getAttribute("idtrans") != null && request.getAttribute("source") != null && request.getAttribute("type") != null)
			{
				String idTransazione = (String)request.getAttribute("idtrans");
				String source = (String)request.getAttribute("source");
				String type = (String)request.getAttribute("type");
				
				if (!idTransazione.equals(""))
				{
					if (source.equalsIgnoreCase("mip"))
					{
						ModuloIntegrazionePagamenti mip = getMIP(idTransazione, nomeForm, request);
						if (mip != null)
						{
							if (type.equals("paymentrequest"))
								sXml = mip.getXmlPaymentRequest();
							else if (type.equals("paymentdata"))
								sXml = mip.getXmlPaymentData();
						}
					}
					else if (source.equalsIgnoreCase("mps"))
					{
						if (request.getAttribute("gruppo") != null && request.getAttribute("progr") != null)
						{
							int iGruppo = Integer.parseInt((String)request.getAttribute("gruppo"));
							int iProgr = Integer.parseInt((String)request.getAttribute("progr"));
							ModuloIntegrazionePagamentiPaymentStatus mps = getMPS(idTransazione, iGruppo, iProgr, nomeForm, request);
							if (mps != null)
							{
								if (type.equals("paymentdata"))
									sXml = mps.getXmlPaymentData();
								else if (type.equals("commitmsg"))
									sXml = mps.getXmlCommitMessage();
							}
						}
						else
							setFormMessage(nomeForm, "Parametri mancanti", request);
					}
					else
						setFormMessage(nomeForm, "Parametri non validi", request);
				}
				else
					setFormMessage(nomeForm, "Id transazione non valido", request);
				
				if (!sXml.equals(""))
				{
					if (!sXml.startsWith("<?xml"))
						sXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + sXml;
					
					//String s = "\"de09d687-b261-4a5b-971d-0eac980bc44d\";\"Lepida\";\"Regione Emilia Romagna\";\"04/05/2011 14:29:43\";\"04/05/2011 14:30:47\";\"10.10.80.254\";\"barnocchi@seda.it\";\"\";\"Web\";\"PayPal\";\"NO\";\"NO\";\"14,20\";\"COMPLETATA CON SUCCESSO\"";
					//String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Root><Nodo>AAAAA</Nodo></Root>";
					nomeFile = getNomeFile(request);
				}
				else
					setFormMessage(nomeForm, "Si è verificato un errore durante il download dei dati richiesti.", request);
				
			}
			else
				setFormMessage(nomeForm, "Parametri mancanti", request);
			
		}
		catch (Exception e) {
			e.printStackTrace();
			setFormMessage(nomeForm, e.getMessage(), request);
		}
		
		if (sXml.equals(""))
			sXml = "Errore nel download dei dati richiesti";
		
		request.setAttribute("nome_file_xml", nomeFile);
		
		request.setAttribute("file_xml", sXml);
		
		request.setAttribute(Field.TX_MESSAGE.format(), getMessage());
		request.setAttribute(Field.TX_ERROR_MESSAGE.format(), getErrorMessage());
		return null;
		
	}

	private String getNomeFile(HttpServletRequest request)
	{
		String nomeFile = "file.xml";
		
		String source = (String)request.getAttribute("source");
		String type = (String)request.getAttribute("type");
		if (source.equalsIgnoreCase("mip"))
		{
			if (type.equals("paymentrequest"))
				nomeFile = "PaymentRequest";
			else if (type.equals("paymentdata"))
				nomeFile = "PaymentData";
		}
		else if (source.equalsIgnoreCase("mps"))
		{
			if (type.equals("paymentdata"))
				nomeFile = "PaymentData_Detail";
			else if (type.equals("commitmsg"))
				nomeFile = "CommitMsg";
		}
			
		nomeFile += "_" + (String)request.getAttribute("idtrans") + ".xml";
		
		return nomeFile;
	}
	
	private ModuloIntegrazionePagamenti getMIP(String idTransazione, String formName, HttpServletRequest request)
	{
		MIPDetailRequest req = new MIPDetailRequest();
		req.setChiaveTransazione(idTransazione);
		
		try {
			MIPDetailResponse res = WSCache.mipServer.getModuloIntegrazionePagamenti(req, request);
			
			if (res != null)
				return res.getMip();
			else
				setFormMessage(formName, "Impossibile recuperare i dati richiesti.", request);
		} 
		catch (Exception e) {
			//setErrorMessage(e.getMessage());
			e.printStackTrace();
			setFormMessage(formName, e.getMessage() , request);
		}
		return null;
	}
	
	private ModuloIntegrazionePagamentiPaymentStatus getMPS(String idTransazione, int gruppo, int progressivo,
			String formName, HttpServletRequest request)
	{
		MPSDetailRequest req = new MPSDetailRequest();
		req.setChiaveTransazione(idTransazione);
		req.setGruppoTentativiNotifica(gruppo);
		req.setNumeroTentativoNotifica(progressivo);
		
		try {
			MPSDetailResponse res = WSCache.mipServer.getModuloIntegrazionePagamentiPaymentStatus(req, request);
			
			if (res != null)
				return res.getMipStatus();
			else
				setFormMessage(formName, "Impossibile recuperare i dati richiesti.", request);
		} 
		catch (Exception e) {
			//setErrorMessage(e.getMessage());
			e.printStackTrace();
			setFormMessage(formName, e.getMessage() , request);
		}
		return null;
	}
}
