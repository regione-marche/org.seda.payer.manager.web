package org.seda.payer.manager.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletContext;

import com.seda.j2ee5.maf.core.application.ApplicationsData;
import com.seda.j2ee5.maf.defender.repository.GlobalRuleSet;
import com.seda.j2ee5.maf.defender.repository.RegexSet;
import com.seda.j2ee5.maf.defender.rule.Regex;
import com.seda.j2ee5.maf.util.MAFAttributes;

public class RuleSetManager {

	public RuleSetManager(){}
	/**
	 * Legge le regex definite nei file "maf-actions.xml" e le rende disponibili
	 * nel contesto in modo che possano essere utilizzate direttamente nelle pagine JSP - 
	 * Ogni regola può essere recuperata dal contesto tramite il parametro "NomeApplicazione_NomeRegola"
	 * @param context
	 */
	public static synchronized void putGlobalRuleSetMapInContext(ServletContext context)
	{
		HashMap<String, GlobalRuleSet> globalRuleSetMap = null;

		ApplicationsData applicationsData = (ApplicationsData)context.getAttribute(MAFAttributes.APPLICATIONS);
        if (applicationsData!=null) {
              globalRuleSetMap=applicationsData.getGlobalRuleSetMap();
        }
        if (globalRuleSetMap != null)
        {
            context.setAttribute(ManagerKeys.GLOBAL_RULE_SET_MAP, "OK");
            Set<String> setAppl = globalRuleSetMap.keySet();
            Iterator<String> applIterator = setAppl.iterator();
            /*
             * Itero su tutte le applicazioni
             */
            while (applIterator.hasNext())
            {
            	String appl = applIterator.next();
            	GlobalRuleSet applRules = globalRuleSetMap.get(appl);
            	/*
            	 * elimino il carattere iniziale "/"
            	 */
            	appl = appl.substring(1);
            	RegexSet regexSet = applRules.getRegexSet();
            	if (regexSet != null)
            	{
                	/*
                	 * Itero su tutte le regole dell'applicazione
                	 */
                	Enumeration<String> regexEnum = regexSet.getRegexSetKeys();
                	while (regexEnum.hasMoreElements())
                	{
                		String key = regexEnum.nextElement();
                		String value = regexSet.getRegex(key).getRegex();
                		context.setAttribute(appl + "_" + key, value);
                	}
            	}
            }
        }
        else
        	System.out.println("ERRORE: GlobalRuleSetMap NULLO");
        
	}
}
