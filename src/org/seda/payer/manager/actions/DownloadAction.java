package org.seda.payer.manager.actions;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.HtmlAction;
/**
 * @author marco.montisano
 */
public class DownloadAction extends HtmlAction {

	private static final long serialVersionUID = 1L;

	public Object service(HttpServletRequest request) throws ActionException {
		
		//PG190360
		String name="", path="";
		name = request.getParameter("name")!=null?(String)request.getParameter("name"):"";
		path = request.getParameter("path")!=null?(String)request.getParameter("path"):"";
		if(name.substring(name.lastIndexOf(".")+1).equals("zip")) {
			path += "zip";
		}
		request.setAttribute("name", name);
		request.setAttribute("path", path + "/" + name);
		//FINE PG190360
		
		return null;
	}
}