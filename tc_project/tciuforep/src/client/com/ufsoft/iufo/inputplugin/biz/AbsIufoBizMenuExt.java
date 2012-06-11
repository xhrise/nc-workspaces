/*
 * �������� 2006-4-5
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
 * IUFOҵ��˵�������ʵ��IExtension����������
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
//     * �õ�ִ��������Ҫ�Ĳ���
//     * NOTICE: ����������ù��캯��������UfoReport���ԡ����ù��캯��ʱ��IPlgiIn.getReport()������ò���ֵ
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
//        throw new IllegalArgumentException("��ǰ���л�����û��UfoReport����");
//    }    
    public ActionUIDes[] getUIDesArr() {
        ActionUIDes uiDes = new ActionUIDes();
        uiDes.setName(getMenuName());
        uiDes.setPaths(getPaths());
        setGroup(uiDes);
        return new ActionUIDes[]{uiDes};
    }
    /**
     * ���ò˵�����
     * @param uiDes
     */
    private void setGroup(ActionUIDes uiDes){
    	String toolBarGroup = getGroup();
    	if(toolBarGroup != null){
    		uiDes.setGroup(toolBarGroup);
    	}
    }
    /**
     * �õ����˵�·��
     * @return
     */
    protected abstract String[] getPaths();
    /**
     * �õ���ǰ�˵�����
     * @return
     */
    protected abstract String getGroup();
    /**
     * �õ������ݡ��˵�·��
     * @return
     */
    protected static String[] doGetDataMenuPaths(){
        return new String[]{MultiLang.getString("data")};//"����"
    }
    
    /**
     * �õ������ݣ�>���롱�˵�·��
     * @return
     */
    protected static String[] doGetImportMenuPaths(){
        return new String[]{MultiLang.getString("data"),MultiLang.getString("import")};//"����","����"
    }
    
    /**
     * �õ������ݣ�>�������˵�·��
     * @return
     */
    protected static String[] doGetExportMenuPaths(){
        return new String[]{MultiLang.getString("data"),MultiLang.getString("export")};//"����","����"
    }
    /**
     * �õ��˵���ʾ����
     * @return
     */
    protected abstract String getMenuName();

    public UfoCommand getCommand() {
        return doGetCommand(getUfoReport());
    }
    
    /**
     * �õ������UfoCommandʵ��
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
