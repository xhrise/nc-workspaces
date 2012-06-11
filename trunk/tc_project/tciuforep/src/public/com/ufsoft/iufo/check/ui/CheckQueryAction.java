/**
 * CheckQueryAction.java  5.0 
 * 
 * WebDeveloper�Զ�����.
 * 2006-01-04
 */
package com.ufsoft.iufo.check.ui;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.RepFormatModelCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.pub.UfoPublic;
import nc.ui.iufo.server.param.ServerParamMngAction;
import nc.ui.iufo.task.TaskBO_Client;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.RepFormatModel;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.table.IWebTableModel;
import com.ufida.web.comp.table.WebTableModel;
import com.ufida.web.comp.tree.IWebTreeModel;
import com.ufida.web.comp.tree.WebTreeModel;
import com.ufida.web.comp.tree.WebTreeNode;
import com.ufida.web.window.WebStatusBar;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.check.vo.TaskCheckVO;
import com.ufsoft.iufo.fmtplugin.formula.FormulaModel;
import com.ufsoft.iufo.fmtplugin.formula.RepCheckVO;
import com.ufsoft.iufo.fmtplugin.formula.SimpleCheckFmlVO;
import com.ufsoft.iufo.querycond.ui.QueryCondBO_Client;
import com.ufsoft.iufo.querycond.ui.QueryConditionUIUtil;
import com.ufsoft.iufo.querycond.ui.WithUnitTreeMultiFrameAction;
import com.ufsoft.iufo.querycond.vo.IQueryCondConstant;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.web.IUFOAction;
import com.ufsoft.script.UfoCmdProxy;
import com.ufsoft.script.cmdcontrol.CmdInterpreter;
import com.ufsoft.script.cmdcontrol.ConditionOfRun;
import com.ufsoft.script.extfunc.LoginInfoFuncDriver;
import com.ufsoft.script.extfunc.MeasFuncDriver;
import com.ufsoft.script.extfunc.OtherFuncDriver;
import com.ufsoft.script.spreadsheet.UfoCalcEnv;

/**
 * ��������������
 * zyjun
 * 2006-01-04
 */
public class CheckQueryAction extends WithUnitTreeMultiFrameAction {
	//��ʾ����λ�򰴹�ʽ�鿴��˽����sessionid
	public final static String SESSION_CHECK_QUEYR_BYUNIT="checkquery_byrep";
	
	//�洢��ѯ������sessionid
	public final static String SESSION_CHECK_QUERY_COND="checkquery_cond";
	
	//�洢�������������sessionid
	public final static String SESSION_CHECK_QUERY_RECHECKCOND="checkquery_recheckcond";
	
	//�洢����Excel�Ķ����sessionid
	public final static String SESSION_CHECK_QUERY_EXPORT="checkquery_export";
	
	//���б�ʾ�Ǳ����˹�ʽ��id��־
	public final static String TASKCHECK_FLAG="@";
	
//	���б�ʾ�Ǳ�����˹�ʽ��id��־
	public final static String REPCHECK_FLAG="$";
	
    /**
     * <MethodDescription>
     * zyjun
     * 2006-01-04
     */
    public ActionForward execute(ActionForm actionForm) throws WebException {
    	CheckQueryMngForm form=(CheckQueryMngForm)actionForm;
    	form.setByUnit(isViewByUnit(this));
    	removeSessionObject(SESSION_CHECK_QUERY_EXPORT);
    	
    	CheckQueryCondVO queryCond=(CheckQueryCondVO)getSessionObject(SESSION_CHECK_QUERY_COND+getCurTaskId());
		if (queryCond==null){
			TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
			KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
			TaskVO task=taskCache.getTaskVO(getCurTaskId());
			KeyGroupVO keyGroup=kgCache.getByPK(task.getKeyGroupId());
			KeyVO[] keys=QueryConditionUIUtil.filterTaskKeys(keyGroup.getKeys());
			queryCond=CheckQueryCondAction.initCheckQueryCond(this);
			queryCond.setInited(true);
			if (keys!=null && keys.length>0 && (queryCond.getKeyConds()==null || queryCond.getKeyConds().length<=0)){
				queryCond=null;
			}else{
				addSessionObject(SESSION_CHECK_QUERY_COND+getCurTaskId(),queryCond);
			}
		}
        
        return new ActionForward(com.ufsoft.iufo.check.ui.CheckQueryMngUI.class.getName());
    }
    
    public ActionForward doViewByUnit(ActionForm form) throws WebException{
		addSessionObject(SESSION_CHECK_QUEYR_BYUNIT,Boolean.TRUE);
		return new CloseForward("opener.location.reload();window.close();");
    }
    
    public ActionForward doViewByFormula(ActionForm form) throws WebException{
		addSessionObject(SESSION_CHECK_QUEYR_BYUNIT,Boolean.FALSE);
		return new CloseForward("opener.location.reload();window.close();");
    }
    
    public IWebTableModel getTableModel(){
    	try{
    		boolean bViewByUnit=isViewByUnit(this);
    		String strOrgPK=getCurOrgPK();
    		CheckQueryCondVO queryCond=(CheckQueryCondVO)getSessionObject(SESSION_CHECK_QUERY_COND+getCurTaskId());
    		CheckQueryExport export=new CheckQueryExport(getCurTaskId(),bViewByUnit,strOrgPK,getTreeSelectedID(),queryCond,getCurUserInfo());
    		addSessionObject(SESSION_CHECK_QUERY_EXPORT,export);
    		return geneTableModel(getCurTaskId(),bViewByUnit,strOrgPK,getTreeSelectedID(),queryCond,getCurUserInfo(),false);
    	}catch(Exception e){
    		AppDebug.debug(e);
    		return null;
    	}
    }
    
    /**
     * �õ����ģ��
     * @return
     * @throws Exception
     * @i18n miufo00831=��
     * @i18n miufo1001376=�汾
     * @i18n uiufofurl0376=�ϲ�����
     * @i18n uiufofurl0375=���𱨱�
     * @i18n uiufohbfunc0033=�汾��
     */
	public static IWebTableModel geneTableModel(String strTaskID,boolean bViewByUnit,String strOrgPK,String strTreeValue,CheckQueryCondVO queryCond,UserInfoVO userInfo,boolean bForExcel) throws Exception{
		UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
		TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
		KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();

		KeyGroupVO keyGroup=kgCache.getByPK(taskCache.getTaskVO(strTaskID).getKeyGroupId());
		
		//���ģ�ͳ�ʼ��
		WebTableModel model=new WebTableModel();
		model.setSelectMode(WebTableModel.NO_SELECTION);
		model.setColumns(new String[]{StringResource.getStringResource("miufoinputnew00027")});
		
		//���δѡ�����ڵ���ѯ����Ϊ�գ��򷵻ؿյı��
		if (strTreeValue==null || queryCond==null)
			return model;
		
		//����querycond����aloneid
		queryCond=(CheckQueryCondVO)queryCond.clone();
		if (bViewByUnit){
			//����λ�鿴ʱ������Ҫ����һ����λ��aloneid
			queryCond.setUnitStyle(IQueryCondConstant.UNIT_TYPE_TREE);
			queryCond.setUnitIds(new String[]{strTreeValue});
		}
		
		CheckQueryCondVO findCond=(CheckQueryCondVO)queryCond.clone();
		findCond.setRepIDs(taskCache.getReportIdsByTaskId(strTaskID));
		String[] strAloneIDs=QueryCondBO_Client.findAloneIDsByQueryCond(findCond,bViewByUnit?strTreeValue:userInfo.getUnitId(),strOrgPK);
		
		//δ�ҵ��������ݣ�ֱ�ӷ���
		if (strAloneIDs==null || strAloneIDs.length<=0)
			return model;
			
		//����MeasurePubDataVO���飬����е�λ�ؼ��֣����ݵ�λ����֯���ϵ�λ�ý�������
		boolean bHasUnitKey=keyGroup.getKeyByKeyPk(KeyVO.CORP_PK)!=null;
		MeasurePubDataVO[] pubDatas=MeasurePubDataBO_Client.findByAloneIDs(strAloneIDs);
		if (bHasUnitKey || queryCond.getDataVersion()==IQueryCondConstant.VER_SEPHBBB)
			Arrays.sort(pubDatas,new MeasurePubDataComparator(strOrgPK));

		//��Ҫ��ѯ�ı���PK����
		String[] strRepIDs=null;
		
		//���ҳ��ı����˺ͱ�����˽��
		CheckResultVO[] taskResults=null;
		CheckResultVO[] repResults=null;
		
		//��������ʽ��ʱ����Ҫ������ʽ����
		String strFormulaContent=null;
		
		int iCheckState=queryCond.getResultType()==CheckQueryCondVO.CHECK_RESULT_ALL?CheckResultVO.CHECK_ALLSTATE:CheckResultVO.NOPASS;
		
		//����λ�鿴ʱ�����ݲ�ѯ�����ж����������ͣ�������˽��������ΧΪ��ѯ�����е����б���
		if (bViewByUnit){
			if (queryCond.getCheckType()==CheckQueryCondVO.CHECK_TYPE_ALL || queryCond.getCheckType()==CheckQueryCondVO.CHECK_TYPE_TASK)
				taskResults=CheckResultBO_Client.loadTaskCheckResults(true,strTaskID,iCheckState,strAloneIDs,null);
			
			if (queryCond.getCheckType()==CheckQueryCondVO.CHECK_TYPE_ALL || queryCond.getCheckType()==CheckQueryCondVO.CHECK_TYPE_REP)
				repResults=CheckResultBO_Client.loadRepCheckResults(true,iCheckState,strAloneIDs, queryCond.getRepIDs(),null);
			
			strRepIDs=queryCond.getRepIDs();
		}else{
			//����ѡ�е��Ǳ����˹�ʽ�������˹�ʽ���в�ѯ
			if (strTreeValue!=null && strTreeValue.trim().length()>0){
				if (strTreeValue.startsWith(TASKCHECK_FLAG)){
					String strFormulaID=strTreeValue.substring(1);
					if (strFormulaID.trim().length()<=0)
						strFormulaID=null;
					else
						strFormulaContent=taskCache.getTaskCheckVO(strTaskID,strFormulaID).getFormula();
					
					taskResults=CheckResultBO_Client.loadTaskCheckResults(true,strTaskID,iCheckState,strAloneIDs,strFormulaID);
				}else{
					int iIndex=strTreeValue.indexOf(REPCHECK_FLAG);
					String strRepID=strTreeValue.substring(0,iIndex);
					String strFormulaID=strTreeValue.substring(iIndex+1);
					if (strFormulaID.trim().length()<=0)
						strFormulaID=null;
					else //���ݹ�ʽid���ӱ���ģ�͵ļ���˹�ʽ�븴����˹�ʽ�У��ҵ���id��Ӧ�Ĺ�ʽ����
						strFormulaContent=loadFormulaContent(strRepID, strFormulaID);
						
					repResults=CheckResultBO_Client.loadRepCheckResults(true,iCheckState,strAloneIDs,new String[]{strRepID},strFormulaID);
					strRepIDs=new String[]{strRepID};
				}			
			}
		}
		
		//�����Ҫ��ʾ��ʽ���ݣ��ڱ��ĵ�һ����ʾ��ʽ����
		if (strFormulaContent!=null && strFormulaContent.trim().length()>0){
			WebLabel label=new WebLabel(StringResource.getStringResource("miufo1002197")+StringResource.getStringResource("miufo00831")+toUserDefForm(strFormulaContent,strTaskID).replaceAll("<","&lt;").replaceAll(">", "&gt;"));
			label.setStyle("color:#00000ff;font-weight:bolder;");
			model.addRow(new Object[]{"",label});
		}
		
		//��MeasurePubDataVO����˹�ʽ��ѭ��
		if (strRepIDs!=null && strRepIDs.length>0){
			for (int i=0;i<pubDatas.length;i++){
				//�ҵ�����aloneid��Ӧ�ı�䡢������˽��
				CheckResultVO[] oneTaskResults=findResultsByAloneID(pubDatas[i].getAloneID(), strRepIDs, taskResults);
				CheckResultVO[] oneRepResults=findResultsByAloneID(pubDatas[i].getAloneID(), strRepIDs, repResults);;
				
				//���û�ж�Ӧ�Ľ����ֱ�ӷ���
				if (oneTaskResults.length<=0 && oneRepResults.length<=0)
					continue;
				
				//�е�λ�ؼ����֣��ҷǰ���λ����ʱ����Ҫ��ʾ��λ��Ϣ
				Object[] unitRowDatas=null;
							
				if (!bViewByUnit && bHasUnitKey){
					UnitInfoVO unitInfo=unitCache.getUnitInfoByPK(pubDatas[i].getUnitPK());
					String strLabel=StringResource.getStringResource("miufopublic125")+StringResource.getStringResource("miufo00831")+unitInfo.getNameWithCode();
					if (queryCond.getDataVersion()==IQueryCondConstant.VER_SEPHBBB)
						strLabel+="&nbsp&nbsp&nbsp&nbsp&nbsp"+StringResource.getStringResource("miufo1001376")+StringResource.getStringResource("miufo00831") + (UfoPublic.isSpecificVer(pubDatas[i].getVer(),HBBBSysParaUtil.VER_HBBB)?StringResource.getStringResource("uiufofurl0376"):StringResource.getStringResource("uiufofurl0375"));
					WebLabel label=new WebLabel(strLabel);
					label.setStyle("color:#000000;font-weight:bolder;");
					unitRowDatas=new Object[]{"",label};
				}else if (queryCond.getDataVersion()==IQueryCondConstant.VER_SEPHBBB){
					WebLabel label=new WebLabel(StringResource.getStringResource("uiufohbfunc0033")+(UfoPublic.isSpecificVer(pubDatas[i].getVer(),HBBBSysParaUtil.VER_HBBB)?StringResource.getStringResource("uiufofurl0376"):StringResource.getStringResource("uiufofurl0375")));
					label.setStyle("color:#000000;font-weight:bolder;");
					unitRowDatas=new Object[]{"",label};
				}
	
				unitRowDatas=addOneTypResultToTable(oneTaskResults,model,unitRowDatas,bViewByUnit,bForExcel);
				unitRowDatas=addOneTypResultToTable(oneRepResults,model,unitRowDatas,bViewByUnit,bForExcel);
			}
		}
		
		return model;
	}
	
	/**
	 * ����˹�ʽת�����û��ɼ��Ĺ�ʽ
	 * @param strFormula
	 * @return
	 */
	private static String toUserDefForm(String strFormula,String strTaskId){
		if (strFormula==null || strFormula.length()<=0)
			return "";
		
		UfoCalcEnv env=new UfoCalcEnv(strTaskId,null,false,null);
		ConditionOfRun conditionRun=null;
		try {
			conditionRun = new ConditionOfRun(new UfoCmdProxy(env));
			conditionRun.getCalEnv().loadFuncListInst().registerExtFuncs(new MeasFuncDriver(env));
			conditionRun.getCalEnv().registerFuncDriver(new LoginInfoFuncDriver());
			conditionRun.getCalEnv().registerFuncDriver(new OtherFuncDriver());
			
			 CmdInterpreter cmdIt = new CmdInterpreter(strFormula, conditionRun);
             cmdIt.getCtrlCmd().checkSyntax(conditionRun, false);
             return cmdIt.getCtrlCmd().toUserDefString(conditionRun.getCalEnv());
		} catch (Exception e1) {
			AppDebug.debug(e1);
			return strFormula;
		}
	}
	
	/**
	 * ��һ����˽����Ϣ��ӵ��б���
	 * @param results
	 * @param model
	 * @param unitRowDatas
	 * @param bViewByUnit
	 * @return
	 * @i18n miufo00831=��
	 */
	private static Object[] addOneTypResultToTable(CheckResultVO[] results,WebTableModel model,Object[] unitRowDatas,boolean bViewByUnit,boolean bForExcel){
		if (results==null)
			return unitRowDatas;
		
		ReportCache repCache=IUFOUICacheManager.getSingleton().getReportCache();
		
		for (int j=0;j<results.length;j++){					
			Object[][] rowDatas=CheckResultUIUtil.getResultRowDatas(results[j].getAllNote(),bForExcel);
			if (rowDatas!=null && rowDatas.length>0){						
				//����ӽ����Ϣǰ���ȼ�һ�пո�
				if (model.getRowCount()>0)
					model.addRow(new Object[]{"",""});
				
				//�����λ��Ϣ��δ��ӣ���ӵ�λ��
				if (unitRowDatas!=null){
					model.addRow(unitRowDatas);
					unitRowDatas=null;
				}
					
				//����ǰ���λ�鿴����Ҫ��ʾ�����˻򱨱�����
				if (bViewByUnit){
					WebLabel label=null;
					if (results[j].getRepId()!=null && results[j].getRepId().trim().length()>0){
						ReportVO report=repCache.getByPK(results[j].getRepId());
						label=new WebLabel(StringResource.getStringResource("uiufofurl0425")+StringResource.getStringResource("miufo00831")+report.getNameWithCode());
					}else{
						label=new WebLabel(StringResource.getStringResource("uiufofurl0142")+StringResource.getStringResource("miufo00831"));
					}
					label.setStyle("color:#000000;font-weight:bolder;");
					model.addRow(new Object[]{"",label});
				}
				
				//��������ϸ���
				for (int k=0;k<rowDatas.length;k++){
					//ȥ���հ���
					if ((rowDatas[k][1] instanceof String && ((String)rowDatas[k][1]).trim().length()<=0)
						|| (rowDatas[k][1] instanceof WebLabel && (((WebLabel)rowDatas[k][1]).getValue()==null || ((WebLabel)rowDatas[k][1]).getValue().trim().length()<=0 || ((WebLabel)rowDatas[k][1]).getValue().equals("&nbsp;"))))
						continue;
					model.addRow(rowDatas[k]);
				}
			}
		}
		
		return unitRowDatas;
	}
	
	/**
	 * ���ݹ�ʽid���ӱ���ģ�͵ļ���˹�ʽ�븴����˹�ʽ�У��ҵ���id��Ӧ�Ĺ�ʽ����
	 * @param strRepID
	 * @param strFormulaID
	 * @return
	 */
	private static String loadFormulaContent(String strRepID,String strFormulaID){
		RepFormatModel formatModel=IUFOUICacheManager.getSingleton().getRepFormatCache().getFormatByPk(strRepID);
		
		FormulaModel formulaModel=FormulaModel.getInstance(formatModel.getFormatModel());
		Vector vSimpleCheck=formulaModel.getSimpleCheckFml();
		Vector vComplexCheck=formulaModel.getComplexCheckFml();
		for (int i =0;vSimpleCheck!=null && i<vSimpleCheck.size();i++){
			SimpleCheckFmlVO check=(SimpleCheckFmlVO)vSimpleCheck.get(i);
			if (check.getID().equals(strFormulaID)){
				return check.getCompleteContent();
			}
		}
		
		if (vComplexCheck!=null){
			for (int i =0;i<vComplexCheck.size();i++){
				RepCheckVO check=(RepCheckVO)vComplexCheck.get(i);
				if (check.getID().equals(strFormulaID)){
					return check.getFormula();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * ��һ����˽���У��ҵ�һ��aloneid��Ӧ����˽��
	 * @param strAloneID
	 * @param strRepIDs
	 * @param results
	 * @return
	 */
	private static CheckResultVO[] findResultsByAloneID(String strAloneID,String[] strRepIDs,CheckResultVO[] results){
		List<CheckResultVO> vRetResult=new ArrayList<CheckResultVO>();
		
		if (results!=null){
			for (int i=0;i<results.length;i++){
				if (results[i]!=null && strAloneID.equals(results[i].getAloneId())){
					vRetResult.add(results[i]);
				}
			}
		}
		
		//����˽������ѯ�����еı����˳���������
		CheckResultVO[] retResults=vRetResult.toArray(new CheckResultVO[0]);
		if (strRepIDs!=null && strRepIDs.length>1 && retResults.length>1 && retResults[0].getRepId()!=null && retResults[0].getRepId().trim().length()>0){
			Arrays.sort(retResults,new CheckResultComparator(strRepIDs));
		}
		
		return retResults;
	}
	
	/**
	 * ����˽��������˳������������
	 * @author weixl
	 *
	 */
	private static class CheckResultComparator implements Comparator<CheckResultVO>{
		Vector<String> vRepID=null;
		public CheckResultComparator(String[] strRepIDs){
			vRepID=new Vector<String>(Arrays.asList(strRepIDs));
		}
		
		public int compare(CheckResultVO o1, CheckResultVO o2) {
			int iIndex1=vRepID.indexOf(o1.getRepId());
			int iIndex2=vRepID.indexOf(o2.getRepId());
			return iIndex1-iIndex2;
		}
	}
	
	/**
	 * ��MeasurePubDataVO����λ�����еļ��ν����������
	 * @author weixl
	 *
	 */
	private static class MeasurePubDataComparator implements Comparator<MeasurePubDataVO>{
		private String m_strOrgPK;
		private UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
		
		public MeasurePubDataComparator(String strOrgPK){
			m_strOrgPK=strOrgPK;
		}
		
		public int compare(MeasurePubDataVO o1, MeasurePubDataVO o2) {
			return compareUnit(o1,o2);
		}
		
		private int compareUnit(MeasurePubDataVO o1,MeasurePubDataVO o2){
			UnitInfoVO unitInfo1=unitCache.getUnitInfoByPK(o1.getUnitPK());
			UnitInfoVO unitInfo2=unitCache.getUnitInfoByPK(o2.getUnitPK());
			if (unitInfo1!=null && unitInfo2!=null){
				String strLevelCode1=unitInfo1.getPropValue(m_strOrgPK);
				String strLevelCode2=unitInfo2.getPropValue(m_strOrgPK); 
				int iComp=strLevelCode1.length()-strLevelCode2.length();
				if (iComp!=0)
					return iComp;
				
				iComp=strLevelCode1.compareTo(strLevelCode2);
				if (iComp!=0)
					return iComp;
			}
			
			return o1.getVer()-o2.getVer();
		}
	}

	/**
	 * ������ģ��
	 */
	public IWebTreeModel getTreeModel() {
		//������ص�λ����ֱ�ӵ����ϼ���ķ���
		if (isViewByUnit(this))
			return super.getTreeModel();
		else
			return getTaskRepTreeModel();
	}
	
    /**
     * ���ּ�����
     */
    public WebTreeNode[] treeGradeLoad(String parentNodeId){
		if (isViewByUnit(this)){
			return super.treeGradeLoad(parentNodeId);
		}else{
			return taskRepTreeGradeLoad(parentNodeId);
		}
    }

	/**
	 * ����״̬���ķ���
	 */
	public WebStatusBar getStatusBar(ActionForm actionForm) {
		return ServerParamMngAction.getStatusBar(this,true,false);
	}
	
	private WebTreeNode[] taskRepTreeGradeLoad(String parentNodeId){
		TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
		RepFormatModelCache formatCache=IUFOUICacheManager.getSingleton().getRepFormatCache();
		String strTaskID=getCurTaskId();
		CheckQueryCondVO queryCond=(CheckQueryCondVO)getSessionObject(SESSION_CHECK_QUERY_COND+getCurTaskId());

		Vector<WebTreeNode> vNode=new Vector<WebTreeNode>();
		
		if (parentNodeId.equals(TASKCHECK_FLAG)){
			Set<String> vFormulaPK=null;
			if (queryCond!=null && queryCond.getResultType()==CheckQueryCondVO.CHECK_RESULT_NOPASS){
				CheckQueryCondVO findCond=(CheckQueryCondVO)queryCond.clone();
				findCond.setRepIDs(taskCache.getReportIdsByTaskId(getCurTaskId()));
				try{
					String[] strFormulaPKs=QueryCondBO_Client.findCheckPKByCond(findCond, getCurUserInfo().getUnitId(), getCurOrgPK(), strTaskID,null);
					vFormulaPK=new HashSet<String>(Arrays.asList(strFormulaPKs));
				}catch(Exception e){
					AppDebug.debug(e);
				}
			}
			Object[] objTaskChecks=taskCache.getData(TaskCache.TYPE_TASKCHECK,strTaskID);
			for (int i=0;i<objTaskChecks.length;i++){
				if (objTaskChecks[i]==null || (vFormulaPK!=null && !vFormulaPK.contains(((TaskCheckVO)objTaskChecks[i]).getId())))
					continue;
				
				TaskCheckVO taskCheck=(TaskCheckVO)objTaskChecks[i];
				WebTreeNode subNode=new WebTreeNode(TASKCHECK_FLAG+taskCheck.getId(),parentNodeId,taskCheck.getName());
				subNode.setGradeLoadLeaf(true);
				vNode.add(subNode);
			}
		}else if (parentNodeId.endsWith(REPCHECK_FLAG)){
			String strRepID=parentNodeId.substring(0,parentNodeId.length()-REPCHECK_FLAG.length());
			RepFormatModel formatModel=formatCache.getFormatByPk(strRepID);
			if (formatModel!=null && formatModel.getFormatModel()!=null){
				Set<String> vFormulaPK=null;
				if (queryCond!=null && queryCond.getResultType()==CheckQueryCondVO.CHECK_RESULT_NOPASS){
					CheckQueryCondVO findCond=(CheckQueryCondVO)queryCond.clone();
					findCond.setRepIDs(taskCache.getReportIdsByTaskId(getCurTaskId()));
					try{
						String[] strFormulaPKs=QueryCondBO_Client.findCheckPKByCond(findCond, getCurUserInfo().getUnitId(), getCurOrgPK(),null,strRepID);
						vFormulaPK=new HashSet<String>(Arrays.asList(strFormulaPKs));
					}catch(Exception e){
						AppDebug.debug(e);
					}
				}
				
				//���ؼ򵥱�����˹�ʽ
				FormulaModel formulaModel=FormulaModel.getInstance(formatModel.getFormatModel());
				Vector vSimpleCheck=formulaModel.getSimpleCheckFml();
				if (vSimpleCheck!=null){
					for (int j=0;j<vSimpleCheck.size();j++){
						SimpleCheckFmlVO simpleCheck=(SimpleCheckFmlVO)vSimpleCheck.get(j);
						if (simpleCheck==null || (vFormulaPK!=null && !vFormulaPK.contains(simpleCheck.getID())))
							continue;
						
						WebTreeNode subNode=new WebTreeNode(strRepID+REPCHECK_FLAG+simpleCheck.getID(),parentNodeId,simpleCheck.getFmlName());
						subNode.setGradeLoadLeaf(true);
						vNode.add(subNode);
					}
				}
				
				//���ر��ڸ�����˹�ʽ
				Vector vComplexCheck=formulaModel.getComplexCheckFml();
				if (vSimpleCheck!=null){
					for (int j=0;j<vComplexCheck.size();j++){
						RepCheckVO complexCheck=(RepCheckVO)vComplexCheck.get(j);
						if (complexCheck==null || (vFormulaPK!=null &&!vFormulaPK.contains(complexCheck.getID())))
							continue;
						
						WebTreeNode subNode=new WebTreeNode(strRepID+REPCHECK_FLAG+complexCheck.getID(),parentNodeId,complexCheck.getName());
						subNode.setGradeLoadLeaf(true);
						vNode.add(subNode);
					}
				}
			}
		}
		
		return vNode.toArray(new WebTreeNode[0]);
	}
	
	/**
	 * ������˹�ʽģ��
	 * @return
	 */
	private IWebTreeModel getTaskRepTreeModel(){
		TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
		ReportCache repCache=IUFOUICacheManager.getSingleton().getReportCache();
		
		CheckQueryCondVO queryCond=(CheckQueryCondVO)getSessionObject(SESSION_CHECK_QUERY_COND+getCurTaskId());
		
		//���ڵ��ʾ���б�䡢���ڹ�ʽ������ѡ��
		Vector<WebTreeNode> vNode=new Vector<WebTreeNode>();		
		WebTreeNode root=new WebTreeNode("ddddddd",null,StringResource.getStringResource("miufotasknew00105"));
		root.setUrl("");
		vNode.add(root);
		
		Set<String> vRepPK=null;
		Set<String> vTaskPK=null;
		if (queryCond!=null && queryCond.getResultType()==CheckQueryCondVO.CHECK_RESULT_NOPASS){
			CheckQueryCondVO findCond=(CheckQueryCondVO)queryCond.clone();
			findCond.setRepIDs(taskCache.getReportIdsByTaskId(getCurTaskId()));
			
			try{
				String[][] strTaskRepPKs=QueryCondBO_Client.findTaskRepByCond(findCond,getCurUserInfo().getUnitId(),getCurOrgPK());
				vTaskPK=new HashSet<String>(Arrays.asList(strTaskRepPKs[0]));
				vRepPK=new HashSet<String>(Arrays.asList(strTaskRepPKs[1]));
			}catch(Exception e){
			}
		}
		
		//����б����˹�ʽ�����ر����˹�ʽ��
		String strTaskID=getCurTaskId();
		TaskVO task=taskCache.getTaskVO(getCurTaskId());
		boolean bHasTaskCheck=false;
		try{
			bHasTaskCheck=TaskBO_Client.getTaskCheckCount(strTaskID)>0;
		}catch(Exception e){
			AppDebug.debug(e);
		}
		if (bHasTaskCheck && (vTaskPK==null || vTaskPK.contains(strTaskID))){
			//�����˸��ڵ㣬��ʾ���б����˹�ʽ
			WebTreeNode taskRoot=new WebTreeNode(TASKCHECK_FLAG,"ddddddd",task.getName());
			vNode.add(taskRoot);
		}

		//��������ر�����˹�ʽ
		String[] strRepPKs=taskCache.getReportIdsByTaskId(strTaskID);
		ReportVO[] reports=repCache.getByPks(strRepPKs);
		if (reports!=null && reports.length>0){
			for (int i=0;i<reports.length;i++){
				if (reports[i]==null || reports[i].isExistCheckFormula()==false || (vRepPK!=null && !vRepPK.contains(reports[i].getReportPK())))
					continue;
				
				//һ��������ڹ�ʽ�ĸ��ڵ㣬��ʾ�ñ����еĹ�ʽ
				ReportVO report=reports[i];
				String strRepRootVal=report.getReportPK()+REPCHECK_FLAG;
				WebTreeNode repRoot=new WebTreeNode(strRepRootVal,"ddddddd",report.getNameWithCode());
				
				vNode.add(repRoot);
			}
		}

		WebTreeModel model=new WebTreeModel();
		model.setNodes(vNode.toArray(new WebTreeNode[0]));
		model.setGradeLoad(true);
		
		return model;
	}
   
	public static boolean isViewByUnit(IUFOAction action){
        Boolean bByUnit=(Boolean)action.getSessionObject(SESSION_CHECK_QUEYR_BYUNIT);
        if (bByUnit!=null)
        	return bByUnit.booleanValue();
        else
        	return true;
    }

	public String getFormName() {
		return CheckQueryMngForm.class.getName();
	}
	
  }
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <MultiActionVO name="CheckQueryAction" package="com.ufsoft.iufo.check.ui">
      <MethodsVO execute="com.ufsoft.iufo.check.ui.CheckQueryMngUI_update">
      </MethodsVO>
    </MultiActionVO>
@WebDeveloper*/  