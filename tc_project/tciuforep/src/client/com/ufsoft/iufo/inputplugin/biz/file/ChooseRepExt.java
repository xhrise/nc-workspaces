/*
 * 创建日期 2006-4-5
 *
 */
package com.ufsoft.iufo.inputplugin.biz.file;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoUIBizMenuExt;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;
/**
 * 选择报表（“打开报表”进行选择）命令
 * 
 * @author liulp
 * @deprecated
 */
public class ChooseRepExt extends AbsIufoUIBizMenuExt{
    private boolean  m_bCanChooseRep = false;
    /**
     * @param ufoReport
     */
    public ChooseRepExt(UfoReport ufoReport){
        super(ufoReport);
    }
    private boolean isCanChooseRep(){
        return m_bCanChooseRep; 
    }
    protected void setCanChooseRep(boolean bCanChooseRep){
        this.m_bCanChooseRep = bCanChooseRep;
    }
    /**
     * 得到用户界面操作的结果参数
     */
    public Object[] getParams(UfoReport container) {
        //begin:如果没有录入关键字，必须先录入关键字(2006-05-25,from zhuyf 易用性要求)
        UfoReport ufoReport = (UfoReport)container;
        String strAlondID = InputBizOper.doGetTransObj(ufoReport).getRepDataParam().getAloneID();
        if(strAlondID == null){
            ChangeKeywordsExt changeKeywordsExt = new ChangeKeywordsExt(ufoReport);
            Object[] paramsPre = changeKeywordsExt.getParams(ufoReport);
            if(paramsPre !=null){
                changeKeywordsExt.getCommand().execute(paramsPre);
            }
        }
        boolean bHasKeywords = AbsIufoBizCmd.doCheckAloneID(ufoReport);
        if(!bHasKeywords){
            return null;
        }        
        //end
        
        //提示保存报表
        AbsIufoBizCmd.doComfirmSave(container);
        
        //得到可选择报表数据
        ChooseRepData[] chooseRepDatas = doGetChooseRepDatas(container);
        if(chooseRepDatas == null || chooseRepDatas.length <=0){
            JOptionPane.showMessageDialog(container,MultiLangInput.getString("uiufotableinput0025"));//该任务可能没有可选择的报表！
            return null;
        }
        //打开选择报表的窗口
        ChooseRepDlg dlg = new ChooseRepDlg(container,chooseRepDatas);
        dlg.setVisible(true);
        if (dlg.getResult() == UfoDialog.ID_OK) {     
            return new Object[]{dlg.getSeledReportPK()};
        }
        return null;
    }
    /**
     * 得到可选择报表数据
     * @param container
     * @return
     */
    public static ChooseRepData[] doGetChooseRepDatas(UfoReport container){
        ChooseRepData[] chooseRepDatas = null;
        Object returnObj = InputBizOper.doLinkServletTask(ITableInputMenuType.BIZ_TYPE_CHOOSEREP,container,false);      
        returnObj = ChangeKeywordsExt.getBizReturnObj(returnObj);
        if(returnObj != null && returnObj instanceof ChooseRepData[]){
            chooseRepDatas = (ChooseRepData[])returnObj;
        }
        return chooseRepDatas;
    }
    protected String getImageFile() {
        return "reportcore/openfile.gif";
    }

    protected String[] getPaths() {
        String strFileTxt = MultiLang.getString("file");//文件;
        return new String[]{strFileTxt};
    }

    protected String getMenuName() {
        return MultiLangInput.getString("uiufotableinput0001");//打开报表;
    }
    
    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new ChooseRepCmd(ufoReport);
    }
    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return isCanChooseRep();
    }

}
 