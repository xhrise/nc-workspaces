package com.ufsoft.iufo.fmtplugin.statusshow;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.ufida.dataset.IContext;
import com.ufida.zior.comp.KLabel;
import com.ufida.zior.comp.KStatusBar;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.event.ViewerListener;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.resource.StringResource;

public class FormatRightStatusAction extends AbstractPluginAction implements
		IUfoContextKey {

	@Override
	public void execute(ActionEvent e) {
	}

	/**
	 * @i18n miufohbbb00099=格式权限
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {

		PluginActionDescriptor des = new PluginActionDescriptor(
				StringResource.getStringResource("miufohbbb00099"));
		des.setExtensionPoints(XPOINT.STATUSBAR);
		des.setCompentFactory(new ComponentFactory());
		return des;
	}

	private class ComponentFactory extends DefaultCompentFactory {

		@Override
		public JComponent createComponet(XPOINT point, String[] paths,
				JComponent root, AbstractAction action) {
			final JLabel comp = new KLabel("");
			getMainboard().getEventManager().addListener(
					new ViewerListener.Sub() {
						public void onActive(Viewer currentActiveView,
								Viewer oldActiveView) {
							IContext context = currentActiveView.getContext();
							int format_right = context
									.getAttribute(FORMAT_RIGHT) == null ? -1
									: Integer.parseInt(context.getAttribute(
											FORMAT_RIGHT).toString());
							String strRight = null;
							switch (format_right) {
							case RIGHT_FORMAT_NULL:
								strRight = StringResource
										.getStringResource("miufopublic358");// miufopublic358:无
								break;
							case RIGHT_FORMAT_READ:
								strRight = StringResource
										.getStringResource("miufopublic268"); // miufopublic268:查看
								break;
							case RIGHT_FORMAT_PERSONAL:
								strRight = StringResource
										.getStringResource("miufo1000693");// miufo1000693:个性化修改
								break;
							case RIGHT_FORMAT_WRITE:
								strRight = StringResource
										.getStringResource("ubiauth0010");// ubiauth0010:写
								break;
							default:
								strRight = StringResource
										.getStringResource("miufopublic358");

							}
							comp.setText(StringResource
									.getStringResource("miufo1001799")
									+ ":" + strRight);// miufo1001799:格式权限
						}
					});

			if (!(root instanceof KStatusBar))
				return null;
			((KStatusBar) root).addImmutableInfo(comp);

			return comp;
		}
	}

}
 