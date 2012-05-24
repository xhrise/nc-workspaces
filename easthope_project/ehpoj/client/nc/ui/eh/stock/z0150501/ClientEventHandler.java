package nc.ui.eh.stock.z0150501;

/**
 * 采购合同
 * @author 王明
 * 创建日期 2008-4-1 16:09:43
 */

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.eh55.FileManagerDialog;
import nc.ui.eh.eh55.FileManagerUtil;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.z0150501.StockContractVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;

public class ClientEventHandler extends AbstractEventHandler {
    boolean iscopy=false;                                  //是否项目调整
    String pk="";                                  //是否项目调整
    private StockContractVO headvo = null;
    String oldpk_contract =null;
    
	public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
		super(billUI, control);
	}
	
	//增加按钮
	@Override
	protected void onBoElse(int intBtn) throws Exception {
		switch (intBtn) {
		case IEHButton.STOCKCOPE: // 合同附件
			onBoStockCope();
            break;
		case IEHButton.STOCKCHANGE:// 合同变更
			onBoStockChange();
			break;
		}
		super.onBoElse(intBtn);
	}

	@Override
	public void onBoSave() throws Exception {
		
		AggregatedValueObject aggVO = getBillUI().getVOFromUI();
		// 空的判断
		getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
		
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel("eh_stock_contract_b");
        //前台校验    
     	int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc"});
     	if(res==1){
             getBillUI().showErrorMessage("物料有重复！");
             return;
     	}
     	
     	
     	
     	
//     	合同日期，计划生效日期，计划终止日期 大小的比较
     	if(!iscopy){
//     		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("writedate").setEnabled(false);
//     		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("startdate").setEnabled(false);
//     		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("writedate").setEnabled(false);
//     	}else{
     		UFDate  Startdate=((StockContractVO)aggVO.getParentVO()).getStartdate();
            if(!(Startdate.compareTo(_getDate())==0||Startdate.after(_getDate()))){
         	   getBillUI().showErrorMessage("计划生效日期应该大于当天日期"); 
         	   return;  
            } 
            UFDate  Enddate=((StockContractVO)aggVO.getParentVO()).getEnddate();
            if(!(Enddate.compareTo(_getDate())==0||Enddate.after(_getDate()))){
         	   getBillUI().showErrorMessage("计划终止日期应该大于当天日期"); 
         	   return;  
            } 
            UFDate  Writedate=((StockContractVO)aggVO.getParentVO()).getWritedate();
            if(!(Writedate.compareTo(_getDate())==0||Writedate.after(_getDate()))){
         	   getBillUI().showErrorMessage("合同日期应该大于当天日期"); 
         	   return;  
            } 
            if(!(Enddate.compareTo(Startdate)==0||Enddate.after(Startdate))){
         	   getBillUI().showErrorMessage("计划终止日期应该大于计划生效日期"); 
         	   return;  
            } 
            if(!(Startdate.compareTo(Writedate)==0||Startdate.after(Writedate))){
         	   getBillUI().showErrorMessage("计划生效日期应该大于合同日期"); 
         	   return;  
            } 
     		
     	}
       
        
        // 将旧版本中关闭按钮给关闭
//        StockContractVO headvo = (StockContractVO) getBillUI().getVOFromUI().getParentVO();
        if(headvo!=null){
            IVOPersistence ivopersistence=(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
            headvo.setLock_flag(new UFBoolean(true));
            ivopersistence.updateVO(headvo);
            //把bvo清空
            headvo = null;
        }
        
        
		super.onBoSave();
            
             //做项目调整时，对原项目的作废标记进行设置
        if(iscopy){
            PubItf pubItf=(PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
            pubItf.setCopyFlag(pk);
            iscopy = false;
            pk=null;
            onBoRefresh();
        }   
        
        StockContractVO hvo =(StockContractVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        String pk_decision  = hvo.getDef_1();
        if(pk_decision!=null&&pk_decision.length()>0){
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	String updateSQL = "update eh_stock_decision set lock_flag = 'Y' where pk_decision = '"+pk_decision+"' ";   
        	pubitf.updateSQL(updateSQL);
        }
        setBoEnabled();
        
        
        
        //合同变更 互换PK
//        String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
        	
//        if(oldpk_contract!=null && lock_flag.equals("true")){
//        	String newpk_contract = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_contract").getValueObject()
//			==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_contract").getValueObject().toString();
////        	String sqlchang2="update eh_stock_contract set pk_contract='eeeeeeeeeeeeeeeeeee5' where pk_contract='"+oldpk_contract+"' ";
//        	String sqlupdatepk4="update eh_stock_contract set pk_contract='"+oldpk_contract+"' where pk_contract='"+newpk_contract+"' ";
//        	String sqlupdatepk3="update eh_stock_contract_b set pk_contract='"+oldpk_contract+"' where pk_contract='"+newpk_contract+"'";
//        	String sqlupdatepk5="update eh_stock_contract_event set pk_contract='"+oldpk_contract+"' where pk_contract='"+newpk_contract+"'";
//        	String sqlupdatepk6="update eh_stock_contract_terms set pk_contract='"+oldpk_contract+"' where pk_contract='"+newpk_contract+"'";
////        	String sqlchang1="update eh_stock_contract set pk_contract='eeeeeeeeeeeeeeeeeee5' where pk_contract='"+oldpk_contract+"'";
//        	String sqlupdatepk2="update eh_stock_contract set pk_contract='"+newpk_contract+"' where pk_contract='"+oldpk_contract+"'";
//        	String sqlupdatepk1="update eh_stock_contract_b set pk_contract='"+newpk_contract+"' where pk_contract='"+oldpk_contract+"'";
//        	String sqlupdatepk7="update eh_stock_contract_event set pk_contract='"+newpk_contract+"' where pk_contract='"+oldpk_contract+"'";
//        	String sqlupdatepk8="update eh_stock_contract_terms set pk_contract='"+newpk_contract+"' where pk_contract='"+oldpk_contract+"'";
//        	
//        	ArrayList<String> al=new ArrayList<String>();
//        	
////        	al.add(sqlchang2);
//        	al.add(sqlupdatepk1);
//        	al.add(sqlupdatepk2);
//        	al.add(sqlupdatepk5);
//        	al.add(sqlupdatepk6);
////        	al.add(sqlchang1);
//        	
//        	al.add(sqlupdatepk3);
//        	al.add(sqlupdatepk4);
//        	al.add(sqlupdatepk7);
//        	al.add(sqlupdatepk8);
//        	PubItf pubItf=(PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
//        	pubItf.UpdateSQLS(al);
//        }
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
//		 修改版本号
		int ver = new Integer(getBillCardPanelWrapper().getBillCardPanel()
				.getHeadItem("ver").getValue()).intValue();
		ver = ver + 1;
		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("ver",
				new Integer(ver).toString());
	}
    
	//合同附件具体实现法方
	private void onBoStockCope() {
       
        // 文档管理
        try {
            String billid = getBillUI().getVOFromUI().getParentVO().getPrimaryKey();
            String billno = getBillUI().getVOFromUI().getParentVO().getAttributeValue("vbillcode")==null?""
                    :getBillUI().getVOFromUI().getParentVO().getAttributeValue("vbillcode").toString();
            if (billid ==null || billid.equals("")){
                this.getBillUI().showWarningMessage("项目保存以后才可以使用文档功能！");
                return;
            }
            FileManagerDialog dlg = new FileManagerDialog(this.getBillUI(),
                    new String[] { billid }, new String[] { billno });
            dlg.setTitle( "单据文档管理");
            dlg.setShowStyle(FileManagerDialog.SHOW_FILE_LOCAL);
            if (dlg.showModal() == nc.ui.pub.beans.UIDialog.ID_CANCEL)
                return;
            String dir = dlg.getDir();
            String fileName = dlg.getSelectedFileName();
            dlg.dispose();
            FileManagerUtil.showFileLocal(dir, fileName);
        } catch (BusinessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
	}
	//合同变更具体实现法方
	private void onBoStockChange() {
		//前个合同的主键
		oldpk_contract = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_contract").getValueObject()
				==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_contract").getValueObject().toString();
        try {
            AggregatedValueObject aggvo = getBillUI().getVOFromUI();
               headvo=(StockContractVO)aggvo.getParentVO();
               pk=headvo.getPrimaryKey();
               int  billstatus=headvo.getVbillstatus().intValue();
               String copy_flag=headvo.getCopy_flag()==null?"N":headvo.getCopy_flag().toString();
               //如果没有选择项目提示错误
               if(headvo.getBillno()==null||headvo.getBillno().equals("")){
                   getBillUI().showErrorMessage("请选择一个项目！");
                   return;
               }
               //不允许重复调整
               if(copy_flag.equals("Y")){
                   getBillUI().showErrorMessage("该项目已调整过，请不要重复调整！");
                   return;
               }
               //只有审批通过的项目才允许调整
               if(billstatus!=1){
                   getBillUI().showErrorMessage("只有审批通过的项目才允许调整！");
                   return;
               }
               
               int ok=getBillUI().showYesNoMessage("是否确认进行项目变更?");
               if(ok==MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                   onBoCopy();
                   //将原单据号后加_0X x为1，2，3.。。。。。。
                   String billno=headvo.getBillno();
//                   String[] ss=billno.split("\\-");
//                   if(ss.length==1){
//                      getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue(headvo.getBillno()+"-01");
//                   }
//                   else{
//                       int code=Integer.parseInt(ss[1]);
//                       String newreqcode="";
//                       if(code<9){
//                           newreqcode=ss[0]+"-0"+(code+1);
//                       }else{
//                           newreqcode=ss[0]+"-"+(code+1);
//                       }
//                      getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue(newreqcode);
//                   }
                   getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").setValue(billno);
               }
               
               //将版本号进行改变在原基础上加1
               Integer ver=headvo.getVer();
               ver=ver+1;
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("ver").setValue(ver);
               getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").setValue(_getOperator());
               getBillCardPanelWrapper().getBillCardPanel().getTailItem("dmakedate").setValue(_getDate());
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapproveid").setValue(null);
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("dapprovedate").setValue(null);
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vapprovenote").setValue(null);
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillstatus").setValue(new Integer(8));
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("copy_flag").setValue("N");
               //设置复制标记为true
               iscopy=true;
               //设置一些字段可编辑状态
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("startdate").setEnabled(false);
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("writedate").setEnabled(false);
               getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
               int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
               
               for(int i=0;i<rows;i++){
            	   getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vcode", false);
               }
               getBillUI().updateUI();
               
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
	}
    
    @Override
    protected void onBoCancel() throws Exception {
        // TODO Auto-generated method stub
        super.onBoCancel();
        iscopy=false;
        pk=null;
        getBillUI().updateUI();
    }
    
	 
    
    //设置多页签打印 add by zqy
    @Override
    protected void onBoPrint() throws Exception {
        nc.ui.pub.print.IDataSource dataSource = new ClientCardPanelPRTS(getBillUI()
                ._getModuleCode(), getBillCardPanelWrapper().getBillCardPanel(),getUIController().getPkField());
        nc.ui.pub.print.PrintEntry print = new nc.ui.pub.print.PrintEntry(null,
                dataSource);
        print.setTemplateID(getBillUI()._getCorp().getPrimaryKey(), getBillUI()
                ._getModuleCode(), getBillUI()._getOperator(), getBillUI()
                .getBusinessType(), getBillUI().getNodeKey());
        print.selectTemplate();
        print.preview();
    }
  

	@Override
	protected void onBoDelete() throws Exception {
	    //回写的到采购申请
	   	int res = onBoDeleteN(); // 1为删除 0为取消删除
	   	if(res==0){
	   		return;
	   	}
	   	StockContractVO hvo =(StockContractVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        String pk_decision  = hvo.getDef_1();
        if(pk_decision!=null&&pk_decision.length()>0){
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	String updateSQL = "update eh_stock_decision set lock_flag = 'N' where pk_decision = '"+pk_decision+"' ";   
        	pubitf.updateSQL(updateSQL);
        }
	    super.onBoTrueDelete();
	}

	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
		 String rate = null;
		 String pk_corp = _getCorp().getPrimaryKey();
		 StringBuffer sql = new StringBuffer()
		 .append(" SELECT NVL(cgrate,0)/100 rate from eh_rate where pk_corp = '"+_getCorp().getPk_corp()+"' and NVL(dr,0)=0 ");
		 IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				HashMap hm = (HashMap)arr.get(0);
				rate = hm.get("rate") == null?"":hm.get("rate").toString();
				int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
		        this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(rate, row, "ratrate");
			}else{
				this.getBillUI().showErrorMessage("请维护销售,采购税率设置");
				return;
			}
		 
		
		
	}
	
	
	
}
