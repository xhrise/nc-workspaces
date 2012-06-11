package com.ufsoft.iufo.fmtplugin.measure;

import java.awt.Component;

import javax.swing.KeyStroke;

import nc.ui.pub.beans.UIMenu;

import com.ufsoft.iufo.fmtplugin.key.AbsEditorAction;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.ReportMenuBar;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.menu.UFCheckBoxPopMenuItem;
import com.ufsoft.report.menu.UFMenu;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.AbstractPlugDes;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.ICommandExt;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.report.plugin.ToggleMenuUIDes;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.table.SelectModel;

/**
 * <pre>
 * </pre>
 * @author zzl
 * @version 5.0
 * Create on 2004-11-10
 */
public class MeasureDescriptor extends AbstractPlugDes{// IPluginDescriptor {
	public MeasureDescriptor(MeasureDefPlugIn plugin) {
		super(plugin);
	}
	
	/* @see com.ufsoft.report.plugin.IPluginDescriptor#getName()
	 */
	public String getName() {
		return StringResource.getStringResource("uiuforep0000600");
	}

	/* @see com.ufsoft.report.plugin.IPluginDescriptor#getNote()
	 */
	public String getNote() {
		return StringResource.getStringResource("uiuforep0000600");
	}

	/* @see com.ufsoft.report.plugin.IPluginDescriptor#getPluginPrerequisites()
	 */
	public String[] getPluginPrerequisites() {
		return null;
	}

	/* @see com.ufsoft.report.plugin.IPluginDescriptor#getExtensions()
	 */
	public IExtension[] createExtensions() {
	    return new ICommandExt[]{
	    		new AbsActionExt(){
					public UfoCommand getCommand() {
						return null;
					}
					@Override
					public Object[] getParams(UfoReport container) {
						return null;
					}
					@Override
					public ActionUIDes[] getUIDesArr() {
						ActionUIDes uiDes = new ActionUIDes();
						uiDes.setName(StringResource.getStringResource("miufo1004045"));
						uiDes.setPaths(new String[]{MultiLang.getString("format")});
						uiDes.setDirectory(true);
						uiDes.setGroup("styleMngExt");
												
						return new ActionUIDes[]{uiDes};
					}					
				},
	    		new MeasureDefineExt(),new MeasureMngExt(), new MeasureRendererExt(getReport())};
	}

	/* @see com.ufsoft.report.plugin.IPluginDescriptor#getHelpNode()
	 */
	public String getHelpNode() {
		return null;
	}
}

abstract class MeasureExt extends AbsActionExt{// implements IMainMenuExt{

	/* @see com.ufsoft.report.plugin.ICommandExt#getName()
	 */
	abstract public String getName();

	/* @see com.ufsoft.report.plugin.ICommandExt#getHint()
	 */
	public String getHint() {
		return null;
	}

//	/* @see com.ufsoft.report.plugin.ICommandExt#getMenuSlot()
//	 */
//	public int getMenuSlot() {
//		return ReportMenuBar.DATA_BEGIN;
//	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
	 */
	public String getImageFile() {
		return null;
	}

	/* @see com.ufsoft.report.plugin.ICommandExt#getAccelerator()
	 */
	public KeyStroke getAccelerator() {
		return null;
	}


	/* @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	abstract public UfoCommand getCommand();
	
	/* @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	abstract public Object[] getParams(UfoReport container);
	
	public String[] getPath(){
	    return new String[]{StringResource.getStringResource("uiuforep0000600")};
	}
}
	class MeasureDefineExt extends MeasureExt {//implements IPopupMenuExt{
		
			/* @see com.ufsoft.report.plugin.ICommandExt#getCommand()
		 */
		public UfoCommand getCommand() {
//			return new MeasureDefineCmd();
			return null;
		}
		/* @see com.ufsoft.report.plugin.ICommandExt#getName()
		 */
		public String getName() {
			return StringResource.getStringResource("miufo1001689");
		}
		/* @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			AbsEditorAction editorAction = new MeasureDefEditorAction(container);
	    	editorAction.execute(editorAction.getParams());
	    	
//			SelectModel sm = container.getCellsModel().getSelectModel();
//			return new Object[]{container,sm.getSelectedCells(),container.getCellsModel()};
	    	return null;
		}
//		/* @see com.ufsoft.report.plugin.ICommandExt#getRMenuPos()
//		 */
//		public int getRMenuPos() {
//			return UFOTable.POPUP_CELLSPANEL;
//		}
        /**
		 * @i18n miufo1001692=数据
         * @i18n miufo1001489=指标
		 */
        public ActionUIDes[] getUIDesArr() {
            ActionUIDes uiDes1 = new ActionUIDes();
            uiDes1.setImageFile("reportcore/measure_def.png");
            uiDes1.setName(getName());
            uiDes1.setPaths(new String[]{StringResource.getStringResource("miufo1001692")});
            uiDes1.setGroup(StringResource.getStringResource("miufo1001489"));
            
            ActionUIDes uiDes2 = new ActionUIDes();
            uiDes2.setImageFile("reportcore/measure_def.png");
            uiDes2.setName(getName());
            uiDes2.setPopup(true);
            uiDes2.setGroup(StringResource.getStringResource("miufo1001489"));
            
            ActionUIDes uiDes3 = new ActionUIDes();
            uiDes3.setImageFile("reportcore/measure_def.png");
            uiDes3.setName(getName());
            uiDes3.setGroup(StringResource.getStringResource("miufo1001489"));
            uiDes3.setToolBar(true);
			
            return new ActionUIDes[]{uiDes1,uiDes2,uiDes3};
        }
}
	
	class MeasureMngExt extends MeasureExt{
		/* @see com.ufsoft.report.plugin.ICommandExt#getCommand()
		 */
		public UfoCommand getCommand() {
			return new MeasureMgtCmd();
		}
		/* @see com.ufsoft.report.plugin.ICommandExt#getName()
		 */
		public String getName() {
			return StringResource.getStringResource("miufo1001602");//"指标管理"
		}
		/* @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
			return new Object[]{container};
		}
        /**
		 * @i18n miufo1001692=数据
         * @i18n miufo1001489=指标
		 */
        public ActionUIDes[] getUIDesArr() {
            ActionUIDes uiDes = new ActionUIDes();
            uiDes.setName(getName());
            uiDes.setPaths(new String[]{StringResource.getStringResource("miufo1001692")});
            uiDes.setGroup(StringResource.getStringResource("miufo1001489"));
            return new ActionUIDes[]{uiDes};
        }
	} 
	
	class MeasureRendererExt extends MeasureExt{
		private UfoReport _report;
		private UFCheckBoxPopMenuItem _menuItemMeasFlag = null;
		MeasureRendererExt(UfoReport report){
			_report = report;
		}
		/* @see com.ufsoft.report.plugin.ICommandExt#getCommand()
		 */
		public UfoCommand getCommand() {
			return new UfoCommand(){

				@Override
				public void execute(Object[] params) {
					MeasureDefPlugIn.setMeasRendererVisible(!MeasureDefPlugIn.isMeasRendererVisible());
					_report.getCellsModel().fireExtPropChanged(null);
				}
				
			};
		}
		/* @see com.ufsoft.report.plugin.ICommandExt#getName()
		 */
		public String getName() {
			return StringResource.getStringResource("miufo10016011112");//"指标管理"
		}
		@Override
		public boolean isEnabled(Component focusComp) {
			// @edit by wangyga at 2009-3-5,下午02:17:48 暂时先关闭
//			return true;
//			setSelectedByMeasPlugIn(getMeasFlagMenuItem());
			return true;
		}
		private void setSelectedByMeasPlugIn(UFCheckBoxPopMenuItem stateChangeComp){
			stateChangeComp.setSelected(MeasureDefPlugIn.isMeasRendererVisible());
		}
		/* @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
		 */
		public Object[] getParams(UfoReport container) {
//			MeasureDefPlugIn.setMeasRendererVisible(!MeasureDefPlugIn.isMeasRendererVisible());
//			container.getCellsModel().fireExtPropChanged(null);
			return new Object[]{container};
		}
        /**
		 * @i18n uiuforep0000790=格式
		 * @i18n miufo1004045=显示扩展属性
		 * @i18n miufo1000172=指标
		 */
		public ActionUIDes[] getUIDesArr() {
			ToggleMenuUIDes uiDes = new ToggleMenuUIDes();
			uiDes.setName(StringResource.getStringResource("miufo1000172"));
			uiDes.setPaths(new String[]{MultiLang.getString("uiuforep0000790"), StringResource.getStringResource("miufo1004045")});
			uiDes.setGroup("styleMngExt");
			uiDes.setCheckBox(true);
			return new ActionUIDes[]{uiDes};
		}
		/**
		 * @i18n uiuforep0000790=格式
		 * @i18n miufo1004045=显示扩展属性
		 * @i18n miufo1000172=指标
		 * @return
		 */
		private UFCheckBoxPopMenuItem getMeasFlagMenuItem(){
			if(_menuItemMeasFlag == null){
				ReportMenuBar memuBar = (ReportMenuBar)_report.getJMenuBar();
				int nMemuCount = memuBar.getMenuCount();
				for(int i = 0; i < nMemuCount; i++) {
					UFMenu menu = (UFMenu)memuBar.getMenu(i);
					if(menu.getName().equals(MultiLang.getString("uiuforep0000790"))) {
						Component[] menuItems = menu.getMenuComponents();
						for(int j = 0, size = menuItems.length; j < size; j++) {
							if(menuItems[j] instanceof UIMenu && 
									menuItems[j].getName().equals(StringResource.getStringResource("miufo1004045"))){
								Component[] subMenuItems = ((UIMenu)menuItems[j]).getMenuComponents();
								for(int k = 0, nsize = subMenuItems.length; k < nsize; k++) {
									if(subMenuItems[k] instanceof UFCheckBoxPopMenuItem && 
											subMenuItems[k].getName().equals(StringResource.getStringResource("miufo1000172"))) {
										return (_menuItemMeasFlag = (UFCheckBoxPopMenuItem)subMenuItems[k]);
									}
								}
							}    	                
						}
					}
				} 	
			} 
			return _menuItemMeasFlag;
		}
	} 