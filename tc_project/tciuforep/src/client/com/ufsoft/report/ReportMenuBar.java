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
 * ] 报表工具中的菜单条
 */
public class ReportMenuBar extends nc.ui.pub.beans.UIMenuBar {
//	/** 菜单位置标识：文件菜单起始位置 */
//	public static int FILE_BEGIN = 1;
//	/** 菜单位置标识：文件菜单结束位置 */
//	public static int FILE_END = 2;
//	/** 菜单位置标识：编辑菜单起始位置 */
//	public static int EDIT_BEGIN = 3;
//	/** 菜单位置标识：编辑菜单起始位置 */
//	public static int EDIT_END = 4;
//	/** 菜单位置标识：格式菜单起始位置 */
//	public static int FORMAT_BEGIN = 5;
//	/** 菜单位置标识：格式菜单起始位置 */
//	public static int FORMAT_END = 6;
//	/** 菜单位置标识：数据菜单 */
//	public static int DATA_BEGIN = 7;
//	/** 菜单位置标识：数据菜单 */
//	public static int DATA_END = 8;
//	/** 菜单位置标识：工具菜单 */
//	public static int TOOL_BEGIN = 9;
//	/** 菜单位置标识：工具菜单 */
//	public static int TOOL_END = 10;
//	/** 菜单位置标识：帮助菜单 */
//	public static int HELP_BEGIN = 11;
//	/** 菜单位置标识：帮助菜单 */
//	public static int HELP_END = 12;
	/** 菜单位置标识：用户自定义。该选项的菜单将在Help菜单之前生成。 */
	//	public static int CUSTOM = Integer.MAX_VALUE;
     
	private static final long serialVersionUID = 1244898328654593204L;
	
	/**预制菜单默认显示属性为false，当添加有子菜单后，才修改显示状态为true*/
	private final String[] DefaultMenuName = {
            MultiLang.getString("file"), //"文件", 
            MultiLang.getString("edit"),//"编辑", 
            MultiLang.getString("view"),//"视图",
            MultiLang.getString("format"),//"格式", 
            MultiLang.getString("data"),//"数据", 
            MultiLang.getString("tool"),//"工具",
            MultiLang.getString("window"),//"窗口"
            MultiLang.getString("help"),//"帮助"
    };
	private final char[] DefaultMnemonic = {'F', 'E', 'V', 'M', 'D', 'T', 'W', 'H'};

	/** 记录父容器 */
	private UfoReport m_report;

	/**
	 * UfoMenuBar 构造子注解。
	 * 
	 * @param report
	 *            父容器实例
	 */
	public ReportMenuBar(UfoReport report) {
		super();
		m_report = report;
		createDefaultMenu();

	}
	/**
	 * 创建第一层次的缺省菜单。
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
	 * 动态的添加菜单的内容。 此处添加的菜单只允许：添加在预制的菜单项下面或者是自定义菜单的菜单组。
	 * 
	 * @param ext
	 *            功能描述。如果为des.getName()==NUll,则添加分割栏。
	 */
	public Component addExtension(IActionExt ext,ActionUIDes uiDes) {	    
	    JComponent parent =MenuUtil.getCompByPath(uiDes.getPaths(),0,this,m_report);	    
	    JComponent comp = MenuUtil.createActionComp(ext, uiDes,m_report);
	    MenuUtil.addCompToParent(comp,parent,uiDes);
	    return comp;
	    
	}
}