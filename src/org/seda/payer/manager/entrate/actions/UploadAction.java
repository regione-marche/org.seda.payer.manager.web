package org.seda.payer.manager.entrate.actions;  

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.HtmlAction;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;

public class UploadAction extends HtmlAction implements Filter {
	
	private static final long serialVersionUID = 1L;
	
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)servletRequest;
		HttpServletResponse response = (HttpServletResponse)servletResponse;
		String messaggio = "", rootPathCSV = null;
		String utenteEnte = request.getParameter("tx_UtenteEnte");
		int fileCaricati = 0;	

		String MAX_FILE_SIZE = "5000000"; // 5MB
		
		ServletContext context = request.getSession(false).getServletContext();
		PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		
		request.getSession().setAttribute("message", "");
		
		if(configuration == null){
			messaggio = "errore nel recupero della configurazione";
			
		} else {
			MAFRequest mafReq = new MAFRequest(request);
			String applicationName = (String)mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
			UserBean userBean = (UserBean)request.getSession().getAttribute(SignOnKeys.USER_BEAN);
			String templateName = userBean.getTemplate(applicationName);
			
			rootPathCSV = configuration.getProperty(PropertiesPath.directoryCSV.format(templateName));
			
			try {
				if (rootPathCSV == null ) {
					throw new Exception("Mancata configurazione della path directoryCSV"); 
				}
				File folderCSV = new File(rootPathCSV);
				if(!folderCSV.exists() && !folderCSV.mkdirs()){
					messaggio = "Errore configurazione ente";
				} else {
					DiskFileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload sf = new ServletFileUpload(factory);
					List<FileItem> multifiles = sf.parseRequest(request);
					for (FileItem item: multifiles) {
						if (item.getName() != null && !item.getName().isEmpty()){
							if (item.getName().length() > 54) {
								throw new Exception("Lunghezza nome file CSV errata: superiore a 50 caratteri"); 
							}
							// controlla la dimensione del file
							long maxFileSize = Optional.of(configuration)
								.map(p -> {
									try {
										return p.getProperty(PropertiesPath.maxSizeCSV.format(templateName), MAX_FILE_SIZE);
									} catch (Exception e) {
										return MAX_FILE_SIZE;
									}
								})
								.map(s -> new Long(s))
								.get().longValue();
							if (item.getSize() > maxFileSize ) {
								throw new Exception(String.format("Dimensione del file superiore a %s MB", Math.abs(maxFileSize/1000000) ));
							}
							fileCaricati++;
							if(getEstensioneFile(item.getName()).equals("csv")) {
								File file = new File(folderCSV, item.getName());
								if (!file.exists()) {
									item.write(file);
									messaggio = "File caricato con successo";
								} else {
									if(templateName.equalsIgnoreCase("aosta")) {
										File oldFile = new File(folderCSV, item.getName());
										file.renameTo(oldFile);
										oldFile.delete();
										item.write(file);
										messaggio = "File gi� esistente. Il file � stato sovrascritto";
									} else {
										messaggio = "File gi� esistente.";
									}
								}
							} else {
								messaggio = "Estensione errata: Il file non � un CSV";
							}
						}
					}
					if (fileCaricati <= 0) {
						messaggio = "Seleziona un file da caricare";
					}
				}		
			} catch (Exception e) {
				e.printStackTrace();
				messaggio = e.getMessage() != null ?  e.getMessage() : "Errore interno dell'applicazione";
			}
		}
		
		request.setAttribute(MAFAttributes.CURRENT_APPLICATION,"entrate");
		request.setAttribute(MAFAttributes.ACTIONS,"uploadDocumento.do");
		request.setAttribute("message", messaggio);
		request.getSession().setAttribute("message", messaggio);
		request.setAttribute("upload", "1");
		
		String url = "uploadDocumento.do?mnId=138&mnLivello=2";
		url += "message="+messaggio;
		url += "&upload=1";
		url += "&tx_UtenteEnte="+utenteEnte;
		
		response.sendRedirect(url);
	}
	
	public String getEstensioneFile(String filename) {
		return filename.substring(filename.lastIndexOf(".")+1, filename.length()).toLowerCase();
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
