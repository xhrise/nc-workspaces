/**
 * DimDataAuthEditDlg.java  5.0 
 * WebDeveloper�Զ�����.
 * 2006-04-18
 */
package nc.ui.bi.dataauth;

import nc.ui.iufo.web.reference.base.UserRefAction;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebButton;
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
 * ά������Ȩ���½�/�޸ĶԻ���
 * zyjun
 * 2006-04-18
 */
public class DimDataAuthEditDlg extends WebDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private WebPanel dlgPane = null;
    private WebPanel fieldPane = null;
    private WebLabel lblUser = null;
    private WebHiddenField hdDimPK = null;
    private WebHiddenField hdMemberPK = null;
    private WebHiddenField hdDataAuthPK = null;
    private WebHiddenField	hdRepPK = null;
    private WebListRef listRefUser = null;
 
    private DataAuthRulePanel 	pnlInclude = null;
    private DataAuthTypePanel  pnlType = null;
    
    private BR br1 = null;
    private WebPanel btnPane = null;
    private WebButton btnSubmit = null;
    private WebCloseButton btnClose = null;

 
    /**
     * ����ҳ������ݺʹ������������
     * ÿ��ҳ��ˢ��ʱ����
     * zyjun
     * 2006-04-18
     */
    protected void setData() throws WebException {        
                                  
        //����ActionForm����
        DataAuthEditForm form = (DataAuthEditForm) getActionForm(DataAuthEditForm.class.getName());
        if(form != null){
            setLabels(form);
            setValues(form);
            pnlInclude.setData(form);
            pnlType.setData(form);
        }
                   
    }
       
    /**
     * ��ʼ��ҳ�����
     * ֻ��servletʵ����ʱ����һ��.
     * �˷����в����漰�κζ����Դ���     
     * zyjun
     * 2006-04-18
     */
    protected void initUI(){
        setWindowWidth(800);
        setWindowHeight(600);

        setContentPane(getDlgPane());
	        
   }


    protected WebLabel getLblUser(){     
        if(lblUser == null){
            lblUser = new WebLabel();
 
        }
        return lblUser;
    }
    

    protected WebHiddenField getHdDimPK(){     
        if(hdDimPK == null){
            hdDimPK = new WebHiddenField();
            hdDimPK.setName("dimPK");
            hdDimPK.setID("dimPK");
 
        }
        return hdDimPK;
    }
    

    protected WebHiddenField getHdMemberPK(){     
        if(hdMemberPK == null){
            hdMemberPK = new WebHiddenField();
            hdMemberPK.setName("dimMemberPK");
            hdMemberPK.setID("dimMemberPK");
 
        }
        return hdMemberPK;
    }
    
    protected WebHiddenField getHdDataAuthPK(){     
        if(hdDataAuthPK == null){
        	hdDataAuthPK = new WebHiddenField();
        	hdDataAuthPK.setName("authPK");
        	hdDataAuthPK.setID("authPK");
 
        }
        return hdDataAuthPK;
    }
    
    protected WebHiddenField getHdRepPK(){     
        if(hdRepPK == null){
        	hdRepPK = new WebHiddenField();
        	hdRepPK.setName("repPK");
        	hdRepPK.setID("repPK");
 
        }
        return hdRepPK;
    }

    protected WebListRef getListRefUser(){     
        if(listRefUser == null){
            listRefUser = new WebListRef();
            listRefUser.setWidth(150);
            listRefUser.setHeight(150);
            listRefUser.setName("autheePKs");
            listRefUser.setID("autheePKs");
            listRefUser.setTextRef(new WebTextRef());
            ActionForward	fwd = new ActionForward(UserRefAction.class.getName(),"execute");
            listRefUser.getTextRef().setActionForward(fwd);
        }
        return listRefUser;
    }
    
    protected WebPanel getFieldPane(){     
        if(fieldPane == null){
            fieldPane = new WebPanel();
            fieldPane.setLayout(new WebGridLayout(4, 3));
            Area area = null;
            area = new Area(1, 1, 1, 1);
            fieldPane.add(getLblUser(), area);
            area = new Area(1, 2, 1, 1);
            fieldPane.add(getHdDimPK(), area);
            area = new Area(1, 3, 1, 1);
            fieldPane.add(getHdMemberPK(), area);
            area = new Area(2, 1, 2, 1);
            fieldPane.add(getListRefUser(), area);
            area = new Area(2, 3, 1, 1);
            fieldPane.add(getHdDataAuthPK(), area);

            area = new Area(3, 1, 1, 1);
            pnlInclude = new DataAuthRulePanel();
            fieldPane.add(pnlInclude, area);
            
            pnlType = new DataAuthTypePanel();
            area = new Area(4, 1, 1, 1);
            fieldPane.add(pnlType, area);
            
            area = new Area(4,2,1,1);
            fieldPane.add(getHdRepPK(), area);
 
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
            ActionForward fwd = new ActionForward(DimDataAuthEditAction.class.getName(),"save");
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
    		setTitle(StringResource.getStringResource("ubiauth0024"));//�޸�ά������Ȩ��"));
    	}else{
    		setTitle(StringResource.getStringResource("ubiauth0023"));//�½�ά������Ȩ��"));
    	}
    	listRefUser.setLang(form.getLangCode());
    	
    	lblUser.setValue(StringResource.getStringResource("ubiauth0025"));//ѡ���û�"));
    	

    	btnSubmit.setValue(StringResource.getStringResource("miufo1003675"));//ȷ��"));
    	btnClose.setValue(StringResource.getStringResource("miufo1000764"));//ȡ��"));
    	
    	
    }
    private void setValues(DataAuthEditForm form){
        hdDimPK.setValue(form.getDimPK());
        hdMemberPK.setValue(form.getDimMemberPK());
        hdDataAuthPK.setValue(form.getAuthPK());
        hdRepPK.setValue(form.getRepPK());
        
        //�����Ƿ��б���У�,����btnSubmit��action
        if( form.getRepPK() != null ){
        	btnSubmit.setActionForward(new ActionForward(RepDataAuthEditAction.class.getName(),"save"));
        }else{
        	btnSubmit.setActionForward(new ActionForward(DimDataAuthEditAction.class.getName(),"save"));
        }
        listRefUser.getList().setItems(form.getAutheeListItems());
        if( form.isUpdate() ){
        	listRefUser.getList().setDiableBtn(true);
        }
    }
    

    
}
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <DialogVO name="DimDataAuthEditDlg" package="nc.ui.bi.dataauth" ����Form="nc.ui.bi.dataauth.DimDataAuthEditForm">
      <WebPanel FlowDirection="VERTICAL" layout="WebFlowLayout" name="dlgPane">
        <LayoutVO name="layout7" type="WebFlowLayout">
          <WebPanel col="0" name="fieldPane" row="0">
            <LayoutVO name="layout1" type="WebGridLayout">
              <WebLabel col="0" name="lblUser" row="0">
              </WebLabel>
              <WebHiddenField col="1" name="hdDimPK" row="0" ����Form����="dimPK">
              </WebHiddenField>
              <WebHiddenField col="2" name="hdMemberPK" row="0" ����Form����="dimMemberPK">
              </WebHiddenField>
              <WebList col="0" name="listUser" row="1" ����Form����="strUserPKs">
              </WebList>
              <WebPanel col="1" name="pnlUserSelBtns" row="1">
                <LayoutVO name="layout2" type="WebGridLayout">
                  <WebButton col="0" name="btn1" row="0">
                  </WebButton>
                  <WebButton col="0" name="btnDel" row="1">
                  </WebButton>
                  <WebButton col="0" name="btnClearAll" row="2">
                  </WebButton>
                </LayoutVO>
              </WebPanel>
              <WebLabel col="0" name="lblRuleInclude" row="2">
              </WebLabel>
              <WebLabel col="1" name="lblType" row="2">
              </WebLabel>
              <WebPanel col="0" name="pnlInclude" row="3">
                <LayoutVO name="layout3" type="WebGridLayout">
                  <WebCheckBox col="0" name="checkBoxSelf" row="0" ����Form����="includeSelf">
                  </WebCheckBox>
                  <WebCheckBox col="1" name="checkBoxSlibe" row="0" ����Form����="includeSlibe">
                  </WebCheckBox>
                  <WebCheckBox col="0" name="checkBoxParent" row="1" ����Form����="includeParent">
                  </WebCheckBox>
                  <WebCheckBox col="1" name="checkBoxChild" row="1" ����Form����="includeChild">
                  </WebCheckBox>
                  <WebCheckBox col="0" name="checkBoxAncestor" row="2" ����Form����="includeAncestor">
                  </WebCheckBox>
                  <WebCheckBox col="1" name="checkBoxOffSpring" row="2" ����Form����="includeOffSpring">
                  </WebCheckBox>
                </LayoutVO>
              </WebPanel>
              <WebPanel col="1" name="pnlType" row="3">
                <LayoutVO name="layout4" type="WebGridLayout">
                  <WebRadioButton col="0" name="btnRadioType" row="0" ����Form����="type">
                  </WebRadioButton>
                </LayoutVO>
              </WebPanel>
            </LayoutVO>
          </WebPanel>
          <BR col="1" name="br1" row="0">
          </BR>
          <WebPanel col="2" layout="WebFlowLayout" name="btnPane" row="0">
            <LayoutVO name="layout8" type="WebFlowLayout">
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