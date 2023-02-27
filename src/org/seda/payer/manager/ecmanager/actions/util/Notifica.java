package org.seda.payer.manager.ecmanager.actions.util;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Notifica {

	public String codiceEnte;
	public String codiceSocieta;
	public String codiceUtente;
	public String flagTipoMessaggio;
	public String flagTipoServizio;
	public String codiceFiscale;
	public String mittente;
	public String destinatario;
	public String conoscenzaNascosta;
	public String messaggio;
	public String dataInvio;
	public String pdfFileBase64;

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
	public String getFlagTipoMessaggio() {
		return flagTipoMessaggio;
	}
	public void setFlagTipoMessaggio(String flagTipoMessaggio) {
		this.flagTipoMessaggio = flagTipoMessaggio;
	}
	public String getFlagTipoServizio() {
		return flagTipoServizio;
	}
	public void setFlagTipoServizio(String flagTipoServizio) {
		this.flagTipoServizio = flagTipoServizio;
	}
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getMittente() {
		return mittente;
	}
	public void setMittente(String mittente) {
		this.mittente = mittente;
	}
	public String getDestinatario() {
		return destinatario;
	}
	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}
	public String getConoscenzaNascosta() {
		return conoscenzaNascosta;
	}
	public void setConoscenzaNascosta(String conoscenzaNascosta) {
		this.conoscenzaNascosta = conoscenzaNascosta;
	}
	public String getMessaggio() {
		return messaggio;
	}
	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	public String getDataInvio() {
		return dataInvio;
	}
	public void setDataInvio(String dataInvio) {
		this.dataInvio = dataInvio;
	}
	public String getPdfFileBase64() {
		return pdfFileBase64;
	}
	public void setPdfFileBase64(String pdfFileBase64) {
		this.pdfFileBase64 = pdfFileBase64;
	}
	
	
}