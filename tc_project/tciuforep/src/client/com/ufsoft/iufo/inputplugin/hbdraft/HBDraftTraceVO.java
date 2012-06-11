package com.ufsoft.iufo.inputplugin.hbdraft;

import java.io.Serializable;


public class HBDraftTraceVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7529858173994308084L;

	private String colType;
	
	private String measurePK;
	
	private String unitId;
	
	private String taskId;
	
	private String reportId;
	
	private String AloneId;
	
	private int ver;

	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	public String getMeasurePK() {
		return measurePK;
	}

	public void setMeasurePK(String measurePK) {
		this.measurePK = measurePK;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public int getVer() {
		return ver;
	}

	public void setVer(int ver) {
		this.ver = ver;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getAloneId() {
		return AloneId;
	}

	public void setAloneId(String aloneId) {
		AloneId = aloneId;
	}

}
