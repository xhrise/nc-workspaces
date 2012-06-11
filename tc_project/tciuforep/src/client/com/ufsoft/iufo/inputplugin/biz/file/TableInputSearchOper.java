package com.ufsoft.iufo.inputplugin.biz.file;

import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.vo.iufo.unit.UnitInfoVO;

import com.ufsoft.iufo.inputplugin.biz.DSInfoSetExt;
import com.ufsoft.iufo.inputplugin.biz.InputBizOper;
import com.ufsoft.iufo.inputplugin.biz.InputDataPlugIn;
import com.ufsoft.iufo.inputplugin.biz.InputFilePlugIn;
import com.ufsoft.iufo.inputplugin.biz.data.AreaCalExt;
import com.ufsoft.iufo.inputplugin.biz.data.CalExt;
import com.ufsoft.iufo.inputplugin.biz.data.CheckAllRepExt;
import com.ufsoft.iufo.inputplugin.biz.data.CheckRepExt;
import com.ufsoft.iufo.inputplugin.biz.data.ExportData2HtmlExt;
import com.ufsoft.iufo.inputplugin.biz.data.ImportExcelDataExt;
import com.ufsoft.iufo.inputplugin.biz.data.ImportIufoDataExt;
import com.ufsoft.iufo.inputplugin.biz.data.TraceDataExt;
import com.ufsoft.iufo.inputplugin.biz.data.TraceSubExt;
import com.ufsoft.iufo.inputplugin.hbdraft.HBDraftExt;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.LinkServletUtil;
import com.ufsoft.iuforeport.tableinput.applet.TableInputAuth;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputException;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.plugin.IExtension;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.UFOTable;

/**
 * �ۺϲ�ѯ��Applet��ServletͨѶ�Ĺ����ࡣ
 * 2007-9-20
 * @author chxw
 */
public class TableInputSearchOper extends InputBizOper{
	/**
     * �Ϸ��Ĳ�ѯ�������
     */
    private ChooseCordPanel m_chooseCordPanel = null;
    
    /**
     * ���ĵ�λ������
     */
    private ChooseRepPanel m_pnlChooseRepPanel = null;
    
    private Object[] m_oOtherParams = null;
    
    /**
     * 
     * @param ufoReport
     * @param otherParams
     */
    public TableInputSearchOper(UfoReport ufoReport, Object[] otherParams) {
        super(ufoReport);
        m_oOtherParams = otherParams;
    }
    
    private Object[] getOtherParams(){
        return m_oOtherParams;
    }
    
    /**
     * �����ۺϲ�ѯ����ֵ
     * @param transReturnObj
     * @param nMenuType
     * @return ���ظ�html����ʾ��Ϣ
     */
    protected String dealTransReturnObj(Object transReturnObj,int nMenuType) {
    	Object[] results = null;
    	if(transReturnObj instanceof Object[]){
    		results = (Object[])transReturnObj;
    	} else{
    		return null;
    	}

    	TableInputContextVO inputContextVO = (TableInputContextVO)this.getUfoReport().getContextVo();
    	Object tableInputTransObj = inputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
			
    	Object firstTransReturnObj = null;
    	if(results != null && results[0] instanceof TableInputException){
    		return super.dealTransReturnObj(transReturnObj, nMenuType);
    	} else if(nMenuType == ITableInputMenuType.MENU_TYPE_OPEN){
    		firstTransReturnObj = new Object[]{results[0],results[1]};
    		Object[] otherRetResults = (Object[])results[2];
    		if(otherRetResults != null && otherRetResults.length > 0){ 
    			String[][] reportsOfTask = (String[][])otherRetResults[3];
    			String[][] dataVers = (String[][])otherRetResults[4];
    			String[][] subDataVers = (String[][])otherRetResults[5];
    			IRepDataParam repDataParam = (IRepDataParam)otherRetResults[6];
    			updateChooseCord(reportsOfTask, dataVers, subDataVers, repDataParam.getVer(), repDataParam.getSubVer());
    			getTransObj().setRepDataParam(repDataParam);
    			//����ѡ�񱨱��ʱ���������������ĵ�λ������ѡ��
    			if(results[1] instanceof Object[] && ((Object[]) results[1])[0] instanceof CellsModel){
    				
    				String strOperUnitPK = inputTransObj.getRepDataParam().getOperUnitPK();
    				String strOperRepPK = inputTransObj.getRepDataParam().getReportPK();
    				UnitInfoVO unitInfo = GeneralQueryUtil.findUnitInfoByPK(strOperUnitPK);
    				ChooseRepPanel  chooseRepPanel = GeneralQueryUtil.getChooseRepPanel(getUfoReport());
					ChooseCordPanel chooseCordPanel = GeneralQueryUtil.getChooseCordPanel(getUfoReport());
					if(chooseRepPanel != null){
						chooseRepPanel.setSelNodeOfNavTree(strOperRepPK, (unitInfo == null)?null:unitInfo.getCode());	
					}
					if(chooseCordPanel != null){
						chooseCordPanel.setSelItemOfCordPanel((unitInfo == null)?null:unitInfo.getCode(), strOperRepPK);	
					}
    				MenuStateData menuStateData = (MenuStateData)otherRetResults[0];
    				doSetPluginState(menuStateData);
    			}
    		}
    	} else if(nMenuType == ITableInputMenuType.BIZ_TYPE_SEARCH_VERS){
    		//�����ۺϲ�ѯ��ѡ���ݰ汾�б�
    		String[][] dataVers = (String[][])results[1];
    		Object[] otherRetResults = (Object[])results[2];
    		String[][] subDataVers =null;
    		if(otherRetResults[0]!=null)
    			subDataVers= (String[][])otherRetResults[0];
    		String dataVer=null;
    		if(otherRetResults!=null&&otherRetResults.length>1)
    			dataVer= otherRetResults[1]==null?null:(String)otherRetResults[1];
    		updateChooseCord(null, dataVers, subDataVers, dataVer, null);
    	} else if(nMenuType == ITableInputMenuType.BIZ_TYPE_SEARCH_SUBVERS){
    		//�����ۺϲ�ѯ��ѡ���ݰ汾�б�
    		String[][] dataSubVers = (String[][])results[1];
    		if(dataSubVers != null && GeneralQueryUtil.getChooseCordPanel(getUfoReport()) != null){
    			GeneralQueryUtil.getChooseCordPanel(getUfoReport()).addDataSubVerItemsToList(dataSubVers);
			}
    	} else if(nMenuType == ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATA && results.length == 3){
    		firstTransReturnObj = new Object[]{results[0], results[1]};
    		Object[] otherRetResults = (Object[])results[2];
    		if(otherRetResults != null && otherRetResults.length == 7){
    			String[][] reportsOfTask = (String[][])otherRetResults[3];
    			String[][] dataVers = (String[][])otherRetResults[4];
    			String[][] subDataVers = (String[][])otherRetResults[5];
    			
    			IRepDataParam repDataParam = (IRepDataParam)otherRetResults[6];
    			getTransObj().setRepDataParam(repDataParam);
    			updateChooseCord(reportsOfTask, dataVers, subDataVers, repDataParam.getVer(), repDataParam.getSubVer());
    			
    			MenuStateData menuStateData = (MenuStateData)otherRetResults[0];
    			doSetPluginState(menuStateData);
    			
    			String strOperUnitPK = inputTransObj.getRepDataParam().getOperUnitPK();
				String strOperRepPK = inputTransObj.getRepDataParam().getReportPK();
				UnitInfoVO unitInfo = GeneralQueryUtil.findUnitInfoByPK(strOperUnitPK);
				if(unitInfo != null){
					GeneralQueryUtil.getChooseCordPanel(getUfoReport()).setSelItemOfCordPanel(unitInfo.getCode(), strOperRepPK);
				}
    		}
    		//���û����¼��ı�������(����¼��Ĺؼ��ֲ�ѯ)������ʾһ���ձ�
    		if(results[1] != null && results[1] instanceof Boolean && !((Boolean)results[1]).booleanValue()){
    			createEmptyTable(getUfoReport());
    			return null;
    		}
    	} else if(nMenuType == ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATASUBMIT && results.length == 3){
    		firstTransReturnObj = new Object[]{results[0], results[1]};
    		Object[] otherRetResults = (Object[])results[2];
    		if(otherRetResults != null && otherRetResults.length == 7){
    			IRepDataParam repDataParam = (IRepDataParam)otherRetResults[6];
    			getTransObj().setRepDataParam(repDataParam);
    			
    			MenuStateData menuStateData = (MenuStateData)otherRetResults[0];
    			doSetPluginState(menuStateData);

    			String strOperUnitPK = inputTransObj.getRepDataParam().getOperUnitPK();
				String strOperRepPK = inputTransObj.getRepDataParam().getReportPK();
				UnitInfoVO unitInfo = GeneralQueryUtil.findUnitInfoByPK(strOperUnitPK);
				if(unitInfo != null){
					GeneralQueryUtil.getChooseCordPanel(getUfoReport()).setSelItemOfCordPanel(unitInfo.getCode(), strOperRepPK);
				}
    		}
    		//���û����¼��ı�������(����¼��Ĺؼ��ֲ�ѯ)������ʾһ���ձ�
    		if(results[1] != null && results[1] instanceof Boolean && !((Boolean)results[1]).booleanValue()){
    			createEmptyTable(getUfoReport());
    			return null;
    		}
    	}  else if(nMenuType == ITableInputMenuType.BIZ_TYPE_SEARCH_REPS){
    		if(results.length > 2 && results[1] != null && results[1] instanceof Object[][]){
    			String[][] reportsOfTask = (String[][])results[1];
    			updateChooseCord(reportsOfTask, null, null, null, null);
    		}
    	} else{
    		firstTransReturnObj = transReturnObj;
    	}    

    	
    	if(results.length >= 2 && results[1] instanceof Object[]){
    		return super.dealTransReturnObj(firstTransReturnObj, nMenuType);
    	} else{
    		return null;
    	}

    }

    /**
	 * �����ۺϲ�ѯ����ѯ����
	 * @param reports
	 * @param dataVers
	 * @param subDataVers
	 */
	private void updateChooseCord(String[][] reports, String[][] dataVers, String[][] subDataVers, String ver, String subVer) {
		ChooseCordPanel chooseCordPanel = GeneralQueryUtil.getChooseCordPanel(getUfoReport());
		if(chooseCordPanel != null){
			//�����ۺϲ�ѯ��ѡ�����б�
			if(reports != null){
				chooseCordPanel.addRepItemsToList(reports);
			}
			
			//�����ۺϲ�ѯ��ѡ���ݰ汾�б�
			chooseCordPanel.addDataVerItemsToList(dataVers);
			//�����ۺϲ�ѯ��ѡ�����ݰ汾�б�
			chooseCordPanel.addDataSubVerItemsToList(subDataVers);
			//�����ۺϲ�ѯ�汾���Ӱ汾ѡ����
			chooseCordPanel.setDefaultDataVer(ver);
			chooseCordPanel.setDefaultDataSubVer(subVer);
		}
	}

	/**
	 * ����һ���ձ�(δ��ѯ����������ʱ����ʾ�ձ�)
	 */
	public static void createEmptyTable(UfoReport ufoReport) {
		UFOTable table = UFOTable.createFiniteTable(50,20);
		CellsModel cellsModel = table.getCellsModel();
		cellsModel.setDirty(false);
		ufoReport.getTable().setCurCellsModel(cellsModel);
		ufoReport.setReadOnly(true, new TableInputAuth(ufoReport));
	}

	/**
	 * ���²��״̬
	 * @param menuStateData
	 */
	private void doSetPluginState(MenuStateData menuStateData) {
		InputFilePlugIn inputFilePlugIn = getInputFilePlugIn();
		doSetFileMenuState(inputFilePlugIn, menuStateData);

		InputDataPlugIn inputDataPlugIn = getInputDataPlugIn();
		doSetDataMenuState(inputDataPlugIn, menuStateData);
	}
    
    /**
     * ִ������Servlet�Ĳ���
     * @param nMenuType
     * @return
     */
    protected  Object doLinkServletTask(int nMenuType){
        return linkServletTask(nMenuType,getTransObj(),getOtherParams());
    }
    
    /**
     * �ۺϲ�ѯ����Ҫ����һЩ���������ҵ������
     * @param nMenuType
     * @param inputTransObj
     * @param oOtherParams
     * @return
     */
    public static Object linkServletTask(int nMenuType, TableInputTransObj inputTransObj, Object[] oOtherParams) {
    	Object returnObj = null;
    	switch(nMenuType){
    		//��ѯ�ɲ�������
    		case ITableInputMenuType.MENU_TYPE_OPEN:
	    	//��ѯ�ɲ�������
	    	case ITableInputMenuType.BIZ_TYPE_SEARCH_REPS:
	    	//��ѯ�ɲ�������汾
	    	case ITableInputMenuType.BIZ_TYPE_SEARCH_VERS:
	    	//��ѯ�ɲ����ӱ���汾
	    	case ITableInputMenuType.BIZ_TYPE_SEARCH_SUBVERS:
	    	//��ѯ��������(����������λ���л�)
	    	case ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATASUBMIT:	
	    	//��ѯ��������(�ύ��ť)
	    	case ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATA:
	    		Object[] results = LinkServletUtil.linkTableInputOperServlet(nMenuType, inputTransObj, null, oOtherParams); 
	    		returnObj = results; 
	    		break;                
	    	default:
	    		break;
    	}

    	return returnObj;
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
//  	ChooseRepExt2 chooseRepExt = (ChooseRepExt2)extensions[nIndex];
//  	chooseRepExt.setCanChooseRep(menuStateData.isCanChooseRep());
//  	nIndex++;
    	//����
    	SaveRepDataExt saveRepDataExt = (SaveRepDataExt)extensions[nIndex];
    	saveRepDataExt.setCommeted(menuStateData.isCommited());
    	saveRepDataExt.setIsRepOpened(menuStateData.isRepOpened());
    	saveRepDataExt.setRepCanModify(menuStateData.isRepCanModify());
    	nIndex++;  
    	//�л��ؼ���
//    	ChangeKeywordsExt changeKeywordsExt = (ChangeKeywordsExt)extensions[nIndex];
//    	changeKeywordsExt.setCanChangeKeywords(menuStateData.isCanChangeKeywords());
//    	nIndex++;     
    	//��������Դ
    	DSInfoSetExt dsInfoSetExt = (DSInfoSetExt)extensions[nIndex];
    	dsInfoSetExt.setCanSetDSInfo(menuStateData.isCanSetDSInfo());
    	nIndex++;     

    	//��ӡ����ز˵� 

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
    	
    	if (GeneralQueryUtil.isGeneralQuery(inputDataPlugIn.getReport().getContext())){
	    	TraceSubExt traceSubExt=(TraceSubExt)extensions[nIndex];
	    	traceSubExt.setIsRepOpened(menuStateData.isRepOpened());
	    	traceSubExt.setCanTrace(menuStateData.isHasUnitKey() && menuStateData.getDataVer()==0 && GeneralQueryUtil.isGeneralQuery(inputDataPlugIn.getReport().getContext()));
	    	nIndex++;
    	}

    	//���
    	CheckRepExt checkRepExt = (CheckRepExt)extensions[nIndex];
    	checkRepExt.setIsHasRepCheckFormula(menuStateData.isHasRepCheckFormula());
    	checkRepExt.setIsRepOpened(menuStateData.isRepOpened());
    	nIndex++;       
    	//ȫ�����(������)
    	CheckAllRepExt checkAllRepExt = (CheckAllRepExt)extensions[nIndex];
    	checkAllRepExt.setIsHasTaskCheckFormula(menuStateData.isHasTaskCheckFormula());
    	nIndex++;     

//  	//�����¼�(?)
//  	nIndex++;             
//  	//�鿴�¼�����(?)
//  	nIndex++;             
//  	//�鿴��Դ(?)   
//  	nIndex++;     
    	//�ϲ��׸�
    	if (GeneralQueryUtil.isGeneralQuery(inputDataPlugIn.getReport().getContext())){
    	HBDraftExt hbDraftExt = (HBDraftExt)extensions[nIndex];
    	hbDraftExt.setHasHBDraft(menuStateData.getDataVer()==HBBBSysParaUtil.VER_HBBB);
    	nIndex++; 
    	}

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
//  	//����->Ufo����
//  	ImportUfoDataExt importUfoDataExt = (ImportUfoDataExt)extensions[nIndex]; 
//  	importUfoDataExt.setIsRepOpened(menuStateData.isRepOpened());
//  	importUfoDataExt.setIsCanImportUfoData(menuStateData.isCanImportU8UFOData());
//  	importUfoDataExt.setHasRepCanModify(menuStateData.isHasRepCanModify());
//  	nIndex++;     
    	//����->Excel��ʽ
//    	ExportData2ExcelExt exportData2ExcelExt = (ExportData2ExcelExt)extensions[nIndex];
//    	exportData2ExcelExt.setIsRepOpened(menuStateData.isRepOpened());
//    	nIndex++;     
    	//����->html��ʽ
    	ExportData2HtmlExt exportData2HtmlExt = (ExportData2HtmlExt)extensions[nIndex];
    	exportData2HtmlExt.setIsRepOpened(menuStateData.isRepOpened());
    	nIndex++;     

    }
    
}
