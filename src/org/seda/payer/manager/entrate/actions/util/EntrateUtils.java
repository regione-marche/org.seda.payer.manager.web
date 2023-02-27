package org.seda.payer.manager.entrate.actions.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.sql.rowset.WebRowSet;

import org.seda.payer.manager.components.config.ManagerStarter;
import org.seda.payer.manager.components.security.UserBean;
import org.seda.payer.manager.entrate.actions.GestioneDocumentiCaricoAction;
import org.seda.payer.manager.entrate.actions.GestioneDocumentiCaricoAction.SessionState;
import org.seda.payer.manager.util.ManagerKeys;
import org.seda.payer.manager.util.PropertiesPath;

import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.ConfigurazioneIUV;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Scadenza;
import com.esed.payer.archiviocarichi.webservice.integraecdifferito.dati.Tributo;
//import com.google.gson.ExclusionStrategy;
//import com.google.gson.FieldAttributes;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import com.seda.commons.properties.tree.PropertiesTree;
import com.seda.commons.string.Convert;
import com.seda.j2ee5.maf.core.security.SignOnKeys;
import com.seda.j2ee5.maf.util.MAFAttributes;
import com.seda.j2ee5.maf.util.MAFRequest;

/** utility es. JSON, provvisorie per lo sviluppo. Tuttavia le conservo per futuri debug  */
public class EntrateUtils {

//  public static class FieldExclusionStrategy implements ExclusionStrategy {
//    String fieldName;
//
//    public FieldExclusionStrategy(String s) {
//      fieldName = s;
//    }
//
//    public boolean shouldSkipClass(Class<?> arg0) {
//      return false;
//    }
//
//    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
//      String fName = fieldAttributes.getName();
//      return fieldName.equals(fName);
//    }
//  }

//  public static class SuperclassExclusionStrategy implements ExclusionStrategy {
//    public boolean shouldSkipClass(Class<?> arg0) {
//      return false;
//    }

//    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
//      String fieldName = fieldAttributes.getName();
//      Class<?> theClass = fieldAttributes.getDeclaringClass();
//
//      return isFieldInSuperclass(theClass, fieldName);
//    }

//    private boolean isFieldInSuperclass(Class<?> subclass, String fieldName) {
//      Class<?> superclass = subclass.getSuperclass();
//      Field field;
//
//      while (superclass != null) {
//        field = getField(superclass, fieldName);
//
//        if (field != null)
//          return true;
//
//        superclass = superclass.getSuperclass();
//      }
//
//      return false;
//    }

//    private Field getField(Class<?> theClass, String fieldName) {
//      try {
//        return theClass.getDeclaredField(fieldName);
//      } catch (Exception e) {
//        return null;
//      }
//    }
//  }

  /*************************************************************************
   * Funzioni di copiate dal framework, per fare qualche prova e capire
   * come funziona... ma continuo ad utilizzare le funzioni del framework.
   *************************************************************************/
  /***
   * Restituisce il nome del template dell'applicazione corrente
   * @param request
   * @param session
   * @return
   */
  public static String getTemplateCurrentApplication(HttpServletRequest request) {
    HttpSession session = request.getSession();
    MAFRequest mafReq = new MAFRequest(request);
    String applicationName = (String) mafReq.getAttribute(MAFAttributes.CURRENT_APPLICATION);

    if (applicationName != null) {
      UserBean userBean = (UserBean) session.getAttribute(SignOnKeys.USER_BEAN);
      if (userBean != null) {
        String templateName = userBean.getTemplate(applicationName);
        if (templateName != null && !templateName.equals(""))
          return templateName;
        else
          return "default";
      }
    } else {
      // recupero il name del template dal file di properties, tramite serverName
      if (ManagerStarter.configuration != null) {
        String serverName = request.getServerName();

        String templateName = ManagerStarter.configuration.getProperty(PropertiesPath.templateBase
            .format()
            + serverName);

        if (templateName != null && !templateName.equals(""))
          return templateName;
      }
    }
    return "default";
  }

  /** Immagino sia il vecchio Nome... perché ci accede facendo questo giro?  */
  public static String getCodSocieta(HttpServletRequest request) {

    HttpSession session = request.getSession();

    String dbSchemaCodSocieta = (String) session.getAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA);
    if (dbSchemaCodSocieta == null) {
      dbSchemaCodSocieta = getConfiguration(session).getProperty(
          PropertiesPath.societa.format(getTemplateCurrentApplication(request)));
      session.setAttribute(ManagerKeys.DBSCHEMA_CODSOCIETA, dbSchemaCodSocieta);
    }

    return dbSchemaCodSocieta;
  }

  static PropertiesTree configuration;

  static PropertiesTree getConfiguration(HttpSession session) {
    if (configuration == null)
      configuration = (PropertiesTree) session.getServletContext().getAttribute(
          ManagerKeys.CONTEXT_PROPERTIES_TREE);
    return configuration;
  }

  /** creo e configuro defaults */
  static public GestioneDocumentiCaricoAction.StatoEditDocumento createEmptyStatoEditDocumento(SessionState sessionState) {
	  GestioneDocumentiCaricoAction.StatoEditDocumento editDocumento = new GestioneDocumentiCaricoAction.StatoEditDocumento();
	  
	  editDocumento.configurazione.setFlagGenerazioneIUV("N");
	  editDocumento.configurazione.setFlagStampaAvviso("N");
	  
	  editDocumento.configurazione.setConfigurazioneIUV(null);
//	  editDocumento.configurazioneIUV =  new ConfigurazioneIUV();  
	  editDocumento.setKeyEnte("");
	  editDocumento.configurazione.setIdentificativoFlusso("");
	  sessionState.anagrafica_siglaProvinciaFiscale = "";
	  sessionState.anagrafica_siglaProvinciaNascita = "";
	  sessionState.editDocumento.setKeyEnte("");
	  
	  return editDocumento;
  }

  public static String webrowsetToCsv(String webRowSetXml) {
	//inizio LP PG21XX04 Leak
    WebRowSet wrs = null;
	//fine LP PG21XX04 Leak
    try {
      //inizio LP PG21XX04 Leak
      //WebRowSet wrs = Convert.stringToWebRowSet(webRowSetXml);
      wrs = Convert.stringToWebRowSet(webRowSetXml);
      //fine LP PG21XX04 Leak
      ResultSetMetaData meta = wrs.getMetaData();
      int colCount = meta.getColumnCount();
      String s = "";
      for (int i = 1; i <= colCount; i++) {
        if (i > 1)
          s += ",";

        s += meta.getColumnName(i);
        s += "[" + i + "]";
      }
      s += "\n";
      while (wrs.next()) {
        for (int i = 1; i <= colCount; i++) {
          if (i > 1)
            s += ",";
          s += wrs.getString(i);
        }
        s += "\n";
      }
      return s;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
	//inizio LP PG21XX04 Leak
	finally {
		try {
			if(wrs != null) {
				wrs.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	//fine LP PG21XX04 Leak
  }

//  static Gson gson;
  static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
  static SimpleDateFormat sdfInversa = new SimpleDateFormat("yyyy-MM-dd");

//  static public Gson getGson() {
//    if (gson == null) {
//      GsonBuilder builder = new GsonBuilder();
//      builder.setPrettyPrinting();
//      builder.addDeserializationExclusionStrategy(new SuperclassExclusionStrategy());
//      builder.addSerializationExclusionStrategy(new SuperclassExclusionStrategy());
//      builder.addSerializationExclusionStrategy(new FieldExclusionStrategy("__equalsCalc"));
//      gson = builder.create();
//    }
//    return gson;
//  }

  /** Elimino i paramtri duplicati negli attributes */
  static public void removeParametriFromAttributes(HttpServletRequest request) {
    Enumeration<String> e = request.getParameterNames();
    while (e.hasMoreElements()) {
      String p = (String) e.nextElement();
      request.removeAttribute(p);
    }
    return;
  }

  public static String parametersToString(HttpServletRequest request) {
    String s = "";
    TreeSet<String> parameterNames = new TreeSet<String>(Collections.list(request
        .getParameterNames()));

    for (String name : parameterNames)
      s += name + "=" + request.getParameter(name) + "\n"; // primo valore
    return s;
  }

  public static String attributesToString(HttpServletRequest request) {
    String s = "";
    TreeSet<String> attibuteNames = new TreeSet<String>(Collections.list(request
        .getAttributeNames()));

    for (String name : attibuteNames) {
      // tronco alla prima linea di una stringa multilinea... es. XML rowset
      Object attributeValue = request.getAttribute(name);
      String attributeStringValue = attributeValue.toString();
      int newLineIndex = attributeStringValue.indexOf("\n");
      if (newLineIndex > 0)
        attributeStringValue = attributeStringValue.substring(0, newLineIndex) + " ...";
      s += name + "=" + attributeStringValue + "\n"; // primo valore
    }
    return s;
  }

  /**
   * Questo metodo restituisce un oggetto calendar a partire dal valore di un
   * tag SEDA "date" individuato dal parametro "prefix" (lo stesso che viene
   * utilizzato nel tag "date" di SEDA)
   * 
   * @param request
   * @param prefix
   * @return calendar
   */
  static public Calendar calendarFromFields(HttpServletRequest request, String prefix) {

    Locale locale = (Locale) request.getSession().getAttribute(MAFAttributes.LOCALE);
    Calendar cal = null;
    if (prefix != null && !prefix.equals("")) {
      String giorno = request.getParameter(prefix + "_day");
      String mese = request.getParameter(prefix + "_month");
      String anno = request.getParameter(prefix + "_year");
      if (giorno != null && mese != null && anno != null && !giorno.trim().equals("")
          && !mese.trim().equals("") && !anno.trim().equals("")) {
        cal = Calendar.getInstance(locale);
        cal.set(Integer.parseInt(anno), Integer.parseInt(mese) - 1, Integer.parseInt(giorno));
      }
    }
    return cal;
  }

  /**
   * @throws IllegalArgumentException
   **/
  static public Calendar calendarFromDDMMYYYY(String data) {
    if (data == null)
      return null;

    Calendar cal = new GregorianCalendar();
    java.util.Date sdate = null;
    if (data != null) {
      try {
        sdate = sdf.parse(data);
      } catch (ParseException ex) {
        throw new IllegalArgumentException(ex);
      }
      cal.setTime(sdate);
    }
    return cal;
  }

  static public String calendarToDDMMYYYY(Calendar cal) {
    if (cal == null)
      return null;
    return sdf.format(cal.getTime());
  }

  static public Calendar calendarFromYYYY_MM_DD(String data) {
    if (data == null)
      return null;

    Calendar cal = new GregorianCalendar();
    java.util.Date sdate = null;
    if (data != null) {
      try {
        sdate = sdfInversa.parse(data);
      } catch (ParseException ex) {
        throw new IllegalArgumentException(ex);
      }
      cal.setTime(sdate);
    }
    return cal;
  }

  static public String dateDDMMYYYYFromFields(HttpServletRequest request, String prefix) {
	  Calendar calendarFromFields = calendarFromFields(request, prefix);
	  if (calendarFromFields == null)
		  return null;
	  return sdf.format(calendarFromFields.getTime());
  }

  static public String dateYYYYMMDDFromFields(HttpServletRequest request, String prefix) {
    Calendar calendarFromFields = calendarFromFields(request, prefix);
    if (calendarFromFields == null)
      return null;
    return sdfInversa.format(calendarFromFields.getTime());
  }
}
