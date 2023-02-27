package org.seda.payer.manager.ecmanager.actions.util;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author lmontesi
 *
 */
@XmlRootElement(name="data")
public class WebServiceOutput<T> extends WsBaseClassAbstract  {

	private static final long serialVersionUID = 1L;
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private List<T> elementList;
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private T element;
	
	public WebServiceOutput(){
		
	}
	
    public WebServiceOutput(String modelName){
    	this.setModelName(modelName);
    	this.setTotalPages(0);
    	this.setTotalRows(0);
    	this.setPageSize(0);
    	this.setPageNumber(0);
    	this.setStatus("OK");
	}
			
	public WebServiceOutput(
			List<T> elementList) {
		super();
		this.elementList = elementList;
	}
	
	public void addElement (T element) {
		
		if (this.elementList != null) {
			this.elementList.add(element);
		} else  {
			this.elementList = new ArrayList<T>();
			this.elementList.add(element);
		}
	}
	
	public List<T> getElementList() {
		return elementList;
	}
	
	@XmlElement(name = "rows")
	public void setElementList(
			List<T> elementList) {
		this.elementList = elementList;
	}
	
	

	public T getElement() {
		return element;
	}

	public void setElement(T element) {
		this.element = element;
	}

	@Override
	public String toString() {
		return "WebServiceOutput [elementList=" + elementList + ", element=" + element + "]";
	}

	
	

	
}
