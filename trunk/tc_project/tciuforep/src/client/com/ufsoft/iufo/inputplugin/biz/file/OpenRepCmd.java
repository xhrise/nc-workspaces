/*
 * 创建日期 2006-4-5
 *
 */
package com.ufsoft.iufo.inputplugin.biz.file;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.UfoPublic;

/**
 * 打开报表命令
 * 
 * @author liulp
 *
 */
public class OpenRepCmd extends AbsIufoBizCmd{

    public OpenRepCmd(UfoReport ufoReport) {
        super(ufoReport);
    }

    protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
        int nBizType = getInitBizType(ufoReport);
        IInputBizOper inputMenuOper = new InputOpenRepOper(ufoReport);
        String hintInfoStr = (String)inputMenuOper.performBizTask(nBizType);     
        if(hintInfoStr != null && !hintInfoStr.equals("true")){
        	UfoPublic.sendErrorMessage(hintInfoStr, ufoReport, null);
        }       
    }
    /**
     * 得到初始打开报表的菜单类型
     * @return
     */
    private int getInitBizType(UfoReport ufoReport) {
        TableInputTransObj transObj = InputBizOper.doGetTransObj(ufoReport);
        int nInitMenuType = ITableInputMenuType.MENU_TYPE_OPEN;
        String strImportAloneID = null;
        if(transObj.getRepDataParam()!=null && transObj.getType() == TableInputTransObj.TYPE_REPDATA){
            strImportAloneID = transObj.getRepDataParam().getImportAloneID();
        }
        if(strImportAloneID!=null && strImportAloneID.length()>0){
            nInitMenuType = ITableInputMenuType.MENU_TYPE_IMPORTDATA_IUFO;
        }
        return nInitMenuType;        
    }
    
    protected  boolean isNeedCheckAloneID(){
        return false;        
    }
    protected boolean isNeedCheckReportPK(){
        return false;
    }
    public void setNeedCheckAloneID(boolean isNeedCheckAloneID) {
		this.isNeedCheckAloneID = isNeedCheckAloneID;
	}
	public void setNeedCheckReportPK(boolean isNeedCheckReportPK) {
		this.isNeedCheckReportPK = isNeedCheckReportPK;
	}
	
	private boolean isNeedCheckAloneID = true;
    private boolean isNeedCheckReportPK = true;
}
