package org.seda.payer.manager.reports.stampe;

import java.io.File;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import javax.sql.DataSource;
import java.util.Date;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;

import com.seda.commons.logger.CustomLoggerManager;
import com.seda.commons.logger.LoggerWrapper;
import com.seda.commons.properties.tree.PropertiesTree;


public class ReportsCreator  {
  
	String outputDirectoryReports;
	String birtDesignPath; 
	String pathNameFile;
	DataSource datasource; 
	String fileName;
	//inizio LP PG21XX04 Bug configuration
	PropertiesTree configuration;
	//fine LP PG21XX04 Bug configuration

	private static LoggerWrapper logger = CustomLoggerManager.get(ReportsCreator.class);
	
	private Level level = Level.WARNING;
	public static String tipo_errore="";
	//inizio LP PAGONET-323
	//public static final String RNNODOMONITORAGGIO = "ricevuta_telematica_nodo_MG.rptdesign";
	public static final String RNNODOMONITORAGGIO = "ricevuta_telematica_nodo_MG-2.0.rptdesign";
	//fine LP PAGONET-323
	
	public static final String TEMPLATERENDICONTAZIONEINCASSI = "rendicontazioneIncassi.rptdesign";
	
	
	public ReportsCreator( 
			String outputDirectoryReports, String birtDesignPath,  DataSource datasource) { 
		this.outputDirectoryReports = outputDirectoryReports;
		this.birtDesignPath = birtDesignPath; 
		this.datasource = datasource;
	}
	
	private IReportEngine getReportEngine() 
	{  
		fileName = getFileName(); 
		System.out.println("getReportEngine - fileName: " + fileName);
		return  BirtEngine.instance().getEngine();
	}
	
	private static String getFileName() {
		InetAddress addr = null;
		String hostname = "";
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		if (addr != null) {
			hostname = addr.getHostName();
		}
		System.out.println("getFileName addr " + addr);
		System.out.println("getFileName hostname " + hostname);

		Calendar calendar = Calendar.getInstance(TimeZone
				.getTimeZone("Europe/Rome"), Locale.ITALY);
		calendar.setTimeInMillis(System.currentTimeMillis());

		StringBuilder sb = new StringBuilder();
		sb.append(hostname);
		sb.append("_ReportEngine_");
		sb.append(calendar.get(Calendar.YEAR));
		sb.append("_");
		sb.append(calendar.get(Calendar.MONTH) + 1);
		sb.append("_");
		sb.append(calendar.get(Calendar.DAY_OF_MONTH));
		sb.append("_");
		sb.append(calendar.get(Calendar.HOUR_OF_DAY));
		sb.append("_");
		sb.append(calendar.get(Calendar.MINUTE));
		sb.append("_");
		sb.append(calendar.get(Calendar.SECOND));
		sb.append(".log");

		return sb.toString();
	}
		
	@SuppressWarnings("unchecked")
	//inizio LP PAGONET-323
	//public String renderRTdaMonitoraggio (			
	//		String anagraficaPagatore, String codiceIdentificativoUnivoco, String emailPagatore, String codiceIUV,
	//		String data, BigDecimal importo_totale_transazione, String denominazioneAttestante, String identificativoDominio,
	//		String identificativoUnivocoRiscossione, String codiceIdentificativoUnivocoPSP, String idTransazione, 
	//		DecimalFormat numberFormat, BigDecimal commissioniApplicatePSP, String oraMessaggio,String numeroAutorizzazione, 
	//		String denominazioneEnte, String pathLogoPAReport, String paramPathLogoSocieta, String esitoPagamento, String causale) throws SQLException {
	public String renderRTdaMonitoraggio (			
			String anagraficaPagatore, String codiceIdentificativoUnivoco, String emailPagatore, String codiceIUV,
			String data, BigDecimal importo_totale_transazione, String denominazioneAttestante, String identificativoDominio,
			String identificativoUnivocoRiscossione, String codiceIdentificativoUnivocoPSP, String idTransazione, 
			DecimalFormat numberFormat, BigDecimal commissioniApplicatePSP, String oraMessaggio,String numeroAutorizzazione, 
			String denominazioneEnte, String pathLogoPAReport, String paramPathLogoSocieta, String esitoPagamento, String causale,
			String dataApplicativa
	) throws SQLException {
	//fine LP PAGONET-323

		logger.debug("inizio renderRTdaMonitoraggio");
		
		System.out.println("inizio renderRTdaMonitoraggio");
		
		pathNameFile = outputDirectoryReports +  getNameFileRtMonitoraggio(idTransazione,codiceIUV);
		
		System.out.println("pathNameFile=" + pathNameFile);
		
		boolean exists = (new File(pathNameFile)).exists();
			
		System.out.println("renderRTdaMonitoraggio - pre getReportEngine");
			IReportEngine engine = getReportEngine();
		System.out.println("renderRTdaMonitoraggio - post getReportEngine");	
			if(engine!=null){
				try {
					System.out.println("renderRTdaMonitoraggio - engine!=null");	
					IReportRunnable design;
					design = engine.openReportDesign(this.birtDesignPath+RNNODOMONITORAGGIO);
					
					IRunAndRenderTask task = engine.createRunAndRenderTask(design);
					task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader()); 
			        
					task.setParameterValue("anagraficaPagatore", anagraficaPagatore);
					task.setParameterValue("codiceIdentificativoUnivoco", codiceIdentificativoUnivoco);
					task.setParameterValue("emailPagatore", emailPagatore);
					task.setParameterValue("codiceIUV", codiceIUV);
					task.setParameterValue("denominazioneAttestante", denominazioneAttestante);
					task.setParameterValue("identificativoDominio", identificativoDominio);
					task.setParameterValue("identificativoUnivocoRiscossione", identificativoUnivocoRiscossione);
					task.setParameterValue("codiceIdentificativoUnivocoPSP", codiceIdentificativoUnivocoPSP);
					task.setParameterValue("data", data);
					task.setParameterValue("ora", oraMessaggio);
					task.setParameterValue("numeroAutorizzazione", numeroAutorizzazione);
					task.setParameterValue("denominazioneEnte", denominazioneEnte);
					task.setParameterValue("importo_totale_transazione", numberFormat.format(importo_totale_transazione));
					task.setParameterValue("pagoPa", pathLogoPAReport);
					task.setParameterValue("logo", paramPathLogoSocieta);
					task.setParameterValue("commissioniApplicatePSP", commissioniApplicatePSP!=null?numberFormat.format(commissioniApplicatePSP):commissioniApplicatePSP);	//PG160150_003 GG
					task.setParameterValue("esitopagamento", esitoPagamento);
					task.setParameterValue("causale", causale);					
					//inizio LP PAGONET-323
					task.setParameterValue("dataApplicativa", dataApplicativa);
					//fineLP PAGONET-323
					PDFRenderOption options = new PDFRenderOption();
					options.setOutputFileName(pathNameFile);
					options.setOutputFormat("pdf");
				
					task.setRenderOption(options);
					task.run();
					task.close();
					
				} catch (EngineException e) {
					
					e.printStackTrace();
					return "";
				} catch (Throwable e){
					
					e.printStackTrace();
					return "";
				}
				
				
			} else {
				System.out.println("renderRTdaMonitoraggio - engine==null");	
				return "";	
			} 
				
		logger.debug("fine renderRTdaMonitoraggio");
		return pathNameFile;
	}
	
	public String generaPdfGiornaliDiCassa(String ente, String dataGdcDa, String dataGdcA, String idFlussoCassa, 
			String dataMdcDa, String dataMdcA, String numeroDocumento, String psp, String logo, Connection connection, String schema){
		
		String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH.mm").format(new Date());
		String pdfBaseName = "Report_rendicontazione_incassi - ";
		pathNameFile = outputDirectoryReports + pdfBaseName + timestamp + ".pdf";
		
		boolean exists = (new File(pathNameFile)).exists();
		
		IReportEngine engine = getReportEngine();
			
		if(engine != null){
				IReportRunnable design = null;
				try {
					design = engine.openReportDesign(this.birtDesignPath + TEMPLATERENDICONTAZIONEINCASSI);			
				} catch (EngineException e) {
					e.printStackTrace();
				}			
				IRunAndRenderTask task = engine.createRunAndRenderTask(design);
				task.getAppContext().put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, this.getClass().getClassLoader());  
				try {					
		            task.getAppContext().put("OdaJDBCDriverPassInConnection", connection);
		        } 
				catch (Exception x) {
		             x.printStackTrace();                                                   
				}
				task.setParameterValue("logo", logo);
				task.setParameterValue("Ente", ente);
				task.setParameterValue("GDC_DATA_DA", dataGdcDa);
				task.setParameterValue("GDC_DATA_A", dataGdcA);
				task.setParameterValue("P_GDC_CGDCIDFL", idFlussoCassa);
				task.setParameterValue("MDC_DATA_DA", dataMdcDa);
				task.setParameterValue("MDC_DATA_A", dataMdcA);
				task.setParameterValue("P_MDC_CMDCNBOL", numeroDocumento);
				task.setParameterValue("P_MDC_CMDCCLIE", psp);
				task.setParameterValue("P_SCHEMA_NAME",schema);
				
				PDFRenderOption options = new PDFRenderOption();
				options.setOutputFileName(pathNameFile);
				options.setOutputFormat("pdf");
			
				task.setRenderOption(options);
				try {
					task.run();
				} catch (EngineException e) {
					e.printStackTrace();
				}
				task.close();
		}else{
			return "error";
		}
		
		return pathNameFile;
	}
	
	private String getNameFileRtMonitoraggio(String idtra, String codiceIUV) {
		return idtra.trim()+ codiceIUV.trim()+ ".pdf";
	}
}
