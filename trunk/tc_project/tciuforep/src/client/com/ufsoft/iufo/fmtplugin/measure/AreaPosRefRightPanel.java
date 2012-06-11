package com.ufsoft.iufo.fmtplugin.measure;

import java.util.ArrayList;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.MeasureCache;
import nc.pub.iufo.cache.RepFormatModelCache;
import nc.pub.iufo.cache.ReportCache;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.table.CellPosition;

/**
 * 单元编号参照Panel
 * @author chxw
 */
public abstract class AreaPosRefRightPanel extends nc.ui.pub.beans.UIPanel {
	protected ReportCache reportCache = CacheProxy.getSingleton().getReportCache();
	protected MeasureCache measureCache = CacheProxy.getSingleton().getMeasureCache();
	protected KeyGroupCache keyGroupCache = CacheProxy.getSingleton().getKeyGroupCache();
	protected RepFormatModelCache formatCache = CacheProxy.getSingleton().getRepFormatCache();
	
	private String _repCode = "";	//选中的报表编码
	private AreaPosRefDlg _parentDlg;
	private KeyGroupVO m_oCurrentKeyGroupVO;
	private ArrayList _excludeCellPosList;
	private ReportVO _reportVO = null;

	private boolean _isContainsCurrentReport;
	
	final protected void changeReport(ReportVO reportVO){
		_reportVO = reportVO;
		if(_reportVO != null){
			setReportCode(_reportVO.getCode());
			changeReportImpl(_reportVO);
		}
	}
	
	abstract protected void changeReportImpl(ReportVO reportVO);
	abstract protected CellPosition getSelectedCellPosition();
	
	protected ReportVO getCurReportVO(){
		return _reportVO;
	}
	
	public AreaPosRefRightPanel(AreaPosRefDlg parentDlg, boolean isContainsCurrentReport, KeyGroupVO currentKeyGroupVO, ArrayList excludeCellPosList) {
		super();
		_isContainsCurrentReport = isContainsCurrentReport;
		_parentDlg = parentDlg;
		m_oCurrentKeyGroupVO = currentKeyGroupVO;
		_excludeCellPosList = excludeCellPosList;
	}
	protected void closeWithOKResult() {
		_parentDlg.setResult(UfoDialog.ID_OK);				
		_parentDlg.close();						
	}

	protected boolean isAutoClose() {
		return _isContainsCurrentReport;
	}
	protected boolean isContainsCurrentReport(){
		return _isContainsCurrentReport;
	}
	protected void setReportCode(String repCode){
		_repCode = repCode;
	}
	public String getStrRefCellPos() {
		CellPosition selCellPos = getSelectedCellPosition();
		if (selCellPos != null)
			return "'" + _repCode + "[" + selCellPos.toString() + "]'";
		else
			return "";
	}
	
	protected boolean isPermitDupRefer() {
		return isContainsCurrentReport();
	}
	protected boolean isReferenced(String code) {
		return _excludeCellPosList.contains(code);
	}

}
