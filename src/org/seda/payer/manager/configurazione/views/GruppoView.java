package org.seda.payer.manager.configurazione.views;

import java.io.Serializable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.views.BaseView;
import org.seda.payer.manager.ws.WSCache;

import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.pgec.webservice.configgruppo.dati.ConfigGruppoVerificaRequest;
import com.seda.payer.pgec.webservice.configgruppo.dati.ConfigGruppoVerificaResponse;

public class GruppoView extends BaseView implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String Name = "gruppo";
	public static final String ActionName = "gruppo";
	
	private String chiaveGruppo;
	private String codiceGruppo;
	private String descrizioneLinguaItaliana;
	private String descrizioneAltraLingua;
	private String listaGruppo;

	public GruppoView(ServletContext context, HttpServletRequest request) {
		super(context, request);
	}

	public GruppoView populate(HttpServletRequest request, String scope) {
		super.populate(request, scope);
		if (RichiestaCancScope.equals(scope)) {
			setValue("richiestacanc", request.getParameter("richiestacanc"));
			setValue(Name + "_chiaveGruppo", request.getParameter(Name + "_chiaveGruppo"));
			this.chiaveGruppo = getValue(Name + "_chiaveGruppo");
		} else if (AddScope.equals(scope)) {
			this.codiceGruppo = getValue(Name + "_codiceGruppo");
			this.descrizioneLinguaItaliana = getValue(Name + "_descrizioneLinguaItaliana");
			this.descrizioneAltraLingua = getValue(Name + "_descrizioneAltraLingua");
		} else if (EditScope.equals(scope)) {
			setValue(Name + "_chiaveGruppo", request.getParameter(Name + "_chiaveGruppo"));
			this.chiaveGruppo = getValue(Name + "_chiaveGruppo");
		} else if (CancelScope.equals(scope)) {
			this.chiaveGruppo = getValue(Name + "_chiaveGruppo");
		} else if (SaveScope.equals(scope) || SaveEditScope.equals(scope) || SaveAddScope.equals(scope)) {
			this.chiaveGruppo = getValue(Name + "_chiaveGruppo");
			this.codiceGruppo = getValue(Name + "_codiceGruppo");
			this.descrizioneLinguaItaliana = getValue(Name + "_descrizioneLinguaItaliana");
			this.descrizioneAltraLingua = getValue(Name + "_descrizioneAltraLingua");
		} else if (BaseListScope.equals(scope)) {
			this.codiceGruppo = getValue(Name + "_codiceGruppo");
			this.descrizioneLinguaItaliana = getValue(Name + "_descrizioneLinguaItaliana");
			this.descrizioneAltraLingua = getValue(Name + "_descrizioneAltraLingua");
		} else {
			this.codiceGruppo = getValue(Name + "_searchcodiceGruppo");
			this.descrizioneLinguaItaliana = getValue(Name + "_searchdescrizioneLinguaItaliana");
			//this.descrizioneAltraLingua = getValue(Name + "_descrizioneAltraLingua");
		}
		return this;
	}

	public GruppoView setListaGruppo(String listXml) {
		this.listaGruppo = listXml;
		request.setAttribute("listaGruppo", listXml);
		return this;
	}

	public GruppoView setListaGruppoPageInfo(com.seda.data.spi.PageInfo pageInfo) {
		super.setPageInfo(pageInfo);
		request.setAttribute("listaGruppo.pageInfo", pageInfo);
		return this;
	}

	public String getListaGruppo() {
		return listaGruppo;
	}

	public String getChiaveGruppo() {
		return chiaveGruppo;
	}

	public GruppoView setChiaveGruppo(String chiaveGruppo) {
		this.chiaveGruppo = chiaveGruppo;
		setValue(Name + "_chiaveGruppo", chiaveGruppo);
		return this;
	}

	public String getCodiceGruppo() {
		return codiceGruppo;
	}

	public GruppoView setCodiceGruppo(String codiceGruppo) {
		this.codiceGruppo = codiceGruppo;
		setValue(Name + "_codiceGruppo", codiceGruppo);
		return this;
	}


	public String getDescrizioneLinguaItaliana() {
		return descrizioneLinguaItaliana;
	}

	public GruppoView setDescrizioneLinguaItaliana(String descrizioneLinguaItaliana) {
		this.descrizioneLinguaItaliana = descrizioneLinguaItaliana;
		setValue(Name + "_descrizioneLinguaItaliana", descrizioneLinguaItaliana);
		return this;
	}


	public String getDescrizioneAltraLingua() {
		return descrizioneAltraLingua;
	}

	public GruppoView setDescrizioneAltraLingua(String descrizioneAltraLingua) {
		this.descrizioneAltraLingua = descrizioneAltraLingua;
		setValue(Name + "_descrizioneAltraLingua", descrizioneAltraLingua);
		return this;
	}

	public boolean validate() throws Exception {
		ConfigGruppoVerificaRequest in = new ConfigGruppoVerificaRequest();
		in.setChiaveGruppo(this.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0 ? null : this.getChiaveGruppo());
		in.setCodiceGruppo(this.getCodiceGruppo());
		ConfigGruppoVerificaResponse res = WSCache.configGruppoServer.verifica(in, request);
		if (res.isWasExist()) {
			this.setMessage("Gruppo con codice già presente");
			this.setError(true);
			return false;
		}
		return true;
	}

	@Override
	public void reset() {
		super.reset();
		setCodiceGruppo("");
		setDescrizioneLinguaItaliana("");
		setDescrizioneAltraLingua("");
	}
}