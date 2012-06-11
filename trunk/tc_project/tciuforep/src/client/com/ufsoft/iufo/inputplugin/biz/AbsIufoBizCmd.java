/*
 * �������� 2006-4-5
 *
 */
package com.ufsoft.iufo.inputplugin.biz;

import javax.swing.JOptionPane;

import com.ufsoft.iufo.inputplugin.biz.file.SaveRepDataCmd;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
/**
 *  IUFOҵ��˵�������ʵ��UfoCommand����������
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
            //��ʾ
            String strNosReportPKAlert = MultiLangInput.getString("uiufotableinput0021");//"����ѡ�񱨱����ܴ򿪱������ݣ�";
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
            //��ʾ
            String strNoAlondIDAlert = MultiLangInput.getString("uiufotableinput0022");//"����¼��ؼ���ֵ�����ܴ򿪱������ݣ�";
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
     * ִ�б��棬����ʾ״̬����
     * �����л��������ã����ڽ���߳�ִ��˳�����⡣
     * @param ufoReport
     * @param withProcess
     */
    public static boolean doComfirmSave(UfoReport ufoReport, boolean withProcess){
        if(ufoReport == null){
            return false;
        }
        
        //��ʾ�����Ѵ򿪵ı���
        ufoReport.stopCellEditing();
        boolean isSucceedSave = true;
        boolean bDirty = ufoReport.isDirty();
        if(bDirty){
            String strSaveAlert = MultiLangInput.getString("uiufotableinput0023");//"�Ѵ򿪵ı������޸ģ���ȷ���Ƿ񱣴棿";
            int bConfirmReturn = JOptionPane.showConfirmDialog(
                    ufoReport,
                    strSaveAlert,
                    MultiLangInput.getString("uiufotableinput0024"),//"�Ƿ񱣴浱ǰ����",
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
     * ִ��IUFO��ҵ������
     * @param ufoReport
     * @param params 
     */
    protected abstract void executeIUFOBizCmd(UfoReport ufoReport, Object[] params);

}
