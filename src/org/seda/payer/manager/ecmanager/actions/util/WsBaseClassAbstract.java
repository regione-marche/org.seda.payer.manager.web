/**
 * 
 */
package org.seda.payer.manager.ecmanager.actions.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author lmontesi
 *
 */
@JsonDeserialize(as=WebServiceOutput.class)
public abstract class WsBaseClassAbstract implements WsBaseInterface, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String modelName ;
	private String status ;
	private String orderBy ;
	private String value ;
	private String errorCode ;
	@JsonInclude(JsonInclude.Include.NON_NULL) 
	private List<String> errorParams;
	private int totalPages;
	private int totalRows;
	private int pageSize;
	private int pageNumber;
	
	
	
	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public String getModelName() {
		return modelName;
	}

	public String getStatus() {
		return status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public String getValue() {
		return value;
	}

	
	@Override
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	@Override
	public void setStatus(String status) {
		this.status=status;
	}

	@Override
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public void setValue(String value) {
		this.value=value;
	}
	
	
	
	
	
	

	public List<String> getErrorParams() {
		return errorParams;
	}

	public void setErrorParams(List<String> errorParams) {
		this.errorParams = errorParams;
	}
	
	public void addErrorParam(String errorParam) {
		if(errorParams==null) {this.errorParams=new ArrayList<String>();}
		errorParams.add(errorParam);
	}

	
}
