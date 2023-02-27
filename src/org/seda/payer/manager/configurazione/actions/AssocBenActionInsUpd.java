package org.seda.payer.manager.configurazione.actions;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.assocben.dati.AssocBen;
import com.seda.payer.pgec.webservice.assocben.dati.AssocBenDetailRequest;
import com.seda.payer.pgec.webservice.assocben.dati.AssocBenDetailResponse;
import com.seda.payer.pgec.webservice.assocben.dati.AssocBenSaveRequest;
import com.seda.payer.pgec.webservice.assocben.dati.StatusResponse;
import com.seda.payer.pgec.webservice.associmpben.dati.ResponseTypeRetCode;
import com.seda.payer.pgec.webservice.srv.FaultType;


@SuppressWarnings("serial")
public class AssocBenActionInsUpd extends BaseRiversamento2Action
{


	public Object service(HttpServletRequest request) throws ActionException 
	{
		super.service(request, "SchedaAssocBen");

		HttpSession session = request.getSession();
		AssocBen associazione=null;

		switch(firedButton)
		{
		case TX_BUTTON_RESET:
			//gg
			resetFormAssociazione(request);
			setProfile(request);
			//resetParametri(request);
			break;
			
		case TX_BUTTON_NULL:
    	case TX_BUTTON_CANCEL:
			break;
			
		case TX_BUTTON_INDIETRO:
			session.setAttribute("noStato", "A");
			break;
			
		case TX_BUTTON_EDIT:
			caricaAssociazioneInForm(request);			
		    break;
			
		case TX_BUTTON_EDIT_END:

			associazione = getAssociazioneFromRequest(request);
    		if (testCampiAssocBen(associazione, request))
    			saveAssocBen(associazione, request);			
			break;
			
			
		case TX_BUTTON_AGGIUNGI:
			resetFormAssociazione(request);
			//resetDDLSession(request);
			break;
			
		case TX_BUTTON_AGGIUNGI_END:
			associazione = getAssociazioneFromRequest(request);
			
    		if (testCampiAssocBen(associazione, request))
    			saveAssocBen(associazione, request);
			break;

		/*case TX_BUTTON_PROVINCIA_CHANGED: 
			codiceProvincia = (request.getAttribute("tx_provincia") == null ? "" : (String)request.getAttribute("tx_provincia"));
		    loadDDLComuni(codiceProvincia, request);		
		    break;*/
		}
		aggiornamentoCombo(request,request.getSession());
		

		return null;
		
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
	
	private void saveAssocBen(AssocBen associazione, HttpServletRequest request)
	{
		String azione = request.getAttribute("codop").equals("add")?"Inserimento":"Aggiornamento";
		UserBean user = getUserBean();
		AssocBenSaveRequest in = new AssocBenSaveRequest();
		associazione.setOperatorCode(user.getUserName());
		if(request.getAttribute("codop").equals("add"))
			in.setCodOp(TypeRequest.ADD_SCOPE.scope());
		else if(request.getAttribute("codop").equals("edit"))
			in.setCodOp(TypeRequest.EDIT_SCOPE.scope());
		
	    in.setAssocBen(associazione);
			
		try {
			StatusResponse out = WSCache.assocBenServer.save(in, request);					
				if (out.getResponse().getRetCode().toString().equals("00")){
					setFormMessage("var_assocben_form", azione+" eseguito ", request);
					if(request.getAttribute("codop").equals("add"))
						resetFormAssociazione(request);
				}
			//setFormMessage("var_assocben_form", out.getResponse().getRetMessage(), request);
			} 
			catch (FaultType e) {
				setFormMessage("var_assocben_form", "Errore: " + e.getMessage1(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("var_assocben_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}

	}
	
// AGGIUNGI	

	/*private void inserisciAssocBen(AssocBen associazione, HttpServletRequest request)
	{
		//Recupero del codice operatore
		UserBean user = getUserBean();
		associazione.setOperatorCode(user.getUserName());
		InserisciTariffaRequest in = new InserisciTariffaRequest();
		in.setTariffa(tariffa);
		
		try 
		{
			InserisciTariffaResponse out = WSCache.impostaSoggiornoConfigServer.inserisciTariffa(in);
			setFormMessage("var_tariffa_form", out.getRisultato().getRetMessage(), request);
			if (out.getRisultato().getRetCode().toString().equals(ResponseTypeRetCode._value1.toString()))
				resetFormTariffa(request);			

		} 
		catch (FaultType e) 
		{
			setFormMessage("var_tariffa_form", "Errore: " + e.getLocalizedMessage(), request);
			e.printStackTrace();
		} 
		catch (RemoteException e) {
			setFormMessage("var_tariffa_form", "Errore: " + e.getMessage(), request);
			e.printStackTrace();
		}
	}*/
		
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

	private boolean testCampiAssocBen(AssocBen associazione, HttpServletRequest request)
	{
		boolean risultato = true;

		//String messaggio = "";
		
		/*if (tariffa.getDataValiditaTariffa()!=null&&!compareDateStringISO(sdf.format(tariffa.getDataValiditaTariffa()), "tx_data_validita", request))
		{
			messaggio = "<br>Inserire una data validità corretta";
		}

		if (messaggio.length()>0)
		{
			messaggio = messaggio.substring(4);
			setFormMessage("var_tariffa_form", messaggio, request);
			risultato = false;
		}
		
		if (risultato)
		{
			risultato = !ricercaAnaloga(tariffa);
			if (!risultato)
			{
				setFormMessage("var_tariffa_form", "Esiste una anagrafica tariffa per lo stesso periodo", request);
			}
		}*/	
		
		return risultato;
	}

	/*private boolean ricercaAnaloga(TariffaImpostaSoggiorno tariffa)
	{
		boolean trovato = true;
		TariffaImpostaSoggiorno ric = new TariffaImpostaSoggiorno();
		
		ric.setCodiceBelfiore(tariffa.getCodiceBelfiore());
		ric.setChiaveTipologiaStruttura(tariffa.getChiaveTipologiaStruttura());
		
//		ric.setDescrizioneComune(tariffa.getDescrizioneComune());
//		ric.setDescrizioneTipologiaRicettiva(tariffa.getDescrizioneTipologiaRicettiva());

		ric.setDescrizioneComune("");
		ric.setDescrizioneTipologiaRicettiva("");
		
		
		ric.setRicercaDataDa(sdf.format(tariffa.getDataValiditaTariffa()));
		ric.setRicercaDataA(sdf.format(tariffa.getDataValiditaTariffa()));
		
		try 
		{
			RecuperaListaTariffaResponse res = null;
			RecuperaListaTariffaRequest in = new RecuperaListaTariffaRequest();

			Paginazione pag = new Paginazione();
			
			pag.setPageNumber(1);
			pag.setRowsPerPage(1);
			pag.setOrder("");
			
			in.setPagina(pag);
						
			in.setTariffa(ric);
			
			res = WSCache.impostaSoggiornoConfigServer.ricercaTariffe(in);

			if (res.getPInfo() != null)
			{
				if (res.getPInfo().getNumRows() == 0)
					trovato = false;
				else
				{
					if (!tariffa.getChiaveTariffa().equals(""))
						{
							if (res.getListXml().indexOf(tariffa.getChiaveTariffa())>0)
								trovato = false;
						}
				}
			}
		} 
		catch (FaultType e) 
		{
		} 
		catch (RemoteException e) 
		{
		}				
		
		return trovato;
	}*/
	
	private void resetFormAssociazione(HttpServletRequest request)
	{
		resetParametri(request);

		request.setAttribute("codSocieta", "");
		request.setAttribute("codUtente","");
		request.setAttribute("codBeneficiario","");
		request.setAttribute("annoRifDa","");
        request.setAttribute("annoRifA","");
        request.setAttribute("tipoRendicontazione","");
        request.setAttribute("strumRiversamento","");
        request.setAttribute("metodoRend","");
        request.setAttribute("dataValidita",null);
        request.setAttribute("tx_data_validita",null);
        
        request.setAttribute("tx_data_validita_year","");
        request.setAttribute("tx_data_validita_month","");
        request.setAttribute("tx_data_validita_day","");
	}
	
	private AssocBen getAssociazioneFromRequest(HttpServletRequest request)
	{
		AssocBen associazione = new AssocBen();
		
		associazione.setCodProvincia("");
		associazione.setCodSocieta(request.getAttribute("codSocieta") == null ? "" : (String)request.getAttribute("codSocieta"));
		associazione.setCodUtente(request.getAttribute("codUtente") == null ? "" : ((String)request.getAttribute("codUtente")).substring(5));
		associazione.setCodBeneficiario(request.getAttribute("codBeneficiario") == null ? "" : ((String)request.getAttribute("codBeneficiario")).substring(10));
		associazione.setAnnoRifDa(request.getAttribute("annoRifDa") == null ? "" : (String)request.getAttribute("annoRifDa"));
		associazione.setAnnoRifA(request.getAttribute("annoRifA") == null ? "" : (String)request.getAttribute("annoRifA"));
		associazione.setTipoRendicontazione(request.getAttribute("tipoRendicontazione") == null ? "" : (String)request.getAttribute("tipoRendicontazione"));
		associazione.setStrumRiversamento(request.getAttribute("strumRiversamento") == null ? "" : (String)request.getAttribute("strumRiversamento"));
		associazione.setMetodoRend(request.getAttribute("metodoRend") == null ? "" : (String)request.getAttribute("metodoRend"));
		
        associazione.setDataValidita(getDateByTag(request, "tx_data_validita")==null?"":sdf.format(getDateByTag(request, "tx_data_validita")));

        return associazione;
	}
	
	private void aggiornamentoCombo(HttpServletRequest request, HttpSession session)
	{
			if (getFiredButton(request)==FiredButton.TX_BUTTON_SOCIETA_CHANGED) 
			{
				loadDDLUtente(request, session, codiceSocieta, null, true);
				LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, null, null, null, true);
				request.setAttribute("codUtente", "");
			}
			else if (getFiredButton(request)==FiredButton.TX_BUTTON_UTENTE_CHANGED) 
			{
				loadDDLUtente(request, session, codiceSocieta, null, false);
				LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, null, null, codiceUtente, true);
			}
			else 
			{
				loadDDLUtente(request, session, codiceSocieta, null, true);
				LoadListaUtentiEntiAllXml_DDL(request, session, codiceSocieta, null, null, codiceUtente, true);
			}
		}



}
