package org.seda.payer.manager.components.security;

import java.io.Serializable;
/**
 * Questo bean contiene la coppia (chiave_profilo_utente,descrizione_profilo)
 * Una collection di istanze di questa classe viene utilizzata
 * per costruire dinamicamente il radio button  nella pagina
 * che l'utente utilizza per sceglieer il profilo con cui vuole fare login
 * @author f.vadicamo
 *
 */
public class Profilo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long chiaveUtente;
	private String descProfilo;
	private String descrSocieta;
	private String descrUtente;
	private String descrEnte;
	
	public Profilo() {}
	
	public Long getChiaveUtente() {
		return chiaveUtente;
	}
	
	public void setChiaveUtente(Long chiaveUtente) {
		this.chiaveUtente = chiaveUtente;
	}
	
	public String getDescProfilo() {
		return descProfilo;
	}
	
	public void setDescProfilo(String descProfilo) {
		this.descProfilo = descProfilo;
	}

	public void setDescrSocieta(String descrSocieta) {
		this.descrSocieta = descrSocieta;
	}

	public String getDescrSocieta() {
		return descrSocieta;
	}

	public void setDescrUtente(String descrUtente) {
		this.descrUtente = descrUtente;
	}

	public String getDescrUtente() {
		return descrUtente;
	}

	public void setDescrEnte(String descrEnte) {
		this.descrEnte = descrEnte;
	}

	public String getDescrEnte() {
		return descrEnte;
	}

	


}
