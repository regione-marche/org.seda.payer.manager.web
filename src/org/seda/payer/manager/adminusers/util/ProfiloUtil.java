package org.seda.payer.manager.adminusers.util;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.string.Convert;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComDetailRequest;
import com.seda.payer.pgec.webservice.anagprovcom.dati.AnagProvComDetailResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaComuniBySiglaProvinciaRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaComuniBySiglaProvinciaResponse;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaProvinceResponse;
import com.seda.security.webservice.dati.ClassificazioneMerceologicaRequestType;
import com.seda.security.webservice.dati.ClassificazioneMerceologicaResponseType;
import com.seda.security.webservice.dati.ListaClassificazioniMerceologicheDDLRequestType;
import com.seda.security.webservice.dati.ListaClassificazioniMerceologicheDDLResponseType;
import com.seda.security.webservice.dati.ListaFamiglieMerceologicheDDLRequestType;
import com.seda.security.webservice.dati.ListaFamiglieMerceologicheDDLResponseType;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.dati.SelezionaUtentePIVARequestType;
import com.seda.security.webservice.dati.SelezionaUtentePIVAResponseType;

public class ProfiloUtil extends BaseAdminusersAction{

	private static final long serialVersionUID = 1L;

	public static void loadProvince(HttpServletRequest request) 
	{
		try {
			RecuperaProvinceResponse res = WSCache.commonsServer.recuperaProvince(request);
			request.setAttribute("listprovincia", res.getListXml());
			
		} catch (Exception e) {
			WSCache.logWriter.logError("loadProvince", e);
		}	
	}
	
	public static void loadComuni(HttpServletRequest request, String siglaProvincia, String sRequestName) 
	{
		try {
			RecuperaComuniBySiglaProvinciaResponse res = WSCache.commonsServer.recuperaComuni_SiglaProvincia(new RecuperaComuniBySiglaProvinciaRequest(siglaProvincia), request);
			request.setAttribute(sRequestName, res.getListXml());
		} catch (Exception e) {
			WSCache.logWriter.logError("loadComuni", e);			
		} 
	}
	
	public static String getListProvince(HttpServletRequest request) 
	{
		try {
			RecuperaProvinceResponse res = WSCache.commonsServer.recuperaProvince(request);
			return res.getListXml();
			
		} catch (Exception e) {
			WSCache.logWriter.logError("getListProvince", e);
		}
		return "";
	}
	
	public static String getDescrizioneComune(String codiceBelfiore, HttpServletRequest request)
	{
		if (codiceBelfiore.trim().length() > 0)
		{
			try {
				AnagProvComDetailRequest req = new AnagProvComDetailRequest();
				req.setCodiceBelfiore(codiceBelfiore);
				
				AnagProvComDetailResponse res = WSCache.anagProvComServer.getAnagProvCom(req, request);
				if (res != null && res.getAnagprovcom() != null)
					return res.getAnagprovcom().getDescrizioneComune();
				else
					WSCache.logWriter.logInfo("getDescrizioneComune ERRORE");
				
			} catch (Exception e) {
				WSCache.logWriter.logError("getDescrizioneComune", e);
			}
		}
		return "";
	}
	
	public static String getDescrizioneProvincia(String listProvinceXml, String siglaProvincia)
	{
		//inizio LP PG21XX04 Leak
		WebRowSet wrs = null;
		//fine LP PG21XX04 Leak
		try {
			//inizio LP PG21XX04 Leak
			//WebRowSet wrs = Convert.stringToWebRowSet(listProvinceXml);
			wrs = Convert.stringToWebRowSet(listProvinceXml);
			//fine LP PG21XX04 Leak
			if (wrs != null)
			{
				while (wrs.next())
				{
					if (wrs.getString(2).equals(siglaProvincia))
						return wrs.getString(1);
				}
			}
			
		} catch (Exception e) {
			WSCache.logWriter.logError("getDescrizioneProvincia", e);
		}
		//inizio LP PG21XX04 Leak
		finally
		{
	    	try {
	    		if(wrs != null) {
	    			wrs.close();
	    		}
	    	} catch (SQLException e) {
	    		e.printStackTrace();
			}
		}
		//fine LP PG21XX04 Leak
		return "";
	}
	
	public static String getCodFamigliaMerceologica(String codiceClassificazioneMerceologica, HttpServletRequest request)
	{
		if (codiceClassificazioneMerceologica != null && codiceClassificazioneMerceologica.trim().length() > 0)
		{
			try {
				ClassificazioneMerceologicaRequestType req = new ClassificazioneMerceologicaRequestType();
				req.setCodiceClassificazioneMerceologica(codiceClassificazioneMerceologica);
				
				ClassificazioneMerceologicaResponseType res = WSCache.securityServer.getDettaglioClassificazioneMerceologica(req, request);
				if (res != null && res.getResponse() != null && res.getResponse().getRetCode().equals("00"))
					return res.getCodiceFamigliaMerceologica();
				else
					WSCache.logWriter.logInfo("getCodFamigliaMerceologica ERRORE");
				
			} catch (Exception e) {
				WSCache.logWriter.logError("getCodFamigliaMerceologica", e);
			}
		}
		return "";
	}
	
	public static void loadFamiglieMerceologiche(HttpServletRequest request) 
	{
		try {
			ListaFamiglieMerceologicheDDLResponseType res = WSCache.securityServer.getListaFamiglieMerceologiche(new ListaFamiglieMerceologicheDDLRequestType(""), request);
			request.setAttribute("listfamigliemerceologiche", res.getListXml());
		} catch (Exception e) {
			WSCache.logWriter.logError("loadFamiglieMerceologiche", e);			
		} 
	}
	
	//inizio LP PG190010_002_LP
	//public static void loadCategorieMerceologiche(HttpServletRequest request, String codiceFamigliaMerceologica) 
	public static void loadCategorieMerceologiche(HttpServletRequest request, String codiceFamigliaMerceologica, boolean isSoris) 
	//fine LP PG190010_002_LP
	{
		try {
			ListaClassificazioniMerceologicheDDLResponseType res = WSCache.securityServer.getListaClassificazioniMerceologiche(new ListaClassificazioniMerceologicheDDLRequestType(codiceFamigliaMerceologica), request);
			//inizio LP PG190010_002_LP
			if(codiceFamigliaMerceologica.equals("55") && !isSoris) {
				String xmlMod = res.getListXml();
				String targetCR = "\r";
				if(xmlMod.indexOf(targetCR) == -1) {
					targetCR = "";
				}
				String target0 = "    <currentRow>" + targetCR + "\n";
				String targetN = "</currentRow>";
				int ik0 = target0.length() + 1; 
				int ikN = targetN.length(); 
				//rimozione categoria 55.20.61
				int ikIni = xmlMod.indexOf("      <columnValue>55.20.61</columnValue>");
				String prima = xmlMod.substring(0, ikIni - ik0);
				String dopo = xmlMod.substring(ikIni);
				int ikFin =  dopo.indexOf(targetN) + ikN + ikIni;
				dopo =  xmlMod.substring(ikFin);
				xmlMod = prima + dopo;
				//rimozione categoria 55.20.62
				ikIni = xmlMod.indexOf("      <columnValue>55.20.62</columnValue>");
				prima = xmlMod.substring(0, ikIni - ik0);
				dopo = xmlMod.substring(ikIni);
				ikFin =  dopo.indexOf(targetN) + ikN + ikIni;
				dopo =  xmlMod.substring(ikFin);
				xmlMod = prima + dopo;
				request.setAttribute("listcategoriemerceologiche", xmlMod);
			} else {
			//fine LP PG190010_002_LP
				request.setAttribute("listcategoriemerceologiche", res.getListXml());
			//inizio LP PG190010_002_LP
			}
			//fine LP PG190010_002_LP
				
		} catch (Exception e) {
			WSCache.logWriter.logError("loadCategorieMerceologiche", e);			
		} 
	}
	
	
	public static String invertiFlag(String flagAttuale)
	{
		if (flagAttuale.equals("Y"))
			return "N";
		else if (flagAttuale.equals("N"))
			return "Y";
		
		return "N";
	}
	
	public static void clearProfilo(Profilo profilo)
	{
		Class<Profilo> aClass = Profilo.class;
		Field[] fields = aClass.getDeclaredFields();
		for (Field field : fields)
		{
			try {
				if (!field.isAccessible())
					field.setAccessible(true);
				field.set(profilo, null);
			} catch (Exception e) {}
		}
	}
	
/*	public static boolean validaUserName(String username, String formToSetError, HttpServletRequest request, HttpSession session)
	{
		String message = "";
		
		if (username.length() < 8)
			message = "Valorizzare almeno 8 caratteri";
		else
		{
			String usernameRegex = ((String)session.getServletContext().getAttribute("usernameRegex")).replace("accept=","").replace(";", "");
			if (!username.matches(usernameRegex))
				message =  (String)session.getServletContext().getAttribute("messageWordReq");
		}
		if (!message.equals(""))
			Generics.setFormMessage(formToSetError, "Username: " + message, request);
		
		return message.equals("");
	}
	*/
	public static boolean verificaDisponibilitàUsername(String username, HttpServletRequest request)
	{
		try
		{
			//verifico che lo username inserito appartenga ad utente esistente nella tabella SEUSRTB
			SelezionaUtentePIVARequestType req = new SelezionaUtentePIVARequestType();
			req.setUserName(username);
			SelezionaUtentePIVAResponseType res = WSCache.securityServer.selezionaUtentePIVA(req, request);
			ResponseType response = null;
			
			if (res != null && res.getResponse() != null)
			{
				response = res.getResponse();
				String retCode = response.getRetCode();
				String retMessage = response.getRetMessage();
				if (retCode.equals("00"))
				{
					//lo username è già presente
					return false;
				}
				else
				{
					if (res.getSelezionaUtentePIVAResponse() == null)
						return true; //lo username non è presente
					else
					{
						WSCache.logWriter.logInfo("verificaDisponibilitàUsername - ERRORE: " + retCode + " - " + retMessage);
						return false;
					}
				}
			}
			else
			{
				WSCache.logWriter.logInfo("verificaDisponibilitàUsername - ERRORE");
				return false;
			}
		}
		catch (Exception e) {
			WSCache.logWriter.logError("verificaDisponibilitàUsername", e);
			return false;
		}
	}
	
/*	public static String getParamFlagAbilitazioneAutomatica()
	{
		if (PortalManagerStarter.configuration != null)
		{
			try
			{
				return PortalManagerStarter.configuration.getProperty(PropertiesPath.registrazioneAbilitazioneAutomatica.format());
			} catch (Exception e) {
				WSCache.logWriter.logError("getParamFlagAbilitazioneAutomatica: ERRORE durante la lettura del parametro dal file di properties" , e);
			}
		}
		else
			WSCache.logWriter.logInfo("getParamFlagAbilitazioneAutomatica: ERRORE durante la lettura del parametro dal file di properties");
		
		//di default torno Y
		return "Y";
	}*/
	
	public static Calendar getMaxDate()
	{
		Timestamp timestamp = Timestamp.valueOf("2099-12-31 23:59:59.000");
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timestamp.getTime());
		
		return cal;
	}
	
	public static String calcoloCodiceFiscale(Profilo profilo)
	{
		String codiceFiscale = "";
		
		try {
			Lettere lettere = new Lettere();
			
			/*
			 * Cognome
			Sono necessari 3 caratteri per rappresentare il cognome, e sono la prima la seconda 
			e la terza consonante del cognome.
			E' possibile che le consonanti siano meno di tre, in questo caso è possibile 
			aggiungere le vocali nell'ordine in cui compaiono nel cognome.
			Per cognomi più corti di 3 caratteri, è possibile sostituire il carattere mancante con la lettera X.
			Chiaramente se ci sono cognomi con più parti, è necessario rimuovere gli spazi 
			e considerare tutto come un cognome unico.
			*/
			
			lettere.elaboraParola(profilo.getCognome().toUpperCase());
			
			if (lettere.consonanti.size() >= 3)
				codiceFiscale += String.valueOf(lettere.consonanti.get(0)) + String.valueOf(lettere.consonanti.get(1)) + String.valueOf(lettere.consonanti.get(2));
			else 
			{
				int lettereMancanti = 3;
				//meno di 3 consonanti
				if (lettere.consonanti.size() == 2)
				{
					codiceFiscale += String.valueOf(lettere.consonanti.get(0)) + String.valueOf(lettere.consonanti.get(1));
					lettereMancanti = 1;
				}
				else if (lettere.consonanti.size() == 1)
				{
					codiceFiscale += String.valueOf(lettere.consonanti.get(0));
					lettereMancanti = 2;
				}
				
				if (lettere.vocali.size() >= lettereMancanti)
				{
					//ho abbastanza vocali per completare la stringa di 3
					for (int i=0; i<lettereMancanti; i++)
						codiceFiscale += String.valueOf(lettere.vocali.get(i));
				}
				else
				{
					//non ho abbastanza vocali, riempio con le X
					for (int i=0; i<lettere.vocali.size(); i++)
						codiceFiscale += String.valueOf(lettere.vocali.get(i));
					for (int i=0; i<(lettereMancanti - lettere.vocali.size()); i++)
						codiceFiscale += "X";
				}
			}
			
			/*
			 * Nome
				Per il nome il discorso è analogo con la particolarità che se il nome è composto da 4 o più consonanti 
				vengono prese nell'ordine la prima, la terza e la quarta.
				Anche qui potremmo trovarci nella situazione di un numero di consonanti 
				minore di 3 e allo stesso modo si aggiungo le vocali.
				Ripetiamo anche qui che se il nome è più corto di 3 lettere è possibile 
				sostituire i caratteri mancanti con delle X.
				Se il nome fosse composto da più nomi, bisogna considerarlo tutto assieme.
			 */
			lettere.elaboraParola(profilo.getNome().toUpperCase());
			
			if (lettere.consonanti.size() > 3)
				codiceFiscale += String.valueOf(lettere.consonanti.get(0)) + String.valueOf(lettere.consonanti.get(2)) + String.valueOf(lettere.consonanti.get(3));
			else if (lettere.consonanti.size() == 3)
				codiceFiscale += String.valueOf(lettere.consonanti.get(0)) + String.valueOf(lettere.consonanti.get(1)) + String.valueOf(lettere.consonanti.get(2));
			else 
			{
				int lettereMancanti = 3;
				//meno di 3 consonanti
				if (lettere.consonanti.size() == 2)
				{
					codiceFiscale += String.valueOf(lettere.consonanti.get(0)) + String.valueOf(lettere.consonanti.get(1));
					lettereMancanti = 1;
				}
				else if (lettere.consonanti.size() == 1)
				{
					codiceFiscale += String.valueOf(lettere.consonanti.get(0));
					lettereMancanti = 2;
				}
				
				if (lettere.vocali.size() >= lettereMancanti)
				{
					//ho abbastanza vocali per completare la stringa di 3
					for (int i=0; i<lettereMancanti; i++)
						codiceFiscale += String.valueOf(lettere.vocali.get(i));
				}
				else
				{
					//non ho abbastanza vocali, riempio con le X
					for (int i=0; i<lettere.vocali.size(); i++)
						codiceFiscale += String.valueOf(lettere.vocali.get(i));
					for (int i=0; i<(lettereMancanti - lettere.vocali.size()); i++)
						codiceFiscale += "X";
				}
			}
	
			/*
			 * Anno di nascita
				Per l'anno vengono prese semplicemente le ultime due cifre.
			 */
			String sYear = String.valueOf(profilo.getDataNascita().get(Calendar.YEAR));
			codiceFiscale += sYear.substring(2);
			
			/*
			 * Per quanto riguarda il mese c'è una tabella di conversione. 
			 * Ad ogni mese corrisponde una lettera dell'alfabeto:
			 */
		
			int iMonth = profilo.getDataNascita().get(Calendar.MONTH) + 1;
			switch (iMonth)
			{
				case 1: codiceFiscale += "A"; break;
				case 2: codiceFiscale += "B"; break;
				case 3: codiceFiscale += "C"; break;
				case 4: codiceFiscale += "D"; break;
				case 5: codiceFiscale += "E"; break;
				case 6: codiceFiscale += "H"; break;
				case 7: codiceFiscale += "L"; break;
				case 8: codiceFiscale += "M"; break;
				case 9: codiceFiscale += "P"; break;
				case 10: codiceFiscale += "R"; break;
				case 11: codiceFiscale += "S"; break;
				case 12: codiceFiscale += "T"; break;
			}
			
			/*
			 * Giorno
				In questo caso è sufficiente riportare il numero del giorno, 
				con il particolare che per le donne questo numero dev'essere aumentato di 40
			 */
			String sesso = profilo.getSesso();
			int iDay = profilo.getDataNascita().get(Calendar.DAY_OF_MONTH);
			if (sesso.equals("F"))
				iDay += 40;
			
			codiceFiscale += GenericsDateNumbers.formatNumToString(2, String.valueOf(iDay));
			
			/*
			 * Comune di nascita
			E' composto da quattro caratteri alfanumerici. 
			E' il codice del comune rilevato dai volumi dei codici dei comuni italiani. (Codice Belfiore)
			 */
			codiceFiscale += profilo.getComuneNascitaBelfiore();
			
			codiceFiscale += getCodiceControlloCodiceFiscale(codiceFiscale);
			
			return codiceFiscale;
			
		} catch (Exception e) {
			return "";
		}
	}
	
	
	private static String getCodiceControlloCodiceFiscale(String codiceFiscale)
	{
		/*Si comincia con il prendere i caratteri del codice fiscale fin qui calcolato che sono 15, 
		 * si prendono quelli in posizione pari e si convertono con i 
		 * numeri corrispondenti della prima tabella. Tutti questi numeri vengono sommati.
		 */
		int iTotalePari = 0;
		for (int i=1; i<14; i=i+2)
		{
			char c = codiceFiscale.charAt(i);
			switch (c) {
				case (int)'0': iTotalePari += 0; break;
				case (int)'1': iTotalePari += 1; break;
				case (int)'2': iTotalePari += 2; break;
				case (int)'3': iTotalePari += 3; break;
				case (int)'4': iTotalePari += 4; break;
				case (int)'5': iTotalePari += 5; break;
				case (int)'6': iTotalePari += 6; break;
				case (int)'7': iTotalePari += 7; break;
				case (int)'8': iTotalePari += 8; break;
				case (int)'9': iTotalePari += 9; break;
				case (int)'A': iTotalePari += 0; break;
				case (int)'B': iTotalePari += 1; break;
				case (int)'C': iTotalePari += 2; break;
				case (int)'D': iTotalePari += 3; break;
				case (int)'E': iTotalePari += 4; break;
				case (int)'F': iTotalePari += 5; break;
				case (int)'G': iTotalePari += 6; break;
				case (int)'H': iTotalePari += 7; break;
				case (int)'I': iTotalePari += 8; break;
				case (int)'J': iTotalePari += 9; break;
				case (int)'K': iTotalePari += 10; break;
				case (int)'L': iTotalePari += 11; break;
				case (int)'M': iTotalePari += 12; break;
				case (int)'N': iTotalePari += 13; break;
				case (int)'O': iTotalePari += 14; break;
				case (int)'P': iTotalePari += 15; break;
				case (int)'Q': iTotalePari += 16; break;
				case (int)'R': iTotalePari += 17; break;
				case (int)'S': iTotalePari += 18; break;
				case (int)'T': iTotalePari += 19; break;
				case (int)'U': iTotalePari += 20; break;
				case (int)'V': iTotalePari += 21; break;
				case (int)'W': iTotalePari += 22; break;
				case (int)'X': iTotalePari += 23; break;
				case (int)'Y': iTotalePari += 24; break;
				case (int)'Z': iTotalePari += 25; break;
			}
		}
			
		/*Allo stesso modo con i caratteri dispari che devono essere convertiti 
		 * però utilizzando la seconda tabella e vengono tutti sommati.
		 */
		int iTotaleDispari = 0;
		for (int i=0; i<15; i=i+2)
		{
			char c = codiceFiscale.charAt(i);
			switch (c) {
				case (int)'0': iTotaleDispari += 1; break;
				case (int)'1': iTotaleDispari += 0; break;
				case (int)'2': iTotaleDispari += 5; break;
				case (int)'3': iTotaleDispari += 7; break;
				case (int)'4': iTotaleDispari += 9; break;
				case (int)'5': iTotaleDispari += 13; break;
				case (int)'6': iTotaleDispari += 15; break;
				case (int)'7': iTotaleDispari += 17; break;
				case (int)'8': iTotaleDispari += 19; break;
				case (int)'9': iTotaleDispari += 21; break;
				case (int)'A': iTotaleDispari += 1; break;
				case (int)'B': iTotaleDispari += 0; break;
				case (int)'C': iTotaleDispari += 5; break;
				case (int)'D': iTotaleDispari += 7; break;
				case (int)'E': iTotaleDispari += 9; break;
				case (int)'F': iTotaleDispari += 13; break;
				case (int)'G': iTotaleDispari += 15; break;
				case (int)'H': iTotaleDispari += 17; break;
				case (int)'I': iTotaleDispari += 19; break;
				case (int)'J': iTotaleDispari += 21; break;
				case (int)'K': iTotaleDispari += 2; break;
				case (int)'L': iTotaleDispari += 4; break;
				case (int)'M': iTotaleDispari += 18; break;
				case (int)'N': iTotaleDispari += 20; break;
				case (int)'O': iTotaleDispari += 11; break;
				case (int)'P': iTotaleDispari += 3; break;
				case (int)'Q': iTotaleDispari += 6; break;
				case (int)'R': iTotaleDispari += 8; break;
				case (int)'S': iTotaleDispari += 12; break;
				case (int)'T': iTotaleDispari += 14; break;
				case (int)'U': iTotaleDispari += 16; break;
				case (int)'V': iTotaleDispari += 10; break;
				case (int)'W': iTotaleDispari += 22; break;
				case (int)'X': iTotaleDispari += 25; break;
				case (int)'Y': iTotaleDispari += 24; break;
				case (int)'Z': iTotaleDispari += 23; break;
			}
		}

		/*I valori ottenuti vengono a loro volta sommati e il totale viene diviso per 26.*/
		int resto = (iTotalePari + iTotaleDispari) % 26;
		
		/*Il resto della divisione dev'essere convertito usando l'ultima tabella.
		* Il carattere corrispondente è il codice di controllo!
		*/
		switch (resto)
		{
			case 0: return "A";
			case 1: return "B"; 
			case 2: return "C"; 
			case 3: return "D"; 
			case 4: return "E"; 
			case 5: return "F"; 
			case 6: return "G"; 
			case 7: return "H"; 
			case 8: return "I"; 
			case 9: return "J"; 
			case 10: return "K"; 
			case 11: return "L"; 
			case 12: return "M"; 
			case 13: return "N"; 
			case 14: return "O"; 
			case 15: return "P"; 
			case 16: return "Q"; 
			case 17: return "R"; 
			case 18: return "S"; 
			case 19: return "T";
			case 20: return "U"; 
			case 21: return "V"; 
			case 22: return "W"; 
			case 23: return "X"; 
			case 24: return "Y"; 
			case 25: return "Z"; 
		}

		return "";
	}
	
	public static boolean verificaCodiciFiscaliOmocodia(String codFiscaleCalcolato, String codiceFiscaleInserito)
	{
		int[] aPos = new int[]{15,14,13,11,10,8,7};
		
		int iIndex = 0;
		
		//elimino l'ultimo carattere
		while (codFiscaleCalcolato != null && iIndex < 7)
		{
			codFiscaleCalcolato = codFiscaleCalcolato.substring(0,15);
			codFiscaleCalcolato = calcolaCodFiscaleOmocode(codFiscaleCalcolato, aPos[iIndex]);
			if (codFiscaleCalcolato != null)
			{
				if (codFiscaleCalcolato.equals(codiceFiscaleInserito))
					return true;
			}
			iIndex++;
		}
		
		return false;
	}
	
	public static boolean verifica2CodiciFiscaliOmocodia(String codFiscaleCalcolato, String codiceFiscaleInserito)
	{
		int[] aPos = new int[]{15,14,13,11,10,8,7};
		
		int iIndex = 0;
		
		//elimino l'ultimo carattere
		String appo = codiceFiscaleInserito.substring(0, 15);
		
		while (iIndex < 7)
		{
			String carattereSostituto = "";
			char c = appo.charAt(aPos[iIndex] - 1);
			try {
				//inizio LP PG21XX04 Leak
				//int i = Integer.valueOf(String.valueOf(c));
				Integer.valueOf(String.valueOf(c));
				//fine LP PG21XX04 Leak
			} catch (NumberFormatException ex) {
				//Se non è numerico significa trattasi di omocodia
				switch (c) {
					case 'L': carattereSostituto = "0"; break;
					case 'M': carattereSostituto = "1"; break;
					case 'N': carattereSostituto = "2"; break;
					case 'P': carattereSostituto = "3"; break;
					case 'Q': carattereSostituto = "4"; break;
					case 'R': carattereSostituto = "5"; break;
					case 'S': carattereSostituto = "6"; break;
					case 'T': carattereSostituto = "7"; break;
					case 'U': carattereSostituto = "8"; break;
					case 'V': carattereSostituto = "9"; break;
				}
			}
			if (carattereSostituto.length() > 0) {
				appo = appo.substring(0, aPos[iIndex] - 1) + 
						carattereSostituto + 
						appo.substring(aPos[iIndex], appo.length());
			}
			iIndex++;
		}
		appo += getCodiceControlloCodiceFiscale(appo);
		if (appo.equalsIgnoreCase(codFiscaleCalcolato))
			return true;
		else 
			return false;
	}
	
	private static String calcolaCodFiscaleOmocode(String codFiscalePrecedente, int posizioneSostituzione)
	{
		String carattereSostituto = "";
		char c = codFiscalePrecedente.charAt(posizioneSostituzione - 1);
		switch (c) {
			case (int)'0': carattereSostituto = "L"; break;
			case (int)'1': carattereSostituto = "M"; break;
			case (int)'2': carattereSostituto = "N"; break;
			case (int)'3': carattereSostituto = "P"; break;
			case (int)'4': carattereSostituto = "Q"; break;
			case (int)'5': carattereSostituto = "R"; break;
			case (int)'6': carattereSostituto = "S"; break;
			case (int)'7': carattereSostituto = "T"; break;
			case (int)'8': carattereSostituto = "U"; break;
			case (int)'9': carattereSostituto = "V"; break;
		}
		if (carattereSostituto.length() == 0)
			return null;
		else
		{
			String codFiscaleOmocode = codFiscalePrecedente.substring(0, posizioneSostituzione - 1) + 
										carattereSostituto + 
										codFiscalePrecedente.substring(posizioneSostituzione, codFiscalePrecedente.length());
			
			codFiscaleOmocode += getCodiceControlloCodiceFiscale(codFiscaleOmocode);
			return codFiscaleOmocode;
		}
	}
}


class Lettere
{
	List<Character> consonanti;
	List<Character> vocali;
	
	//data una parola divide le consonanti e le vocali
	void elaboraParola(String parola)
	{
		parola = parola.toUpperCase();
		consonanti = new ArrayList<Character>();
		vocali = new ArrayList<Character>();
		
		List<Character> listVocali = new ArrayList<Character>(Arrays.asList('A','E','I','O','U'));
		List<Character> listConsonanti = new ArrayList<Character>(Arrays.asList('B','C','D','F','G','H','J','K','L','M','N','P','Q','R','S','T','V','W','X','Y','Z'));
		for (int i=0; i<parola.length(); i++)
		{
			Character c =parola.charAt(i);
			if (listVocali.contains(c))
				vocali.add(c);
			else if (listConsonanti.contains(c))
				consonanti.add(c);
		}
	}
}
