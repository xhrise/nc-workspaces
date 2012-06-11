package com.ufsoft.iufo.inputplugin.biz.file;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import nc.ui.pub.beans.UIMenu;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.InputFilePlugIn;
import com.ufsoft.iufo.inputplugin.biz.RepSelectionPlugIn;
import com.ufsoft.report.ReportMenuBar;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.MenuUtil;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.INavigationExt;
import com.ufsoft.report.plugin.IPlugIn;
import com.ufsoft.report.plugin.IPluginDescriptor;
import com.ufsoft.report.util.MultiLang;

/**
 * �л���λ����������
 * @author chxw
 * 2007-09-04
 */
public class SwitchTreeView extends AbsActionExt {
	private UfoReport m_report; //������
	
	public SwitchTreeView(UfoReport report) {
		m_report = report;
	}
	
	@Override
	public UfoCommand getCommand() {
		return null;
	}

	@Override
	public Object[] getParams(UfoReport container) {
		if(!isCommit())
			AbsIufoBizCmd.doComfirmSave(m_report, false);
		
		RepSelectionPlugIn selectionPlugIn = (RepSelectionPlugIn)
			container.getPluginManager().getPlugin(RepSelectionPlugIn.class.getName());
		if(selectionPlugIn != null){
			String strOperUnit = null;
			String strOperRep  = null;
			
			ChooseRepExt2  chooseRepExt2  = (ChooseRepExt2)(selectionPlugIn.getDescriptor().getExtensions()[0]);
			ChooseRepPanel chooseRepPanel = chooseRepExt2.getChooseRepNavPanel();
			strOperUnit = chooseRepPanel.getOperUnit();
			strOperRep  = chooseRepPanel.getOperRep();
			chooseRepPanel.switchTreeView();
			
			ChooseCordExt chooseCordExt     = (ChooseCordExt)(selectionPlugIn.getDescriptor().getExtensions()[1]);
			ChooseCordPanel chooseCordPanel = (ChooseCordPanel)chooseCordExt.getPanel();
			chooseCordPanel.switchTreeView(strOperUnit, strOperRep);
			
			//�����л��˵���ʾ
			String strMenuLabel = null, strPanelMenuLabel = null;
	    	if (GeneralQueryUtil.isShowRepTree(m_report)){ 
	    		
	    		strMenuLabel = ChooseRepExt2.LABEL_UNITTREE; //miufotableinput0001=����λ��֯
	    		strPanelMenuLabel = ChooseRepExt2.LABEL_REPLIST;
	    	} else{
	    		strMenuLabel = ChooseRepExt2.LABEL_REPTREE; //miufotableinput0002=��������֯
	    		strPanelMenuLabel = ChooseRepExt2.LABEL_UNITLIST;
	    	}
	    	
	    	JMenuItem treeViewComp = getTreeViewComp();
	    	treeViewComp.setText(strMenuLabel);
	    	String oldViewItemName;
	    	if(strPanelMenuLabel.equals(ChooseRepExt2.LABEL_REPLIST)){
	    		oldViewItemName=ChooseRepExt2.LABEL_UNITLIST;
	    	}else{
	    		oldViewItemName=ChooseRepExt2.LABEL_REPLIST;
	    	}
	    	JCheckBoxMenuItem treePanelViewComp = getPanelMenuItemOfTreeView(oldViewItemName);
	    	if(treePanelViewComp!=null){
	    		treePanelViewComp.setText(strPanelMenuLabel);
	    		treePanelViewComp.setName(strPanelMenuLabel);
	    	}
	    	chooseRepExt2.setExtName(strPanelMenuLabel);
		}
		return null;
	}
	
	@Override
	public ActionUIDes[] getUIDesArr() {
		ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(getName());
        uiDes1.setPaths(new String[]{MultiLang.getString("file")}); //"�ļ�"
        return new ActionUIDes[]{uiDes1};
	}
	
	private String getName(){
		String strMenuLabel = null;
		if (GeneralQueryUtil.isShowRepTree(m_report)){ 
			strMenuLabel = ChooseRepExt2.LABEL_UNITTREE; //miufotableinput0001=����λ��֯

		} else{
			strMenuLabel = ChooseRepExt2.LABEL_REPTREE; //miufotableinput0002=��������֯
		}
		return strMenuLabel;
	}

	/**
	 * ���ص�λ���������л��˵����ʵ��JMenuItem
	 * 
	 * @i18n miufotableinput0001=����λ��֯
	 * @i18n miufotableinput0002=��������֯
	 * @return
	 */
	private JMenuItem getTreeViewComp(){
		//�ļ��˵�
		ReportMenuBar menuBar = m_report.getReportMenuBar();
		UIMenu fileMenu = (UIMenu)menuBar.getMenu(0);
		
		//��λ���������л��˵���
		Component[] comps = fileMenu.getPopupMenu().getComponents();
		for(int i = 0; i < comps.length; i++) {
			if (comps[i] instanceof JMenuItem
					&& (comps[i].getName().equals(ChooseRepExt2.LABEL_UNITTREE) ||     //miufotableinput0001=����λ��֯
							comps[i].getName().equals(ChooseRepExt2.LABEL_REPTREE))) { //miufotableinput0002=��������֯
				return (JMenuItem)comps[i];
			}
		}
		
		return null;
	}
	
	/**
	 * ���ص�λ�����������˵����ʵ��JMenuItem
	 *
	 */
	private JCheckBoxMenuItem getPanelMenuItemOfTreeView(String treeName) {
		JComponent parent = MenuUtil.getCompByPath(new String[] {
				MultiLang.getString("view"),
				MultiLang.getString("panelManager") }, 0, m_report
				.getJMenuBar(), m_report);
		JCheckBoxMenuItem windowMenu = null;
		if (parent instanceof JMenu) {
			JMenu menu = (JMenu) parent;
			if (menu.getItemCount() < 1) {
				ActionListener[] listeners = menu.getActionListeners();
				ActionEvent event = null;
				for (int i = 0; i < listeners.length; i++) {
					event = new ActionEvent(menu, ActionEvent.ACTION_PERFORMED,
							"");
					listeners[i].actionPerformed(event);
				}
			}

			JPopupMenu popmenu = menu.getPopupMenu();
			for (int i = 0; i < popmenu.getComponentCount(); i++) {
				if (treeName.equals(popmenu.getComponent(i).getName())
						&& popmenu.getComponent(i) instanceof JCheckBoxMenuItem)
					windowMenu = (JCheckBoxMenuItem) popmenu.getComponent(i);
			}

		}

		return windowMenu;
	}
	
	/**
	 * �����Ƿ����ϱ�
	 * 
	 */
	public boolean isCommit() {
		InputFilePlugIn plugIn = (InputFilePlugIn)m_report.getPluginManager().getPlugin(InputFilePlugIn.class.getName());
		if(plugIn != null){
			IExtension[] exts = plugIn.getDescriptor().getExtensions();
			SaveRepDataExt saveRepDataExt = (SaveRepDataExt)exts[0];
	    	return !saveRepDataExt.isEnabledSelf();
		}
		return false;
		
	}
	 
}
