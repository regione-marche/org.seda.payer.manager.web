package org.seda.payer.manager.ecmanager.actions.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class NotificheLis {

	public String chiaveTabellaNotifica;
	public String codiceEnte;
	public String codiceSocieta;
	public String codiceUtente;
	public String codiceFiscale;
	public String tipologiaNotifica;
	public String tipologiaServizio;
	public String numeroDocumento;
	public String email;
	public String emailPec;
	public String numTelefono;
	public String esitoNotifica;
	public String dataRicezione;
	public String dataInvio;
	public String getChiaveTabellaNotifica() {
		return chiaveTabellaNotifica;
	}
	public void setChiaveTabellaNotifica(String chiaveTabellaNotifica) {
		this.chiaveTabellaNotifica = chiaveTabellaNotifica;
	}
	public String getCodiceEnte() {
		return codiceEnte;
	}
	public void setCodiceEnte(String codiceEnte) {
		this.codiceEnte = codiceEnte;
	}
	public String getCodiceSocieta() {
		return codiceSocieta;
	}
	public void setCodiceSocieta(String codiceSocieta) {
		this.codiceSocieta = codiceSocieta;
	}
	public String getCodiceUtente() {
		return codiceUtente;
	}
	public void setCodiceUtente(String codiceUtente) {
		this.codiceUtente = codiceUtente;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getTipologiaNotifica() {
		return tipologiaNotifica;
	}
	public void setTipologiaNotifica(String tipologiaNotifica) {
		this.tipologiaNotifica = tipologiaNotifica;
	}
	public String getTipologiaServizio() {
		return tipologiaServizio;
	}
	public void setTipologiaServizio(String tipologiaServizio) {
		this.tipologiaServizio = tipologiaServizio;
	}
	public String getNumeroDocumento() {
		return numeroDocumento;
	}
	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getNumTelefono() {
		return numTelefono;
	}
	public void setNumTelefono(String numTelefono) {
		this.numTelefono = numTelefono;
	}
	public String getEsitoNotifica() {
		return esitoNotifica;
	}
	public void setEsitoNotifica(String esitoNotifica) {
		this.esitoNotifica = esitoNotifica;
	}
	public String getDataRicezione() {
		return dataRicezione;
	}
	public void setDataRicezione(String dataRicezione) {
		this.dataRicezione = dataRicezione;
	}
	public String getDataInvio() {
		return dataInvio;
	}
	public void setDataInvio(String dataInvio) {
		this.dataInvio = dataInvio;
	}
	public String getEmailPec() {
		return emailPec;
	}
	public void setEmailPec(String emailPec) {
		this.emailPec = emailPec;
	}
	
}