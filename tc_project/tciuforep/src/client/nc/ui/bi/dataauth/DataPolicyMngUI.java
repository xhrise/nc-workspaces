/**
 * DataPolicyMngUI.java  5.0 
 * 
 * WebDeveloper�Զ�����.
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
 * ��������������
 * zyjun
 * 2006-04-17
 */
public class DataPolicyMngUI extends WebMultiFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WebMenubar 	bar1 = null;
    private WebMenuItem	menuItemCreate = null;
    private WebMenuItem	menuItemUpdate = null;
    private WebMenuItem	menuItemDelete = null;
    
    private WebToolBar toolBar1 = null;


    /**
     * ���ý�����Ϣ�����������
     * ÿ��ҳ��ˢ��ʱ����
     */
    protected void setData() throws WebException {
    	
    	super.setData();
        setWindowWidth(800);
        setWindowHeight(600);
        setTitle(StringResource.getStringResource("ubiauth0019"));//���ݰ�ȫ���Թ���������"));
        setMenuItemLabels();
        
        DataAuthMngForm		form = (DataAuthMngForm) getActionForm(DataAuthMngForm.class.getName());
        if( form != null ){
        	menuItemCreate.getActionForward().addParameter(IDataAuthConst.DIMPK, form.getDimPK());
        	menuItemUpdate.getActionForward().addParameter(IDataAuthConst.DIMPK, form.getDimPK());
        }
    	
    	disableTree();
    	    	
    }
    /**
     * ��ʼ���˵�
     * ֻ��servletʵ����ʱ����һ��.
     * �˷����в����漰�����Դ���        
     */
    protected void initMenuBar() {       
        this.setMenubar(getBar1());  
    }
    
    /**
	 * ��ʼ��������
     * ֻ��servletʵ����ʱ����һ��.
     * �˷����в����漰�����Դ���         
	 */
    protected void initToolBar(){
        this.setToolBar(getToolBar1());	
    }
        


    protected WebMenubar getBar1(){     
        if(bar1 == null){
            bar1 = new WebMenubar();
            
            ActionForward	fwd = null;
            menuItemCreate = new WebMenuItem();
            fwd = new ActionForward(DataPolicyEditAction.class.getName(),"create");
            menuItemCreate.setActionForward(fwd);
            
            menuItemUpdate = new WebMenuItem();
            fwd = new ActionForward(DataPolicyEditAction.class.getName(),"update");
            menuItemUpdate.setActionForward(fwd);
            menuItemUpdate.setSubmitType(WebMenuItem.TABLE_SUBMIT);        
            
            menuItemDelete = new WebMenuItem();
            fwd = new ActionForward(DataPolicyMngAction.class.getName(),"delete");
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
    	menuItemCreate.setMenuLabel(StringResource.getStringResource("miufopublic242"));//�½�"));
    	menuItemUpdate.setMenuLabel(StringResource.getStringResource("miufopublic244"));//�޸�"));
    	menuItemDelete.setMenuLabel(StringResource.getStringResource("miufopublic243"));//ɾ��"));
    }
    
}
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <MultiFrameVO name="DataPolicyMngUI" package="nc.ui.bi.dataauth">
      <WebMenubar name="bar1">
        <LayoutVO name="layout1" type="WebFlowLayout">
        </LayoutVO>
      </WebMenubar>
    </MultiFrameVO>
@WebDeveloper*/