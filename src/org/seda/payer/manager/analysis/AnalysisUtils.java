package org.seda.payer.manager.analysis;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpSession;

import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.seda.commons.properties.tree.PropertiesTree;

public final class AnalysisUtils {
	
	public static void setJVMProperty(HttpSession session) {
		
		PropertiesTree configuration = (PropertiesTree)session.getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
//		System.setProperty("analysis.path_cubo", configuration.getProperty(PropertiesPath.path_cubo.format()).replaceFirst("\\Qfile:///\\E", "").replaceFirst("\\Qfile:\\E", ""));
	}
	
//	public static void deleteJPivotFiles() {
//		
//		File directory = new File(System.getProperty("analysis.path_cubo"));
//		File[] toBeDeleted = directory.listFiles(new FilenameFilter() {
//			public boolean accept(File dir, String name) {
//				return name.endsWith(".png");
//			}
//		});
//		for (File file : toBeDeleted) {
//			file.delete();
//		}
//	}

	public static void deleteCubeFile(HttpSession session) {
		
		if (session.getAttribute("analysis.filename")!= null)
		{
			PropertiesTree configuration = (PropertiesTree)session.getServletContext().getAttribute(ManagerKeys.CONTEXT_PROPERTIES_TREE);
			String cuboPath = configuration.getProperty(PropertiesPath.path_cubo.format());
			try
			{
				File cuboFile = new File( new URI(cuboPath + "/" + session.getAttribute("analysis.filename")));
				cuboFile.delete();
				
			}
			catch (URISyntaxException e) {
				//e.printStackTrace();
			}
			finally
			{
				session.setAttribute("analysis.filename", null);
			}
		}
	}
}
