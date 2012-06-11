package com.ufsoft.report.menu;

import java.awt.Component;

import nc.ui.pub.beans.UICheckBoxMenuItem;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;

public class UFCheckBoxMenuItem extends UICheckBoxMenuItem {
	protected IActionExt m_Extension = null;
	protected UfoReport aReport;

	public UFCheckBoxMenuItem(IActionExt extension, ActionUIDes uiDes,
			UfoReport report) {
		super(uiDes.getName());
		setName(uiDes.getName());
		m_Extension = extension;
		aReport = report;
	}

	public boolean isEnabled(Component focusComp) {
		return m_Extension.isEnabled(focusComp);
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getText();
	}

	/*
	 * @see javax.swing.AbstractButton#setMnemonic(int)
	 */
	public void setMnemonic(int mnemonic) {
		super.setMnemonic(mnemonic);
		dealMnemonicDisPlay();
	}

	/**
	 * 处理记忆键的显示.
	 * 菜单路径比较使用name值.
	 *  void
	 */
	private void dealMnemonicDisPlay() {
		if (this.getDisplayedMnemonicIndex() == -1 && getMnemonic() != 0) {
			setText(getText() + "(" + (char) getMnemonic() + ")");
		}
	}
}
