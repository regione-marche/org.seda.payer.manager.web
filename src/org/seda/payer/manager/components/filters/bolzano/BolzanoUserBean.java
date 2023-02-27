package org.seda.payer.manager.components.filters.bolzano;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.seda.payer.manager.components.filters.TemplateFilter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.ws.WSCache;


import com.seda.commons.security.ChryptoServiceException;
import com.seda.commons.security.TokenGenerator;
import com.seda.commons.security.TripleDESChryptoService;
import com.seda.j2ee5.maf.core.security.SignOnKeys;



public class BolzanoUserBean {
	
	//private NaturalPerson actor;
	private String nome;
	private String cognome;
	private String codiceFiscale;
	//private Account account;
	private String username;
	private boolean initialized=false;

	public BolzanoUserBean() {
	}
	
	
	
	public void loadUserBean(HttpServletRequest request) throws UnsupportedEncodingException, ChryptoServiceException{
		this.nome=decrypt((String) request.getParameter("nome"));
		this.cognome=decrypt((String) request.getParameter("cognome"));
		this.codiceFiscale=decrypt((String) request.getParameter("codiceFiscale"));
		this.initialized=true;
	}
	
	private String decrypt(String dataCrypted) {
		{
			try
			{
				TripleDESChryptoService desChrypto = new TripleDESChryptoService();
				desChrypto.setIv(WSCache.securityIV);
				desChrypto.setKeyValue(WSCache.securityKey);
				return desChrypto.decryptBASE64(dataCrypted);	
				
			} catch (Exception ex) 
			{ return ""; }
			
		}
	}



	public void loadMockUserBean(){
		this.username="SEDA_Username";
		this.nome="SEDA_Name";
		this.cognome="SEDA_Cognome";
		this.codiceFiscale="ASDCVB78A12A555Z";
		this.initialized=true;
	}

	

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	
	public boolean isValidBolzanoAccount(){
		return this.nome!=null&&this.cognome!=null&&this.codiceFiscale!=null&&this.nome.length()>0&&this.cognome.length()>0&&this.codiceFiscale.length()>0 ;
	}
	

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public boolean isValidUserBean(){
		return (this.nome!=null&&this.cognome!=null&&this.codiceFiscale!=null);
	}
	
	
	public UserBean createNewUserBean(HttpServletRequest request, HttpSession session){
		if(!initialized){
			return null;
		}
		UserBean userBean = new UserBean();
		userBean.setSession(session.getId());
		TemplateFilter.setTemplateToUserBean(userBean, session, request);
		userBean.setCodiceFiscale(this.getCodiceFiscale());
		userBean.setUserName(this.getCodiceFiscale());
		userBean.setNome(this.nome);
		userBean.setCognome(this.cognome);
		
		//Valorizzo il token che serve per il log degli accessi
		String token = null;
		try {
			token = TokenGenerator.generateSecureToken(40);
		} catch (NoSuchAlgorithmException e) {
			token = null;
		}
		session.setAttribute(SignOnKeys.USER_BEAN, userBean);
		session.setAttribute(SignOnKeys.SIGNED_ON_USER, new Boolean(true));
		userBean.setSession(session.getId());
		return userBean;
		
	}
	

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BolzanoUserBean [account=");
		builder.append(", actor=");
		builder.append(", codiceFiscale=");
		builder.append(codiceFiscale);
		builder.append(", cognome=");
		builder.append(cognome);
		builder.append(", initialized=");
		builder.append(initialized);
		builder.append(", nome=");
		builder.append(nome);
		builder.append(", username=");
		builder.append(username);
		builder.append("]");
		return builder.toString();
	}
	
	

}
