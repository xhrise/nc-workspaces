/**
 * AbstractInputKeywordsUI1.java  5.0 
 * WebDeveloper自动生成.
 * 2006-02-23
 */
package nc.ui.iufo.input;
import com.ufida.iufo.pub.tools.AppDebug;

import java.util.HashMap;

import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.web.reference.BusinessRefAction;
import nc.ui.iufo.web.reference.base.BDRefAction;
import nc.vo.iufo.code.CodeVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.task.TaskDefaultVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iufo.user.UserInfoVO;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.action.CloseForward;
import com.ufida.web.comp.Align;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebButton;
import com.ufida.web.comp.WebDateRef;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.WebTextField;
import com.ufida.web.comp.WebTextRef;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.BR;
import com.ufida.web.html.Element;
import com.ufida.web.html.HR;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * 录入关键字、导入IUFO数据界面基类Action
 * weixl
 * 2006-02-23
 */
public class AutoAbstractInputKeywordsUI extends WebDialog {
	private static final long serialVersionUID = 5605385728440350282L;
	
	private WebPanel dlgPane = null;
    private BR br1 = null;
    private WebPanel btnPane = null;
    private WebButton btnSubmit = null;
    private WebButton btnCancel = null;

 
    /**
     * 设置页面表单数据和处理组件多语言
     * 每次页面刷新时调用
     * weixl
     * 2006-02-23
     */
    protected void setData() throws WebException {                            
        getBtnCancel().setValue(StringResource.getStringResource("miufopublic247"));
        getBtnSubmit().setValue(StringResource.getStringResource("miufopublic246"));

        //关联ActionForm数据
        nc.ui.iufo.input.InputKeywordsForm form = (nc.ui.iufo.input.InputKeywordsForm) getActionForm(nc.ui.iufo.input.InputKeywordsForm.class.getName());
        if(form == null){
            return;
        }
        setTitle(doGetUITitle(form));
        
        getDlgPane().add(getFieldPane(form),new Area(1,1,1,1,new Align(Align.CENTER)));
        
        ActionForward fwd=getSubmitButtonForward(form);
        if (form.getPubDataVO()!=null && form.getPubDataVO().getUnitPK()!=null)
        	fwd.addParameter(AutoAbstractInputKeywordsAction.PARAM_OLD_UNITID,form.getPubDataVO().getUnitPK());
        
        try{
        	CSomeParam bakParam=(CSomeParam)form.getSomeParam().clone();
        	bakParam.setFrom(CSomeParam.FROM_CHANGEREP);
        	bakParam.setOperate(CSomeParam.OPERATE_NONE);
        	bakParam.getLinkString(fwd);
        }
        catch(Exception e){
        	AppDebug.debug(e);//@devTools e.printStackTrace();
        }
        getBtnSubmit().setActionForward(fwd);
        
        getBtnCancel().setActionForward(getCancelButtonForward(form));
//        fwd.setActionName("nc.ui.iufo.input.AbstractInputKeywordsAction");
//        fwd.setMethodName("execute");
//        this.retFwd(fwd);        
    }
    
//    protected ActionForward retFwd(ActionForward fwd){
//    	return fwd;
//    }
    
    /**
     * 初始化页面组件
     * 只在servlet实例化时调用一次.
     * 此方法中不得涉及任何多语言处理     
     * weixl
     * 2006-02-23
     */
    protected void initUI(){
        setWindowWidth(420);
        setWindowHeight(320);

        disableAutoResize();
        setRebuild(true);
        
        setContentPane(getDlgPane());
    }
    
    /**
     * 生成录入关键字Panel
     * @param keys，关键字数组
     * @param strKeyValues，以前录入的关键字值
     * @param taskDefault，任务缺省值
     * @param curUser，当前用户
     * @param strCurDate，当前期间
     * @param bShowOtherKey，是否显示除单位、时间外的其他关键字
     * @param bShowTimeKey，是否显示时间关键字
     * @return
     */
    public static WebPanel getInputKeywordsPanel(WebPanel keywordPanel,int iStartRow,KeyVO[] keys,String[] strKeyValues,TaskDefaultVO taskDefault,UserInfoVO curUser,String strCurDate,boolean bShowOtherKey,boolean bShowTimeKey,boolean bInOneRow){
    	if (keywordPanel==null){
	    	keywordPanel=new WebPanel();
	        WebGridLayout subLayout = null;
	        if (!bInOneRow)
	        	subLayout=new WebGridLayout(keys.length, 2);
	        else
	        	subLayout=new WebGridLayout(1,keys.length*2);
	        keywordPanel.setLayout(subLayout);
    	}
        
        UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();

        for(int i = 0; i < keys.length; i++){
        	//判断该关键字是否需要录入
        	if (bShowOtherKey==false && keys[i].getKeywordPK().equals(KeyVO.CORP_PK)==false && keys[i].getType() != KeyVO.TYPE_TIME)
        		continue;
        	
        	if (bShowTimeKey==false && keys[i].getType() == KeyVO.TYPE_TIME )
        		continue;
        	
        	Element txt=null;
        	String strName=null;
        	ActionForward fwd=null;
        	String strValue=null;
        	
            boolean bUnitKey = false;
            if(keys[i].getKeywordPK().equals(KeyVO.CORP_PK)){ //单位
                strName=StringResource.getStringResource("miufopublic125");
                txt=new WebTextRef();
                ((WebTextRef)txt).setShowValue(true);
                fwd=new ActionForward(BusinessRefAction.class.getName(),"getUnitCodeTreeRef");  
                fwd.addParameter(BDRefAction.UNIT_ID,curUser.getUnitId());
                fwd.addParameter(BDRefAction.UNIT_REF_SHOW_TASK, "true");
                strValue=curUser.getUnitId();
                strValue=unitCache.getUnitInfoByPK(strValue).getCode();
                bUnitKey = true;
            } else if(keys[i].getKeywordPK().equals(KeyVO.DIC_CORP_PK)){//对方单位
            	strName=StringResource.getStringResource("miufopublic276");
                txt=new WebTextRef();
                ((WebTextRef)txt).setShowValue(true);                
                fwd=new ActionForward(BusinessRefAction.class.getName(),"getUnitCodeTreeRef");
                fwd.addParameter(BDRefAction.UNIT_ID,unitCache.getRootUnitInfo().getPK());
                fwd.addParameter(BDRefAction.UNIT_REF_SHOW_TASK, "true");                
                strValue=curUser.getUnitId();
                strValue=unitCache.getUnitInfoByPK(strValue).getCode();
                bUnitKey = true;
            } else if(keys[i].getType() == KeyVO.TYPE_TIME){//时间
            	strName=StringResource.getStringResource("miufopublic328");
            	txt=new WebDateRef();            	
            	strValue=strCurDate;
            } else if (keys[i].getRef()!=null){//代码关键字
            	strName=keys[i].getName();
            	txt=new WebTextRef();
            	fwd=new ActionForward(BusinessRefAction.class.getName(),"getCodeRef");
            	fwd.addParameter(BDRefAction.CODE_ID,keys[i].getRef());
                fwd.addParameter(BDRefAction.CODE_REF_RETURN_TYPE,CodeVO.ReturnType_Code);
                fwd.addParameter(BDRefAction.CODE_REF_TYPE, CodeVO.REFTYPE_ALL);
            }
            else{
            	strName=keys[i].getName();
            	txt=new WebTextField();
            	((WebTextField)txt).setMaxlength(keys[i].getLen());
            }
            
            if (strKeyValues!=null && i<strKeyValues.length){
            	strValue=strKeyValues[i];
                if(bUnitKey && strValue!=null){
                	UnitInfoVO unitInfo=unitCache.getUnitInfoByPK(strValue);
                	if (unitInfo==null)
                		strValue=null;
                	else
                		strValue=unitInfo.getCode();
                }
                
            }else if (strValue==null && taskDefault!=null)
            	strValue=taskDefault.getKeywordValueByIndex(i+1);
            
            boolean bShow=bShowOtherKey==false || (keys[i].getKeywordPK().equals(KeyVO.CORP_PK)==false && keys[i].getType() != KeyVO.TYPE_TIME);
            
            if (txt instanceof WebTextRef){
            	((WebTextRef)txt).setActionForward(fwd);
            	((WebTextField)((WebTextRef)txt).getRefFld()).setVld_NoNull(true);
            	((WebTextField)((WebTextRef)txt).getRefFld()).setVld_label_id(strName);
            	((WebTextField)((WebTextRef)txt).getRefFld()).setMaxlength(128);
            	((WebTextRef)txt).setValue(strValue);
            	
            	if (keys[i].getKeywordPK().equals(KeyVO.CORP_PK) || keys[i].getKeywordPK().equals(KeyVO.DIC_CORP_PK))
            		((WebTextField)((WebTextRef)txt).getRefFld()).setValue(strValue);
            	
            	((WebTextRef)txt).setID("id_key"+i);
            	((WebTextRef)txt).setName("id_key"+i);
            }
            else if (txt instanceof WebDateRef){
            	((WebTextField)((WebDateRef)txt).getRefFld()).setVld_NoNull(true);
            	((WebTextField)((WebDateRef)txt).getRefFld()).setVld_label_id(strName);
            	((WebTextField)((WebDateRef)txt).getRefFld()).setMaxlength(10);
            	((WebDateRef)txt).setValue(strValue);
            	((WebDateRef)txt).setID("id_key"+i);
            	((WebDateRef)txt).setName("id_key"+i);      
            }
            else if (txt instanceof WebTextField){
            	((WebTextField)txt).setID("id_key"+i);
            	((WebTextField)txt).setName("id_key"+i);
            	((WebTextField)txt).setVld_NoNull(true);
            	((WebTextField)txt).setVld_label_id(strName);
            	((WebTextField)txt).setValue(strValue);
            	
            	if (bShow==false)
            		((WebTextField)txt).setVisible(false);
            }
            
            WebLabel lblName=new WebLabel(strName);
            lblName.setID("id_name"+i);
            
            if (!bInOneRow){
            	keywordPanel.add(lblName,new Area(i+iStartRow,1,1,1));
            	keywordPanel.add(txt,new Area(i+iStartRow,2,1,1));
            }else{
            	keywordPanel.add(lblName,new Area(1,i*2+1,1,1));
            	keywordPanel.add(txt,new Area(1,i*2+2,1,1));
            }
        }
        return keywordPanel;    	
    }


    protected WebPanel getFieldPane(InputKeywordsForm form){
    	return getInputKeywordsPanel(null,1,form.getKeyVOs(),form.getPubDataVO()==null?null:form.getPubDataVO().getKeywords(),form.getTaskDefault(),form.getCurUserInfo(),form.getCurDate(),isShowOtherKeys(),true,false);
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
 
        }
        return btnSubmit;
    }
    

    protected WebButton getBtnCancel(){     
        if(btnCancel == null){
        	btnCancel = new WebButton();
        	btnCancel.setProcessValidate(false);
        }
        return btnCancel;
    }
    

    protected WebPanel getBtnPane(){     
        if(btnPane == null){
            btnPane = new WebPanel();
            WebGridLayout layout=new WebGridLayout(1,2);
            layout.setAlign(new Align(Align.RIGHT));
            btnPane.setLayout(layout);
            Area area = null;
            area = new Area(1, 1, 1, 1);
            btnPane.add(getBtnSubmit(), area);
            area = new Area(1, 2, 1, 1);
            btnPane.add(getBtnCancel(), area);
 
        }
        return btnPane;
    }
    

    protected WebPanel getDlgPane(){     
        if(dlgPane == null){
            dlgPane = new WebPanel();
            dlgPane.setLayout(new WebGridLayout(5,1));
            dlgPane.add(getBr1(),new Area(2,1,1,1,new Align(Align.CENTER)));
            dlgPane.add(getBr1(),new Area(3,1,1,1,new Align(Align.CENTER)));
            HR hr = new HR();
            hr.setSize(1);
            dlgPane.add(hr,new Area(4,1,1,1,new Align(Align.CENTER)));
            dlgPane.add(getBtnPane(),new Area(5,1,1,1,new Align(Align.RIGHT)));
 
        }
        return dlgPane;
    }
    
	protected void clear() {
		btnCancel=null;
		btnSubmit=null;
		dlgPane=null;
		btnPane = null;
		super.clear();
	}
   
	private String doGetUITitle(InputKeywordsForm form){
		if(form != null && form.getUITitle()!=null){
			return form.getUITitle();
		}else{
			return getUITitle();
		}
	}
   
	/**
	 * 没有在Form设置Title的子类Action,必须在UI子类里重写该方法
	 * @return
	 */
	protected String getUITitle(){
		return null;
	}
	
	/**
	 * 如果没有在InputKeywordsForm设置SubmitButtonForward的值，则子类必须重写该方法
	 * @param param
	 * @return
	 */
	protected ActionForward getSubmitButtonForward(CSomeParam param){
		return null;
	}
   
	/**
	 * 得到取消按钮的链接
	 * @param form
	 * @return
	 */
	protected ActionForward getCancelButtonForward(InputKeywordsForm form) {
		if(form != null && form.getCancelFwd() != null){
			return form.getCancelFwd();
		}else{ 
			return new CloseForward(CloseForward.CLOSE);
		}
	}

	/**
	 * 得到确定按钮的链接
	 * @param form
	 * @return
	 */
	protected ActionForward getSubmitButtonForward(InputKeywordsForm form) {
		if(form != null && form.getSubmitFwd() != null){
			return form.getSubmitFwd();
		}else{           
			CSomeParam param = form.getSomeParam();
			return getSubmitButtonForward(param);
		}
	}
	
	//是否显示除单位、时间外的其他关键字
	protected boolean isShowOtherKeys(){
		return true;
	}
}
/**@WebDeveloper
<?xml version="1.0" encoding='gb2312'?>
    <DialogVO name="AbstractInputKeywordsUI1" package="nc.ui.iufo.input" windowHeight="600" windowWidth="800" 关联Form="nc.ui.iufo.input.InputKeywordsForm">
      <WebPanel FlowDirection="VERTICAL" layout="WebFlowLayout" name="dlgPane">
        <LayoutVO name="layout1" type="WebFlowLayout">
          <WebPanel col="0" name="fieldPane" row="0">
            <LayoutVO name="layout1" type="WebGridLayout">
            </LayoutVO>
          </WebPanel>
          <BR col="1" name="br1" row="0">
          </BR>
          <WebPanel col="2" name="btnPane" row="0">
            <LayoutVO name="layout3" type="WebGridLayout">
              <WebButton col="0" name="btnSubmit" row="0">
              </WebButton>
              <WebButton col="1" name="btn1" row="0" value="取消">
              </WebButton>
            </LayoutVO>
          </WebPanel>
        </LayoutVO>
      </WebPanel>
    </DialogVO>
@WebDeveloper*/