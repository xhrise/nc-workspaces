package com.ufsoft.iufo.fmtplugin.statusshow;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.ufida.zior.comp.KLabel;
import com.ufida.zior.comp.KStatusBar;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.event.CellsModelSelectedListener;
import com.ufsoft.iufo.resource.StringResource;

public class CellPositionStatusBarAction extends AbstractPluginAction{

	@Override
	public void execute(ActionEvent e) {
	}

	/**
	 * @i18n miufohbbb00100=单元位置
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		 
		PluginActionDescriptor des = new PluginActionDescriptor(StringResource.getStringResource("miufohbbb00100"));
		des.setExtensionPoints(XPOINT.STATUSBAR);
		des.setCompentFactory(new ComponentFactory());
		return des;
	}

	private class ComponentFactory extends DefaultCompentFactory{

		@Override
		public JComponent createComponet(XPOINT point, String[] paths,
				JComponent root, AbstractAction action) {
            final JLabel comp = new KLabel("");
            if(!(root instanceof KStatusBar))
            	return null;
            
            CellsModelSelectedListener lis = new CellsModelSelectedListener(){

				public void anchorChanged(CellsModel model,
						CellPosition oldAnchor, CellPosition newAnchor) {
					comp.setText(MultiLang.getString("uiuforep0000883")+":"+newAnchor.toString());//uiuforep0000883:位置
				}

				public void selectedChanged(CellsModel cellsModel,
						AreaPosition[] changedArea) {					
				}          	
            };
            getMainboard().getEventManager().addListener(lis);
            ((KStatusBar)root).addImmutableInfo(comp);
			return comp;
		}
		
	}
}
 