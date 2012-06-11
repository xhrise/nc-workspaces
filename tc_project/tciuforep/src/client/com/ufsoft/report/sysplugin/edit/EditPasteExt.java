package com.ufsoft.report.sysplugin.edit;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyEvent;

import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;

import nc.ui.pub.beans.UIMenuItem;

import com.ufsoft.report.StateUtil;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.menu.UFPopupMenu;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
/**
 * 粘贴插件 add by 2008-3-25 王宇光
 * @author wangyga
 *
 */
public class EditPasteExt extends AbsActionExt{// implements IMainMenuExt {

	/** 定义选择性粘贴的选项：全部 */
	public static final String ALL = "ALL";
	/** 定义选择性粘贴的选项：格式 */
	public static final String FORMAT = "FORMAT";
	/** 定义选择性粘贴的选项：内容 */
	public static final String CONTENT = "CONTENT";
	/** 定义选择性粘贴的选项：边框除外 */
	public static final String NO_BORDER = "NO_BORDER";
	/** 定义选择性粘贴的选项：列宽 */
	public static final String WIDTH = "WIDTH";
	/** 定义选择性粘贴的选项：批注 */
	public static final String POSTIL = "POSTIL";
	/** 定义选择性粘贴的选项：值和数字格式 */
	public static final String VALUE_NUM_FORMAT = "VALUE_NUM_FORMAT";
	/** 定义选择性粘贴的选项：转置 */
	public static final String TRANSFER = "TRANSFER";
	/** 定义选择性粘贴的选项：选择性粘贴对话框 */
	public static final String CHOOSE_DIALOG = "CHOOSE_DIALOG";
	/** 选择性粘贴的组件 */
	private JComboBox item = null;

	private UfoReport _report;
	
	public EditPasteExt(UfoReport report) {
		super();
		_report = report;
	}
 
	/* @see com.ufsoft.report.plugin.ICommandExt#getImageFile()
	 */
	public String getImageFile() {
		return "paste.gif";
	}

	/* 
	 * @see com.ufsoft.report.plugin.ICommandExt#getCommand()
	 */
	public UfoCommand getCommand() {
		return new UfoCommand(){
			public void execute(Object[] params) {
				if(!isEnabled(null))
					return;
				if (params == null || params.length < 2) {
					return;
				}
				AbsChoosePaste choosePaste = (AbsChoosePaste) params[1];// 获得状态对象
				if (choosePaste == null) {
					return;
				}
				choosePaste.choosePaste();// 执行粘贴
			}
			
		};
	}

	/* modify by 王宇光 2008-4-14 获得选择性粘贴对象
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		Object[] aryParams = new Object[2];// 对象数组
		AbsChoosePaste choosePaste = null;
		JComboBox comboBox = getItem();// 获得选择性粘贴控件的引用
		if (comboBox == null) {// 粘贴快捷键的处理：默认是粘贴全部
			comboBox = new JComboBox();
		}

		Object selectValueObj = comboBox.getSelectedItem();// 获得选择性粘贴的值
		if (selectValueObj == null) {// 如果此值为NULL，则是用快捷键粘贴
			if(isFormatState(_report)){
				choosePaste = new EditPasteAll(_report, false);
			}else{
				choosePaste = new EditPasteContent(_report, false);//数据态的复制、剪切、粘贴都时针对值，不包括格式等信息
			}	
		}
		if (selectValueObj != null) {// 获得由下拉菜单选择返回的状态对象
			choosePaste = getSelectPaste(selectValueObj.toString());
		}
		comboBox.getModel().setSelectedItem(null);
		aryParams[0] = container;
		aryParams[1] = choosePaste;// 选择时的状态对象实例
		return aryParams;
	}

	/**add by 王宇光 2008-4-14
	 * 获得由下拉菜单选择返回的状态对象
	 * 
	 * @param String
	 *            strSelectItem:选择下拉菜单时选中的值
	 * 
	 */
	protected AbsChoosePaste getSelectPaste(String strSelectItem) {
		if (strSelectItem == null || strSelectItem.length() == 0) {
			return null;
		}
		AbsChoosePaste choosePaste = null;
		if (strSelectItem.trim().equals(CONTENT)) {// 内容
			choosePaste = new EditPasteContent(_report, false);
		} else if (strSelectItem.trim().equals(FORMAT)) {// 格式
			choosePaste = new EditPasteFormat(_report, false);
		} else if (strSelectItem.trim().equals(NO_BORDER)) {// 边框除外
			choosePaste = new EditPasteNoBorder(_report, false);
		} else if (strSelectItem.trim().equals(TRANSFER)) {// 转置
			choosePaste = new EditPasteAll(_report, true);
		} else if (strSelectItem.trim().equals(CHOOSE_DIALOG)) {// 弹出选择性粘贴对话框
			ChoosePasteDailog pasteDlg = getChoosePasteDlg(_report);// 选择性粘贴对话框
			pasteDlg.show();
			if (pasteDlg.getResult() == UfoDialog.ID_OK) {
				choosePaste = pasteDlg.getChoosePaste();
			}
		}
		return choosePaste;
	}

	private boolean isFormatState(UfoReport report){
		return report.getOperationState() == UfoReport.OPERATION_FORMAT;
	}

	
    @Override
	public void initListenerByComp(Component stateChangeComp) {
    	if(stateChangeComp instanceof JComboBox){
			setItem((JComboBox) stateChangeComp);
		}
	}

	/*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return StateUtil.isAnchorEditable(_report.getCellsModel());
    }
    /*
     * @see com.ufsoft.report.plugin.AbsActionExt#getUIDesArr()
     */
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes1 = new ActionUIDes();
        uiDes1.setName(MultiLang.getString("miufo1000655"));
        uiDes1.setPaths(new String[]{MultiLang.getString("edit")});
        uiDes1.setGroup(MultiLang.getString("edit"));
        uiDes1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
           	
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(MultiLang.getString("miufo1000655"));
        uiDes.setImageFile("reportcore/paste.gif");
        uiDes.setToolBar(true);                    
        uiDes.setGroup(MultiLang.getString("edit"));
        uiDes.setComboType(2);
        uiDes.setListCombo(true);
        uiDes.setComboComponent(createComboxMenu());       
        uiDes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
        
        ActionUIDes uiDes2 = new ActionUIDes();
        uiDes2.setName(MultiLang.getString("miufo1000655"));
        uiDes2.setImageFile("reportcore/paste.gif");
        uiDes2.setPopup(true);
        uiDes2.setGroup(MultiLang.getString("edit"));
        uiDes2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_MASK));
        return new ActionUIDes[]{uiDes1,uiDes,uiDes2};
    }
    
    /**
	 * 返回下拉弹出的菜单部分
	 * 
	 * @param
	 * @return JPopupMenu
	 */
	private JPopupMenu createComboxMenu(){
		JPopupMenu menu = new UFPopupMenu();
		
        JMenuItem contentItem = new UIMenuItem(MultiLang.getString("miufo1000275"));
        contentItem.setActionCommand(EditPasteExt.CONTENT);
        menu.add(contentItem);
        
        JMenuItem formatItem = new UIMenuItem(MultiLang.getString("miufo1000877"));
        formatItem.setActionCommand(EditPasteExt.FORMAT);
        menu.add(formatItem);
        
        JMenuItem noBorderItem = new UIMenuItem(MultiLang.getString("miufo1004057"));
        noBorderItem.setActionCommand(EditPasteExt.NO_BORDER);
        menu.add(noBorderItem);
        
        JMenuItem turnItem = new UIMenuItem(MultiLang.getString("miufo1004048"));
        turnItem.setActionCommand(EditPasteExt.TRANSFER);
        menu.add(turnItem);
        
        menu.addSeparator();
        
        JMenuItem chooseItem = new UIMenuItem(MultiLang.getString("miufo1004049"));
        chooseItem.setActionCommand(EditPasteExt.CHOOSE_DIALOG);
        menu.add(chooseItem);
        return menu;
	}
	
	public JComboBox getItem() {
		return item;
	}
	public void setItem(JComboBox item) {
		this.item = item;
	}

	protected UfoReport getReport(){
		return _report;
	}
	
	private ChoosePasteDailog getChoosePasteDlg(Container owner) {
		return new ChoosePasteDailog(owner);
	}
}
