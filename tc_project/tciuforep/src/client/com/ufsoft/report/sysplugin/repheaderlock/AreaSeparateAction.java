package com.ufsoft.report.sysplugin.repheaderlock;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.JComponent;

import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.event.ViewerListener;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.UFOTable;
import com.ufsoft.table.event.PropertyChangeEventAdapter;
import com.ufsoft.table.event.PropertyChangeListenerAdapter;

public class AreaSeparateAction extends AbstractLockAction{

	@Override
	protected String getImagePath() {
		return "images/reportcore/split.png";
	}

	@Override
	protected String getName() {
		return isSplitState() ? MultiLang.getString("area_seperate_cancel")
				: MultiLang.getString("area_seperate");
	}

	@Override
	public void execute(ActionEvent e) {
		UFOTable table = getTable();
		if (table.isFrozenNoSplit()) {
			if (table.isFreezing()) {
				table.setFreezing(false);
			} else {
				// throw new IllegalStateException(); liuyy. 2007-04-19 
			}
			table.setFrozenNoSplit(false);
		} else {
			if (isSplitState()) {
				table.cancelSeperate();
			} else {
				CellPosition anchor = table.getCellsModel().getSelectModel().getAnchorCell();
				table.setSeperatePos(anchor.getRow(),
						anchor.getColumn());
			}
			getTable().setFreezing(false);
		}
	}

	@Override
	protected ICompentFactory getComponentFactory() {

		return new DefaultCompentFactory(){

			@Override
			protected JComponent createMenuItem(String strGroup,
					AbstractAction action) {
				final JComponent component = super.createMenuItem(strGroup, action);
				getMainboard().getEventManager().addListener(new ViewerListener.Sub(){

					@Override
					public void onActive(Viewer currentActiveView,
							Viewer oldActiveView) {
						((AbstractButton)component).setText(getName()+"(S)");
					}
					
				});
				
				getMainboard().getEventManager().addListener(new PropertyChangeListenerAdapter(){

					public void propertyChange(PropertyChangeEventAdapter evt) {
						if(evt.getPropertyName().equals("seperate2lock"))
						((AbstractButton)component).setText(getName()+"(S)");
					}
					
				});
				return component;
			}
			
		};
	}

	private boolean isSplitState() {
		UFOTable table = getTable();
		if(table == null || table.getCellsModel() == null){
			return false;
		}
		if (table.isFrozenNoSplit()) {
			return false;
		} else {
			return isRealSplitState();
		}
	}
	
	private boolean isRealSplitState() {
		UFOTable table = getTable();
		if (table.getSeperateRow() == 0
				&& table.getSeperateCol() == 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor desc = (PluginActionDescriptor) super
				.getPluginActionDescriptor();
		desc.setGroupPaths(new String[] { MultiLang.getString("window"),
				"areaLock" });
		desc.setMemonic('S');
		return desc;
	}

}
