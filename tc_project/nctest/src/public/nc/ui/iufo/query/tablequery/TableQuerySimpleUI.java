package nc.ui.iufo.query.tablequery;

import nc.ui.iufo.constants.IIUFOConstants;
import nc.ui.iufo.dataexchange.MultiSheetExcelAction;
import nc.ui.iufo.function.FunctionRightHandler;
import nc.ui.iufo.function.IFuncOrderFlag;
import nc.ui.iufo.function.IFuncUniqueFlag;
import nc.ui.iufo.input.AutoInputKeywordsAction;
import nc.ui.iufo.input.InputKeywordsEnterAction;
import nc.ui.iufo.input.table.TableInputAction;
import nc.ui.iufo.input.table.TablePrintAction;
import nc.ui.iufo.query.returnquery.ReportCommitAction;
import nc.ui.iufo.release.IReleaseRepType;
import nc.ui.iufo.release.InfoReleaseAction;
import nc.ui.iufo.release.InfoReleaseByNetAction;
import nc.ui.iufo.server.param.ServerParamMngAction;
import nc.ui.iufo.totalsub.TotalSubMngAction;

import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.menu.WebMenu;
import com.ufida.web.comp.menu.WebMenuItem;
import com.ufida.web.comp.menu.WebMenubar;
import com.ufsoft.iufo.check.ui.CheckQueryAction;
import com.ufsoft.iufo.querycond.ui.QueryListConfigAction;
import com.ufsoft.iufo.querycond.vo.IQueryCondConstant;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.xbrl.ui.XBRLInstanceGenAction;

/**
 * 报表数据查询界面UI
 * @author weixl
 *
 */
public class TableQuerySimpleUI extends com.ufsoft.iufo.querycond.ui.AbsQuerySimpleUI implements IFuncOrderFlag {
	private static final long serialVersionUID = 5256990389013919259L;
	
	private WebMenubar menubar1 = null;

	//按任务查看和按报表查看菜单项
	private WebMenuItem menuItem_tableQueryByTask=null;
	private WebMenuItem menuItem_tableQueryByRep=null;
	
	//录入子菜单及web录入、报表工具录入菜单项
	private WebMenu menuInput=null;
	private WebMenuItem menuItemWebInput=null;
	private WebMenuItem menuItemRepToolInput=null;
	private WebMenuItem menuItemAutoInput = null;
	
	//表间审核、表内审核菜单项
	private WebMenuItem menuItem_tableTaskCheck=null;
	private WebMenuItem menuItem_tableRepCheck=null;
	
	//上报子菜单及上报确认、请求取消上报菜单项
	private WebMenu menuReportCommit=null;
	private WebMenuItem menuItem_tableReportCommit=null;
	private WebMenuItem menuItem_tableAskCancel=null;
	
	private WebMenuItem menuItem_QueryCheck=null;
	
	//舍位菜单项
	private WebMenuItem menuItem_tableSwBal = null;
	
	//汇总下级菜单项
	private WebMenuItem menuItem_totalSub=null;
	
	//删除菜单项
	private WebMenuItem menuItem_tableRemove = null;
	
	//发布子菜单及发布、发布到网站菜单项
	private WebMenu menuRelease=null;
	private WebMenuItem menuItem_tableQueryRelease = null;
	private WebMenuItem menuItem_tableQueryReleaseByNet = null;
	
	//列表定制菜单项
	private WebMenuItem menuItem_ListConfig=null;
	
	//导出及打印菜单项
	private WebMenuItem menuItem_multiSheetExcel = null;
	private WebMenuItem menuItem_multiSheetPrint = null;
	
	//生成XBRL Instance
	private WebMenuItem menuItem_xbrlInstanceGen = null;
	
	public String getTitle() {
		return StringResource.getStringResource("miufopublic189"); // "报表查询"
	}	
	
	protected void setData(){
		super.setData();
		
		TableQueryForm form=(TableQueryForm)getActionForm(TableQueryForm.class.getName());
		setMenuItemVisible(form);
		
		WebMenuItem[] webMenuItems = new WebMenuItem[]{menuItemWebInput,menuItemRepToolInput,menuItemAutoInput,menuItem_tableSwBal,
				menuItem_tableRemove,menuItem_tableRepCheck,menuItem_tableTaskCheck,menuItem_QueryCheck,
				menuItem_totalSub,menuItem_tableReportCommit,menuItem_tableAskCancel,menuItem_tableQueryRelease,
				menuItem_tableQueryReleaseByNet,menuItem_ListConfig,menuItem_multiSheetExcel,
				menuItem_multiSheetPrint,menuItem_xbrlInstanceGen};

		String[] strFuncOrders=new String[]{MYREP_REPDATA_INPUTWEB,MYREP_REPDATA_INPUTREPTOOL,MYREP_REPDATA_BALANCE,
				MYREP_REPDATA_REMOVE,MYREP_REPDATA_CHECK,MYREP_REPDATA_CHECK,MYREP_REPDATA_CHECKRESULTQUERY,
				MYREP_REPDATA_TOTALSUB,MYREP_REPDATA_RETRUNQUERY_COMMIT,MYREP_REPDATA_RETRUNQUERY_CANCEL,MYREP_REPDATA_RELEASE_RELEASE,
				MYREP_REPDATA_RELEASE_WEBSITE,MYREP_REPDATA_PROPLIST,MYREP_REPDATA_EXPORT,
				MYREP_REPDATA_PRINT,MYREP_REPDATA_XBRLINSTANCE_GEN};
		
        FunctionRightHandler.setMenuItemFuncOrders(strFuncOrders,webMenuItems);
	}
	
	/**
	 * 得到控制菜单显示的脚本
	 * @param bSelfWindow
	 * @return
	 */
	private void setMenuItemVisible(TableQueryForm form){
		//按任务查看隐藏"按任务查看"、"表内审核"菜单,
		if (form.isByReport()==false){
			menuItem_tableQueryByTask.setVisible(false);
			menuItem_tableRepCheck.setVisible(false);
			if (form.isHasTaskCheck()==false)
				menuItem_tableTaskCheck.setVisible(false);
		}
		//按报表查看隐藏"按报表查看"、"表间审核"、"删除"菜单
		else{
			menuItem_tableQueryByRep.setVisible(false);
			
			if (form.isCommitByTask())
				menuReportCommit.setVisible(false);
			
			menuItem_tableTaskCheck.setVisible(false);
		}
		if (form.isByReport()==true || form.isHasBalConds()==false)
			menuItem_tableSwBal.setVisible(false);
	}

	protected void initMenuLabel(){
		menuItem_tableQueryByRep.setMenuLabel(StringResource.getStringResource("miufoinputnew00047"));
		menuItem_tableQueryByTask.setMenuLabel(StringResource.getStringResource("miufoinputnew00046"));
		
		menuInput.setMenuLabel(StringResource.getStringResource("miufopublic185"));
		menuItemWebInput.setMenuLabel(StringResource.getStringResource("miufoinputnew00052"));
		menuItemRepToolInput.setMenuLabel(StringResource.getStringResource("miufotableinput000001"));

		menuItem_tableSwBal.setMenuLabel(StringResource.getStringResource("miufo1001627"));
		menuItem_tableRemove.setMenuLabel(StringResource.getStringResource("miufo1001641"));
		
		menuItem_tableRepCheck.setMenuLabel(StringResource.getStringResource("miufo1000167"));
		menuItem_tableTaskCheck.setMenuLabel(StringResource.getStringResource("miufo1000168"));
		menuItem_QueryCheck.setMenuLabel(StringResource.getStringResource("uiufofurl0174"));
		
		menuItem_totalSub.setMenuLabel(StringResource.getStringResource("miufoinputnew00032"));
		
		menuReportCommit.setMenuLabel(StringResource.getStringResource("miufopublic228"));
		menuItem_tableReportCommit.setMenuLabel(StringResource.getStringResource("miufopublic155"));
		menuItem_tableAskCancel.setMenuLabel(StringResource.getStringResource("miufopublic231"));

		menuRelease.setMenuLabel(StringResource.getStringResource("miufopublic341"));
		menuItem_tableQueryRelease.setMenuLabel(StringResource.getStringResource("miufopublic341"));
		menuItem_tableQueryReleaseByNet.setMenuLabel(StringResource.getStringResource("uiuforelease00005"));
		
		menuItem_ListConfig.setMenuLabel(StringResource.getStringResource("miufo1003701"));
		menuItem_multiSheetExcel.setMenuLabel(StringResource.getStringResource("miufopublic157"));
		menuItem_multiSheetPrint.setMenuLabel(StringResource.getStringResource("miufo1001332"));
		
		menuItem_xbrlInstanceGen.setMenuLabel(StringResource.getStringResource("uiufoadjustrep007"));
	}

	protected void initMenuBar() {
		if (menubar1 == null) {
			menubar1 = new WebMenubar();
			menubar1.add(getMenuItemQueryByRep());
			menubar1.add(getMenuItemQueryByTask());
			menubar1.add(getMenuInput());
			menubar1.add(getMenuReportCommit());;
			menubar1.add(getMenuRelease());			
			menubar1.add(getMenuItemTaskCheck());
			menubar1.add(getMenuItemRepCheck());
			menubar1.add(getMenuItemQueryCheck());
			menubar1.add(getMenuItemTotalSub());
			menubar1.add(getMenuItemSwBal());
			menubar1.add(getMenuItemRemove());
			menubar1.add(getMenuItemListConfig());
			menubar1.add(getMenuItemSheetExcel());
			menubar1.add(getMenuItemSheetPrint());
			
			menubar1.add(getMenuItemXBRLGenerate());
			setMenubar(menubar1);
		}		
		
	}

	protected WebMenuItem getMenuItemQueryByTask() {
		if (menuItem_tableQueryByTask==null){
			menuItem_tableQueryByTask=new WebMenuItem();
			ActionForward fwd=new ActionForward(TableQuerySimpleAction.class.getName(),"doViewByTask");
			menuItem_tableQueryByTask.setActionForward(fwd);
		}
		return menuItem_tableQueryByTask;	
	}
		
	protected WebMenuItem getMenuItemQueryByRep() {
		if (menuItem_tableQueryByRep==null){
			menuItem_tableQueryByRep=new WebMenuItem();
			ActionForward fwd=new ActionForward(TableQuerySimpleAction.class.getName(),"doViewByRep");
			menuItem_tableQueryByRep.setActionForward(fwd);
		}
		return menuItem_tableQueryByRep;	
	}	
	
	protected WebMenuItem getMenuItemTaskCheck(){
		if (menuItem_tableTaskCheck==null){
			menuItem_tableTaskCheck=new WebMenuItem();
			ActionForward fwd=new ActionForward(TableQuerySimpleAction.class.getName(),"checkTask");
			menuItem_tableTaskCheck.setActionForward(fwd);
			menuItem_tableTaskCheck.setSubmitType(WebMenuItem.TABLE_SUBMIT);
		}
		return menuItem_tableTaskCheck;
	}
	
	protected WebMenuItem getMenuItemRepCheck(){
		if (menuItem_tableRepCheck==null){
			menuItem_tableRepCheck=new WebMenuItem();
			ActionForward fwd=new ActionForward(TableQuerySimpleAction.class.getName(),"checkRep");
			menuItem_tableRepCheck.setActionForward(fwd);
			menuItem_tableRepCheck.setSubmitType(WebMenuItem.TABLE_SUBMIT);
		}
		return menuItem_tableRepCheck;
	}	
	
	protected WebMenuItem getMenuItemQueryCheck(){
		if (menuItem_QueryCheck==null){
			menuItem_QueryCheck=new WebMenuItem();
			ActionForward fwd=new ActionForward(CheckQueryAction.class.getName(),"");
			menuItem_QueryCheck.setActionForward(fwd);
			menuItem_QueryCheck.setSubmitType(WebMenuItem.SIMPLE_SUBMIT);
		}
		return menuItem_QueryCheck;
	}
	
	protected WebMenuItem getMenuItemTotalSub(){
		if (menuItem_totalSub==null){
			menuItem_totalSub=new WebMenuItem();
			ActionForward fwd=new ActionForward(TotalSubMngAction.class.getName(),"");
			menuItem_totalSub.setActionForward(fwd);
			menuItem_totalSub.setSubmitType(WebMenuItem.SIMPLE_SUBMIT);
		}
		return menuItem_totalSub;
	}
	
	
	
	protected WebMenu getMenuReportCommit(){
		if (menuReportCommit==null){
			menuReportCommit=new WebMenu();
			
			menuItem_tableReportCommit=new WebMenuItem();
			ActionForward fwd=new ActionForward(ReportCommitAction.class.getName(),"doOprtCommit");
			menuItem_tableReportCommit.setActionForward(fwd);
			menuItem_tableReportCommit.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			menuReportCommit.add(menuItem_tableReportCommit);
			
			menuItem_tableAskCancel=new WebMenuItem();
			fwd=new ActionForward(ReportCommitAction.class.getName(),"doAskForCancel");
			menuItem_tableAskCancel.setActionForward(fwd);
			menuItem_tableAskCancel.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			menuReportCommit.add(menuItem_tableAskCancel);
		}
		return menuReportCommit;
	}
	
	protected WebMenu getMenuInput(){
		if (menuInput==null){
			menuInput=new WebMenu();
			
			menuItemWebInput=new WebMenuItem();
			ActionForward fwd=new ActionForward(InputKeywordsEnterAction.class.getName(),IIUFOConstants.ACTION_METHOD_OPEN);
			menuItemWebInput.setActionForward(fwd);
			menuInput.add(menuItemWebInput);
			
			menuItemRepToolInput=new WebMenuItem();
			fwd=new ActionForward(TableInputAction.class.getName(),"");
			menuItemRepToolInput.setActionForward(fwd);
			menuInput.add(menuItemRepToolInput);
			
			menuItemAutoInput=new WebMenuItem();
			menuItemAutoInput.setMenuLabel("子公司上报报表录入");
			fwd=new ActionForward(AutoInputKeywordsAction.class.getName(),IIUFOConstants.ACTION_METHOD_OPEN);
			menuItemAutoInput.setActionForward(fwd);
			menuInput.add(menuItemAutoInput);
			
		}
		return menuInput;
	}

	protected WebMenuItem getMenuItemSwBal() {
		if (menuItem_tableSwBal == null) {
			menuItem_tableSwBal = new WebMenuItem();
			menuItem_tableSwBal.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			ActionForward fwd = new ActionForward(TableSwBalAction.class.getName(), "execute");
			fwd.addParameter(IFuncUniqueFlag.UNIQUE_ID,""+IQueryCondConstant.MODULE_REPORTQUERY);
			menuItem_tableSwBal.setActionForward(fwd);
		}
		return menuItem_tableSwBal;
	}
	
	protected WebMenuItem getMenuItemRemove(){
		if (menuItem_tableRemove==null){
			menuItem_tableRemove = new WebMenuItem();
			menuItem_tableRemove.setSubmitDel(true);
			menuItem_tableRemove.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			ActionForward fwd = new ActionForward(TableRemoveAction.class.getName(), "execute");
			menuItem_tableRemove.setActionForward(fwd);
		}
		return menuItem_tableRemove;
	}

	protected WebMenu getMenuRelease() {
		if (menuRelease == null) {
			menuRelease = new WebMenu();

			menuItem_tableQueryRelease = new WebMenuItem();
			menuItem_tableQueryRelease.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			ActionForward fwd = new ActionForward(InfoReleaseAction.class.getName(), "execute");
			fwd.addParameter(InfoReleaseAction.BUSINESS_REL_TYPE, String.valueOf(IReleaseRepType.QUERYREP));
			menuItem_tableQueryRelease.setActionForward(fwd);
			menuRelease.add(menuItem_tableQueryRelease);

			menuItem_tableQueryReleaseByNet = new WebMenuItem();
			menuItem_tableQueryReleaseByNet.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			fwd = new ActionForward(InfoReleaseByNetAction.class.getName(), "execute");
			fwd.addParameter(InfoReleaseAction.BUSINESS_REL_TYPE, String.valueOf(IReleaseRepType.QUERYREP));			
			menuItem_tableQueryReleaseByNet.setActionForward(fwd);
			menuRelease.add(menuItem_tableQueryReleaseByNet);
		}
		return menuRelease;
	}
	
	protected WebMenuItem getMenuItemListConfig(){
		if (menuItem_ListConfig==null){
			menuItem_ListConfig=new WebMenuItem();
			ActionForward fwd=new ActionForward(QueryListConfigAction.class.getName(),IIUFOConstants.ACTION_METHOD_OPEN);
			fwd.addParameter(QueryListConfigAction.PARAM_MODULE_TYPE,""+IQueryCondConstant.MODULE_REPORTQUERY);
			menuItem_ListConfig.setActionForward(fwd);		
		}
		return menuItem_ListConfig;
	}

	protected WebMenuItem getMenuItemSheetExcel(){
		if (menuItem_multiSheetExcel==null){
			menuItem_multiSheetExcel = new WebMenuItem();
			menuItem_multiSheetExcel.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			ActionForward fwd = new ActionForward(MultiSheetExcelAction.class.getName(), "execute");
			menuItem_multiSheetExcel.setActionForward(fwd);
		}
		return menuItem_multiSheetExcel;
	}
	protected WebMenuItem getMenuItemSheetPrint(){
		if (menuItem_multiSheetPrint==null){
			menuItem_multiSheetPrint = new WebMenuItem();
			menuItem_multiSheetPrint.setSubmitType(WebMenuItem.TABLE_SUBMIT);
//			ActionForward fwd = new ActionForward(MultiSheetPrintAction.class.getName(),"action");
			ActionForward fwd = new ActionForward(TablePrintAction.class.getName(),"execute");
			menuItem_multiSheetPrint.setActionForward(fwd);
		}
		return menuItem_multiSheetPrint;
	}
	
	protected WebMenuItem getMenuItemXBRLGenerate(){
		if(menuItem_xbrlInstanceGen == null){
			menuItem_xbrlInstanceGen = new WebMenuItem();
			menuItem_xbrlInstanceGen.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			ActionForward fwd = new ActionForward(XBRLInstanceGenAction.class.getName(), "");
			menuItem_xbrlInstanceGen.setActionForward(fwd);
		}
		return menuItem_xbrlInstanceGen;
	}
	
	protected void initStatusBar() {
		setStatusBar(ServerParamMngAction.getStatusBar(null,true,true));
	}	
}
