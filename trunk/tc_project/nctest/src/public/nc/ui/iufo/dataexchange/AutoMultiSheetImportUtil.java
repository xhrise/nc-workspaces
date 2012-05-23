package nc.ui.iufo.dataexchange;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.SAXException;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.MeasureCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.base.CodeCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.data.RepDataBO_Client;
import nc.ui.iufo.input.InputUtil;
import nc.ui.iufo.query.returnquery.ReportCommitBO_Client;
import nc.ui.iufo.repdataright.RepDataRightUtil;
import nc.vo.iufo.code.CodeInfoVO;
import nc.vo.iufo.data.MeasureDataVO;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.data.RepDataVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.pub.date.UFODate;
import nc.vo.iufo.query.returnquery.ReportCommitVO;
import nc.vo.iufo.repdataright.RepDataRightVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.ReportVO;
import nc.vo.jcom.xml.XMLUtil;

import com.ufsoft.iufo.batchreport.ui.Log;
import com.ufsoft.iufo.check.ui.CheckBO_Client;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.fmtplugin.key.KeywordModel;
import com.ufsoft.iufo.fmtplugin.measure.MeasureModel;
import com.ufsoft.iufo.fmtplugin.service.ReportFormatSrv;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropMng;
import com.ufsoft.table.AreaPosition;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

/**
* @update
* 2004-08-25 whtao
* 修改错误：1、无法导入动态区域下有固定区域指标数据，无法导入动态区域有单位名称的报表数据。 2、对有参照类型的指标或关键字，其数据中有小写的英文，无法导入
* 错误原因是：1、对动态区下的固定区域位置计算有问题;2、对参照类型的名称比较存在bug
* @end
* * @update 2004-08-07 whtao
* 解决动态区下方还有主表指标且行数达于两行时第二行的数据无法导入的错误
* 原因是在计算实际主表指标位置时方法错误，导致没有定位到正确的Excel位置
* @end

* * @update 2004-07-26 whtao
* 解决动态区为公有关键字时，写入了lineNo的错误
* @end

* @update 2004-07-26 whtao
* 解决整表只有一个指标数据时导入报错的错误
* @end
 * <p>Title: 多表页Excel数据导入执行 </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004-04-13</p>
 * <p>Company: </p>
 * @author whtao
 * @version 1.0
 */

public class AutoMultiSheetImportUtil {
	private String m_strOrgPK=null;
    private String sheetName = null;
    private String repCode = null;
    private String repName = null;
    private CellsModel excelCellsModel=null;
    private ReportFormatSrv repFormatSrv=null;
    DataSourceVO m_voDataSource=null;

    private String strtaskId = null;
    private UserInfoVO userInfo = null;
    private int dynEndRow = -1;//动态区结束行位置，该位置是实际位置
    private int errCount = 0;//当前报表中的错误数量，如果超过SINGLE_SHEET_SER_NUM，则该报表中的数据不被保存
    private Log logFile = new Log();
    private Vector m_vCheckResult=new Vector();
    private MeasurePubDataVO mainPubDataVO = null;
    private ArrayList dynPubDatavoList = null;
    public static final int SINGLE_SHEET_SER_NUM = 10;//预置错误数
    private static final String MEAS_TYPE_NUM = StringResource.getStringResource("miufopublic265");  //"数值"
    private boolean bAutoCalc=false;
    private String strLoginDate=null;

    /**
     * 构造器,此时清空日志中存储的内容
     * @param repcode String
     * @param sheetVal HashMap
     * @param sheetname String
     * @param repname String
     */
    public AutoMultiSheetImportUtil(String repcode,CellsModel cellsModel,String sheetname,Integer dynendrow,MeasurePubDataVO mainpubVo,UserInfoVO uservo,String taskId,DataSourceVO dataSource,String strOrgPK,boolean bAutoCalc,String strLoginDate) {
        init(repcode,cellsModel,sheetname,dynendrow,mainpubVo,uservo,taskId,dataSource,strOrgPK,bAutoCalc,strLoginDate);
    }

    //　初始化信息
    public void init(String repcode,CellsModel cellsModel,String sheetname,Integer dynendrow,MeasurePubDataVO mainpubVo,UserInfoVO uservo,String taskId,DataSourceVO dataSource,String strOrgPK,boolean bAutoCalc,String strLoginDate) {
        this.repCode = repcode;
        this.excelCellsModel = cellsModel;
        this.sheetName = sheetname;
        this.dynEndRow=-1;
        if (dynendrow!=null)
        	this.dynEndRow = dynendrow.intValue();
        this.mainPubDataVO = mainpubVo;
        this.dynPubDatavoList = new ArrayList();
        this.strtaskId = taskId;
        this.userInfo = uservo;
        this.m_voDataSource=dataSource;
        m_strOrgPK=strOrgPK;
        errCount = 0;
        this.bAutoCalc=bAutoCalc;
        this.strLoginDate=strLoginDate;
    }

    /**
     * 重新构造,此时不会改变日志中存储的内容
     * @param repcode String
     * @param sheetVal HashMap
     * @param sheetname String
     * @param repname String
     */
    public void reInit(String repcode,CellsModel cellsModel,String sheetname,Integer dynendrow){
        this.repCode = repcode;
        this.excelCellsModel = cellsModel;
        this.sheetName = sheetname;
        this.dynEndRow = dynendrow.intValue();
        this.dynPubDatavoList = new ArrayList();
        errCount = 0;
    }
    
    /**
     * 导入数据，目前仅支持一个动态区
     * boolean isNeedSave 是否需要保存,如果是单表导入,则不需要保存,而是设置到 m_tabUtil中，如果是多表导入，则需要保存
     * @throws CommonException
     * @return int 错误数量
     */
    public int processImportData(boolean isNeedSave) throws CommonException{
        ReportCache reportCache = IUFOUICacheManager.getSingleton().getReportCache(); // 获取当前缓存信息
 
        ReportVO report = reportCache.getByCode(repCode,false); // 获取样表的相关信息
        
        //如果工作表或报表为空，则无法导入数据，直接返回
        if(excelCellsModel == null || report == null)
            return 0;
        
        this.repName = report.getName();  // 样表名称 对应数据库字段 = NAME
        //判断是否该报表上报了，如果上报了的话，则不能进行导入
        if(isCommitedOrHaveNoRight(report.getReportPK()))
            return errCount;
        
        
        UfoContextVO context = new UfoContextVO();
        context.setContextId(report.getReportPK());
        context.setCurUserId(report.getUserPK());
        
        //由于打开的是格式，所以，对格式应该有写的权限，对数据只有查看的权限
        repFormatSrv=new ReportFormatSrv(context,false);
        DynAreaVO[] dynAreas=repFormatSrv.getDynAreas();
 
        //得到主表的指标数据
        ArrayList mainListData = importMainRepData(report);
        
        //处理动态区的指标和关键字数据
        ArrayList dynaListData = null;
        if(dynAreas != null && dynAreas.length > 0){
            if(dynAreas[0] != null ){
                try{
                	dynaListData = importDynRepData(dynAreas[0]);
                	if (dynaListData!=null){
                		//校验数据合法性
                		checkPubDataVO(dynPubDatavoList,dynaListData);
                	}
                }
                catch(CommonException ce){
                    addErr(ce.getMessage());
                }
            }
        }
        if(errCount > 0)
            return errCount;
        //构造MeasurePubDataVO数组，包含动态区
        MeasurePubDataVO[] mpdatas = new MeasurePubDataVO[dynPubDatavoList.size() + 1];
        mpdatas[0] = mainPubDataVO;
        if(dynPubDatavoList.size() > 0){
            for(int i = 0; i < dynPubDatavoList.size();i++)
                mpdatas[i+1] = (MeasurePubDataVO)dynPubDatavoList.get(i);
        }
        
        int dynListLen = 0;
        if(dynaListData != null && dynaListData.size() > 0)
            dynListLen = dynaListData.size();
        
        int mainDataCount = 0;
        if(mainListData != null && mainListData.size() > 0)
            mainDataCount = mainListData.size();
        
        int len = mainDataCount + dynListLen - 1;
        MeasureDataVO[] mdatas = new MeasureDataVO[len+1];
        for(;len >= 0;len--){
            if( len >= mainDataCount )
                mdatas[len] = (MeasureDataVO)dynaListData.get(len - mainDataCount);
            else
                mdatas[len] = (MeasureDataVO)mainListData.get(len);
        }
        
        RepDataVO repData = new RepDataVO(report.getReportPK(), report.getKeyCombPK());
        repData.setDatas(mpdatas, mdatas);
        repData.setUserID(context.getCurUserId());
        
        //保存指标数据
        try{
        	ReportFormatSrv repFormatSrv=InputUtil.getReportFormatSrv(report.getReportPK(), null, repData.getMainPubData().getAloneID(), repData.getMainPubData().getUnitPK(),userInfo.getID(), true, false,m_strOrgPK,strLoginDate);
			Hashtable<String,Vector<String>> hashDynAloneID=CellsModelOperator.loadDynDataAloneIDs(repFormatSrv.getCellsModel(), repFormatSrv.getContextVO());
            if(context.isOnServer())
                new nc.bs.iufo.data.RepDataBO().createRepData(repData,m_strOrgPK,hashDynAloneID);
            else
                RepDataBO_Client.createRepData(repData,m_strOrgPK,hashDynAloneID);
            
            if (bAutoCalc){
            	ReportFormatSrv calRepFormatSrv=InputUtil.getReportFormatSrv(report.getReportPK(), null, repData.getMainPubData().getAloneID(), repData.getMainPubData().getUnitPK(),userInfo.getID(), true, false,m_strOrgPK,strLoginDate);
            	InputUtil.calculate(calRepFormatSrv, strtaskId, userInfo.getID(), true,strLoginDate);
            	calRepFormatSrv.saveReportData(hashDynAloneID);
            }
            
            String autoCheck = SysPropMng.getSysProp(ISysProp.AUTO_CHECK).getValue();
            if (autoCheck != null && autoCheck.equals("true")){
	            CheckResultVO[][] results=CheckBO_Client.runRepCheck(strtaskId,new String[]{report.getReportPK()},new String[]{mainPubDataVO.getAloneID()},m_voDataSource,false,userInfo.getID(),strLoginDate);
	            if (results!=null){
	            	for (int i=0;i<results.length;i++){
	            		if (results[i]!=null)
	            			m_vCheckResult.addAll(Arrays.asList(results[i]));
	            	}
	            }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            addErrSave();
        }

        return errCount;
    }
    
    /**
     * 导入主表指标的数据
     * @param sheetValue HashMap
     * @param measVec Vector
     * @param logFile Log
     */
    private ArrayList importMainRepData(ReportVO report){
    	MeasureCache measCache=IUFOUICacheManager.getSingleton().getMeasureCache();
    	
    	String[] strMeasPKs=repFormatSrv.getAllMeausrePK();
    	if (strMeasPKs==null || strMeasPKs.length<=0)
    		return null;

    	CellPosition maxCell=repFormatSrv.getMaxCellPosition();
    	int iRowNum=maxCell.getRow()+1;
    	int iColNum=maxCell.getColumn()+1;
    	
    	ArrayList vMeasData=new ArrayList();
    	for (int iRow=0;iRow<iRowNum;iRow++){
    		for (int iCol=0;iCol<iColNum;iCol++){
    			CellPosition cell=CellPosition.getInstance(iRow,iCol);
    			if (repFormatSrv.isSingleCellOrCombCellLeftTop(cell)==false)
    				continue;
				
    			String strMeasID=repFormatSrv.getMeasurePKByCell(cell);
    			MeasureVO measure=measCache.getMeasure(strMeasID);
    			if (measure==null)
    				continue;
    			
    			if (measure.getKeyCombPK().equals(report.getKeyCombPK())==false)
    				continue;
    			
    			CellPosition numRowCol=repFormatSrv.getCellRowColNum(cell);
    			AreaPosition area=AreaPosition.getInstance(cell.getRow(),cell.getColumn(),numRowCol.getColumn(),numRowCol.getRow());
    			
    			area=getMainMeasureArea(area);
    			String objVal = importMeasureDataByArea(area,measure);
    			
                MeasureDataVO measData = new MeasureDataVO();
                measData.setAloneID(mainPubDataVO.getAloneID());
                
                measData.setMeasureVO(measure);
                if(mainPubDataVO.getKeyGroup() != null){
                	measData.setPrvtKeyGroupPK(mainPubDataVO.getKeyGroup().getKeyGroupPK());
                }
                measData.setDataValue(objVal);
                vMeasData.add(measData);
    		}
    	}
    	return vMeasData;
    }
    
    
    private AreaPosition getMainMeasureArea(AreaPosition posArea){
        if(dynEndRow < 0)
            return posArea;
        
        DynAreaVO[] dynAreas=repFormatSrv.getDynAreas();
        if (dynAreas==null || dynAreas.length<=0)
        	return posArea;

        int mdOffset = 0;//循环中当前动态区和该主表指标之间的间隔行数
        int mOf = 0;//posArea地结束行与起始行之间的间隔行数
        for( int i=0;i<dynAreas.length;i++ ){
        	DynAreaVO dynVO=dynAreas[i];
            AreaPosition area=dynVO.getOriArea();
            if(posArea.getStart().getRow()>area.getEnd().getRow()){
                mdOffset = posArea.getStart().getRow()-area.getEnd().getRow();
                mOf =posArea.getEnd().getRow()- posArea.getStart().getRow();
                
                CellPosition newStart=CellPosition.getInstance(dynEndRow-1+mdOffset,posArea.getStart().getColumn());
                CellPosition newEnd=CellPosition.getInstance(newStart.getRow()+mOf,posArea.getEnd().getColumn());
                posArea=AreaPosition.getInstance(newStart,newEnd);
            }
        }
        return posArea;
    }
    /**
     * 导入动态区指标的数据
     * @param sheetValue HashMap
     * @param measVec Vector
     * @param logFile Log
     */
    private ArrayList importDynRepData(DynAreaVO dynVo){
        //如果错误数超过预置错误数，则直接返回，并将错误日志提示给用户
        if(errCount > SINGLE_SHEET_SER_NUM)
            return null;

        //如果动态区结束行位置为-1或者结束行位置要小于动态区起始行位置，则直接返回null;
        AreaPosition dynArea =dynVo.getOriArea();
        if(dynEndRow == -1 || dynEndRow < dynArea.getStart().getRow())
            return null;
        
        MeasureModel measModel=MeasureModel.getInstance(repFormatSrv.getCellsModel());
        Hashtable hashDynMeas=measModel.getDynAreaMeasureVOPos(dynVo.getDynamicAreaPK());
        if (hashDynMeas==null || hashDynMeas.size()<=0)
        	return null;
        
        KeywordModel keyModel=KeywordModel.getInstance(repFormatSrv.getCellsModel());
        Hashtable hashDynKeys=keyModel.getDynKeyVOPos(dynVo.getDynamicAreaPK());
        if (hashDynKeys==null || hashDynKeys.size()<=0)
        	return null;
        
        
        //考虑动态区内的指标可能定义在多行上，需要记录一下增加一个pubdataVO数据的时候一次增加多少行,即偏移量
        //默认为指标和关键字都在同一行上
        int dynRowOffset = getDynOffsetRows(hashDynMeas,hashDynKeys);
     //   int dynRowOffset=0;
        
        //记录当前增加的一条记录后实际开始的行位置
        int dynAddRowActIndex =0;

        MeasureDataVO md = null;
        MeasurePubDataVO dynPubDataVo = null;
        KeyGroupCache keyGroupCache = IUFOUICacheManager.getSingleton().getKeyGroupCache();
        KeyGroupVO newMainKeyGroup = (KeyGroupVO)mainPubDataVO.getKeyGroup().clone();
        
        KeyVO[] dynKeyVos =(KeyVO[])hashDynKeys.values().toArray(new KeyVO[0]);

        newMainKeyGroup.addKeyToGroup(dynKeyVos);
        newMainKeyGroup = keyGroupCache.getPkByKeyGroup(newMainKeyGroup);
        HashMap priKeyVOIndexMap = new HashMap();
        KeyVO[] priKeyVos = newMainKeyGroup.getPrivatekey();
        boolean isHavePriKey = false;
        if(priKeyVos != null && priKeyVos.length > 0){
            isHavePriKey = true;
            for(int n = 0; n < priKeyVos.length; n++){
                priKeyVOIndexMap.put(priKeyVos[n].getName(), new Integer(n + 1));
            }
        }
        
        ArrayList vecDataList=new ArrayList();
        HashSet<String> setKeyValue=new HashSet<String>();
        
        int iMaxDataRow=getMaxDataRow();
        int iDynStartRow=dynArea.getStart().getRow();
        int iDynRowNo=1;
        while(iDynStartRow <= dynEndRow-1 && iDynStartRow<=iMaxDataRow){
        	HashMap indexValueMap = new HashMap();
        	
            //构造动态区的pubdataVO
        	if (isHavePriKey==false){
	            dynPubDataVo = new MeasurePubDataVO();
	            dynPubDataVo.setKType(newMainKeyGroup.getKeyGroupPK());
	            dynPubDataVo.setKeyGroup(newMainKeyGroup);
	            dynPubDataVo.setUnitPK(mainPubDataVO.getUnitPK());
	            dynPubDataVo.setVer(mainPubDataVO.getVer());
	            dynPubDataVo.setFormulaID(mainPubDataVO.getFormulaID());
	
	            KeyVO[] mainKeys = mainPubDataVO.getKeyGroup().getKeys();
	            if(mainKeys != null && mainKeys.length > 0 ){
	                for(int m = 0; m < mainKeys.length; m++)
	                    dynPubDataVo.setKeywordByName(mainKeys[m].getName(),mainPubDataVO.getKeywordByName(mainKeys[m].getName()));
            }
        	}else
        		dynPubDataVo=null;
            
            //标志校验动态区的关键字输入是否合法，如果不合法，则不能将该关键字所确定的一组数据导入
            boolean checkKey = true;
            String value;
            CellPosition[] cells=(CellPosition[])hashDynKeys.keySet().toArray(new CellPosition[0]);
            String strPrivKeyValue="";
            for(int j = 0 ; j <cells.length; j++ ){
            	CellPosition cell=cells[j];
            	CellPosition numRowCol=repFormatSrv.getCellRowColNum(cell);
            	KeyVO key=(KeyVO)hashDynKeys.get(cell);
            	
            	AreaPosition area=AreaPosition.getInstance(cell.getRow(),cell.getColumn(),numRowCol.getColumn(),numRowCol.getRow());
                AreaPosition actArea = getDynActArea(dynAddRowActIndex,area,dynArea);
                value = importKeyDataByArea(actArea,key);
                if(value == null || value.trim().length()<=0){
                    checkKey = false;
                    break;
                }
                
                if (dynPubDataVo!=null)
                	dynPubDataVo.setKeywordByName(key.getName(),value);
                if(priKeyVOIndexMap.get(key.getName()) != null){
                    indexValueMap.put(priKeyVOIndexMap.get(key.getName()),value);
                    strPrivKeyValue+=value+"\r\n";
                }
            }
            
            if (strPrivKeyValue.length()>0 && setKeyValue.contains(strPrivKeyValue)){
            	addErr(StringResource.getStringResource("miufoUnitMng027",new String[]{strPrivKeyValue.replaceAll("\r\n", "、")}));
            	return null;
            }
            setKeyValue.add(strPrivKeyValue);
            
            if(checkKey){
                String strAloneID = mainPubDataVO.getAloneID();
                try{
                	if (dynPubDataVo!=null){
                		strAloneID = MeasurePubDataBO_Client.getAloneID(dynPubDataVo);
                        dynPubDataVo.setAloneID(strAloneID);
                        dynPubDatavoList.add(dynPubDataVo);
                	}
                }
                catch(Exception e){
                }

                //关键字校验成功才允许进行指标数据的导入
                cells=(CellPosition[])hashDynMeas.keySet().toArray(new CellPosition[0]);
                for(int i = 0; i < cells.length; i++){
                	CellPosition cell=cells[i];
                	CellPosition numRowCol=repFormatSrv.getCellRowColNum(cell);
                	MeasureVO measure=(MeasureVO)hashDynMeas.get(cell);
                	
                	AreaPosition area=AreaPosition.getInstance(cell.getRow(),cell.getColumn(),numRowCol.getColumn(),numRowCol.getRow());
                    AreaPosition actArea = getDynActArea(dynAddRowActIndex,area,dynArea);
                    
                    //控制导入时按照用户设定的值导入
                    if(actArea.getStart().getRow() > dynEndRow)
                        continue;
                    
                    String objVal = importMeasureDataByArea(actArea,measure);
                    if(objVal == null)
                        continue;
                    
                    md = new MeasureDataVO();
                    md.setAloneID(strAloneID);
                    
                    md.setMeasureVO(measure);
                    if(isHavePriKey)
                        //动态区为私有关键字时，要设置行号
                        md.setRowNo(iDynRowNo);
                    else
                        //动态区为公有关键字时，行号设为0
                        md.setRowNo(0);
                    
                    setMeasureDataVOKeyValue(md,indexValueMap);
                    md.setPrvtKeyGroupPK(measure.getKeyCombPK());
                    md.setDataValue(objVal);
                    vecDataList.add(md);
                }
            }
            dynAddRowActIndex = dynAddRowActIndex + dynRowOffset;
            iDynStartRow=iDynStartRow+dynRowOffset;
            iDynRowNo++;
        }
        return vecDataList;
    }
    
    private void setMeasureDataVOKeyValue( MeasureDataVO md,HashMap indexValueMap){
        Set key = indexValueMap.keySet();
        Integer index = null;
        Iterator it = key.iterator();
        while(it.hasNext())
        {
            index = (Integer)it.next();
            md.setKeyValueByIndex(index.intValue(),(String)indexValueMap.get(index));
        }
    }
    
    /**
     * 转换指标或关键字sheet中对应数据的实际位置，包括增加一行数据后指标或关键字对应的新位置
     * @param dynAddRowActIndex int
     * @param posArea AreaPosition
     * @param dynArea AreaPosition
     * @return AreaPosition
     */
    private AreaPosition getDynActArea(int dynAddRowActIndex,AreaPosition posArea,AreaPosition dynArea){
        //目前仅支持行扩展
        int iStartRow = dynAddRowActIndex + posArea.getStart().getRow();
        int iStartCol =  posArea.getStart().getColumn();
        int iEndRow = dynAddRowActIndex + posArea.getEnd().getRow();
        int iEndCol= posArea.getEnd().getColumn();
        return AreaPosition.getInstance(CellPosition.getInstance(iStartRow,iStartCol),CellPosition.getInstance(iEndRow,iEndCol));
    }
    
    /**
     * 计算动态区增加一组数据应该要增加的实际行数，用来在动态区定位指标或关键字取相应位置的值
     * @param measVec Vector
     * @param keywordVec Vector
     * @return int
     */
    private int getDynOffsetRows(Hashtable hashMeas,Hashtable hashKey){
        int startRow = -1,endRow =-1;
        
        ArrayList vCell=new ArrayList();
        vCell.addAll(hashMeas.keySet());
        vCell.addAll(hashKey.keySet());

        for(int i=0;i<vCell.size();i++ ){
        	CellPosition cell=(CellPosition)vCell.get(i);
        	CellPosition numRowCol=repFormatSrv.getCellRowColNum(cell);
        	AreaPosition area=AreaPosition.getInstance(cell.getRow(),cell.getColumn(),numRowCol.getColumn(),numRowCol.getRow());

            if(startRow<0 || area.getStart().getRow()< startRow )
                startRow = area.getStart().getRow();
            if(endRow<0 || area.getEnd().getRow()> endRow)
                endRow =area.getEnd().getRow();
        }
    
        return endRow - startRow + 1;
    }
    
    
    private String importKeyDataByArea(AreaPosition area,KeyVO kvo){
    	Object objVal=excelCellsModel.getCellValue(area.getStart());
        String contentValue = null;
        
        //如果当前单元没有值，则继续进行下一个单元的取值
        if(objVal == null)
            return null;
       
        if(kvo.getType() == KeyVO.TYPE_TIME)
           //对时间类型关键字的导入,要校验日期的合法性，
            contentValue = checkKeyDateStr(objVal.toString(),kvo.getTimeProperty(),area); 
        else if(kvo.getType() == KeyVO.TYPE_REF)
            contentValue = checkRefDataStr(objVal.toString(),kvo.getRef(),area);
        else
            //对时间类型关键字的导入,要校验日期的合法性，
            contentValue = checkKeyCharStr(kvo,objVal.toString(),area);
       
        return contentValue;

    }
    private String checkRefDataStr(String strValue,String codeId,AreaPosition area)
    {
        if(strValue != null && strValue.length() > 0)
        {
            //对参照型的导入，如果得到的值在缓存中查找不到，则说明输入数据有误！
            CodeCache codeCache = IUFOUICacheManager.getSingleton().getCodeCache();
            try{
                nc.vo.iufo.code.CodeVO codevo = codeCache.findCodeByID(codeId);
                //返回的第一个是以CodeInfoVO.getId()为索引，第二个以CodeInfoVO.getContent()为索引
                HashMap[] codeInfoMap = genCodeInfoMap(codeCache.getAllSortCodeInfo(codevo));
                if(!codeInfoMap[1].containsValue(strValue.trim())){
                    if(codeInfoMap[0].containsValue(strValue.trim())){
                        strValue = (String)codeInfoMap[1].get(strValue.trim());
                    } else{
                        addErrData(area.toString());
                        return null;
                    }
                }
                return strValue;
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;

    }
    /**
     * 校验时间关键字的录入
     * @param strValue String
     * @param timeProp String
     * @param area AreaPosition
     * @return String
     */
    private String checkKeyDateStr(String strValue,String timeProp,AreaPosition area)
    {
        if(strValue != null && strValue.length() > 0)
        {
            if(UFODate.isAllowDate(strValue) == false){
                addErrDataTime(area.toString(),StringResource.getStringResource("miufo1002756"));  //"日期不合法"
                return null;
            }
            UFODate ufoDate = new nc.vo.iufo.pub.date.UFODate(strValue);
            String strInputDate = ufoDate.getEndDay(timeProp).toString();
            TaskVO taskVO = IUFOUICacheManager.getSingleton().getTaskCache().getTaskVO(strtaskId);
            //对任务的有效期限作判断
            if(taskVO != null){
                if(taskVO.getStartTime() != null && !taskVO.getStartTime().equals("")){
                    UFODate inputUfoDate = new UFODate(strInputDate);
                    UFODate startUfoDate = new UFODate(taskVO.getStartTime());
                    startUfoDate = startUfoDate.getEndDay(timeProp);
                    if(inputUfoDate.compareTo(startUfoDate) < 0){
                        addErrDataTime(area.toString(),StringResource.getStringResource("miufo1002757"));  //"录入日期不在任务有效期限范围内"
                        return null;
                    }
                }
                if(taskVO.getEndTime() != null && !taskVO.getEndTime().equals("")){
                    UFODate inputUfoDate = new UFODate(strInputDate);
                    UFODate endUfoDate = new UFODate(taskVO.getEndTime());
                    endUfoDate = endUfoDate.getEndDay(timeProp);
                    if(endUfoDate.compareTo(inputUfoDate) < 0){
                        addErrDataTime(area.toString(),StringResource.getStringResource("miufo1002758"));  //"录入日期不在任务有效期范围内"
                        return null;
                    }
                }
            }
            strValue = strInputDate;
        }
        return strValue;
    }
    /**
     * 校验单位关键字
     * @param kvo KeyVO
     * @param strValue String
     * @param area AreaPosition
     * @return String
     */
    private String checkKeyCharStr(KeyVO kvo,String strValue,AreaPosition area){
        if(strValue != null && strValue.length() > 0){
            UnitCache unitCache = IUFOUICacheManager.getSingleton().getUnitCache();
            //主要校验单位合法性
            if(kvo.getKeywordPK().equals(KeyVO.CORP_PK)){
            	UnitInfoVO unitInfo=unitCache.getUnitInfoByCode(strValue);
                if(unitInfo == null){
                    addErrDataTime(area.toString(), StringResource.getStringResource("miufo1002759"));  //"不存在该单位"
                    return null;
                }
                strValue=unitInfo.getPK();
                if(unitCache.isSubOrGrandSubUnit(strValue, userInfo.getUnitId(),m_strOrgPK) == false &&
                   strValue.equals(userInfo.getUnitId()) == false){
                    addErrDataTime(area.toString(), StringResource.getStringResource("miufo1002760"));  //"不可录入上级单位的数据"
                    return null;
                }
            } 
            else if(kvo.getKeywordPK().equals(KeyVO.DIC_CORP_PK)){
            	UnitInfoVO unitInfo=unitCache.getUnitInfoByCode(strValue);
                if(unitInfo == null){
                    addErrDataTime(area.toString(), StringResource.getStringResource("miufo1002761"));  //"不存在该对方单位"
                    return null;
                }
                strValue=unitInfo.getPK();
            }
            else if(StringResource.getStringResource("miufo1001508").equals(kvo.getName())){  //"行号"
                try{
                    strValue = "" + new Double(strValue).intValue();
                    System.out.println("rowmun = " + strValue);
                }
                catch(Exception e){
                    addErrDataType(area.toString(), MEAS_TYPE_NUM);
                    return null;
                }
            }
            return strValue;
        }
        return null;
    }
    
    /**
     * 根据位置和指标的类型得到录入的值
     * @param area AreaPosition
     * @param mvo MeasureTableVO
     * @return String
     */
    private String importMeasureDataByArea(AreaPosition area,MeasureVO measure){
    	Object objVal=excelCellsModel.getCellValue(area.getStart());

        //如果当前单元没有值，则继续进行下一个单元的取值
        if(objVal == null)
            return null;
        
        String content = objVal.toString();
        if(measure.getType() == MeasureVO.TYPE_NUMBER && content!=null && content.trim().length()>0){
           //对数值型指标的导入,如果Double.parseDouble出错，则说明类型不符
            try{
                Double.parseDouble(content);
            }
            catch(Exception e){
                addErrDataType(area.toString(), MEAS_TYPE_NUM);
                return null;
            }
            content = objVal.toString();
        }
        else if(measure.getType() == MeasureVO.TYPE_CODE)
            content = checkRefDataStr(content,measure.getRefPK(),area);
       
        //对于字符型指标，由于字符限制比较少，则直接处理
        return content;
    }
   
    /**
     * 得到编码内容和编码ID的索引
     * @param codeInfos CodeInfoVO[]
     * @return HashMap[]
     */
    private HashMap[] genCodeInfoMap(CodeInfoVO[] codeInfos)
    {
        HashMap[] map = new HashMap[2];
        if(codeInfos != null )
        {
            CodeInfoVO info = null;
            String id ,content;
            map[0] = new HashMap();
            map[1] = new HashMap();
            for(int i = 0 ; i < codeInfos.length ; i++ )
            {
                info = codeInfos[i];
                if( info != null )
                {
                    id = info.getId();
                    content = info.getContent();
                    if(id != null && content != null)
                    {
                        map[0].put(id, content);
                        map[1].put(content, id);
                    }
                }
            }
        }
        return map;
    }

    /**
     * 写错误日志,相应的位置上没有相应的指定的类型的数据,或类型也不相符合
     * @param area String
     * @param repType String
     */
    private void addErrDataType(String area,String repType){
        errCount += 1;
        //"IUFO报表："+repName+"和匹配的Excel工作表："+sheetName+"中的相应位置 "+area+" 上数据类型不符!需要\""+repType+"\"类型"
        logFile.writer(StringResource.getStringResource("miufo1002749")+  //"IUFO报表"
                       StringResource.getStringResource("miufopublic100")+repName+StringResource.getStringResource("miufo1002765")+
                       StringResource.getStringResource("miufopublic100")+sheetName+StringResource.getStringResource("miufo1002763")+" "+area+" "+
                       StringResource.getStringResource("miufo1002766")+"\""+repType+"\""+
                       StringResource.getStringResource("miufopublic263"));  //"类型"
    }
    /**
     * 写错误日志,相应的位置上没有相应的指定的类型的数据,或类型也不相符合，主要指关键字的日期不合法
     * @param area String
     * @param repType String
     */
    private void addErrDataTime(String area,String errMsg)
    {
        errCount += 1;
        //"IUFO报表："+repName+"和匹配的Excel工作表："+sheetName+"中的相应位置 "+area+" 上关键字录入错误："+errMsg+"!"
        logFile.writer(StringResource.getStringResource("miufo1002749")+  //"IUFO报表"
                       StringResource.getStringResource("miufopublic100")+repName+StringResource.getStringResource("miufo1002765")+StringResource.getStringResource("miufopublic100")+sheetName+
                       StringResource.getStringResource("miufo1002763")+" "+area+" "+
                       StringResource.getStringResource("miufo1002767")+StringResource.getStringResource("miufopublic100")+errMsg+"!");
    }

    /**
     * 写错误日志,相应的位置上没有相应的指定的类型的数据,或类型也不相符合
     * @param area String
     */
    private void addErrData(String area)
    {
        errCount += 1;
        //"IUFO报表："+repName+"和匹配的Excel工作表："+sheetName+"中的相应位置 "+area+" 上数据错误！输入的编码在系统中不存在！"
        logFile.writer(StringResource.getStringResource("miufo1002749")+  //"IUFO报表"
                       StringResource.getStringResource("miufopublic100")+repName+StringResource.getStringResource("miufo1002765")+StringResource.getStringResource("miufopublic100")+sheetName+
                       StringResource.getStringResource("miufo1002763")+" "+area+" "+
                       StringResource.getStringResource("miufo1002768"));  //"上数据错误！输入的编码在系统中不存在！"
    }
    private void addErrSave()
    { 
        errCount += 1;
        //"保存报表"+repName+"("+repCode+")"+"的指标数据时出错！"
        logFile.writer(StringResource.getStringResource("miufo1002770",new String[]{repName,repCode}));
//        +  //"保存报表"
//                       repName+"("+repCode+")"+StringResource.getStringResource("miufo1002770"));
    }
    private void addErrCommited()
    {
        errCount += 1;
        //"报表"+repName+"("+repCode+")已经上报，不能再修改该报表的数据!"
        logFile.writer(StringResource.getStringResource("miufo1002771",new String[]{repName,repCode}));  //"已经上报，不能再修改该报表的数据!"
    }
    private void addErr(String errStr)
    {
        errCount += 1;
        logFile.writer(errStr);
    }
    public Log getLog()
    {
        return logFile;
    }
    /**
     * 判断报表是否已经上报，如果已经上报，则不能进行导入数据
     * @return boolean
     */
    private boolean isCommitedOrHaveNoRight(String repPk)
    {
        try
        {
            String aloneId = mainPubDataVO.getAloneID();
            ReportCommitVO[][] repCommits = ReportCommitBO_Client.
                loadRptCommitByAloneIds(new String[] {aloneId});

//            String[] strPassedRepIds = CheckResultBO_Client.loadPassedRepIdsByAloneId(aloneId);

            //报表上报后不允许保存
            boolean check = true;
            for(int i = 0 ; i < repCommits.length ; i++){
            	for(int j = 0 ; j < repCommits[i].length ; j++ ){
            		if(repCommits[i][j].getCommitFlag() == 0){
            			check = false;
            			break;
            		}
            	}
            }
            
            
            if (check) {
                for (int m = 0; m < repCommits.length; m++) {
                    if (repCommits[m] != null && repCommits[m].length > 0) {
                        for (int n = 0; n < repCommits[m].length; n++) {
                            if (repCommits[m][n] != null &&
                                repCommits[m][n].getRepId().equals(repPk)) {
                                addErrCommited();
                                return true;
                            }
                        }
                    }
                }
            }
            
            MeasurePubDataVO pubData=MeasurePubDataBO_Client.findByAloneID(aloneId);
            String[] strCanImportReps=new String[]{repPk};
            if (isNeedFilterByDataRight() && pubData.getUnitPK()!=null && pubData.getUnitPK().trim().length()>0){
            	TaskVO task=IUFOUICacheManager.getSingleton().getTaskCache().getTaskVO(strtaskId);
            	strCanImportReps=RepDataRightUtil.loadRepsByRight(userInfo,task,RepDataRightVO.RIGHT_TYPE_MODIFY,new String[]{repPk},pubData.getUnitPK(),m_strOrgPK);
            }
            if (strCanImportReps==null || strCanImportReps.length<=0)
            	return true;
        }
        catch(nc.pub.iufo.exception.UFOSrvException e){
          e.printStackTrace();
          if (e.detail!=null && e.detail instanceof nc.util.iufo.pub.UfoException)
            throw new CommonException(((nc.util.iufo.pub.UfoException)e.detail).getExResourceId(),((nc.util.iufo.pub.UfoException)e.detail).getParams());
          else
            throw new CommonException("miufo10000");
        }
        catch (Exception e) {
          throw new CommonException("miufo10000");//未定义的错误
        }
        return false;
    }
    /**
     * 如果需要对导入的数据进行校验，则通过继承本类，并实现该方法来进行校验
     * @param pubDataVOList ArrayList
     * @param measureDataList ArrayList
     * @throws CommonException
     */
    protected void checkPubDataVO(ArrayList pubDataVOList,ArrayList measureDataList) throws CommonException{
    }
    
    protected boolean isNeedFilterByDataRight(){
    	return true;
    }
    
    protected int getMaxDataRow(){
    	return excelCellsModel.getMaxRow();
    }
    
    public CheckResultVO[] getCheckResults(){
    	return (CheckResultVO[])m_vCheckResult.toArray(new CheckResultVO[0]);
    }
    
    public void setCheckResults(CheckResultVO[] results){
    	m_vCheckResult=new Vector(Arrays.asList(results));
    }
}
