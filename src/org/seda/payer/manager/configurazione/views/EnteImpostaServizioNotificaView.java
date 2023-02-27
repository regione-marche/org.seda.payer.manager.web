package org.seda.payer.manager.configurazione.views;

import java.io.Serializable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.seda.payer.manager.views.BaseView;

public class EnteImpostaServizioNotificaView extends BaseView implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String Name = "enteimpostaservizionotifica";
	public static final String ActionName = "enteImpostaServizioNotifica";

	private String codiceUtente;
	private String chiaveEnte;
	private String codiceImpostaServizio;
	private String flagNotificaAllegato;
	private String listaRecDB;
	
	public EnteImpostaServizioNotificaView(ServletContext context, HttpServletRequest request) {
		super(context, request);
	}

	public EnteImpostaServizioNotificaView populate(HttpServletRequest request, String scope) {
		super.populate(request, scope);
		if (RichiestaCancScope.equals(scope)) {
			setValue("richiestacanc", request.getParameter("richiestacanc"));
			setValue(Name + "_userCode", request.getParameter(Name + "_userCode"));		  
			setValue(Name + "_chiaveEnte", request.getParameter(Name + "_chiaveEnte"));
			setValue(Name + "_codiceImpostaServizio", request.getParameter(Name + "_codiceImpostaServizio"));
			this.codiceUtente = getValue(Name + "_userCode");
			this.chiaveEnte = getValue(Name + "_chiaveEnte");
			this.codiceImpostaServizio = getValue(Name + "_codiceImpostaServizio");
		} else if (AddScope.equals(scope)) {
			this.codiceUtente = getValue(Name + "_userCode");
			this.chiaveEnte = getValue(Name + "_chiaveEnte");
			this.codiceImpostaServizio = getValue(Name + "_codiceImpostaServizio");
			super.setCodiceUtente(this.codiceUtente);
		} else if (EditScope.equals(scope)) {
			setValue(Name + "_userCode", request.getParameter(Name + "_userCode"));		  
			setValue(Name + "_chiaveEnte", request.getParameter(Name + "_chiaveEnte"));
			setValue(Name + "_codiceImpostaServizio", request.getParameter(Name + "_codiceImpostaServizio"));
			this.codiceUtente = getValue(Name + "_userCode");
			this.chiaveEnte = getValue(Name + "_chiaveEnte");
			this.codiceImpostaServizio = getValue(Name + "_codiceImpostaServizio");
			super.setCodiceUtente(this.codiceUtente);
		} else if (CancelScope.equals(scope)) {
			this.codiceUtente = getValue(Name + "_userCode");
			this.chiaveEnte = getValue(Name + "_chiaveEnte");
			this.codiceImpostaServizio = getValue(Name + "_codiceImpostaServizio");
		} else if (SaveScope.equals(scope)) {
			this.codiceUtente = getParam(Name + "_userCode");
			this.chiaveEnte = getParam(Name + "_chiaveEnte");
			this.codiceImpostaServizio = getParam(Name + "_codiceImpostaServizio");
			this.flagNotificaAllegato = getParam(Name + "_flagNotificaAllegato");       
		} else {
			this.codiceUtente = getValue(Name + "_searchuserCode");
			this.chiaveEnte = getValue(Name + "_searchchiaveEnte");
			this.codiceImpostaServizio = getValue(Name + "_searchcodiceImpostaServizio");
			this.flagNotificaAllegato = getValue(Name + "_searchflagNotificaAllegato");       
			super.setCodiceUtente(this.codiceUtente);
		}
		return this;
	}

	public String getUserCode() {
		return codiceUtente != null ? codiceUtente : "";
	}

	public EnteImpostaServizioNotificaView setUserCode(String userCode) {
		this.codiceUtente = userCode;
		request.setAttribute(Name + "_userCode", this.codiceUtente);
		super.setCodiceUtente(this.codiceUtente);
		return this;
	}

	public String getChiaveEnte() {
		return chiaveEnte != null ? chiaveEnte : "";
	}

	public EnteImpostaServizioNotificaView setChiaveEnte(String chiaveEnte) {
		this.chiaveEnte = chiaveEnte;
		request.setAttribute(Name + "_chiaveEnte", this.chiaveEnte);
		return this;
	}

	public EnteImpostaServizioNotificaView setListaRecDB(String listXml) {
		this.listaRecDB = listXml;
		request.setAttribute("listaRecDB", listXml);
		return this;
	}

	public EnteImpostaServizioNotificaView setListaRecDBPageInfo(com.seda.data.spi.PageInfo pageInfo) {
		super.setPageInfo(pageInfo);
		request.setAttribute("listaRecDB.pageInfo", pageInfo);
		return this;
	}
	
	public String getListaRecDB() {
		return listaRecDB;
	}

	public String getCodiceImpostaServizio() {
		return codiceImpostaServizio != null ? codiceImpostaServizio : "";
	}

	public EnteImpostaServizioNotificaView setCodiceImpostaServizio(String codiceImpostaServizio) {
		this.codiceImpostaServizio = codiceImpostaServizio;
		request.setAttribute(Name + "_codiceImpostaServizio", codiceImpostaServizio);
		return this;		
	}

	public String getFlagNotificaAllegato() {
		return flagNotificaAllegato != null ? flagNotificaAllegato : "";
	}

	public EnteImpostaServizioNotificaView setFlagNotificaAllegato(String flagNotificaAllegato) {
		this.flagNotificaAllegato = flagNotificaAllegato;
		request.setAttribute(Name + "_flagNotificaAllegato", flagNotificaAllegato);
		return this;		
	}

	@Override
	public void reset() {
		super.reset();
		setUserCode("");
		setChiaveEnte("");
		setCodiceImpostaServizio("");
		setFlagNotificaAllegato("");
		super.setCodiceUtente(this.codiceUtente);
	}
}