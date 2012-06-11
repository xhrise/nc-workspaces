//Source file: F:\\workspace\\reportTool\\src\\com\\ufsoft\\report\\ReportMenuBar.java

package com.ufsoft.report;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JMenu;

import com.ufsoft.report.menu.MenuUtil;
import com.ufsoft.report.menu.UFMenu;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.plugin.IActionExt;
import com.ufsoft.report.util.MultiLang;
/**
 * ] �������еĲ˵���
 */
public class ReportMenuBar extends nc.ui.pub.beans.UIMenuBar {
//	/** �˵�λ�ñ�ʶ���ļ��˵���ʼλ�� */
//	public static int FILE_BEGIN = 1;
//	/** �˵�λ�ñ�ʶ���ļ��˵�����λ�� */
//	public static int FILE_END = 2;
//	/** �˵�λ�ñ�ʶ���༭�˵���ʼλ�� */
//	public static int EDIT_BEGIN = 3;
//	/** �˵�λ�ñ�ʶ���༭�˵���ʼλ�� */
//	public static int EDIT_END = 4;
//	/** �˵�λ�ñ�ʶ����ʽ�˵���ʼλ�� */
//	public static int FORMAT_BEGIN = 5;
//	/** �˵�λ�ñ�ʶ����ʽ�˵���ʼλ�� */
//	public static int FORMAT_END = 6;
//	/** �˵�λ�ñ�ʶ�����ݲ˵� */
//	public static int DATA_BEGIN = 7;
//	/** �˵�λ�ñ�ʶ�����ݲ˵� */
//	public static int DATA_END = 8;
//	/** �˵�λ�ñ�ʶ�����߲˵� */
//	public static int TOOL_BEGIN = 9;
//	/** �˵�λ�ñ�ʶ�����߲˵� */
//	public static int TOOL_END = 10;
//	/** �˵�λ�ñ�ʶ�������˵� */
//	public static int HELP_BEGIN = 11;
//	/** �˵�λ�ñ�ʶ�������˵� */
//	public static int HELP_END = 12;
	/** �˵�λ�ñ�ʶ���û��Զ��塣��ѡ��Ĳ˵�����Help�˵�֮ǰ���ɡ� */
	//	public static int CUSTOM = Integer.MAX_VALUE;
     
	private static final long serialVersionUID = 1244898328654593204L;
	
	/**Ԥ�Ʋ˵�Ĭ����ʾ����Ϊfalse����������Ӳ˵��󣬲��޸���ʾ״̬Ϊtrue*/
	private final String[] DefaultMenuName = {
            MultiLang.getString("file"), //"�ļ�", 
            MultiLang.getString("edit"),//"�༭", 
            MultiLang.getString("view"),//"��ͼ",
            MultiLang.getString("format"),//"��ʽ", 
            MultiLang.getString("data"),//"����", 
            MultiLang.getString("tool"),//"����",
            MultiLang.getString("window"),//"����"
            MultiLang.getString("help"),//"����"
    };
	private final char[] DefaultMnemonic = {'F', 'E', 'V', 'M', 'D', 'T', 'W', 'H'};

	/** ��¼������ */
	private UfoReport m_report;

	/**
	 * UfoMenuBar ������ע�⡣
	 * 
	 * @param report
	 *            ������ʵ��
	 */
	public ReportMenuBar(UfoReport report) {
		super();
		m_report = report;
		createDefaultMenu();

	}
	/**
	 * ������һ��ε�ȱʡ�˵���
	 */
	private void createDefaultMenu() {
		for (int i = 0; i < DefaultMenuName.length; i++) {
			createMenu(DefaultMenuName[i], DefaultMnemonic[i]);
		}
	}
	private void createMenu(String name, char c) {
//		if (name != null && c > 0) {
//			name += " (" + c + ")";
//		}
		JMenu menu = new UFMenu(name,m_report);
		menu.setMnemonic(c);
		add(menu);
	}
	/**
	 * ��̬����Ӳ˵������ݡ� �˴���ӵĲ˵�ֻ���������Ԥ�ƵĲ˵�������������Զ���˵��Ĳ˵��顣
	 * 
	 * @param ext
	 *            �������������Ϊdes.getName()==NUll,����ӷָ�����
	 */
	public Component addExtension(IActionExt ext,ActionUIDes uiDes) {	    
	    JComponent parent =MenuUtil.getCompByPath(uiDes.getPaths(),0,this,m_report);	    
	    JComponent comp = MenuUtil.createActionComp(ext, uiDes,m_report);
	    MenuUtil.addCompToParent(comp,parent,uiDes);
	    return comp;
	    
	}
}