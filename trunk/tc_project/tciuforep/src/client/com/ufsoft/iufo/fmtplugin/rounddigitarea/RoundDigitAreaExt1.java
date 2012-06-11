package com.ufsoft.iufo.fmtplugin.rounddigitarea;

import java.awt.Component;

import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IStatusBarExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.SelectModel;

public class RoundDigitAreaExt1 extends AbsActionExt {
	private UfoReport m_report;//报表工具
	
	public RoundDigitAreaExt1(UfoReport report) {
		m_report = report;
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(StringResource.getStringResource("uiiufofmt00070"));
        uiDes1.setGroup("styleMngExt");
        uiDes1.setPaths(new String[]{MultiLang.getString("format")});
        return new ActionUIDes[]{uiDes1};
	}

	public UfoCommand getCommand() {
		return new RoundDigitAreaCmd1(this);
	}

	public Object[] getParams(UfoReport container) {
		return null;
	}

	/**
	 * 检查选择区域单元是否为非舍位区域
	 * @return null-不全是;Boolean.TRUE-是;Boolean.FALSE-否!
	 */
	public Boolean isUnRoundDigitArea(){
		CellPosition[] selPoss = getSelectModel().getSelectedCells();
		boolean isHasUnRoundDigitPos = false;
		boolean isHasRoundDigitPos = false;
		for (int i = 0; i < selPoss.length; i++) {
			if(getRoundDigitAreaModel().isUnRoundDigitPos(selPoss[i])){
				isHasUnRoundDigitPos = true;
			}else{
				isHasRoundDigitPos = true;
			}
			if(isHasUnRoundDigitPos && isHasRoundDigitPos){
				break;
			}
		}	
		if(isHasUnRoundDigitPos && isHasRoundDigitPos){
			return null;
		}else if(isHasUnRoundDigitPos){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
	}

	/**
	 * 设置选择区域单元是否为舍位区域
	 * @param b
	 */
	public void setUnRoundDigitArea(boolean b) {
		CellPosition[] cellPoss = getSelectModel().getSelectedCells();
		if(b){
			getRoundDigitAreaModel().addUnRoundDigitPos(cellPoss) ;
		}else{
			getRoundDigitAreaModel().removeUnRoundDigitPos(cellPoss);
		}
		getCellsModel().setDirty(true);
		getCellsModel().fireExtPropChanged(getSelectModel().getSelectedArea());
	}
	
	private CellsModel getCellsModel(){
		return m_report.getCellsModel();
	}
	
	private SelectModel getSelectModel(){
		return getCellsModel().getSelectModel();
	}
	
	private RoundDigitAreaModel getRoundDigitAreaModel(){
		return RoundDigitAreaModel.getInstance(getCellsModel());
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
	 */
	public boolean isEnabled(Component focusComp) {
		return isVisiable(focusComp);
	}

	/*
	 * @see com.ufsoft.report.plugin.IPopupMenuExt#isVisiable(java.awt.Component)
	 */
	public boolean isVisiable(Component focusComp) {
		return StateUtil.isFormat_CPane1THeader(m_report, focusComp);
	}
	
}
 