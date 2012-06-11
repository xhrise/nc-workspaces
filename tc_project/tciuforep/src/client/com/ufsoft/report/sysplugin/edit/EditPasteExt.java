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
 * ճ����� add by 2008-3-25 �����
 * @author wangyga
 *
 */
public class EditPasteExt extends AbsActionExt{// implements IMainMenuExt {

	/** ����ѡ����ճ����ѡ�ȫ�� */
	public static final String ALL = "ALL";
	/** ����ѡ����ճ����ѡ���ʽ */
	public static final String FORMAT = "FORMAT";
	/** ����ѡ����ճ����ѡ����� */
	public static final String CONTENT = "CONTENT";
	/** ����ѡ����ճ����ѡ��߿���� */
	public static final String NO_BORDER = "NO_BORDER";
	/** ����ѡ����ճ����ѡ��п� */
	public static final String WIDTH = "WIDTH";
	/** ����ѡ����ճ����ѡ���ע */
	public static final String POSTIL = "POSTIL";
	/** ����ѡ����ճ����ѡ�ֵ�����ָ�ʽ */
	public static final String VALUE_NUM_FORMAT = "VALUE_NUM_FORMAT";
	/** ����ѡ����ճ����ѡ�ת�� */
	public static final String TRANSFER = "TRANSFER";
	/** ����ѡ����ճ����ѡ�ѡ����ճ���Ի��� */
	public static final String CHOOSE_DIALOG = "CHOOSE_DIALOG";
	/** ѡ����ճ������� */
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
				AbsChoosePaste choosePaste = (AbsChoosePaste) params[1];// ���״̬����
				if (choosePaste == null) {
					return;
				}
				choosePaste.choosePaste();// ִ��ճ��
			}
			
		};
	}

	/* modify by ����� 2008-4-14 ���ѡ����ճ������
	 * @see com.ufsoft.report.plugin.ICommandExt#getParams(com.ufsoft.report.UfoReport)
	 */
	public Object[] getParams(UfoReport container) {
		Object[] aryParams = new Object[2];// ��������
		AbsChoosePaste choosePaste = null;
		JComboBox comboBox = getItem();// ���ѡ����ճ���ؼ�������
		if (comboBox == null) {// ճ����ݼ��Ĵ���Ĭ����ճ��ȫ��
			comboBox = new JComboBox();
		}

		Object selectValueObj = comboBox.getSelectedItem();// ���ѡ����ճ����ֵ
		if (selectValueObj == null) {// �����ֵΪNULL�������ÿ�ݼ�ճ��
			if(isFormatState(_report)){
				choosePaste = new EditPasteAll(_report, false);
			}else{
				choosePaste = new EditPasteContent(_report, false);//����̬�ĸ��ơ����С�ճ����ʱ���ֵ����������ʽ����Ϣ
			}	
		}
		if (selectValueObj != null) {// ����������˵�ѡ�񷵻ص�״̬����
			choosePaste = getSelectPaste(selectValueObj.toString());
		}
		comboBox.getModel().setSelectedItem(null);
		aryParams[0] = container;
		aryParams[1] = choosePaste;// ѡ��ʱ��״̬����ʵ��
		return aryParams;
	}

	/**add by ����� 2008-4-14
	 * ����������˵�ѡ�񷵻ص�״̬����
	 * 
	 * @param String
	 *            strSelectItem:ѡ�������˵�ʱѡ�е�ֵ
	 * 
	 */
	protected AbsChoosePaste getSelectPaste(String strSelectItem) {
		if (strSelectItem == null || strSelectItem.length() == 0) {
			return null;
		}
		AbsChoosePaste choosePaste = null;
		if (strSelectItem.trim().equals(CONTENT)) {// ����
			choosePaste = new EditPasteContent(_report, false);
		} else if (strSelectItem.trim().equals(FORMAT)) {// ��ʽ
			choosePaste = new EditPasteFormat(_report, false);
		} else if (strSelectItem.trim().equals(NO_BORDER)) {// �߿����
			choosePaste = new EditPasteNoBorder(_report, false);
		} else if (strSelectItem.trim().equals(TRANSFER)) {// ת��
			choosePaste = new EditPasteAll(_report, true);
		} else if (strSelectItem.trim().equals(CHOOSE_DIALOG)) {// ����ѡ����ճ���Ի���
			ChoosePasteDailog pasteDlg = getChoosePasteDlg(_report);// ѡ����ճ���Ի���
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
	 * �������������Ĳ˵�����
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
