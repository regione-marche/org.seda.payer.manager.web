package org.seda.payer.manager.adminusers.actions;

import java.rmi.RemoteException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.actions.BaseAdminusersAction;
import org.seda.payer.manager.adminusers.util.Error;
import org.seda.payer.manager.adminusers.util.Profilo;
import org.seda.payer.manager.adminusers.util.ProfiloUtil;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.GenericsDateNumbers;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.ws.WSCache;

import com.seda.j2ee5.maf.components.defender.bfl.BFLContext;
import com.seda.j2ee5.maf.components.defender.bfl.BFLUser;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.security.webservice.dati.AggiornaUtentePIVARequestType;
import com.seda.security.webservice.dati.AggiornaUtentePIVAResponseType;
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
import com.seda.security.webservice.dati.SendMailCodiciRequestType;
import com.seda.security.webservice.dati.SendMailCodiciResponseType;
import com.seda.security.webservice.dati.TipoEmail;
import com.seda.security.webservice.dati.UtentePIVA;
import com.seda.security.webservice.dati.UtentePIVAFlagOperatoreBackOffice;
import com.seda.security.webservice.dati.UtentePIVATipologiaUtente;
import com.seda.security.webservice.dati.UtentePIVAUtenzaAttiva;
import com.seda.security.webservice.srv.FaultType;

@SuppressWarnings("serial")
public class SeUsersEnableAction extends BaseAdminusersAction{
	String username = null;
	
	private Profilo profilo = new Profilo();
	

	public Object service(HttpServletRequest request) throws ActionException {
		BFLContext bflContext = null;
		/*
		 * Individuo la richiesta dell'utente
		 */
		FiredButton firedButton = getFiredButton(request);
        /*
         * Inizializzazione
         */
		String retCode = null;
		String retMessage = null;
		String esito = "OK";
		HttpSession session = request.getSession();
		//prendo il profilo dalla session
		
		
		switch(firedButton)
		{
		case TX_BUTTON_AVANTI:
			profilo=(Profilo) session.getAttribute("profilo");
			String utenzaAttiva = (String)request.getAttribute("utenzaAttiva");
			
			try{		
				profilo.setUtenzaAttiva(utenzaAttiva.equals("Y")?true:false);	
				aggiornaProfiloPesronaFisica(profilo,request);	
				setFormMessage("enable_form", "Aggiornamento eseguito con successo", request);
			}catch(Exception e) {
				setFormMessage("enable_form", Messages.COMUNICAZIONE_KO.format(e.getLocalizedMessage()), request);
				e.printStackTrace();
				request.setAttribute("utenzaAttiva", utenzaAttiva);
			}
		    session.setAttribute("enable_step", "step2");
			break;

		case TX_BUTTON_NULL:
			
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
							profilo.setNote(utente.getNote());
							profilo.setOperatoreBackOffice(utente.getFlagOperatoreBackOffice().equals(UtentePIVAFlagOperatoreBackOffice.Y));
							session.setAttribute("SeUsersEnableAction_utenzaAttivaInitial", utente.getUtenzaAttiva().getValue());
							String listProvinceXml = ProfiloUtil.getListProvince(request);
							setDatiAnag(utente, profilo, listProvinceXml, request);
							setDatiPIVA(utente, profilo, listProvinceXml, request);
							/*
							 * Profilo in request per 
							 */
						    session.setAttribute("profilo", profilo);
						
							   
							
						}
						else
							setFormMessage("enable_form", retCode + " - " + retMessage, request);
					}
					else
					{
						setFormMessage("enable_form", Messages.GENERIC_ERROR.format(), request);
					}
				}
				else
				{
					setFormMessage("enable_form",Messages.UTENZA_NON_VALIDA.format(), request);
				} 
			}catch (FaultType e) {
					setFormMessage("enable_form", Messages.COMUNICAZIONE_KO.format(e.getLocalizedMessage()), request);
					e.printStackTrace();
				} catch (RemoteException e) {
					setFormMessage("enable_form", Messages.COMUNICAZIONE_KO.format(e.getMessage()), request);
					e.printStackTrace();
				}
			session.setAttribute("enable_step", "step1");
			break;
			
			/*
			 * Torno al form di ricerca	
			 */
		case TX_BUTTON_INDIETRO:
				request.setAttribute("vista","seusers_search");
				session.setAttribute("enable_step", "step2");	
				break;
		
		case TX_BUTTON_CERCA:
			request.setAttribute("vista","seusers_search");
			break;
		}
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
	
	
	private void aggiornaProfiloPesronaFisica(Profilo profilo, HttpServletRequest request){
		String retCode = null;
		String retMessage = null;
		String esito = "OK";
	//	String codop = "edit";
		HttpSession session = request.getSession();
		esito = "OK";
			// AGGIORNO L'UTENTE
			AggiornaUtentePIVARequestType req = new AggiornaUtentePIVARequestType();
			UtentePIVA utente = new UtentePIVA();
			getDatiRegistrazione(utente, profilo, null, session);
			req.setUtentePIVA(utente);
			//String utenzaAttivaWEB =  req.getUtentePIVA().getUtenzaAttiva().getValue();
			//String utenzaAttivaDB = (String) session.getAttribute("utenzaAttivaDB"); 
			try {
				AggiornaUtentePIVAResponseType out = WSCache.securityServer.aggiornaUtentePIVA(req, request);
				ResponseType response = out.getResponse();
				if (response != null)
				{
					/*
					 * Setto l'attributo "SeUsersEnableAction_aggiornaProfilo" perchè il flow sappia
					 * dove redirigere la richiesta: sulla pagina di ricerca
					 * se l'inserimento è terminato correttamente
					 * oppure ancora sulla pagina di inserimento
					 * 
					 * Se l'esito è positivo metto anche in sessione
					 * il messaggio da visualizzare all'utente sulla
					 * maschera di ricerca.
					 * 
					 */
					retCode = response.getRetCode();
					retMessage = response.getRetMessage();
					request.setAttribute("SeUsersEnableAction_aggiornaProfiloEsito", retCode);
					String userName = profilo.getUsername();				
					SendMailCodiciRequestType in=new SendMailCodiciRequestType();
					SendMailCodiciResponseType outsend = null;
					in.setUserName(userName);
					in.setUrlSito(request.getServerName());
				    /*
				     * Aggiornamento profilo in session
				     */
					session.setAttribute(ManagerKeys.PROFILO_UTENTEPIVA,profilo);
					request.setAttribute("SeUsersEnableAction_aggiornaProfiloEsito", esito);
					
					/*
					if (retCode.equals("00")) {
						session.setAttribute("addUserRetMessage", retMessage);
						if (utenzaAttivaDB != utenzaAttivaWEB) 
							if (utenzaAttivaDB == "Y") {
								in.setTipoEmail(TipoEmail.REV);
								outsend =  WSCache.securityServer.sendMailCodici(in, request);
							}
							else {
								in.setTipoEmail(TipoEmail.ATT);
								outsend =  WSCache.securityServer.sendMailCodici(in, request);
							}
						if (outsend!=null) {
							ResponseType responseType = outsend.getResponse();
						
							if (responseType.getRetCode().equals("00"))
								setFormMessage("form_personafisica", "Errore nell''invio email" , request);
						}
					}
					else 
						setFormMessage("form_personafisica", retCode + " - " + retMessage, request);				
                    */
					
				}
				else
					setFormMessage("enable_form", Messages.GENERIC_ERROR.format(),request);
							
			} catch (FaultType e) {
				setFormMessage("enable_form", Messages.GENERIC_ERROR.format(),request);
				e.printStackTrace();
			} catch (RemoteException e) {
				setFormMessage("enable_form", Messages.GENERIC_ERROR.format(),request);
				e.printStackTrace();
			}
		
	}
	
	
	private void getDatiRegistrazione(UtentePIVA utente, Profilo profilo, String flagAbilitazioneAutomatica, HttpSession session)
	{
		userBean = (UserBean) session.getAttribute(SignOnKeys.USER_BEAN);
		
		Calendar now = Calendar.getInstance();
		
		utente.setUsername(profilo.getUsername()); //in modifica è la chiave
		utente.setTipologiaUtente(UtentePIVATipologiaUtente.fromString(profilo.getTipologiaUtente()));
		
		
		//dati da non modificare
		utente.setUtenzaAttiva(UtentePIVAUtenzaAttiva.fromString(profilo.isUtenzaAttiva()? "Y": "N")); 
		utente.setDataInizioValiditaUtenza(null);
		utente.setDataFineValiditaUtenza(profilo.getDataFineValiditaUtenza());
		utente.setPrimoAccesso(null);
		utente.setNote(profilo.getNote());
	
		
		utente.setDataScadenzaPassword(null); 
		utente.setDataUltimoAccesso(null); 
		utente.setDataInserimentoUtenza(null); 
		
		utente.setDataUltimoAggiornamento(now);
	    utente.setOperatoreUltimoAggiornamento(userBean.getUserName());
		
	    //dati impresa
	    if (utente.getTipologiaUtente().equals(UtentePIVATipologiaUtente.G))
	    	utente.setDatiUtentePIVA(getDatiPIVA(profilo));
	    else 
	    {
	    	//potrei aver compilato i dati della persona giuridica; se alla fine ho scelto persona fisica
	    	//al salvataggio devo ripulire le informazioni della persona giuridica
	    	utente.setDatiUtentePIVA(getDatiPIVAEmpty());
	    }

	    //dati persona fisica / delegato
	    utente.setDatiAnagPersonaFisicaDelegato(getDatiAnag(profilo));
	
	}

	
	private DatiAnagPersonaFisicaDelegato getDatiAnag(Profilo profilo)
	{
		DatiAnagPersonaFisicaDelegato anag = new DatiAnagPersonaFisicaDelegato();
	    //Dati generali
	    DatiGeneraliPersonaFisicaDelegato datiGenerali = new DatiGeneraliPersonaFisicaDelegato();
	    
	    datiGenerali.setCognome(profilo.getCognome());
	    datiGenerali.setNome(profilo.getNome());
	    datiGenerali.setSesso(profilo.getSesso().equals("F") ? DatiGeneraliPersonaFisicaDelegatoSesso.F : DatiGeneraliPersonaFisicaDelegatoSesso.M);
	    datiGenerali.setCodiceFiscale(profilo.getCodiceFiscale());
	    
	    anag.setDatiGeneraliPersonaFisicaDelegato(datiGenerali);

	    //Dati di Nascita
	    DatidiNascitaPersonaFisicaDelegato datiNascita = new DatidiNascitaPersonaFisicaDelegato();
	    
	    datiNascita.setDataDiNascita(profilo.getDataNascita().getTime());
	    datiNascita.setProvinciaDiNascita(profilo.getProvinciaNascitaSigla());
	    datiNascita.setCodbelfDiNascita(profilo.getComuneNascitaBelfiore());
	    datiNascita.setFlagEsteroNascita(profilo.getComuneNascitaEstero().equals("Y") ? DatidiNascitaPersonaFisicaDelegatoFlagEsteroNascita.Y : DatidiNascitaPersonaFisicaDelegatoFlagEsteroNascita.N);
	    
	    
	    anag.setDatidiNascitaPersonaFisicaDelegato(datiNascita);

	    //Dati Documento
	    DatiDocumentoPersonaFisicaDelegato datiDocumento = new DatiDocumentoPersonaFisicaDelegato();
	    
	    if (profilo.getTipoDocumento().equals("CI"))
	    	datiDocumento.setTipoDocumentoRiconoscimento(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.CI);
	    else if (profilo.getTipoDocumento().equals("PA"))
	    	datiDocumento.setTipoDocumentoRiconoscimento(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.PA);
	    else if (profilo.getTipoDocumento().equals("PP"))
	    	datiDocumento.setTipoDocumentoRiconoscimento(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.PP);
	    else 
	    	datiDocumento.setTipoDocumentoRiconoscimento(DatiDocumentoPersonaFisicaDelegatoTipoDocumentoRiconoscimento.NP);

	    datiDocumento.setNumeroDocumentoRiconoscimento(profilo.getNumeroDocumento());
	    datiDocumento.setEnteRilascioDocumento(profilo.getEnteRilascioDocumento());
	    datiDocumento.setDataRilascioDocumento(profilo.getDataRilascioDocumento().getTime());
	    
	    anag.setDatiDocumentoPersonaFisicaDelegato(datiDocumento);

	    //Dati Residenza
	    DatiResidenzaPersonaFisicaDelegato datiResidenza = new DatiResidenzaPersonaFisicaDelegato();
	    
	    datiResidenza.setIndirizzoResidenza(profilo.getIndirizzoResidenza());
	    datiResidenza.setProvinciaResidenza(profilo.getProvinciaResidenzaSigla());
	    datiResidenza.setCodbelfResidenza(profilo.getComuneResidenzaBelfiore());
	    datiResidenza.setCapComuneResidenza(profilo.getCapResidenza());
	    datiResidenza.setEsteroResidenza(profilo.getComuneResidenzaEstero().equals("Y") ? DatiResidenzaPersonaFisicaDelegatoEsteroResidenza.Y : DatiResidenzaPersonaFisicaDelegatoEsteroResidenza.N);
	    
	    anag.setDatiResidenzaPersonaFisicaDelegato(datiResidenza);

	    //Dati Domicilio
	    DatiDomicilioPersonaFisicaDelegato datiDomicilio = new DatiDomicilioPersonaFisicaDelegato();
	    
	    datiDomicilio.setIndirizzoDomicilio(profilo.getIndirizzoDomicilio());
	    datiDomicilio.setProvinciaDomicilio(profilo.getProvinciaDomicilioSigla());
	    datiDomicilio.setCodbelfDomicilio(profilo.getComuneDomicilioBelfiore());
	    datiDomicilio.setCapComuneDomicilio(profilo.getCapDomicilio());
	    datiDomicilio.setEsteroDomicilio(profilo.getComuneDomicilioEstero().equals("Y") ? DatiDomicilioPersonaFisicaDelegatoEsteroDomicilio.Y : DatiDomicilioPersonaFisicaDelegatoEsteroDomicilio.N);
	    
	    anag.setDatiDomicilioPersonaFisicaDelegato(datiDomicilio);

	    //Dati Contatti
	    DatiContattiPersonaFisicaDelegato datiContatti = new DatiContattiPersonaFisicaDelegato();
	    
	    datiContatti.setMail(profilo.getEmail());
	    datiContatti.setPec(profilo.getEmailPEC());
	    datiContatti.setTelefono(profilo.getTelefono());
	    datiContatti.setCellulare(profilo.getCellulare());
	    datiContatti.setFax(profilo.getFax());
	    
	    anag.setDatiContattiPersonaFisicaDelegato(datiContatti);
	    
	    return anag;
	}
	
	private DatiUtentePIVA getDatiPIVA(Profilo profilo)
	{
		DatiUtentePIVA datiPiva = new DatiUtentePIVA();
		
		//Dati Generali
		DatiGeneraliPIVA datiGenerali = new DatiGeneraliPIVA();
		
		datiGenerali.setPartitaIVA(profilo.getPartitaIVA());
		datiGenerali.setRagioneSociale(profilo.getRagioneSociale());
		datiGenerali.setClassificazioneMerceologica(!profilo.getClassificazioneMerceologicaDettaglio().equals("") ? profilo.getClassificazioneMerceologicaDettaglio() : "xx.xx.xx");
		datiGenerali.setNumeroAutorizzazione(profilo.getNumeroAutorizzazione());
	    
		datiPiva.setDatiGeneraliPIVA(datiGenerali);

		//Sede Legale
		SedeLegale sedeLegale = new SedeLegale();
	    
		sedeLegale.setIndirizzoSedeLegale(profilo.getIndirizzoSedeLegale());
		sedeLegale.setCodbelfSedeLegale(profilo.getComuneSedeLegaleBelfiore());
		sedeLegale.setProvinciaSedeLegale(profilo.getProvinciaSedeLegaleSigla());
		sedeLegale.setCapComuneSedeLegale(profilo.getCapSedeLegale());
		sedeLegale.setEsteroSedeLegale(profilo.getComuneSedeLegaleEstero().equals("Y") ? SedeLegaleEsteroSedeLegale.Y : SedeLegaleEsteroSedeLegale.N);
	    
	    datiPiva.setSedeLegale(sedeLegale);

	    //Sede Operativa
	    SedeOperativa sedeOperativa = new SedeOperativa();
	    
	    sedeOperativa.setIndirizzoSedeOperativa(profilo.getIndirizzoSedeOperativa());
		sedeOperativa.setCodbelfSedeOperativa(profilo.getComuneSedeOperativaBelfiore());
		sedeOperativa.setProvinciaSedeOperativa(profilo.getProvinciaSedeOperativaSigla());
		sedeOperativa.setCapComuneSedeOperativa(profilo.getCapSedeOperativa());
		
	    datiPiva.setSedeOperativa(sedeOperativa);

	    //Contatti Impresa
	    ContattiPIVA contatti = new ContattiPIVA();
	    
	    contatti.setMailUtentePIVA(profilo.getEmailImpresa());
	    contatti.setPecUtentePIVA(profilo.getEmailPECImpresa());
	    contatti.setTelefonoUtentePIVA(profilo.getTelefonoImpresa());
	    contatti.setCellulareUtentePIVA(profilo.getCellulareImpresa());
	    contatti.setFaxUtentePIVA(profilo.getFaxImpresa());
	    
	    datiPiva.setContattiPIVA(contatti);
	    
		return datiPiva;
	}
	
	private DatiUtentePIVA getDatiPIVAEmpty()
	{
		DatiUtentePIVA datiPiva = new DatiUtentePIVA();
		
		//Dati Generali
		DatiGeneraliPIVA datiGenerali = new DatiGeneraliPIVA();
		
		datiGenerali.setPartitaIVA("");
		datiGenerali.setRagioneSociale("");
		datiGenerali.setClassificazioneMerceologica("xx.xx.xx");
		datiGenerali.setNumeroAutorizzazione("");
	    
		datiPiva.setDatiGeneraliPIVA(datiGenerali);

		//Sede Legale
		SedeLegale sedeLegale = new SedeLegale();
	    
		sedeLegale.setIndirizzoSedeLegale("");
		sedeLegale.setCodbelfSedeLegale("");
		sedeLegale.setProvinciaSedeLegale("");
		sedeLegale.setCapComuneSedeLegale("");
		sedeLegale.setEsteroSedeLegale(SedeLegaleEsteroSedeLegale.N);
	    
	    datiPiva.setSedeLegale(sedeLegale);

	    //Sede Operativa
	    SedeOperativa sedeOperativa = new SedeOperativa();
	    
	    sedeOperativa.setIndirizzoSedeOperativa("");
		sedeOperativa.setCodbelfSedeOperativa("");
		sedeOperativa.setProvinciaSedeOperativa("");
		sedeOperativa.setCapComuneSedeOperativa("");
		
	    datiPiva.setSedeOperativa(sedeOperativa);

	    //Contatti Impresa
	    ContattiPIVA contatti = new ContattiPIVA();
	    
	    contatti.setMailUtentePIVA("");
	    contatti.setPecUtentePIVA("");
	    contatti.setTelefonoUtentePIVA("");
	    contatti.setCellulareUtentePIVA("");
	    contatti.setFaxUtentePIVA("");
	    
	    datiPiva.setContattiPIVA(contatti);
	    
		return datiPiva;
	}
	
}
