/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.file;

import java.awt.Component;
import java.awt.Container;

import com.ufida.zior.console.ActionHandler;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoUIBizMenuExt;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iuforeport.repdatainput.LoginEnvVO;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.MultiLang;

/**
 * 切换关键字的IActionExt实现类
 * 
 * @author liulp
 *
 */
public class ChangeKeywordsExt extends AbsIufoUIBizMenuExt{
    private boolean m_bCanChangeKeywords = false;
    /**
     * @param ufoReport
     */
    public ChangeKeywordsExt(UfoReport ufoReport){
        super(ufoReport);
    }
    private boolean isCanChangeKeywords(){
        return m_bCanChangeKeywords; 
    }
    protected void setCanChangeKeywords(boolean bCanChangeKeywords){
        this.m_bCanChangeKeywords = bCanChangeKeywords;
    }
   
    protected String getImageFile(){
        return "reportcore/toright.gif";
    }
    protected String[] getPaths() {
        return new String[]{MultiLang.getString("file")};//"文件"
    }

    protected String getMenuName() {
        return MultiLangInput.getString("uiufotableinput0002");//"切换关键字";
    }

    protected UfoCommand doGetCommand(UfoReport ufoReport) {
        return new ChangeKeywordsCmd(ufoReport);
    }
   
    public Object[] getParams(UfoReport container) {
        return doGetInputKeyValus(container,container);
    }
    
    public static Object[] doGetInputKeyValus(UfoReport container,UfoReport ufoReport){
        //提示保存报表
    	if(!AbsIufoBizCmd.doComfirmSave(ufoReport, false)){
    		return null;
    	}
    	
    	//得到切换关键字数据对象
        ChangeKeywordsData[] changeKeywordsDatas = null;
        Object returnObj = InputBizOper.doLinkServletTask(ITableInputMenuType.BIZ_TYPE_CHANGEKEYWORDDATA,ufoReport,false);
        returnObj = getBizReturnObj(returnObj);
        if(returnObj != null && returnObj instanceof ChangeKeywordsData[]){
            changeKeywordsDatas = (ChangeKeywordsData[])returnObj;
        }
        //打开选择报表的窗口
        ChangeKeywordsDlg dlg = new ChangeKeywordsDlg(container,changeKeywordsDatas);
        dlg.setVisible(true);
        if (dlg.getResult() == UfoDialog.ID_OK) {     
            return dlg.getInputKeyValues();
        }
        return null;
    }
    
    public static String[] doGetInputKeyValus_New(IRepDataParam param,LoginEnvVO loginEnv,Container container){    	
    	//得到切换关键字数据对象
        ChangeKeywordsData[] changeKeywordsDatas =(ChangeKeywordsData[])ActionHandler.exec("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "getChangeKeywordsData",
				new Object[]{param,loginEnv});
        //打开选择报表的窗口
        ChangeKeywordsDlg dlg = new ChangeKeywordsDlg(container,changeKeywordsDatas);
        dlg.setVisible(true);
        if (dlg.getResult() == UfoDialog.ID_OK) {     
            return dlg.getInputKeyValues();
        }
        return null;
    }

    protected static Object getBizReturnObj(Object returnObj) {
        if(returnObj != null && returnObj instanceof Object[] && ((Object[])returnObj).length >=2){
            return ((Object[])returnObj)[1];
        }            
        return null;
    }

    /*
     * @see com.ufsoft.report.plugin.ICommandExt#isEnabled(java.awt.Component)
     */
    public boolean isEnabled(Component focusComp) {
        return isCanChangeKeywords();
    }
	@Override
	protected String getGroup() {
		return "file";
	}

}
