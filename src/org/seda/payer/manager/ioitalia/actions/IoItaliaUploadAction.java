package org.seda.payer.manager.ioitalia.actions;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;


public class IoItaliaUploadAction extends IoItaliaBaseManagerAction implements Filter {


//	private String soc;
//	private String ente;
//	private String codSoc;
	private static final long serialVersionUID = 1L;
	private static final long MAX_FILE_SIZE = 1024 * 1024 * 100;
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain arg2)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String resultMex = "";
		String rootPathCSV = null;
		String rootScartati = null;
		int fileCaricati = 0;
//		soc = (String) request.getSession().getAttribute("tx_societa_s");
//		ente = (String) request.getSession().getAttribute("tx_ente_s");
		try {
			service(request);
		} catch (ActionException e1) {
			resultMex = "Errore Connessione";
			e1.printStackTrace();
		}

		ServletContext context = request.getSession().getServletContext();
		PropertiesTree configuration = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);

		if (configuration == null) {
			resultMex = "Errore nel recupero della configurazione";

		} else {
			MAFRequest mafReq = new MAFRequest(request);
			String applicationName = (String) mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
			UserBean userBean = (UserBean) request.getSession().getAttribute(SignOnKeys.USER_BEAN);
			String templateName = userBean.getTemplate(applicationName);

			rootPathCSV = configuration.getProperty(PropertiesPath.directoryIoItaliaCSVValidi.format(templateName));
			rootScartati = configuration.getProperty(PropertiesPath.directoryIoItaliaCSVScartati.format(templateName));

			try {
				File folderCSV = new File(rootPathCSV);
				if (!folderCSV.exists() && !folderCSV.mkdirs()) {
					resultMex = "Errore nel recupero directory dalla configurazione";
				} else {
					DiskFileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload sf = new ServletFileUpload(factory);
					List<FileItem> multifiles = sf.parseRequest(request);

					for (FileItem item : multifiles) {
						if (item.getName() != null && !item.getName().isEmpty()) {
							fileCaricati++;
							
							LocalDateTime ldt = LocalDateTime.now();
							String etichetta = ldt.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS")).concat(item.getName());
							
							// check estensione file (solo CSV ammesso) && check estensioni multiple
							if (getEstensioneFile(item.getName()).equalsIgnoreCase("csv") && item.getName().split("[.]").length == 2) {	
								
								// check dimensione file
								if(item.getSize() <= MAX_FILE_SIZE) {
									File file = new File(folderCSV, etichetta);
								        item.write(file);
								        resultMex = "File caricato con successo";							
								} else {
									File file = new File(rootScartati, etichetta);
									item.write(file);
									resultMex = "Il file &egrave; troppo grande: 50MB superati";
								}						
							} else if (!getEstensioneFile(item.getName()).equalsIgnoreCase("csv")) {								
								File file = new File(rootScartati, etichetta);
								item.write(file);
								resultMex = "Estensione errata: Il file non &egrave; un CSV";
							} else {
								File file = new File(rootScartati, etichetta);
								item.write(file);
								resultMex = "Non è ammesso il carattere \".\" nel nome del file";
							}
						}
					}
					if (fileCaricati <= 0) {
						resultMex = "Seleziona un file da caricare";
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				resultMex = "Errore interno dell'applicazione";
			}
		}

		request.setAttribute(MAFAttributes.CURRENT_APPLICATION, "ioitalia");
		request.setAttribute(MAFAttributes.ACTIONS, "ioitaliaupload.do");

//		request.getSession().setAttribute("fileName", fileName);
//		request.getSession().setAttribute("message", resultMex);
//		request.setAttribute("upload", "1");

//		if (fileName != null) {
//			// PropertiesTree configuration = (PropertiesTree)
//			// context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
//			MAFRequest mafReq = new MAFRequest(request);
//
//			String applicationName = (String) mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);
//			UserBean userBean = (UserBean) request.getSession().getAttribute(SignOnKeys.USER_BEAN);
//
//			String templateName = userBean.getTemplate(applicationName);
//
//			String folderCsv = configuration.getProperty(PropertiesPath.directoryIoItaliaCSV.format(templateName));
//			String rootScartati = configuration
//					.getProperty(PropertiesPath.directoryIoItaliaCSVScartati.format(templateName));
//			String rootAccettati = configuration
//					.getProperty(PropertiesPath.directoryIoItaliaCSVValidi.format(templateName));
//
//			File file = new File(folderCsv.concat(File.separatorChar + fileName));
//
//			resultMex = "File caricato correttamente";
//			File fileValido = new File(rootAccettati, file.getName());
//			FileUtils.copyFile(file, fileValido);
//
//			file.delete();
//		}
//		request.getSession().setAttribute("tx_societa_s", null);
//		request.getSession().setAttribute("tx_ente_s", null);
//		YLM PG22XX06 INI 
//		String url = "ioitaliaupload.do?mnId=155&mnLivello=3&tx_message=" + resultMex;
		String url = "ioitaliaupload.do?mnId=155&mnLivello=3";

		request.getSession().setAttribute("tx_message", resultMex);
		request.setAttribute("tx_message", resultMex);
//		YLM PG22XX06 FINE 		

		response.sendRedirect(url);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

	@Override
	public void destroy() {

	}

	@Override
	public Object service(HttpServletRequest request) throws ActionException {
		super.service(request);
		return null;
	}

	public String getEstensioneFile(String filename) {
		return filename.substring(filename.lastIndexOf(".") + 1, filename.length()).toLowerCase();
	}

}
