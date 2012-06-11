/*
 * �������� 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import java.util.List;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.report.UfoReport;
/**
 * ����Excel���ݵ�UfoCommand
 * 
 * @author liulp
 *
 */
public class ImportExcelDataCmd extends AbsIufoBizCmd{

    protected ImportExcelDataCmd(UfoReport ufoReport) {
        super(ufoReport);
    }

    protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
        if(params == null || params.length <= 0 || 
                params[0] ==null && !(params[0] instanceof List)){
            return;
        }

        //ִ�е��뵽DB�������ǰ�򿪵ı����е��룬����Ҫ��������CellsModel��ˢ����ʾ  
        IInputBizOper inputMenuOper = new InputImportExcelDataOper(ufoReport,params);
        inputMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_IMPORTDATA_EXCEL);
    }

}
