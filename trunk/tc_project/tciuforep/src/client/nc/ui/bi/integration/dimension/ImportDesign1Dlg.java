/**
 * Dialog1.java  5.0 
 * WebDeveloper自动生成.
 * 2006-01-17
 */
package nc.ui.bi.integration.dimension;

import nc.ui.bi.web.reference.QueryRefAction;
import nc.vo.bi.integration.dimension.DimFldcontrastVO;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.WebCloseButton;
import com.ufida.web.comp.WebHiddenField;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.WebRadioGroup;
import com.ufida.web.comp.WebTextField;
import com.ufida.web.comp.WebTextRef;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.BR;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 类作用描述文字
 * syang
 * 2006-01-17
 */
public class ImportDesign1Dlg extends WebDialog {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @i18n mbidim00055=引入查询
	 */
    public final static String TITLE = StringResource.getStringResource("mbidim00055");
    /**
	 * @i18n miufopublic261=下一步
	 */
//    private final static String BTN_NEXT = StringResource.getStringResource("miufopublic261");


    private WebPanel dlgPane = null;
    private WebPanel fieldPane = null;
    //树控件
//    private WebTree m_tree;
    
    private BR br1 = null;
    private WebPanel btnPane = null;
    private WebButton btnSubmit = null;
    private WebCloseButton btnClose = null;
    private WebTextField oldQueryNameFld = null;
    private WebRadioGroup spliterRuleTypeRadio = null;
    private WebTextRef queryRefFld = null;
    private WebHiddenField m_hidden = null;
 
    /**
     * 设置页面表单数据和处理组件多语言
     * 每次页面刷新时调用
     * syang
     * 2006-01-17
     */
    protected void setData() throws WebException {
    	setTitle(TITLE);
                                  
		btnSubmit.setValue(StringResource.getStringResource("miufopublic261")); // "下一步"
		btnClose.setValue(StringResource.getStringResource("miufopublic247")); // "取消"

        //关联ActionForm数据
        ImportDataForm form = (ImportDataForm) getActionForm(ImportDataForm.class.getName());
        if(form == null){
            return;
        }
      //  m_tree.setModel(form.getQuery_model());
        m_hidden.setValue(form.getDimID());
        this.addHiddenField(new WebHiddenField("currentUIFlag", ImportDataAction.METHOD_IMPORT_DESIGN));
        
        getSpliterRuleTypeRadio().setValue(form.getSpliterRuleType());
     //   getOldQueryNameFld().setValue(form.getOldQueryName());
        if(form.getQueryID() != null){
        	getQueryRefFld().setValue(form.getQueryID());        
        	if(getQueryRefFld().getRefFld() instanceof WebTextField){
        		((WebTextField)getQueryRefFld().getRefFld()).setValue(form.getOldQueryName());
        	}
        //	getDocument().appendOnLoad("setTreeSelectedID('"+ form.getQueryID() + "', null);refreshTree(null,null);"); 	
        }
        
    }
       
    /**
     * 初始化页面组件
     * 只在servlet实例化时调用一次.
     * 此方法中不得涉及任何多语言处理     
     * syang
     * 2006-01-17
     */
    protected void initUI(){
        setWindowWidth(0);
        setWindowHeight(0);

        setContentPane(getDlgPane());
        
        m_hidden = new WebHiddenField("dimID");
        m_hidden.setID("dimID");
        this.addHiddenField(m_hidden);
	        
   }
   /**
 * @i18n mbiadhoc00035=查询对象
 * @i18n mbidim00034=拆分规则
 */
protected WebPanel getFieldPane(){     
        if(fieldPane == null){
            fieldPane = new WebPanel();
            fieldPane.setLayout(new WebGridLayout(2, 2));
            
         //   WebPanel pane = new WebPanel(2,2);
            fieldPane.add(new WebLabel(StringResource.getStringResource("mbiadhoc00035")), new Area(1,1,1,1));
//            pane.add(getOldQueryNameFld(), new Area(1,2,1,1));      
            fieldPane.add(getQueryRefFld(), new Area(1,2,1,1));               
           // fieldPane.add(pane, new Area(1,1,1,1));
            
            fieldPane.add(new WebLabel(StringResource.getStringResource("mbidim00034")), new Area(2,1,1,1));
            fieldPane.add(getSpliterRuleTypeRadio(), new Area(2,2,1,1));
        }
        return fieldPane;
    }
    

   /**
 * @i18n mbidim00056=编码规则
 * @i18n mbidim00057=层次规则(如父子关系)
 */
protected WebRadioGroup getSpliterRuleTypeRadio(){
	   if(spliterRuleTypeRadio == null){
		   spliterRuleTypeRadio = new WebRadioGroup();
		   spliterRuleTypeRadio.setID(ImportDataAction.SPLITER_RULE_TYPE);
		   spliterRuleTypeRadio.setName(ImportDataAction.SPLITER_RULE_TYPE);	
		   spliterRuleTypeRadio.setItems(new String[][] {
				   new String[] { String.valueOf(DimFldcontrastVO.SPLITER_BY_CODE), StringResource.getStringResource("mbidim00056")},
					new String[] { String.valueOf(DimFldcontrastVO.SPLITER_BY_LEVEL), StringResource.getStringResource("mbidim00057") }
				   });
	   }
	   return spliterRuleTypeRadio;
   }
    protected BR getBr1(){     
        if(br1 == null){
            br1 = new BR();
 
        }
        return br1;
    }
     protected WebButton getBtnSubmit(){     
        if(btnSubmit == null){
 
        }
        return btnSubmit;
    }
     protected WebTextField getOldQueryNameFld(){     
         if(oldQueryNameFld == null){
        	 oldQueryNameFld = new WebTextField();
        	 oldQueryNameFld.setDisabled(true);
        	 oldQueryNameFld.setReadOnly(true);
         }
         return oldQueryNameFld;
     }
     protected WebTextRef getQueryRefFld(){     
         if(queryRefFld == null){
        	 queryRefFld = new WebTextRef();
        	 queryRefFld.setID("queryID");
        	 queryRefFld.setName("queryID");
        	 queryRefFld.setActionForward(new ActionForward(QueryRefAction.class.getName(), ""));
         }
         return queryRefFld;
     }
     
    protected WebPanel getBtnPane(){     
        if(btnPane == null){
            btnPane = new WebPanel();
            btnPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.HORIZONTAL));
            btnSubmit = new WebButton();
            ActionForward fwd = new ActionForward(ImportDataAction.class.getName(),ImportDataAction.METHOD_IMPORT_DESIGN_NEXT);
            btnSubmit.setActionForward(fwd);
            btnPane.add(btnSubmit);
 
            btnClose = new WebCloseButton();
            btnPane.add(btnClose);
 
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
    

    
}
 