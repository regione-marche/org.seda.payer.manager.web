package org.seda.payer.manager.riconciliazionemt.actions;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.HtmlAction;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import com.seda.payer.pgec.webservice.ente.dati.Ente;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailRequest;
import com.seda.payer.pgec.webservice.ente.dati.EnteDetailResponse;

public class UploadAction extends HtmlAction implements Filter {
	
	private static final long serialVersionUID = 1L;
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		String messaggio = "";
		String utenteEnte = request.getParameter("tx_UtenteEnte");
		int fileCaricati = 0;
		
		ServletContext context = request.getSession(false).getServletContext();
		PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		
		String rootPathGDC = null;
		
		if(configuration == null){
			messaggio = "errore nel recupero della configurazione";
		}else{
			MAFRequest mafReq = new MAFRequest(request);
			String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
			UserBean userBean = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
			String templateName = userBean.getTemplate(applicationName);

			rootPathGDC = configuration.getProperty(PropertiesPath.directoryGDC.format(templateName));
		}

		if (utenteEnte == null || utenteEnte.isEmpty()) {
			messaggio = "Scegliere l'ente prima di caricare il file";
		}else{
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			if (isMultipart) {
				String csoccsoc = utenteEnte.substring(0, 5);
				String cutecute = utenteEnte.substring(5, 10);
				String chiaveEnte = utenteEnte.substring(10);

				EnteDetailResponse enteRes = WSCache.enteServer.getEnte(new EnteDetailRequest(csoccsoc, cutecute, chiaveEnte), request);

				Ente ente = enteRes.getEnte();
				
				if(ente==null || ente.getFlagGDC()==null || !ente.getFlagGDC().equals("Y")){
					messaggio = "La funzionalità non è attiva per l'ente";
				}else{
					String entePathGDC = ente.getCFiscalePIva();
					if(ente.getDirGDCInput()!=null && !ente.getDirGDCInput().equals("")){
						entePathGDC = ente.getDirGDCInput();
					}
					File folderGDC = new File(rootPathGDC, entePathGDC);
					if(!folderGDC.exists() && !folderGDC.mkdirs()){
						messaggio = "Errore configurazione ente";
					}else{
						try {
							DiskFileItemFactory factory = new DiskFileItemFactory();
							ServletFileUpload sf = new ServletFileUpload(factory);
							List<FileItem> multifiles = sf.parseRequest(request);
							for (FileItem item: multifiles) {
								if (item.getName() != null && !item.getName().isEmpty()){
									fileCaricati++;
									File file = new File(folderGDC, item.getName());
									if (!file.exists()) {		
										item.write(file);
										messaggio = "File caricato con successo";
									} else {
										messaggio = "File già esistente";
									}
								}
							}
							if (fileCaricati <= 0) {
								messaggio = "Seleziona un file da caricare";
							}
						} catch (Exception e) {
							e.printStackTrace();
							messaggio = "Errore interno dell'applicazione";
						}
					}
				}
			}
		}
		request.setAttribute(MAFAttributes.CURRENT_APPLICATION,"riconciliazionemt");
		request.setAttribute(MAFAttributes.ACTIONS,"caricaGiornaleCassa.do");
		request.setAttribute("message", messaggio);
		request.setAttribute("upload", "1");
		String url = "caricaGiornaleCassa.do?";
		url += "message="+messaggio;
		url += "&upload=1";
		url += "&tx_UtenteEnte="+utenteEnte;
		//request.getRequestDispatcher(url).forward(request, response);
		response.sendRedirect(url);
	}


	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void init(FilterConfig paramFilterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Object service(HttpServletRequest paramHttpServletRequest)
			throws ActionException {
		// TODO Auto-generated method stub
		return null;
	}
}
