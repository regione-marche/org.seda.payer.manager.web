package org.seda.payer.manager.configurazione.views;

import java.io.Serializable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.seda.payer.manager.util.Messages;
import org.seda.payer.manager.views.BaseView;
import org.seda.payer.manager.ws.WSCache;

import com.seda.payer.commons.bean.TypeRequest;
import com.seda.payer.commons.utility.StringUtility;
import com.seda.payer.ottico.webservice.configurazione.dati.VerificaAssociaTemplateRequest;
import com.seda.payer.ottico.webservice.configurazione.dati.VerificaAssociaTemplateResponse;
/**
 * @author marco.montisano
 */
public class TemplateReportDinamiciView extends BaseView implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String Name = "templatereportdinamici";
	public static final String ActionName = "templateReportDinamici";
	public enum TipoDocumentoType { 
		Documento("D"), Bolletino("B"), Quietanza("Q");
		private String code;
		TipoDocumentoType(String code) {
			this.code = code;
		}
		public String getCode() {
			return this.code;
		}
	}
	private String chiaveTemplate;
	private String companyCode;
	private String siglaProvincia;
	private String userCode;
	private String chiaveEnte;
	private String tipoDocumento;
	private String tipologiaServizio;
	private String tipoBollettino; 
	private String riferimentoTemplate;
	private String listaTemplate;

	public TemplateReportDinamiciView(ServletContext context, HttpServletRequest request) {
		super(context, request);
	}

	public TemplateReportDinamiciView populate(HttpServletRequest request, String scope) {
		super.populate(request, scope);
		if (RichiestaCancScope.equals(scope)) {
			setValue("richiestacanc", request.getParameter("richiestacanc"));
			setValue(Name + "_chiaveTemplate", request.getParameter(Name + "_chiaveTemplate"));
			this.chiaveTemplate = getValue(Name + "_chiaveTemplate");
		} else if (AddScope.equals(scope)) {
			this.companyCode = getValue(Name + "_companyCode");
			this.tipologiaServizio = getValue(Name + "_tipologiaServizio");
			this.userCode = getValue(Name + "_userCode");
			this.chiaveEnte = getValue(Name + "_chiaveEnte");
			super.setCodiceSocieta(this.companyCode);
			super.setCodiceUtente(this.userCode);
		} else if (EditScope.equals(scope)) {
			setValue(Name + "_chiaveTemplate", request.getParameter(Name + "_chiaveTemplate"));
			this.chiaveTemplate = getValue(Name + "_chiaveTemplate");
		} else if (CancelScope.equals(scope)) {
			this.chiaveTemplate = getValue(Name + "_chiaveTemplate");
		} else if (SaveScope.equals(scope) || SaveEditScope.equals(scope) || SaveAddScope.equals(scope)) {
			this.chiaveTemplate = getValue(Name + "_chiaveTemplate");
			this.companyCode = getParam(Name + "_companyCode");
			this.userCode = getParam(Name + "_userCode");
			this.chiaveEnte = getParam(Name + "_chiaveEnte");
			this.tipologiaServizio = getValue(Name + "_tipologiaServizio");
			this.tipoDocumento = getValue(Name + "_tipoDocumento");
			this.tipoBollettino = getValue(Name + "_tipoBollettino");
			this.riferimentoTemplate = getValue(Name + "_riferimentoTemplate");
		} else if (BaseListScope.equals(scope)) {
			this.companyCode = getValue(Name + "_companyCode");
			this.tipologiaServizio = getValue(Name + "_tipologiaServizio");
			this.userCode = getValue(Name + "_userCode");
			this.chiaveEnte = getValue(Name + "_chiaveEnte");
			super.setCodiceSocieta(this.companyCode);
			super.setCodiceUtente(this.userCode);
		} else {
			this.companyCode = this.getCodiceSocieta();
			this.userCode = getValue(Name + "_searchuserCode");
			this.chiaveEnte = getValue(Name + "_searchchiaveEnte");
			this.siglaProvincia = getValue(Name + "_siglaProvincia");
			this.tipoDocumento = getValue(Name + "_searchtipoDocumento");
			this.tipologiaServizio = getValue(Name + "_searchtipologiaServizio");
			super.setCodiceProvincia(this.siglaProvincia);
			super.setCodiceUtente(this.userCode);
		}
		
		if (this.userCode != null && this.userCode.trim().length() > 0 && this.userCode.length() == 10)
		{
			this.companyCode = this.userCode.substring(0, 5);
			this.userCode = this.userCode.substring(5);
		}
		if (this.chiaveEnte != null && this.chiaveEnte.trim().length() > 0 && this.chiaveEnte.length() == 20)
		{
			this.companyCode = this.chiaveEnte.substring(0, 5);
			this.userCode = this.chiaveEnte.substring(5, 10);
			this.chiaveEnte = this.chiaveEnte.substring(10);
		}
		return this;
	}

	public String getCompanyCode() {
		return companyCode != null ? companyCode : "";
	}

	public TemplateReportDinamiciView setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
		request.setAttribute(Name + "_companyCode", this.companyCode);
		return this;
	}

	public String getUserCode() {
		return userCode != null ? userCode : "";
	}

	public TemplateReportDinamiciView setUserCode(String userCode) {
		this.userCode = userCode;
		request.setAttribute(Name + "_userCode", this.userCode);
		super.setCodiceUtente(this.userCode);
		return this;
	}

	public String getChiaveEnte() {
		return chiaveEnte != null ? chiaveEnte : "";
	}

	public TemplateReportDinamiciView setChiaveEnte(String chiaveEnte) {
		this.chiaveEnte = chiaveEnte;
		request.setAttribute(Name + "_chiaveEnte", this.chiaveEnte);
		return this;
	}

	public TemplateReportDinamiciView setListaTemplate(String listXml) {
		this.listaTemplate = listXml;
		request.setAttribute("listaTemplate", listXml);
		return this;
	}

	public TemplateReportDinamiciView setListaTemplatePageInfo(com.seda.data.spi.PageInfo pageInfo) {
		super.setPageInfo(pageInfo);
		request.setAttribute("listaTemplate.pageInfo", pageInfo);
		return this;
	}

	public String getListaTemplate() {
		return listaTemplate;
	}

	public String getSiglaProvincia() {
		return siglaProvincia;
	}

	public TemplateReportDinamiciView setSiglaProvincia(String siglaProvincia) {
		this.siglaProvincia = siglaProvincia;
		setValue(Name + "_siglaProvincia", siglaProvincia);
		super.setCodiceProvincia(this.siglaProvincia);
		return this;
	}

	public String getChiaveTemplate() {
		return chiaveTemplate;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public String getTipologiaServizio() {
		return tipologiaServizio;
	}

	public String getTipoBollettino() {
		return tipoBollettino;
	}

	public String getRiferimentoTemplate() {
		return riferimentoTemplate;
	}

	public TemplateReportDinamiciView setChiaveTemplate(String chiaveTemplate) {
		this.chiaveTemplate = chiaveTemplate;
		setValue(Name + "_chiaveTemplate", chiaveTemplate);
		return this;
	}

	public TemplateReportDinamiciView setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
		setValue(Name + "_tipoDocumento", tipoDocumento);
		return this;
	}

	public TemplateReportDinamiciView setTipologiaServizio(String tipologiaServizio) {
		this.tipologiaServizio = tipologiaServizio;
		setValue(Name + "_tipologiaServizio", tipologiaServizio);
		return this;
	}

	public TemplateReportDinamiciView setTipoBollettino(String tipoBollettino) {
		this.tipoBollettino = tipoBollettino;
		setValue(Name + "_tipoBollettino", tipoBollettino);
		return this;
	}

	public TemplateReportDinamiciView setRiferimentoTemplate(String riferimentoTemplate) {
		this.riferimentoTemplate = riferimentoTemplate;
		setValue(Name + "_riferimentoTemplate", riferimentoTemplate);
		setCachedValue(Name + "_riferimentoTemplate", riferimentoTemplate);
		return this;
	}

	public boolean validate() throws Exception {
		// we check for keys tipoDocumento/companyCode/tipologiaServizio/userCode/chiaveEnte and date if was exist template
		VerificaAssociaTemplateResponse response = WSCache.configurazioneServer.verificaAssociaTemplate(
				new VerificaAssociaTemplateRequest(this.getTipoDocumento(), this.getCompanyCode(), 
						this.getTipologiaServizio(), this.getUserCode(), this.getChiaveEnte(), 
						this.getDataDa(), this.getDataA(), 
						(this.getTypeRequest().compareTo(TypeRequest.ADD_SCOPE.scope()) == 0 
						 ? null : this.getChiaveTemplate() /* exclude it self */)));
		if (response.isWasExist()) {
			this.setMessage(Messages.ERR_TEMPLATE_RPT_OVERLAY.format());
			this.setError(true);
			return false;
		}
		// we check if tipoDocumento equal TipoDocumentoType.Quietanza then 
		// companyCode/tipologiaServizio/userCode/chiaveEnte must be empty
		if (this.getTipoDocumento().equals(TipoDocumentoType.Quietanza.getCode())) {
			if (!StringUtility.isEmpty(this.getCompanyCode())
					|| !StringUtility.isEmpty(this.getTipologiaServizio())
					|| !StringUtility.isEmpty(this.getUserCode())
					|| !StringUtility.isEmpty(this.getChiaveEnte())) {
				this.setMessage(Messages.ERR_TEMPLATE_RPT_QUIETANZA.format());
				this.setError(true);
				return false;
			}
		}
		if (!this.getTipoDocumento().equals(TipoDocumentoType.Quietanza.getCode())) {
			if (!StringUtility.isEmpty(this.getTipologiaServizio()) && StringUtility.isEmpty(this.getCompanyCode())) {
				this.setMessage(Messages.ERR_TEMPLATE_RPT_PRIMARY_KEY_A.format());
				this.setError(true);
				return false;
			} else if (!StringUtility.isEmpty(this.getUserCode()) && (StringUtility.isEmpty(this.getCompanyCode()) || StringUtility.isEmpty(this.getTipologiaServizio()))) {
				this.setMessage(Messages.ERR_TEMPLATE_RPT_PRIMARY_KEY_B.format());
				this.setError(true);
				return false;
			} else if (!StringUtility.isEmpty(this.getChiaveEnte()) && (StringUtility.isEmpty(this.getCompanyCode()) || StringUtility.isEmpty(this.getUserCode()) || StringUtility.isEmpty(this.getTipologiaServizio()))) {
				this.setMessage(Messages.ERR_TEMPLATE_RPT_PRIMARY_KEY_C.format());
				this.setError(true);
				return false;
			}
		}
/*
 		} else {
			if (StringUtility.isEmpty(this.getCompanyCode())
					|| StringUtility.isEmpty(this.getTipologiaServizio())
					|| StringUtility.isEmpty(this.getUserCode())
					|| StringUtility.isEmpty(this.getChiaveEnte())) {
				this.setMessage(Messages.ERR_TEMPLATE_RPT_PRIMARY_KEY.format());
				this.setError(true);
				return false;
			}
		}
*/
		// we check if tipoDocumento not equal TipoDocumentoType.Bolletino then tipoBollettino must be empty
		if (!this.getTipoDocumento().equals(TipoDocumentoType.Bolletino.getCode())
				&& !StringUtility.isEmpty(this.getTipoBollettino())) {
			this.setMessage(Messages.ERR_TEMPLATE_RPT_SET_BOLLETTINO.format());
			this.setError(true);
			return false;
		} else if (this.getTipoDocumento().equals(TipoDocumentoType.Bolletino.getCode()) 
						&& StringUtility.isEmpty(this.getTipoBollettino())) {
			this.setMessage(Messages.ERR_TEMPLATE_RPT_SET_BOLLETTINO_REQUIRED.format());
			this.setError(true);
			return false;
		}
		return true;
	}

	@Override
	public void reset() {
		super.reset();
		setCodiceSocieta("");
		setCompanyCode("");
		setUserCode("");
		setChiaveEnte("");
		setSiglaProvincia("");
		setChiaveTemplate("");
		setTipoDocumento("");
		setTipologiaServizio("");
		setTipoBollettino(""); 
		setRiferimentoTemplate("");
		super.setCodiceUtente(this.userCode);
		super.setCodiceProvincia(this.siglaProvincia);
	}
}