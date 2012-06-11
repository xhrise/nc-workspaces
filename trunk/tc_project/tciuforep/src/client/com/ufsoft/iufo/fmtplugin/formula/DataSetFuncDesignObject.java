package com.ufsoft.iufo.fmtplugin.formula;

import java.util.Hashtable;
import java.util.Vector;

import nc.vo.iufo.datasetmanager.DataSetDefVO;
import nc.vo.pub.dsmanager.DataSetDesignObject;

import com.ufida.dataset.Condition;
import com.ufida.dataset.metadata.Field;
import com.ufsoft.script.extfunc.DataSetFunc;
import com.ufsoft.script.extfunc.DataSetFuncDriver;
import com.ufsoft.script.extfunc.LimitRowNumber;

/*
 * ���ݼ����������������ݽṹ.
 * Creation date: (2008-06-24 15:39:08)
 * @author: chxw
 */
public class DataSetFuncDesignObject extends DataSetDesignObject {
	//��ǰ�༭�����ݼ���ʽ
	private DataSetFunc m_objEditedDataSetFunc = null;
	
	// ��ǰ���ݼ���ѯԪ����
	private Vector<String> m_curMetaDatas = new Vector<String>();
	
	// �༭״̬��1-������2-�޸ģ�
	private int status = STATUS_CREATE;

	//���ݼ�����������������������
	private String strFuncRowCord = null; 
	private String strFuncLimitRowCord = null; 
	private String strFuncParamCord = null;
	
	//��ʱ������Ϣ:������������������������������
	private LimitRowNumber limitRowNumber = null;
	private Hashtable<String, Condition[]> rowConds = null;
	private Hashtable<String, String> paramConds = null;
	
	public void setCurDataSetDef(DataSetDefVO curDataSetDef) {
		super.setCurDataSetDef(curDataSetDef);
		clearMetaDatas();
	}

	public void setParamCordDef(
			Hashtable<String, Condition[]> rowCondFilters, 
			Hashtable<String, String> paramCondFilters, 
			LimitRowNumber limitRowNumber) {
		this.rowConds = rowCondFilters;
		this.paramConds = paramCondFilters;
		this.limitRowNumber = limitRowNumber;	
	}
	
	public DataSetFunc getEditedDataSetFunc() {
		return m_objEditedDataSetFunc;
	}

	public void setEditedDataSetFunc(DataSetFunc dataSetFunc) {
		this.m_objEditedDataSetFunc = dataSetFunc;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void removeMetaData(String metaData){
		m_curMetaDatas.remove(metaData);
	}
	
	public Vector<String> getMetaDatas(){
		return m_curMetaDatas;
	}
	
	public void clearMetaDatas(){
		m_curMetaDatas.clear();
	}
	
	public String getFuncRowCord() {
		return strFuncRowCord;
	}

	public void setFuncRowCord(String strFuncRowCord) {
		this.strFuncRowCord = strFuncRowCord;
	}

	public String getFuncLimitRowCord() {
		return strFuncLimitRowCord;
	}

	public void setFuncLimitRowCord(String strFuncLimitRowCord) {
		this.strFuncLimitRowCord = strFuncLimitRowCord;
	}
	
	public String getFuncParamCord() {
		return strFuncParamCord;
	}

	public void setFuncParamCord(String strFuncParamCord) {
		this.strFuncParamCord = strFuncParamCord;
	}

	public LimitRowNumber getLimitRowNumber() {
		return limitRowNumber;
	}

	public Hashtable<String, String> getParamConds() {
		return paramConds;
	}

	public Hashtable<String, Condition[]> getRowConds() {
		return rowConds;
	}
	
	@Override
	public String toString() {
		if(getCurDataSetDef() == null){
			return null;
		}
		
		StringBuffer datasetFuncStr = new StringBuffer();
		datasetFuncStr.append(DataSetFuncDriver.GETDATA).append("(");
		
		// �������ݼ�����
		datasetFuncStr.append("'").append(getCurDataSetDef().getCode()).append("'");
		datasetFuncStr.append(",'");
		// �����ֶ�����
		if(getCurDataSetDef().getDataSetDef().getMetaData().getFields() != null){
			for(Field field : getCurDataSetDef().getDataSetDef().getMetaData().getFields()){
				datasetFuncStr.append(field.getCaption()).append(",");
			}
			datasetFuncStr.deleteCharAt(datasetFuncStr.length() - 1);
		}
		datasetFuncStr.append("'");
		// �����������
		boolean hasFieldCord = strFuncRowCord != null && strFuncRowCord.trim().length() > 0;
		boolean hasParamCord = strFuncParamCord != null  && strFuncParamCord.trim().length() > 0;
		boolean hasLimitRowCord = strFuncLimitRowCord != null  && strFuncLimitRowCord.trim().length() > 0;
		if(hasFieldCord || hasParamCord || hasLimitRowCord) datasetFuncStr.append(",");
		if(hasFieldCord){
			datasetFuncStr.append(strFuncRowCord);
		}
		if(hasLimitRowCord){
			datasetFuncStr.append(",");
			datasetFuncStr.append(strFuncLimitRowCord);
		}
		if(hasParamCord){
			if(!hasFieldCord && !hasLimitRowCord){
				datasetFuncStr.append(",");
			} else if(hasFieldCord && !hasLimitRowCord){
				datasetFuncStr.append(",");
			}
			datasetFuncStr.append(",");
			datasetFuncStr.append(strFuncParamCord);
		}		
		datasetFuncStr.append(")");
		return datasetFuncStr.toString();
	}
	
}
