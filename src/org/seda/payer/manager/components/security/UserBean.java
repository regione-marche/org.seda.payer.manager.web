package org.seda.payer.manager.components.security;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import com.seda.commons.security.TokenGenerator;
import com.seda.j2ee5.maf.core.security.UserBeanSupport;
import com.seda.payer.pgec.webservice.adminusers.dati.UserBeanType;
import com.seda.security.webservice.dati.UtentePIVA;


@SuppressWarnings("serial")
/**
 * Questa classe contiene le informazioni relative all'utente collegato: alcuni campi sono presi da
 * SEC00DB0.SE000SV.SEUSRTB, altri da PAY00DB0.SE000SV.PYUSRTB e la lista delle applicazioni da
 * PAY00DB0.SE000SV.PYMNATB.
 */
public class UserBean extends UserBeanSupport implements Serializable {

	
	public static final String AUTENTICAZIONE_PROPRIETARIA="P";
	public static final String AUTENTICAZIONE_FEDERATA="F";
	
	/*************************************************
	 * TOKEN DI SESSIONE
	 *************************************************/
	private String userToken = null;
	/*************************************************
	 * LISTA DELLE APPLICAZIONI ABILITATE PER L'UTENTE
	 *************************************************/
	private List<String> applicazioni = null;
	/*************************************************
	 * CAMPI DALLA TABELLA SEC00DB0.SE000SV.SEUSRTB
	 *************************************************/
	/**
	 * USR_CUSRUSER VARCHAR(50)'USERNAME PER ACCESSO AL SISTEMA'!
	 */ 
	private String userName = null;
	/**
	 * 	USR_DUSRNOME VARCHAR(50)'NOME'!
	 */ 
	private String nome = null;
	/**
	 * 	USR_DUSRCOGN VARCHAR(256)'COGNOME/DENOMINAZIONE PER PERSONE GIURIDICHE/DITTE INDIVIDUALI'!
	 */ 
	private String cognome = null;
	/**
	 * USR_TUSRUSER CHAR(1)'TIPOLOGIA UTENTE (''F''= PERSONA FISICA, ''G''=PERSONA GIURIDICA, ''D''=DITTA INDIVIDUALE)'!
	 */
	private String tipologiaUtente = null;
	/**
	 * //USR_CUSRCFIS VARCHAR(16)'CODICE FISCALE/PARTITA IVA'!
	 */
	private String codiceFiscale = null;
	/**
	 * 	//USR_EUSRMAIL VARCHAR(50)'EMAIL UTENTE PER INVIO NOTIFICHE'!
	 */
	private String emailNotifiche = null;
	/**
	 * 	//USR_CUSRNSMS VARCHAR(15)'NUMERO TELEFONICO UTENTE PER INVIO NOTIFICHE'!
	 */
	private String smsNotifiche = null;
	/**
	 * 	COMMENT ON COLUMN SEUSRTB.USR_GUSRACCE IS 'DATA ULTIMO ACCESSO'!
	 */
	private Calendar dataUltimoAccesso = null;
	
	/*************************************************
	 * CAMPI DALLA TABELLA PAY00DB0.SE000SV.PYUSRTB
	 *************************************************/
	/**
	 *  //USR_KUSRKURS BIGINT 'CHIAVE UTENTE CALCOLATA'
	 */
	private Long chiaveUtente = null;
	/**
	 * 	//USR_CPRFPROF VARCHAR(10) 'PROFILO UTENTE'!
	 */
	private String userProfile = null;
	/**
	 * 	//USR_FUSRFATT CHAR(1) 'FLAG ATTIVAZIONE (Y/N)'!
	 */
	private boolean flagAttivazioneEnabled = false;
	/**
	 * 	//USR_CSOCCSOC CHAR(5) 'ATTRIBUTO CODICE SOCIETA'!
	 */
	private String codiceSocieta = null;
	/**
	 * 	//USR_CUTECUTE CHAR(5) 'ATTRIBUTO CODICE UTENTE'!
	 */
	private String codiceUtente = null;
	/**
	 * 	//USR_KANEKENT_CON CHAR(10) 'ATTRIBUTO CHIAVE ENTE/CONSORZIO'!
	 */
	private String chiaveEnteConsorzio = null;
	/**
	 * 	//USR_KANEKENT_ENT CHAR(10) 'ATTRIBUTO CHIAVE ENTE CONSORZIATO'!
	 */
	private String chiaveEnteConsorziato = null;
	/**
	 * 	//USR_FUSRRDWN CHAR(1) 'FLAG ABILITAZIONE DOWNLOAD HTTP FILE RENDICONTAZIONE (Y/N)'!
	 */
	private boolean downloadFlussiRendicontazioneEnabled = false;
	/**
	 * 	//USR_FUSRRFTP CHAR(1) 'FLAG ABILITAZIONE INVIO FTP FILE RENDICONTAZIONE (Y/N)'!
	 */
	private boolean invioFlussiRendicontazioneViaFtpEnabled = false;
	/**
	 * 	//USR_FUSRRMAI CHAR(1) 'FLAG ABILITAZIONE INVIO MAIL FILE RENDICONTAZIONE (Y/N)'!
	 */
	private boolean invioFlussiRendicontazioneViaEmailEnabled = false;
	/**
	 * 	//USR_FUSRRWS CHAR(1) 'FLAG ABILITAZIONE INVIO WS FILE RENDICONTAZIONE (Y/N)'!
	 */
	private boolean invioFlussiRendicontazioneViaWsEnabled = false;
	/**
	 * 	//USR_FUSRTROK CHAR(1) 'FLAG ABILITAZIONE AZIONI PER TRANSAZIONI COMPLETATE CON SUCCESSO'!
	 */
	private boolean azioniPerTransazioniOkEnabled = false;
	/**
	 * 	//USR_FUSRTRKO CHAR(1) 'FLAG ABILITAZIONE AZIONI PER TRANSAZIONI SOSPESE/FALLITE'!
	 */
	private boolean azioniPerTransazioniKoEnabled= false;
	/**
	 * 	//USR_FUSRRICO CHAR(1) 'FLAG ABILITAZIONE AZIONI (QUADRATURA MAN. E CHIUSURA), DOWNLOAD FLUSSO CBI E SEZIONE RICONCILIAZIONE MANUALE.'!
	 */
	private boolean azioniPerRiconciliazioneManualeEnabled = false;
	/**
	 * 	//USR_FUSRAECM CHAR(1) 'FLAG ATTIVAZIONE ESTRATTO CONTO MANAGER (Y/N)'!
	 */
	private boolean attivazioneEstrattoContoManagerEnabled = false;
	/**
	 * 	//USR_FUSRRIVE CHAR(1) 'FLAG ABILITAZIONE PROFILO RIVERSAMENTO ( Y = COMPLETO, N = RIVERSAMENTI ESEGUITI ED IN VISUALIZZAZIONE ).'!
	 */
	private boolean profiloRiversamentoEnabled = false;
	/**
	 * 	//USR_FUSRMUTE CHAR(1) 'FLAG ABILITAZIONE GESTIONE MULTI UTENTE PER PROFILI APPLICATIVI DI TIPO "AMEN" ( Y = MULTI UTENTE, N = SINGOLO UTENTE ).'!
	 */
	private boolean multiUtenteEnabled = false;

	/**
	//USR_FUSRAECM CHAR(1) 'FLAG ABILITAZIONE INVIO MAIL MOD 21 CONTO DI GESTIONE (Y/N)'!
	 */
	private boolean mailContoGestione = false;
	
	//EP160510_001 GG 03112016 - inizio
	/**
	 * 	//USR_CUSRCENP CHAR(5) 'CODICE ENTE DI PERTINENZA EC MANAGER'!
	 */
	private String entePertinenza = null;
	//EP160510_001 GG 03112016 - fine
	
	//RE180181_001 SB - inizio
	/**
	 * 	//USR_CUSRCGRU VARCHAR(3) 'CODICE GRUPPO AGENZIA'!
	 */
	private String gruppoAgenzia = null;
	
	//USR_CUSRMAIL VARCHAR(100) 'INDIRIZZO MAIL'
	private String mail = null;
	
	//USR_CUSRmpec VARCHAR(100) 'INDIRIZZO MAIL PEC'
	private String mailPec = null;
	
	//USR_CUSRMPIN VARCHAR(8) 'PINCODE MAIL'
	private String pinCodeMail = null;
	
	//USR_CUSRPPIN VARCHAR(8) 'PINCODE MAIL PEC'
	private String pinCodePec = null;
	
	//USR_FUSRVMPN CHAR(1) 'FLAG VALIDAZIONE PIN CODE MAIL ("Y"=PIN CODE VALIDATO, "N"=PIN CODE NON VALIDATO)'
	private String flagValidazioneMail = null;
	
	//USR_FUSRVPPN CHAR(1) 'FLAG PER CONFERMARE LA VALIDAZIONE DELLA EMAIL PEC'
	private String flagValidazionePec = null;
	
	//RE180181_001 SB - fine
	
	/* TIPO AUTENTICAZIONE
	 * 'P'=PROPRIETARIA
	 * 'F'=FEDERATA
	 */
	private String tipoAutenticazione;
	
	/**
	 * 	//"PYUSRTB"."USR_FUSRAMTD" IS 'ASSOCIAZIONE MOVIMENTI TESORERIA DEFINITIVA';
	 */
	private boolean associazioniDefinitiveRiconciliazionemtEnabled=false;
	
	/**
	 * 	//"PYUSRTB"."USR_FUSRAMTP" IS 'ASSOCIAZIONE MOVIMENTI TESORERIA PROVVISORIA';
	 */
	private boolean associazioniProvvisorieRiconciliazionemtEnabled=false;
	
	public String getTipoAutenticazione() {
		return tipoAutenticazione;
	}

	public void setTipoAutenticazione(String tipoAutenticazione) {
		this.tipoAutenticazione = tipoAutenticazione;
	}

	private List<String> listaTipologiaServizio =null;


	public String getUserToken() {
		return userToken;
	}

	public List<String> getApplicazioni() {
		return applicazioni;
	}

	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNome() {
		return nome;
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}
	
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getTipologiaUtente() {
		return tipologiaUtente;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getEmailNotifiche() {
		return emailNotifiche;
	}

	public void setEmailNotifiche(String emailNotifiche) {
		this.emailNotifiche = emailNotifiche;
	}

	public String getSmsNotifiche() {
		return smsNotifiche;
	}

	public Calendar getDataUltimoAccesso() {
		return dataUltimoAccesso;
	}

	public Long getChiaveUtente() {
		return chiaveUtente;
	}
	
	public String getUserProfile() {
		return userProfile;
	}
	
	public void setUserProfile(String userProfile) {
		this.userProfile = userProfile;
		super.setProfile(userProfile);
	}
	
	public boolean getFlagAttivazioneEnabled() {
		return flagAttivazioneEnabled;
	}

	public String getCodiceSocieta() {
		return codiceSocieta;
	}

	public String getCodiceUtente() {
		return codiceUtente;
	}

	public String getChiaveEnteConsorzio() {
		return chiaveEnteConsorzio;
	}

	public String getChiaveEnteConsorziato() {
		return chiaveEnteConsorziato;
	}

	public boolean getDownloadFlussiRendicontazioneEnabled() {
		return downloadFlussiRendicontazioneEnabled;
	}

	public boolean getInvioFlussiRendicontazioneViaFtpEnabled() {
		return invioFlussiRendicontazioneViaFtpEnabled;
	}

	public boolean getInvioFlussiRendicontazioneViaEmailEnabled() {
		return invioFlussiRendicontazioneViaEmailEnabled;
	}

	public boolean getAzioniPerTransazioniOkEnabled() {
		return azioniPerTransazioniOkEnabled;
	}

	public boolean getAzioniPerTransazioniKoEnabled() {
		return azioniPerTransazioniKoEnabled;
	}

	public boolean getAzioniPerRiconciliazioneManualeEnabled() {
		return azioniPerRiconciliazioneManualeEnabled;
	}

	public boolean getAttivazioneEstrattoContoManagerEnabled() {
		return attivazioneEstrattoContoManagerEnabled;
	}

	public boolean getProfiloRiversamentoEnabled() {
		return profiloRiversamentoEnabled;
	}

	public boolean getMultiUtenteEnabled() {
		return multiUtenteEnabled;
	}

	
	public boolean isMailContoGestione() {
		return mailContoGestione;
	}

	public void setMailContoGestione(boolean mailContoGestione) {
		this.mailContoGestione = mailContoGestione;
	}
	
	//EP160510_001 GG 03112016 - inizio
	public String getEntePertinenza() {
		return entePertinenza;
	}

	public void setEntePertinenza(String entePertinenza) {
		this.entePertinenza = entePertinenza;
	}
	//EP160510_001 GG 03112016 - fine
	
	//RE180181_001 SB - inizio
	public String getGruppoAgenzia() {
		return gruppoAgenzia;
	}

	
	//PG14001X_001 GG 25112014 - inizio
	public void setUserToken(String token) {
		this.userToken = token;
	}
	//PG14001X_001 GG 25112014 - fine
	
	public void setGruppoAgenzia(String gruppoAgenzia) {
		this.gruppoAgenzia = gruppoAgenzia;
	}
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	public String getMailPec() {
		return mailPec;
	}

	public void setMailPec(String mailPec) {
		this.mailPec = mailPec;
	}
	
	public String getPinCodeMail() {
		return pinCodeMail;
	}

	public void setPinCodeMail(String pinCodeMail) {
		this.pinCodeMail = pinCodeMail;
	}
	
	public String getPinCodePec() {
		return pinCodePec;
	}

	public void setPinCodePec (String pinCodePec) {
		this.pinCodePec = pinCodePec;
	}
	
	public String getFlagValidazioneMail() {
		return flagValidazioneMail;
	}

	public void setFlagValidazioneMail(String flagValidazioneMail) {
		this.flagValidazioneMail = flagValidazioneMail;
	}
	
	public String getFlagValidazionePec() {
		return flagValidazionePec;
	}

	public void setFlagValidazionePec(String flagValidazionePec) {
		this.flagValidazionePec = flagValidazionePec;
	}
	//RE180181_001 SB - fine
	
	public boolean getAssociazioniDefinitiveRiconciliazionemtEnabled() {
		return associazioniDefinitiveRiconciliazionemtEnabled;
	}
	
	public boolean getAssociazioniProvvisorieRiconciliazionemtEnabled() {
		return associazioniProvvisorieRiconciliazionemtEnabled;
	}

	public boolean getInvioFlussiRendicontazioneViaWsEnabled() {
		return invioFlussiRendicontazioneViaWsEnabled;
	}

	public UserBean() {}

	public UserBean(UtentePIVA seUser)
	{
		setSeProperties(seUser);
	}
	
	public void setSeUserBean(UtentePIVA seUser)
	{
		setSeProperties(seUser);
	}
		
	private void setSeProperties(UtentePIVA seUser)
	{
		String token = null;
		if(seUser == null) return;
		try {
			token =  TokenGenerator.generateSecureToken(40);
		} catch (NoSuchAlgorithmException e) {
		}
		if(token == null) return;
		/*************************************************
		 * TOKEN DI SESSIONE
		 *************************************************/
		userToken = token;
		/*************************************************
		 * CAMPI DALLA TABELLA SEC00DB0.SE000SV.SEUSRTB
		 *************************************************/
		userName = seUser.getUsername();
		super.setName(userName);
		nome = seUser.getDatiAnagPersonaFisicaDelegato().getDatiGeneraliPersonaFisicaDelegato().getNome();
		cognome = seUser.getDatiAnagPersonaFisicaDelegato().getDatiGeneraliPersonaFisicaDelegato().getCognome();
		tipologiaUtente = seUser.getTipologiaUtente().getValue();
		codiceFiscale = seUser.getDatiAnagPersonaFisicaDelegato().getDatiGeneraliPersonaFisicaDelegato().getCodiceFiscale();
		emailNotifiche = seUser.getDatiAnagPersonaFisicaDelegato().getDatiContattiPersonaFisicaDelegato().getMail();
		smsNotifiche = seUser.getDatiAnagPersonaFisicaDelegato().getDatiContattiPersonaFisicaDelegato().getCellulare();
		dataUltimoAccesso = seUser.getDataUltimoAccesso();
	}
	
	
	public void setPyUserBean_ListaAppl(UserBeanType pyUser,String[] listaApplicazioni)
	{
		if(pyUser == null || listaApplicazioni == null) return;
		//QF 2013.05.07 perchè devo controllarlo?	if (userToken == null) return;
		

		
		/*************************************************
		 * CAMPI DALLA TABELLA PAY00DB0.SE000SV.PYUSRTB
		 *************************************************/
		chiaveUtente = pyUser.getChiaveUtente();
		userProfile = pyUser.getUserProfile();
		super.setProfile(userProfile);
		flagAttivazioneEnabled = (pyUser.getFlagAttivazione() == null ? false : pyUser.getFlagAttivazione().equalsIgnoreCase("Y"));
		codiceSocieta = pyUser.getCodiceSocieta();
		codiceUtente = pyUser.getCodiceUtente();
		chiaveEnteConsorzio = pyUser.getChiaveEnteConsorzio();
		chiaveEnteConsorziato = pyUser.getChiaveEnteConsorziato();
		downloadFlussiRendicontazioneEnabled = (pyUser.getDownloadFlussiRendicontazione() == null ? false : pyUser.getDownloadFlussiRendicontazione().equalsIgnoreCase("Y"));
		invioFlussiRendicontazioneViaFtpEnabled = (pyUser.getInvioFlussiRendicontazioneViaFtp() == null ? false : pyUser.getInvioFlussiRendicontazioneViaFtp().equalsIgnoreCase("Y"));
		invioFlussiRendicontazioneViaEmailEnabled = (pyUser.getInvioFlussiRendicontazioneViaEmail() == null ? false : pyUser.getInvioFlussiRendicontazioneViaEmail().equalsIgnoreCase("Y"));
		invioFlussiRendicontazioneViaWsEnabled = (pyUser.getInvioFlussiRendicontazioneViaWs() == null ? false : pyUser.getInvioFlussiRendicontazioneViaWs().equalsIgnoreCase("Y"));
		azioniPerTransazioniOkEnabled = (pyUser.getAzioniPerTransazioniOK() == null ? false : pyUser.getAzioniPerTransazioniOK().equalsIgnoreCase("Y"));
		azioniPerTransazioniKoEnabled= (pyUser.getAzioniPerTransazioniKO() == null ? false : pyUser.getAzioniPerTransazioniKO().equalsIgnoreCase("Y"));
		azioniPerRiconciliazioneManualeEnabled = (pyUser.getAzioniPerRiconciliazioneManuale() == null ? false : pyUser.getAzioniPerRiconciliazioneManuale().equalsIgnoreCase("Y"));
		attivazioneEstrattoContoManagerEnabled = (pyUser.getAttivazioneEstrattoContoManager() == null ? false : pyUser.getAttivazioneEstrattoContoManager().equalsIgnoreCase("Y"));
		profiloRiversamentoEnabled = (pyUser.getAbilitazioneProfiloRiversamento() == null ? false : pyUser.getAbilitazioneProfiloRiversamento().equalsIgnoreCase("Y"));
		multiUtenteEnabled = (pyUser.getAbilitazioneMultiUtente() == null ? false : pyUser.getAbilitazioneMultiUtente().equalsIgnoreCase("Y"));

		mailContoGestione = (pyUser.getMailContoGestione() == null ? false : pyUser.getMailContoGestione().equalsIgnoreCase("Y"));	
		
		associazioniDefinitiveRiconciliazionemtEnabled = (pyUser.getAssociazioniDefinitiveRiconciliazionemt() == null ? false : pyUser.getAssociazioniDefinitiveRiconciliazionemt().equalsIgnoreCase("Y"));
		associazioniProvvisorieRiconciliazionemtEnabled = (pyUser.getAssociazioniProvvisorieRiconciliazionemt() == null ? false : pyUser.getAssociazioniProvvisorieRiconciliazionemt().equalsIgnoreCase("Y"));
		
		if(listaApplicazioni.length > 0)
		{
			applicazioni = new Vector<String>();
			for(int i=0; i<listaApplicazioni.length; i++)
			{
				applicazioni.add(listaApplicazioni[i]);
			}
		}
		
		setListaTipologiaServizio(pyUser.getListaTipologiaSevizio().equals("") ? new String[0] :  pyUser.getListaTipologiaSevizio().split("\\|"));
		
		entePertinenza = (pyUser.getEntePertinenza() == null ? "" : pyUser.getEntePertinenza()); 	//EP160510_001 GG 03112016
		
		//RE180181_001 SB - inizio
		
		gruppoAgenzia = (pyUser.getGruppoAgenzia() == null ? "" : pyUser.getGruppoAgenzia()); 	
		mail = (pyUser.getMail() == null ? "" : pyUser.getMail()); 	
		mailPec = (pyUser.getMailPec() == null ? "" : pyUser.getMailPec()); 	
		pinCodeMail = (pyUser.getPinCodeMail() == null ? "" : pyUser.getPinCodeMail()); 	
		pinCodePec = (pyUser.getPinCodePec() == null ? "" : pyUser.getPinCodePec()); 
		flagValidazioneMail = (pyUser.getFlagValidazioneMail() == null ? "N" : pyUser.getFlagValidazioneMail()); 
		flagValidazionePec = (pyUser.getFlagValidazionePec() == null ? "N" : pyUser.getFlagValidazionePec()); 
		//RE180181_001 SB - fine
		}
	
	public static boolean isValid(UserBean userBean)
	{
		if(userBean == null) return false;
		return (userBean.getUserName() != null && userBean.getUserToken() != null);
				//&& !userBean.getUserProfile().equalsIgnoreCase(ManagerKeys.CHIAVE_MULTIPROFILO));
	}

	public void setListaTipologiaServizio(String[] listaTipologiaServizio) {
		if(listaTipologiaServizio.length > 0)
		{
			this.listaTipologiaServizio = new ArrayList<String>();
			for(int i=0; i<listaTipologiaServizio.length; i++)
			{
				this.listaTipologiaServizio.add(listaTipologiaServizio[i]);
			}
		}
		
	}

	public List<String> getListaTipologiaServizio() {
		return this.listaTipologiaServizio;
	}

	public String getListaTipologiaServizioString() {
		String listaTipologiaServizio="";
		if (this.listaTipologiaServizio != null) {
			for (String tipologiaServizio : this.listaTipologiaServizio) {
				listaTipologiaServizio+="'" + tipologiaServizio + "',";
			}	
		}
		
		if (listaTipologiaServizio.equals(""))
			return listaTipologiaServizio;
		else
			return listaTipologiaServizio.substring(0, listaTipologiaServizio.length()-1);
	}

	
	
	
	
	
}