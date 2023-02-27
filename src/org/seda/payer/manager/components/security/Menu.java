package org.seda.payer.manager.components.security;

import java.io.Serializable;
import java.sql.SQLException;

import javax.sql.rowset.WebRowSet;

public class Menu implements Serializable {
	private static final long serialVersionUID = 1L;
		
	//MNU_PMNUIDMN
	private int idMenu;
	//MNU_PMNUPRNT
	private int idParent;
	//MNU_CMNUAPPL
	private String applicazioni;
	//MNU_NMNULIVE
	private int livelloMenu;
	//MNU_DMNUDESC
	private String voceDescrizioneMenu;
	//MNU_DMNUTTIP
	private String tooltipVoceMenu;
	//MNU_DMNUACTN
	private String urlAction;
	//MNU_FMNUFATT
	private String flagEnabled;
	//MNU_PMNUORDE
	private int order;	
	
	public Menu(){
		super();
	}
	
	/* CONFIGURAZIONE MENU APPLICATIVO
	 * @param 
	 * @return Menu
	 */
	public Menu(int idMenu, int idParent, String applicazioni, int livelloMenu,
			String voceDescrizioneMenu, String tooltipVoceMenu,
			String urlAction, String flagEnabled, int order) {
		super();
		this.idMenu = idMenu;
		this.idParent = idParent;
		this.applicazioni = applicazioni;
		this.livelloMenu = livelloMenu;
		this.voceDescrizioneMenu = voceDescrizioneMenu;
		this.tooltipVoceMenu = tooltipVoceMenu;
		this.urlAction = urlAction;
		this.flagEnabled = flagEnabled;
		this.order = order;
	}


	public int getIdMenu() {
		return idMenu;
	}

	public void setIdMenu(int idMenu) {
		this.idMenu = idMenu;
	}

	public int getIdParent() {
		return idParent;
	}

	public void setIdParent(int idParent) {
		this.idParent = idParent;
	}

	public String getApplicazioni() {
		return applicazioni;
	}

	public void setApplicazioni(String applicazioni) {
		this.applicazioni = applicazioni;
	}

	public int getLivelloMenu() {
		return livelloMenu;
	}

	public void setLivelloMenu(int livelloMenu) {
		this.livelloMenu = livelloMenu;
	}

	public String getVoceDescrizioneMenu() {
		return voceDescrizioneMenu;
	}

	public void setVoceDescrizioneMenu(String voceDescrizioneMenu) {
		this.voceDescrizioneMenu = voceDescrizioneMenu;
	}

	public String getTooltipVoceMenu() {
		return tooltipVoceMenu;
	}

	public void setTooltipVoceMenu(String tooltipVoceMenu) {
		this.tooltipVoceMenu = tooltipVoceMenu;
	}

	public String getUrlAction() {
		return urlAction;
	}

	public void setUrlAction(String urlAction) {
		this.urlAction = urlAction;
	}

	public String getFlagEnabled() {
		return flagEnabled;
	}

	public void setFlagEnabled(String flagEnabled) {
		this.flagEnabled = flagEnabled;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	

	
}
