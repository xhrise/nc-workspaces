/*
 * 创建日期 2006-4-5
 *
 */
package com.ufsoft.iufo.inputplugin.biz;

import java.awt.Component;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.plugin.AbsActionExt;
import com.ufsoft.report.plugin.ActionUIDes;
import com.ufsoft.report.util.MultiLang;
/**
 * IUFO业务菜单操作的实现IExtension基础抽象类
 * 
 * @author liulp
 *
 */
public abstract class AbsIufoBizMenuExt  extends AbsActionExt {
    private UfoReport m_Report = null;
    /**
     * @param ufoReport
     */
    public AbsIufoBizMenuExt(UfoReport ufoReport){
        super();
        this.m_Report = ufoReport;
    }
    protected UfoReport getUfoReport(){
        return m_Report;
    }    
//    /**
//     * 得到执行命令需要的参数
//     * NOTICE: 否则就是利用构造函数，设置UfoReport属性。调用构造函数时在IPlgiIn.getReport()方法获得参数值
//     * @return
//     */
//    private UfoReport getUfoReport() {
//        Component com = FocusManager.getCurrentManager().getFocusOwner();
//        while(com != null){
//            if(com instanceof UfoReport){
//                return (UfoReport) com;
//            }else if(com instanceof JApplet){
//              return (UfoReport) ((JApplet)com).getRootPane();
//            }else{
//                com = com.getParent();
//            }
//        }
//        throw new IllegalArgumentException("当前运行环境中没有UfoReport对象。");
//    }    
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getMenuName());
        uiDes.setPaths(getPaths());
        setGroup(uiDes);
        return new ActionUIDes[]{uiDes};
    }
    /**
     * 设置菜单分组
     * @param uiDes
     */
    private void setGroup(ActionUIDes uiDes){
    	String toolBarGroup = getGroup();
    	if(toolBarGroup != null){
    		uiDes.setGroup(toolBarGroup);
    	}
    }
    /**
     * 得到父菜单路径
     * @return
     */
    protected abstract String[] getPaths();
    /**
     * 得到当前菜单分组
     * @return
     */
    protected abstract String getGroup();
    /**
     * 得到“数据”菜单路径
     * @return
     */
    protected static String[] doGetDataMenuPaths(){
        return new String[]{MultiLang.getString("data")};//"数据"
    }
    
    /**
     * 得到“数据－>导入”菜单路径
     * @return
     */
    protected static String[] doGetImportMenuPaths(){
        return new String[]{MultiLang.getString("data"),MultiLang.getString("import")};//"数据","导入"
    }
    
    /**
     * 得到“数据－>导出”菜单路径
     * @return
     */
    protected static String[] doGetExportMenuPaths(){
        return new String[]{MultiLang.getString("data"),MultiLang.getString("export")};//"数据","导出"
    }
    /**
     * 得到菜单显示名称
     * @return
     */
    protected abstract String getMenuName();

    public UfoCommand getCommand() {
        return doGetCommand(getUfoReport());
    }
    
    /**
     * 得到具体的UfoCommand实例
     * @param ufoReport
     * @return
     */
    protected abstract UfoCommand doGetCommand(UfoReport ufoReport);
    
    public Object[] getParams(UfoReport container) {
        return null;
    }
    
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return true;
    }

}
