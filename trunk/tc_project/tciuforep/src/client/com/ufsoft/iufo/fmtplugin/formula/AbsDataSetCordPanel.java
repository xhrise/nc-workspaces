package com.ufsoft.iufo.fmtplugin.formula;

import nc.ui.pub.beans.UIPanel;

import com.ufida.dataset.DataSet;
import com.ufida.dataset.IContext;
import com.ufsoft.table.CellsModel;

/*
 * 取数条件定义抽象面板.
 * Creation date: (2008-07-07 15:39:08)
 * @author: chxw
 */
public abstract class AbsDataSetCordPanel extends UIPanel {
	private static final long serialVersionUID = 1L;

//	private UfoReport m_ufoReport = null;
	CellsModel cellsModel = null;
	
	IContext context = null;
	
	private DataSet m_dataSet = null;

	public AbsDataSetCordPanel(CellsModel cellsModel,IContext context) {
		this.cellsModel = cellsModel;
		this.context = context;
//		m_ufoReport = ufoReport;
	}

//	public UfoReport getUfoReport(){
//		return m_ufoReport;
//	}
	
	protected DataSet getDataSet() {
		return m_dataSet;
	}

	protected void setDataSet(DataSet dataSet) {
		this.m_dataSet = dataSet;
	}

	protected CellsModel getCellsModel() {
		return cellsModel;
	}
	
	protected IContext getContext() {
		return context;
	}

}
