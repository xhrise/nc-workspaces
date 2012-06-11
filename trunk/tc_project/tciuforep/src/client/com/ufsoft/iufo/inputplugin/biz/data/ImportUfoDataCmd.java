/*
 * 创建日期 2006-4-6
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.report.UfoReport;
/**
 * 导入Ufo数据的UfoCommand
 * 
 * @author liulp
 *
 */
public class ImportUfoDataCmd extends AbsIufoBizCmd{

    protected ImportUfoDataCmd(UfoReport ufoReport) {
        super(ufoReport);
    }

    protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
        
    }

}
