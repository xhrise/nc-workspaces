package com.ufsoft.report.sysplugin.headerlock;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.UFOTable;

/**
 * ·ÖÀ¸
 * @author zzl 2005-5-23
 */
public class AreaSeparateExt extends AbsActionExt {//implements IMainMenuExt{

	private UfoReport m_report;

	/**
	 * @param m_report
	 */
	public AreaSeparateExt(UfoReport report) {
		m_report = report;
	}

	private String getName() {
		return isSplitState() ? MultiLang.getString("area_seperate_cancel")
				: MultiLang.getString("area_seperate");
	}

	private boolean isRealSplitState() {
		if (getUFOTable().getSeperateRow() == 0
				&& getUFOTable().getSeperateCol() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#changeCompState(java.awt.Component)
	 */
	public void initListenerByComp(final Component stateChangeComp) {
		getUFOTable().addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("seperate2lock")) {
					((AbstractButton) stateChangeComp).setText(getName());
				}
			}
		});
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new UfoCommand() {
			public void execute(Object[] params) {
				CellPosition anchor = (CellPosition) params[0];
				if (getUFOTable().isFrozenNoSplit()) {
					if (getUFOTable().isFreezing()) {
						getUFOTable().setFreezing(false);
					} else {
						// throw new IllegalStateException(); liuyy. 2007-04-19 
					}
					getUFOTable().setFrozenNoSplit(false);
				} else {
					if (isSplitState()) {
						getUFOTable().cancelSeperate();
					} else {
						getUFOTable().setSeperatePos(anchor.getRow(),
								anchor.getColumn());
					}
					getUFOTable().setFreezing(false);
				}
				m_report.resetGlobalPopMenuSupport();
			}

		};
	}

	private boolean isSplitState() {
		if(getUFOTable() == null){
			return false;
		}
		if (getUFOTable().isFrozenNoSplit()) {
			return false;
		} else {
			return isRealSplitState();
		}
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		CellPosition anchor = getUFOTable().getCellsModel().getSelectModel()
				.getAnchorCell();
		return new Object[] { anchor };
	}

	private UFOTable getUFOTable() {
		return m_report.getTable();
	}

	/*
	 * @see com.ufsoft.report.plugin.IExtension#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/*
	 * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
	 */
	public boolean isEnabled(Component focusComp) {
		return true;//StateUtil.isFormatState(m_report,focusComp);
	}

	/*
	 * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
	 */
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setName(getName());
		uiDes.setImageFile("reportcore/split.png");
		uiDes.setPaths(new String[] { MultiLang.getString("window") });
		uiDes.setGroup("ViewFormat");
		ActionUIDes uiDes1 = new ActionUIDes();
		uiDes1.setName(getName());
		uiDes1.setImageFile("reportcore/split.png");
		uiDes1.setPopup(true);
		uiDes1.setGroup("ViewFormat");
		return new ActionUIDes[] { uiDes, uiDes1 };
	}
}
