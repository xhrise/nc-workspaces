package com.ufsoft.iufo.inputplugin.ufobiz.data;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;

import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.edit.RepDataEditor;

import com.ufida.zior.comp.KCheckBoxMenuItem;
import com.ufida.zior.comp.KMenu;
import com.ufida.zior.comp.KMenuBar;
import com.ufida.zior.comp.KPopupMenu;
import com.ufida.zior.plugin.AbstractPluginAction;
import com.ufida.zior.plugin.DefaultCompentFactory;
import com.ufida.zior.plugin.ICompentFactory;
import com.ufida.zior.plugin.IPluginActionDescriptor;
import com.ufida.zior.plugin.PluginActionDescriptor;
import com.ufida.zior.plugin.PluginKeys;
import com.ufida.zior.plugin.PluginKeys.XPOINT;
import com.ufida.zior.util.UIUtilities;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.Viewer;
import com.ufida.zior.view.event.ViewerListener;
import com.ufsoft.iufo.resource.StringResource;

public abstract class AbsEditInputDirExt extends AbstractPluginAction{

	public void execute(ActionEvent e) {
		RepDataEditor editor=(RepDataEditor)getCurrentView();
		editor.setInputDir(getEditInputDir());
		RepDataControler.getInstance(editor.getMainboard()).setLastInputDir(getEditInputDir());
		refreshMenuItemState(editor);
	}
	
	public IPluginActionDescriptor getPluginActionDescriptor() {
		PluginActionDescriptor pad = new PluginActionDescriptor(getMenuName());
		pad.setGroupPaths(new String[]{StringResource.getStringResource("miufopublic280"),StringResource.getStringResource("uiufotask00073")});
		pad.setIcon(null);
		
		pad.setExtensionPoints(XPOINT.MENU,XPOINT.POPUPMENU);
		pad.setMemonic(getAltChar());
		
		pad.setCompentFactory(createCompFactory());
		return pad;
	}
	
	public boolean isEnabled() {
    	Viewer curView=getCurrentView();
    	if (curView==null || curView instanceof RepDataEditor==false)
    		return false;
		
    	return true;
	}
	
	protected ICompentFactory createCompFactory() {
		return new DefaultCompentFactory() {
			protected JComponent createMenuItem(String[] paths,
					JComponent root, AbstractAction action) {
				if (root instanceof KMenuBar){
					JComponent parent = root;
					
					//²Ëµ¥Â·¾¶
					for (int i = 0; i < paths.length - 1; i++) {
						String name = paths[i];
						JComponent comp = (JComponent) UIUtilities.getComponentByName(parent, name);
						if(comp == null){
							comp = new KMenu(name);				
							if(parent instanceof KMenu || parent instanceof KPopupMenu){
								String strGroup = paths[paths.length - 1];
								if(strGroup == null || strGroup.trim().length() ==0){
									strGroup = PluginKeys.GROUP;
								}					
								((KMenu)comp).setGroup(strGroup);
								addMenuToGroup(parent,comp,strGroup);
							} else {
								parent.add(comp);
							}				
						}
						if(comp instanceof KMenu && !comp.isVisible()){
							comp.setVisible(true);
						}
						parent = comp;
					}
	
					String group = paths[paths.length - 1];
					if(group == null){
						group = PluginKeys.GROUP;
					}
						
					KCheckBoxMenuItem menuCom = new MyCheckBoxMenuItem(action);
					menuCom.setIcon(null);
					menuCom.addActionListener(new MyItemListener(menuCom));
					menuCom.setGroup(group);
					
					addMenuToGroup(parent,menuCom,group);
					 
					return menuCom;
				}else{
					JComponent parent = root;
	
					String group = paths[paths.length - 1];
					if(group == null){
						group = PluginKeys.GROUP;
					}
						
					KCheckBoxMenuItem menuCom = new MyCheckBoxMenuItem(action);
					menuCom.setIcon(null);
					menuCom.addActionListener(new MyItemListener(menuCom));
					menuCom.setGroup(group);
					
					getMainboard().getEventManager().addListener(new MyViewActiveListener(menuCom));
					
					addMenuToGroup(parent,menuCom,group);
					 
					return menuCom;
				}
			}
		};
	}
	
	private void refreshMenuItemState(RepDataEditor editor){
		Mainboard mainBoard=getMainboard();
		JPopupMenu popMenu=mainBoard.getPopupMenu();		
		if (popMenu!=null){
			MenuElement[] elements=popMenu.getSubElements();
			for (int i=0;i<elements.length;i++){
				Component elemComp=elements[i].getComponent();
				if (elemComp instanceof MyCheckBoxMenuItem){
					MyCheckBoxMenuItem menuItem=(MyCheckBoxMenuItem)elemComp;
					if (menuItem.getEditInputDir()==editor.getInputDir())
						menuItem.setSelected(true);
					else
						menuItem.setSelected(false);
				}
			}
		}
		
		innerFreshMenuItemState(mainBoard.getJMenuBar(),editor);
	}
	
	private void innerFreshMenuItemState(MenuElement menu,RepDataEditor editor){
		if (menu instanceof MyCheckBoxMenuItem){
			MyCheckBoxMenuItem menuItem=(MyCheckBoxMenuItem)menu;
			if (menuItem.getEditInputDir()==editor.getInputDir())
				menuItem.setSelected(true);
			else
				menuItem.setSelected(false);
			return;
		}
		MenuElement[] subMenus=menu.getSubElements();
		if (subMenus!=null){
			for (int i=0;i<subMenus.length;i++)
				innerFreshMenuItemState(subMenus[i],editor);
		}
	}
	
	class MyViewActiveListener extends ViewerListener.Sub{
		Component item=null;
		MyViewActiveListener(Component item){
			this.item=item;
		}

		public void onActive(Viewer currentActiveView, Viewer oldActiveView) {
			if (currentActiveView instanceof RepDataEditor){
				refreshMenuItemState((RepDataEditor)currentActiveView);
			}
		}
	}
	
	class MyCheckBoxMenuItem extends KCheckBoxMenuItem{
		private static final long serialVersionUID = -8209148691293655023L;

		MyCheckBoxMenuItem(AbstractAction action){
			super(action);
			
			Icon icon=(Icon)action.getValue(AbstractAction.SMALL_ICON);
			action.putValue(AbstractAction.SMALL_ICON, null);
			setAction(action);
			action.putValue(AbstractAction.SMALL_ICON, icon);
		}
		
		int getEditInputDir(){
			return AbsEditInputDirExt.this.getEditInputDir();
		}
		
		protected void configurePropertiesFromAction(Action a) {
			super.configurePropertiesFromAction(a);
			String[] alltypes = { Action.MNEMONIC_KEY,"isShowDialog"};
			for(String strKey : alltypes){
				if(Action.MNEMONIC_KEY.equals(strKey)){
					Integer n = (a==null) ? null : (Integer)a.getValue(strKey);
					if(n != null && n.intValue() != 0){
						setText(getText() + "(" + (char) n.intValue() + ")");
					}
				} else if("isShowDialog".equals(strKey)){
					Boolean isShowDialog = (Boolean)a.getValue(strKey);
					if(isShowDialog != null && isShowDialog.booleanValue()){
						setText(getText()+"...");
					}
				}
			}
		}
	}
	
	class MyItemListener implements ActionListener{
		KCheckBoxMenuItem item=null;
		MyItemListener(KCheckBoxMenuItem item){
			this.item=item;
		}
		public void actionPerformed(ActionEvent e) {
			AbsEditInputDirExt.this.execute(null);
			
			refreshMenuItemState((RepDataEditor)getCurrentView());
		}
	}

	protected abstract int getEditInputDir();
	
	protected abstract String getMenuName();
	
	protected abstract int getAltChar();
}
