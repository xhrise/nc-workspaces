/*
 * 创建日期 2006-7-4
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.segrep.segdef;

import nc.ui.iufo.function.FunctionRightHandler;
import nc.ui.iufo.release.IReleaseRepType;
import nc.ui.iufo.release.InfoReleaseAction;
import nc.ui.iufo.resmng.common.ResWebEnvKit;
import nc.ui.iufo.resmng.uitemplate.TreeTableMngUI;
import nc.util.segrep.resmng.ISRResMngConstants;

import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.menu.WebMenu;
import com.ufida.web.comp.menu.WebMenuItem;
import com.ufida.web.comp.menu.WebMenubar;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 *
 * 分部定义管理主界面
 */
public class SegDefMngUI extends TreeTableMngUI {
	private static final long serialVersionUID = 1L;
	private  	WebMenu		m_menuTool = null;
	private	WebMenuItem	m_menuCreateReport = null;
	private	WebMenuItem	m_menuViewReport = null;
	private	WebMenuItem	m_menuRelease = null;

	/* （非 Javadoc）
	 * @see nc.ui.iufo.resmng.uitemplate.TreeTableMngUI#getModuleTitle()
	 */
	protected String getModuleTitle() {
		return StringResource.getStringResource("usrdef0006");//"分部划分管理");
	}
	protected WebMenubar  getMenubar1(){
		WebMenubar		bar1 = super.getMenubar1();
		if( m_menuTool == null ){
			bar1.add(getMenuTool());
		}
		return bar1;
	}
	protected void  setData(){
		//IUFO5.3 增加根据权限显示菜单项状态的代码
		WebMenu[] webMenus = new WebMenu[]{menuDir,menuFile,menuAuth,m_menuTool};
		String[][] funcOrders = new String[][]{
				IFuncFlagSegDefMng.FUNCORDERS_DIR
				,IFuncFlagSegDefMng.FUNCORDERS_FILE
				,IFuncFlagSegDefMng.FUNCORDERS_AUTH
				,IFuncFlagSegDefMng.FUNCORDERS_TOOL};  
		//两种方案均可
        FunctionRightHandler.setMenuItemFuncOrders(funcOrders,webMenus);//方案1
//        IFuncForm funcForm = (IFuncForm)getActionForm(getFormName());        
//        FunctionRightHandler.setMenuItemEnables(funcForm,funcOrders,webMenus);//方案2
		
		super.setData();
		setMenuLabels();
	}
	private WebMenu	getMenuTool(){
		if( m_menuTool == null ){
			m_menuTool = new WebMenu();
			
			m_menuCreateReport = new WebMenuItem();
			ActionForward	fwd = new ActionForward(SegRepExecAction.class.getName(), "execute");
			ResWebEnvKit.addMenuOperParam(fwd,ISRResMngConstants.MODULE_SEGREP_DEF,
					WebGlobalValue.MenuType_Modify,WebGlobalValue.MENU_OPER_OBJ_DIR);
			m_menuCreateReport.setActionForward(fwd);
			m_menuCreateReport.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			
			m_menuViewReport = new WebMenuItem();
			fwd = new ActionForward(SegRepExecAction.class.getName(), "showData");
			m_menuViewReport.setActionForward(fwd);
			m_menuViewReport.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			
			m_menuRelease = new WebMenuItem();
			fwd = new ActionForward(InfoReleaseAction.class.getName(), "execute");
			fwd.addParameter(InfoReleaseAction.BUSINESS_REL_TYPE, Integer.toString(IReleaseRepType.SEGREP));
			m_menuRelease.setActionForward(fwd);
			m_menuRelease.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			
			m_menuTool.add(m_menuCreateReport);
			m_menuTool.add(m_menuViewReport);
			m_menuTool.add(m_menuRelease);
		}
		
		return m_menuTool;
	}
	
	private void setMenuLabels(){
		m_menuTool.setMenuLabel(StringResource.getStringResource("usrdef0035"));
		m_menuCreateReport.setMenuLabel(StringResource.getStringResource("usrdef0007"));//"生成分部报告"));
		m_menuViewReport.setMenuLabel(StringResource.getStringResource("usrdef0008"));//查看分部报告"));
		m_menuRelease.setMenuLabel(StringResource.getStringResource("usrdef0036"));//分布
	}
	
}
