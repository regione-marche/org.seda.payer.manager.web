package org.seda.payer.manager.ecmanager.actions.util;

/**
 * Questa interfaccia dpovrà essere implementata da tutti quei controller che saranno utilizzatio per  generare i servizi 
 * RESTFUL, in quanto chi prende in pasto la risposta si aspetta un dato organizzato secondo questa interfaccia
 * @author lmontesi
 *
 */
public interface WsBaseInterface {
	/* Valorizzazione del nome del modello in output del RESTFUL*/
	void setModelName(String modelName);

	/* stato della risposta*/
	void setStatus(String status);

	/* Valorizzato nel caso di liste e indica i campi di ordinamento*/
	void setOrderBy(String orderBy);
	
	/* */
	void setValue(String value);
	
	
	
}

