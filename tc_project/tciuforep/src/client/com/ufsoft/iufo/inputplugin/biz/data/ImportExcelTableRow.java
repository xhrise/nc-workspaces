/*
 * 创建日期 2006-9-18
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;

import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepData;
/**
 * Excel导入的表行数据对象
 * @author liulp
 *
 */
public class ImportExcelTableRow {
    private boolean m_bSelected = false;
    private ChooseRepData m_oChooseRepData = null;
    private String m_strSheetName = null;
    private int m_nDynAreaEndRow = -1;
    
    public ImportExcelTableRow(ChooseRepData chooseRepData,String strSheetName) {
        super();
        this.m_oChooseRepData = chooseRepData;
        this.m_strSheetName = strSheetName;
    }
    protected boolean isSelected(){
        return m_bSelected;
    }

    protected void setIsSelected(boolean bSelected){
        m_bSelected = bSelected;
    }
    protected String getRepCode(){
        return m_oChooseRepData.getReportCode();
    }
    protected ChooseRepData getChooseRepData(){
        return m_oChooseRepData;
    }
    protected void setChooseRepData(ChooseRepData chooseRepData){
        this.m_oChooseRepData = chooseRepData;
    }
    protected String getSheetName(){
        return m_strSheetName;
    }
    protected int getDynAreaEndRow(){
        return m_nDynAreaEndRow;
    }
    protected void setDynAreaEndRow(int nDynAreaEndRow){
        this.m_nDynAreaEndRow = nDynAreaEndRow;
    }
    public String toString(){
        return m_oChooseRepData.toString();
    }

}
