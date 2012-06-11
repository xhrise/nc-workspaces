/**
 * 
 */
package com.ufsoft.report.sysplugin.repstyle;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.EventObject;

import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import nc.ui.pub.beans.constenum.DefaultConstEnum;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.comp.KToolBarPane;
import com.ufida.zior.exception.ForbidedOprException;
import com.ufida.zior.exception.MessageException;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.event.ViewerListener;
import com.ufsoft.report.AbstractRepPluginAction;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.ReportStyle;
import com.ufsoft.report.constant.DefaultSetting;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.UserActionListner;
import com.ufsoft.table.UserUIEvent;

/**
 * @author wangyga
 * 表页显示比例插件动作类
 * @created at 2009-9-3,下午01:13:36
 *
 */
public class RepDisplayPercentAction extends AbstractRepPluginAction{

	@Override
	public void execute(ActionEvent e) {
		try {
			JComboBox source = (JComboBox)e.getSource();
			DefaultConstEnum selectItem = (DefaultConstEnum)source.getSelectedItem();
			if(selectItem == null){
				return ;
			}
			
			UFOTable table = getTable();
			ReportStyle oldStyle = table.getStyle();
			oldStyle.setPercent((Integer)selectItem.getValue());
			table.setStyle(oldStyle);
			
		} catch (Exception e2) {
			AppDebug.debug(e);
			throw new MessageException(MessageException.TYPE_ERROR,"设置失败");
		}
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = new PluginActionDescriptor("显示比例");
		desc.setExtensionPoints(XPOINT.TOOLBAR);
		desc.setGroupPaths(new String[]{MultiLang.getString("miufo1001189")});
		desc.setCompentFactory(createCompFactory());
		return desc;
	}

	private ICompentFactory createCompFactory(){
		return new DefaultCompentFactory(){
			@Override
			protected JComponent createToolBarItem(String[] paths,
					JComponent root, AbstractAction action) {
				final JComboBox comp = new JComboBox(DefaultSetting.REP_DISPLAY_PERCENT);
				comp.setPreferredSize(new Dimension(60,23));
				String strGroup = paths[paths.length - 1];
				KToolBarPane pane = (KToolBarPane) root;
				JToolBar parent = pane.getToolBar(strGroup);
				parent.add(comp);
				comp.setSelectedItem(DefaultSetting.REP_DISPLAY_PERCENT[1]);
				comp.setAction(action);
				
				getMainboard().getEventManager().addListener(new ViewerListener.Sub(){

					@Override
					public void onActive(Viewer currentActiveView,
							Viewer oldActiveView) {
						if(!(currentActiveView instanceof ReportDesigner)){
							return ;
						}
						ReportStyle style = ((ReportDesigner)currentActiveView).getTable().getStyle();
						comp.setSelectedItem(getConstByValue(style.getPercent()));
					}
					
					private DefaultConstEnum getConstByValue(int iPercent){
						DefaultConstEnum percent = new DefaultConstEnum(iPercent,iPercent+"%");
						return percent;
					}
					
				});
				
				getMainboard().getEventManager().addListener(new UserActionListner(){

					public void userActionPerformed(UserUIEvent e) {
						if (e.getEventType() == 15
										&& e.getSource() != null
										&& "repPercent".equals(e.getSource()
												.toString()) && e.getNewValue() != null) {
							Integer iPercent = new Integer(e.getNewValue().toString());
							DefaultConstEnum percent = new DefaultConstEnum(iPercent.intValue(),iPercent+"%");
							if(((DefaultComboBoxModel)comp.getModel()).getIndexOf(percent) > -1){
								comp.setSelectedItem(percent);
							} else {
								comp.addItem(percent);
								comp.setSelectedItem(percent);
							}
						}
					}
					
					public String isSupport(int source, EventObject e)
							throws ForbidedOprException {
						// TODO Auto-generated method stub
						return null;
					}
					
				});
				return comp;
			}
			
		};
	}
	
}
