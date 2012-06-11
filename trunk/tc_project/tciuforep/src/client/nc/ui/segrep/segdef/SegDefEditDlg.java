/*
 * 创建日期 2006-7-4
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.segrep.segdef;

import nc.ui.bi.web.reference.BIRefAction;
import nc.ui.bi.web.reference.QueryRefAction;
import nc.vo.iufo.authorization.IAuthorizeTypes;

import com.ufida.web.WebException;
import com.ufida.web.action.ActionForward;
import com.ufida.web.comp.*;
import com.ufida.web.container.WebFlowLayout;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.BR;
import com.ufida.web.window.WebDialog;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 *
 * 分部划分编辑对话框
 */
public class SegDefEditDlg extends WebDialog {
	private 	WebPanel 		dlgPane = null;
    private 	WebPanel 		fieldPane = null;
	private    	WebTabbedPane	tabbedPane = null;
	
	private		WebPanel		pnlBasic = null;
	private    	WebPanel    	pnlScope = null;
	private    	WebPanel		pnlReport = null;
	
	private    	WebLabel    	lblName = null;
	private    	WebTextField	txtName = null;
	private    	WebLabel        lblQuery = null;
	private    	WebTextRef		refQuery = null;
	private    	WebLabel		lblOrgDim = null;
	private    	WebChoice       choiceOrgDim = null;
	private    	WebLabel		lblOrgDimField = null;
	private    	WebChoice		choiceOrgDimField = null;
	private    	WebLabel		lblTradeOrgDim = null;
	private    	WebChoice       choiceTradeOrgDim = null;
	private    	WebLabel		lblTradeOrgDimField = null;
	private    	WebChoice		choiceTradeOrgDimField = null;
//	private    	WebLabel		lblSegItemDim = null;
//	private 	WebChoice		choiceSegItemDim = null;
	private    	WebHiddenField	hdSegDefPK = null;
	private    	WebHiddenField	hdDirPK = null;
	
	private    	WebTree2List	listScope = null;
	
	private    	WebLabel		lblSegQuery = null;
	private    	WebTextRef		refSegQueryDir = null;
	private    	WebLabel		lblSegQueryName = null;
	private    	WebTextField	txtSegQueryName = null;
	
	private    	WebLabel		lblSegReport = null;
	private    	WebTextRef		refSegReportDir = null;
	private    	WebLabel		lblSegReportName = null;
	private    	WebTextField	txtSegReportName = null;
	private    	WebHiddenField	hdSegQueryPK = null;
	private    	WebHiddenField	hdSegReportPK = null;
	
    private 	WebPanel 		btnPane = null;
    private 	WebButton 		btnSubmit = null;
    private 	WebCloseButton 	btnClose = null;
  	
	private    WebTextField    getTxtName(){
		if( txtName == null ){
			txtName = new WebTextField();
			txtName.setID("strSegDefName");
			txtName.setName("strSegDefName");
			txtName.setMaxlength(50);
			txtName.setVld_NoNull(true);
		}
		return txtName;
	}
	private  WebTextRef	getQueryRef(){
		if( refQuery == null ){
			refQuery = new WebTextRef();
			refQuery.setID("strQueryPK");
			refQuery.setName("strQueryPK");
			refQuery.setReadonly(true);
			
			//action是查询参照
			ActionForward	fwd = new ActionForward(QueryRefAction.class.getName(), "execute");
			refQuery.setActionForward(fwd);
			
			//设置查询改变后的执行脚本
			fwd = new ActionForward(SegDefEditAction.class.getName(),"onQueryChanged");
			//String	strScript ="segrep_queryrefresh('"+fwd.genURI()+"')";
			String	strScript ="opener.document.form1.action ='"+fwd.genURI()+"';";
			strScript += "opener.document.form1.method='post';";
			strScript += "opener.document.form1.submit();"; 
            refQuery.setExtendScript(strScript);

		}
		return refQuery;
	}
	private WebChoice getChoiceOrgDim(){
		if( choiceOrgDim == null ){
			choiceOrgDim = new WebChoice();
			choiceOrgDim.setID("strOrgDimPK");
			choiceOrgDim.setName("strOrgDimPK");
			
			//设置onChange脚本
			ActionForward	fwd = new ActionForward(SegDefEditAction.class.getName(),"onOrgDimChanged");
			choiceOrgDim.setOnChange("btn_action('"+fwd.genURI()+"',false)");
		}
		return choiceOrgDim;
	}
	
	private WebChoice getChoiceOrgDimField(){
		if( choiceOrgDimField == null ){
			choiceOrgDimField = new WebChoice();
			choiceOrgDimField.setID("strOrgDimField");
			choiceOrgDimField.setName("strOrgDimField");
		}
		return choiceOrgDimField;
	}
	private WebChoice getChoiceTradeOrgDim(){
		if( choiceTradeOrgDim == null ){
			choiceTradeOrgDim = new WebChoice();
			choiceTradeOrgDim.setID("strTradeOrgDimPK");
			choiceTradeOrgDim.setName("strTradeOrgDimPK");
			
			//设置onChange脚本
			ActionForward	fwd = new ActionForward(SegDefEditAction.class.getName(),"onTradeDimChanged");
			choiceTradeOrgDim.setOnChange("btn_action('"+fwd.genURI()+"',false)");
		}
		return choiceTradeOrgDim;
	}
	
	private WebChoice getChoiceTradeOrgDimField(){
		if( choiceTradeOrgDimField == null ){
			choiceTradeOrgDimField = new WebChoice();
			choiceTradeOrgDimField.setID("strTradeOrgDimField");
			choiceTradeOrgDimField.setName("strTradeOrgDimField");
		}
		return choiceTradeOrgDimField;
	}

	
	private  WebHiddenField	getHdPK(){
		if( hdSegDefPK == null ){
			hdSegDefPK = new WebHiddenField();
			hdSegDefPK.setID("strSegDefPK");
			hdSegDefPK.setName("strSegDefPK");
		}
		return hdSegDefPK;
	}
	
	private  WebHiddenField	getHdDirPK(){
		if( hdDirPK == null ){
			hdDirPK = new WebHiddenField();
			hdDirPK.setID("strDirPK");
			hdDirPK.setName("strDirPK");
		}
		return hdDirPK;
	}
	/**
	 * 分部划分
	 * @return
	 */
	private WebPanel  getPnlBasic(){
		if( pnlBasic == null ){
			pnlBasic = new WebPanel(6,3);
			lblName = new WebLabel();
			Area	area = new Area(1,1,1,1);
			pnlBasic.add(lblName, area);
			area = new Area(1,2,1,1);
			pnlBasic.add(getTxtName(), area);
			area = new Area(1,3,1,1);
			pnlBasic.add(getHdPK(), area);
			
			lblQuery = new WebLabel();
			area = new Area(2,1,1,1);
			pnlBasic.add(lblQuery, area);
			area = new Area(2,2,1,1);
			pnlBasic.add(getQueryRef(), area);
			area = new Area(2,3,1,1);
			pnlBasic.add(getHdDirPK(), area);
			
			area = new Area(3,1,3,1);
			lblOrgDim = new WebLabel();
			area = new Area(3,1,1,1);
			pnlBasic.add(lblOrgDim, area);
			area = new Area(3,2,1,1);
			pnlBasic.add(getChoiceOrgDim(), area);
			
			lblOrgDimField = new WebLabel();
			area = new Area(4,1,1,1);
			pnlBasic.add(lblOrgDimField, area);
			area = new Area(4,2,1,1);
			pnlBasic.add(getChoiceOrgDimField(), area);

			lblTradeOrgDim = new WebLabel();
			area = new Area(5,1,1,1);
			pnlBasic.add(lblTradeOrgDim, area);
			area = new Area(5,2,1,1);
			pnlBasic.add(getChoiceTradeOrgDim(), area);
			
			lblTradeOrgDimField = new WebLabel();
			area = new Area(6,1,1,1);
			pnlBasic.add(lblTradeOrgDimField, area);
			area = new Area(6,2,1,1);
			pnlBasic.add(getChoiceTradeOrgDimField(), area);
//			lblSegItemDim = new WebLabel();
//			area = new Area(7,1,1,1);
//			pnlBasic.add(lblSegItemDim, area);
//			area = new Area(7,2,1,1);
//			pnlBasic.add(getChoiceSegItemDim(), area);
		}
		return pnlBasic;
	}

	private WebTree2List	getListScope(){
		if( listScope == null ){
			listScope = new WebTree2List();
			listScope.setID("strOrgDimMembers");
			listScope.setName("strOrgDimMembers");
		}
		return listScope;
	}
	/**
	 * 组织范围
	 * @return
	 */
	private WebPanel     getPnlScope(){
		if( pnlScope == null ){
			pnlScope = new WebPanel(1,1);
			Area  area = new Area(1,1,1,1);
			pnlScope.add(getListScope(), area);
		}
		return pnlScope;
	}
	
	private  WebTextRef   getSegQueryDirRef(){
		if( refSegQueryDir == null ){
			refSegQueryDir = new WebTextRef();
			refSegQueryDir.setID("strSegQueryDirPK");
			refSegQueryDir.setName("strSegQueryDirPK");
			//设置action
			ActionForward	fwd = new ActionForward(BIRefAction.class.getName(), "getQueryDirRef");
			fwd.addParameter(BIRefAction.PARAM_AUTH_TYPE, Integer.toString(IAuthorizeTypes.AU_TYPE_MODIFY));
			refSegQueryDir.setActionForward(fwd);
			
			refSegQueryDir.setReadonly(true);
		}
		return refSegQueryDir;
	}
	private  WebTextField  getTxtSegQueryName(){
		if( txtSegQueryName == null ){
			txtSegQueryName = new WebTextField();
			txtSegQueryName.setID("strSegQueryName");
			txtSegQueryName.setName("strSegQueryName");
			txtSegQueryName.setVld_NoNull(true);
			txtSegQueryName.setMaxlength(50);
		}
		return txtSegQueryName;
	}
	private  WebTextRef   getSegReportDirRef(){
		if( refSegReportDir == null ){
			refSegReportDir = new WebTextRef();
			refSegReportDir.setID("strSegReportDirPK");
			refSegReportDir.setName("strSegReportDirPK");
			//设置action
			ActionForward	fwd = new ActionForward(BIRefAction.class.getName(), "getReportDirRef");
			fwd.addParameter(BIRefAction.PARAM_AUTH_TYPE, Integer.toString(IAuthorizeTypes.AU_TYPE_MODIFY));
			refSegReportDir.setActionForward(fwd);
			
			refSegReportDir.setReadonly(true);
		}
		return refSegReportDir;
	}
	private  WebTextField  getTxtSegReportName(){
		if( txtSegReportName == null ){
			txtSegReportName = new WebTextField();
			txtSegReportName.setID("strSegReportName");
			txtSegReportName.setName("strSegReportName");
			txtSegReportName.setVld_NoNull(true);
			txtSegReportName.setMaxlength(50);
		}
		return txtSegReportName;
	}
	private  WebHiddenField	getHdSegQueryPK(){
		if( hdSegQueryPK == null ){
			hdSegQueryPK = new WebHiddenField();
			hdSegQueryPK.setID("strSegReportQueryPK");
			hdSegQueryPK.setName("strSegReportQueryPK");
		}
		return hdSegQueryPK;
	}
	private  WebHiddenField	getHdSegReportPK(){
		if( hdSegReportPK == null ){
			hdSegReportPK = new WebHiddenField();
			hdSegReportPK.setID("strSegReportPK");
			hdSegReportPK.setName("strSegReportPK");
		}
		return hdSegReportPK;
	}
	/**
	 * 存储位置
	 * @return
	 */
	private WebPanel	getPnlReport(){
		if( pnlReport == null ){
			pnlReport = new WebPanel(6,3);
			Area	area = null;
			
			lblSegQuery = new WebLabel();
			area = new Area(1,1,1,1);
			pnlReport.add(lblSegQuery, area);
			area = new Area(2,1,1,1);
			pnlReport.add( new BR(), area);
			area = new Area(2,2,1,1);
			pnlReport.add( getSegQueryDirRef(), area);
			lblSegQueryName = new WebLabel();
			area = new Area(3,1,1,1);
			pnlReport.add(lblSegQueryName, area);
			area = new Area(3,2,1,1);
			pnlReport.add(getTxtSegQueryName(), area);
			area = new Area(3,3,1,1);
			pnlReport.add(getHdSegQueryPK(), area);
			
			lblSegReport = new WebLabel();
			area = new Area(4,1,1,1);
			pnlReport.add(lblSegReport, area);
			area = new Area(5,1,1,1);
			pnlReport.add( new BR(), area);
			area = new Area(5,2,1,1);
			pnlReport.add( getSegReportDirRef(), area);
			lblSegReportName = new WebLabel();
			area = new Area(6,1,1,1);
			pnlReport.add(lblSegReportName, area);
			area = new Area(6,2,1,1);
			pnlReport.add(getTxtSegReportName(), area);
			area = new Area(6,3,1,1);
			pnlReport.add(getHdSegReportPK(), area);
			
		}
		return pnlReport;
	}

    protected WebButton getBtnSubmit(){     
        if(btnSubmit == null){
            btnSubmit = new WebButton();
            ActionForward fwd = new ActionForward(SegDefEditAction.class.getName(), "save");
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
        if(fieldPane == null){
            fieldPane = new WebPanel();
            fieldPane.setLayout(new WebGridLayout(1, 1));
            Area area = null;
            area = new Area(1, 1, 1, 1);
            tabbedPane = new WebTabbedPane();
            fieldPane.add(tabbedPane, area); 
        }
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
        disableAutoResize();

        setContentPane(getDlgPane());
	        
   }
   protected void setData() throws WebException {
   		SegDefEditForm		form = (SegDefEditForm)getActionForm(SegDefEditForm.class.getName());
   		if( form != null ){
   			//设置多页签
   			tabbedPane.addTab(StringResource.getStringResource("usrdef0011"), getPnlBasic());//分部定义
   			tabbedPane.addTab(StringResource.getStringResource("usrdef0012"), getPnlScope());//分部范围
   			tabbedPane.addTab(StringResource.getStringResource("usrdef0013"), getPnlReport());//存储位置
   			
   			setLabelValues();
   			//设置值
   			if( form.getSegDefPK() == null || form.getSegDefPK().length()==0 ){
   				//新建
   				setTitle(StringResource.getStringResource("usrdef0023"));//新建分部划分
   			}else{
   				setTitle(StringResource.getStringResource("usrdef0024"));//修改分部划分
   			}
   			
   			String[][]		strNullItems = {{"",""}};
   			txtName.setValue(form.getSegDefName());
   			refQuery.setValue(form.getQueryPK());
   			WebTextField	refQueryText = (WebTextField)refQuery.getRefFld();
   			refQueryText.setValue(form.getQueryName());
   			
   			hdSegDefPK.setValue(form.getSegDefPK());
   			hdDirPK.setValue(form.getDirPK());
   			if( form.getDimItems() == null ){
   				choiceOrgDim.setItems(strNullItems);
   				choiceTradeOrgDim.setItems(strNullItems);
   			}else{
   				choiceOrgDim.setItems(form.getDimItems());
   				choiceTradeOrgDim.setItems(form.getDimItems());
   			}
   			choiceOrgDim.setValue(form.getOrgDimPK());
   			if( form.getOrgDimFieldItems() == null ){
   				choiceOrgDimField.setItems(strNullItems);
   			}else{
   				choiceOrgDimField.setItems(form.getOrgDimFieldItems());
   			}
   			choiceOrgDimField.setValue(form.getOrgDimField());
   			choiceTradeOrgDim.setValue(form.getTradeOrgDimPK());
   			if( form.getTradeDimFieldItems() == null ){
   				choiceTradeOrgDimField.setItems(strNullItems);
   			}else{
   				choiceTradeOrgDimField.setItems(form.getTradeDimFieldItems());
   			}
   			choiceTradeOrgDimField.setValue(form.getTradeOrgDimField());


   			listScope.setModel(form.getListModel());	
   			
   			refSegQueryDir.setValue(form.getSegQueryDirPK());
   			WebTextField	refSegQueryDirText = (WebTextField)refSegQueryDir.getRefFld();
   			refSegQueryDirText.setValue(form.getSegQueryDirName());
   			txtSegQueryName.setValue(form.getSegQueryName());
   			
   			refSegReportDir.setValue(form.getSegReportDirPK());
   			WebTextField	refSegRepDirText = (WebTextField)refSegReportDir.getRefFld();
   			refSegRepDirText.setValue(form.getSegRepDirName());
   			txtSegReportName.setValue(form.getSegReportName());
   			hdSegQueryPK.setValue(form.getSegReportQueryPK());
   			hdSegReportPK.setValue(form.getSegReportPK());
   			
   		}
   }
   private void setLabelValues(){
   		lblName.setValue(StringResource.getStringResource("usrdef0025"));//分部名称
   		lblQuery.setValue(StringResource.getStringResource("usrdef0026"));//选择查询

   		lblSegQuery.setValue(StringResource.getStringResource("usrdef0027"));//分部划分名称
   		lblSegQueryName.setValue(StringResource.getStringResource("usrdef0028"));//分部划分名称
   		lblSegReport.setValue(StringResource.getStringResource("usrdef0029"));//分部划分名称
   		lblSegReportName.setValue(StringResource.getStringResource("usrdef0030"));//分部划分名称

    	btnSubmit.setValue(StringResource.getStringResource("miufo1003675"));//确定"));
    	btnClose.setValue(StringResource.getStringResource("miufo1000764"));//取消"));
    	
   		lblOrgDim.setValue(StringResource.getStringResource("usrdef0031"));//组织维度
   		lblOrgDimField.setValue(StringResource.getStringResource("usrdef0032"));//分部划分依据
   		lblTradeOrgDim.setValue(StringResource.getStringResource("usrdef0033"));//对方组织维度
   		lblTradeOrgDimField.setValue(StringResource.getStringResource("usrdef0034"));//对应分部划分
 
   }
   protected void clear(){
	   super.clear();
   		dlgPane = null;
   		fieldPane = null;
   		tabbedPane = null;
	
   		pnlBasic = null;
   		pnlScope = null;
   		pnlReport = null;
	
   		lblName = null;
   		txtName = null;
   		lblQuery = null;
   		refQuery = null;
   		lblOrgDim = null;
		choiceOrgDim = null;
		lblOrgDimField = null;
		choiceOrgDimField = null;
		lblTradeOrgDim = null;
		choiceTradeOrgDim = null;
		lblTradeOrgDimField = null;
		choiceTradeOrgDimField = null;

		hdSegDefPK = null;
		hdDirPK = null;
		
		listScope = null;
		
		lblSegQuery = null;
		refSegQueryDir = null;
		lblSegQueryName = null;
		txtSegQueryName = null;
		
		lblSegReport = null;
		refSegReportDir = null;
		lblSegReportName = null;
		txtSegReportName = null;
		hdSegQueryPK = null;
		hdSegReportPK = null;
		
	    btnPane = null;
	    btnSubmit = null;
	    btnClose = null;
   	
   }
   
}
