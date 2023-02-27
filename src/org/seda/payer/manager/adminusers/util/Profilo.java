package org.seda.payer.manager.adminusers.util;

import java.io.Serializable;
import java.util.Calendar;

import org.seda.payer.manager.util.GenericsDateNumbers;


public class Profilo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String personaFisica;
	private String personaGiuridica;
	private String username;
	private String usernameValid;
	
	private String password;
	private String note;
	private String tipologiaUtente;
	private boolean passwordAutogenerata=true;
	private boolean nessunaScadenza=true;
	private boolean utenzaAttiva=true;
	private boolean operatoreBackOffice=true;
	private boolean flagNoControlli=false;	//PG180170 GG 17102018
	
	public boolean isPasswordAutogenerata() {
		return  passwordAutogenerata;
	}
	public void setPasswordAutogenerata(boolean passwordAutogenerata) {
		this.passwordAutogenerata = passwordAutogenerata;
	}
	public boolean isNessunaScadenza() {
		return nessunaScadenza;
	}
	public void setNessunaScadenza(boolean nessunaScadenza) {
		this.nessunaScadenza = nessunaScadenza;
	}
	public boolean isUtenzaAttiva() {
		return utenzaAttiva;
	}
	public void setUtenzaAttiva(boolean utenzaAttiva) {
		this.utenzaAttiva = utenzaAttiva;
	}
	public boolean isOperatoreBackOffice() {
		return operatoreBackOffice;
	}
	public void setOperatoreBackOffice(boolean operatoreBackOffice) {
		this.operatoreBackOffice = operatoreBackOffice;
	}
	public boolean isFlagNoControlli() {
		return flagNoControlli;
	}
	public void setFlagNoControlli(boolean flagNoControlli) {
		this.flagNoControlli = flagNoControlli;
	}


	private Calendar dataFineValiditaUtenza; 
	
	/** DATI PERSONA FISICA / DELEGATO PERSONA GIURIDICA **/
	private String cognome;
	private String nome;
	private String codiceFiscale;
	private String sesso;
	private Calendar dataNascita;
	private String provinciaNascitaSigla;
	private String provinciaNascita;
	private String comuneNascitaSelected;
	private String comuneNascitaBelfiore;
	private String comuneNascitaEstero;
	private String ComuneNascitaDescrizione;
	private String tipoDocumento;
	private String numeroDocumento;
	private String enteRilascioDocumento;
	private Calendar dataRilascioDocumento;
	private String indirizzoResidenza;
	private String provinciaResidenzaSigla;
	private String provinciaResidenza;
	private String comuneResidenzaSelected;
	private String comuneResidenzaBelfiore;
	private String ComuneResidenzaDescrizione;
	private String capResidenza;
	private String comuneResidenzaEstero;
	private String indirizzoDomicilio;
	private String provinciaDomicilioSigla;
	private String provinciaDomicilio;
	private String comuneDomicilioSelected;
	private String comuneDomicilioBelfiore;
	private String ComuneDomicilioDescrizione;
	private String capDomicilio;
	private String comuneDomicilioEstero;
	private String email;
	private String emailPEC;
	private String telefono;
	private String cellulare;
	private String fax;
	
	
	/** DATI IMPRESA **/
	private String partitaIVA;
	private String ragioneSociale;
	private String classificazioneMerceologica;
	private String classificazioneMerceologicaDettaglio;
	private String numeroAutorizzazione;
	private String indirizzoSedeLegale;
	private String provinciaSedeLegaleSigla;
	private String provinciaSedeLegale;
	private String comuneSedeLegaleSelected;
	private String comuneSedeLegaleBelfiore;
	private String ComuneSedeLegaleDescrizione;
	private String capSedeLegale;
	private String comuneSedeLegaleEstero;
	
	private String indirizzoSedeOperativa;
	private String provinciaSedeOperativaSigla;
	private String provinciaSedeOperativa;
	private String comuneSedeOperativaSelected;
	private String comuneSedeOperativaBelfiore;
	private String ComuneSedeOperativaDescrizione;
	private String capSedeOperativa;
	private String emailImpresa;
	private String emailPECImpresa;
	private String telefonoImpresa;
	private String cellulareImpresa;
	private String faxImpresa;
	
	public void setPersonaFisica(String personaFisica) {
		this.personaFisica = personaFisica;
	}
	public String getPersonaFisica() {
		return personaFisica != null ? personaFisica : "Y";
	}
	public void setPersonaGiuridica(String personaGiuridica) {
		this.personaGiuridica = personaGiuridica;
	}
	public String getPersonaGiuridica() {
		return personaGiuridica != null ? personaGiuridica : "N";
	}
	public String getUsername() {
		return username != null ? username : "";
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setUsernameValid(String usernameValid) {
		this.usernameValid = usernameValid;
	}
	public String getUsernameValid() {
		return usernameValid != null ? usernameValid : "";
	}
	public String getCognome() {
		return cognome != null ? cognome : "";
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNome() {
		return nome != null ? nome : "";
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCodiceFiscale() {
		return codiceFiscale != null ? codiceFiscale.toUpperCase() : "";
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getSesso() {
		return sesso != null ? sesso : "M";
	}
	public void setSesso(String sesso) {
		this.sesso = sesso;
	}
	public Calendar getDataNascita() {
		return dataNascita != null ? dataNascita : GenericsDateNumbers.getMinDate();
	}
	public void setDataNascita(Calendar dataNascita) {
		this.dataNascita = dataNascita;
	}
	public String getProvinciaNascitaSigla() {
		return provinciaNascitaSigla != null ? provinciaNascitaSigla : "";
	}
	public void setProvinciaNascitaSigla(String provinciaNascitaSigla) {
		this.provinciaNascitaSigla = provinciaNascitaSigla;
	}
	public String getProvinciaNascita() {
		return provinciaNascita != null ? provinciaNascita : "";
	}
	public void setProvinciaNascita(String provinciaNascita) {
		this.provinciaNascita = provinciaNascita;
	}
	public void setComuneNascitaSelected(String comuneNascitaSelected) {
		this.comuneNascitaSelected = comuneNascitaSelected;
	}
	public String getComuneNascitaSelected() {
		return comuneNascitaSelected;
	}
	public String getComuneNascitaBelfiore() {
		return comuneNascitaBelfiore != null ? comuneNascitaBelfiore : "";
	}
	public void setComuneNascitaBelfiore(String comuneNascitaBelfiore) {
		this.comuneNascitaBelfiore = comuneNascitaBelfiore;
	}
	public String getComuneNascitaEstero() {
		return comuneNascitaEstero != null ? comuneNascitaEstero : "N";
	}
	public void setComuneNascitaEstero(String comuneNascitaEstero) {
		this.comuneNascitaEstero = comuneNascitaEstero;
	}
	public String getTipoDocumento() {
		return tipoDocumento != null ? tipoDocumento : "";
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getNumeroDocumento() {
		return numeroDocumento != null ? numeroDocumento : "";
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getEnteRilascioDocumento() {
		return enteRilascioDocumento != null ? enteRilascioDocumento : "";
	}
	public void setEnteRilascioDocumento(String enteRilascioDocumento) {
		this.enteRilascioDocumento = enteRilascioDocumento;
	}
	public Calendar getDataRilascioDocumento() {
		return dataRilascioDocumento != null ? dataRilascioDocumento : GenericsDateNumbers.getMinDate();
	}
	public void setDataRilascioDocumento(Calendar dataRilascioDocumento) {
		this.dataRilascioDocumento = dataRilascioDocumento;
	}
	public String getIndirizzoResidenza() {
		return indirizzoResidenza != null ? indirizzoResidenza : "";
	}
	public void setIndirizzoResidenza(String indirizzoResidenza) {
		this.indirizzoResidenza = indirizzoResidenza;
	}
	public String getProvinciaResidenzaSigla() {
		return provinciaResidenzaSigla != null ? provinciaResidenzaSigla : "";
	}
	public void setProvinciaResidenzaSigla(String provinciaResidenzaSigla) {
		this.provinciaResidenzaSigla = provinciaResidenzaSigla;
	}
	public String getProvinciaResidenza() {
		return provinciaResidenza != null ? provinciaResidenza : "";
	}
	public void setProvinciaResidenza(String provinciaResidenza) {
		this.provinciaResidenza = provinciaResidenza;
	}
	public String getComuneResidenzaSelected() {
		return comuneResidenzaSelected != null ? comuneResidenzaSelected : "";
	}
	public void setComuneResidenzaSelected(String comuneResidenzaSelected) {
		this.comuneResidenzaSelected = comuneResidenzaSelected;
	}
	public String getComuneResidenzaBelfiore() {
		return comuneResidenzaBelfiore != null ? comuneResidenzaBelfiore : "";
	}
	public void setComuneResidenzaBelfiore(String comuneResidenzaBelfiore) {
		this.comuneResidenzaBelfiore = comuneResidenzaBelfiore;
	}
	public String getCapResidenza() {
		return capResidenza != null ? capResidenza : "";
	}
	public void setCapResidenza(String capResidenza) {
		this.capResidenza = capResidenza;
	}
	public String getComuneResidenzaEstero() {
		return comuneResidenzaEstero != null ? comuneResidenzaEstero : "N";
	}
	public void setComuneResidenzaEstero(String comuneResidenzaEstero) {
		this.comuneResidenzaEstero = comuneResidenzaEstero;
	}
	public String getIndirizzoDomicilio() {
		return indirizzoDomicilio != null ? indirizzoDomicilio : "";
	}
	public void setIndirizzoDomicilio(String indirizzoDomicilio) {
		this.indirizzoDomicilio = indirizzoDomicilio;
	}
	public String getProvinciaDomicilioSigla() {
		return provinciaDomicilioSigla != null ? provinciaDomicilioSigla : "";
	}
	public void setProvinciaDomicilioSigla(String provinciaDomicilioSigla) {
		this.provinciaDomicilioSigla = provinciaDomicilioSigla;
	}
	public String getProvinciaDomicilio() {
		return provinciaDomicilio != null ? provinciaDomicilio : "";
	}
	public void setProvinciaDomicilio(String provinciaDomicilio) {
		this.provinciaDomicilio = provinciaDomicilio;
	}
	public String getComuneDomicilioSelected() {
		return comuneDomicilioSelected != null ? comuneDomicilioSelected : "";
	}
	public void setComuneDomicilioSelected(String comuneDomicilioSelected) {
		this.comuneDomicilioSelected = comuneDomicilioSelected;
	}
	public String getComuneDomicilioBelfiore() {
		return comuneDomicilioBelfiore != null ? comuneDomicilioBelfiore : "";
	}
	public void setComuneDomicilioBelfiore(String comuneDomicilioBelfiore) {
		this.comuneDomicilioBelfiore = comuneDomicilioBelfiore;
	}
	public String getCapDomicilio() {
		return capDomicilio != null ? capDomicilio : "";
	}
	public void setCapDomicilio(String capDomicilio) {
		this.capDomicilio = capDomicilio;
	}
	public void setComuneDomicilioEstero(String comuneDomicilioEstero) {
		this.comuneDomicilioEstero = comuneDomicilioEstero;
	}
	public String getComuneDomicilioEstero() {
		return comuneDomicilioEstero != null ? comuneDomicilioEstero : "N";
	}
	public String getEmail() {
		return email != null ? email : "";
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmailPEC() {
		return emailPEC != null ? emailPEC : "";
	}
	public void setEmailPEC(String emailPEC) {
		this.emailPEC = emailPEC;
	}
	public String getTelefono() {
		return telefono != null ? telefono : "";
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getCellulare() {
		return cellulare != null ? cellulare : "";
	}
	public void setCellulare(String cellulare) {
		this.cellulare = cellulare;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getFax() {
		return fax != null ? fax : "";
	}
	public String getPartitaIVA() {
		return partitaIVA != null ? partitaIVA : "";
	}
	public void setPartitaIVA(String partitaIVA) {
		this.partitaIVA = partitaIVA;
	}
	public String getRagioneSociale() {
		return ragioneSociale != null ? ragioneSociale : "";
	}
	public void setRagioneSociale(String ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public String getClassificazioneMerceologica() {
		return classificazioneMerceologica != null ? classificazioneMerceologica : "";
	}
	public void setClassificazioneMerceologica(String classificazioneMerceologica) {
		this.classificazioneMerceologica = classificazioneMerceologica;
	}
	public String getClassificazioneMerceologicaDettaglio() {
		return classificazioneMerceologicaDettaglio != null ? classificazioneMerceologicaDettaglio : "";
	}
	public void setClassificazioneMerceologicaDettaglio(String classificazioneMerceologicaDettaglio) {
		this.classificazioneMerceologicaDettaglio = classificazioneMerceologicaDettaglio;
	}
	public String getNumeroAutorizzazione() {
		return numeroAutorizzazione != null ? numeroAutorizzazione : "";
	}
	public void setNumeroAutorizzazione(String numeroAutorizzazione) {
		this.numeroAutorizzazione = numeroAutorizzazione;
	}
	public String getIndirizzoSedeLegale() {
		return indirizzoSedeLegale != null ? indirizzoSedeLegale : "";
	}
	public void setIndirizzoSedeLegale(String indirizzoSedeLegale) {
		this.indirizzoSedeLegale = indirizzoSedeLegale;
	}
	public String getProvinciaSedeLegaleSigla() {
		return provinciaSedeLegaleSigla != null ? provinciaSedeLegaleSigla : "";
	}
	public void setProvinciaSedeLegaleSigla(String provinciaSedeLegaleSigla) {
		this.provinciaSedeLegaleSigla = provinciaSedeLegaleSigla;
	}
	public String getProvinciaSedeLegale() {
		return provinciaSedeLegale != null ? provinciaSedeLegale : "";
	}
	public void setProvinciaSedeLegale(String provinciaSedeLegale) {
		this.provinciaSedeLegale = provinciaSedeLegale;
	}
	public String getComuneSedeLegaleSelected() {
		return comuneSedeLegaleSelected != null ? comuneSedeLegaleSelected : "";
	}
	public void setComuneSedeLegaleSelected(String comuneSedeLegaleSelected) {
		this.comuneSedeLegaleSelected = comuneSedeLegaleSelected;
	}
	public String getComuneSedeLegaleBelfiore() {
		return comuneSedeLegaleBelfiore != null ? comuneSedeLegaleBelfiore : "";
	}
	public void setComuneSedeLegaleBelfiore(String comuneSedeLegaleBelfiore) {
		this.comuneSedeLegaleBelfiore = comuneSedeLegaleBelfiore;
	}
	public String getCapSedeLegale() {
		return capSedeLegale != null ? capSedeLegale : "";
	}
	public void setCapSedeLegale(String capSedeLegale) {
		this.capSedeLegale = capSedeLegale;
	}
	public String getComuneSedeLegaleEstero() {
		return comuneSedeLegaleEstero != null ? comuneSedeLegaleEstero : "N";
	}
	public void setComuneSedeLegaleEstero(String comuneSedeLegaleEstero) {
		this.comuneSedeLegaleEstero = comuneSedeLegaleEstero;
	}
	public String getIndirizzoSedeOperativa() {
		return indirizzoSedeOperativa != null ? indirizzoSedeOperativa : "";
	}
	public void setIndirizzoSedeOperativa(String indirizzoSedeOperativa) {
		this.indirizzoSedeOperativa = indirizzoSedeOperativa;
	}
	public void setProvinciaSedeOperativaSigla(String provinciaSedeOperativaSigla) {
		this.provinciaSedeOperativaSigla = provinciaSedeOperativaSigla;
	}
	public String getProvinciaSedeOperativaSigla() {
		return provinciaSedeOperativaSigla != null ? provinciaSedeOperativaSigla : "";
	}
	public String getProvinciaSedeOperativa() {
		return provinciaSedeOperativa != null ? provinciaSedeOperativa : "";
	}
	public void setProvinciaSedeOperativa(String provinciaSedeOperativa) {
		this.provinciaSedeOperativa = provinciaSedeOperativa;
	}
	public String getComuneSedeOperativaSelected() {
		return comuneSedeOperativaSelected != null ? comuneSedeOperativaSelected : "";
	}
	public void setComuneSedeOperativaSelected(String comuneSedeOperativaSelected) {
		this.comuneSedeOperativaSelected = comuneSedeOperativaSelected;
	}
	public String getComuneSedeOperativaBelfiore() {
		return comuneSedeOperativaBelfiore != null ? comuneSedeOperativaBelfiore : "";
	}
	public void setComuneSedeOperativaBelfiore(String comuneSedeOperativaBelfiore) {
		this.comuneSedeOperativaBelfiore = comuneSedeOperativaBelfiore;
	}
	public String getCapSedeOperativa() {
		return capSedeOperativa != null ? capSedeOperativa : "";
	}
	public void setCapSedeOperativa(String capSedeOperativa) {
		this.capSedeOperativa = capSedeOperativa;
	}
	public String getEmailImpresa() {
		return emailImpresa != null ? emailImpresa : "";
	}
	public void setEmailImpresa(String emailImpresa) {
		this.emailImpresa = emailImpresa;
	}
	public String getEmailPECImpresa() {
		return emailPECImpresa != null ? emailPECImpresa : "";
	}
	public void setEmailPECImpresa(String emailPECImpresa) {
		this.emailPECImpresa = emailPECImpresa;
	}
	public String getTelefonoImpresa() {
		return telefonoImpresa != null ? telefonoImpresa : "";
	}
	public void setTelefonoImpresa(String telefonoImpresa) {
		this.telefonoImpresa = telefonoImpresa;
	}
	public String getCellulareImpresa() {
		return cellulareImpresa != null ? cellulareImpresa : "";
	}
	public void setCellulareImpresa(String cellulareImpresa) {
		this.cellulareImpresa = cellulareImpresa;
	}
	
	public void setFaxImpresa(String faxImpresa) {
		this.faxImpresa = faxImpresa;
	}
	public String getFaxImpresa() {
		return faxImpresa != null ? faxImpresa : "";
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPassword() {
		return password;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getNote() {
		return note  == null ? "" : note ;
	}
	public void setTipologiaUtente(String tipologiaUtente) {
		this.tipologiaUtente = tipologiaUtente;
	}
	public String getTipologiaUtente() {
		return tipologiaUtente == null ? "" : tipologiaUtente;
	}
	public void setDataFineValiditaUtenza(Calendar dataFineValiditaUtenza) {
		this.dataFineValiditaUtenza = dataFineValiditaUtenza;
	}
	public Calendar getDataFineValiditaUtenza() {
		return dataFineValiditaUtenza;
	}

	public void setComuneNascitaDescrizione(String comuneNascitaDescrizione) {
		ComuneNascitaDescrizione = comuneNascitaDescrizione;
	}
	public String getComuneNascitaDescrizione() {
		return ComuneNascitaDescrizione;
	}
	public void setComuneResidenzaDescrizione(String comuneResidenzaDescrizione) {
		ComuneResidenzaDescrizione = comuneResidenzaDescrizione;
	}
	public String getComuneResidenzaDescrizione() {
		return ComuneResidenzaDescrizione;
	}
	public void setComuneDomicilioDescrizione(String comuneDomicilioDescrizione) {
		ComuneDomicilioDescrizione = comuneDomicilioDescrizione;
	}
	public String getComuneDomicilioDescrizione() {
		return ComuneDomicilioDescrizione;
	}
	public void setComuneSedeLegaleDescrizione(
			String comuneSedeLegaleDescrizione) {
		ComuneSedeLegaleDescrizione = comuneSedeLegaleDescrizione;
	}
	public String getComuneSedeLegaleDescrizione() {
		return ComuneSedeLegaleDescrizione;
	}
	public void setComuneSedeOperativaDescrizione(
			String comuneSedeOperativaDescrizione) {
		ComuneSedeOperativaDescrizione = comuneSedeOperativaDescrizione;
	}
	public String getComuneSedeOperativaDescrizione() {
		return ComuneSedeOperativaDescrizione;
	}
	
	public void clearProvinciaNascita()
	{
		setProvinciaNascita("");
		setProvinciaNascitaSigla("");
	}
	
	public void clearComuneNascita()
	{
		setComuneNascitaSelected("");
		setComuneNascitaBelfiore("");
		setComuneNascitaDescrizione("");
	}
	
	public void clearProvinciaResidenza()
	{
		setProvinciaResidenza("");
		setProvinciaResidenzaSigla("");
	}
	
	public void clearComuneResidenza()
	{
		setComuneResidenzaSelected("");
		setComuneResidenzaBelfiore("");
		setComuneResidenzaDescrizione("");
	}
	
	public void clearProvinciaDomicilio()
	{
		setProvinciaDomicilio("");
		setProvinciaDomicilioSigla("");
	}
	
	public void clearComuneDomicilio()
	{
		setComuneDomicilioSelected("");
		setComuneDomicilioBelfiore("");
		setComuneDomicilioDescrizione("");
	}
	
	public void clearProvinciaSedeLegale()
	{
		setProvinciaSedeLegale("");
		setProvinciaSedeLegaleSigla("");
	}
	
	public void clearComuneSedeLegale()
	{
		setComuneSedeLegaleSelected("");
		setComuneSedeLegaleBelfiore("");
		setComuneSedeLegaleDescrizione("");
	}
	
	public void clearProvinciaSedeOperativa()
	{
		setProvinciaSedeOperativa("");
		setProvinciaSedeOperativaSigla("");
	}
	
	public void clearComuneSedeOperativa()
	{
		setComuneSedeOperativaSelected("");
		setComuneSedeOperativaBelfiore("");
		setComuneSedeOperativaDescrizione("");
	}
	
	public void clearClassificazioneMerceologica()
	{
		setClassificazioneMerceologicaDettaglio("");
		setNumeroAutorizzazione("");
	}
	
	//PG180050_001 GG 18042018 - inizio
	public void clearNumeroAutorizzazione()
	{
		setNumeroAutorizzazione("");
	}
	//PG180050_001 GG 18042018 - fine
}
