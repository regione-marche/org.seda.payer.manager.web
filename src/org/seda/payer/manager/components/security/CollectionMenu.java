/**
 * 
 */
package org.seda.payer.manager.components.security;

import java.io.StringReader;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.ws.WSCache;

import com.seda.payer.pgec.webservice.menu.dati.MenuGetRequest;
import com.seda.payer.pgec.webservice.menu.dati.MenuGetResponse;
import com.sun.rowset.WebRowSetImpl;



/**
 * @author m.raimondo
 *
 */
public class CollectionMenu {

	private List <Menu> menuCollection;
	
	//inizio LP PG21XX04 Leak
	//Nota. I potenziali leak qui segnalati, sono eliminati nel metodo che usa i WebRowSet qui costruiti.
	//fine LP PG21XX04 Leak
	public static WebRowSet[] getMenuLivello123(Long chiaveutente, HttpServletRequest request) {
		WebRowSet[] menus = null;
		WebRowSet menuLivelloUnoWrs = null;
		WebRowSet menuLivelloDueWrs = null;
		WebRowSet menuLivelloTreWrs = null;
		String menuLivelloUno = null;
		String menuLivelloDue = null;
		String menuLivelloTre = null;
		StringReader strRdrLivelloUno = null;
		StringReader strRdrLivelloDue = null;
		StringReader strRdrLivelloTre = null;		
				
		MenuGetResponse menuGetResponse = new MenuGetResponse();
		MenuGetRequest menuGetRequest = new MenuGetRequest();
		menuGetRequest.setChiaveUtente(chiaveutente);
		try {
			menuGetResponse = WSCache.menuServer.getMenu(menuGetRequest, request);
			menuLivelloUno = menuGetResponse.getResponse().getMenu_livello_uno();			
			menuLivelloDue = menuGetResponse.getResponse().getMenu_livello_due();
			menuLivelloTre = menuGetResponse.getResponse().getMenu_livello_tre();
			
			strRdrLivelloUno = new StringReader(menuLivelloUno);
			strRdrLivelloDue = new StringReader(menuLivelloDue);
			strRdrLivelloTre = new StringReader(menuLivelloTre);
			
			menuLivelloUnoWrs = new WebRowSetImpl();
			menuLivelloDueWrs = new WebRowSetImpl();
			menuLivelloTreWrs = new WebRowSetImpl();
			
			menuLivelloUnoWrs.readXml(strRdrLivelloUno);
			menuLivelloDueWrs.readXml(strRdrLivelloDue);
			menuLivelloTreWrs.readXml(strRdrLivelloTre);
			
			menus = new WebRowSet[3];
			menus[0] = menuLivelloUnoWrs;
			menus[1] = menuLivelloDueWrs;
			menus[2] = menuLivelloTreWrs;

		} catch (com.seda.payer.pgec.webservice.srv.FaultType e) {
			menus = null;
			e.printStackTrace();
		} catch (RemoteException e) {
			menus = null;
			e.printStackTrace();
		} catch (SQLException e) {
			menus = null;
			e.printStackTrace();
		}
		return menus;

	}


	/**
	 * 
	 */
	public CollectionMenu() {
		// TODO Auto-generated constructor stub
	}
	
	
	public CollectionMenu (WebRowSet wrs) throws SQLException {
		if (wrs == null)
    		return;
		else{
			setMenuCollection(new ArrayList<Menu>());
			while (wrs.next())
			{
				Menu menu = new Menu(); 			
				menu.setVoceDescrizioneMenu(wrs.getString("MNU_DMNUDESC"));
				menu.setIdMenu(wrs.getInt("MNU_PMNUIDMN"));
				menu.setIdParent(wrs.getInt("MNU_PMNUPRNT"));
				menu.setApplicazioni(wrs.getString("MNU_CMNUAPPL"));
				menu.setLivelloMenu(wrs.getInt("MNU_NMNULIVE"));
				menu.setVoceDescrizioneMenu(wrs.getString("MNU_DMNUDESC"));
				menu.setTooltipVoceMenu(wrs.getString("MNU_DMNUTTIP"));
				menu.setUrlAction(wrs.getString("MNU_DMNUACTN"));
				menu.setFlagEnabled(wrs.getString("MNU_FMNUFATT"));
				menu.setOrder(wrs.getInt("MNU_PMNUORDE"));
				menuCollection.add(menu);
			}
		}
		//inizio LP PG21XX04 Leak
    	try {
    		if(wrs != null) {
    			wrs.close();
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
		}
		//fine LP PG21XX04 Leak
	}

	/**
	 * Restituisce la voce di menu che corrisponde
	 * ad un dato ServletPath o null se non lo trova
	 * 
	 * @param servletPath
	 * @return
	 */
	public static Menu find(List<Menu> menuCollection, String servletPath)
	{
		Menu menuItem = null;
		
		if (menuCollection == null 
				|| servletPath == null
				|| menuCollection.isEmpty()
				|| servletPath.equals("")) return null;
		
		Iterator<Menu> itr = menuCollection.iterator();
		while(itr.hasNext())
		{
			Menu m = itr.next();
			String urlAction = m.getUrlAction();
			if (urlAction != null && urlAction.indexOf(servletPath) > 0)
			{
				menuItem = m;
				break;
			}
		}
		return menuItem;
	}
	
	/**
	 * Restituisce la voce di menu che corrisponde
	 * ad un dato Id o null se non lo trova
	 * 
	 * @param id
	 * @return
	 */
	public static Menu find(List<Menu> menuCollection,int id)
	{
		Menu menuItem = null;
		
		if (menuCollection == null 
				|| menuCollection.isEmpty()
				|| id <= 0) return null;

		Iterator<Menu> itr = menuCollection.iterator();
		while(itr.hasNext())
		{
			Menu m = itr.next();
			if (m.getIdMenu() == id)
			{
				menuItem = m;
				break;
			}
		}
		return menuItem;
	}
	

	public List<Menu> getMenuCollection() {
		return menuCollection;
	}
	public void setMenuCollection(List<Menu> menuCollection) {
		this.menuCollection = menuCollection;
	}

}
