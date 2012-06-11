/*
 * 创建日期 2006-4-17
 *
 */
package com.ufsoft.iufo.inputplugin.biz.file;

import com.ufsoft.iufo.inputplugin.biz.DSInfoSetExt;
import com.ufsoft.iufo.inputplugin.biz.FormulaTracePlugIn;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputDataPlugIn;
import com.ufsoft.iufo.inputplugin.biz.InputFilePlugIn;
import com.ufsoft.iufo.inputplugin.biz.data.AreaCalExt;
import com.ufsoft.iufo.inputplugin.biz.data.CalExt;
import com.ufsoft.iufo.inputplugin.biz.data.CheckAllRepExt;
import com.ufsoft.iufo.inputplugin.biz.data.CheckRepExt;
import com.ufsoft.iufo.inputplugin.biz.data.ExportData2ExcelExt;
import com.ufsoft.iufo.inputplugin.biz.data.ExportData2HtmlExt;
import com.ufsoft.iufo.inputplugin.biz.data.ImportExcelDataExt;
import com.ufsoft.iufo.inputplugin.biz.data.ImportIufoDataExt;
import com.ufsoft.iufo.inputplugin.biz.data.TraceDataExt;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceExt;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.table.CellsModel;

public class InputOpenRepOper extends InputBizOper{
    public InputOpenRepOper(UfoReport ufoReport) {
        super(ufoReport);
    }
    /**
     * 
     * @param transReturnObj
     * @param nMenuType
     * @return 返回给html的提示信息
     */
    protected String dealTransReturnObj(Object transReturnObj,int nMenuType) {
        Object[] results = null;
        if(transReturnObj instanceof Object[]){
            results = (Object[])transReturnObj;
        }else{
            return null;
        }
        
        Object firstTransReturnObj = null;
        if(results.length ==3){            
            firstTransReturnObj = new Object[]{results[0],results[1]};
            Object[] otherRetResults = (Object[])results[2];
            if(otherRetResults != null && otherRetResults.length > 0){
                MenuStateData menuStateData = (MenuStateData)otherRetResults[0];
                
                InputFilePlugIn inputFilePlugIn = getInputFilePlugIn();
                doSetFileMenuState(inputFilePlugIn,menuStateData);
                
                InputDataPlugIn inputDataPlugIn = getInputDataPlugIn();
                doSetDataMenuState(inputDataPlugIn,menuStateData);
                FormulaTracePlugIn formulaTracePlugIn = getFormulaTracePlugIn();              
                doSetDataMenuState(formulaTracePlugIn,menuStateData);
            }
        }else{
            firstTransReturnObj = transReturnObj;
        }    
        
//        if(firstTransReturnObj != null && results.length >=2 && results[1] instanceof CellsModel){
        if(results.length >=2 && results[1] instanceof CellsModel){
            return super.dealTransReturnObj(firstTransReturnObj,nMenuType);
        }else{
            return null;
        }
    }
    protected void doSetDataMenuState(FormulaTracePlugIn formulaTracePlugIn,MenuStateData menuStateData){
    	if(formulaTracePlugIn == null || menuStateData == null ){
            return;
        }
        IExtension[] extensions = formulaTracePlugIn.getDescriptor().getExtensions();
        int nIndex = 0;
        FormulaTraceExt formulaTraceExt = (FormulaTraceExt)extensions[nIndex];
        formulaTraceExt.setIsRepOpened(menuStateData.isRepOpened());
    }

    /**
     * @return
     */
    private InputFilePlugIn getInputFilePlugIn() {
        return (InputFilePlugIn)getUfoReport().getPluginManager().getPlugin(InputFilePlugIn.class.getName());
    }
    /**
     * @param menuStateData
     */
    protected static void doSetFileMenuState(InputFilePlugIn inputFilePlugIn,MenuStateData menuStateData) {
        if(inputFilePlugIn == null || menuStateData == null ){
            return;
        }
        IExtension[] extensions = inputFilePlugIn.getDescriptor().getExtensions();
        int nIndex = 0;
        //modify by ljhua 2007-3-12 去除打开报表菜单，用左侧面板导航代替
        //打开报表
//        ChooseRepExt2 chooseRepExt = (ChooseRepExt2)extensions[nIndex];
//        chooseRepExt.setCanChooseRep(menuStateData.isCanChooseRep());
//        nIndex++;
        //保存
        SaveRepDataExt saveRepDataExt = (SaveRepDataExt)extensions[nIndex];
        saveRepDataExt.setCommeted(menuStateData.isCommited());
        saveRepDataExt.setIsRepOpened(menuStateData.isRepOpened());
        saveRepDataExt.setRepCanModify(menuStateData.isRepCanModify());
        nIndex++;  
        //切换关键字
        ChangeKeywordsExt changeKeywordsExt = (ChangeKeywordsExt)extensions[nIndex];
        changeKeywordsExt.setCanChangeKeywords(menuStateData.isCanChangeKeywords());
        nIndex++;     
        //设置数据源
        DSInfoSetExt dsInfoSetExt = (DSInfoSetExt)extensions[nIndex];
        dsInfoSetExt.setCanSetDSInfo(menuStateData.isCanSetDSInfo());
        nIndex++;     
        
        //打印的相关菜单 
        
    }
    /**
     * @return
     */
    private FormulaTracePlugIn getFormulaTracePlugIn() {
        return (FormulaTracePlugIn)getUfoReport().getPluginManager().getPlugin(FormulaTracePlugIn.class.getName());
    }
    /**
     * @return
     */
    private InputDataPlugIn getInputDataPlugIn() {
        return (InputDataPlugIn)getUfoReport().getPluginManager().getPlugin(InputDataPlugIn.class.getName());
    }
    
    /**
     * @param menuStateData
     */
    protected static void doSetDataMenuState(InputDataPlugIn inputDataPlugIn,MenuStateData menuStateData) {
        if(inputDataPlugIn == null || menuStateData == null){
            return;
        }
        
        IExtension[] extensions = inputDataPlugIn.getDescriptor().getExtensions();
        int nIndex = 0;
        //区域计算
        AreaCalExt areaCalExt = (AreaCalExt)extensions[nIndex];
        areaCalExt.setIsRepOpened(menuStateData.isRepOpened());
        areaCalExt.setCanAreaCal(menuStateData.isCanAreaCal());
        nIndex++;
        //计算
        CalExt calExt = (CalExt)extensions[nIndex];
        calExt.setIsRepOpened(menuStateData.isRepOpened());
        calExt.setCanCal(menuStateData.isCanCal());
        nIndex++;
        
        TraceDataExt traceExt=(TraceDataExt)extensions[nIndex];
        traceExt.setIsRepOpened(menuStateData.isRepOpened());
        traceExt.setCanTrace(menuStateData.isCanTraceData());
        traceExt.setTotal(menuStateData.getDataVer()==350);
        nIndex++;
        
        //审核
        CheckRepExt checkRepExt = (CheckRepExt)extensions[nIndex];
        checkRepExt.setIsHasRepCheckFormula(menuStateData.isHasRepCheckFormula());
        checkRepExt.setIsRepOpened(menuStateData.isRepOpened());
        nIndex++;       
        //全部审核(表间审核)
        CheckAllRepExt checkAllRepExt = (CheckAllRepExt)extensions[nIndex];
        checkAllRepExt.setIsHasTaskCheckFormula(menuStateData.isHasTaskCheckFormula());
        nIndex++;     
        
//        //汇总下级(?)
//        nIndex++;             
//        //查看下级数据(?)
//        nIndex++;             
//        //查看来源(?)   
//        nIndex++;     

        //导入->Iufo数据
        ImportIufoDataExt importIufoDataExt = (ImportIufoDataExt)extensions[nIndex];
        importIufoDataExt.setIsRepOpened(menuStateData.isRepOpened());
        importIufoDataExt.setRepCanModify(menuStateData.isRepCanModify());        
        nIndex++;     
        //导入->Excel数据
        ImportExcelDataExt importExcelDataExt = (ImportExcelDataExt)extensions[nIndex];
        importExcelDataExt.setIsRepOpened(menuStateData.isRepOpened());
        importExcelDataExt.setHasRepCanModify(menuStateData.isHasRepCanModify());
        nIndex++;     
//        //导入->Ufo数据
//        ImportUfoDataExt importUfoDataExt = (ImportUfoDataExt)extensions[nIndex]; 
//        importUfoDataExt.setIsRepOpened(menuStateData.isRepOpened());
//        importUfoDataExt.setIsCanImportUfoData(menuStateData.isCanImportU8UFOData());
//        importUfoDataExt.setHasRepCanModify(menuStateData.isHasRepCanModify());
//        nIndex++;     
        //导出->Excel格式
        ExportData2ExcelExt exportData2ExcelExt = (ExportData2ExcelExt)extensions[nIndex];
        exportData2ExcelExt.setIsRepOpened(menuStateData.isRepOpened());
        nIndex++;     
        //导出->html格式
        ExportData2HtmlExt exportData2HtmlExt = (ExportData2HtmlExt)extensions[nIndex];
        exportData2HtmlExt.setIsRepOpened(menuStateData.isRepOpened());
        nIndex++;     
        
    }
 }
