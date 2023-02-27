/**
 * 
 */
package org.seda.payer.manager.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import sun.security.krb5.internal.PAEncTSEnc;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.core.security.UserBeanSupport;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;
import org.seda.payer.manager.util.PropertiesPath;

/**
 * @author barnocchi
 *
 */
public class PayerCommandInvoker {

	private static Map<String, Class<PayerCommand>> buffer;
	
	private static PayerCommandInvoker me;
	
	static {
		me=new PayerCommandInvoker();
	}

	private PayerCommandInvoker() {
		buffer=Collections.synchronizedMap(new HashMap<String, Class<PayerCommand>>());
	}
	
	public static PayerCommandInvoker instance() {
		return me;
	}
	
	public Object executeCommand(HttpServletRequest request) {
		MAFRequest mafRequest = new MAFRequest(request);
		String bufferKey = getBufferKey(mafRequest);
		Class<PayerCommand> payerCommandClass=null;
		if (buffer.containsKey(bufferKey)) {
			payerCommandClass=buffer.get(bufferKey);
		} else {
			String payerCommnadClassName = getCommandClassName(request, bufferKey);
			payerCommandClass=loadClassForName(payerCommnadClassName);
			buffer.put(bufferKey, payerCommandClass);
		}
		PayerCommand payerCommand=null;
		Object returnObject=null;
		try {
			payerCommand=createCommand(payerCommandClass);
			returnObject=payerCommand.execute(request);	
		} finally {
			payerCommand=null;
		}
		return returnObject;
	}
	
	private PayerCommand createCommand(
			Class<PayerCommand> payerCommnadClass) {
		PayerCommand payerCommand=null;
		try {
			payerCommand=payerCommnadClass.newInstance();
		} catch (Exception x) {
			throw new PayerCommandException("Errore imprevisto nella creazione del del comando "+payerCommnadClass,x);
		}
		return payerCommand;
	}

	private Class<PayerCommand> loadClassForName(
			String payerCommandClassName) {
		Class<PayerCommand> payerCommandClass=null;
		try {
			payerCommandClass=(Class<PayerCommand>) Class.forName(payerCommandClassName);
		} catch (ClassNotFoundException x) {
			throw new PayerCommandException("La classe di esecuzione del comando non è stata trovata " +payerCommandClassName);
		} catch (Exception x) {
			throw new PayerCommandException("Errore imprevisto nel caricamento del comando "+payerCommandClassName,x);
		}
		return payerCommandClass;
	}

	private String getCommandClassName(HttpServletRequest request,
			String bufferKey) {
		// in questo punto la sessione è già stata testata
		ServletContext context = request.getSession(false).getServletContext();
		PropertiesTree propertiesTree = (PropertiesTree) context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
		// bufferKey è applicazione.template.azione(senza estensione)
		String className = propertiesTree.getProperty(PropertiesPath.templateActions.format(PropertiesPath.defaultnode.format()) + bufferKey);
		if (className==null)
			throw new PayerCommandException("La richiesta corrente si riferisce ad una azione di comando non configurata correttamente");
		return className;
	}

	private String getBufferKey(MAFRequest mafRequest) {
		String actionName = getActionName(mafRequest.getTargetURL(),mafRequest.getExtension());
		String applid = (String)mafRequest.getAttribute(MAFAttributes.CURRENT_APPLICATION);
		if (applid==null) {
			throw new PayerCommandException("L'applicazione corrente non è valida");
		}		
		String template = getTemaple(mafRequest, applid);
		return applid+"."+template+"."+actionName;
	}
	
	private String getTemaple(MAFRequest mafRequest, String applid) {
		HttpSession session = mafRequest.getHttpSession(false);
		String template = null;
		if (session==null) {
			throw new PayerCommandException("La sessione di navigazione non è valida");
		}
		UserBeanSupport userBean = (UserBeanSupport)session.getAttribute(SignOnKeys.USER_BEAN);
		if (userBean==null) {
			throw new PayerCommandException("Utente non registrato nella sessione di navigazione");
		}
		template = userBean.getTemplate(applid);
		return (template == null ? "default" : template);
	}
	
	private static String getActionName(String targetURL, String extension) {
		return targetURL.substring(0, targetURL.length() - extension.length());
	}
	
}
