/*
 * �������� 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.file.InputChangeKeywordsOper;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.UfoPublic;
/**
 * ����Iufo���ݵ�UfoCommand
 * 
 * @author liulp
 *
 */
public class ImportIufoDataCmd extends AbsIufoBizCmd{

    protected ImportIufoDataCmd(UfoReport ufoReport) {
        super(ufoReport);
    }

    protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
        //����Ĳ�����¼��ĸ����ؼ��ֵ�ֵ
        if(params == null){
            return;
        }
        String[] strKeyValues = (String[])params;
        IInputBizOper inputMenuOper = new InputChangeKeywordsOper(ufoReport,strKeyValues);
        Object objReturn = inputMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_IMPORTDATA_IUFO);
        if(objReturn != null && objReturn.toString().length() > 0 && !objReturn.toString().equals("true")){
        	UfoPublic.sendWarningMessage(objReturn.toString(), ufoReport);
        }
    }

}
