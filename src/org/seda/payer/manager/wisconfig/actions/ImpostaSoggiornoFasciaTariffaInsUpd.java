package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.configurazione.actions.BaseConfigurazioneAction;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaFasciaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaFasciaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciFasciaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciFasciaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaFasciaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaFasciaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.ResponseTypeRetCode;
import com.seda.payer.impostasoggiorno.webservice.dati.FasciaTariffaImpostaSoggiorno;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class ImpostaSoggiornoFasciaTariffaInsUpd extends BaseConfigurazioneAction
{

	public Object service(HttpServletRequest request) throws ActionException 
	{
		super.service(request, "impostaSoggiornoFasciaTariffa_var");

		FasciaTariffaImpostaSoggiorno fasciaTariffa = null;

		switch(firedButton)
		{
			case TX_BUTTON_RESET:
				resetFormFasciaTariffa(request);
				break;
				
			case TX_BUTTON_NULL:
	    	case TX_BUTTON_CANCEL:
				break;
				
			case TX_BUTTON_INDIETRO:
				break;
				
			case TX_BUTTON_EDIT:
				String provE = (String) request.getParameter("tx_provincia");
				if(provE == null) {
					provE =  (String) request.getAttribute("tx_provincia");
				}
				String comuneE = (String) request.getParameter("tx_comune");
				if(comuneE == null) {
					comuneE =  (String) request.getAttribute("tx_comune");
				}
				String struttE = (String) request.getParameter("tx_struttura");
				if(struttE == null) {
					struttE =  (String) request.getAttribute("tx_struttura");
				}
			    Object par = request.getAttribute("tx_chiaveFasciaTar");
				Long chiaveFasciaTariffa = (par == null ? new Long(0) : new Long(par.toString()));
				caricaFasciaTariffaInForm(chiaveFasciaTariffa, request);	
				boolean bEdit = verificaFasciaTariffaComunicazioni(chiaveFasciaTariffa, request);
				if (!bEdit) {
					setFormMessage("var_fascia_tariffa_form", "Non è possibile modificare la fascia tariffa poichè risulta utilizzata in una o più comunicazioni", request);
				}
				request.setAttribute("edit_fascia_tariffa", bEdit ? "Y" : "N");
				request.setAttribute("codop","edit");
				request.setAttribute("tx_provincia", provE);
				request.setAttribute("tx_comune", comuneE);
				request.setAttribute("tx_struttura", struttE);
			    loadDDLComuni(provE, request);		
			    break;
				
			case TX_BUTTON_EDIT_END:
				fasciaTariffa = getFasciaTariffaFromRequest(request);
	    		if (testCampiFasciaTariffaImpostaSoggiorno(fasciaTariffa, request))
	    			inserimentoFasciaTariffa(fasciaTariffa, request);			
				break;
	
			case TX_BUTTON_AGGIUNGI:
				String chiaveTariffa = (String) request.getParameter("tx_chiaveTar");
				String prov = (String) request.getParameter("tx_provincia");
				String comune = (String) request.getParameter("tx_comune");
				String strutt = (String) request.getParameter("tx_struttura");
				resetFormFasciaTariffa(request);
				request.setAttribute("vista","impostaSoggiornoTariffa_var");
				request.setAttribute("codop","add");
				request.setAttribute("chiaveTariffa", chiaveTariffa);
				request.setAttribute("tx_provincia", prov);
				request.setAttribute("tx_comune", comune);
				request.setAttribute("tx_struttura", strutt);
			    loadDDLComuni(prov, request);		
				break;
				
			case TX_BUTTON_AGGIUNGI_END:
				fasciaTariffa = getFasciaTariffaFromRequest(request);
	    		if(testCampiFasciaTariffaImpostaSoggiorno(fasciaTariffa, request))
					aggiungiFasciaTariffa(fasciaTariffa, request);
				break;
		}
		return null;
	}

	private void caricaFasciaTariffaInForm(Long chiaveFasciaTariffa, HttpServletRequest request)
	{
		if (chiaveFasciaTariffa != null && chiaveFasciaTariffa.intValue() != 0)
		{				
			RecuperaFasciaTariffaResponse detailResponse = null;
			FasciaTariffaImpostaSoggiorno fasciaTariffa = new FasciaTariffaImpostaSoggiorno();
			fasciaTariffa.setChiaveFasciaTariffa(chiaveFasciaTariffa);
			RecuperaFasciaTariffaRequest in = new RecuperaFasciaTariffaRequest();
			try {
				in.setTariffa(fasciaTariffa);
				detailResponse = WSCache.impostaSoggiornoConfigServer.recuperaFasciaTariffa(in, request);
				if (detailResponse != null)
				{
					if (detailResponse.getTariffa()!= null)
					{
						setFasciaTariffaToRequest(detailResponse.getTariffa(), request);
					}
					else setFormMessage("var_fascia_tariffa_form", Messages.GENERIC_ERROR.format(), request);
				}
				else setFormMessage("var_fascia_tariffa_form", Messages.GENERIC_ERROR.format(), request);
			} catch (FaultType e) {
				setFormMessage("var_fascia_tariffa_form", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("var_fascia_tariffa_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}
		}
	}
	
	private boolean verificaFasciaTariffaComunicazioni(Long chiaveFasciaTariffa, HttpServletRequest request)
	{
		if (chiaveFasciaTariffa != null && chiaveFasciaTariffa.intValue() != 0)
		{
			/*
			VerificaFasciaTariffaComunicazioniRequest req = new VerificaFasciaTariffaComunicazioniRequest();
			req.setChiaveFasciaTariffa(chiaveFasciaTariffa);
			
			try {
				VerificaFasciaTariffaComunicazioniResponse res = WSCache.impostaSoggiornoConfigServer.verificaFasciaTariffaComunicazioni(req, request);
				if (res != null && res.getResponse()!= null && res.getResponse().getRetCode().getValue().equals("00"))
				{
					return !res.isTariffaInUso();
				}
				return false;
				
			} catch (FaultType e) {
				setFormMessage("var_fascia_tariffa_form", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("var_fascia_tariffa_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}
			*/
		}
		return true;
	}
	
	private void inserimentoFasciaTariffa(FasciaTariffaImpostaSoggiorno fasciaTariffa, HttpServletRequest request)
	{
		UserBean user = getUserBean();
		fasciaTariffa.setOperatoreUltimoAggiornamento(user.getUserName());
		AggiornaFasciaTariffaRequest in = new AggiornaFasciaTariffaRequest();

	    in.setTariffa(fasciaTariffa);
			
		try {
			AggiornaFasciaTariffaResponse out = WSCache.impostaSoggiornoConfigServer.aggiornaFasciaTariffa(in, request);					
			setFormMessage("var_fascia_tariffa_form", out.getRisultato().getRetMessage(), request);
		} 
		catch (FaultType e) {
			setFormMessage("var_fascia_tariffa_form", "Errore: " + e.getLocalizedMessage(), request);
			e.printStackTrace();
		} catch (RemoteException e) {
			setFormMessage("var_fascia_tariffa_form", "Errore: " + e.getMessage(), request);
			e.printStackTrace();
		}
	}
	
// AGGIUNGI	
	private void aggiungiFasciaTariffa(FasciaTariffaImpostaSoggiorno fasciaTariffa, HttpServletRequest request)
	{
		//Recupero del codice operatore
		UserBean user = getUserBean();
		fasciaTariffa.setOperatoreUltimoAggiornamento(user.getUserName());
		if(fasciaTariffa.getChiaveTariffa() == null || fasciaTariffa.getChiaveTariffa().equals("")) {
			String chiaveTariffa = (String) request.getAttribute("chiaveTariffa");
			fasciaTariffa.setChiaveTariffa(chiaveTariffa);
		}
		InserisciFasciaTariffaRequest in = new InserisciFasciaTariffaRequest();
		in.setTariffa(fasciaTariffa);
		
		try 
		{
			InserisciFasciaTariffaResponse out = WSCache.impostaSoggiornoConfigServer.inserisciFasciaTariffa(in, request);
			setFormMessage("var_fascia_tariffa_form", out.getRisultato().getRetMessage(), request);
			if (out.getRisultato().getRetCode().toString().equals(ResponseTypeRetCode._value1.toString())) {
				resetFormFasciaTariffa(request);
			}
		} 
		catch (FaultType e) 
		{
			setFormMessage("var_fascia_tariffa_form", "Errore: " + e.getLocalizedMessage(), request);
			e.printStackTrace();
		} 
		catch (RemoteException e) {
			setFormMessage("var_fascia_tariffa_form", "Errore: " + e.getMessage(), request);
			e.printStackTrace();
		}
	}
		
	private void setFasciaTariffaToRequest(FasciaTariffaImpostaSoggiorno fasciaTariffa, HttpServletRequest request)
	{
		    request.setAttribute("tx_chiaveFasciaTar", fasciaTariffa.getChiaveFasciaTariffa());
		    request.setAttribute("tx_chiave_tariffa", fasciaTariffa.getChiaveTariffa());
		    request.setAttribute("tx_codice", fasciaTariffa.getCodice());
	        request.setAttribute("tx_descrizione", fasciaTariffa.getDescrizione());
/*	      
			Locale locale = (Locale) request.getSession().getAttribute(MAFAttributes.LOCALE);
			Calendar cal = Calendar.getInstance(locale);
			cal.setTime(fasciaTariffa.getDataValiditaTariffa());
	        
	        request.setAttribute("dataValidita", cal);
       
	        request.setAttribute("tx_data_validita_year", cal.get(Calendar.YEAR));
	        String appoggio = "00" + (cal.get(Calendar.MONTH)+1);
	        request.setAttribute("tx_data_validita_month", appoggio.substring(appoggio.length()-2));
	        appoggio = "00" + cal.get(Calendar.DAY_OF_MONTH);
	        request.setAttribute("tx_data_validita_day", appoggio.substring(appoggio.length()-2));
*/
	        String importo = fasciaTariffa.getImportoTariffa().toString().replace(".", ",");
        	request.setAttribute("tx_importo", importo);
	}

	private boolean testCampiFasciaTariffaImpostaSoggiorno(FasciaTariffaImpostaSoggiorno fasciaTariffa, HttpServletRequest request)
	{
		boolean risultato = true;
		risultato = !ricercaAnaloga(fasciaTariffa, request);
		if (!risultato)
		{
			setFormMessage("var_fascia_tariffa_form", "Esiste già una fascia tariffa con lo stesso codice", request);
		}
		return risultato;
	}

	private boolean ricercaAnaloga(FasciaTariffaImpostaSoggiorno fasciaTariffa, HttpServletRequest request)
	{
		boolean trovato = false;
		if(fasciaTariffa.getChiaveFasciaTariffa() == 0) {
			try {
				RecuperaFasciaTariffaRequest in = new RecuperaFasciaTariffaRequest();
				in.setTariffa(fasciaTariffa);
				RecuperaFasciaTariffaResponse res = WSCache.impostaSoggiornoConfigServer.recuperaFasciaTariffa(in, request);
				if(res != null && res.getTariffa() != null) {
					trovato = true;
				}
			} catch (FaultType e) {
			} catch (RemoteException e) {
			}
		}
		return trovato;
	}
	
	private void resetFormFasciaTariffa(HttpServletRequest request)
	{
		resetParametri(request);
		/*
		request.setAttribute("tx_chiaveFasciaTar", null);
		request.setAttribute("tx_chiave_tariffa", "");
		*/
		request.setAttribute("tx_codice", "");
        request.setAttribute("tx_descrizione", "");
    	request.setAttribute("tx_importo", "");
	}
	
	//inizio LP PG190010_002_LP
    private static String convertiByteEuro(String s) {
        String out = s;
        try {
        	byte[] b = s.getBytes("ISO-8859-1");
        	ArrayList<Integer> lista = new ArrayList<Integer>();
        	for(int i = 0; i < b.length; i++) {
        		if(b[i] == -128) {//128 codice ascii carattere € 
        			lista.add(new Integer(i));
        		}
        	}
        	if(lista.size() > 0) {
        		for(int k = 0; k < lista.size(); k++) {
        			int ik = lista.get(k);
        			s = s.substring(0, ik) + "€" + s.substring(ik + 1);
        		}
        		out = s;
        	}
        } catch (java.io.UnsupportedEncodingException e) {
        	e.printStackTrace();
            return "";
        }
        return out;
    }	
	//fine LP PG190010_002_LP
	
	private FasciaTariffaImpostaSoggiorno getFasciaTariffaFromRequest(HttpServletRequest request)
	{
		FasciaTariffaImpostaSoggiorno fasciaTariffa = new FasciaTariffaImpostaSoggiorno();

	    Object par = request.getAttribute("tx_chiaveFasciaTar");
		Long chiaveFasciaTariffa = (par == null ? new Long(0) : new Long(par.toString()));
		fasciaTariffa.setChiaveFasciaTariffa(chiaveFasciaTariffa);
		fasciaTariffa.setChiaveTariffa(request.getAttribute("tx_chiave_tariffa") == null ? "" : (String) request.getAttribute("tx_chiave_tariffa"));
		if(fasciaTariffa.getChiaveTariffa() == null || fasciaTariffa.getChiaveTariffa().equals("")) {
			String chiaveTariffa = (String) request.getAttribute("chiaveTariffa");
			fasciaTariffa.setChiaveTariffa(chiaveTariffa);
		}
		fasciaTariffa.setCodice(request.getAttribute("tx_codice") == null ? "" : (String) request.getAttribute("tx_codice"));
    	//inizio LP PG190010_002_LP
		//fasciaTariffa.setDescrizione(request.getAttribute("tx_descrizione") == null ? "" : (String) request.getAttribute("tx_descrizione"));
    	//fasciaTariffaDto.setDescrizione(fasciaTariffa.getDescrizione());
		String descForm = (request.getAttribute("tx_descrizione") == null ? "" : (String) request.getAttribute("tx_descrizione"));
		String parseDesc = convertiByteEuro(descForm);		
    	parseDesc = parseDesc.replace("€", "&euro;").replace("<", "&lt;").replace(">", "&gt;");
    	//per prevenire errore in scrittura sulla colonna descrizione
    	if(parseDesc.length() > 120) {
    		parseDesc = parseDesc.substring(0,119);
    	}
    	fasciaTariffa.setDescrizione(parseDesc);
    	//fine LP PG190010_002_LP
		
        String importo = request.getAttribute("tx_importo") == null ? "0,00" : (String) request.getAttribute("tx_importo");
        int index = importo.indexOf(',');
        if(index == -1) {
        	importo = "0,00";
        	index = importo.indexOf(',');
        }
        String sParteIntera = importo.substring(0, index);
        String sParteDecimale = importo.substring(index + 1, importo.length());
        fasciaTariffa.setImportoTariffa(GenericsDateNumbers.getBigDecimalFromString(sParteIntera, sParteDecimale));

        //NB: NON USARE IL Double. Crea problemi di arrotondamento su alcuni Sistemi Operativi / WebServer.			setFormMessage("delete_form", out.getRisultato().getRetMessage(), request);

        //importo = importo.replace(',','.');
        //tariffa.setImportoTariffa(new BigDecimal(Double.parseDouble(importo)));

        fasciaTariffa.setOperatoreUltimoAggiornamento(getUserBean().getUserName());
        return fasciaTariffa;
	}
}
