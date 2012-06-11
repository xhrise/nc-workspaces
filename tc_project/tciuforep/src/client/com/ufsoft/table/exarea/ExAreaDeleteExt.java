package com.ufsoft.table.exarea;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.report.util.MultiLang;

public class ExAreaDeleteExt extends ExAreaExt {
	
	public ExAreaDeleteExt(ExAreaPlugin plugin) {
		super(plugin);
	}

	public boolean isEnabled(Component focusComp) {
        
		return getSelectedEx() != null && StateUtil.isFormatState(getPlugIn().getReport(), focusComp); 
	}

	/**
	 * @i18n miufo00083=�������ɾ���Ŀ���չ����
	 * @i18n miufo00082=������ʾ
	 * @i18n miufo00114=ȷ��ɾ������չ����
	 * @i18n miufo00115=����
	 * @i18n miufo00085=ɾ��ȷ��
	 */
	@Override
	public void excuteImpl(UfoReport report) {
		ExAreaCell cell = getSelectedEx();
		if(cell == null){
			UfoPublic.showErrorDialog(getPlugIn().getReport(), MultiLang.getString("miufo00083"), MultiLang.getString("miufo00082"));
			return;
		}
          
      	if(UfoPublic.showConfirmDialog(getPlugIn().getReport(), MultiLang.getString("miufo00114") + cell.toString() + MultiLang.getString("miufo00115"), MultiLang.getString("miufo00085"), JOptionPane.YES_NO_OPTION) != 0){
      		return;
      	}
      	
		String error = cell.fireUIEvent(ExAreaModelListener.REMOVE, cell, cell);
		if(error != null && error.length() > 1){
			UfoPublic.showErrorDialog(getPlugIn().getReport(), error, MultiLang.getString("miufo00082")); 
			return;
		} 

		getPlugIn().getExAreaModel().removeExArea(cell);
		getPlugIn().getReport().setFocusComp(getPlugIn().getReport().getTable().getCells());
		//���»��ƿ���չ����
//		IArea area = cell.getArea();//
		getPlugIn().getReport().updateUI();//.repaint(area);
		
		
	}

	/**
	 * @i18n miufo00116=ɾ������չ����
	 */
	@Override
	public String getDesName() {
		return MultiLang.getString("miufo00116");
	}
	
	

}
 