/*
 * �������� 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.report.UfoReport;

/**
 * ȫ�����(�����ڵ����б���)��UfoCommand
 * 
 * @author liulp
 *
 */
public class CheckAllRepCmd extends AbsIufoBizCmd{

    protected CheckAllRepCmd(UfoReport ufoReport) {
        super(ufoReport);
    }

    protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
        IInputBizOper inputMenuOper = new InputBizOper(ufoReport);
        inputMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_CHECK_BETWEEN);  
        
    }

}
