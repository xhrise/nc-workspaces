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
* �޸Ĵ���1���޷����붯̬�������й̶�����ָ�����ݣ��޷����붯̬�����е�λ���Ƶı������ݡ� 2�����в������͵�ָ���ؼ��֣�����������Сд��Ӣ�ģ��޷�����
* ����ԭ���ǣ�1���Զ�̬���µĹ̶�����λ�ü���������;2���Բ������͵����ƱȽϴ���bug
* @end
* * @update 2004-08-07 whtao
* �����̬���·���������ָ����������������ʱ�ڶ��е������޷�����Ĵ���
* ԭ�����ڼ���ʵ������ָ��λ��ʱ�������󣬵���û�ж�λ����ȷ��Excelλ��
* @end

* * @update 2004-07-26 whtao
* �����̬��Ϊ���йؼ���ʱ��д����lineNo�Ĵ���
* @end

* @update 2004-07-26 whtao
* �������ֻ��һ��ָ������ʱ���뱨��Ĵ���
* @end
 * <p>Title: ���ҳExcel���ݵ���ִ�� </p>
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
    private int dynEndRow = -1;//��̬��������λ�ã���λ����ʵ��λ��
    private int errCount = 0;//��ǰ�����еĴ����������������SINGLE_SHEET_SER_NUM����ñ����е����ݲ�������
    private Log logFile = new Log();
    private Vector m_vCheckResult=new Vector();
    private MeasurePubDataVO mainPubDataVO = null;
    private ArrayList dynPubDatavoList = null;
    public static final int SINGLE_SHEET_SER_NUM = 10;//Ԥ�ô�����
    private static final String MEAS_TYPE_NUM = StringResource.getStringResource("miufopublic265");  //"��ֵ"
    private boolean bAutoCalc=false;
    private String strLoginDate=null;

    /**
     * ������,��ʱ�����־�д洢������
     * @param repcode String
     * @param sheetVal HashMap
     * @param sheetname String
     * @param repname String
     */
    public AutoMultiSheetImportUtil(String repcode,CellsModel cellsModel,String sheetname,Integer dynendrow,MeasurePubDataVO mainpubVo,UserInfoVO uservo,String taskId,DataSourceVO dataSource,String strOrgPK,boolean bAutoCalc,String strLoginDate) {
        init(repcode,cellsModel,sheetname,dynendrow,mainpubVo,uservo,taskId,dataSource,strOrgPK,bAutoCalc,strLoginDate);
    }

    //����ʼ����Ϣ
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
     * ���¹���,��ʱ����ı���־�д洢������
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
     * �������ݣ�Ŀǰ��֧��һ����̬��
     * boolean isNeedSave �Ƿ���Ҫ����,����ǵ�����,����Ҫ����,�������õ� m_tabUtil�У�����Ƕ���룬����Ҫ����
     * @throws CommonException
     * @return int ��������
     */
    public int processImportData(boolean isNeedSave) throws CommonException{
        ReportCache reportCache = IUFOUICacheManager.getSingleton().getReportCache(); // ��ȡ��ǰ������Ϣ
 
        ReportVO report = reportCache.getByCode(repCode,false); // ��ȡ����������Ϣ
        
        //���������򱨱�Ϊ�գ����޷��������ݣ�ֱ�ӷ���
        if(excelCellsModel == null || report == null)
            return 0;
        
        this.repName = report.getName();  // �������� ��Ӧ���ݿ��ֶ� = NAME
        //�ж��Ƿ�ñ����ϱ��ˣ�����ϱ��˵Ļ������ܽ��е���
        if(isCommitedOrHaveNoRight(report.getReportPK()))
            return errCount;
        
        
        UfoContextVO context = new UfoContextVO();
        context.setContextId(report.getReportPK());
        context.setCurUserId(report.getUserPK());
        
        //���ڴ򿪵��Ǹ�ʽ�����ԣ��Ը�ʽӦ����д��Ȩ�ޣ�������ֻ�в鿴��Ȩ��
        repFormatSrv=new ReportFormatSrv(context,false);
        DynAreaVO[] dynAreas=repFormatSrv.getDynAreas();
 
        //�õ������ָ������
        ArrayList mainListData = importMainRepData(report);
        
        //����̬����ָ��͹ؼ�������
        ArrayList dynaListData = null;
        if(dynAreas != null && dynAreas.length > 0){
            if(dynAreas[0] != null ){
                try{
                	dynaListData = importDynRepData(dynAreas[0]);
                	if (dynaListData!=null){
                		//У�����ݺϷ���
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
        //����MeasurePubDataVO���飬������̬��
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
        
        //����ָ������
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
     * ��������ָ�������
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

        int mdOffset = 0;//ѭ���е�ǰ��̬���͸�����ָ��֮��ļ������
        int mOf = 0;//posArea�ؽ���������ʼ��֮��ļ������
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
     * ���붯̬��ָ�������
     * @param sheetValue HashMap
     * @param measVec Vector
     * @param logFile Log
     */
    private ArrayList importDynRepData(DynAreaVO dynVo){
        //�������������Ԥ�ô���������ֱ�ӷ��أ�����������־��ʾ���û�
        if(errCount > SINGLE_SHEET_SER_NUM)
            return null;

        //�����̬��������λ��Ϊ-1���߽�����λ��ҪС�ڶ�̬����ʼ��λ�ã���ֱ�ӷ���null;
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
        
        
        //���Ƕ�̬���ڵ�ָ����ܶ����ڶ����ϣ���Ҫ��¼һ������һ��pubdataVO���ݵ�ʱ��һ�����Ӷ�����,��ƫ����
        //Ĭ��Ϊָ��͹ؼ��ֶ���ͬһ����
        int dynRowOffset = getDynOffsetRows(hashDynMeas,hashDynKeys);
     //   int dynRowOffset=0;
        
        //��¼��ǰ���ӵ�һ����¼��ʵ�ʿ�ʼ����λ��
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
        	
            //���춯̬����pubdataVO
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
            
            //��־У�鶯̬���Ĺؼ��������Ƿ�Ϸ���������Ϸ������ܽ��ùؼ�����ȷ����һ�����ݵ���
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
            	addErr(StringResource.getStringResource("miufoUnitMng027",new String[]{strPrivKeyValue.replaceAll("\r\n", "��")}));
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

                //�ؼ���У��ɹ����������ָ�����ݵĵ���
                cells=(CellPosition[])hashDynMeas.keySet().toArray(new CellPosition[0]);
                for(int i = 0; i < cells.length; i++){
                	CellPosition cell=cells[i];
                	CellPosition numRowCol=repFormatSrv.getCellRowColNum(cell);
                	MeasureVO measure=(MeasureVO)hashDynMeas.get(cell);
                	
                	AreaPosition area=AreaPosition.getInstance(cell.getRow(),cell.getColumn(),numRowCol.getColumn(),numRowCol.getRow());
                    AreaPosition actArea = getDynActArea(dynAddRowActIndex,area,dynArea);
                    
                    //���Ƶ���ʱ�����û��趨��ֵ����
                    if(actArea.getStart().getRow() > dynEndRow)
                        continue;
                    
                    String objVal = importMeasureDataByArea(actArea,measure);
                    if(objVal == null)
                        continue;
                    
                    md = new MeasureDataVO();
                    md.setAloneID(strAloneID);
                    
                    md.setMeasureVO(measure);
                    if(isHavePriKey)
                        //��̬��Ϊ˽�йؼ���ʱ��Ҫ�����к�
                        md.setRowNo(iDynRowNo);
                    else
                        //��̬��Ϊ���йؼ���ʱ���к���Ϊ0
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
     * ת��ָ���ؼ���sheet�ж�Ӧ���ݵ�ʵ��λ�ã���������һ�����ݺ�ָ���ؼ��ֶ�Ӧ����λ��
     * @param dynAddRowActIndex int
     * @param posArea AreaPosition
     * @param dynArea AreaPosition
     * @return AreaPosition
     */
    private AreaPosition getDynActArea(int dynAddRowActIndex,AreaPosition posArea,AreaPosition dynArea){
        //Ŀǰ��֧������չ
        int iStartRow = dynAddRowActIndex + posArea.getStart().getRow();
        int iStartCol =  posArea.getStart().getColumn();
        int iEndRow = dynAddRowActIndex + posArea.getEnd().getRow();
        int iEndCol= posArea.getEnd().getColumn();
        return AreaPosition.getInstance(CellPosition.getInstance(iStartRow,iStartCol),CellPosition.getInstance(iEndRow,iEndCol));
    }
    
    /**
     * ���㶯̬������һ������Ӧ��Ҫ���ӵ�ʵ�������������ڶ�̬����λָ���ؼ���ȡ��Ӧλ�õ�ֵ
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
        
        //�����ǰ��Ԫû��ֵ�������������һ����Ԫ��ȡֵ
        if(objVal == null)
            return null;
       
        if(kvo.getType() == KeyVO.TYPE_TIME)
           //��ʱ�����͹ؼ��ֵĵ���,ҪУ�����ڵĺϷ��ԣ�
            contentValue = checkKeyDateStr(objVal.toString(),kvo.getTimeProperty(),area); 
        else if(kvo.getType() == KeyVO.TYPE_REF)
            contentValue = checkRefDataStr(objVal.toString(),kvo.getRef(),area);
        else
            //��ʱ�����͹ؼ��ֵĵ���,ҪУ�����ڵĺϷ��ԣ�
            contentValue = checkKeyCharStr(kvo,objVal.toString(),area);
       
        return contentValue;

    }
    private String checkRefDataStr(String strValue,String codeId,AreaPosition area)
    {
        if(strValue != null && strValue.length() > 0)
        {
            //�Բ����͵ĵ��룬����õ���ֵ�ڻ����в��Ҳ�������˵��������������
            CodeCache codeCache = IUFOUICacheManager.getSingleton().getCodeCache();
            try{
                nc.vo.iufo.code.CodeVO codevo = codeCache.findCodeByID(codeId);
                //���صĵ�һ������CodeInfoVO.getId()Ϊ�������ڶ�����CodeInfoVO.getContent()Ϊ����
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
     * У��ʱ��ؼ��ֵ�¼��
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
                addErrDataTime(area.toString(),StringResource.getStringResource("miufo1002756"));  //"���ڲ��Ϸ�"
                return null;
            }
            UFODate ufoDate = new nc.vo.iufo.pub.date.UFODate(strValue);
            String strInputDate = ufoDate.getEndDay(timeProp).toString();
            TaskVO taskVO = IUFOUICacheManager.getSingleton().getTaskCache().getTaskVO(strtaskId);
            //���������Ч�������ж�
            if(taskVO != null){
                if(taskVO.getStartTime() != null && !taskVO.getStartTime().equals("")){
                    UFODate inputUfoDate = new UFODate(strInputDate);
                    UFODate startUfoDate = new UFODate(taskVO.getStartTime());
                    startUfoDate = startUfoDate.getEndDay(timeProp);
                    if(inputUfoDate.compareTo(startUfoDate) < 0){
                        addErrDataTime(area.toString(),StringResource.getStringResource("miufo1002757"));  //"¼�����ڲ���������Ч���޷�Χ��"
                        return null;
                    }
                }
                if(taskVO.getEndTime() != null && !taskVO.getEndTime().equals("")){
                    UFODate inputUfoDate = new UFODate(strInputDate);
                    UFODate endUfoDate = new UFODate(taskVO.getEndTime());
                    endUfoDate = endUfoDate.getEndDay(timeProp);
                    if(endUfoDate.compareTo(inputUfoDate) < 0){
                        addErrDataTime(area.toString(),StringResource.getStringResource("miufo1002758"));  //"¼�����ڲ���������Ч�ڷ�Χ��"
                        return null;
                    }
                }
            }
            strValue = strInputDate;
        }
        return strValue;
    }
    /**
     * У�鵥λ�ؼ���
     * @param kvo KeyVO
     * @param strValue String
     * @param area AreaPosition
     * @return String
     */
    private String checkKeyCharStr(KeyVO kvo,String strValue,AreaPosition area){
        if(strValue != null && strValue.length() > 0){
            UnitCache unitCache = IUFOUICacheManager.getSingleton().getUnitCache();
            //��ҪУ�鵥λ�Ϸ���
            if(kvo.getKeywordPK().equals(KeyVO.CORP_PK)){
            	UnitInfoVO unitInfo=unitCache.getUnitInfoByCode(strValue);
                if(unitInfo == null){
                    addErrDataTime(area.toString(), StringResource.getStringResource("miufo1002759"));  //"�����ڸõ�λ"
                    return null;
                }
                strValue=unitInfo.getPK();
                if(unitCache.isSubOrGrandSubUnit(strValue, userInfo.getUnitId(),m_strOrgPK) == false &&
                   strValue.equals(userInfo.getUnitId()) == false){
                    addErrDataTime(area.toString(), StringResource.getStringResource("miufo1002760"));  //"����¼���ϼ���λ������"
                    return null;
                }
            } 
            else if(kvo.getKeywordPK().equals(KeyVO.DIC_CORP_PK)){
            	UnitInfoVO unitInfo=unitCache.getUnitInfoByCode(strValue);
                if(unitInfo == null){
                    addErrDataTime(area.toString(), StringResource.getStringResource("miufo1002761"));  //"�����ڸöԷ���λ"
                    return null;
                }
                strValue=unitInfo.getPK();
            }
            else if(StringResource.getStringResource("miufo1001508").equals(kvo.getName())){  //"�к�"
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
     * ����λ�ú�ָ������͵õ�¼���ֵ
     * @param area AreaPosition
     * @param mvo MeasureTableVO
     * @return String
     */
    private String importMeasureDataByArea(AreaPosition area,MeasureVO measure){
    	Object objVal=excelCellsModel.getCellValue(area.getStart());

        //�����ǰ��Ԫû��ֵ�������������һ����Ԫ��ȡֵ
        if(objVal == null)
            return null;
        
        String content = objVal.toString();
        if(measure.getType() == MeasureVO.TYPE_NUMBER && content!=null && content.trim().length()>0){
           //����ֵ��ָ��ĵ���,���Double.parseDouble������˵�����Ͳ���
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
       
        //�����ַ���ָ�꣬�����ַ����ƱȽ��٣���ֱ�Ӵ���
        return content;
    }
   
    /**
     * �õ��������ݺͱ���ID������
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
     * д������־,��Ӧ��λ����û����Ӧ��ָ�������͵�����,������Ҳ�������
     * @param area String
     * @param repType String
     */
    private void addErrDataType(String area,String repType){
        errCount += 1;
        //"IUFO����"+repName+"��ƥ���Excel������"+sheetName+"�е���Ӧλ�� "+area+" ���������Ͳ���!��Ҫ\""+repType+"\"����"
        logFile.writer(StringResource.getStringResource("miufo1002749")+  //"IUFO����"
                       StringResource.getStringResource("miufopublic100")+repName+StringResource.getStringResource("miufo1002765")+
                       StringResource.getStringResource("miufopublic100")+sheetName+StringResource.getStringResource("miufo1002763")+" "+area+" "+
                       StringResource.getStringResource("miufo1002766")+"\""+repType+"\""+
                       StringResource.getStringResource("miufopublic263"));  //"����"
    }
    /**
     * д������־,��Ӧ��λ����û����Ӧ��ָ�������͵�����,������Ҳ������ϣ���Ҫָ�ؼ��ֵ����ڲ��Ϸ�
     * @param area String
     * @param repType String
     */
    private void addErrDataTime(String area,String errMsg)
    {
        errCount += 1;
        //"IUFO����"+repName+"��ƥ���Excel������"+sheetName+"�е���Ӧλ�� "+area+" �Ϲؼ���¼�����"+errMsg+"!"
        logFile.writer(StringResource.getStringResource("miufo1002749")+  //"IUFO����"
                       StringResource.getStringResource("miufopublic100")+repName+StringResource.getStringResource("miufo1002765")+StringResource.getStringResource("miufopublic100")+sheetName+
                       StringResource.getStringResource("miufo1002763")+" "+area+" "+
                       StringResource.getStringResource("miufo1002767")+StringResource.getStringResource("miufopublic100")+errMsg+"!");
    }

    /**
     * д������־,��Ӧ��λ����û����Ӧ��ָ�������͵�����,������Ҳ�������
     * @param area String
     */
    private void addErrData(String area)
    {
        errCount += 1;
        //"IUFO����"+repName+"��ƥ���Excel������"+sheetName+"�е���Ӧλ�� "+area+" �����ݴ�������ı�����ϵͳ�в����ڣ�"
        logFile.writer(StringResource.getStringResource("miufo1002749")+  //"IUFO����"
                       StringResource.getStringResource("miufopublic100")+repName+StringResource.getStringResource("miufo1002765")+StringResource.getStringResource("miufopublic100")+sheetName+
                       StringResource.getStringResource("miufo1002763")+" "+area+" "+
                       StringResource.getStringResource("miufo1002768"));  //"�����ݴ�������ı�����ϵͳ�в����ڣ�"
    }
    private void addErrSave()
    { 
        errCount += 1;
        //"���汨��"+repName+"("+repCode+")"+"��ָ������ʱ����"
        logFile.writer(StringResource.getStringResource("miufo1002770",new String[]{repName,repCode}));
//        +  //"���汨��"
//                       repName+"("+repCode+")"+StringResource.getStringResource("miufo1002770"));
    }
    private void addErrCommited()
    {
        errCount += 1;
        //"����"+repName+"("+repCode+")�Ѿ��ϱ����������޸ĸñ��������!"
        logFile.writer(StringResource.getStringResource("miufo1002771",new String[]{repName,repCode}));  //"�Ѿ��ϱ����������޸ĸñ��������!"
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
     * �жϱ����Ƿ��Ѿ��ϱ�������Ѿ��ϱ������ܽ��е�������
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

            //�����ϱ���������
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
          throw new CommonException("miufo10000");//δ����Ĵ���
        }
        return false;
    }
    /**
     * �����Ҫ�Ե�������ݽ���У�飬��ͨ���̳б��࣬��ʵ�ָ÷���������У��
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
