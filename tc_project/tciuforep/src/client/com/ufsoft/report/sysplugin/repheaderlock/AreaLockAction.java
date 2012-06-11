package com.ufsoft.report.sysplugin.repheaderlock;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.util.ResourceManager;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.event.ViewerListener;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.event.PropertyChangeEventAdapter;
import com.ufsoft.table.event.PropertyChangeListenerAdapter;

public class AreaLockAction extends AbstractLockAction {

	//图片资源只加载一次
	private ImageIcon unfreezeImage = null;
	
	private ImageIcon freezeImage = null;
	
	@Override
	protected String getImagePath() {
		if (getTable() == null) {
			return "images/reportcore/freeze.png";
		}
		return getTable().isFreezing() ? "images/reportcore/unfreeze.png"
				: "images/reportcore/freeze.png";
	}

	@Override
	protected String getName() {
		if (getTable() == null) {
			return MultiLang.getString("area_lock");
		}
		return getTable().isFreezing() ? MultiLang
				.getString("area_lock_cancel") : MultiLang
				.getString("area_lock");
	}

	@Override
	public void execute(ActionEvent e) {
		UFOTable table = getTable();
		if (table.isFreezing()) {
			if (table.isFrozenNoSplit()) {
				table.cancelSeperate();
			}
			table.setFreezing(false);
		} else {
			if (table.getSeperateRow() == 0 && table.getSeperateCol() == 0) {
				CellPosition anchor = getTable().getCellsModel()
						.getSelectModel().getAnchorCell();
				table.setFrozenNoSplit(true);
				table.setSeperatePos(anchor.getRow(), anchor.getColumn());
			}
			table.setFreezing(true);
		}

	}

	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = (PluginActionDescriptor) super
				.getPluginActionDescriptor();
		desc.setGroupPaths(new String[] { MultiLang.getString("window"),
				"areaLock" });
		desc.setMemonic('F');
		return desc;
	}

	@Override
	protected ICompentFactory getComponentFactory() {

		return new DefaultCompentFactory(){

			@Override
			protected JComponent createMenuItem(String strGroup,
					AbstractAction action) {
				final JComponent component = super.createMenuItem(strGroup, action);;
				
				getMainboard().getEventManager().addListener(new ViewerListener.Sub(){

					@Override
					public void onActive(Viewer currentActiveView,
							Viewer oldActiveView) {
						((AbstractButton)component).setText(getName()+"(F)");
						((AbstractButton)component).setIcon(getImage());
					}
					
				});
				
				getMainboard().getEventManager().addListener(new PropertyChangeListenerAdapter(){

					public void propertyChange(PropertyChangeEventAdapter evt) {
						if(evt.getPropertyName().equals("seperate2lock"))
						((AbstractButton)component).setText(getName()+"(F)");
					}
					
				});
				return component;
			}
	 
		};
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	private ImageIcon getImage(){
		if(getTable().isFreezing()){
			if(unfreezeImage != null){
				return unfreezeImage;
			}
			unfreezeImage = ResourceManager.createIcon(getImagePath());
			return unfreezeImage;
		} 
		
		if(freezeImage != null){
			return freezeImage;
		}
		freezeImage = ResourceManager.createIcon(getImagePath());
		return freezeImage;
	}
}
