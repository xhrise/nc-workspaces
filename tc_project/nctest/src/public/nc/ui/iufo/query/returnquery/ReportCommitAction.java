package nc.ui.iufo.query.returnquery;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;

import nc.pub.iufo.cache.KeyGroupCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.cache.TaskCache;
import nc.pub.iufo.cache.base.UnitCache;
import nc.pub.iufo.exception.CommonException;
import nc.ui.hbbb.pub.HBBBSysParaUtil;
import nc.ui.iufo.balance.BalanceBO_Client;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.constants.IIUFOConstants;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.pub.UfoPublic;
import nc.ui.iufo.release.InfoReleaseAction;
import nc.ui.iufo.release.ReleaseInfoValue;
import nc.ui.iufo.repdataright.RepDataRightUtil;
import nc.ui.iufo.server.center.CreateExportAction;
import nc.ui.iufo.server.center.ExportCenter;
import nc.ui.iufo.server.center.ExportInfoMngUI;
import nc.ui.iufo.server.center.SaveImportInfoToSession;
import nc.ui.iufo.server.param.ServerParamBO_Client;
import nc.ui.iufo.task.TaskBO_Client;
import nc.util.iufo.server.module.RegistCenter;
import nc.util.iufo.server.module.help.RepExSepSubmit;
import nc.util.iufo.server.module.help.SelectedModuleInfo;
import nc.vo.iufo.balance.BalanceCondVO;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.pub.date.UFODate;
import nc.vo.iufo.query.returnquery.ReportCommitVO;
import nc.vo.iufo.repdataright.RepDataRightVO;
import nc.vo.iufo.server.param.ServerParamVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufsoft.iufo.check.ui.CheckResultBO_Client;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.sysprop.ui.ISysProp;
import com.ufsoft.iufo.sysprop.ui.SysPropMng;
import com.ufsoft.iufo.web.MultiFrameAction;

/**
 * 执行上报确认、请求取消上报操作
 * @author ll
 */
public class ReportCommitAction extends MultiFrameAction {

	public final static String SPLIT_FLAG = "@";

	/**
	 * 上报确认主方法
	 * @param actionForm
	 * @return
	 * @throws WebException
	 */
	public ActionForward doOprtCommit(ActionForm actionForm) throws WebException {
		try {
			//是否审核结果不通过不允许上报
			String strNotMustCheckPass = SysPropMng.getSysProp(ISysProp.SENDUP_CHECK).getValue();
			boolean bMustCheckPass=strNotMustCheckPass != null && strNotMustCheckPass.equals("false");
			
			//从界面上得到用户选择的上报信息及出错信息
			Vector<ReportCommitVO> vCommitVO=new Vector<ReportCommitVO>();
			Vector<String> vBalErrMsg=new Vector<String>();
			Vector<String> vCommitErrMsg=new Vector<String>();
			ActionForward fwd=loadCommitFromUI(vCommitVO,vBalErrMsg,vCommitErrMsg,new Hashtable<String,MeasurePubDataVO>(),bMustCheckPass,true);
			if (fwd!=null)
				return fwd;
			
			if (vCommitErrMsg.size()>0){
				StringBuffer bufErrMsg=new StringBuffer();
				bufErrMsg.append(StringResource.getStringResource("miufotasknew00108")+"<BR>");
				for (int i=0;i<vCommitErrMsg.size();i++)
					bufErrMsg.append("&nbsp;&nbsp;&nbsp;&nbsp;("+(i+1)+")"+vCommitErrMsg.get(i)+"<BR>");
				vBalErrMsg.insertElementAt(bufErrMsg.toString(), 0);
				
				fwd=new ActionForward(ErrorRefreshAction.class.getName(),"");
				addRequestObject(ErrorRefreshAction.REQ_PARAM_ERROR,vBalErrMsg.toArray(new String[0]));
				fwd.addParameter(ErrorRefreshAction.PARAM_ERR_TYPE,CloseForward.CLOSE_REFRESH_MAIN);
				return fwd;
			}
			
			//从界面上得到的上报信息没有上报、请求标志信息，从数据库中读取记录将这些信息补全
			TaskVO taskVO=IUFOUICacheManager.getSingleton().getTaskCache().getTaskVO(getCurTaskId());
			String strTimeProp=IUFOUICacheManager.getSingleton().getKeyGroupCache().getByPK(taskVO.getKeyGroupId()).getTimeProp();
			
			ReportCommitVO[] commitVOs=vCommitVO.toArray(new ReportCommitVO[0]);
			Vector<ReportCommitVO> vExportCommit=new Vector<ReportCommitVO>();
			commitVOs=adjustCommit(commitVOs,strTimeProp,taskVO,vExportCommit);
			
			//数据库操作
			ReportCommitBO_Client.commit(commitVOs);
			
			String[] strErrMsgs=(String[])vBalErrMsg.toArray(new String[0]);
			
			//如果存在上级服务器，自动传输报表数据
		    fwd=doTreatExportRepData(vExportCommit.toArray(new ReportCommitVO[0]),strErrMsgs);
			if (fwd!=null)
				return fwd;

			//有出错信息，给出出错信息,判断是否需要刷新父窗口列表
			if (strErrMsgs.length> 0 && commitVOs!=null && commitVOs.length>0){
				fwd=new ActionForward(ErrorRefreshAction.class.getName(),"");
				fwd.addParameter(ErrorRefreshAction.PARAM_ERR_TYPE,ErrorRefreshAction.CLOSE_REFRESH_TREE);
				addRequestObject(ErrorRefreshAction.REQ_PARAM_ERROR,strErrMsgs);
				return fwd;
			}
			else if (strErrMsgs.length>0){
				return new ErrorForward(strErrMsgs);
			}

			if (commitVOs!=null && commitVOs.length>0)
				return  new CloseForward("refreshGradeLoadTree(true);window.close();");
			else
				return new CloseForward(CloseForward.CLOSE);
		} catch (CommonException e) {
			throw e; 
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException("miufo10000");// 未定义的错误！
		}
	}
	
	/**
	 * 请求取消上报的主方法
	 * @param actionForm
	 * @return
	 * @throws WebException
	 */
	public ActionForward doAskForCancel(ActionForm actionForm) throws WebException {
		try{
			//从界面上取得用户选择了哪些aloneid、repid的数据取消上报
			Vector<ReportCommitVO> vCommit=new Vector<ReportCommitVO>();
			Vector<String> vBalErrMsg=new Vector<String>();
			Vector<String> vCommitErrMsg=new Vector<String>();
			Hashtable<String,MeasurePubDataVO> hashPubData=new Hashtable<String,MeasurePubDataVO>();
			
			ActionForward fwd=loadCommitFromUI(vCommit,vBalErrMsg,vCommitErrMsg,hashPubData,false,false);
			if (fwd!=null)
				return fwd;
			
			//根据数据库中记录生成真正的ReportCommitVO
			ReportCommitVO[] commitVOs=vCommit.toArray(new ReportCommitVO[0]);
			commitVOs=adjustCommit(commitVOs,null,null,new Vector<ReportCommitVO>());
			if (commitVOs == null || commitVOs.length <=0)
				return new CloseForward(CloseForward.CLOSE);
			
			//根据上报信息，生成公告栏消息
			ReleaseInfoValue releaseInfo=sendCancelCommit(commitVOs, getCurTaskId(),hashPubData);
	        if(releaseInfo ==null)
	    		return new CloseForward(CloseForward.CLOSE);
	        
	        //将公告栏消息置于request中，转到发布公告栏消息界面
        	addSessionObject(InfoReleaseAction.REQ_PARAM_RELEASEINFO,releaseInfo);
            fwd=new ActionForward(InfoReleaseAction.class.getName(),"createBBSReleaseFromRepCommit");
            fwd.setRedirect(true);
        	return fwd;	
		}
		catch(Exception e){
			e.printStackTrace();
			return new ErrorForward(e.getMessage());
		}
	}


	/**
	 * 获取Action对应的form名称 ll 2006-01-16
	 */
	public String getFormName() {
		return null;
	}	
	
	/**
	 * 从界面上得到的上报信息无上报标志、请求取消标志，需要从数据库中读取这些信息将上报信息补全
	 * @param commits
	 * @param strTimeProp
	 * @param taskVO
	 * @param vExportCommit
	 * @return
	 * @throws Exception
	 */
	private ReportCommitVO[] adjustCommit(ReportCommitVO[] commits,String strTimeProp,TaskVO taskVO,Vector<ReportCommitVO> vExportCommit) throws Exception{
		if (commits==null || commits.length<=0)
			return commits;
		
		//得到所有的aloneid、repid、及按aloneid+repid分组的上报信息
		ArrayList<String> vAloneID=new ArrayList<String>();
		ArrayList<String> vRepID=new ArrayList<String>();
		HashMap<String,ReportCommitVO> hashCommit=new HashMap<String,ReportCommitVO>();
		for (int i=0;i<commits.length;i++){
			if (vAloneID.contains(commits[i].getAloneId())==false)
				vAloneID.add(commits[i].getAloneId());
			
			if (vRepID.contains(commits[i].getRepId())==false)
				vRepID.add(commits[i].getRepId());
			
			hashCommit.put(commits[i].getAloneId()+"#"+commits[i].getRepId(),commits[i]);
		}
		
		//从数据库中得到所有的MeasurePubDataVO和上报信息
		MeasurePubDataVO[] pubDatas=MeasurePubDataBO_Client.findByAloneIDs(vAloneID.toArray(new String[0]));
		ReportCommitVO[][] repCommitVOs=ReportCommitBO_Client.loadRepComByIds(vAloneID.toArray(new String[0]),vRepID.toArray(new String[0]));
		
		ArrayList<ReportCommitVO> vRetCommit=new ArrayList<ReportCommitVO>();
		//对所有的上报信息作循环
		for (int i=0;i<vAloneID.size();i++){
			String strAloneID=(String)vAloneID.get(i);
			
			//得到当前aloneid对应的上报信息
			ReportCommitVO[] oneAloneIDCommitVOs=null;
			if (repCommitVOs!=null && i<repCommitVOs.length)
				oneAloneIDCommitVOs=repCommitVOs[i];
			for (int j=0;j<vRepID.size();j++){
				String strRepID=(String)vRepID.get(j);
				
				//是否有上报信息需要处理
				ReportCommitVO commit=(ReportCommitVO)hashCommit.get(strAloneID+"#"+strRepID);
				if (commit==null)
					continue;
				
				//taskVO参数如果不为空，则需要处理上报、补报、及请求取消的报表再次上报等同于取消掉请求取消标记的问题
				//taskVO参数为空，表示仅需要从数据库中取得上报信息
				if (taskVO!=null){
					//如果数据已经上报，并且已经请求取消上报，则去掉请求取消标记，否则不作处理
					if (oneAloneIDCommitVOs!=null && j<oneAloneIDCommitVOs.length && oneAloneIDCommitVOs[j]!=null && oneAloneIDCommitVOs[j].getCommitFlag()>ReportCommitVO.NOTCOMMITED){
						if (oneAloneIDCommitVOs[j].getRequestCancel()<1)
							continue;
						
						//去掉请求取消标记，已经上报的数据不需要导出
						oneAloneIDCommitVOs[j].setRequestCancel(0);
						vRetCommit.add(oneAloneIDCommitVOs[j]);
						continue;
					}
					
					//改上标记为上报，判断是否是补报
					commit.setCommitFlag(ReportCommitVO.COMMITED);
					if (strTimeProp!=null && strTimeProp.equals(UFODate.NONE_PERIOD)==false && taskVO.getEndDatePoint()!=null && taskVO.getEndDatePoint().trim().length()>0){
						UFODate ufoDate = new UFODate(pubDatas[i].getInputDate());
						ufoDate = ufoDate.getEndDay(strTimeProp);
						if(isAfterCommited(ufoDate, Integer.parseInt(taskVO.getEndDatePoint()), taskVO.getEndTimePoint(),null)){
							commit.setCommitFlag(ReportCommitVO.AFTERCOMMITED);
						}
					}
					vRetCommit.add(commit);
					vExportCommit.add(commit);
				}
				else{
					if (oneAloneIDCommitVOs!=null && j<oneAloneIDCommitVOs.length && oneAloneIDCommitVOs[j]!=null){
						vRetCommit.add(oneAloneIDCommitVOs[j]);
						vExportCommit.add(oneAloneIDCommitVOs[j]);
					}
				}
			}
		}
		
		return (ReportCommitVO[])vRetCommit.toArray(new ReportCommitVO[0]);
	}
	/**
	 * 
	 * @param ufoDate
	 * @param nEndDatePoint
	 * @param strEndTimePoint
	 * @param strDateTimeDst 没有值则使用系统当前时间	
	 * @return
	 */
	public static boolean isAfterCommited(UFODate ufoDate,int nEndDatePoint,String strEndTimePoint,String strDateTimeDst){
		if(ufoDate == null){
			return false;
		}
		//比较　D1(Day + time) < D2(strDateTimeDst)
		//得到D1=Day + time 
		String strDateTimeSrc = ufoDate.getDateAfter(nEndDatePoint).getDateString();
		if(strEndTimePoint != null){
			strDateTimeSrc += " " + strEndTimePoint;
		}
		//得到D2,没有值则使用系统当前时间		
		if(strDateTimeDst == null){
			strDateTimeDst = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new java.util.Date());
		}
		//比较D1,D2
		if (strDateTimeSrc.compareTo(strDateTimeDst) < 0){
			return true;
		}
		return false;
	}
	/**
	 * 说明：与系统当前时间进行比较
	 * @param strDateSrc
	 * @param nEndDatePoint
	 * @param strEndTimePoint
	 * @return
	 */
	public static boolean isAfterCommited(String strDateSrc,int nEndDatePoint,String strEndTimePoint){
		if(strDateSrc == null){
			return false;
		}
		UFODate ufoDate = new UFODate(strDateSrc);
		return isAfterCommited(ufoDate,nEndDatePoint,strEndTimePoint,null);
	}
	/**
	 * 从界面上得到用户选择的上报记录
	 * @param taskVO
	 * @param strIds
	 * @param vCommitVO
	 * @param vAloneID
	 */
	public static void loadCommitVOFromTable(TaskVO taskVO,String[] strIds,List<ReportCommitVO> vCommitVO,List<String> vAloneID){
		for (int i = 0; i < strIds.length; i++) {
			ReportCommitVO commitVO = new ReportCommitVO();
			StringTokenizer st = new StringTokenizer(strIds[i], SPLIT_FLAG);
			int count = 0;
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if (count == 1) {
					commitVO.setRepId(token);
				} else if (count == 0) {
					commitVO.setAloneId(token);
					commitVO.setId(token);
					if (vAloneID.contains(token)==false)
						vAloneID.add(token);
				}
				count++;
			}
			vCommitVO.add(commitVO);
		}		
	}
	
	/**
	 * 得到两个数组的交集
	 * @param strVals1
	 * @param strVals2
	 * @return
	 */
	private String[] insertsectArray(String[] strVals1,String[] strVals2){
		if (strVals1==null || strVals1.length<=0 || strVals2==null || strVals2.length<=0)
			return new String[0];
		
		ArrayList<String> vRet=new ArrayList<String>();
		for (int i=0;i<strVals1.length;i++){
			String strVal1=strVals1[i];
			if (strVal1==null)
				continue;
			
			for (int j=0;j<strVals2.length;j++){
				String strVal2=strVals2[j];
				if (strVal1.equals(strVal2)){
					vRet.add(strVal1);
					break;
				}
			}
		}
		return (String[])vRet.toArray(new String[0]);
	}
	
	/**
	 * 得到一个任务下，对应一组aloneid，得到录入的报表、审核通过的报表、表间审核通过的aloneid，对应各aloneid的MeasurePubDataVO
	 * @param strTaskID
	 * @param vAloneID1
	 * @param hashPassedRepID
	 * @param hashInputRepID
	 * @param hashTaskCheck
	 * @param hashPubData
	 * @param bMustCheckPass
	 * @throws Exception
	 */
	private void loadCheckInputResults(String strTaskID,List<String> vAloneID1,Map<String,List<String>> hashPassedRepID,Map<String,List<String>> hashInputRepID,Map<String,Boolean> hashTaskCheck,Map<String,MeasurePubDataVO> hashPubData,boolean bMustCheckPass) throws Exception{
		TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
		
		//得到任务中的报表及任务中是否定义了表间审核公式
		boolean bHasTaskCheck=TaskBO_Client.getTaskCheckCount(strTaskID)>0;
		
		String[] strTaskRepIDs=taskCache.getReportIdsByTaskId(strTaskID);
		
		//加载所有MeasurePubDataVO
		MeasurePubDataVO[] pubDatas=MeasurePubDataBO_Client.findByAloneIDs(vAloneID1.toArray(new String[0]));
		if (pubDatas==null || pubDatas.length<=0)
			return;
		
		for (int i=0;i<pubDatas.length;i++){
			MeasurePubDataVO pubData=pubDatas[i];
			
			String strAloneID=pubDatas[i].getAloneID();
			hashPubData.put(strAloneID,pubData);
			
			//对于舍位数据，不存在上报问题，不用作处理
			if (pubData.getVer()>=1000)
				continue;
			
			//得到对应aloneid，所有录入的报表
			String[] strInputRepIDs= CheckResultBO_Client.loadRepIdsByAloneId(strAloneID);
			strInputRepIDs=insertsectArray(strTaskRepIDs,strInputRepIDs);
			if (strInputRepIDs!=null && strInputRepIDs.length>0)
				hashInputRepID.put(strAloneID,new ArrayList<String>(Arrays.asList(strInputRepIDs)));
			
			//如果审核未通过不允放上报，需要检查是否表内、表间审核通过
			if (bMustCheckPass==false)
				continue;
			
			//得到表内审核通过的报表
			String[] strPassedRepIds = CheckResultBO_Client.loadPassedRepIdsByAloneId(strAloneID);
			strPassedRepIds=insertsectArray(strTaskRepIDs,strPassedRepIds);
			if (strPassedRepIds!=null && strPassedRepIds.length>0)
				hashPassedRepID.put(strAloneID,new ArrayList<String>(Arrays.asList(strPassedRepIds)));		
			
			//如果有表间审核公式，判断表间审核是否通过
			if (bHasTaskCheck==false)
				continue;
			
			CheckResultVO[] chres = CheckResultBO_Client.loadTaskCheckResults(strTaskID,CheckResultVO.CHECK_ALLSTATE, new String[]{strAloneID},null);
			if (chres==null || chres.length<=0) 
				continue;
			
			for (int n = 0; n < chres.length; n++) {
				// 该任务下为审核或者审核为通过,则抛错
				if (chres[n].getCheckState() == CheckResultVO.NOPASS
						|| chres[n].getCheckState() == CheckResultVO.NOCHECK) {
					hashTaskCheck.put(strAloneID,Boolean.TRUE);
					break;
				}
			}
		}				
	}
	
	/**
	 * 根据需要上报的数据，自动传输报表数据，并显示导出结果界面
	 * @param repCommitVOs
	 * @param strErrMsgs
	 * @return
	 * @throws Exception
	 */
	private ActionForward doTreatExportRepData(ReportCommitVO[] repCommitVOs,String[] strErrMsgs) throws Exception{
		if (repCommitVOs==null || repCommitVOs.length<=0)
			return null;
		
		//如果没有主服务器，则不需要传输数据
		ServerParamVO[] params=ServerParamBO_Client.loadAllSrvParams();
		if (params==null || params.length<=0)
			return null;
		
		String strParamID=null;
		for (int i=0;i<params.length;i++){
			if (params[i].getSrvType()==ServerParamVO.SRVTYPE_PARENT){
				strParamID=params[i].getStrSrvSettingId();
				break;
			}
		}
		
		if (strParamID==null)
			return null;
		
		String strTaskID=getCurTaskId();
		
		//将repid按aloneid分组
		Map<String,List<String>> hashAloneIDByRepID=new HashMap<String,List<String>>();
		for (int i=0;i<repCommitVOs.length;i++){
			List<String> vAloneID=hashAloneIDByRepID.get(repCommitVOs[i].getRepId());
			if (vAloneID==null){
				vAloneID=new ArrayList<String>();
				hashAloneIDByRepID.put(repCommitVOs[i].getRepId(),vAloneID);
			}
			vAloneID.add(repCommitVOs[i].getAloneId());
		}
		
		//构建报表数据导出对象
		Vector<RepExSepSubmit> vRepExSepSumbit=new Vector<RepExSepSubmit>();
		String[] strRepIDs=(String[])hashAloneIDByRepID.keySet().toArray(new String[0]);
		for (int i=0;i<strRepIDs.length;i++){
			RepExSepSubmit submit=new RepExSepSubmit();
			submit.setReportPK(repCommitVOs[i].getRepId());
			submit.setTaskID(strTaskID);
			submit.setAloneID(hashAloneIDByRepID.get(repCommitVOs[i].getRepId()));
			
			vRepExSepSumbit.add(submit);
		}
		
		//住session中存入模块导出信息
		SelectedModuleInfo module=new SelectedModuleInfo();
		module.setSelServers(new Vector<String>(Arrays.asList(new String[]{strParamID})));
		Hashtable<String,Object> hashModuleData=new Hashtable<String,Object>();
		hashModuleData.put(RegistCenter.REPDATA_TAG,vRepExSepSumbit);
		module.setSelModules(hashModuleData);
		addSessionObject(ExportInfoMngUI.class.getName(),module);
		
		//调用导出对象进行导出并传输
		ExportCenter exportC = new ExportCenter();
		exportC.setUserVO(SaveImportInfoToSession.getLoginUserVO(this));
		exportC.setVecSelModule(module);
		
		//置入上报时的出错信息，用于导出结果界面的显示
		addRequestObject(ErrorRefreshAction.REQ_PARAM_ERROR,strErrMsgs);
		
		//转出到导出结果界面
		ActionForward fwd=new ActionForward(CreateExportAction.class.getName(),IIUFOConstants.ACTION_METHOD_OPEN);
		fwd.addParameter(ErrorRefreshAction.PARAM_ERR_TYPE,ErrorRefreshAction.CLOSE_REFRESH_TREE);
		return fwd;
	}		
	
	/**
	 * 从界面上用户选择的记录得到上报信息，并按是否允许上报做过滤，生成出错信息
	 * @param vRetCommitVO
	 * @param vErrMsg
	 * @param hashPubData
	 * @param bMustCheckPass
	 * @return
	 * @throws Exception
	 */
	private ActionForward loadCommitFromUI(Vector<ReportCommitVO> vRetCommitVO,Vector<String> vBalErrMsg,Vector<String> vCommitErrMsg,Hashtable<String,MeasurePubDataVO> hashPubData,boolean bMustCheckPass,boolean bCommit) throws Exception{
		String[] strIds = getTableSelectedIDs();
		if (strIds==null || strIds.length<=0)
			return new CloseForward(CloseForward.CLOSE);
		
		TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
		ReportCache repCache=IUFOUICacheManager.getSingleton().getReportCache();
		
		String taskId = getCurTaskId();
		TaskVO taskVO = taskCache.getTaskVO(taskId);
		String[] strTaskRepPKs=taskCache.getReportIdsByTaskId(taskId);
		
		//从界面上读取用户选择的aloneid、repid等信息，生成ReportCommitVO数组
		ArrayList<String> vAloneID=new ArrayList<String>();
		ArrayList<ReportCommitVO> vecCommitVOs = new ArrayList<ReportCommitVO>();
		loadCommitVOFromTable(taskVO,strIds,vecCommitVOs,vAloneID);
		
		if (vecCommitVOs.size()<=0)
			return new CloseForward(CloseForward.CLOSE);
				
		//得到录入的报表、审核通过的报表、表间通过的aloneid及aloneid对应的MeasurePubData
		HashMap<String,List<String>> hashPassedRepID=new HashMap<String,List<String>>();
		HashMap<String,List<String>> hashInputRepID=new HashMap<String,List<String>>();
		HashMap<String,Boolean> hashTaskCheck=new HashMap<String,Boolean>();
		loadCheckInputResults(taskId,vAloneID,hashPassedRepID,hashInputRepID,hashTaskCheck,hashPubData,bMustCheckPass);

		//记录下哪些舍位条件的数据用户选择了上报
		BalanceCondVO[] balConds=BalanceBO_Client.loadAllBalanceCond();
		Vector<BalanceCondVO> vErrBalCond=new Vector<BalanceCondVO>();
		
		for (int i = 0; i < vecCommitVOs.size(); i++) {
			ReportCommitVO commitVO = (ReportCommitVO) vecCommitVOs.get(i);
			if (vAloneID.contains(commitVO.getAloneId())==false)
				continue;

			ReportVO[] repVOs = repCache.getByPks(new String[] { commitVO.getRepId()});
			
			//如果报表存在，表示按报表上报，否则表示按任务上报
			if (repVOs[0] != null){
				boolean bHasCheckFormula = repVOs[0].isExistCheckFormula();

				//如果对应该aloneid没有已录入的报表，表示是舍位数据，给出舍位数据不允许上报的提示信息
				List<String> vInputRepID=hashInputRepID.get(commitVO.getAloneId());
				if (vInputRepID==null || !vInputRepID.contains(commitVO.getRepId())){
					processSWCommitMsg(hashPubData,commitVO,balConds,vErrBalCond,vBalErrMsg);		
					continue;
				}
				else if (bMustCheckPass){
					boolean bCanCommit=true;
					
					//判断表内审核是否通过，并给出出错提示
					List<String> vPassRepID=hashPassedRepID.get(commitVO.getAloneId());
					if (bHasCheckFormula && (vPassRepID==null || !vPassRepID.contains(commitVO.getRepId()))){
						//报表{0}定义过表内审核公式,但是审核未通过或者未审核,不允许上报
						String strResPrompt = StringResource.getStringResource("miufo150026",new String[]{repVOs[0].getNameWithCode()});
						vBalErrMsg.add(strResPrompt);
						bCanCommit=false;
					}
					
					//判断表间审核是否通过，并给出出错提示
					Boolean bNotCheck=hashTaskCheck.get(commitVO.getAloneId());
					if (bNotCheck!=null && bNotCheck.booleanValue()==true){
						// 报表{0}所在任务\"{1}\"定义过表间审核公式,但是审核未通过或者未审核,不允许上报
						String strResPrompt = StringResource.getStringResource("miufo150025",new String[]{repVOs[0].getNameWithCode(),taskVO.getName()});
						vBalErrMsg.add(strResPrompt);
						bCanCommit=false;
					}
					if (!bCanCommit)
						continue;
				}

				vRetCommitVO.addElement(commitVO);
			}
			else{//按任务上报
				//如果对应该aloneid没有已录入的报表，表示是舍位数据，给出舍位数据不允许上报的提示信息
				List<String> vCommitRepID=hashInputRepID.get(commitVO.getAloneId());
				if (vCommitRepID==null || vCommitRepID.size()<=0){
					processSWCommitMsg(hashPubData,commitVO,balConds,vErrBalCond,vBalErrMsg);
					continue;
				}
				
				//如果必须审核通过，则判断表间审核是否通过，在表间审核通过的基础上，得到表内审核通过的报表
				if (bMustCheckPass){
					Boolean bNotCheck=hashTaskCheck.get(commitVO.getAloneId());
					if (bNotCheck==null || bNotCheck.booleanValue()==false){
						List<String> vPassRepID=hashPassedRepID.get(commitVO.getAloneId());
						
						for (int j=vCommitRepID.size()-1;j>=0;j--){
							String strRepID=(String)vCommitRepID.get(j);
							ReportVO report=repCache.getByPks(new String[]{strRepID})[0];
							if (report.isExistCheckFormula() && (vPassRepID==null || !vPassRepID.contains(strRepID)))
								vCommitRepID.remove(j);
						}
					}
					else
						vCommitRepID=new ArrayList<String>();
				}
				
				MeasurePubDataVO pubData=(MeasurePubDataVO)hashPubData.get(commitVO.getAloneId());
				String[] strCommitRepIDs=vCommitRepID.toArray(new String[0]);
				
				//对个别报表数据需要根据报表数据权限进行过滤
				if (!HBBBSysParaUtil.isHBBBAndSWVer(pubData.getVer())){
					if (pubData.getUnitPK()!=null && pubData.getUnitPK().trim().length()>0)
						strCommitRepIDs=RepDataRightUtil.loadRepsByRight(getCurUserInfo(),taskVO,RepDataRightVO.RIGHT_TYPE_VIEW,strCommitRepIDs,pubData.getUnitPK(),getCurOrgPK());
				}
				
				if (bCommit && taskVO.isCommitByTask() && strCommitRepIDs.length<strTaskRepPKs.length){
					vCommitErrMsg.add(getKeywordStrByAloneID(commitVO.getAloneId()));
					continue;
				}
				
				//将一条上报记录按可以上报的报表拆成多条记录
				for (int j=0;strCommitRepIDs!=null && j<strCommitRepIDs.length;j++){
					ReportCommitVO repCommit=new ReportCommitVO();
					repCommit.setAloneId(commitVO.getAloneId());
					repCommit.setRepId(strCommitRepIDs[j]);
					vRetCommitVO.add(repCommit);
				}
			}
		}

		return null;
	}
	
	/**
	 * 得到aloneid得到显示给用户的关键字内容
	 * @param strAloneID
	 * @return
	 */
	private String getKeywordStrByAloneID(String strAloneID){
		MeasurePubDataVO pubData=null;
		try{
			pubData=MeasurePubDataBO_Client.findByAloneID(strAloneID);
		}catch(Exception e){
			AppDebug.debug(e);
		}
		if (pubData==null)
			return null;
		
		StringBuffer buf=new StringBuffer();
		
		UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
		KeyGroupVO keyGroup=pubData.getKeyGroup();
		KeyVO[] keys=keyGroup.getKeys();
		for (int i=0;i<keys.length;i++){
			String strKeyVal=pubData.getKeywordByIndex(i+1);
			if (keys[i].getKeywordPK().equals(KeyVO.CORP_PK) || keys[i].getKeywordPK().equals(KeyVO.DIC_CORP_PK)){
				UnitInfoVO unitInfo=unitCache.getUnitInfoByPK(strKeyVal);
				if (unitInfo!=null)
					strKeyVal=unitInfo.getCode();
			}
			buf.append(strKeyVal);
			if (i<keys.length-1)
				buf.append("、");
		}
		
		return buf.toString();
	}
	
	/**
	 * 给出舍位数据不允许上报的提示信息，提示舍位条件名称
	 * @param hashPubData
	 * @param commitVO
	 * @param balConds
	 * @param vErrBalCond
	 * @param vErrMsg
	 */
	private void processSWCommitMsg(Map<String,MeasurePubDataVO> hashPubData,ReportCommitVO commitVO,BalanceCondVO[] balConds,List<BalanceCondVO> vErrBalCond,List<String> vErrMsg){
		MeasurePubDataVO pubData=(MeasurePubDataVO)hashPubData.get(commitVO.getAloneId());
		BalanceCondVO balCond=getBalanceCondByVer(balConds,pubData.getVer());
		if (balCond!=null && vErrBalCond.contains(balCond)==false){
			vErrMsg.add(StringResource.getStringResource("miufoinputnew00056",new String[]{balCond.getName()}));
			vErrBalCond.add(balCond);
		}
	}
	
	/**
	 * 根据报表数据版本，得到舍位条件
	 * @param balConds
	 * @param iDataVer
	 * @return
	 */
	private BalanceCondVO getBalanceCondByVer(BalanceCondVO[] balConds,int iDataVer){
		if (iDataVer<1000 || balConds==null || balConds.length<=0)
			return null;
		
		int iVer=iDataVer-999;
		if (iVer>10){
			iVer=iDataVer-999-HBBBSysParaUtil.VER_HBBB*10;
		}
		
		if (iVer<=0 || iVer>10)
			return null;
			
		for (int i=0;i<balConds.length;i++){
			if (balConds[i].getVersion()==iVer)
				return balConds[i];
		}
		return null;
	}

	/**
	 * 根据需要请求取消的信息，生成请求取消的数据库记录并生成公告栏消息
	 * @param commitVOs
	 * @param taskId
	 * @param hashPubData
	 * @return
	 */
	private ReleaseInfoValue sendCancelCommit(ReportCommitVO[] commitVOs, String taskId,Hashtable hashPubData) {
	    try {
	    	UserInfoVO userInfo=getCurUserInfo();
	    	
	    	TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
	    	ReportCache repCache=IUFOUICacheManager.getSingleton().getReportCache();
	    	UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
	    	KeyGroupCache kgCache=IUFOUICacheManager.getSingleton().getKeyGroupCache();
	    	
	    	//得到任务中报表
	    	TaskVO task=taskCache.getTaskVO(taskId);
	        String[] repIds=taskCache.getReportIdsByTaskId(taskId);
	        ReportVO[] repVOs =repCache.getByPks(repIds);
	        Hashtable<String,String> hashRepName = new Hashtable<String,String>();
            for (int i = 0; repVOs!=null && i < repVOs.length; i++) {
            	hashRepName.put(repVOs[i].getReportPK(), repVOs[i].getNameWithCode());
            }

            //得到任务中关键字的名称
            KeyVO[] keys =kgCache.getByPK(task.getKeyGroupId()).getKeys();

	        //消息说明信息
	        StringBuffer sbNoteInfo = new StringBuffer();	
	        
	        //先生成任务消息
	        sbNoteInfo.append(StringResource.getStringResource(IReturnQueryStrResource.RETURNQUERY_TASK)+"："+task.getName()+"\r\n");

	        Vector<ReportCommitVO> vecCommitVOs = new Vector<ReportCommitVO>();
	        for (int i = 0; i < commitVOs.length; i++) {           
	            if (commitVOs[i].getCommitFlag()<=ReportCommitVO.NOTCOMMITED)
	            	continue;
	            
	            //已经请求取消的不需要更新数据库记录，但要生成公告栏消息
	            if (commitVOs[i].getRequestCancel()<=0)
	                vecCommitVOs.add(commitVOs[i]);
	            
	            MeasurePubDataVO pubData=(MeasurePubDataVO)hashPubData.get(commitVOs[i].getAloneId());
	            String strRepName=(String)hashRepName.get(commitVOs[i].getRepId());
	            	            
	            genOneRepReleaseInfo(sbNoteInfo,pubData,strRepName,keys);
	        }
	        
	        //更新数据库中的请求标记
	        if (vecCommitVOs != null && vecCommitVOs.size() > 0) 
	            ReportCommitBO_Client.requestCancel((ReportCommitVO[])vecCommitVOs.toArray(new ReportCommitVO[0]));
	        
	        //将公告栏消息转成ReleaseInfoValue对象
	        UnitInfoVO unitInfo =unitCache.getUnitInfoByPK(userInfo.getUnitId());
        	ReleaseInfoValue releaseInfoValue = new ReleaseInfoValue();
        	releaseInfoValue.setTitleValue(unitInfo.getNameWithCode()+StringResource.getStringResource("miuforq001"));
        	releaseInfoValue.setNote(sbNoteInfo.toString());
        	
        	UnitInfoVO createUnit=unitCache.getUnitInfoByPK(task.getCreator());
        	if (createUnit!=null){
        		releaseInfoValue.setTargetDispValues(new String[]{createUnit.getNameWithCode()+InfoReleaseAction.SEPARATOR_2+InfoReleaseAction.ALL_SYMBOL});
        		releaseInfoValue.setTargetValues(new String[]{InfoReleaseAction.UNIT_SYMBOL+InfoReleaseAction.SEPARATOR+createUnit.getPK()});
        	}
        	releaseInfoValue.setCloseType(ErrorRefreshAction.CLOSE_REFRESH_TREE);
	       
	        return releaseInfoValue;
	    } catch (CommonException e) {
	    	e.printStackTrace();
	        throw e;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new CommonException("miufo1000329");  //miufo1000329=操作失败
	    }
	}
	
	/**
	 * 生成对应一个报表表页数据的消息，此方法在报送管理也用到
	 * @param sbNoteInfo
	 * @param pubData
	 * @param strRepName
	 * @param keys
	 */
	public static void genOneRepReleaseInfo(StringBuffer sbNoteInfo,MeasurePubDataVO pubData,String strRepName,KeyVO[] keys){
        //标明是哪一个报表
        sbNoteInfo.append("\r\n"+StringResource.getStringResource("miufo1003208")+strRepName+"\r\n");  //" 报表："
        
        //标明报表数据的关键字信息
        if (pubData != null && keys != null && keys.length > 0) {
        	sbNoteInfo.append(StringResource.getStringResource("miufo1003209"));  //" 关键字："
            for (int i= 0; i< keys.length; i++) {
                String codeContent =UfoPublic.getCodeContentByKeyId(keys[i].getKeywordPK(), pubData.getKeywordByIndex(i + 1));
                sbNoteInfo.append(keys[i].getName()+"=");
                if (codeContent != null)
                	sbNoteInfo.append(codeContent);
                else
                	sbNoteInfo.append(pubData.getKeywordByIndex(i+ 1));
                sbNoteInfo.append(" ");
            }
            sbNoteInfo.append("\r\n");
        }
        
        //标明报表数据的版本
        if (pubData!=null){
        	sbNoteInfo.append(StringResource.getStringResource("miufo1001376")+"：");
        	if (pubData.getVer()==0)
        		sbNoteInfo.append(StringResource.getStringResource("miufopublic235"));
        	else
        		sbNoteInfo.append(StringResource.getStringResource("miufopublic142"));
        	sbNoteInfo.append("\r\n");
        }	 		
	}
}
