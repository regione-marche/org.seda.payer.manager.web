package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseManagerAction.FiredButton;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.impostasoggiorno.webservice.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.assocben.dati.AssocBen;
import com.seda.payer.pgec.webservice.assocben.dati.AssocBenCancelRequest;
import com.seda.payer.pgec.webservice.assocben.dati.AssocBenDetailRequest;
import com.seda.payer.pgec.webservice.assocben.dati.AssocBenDetailResponse;
import com.seda.payer.pgec.webservice.assocben.dati.StatusResponse;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class AssocBenActionCancel extends BaseRiversamento2Action{

//	public static String[] labelEx = {"confermaDisabilitata"};
//	public static String[] stringheE = (String[]) ArrayUtils.addAll(labelBottoni, labelEx );
	
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request, "CancellaAssocBen");

		switch(firedButton)
		{
		case TX_BUTTON_NULL:
    	case TX_BUTTON_CANCEL:
    		break;
			
		case TX_BUTTON_DELETE:
    		caricaAssociazioneInForm(request);
			request.setAttribute("confermaDisabilitata", false);
			break;

		case TX_BUTTON_DELETE_END:
			eliminaBeneficiario(request);
			break;
			
		case TX_BUTTON_INDIETRO:
			break;
		}
		loadCombo(request,request.getSession());

		return null;
		
	}

	private void eliminaBeneficiario(HttpServletRequest request)
	{
		AssocBen associazione = new AssocBen();
		associazione.setCodProvincia("");
		associazione.setCodSocieta(request.getAttribute("chiaveSocieta") == null ? "":(String)request.getAttribute("chiaveSocieta"));
		associazione.setCodUtente(request.getAttribute("chiaveUtente") == null ? "":(String)request.getAttribute("chiaveUtente"));
		associazione.setCodBeneficiario(request.getAttribute("chiaveBenef") == null ? "":(String)request.getAttribute("chiaveBenef"));
		associazione.setDataValidita(request.getAttribute("chiavedataVal") == null || ((String)request.getAttribute("chiavedataVal")).equals("")  ? null:((String)request.getAttribute("chiavedataVal")).substring(0,10));
		associazione.setAnnoRifDa("");
		associazione.setAnnoRifA("");
		associazione.setTipoRendicontazione("");
		associazione.setStrumRiversamento("");
		associazione.setMetodoRend("");
		associazione.setOperatorCode("");

		AssocBenCancelRequest cancelRequest = new AssocBenCancelRequest();
		cancelRequest.setAssocBen(associazione);
		try {
			StatusResponse out = WSCache.assocBenServer.cancel(cancelRequest, request);
			
			if(out.getResponse().getRetCode().getValue().equals(ResponseTypeRetCode._value1))
					request.setAttribute("confermaDisabilitata", true);
	
			setFormMessage("delete_form", out.getResponse().getRetMessage(), request);
		} catch (FaultType e) {
			setFormMessage("delete_form", "Errore: " + e.getLocalizedMessage(), request);
			e.printStackTrace();
		} catch (RemoteException e) {
			setFormMessage("delete_form", "Errore: " + e.getMessage(), request);
			e.printStackTrace();
		}
		
	}
	
	private void caricaAssociazioneInForm(HttpServletRequest request)
	{
		String chiaveSocieta = request.getAttribute("chiaveSocieta") == null ? "":(String)request.getAttribute("chiaveSocieta");
		String chiaveUtente = request.getAttribute("chiaveUtente") == null ? "":(String)request.getAttribute("chiaveUtente");
		String chiaveBenef = request.getAttribute("chiaveBenef") == null ? "":(String)request.getAttribute("chiaveBenef");
		String chiavedataVal = request.getAttribute("chiavedataVal") == null || ((String)request.getAttribute("chiavedataVal")).equals("")  ? null:((String)request.getAttribute("chiavedataVal")).substring(0,10);
		if (!chiaveSocieta.equals("") && !chiaveSocieta.equals("") && !chiaveBenef.equals("") && chiavedataVal != null)
		{				
			AssocBenDetailResponse detailResponse = null;
			AssocBen associazione = new AssocBen();
			associazione.setCodProvincia("");
			associazione.setCodSocieta(chiaveSocieta);
			associazione.setCodUtente(chiaveUtente);
			associazione.setCodBeneficiario(chiaveBenef);
			associazione.setDataValidita(chiavedataVal);
			associazione.setAnnoRifDa("");
			associazione.setAnnoRifA("");
			associazione.setTipoRendicontazione("");
			associazione.setStrumRiversamento("");
			associazione.setMetodoRend("");
			associazione.setOperatorCode("");
			AssocBenDetailRequest in = new AssocBenDetailRequest();
			try {
				in.setAssocBen(associazione);
				detailResponse = WSCache.assocBenServer.getAssociazioneBen(in, request);
				if (detailResponse != null)
				{
					if (detailResponse.getAssocBen()!= null)
					{
						setAssociazioneToRequest(detailResponse.getAssocBen(), request);
					}
					else setFormMessage("var_assocben_form", Messages.GENERIC_ERROR.format(), request);
				}
				else setFormMessage("var_assocben_form", Messages.GENERIC_ERROR.format(), request);
			} catch (FaultType e) {
				setFormMessage("var_assocben_form", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("var_assocben_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}
		}
	}
	
	private void setAssociazioneToRequest(AssocBen associazione, HttpServletRequest request)
	{

		    request.setAttribute("codSocieta", associazione.getCodSocieta());
		    request.setAttribute("codUtente",  associazione.getCodSocieta() + associazione.getCodUtente());
	        request.setAttribute("codBeneficiario", associazione.getCodSocieta() + associazione.getCodUtente() + associazione.getCodBeneficiario());
	        request.setAttribute("annoRifDa", associazione.getAnnoRifDa());
	        request.setAttribute("annoRifA", associazione.getAnnoRifA());
	        request.setAttribute("tipoRendicontazione", associazione.getTipoRendicontazione());
	        request.setAttribute("strumRiversamento", associazione.getStrumRiversamento());
	        request.setAttribute("metodoRend", associazione.getMetodoRend());
	        
	        Calendar cal = getCalendarByString(request, associazione.getDataValidita());
	        
	        request.setAttribute("tx_data_validita", cal);
	        
	        request.setAttribute("tx_data_validita_year", cal.get(Calendar.YEAR));
	        String appoggio = "00" + (cal.get(Calendar.MONTH)+1);
	        request.setAttribute("tx_data_validita_month", appoggio.substring(appoggio.length()-2));
	        appoggio = "00" + cal.get(Calendar.DAY_OF_MONTH);
	        request.setAttribute("tx_data_validita_day", appoggio.substring(appoggio.length()-2));
	}

	private void loadCombo(HttpServletRequest request, HttpSession session)
	{
			loadDDLUtente(request, session, codiceSocieta, null, true);
			LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, null, null, codiceUtente, true);
	}
	
	
	
}
