package com.ufsoft.iufo.fmtplugin.rounddigitarea;

import java.awt.Component;

import javax.swing.JLabel;

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
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.SelectModel;
import com.ufsoft.table.event.SelectEvent;

public class RoundDigitAreaExt extends AbsActionExt implements IStatusBarExt{
	private UfoReport m_report;//������

	public RoundDigitAreaExt(UfoReport report) {
		m_report = report;
	}

	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(StringResource.getStringResource("uiiufofmt00071"));
        uiDes1.setGroup("styleMngExt");
        uiDes1.setPaths(new String[]{MultiLang.getString("format")});
        return new ActionUIDes[]{uiDes1};
	}

	public UfoCommand getCommand() {
		return new RoundDigitAreaCmd(this);
	}

	public Object[] getParams(UfoReport container) {
		return null;
	}

	/**
	 * @i18n uiiufofmt00039=��λ����
	 */
	public String getStatusMark() {
		return StringResource.getStringResource("uiiufofmt00039");
	}

	/**
	 * @i18n uiiufofmt00040=��ȫ��
	 * @i18n miufopublic506=��
	 * @i18n miufopublic505=��
	 */
	public String getStatusValue() {
		Boolean isUnRoundDigitArea = isUnRoundDigitArea();
		if(isUnRoundDigitArea == null){
			return StringResource.getStringResource("uiiufofmt00040");
		}else if(isUnRoundDigitArea.equals(Boolean.TRUE)){
			return StringResource.getStringResource("miufopublic506");
		}else{
			return StringResource.getStringResource("miufopublic505");
		}
	}
	
	/**
	 * ���ѡ������Ԫ�Ƿ�Ϊ����λ����
	 * @return null-��ȫ��;Boolean.TRUE-��;Boolean.FALSE-��!
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
	
	public int getStatusPos() {
		return 1;
	}

	public void initListenerByComp(Component stateChangeComp) {
		if(!(stateChangeComp instanceof JLabel)) return; 
		
		final JLabel label = (JLabel) stateChangeComp;
		getSelectModel().addSelectModelListener(new SelectListener(){
			public void selectedChanged(SelectEvent e) {
				label.setText(getStatusMark()+":"+getStatusValue());
			}
		});
		getRoundDigitAreaModel().addModelChangeListener(new RoundDigitAreaModelListener(){
			public void modelChanged() {
				label.setText(getStatusMark()+":"+getStatusValue());
			}			
		});
		if(label.getText().equals("")){
			label.setText(getStatusMark() + ":" + StringResource.getStringResource("miufopublic358"));
		}
	}
	private SelectModel getSelectModel(){
		return getCellsMoel().getSelectModel();
	}
	private RoundDigitAreaModel getRoundDigitAreaModel(){
		return RoundDigitAreaModel.getInstance(getCellsMoel());
	}

	/**
	 * ����ѡ������Ԫ�Ƿ�Ϊ��λ����
	 * @param b
	 */
	public void setUnRoundDigitArea(boolean b) {
		CellPosition[] cellPoss = getSelectModel().getSelectedCells();
		if(b){
			getRoundDigitAreaModel().addUnRoundDigitPos(cellPoss) ;
		}else{
			getRoundDigitAreaModel().removeUnRoundDigitPos(cellPoss);
		}
		getCellsMoel().setDirty(true);
		getCellsMoel().fireExtPropChanged(getSelectModel().getSelectedArea());
	}
	
	private CellsModel getCellsMoel(){
		return m_report.getCellsModel();
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
 