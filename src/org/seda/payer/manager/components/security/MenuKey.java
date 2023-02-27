package org.seda.payer.manager.components.security;

public class MenuKey {

	/**
	 * E' la key del bean che si trova in sessione e che
	 * contiene i valori necessari a ricostruire il menu.
	 */
	public static final String PARAM_MENU = "menu_request_params";
	/**
	 * Livello del menu (1, 2 o 3)
	 */
	public static final String PARAM_LIVELLO = "mnLivello";
	/**
	 * "Id" della voce corrente del menu che può
	 * appartenere ad uno qualsiasi dei livelli.
	 */
	public static final String PARAM_ID = "mnId";
	/**
	 * Se presente, rappresenta la voce di menu di primo
	 * livello selezionata.
	 */
	public static final String PARAM_ID1 = "mnId1";
	/**
	 * Se presente, rappresenta la voce di menu di secondo
	 * livello selezionata.
	 */
	public static final String PARAM_ID2 = "mnId2";
	/**
	 * E' la chiave della collezione di voci di menu di 
	 * primo livello che si trova in sessione dopo un login
	 * effettuato correttamente
	 */
	public static final String MENU_LIVELLO_UNO = "menuLivelloUno";
	/**
	 * E' la chiave della collezione di voci di menu di 
	 * secondo livello che si trova in sessione dopo un login
	 * effettuato correttamente
	 */
	public static final String MENU_LIVELLO_DUE = "menuLivelloDue";
	/**
	 * E' la chiave della collezione di voci di menu di 
	 * terzo livello che si trova in sessione dopo un login
	 * effettuato correttamente
	 */
	public static final String MENU_LIVELLO_TRE = "menuLivelloTre";

	private MenuKey() {}
}
