/*
 * �������� 2006-4-17
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
     * @return ���ظ�html����ʾ��Ϣ
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
        //modify by ljhua 2007-3-12 ȥ���򿪱���˵����������嵼������
        //�򿪱���
//        ChooseRepExt2 chooseRepExt = (ChooseRepExt2)extensions[nIndex];
//        chooseRepExt.setCanChooseRep(menuStateData.isCanChooseRep());
//        nIndex++;
        //����
        SaveRepDataExt saveRepDataExt = (SaveRepDataExt)extensions[nIndex];
        saveRepDataExt.setCommeted(menuStateData.isCommited());
        saveRepDataExt.setIsRepOpened(menuStateData.isRepOpened());
        saveRepDataExt.setRepCanModify(menuStateData.isRepCanModify());
        nIndex++;  
        //�л��ؼ���
        ChangeKeywordsExt changeKeywordsExt = (ChangeKeywordsExt)extensions[nIndex];
        changeKeywordsExt.setCanChangeKeywords(menuStateData.isCanChangeKeywords());
        nIndex++;     
        //��������Դ
        DSInfoSetExt dsInfoSetExt = (DSInfoSetExt)extensions[nIndex];
        dsInfoSetExt.setCanSetDSInfo(menuStateData.isCanSetDSInfo());
        nIndex++;     
        
        //��ӡ����ز˵� 
        
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
        //�������
        AreaCalExt areaCalExt = (AreaCalExt)extensions[nIndex];
        areaCalExt.setIsRepOpened(menuStateData.isRepOpened());
        areaCalExt.setCanAreaCal(menuStateData.isCanAreaCal());
        nIndex++;
        //����
        CalExt calExt = (CalExt)extensions[nIndex];
        calExt.setIsRepOpened(menuStateData.isRepOpened());
        calExt.setCanCal(menuStateData.isCanCal());
        nIndex++;
        
        TraceDataExt traceExt=(TraceDataExt)extensions[nIndex];
        traceExt.setIsRepOpened(menuStateData.isRepOpened());
        traceExt.setCanTrace(menuStateData.isCanTraceData());
        traceExt.setTotal(menuStateData.getDataVer()==350);
        nIndex++;
        
        //���
        CheckRepExt checkRepExt = (CheckRepExt)extensions[nIndex];
        checkRepExt.setIsHasRepCheckFormula(menuStateData.isHasRepCheckFormula());
        checkRepExt.setIsRepOpened(menuStateData.isRepOpened());
        nIndex++;       
        //ȫ�����(������)
        CheckAllRepExt checkAllRepExt = (CheckAllRepExt)extensions[nIndex];
        checkAllRepExt.setIsHasTaskCheckFormula(menuStateData.isHasTaskCheckFormula());
        nIndex++;     
        
//        //�����¼�(?)
//        nIndex++;             
//        //�鿴�¼�����(?)
//        nIndex++;             
//        //�鿴��Դ(?)   
//        nIndex++;     

        //����->Iufo����
        ImportIufoDataExt importIufoDataExt = (ImportIufoDataExt)extensions[nIndex];
        importIufoDataExt.setIsRepOpened(menuStateData.isRepOpened());
        importIufoDataExt.setRepCanModify(menuStateData.isRepCanModify());        
        nIndex++;     
        //����->Excel����
        ImportExcelDataExt importExcelDataExt = (ImportExcelDataExt)extensions[nIndex];
        importExcelDataExt.setIsRepOpened(menuStateData.isRepOpened());
        importExcelDataExt.setHasRepCanModify(menuStateData.isHasRepCanModify());
        nIndex++;     
//        //����->Ufo����
//        ImportUfoDataExt importUfoDataExt = (ImportUfoDataExt)extensions[nIndex]; 
//        importUfoDataExt.setIsRepOpened(menuStateData.isRepOpened());
//        importUfoDataExt.setIsCanImportUfoData(menuStateData.isCanImportU8UFOData());
//        importUfoDataExt.setHasRepCanModify(menuStateData.isHasRepCanModify());
//        nIndex++;     
        //����->Excel��ʽ
        ExportData2ExcelExt exportData2ExcelExt = (ExportData2ExcelExt)extensions[nIndex];
        exportData2ExcelExt.setIsRepOpened(menuStateData.isRepOpened());
        nIndex++;     
        //����->html��ʽ
        ExportData2HtmlExt exportData2HtmlExt = (ExportData2HtmlExt)extensions[nIndex];
        exportData2HtmlExt.setIsRepOpened(menuStateData.isRepOpened());
        nIndex++;     
        
    }
 }
