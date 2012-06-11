/*
 * Created on 2004-12-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.ufsoft.report.sysplugin.combinecell;

import java.awt.Component;

import javax.swing.KeyStroke;

import com.ufsoft.report.ReportMenuBar;
import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellsModel;

/**
 * ��ϵ�Ԫ����Ĺ��ܵ㣺��ϵ�Ԫ
 * @author zzl
 * @version 5.0
 * Create on 2004-12-1
 * ���ڹ������ϵ�"��ϵ�Ԫ"��״̬���ƺͻ��Ʒ������Ҽ��Ͳ˵�������಻ͬ���ʵ���������̳и�ʵ��
 * Modify by guogang 2008-2-25
 */
public class CombineCellExt extends AbsActionExt {
	
	private UfoReport m_report;

    /**
     * @param m_report
     */
    public CombineCellExt(UfoReport report) {
        m_report = report;
    }

    /* @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	public String getName() {
		return MultiLang.getString("miufo1000900");//"��ϵ�Ԫ";
	}
 
	/* @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
	    return new CombineCellCmd();
	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		return new Object[]{container};
	}

   
    public String[] getPath() {
        return null;
    }

 
    public boolean isEnabled(Component focusComp) {
		CellsModel cellsModel = m_report.getCellsModel();
		AreaPosition area = cellsModel.getSelectModel().getSelectedArea();
		return isVisiable(focusComp)
				&& (!area.isCell() || cellsModel.getCombinedAreaModel()
						.belongToCombinedCell(area.getStart()) != null);
	}

 
    public boolean isVisiable(Component focusComp) {
        return StateUtil.isFormat_CPane(m_report,focusComp);
    }

    /*
     * �ù��ܵ���ص�λ����Ϣ
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(MultiLang.getString("miufo1000900"));
        uiDes1.setPaths(new String[]{MultiLang.getString("format")});
        ActionUIDes uiDes2 = new ActionUIDes();
        uiDes2.setName(MultiLang.getString("miufo1000900"));
        uiDes2.setPopup(true);
        uiDes2.setGroup("ViewFormat");
        return new ActionUIDes[]{uiDes1,uiDes2};
    }
    
    public UfoReport getReport() {
        return m_report;
    }
}
