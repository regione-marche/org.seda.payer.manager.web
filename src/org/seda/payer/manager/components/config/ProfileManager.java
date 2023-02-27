package org.seda.payer.manager.components.config;

import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.security.UserBean;


public class ProfileManager {
	
	private static final String SOCIETA_DDL_DISABLED = "societaDdlDisabled";
	private static final String PROVINCIA_DDL_DISABLED = "provinciaDdlDisabled";
	private static final String UTENTE_DDL_DISABLED = "utenteDdlDisabled";
	private static final String ENTE_DDL_DISABLED = "enteDdlDisabled";
	private static final String UTENTE_ABILITATO_RICONCILIAZIONE = "utenteAbilitatoRiconciliazione";
	public enum Profile {
		AMMI, AMSO, AMUT, AMEN
	}
	
	public static boolean isSocietaDdlDisabled(HttpSession session) {
		return (Boolean)session.getAttribute(SOCIETA_DDL_DISABLED);
	}
	public static void setSocietaDdlDisabled(HttpSession session, boolean societaDdlDisabled) {
		session.setAttribute(SOCIETA_DDL_DISABLED, societaDdlDisabled);
	}
	public static boolean isProvinciaDdlDisabled(HttpSession session) {
		return (Boolean)session.getAttribute(PROVINCIA_DDL_DISABLED);
	}
	public static void setProvinciaDdlDisabled(HttpSession session, boolean provinciaDdlDisabled) {
		session.setAttribute(PROVINCIA_DDL_DISABLED, provinciaDdlDisabled);
	}
	public static boolean isUtenteDdlDisabled(HttpSession session) {
		return (Boolean)session.getAttribute(UTENTE_DDL_DISABLED);
	}
	public static void setUtenteDdlDisabled(HttpSession session, boolean utenteDdlDisabled) {
		session.setAttribute(UTENTE_DDL_DISABLED, utenteDdlDisabled);
	}
	public static boolean isEnteDdlDisabled(HttpSession session) {
		return (Boolean)session.getAttribute(ENTE_DDL_DISABLED);
	}
	public static void setEnteDdlDisabled(HttpSession session, boolean enteDdlDisabled) {
		session.setAttribute(ENTE_DDL_DISABLED, enteDdlDisabled);
	}
	public static boolean isUtenteAbilitatoRiconciliazione(HttpSession session) {
		return (Boolean)session.getAttribute(UTENTE_ABILITATO_RICONCILIAZIONE);
	}
	public static void setUtenteAbilitatoRiconciliazione(HttpSession session, boolean enteDdlDisabled) {
		session.setAttribute(UTENTE_ABILITATO_RICONCILIAZIONE, enteDdlDisabled);
	}
	
	public static void setDdlPermissions(HttpSession session, UserBean portalUserBean) {
//		1)Amministratore centro servizi C.S.I. (AMMI / livello gerarchico 0): tutti e 4 i drop-down list abilitati.
//		2)Amministratore societa (AMSO / livello gerarchico 1): Società disabilitato (impostato di default con la descrizione corrispondente alla chiave USR_CSOCCSOC della tabella PYUSRTB), Provincia, Utente e Ente abilitati.
//		3)Amministratore utente (AMUT / livello gerarchico 2): Società e Utente disabilitati (impostati di default con le descrizioni corrispondenti alle chiavi USR_CSOCCSOC e USR_CUTECUTE della tabella PYUSRTB), Ente e Provincia abilitati.
//		4)Amministratore ente/consorzio (AMEN / livello gerarchico 3): tutti e 4 drop-down list disabilitati (impostati di default con le descrizioni corrispondenti alle chiavi USR_CSOCCSOC, USR_CUTECUTE e USR_KANEKENT_CON della tabella PYUSRTB).
		session.setAttribute("tx_societa", portalUserBean.getCodiceSocieta());
		session.setAttribute("tx_utente", portalUserBean.getCodiceUtente());
		session.setAttribute("tx_ente", portalUserBean.getChiaveEnteConsorzio());
		setUtenteAbilitatoRiconciliazione(session, portalUserBean.getAzioniPerRiconciliazioneManualeEnabled());
		switch (Profile.valueOf(portalUserBean.getProfile())) {
			case AMMI:
				setSocietaDdlDisabled(session, false);	
				setProvinciaDdlDisabled(session, false);	
				setUtenteDdlDisabled(session, false);	
				setEnteDdlDisabled(session, false);
				break;
			case AMSO:
				setSocietaDdlDisabled(session, true);	
				setProvinciaDdlDisabled(session, false);	
				setUtenteDdlDisabled(session, false);	
				setEnteDdlDisabled(session, false);
				break;
			case AMUT:
				setSocietaDdlDisabled(session, true);	
				setProvinciaDdlDisabled(session, false);	
				setUtenteDdlDisabled(session, true);	
				setEnteDdlDisabled(session, false);
				break;
			case AMEN:
				setSocietaDdlDisabled(session, true);	
				setProvinciaDdlDisabled(session, true);	
				setUtenteDdlDisabled(session, true);	
				setEnteDdlDisabled(session, true);
				break;
		}
	}
}
