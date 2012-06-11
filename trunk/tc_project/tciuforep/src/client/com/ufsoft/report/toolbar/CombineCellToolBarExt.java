package com.ufsoft.report.toolbar;

import java.awt.Component;

import javax.swing.AbstractButton;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.sysplugin.combinecell.CombineCellExt;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.SelectListener;
import com.ufsoft.table.event.SelectEvent;

public class CombineCellToolBarExt extends CombineCellExt {

	public CombineCellToolBarExt(UfoReport report) {
		super(report);
	}

	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes3 = new ActionUIDes();
		uiDes3.setToolBar(true);
		uiDes3.setGroup(MultiLang.getString("uiuforep0000870"));
		uiDes3.setPaths(new String[] {});
		uiDes3.setName(MultiLang.getString("miufo1000900"));
		uiDes3.setTooltip(MultiLang.getString("miufo1000900"));
		uiDes3.setImageFile("reportcore/combinecell.gif");
		return new ActionUIDes[]{uiDes3};
		
	}
//
//	@Override
	public boolean isEnabled(Component focusComp) {
		return true;
	}
	
	@Override
	public UfoCommand getCommand() {
	    return new CombineCellToolBarCmd();
	}
	/**
	 * override
	 * 
	 * @see AbsActionExt.initListenerByComp();
	 */
	public void initListenerByComp(final Component stateChangeComp) {
		getReport().getCellsModel().getSelectModel().addSelectModelListener(
				new SelectListener() {
					public void selectedChanged(SelectEvent e) {
						AreaPosition area = getReport().getCellsModel().getSelectModel()
								.getSelectedArea();
						if (e.getProperty() == SelectEvent.ANCHOR_CHANGED) {
							if (stateChangeComp instanceof AbstractButton) {
								// 当单元格切换的时候更新工具栏的状态
								if (getReport().getCellsModel().getCombinedAreaModel().belongToCombinedCell(area
										.getStart()) != null) {
									((AbstractButton) stateChangeComp)
											.setBorderPainted(true);
								} else {
									((AbstractButton) stateChangeComp)
											.setBorderPainted(false);
								}
							}
						}

					}
				});
	}
}
