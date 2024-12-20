package org.seda.payer.manager.wisconfig.actions;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.configurazione.actions.BaseConfigurazioneAction;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PayerCommand;
import org.seda.payer.manager.util.PayerCommandException;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.AggiornaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.InserisciTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaListaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTariffaRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.RecuperaTariffaResponse;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.VerificaTariffaComunicazioniRequest;
import com.seda.payer.impostasoggiorno.webservice.configurazione.dati.VerificaTariffaComunicazioniResponse;
import com.seda.payer.impostasoggiorno.webservice.dati.Paginazione;
import com.seda.payer.impostasoggiorno.webservice.dati.ResponseTypeRetCode;
import com.seda.payer.impostasoggiorno.webservice.dati.TariffaImpostaSoggiorno;
import com.seda.payer.pgec.webservice.srv.FaultType;

@SuppressWarnings("serial")
//inizio LP PG1800XX_016
//public class ImpostaSoggiornoTariffaInsUpd extends BaseConfigurazioneAction
public class ImpostaSoggiornoTariffaInsUpd extends BaseConfigurazioneAction implements PayerCommand
//fine  LP PG1800XX_016
{
	//inizio LP PG1800XX_016
	//public Object service(HttpServletRequest request) throws ActionException
	public Object execute(HttpServletRequest request) throws PayerCommandException {
	//fine  LP PG1800XX_016

		//inizio LP PG1800XX_016
		try {
		//fine  LP PG1800XX_016
			super.service(request, "impostaSoggiornoTariffa_var");
		//inizio LP PG1800XX_016
		} catch (ActionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//fine  LP PG1800XX_016
		
		TariffaImpostaSoggiorno tariffa=null;

		switch(firedButton)
		{
		case TX_BUTTON_RESET:
			//gg
			resetFormTariffa(request);	
			//resetParametri(request);
			break;
			
		case TX_BUTTON_NULL:
    	case TX_BUTTON_CANCEL:
			break;
			
		case TX_BUTTON_INDIETRO:
			break;
			
		case TX_BUTTON_EDIT:

			String chiaveTariffa = request.getAttribute("tx_chiaveTar") == null ? "":(String)request.getAttribute("tx_chiaveTar");
			caricaTariffaInForm(chiaveTariffa, request);	
			boolean bEdit = verificaTariffaComunicazioni(chiaveTariffa, request);
			if (!bEdit)
				setFormMessage("var_tariffa_form", "Non � possibile modificare la tariffa poich� risulta utilizzata in una o pi� comunicazioni", request);
			
			request.setAttribute("edit_tariffa", bEdit ? "Y" : "N");
			//inizio LP PG1800XX_016
			request.setAttribute("codop","edit");
			//fine LP PG1800XX_016
			
			String codiceProvincia = (request.getAttribute("tx_provincia") == null ? "" : (String)request.getAttribute("tx_provincia"));
		    loadDDLComuni(codiceProvincia, request);		
		    break;
			
		case TX_BUTTON_EDIT_END:

			tariffa = getTariffaFromRequest(request);
			
    		if (testCampiTariffaImpostaSoggiorno(tariffa, request))
    			inserimentoTariffa(tariffa, request);			
			break;
			
			
		case TX_BUTTON_AGGIUNGI:
			resetFormTariffa(request);
			//inizio LP PG1800XX_016
			request.setAttribute("codop","add");
			request.setAttribute("edit_tariffa", "Y");
			//fine LP PG1800XX_016
			break;
			
		case TX_BUTTON_AGGIUNGI_END:
			tariffa = getTariffaFromRequest(request);
			
    		if (testCampiTariffaImpostaSoggiorno(tariffa, request))
				aggiungiTariffa(tariffa, request);
			break;

		case TX_BUTTON_PROVINCIA_CHANGED: 
			codiceProvincia = (request.getAttribute("tx_provincia") == null ? "" : (String)request.getAttribute("tx_provincia"));
		    loadDDLComuni(codiceProvincia, request);		
		    break;
		}
		

		return null;
		
	}

	

	
	private void caricaTariffaInForm(String chiaveTariffa, HttpServletRequest request)
	{
		if (!chiaveTariffa.equals(""))
		{				
			RecuperaTariffaResponse detailResponse = null;
			TariffaImpostaSoggiorno tariffa = new TariffaImpostaSoggiorno();
			tariffa.setChiaveTariffa(chiaveTariffa);
			RecuperaTariffaRequest in = new RecuperaTariffaRequest();
			try {
				in.setTariffa(tariffa);
				detailResponse = WSCache.impostaSoggiornoConfigServer.recuperaTariffa(in, request);
				if (detailResponse != null)
				{
					if (detailResponse.getTariffa()!= null)
					{
						setTariffaToRequest(detailResponse.getTariffa(), request);
					}
					else setFormMessage("var_tariffa_form", Messages.GENERIC_ERROR.format(), request);
				}
				else setFormMessage("var_tariffa_form", Messages.GENERIC_ERROR.format(), request);
			} catch (FaultType e) {
				setFormMessage("var_tariffa_form", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("var_tariffa_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}
		}
	}
	
	private boolean verificaTariffaComunicazioni(String chiaveTariffa, HttpServletRequest request)
	{
		if (!chiaveTariffa.equals(""))
		{				
			VerificaTariffaComunicazioniRequest req = new VerificaTariffaComunicazioniRequest();
			req.setChiaveTariffa(chiaveTariffa);
			
			try {
				VerificaTariffaComunicazioniResponse res = WSCache.impostaSoggiornoConfigServer.verificaTariffaComunicazioni(req, request);
				if (res != null && res.getResponse()!= null && res.getResponse().getRetCode().getValue().equals("00"))
				{
					return !res.isTariffaInUso();
				}
				return false;
			} catch (FaultType e) {
				setFormMessage("var_tariffa_form", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("var_tariffa_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	private void inserimentoTariffa(TariffaImpostaSoggiorno tariffa, HttpServletRequest request)
	{
				
		UserBean user = getUserBean();
		tariffa.setOperatoreUltimoAggiornamento(user.getUserName());
		AggiornaTariffaRequest in = new AggiornaTariffaRequest();

		
	    in.setTariffa(tariffa);
			
		try {
				AggiornaTariffaResponse out = WSCache.impostaSoggiornoConfigServer.aggiornaTariffa(in, request);					
				setFormMessage("var_tariffa_form", out.getRisultato().getRetMessage(), request);
			} 
			catch (FaultType e) {
				setFormMessage("var_tariffa_form", "Errore: " + e.getLocalizedMessage(), request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("var_tariffa_form", "Errore: " + e.getMessage(), request);
				e.printStackTrace();
			}

	}
	
// AGGIUNGI	

	private void aggiungiTariffa(TariffaImpostaSoggiorno tariffa, HttpServletRequest request)
	{
		//Recupero del codice operatore
		UserBean user = getUserBean();
		tariffa.setOperatoreUltimoAggiornamento(user.getUserName());
		InserisciTariffaRequest in = new InserisciTariffaRequest();
		in.setTariffa(tariffa);
		
		try 
		{
			InserisciTariffaResponse out = WSCache.impostaSoggiornoConfigServer.inserisciTariffa(in, request);
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
	}
		
	private void setTariffaToRequest(TariffaImpostaSoggiorno tariffa, HttpServletRequest request)
	{

		    request.setAttribute("tx_chiave_tariffa", tariffa.getChiaveTariffa());
		    request.setAttribute("tx_comune", tariffa.getCodiceBelfiore());
	        request.setAttribute("tx_struttura", tariffa.getChiaveTipologiaStruttura());
	      
			Locale locale = (Locale) request.getSession().getAttribute(MAFAttributes.LOCALE);
			Calendar cal = Calendar.getInstance(locale);
			cal.setTime(tariffa.getDataValiditaTariffa());
	        
	        request.setAttribute("dataValidita", cal);
	        
	        request.setAttribute("tx_data_validita_year", cal.get(Calendar.YEAR));
	        String appoggio = "00" + (cal.get(Calendar.MONTH)+1);
	        request.setAttribute("tx_data_validita_month", appoggio.substring(appoggio.length()-2));
	        appoggio = "00" + cal.get(Calendar.DAY_OF_MONTH);
	        request.setAttribute("tx_data_validita_day", appoggio.substring(appoggio.length()-2));

	        String importo = tariffa.getImportoTariffa().toString().replace(".", ",");

	        request.setAttribute("tx_importo", importo);

	}

	private boolean testCampiTariffaImpostaSoggiorno(TariffaImpostaSoggiorno tariffa, HttpServletRequest request)
	{
		boolean risultato = true;

		String messaggio = "";
		
		if (tariffa.getDataValiditaTariffa()!=null&&!compareDateStringISO(sdf.format(tariffa.getDataValiditaTariffa()), "tx_data_validita", request))
		{
			messaggio = "<br>Inserire una data validit� corretta";
		}

		if (messaggio.length()>0)
		{
			messaggio = messaggio.substring(4);
			setFormMessage("var_tariffa_form", messaggio, request);
			risultato = false;
		}
		
		if (risultato)
		{
			risultato = !ricercaAnaloga(tariffa, request);
			if (!risultato)
			{
				setFormMessage("var_tariffa_form", "Esiste una anagrafica tariffa per lo stesso periodo", request);
			}
		}	
		
		return risultato;
	}

	private boolean ricercaAnaloga(TariffaImpostaSoggiorno tariffa, HttpServletRequest request)
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
			
			res = WSCache.impostaSoggiornoConfigServer.ricercaTariffe(in, request);

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
	}
	
	private void resetFormTariffa(HttpServletRequest request)
	{
		resetParametri(request);

		request.setAttribute("tx_chiaveTar", null);
		request.setAttribute("tx_chiave_tariffa","");
		request.setAttribute("tx_provincia","");
		request.setAttribute("tx_comune","");
        request.setAttribute("tx_struttura","");
        request.setAttribute("dataValidita",null);
        request.setAttribute("tx_data_validita",null);
        
        request.setAttribute("tx_data_validita_year","");
        request.setAttribute("tx_data_validita_month","");
        request.setAttribute("tx_data_validita_day","");

        request.setAttribute("tx_importo","");
        //GG resetto la lista comune
        request.setAttribute("listbelfiore", null);

	}
	
	
	private TariffaImpostaSoggiorno getTariffaFromRequest(HttpServletRequest request)
	{
		TariffaImpostaSoggiorno tariffa = new TariffaImpostaSoggiorno();
		
		tariffa.setChiaveTariffa(request.getAttribute("tx_chiave_tariffa") == null ? "" : (String)request.getAttribute("tx_chiave_tariffa"));

		tariffa.setCodiceBelfiore(request.getAttribute("tx_comune") == null ? "" : (String)request.getAttribute("tx_comune"));
        tariffa.setChiaveTipologiaStruttura(request.getAttribute("tx_struttura") == null ? "" : (String)request.getAttribute("tx_struttura"));
        tariffa.setDataValiditaTariffa(getDateByTag(request, "tx_data_validita"));
        
        String importo = request.getAttribute("tx_importo") == null ? "0,00" : (String)request.getAttribute("tx_importo");		        

        int index = importo.indexOf(',');
        
        String sParteIntera = importo.substring(0,index);
        String sParteDecimale = importo.substring(index + 1, importo.length());
        tariffa.setImportoTariffa(GenericsDateNumbers.getBigDecimalFromString(sParteIntera, sParteDecimale));
        
        //NB: NON USARE IL Double. Crea problemi di arrotondamento su alcuni Sistemi Operativi / WebServer.
        //importo = importo.replace(',','.');
        //tariffa.setImportoTariffa(new BigDecimal(Double.parseDouble(importo)));
        
        tariffa.setOperatoreUltimoAggiornamento( getUserBean().getUserName());

        return tariffa;
	}


}
