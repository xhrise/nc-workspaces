/**
 * 公式追踪追溯，前一个，后一个基类
 * @author yp
 * @
 */
package com.ufsoft.iufo.inputplugin.querynavigation;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;

import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.docking.core.Dockable;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.view.ViewAdapter;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;

public abstract class AbsFormulaQueryNavAction extends AbstractPluginAction{

	private boolean bPre = true;
	private JComponent comp = null;
	
	public AbsFormulaQueryNavAction(boolean bPre){
		this.bPre = bPre;
	}
	@Override
	public void execute(ActionEvent e) {
		FormulaTraceNavigation navigation = FormulaTraceNavigation.getInstance(getMainboard());
		FormulaTraceNavView navView = null;
		FormulaTraceNavView curView = navigation.getCurView();
		if(bPre)
			navView = navigation.getPreView();
		else
			navView = navigation.getNextView();
		if(navView== null){
			comp.setEnabled(false);
			return;
		}
		 Dockable dockable =  getMainboard().getDockingManager().getDockable(navView.getEditor().getId());
		 if(dockable != null){
			 RepDataEditor editor = (RepDataEditor) ((ViewAdapter) dockable)
					.getViewer();
			List<CellPosition> lstPos = new ArrayList<CellPosition>();
			lstPos.add(navView.getCellPos());
			dockable.active();
			if (curView.getEditor().getId().equals(navView.getEditor().getId())) {
				editor.getCellsPane().repaint(curView.getCellPos(), true);
				editor.getCellsPane().repaint(navView.getCellPos(), true);
			}
			editor.setTraceCells(lstPos);
			WindowNavUtil.refreshNavPanel(editor, navView.getCellPos());
		 }else{
			 navigation.remove(navView);
		 }
		 comp.setEnabled(bPre? navigation.hasPre():navigation.hasNext());
		
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor uiDes = new PluginActionDescriptor();
		uiDes.setIcon(getIcon());
		uiDes.setName(getToolTip());
		uiDes.setGroupPaths(MultiLang.getString("file"));
		uiDes.setExtensionPoints(XPOINT.TOOLBAR);
		uiDes.setToolTipText(getToolTip());
		uiDes.setCompentFactory(new DefaultCompentFactory(){

			@Override
			protected JComponent createToolBarItem(String[] paths,
					JComponent root, AbstractAction action) {
				comp =  super.createToolBarItem(paths, root, action);;
				return comp;
			}
					
		});
		return uiDes;
		
	}
	
	public boolean isEnabled() {
		FormulaTraceNavigation navigation = FormulaTraceNavigation.getInstance(getMainboard());
		return bPre? navigation.hasPre():navigation.hasNext();
	}
	
	protected abstract String getToolTip();
	
	protected abstract String getIcon();
	

}
