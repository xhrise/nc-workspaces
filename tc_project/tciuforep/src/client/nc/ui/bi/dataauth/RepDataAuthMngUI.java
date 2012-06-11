/**
 * RepDataAuthMngUI.java  5.0 
 * 
 * WebDeveloper自动生成.
 * 2006-04-17
 */
package nc.ui.bi.dataauth;

import nc.vo.bi.dataauth.IDataAuthConst;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebChoice;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.menu.WebMenuItem;
import com.ufida.web.comp.menu.WebMenubar;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.Element;
import com.ufida.web.window.WebMultiFrame;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 类作用描述文字
 * zyjun
 * 2006-04-17
 */
public class RepDataAuthMngUI extends WebMultiFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WebMenubar 	bar1 = null;
//    private WebToolBar 	toolBar1 = null;
    private WebLabel		lblDim = null;
    private WebChoice      choiceDim = null;
    private WebPanel		pnlDim = null;

    private WebMenuItem	menuItemCreate = null;
    private WebMenuItem	menuItemUpdate = null;
    private WebMenuItem	menuItemDelete = null;

    /**
     * 设置界面信息及处理多语言
     * 每次页面刷新时调用
     */
    protected void setData() throws WebException {
    	
    	getPnlDim();
    	
        setTitle(StringResource.getStringResource("ubiauth0021"));//报表数据权限管理界面"));
        //设置菜单名称
      	menuItemCreate.setMenuLabel(StringResource.getStringResource("miufopublic242"));//新建"));
    	menuItemUpdate.setMenuLabel(StringResource.getStringResource("miufopublic244"));//修改"));
    	menuItemDelete.setMenuLabel(StringResource.getStringResource("miufopublic243"));//删除"));
    	
        lblDim.setValue(StringResource.getStringResource("ubiauth0022"));//维度"));

        DataAuthMngForm		form = (DataAuthMngForm) getActionForm(DataAuthMngForm.class.getName());
        if( form != null ){
//        	menuItemCreate.getActionForward().addParameter(IDataAuthConst.DIMPK, form.getDimPK());
        	menuItemCreate.getActionForward().addParameter(IDataAuthConst.REPPK, form.getRepPK());
//        	menuItemUpdate.getActionForward().addParameter(IDataAuthConst.DIMPK, form.getDimPK());
        	menuItemUpdate.getActionForward().addParameter(IDataAuthConst.REPPK, form.getRepPK());
        	
        	choiceDim.setItems(form.getDimItems());
        	choiceDim.setValue(form.getDimPK());
        }
        
        setWindowWidth(800);
        setWindowHeight(600);
    	
    	super.setData();    	
    }
    

	protected Element genLeftTopHtml() {
		return getPnlDim();
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
//    protected void initToolBar(){
//        this.setToolBar(getPnlDim());	
//    }
        


    protected WebMenubar getBar1(){     
        if(bar1 == null){
            bar1 = new WebMenubar();
            bar1 = new WebMenubar();
            
            ActionForward	fwd = null;
            menuItemCreate = new WebMenuItem();
            fwd = new ActionForward(RepDataAuthEditAction.class.getName(),"create");
            menuItemCreate.setActionForward(fwd);
            menuItemCreate.setSubmitType(WebMenuItem.TREE_SUBMIT);    
            
            menuItemUpdate = new WebMenuItem();
            fwd = new ActionForward(RepDataAuthEditAction.class.getName(),"update");
            menuItemUpdate.setActionForward(fwd);
            menuItemUpdate.setSubmitType(WebMenuItem.TABLE_SUBMIT);        
            
            menuItemDelete = new WebMenuItem();
            fwd = new ActionForward(RepDataAuthMngAction.class.getName(),"delete");
            menuItemDelete.setActionForward(fwd);
            menuItemDelete.setSubmitType(WebMenuItem.TABLE_SUBMIT);    
            menuItemDelete.setSubmitDel(true);
            
            bar1.add(menuItemCreate);
            bar1.add(menuItemUpdate);
            bar1.add(menuItemDelete);
            
 
        }
        return bar1;
    }
    

//    protected WebToolBar getToolBar1(){     
//        if(toolBar1 == null){
//            toolBar1 = new WebToolBar();
//            toolBar1.addElement(getPnlDim());
// 
//        }
//        return toolBar1;
//    }
    
    private WebPanel getPnlDim(){
    	if( pnlDim == null ){
    		pnlDim = new WebPanel(1,2);
    		Area	area = new Area(1,1,1,1);
    		lblDim = new WebLabel();
    		pnlDim.add(lblDim, area);
    		
    		area = new Area(1,2,1,1);
    		choiceDim = new WebChoice();
    		choiceDim.setID("dimPK");
    		choiceDim.setName("dimPK");
    		choiceDim.setOnChange("ajaxTree(true);ajaxTable(null,true);");
    		pnlDim.add(choiceDim, area);
    	}
    	return pnlDim;
    }
    
    
    
    
	protected void initUI() {
		super.initUI();
		this.enableToolBar();
		//getDocument().appendOnLoad("ajaxTree(true);ajaxTable(null,true);");
	}
}
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <MultiFrameVO name="RepDataAuthMngUI" package="nc.ui.bi.dataauth">
      <WebMenubar name="bar1">
        <LayoutVO name="layout1" type="WebFlowLayout">
        </LayoutVO>
      </WebMenubar>
    </MultiFrameVO>
@WebDeveloper*/