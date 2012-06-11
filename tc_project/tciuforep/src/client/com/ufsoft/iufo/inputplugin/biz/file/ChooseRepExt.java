/*
 * �������� 2006-4-5
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
 * ѡ�񱨱����򿪱�������ѡ������
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
     * �õ��û���������Ľ������
     */
    public Object[] getParams(UfoReport container) {
        //begin:���û��¼��ؼ��֣�������¼��ؼ���(2006-05-25,from zhuyf ������Ҫ��)
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
        
        //��ʾ���汨��
        AbsIufoBizCmd.doComfirmSave(container);
        
        //�õ���ѡ�񱨱�����
        ChooseRepData[] chooseRepDatas = doGetChooseRepDatas(container);
        if(chooseRepDatas == null || chooseRepDatas.length <=0){
            JOptionPane.showMessageDialog(container,MultiLangInput.getString("uiufotableinput0025"));//���������û�п�ѡ��ı���
            return null;
        }
        //��ѡ�񱨱�Ĵ���
        ChooseRepDlg dlg = new ChooseRepDlg(container,chooseRepDatas);
        dlg.setVisible(true);
        if (dlg.getResult() == UfoDialog.ID_OK) {     
            return new Object[]{dlg.getSeledReportPK()};
        }
        return null;
    }
    /**
     * �õ���ѡ�񱨱�����
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
        String strFileTxt = MultiLang.getString("file");//�ļ�;
        return new String[]{strFileTxt};
    }

    protected String getMenuName() {
        return MultiLangInput.getString("uiufotableinput0001");//�򿪱���;
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
 