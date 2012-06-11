package com.ufsoft.report.fmtplugin.formula;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JToolBar;

import com.ufida.zior.comp.KToolBarPane;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.util.MultiLang;
/**
 * 区域公式菜单条的扩展组件，为公式菜单条加入区域态公式插件<code>AreaFormulaDefPlugin</code>提供接入方式。
 * <p>
 * 供HR使用
 * @author zhaopq
 * @created at 2009-4-15,下午03:05:08
 * @since v5.6
 */
public class AreaFormulaToolBarAction extends AbstractPluginAction{
	
	@Override
	public void execute(ActionEvent e) {
		
	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor(MultiLang.getString(AreaFormulaDefPlugin.GROUP));
		descriptor.setExtensionPoints(new XPOINT[] { XPOINT.TOOLBAR });
		descriptor.setGroupPaths(MultiLang.getString(AreaFormulaDefPlugin.GROUP));
		descriptor.setCompentFactory(new DefaultCompentFactory() {
			@Override
			protected JComponent createToolBarItem(String[] paths,
					JComponent root, AbstractAction action) {
				KToolBarPane pane = (KToolBarPane) root;
				String group = paths[paths.length - 1];
				JToolBar bar = pane.getToolBar(group);
				AreaFormulaToolBar toolBar = createToolBar();
				bar.add(toolBar);
				bar.validate();
				bar.setVisible(true);
				return toolBar;
			}
		});
		return descriptor;
	}
	
	protected AreaFormulaToolBar createToolBar(){
		return new AreaFormulaToolBar(getMainboard());
	}

}
