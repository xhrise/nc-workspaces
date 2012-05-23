package nc.ui.iufo.query.tablequery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import nc.bs.framework.common.NCLocator;
import nc.imp.tc.imp.QueryList;
import nc.itf.tc.imp.IQueryList;
import nc.pub.iufo.cache.TaskCache;
import nc.ui.iufo.balance.BalanceBO_Client;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.constants.IIUFOConstants;
import nc.ui.iufo.dataexchange.AutoMultiSheetImportAction;
import nc.ui.iufo.dataexchange.MultiSheetImportForm;
import nc.ui.iufo.server.param.ServerParamMngAction;
import nc.ui.iufo.task.TaskBO_Client;
import nc.vo.iufo.balance.BalanceCondVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.task.TaskVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.web.WebException;
import com.ufida.web.action.Action;
import com.ufida.web.action.ActionForm;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.action.ErrorForward;
import com.ufida.web.html.Script;
import com.ufida.web.window.WebStatusBar;
import com.ufsoft.iufo.check.ui.CheckBO_Client;
import com.ufsoft.iufo.querycond.ui.AbsQuerySimpleAction;
import com.ufsoft.iufo.querycond.vo.IQueryCondConstant;

/**
 * 报表数据查询界面Action
 * @author weixl
 *
 */
public class TableQuerySimpleAction extends AbsQuerySimpleAction {
	protected void processMenuVisible(ActionForm actionForm){
		TableQueryForm form=(TableQueryForm)actionForm;
		
		// --------------------------
		
		QueryList queryList = new QueryList();
		try {
			queryList.getLikeFuncCount();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//---------------------------
		
		
		form.setByReport(isViewByReport(this));
		
		TaskCache taskCache=IUFOUICacheManager.getSingleton().getTaskCache();
		TaskVO task=taskCache.getTaskVO(getCurTaskId());
		try{
			form.setHasTaskCheck(TaskBO_Client.getTaskCheckCount(getCurTaskId())>0);
		}catch(Exception e){
			AppDebug.debug(e);
		}
		form.setCommitByTask(task.isCommitByTask());
		
		try{
			BalanceCondVO[] balConds=BalanceBO_Client.loadAllBalanceCond();
			if (balConds!=null && balConds.length>0)
				form.setHasBalConds(true);
		}catch(Exception e){
			AppDebug.debug(e);
		}
		
	}

	protected Script getOnLoadScript() {
		return null;
	}

	//响应表内审核菜单
	public ActionForward checkRep(ActionForm actionForm) throws WebException{
		return check(true);
	}
	
	//响应表间审核菜单
	public ActionForward checkTask(ActionForm actionForm) throws WebException{
		return check(false);
	}	
	
	/**
	 * 执行审核的具体方法
	 * @param bViewByReport，是否在表内审核
	 * @return
	 * @throws WebException
	 */
	private ActionForward check(boolean bViewByReport) throws WebException{		
		String[] strIDs=getTableSelectedIDs();

		ArrayList<String> vAloneID=new ArrayList<String>();
		ArrayList<String> vRepID=new ArrayList<String>();
		
		for (int i=0;i<strIDs.length;i++){
			StringTokenizer token=new StringTokenizer(strIDs[i],"@");
			vAloneID.add(token.nextToken());
			vRepID.add(token.nextToken());
		}
			
		try{
			if (bViewByReport){
				//表内审核，将aloneid按reid进行分组
				HashMap<String,List<String>> hashAloneIDByRepID=new HashMap<String,List<String>>();
				for (int i=0;i<vRepID.size();i++){
					String strRepID=(String)vRepID.get(i);
					List<String> vOneAloneID=hashAloneIDByRepID.get(strRepID);
					if(vOneAloneID==null){
						vOneAloneID=new ArrayList<String>();
						hashAloneIDByRepID.put(strRepID,vOneAloneID);
					}
					vOneAloneID.add(vAloneID.get(i));
				}
				
				String[] strRepIDs=(String[])hashAloneIDByRepID.keySet().toArray(new String[0]);
				DataSourceVO ds=(DataSourceVO)getSessionObject(IIUFOConstants.DefaultDSInSession);
				//分报表进行审核
				for (int i=0;i<strRepIDs.length;i++){
					List<String> vOneAloneID=hashAloneIDByRepID.get(strRepIDs[i]);
					String[] strAloneIDs=vOneAloneID.toArray(new String[0]);
					CheckBO_Client.runRepCheck(getCurTaskId(),new String[]{strRepIDs[i]},strAloneIDs,ds,false,getCurUserInfo().getID(),getCurLoginDate());
				}
			}
			else{
				CheckBO_Client.runTaskCheck(getCurTaskId(),(String[])vAloneID.toArray(new String[0]),false,getCurUserInfo().getID(),getCurLoginDate());
			}
		}
		catch(Exception e){
			AppDebug.debug(e);//@devTools e.printStackTrace();
			return new ErrorForward(e);
		}
		
		return new CloseForward("refreshGradeLoadTree(true);window.close();");
	}
	
	protected int getModuleType() {
		return IQueryCondConstant.MODULE_REPORTQUERY;
	}

	public static boolean isViewByReport(Action action){
		Boolean bViewByTask=(Boolean)action.getSessionObject(IIUFOConstants.TableViewTypeInSession);
		if (bViewByTask!=null && bViewByTask.booleanValue())
			return false;
		return true;
	}
	
	//响应按任务查看菜单
	public ActionForward doViewByTask(ActionForm form){
		addSessionObject(IIUFOConstants.TableViewTypeInSession,Boolean.TRUE);
		return new CloseForward("opener.ajaxMenu();refreshGradeLoadTree(true);window.close();");
	}
	
	//响应按报表查看菜单
	public ActionForward doViewByRep(ActionForm form){
		addSessionObject(IIUFOConstants.TableViewTypeInSession,Boolean.FALSE);
		return new CloseForward("opener.ajaxMenu();refreshGradeLoadTree(true);window.close();");
	}
	
	public WebStatusBar getStatusBar(ActionForm actionForm) {
		return ServerParamMngAction.getStatusBar(this,true,true);
	}		
	
	public String getFormName() {
		return TableQueryForm.class.getName();
	}		
}
