package org.seda.payer.manager.ws;

//import com.seda.logwriter.webservices.dati.LogWriterRequestType;
//import com.seda.logwriter.webservices.dati.LogWriterRequestTypeLogLevel;
//import com.seda.logwriter.webservices.source.LogWriterSOAPBindingStub;
//import com.seda.logwriter.webservices.source.LogWriterServiceLocator;


public class LogWriter
{
//	private LogWriterSOAPBindingStub lwCaller = null;
	
	private final static String CONTEXT = "LogPayerManagerWeb";
	
	public static String LOG_ALL = "ALL"; //LogWriterRequestTypeLogLevel._ALL;
	public static String LOG_ERROR = "ERROR"; // LogWriterRequestTypeLogLevel._ERROR;
	public static String LOG_WARN = "WARN"; // LogWriterRequestTypeLogLevel._WARN;
	public static String LOG_INFO = "INFO"; // LogWriterRequestTypeLogLevel._INFO;
	public static String LOG_TRACE = "TRACE"; // LogWriterRequestTypeLogLevel._TRACE;
	public static String LOG_DEBUG = "DEBUG"; // LogWriterRequestTypeLogLevel._DEBUG;
	public static String LOG_FATAL = "FATAL"; // LogWriterRequestTypeLogLevel._FATAL;


	public LogWriter(String endPoint) {
//		LogWriterServiceLocator lwService = new LogWriterServiceLocator();
//		lwService.setLogWriterPortEndpointAddress(endPoint);
//		try  {
//			lwCaller = (LogWriterSOAPBindingStub)lwService.getLogWriterPort();
//		}
//		catch (ServiceException e) {
//			e.printStackTrace();
//		}
	} 


	public void doLog(String logLevel, String logDataText) {
		
//		if (lwCaller != null) {
//			LogWriterRequestType lwBean = new LogWriterRequestType();
//
//			lwBean.setLogContextName(CONTEXT);
//			lwBean.setLogLevel(LogWriterRequestTypeLogLevel.fromString(logLevel));
//			lwBean.setLogDataText(logDataText);
//
//			try{
//				lwCaller.getLogWriter(lwBean);
//			}
//
//			catch (RemoteException ex) {
//				//ex.printStackTrace();
//			}
//		}
//		else {
//			System.out.println("Cannot access webservice. Unknown cause.");
//		}
		
		doLog(CONTEXT, logLevel, logDataText);
		
	}

	public void doLog(String contextName, String logLevel, String logDataText) {
		
//		if (lwCaller != null) {
//			LogWriterRequestType lwBean = new LogWriterRequestType();
//
//			lwBean.setLogContextName(contextName);
//			lwBean.setLogLevel(LogWriterRequestTypeLogLevel.fromString(logLevel));
//			lwBean.setLogDataText(logDataText);
//
//			try {
//				lwCaller.getLogWriter(lwBean);
//			}
//
//			catch (RemoteException ex) {
//			}
//		}
//		else {
//			System.out.println("Cannot access webservice. Unknown cause.");
//		}

		System.out.println(String.format("%s [%s] %s", logLevel, contextName, logDataText));

	}
	public void logError(String message, Exception e){
		doLog(LOG_ERROR, message + " Exception: " + e);
	}

	public void logInfo(String message)	{
		doLog(LOG_INFO, message);
	}

	public void logDebug(String message)	{
		doLog(LOG_DEBUG, message);
	}
}
