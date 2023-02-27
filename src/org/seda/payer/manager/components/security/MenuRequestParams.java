package org.seda.payer.manager.components.security;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")

/**
 * Questo bean contiene gli ID dei 3 livelli di menu
 *
 */
public class MenuRequestParams  implements Serializable{

	private String idMenuLivelloUno = null;
	private String idMenuLivelloDue = null;
	private String idMenuLivelloTre = null;
	
	public String getIdMenuLivelloUno() {
		return idMenuLivelloUno;
	}
	public void setIdMenuLivelloUno(String idMenuLivelloUno) {
		this.idMenuLivelloUno = idMenuLivelloUno;
	}
	public String getIdMenuLivelloDue() {
		return idMenuLivelloDue;
	}
	public void setIdMenuLivelloDue(String idMenuLivelloDue) {
		this.idMenuLivelloDue = idMenuLivelloDue;
	}
	public String getIdMenuLivelloTre() {
		return idMenuLivelloTre;
	}
	public void setIdMenuLivelloTre(String idMenuLivelloTre) {
		this.idMenuLivelloTre = idMenuLivelloTre;
	}

	/**
	 * Questo metodo aggiunge nella request, se non presenti, 
	 * i parametri necessari alla costruzione del menu - 
	 * I parametri sono gli ID dei tre livelli di menu contenuti
	 * in una istanza della classe "MenuRequestParams".
	 * 
	 * - 1) Se l'utente ha fatto click su una voce di menù di livello 1 0 2 
	 * non faccio nulla.
	 * - 2) Se l'utente ha fatto click su una voce di menù di livello 3 allora 
	 * sono presenti tutti e 3 gli ID e mi limito a salvarli in sessione.
	 * - 3) Se l'utente non ha fatto click su una voce di menù cerco di trovare
	 * gli ID dei 3 livelli dalla request: se li trovo li metto in sessione e 
	 * li utilizzo altrimenti utilizzo quelli messi in sessione in precedenza
	 */
	public static void set(HttpServletRequest hreq)
	{
		MenuRequestParams menuRequestParams = null;
		MenuRequestParams menuRequestParamsNew = null;
		HttpSession session = hreq.getSession();
		//hreq.getServletPath()  ==> /rendicontazione/ricercaFlussi.do

		/*
		 * Se la request contiene i parametri relativi al menu
		 * e si tratta di un terzo livello li metto in sessione
		 */
		String livelloMenu = hreq.getParameter(MenuKey.PARAM_LIVELLO);
		menuRequestParams = (MenuRequestParams) session.getAttribute(MenuKey.PARAM_MENU);
		/*
		 * Se "livelloMenu" non è nullo vuol dire che l'utente ha cliccato 
		 * su una voce di menu
		 */
		if(livelloMenu != null)
		{
			if(menuRequestParams != null) session.removeAttribute(MenuKey.PARAM_MENU);
			if(livelloMenu.trim().equals("3"))
			{
				menuRequestParams = new MenuRequestParams();
				menuRequestParams.setIdMenuLivelloTre(hreq.getParameter(MenuKey.PARAM_ID).trim());
				menuRequestParams.setIdMenuLivelloUno(hreq.getParameter(MenuKey.PARAM_ID1).trim());
				menuRequestParams.setIdMenuLivelloDue(hreq.getParameter(MenuKey.PARAM_ID2).trim());
				session.setAttribute(MenuKey.PARAM_MENU, menuRequestParams);
			}
		}
		/*
		 * Se la request non contiene i parametri relativi al menu vuol dire
		 * che l'utente non ha cliccato su una voce di menu.
		 * 
		 * In tal caso cerco di ricavare gli ID dei tre livelli dalla request: 
		 * 1) se li trovo li uso e li metto in sessione 
		 * 2) altrimenti uso quelli precedenti
		 */
		else
		{
			menuRequestParamsNew = find(hreq);
			if (menuRequestParamsNew != null) 
			{
				/*
				 * Metto in sessione gli ID dei 3 livelli trovati
				 */
				if(menuRequestParams != null) session.removeAttribute(MenuKey.PARAM_MENU);
				session.setAttribute(MenuKey.PARAM_MENU, menuRequestParamsNew);
			}
			/*
			 * Setto gli ID nella request utilizzando i dati appena messi in sessione
			 * (se ho trovato gli ID) oppure quelli già presenti in sessione (se non ho trovato gli ID)
			 */
			menuRequestParams = (menuRequestParamsNew != null ? menuRequestParamsNew : menuRequestParams);
			if(menuRequestParams != null)
			{
				hreq.setAttribute(MenuKey.PARAM_LIVELLO, "3");
				hreq.setAttribute(MenuKey.PARAM_ID,menuRequestParams.getIdMenuLivelloTre());
				hreq.setAttribute(MenuKey.PARAM_ID1, menuRequestParams.getIdMenuLivelloUno());
				hreq.setAttribute(MenuKey.PARAM_ID2, menuRequestParams.getIdMenuLivelloDue());
			}
		}
	}

	/**
	 * Restituisce una istanza di MenuRequestParam 
	 * che corrisponde alla request pervenuta oppure
	 * null se non si trova corrispondenza
	 * 
	 * @param hreq
	 * @return
	 */
	private static MenuRequestParams find(HttpServletRequest hreq)
	{
		HttpSession session = hreq.getSession();
		MenuRequestParams menuItem = null;
		List<Menu> menuLivelloTre = null;
		List<Menu> menuLivelloDue = null;
		int id1 = 0;
		int id2 = 0;
		int id3 = 0;

		String servletPath = hreq.getServletPath();
		menuLivelloTre = (List<Menu>)session.getAttribute(MenuKey.MENU_LIVELLO_TRE);
		if (menuLivelloTre != null)
		{
			Menu m3 = CollectionMenu.find(menuLivelloTre,servletPath);
			if (m3 != null)
			{
				id3 = m3.getIdMenu();
				id2 = m3.getIdParent();
				menuLivelloDue = (List<Menu>)session.getAttribute(MenuKey.MENU_LIVELLO_DUE);
				if (menuLivelloDue != null)
				{
					Menu m2 = CollectionMenu.find(menuLivelloDue,id2);
					if (m2 != null)
					{
						id1 = m2.getIdParent();
						menuItem = new MenuRequestParams();
						menuItem.setIdMenuLivelloUno(String.valueOf(id1));
						menuItem.setIdMenuLivelloDue(String.valueOf(id2));
						menuItem.setIdMenuLivelloTre(String.valueOf(id3));
					}
				}
			}
		}
		return menuItem;
		
	}
	
	/**
	 * Questo metodo rimuove dalla request gli attributi necessari
	 * alla costruzione del menu
	 * 
	 * @param hreq
	 */
	public static void clear(HttpServletRequest hreq)
	{
		if (hreq != null)
		{
			hreq.setAttribute(MenuKey.PARAM_LIVELLO,"");
			hreq.setAttribute(MenuKey.PARAM_ID,"");
			hreq.setAttribute(MenuKey.PARAM_ID1,"");
			hreq.setAttribute(MenuKey.PARAM_ID2,"");
		}
	}
}
