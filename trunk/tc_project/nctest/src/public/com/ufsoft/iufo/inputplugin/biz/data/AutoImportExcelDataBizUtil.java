/*
 * 创建日期 2006-9-8
 *
 */
package com.ufsoft.iufo.inputplugin.biz.data;
import com.ufida.iufo.pub.tools.AppDebug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import nc.pub.iufo.cache.ReportCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.dataexchange.AutoMultiSheetImportUtil;
import nc.ui.iufo.input.CSomeParam;
import nc.ui.iufo.input.InputActionUtil;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.user.UserInfoVO;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.ufsoft.iufo.inputplugin.biz.file.ChooseRepData;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.tableinput.applet.TableInputException;
import com.ufsoft.report.sysplugin.excel.ExcelImpUtil;
import com.ufsoft.table.CellsModel;

public class AutoImportExcelDataBizUtil {
    private static final String NONE_MATCH = "none_match";

    public AutoImportExcelDataBizUtil() {
        super();
    }
    /**
     * 按照先名称后编码的顺序自动进行匹配，如果当前报表已经打开，而且导入的Excel中也只有一张报表，则直接生成匹配对儿返回
     * @param chooseRepDatas
     * @param workBook
     * @param strCurRepPK
     * @return
     * @throws TableInputException 
     */
    public static Hashtable<String, Object> doGetAutoMatchMap(ChooseRepData[] chooseRepDatas, HSSFWorkbook workBook, String strCurRepPK) throws TableInputException {
        Hashtable<String, Object> matchMap = new Hashtable<String, Object>();
    	if(chooseRepDatas == null || chooseRepDatas.length == 0){
    		return matchMap;
    	}
    	
    	//对每一个工作表进行匹配，然后进行记录，
        //匹配成功的话按照（工作表名称：报表编码）为一组；
        //匹配不成功，则需要将对该工作表设置默认值，表示没有匹配
        Map<String, String> repNMCMap = new HashMap<String, String>();
        Map<String, String> repCMNMap = new HashMap<String, String>();
        if(workBook != null){
            int sheetNum = workBook.getNumberOfSheets();
            String sheetName = null;
            String repCode = null;
            String repName = null;
            if(sheetNum == 1 && strCurRepPK!=null){
            	//现在不这么要求匹配当前打开报表
//                if(strCurRepPK == null){
//                    throw new TableInputException("miufo1002752");//当前系统中没有打开Excel数据要导入的目标报表  //"当前系统中没有打开Excel数据要导入的目标报表"+"!"
//                }
                sheetName = workBook.getSheetName(0);
                ChooseRepData chooseRepData = getCurRepData(chooseRepDatas,strCurRepPK);
                if(chooseRepData != null ){
                    repName = chooseRepData.getReportName();
                    repCode = chooseRepData.getReportCode();
                    //将匹配的值记录下来，并保证一张IUFO报表只能对应一张sheet
                    matchMap.put(sheetName, new String[]{repName, repCode});
                }
            }
            else{
                //构造报表名称报表编码对照表
                int nRepCount = chooseRepDatas.length;
                for(int j = 0; j < nRepCount; j++){
                    repNMCMap.put(chooseRepDatas[j].getReportName(), chooseRepDatas[j].getReportCode());
                    repCMNMap.put(chooseRepDatas[j].getReportCode(), chooseRepDatas[j].getReportName());
                }

                for(int i = 0; i < sheetNum; i++){
                    sheetName = workBook.getSheetName(i);
                    if(repNMCMap.containsKey(sheetName)){
                        //首先按照报表名称进行匹配
                        repCode = (String)repNMCMap.get(sheetName);
                        repName = sheetName;
                        repNMCMap.remove(repName);
                        repCMNMap.remove(repCode);
                    } else if(repCMNMap.containsKey(sheetName)){
                        //按照报表编码进行匹配
                        repCode = sheetName;
                        repName = (String)repCMNMap.get(sheetName);
                        repNMCMap.remove(repName);
                        repCMNMap.remove(repCode);
                    } else{
                        //没有匹配成功
                        matchMap.put(sheetName, NONE_MATCH);
                    }
                    //将匹配的值记录下来，并保证一张IUFO报表只能对应一张sheet
                    matchMap.put(sheetName, new String[]{repName, repCode});
                }
            }
        }
        return matchMap;
    }
    /**
     * 从当前可导入的报表信息集合中找到指定报表信息
     * @param chooseRepDatas
     * @param strCurRepPK
     * @return
     */
    public static ChooseRepData getCurRepData(ChooseRepData[] chooseRepDatas, String strCurRepPK) {
    	if(chooseRepDatas == null || strCurRepPK == null){
    		return null;
    	}
        int nTotalRepCount = chooseRepDatas.length;
        for(int i =0;i < nTotalRepCount;i++){
            if(chooseRepDatas[i]!=null && chooseRepDatas[i].getReportPK().equals(strCurRepPK)){
                return chooseRepDatas[i];
            }
        }
        return null;
    }
    /**
     * 从当前可导入的报表信息集合中找到指定报表信息
     * @param chooseRepDatas
     * @param strCurRepCode
     * @return
     */
    public static ChooseRepData getCurRepDataByCode(ChooseRepData[] chooseRepDatas, String strCurRepCode) {
    	if(chooseRepDatas == null || strCurRepCode == null){
    		return null;
    	}
    	int nTotalRepCount = chooseRepDatas.length;
        for(int i =0;i < nTotalRepCount;i++){
            if(chooseRepDatas[i]!=null && chooseRepDatas[i].getReportCode().equals(strCurRepCode)){
                return chooseRepDatas[i];
            }
        }
        return null;
    }
    /**
     * 得到导入Excel准备数据信息，具有可传输性
     *   包括通过配置信息，将sheet转换为CellsModel
     * @param array
     * @param workBook
     * @return List,其中每个Elemet是Object[]{sheetname,repcode,dynendrow,cellsModel}
     */
    public static List<Object[]> getImportInfos(List array, HSSFWorkbook workBook) {
        if(workBook == null || array == null || array.size() <=0 ){
            return null;
        }

        int nSheetSize = array.size();
        List<Object[]> listImportInfos = new ArrayList<Object[]>();
        String[] selVals = null;            
        for(int i = 0 ; i <  nSheetSize; i++){
            Object[] objImportInfos = new Object[4];//{sheetname,repcode,dynendrow,cellsModel}
            selVals = (String[])array.get(i);//{sheetname,repcode,dynendrow}
            System.arraycopy(selVals,0,objImportInfos,0,3);  // 将selVals数组中的数据复制到objImportInfos中 0 = 起始索引  3 = 复制长度
            
            String strSheetName = selVals[0];      //　会的导入的EXCEL的名称          
                //得到sheet
            int sheetIndex = workBook.getSheetIndex(strSheetName); // 获取当前导入EXCEL的页签数，若无则小于0
            if (sheetIndex<0){
                continue;
            }                
            CellsModel cellsModel=ExcelImpUtil.getCellsModelByExcel(workBook.getSheetAt(sheetIndex),workBook); //　将sheet信息转换成cellsModel数据
            if (cellsModel==null){
                continue;
            }else{
                objImportInfos[3] = cellsModel; //　添加到新的数组中
                listImportInfos.add(objImportInfos);  // 添加到LIST中
            }
        }
        return listImportInfos;
    }
    /**
     * 读取Excel中的sheet信息
     * @param filepath
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static HSSFWorkbook getImportWorkBook(String filepath) throws IOException, FileNotFoundException {
        if(filepath == null){
            return null;
        }
        
        File file = null;
        try {
        	System.out.println(filepath);
        	file = new File(filepath);
        	System.out.println(file.getAbsolutePath());
        } catch (Exception e) {
        	e.printStackTrace();
        	AppDebug.debug(e);
        	System.out.println(e.getMessage());
        }
        
        POIFSFileSystem fs =new POIFSFileSystem(new FileInputStream(file));
        HSSFWorkbook workBook= new HSSFWorkbook(fs);
        return workBook;
    }
    /**
     * 读取Excel中的sheet信息
     * @param filepath
     * @return
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static HSSFWorkbook getImportWorkBook(File file) throws IOException, FileNotFoundException {
        if(file == null){
            return null;
        }
        
        POIFSFileSystem fs =new POIFSFileSystem(new FileInputStream(file));
        HSSFWorkbook workBook= new HSSFWorkbook(fs);
        return workBook;
    }
    /**
     * 获得动态区匹配表格的Hearder信息数组
     * @return
     */
    public static String[] getTableColumns() {
        String[] columns = new String[]{
                StringResource.getStringResource("miufo1002749"),  //"IUFO报表"
                StringResource.getStringResource("miufo1002750"),  //"Excel工作表"
                StringResource.getStringResource("miufo1002751")};  //"动态区结束行位置"
        return columns;
    }
    /**
     * 将导入Excel准备数据信息(CellsModel),导入到数据库
     * @param importUtil
     * @param listImportInfos List,其中每个Elemet是Object[]{sheetname,repcode,dynendrow,cellsModel}
     * @param isNeedSave
     */
    public static void processImportData(AutoMultiSheetImportUtil importUtil, List listImportInfos, boolean isNeedSave) {
        if(importUtil == null || listImportInfos == null || listImportInfos.size() <=0){
            return;
        }
        int nValidSheetSize = listImportInfos.size();
        for(int i = 0 ; i < nValidSheetSize; i++){
            Object[] objImportInfos = (Object[])listImportInfos.get(i);//{sheetname,repcode,dynendrow,cellsModel}
            String strSheetName = (String)objImportInfos[0];
            String strRepCode = (String)objImportInfos[1];
            Integer nDynEndRow = new Integer((String)objImportInfos[2]);                
            CellsModel cellsModel= (CellsModel)objImportInfos[3];
            if (cellsModel==null){
                continue;
            }
            //设置报表相关信息
            importUtil.reInit(strRepCode,cellsModel, strSheetName, nDynEndRow);                    
            //导入Excel准备数据信息(CellsModel)
            importUtil.processImportData(isNeedSave); 
        }
    }
    /**
     * 得到MultiSheetImportUtil实例
     *      注意：是不含具体报表信息，使用前需要调用 MultiSheetImportUtil.reInit方法
     * @param strTaskPK
     * @param curUserInfoVO
     * @param strImportExcelDataClassPath
     * @param dataSource
     * @param importUtil
     * @param pubdataVo
     * @return
     */
    public static AutoMultiSheetImportUtil getImportUtilBase(String strTaskPK, UserInfoVO curUserInfoVO, String strImportExcelDataClassPath, DataSourceVO dataSource, MeasurePubDataVO pubdataVo,String strOrgPK,boolean bAutoCalc,String strLoginDate) {
        AutoMultiSheetImportUtil importUtil = null;
        String[] selVals = new String[3];
        selVals[2] = "-1";
        CellsModel cellsModel = null;
        String classPath = strImportExcelDataClassPath;
        if(classPath != null && classPath.length() > 0){
            try{
                Class utilClass = Class.forName(classPath);
                Class[] params = new Class[]{String.class,CellsModel.class,String.class,Integer.class,MeasurePubDataVO.class,UserInfoVO.class,String.class,DataSourceVO.class,String.class,boolean.class,String.class};
                Object[] paramValues = new Object[]{selVals[1], cellsModel, selVals[0], new Integer(selVals[2]), pubdataVo, curUserInfoVO, strTaskPK,dataSource,strOrgPK,bAutoCalc,strLoginDate};
                Constructor cons = utilClass.getDeclaredConstructor(params);
                importUtil = (AutoMultiSheetImportUtil)cons.newInstance(paramValues);
            }catch(Exception e){
            	AppDebug.debug(e);//@devTools                 e.printStackTrace();
            }
        }
        else{
            importUtil = new AutoMultiSheetImportUtil(selVals[1],cellsModel,selVals[0],new Integer(selVals[2]),pubdataVo,curUserInfoVO,strTaskPK,dataSource,strOrgPK,bAutoCalc,strLoginDate);
        }
        return importUtil;
    }
    /**
     * 检查预匹配结果
     * @param matchMap
     * @throws TableInputException 
     */
    public static void checkMatchMap(Hashtable matchMap) throws TableInputException {
        if(matchMap == null || matchMap.size() <= 0)
            throw new TableInputException("miufo1002743");  //"导入的Excel文件中没有工作表"
    }

    /**
     * 格式是否是支持直接导入的报表
     * modify by guogang 2007-11-30
     * 支持录入的含动态区域的报表
     * @param repId
     * @return
     * @throws Exception
     */
    public static boolean isCanImportDirectedRep(String repId) throws Exception {
        //报表是否含有动态区域
        boolean bHaveDynArea = isHaveDynArea(repId);
        return !bHaveDynArea;
    }

    /**
     * 报表是否含有动态区域
     * @param repId
     * @return
     */
    private static boolean isHaveDynArea(String repId) {
        if(repId == null){
            return false;
        }
        boolean isHaveDynArea = false;
        ReportCache reportCache = IUFOUICacheManager.getSingleton().getReportCache();
        String[] repKgPks = reportCache.getKeyCombs(repId);
        if(repKgPks != null){
            //如果报表的关键字组合数量大于1，则肯定包含动态区
            if(repKgPks.length > 1){
                isHaveDynArea = true;
            }
        }
        return isHaveDynArea;
    }

}
