/*
 * 创建日期 2006-9-9
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.biz.file.InputChangeKeywordsOper;
import com.ufsoft.report.UfoReport;

public class InputImportExcelDataOper extends InputBizOper{
    private Object[] m_oOtherParams = null;

    public InputImportExcelDataOper(UfoReport ufoReport,Object[] otherParams) {
        super(ufoReport);
        m_oOtherParams = otherParams;
    }
    private Object[] getOtherParams(){
        return m_oOtherParams;
    }
    /**
     * 
     * @param transReturnObj
     * @param nMenuType
     * @return 返回给html的提示信息
     */
    protected String dealTransReturnObj(Object transReturnObj,int nMenuType) {
        return super.dealTransReturnObj(transReturnObj,nMenuType);
        
    }
    /**
     * 执行连接Servlet的操作
     * @param nMenuType
     * @return
     */
    protected  Object doLinkServletTask(int nMenuType){
        return InputChangeKeywordsOper.linkServletTask(nMenuType,getTransObj(),getOtherParams());
    }
}
