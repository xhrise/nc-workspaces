package com.ufsoft.report.plugin;

import java.awt.Dimension;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import com.ufida.zior.comp.KCheckBoxMenuItem;
import com.ufida.zior.comp.KCheckBoxPopMenuItem;
import com.ufida.zior.comp.KRadioButtonMenuItem;
import com.ufida.zior.comp.KToolBarPane;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.toolbar.dropdown.JPopupPanelButton;
import com.ufsoft.report.toolbar.dropdown.SwatchPanel;

/**
 * 特殊组件工厂类，适配新的UI框架，比如工具栏的：下拉列表相关组件
 * 
 * @author wangyga
 * @created at 2009-3-7,上午11:05:46
 * 
 */
public class SpecialComponentFactory extends DefaultCompentFactory {

	ActionUIDes[] actionUIDes = null;

	public SpecialComponentFactory(ActionUIDes[] uiDes) {
		if (uiDes == null || uiDes.length == 0) {
			throw new IllegalArgumentException();
		}
		this.actionUIDes = uiDes;
	}

	@Override
	protected JComponent createToolBarItem(String[] paths, JComponent root,
			AbstractAction action) {
		ActionUIDes uiDes = getSpecialComUiDes();
		String strGroup = paths[paths.length - 1];
		KToolBarPane pane = (KToolBarPane) root;
		JToolBar parent = pane.getToolBar(strGroup);

		JComboBox actionComp = null;
		switch (uiDes.getComboType()) {
		case 0:
			actionComp = new JComboBox(){
				@Override
				public Dimension getPreferredSize() {
					Dimension oldSize = super.getPreferredSize();
					if(oldSize != null){
						return new Dimension(oldSize.width,23);
					}
					return null;
				}
			};
			String[] comboItem = uiDes.getListItem();
			for (int i = 0; i < comboItem.length; i++) {
				actionComp.addItem(comboItem[i]);
			}
			break;
		case 1:
			if (uiDes.getComboComponent() instanceof SwatchPanel) {
				actionComp = new JPopupPanelButton(null, uiDes.getName(),
						ResConst.getImageIcon(uiDes.getImageFile()),
						(SwatchPanel) uiDes.getComboComponent());
				actionComp.setSelectedItem(uiDes.getDefaultSelected());
			}
			break;
		case 2:
			if (uiDes.getComboComponent() instanceof JPopupMenu) {
				actionComp = new JPopupPanelButton(null, uiDes.getName(),
						ResConst.getImageIcon(uiDes.getImageFile()),
						(JPopupMenu) uiDes.getComboComponent());
			}
			break;
		default:
			break;
		}

		if (actionComp != null) {
			actionComp.setName(uiDes.getName());
			actionComp.setSelectedItem(uiDes.getDefaultSelected());
			parent.add(actionComp);
			actionComp.setAction(action);
		}

		return actionComp;
	}

	@Override
	protected JComponent createMenuItem(String strGroup, AbstractAction action) {

		JComponent actionComp = null;
		if (getSpecialComUiDes() instanceof ToggleMenuUIDes) {
			ToggleMenuUIDes togUiDes = (ToggleMenuUIDes) getSpecialComUiDes();
			if (togUiDes.isCheckBox()) {
				actionComp = new KCheckBoxPopMenuItem();
				((KCheckBoxPopMenuItem) actionComp).setSelected(togUiDes
						.isSelected());
				((KCheckBoxPopMenuItem) actionComp).setGroup(strGroup);
				((KCheckBoxPopMenuItem) actionComp).setAction(action);
				((KCheckBoxPopMenuItem) actionComp).setText(togUiDes.getName());
			} else {
				actionComp = new KRadioButtonMenuItem();
				((KRadioButtonMenuItem) actionComp).setSelected(togUiDes
						.isSelected());
				((KRadioButtonMenuItem) actionComp).setGroup(strGroup);
				((KRadioButtonMenuItem) actionComp).setAction(action);
				((KRadioButtonMenuItem) actionComp).setText(togUiDes.getName());
			}

		} else {
			ActionUIDes uiDes = getSpecialComUiDes();
			if (uiDes.isCheckBoxMenuItem()) {
				actionComp = new KCheckBoxMenuItem();
				((KCheckBoxPopMenuItem) actionComp).setGroup(strGroup);
				((KCheckBoxPopMenuItem) actionComp).setAction(action);
				((KCheckBoxPopMenuItem) actionComp).setText(uiDes.getName());
			}
			
		}
        if(actionComp == null){
        	actionComp = super.createMenuItem(strGroup, action);
        }
		return actionComp;
	}

	private ActionUIDes getSpecialComUiDes() {
		for (ActionUIDes uiDes : actionUIDes) {
			if (uiDes.isListCombo() || uiDes instanceof ToggleMenuUIDes
					|| uiDes.isCheckBoxMenuItem()) {
				return uiDes;
			}
		}
		return null;
	}
}
