package com.ufsoft.report.plugin;

import javax.swing.KeyStroke;

import com.ufsoft.table.format.TableConstant;

/**
 * �˵������������Ҽ���״̬��UI��Ϣ��
 * 
 * @author zzl 2005-6-28
 * 
 * @deprecated
 */
public class ActionUIDes {
	private String tooltip;

	private String imageFile;
	/** �˵���˵����·�� */
	private String[] paths;
	/** �˵���˵�������� */
	private String name = "";
	private boolean directory;
	// add by guogang 2007-5-31 ���������˵���ǩ��֧�������˵�
	/** �Ƿ��������˵�*/
	private boolean listCombo;
	//modify by ����� 2008-4-25
	/** ���������� :0=��ͨ,1=���������ColorPanel,2=�����˵�menu*/
	private int comboType=0;
	/** �����˵���ȡindex����ȡitem:true/index*/
	private boolean isKey;
	/** �������˵��ؼ���*/
	private int listKey;
	/** �������˵���ѡ��*/
	private Object defaultSelected;
	/** ��ͨ��������б�*/
	private String[] listItem;
	/** �������*/
//	private SwatchPanel comboPanel;
	/** add by ����� 2008-3-22 ֧��ͨ�õ������б������:����panel������menu*/
	private Object comboComponent;
	/** �Ƿ��Ǹ�ѡ��˵����Ҫ����֧����ͼ�˵�����ͼѡ��*/
	private boolean checkBoxMenuItem;
	/** ��ͼ��Ӧ����ĸ�panelλ��*/
    private int viewComponentPos;
    /** ��ͼ��Ӧ��Panel����*/
    private String viewPanelClassName;
	/** �˵���˵���ļ���� */
	private int mnemonic;
	/** �˵���˵���ļ��ټ� */
	private KeyStroke accelerator;
	/** �˵����ͣ��Ƿ񵯳��˵� */
	private boolean popup;
	/** �����˵���ʾ������Ƿ񵯳��˵� */
	private int popupAimComp = TableConstant.UNDEFINED;
	/**���ܵ��Ƿ���빤����*/
	private boolean toolBar;
	/**������������߲˵����� */
	private String group; 
	/**�Ƿ񵯳�dialog,���true��˵����"..." */
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
	 * @return ���� imageFile��
	 */
	public String getImageFile() {
		return imageFile;
	}

	/**
	 * @param imageFile
	 *            Ҫ���õ� imageFile��
	 */
	public void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	/**
	 * @return ���� name��
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            Ҫ���õ� name��
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return ���� paths��
	 */
	public String[] getPaths() {
		return paths;
	}

	/**
	 * @param paths
	 *            Ҫ���õ� paths��
	 */
	public void setPaths(String[] paths) {
		this.paths = paths;
	}

	/**
	 * @return ���� tooltip��
	 */
	public String getTooltip() {
		return tooltip == null ? getName() : tooltip;
	}

	/**
	 * @param tooltip
	 *            Ҫ���õ� tooltip��
	 */
	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}

	/**
	 * @return ���� directory��
	 */
	public boolean isDirectory() {
		return directory;
	}

	/**
	 * @param directory
	 *            Ҫ���õ� directory��
	 */
	public void setDirectory(boolean dirrctory) {
		this.directory = dirrctory;
	}
	/**
	 * @return ���� accelerator��
	 */
	public KeyStroke getAccelerator() {
		return accelerator;
	}

	/**
	 * @param accelerator
	 *            Ҫ���õ� accelerator��
	 */
	public void setAccelerator(KeyStroke accelerator) {
		this.accelerator = accelerator;
	}

	/**
	 * @return ���� mnemonic��
	 */
	public int getMnemonic() {
		return mnemonic;
	}

	/**
	 * @param mnemonic
	 *            Ҫ���õ� mnemonic��
	 */
	public void setMnemonic(int mnemonic) {
		this.mnemonic = mnemonic;
	}

	/**
	 * @return ���� popup��
	 */
	public boolean isPopup() {
		return popup;
	}

	/**
	 * @param popup
	 *            Ҫ���õ� popup��
	 */
	public void setPopup(boolean popup) {
		this.popup = popup;
	}

	/**
	 * @return ���� toolBar��
	 */
	public boolean isToolBar() {
		return toolBar;
	}

	/**
	 * @param toolBar
	 *            Ҫ���õ� toolBar��
	 */
	public void setToolBar(boolean toolBar) {
		this.toolBar = toolBar;
	}

	/**
	 * @return ���� group��
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * @param group
	 *            Ҫ���õ� group��
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
