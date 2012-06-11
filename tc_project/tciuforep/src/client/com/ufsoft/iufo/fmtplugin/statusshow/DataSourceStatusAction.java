package com.ufsoft.iufo.fmtplugin.statusshow;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;

import nc.ui.iufo.datasource.DataSourceBO_Client;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iuforeport.applet.IDataSourceParam;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.iufo.pub.tools.DateUtil;
import com.ufida.zior.comp.KLabel;
import com.ufida.zior.comp.KStatusBar;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;

public class DataSourceStatusAction extends AbstractPluginAction implements IUfoContextKey{

	@Override
	public void execute(ActionEvent e) {
	}

	/**
	 * @i18n miufotasknew00022=数据源信息
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {

		PluginActionDescriptor des = new PluginActionDescriptor(StringResource.getStringResource("miufotasknew00022"));
		des.setExtensionPoints(XPOINT.STATUSBAR);
		des.setCompentFactory(new ComponentFactory());
		return des;
	}

	private class ComponentFactory extends DefaultCompentFactory {
		/**
		 * @i18n miufopublic358=无
		 */
		@Override
		public JComponent createComponet(XPOINT point, String[] paths,
				JComponent root, AbstractAction action) {
			IContext context = getMainboard().getContext();				
			
			String dataSourceID = context.getAttribute(DATA_SOURCE_ID) == null ? null : context.getAttribute(DATA_SOURCE_ID).toString();
			final JLabel comp = new KLabel("");
			if (dataSourceID != null) {
				try {
					DataSourceVO dataSourceVO = DataSourceBO_Client
							.loadDataSByID(dataSourceID);
					comp.setText(StringResource.getStringResource("miufo1001422") + 
							(dataSourceVO != null ? dataSourceVO.getName() : 
								StringResource.getStringResource("miufopublic358")));
					String strInputDate=(String)context.getAttribute(IUfoContextKey.LOGIN_DATE);
					if (strInputDate==null)
						strInputDate=DateUtil.getCurDay();
					dataSourceVO.setLoginDate(strInputDate);
					dataSourceVO.setLoginUnit((String)context.getAttribute(IDataSourceParam.DS_UNIT));
					dataSourceVO.setLoginName((String)context.getAttribute(IDataSourceParam.DS_USER));
					
					String dsPassword = nc.bs.iufo.toolkit.Encrypt.decode((String) context.getAttribute(
									IDataSourceParam.DS_PASSWORD), dataSourceID);
					dataSourceVO.setLoginPassw(dsPassword);
					context.setAttribute(DATA_SOURCE,dataSourceVO);
				} catch (Throwable e) {
					AppDebug.debug(e);
				} 
				
			}else {
				comp.setText(StringResource.getStringResource("miufo1001422")+""+StringResource.getStringResource("miufopublic358"));
			}
			
			if (!(root instanceof KStatusBar))
				return null;
			 ((KStatusBar)root).addImmutableInfo(comp);
			
			return comp;
		}
	}
	
}
 