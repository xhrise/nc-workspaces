package com.ufsoft.iufo.fmtplugin.formula;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.table.DefaultTableModel;

import nc.ui.pub.dsmanager.ParameterSetPanel;

import com.ufida.dataset.DataSet;
import com.ufida.dataset.Parameter;
import com.ufida.dataset.ParameterAnalyzer;
import com.ufsoft.report.UfoReport;

/*
 * 数据集参数条件定义面板.
 * Creation date: (2008-09-24 15:39:08)
 * @author: chxw
 */
public class UfoParameterSetPanel extends ParameterSetPanel {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 初始化一个空的参数定义面板
	 * @param ufoReport
	 */
	public UfoParameterSetPanel(UfoReport ufoReport) {
		super(ufoReport);
	}	

	public void setDataset(DataSet dataset, Hashtable<String, String> paramCondFilters) {
		super.setDataset(dataset);
		if(paramCondFilters == null || paramCondFilters.size() == 0){
			return;
		}
		Parameter[] usedParams = getParams();
		Enumeration<String> enumParamNames = paramCondFilters.keys();
		while(enumParamNames.hasMoreElements()){
			String name = enumParamNames.nextElement();
			for(Parameter p:usedParams){
				if(name.equals(p.getName())){
					p.setValue(paramCondFilters.get(p.getName()));
					break;
				}				
			}
		}
	}
	
	/**
	 * 参数过滤条件
	 * @return
	 */
	public String getParamFilter(){
		this.validateParams();
		DefaultTableModel dataModel =  (DefaultTableModel)getTM();
		int nRow = dataModel.getRowCount();
		StringBuffer bufParamCord = new StringBuffer();
		for(int row = 0; row < nRow; row++){
			String strParamName = (String)dataModel.getValueAt(row, 1);
			String strParamValue = dataModel.getValueAt(row, 3).toString();
			String strParamOpr = dataModel.getValueAt(row, 2).toString();
			bufParamCord.append("'");
			bufParamCord.append(strParamName);
			bufParamCord.append("'").append(strParamOpr);
			boolean isAddQuot = isAddQuotes(strParamName);
			if(isAddQuot) bufParamCord.append("'");
			bufParamCord.append(strParamValue);
			if(isAddQuot) bufParamCord.append("'");
			if(row < nRow-1) bufParamCord.append(" AND ");
		}
		return bufParamCord.toString();
		
	}
	
	/**
	 * 参数值是否字符类型
	 * @param strParamName
	 * @return
	 */
	private boolean isAddQuotes(String strParamName){
		DataSet ds = getDataset();
		if(ds == null || ds.getParameterByCapture(strParamName) == null){
			return false;
		}
		Parameter param = ds.getParameterByCapture(strParamName);
		if(param.getDataType() == ParameterAnalyzer.STRING ||
				param.getDataType() == ParameterAnalyzer.COMBO_STRING ||
				param.getDataType() == ParameterAnalyzer.REF_ID ||
				param.getDataType() == ParameterAnalyzer.REF_CODE ||
				param.getDataType() == ParameterAnalyzer.REF_NAME ||
				param.getDataType() == ParameterAnalyzer.REF_PARAM ||
				param.getDataType() == ParameterAnalyzer.REF_IUFO ||
				param.getDataType() == ParameterAnalyzer.REPLACE_PARAM){
			return true;
		}
		return false;
	}
	
}
