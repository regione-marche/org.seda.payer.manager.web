package org.seda.payer.manager.configurazione.views;

import java.io.Serializable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.seda.payer.manager.views.BaseView;
/**
 * @author marco.montisano
 */
public class ParametriOttivoView extends BaseView implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String Name = "parametriottico";
	public static final String ActionName = "parametriOttico";
	private String companyCode;
	private String userCode;
	private String chiaveEnte;
	private String siglaProvincia;
	private String sorgenteImmagini;
	private String documento;
	private String relata;
	private String bollettino;
	private String quietanza;
	private String server;
	private String userServer;
	private String passwordServer;
	private String dirInputFlussiDati;
	private String dirOutputFlussiDati;
	private String dirInputFlussiImmagini;
	private String dirOutputFlussiImmagini;
	private String emailAmministratore;
	private String dirLogFlussi;
	private String dirImmagini;
	private String listaOttico;

	public ParametriOttivoView(ServletContext context, HttpServletRequest request) {
		super(context, request);
	}

	public ParametriOttivoView populate(HttpServletRequest request, String scope) {
		super.populate(request, scope);
		if (RichiestaCancScope.equals(scope)) {
			setValue("richiestacanc", request.getParameter("richiestacanc"));
			setValue(Name + "_companyCode", request.getParameter(Name + "_companyCode"));
			setValue(Name + "_userCode", request.getParameter(Name + "_userCode"));		  
			setValue(Name + "_chiaveEnte", request.getParameter(Name + "_chiaveEnte"));
			this.companyCode = getValue(Name + "_companyCode");
			this.userCode = getValue(Name + "_userCode");
			this.chiaveEnte = getValue(Name + "_chiaveEnte");
		} else if (AddScope.equals(scope)) {
			this.companyCode = getValue(Name + "_companyCode");
			this.userCode = getValue(Name + "_userCode");
			this.chiaveEnte = getValue(Name + "_chiaveEnte");
			super.setCodiceSocieta(this.companyCode);
			super.setCodiceUtente(this.userCode);
		} else if (EditScope.equals(scope)) {
			setValue(Name + "_companyCode", request.getParameter(Name + "_companyCode"));
			setValue(Name + "_userCode", request.getParameter(Name + "_userCode"));		  
			setValue(Name + "_chiaveEnte", request.getParameter(Name + "_chiaveEnte"));
			this.companyCode = getValue(Name + "_companyCode");
			this.userCode = getValue(Name + "_userCode");
			this.chiaveEnte = getValue(Name + "_chiaveEnte");
			super.setCodiceSocieta(this.companyCode);
			super.setCodiceUtente(this.userCode);
		} else if (CancelScope.equals(scope)) {
			this.companyCode = getValue(Name + "_companyCode");
			this.userCode = getValue(Name + "_userCode");
			this.chiaveEnte = getValue(Name + "_chiaveEnte");
		} else if (SaveScope.equals(scope)) {
			this.companyCode = getParam(Name + "_companyCode");
			this.userCode = getParam(Name + "_userCode");
			this.chiaveEnte = getParam(Name + "_chiaveEnte");
			this.sorgenteImmagini         = getParam(Name + "_sorgenteImmagini");       
			this.documento                = getParam(Name + "_documento");              
			this.relata                   = getParam(Name + "_relata");                 
			this.bollettino               = getParam(Name + "_bollettino");             
			this.quietanza                = getParam(Name + "_quietanza");              
			this.server                   = getParam(Name + "_server");                 
			this.userServer               = getParam(Name + "_userServer");             
			this.passwordServer           = getParam(Name + "_passwordServer");         
			this.dirInputFlussiDati       = getParam(Name + "_dirInputFlussiDati");     
			this.dirOutputFlussiDati      = getParam(Name + "_dirOutputFlussiDati");    
			this.dirInputFlussiImmagini   = getParam(Name + "_dirInputFlussiImmagini"); 
			this.dirOutputFlussiImmagini  = getParam(Name + "_dirOutputFlussiImmagini");
			this.dirLogFlussi             = getParam(Name + "_dirLogFlussi");           
			this.dirImmagini              = getParam(Name + "_dirImmagini");
			this.emailAmministratore      = getParam(Name + "_emailAmministratore");
		} else {
			this.userCode = getValue(Name + "_searchuserCode");
			this.companyCode = this.getCodiceSocieta();
			this.chiaveEnte = getValue(Name + "_searchchiaveEnte");
			this.sorgenteImmagini = getValue(Name + "_searchsorgenteImmagini");
			this.siglaProvincia = getValue(Name + "_siglaProvincia");
			super.setCodiceProvincia(this.siglaProvincia);
			super.setCodiceUtente(this.userCode);
		}
		return this;
	}

	public String getCompanyCode() {
		return companyCode != null ? companyCode : "";
	}

	public ParametriOttivoView setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
		request.setAttribute(Name + "_companyCode", this.companyCode);
		return this;
	}

	public String getUserCode() {
		return userCode != null ? userCode : "";
	}

	public ParametriOttivoView setUserCode(String userCode) {
		this.userCode = userCode;
		request.setAttribute(Name + "_userCode", this.userCode);
		super.setCodiceUtente(this.userCode);
		return this;
	}

	public String getChiaveEnte() {
		return chiaveEnte != null ? chiaveEnte : "";
	}

	public ParametriOttivoView setChiaveEnte(String chiaveEnte) {
		this.chiaveEnte = chiaveEnte;
		request.setAttribute(Name + "_chiaveEnte", this.chiaveEnte);
		return this;
	}

	public ParametriOttivoView setListaOttico(String listXml) {
		this.listaOttico = listXml;
		request.setAttribute("listaOttico", listXml);
		return this;
	}

	public ParametriOttivoView setListaOtticoPageInfo(com.seda.data.spi.PageInfo pageInfo) {
		super.setPageInfo(pageInfo);
		request.setAttribute("listaOttico.pageInfo", pageInfo);
		return this;
	}
	
	public String getListaOttico() {
		return listaOttico;
	}

	public void setEntetiposervizios(String listXml) {
		request.setAttribute("entetiposervizios", listXml);
	}

	public String getSorgenteImmagini() {
		return sorgenteImmagini;
	}

	public ParametriOttivoView setSorgenteImmagini(String sorgenteImmagini) {
		this.sorgenteImmagini = sorgenteImmagini;
		request.setAttribute(Name + "_sorgenteImmagini", sorgenteImmagini);
		return this;
	}

	public String getEmailAmministratore() {
		return emailAmministratore;
	}

	public ParametriOttivoView setEmailAmministratore(String emailAmministratore) {
		this.emailAmministratore = emailAmministratore;
		request.setAttribute(Name + "_emailAmministratore", emailAmministratore);
		return this;
	}

	public String getDocumento() {
		return documento;
	}

	public ParametriOttivoView setDocumento(String documento) {
		this.documento = documento;
		request.setAttribute(Name + "_documento", documento);
		return this;
	}

	public String getRelata() {
		return relata;
	}

	public ParametriOttivoView setRelata(String relata) {
		this.relata = relata;
		request.setAttribute(Name + "_relata", relata);
		return this;
	}

	public String getBollettino() {
		return bollettino;
	}

	public ParametriOttivoView setBollettino(String bollettino) {
		this.bollettino = bollettino;
		request.setAttribute(Name + "_bollettino", bollettino);
		return this;
	}

	public String getQuietanza() {
		return quietanza;
	}

	public ParametriOttivoView setQuietanza(String quietanza) {
		this.quietanza = quietanza;
		request.setAttribute(Name + "_quietanza", quietanza);
		return this;
	}

	public String getServer() {
		return server;
	}

	public ParametriOttivoView setServer(String server) {
		this.server = server;
		request.setAttribute(Name + "_server", server);
		return this;
	}

	public String getUserServer() {
		return userServer;
	}

	public ParametriOttivoView setUserServer(String userServer) {
		this.userServer = userServer;
		request.setAttribute(Name + "_userServer", userServer);
		return this;
	}

	public String getPasswordServer() {
		return passwordServer;
	}

	public ParametriOttivoView setPasswordServer(String passwordServer) {
		this.passwordServer = passwordServer;
		request.setAttribute(Name + "_passwordServer", passwordServer);
		return this;
	}

	public String getDirInputFlussiDati() {
		return dirInputFlussiDati;
	}

	public ParametriOttivoView setDirInputFlussiDati(String dirInputFlussiDati) {
		this.dirInputFlussiDati = dirInputFlussiDati;
		request.setAttribute(Name + "_dirInputFlussiDati", dirInputFlussiDati);
		return this;
	}

	public String getDirOutputFlussiDati() {
		return dirOutputFlussiDati;
	}

	public ParametriOttivoView setDirOutputFlussiDati(String dirOutputFlussiDati) {
		this.dirOutputFlussiDati = dirOutputFlussiDati;
		request.setAttribute(Name + "_dirOutputFlussiDati", dirOutputFlussiDati);
		return this;
	}

	public String getDirInputFlussiImmagini() {
		return dirInputFlussiImmagini;
	}

	public ParametriOttivoView setDirInputFlussiImmagini(String dirInputFlussiImmagini) {
		this.dirInputFlussiImmagini = dirInputFlussiImmagini;
		request.setAttribute(Name + "_dirInputFlussiImmagini", dirInputFlussiImmagini);
		return this;
	}

	public String getDirOutputFlussiImmagini() {
		return dirOutputFlussiImmagini;
	}

	public ParametriOttivoView setDirOutputFlussiImmagini(String dirOutputFlussiImmagini) {
		this.dirOutputFlussiImmagini = dirOutputFlussiImmagini;
		request.setAttribute(Name + "_dirOutputFlussiImmagini", dirOutputFlussiImmagini);
		return this;
	}

	public String getDirLogFlussi() {
		return dirLogFlussi;
	}

	public ParametriOttivoView setDirLogFlussi(String dirLogFlussi) {
		this.dirLogFlussi = dirLogFlussi;
		request.setAttribute(Name + "_dirLogFlussi", dirLogFlussi);
		return this;
	}

	public String getDirImmagini() {
		return dirImmagini;
	}

	public ParametriOttivoView setDirImmagini(String dirImmagini) {
		this.dirImmagini = dirImmagini;
		request.setAttribute(Name + "_dirImmagini", dirImmagini);
		return this;		
	}

	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	public ParametriOttivoView setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
		setValue(Name + "_siglaProvincia", siglaProvincia);
		super.setCodiceProvincia(this.siglaProvincia);
		return this;
	}

	@Override
	public void reset() {
		super.reset();
		setCodiceSocieta("");
		setCompanyCode("");
		setUserCode("");
		setChiaveEnte("");
		setSiglaProvincia("");
		setSorgenteImmagini("");
		super.setCodiceUtente(this.userCode);
		super.setCodiceProvincia(this.siglaProvincia);
	}
}