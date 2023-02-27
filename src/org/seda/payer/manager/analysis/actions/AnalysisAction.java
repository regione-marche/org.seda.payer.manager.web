package org.seda.payer.manager.analysis.actions;


import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.LineNumberReader;
import java.net.URI;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.RandomStringUtils;
import org.seda.payer.manager.analysis.AnalysisUtils;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;
import org.seda.payer.manager.ws.BaseServer;

import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.j2ee5.maf.core.action.ActionException;
import com.seda.j2ee5.maf.core.action.HtmlAction;
import com.seda.j2ee5.maf.core.security.SignOnKeys;

public class AnalysisAction extends HtmlAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public Object service(HttpServletRequest request) throws ActionException {

		HttpSession session=request.getSession();
		
		UserBean user = ((UserBean)session.getAttribute(SignOnKeys.USER_BEAN));
		
		String query = request.getParameter("query");
		String mnId = request.getParameter("mnId");
		String mnId1 = request.getParameter("mnId1");
		String mnId2 = request.getParameter("mnId2");
		String mnLivello = request.getParameter("mnLivello");
		String dataOdierna = "";
		
		
		if(mnId != null) {
			session.setAttribute("mnId", mnId);
		} else {
			request.getParameterMap().put("mnId", session.getAttribute("mnId"));
		}
		
		if(mnId1 != null) {
			session.setAttribute("mnId1", mnId1);
		} else {
			request.getParameterMap().put("mnId1", session.getAttribute("mnId1"));
		}
		if(mnId2 != null) {
			session.setAttribute("mnId2", mnId2);
		} else {
			request.getParameterMap().put("mnId2", session.getAttribute("mnId2"));
		}
		if(mnLivello != null) {
			session.setAttribute("mnLivello", mnLivello);
		} else {
			request.getParameterMap().put("mnLivello", session.getAttribute("mnLivello"));
		}
			
		// Determino la connessione al database
		PropertiesTree configuration = (PropertiesTree)context.getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);

		// Determino la data odierna - 1
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, -1);
		java.text.SimpleDateFormat mese = new java.text.SimpleDateFormat("MM");
		dataOdierna = c.get(Calendar.DAY_OF_MONTH) + "/" + mese.format(c.getTime()) + "/" + c.get(Calendar.YEAR);

		boolean creaCubo = request.getParameter("cache") != null && !Boolean.getBoolean(request.getParameter("cache"));
		String fileName = (String) session.getAttribute("analysis.filename");
		String outputPath = configuration.getProperty(PropertiesPath.path_cubo.format());
		if(fileName == null) {
			creaCubo = true;
		}
		
		if (creaCubo) {
			AnalysisUtils.deleteCubeFile(session);
			fileName = RandomStringUtils.random(20, true, true);	
			session.setAttribute("analysis.filename", fileName);
			creaCubo(session.getServletContext().getRealPath("/WEB-INF/queries/"), outputPath, fileName, user);
		}

		BaseServer baseServer = new BaseServer();
		String codSocieta = baseServer.getCodSocieta(request);
		String dataSource = configuration.getProperty(PropertiesPath.dataSource.format(codSocieta) + "base.name");
		
		fileName = configuration.getProperty(PropertiesPath.path_cubo.format()) + "/" + (String)session.getAttribute("analysis.filename");
		
		session.setAttribute("fileName", fileName);
		//session.setAttribute("analysis.dataAnalisi", dataOdierna);
		session.setAttribute("dataSource", dataSource);
		session.setAttribute("data01", dataOdierna);
		
		if (query!=null) {
			if (query.equalsIgnoreCase("transazioniQuery")) 
				session.setAttribute("title01", "Transazioni");
			else
				session.setAttribute("title01", "Dettaglio");
		}
		
		
		return null;  
	}

	private void creaCubo(String templatePath, String outputPath, String fileName, UserBean user ){
		
		
		
		try{
            LineNumberReader lnr = new LineNumberReader( new FileReader(templatePath+java.io.File.separator +"Seda_Transazioni.xml"));
            File outputFile = new File(new URI(outputPath + "/" + fileName));
            FileWriter fw = new FileWriter(outputFile);
            String s;
            
            while((s = lnr.readLine()) != null) {
            	String newS = s.replace("XXXWHERECONDITION_TRANSXXX", creaWhereConditionTrans(user));
            	newS = newS.replace("XXXWHERECONDITION_SOCXXX", creaWhereConditionSoc(user));
            	newS = newS.replace("XXXWHERECONDITION_ORGXXX", creaWhereConditionOrg(user));
            	newS = newS.replace("XXXWHERECONDITION_DETXXX", creaWhereConditionDet(user));
                fw.write(newS);
            }
            fw.flush();
            lnr.close();
            fw.close();
            }
        catch(Exception e){
            e.printStackTrace();
        }
	}
	
	private String creaWhereConditionSoc(UserBean user){
		if ( !user.getProfile().equals("AMMI")){
				return " where SOC_CSOCCSOC = '"+ user.getCodiceSocieta() +"'";
		}
		else return "";
	}
	
	private String creaWhereConditionTrans(UserBean user){
		if ( user.getProfile().equals("AMSO")){
			return " where TRA_GTRADTRA &lt; CURRENT_DATE and TRA_CSOCCSOC = '"+ user.getCodiceSocieta() +"'";
		}
		/*
		if ( user.getProfile().equals("AMEN")){
			return " where TRA_CSOCCSOC = '"+ user.getCodiceSocieta() +"'" 
			+ " and USR_KANEKENT_CON = '"+ user.getChiaveEnteConsorzio()+"'" 
			+ " and GTW_CUTECUTE = '"+ user.getCodiceUtente()+"'";
		}
		*/
		if ( user.getProfile().equals("AMUT")){
			return " where TRA_GTRADTRA &lt; CURRENT_DATE and TRA_CSOCCSOC = '"+ user.getCodiceSocieta() +"'" 
			+ " and GTW_CUTECUTE = '"+ user.getCodiceUtente()+"'";
		}
		if ( user.getProfile().equals("AMMI")){
			return " where TRA_GTRADTRA &lt; CURRENT_DATE ";
		}
		
		return "";
	}
	
	private String creaWhereConditionOrg(UserBean user){
		if ( user.getProfile().equals("AMSO")){
			return " where ENT_CSOCCSOC = '"+ user.getCodiceSocieta() +"'";
		}
		/*
		if ( user.getProfile().equals("AMEN")){
			return " where ENT_CSOCCSOC = '"+ user.getCodiceSocieta() +"'" 
			+ " and ENT_KANEKENT = '"+ user.getChiaveEnteConsorzio()+"'" 
			+ " and ENT_CUTECUTE = '"+ user.getCodiceUtente()+"'";
		}
		*/
		if ( user.getProfile().equals("AMUT")){
			return " where ENT_CSOCCSOC = '"+ user.getCodiceSocieta() +"'" 
			+ " and ENT_CUTECUTE = '"+ user.getCodiceUtente()+"'";
		}
		if ( user.getProfile().equals("AMMI")){
			return "";
		}
		
		return "";
	}
	
	private String creaWhereConditionDet(UserBean user){
		if ( user.getProfile().equals("AMSO")){
			return " where CSOCCSOC = '"+ user.getCodiceSocieta() +"'";
		}
		/*
		if ( user.getProfile().equals("AMEN")){
			return " where CSOCCSOC = '"+ user.getCodiceSocieta() +"'" 
			+ " and KANEKENT = '"+ user.getChiaveEnteConsorzio()+"'" 
			+ " and CUTECUTE = '"+ user.getCodiceUtente()+"'";
		}
		*/
		if ( user.getProfile().equals("AMUT")){
			return " where CSOCCSOC = '"+ user.getCodiceSocieta() +"'" 
			+ " and CUTECUTE = '"+ user.getCodiceUtente()+"'";
		}
		if ( user.getProfile().equals("AMMI")){
			return "";
		}
		
		return "";
	}
}
