/**
 * DataPolicyEditDlg.java  5.0 
 * WebDeveloper自动生成.
 * 2006-04-19
 */
package nc.ui.bi.dataauth;

import nc.ui.iufo.web.reference.base.RoleRefAction;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.WebChoice;
import com.ufida.web.comp.WebCloseButton;
import com.ufida.web.comp.WebHiddenField;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.WebListRef;
import com.ufida.web.comp.WebTextRef;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.BR;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 类作用描述文字
 * zyjun
 * 2006-04-19
 */
public class DataPolicyEditDlg extends WebDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WebPanel dlgPane = null;
    private WebPanel fieldPane = null;
    private WebLabel lblRole = null;
    private WebHiddenField hdDimPK = null;
    private WebHiddenField hdPK = null;
    private WebListRef listRefRole = null;
 
    private WebPanel pnlField = null;
    private WebLabel lblUserField = null;
    private WebLabel lblDimField = null;
    private WebChoice choiceUserFields = null;
    private WebChoice choiceDimFields = null;
    
    private DataAuthRulePanel pnlInclude = null;
    private DataAuthTypePanel pnlType = null;
    
    private BR br1 = null;
    private WebPanel btnPane = null;
    private WebButton btnSubmit = null;
    private WebCloseButton btnClose = null;


 
    /**
     * 设置页面表单数据和处理组件多语言
     * 每次页面刷新时调用
     * zyjun
     * 2006-04-19
     */
    protected void setData() throws WebException {        
 
        //关联ActionForm数据
        DataAuthEditForm form = (DataAuthEditForm) getActionForm(DataAuthEditForm.class.getName());
        if(form != null){
            setLabels(form);
            setValues(form);
            pnlInclude.setData(form);
            pnlType.setData(form);
        }
                   
    }
       
    /**
     * 初始化页面组件
     * 只在servlet实例化时调用一次.
     * 此方法中不得涉及任何多语言处理     
     * zyjun
     * 2006-04-18
     */
    protected void initUI(){
        setWindowWidth(800);
        setWindowHeight(600);

        setContentPane(getDlgPane());
	        
   }


    protected WebLabel getLblRole(){     
        if(lblRole == null){
        	lblRole = new WebLabel();
        }
        return lblRole;
    }
    

    protected WebHiddenField getHdDimPK(){     
        if(hdDimPK == null){
            hdDimPK = new WebHiddenField();
            hdDimPK.setName("dimPK");
            hdDimPK.setID("dimPK");
 
        }
        return hdDimPK;
    }
    

    
    protected WebHiddenField getHdPK(){     
        if(hdPK == null){
        	hdPK = new WebHiddenField();
        	hdPK.setName("authPK");
        	hdPK.setID("authPK");
 
        }
        return hdPK;
    }
    

    protected WebListRef getListRefRole(){     
        if(listRefRole == null){
        	listRefRole = new WebListRef();
        	listRefRole.setWidth(150);
        	listRefRole.setHeight(150);
        	listRefRole.setName("autheePKs");
        	listRefRole.setID("autheePKs");
        	listRefRole.setTextRef(new WebTextRef());
            ActionForward	fwd = new ActionForward(RoleRefAction.class.getName(),"execute");
            listRefRole.getTextRef().setActionForward(fwd);
        }
        return listRefRole;
    }
    
    private  WebPanel   getPnlField(){
    	if( pnlField == null ){
    		pnlField = new WebPanel(2,2);
    		Area 	area = new Area(1,1,1,1);

            lblUserField = new WebLabel();
            pnlField.add(lblUserField, area);
            area = new Area(1,2,1,1);
            pnlField.add(getChoiceUserField(), area);
            
            area = new Area(2,1,1,1);
            lblDimField = new WebLabel();
            pnlField.add(lblDimField, area);
            area = new Area(2,2,1,1);
            pnlField.add(getChoiceDimField(), area);
    	}
    	return pnlField;
    }
    private  WebChoice  getChoiceDimField(){
    	if( choiceDimFields == null ){
    		choiceDimFields = new WebChoice();
    		choiceDimFields.setID("dimField");
    		choiceDimFields.setName("dimField");
    	}
    	return choiceDimFields;
    }
    
    private  WebChoice  getChoiceUserField(){
    	if( choiceUserFields == null ){
    		choiceUserFields = new WebChoice();
    		choiceUserFields.setID("userField");
    		choiceUserFields.setName("userField");
    	}
    	return choiceUserFields;
    }

    
    protected WebPanel getFieldPane(){     
        if(fieldPane == null){
            fieldPane = new WebPanel();
            fieldPane.setLayout(new WebGridLayout(7, 3));
            Area area = null;
            area = new Area(1, 1, 1, 1);
            fieldPane.add(getLblRole(), area);
            area = new Area(1, 2, 1, 1);
            fieldPane.add(getHdDimPK(), area);
            area = new Area(2, 1, 2, 1);
            fieldPane.add(getListRefRole(), area);
            area = new Area(2, 3, 1, 1);
            fieldPane.add(getHdPK(), area);
            
            area = new Area(3,1,1,1);
            fieldPane.add(new BR(), area);
            
            area = new Area(4,1,1,1);
            fieldPane.add(getPnlField(), area);

            area = new Area(5,1,1,1);
            fieldPane.add(new BR(), area);
            

            area = new Area(6, 1, 1, 1);
            pnlInclude = new DataAuthRulePanel();
            fieldPane.add(pnlInclude, area);
            
            area = new Area(7, 1, 1, 1);
            pnlType = new DataAuthTypePanel();
            fieldPane.add(pnlType, area);

 
        }
        return fieldPane;
    }
    

    protected BR getBr1(){     
        if(br1 == null){
            br1 = new BR();
 
        }
        return br1;
    }
    

    protected WebButton getBtnSubmit(){     
        if(btnSubmit == null){
            btnSubmit = new WebButton();
            ActionForward fwd = new ActionForward(DataPolicyEditAction.class.getName(),"save");
            btnSubmit.setActionForward(fwd);
 
        }
        return btnSubmit;
    }
    

    protected WebCloseButton getBtnClose(){     
        if(btnClose == null){
            btnClose = new WebCloseButton();
 
        }
        return btnClose;
    }
    

    protected WebPanel getBtnPane(){     
        if(btnPane == null){
            btnPane = new WebPanel();
            btnPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.HORIZONTAL));
            btnPane.add(getBtnSubmit());
            btnPane.add(getBtnClose());
 
        }
        return btnPane;
    }
    

    protected WebPanel getDlgPane(){     
        if(dlgPane == null){
            dlgPane = new WebPanel();
            dlgPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.VERTICAL));
            dlgPane.add(getFieldPane());
            dlgPane.add(getBr1());
            dlgPane.add(getBtnPane());
 
        }
        return dlgPane;
    }
    
    private void setLabels(DataAuthEditForm form){
    	
    	if( form.isUpdate() ){
    		setTitle(StringResource.getStringResource("ubiauth0012"));//修改数据安全策略"));
    	}else{
    		setTitle(StringResource.getStringResource("ubiauth0011"));//新建数据安全策略"));
    	}
    	listRefRole.setLang(form.getLangCode());
    	
    	lblRole.setValue(StringResource.getStringResource("ubiauth0013"));//选择角色"));
    	lblDimField.setValue(StringResource.getStringResource("ubiauth0014"));//维度字段"));
    	lblUserField.setValue(StringResource.getStringResource("ubiauth0015"));//用户字段"));
    	
    	btnSubmit.setValue(StringResource.getStringResource("miufo1003675"));//确定"));
    	btnClose.setValue(StringResource.getStringResource("miufo1000764"));//取消"));
    	
    	
    }
    private void setValues(DataAuthEditForm form){
        hdDimPK.setValue(form.getDimPK());
        hdPK.setValue(form.getAuthPK());
        listRefRole.getList().setItems(form.getAutheeListItems());
        choiceDimFields.setItems(form.getDimFieldItems());
        choiceUserFields.setItems(form.getUserFieldItems());
        choiceDimFields.setValue(form.getDimField());
        choiceUserFields.setValue(form.getUserField());
        
        if( form.isUpdate() ){
        	listRefRole.getList().setDiableBtn(true);
        }
    }
    
    
}
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <DialogVO name="DataPolicyEditDlg" package="nc.ui.bi.dataauth" 关联Form="nc.ui.bi.dataauth.DataPolicyEditForm">
      <WebPanel FlowDirection="VERTICAL" layout="WebFlowLayout" name="dlgPane">
        <LayoutVO name="layout1" type="WebFlowLayout">
          <WebPanel col="0" name="fieldPane" row="0">
            <LayoutVO name="layout1" type="WebGridLayout">
            </LayoutVO>
          </WebPanel>
          <BR col="1" name="br1" row="0">
          </BR>
          <WebPanel col="2" layout="WebFlowLayout" name="btnPane" row="0">
            <LayoutVO name="layout2" type="WebFlowLayout">
              <WebButton col="0" name="btnSubmit" row="0">
              </WebButton>
              <WebCloseButton col="1" name="btnClose" row="0">
              </WebCloseButton>
            </LayoutVO>
          </WebPanel>
        </LayoutVO>
      </WebPanel>
    </DialogVO>
@WebDeveloper*/