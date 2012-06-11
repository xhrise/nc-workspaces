package com.ufida.report.spreedsheet.applet;

import nc.vo.bi.query.manager.QueryModelVO;

import com.ufida.report.multidimension.model.SelDimModel;
import com.ufsoft.table.CellPosition;

public class SelQueryAreaInfo {
	private QueryModelVO m_selQuery = null;
	private SelDimModel m_selDimModel = null;
	private CellPosition m_position = null;
	public CellPosition getCellPosition() {
		return m_position;
	}
	public void setCellPosition(CellPosition m_position) {
		this.m_position = m_position;
	}
	public SelDimModel getSelDimModel() {
		return m_selDimModel;
	}
	public void setSelDimModel(SelDimModel dimModel) {
		m_selDimModel = dimModel;
	}
	public QueryModelVO getSelQuery() {
		return m_selQuery;
	}
	public void setSelQuery(QueryModelVO query) {
		m_selQuery = query;
	}

}
