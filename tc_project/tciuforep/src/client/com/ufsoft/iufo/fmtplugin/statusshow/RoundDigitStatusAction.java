package com.ufsoft.iufo.fmtplugin.statusshow;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;

import com.ufida.zior.comp.KLabel;
import com.ufida.zior.comp.KStatusBar;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufsoft.iufo.AbsIUFORptDesignerPluginAction;
import com.ufsoft.iufo.fmtplugin.rounddigit.RoundDigitUtil;
import com.ufsoft.iufo.fmtplugin.rounddigitarea.RoundDigitAreaModelListener;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.event.CellsModelSelectedListener;

public class RoundDigitStatusAction  extends AbsIUFORptDesignerPluginAction{

	@Override
	public void execute(ActionEvent e) {
	}

	/**
	 * @i18n uiiufofmt00039=舍位区域
	 */
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor descriptor = new PluginActionDescriptor(StringResource.getStringResource("uiiufofmt00039"));
		descriptor.setExtensionPoints(new XPOINT[]{XPOINT.STATUSBAR});
		descriptor.setCompentFactory(new DefaultCompentFactory(){//添加到状态栏
			public JComponent createComponet(XPOINT point, String[] paths,
					JComponent root, AbstractAction action) {
				if (!(root instanceof KStatusBar))
					return null;
				
				final JLabel comp = new KLabel("");
				
				getMainboard().getEventManager().addListener(new CellsModelSelectedListener(){

					public void anchorChanged(CellsModel model, CellPosition oldAnchor, CellPosition newAnchor) {
						comp.setText(getStatusMark()+":"+getStatusValue());
						
					}

					public void selectedChanged(CellsModel cellsModel, AreaPosition[] changedArea) {
						
					}
				});
				
				getMainboard().getEventManager().addListener(new RoundDigitAreaModelListener(){
					public void modelChanged() {
						comp.setText(getStatusMark()+":"+getStatusValue());
					}			
				});
						
				if(comp.getText().equals("")){
					comp.setText(getStatusMark() + ":" + StringResource.getStringResource("miufopublic358"));
				}
				
				
				 ((KStatusBar)root).addImmutableInfo(comp);
				return comp;
			}
		});
		return descriptor;
	}
	
	/**
	 * @i18n uiiufofmt00039=舍位区域
	 */
	private String getStatusMark() {
		return StringResource.getStringResource("uiiufofmt00039");
	}
	
	/**
	 * @i18n uiiufofmt00040=不全是
	 * @i18n miufopublic506=否
	 * @i18n miufopublic505=是
	 */
	private String getStatusValue() {
		Boolean isUnRoundDigitArea = RoundDigitUtil.isUnRoundDigitArea(getCellsModel());
		if(isUnRoundDigitArea == null){
			return StringResource.getStringResource("uiiufofmt00040");
		}else if(isUnRoundDigitArea.equals(Boolean.TRUE)){
			return StringResource.getStringResource("miufopublic506");
		}else{
			return StringResource.getStringResource("miufopublic505");
		}
	}

}
 