package org.seda.payer.manager.riconciliazionenn.actions;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.Field;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.WSCache;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.data.spi.PageInfo;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaIUVFlussoNodoRequest;
import com.seda.payer.pgec.webservice.commons.dati.RecuperaIUVFlussoNodoResponse;
import com.seda.payer.pgec.webservice.commons.srv.FaultType;

public class DettaglioIUVFlussoAction extends BaseRiconciliazioneNodoAction 
{
	private static final long serialVersionUID = 1L;
	/*
	private int rowsPerPage;
	private int pageNumber;
	private String order;
	*/
	private Long keyQuadratura = null;
	
	public Object service(HttpServletRequest request) throws ActionException {
		HttpSession session = request.getSession();
		tx_SalvaStato(request);
		super.service(request);
		
		String appoQuadratura = (String) request.getAttribute("idquad");
		if (appoQuadratura != null && appoQuadratura != "") {
			try {
				setParametersSession(request, session);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		String idquad = (String) session.getAttribute("flusso_idquad");
		keyQuadratura = new Long(idquad);
		/*
		rowsPerPage = ((String)request.getAttribute("rowsPerPage") == null) ? getDefaultListRows(request) : Integer.parseInt((String)request.getAttribute("rowsPerPage"));
		pageNumber = ((String)request.getAttribute("pageNumber") == null) || (((String)request.getAttribute("pageNumber")).equals("")) ? 1 : Integer.parseInt((String)request.getAttribute("pageNumber"));	
		order = request.getParameter("order");
		 */
		if (request.getAttribute("indietro") != null){
			request.setAttribute("lista_iuv_di_flusso", session.getAttribute("lista_iuv"));
			request.setAttribute("lista_iuv_di_flusso.pageInfo", session.getAttribute("lista_iuv.pageInfo"));
		}
		
		switch(getFiredButton(request)) {
			case TX_BUTTON_CERCA:
				salvaFiltri(request, session);
				try {
					RecuperaIUVFlussoNodoResponse IUVFlussoNodo = getListaIUVFlussoNodo(request);
					com.seda.payer.pgec.webservice.commons.dati.PageInfo pageInfoPgec = IUVFlussoNodo.getPageInfo();
					PageInfo pageInfo = this.getMovimentiPageInfo(pageInfoPgec);
					
					if (IUVFlussoNodo.getResponse().getRetCode().getValue() != "00") {
						setFormMessage("dettaglioIUVFlussoForm", "Errore generico - Impossibile recuperare i dati", request);
					} else {
						if(pageInfo != null)
						{
							if(pageInfo.getNumRows() > 0)
							{
								session.setAttribute("lista_iuv", IUVFlussoNodo.getIUVListaXml());
								session.setAttribute("lista_iuv.pageInfo", pageInfo);
								request.setAttribute("lista_iuv_di_flusso", IUVFlussoNodo.getIUVListaXml());
								request.setAttribute("lista_iuv_di_flusso.pageInfo", pageInfo);
							}
							else { 
								request.setAttribute("lista_iuv_di_flusso", null);
								setFormMessage("dettaglioIUVFlussoForm", Messages.NO_DATA_FOUND.format(), request);
							}
						}
						else { 
							setFormMessage("dettaglioIUVFlussoForm", "Errore generico - Impossibile recuperare i dati", request);
						}
					}
				} catch(Exception e) {
					setErrorMessage(e.getMessage());
					e.printStackTrace();
				}
				break;
				
			case TX_BUTTON_RESET:
				session.setAttribute("lista_iuv", "");
				request.setAttribute("lista_iuv_di_flusso", "");
				request.setAttribute("codiceIUV", "");
				request.setAttribute("statoQuadratura", "");
				request.setAttribute("importoUIV_da", "");
				request.setAttribute("importoUIV_a", "");
				/*
				request.setAttribute("codiceEsito", "");
				request.setAttribute("codiceQuadratura", "");
				*/

			default:
				break;
		}
		
		return null; 
	}
	
	protected RecuperaIUVFlussoNodoResponse getListaIUVFlussoNodo(HttpServletRequest request) throws FaultType, RemoteException
	{
		PropertiesTree configuration; 
		configuration = (PropertiesTree)(request.getSession().getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE));

		int rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));

		if (request.getAttribute("rowsPerPage")!=null&&request.getAttribute("rowsPerPage").toString().indexOf(";")==-1)  {
			rowsPerPage = Integer.parseInt(request.getAttribute("rowsPerPage").toString());
		}

		//BUG da sistemare:
		//  viene inserito sul form il valore rowsPerPage == -1
		//  e funziona male il meccanismo della paginazione ...
		if(rowsPerPage == -1) {
			rowsPerPage = Integer.parseInt(configuration.getProperty(PropertiesPath.defaultListRows.format()));
		}
       
		int pageNumber = (request.getAttribute("pageNumber") == null) || (request.getAttribute("pageNumber").equals("")) ? 1 : Integer.parseInt(request.getAttribute("pageNumber").toString());

		String order = (request.getAttribute("order") == null) ? "" : request.getAttribute("order").toString();
		
		
		RecuperaIUVFlussoNodoRequest recuperaIUVFlussoNodoRequest = new RecuperaIUVFlussoNodoRequest();
		recuperaIUVFlussoNodoRequest.setOrder(order);
		recuperaIUVFlussoNodoRequest.setPageNumber(pageNumber);
		recuperaIUVFlussoNodoRequest.setRowsPerPage(rowsPerPage);
		recuperaIUVFlussoNodoRequest.setKeyQuadratura(keyQuadratura);
		recuperaIUVFlussoNodoRequest.setCodiceIUV(request.getParameter("codiceIUV") == null ? "" : request.getParameter("codiceIUV"));
		recuperaIUVFlussoNodoRequest.setStatoQuadratura(request.getParameter("statoQuadratura") == null ? "" : request.getParameter("statoQuadratura"));
		/*
		recuperaIUVMovimentoNodoRequest.setCodiceEsito(codiceEsito);
		recuperaIUVMovimentoNodoRequest.setCodiceQuadratura(codiceQuadratura);
		*/
		String importoUIV_da = (request.getParameter("importoUIV_da") == null) ? "" : request.getParameter("importoUIV_da");
		String importoUIV_a = (request.getParameter("importoUIV_a") == null) ? "" : request.getParameter("importoUIV_a");
		//inzio LP PG200360
		importoUIV_da = importoUIV_da.replaceAll("\\.", "");
		importoUIV_da = importoUIV_da.replaceAll(",", ".");
		importoUIV_a = importoUIV_a.replaceAll("\\.", "");
		importoUIV_a = importoUIV_a.replaceAll(",", ".");
		//fine LP PG200360
		recuperaIUVFlussoNodoRequest.setImpPagamentoDa(importoUIV_da);
		recuperaIUVFlussoNodoRequest.setImpPagamentoA(importoUIV_a);
		return WSCache.commonsServer.recuperaIUVFlussoNodo(recuperaIUVFlussoNodoRequest, request);
	}

	private void setParametersSession(HttpServletRequest request, HttpSession session) throws ParseException{
		String idflusso = (String)request.getAttribute("idflusso");
		String societa = (String)request.getAttribute("societa");
		String data = (String)request.getAttribute("data");
		String numrt = (String)request.getAttribute("numrt");
		String iuvsca = (String)request.getAttribute("iuvsca");
		String idquad = (String)request.getAttribute("idquad");
		String flusso_errore = (String)request.getAttribute("flusso_errore") != "" ? (String)request.getAttribute("flusso_errore") : "-";

		data = data.substring(0, 10);
		SimpleDateFormat parser=new SimpleDateFormat("yyyy-MM-dd");
		Date date = parser.parse(data);
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		String formattedDate = formatter.format(date);

		session.setAttribute("flusso_id", idflusso);
		session.setAttribute("flusso_soc", societa);
		session.setAttribute("flusso_data", formattedDate);
		session.setAttribute("flusso_numrt", numrt);
		session.setAttribute("flusso_iuvsca", iuvsca);
		session.setAttribute("flusso_idquad", idquad);
		session.setAttribute("flusso_errore", flusso_errore);
	}
	
	private void salvaFiltri(HttpServletRequest request, HttpSession session) {
		try {
			System.out.println("Salvataggio filtri...");
			session.setAttribute("codiceIUV", request.getAttribute("codiceIUV"));
			session.setAttribute("statoQuadratura", request.getAttribute("statoQuadratura"));
			session.setAttribute("importoUIV_da", request.getAttribute("importoUIV_da"));
			session.setAttribute("importoUIV_a", request.getAttribute("importoUIV_a"));
			/*
			session.setAttribute("codiceEsito", request.getAttribute("codiceEsito"));
			session.setAttribute("codiceQuadratura", request.getAttribute("codiceQuadratura"));
			*/
		} catch (Exception e) {
			System.out.println("Errore salvataggio filtri : ");
			e.getMessage();
		}
	}
	
}
