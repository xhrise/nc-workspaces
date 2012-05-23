/*
 * �������� 2006-9-8
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
     * ���������ƺ�����˳���Զ�����ƥ�䣬�����ǰ�����Ѿ��򿪣����ҵ����Excel��Ҳֻ��һ�ű�����ֱ������ƥ��Զ�����
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
    	
    	//��ÿһ�����������ƥ�䣬Ȼ����м�¼��
        //ƥ��ɹ��Ļ����գ����������ƣ�������룩Ϊһ�飻
        //ƥ�䲻�ɹ�������Ҫ���Ըù���������Ĭ��ֵ����ʾû��ƥ��
        Map<String, String> repNMCMap = new HashMap<String, String>();
        Map<String, String> repCMNMap = new HashMap<String, String>();
        if(workBook != null){
            int sheetNum = workBook.getNumberOfSheets();
            String sheetName = null;
            String repCode = null;
            String repName = null;
            if(sheetNum == 1 && strCurRepPK!=null){
            	//���ڲ���ôҪ��ƥ�䵱ǰ�򿪱���
//                if(strCurRepPK == null){
//                    throw new TableInputException("miufo1002752");//��ǰϵͳ��û�д�Excel����Ҫ�����Ŀ�걨��  //"��ǰϵͳ��û�д�Excel����Ҫ�����Ŀ�걨��"+"!"
//                }
                sheetName = workBook.getSheetName(0);
                ChooseRepData chooseRepData = getCurRepData(chooseRepDatas,strCurRepPK);
                if(chooseRepData != null ){
                    repName = chooseRepData.getReportName();
                    repCode = chooseRepData.getReportCode();
                    //��ƥ���ֵ��¼����������֤һ��IUFO����ֻ�ܶ�Ӧһ��sheet
                    matchMap.put(sheetName, new String[]{repName, repCode});
                }
            }
            else{
                //���챨�����Ʊ��������ձ�
                int nRepCount = chooseRepDatas.length;
                for(int j = 0; j < nRepCount; j++){
                    repNMCMap.put(chooseRepDatas[j].getReportName(), chooseRepDatas[j].getReportCode());
                    repCMNMap.put(chooseRepDatas[j].getReportCode(), chooseRepDatas[j].getReportName());
                }

                for(int i = 0; i < sheetNum; i++){
                    sheetName = workBook.getSheetName(i);
                    if(repNMCMap.containsKey(sheetName)){
                        //���Ȱ��ձ������ƽ���ƥ��
                        repCode = (String)repNMCMap.get(sheetName);
                        repName = sheetName;
                        repNMCMap.remove(repName);
                        repCMNMap.remove(repCode);
                    } else if(repCMNMap.containsKey(sheetName)){
                        //���ձ���������ƥ��
                        repCode = sheetName;
                        repName = (String)repCMNMap.get(sheetName);
                        repNMCMap.remove(repName);
                        repCMNMap.remove(repCode);
                    } else{
                        //û��ƥ��ɹ�
                        matchMap.put(sheetName, NONE_MATCH);
                    }
                    //��ƥ���ֵ��¼����������֤һ��IUFO����ֻ�ܶ�Ӧһ��sheet
                    matchMap.put(sheetName, new String[]{repName, repCode});
                }
            }
        }
        return matchMap;
    }
    /**
     * �ӵ�ǰ�ɵ���ı�����Ϣ�������ҵ�ָ��������Ϣ
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
     * �ӵ�ǰ�ɵ���ı�����Ϣ�������ҵ�ָ��������Ϣ
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
     * �õ�����Excel׼��������Ϣ�����пɴ�����
     *   ����ͨ��������Ϣ����sheetת��ΪCellsModel
     * @param array
     * @param workBook
     * @return List,����ÿ��Elemet��Object[]{sheetname,repcode,dynendrow,cellsModel}
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
            System.arraycopy(selVals,0,objImportInfos,0,3);  // ��selVals�����е����ݸ��Ƶ�objImportInfos�� 0 = ��ʼ����  3 = ���Ƴ���
            
            String strSheetName = selVals[0];      //����ĵ����EXCEL������          
                //�õ�sheet
            int sheetIndex = workBook.getSheetIndex(strSheetName); // ��ȡ��ǰ����EXCEL��ҳǩ����������С��0
            if (sheetIndex<0){
                continue;
            }                
            CellsModel cellsModel=ExcelImpUtil.getCellsModelByExcel(workBook.getSheetAt(sheetIndex),workBook); //����sheet��Ϣת����cellsModel����
            if (cellsModel==null){
                continue;
            }else{
                objImportInfos[3] = cellsModel; //����ӵ��µ�������
                listImportInfos.add(objImportInfos);  // ��ӵ�LIST��
            }
        }
        return listImportInfos;
    }
    /**
     * ��ȡExcel�е�sheet��Ϣ
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
     * ��ȡExcel�е�sheet��Ϣ
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
     * ��ö�̬��ƥ�����Hearder��Ϣ����
     * @return
     */
    public static String[] getTableColumns() {
        String[] columns = new String[]{
                StringResource.getStringResource("miufo1002749"),  //"IUFO����"
                StringResource.getStringResource("miufo1002750"),  //"Excel������"
                StringResource.getStringResource("miufo1002751")};  //"��̬��������λ��"
        return columns;
    }
    /**
     * ������Excel׼��������Ϣ(CellsModel),���뵽���ݿ�
     * @param importUtil
     * @param listImportInfos List,����ÿ��Elemet��Object[]{sheetname,repcode,dynendrow,cellsModel}
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
            //���ñ��������Ϣ
            importUtil.reInit(strRepCode,cellsModel, strSheetName, nDynEndRow);                    
            //����Excel׼��������Ϣ(CellsModel)
            importUtil.processImportData(isNeedSave); 
        }
    }
    /**
     * �õ�MultiSheetImportUtilʵ��
     *      ע�⣺�ǲ������屨����Ϣ��ʹ��ǰ��Ҫ���� MultiSheetImportUtil.reInit����
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
     * ���Ԥƥ����
     * @param matchMap
     * @throws TableInputException 
     */
    public static void checkMatchMap(Hashtable matchMap) throws TableInputException {
        if(matchMap == null || matchMap.size() <= 0)
            throw new TableInputException("miufo1002743");  //"�����Excel�ļ���û�й�����"
    }

    /**
     * ��ʽ�Ƿ���֧��ֱ�ӵ���ı���
     * modify by guogang 2007-11-30
     * ֧��¼��ĺ���̬����ı���
     * @param repId
     * @return
     * @throws Exception
     */
    public static boolean isCanImportDirectedRep(String repId) throws Exception {
        //�����Ƿ��ж�̬����
        boolean bHaveDynArea = isHaveDynArea(repId);
        return !bHaveDynArea;
    }

    /**
     * �����Ƿ��ж�̬����
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
            //�������Ĺؼ��������������1����϶�������̬��
            if(repKgPks.length > 1){
                isHaveDynArea = true;
            }
        }
        return isHaveDynArea;
    }

}
