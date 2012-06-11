/**
 * DimDataAuthMngUI.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-04-17
 */
package nc.ui.bi.dataauth;

import nc.vo.bi.dataauth.IDataAuthConst;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.menu.WebMenuItem;
import com.ufida.web.comp.menu.WebMenubar;
import com.ufida.web.comp.menu.WebToolBar;
import com.ufida.web.window.WebMultiFrame;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 维度数据权限管理主界面
 * zyjun
 * 2006-04-17
 */
public class DimDataAuthMngUI extends WebMultiFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WebMenubar 	bar1 = null;
    private WebMenuItem	menuItemCreate = null;
    private WebMenuItem	menuItemUpdate = null;
    private WebMenuItem	menuItemDelete = null;
    private WebToolBar 	toolBar1 = null;

    /**
     * 设置界面信息及处理多语言
     * 每次页面刷新时调用
     */
    protected void setData() throws WebException {
    	
        setWindowWidth(800);
        setWindowHeight(600);
        setTitle(StringResource.getStringResource("ubiauth0020"));//维度数据权限管理界面"));
        setMenuItemLabels();
        
        DataAuthMngForm		form = (DataAuthMngForm) getActionForm(DataAuthMngForm.class.getName());
        if( form != null ){
        	menuItemCreate.getActionForward().addParameter(IDataAuthConst.DIMPK, form.getDimPK());
        	menuItemUpdate.getActionForward().addParameter(IDataAuthConst.DIMPK, form.getDimPK());
        }
    	
    	super.setData();    	
    }
    /**
     * 初始化菜单
     * 只在servlet实例化时调用一次.
     * 此方法中不得涉及多语言处理        
     */
    protected void initMenuBar() {       
        this.setMenubar(getBar1());  
    }
    
    /**
	 * 初始化工具栏
     * 只在servlet实例化时调用一次.
     * 此方法中不得涉及多语言处理         
	 */
    protected void initToolBar(){
        this.setToolBar(getToolBar1());	
    }
        
    protected WebMenubar getBar1(){     
        if(bar1 == null){
            bar1 = new WebMenubar();
            
            ActionForward	fwd = null;
            menuItemCreate = new WebMenuItem();
            fwd = new ActionForward(DimDataAuthEditAction.class.getName(),"create");
            menuItemCreate.setActionForward(fwd);
            menuItemCreate.setSubmitType(WebMenuItem.TREE_SUBMIT);
            
            menuItemUpdate = new WebMenuItem();
            fwd = new ActionForward(DimDataAuthEditAction.class.getName(),"update");
            menuItemUpdate.setActionForward(fwd);
            menuItemUpdate.setSubmitType(WebMenuItem.TABLE_SUBMIT);        
            
            menuItemDelete = new WebMenuItem();
            fwd = new ActionForward(DimDataAuthMngAction.class.getName(),"delete");
            menuItemDelete.setActionForward(fwd);
            menuItemDelete.setSubmitType(WebMenuItem.TABLE_SUBMIT);    
            menuItemDelete.setSubmitDel(true);
            
            bar1.add(menuItemCreate);
            bar1.add(menuItemUpdate);
            bar1.add(menuItemDelete);
            
        }
        return bar1;
    }
    

    protected WebToolBar getToolBar1(){     
        if(toolBar1 == null){
            toolBar1 = new WebToolBar();
 
        }
        return toolBar1;
    }
    

    private void setMenuItemLabels(){
       	menuItemCreate.setMenuLabel(StringResource.getStringResource("miufopublic242"));//新建"));
    	menuItemUpdate.setMenuLabel(StringResource.getStringResource("miufopublic244"));//修改"));
    	menuItemDelete.setMenuLabel(StringResource.getStringResource("miufopublic243"));//删除"));
    }
    
}
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <MultiFrameVO name="DimDataAuthMngUI" package="nc.ui.bi.dataauth">
      <WebMenubar name="bar1">
        <LayoutVO name="layout6" type="WebFlowLayout">
        </LayoutVO>
      </WebMenubar>
    </MultiFrameVO>
@WebDeveloper*/