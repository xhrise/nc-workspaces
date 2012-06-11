package nc.ui.iufo.query.tablequery;

import nc.ui.iufo.function.IFuncOrderFlag;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.query.returnquery.ReportCommitVO;
import nc.vo.iufo.task.TaskVO;
import nc.vo.iufo.user.UserInfoVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.web.action.ActionForward;
import com.ufsoft.iufo.querycond.ui.AbsQueryDataLoad;
import com.ufsoft.iufo.querycond.ui.IQueryDataLoad;
import com.ufsoft.iufo.querycond.vo.QueryCondVO;
import com.ufsoft.iufo.web.IUFOAction;

/**
 * 报表数据查询界面列表数据加载的接口的实现
 * @author weixl
 *
 */
public class TableQueryDataLoad extends AbsQueryDataLoad implements IQueryDataLoad {
	
	//支持查找未录入的报表数据，需要根据查询条件生成数据库中不存在的MeasurePubDataVO
	protected MeasurePubDataVO[] adjustMeasurePubDatas(MeasurePubDataVO[] pubDatas, QueryCondVO cond,String strTreeUnitID,String strOrgPK) {
		return pubDatas;
	}

	//按录入状态对报表进行过滤
	protected String[] filterRepIDs(String[] strQueryRepIDs,String[] strInputedRepIDs,MeasurePubDataVO pubData,QueryCondVO queryCond) {
		return null;
	}

	//报表数据进行操作的入口界面类
	protected ActionForward getDataOperActionForward() {
		return new ActionForward(ReportDataOperEnterUI.class.getName());
	}

	protected String getSelectCheckValue(ReportVO report, MeasurePubDataVO pubData) {
		StringBuilder bufVal=new StringBuilder();
		
		String strAloneID=getAloneIDStr(pubData);
		if (report==null)
			bufVal.append(strAloneID).append("@repid@").append(pubData.getVer());
		else
			bufVal.append(strAloneID).append("@").append(report.getReportPK()).append("@").append(pubData.getVer());
		
		return bufVal.toString();
	}
	
	protected boolean[] isCanOperRepData(boolean enable) {
		return new boolean[]{false,true};
	}

	//录入的报表才可以被选中
	protected boolean isEnable(MeasurePubDataVO pubData,TaskVO task,UserInfoVO userInfo,ReportCommitVO repCommit, String strCheckState, boolean bMustCheckPassBeforeCommit,boolean bSaved,boolean bHasForm) {
		return bSaved;
	}

	public boolean isViewByReport(IUFOAction action) {
		return TableQuerySimpleAction.isViewByReport(action);
	}
	
	public int[] getShowColumnIndex(KeyVO[] taskKeys) {
		//默认显示列为：报表名称、关键字、审核状态、报表数据类型
		//报表名称，报表数据类型，上报状态，请求取消状态，版本，录入状态，上报时间，审核时间，审核状态，上报单位，上报用户
		int iKeyNum=0;
		if (taskKeys!=null)
			iKeyNum=taskKeys.length;
		
		int[] iIndexes=new int[5+iKeyNum];
		iIndexes[0]=0;

		for (int i=0;i<iKeyNum;i++)
			iIndexes[1+i]=i-iKeyNum;
		
		iIndexes[iKeyNum+1]=1;
		iIndexes[iKeyNum+2]=8;
		iIndexes[iKeyNum+3]=2;
		iIndexes[iKeyNum+4]=3;

		return iIndexes;
	}

	protected String[] getDataOperFuncOrders() {
		return new String[]{IFuncOrderFlag.MYREP_REPDATA_INPUTWEB_VIEW,IFuncOrderFlag.MYREP_REPDATA_INPUTREPTOOL};
	}	
}
