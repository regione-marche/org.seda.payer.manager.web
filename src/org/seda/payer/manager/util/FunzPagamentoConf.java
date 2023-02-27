package org.seda.payer.manager.util;

import java.io.Serializable;

public class FunzPagamentoConf implements Serializable{
	private static final long serialVersionUID = -797361696208851270L;
	private String chiave;
	private String descrizione;
	private String obbligatorieta;
	private String tipoValore;
	private String tipoLunghezza;
	private int lunghezza;
	private String lunghezzaMax;
	private String tipoDescrizioneAlternativa = "";
	private String descrizioneAlternativa = "";
	private boolean flagBloccoObbligatorioModificabile = true;
	private boolean flagBloccoTipoValoreModificabile = true;
	private boolean flagBloccoLunghezzaModificabile = true;
	private boolean flagBloccoDescrizioneAlternativaModificabile = true;
	

	public FunzPagamentoConf(){}
	
	
	public FunzPagamentoConf(String nomeForm, String descrizione, String immissioneVal,
			String flagTipoValore, String flagLunghezza, String lunghezza,
			String flagDescrAlt, String descrAlternativa, String lunghezzaMax,
			boolean  flagBloccoObbligatorioModificabile, boolean  flagBloccoTipoValoreModificabile, boolean flagBloccoLunghezzaModificabile, boolean  flagBloccoDescrizioneAlternativaModificabile) {
		this.chiave=nomeForm;
		this.descrizione=descrizione;
		this.obbligatorieta=immissioneVal;
		this.tipoValore=flagTipoValore;
		this.tipoLunghezza=flagLunghezza;
		this.lunghezza=Integer.parseInt(lunghezza);
		this.lunghezzaMax=lunghezzaMax;
		this.tipoDescrizioneAlternativa=flagDescrAlt;
		this.descrizioneAlternativa=descrAlternativa; 
		this.flagBloccoObbligatorioModificabile = flagBloccoObbligatorioModificabile ;
		this.flagBloccoTipoValoreModificabile = flagBloccoTipoValoreModificabile ;
		this.flagBloccoLunghezzaModificabile = flagBloccoLunghezzaModificabile;
		this.flagBloccoDescrizioneAlternativaModificabile = flagBloccoDescrizioneAlternativaModificabile;
	}
	
	
	
	public FunzPagamentoConf(String nomeForm, String descrizione, String immissioneVal,
			String flagTipoValore, String flagLunghezza, String lunghezza,
			String flagDescrAlt, String descrAlternativa, String lunghezzaMax,
			String flagBloccoObbligatorioModificabile, boolean  flagBloccoTipoValoreModificabile, String flagBloccoLunghezzaModificabile, String flagBloccoDescrizioneAlternativaModificabile) {
		
		this(nomeForm, flagBloccoDescrizioneAlternativaModificabile, immissioneVal, flagTipoValore, flagLunghezza, flagBloccoLunghezzaModificabile, flagDescrAlt, descrAlternativa, lunghezzaMax, 
				Boolean.valueOf( flagBloccoObbligatorioModificabile ), 
				Boolean.valueOf( flagBloccoTipoValoreModificabile ), 
				Boolean.valueOf( flagBloccoLunghezzaModificabile ),
				Boolean.valueOf( flagBloccoDescrizioneAlternativaModificabile ));
		 
	}
	
//	public FunzPagamentoConf(String nomeForm, String descrizione, String immissioneVal,
//			String flagTipoValore, String flagLunghezza, String lunghezza2,
//			String flagDescrAlt, String descrAlternativa) {
//		this.chiave=nomeForm;
//		this.descrizione=descrizione;
//		this.obbligatorieta=immissioneVal;
//		this.tipoValore=flagTipoValore;
//		this.tipoLunghezza=flagLunghezza;
//		this.lunghezza=Integer.parseInt(lunghezza2);
//		this.lunghezzaMax=lunghezza2;
//		this.tipoDescrizioneAlternativa=flagDescrAlt;
//		this.descrizioneAlternativa=descrAlternativa;
//	}
	 
	public String getChiave() {
		return chiave;
	}
	public void setChiave(String chiave) {
		this.chiave = chiave;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	public String getObbligatorieta() {
		return obbligatorieta;
	}
	public void setObbligatorieta(String obbligatorieta) {
		this.obbligatorieta = obbligatorieta;
	}
	public String getTipoValore() {
		return tipoValore;
	}
	public void setTipoValore(String tipoValore) {
		this.tipoValore = tipoValore;
	}
	public String getTipoLunghezza() {
		return tipoLunghezza;
	}
	public void setTipoLunghezza(String tipoLunghezza) {
		this.tipoLunghezza = tipoLunghezza;
	}
	public String getLunghezzaMax() {
		return lunghezzaMax;
	}
	public void setLunghezzaMax(String lunghezzaMax) {
		this.lunghezzaMax = lunghezzaMax;
	}
	public String getTipoDescrizioneAlternativa() {
		return tipoDescrizioneAlternativa;
	}
	public void setTipoDescrizioneAlternativa(String tipoDescrizioneAlternativa) {
		this.tipoDescrizioneAlternativa = tipoDescrizioneAlternativa;
	}
	public String getDescrizioneAlternativa() {
		return descrizioneAlternativa;
	}
	public void setDescrizioneAlternativa(String descrizioneAlternativa) {
		this.descrizioneAlternativa = descrizioneAlternativa;
	}
	public void setLunghezza(int lunghezza) {
		this.lunghezza = lunghezza;
	}
	public int getLunghezza() {
		return lunghezza;
	}

	public boolean isFlagBloccoObbligatorioModificabile() {
		return flagBloccoObbligatorioModificabile;
	}
	
	public void setFlagBloccoObbligatorioModificabile(String flag) {
		setFlagBloccoObbligatorioModificabile( flag!=null && flag.equalsIgnoreCase("Y") ); 
	}
	
	public void setFlagBloccoObbligatorioModificabile(
			boolean flagBloccoObbligatorioModificabile) {
		this.flagBloccoObbligatorioModificabile = flagBloccoObbligatorioModificabile;
	}
	 

	public void setFlagBloccoTipoValoreModificabile(String flag) {
		setFlagBloccoTipoValoreModificabile( flag!=null && flag.equalsIgnoreCase("Y") ); 
	}
	
	public boolean isFlagBloccoTipoValoreModificabile() {
		return flagBloccoTipoValoreModificabile;
	}

	public void setFlagBloccoTipoValoreModificabile(
			boolean flagBloccoTipoValoreModificabile) {
		this.flagBloccoTipoValoreModificabile = flagBloccoTipoValoreModificabile;
	}
	

	public boolean isFlagBloccoLunghezzaModificabile() {
		return flagBloccoLunghezzaModificabile;
	}

	public void setFlagBloccoLunghezzaModificabile(
			boolean flagBloccoLunghezzaModificabile) {
		this.flagBloccoLunghezzaModificabile = flagBloccoLunghezzaModificabile;
	}


	public void setFlagBloccoLunghezzaModificabile(String flag) {
		setFlagBloccoLunghezzaModificabile( flag!=null && flag.equalsIgnoreCase("Y") ); 
	}

	
	public boolean isFlagBloccoDescrizioneAlternativaModificabile() {
		return flagBloccoDescrizioneAlternativaModificabile;
	}

	public void setFlagBloccoDescrizioneAlternativaModificabile(
			boolean flagBloccoDescrizioneAlternativaModificabile) {
		this.flagBloccoDescrizioneAlternativaModificabile = flagBloccoDescrizioneAlternativaModificabile;
	}
	

	public void setFlagBloccoDescrizioneAlternativaModificabile(String flag) {
		setFlagBloccoDescrizioneAlternativaModificabile( flag!=null && flag.equalsIgnoreCase("Y") ); 
	}

	
}
