package com.ufsoft.report.sysplugin.editplugin;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import nc.ui.pub.beans.UIMenuItem;

import com.ufida.zior.comp.KToolBarPane;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.util.UIUtilities;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.menu.UFPopupMenu;
import com.ufsoft.report.resource.ResConst;
import com.ufsoft.report.sysplugin.edit.EditPasteExt;
import com.ufsoft.report.toolbar.dropdown.JPopupPanelButton;
import com.ufsoft.report.util.MultiLang;

public class PasteAction extends AbsEditAction{

	/** ѡ����ճ������� */
	private JComboBox item = null;
	
	@Override
	public void execute(ActionEvent e) {
		if(!isEnabled()){
			return;
		}
		AbsPasteExecutor choosePaste = null;
		JComboBox comboBox = getItem();// ���ѡ����ճ���ؼ�������
		if (comboBox == null) {// ճ����ݼ��Ĵ���Ĭ����ճ��ȫ��
			comboBox = new JComboBox();
		}

		Object selectValueObj = comboBox.getSelectedItem();// ���ѡ����ճ����ֵ
		if (selectValueObj == null) {// �����ֵΪNULL�������ÿ�ݼ�ճ��
			if(isFormatState()){
				choosePaste = new PasteAll(getMainboard(), false);
			}else{
				choosePaste = new PasteContent(getMainboard(), false);//����̬�ĸ��ơ����С�ճ����ʱ���ֵ����������ʽ����Ϣ
			}	
		} else {// ����������˵�ѡ�񷵻ص�״̬����
			choosePaste = getSelectPaste(selectValueObj.toString());
			comboBox.setSelectedItem(null);
		}
		
		if(choosePaste != null){
			choosePaste.choosePaste();// ִ��ճ��
		}
	}
	
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
         PluginActionDescriptor des = new PluginActionDescriptor(MultiLang.getString("miufo1000655"));
         des.setCompentFactory(new PopupCompFactory());
         des.setExtensionPoints(XPOINT.MENU,XPOINT.TOOLBAR,XPOINT.POPUPMENU);
         des.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
         des.setMemonic('P');
         des.setToolTipText(MultiLang.getString("miufo1000655"));
         des.setGroupPaths(new String[]{MultiLang.getString("edit"),MultiLang.getString("edit")});
         des.setIcon("/images/reportcore/paste.gif");
		return des;
	}

	private class PopupCompFactory extends DefaultCompentFactory{

		@Override
		protected JComponent createToolBarItem(String[] paths, JComponent root,
				AbstractAction action) {
			JPopupPanelButton actionComp = new JPopupPanelButton(null, MultiLang
					.getString("miufo1000655"), ResConst
					.getImageIcon("reportcore/paste.gif"),
					createComboxMenu());
			actionComp.setName(MultiLang.getString("miufo1000655"));
			String strGroup = paths[paths.length - 1];
			KToolBarPane pane = (KToolBarPane) root;
			JToolBar parent = pane.getToolBar(strGroup);
			parent.add(actionComp);
			actionComp.setSelectedItem(null);
			actionComp.setAction(action);
			actionComp.getBttLeft().setToolTipText(UIUtilities.getTipText(action));
			item = actionComp;
			
			return actionComp;
		}
		
	}
	
	private AbsPasteExecutor getSelectPaste(String strSelectItem) {
		if (strSelectItem == null || strSelectItem.length() == 0) {
			return null;
		}
		AbsPasteExecutor choosePaste = null;
		if (strSelectItem.trim().equals(EditPasteExt.CONTENT)) {// ����
			choosePaste = new PasteContent(getMainboard(), false);
		} else if (strSelectItem.trim().equals(EditPasteExt.FORMAT)) {// ��ʽ
			choosePaste = new PasteFormat(getMainboard(), false);
		} else if (strSelectItem.trim().equals(EditPasteExt.NO_BORDER)) {// �߿����
			choosePaste = new PasteNoBorder(getMainboard(), false);
		} else if (strSelectItem.trim().equals(EditPasteExt.TRANSFER)) {// ת��
			if(isFormatState()){
				choosePaste = new PasteAll(getMainboard(), true);
			} else {
				choosePaste = new PasteContent(getMainboard(), true);
			}
		} else if (strSelectItem.trim().equals(EditPasteExt.CHOOSE_DIALOG)) {// ����ѡ����ճ���Ի���
			ChoosePasteDlg pasteDlg = getChoosePasteDlg(getMainboard());// ѡ����ճ���Ի���
			pasteDlg.show();
			if (pasteDlg.getResult() == UfoDialog.ID_OK) {
				choosePaste = pasteDlg.getChoosePaste();
			}
		}
		return choosePaste;
	}
	
	private ChoosePasteDlg getChoosePasteDlg(Container owner) {
		return new ChoosePasteDlg(owner);
	}
	
	private JPopupMenu createComboxMenu(){
		JPopupMenu menu = new UFPopupMenu();
		
        JMenuItem contentItem = new UIMenuItem(MultiLang.getString("miufo1000275"));
        contentItem.setActionCommand(EditPasteExt.CONTENT);
        menu.add(contentItem);
        
        JMenuItem formatItem = new UIMenuItem(MultiLang.getString("miufo1000877"));
        formatItem.setActionCommand(EditPasteExt.FORMAT);
        menu.add(formatItem);
        
        JMenuItem noBorderItem = new UIMenuItem(MultiLang.getString("miufo1004057"));
        noBorderItem.setActionCommand(EditPasteExt.NO_BORDER);
        menu.add(noBorderItem);
        
        JMenuItem turnItem = new UIMenuItem(MultiLang.getString("miufo1004048"));
        turnItem.setActionCommand(EditPasteExt.TRANSFER);
        menu.add(turnItem);
        
        menu.addSeparator();
        
        JMenuItem chooseItem = new UIMenuItem(MultiLang.getString("miufo1004049"));
        chooseItem.setActionCommand(EditPasteExt.CHOOSE_DIALOG);
        menu.add(chooseItem);
        return menu;
	}
	
	private JComboBox getItem() {
		return item;
	}
}
