/*
 * 创建日期 2006-4-5
 *
 */
package com.ufsoft.iufo.inputplugin.biz;

import javax.swing.JOptionPane;

import com.ufsoft.iufo.inputplugin.biz.file.SaveRepDataCmd;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
/**
 *  IUFO业务菜单操作的实现UfoCommand基础抽象类
 * 
 * @author liulp
 *
 */
public abstract class AbsIufoBizCmd extends UfoCommand{
    private UfoReport m_oUfoReport = null;
    protected AbsIufoBizCmd(UfoReport ufoReport){
        m_oUfoReport = ufoReport;
    }
    
    protected UfoReport getUfoReport(){
        return m_oUfoReport;
    }
    
    public void execute(Object[] params) {
        if(isNeedCheckParams()){
            if(!isValidParams(params)){
                return;
            }
        }
        if(isNeedCheckAloneID()){
            if(!doCheckAloneID(getUfoReport())){
                return;
            }
        }
        if(isNeedCheckReportPK()){
            if(!doCheckReportPK(getUfoReport())){
                return;
            }
        }
        if(isNeedComfirmSave()){
            doComfirmSave(getUfoReport());
        }
        executeIUFOBizCmd(getUfoReport(),params);
    }
    
    protected boolean isValidParams(Object[] params) {
        return true;
    }

    protected boolean isNeedCheckParams() {
        return false;
    }

    protected  boolean isNeedCheckAloneID(){
        return true;        
    }
    protected boolean isNeedCheckReportPK(){
        return true;
    }
    
    protected  boolean isNeedComfirmSave(){
        return false;        
    }
    
    public static  boolean doCheckReportPK(UfoReport ufoReport){
        if(ufoReport == null){
            return false;
        }
        String strReportPK = InputBizOper.doGetTransObj(ufoReport).getRepDataParam().getAloneID();
        if(strReportPK == null){
            //提示
            String strNosReportPKAlert = MultiLangInput.getString("uiufotableinput0021");//"请先选择报表，才能打开报表数据！";
            JOptionPane.showMessageDialog(
                    ufoReport,
                    strNosReportPKAlert);
            return false;
        }
        return true;
    }
    public static  boolean doCheckAloneID(UfoReport ufoReport){
        if(ufoReport == null){
            return false;
        }
        String strAlondID = InputBizOper.doGetTransObj(ufoReport).getRepDataParam().getAloneID();
        if(strAlondID == null){
            //提示
            String strNoAlondIDAlert = MultiLangInput.getString("uiufotableinput0022");//"请先录入关键字值，才能打开报表数据！";
            JOptionPane.showMessageDialog(
                    ufoReport,
                    strNoAlondIDAlert);
            return false;
        }
        return true;
    }
    
    public static boolean doComfirmSave(UfoReport ufoReport){
    	return doComfirmSave(ufoReport, true);
    }
    /**
     * 执行保存，不显示状态栏，
     * 对于切换报表适用，用于解决线程执行顺序问题。
     * @param ufoReport
     * @param withProcess
     */
    public static boolean doComfirmSave(UfoReport ufoReport, boolean withProcess){
        if(ufoReport == null){
            return false;
        }
        
        //提示保存已打开的报表
        ufoReport.stopCellEditing();
        boolean isSucceedSave = true;
        boolean bDirty = ufoReport.isDirty();
        if(bDirty){
            String strSaveAlert = MultiLangInput.getString("uiufotableinput0023");//"已打开的报表已修改，请确认是否保存？";
            int bConfirmReturn = JOptionPane.showConfirmDialog(
                    ufoReport,
                    strSaveAlert,
                    MultiLangInput.getString("uiufotableinput0024"),//"是否保存当前报表",
                    JOptionPane.OK_CANCEL_OPTION);
            if(bConfirmReturn == JOptionPane.OK_OPTION){
                if(withProcess){
                	SaveRepDataCmd saveRepCmd = new SaveRepDataCmd(ufoReport);
                	saveRepCmd.execute(null);
                	isSucceedSave = saveRepCmd.isSucceedSave();
                } else{
                	isSucceedSave = SaveRepDataCmd.save(ufoReport);
                }
            } 
        }
        
        return isSucceedSave;
    }
    /**
     * 执行IUFO的业务命令
     * @param ufoReport
     * @param params 
     */
    protected abstract void executeIUFOBizCmd(UfoReport ufoReport, Object[] params);

}
