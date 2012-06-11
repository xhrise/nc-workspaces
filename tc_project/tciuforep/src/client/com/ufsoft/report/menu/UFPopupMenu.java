package com.ufsoft.report.menu;

import java.awt.Component;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.JSeparator;

import nc.ui.pub.beans.UISeparator;

public class UFPopupMenu extends nc.ui.pub.beans.UIPopupMenu {

	private static final long serialVersionUID = -1238894317864131459L;
	private Hashtable<String, JSeparator> separators = new Hashtable<String, JSeparator>();
	private Hashtable<String, ButtonGroup> htButtonGroup = new Hashtable<String, ButtonGroup>();

	/**
	 * 考虑buttonGroup.
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
				throw new IllegalArgumentException();
			}
			buttonGroup.add((AbstractButton) c);
		}
		return rtn;
	}

	/**
	 * 当加入子菜单后，显示属性修改为true；
	 */
	public Component add(Component c, String group) {

		if (group == null) {
			group = "";
		}
		if (separators.keySet().contains(group)) {
			JSeparator sep = separators.get(group);
			List<Component> vec = Arrays.asList(getComponents());
			int index = vec.indexOf(sep);
			super.add(c, index);
		} else {
			JSeparator sep = new UISeparator();
			sep.setVisible(false);
			separators.put(group, sep);
			super.add(c);
			super.add(sep);
		}
		Component[] comps = getComponents();
		for (int i = 0; i < comps.length - 1; i++) {
			if (comps[i] instanceof JSeparator) {
				comps[i].setVisible(true);
			}
		}
		return c;
	}
}
