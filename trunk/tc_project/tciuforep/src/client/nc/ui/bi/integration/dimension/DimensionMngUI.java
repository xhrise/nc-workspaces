/*
 * 创建日期 2006-3-23
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.bi.integration.dimension;

import nc.ui.bi.dataauth.DataPolicyMngAction;
import nc.ui.bi.dataauth.DimDataAuthMngAction;
import nc.ui.iufo.authorization.AuthorizUIToolkit;
import nc.ui.iufo.function.FunctionRightHandler;
import nc.ui.iufo.resmng.common.ResWebEnvKit;
import nc.ui.iufo.resmng.uitemplate.TreeTableMngUI;
import nc.util.bi.resmng.IBIResMngConstants;

import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.menu.WebMenu;
import com.ufida.web.comp.menu.WebMenuItem;
import com.ufida.web.comp.menu.WebMenubar;
import com.ufida.web.util.WebGlobalValue;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 *
 * 维度管理主界面
 */
public class DimensionMngUI extends TreeTableMngUI {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private 	boolean	m_bMenuAdded = false;
	
	private    WebMenu		m_menuTool = null;
	private    WebMenu     m_menuInfo = null;
	private    WebMenu		m_menuRight = null;
	
	//private    WebMenuItem		m_menuItemLevelDef = null;
	private    WebMenuItem		m_menuItemMember = null;
	
	private    WebMenuItem		m_menuItemResAuth = null;
	private    WebMenuItem		m_menuItemDataAuth = null;
	private    WebMenuItem		m_menuItemPolicy = null;
	private    WebMenuItem      m_menuItemExPropList = null;
	private    WebMenuItem      m_menuItemExPropMng = null;
	
	private    WebMenuItem		m_menuItemImportDef = null;
	private    WebMenuItem		m_menuItemImport = null;
//	private    WebMenuItem		m_menuItemImportXML = null;
//	private    WebMenuItem		m_menuItemExportXML = null;
	
	/* （非 Javadoc）
	 * @see nc.ui.iufo.resmng.uitemplate.TreeTableMngUI#getModuleTitle()
	 */
	protected String getModuleTitle() {
		return StringResource.getStringResource("ubidim0004");//"维度管理"
	}
	protected WebMenubar	getMenubar1(){
		WebMenubar   menubar1 = super.getMenubar1();
		if( menubar1 != null ){
			if( m_bMenuAdded == false ){
				menubar1.add(getMenuInfo());
				menubar1.add(getMenuRight());
				menubar1.add(getMenuTool());
				m_bMenuAdded = true;
			}
		}
		return menubar1;
	}
	 protected WebMenuItem getMenuItemExPropMng(){     
	        if(m_menuItemExPropMng == null){
	        	m_menuItemExPropMng = new WebMenuItem();
	        	m_menuItemExPropMng.setSubmitType(WebMenuItem.TABLE_SUBMIT);
	        	ActionForward	fwd = new ActionForward(DimensionMngAction.class.getName(),
	        			"toExPropMng");
	        	m_menuItemExPropMng.setActionForward(fwd);
	        }
	        return m_menuItemExPropMng;
	    }
	    

	    protected WebMenuItem getMenuItemExPropList(){     
	        if(m_menuItemExPropList == null){
//	        	m_menuItemExPropList = ExPropMenuToolkit.geneMenuItemExPropList(getExPropModuleID());
	        	m_menuItemExPropList = new WebMenuItem();
	        	m_menuItemExPropList.setSubmitType(WebMenuItem.TABLE_SUBMIT);
	        	ActionForward	fwd = new ActionForward(DimensionMngAction.class.getName(),
    			"toExPropList");
	        	m_menuItemExPropList.setActionForward(fwd);
	         }
	        return m_menuItemExPropList;
	    }
	    
	private WebMenu	getMenuInfo(){
		if( m_menuInfo == null ){
			m_menuInfo = new WebMenu();
			
			ActionForward	fwd = null;
			
		//	m_menuItemLevelDef = new WebMenuItem();
			fwd = null;
			//m_menuItemLevelDef.setActionForward(fwd);
			//m_menuItemLevelDef.setSubmitType(WebMenuItem.TABLE_SUBMIT);
			
			m_menuItemMember = new WebMenuItem();
			fwd = new ActionForward(MemberMngAction.class.getName(), MemberMngAction.METHOD_EXECUTE);
			m_menuItemMember.setActionForward(fwd);
			m_menuItemMember.setSubmitType(WebMenuItem.TABLE_SUBMIT);			
			
			//m_menuInfo.add(m_menuItemLevelDef);
			m_menuInfo.add(m_menuItemMember);
			m_menuInfo.add(getMenuItemExPropMng());
		//	m_menuInfo.add(getMenuItemExPropList());
		}
		
		return m_menuInfo;
	}
	
	private WebMenu	getMenuRight(){
		if( m_menuRight == null ){
			m_menuRight = new WebMenu();
			ActionForward	fwd = null;
			
			m_menuItemResAuth = new WebMenuItem();
			fwd = AuthorizUIToolkit.getAuthResActFwd(IBIResMngConstants.MODULE_DIMENSION);
			m_menuItemResAuth.setActionForward(fwd);
			m_menuItemResAuth.setSubmitType(WebMenuItem.TREE_SUBMIT);			

			m_menuItemDataAuth = new WebMenuItem();
			fwd = new ActionForward(DimDataAuthMngAction.class.getName(),"execute");
			m_menuItemDataAuth.setActionForward(fwd);
			m_menuItemDataAuth.setSubmitType(WebMenuItem.TABLE_SUBMIT);		
			
			m_menuItemPolicy = new WebMenuItem();
			fwd = new ActionForward(DataPolicyMngAction.class.getName(), "execute");
			m_menuItemPolicy.setActionForward(fwd);
			m_menuItemPolicy.setSubmitType(WebMenuItem.TABLE_SUBMIT);	
			
			m_menuRight.add(m_menuItemResAuth);
			m_menuRight.add(m_menuItemDataAuth);
			m_menuRight.add(m_menuItemPolicy);
			
		}
		
		return m_menuRight;
	}
	
	private WebMenu	getMenuTool(){
		if( m_menuTool == null ){
			m_menuTool = new WebMenu();
			ActionForward	fwd = null;
			
			//只有修改权限才能导入设置和导入
			m_menuItemImportDef = new WebMenuItem();
			fwd = new ActionForward(ImportDataAction.class.getName(), ImportDataAction.METHOD_IMPORT_DESIGN,true);
			ResWebEnvKit.addMenuOperParam(fwd,IBIResMngConstants.MODULE_DIMENSION,
					WebGlobalValue.MenuType_Modify,WebGlobalValue.MENU_OPER_OBJ_FILE);
			m_menuItemImportDef.setActionForward(fwd);
			m_menuItemImportDef.setSubmitType(WebMenuItem.TABLE_SUBMIT);	
			
			m_menuItemImport = new WebMenuItem();
			fwd = new ActionForward(ImportDataAction.class.getName(), ImportDataAction.METHOD_IMPORT_DATA);
			ResWebEnvKit.addMenuOperParam(fwd,IBIResMngConstants.MODULE_DIMENSION,
					WebGlobalValue.MenuType_Modify,WebGlobalValue.MENU_OPER_OBJ_FILE);
			m_menuItemImport.setActionForward(fwd);
			m_menuItemImport.setSubmitType(WebMenuItem.TABLE_SUBMIT);	
			
//			m_menuItemImportXML = new WebMenuItem();
//			fwd = null;
//			m_menuItemImportXML.setActionForward(fwd);
//			m_menuItemImportXML.setSubmitType(WebMenuItem.TABLE_SUBMIT);				
//
//			m_menuItemExportXML = new WebMenuItem();
//			fwd = null;
//			m_menuItemExportXML.setActionForward(fwd);
//			m_menuItemExportXML.setSubmitType(WebMenuItem.TABLE_SUBMIT);	
			
			m_menuTool.add(m_menuItemImportDef);
			m_menuTool.add(m_menuItemImport);
//			m_menuTool.add(m_menuItemExportXML);
//			m_menuTool.add(m_menuItemImportXML);
			
		}
		return m_menuTool;
	}
	
	protected void clear(){
		super.clear();
		
		m_menuItemExPropMng = null;
		m_menuItemExPropList = null;
		
		m_bMenuAdded = false;
		m_menuTool = null;
		m_menuInfo = null;
		m_menuRight = null;
		
		//m_menuItemLevelDef = null;

		m_menuItemMember = null;
		
		m_menuItemResAuth = null;
		m_menuItemDataAuth = null;
		m_menuItemPolicy = null;
		
		m_menuItemImportDef = null;
		m_menuItemImport = null;
//		m_menuItemImportXML = null;
//		m_menuItemExportXML = null;		
	}
	
	private  void setMenuLabels(){
		m_menuTool.setMenuLabel(StringResource.getStringResource("usrdef0035")) ;//工具
		m_menuInfo.setMenuLabel(StringResource.getStringResource("ubdim0013")) ;//信息维护
		m_menuRight.setMenuLabel(StringResource.getStringResource("ubdim0014")) ;//授权
		
		//m_menuItemLevelDef.setMenuLabel(StringResource.getStringResource("ubdim0015")) ;//层次定义
		m_menuItemMember.setMenuLabel(StringResource.getStringResource("ubdim0016")) ;//成员维护
		m_menuItemExPropMng.setMenuLabel(StringResource.getStringResource("ubdim0017")) ;//维度结构管理
	//	m_menuItemExPropList.setMenuLabel(StringResource.getStringResource("ubdim0018")) ;//列表定制
		
		
		m_menuItemResAuth.setMenuLabel(StringResource.getStringResource("ubdim0019")) ;//资源授权
		m_menuItemDataAuth.setMenuLabel(StringResource.getStringResource("ubdim0020")) ;//数据授权
		m_menuItemPolicy.setMenuLabel(StringResource.getStringResource("ubdim0021")) ;//数据安全策略
		
		m_menuItemImportDef.setMenuLabel(StringResource.getStringResource("ubdim0022")) ;//导入设置
		m_menuItemImport.setMenuLabel(StringResource.getStringResource("ubdim0023")) ;//导入
//		m_menuItemImportXML.setMenuLabel(StringResource.getStringResource("ubdim0024")) ;//导入XML
//		m_menuItemExportXML.setMenuLabel(StringResource.getStringResource("ubdim0025")) ;	//导出XML	
	}
	
	protected void  setData(){
		//IUFO5.3 增加设置菜单项的FuncOrder
		WebMenu[] webMenus = new WebMenu[]{menuDir,menuFile,m_menuInfo,m_menuRight,m_menuTool};
		String[][] funcOrders = new String[][]{
				IFuncFlagDimensionMng.FUNCORDERS_DIR
				,IFuncFlagDimensionMng.FUNCORDERS_FILE
				,IFuncFlagDimensionMng.FUNCORDERS_INFO
				,IFuncFlagDimensionMng.FUNCORDERS_AUTH
				,IFuncFlagDimensionMng.FUNCORDERS_TOOL};     
        FunctionRightHandler.setMenuItemFuncOrders(funcOrders,webMenus);
        
		super.setData();
		setMenuLabels();
	}
	

}
