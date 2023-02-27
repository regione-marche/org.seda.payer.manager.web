package org.seda.payer.manager.reports.stampe;


import java.util.logging.Level;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;


import com.seda.commons.properties.tree.PropertiesNodeException;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.compatibility.SystemVariable;


public class BirtEngine {
	public static PropertiesTree configuration;
	IReportEngine engine=null;
	EngineConfig config = null;

	private static BirtEngine me;

	
	 
	public IReportEngine getEngine() {
		return engine;
	}

	static {
		me=new BirtEngine();
	}

	public static BirtEngine instance() {
		return me;
	}

	private BirtEngine() {
		
		try{
			config = new EngineConfig( );
			startEngine();
			config.setEngineHome(getBirtHomeEngine());
			config.setLogConfig(getBirtHomeLogs(), Level.WARNING);
			config.setResourcePath(getBirtImagePath());  
			
			System.out.println( getBirtImagePath() );//FIXME PG130100
			
			Platform.startup(config);  //If using RE API in Eclipse/RCP application this is not needed.
			IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
			engine = factory.createReportEngine( config ); 
			//engine.changeLogLevel(Level.WARNING);
		}
		catch( Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public synchronized void destroyBirtEngine() {
		if (engine == null) {
			return;
		}  
		engine.destroy();
		Platform.shutdown();
		engine = null;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	public void startEngine() throws PropertiesNodeException  {
		
		SystemVariable sv = new SystemVariable();
		String rootPath=sv.getSystemVariableValue(ManagerKeys.ENV_CONFIG_FILE);
		sv=null;
		 
		if (rootPath!=null) {
				// caricamento configurazioni esterne
			configuration = new PropertiesTree(rootPath);
		}
	}
	
	public static String getBirtHomeEngine() {
		return configuration.getProperty(PropertiesPath.birthomeengine.format());
	}
	public static String getBirtHomeLogs() {
		return configuration.getProperty(PropertiesPath.birthomelogs.format());
	}

	public static String getBirtImagePath() {
		return configuration.getProperty(PropertiesPath.birtImagePath.format());		//FIXME PG130100
	}
	
	
}
