package com.ufsoft.report.menu;

import java.awt.Component;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JSeparator;

import com.ufida.zior.comp.KMenu;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.constant.UIStyle;

/**
 * name值用于比较路径,text值用于显示.
 * 
 * @author zzl 2005-6-30
 */
public class UFMenu extends KMenu {
	private UfoReport aReport;

	private Hashtable separators = new Hashtable();

	private Hashtable htButtonGroup = new Hashtable();

	/**
	 * @param s
	 */
	public UFMenu(String s, UfoReport report) {
		super(s);
		setName(s);
		setFont(UIStyle.MENUFONT);
		aReport = report;
		setVisible(false);
	}

	/**
	 * 当加入子菜单后，显示属性修改为true；
	 */
	public Component add(Component c) {
		setVisible(true);
		return super.add(c);
	}

	/**
	 * 考虑buttonGroup.
	 * 
	 * @return Component
	 */
	public Component add(Component c, String group, String strButtonGroup) {
		Component rtn = add(c, group);
		if (strButtonGroup != null) {
			if (!htButtonGroup.keySet().contains(strButtonGroup)) {
				htButtonGroup.put(strButtonGroup, new ButtonGroup());
			}
			ButtonGroup buttonGroup = (ButtonGroup) htButtonGroup
					.get(strButtonGroup);
			if (!(c instanceof AbstractButton)) {
				throw new IllegalArgumentException(
						"This componnet can not be added to ButtonGroup.");
			}
			buttonGroup.add((AbstractButton) c);
		}
		return rtn;
	}

	/**
	 * 当加入子菜单后，显示属性修改为true；
	 */
	public Component add(Component c, String group) {
		setVisible(true);
		if (group == null) {
			group = "";
		}
		if (separators.keySet().contains(group)) {
			JSeparator sep = (JSeparator) separators.get(group);
			List vec = Arrays.asList(this.getMenuComponents());
			int index = vec.indexOf(sep);
			super.add(c, index);
		} else {
			JSeparator sep = new JSeparator();
			sep.setVisible(false);
			separators.put(group, sep);
			super.add(c);
			super.add(sep);
		}
		Component[] comps = getMenuComponents();
		for (int i = 0; i < comps.length - 1; i++) {
			if (comps[i] instanceof JSeparator) {
				comps[i].setVisible(true);
			}
		}
		return c;
	}

	/**
	 * 修改子菜单的选择状态。
	 */
	protected void fireMenuSelected() {
		Component[] cmps = this.getMenuComponents();
		if (cmps != null) {
			for (int i = 0; i < cmps.length; i++) {
				if (cmps[i] instanceof UFMenuItem ) {
					UFMenuItem item = (UFMenuItem) cmps[i];
					if (aReport != null) {
						boolean enable = item.m_Extension.isEnabled(aReport
								.getFocusComp());
						if (enable != item.isEnabled()) {
							item.setEnabled(enable);
						}
					}
				}else if (cmps[i] instanceof UFCheckBoxMenuItem) {
					UFCheckBoxMenuItem item = (UFCheckBoxMenuItem) cmps[i];
					if (aReport != null) {
						boolean enable = item.m_Extension.isEnabled(aReport
								.getFocusComp());
						if (enable != item.isEnabled()) {
							item.setEnabled(enable);
						}
					}
				}
			}
		}
		super.fireMenuSelected();
	}

	/*
	 * @see java.awt.Container#getComponents()
	 */
	public Component[] getComponents() {
		return super.getMenuComponents();
	}

	/*
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getText();
	}

	// public void updateUI() {
	// MenuItemUI ui = new BasicMenuUI(){
	// protected void paintText(Graphics g, JMenuItem menuItem, Rectangle
	// textRect, String text) {
	// if(menuItem.getDisplayedMnemonicIndex() == -1 && getMnemonic() != 0){
	// text = text + "("+(char)getMnemonic()+")";
	// }
	// super.paintText(g,menuItem,textRect,text);
	// }
	// };
	// setUI(ui);
	// }
	/*
	 * @see javax.swing.AbstractButton#setMnemonic(int)
	 */
	public void setMnemonic(int mnemonic) {
		super.setMnemonic(mnemonic);
		dealMnemonicDisPlay();
	}

	/**
	 * 处理记忆键的显示. 菜单路径比较使用name值. void
	 */
	private void dealMnemonicDisPlay() {
		if (this.getDisplayedMnemonicIndex() == -1 && getMnemonic() != 0) {
			setText(getText() + "(" + (char) getMnemonic() + ")");
		}
	}
}
