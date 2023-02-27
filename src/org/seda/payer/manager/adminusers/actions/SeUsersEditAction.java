package org.seda.payer.manager.adminusers.actions;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.adminusers.util.Profilo;
import org.seda.payer.manager.adminusers.util.ProfiloUtil;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PayerCommand;
import org.seda.payer.manager.util.PayerCommandException;
import org.seda.payer.manager.ws.WSCache;

import com.seda.security.webservice.dati.ContattiPIVA;
import com.seda.security.webservice.dati.DatiAnagPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiContattiPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiDocumentoPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento;
import com.seda.security.webservice.dati.DatiDomicilioPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiDomicilioPersonaFisicaDelegatoEsteroDomicilio;
import com.seda.security.webservice.dati.DatiGeneraliPIVA;
import com.seda.security.webservice.dati.DatiGeneraliPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiGeneraliPersonaFisicaDelegatoSesso;
import com.seda.security.webservice.dati.DatiResidenzaPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatiResidenzaPersonaFisicaDelegatoEsteroResidenza;
import com.seda.security.webservice.dati.DatiUtentePIVA;
import com.seda.security.webservice.dati.DatidiNascitaPersonaFisicaDelegato;
import com.seda.security.webservice.dati.DatidiNascitaPersonaFisicaDelegatoFlagEsteroNascita;
import com.seda.security.webservice.dati.ResponseType;
import com.seda.security.webservice.dati.SedeLegale;
import com.seda.security.webservice.dati.SedeLegaleEsteroSedeLegale;
import com.seda.security.webservice.dati.SedeOperativa;
import com.seda.security.webservice.dati.SelezionaUtentePIVARequestType;
import com.seda.security.webservice.dati.SelezionaUtentePIVAResponseType;
import com.seda.security.webservice.dati.UtentePIVA;
import com.seda.security.webservice.dati.UtentePIVAFlagOperatoreBackOffice;
import com.seda.security.webservice.dati.UtentePIVATipologiaUtente;
import com.seda.security.webservice.dati.UtentePIVAUtenzaAttiva;
import com.seda.security.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class SeUsersEditAction extends BaseAdminusersAction implements PayerCommand{

	private static String codop = "edit";
	private static String step = "seuseredit";
	private String stepOld;
	
	private Profilo profilo = new Profilo();

	public Object execute(HttpServletRequest request) throws PayerCommandException {
		HttpSession session = request.getSession();
		String retCode = null;
		String retMessage = null;
		String esito = "OK";
		Calendar cal = Calendar.getInstance();
		request.setAttribute("yearNow", cal.get(Calendar.YEAR));
		//inizio LP PG200060
		String template = getTemplateCurrentApplication(request, session); 
		//fine LP PG200060
		
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
		/*
		 * Azzero il campo hidden "fired_button_hidden" per evitare
		 * che possa essere riproposto nel caso in cui interviene 
		 * il "validator"
		 */
		request.setAttribute("fired_button_hidden", "");
		
		//prendo il profilo dalla session
		if (session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA) != null)
			profilo = (Profilo) session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA);
		
		//PREJAVA18_LUCAP_16092020
		if(session.getAttribute("profiloStatico")==null) {
			session.setAttribute("profiloStatico", profilo);
		}
		//FINE PREJAVA18_LUCAP_16092020
		
		if (!firedButton.equals(FiredButton.TX_BUTTON_RESET) && 
				!firedButton.equals(FiredButton.TX_BUTTON_NULL) &&
				!firedButton.equals(FiredButton.TX_BUTTON_STEP1) &&
				!firedButton.equals(FiredButton.TX_BUTTON_NUOVO)) 
		{
			/*
			 *  Ricavo i parametri della request
			 */
			profilo = getSeUsersFormParametersUtentePIVA(request, codop, profilo);

			/*
			 * Controllo che siano stati riempiti
			 * tutti i campi obbligatori
			 */
			esito = checkSeUsersInputFieldsUtentePIVA(profilo,codop, request);
			request.setAttribute("inputFieldChecked", esito);
		}
		/*
		 * Il campo userName non è modificabile
		 */
		request.setAttribute("usernameModificabile", false);
		request.setAttribute("tipologiaDisabled", true);
		
		/*
		 * Compio le azioni corrispondenti alla richiesta dell'utente
		 */
		switch(firedButton)
		{
			/* PROVENGO DALLO STEP1 DELLA PERSONA FISICA O PERSONA GIURIDICA*/
			case TX_BUTTON_STEP1:
				if (session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA) != null)
					profilo = (Profilo) session.getAttribute(ManagerKeys.PROFILO_UTENTEPIVA);
				
				
				break;
			
			/*
			 * Vado all'inserimento della Persona Giuridica o all'inserimento della Persona Fisica 
			 * a seconda della tipologia utente da inserire.	
			 */
			case TX_BUTTON_AVANTI:
				if (esito.equals("OK")) 
					stepOld="seuseredit";
				else	
					setFormMessage("var_form", esito, request);
				
				break;
							
			case TX_BUTTON_SCADENZA_CHANGED:
				
				break;

			case TX_BUTTON_RESET:
			case TX_BUTTON_NULL: 
				
				profilo=new Profilo();
				
				/*
				 * Valorizzo i campi della form 
				 * con i valori presenti in archivio
				 */
				try {
					String username = (String)request.getAttribute("tx_username_hidden");
					
					if (username != null && !username.equals(""))
					{
						SelezionaUtentePIVARequestType in = new SelezionaUtentePIVARequestType();
						in.setUserName(username);
						SelezionaUtentePIVAResponseType out = WSCache.securityServer.selezionaUtentePIVA(in, request);
						ResponseType response = null;
						
						if (out != null && out.getResponse() != null)
						{
							response = out.getResponse();
							retCode = response.getRetCode();
							retMessage = response.getRetMessage();
							if (retCode.equals("00"))
							{
								//setSeUsersFormFromWs(session, request, out, profilo);
								
								UtentePIVA utente = out.getSelezionaUtentePIVAResponse();
								
								profilo.setUsername(username);
								profilo.setPersonaFisica(utente.getTipologiaUtente().equals(UtentePIVATipologiaUtente.F)  ? "Y" : "N");
								profilo.setPersonaGiuridica(ProfiloUtil.invertiFlag(profilo.getPersonaFisica()));
								profilo.setTipologiaUtente(utente.getTipologiaUtente().getValue());
								profilo.setUtenzaAttiva(utente.getUtenzaAttiva().equals(UtentePIVAUtenzaAttiva.Y));
								profilo.setNessunaScadenza(isFine2099(GenericsDateNumbers.setLocaleTimeZone(utente.getDataFineValiditaUtenza())));
								//inizio LP PG200060
								if(!template.equalsIgnoreCase("regmarche")) {
								//fine LP PG200060
								if(!isFine2099(GenericsDateNumbers.setLocaleTimeZone(utente.getDataFineValiditaUtenza())))
									profilo.setDataFineValiditaUtenza(utente.getDataFineValiditaUtenza());
								//inizio LP PG200060
								}
								//fine LP PG200060
								profilo.setNote(utente.getNote());
								//inizio LP PG200060
								if(!template.equalsIgnoreCase("regmarche")) {
								//fine LP PG200060
								profilo.setOperatoreBackOffice(utente.getFlagOperatoreBackOffice().equals(UtentePIVAFlagOperatoreBackOffice.Y));
								profilo.setFlagNoControlli(false);	//PG180170 GG 17102018	//Il check box è sempre proposto come NON SELEZIONATO
								//inizio LP PG200060
								}
								//fine LP PG200060
								
								session.setAttribute("utenzaAttivaDB", utente.getUtenzaAttiva().getValue());
								
								String listProvinceXml = ProfiloUtil.getListProvince(request);
								setDatiAnag(utente, profilo, listProvinceXml, request);
								setDatiPIVA(utente, profilo, listProvinceXml, request);
								
							}
							else
								setFormMessage("var_form", retCode + " - " + retMessage, request);
						}
						else
						{
							setFormMessage("var_form", Messages.GENERIC_ERROR.format(), request);
						}
					}
					else
					{
						setFormMessage("var_form",Messages.UTENZA_NON_VALIDA.format(), request);
					}
				} catch (FaultType e) {
					setFormMessage("var_form", Messages.COMUNICAZIONE_KO.format(e.getLocalizedMessage()), request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("var_form", Messages.COMUNICAZIONE_KO.format(e.getMessage()), request);
					e.printStackTrace();
				}
				
				break;
			/*
			 * Torno al form di ricerca	
			 */
			case TX_BUTTON_CERCA:
				request.setAttribute("vista","seusers_search");
				break;
		}
		
			
		AddProfiloInSession(session, profilo);
		MoveProfiloInRequest(request, profilo);
		
		
		/*
		 * Setto l'action della form di "seUsers_var.jsp",
		 * e il tipo di operazione 
		 */
				
		
		/*
		 * Setto l'action della form di "adminusers_var.jsp"
		 * ed il tipo di operazione
		 */
		request.setAttribute("do_command_name","seUsersEdit.do");
		request.setAttribute("codop",codop);
		session.setAttribute("Step", step);
		session.setAttribute("Stepold", stepOld == null ? session.getAttribute("Stepold") : stepOld);
		request.setAttribute("Step", session.getAttribute("Step"));
		request.setAttribute("Stepold", session.getAttribute("Stepold"));
		
		return null;
	}

	
	private void setDatiAnag(UtentePIVA utente, Profilo profilo, String listProvinceXml, HttpServletRequest request)
	{
		DatiAnagPersonaFisicaDelegato anag = utente.getDatiAnagPersonaFisicaDelegato();
	    //Dati generali
	    DatiGeneraliPersonaFisicaDelegato datiGenerali = anag.getDatiGeneraliPersonaFisicaDelegato();
	    
	    profilo.setCognome(datiGenerali.getCognome().trim());
	    profilo.setNome(datiGenerali.getNome().trim());
	    profilo.setSesso(datiGenerali.getSesso().equals(DatiGeneraliPersonaFisicaDelegatoSesso.F) ? "F" : "M");
	    profilo.setCodiceFiscale(datiGenerali.getCodiceFiscale().trim());
	    

	    //Dati di Nascita
	    DatidiNascitaPersonaFisicaDelegato datiNascita = anag.getDatidiNascitaPersonaFisicaDelegato();
	    
	    profilo.setDataNascita(GenericsDateNumbers.getCalendarFromDate(datiNascita.getDataDiNascita()));
	    profilo.setProvinciaNascitaSigla(datiNascita.getProvinciaDiNascita().trim());
	    if (!profilo.getProvinciaNascitaSigla().equals("EE"))
	    	profilo.setProvinciaNascita(ProfiloUtil.getDescrizioneProvincia(listProvinceXml, profilo.getProvinciaNascitaSigla()));
	    profilo.setComuneNascitaBelfiore(datiNascita.getCodbelfDiNascita().trim());
	    if (profilo.getComuneNascitaBelfiore().length() > 0)
	    {
	    	profilo.setComuneNascitaDescrizione(ProfiloUtil.getDescrizioneComune(profilo.getComuneNascitaBelfiore(), request));
	    	profilo.setComuneNascitaSelected(profilo.getComuneNascitaBelfiore() + "|" + profilo.getComuneNascitaDescrizione());
	    }
	    profilo.setComuneNascitaEstero(datiNascita.getFlagEsteroNascita().equals(DatidiNascitaPersonaFisicaDelegatoFlagEsteroNascita.Y) ? "Y" : "N");
	    

	    //Dati Documento
	    DatiDocumentoPersonaFisicaDelegato datiDocumento = anag.getDatiDocumentoPersonaFisicaDelegato();
	    
	    if (datiDocumento.getTipoDocumentoRiconoscimento().equals(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.CI))
	    	profilo.setTipoDocumento("CI");
	    else if (datiDocumento.getTipoDocumentoRiconoscimento().equals(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.PA))
	    	profilo.setTipoDocumento("PA");
	    else if (datiDocumento.getTipoDocumentoRiconoscimento().equals(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.PP))
	    	profilo.setTipoDocumento("PP");
	    else
	    	profilo.setTipoDocumento("");
	    
	    profilo.setNumeroDocumento(datiDocumento.getNumeroDocumentoRiconoscimento().trim());
	    profilo.setEnteRilascioDocumento(datiDocumento.getEnteRilascioDocumento().trim());
	    profilo.setDataRilascioDocumento(GenericsDateNumbers.getCalendarFromDate(datiDocumento.getDataRilascioDocumento()));
	    

	    //Dati Residenza
	    DatiResidenzaPersonaFisicaDelegato datiResidenza = anag.getDatiResidenzaPersonaFisicaDelegato();
	    
	    profilo.setIndirizzoResidenza(datiResidenza.getIndirizzoResidenza().trim());
	    profilo.setProvinciaResidenzaSigla(datiResidenza.getProvinciaResidenza().trim());
	    if (!profilo.getProvinciaResidenzaSigla().equals("EE"))
	    	profilo.setProvinciaResidenza(ProfiloUtil.getDescrizioneProvincia(listProvinceXml, profilo.getProvinciaResidenzaSigla()));
	    profilo.setComuneResidenzaBelfiore(datiResidenza.getCodbelfResidenza().trim());
	    if (profilo.getComuneResidenzaBelfiore().length() > 0)
	    {
	    	profilo.setComuneResidenzaDescrizione(ProfiloUtil.getDescrizioneComune(profilo.getComuneResidenzaBelfiore(), request));
	    	profilo.setComuneResidenzaSelected(profilo.getComuneResidenzaBelfiore() + "|" + profilo.getComuneResidenzaDescrizione());
	    }
	    profilo.setCapResidenza(datiResidenza.getCapComuneResidenza().trim());
	    profilo.setComuneResidenzaEstero(datiResidenza.getEsteroResidenza().equals(DatiResidenzaPersonaFisicaDelegatoEsteroResidenza.Y) ? "Y" : "N");
	    

	    //Dati Domicilio
	    DatiDomicilioPersonaFisicaDelegato datiDomicilio = anag.getDatiDomicilioPersonaFisicaDelegato();
	    
	    profilo.setIndirizzoDomicilio(datiDomicilio.getIndirizzoDomicilio().trim());
	    profilo.setProvinciaDomicilioSigla(datiDomicilio.getProvinciaDomicilio().trim());
	    if (!profilo.getProvinciaDomicilioSigla().equals("EE"))
	    	profilo.setProvinciaDomicilio(ProfiloUtil.getDescrizioneProvincia(listProvinceXml, profilo.getProvinciaDomicilioSigla()));
	    profilo.setComuneDomicilioBelfiore(datiDomicilio.getCodbelfDomicilio().trim());
	    if (profilo.getComuneDomicilioBelfiore().length() > 0)
	    {
	    	profilo.setComuneDomicilioDescrizione(ProfiloUtil.getDescrizioneComune(profilo.getComuneDomicilioBelfiore(), request));
	    	profilo.setComuneDomicilioSelected(profilo.getComuneDomicilioBelfiore() + "|" + profilo.getComuneDomicilioDescrizione());
	    }
	    profilo.setCapDomicilio(datiDomicilio.getCapComuneDomicilio().trim());
	    profilo.setComuneDomicilioEstero(datiDomicilio.getEsteroDomicilio().equals(DatiDomicilioPersonaFisicaDelegatoEsteroDomicilio.Y) ? "Y" : "N");
	    

	    //Dati Contatti
	    DatiContattiPersonaFisicaDelegato datiContatti = anag.getDatiContattiPersonaFisicaDelegato();
	    
	    profilo.setEmail(datiContatti.getMail().trim());
	    profilo.setEmailPEC(datiContatti.getPec().trim());
	    profilo.setTelefono(datiContatti.getTelefono().trim());
	    profilo.setCellulare(datiContatti.getCellulare().trim());
	    profilo.setFax(datiContatti.getFax().trim());
	    
	}
	
	private void setDatiPIVA(UtentePIVA utente, Profilo profilo, String listProvinceXml, HttpServletRequest request)
	{
		DatiUtentePIVA datiPiva = utente.getDatiUtentePIVA();
		
		//Dati Generali
		DatiGeneraliPIVA datiGenerali = datiPiva.getDatiGeneraliPIVA();
		
		profilo.setPartitaIVA(datiGenerali.getPartitaIVA().trim());
		profilo.setRagioneSociale(datiGenerali.getRagioneSociale().trim());
		profilo.setClassificazioneMerceologicaDettaglio(datiGenerali.getClassificazioneMerceologica().equals("xx.xx.xx") ? "" : datiGenerali.getClassificazioneMerceologica());
		profilo.setClassificazioneMerceologica(ProfiloUtil.getCodFamigliaMerceologica(profilo.getClassificazioneMerceologicaDettaglio(), request));
		profilo.setNumeroAutorizzazione(datiGenerali.getNumeroAutorizzazione().trim());
	    

		//Sede Legale
		SedeLegale sedeLegale = datiPiva.getSedeLegale();
	    
		profilo.setIndirizzoSedeLegale(sedeLegale.getIndirizzoSedeLegale().trim());
		profilo.setProvinciaSedeLegaleSigla(sedeLegale.getProvinciaSedeLegale().trim());
		if (!profilo.getProvinciaSedeLegaleSigla().equals("EE"))
			profilo.setProvinciaSedeLegale(ProfiloUtil.getDescrizioneProvincia(listProvinceXml, profilo.getProvinciaSedeLegaleSigla()));
		profilo.setComuneSedeLegaleBelfiore(sedeLegale.getCodbelfSedeLegale().trim());
		if (profilo.getComuneSedeLegaleBelfiore().length() > 0)
		{
			profilo.setComuneSedeLegaleDescrizione(ProfiloUtil.getDescrizioneComune(profilo.getComuneSedeLegaleBelfiore(), request));
			profilo.setComuneSedeLegaleSelected(profilo.getComuneSedeLegaleBelfiore() + "|" + profilo.getComuneSedeLegaleDescrizione());
		}
		profilo.setCapSedeLegale(sedeLegale.getCapComuneSedeLegale().trim());
		profilo.setComuneSedeLegaleEstero(sedeLegale.getEsteroSedeLegale().equals(SedeLegaleEsteroSedeLegale.Y) ? "Y" : "N");
	    

	    //Sede Operativa
	    SedeOperativa sedeOperativa = datiPiva.getSedeOperativa();
	    
	    profilo.setIndirizzoSedeOperativa(sedeOperativa.getIndirizzoSedeOperativa().trim());
		profilo.setProvinciaSedeOperativaSigla(sedeOperativa.getProvinciaSedeOperativa().trim());
		if (!profilo.getProvinciaSedeOperativaSigla().equals("EE"))
			profilo.setProvinciaSedeOperativa(ProfiloUtil.getDescrizioneProvincia(listProvinceXml, profilo.getProvinciaSedeOperativaSigla()));
		profilo.setComuneSedeOperativaBelfiore(sedeOperativa.getCodbelfSedeOperativa().trim());
		if (profilo.getComuneSedeOperativaBelfiore().length() > 0)
		{
			profilo.setComuneSedeOperativaDescrizione(ProfiloUtil.getDescrizioneComune(profilo.getComuneSedeOperativaBelfiore(), request));
			profilo.setComuneSedeOperativaSelected(profilo.getComuneSedeOperativaBelfiore() + "|" + profilo.getComuneSedeOperativaDescrizione());
		}
		profilo.setCapSedeOperativa(sedeOperativa.getCapComuneSedeOperativa().trim());
		

	    //Contatti Impresa
	    ContattiPIVA contatti = datiPiva.getContattiPIVA();
	    
	    profilo.setEmailImpresa(contatti.getMailUtentePIVA().trim());
	    profilo.setEmailPECImpresa(contatti.getPecUtentePIVA().trim());
	    profilo.setTelefonoImpresa(contatti.getTelefonoUtentePIVA().trim());
	    profilo.setCellulareImpresa(contatti.getCellulareUtentePIVA().trim());
	    profilo.setFaxImpresa(contatti.getFaxUtentePIVA().trim());
	    
	}
	
}
