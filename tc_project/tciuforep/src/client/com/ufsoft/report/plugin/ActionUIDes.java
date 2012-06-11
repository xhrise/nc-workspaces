package com.ufsoft.report.plugin;

import javax.swing.KeyStroke;

import com.ufsoft.table.format.TableConstant;

/**
 * 菜单、工具栏、右键、状态栏UI信息。
 * 
 * @author zzl 2005-6-28
 * 
 * @deprecated
 */
public class ActionUIDes {
	private String tooltip;

	private String imageFile;
	/** 菜单或菜单项的路径 */
	private String[] paths;
	/** 菜单或菜单项的名字 */
	private String name = "";
	private boolean directory;
	// add by guogang 2007-5-31 增加下拉菜单标签以支持下拉菜单
	/** 是否是下拉菜单*/
	private boolean listCombo;
	//modify by 王宇光 2008-4-25
	/** 下拉框类型 :0=普通,1=下拉面板是ColorPanel,2=下拉菜单menu*/
	private int comboType=0;
	/** 下拉菜单是取index还是取item:true/index*/
	private boolean isKey;
	/** 该下拉菜单关键字*/
	private int listKey;
	/** 该下拉菜单首选项*/
	private Object defaultSelected;
	/** 普通下拉框的列表*/
	private String[] listItem;
	/** 下拉面板*/
//	private SwatchPanel comboPanel;
	/** add by 王宇光 2008-3-22 支持通用的下拉列表弹出组件:下拉panel和下拉menu*/
	private Object comboComponent;
	/** 是否是复选框菜单项，主要用来支持视图菜单中视图选项*/
	private boolean checkBoxMenuItem;
	/** 视图对应组件的父panel位置*/
    private int viewComponentPos;
    /** 视图对应的Panel类名*/
    private String viewPanelClassName;
	/** 菜单或菜单项的记忆键 */
	private int mnemonic;
	/** 菜单或菜单项的加速键 */
	private KeyStroke accelerator;
	/** 菜单类型：是否弹出菜单 */
	private boolean popup;
	/** 弹出菜单显示组件：是否弹出菜单 */
	private int popupAimComp = TableConstant.UNDEFINED;
	/**功能点是否加入工具栏*/
	private boolean toolBar;
	/**工具栏分组或者菜单分组 */
	private String group; 
	/**是否弹出dialog,如果true则菜单后加"..." */
	private boolean isShowDialog = false;

	public int getViewComponentPos() {
		return viewComponentPos;
	}

	public void setViewComponentPos(int viewComponentPos) {
		this.viewComponentPos = viewComponentPos;
	}

	public String getViewPanelClassName() {
		return viewPanelClassName;
	}

	public void setViewPanelClassName(String viewPanelClassName) {
		this.viewPanelClassName = viewPanelClassName;
	}

	public boolean isCheckBoxMenuItem() {
		return checkBoxMenuItem;
	}

	public void setCheckBoxMenuItem(boolean checkBoxMenuItem) {
		this.checkBoxMenuItem = checkBoxMenuItem;
	}

	public Object getDefaultSelected() {
		return defaultSelected;
	}

	public void setDefaultSelected(Object defaultSelected) {
		this.defaultSelected = defaultSelected;
	}

	public boolean isListCombo() {
		return listCombo;
	}

	public void setListCombo(boolean listCombo) {
		this.listCombo = listCombo;
	}
    
	public int getComboType() {
		return comboType;
	}

	public void setComboType(int comboType) {
		this.comboType = comboType;
	}

	public boolean isKey() {
		return isKey;
	}

	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}

	public String[] getListItem() {
		return listItem;
	}

	public void setListItem(String[] listItem) {
		this.listItem = listItem;
	}

	public int getListKey() {
		return listKey;
	}

	public void setListKey(int listKey) {
		this.listKey = listKey;
	}
    
	// end add

	/**
	 * @return 返回 imageFile。
	 */
	public String getImageFile() {
		return imageFile;
	}

	/**
	 * @param imageFile
	 *            要设置的 imageFile。
	 */
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	/**
	 * @return 返回 name。
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            要设置的 name。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 paths。
	 */
	public String[] getPaths() {
		return paths;
	}

	/**
	 * @param paths
	 *            要设置的 paths。
	 */
	public void setPaths(String[] paths) {
		this.paths = paths;
	}

	/**
	 * @return 返回 tooltip。
	 */
	public String getTooltip() {
		return tooltip == null ? getName() : tooltip;
	}

	/**
	 * @param tooltip
	 *            要设置的 tooltip。
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * @return 返回 directory。
	 */
	public boolean isDirectory() {
		return directory;
	}

	/**
	 * @param directory
	 *            要设置的 directory。
	 */
	public void setDirectory(boolean dirrctory) {
		this.directory = dirrctory;
	}
	/**
	 * @return 返回 accelerator。
	 */
	public KeyStroke getAccelerator() {
		return accelerator;
	}

	/**
	 * @param accelerator
	 *            要设置的 accelerator。
	 */
	public void setAccelerator(KeyStroke accelerator) {
		this.accelerator = accelerator;
	}

	/**
	 * @return 返回 mnemonic。
	 */
	public int getMnemonic() {
		return mnemonic;
	}

	/**
	 * @param mnemonic
	 *            要设置的 mnemonic。
	 */
	public void setMnemonic(int mnemonic) {
		this.mnemonic = mnemonic;
	}

	/**
	 * @return 返回 popup。
	 */
	public boolean isPopup() {
		return popup;
	}

	/**
	 * @param popup
	 *            要设置的 popup。
	 */
	public void setPopup(boolean popup) {
		this.popup = popup;
	}

	/**
	 * @return 返回 toolBar。
	 */
	public boolean isToolBar() {
		return toolBar;
	}

	/**
	 * @param toolBar
	 *            要设置的 toolBar。
	 */
	public void setToolBar(boolean toolBar) {
		this.toolBar = toolBar;
	}

	/**
	 * @return 返回 group。
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            要设置的 group。
	 */
	public void setGroup(String toolBarGroup) {
		this.group = toolBarGroup;
	}

	public Object clone() {
		ActionUIDes uiDes = new ActionUIDes();
		uiDes.setAccelerator(this.getAccelerator());
		uiDes.setDirectory(this.isDirectory());
		uiDes.setImageFile(this.getImageFile());
		uiDes.setMnemonic(this.getMnemonic());
		uiDes.setName(this.getName());
		uiDes.setPaths(this.getPaths());
		uiDes.setPopup(this.isPopup());
		uiDes.setToolBar(this.isToolBar());
		uiDes.setGroup(this.getGroup());
		uiDes.setTooltip(this.getTooltip());
		
		uiDes.setListCombo(this.isListCombo());
		uiDes.setKey(this.isKey());
		uiDes.setListItem(this.getListItem());
		uiDes.setListKey(this.getListKey());
		uiDes.setDefaultSelected(this.getDefaultSelected());
		uiDes.setCheckBoxMenuItem(this.isCheckBoxMenuItem());
		uiDes.setViewComponentPos(this.getViewComponentPos());
		uiDes.setViewPanelClassName(this.getViewPanelClassName());
		uiDes.setPopupAimComp(this.getPopupAimComp());
		return uiDes;
	}

	public int getPopupAimComp() {
		return popupAimComp;
	}

	public void setPopupAimComp(int popupAimComp) {
		this.popupAimComp = popupAimComp;
	}

	public Object getComboComponent() {
		return comboComponent;
	}

	public void setComboComponent(Object comboComponent) {
		this.comboComponent = comboComponent;
	}

	public boolean isShowDialog() {
		return isShowDialog;
	}

	public void setShowDialog(boolean isShowDialog) {
		this.isShowDialog = isShowDialog;
	}
	
	
}
