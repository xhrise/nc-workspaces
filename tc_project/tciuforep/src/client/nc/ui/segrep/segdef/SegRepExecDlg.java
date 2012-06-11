/*
 * 创建日期 2006-7-17
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.segrep.segdef;

import nc.ui.bi.web.reference.BIRefAction;
import nc.vo.segrep.segdef.ISegRepConstants;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.*;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

public class SegRepExecDlg extends WebDialog {
	private 	WebPanel 		dlgPane = null;
    private 	WebPanel 		fieldPane = null;
    private 	WebPanel 		btnPane = null;
    private 	WebButton 		btnSubmit = null;
    private 	WebCloseButton 	btnClose = null;
    private		WebHiddenField	hdSegDefPK = null;
    private		WebHiddenField	hdDimNum = null;
    //private     WebHiddenField  hdOpType = null;
    
    private  	WebHiddenField	getHDSegDefPK(){
    	if( hdSegDefPK == null ){
    		hdSegDefPK = new WebHiddenField();
    		hdSegDefPK.setID("strSegDefPK");
    		hdSegDefPK.setName("strSegDefPK");
    	}
    	return hdSegDefPK;
    }
    private  	WebHiddenField	getHDDimNum(){
    	if( hdDimNum == null ){
    		hdDimNum = new WebHiddenField();
    		hdDimNum.setID("nDimNumbers");
    		hdDimNum.setName("nDimNumbers");
    	}
    	return hdDimNum;
    }

    protected WebButton getBtnSubmit(){     
        if(btnSubmit == null){
            btnSubmit = new WebButton();
            ActionForward fwd = new ActionForward(SegRepExecAction.class.getName(), "generateData");
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

    protected WebPanel getFieldPane(){    
    	return fieldPane;
    }
    protected WebPanel getDlgPane(){     
        if(dlgPane == null){
            dlgPane = new WebPanel();
            dlgPane.setLayout(new WebFlowLayout(WebFlowLayout.CENTER, WebFlowLayout.VERTICAL));
            dlgPane.add(getFieldPane());
            dlgPane.add(getBtnPane());
 
        }
        return dlgPane;
    }
    
    protected void initUI(){
        setWindowWidth(640);
        setWindowHeight(600);
        //disableAutoResize();
        setRebuild(true);
        setContentPane(getDlgPane());
	        
   }
   protected void setData() throws WebException {
	   SegExecForm	form = (SegExecForm)getActionForm(SegExecForm.class.getName());
	   if( form != null ){
		   setTitle(StringResource.getStringResource("usrdef0037"));//"分部编制对象"
		   setFieldPane(form);
	   }
   }
   private void setFieldPane(SegExecForm  form){
	   fieldPane = new WebPanel(form.getDimNumbers()+1,3);
	   Area		area = null;
	   for( int i=0; i<form.getDimNumbers(); i++ ){
		   area = new Area(i+1, 1, 1,1);
		   WebLabel lbl = new WebLabel();
		   lbl.setValue(form.getDimVOs()[i].getDimname());
		   fieldPane.add(lbl, area);
		   
		   area = new Area(i+1, 2, 1,1);
		   WebTextRef	ref = new WebTextRef();
		   ref.setReadonly(true);
		   ref.setID(ISegRepConstants.PARAM_DIMMEMBER+i);
		   ref.setName(ISegRepConstants.PARAM_DIMMEMBER+i);
		   ActionForward	fwd = new ActionForward(BIRefAction.class.getName(), "getDimMemberRef");//维度成员参照
		   fwd.addParameter(BIRefAction.DIM_ID, form.getDimVOs()[i].getDimID());
		   fwd.addParameter(BIRefAction.PARAM_MEMBER_SELTYPE, BIRefAction.SELECT_LEAF);
		   ref.setActionForward(fwd);
		   //不能为空
		   ((WebTextField)ref.getRefFld()).setVld_NoNull(true);
		   fieldPane.add(ref, area);
		   
		   area = new Area(i+1, 3, 1,1);
		   WebHiddenField	hd = new WebHiddenField();
		   hd.setID(ISegRepConstants.PARAM_DIMPK+i);
		   hd.setName(ISegRepConstants.PARAM_DIMPK+i);
		   hd.setValue(form.getDimVOs()[i].getDimID());
		   fieldPane.add(hd,area);
	   }
	   area = new Area(form.getDimNumbers()+1, 1,1,1);
	   fieldPane.add(getHDSegDefPK(), area);
	   hdSegDefPK.setValue(form.getSegDefPK());
	   
	   area = new Area(form.getDimNumbers()+1, 2,1,1);
	   fieldPane.add(getHDDimNum(), area);
	   hdDimNum.setValue(form.getDimNumbers());
	   

   }
}
