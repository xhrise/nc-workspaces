package nc.ui.eh.stock.z06005;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.util.NCOptionPane;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.trade.button.IBillButton;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.stock.z06005.SbbillBVO;
import nc.vo.eh.stock.z06005.SbbillVO;
import nc.vo.eh.trade.z0600301.WeighbridgeconfigVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能：司磅单
 * 作者：newyear
 * 日期：2008-4-17 上午11:11:15
 */
public class ClientEventHandler extends AbstractEventHandler {
    private static ReadComm readComm ;
    private static WeighbridgeconfigVO wbVO ;
    UIRefPane ref=null;
    IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    ClientEnvironment ce = ClientEnvironment.getInstance();
	@SuppressWarnings("deprecation")
	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
		//对关闭按钮的减小操作的便捷性,防止误操作  add by wb at 2008-10-6 18:44:43
		ButtonObject bo = getButtonManager().getButton(IEHButton.BusinesBtn);
		if(bo!=null){
			ButtonObject bolock = new ButtonObject("关闭");
			bolock.setTag(String.valueOf(IEHButton.SBCLOSE));
			bolock.setCode("关闭");
			bo.addChildButton(bolock);
			bo.removeChildButton(bo.getChildButtonGroup()[0]);
		}
	}
	
     /* 
     * 功能：自定义按钮
     * @date 2008-4-14
     */
    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.FIRSTREADDATE:    //第一次读重  读重改为一次读重 空车和重车都是此按钮
            	onBoFirstRead();
                break;
            case IEHButton.SBCLOSE: //关闭司磅
            	onBoLockBill();
                break;
        }   
        super.onBoElse(intBtn);
    }
    
    
    /**
     * 功能：读取司磅的配置信息
     */
    private WeighbridgeconfigVO getWeighbridgeconfigVO() throws Exception{
        if(wbVO==null){
            StringBuffer br=new StringBuffer();
            br.append("isnull(dr,0)=0 and pk_corp = '"+_getCorp().getPk_corp()+"'");
            nc.ui.trade.bsdelegate.BusinessDelegator bd=new nc.ui.trade.bsdelegate.BusinessDelegator();
            WeighbridgeconfigVO[] queryVos = (WeighbridgeconfigVO[]) bd.queryByCondition(WeighbridgeconfigVO.class,br.toString());
            if(queryVos!=null && queryVos.length>1){
            	//此条件为有两个司磅读取相应的相应的信息参数的情况
            	//现在没有完善 留有接口
            	//取配置文件的信息
//            	String choosekey=getChooseConfig();
//            	for(int i=0;i<queryVos.length;i++){
//            		String key=queryVos[i].getWbcode()==null?"":queryVos[i].getWbcode().toString();
//            		if(choosekey.equals(key)){
//            			wbVO=queryVos[i];
//            		}
//            	}
            }else if(queryVos!=null && queryVos.length==1){
            	wbVO=queryVos[0];
            }
        }
        return wbVO;
    }
    
    /**
     * 选择配置文件
     * @return
     * @throws IOException 
     * @throws URISyntaxException 
     */
    public String getChooseConfig() throws IOException, URISyntaxException{
    	BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(new File("C:/weightmanconfig.txt"))));
    	String line=null;
    	String[] lines=null;
    	while((line=in.readLine())!=null){
    		lines=line.split("/");
    	}
    	if(lines!=null){
    		return lines[1];
    	}else{
    		return "XXXXXX";
    	}
    }
    
  
    
     /*
     * 窜口对象
     * @return
     */
    @SuppressWarnings("static-access")
    private ReadComm getReadComm(){
        if(this.readComm==null){
            this.readComm=new ReadComm();
        }
        return this.readComm;
    }
 
    
   /**
   功能：手工关闭当前司磅单
   作者：newyear
   日期：2008-6-4 下午02:16:58
   @throws BusinessException
    */
    protected void onBoLockBill() throws Exception{
    	SuperVO parentvo = (SuperVO)getBillUI().getVOFromUI().getParentVO();
    	String lock_flag=parentvo.getAttributeValue("close_flag")==null?"":parentvo.getAttributeValue("close_flag").toString();
    	String primaryKey = parentvo.getPrimaryKey();
    	if(lock_flag.equals("Y")){
            getBillUI().showErrorMessage("该单据已经关闭!");
            return;
        }
        else if(!primaryKey.equals("")){
            int iRet = getBillUI().showYesNoMessage("是否确定进行关闭操作?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
            	IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
                parentvo.setAttributeValue("close_flag", new UFBoolean(true));
                ivoPersistence.updateVO(parentvo);
            	getBillUI().showWarningMessage("已经关闭成功");
                onBoRefresh();
            }
            else{
            	return;
            }
        }
    }
    /*
     * 读取司磅信息
     * @作者:牛冶
     * @时间:2008-4-12
     */
    @SuppressWarnings("static-access")
	private void onBoFirstRead() throws Exception{
//    	getWeighbridgeconfigVO();
        if(!isCheckType())
            return;
        Object close_flag = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("close_flag").getValueObject();
        if(close_flag!=null && ("true".equalsIgnoreCase(close_flag.toString()))){
            getBillUI().showErrorMessage("司磅单已关闭!!");
            return;  
        }
        
      String weight = getReadComm().readWeight(getWeighbridgeconfigVO());
      if(weight!=null && (weight.equals("-2"))){
      		getBillUI().showErrorMessage("COM口被占用,请检查是否有其他设备连接使用!");
      		return;
      }
      if(weight!=null && (weight.equals("-3"))){
        	getBillUI().showErrorMessage("司磅连接失败,请检查!");
        	return;
        }
      if(weight!=null && (!weight.equals("-1"))){
      	Object FirstWeight = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject();
      	String pk = getBillCardPanelWrapper().getBillVOFromUI().getParentVO().getPrimaryKey();
	    UFDateTime ts = ce.getServerTime();
      	if(pk!=null&&pk.length()>0&&FirstWeight!=null && (!FirstWeight.equals(""))){      // 第一次取数已经有值了，则填入第二次取数中
	        getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_7", new UFDouble(weight)); 
	        getBillCardPanelWrapper().getBillCardPanel().setTailItem("def_3", ts); 
	        getBillCardPanelWrapper().getBillCardPanel().setTailItem("endperson", _getOperator()); 
	    }
	    else{
	        this.getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_6", new UFDouble(weight));
	        getBillCardPanelWrapper().getBillCardPanel().setTailItem("def_2", ts); 	
	    }
        String[] formual=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sumsuttle").getEditFormulas();//获取净重编辑公式
        getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(formual);                         //执行编辑公式
      }else{
      	getBillUI().showErrorMessage("司磅取数失败,请重新读取!");
      }
    }


    /*
     * 说明：司磅单自动取数
     * @作者:牛冶
     * @时间:2008-4-12
     */
    private boolean isCheckType() {
        //判断取数方式是否为自动取数
        String type = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").getValueObject()==null?
                "0":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").getValueObject().toString() ;
        if("0".equalsIgnoreCase(type)){
            getBillUI().showErrorMessage("取数方式不匹配!");
            return false;
        }
        return true;
    }
    
    public void onBoAdd(ButtonObject bo) throws Exception {
        super.onBoAdd(bo);
        //列可用状态
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").setEnabled(true);
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(true);
        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(true);
        
        getBillUI().updateButtonUI();
    }
    
    
    
    @SuppressWarnings("unchecked")
	protected void onBoEdit() throws Exception {    	
    	String emptyload=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("emptyload").getValueObject()==null?""
   			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("emptyload").getValueObject().toString();
      	 String fullload=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fullload").getValueObject()==null?""
      			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fullload").getValueObject().toString();
         
    	ref=(UIRefPane) getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").getComponent();
        Object closeFlag = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("close_flag").getValueObject();
        if (closeFlag!=null && ("true".equals(closeFlag))){
            this.getBillUI().showErrorMessage("司磅单已关闭!");
            return;
        }

        super.onBoEdit3();		//司磅单是可以非制单人外进行修改的 add by zqy  2010-11-17 11:30:19 
        
        //add by houcq begin
          getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").setEnabled(false);
     	  IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
          Object billno = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").getValueObject();
          String oldsql = " select readflag from eh_sbbill  where pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"' and isnull(dr,0)=0 and billno='"+billno.toString()+"'";
      	  try {
  	           ArrayList all = (ArrayList) iUAPQueryBS.executeQuery(oldsql.toString(), new MapListProcessor());
  	           if(all.size()>0)
  	           { 
  	 	             HashMap oldhm = (HashMap) all.get(0);
  	 	             String oldreadflag = oldhm.get("readflag")==null?"":oldhm.get("readflag").toString();
  	 	             if(oldreadflag.equals("Y"))
  	 	             {
  	 	            	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("readflag").setEnabled(false);
  	 	            	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").setEnabled(true);
  	 	             	  
  	 	             }
  	           }                
  	       } catch (BusinessException e1) 
  	       {
  	           e1.printStackTrace();
  	       } 
          //add by houcq end
        String sbtype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject()==null
        ?"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject().toString();
        if(!sbtype.equals("0")){
        	
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
            if(sbtype.equals("1")){
            	sbtype = ICombobox.STR_SBBILLTYPE[1]; 
            }
            if(sbtype.equals("2")){
//            	String cppks=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject()==null?"":
//                	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject().toString();
//            	if(!cppks.equals("")){
//            		 String sqlupdate="update eh_icout set sb_flag='N' where pk_icout in "+cppks;
//                     PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//                     pubitf.updateSQL(sqlupdate);
//            	}
            	sbtype = ICombobox.STR_SBBILLTYPE[2]; 
            }
//            if(sbtype.equals("3")){//tuihuo 
//            	sbtype = ICombobox.STR_SBBILLTYPE[3]; 
//            }
        	changeRefModel(sbtype);
        }
        //如果是自动读取则数据重车重量、空车重量将不允许编辑
        String getType = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").getValueObject()==null?
                "0":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").getValueObject().toString() ;
        //getBillCardPanelWrapper().getBillCardPanel().getHeadItem("numbertype").setEnabled(false);				//不允许修改司磅类型
        if(getType.equalsIgnoreCase("1")){
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").setEnabled(false);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").setEnabled(false);
        }else{
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").setEnabled(true);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").setEnabled(true);
        }
        getBillUI().updateButtonUI();
    	 if(!emptyload.equals("")&&!fullload.equals("")){
       	    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").setEnabled(false);
            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").setEnabled(false);
        }
    	 //add by houcq begin 2011-01-05二次读数之后，不允许重新读数
    	 String firstNumber = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject()==null?
                 "":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject().toString() ; 
       	String secondNumber = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject()==null?
                   "":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject().toString() ;
       	if (!"".equals(secondNumber)&&!"".equals(firstNumber))
       	{
       	   getButtonManager().getButton(IEHButton.FIRSTREADDATE).setEnabled(false);
       	   getBillUI().updateButtonUI();
       	}    	
       	//add by houcq end 
    	/*
    	 * 1、司磅单生成检测申请单后，司磅单中增加检测申请标记（已有）。
		 * 2、已有检测申请标记的司磅单中的：司磅类型、选择司磅类型、客户三个字段在任何情况下，均禁止修改
		 *	（目前在第一次选择时客户字段不可修改，当保存再点修改时，客户字段则可以修改了，应该也不能修改）。
    	 */
    	//add by houcq 2011-06-22 begin
       	if(sbtype.equals("原料司磅"))
       	{
       		SbbillVO hvo = (SbbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        	String sql ="select ycy_flag from eh_sbbill where pk_sbbill= '"+hvo.getPk_sbbill()+"' and ycy_flag='Y'";
        	ArrayList cy = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
        	if (cy!=null&& cy.size()>0)
        	{
        		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").setEnabled(false);
               	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(false);
               	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
        	}
        	getButtonManager().getButton(IBillButton.DelLine).setEnabled(false);
        	getBillUI().updateButtonUI();
       	}    	
    	if(sbtype.equals("成品司磅"))
    	{
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").setEnabled(false);
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(false);
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);
    	}
    	//end
    }
	

    @SuppressWarnings("unchecked")
	protected void onBoDelete() throws Exception {
    	//add by houcq 2011-06-22 begin 已经抽样不允许删除
    	 String sbtype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject()==null
         ?"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject().toString();
    	if("1".equals(sbtype))
       	{
       		SbbillVO hvo = (SbbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        	String sql ="select ycy_flag from eh_sbbill where pk_sbbill= '"+hvo.getPk_sbbill()+"' and ycy_flag='Y'";
        	ArrayList cy = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
        	if (cy!=null&& cy.size()>0)
        	{
        		getBillUI().showErrorMessage("该单据已做原料检测申请,禁止删除!");
                return;
        	}
       	}  
    	//end
    	 String emptyload=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("emptyload").getValueObject()==null?""
    			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("emptyload").getValueObject().toString();
       	 String fullload=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fullload").getValueObject()==null?""
       			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fullload").getValueObject().toString();
         if(!emptyload.equals("")&&!fullload.equals("")){
        	 getBillUI().showErrorMessage("已司磅数据不允许删除！");
        	return;
         }
         
        Object closeFlag = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("close_flag").getValueObject();
        if (closeFlag!=null && ("true".equals(closeFlag))){
            this.getBillUI().showErrorMessage("司磅单已关闭!");
            return;
        }
        super.onBoDelete2();
    }
    
    public void changeRefModel(String objtype){
		if(objtype.equals(ICombobox.STR_SBBILLTYPE[0])){
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(false);
		}
		if(objtype.equals(ICombobox.STR_SBBILLTYPE[1])){   //收货通知单
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
			nc.ui.eh.businessref.ReceiptRefModel invRefModel = new nc.ui.eh.businessref.ReceiptRefModel();
    	    ref.setRefModel(invRefModel);
    	    ref.setMultiSelectedEnabled(false);
            ref.setTreeGridNodeMultiSelected(false);
    	    String fumual = "vsourcebilltype->getColValue(eh_stock_receipt, vbilltype, pk_receipt, getColValue(eh_stock_receipt_b, pk_receipt, pk_receipt_b, vbillno));";
    	    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEditFormula(new String[]{fumual} );//子表主键
    	    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setAddSelectedListener(true);
    	    getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_invbasdoc").setEnabled(false);
    	}
    	if(objtype.equals(ICombobox.STR_SBBILLTYPE[2])){        // 成品出库
    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
    		nc.ui.eh.businessref.IcoutRefModel IcoutRefModel = new nc.ui.eh.businessref.IcoutRefModel();
    		
    		ref.setRefModel(IcoutRefModel);
    		ref.setMultiSelectedEnabled(true);
            ref.setTreeGridNodeMultiSelected(true);
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vsourcebilltype",IBillType.eh_z0255001);
    	}
    	
//    	if(objtype.equals(ICombobox.STR_SBBILLTYPE[3])){         // 原料退货出库
//    		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vbillno").setEnabled(true);
//    		nc.ui.eh.businessref.BackRefModel CkdRefModel = new nc.ui.eh.businessref.BackRefModel();
//    		ref.setRefModel(CkdRefModel);
//            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("vsourcebilltype",IBillType.eh_z0502515);
//    	}
	}
    
    @SuppressWarnings({ "static-access", "unchecked" })
	public void onBoSave() throws Exception {
        //非空判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        //如果空车重量与重车重量数据都有，将自动关闭该张司磅单
        Object FirstWeight = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject();
        Object SecondWeight = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject();
        //如果空车与重车对司磅单中包装物重及包装件数进行计算
        if(FirstWeight!=null && SecondWeight!=null && (!FirstWeight.equals("")) && (!SecondWeight.equals(""))){
        	 //对空车对单件重和包装物重的计算
            UFDouble packnum = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("packnum").
            		getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("packnum").
            				getValueObject().toString());
            String pk_sbbill=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").
    		getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").
    				getValueObject().toString();
            if(!pk_sbbill.equals("")){
                	UFDouble suttle = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("suttle").
                    		getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("suttle").
                    				getValueObject().toString());
                	
                	UFDouble singleweight=suttle.div(packnum);
                	
                	UFDouble wrapperweight = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("wrapperweight").
                    		getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("wrapperweight").
                    				getValueObject().toString());    	
                	UFDouble bzkz=wrapperweight.multiply(packnum);
                	
                	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("singleweight", singleweight);//单件重量
                	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("bzkz", bzkz);//包装物总重
                }
	            UFDouble FirstWeights = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject()==null?
						"0":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject().toString());
				UFDouble SecondWeights = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject()==null?
									"0":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject().toString());
				if (SecondWeights.compareTo(FirstWeights)<0){
				//说明第一次比第二次重
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("emptyload", SecondWeight);//空车
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("fullload", FirstWeight);//重车
				}else{
				//说明第一次比第二次轻
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("fullload", SecondWeight);//重车
				getBillCardPanelWrapper().getBillCardPanel().setHeadItem("emptyload", FirstWeight);//空车
				}  
            }
        //执行公式  
		String[] formulas = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("emptyload").getEditFormulas();
		getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(formulas);
        String[] fullload= getBillCardPanelWrapper().getBillCardPanel().getHeadItem("fullload").getEditFormulas();
        getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(fullload);
        String [] packnum=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("packnum").getEditFormulas();
        getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(packnum);
        String[] wrapperweight=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("wrapperweight").getEditFormulas();
        getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(wrapperweight);
        //只有第一次读重的时候才能执行公式 外过磅的时候能用 保存净重 by wm 2008年9月28日11:16:19
        String first=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject()==null?""
   			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject().toString();
      	 String second=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject()==null?""
      			 :getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject().toString();
        if(!first.equals("") && second.equals("")){
        	String[] sformulas = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sumsuttle").getEditFormulas();
    		getBillCardPanelWrapper().getBillCardPanel().execHeadFormulas(sformulas);
        }
     
        String sbtype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject()==null
        ?"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject().toString();
        /** modify by houcq 2011-05-12保存时不再修改读重时间
        //最修改人后人
        UFDouble dyi=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject()==null?"0":
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject().toString());
        UFDouble dye=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject()==null?"0":
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject().toString());
        String deperson=getBillCardPanelWrapper().getBillCardPanel().getTailItem("endperson").getValueObject()==null?"":
        	getBillCardPanelWrapper().getBillCardPanel().getTailItem("endperson").getValueObject().toString();
        
        UFDateTime ts = ce.getServerTime();
        if(dyi.toDouble()>0 && dye.toDouble()>0 && deperson.equals("")){
        	 getBillCardPanelWrapper().getBillCardPanel().setTailItem("def_3", ts); 
 	         getBillCardPanelWrapper().getBillCardPanel().setTailItem("endperson", _getOperator()); 
        }else if(dyi.toDouble()>0 && dye.toDouble()==0){
        	getBillCardPanelWrapper().getBillCardPanel().setTailItem("def_2", ts); 
        }else if(dyi.toDouble()==0 && dye.toDouble()>0){
        	getBillCardPanelWrapper().getBillCardPanel().setTailItem("def_2", ts); 
        }
        */
        
        if(sbtype.equals("2")){
        	//add by houcq 2010-10-18 begin
        	//判断表体不能为空
   		 	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
   	        BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
   	        if (model != null) {
   	            int rowCount = model.getRowCount();
   	            if (rowCount < 1) {
   	                NCOptionPane.showMessageDialog(getBillUI(), "成品未出库，不允许保存，请先进行成品出库!");
   	                return;
   	            }
   	        }
   	        //add by houcq end
            String pk_sbbill=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").
      		 getValueObject()==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").
      		 getValueObject().toString();
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
              
        	if(!pk_sbbill.equals("")){
           	 String sqlht="select sum(rkmount) amount from eh_sc_cprkd a,eh_sc_cprkd_b b where a.pk_rkd=b.pk_rkd and a.pk_sbbill='"+pk_sbbill+"'";
           	 ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sqlht, new MapListProcessor());
           	 for(int i=0;i<al.size();i++){
           		 HashMap hm=(HashMap) al.get(i);
           		 UFDouble rkmount=new UFDouble(hm.get("amount")==null?"":hm.get("amount").toString()); 
           		 getBillCardPanelWrapper().getBillCardPanel().setHeadItem("thamount", rkmount);
           	 }
            }	
        	//add by houcq begin 2011-03-02在出库单中回写sb_flag
        	String cppks=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject()==null?"":
            	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject().toString();
        	String sqlupdate="update eh_icout set sb_flag='Y' where pk_icout in "+cppks;
            PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            pubitf.updateSQL(sqlupdate);
        	//add by houcq end
        }
        if(sbtype.equals("1")){
        	//add by houcq 2010-10-18 begin
        	//判断表体不能为空
   		 	getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
   	        BillModel model = getBillCardPanelWrapper().getBillCardPanel().getBillModel();
   	        if (model != null) {
   	            int rowCount = model.getRowCount();
   	            if (rowCount < 1) {
   	                NCOptionPane.showMessageDialog(getBillUI(), "错误：来源单据为空，请选择收货通知单!");
   	                return;
   	            }
   	        }
        }
   	        //add by houcq end
        /**在原料退货司磅中司磅单的净重在高于退货通知单的退货重量时不允许保存 add by wb 2008-10-31 10:10:26**/
        if(sbtype.equals("3")){
        	SbbillVO hvo = (SbbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        	SbbillBVO[] bvos = (SbbillBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        	if(bvos!=null&&bvos.length>0){
        		String[] pk_back_bs = new String[bvos.length]; 
        		for(int i=0;i<bvos.length;i++){
        			pk_back_bs[i] = bvos[i].getDef_6();				//原料退货子表主键
        		}
        		String pk_back_b = PubTools.combinArrayToString(pk_back_bs);
        		UFDouble sumsuttle = hvo.getSumsuttle()==null?new UFDouble(0):hvo.getSumsuttle();				//司磅单净重
        		String pk_sbbill = hvo.getPk_sbbill();
        		StringBuffer sql = new StringBuffer()
        		.append(" select a.sbamount,b.backamount")
        		.append(" from ")
        		.append(" (select sum(distinct isnull(a.sumsuttle,0)) sbamount")
        		.append(" from eh_sbbill a,eh_sbbill_b b")
        		.append(" where a.pk_sbbill = b.pk_sbbill")
        		.append(" and b.def_6 in "+pk_back_b+"")
        		.append(" and a.pk_sbbill <> '"+pk_sbbill+"'")
        		.append(" and isnull(a.dr,0)=0")
        		.append(" and isnull(b.dr,0)=0")
        		.append(" ) a,")
        		.append(" (select sum(isnull(b.amount,0))*1000 backamount")			//退货中是吨重,转化为千克
        		.append(" from eh_stock_back a,eh_stock_back_b b")
        		.append(" where a.pk_back = b.pk_back")
        		.append(" and b.pk_back_b in "+pk_back_b+"")
        		.append(" and isnull(a.dr,0)=0 and isnull(b.dr,0)=0")
        		.append(" ) b");
        		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
                ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                if(arr!=null&&arr.size()==1){
                	HashMap hm = (HashMap)arr.get(0);
                	UFDouble sbamount = new UFDouble(hm.get("sbamount")==null?"0":hm.get("sbamount").toString());
                	UFDouble backamount = new UFDouble(hm.get("backamount")==null?"0":hm.get("backamount").toString());
                	UFDouble subamount = new UFDouble(sbamount.add(sumsuttle).sub(backamount).toDouble(),0);
                	if(subamount.toDouble()>0){
                		getBillUI().showErrorMessage("原料退货司磅单总净重超过实际退货数量,超过"+subamount+"kg,不能保存,请检查!");
                		return;
                	}
                }
        	}
        }
        
        
        //要司磅的退货通知单司磅完毕，把重量等包装物重回写到退货通知单中去
        UFDouble def_6=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject()==null?"0":
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_6").getValueObject().toString());
        UFDouble def_7=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject()==null?"0":
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_7").getValueObject().toString());
        
        if(sbtype.equals("3") && def_6.toDouble()>0 && def_7.toDouble()>0){
        	SbbillVO hvo = (SbbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        	String cpah=hvo.getCarnumber()==null?"":hvo.getCarnumber().toString();
        	String cpih=hvo.getTranno()==null?"":hvo.getTranno().toString();
        	UFDouble fye=new UFDouble(0);
        	UFDouble taxmoney=new UFDouble(0);
        	UFDouble Suttle=hvo.getSuttle()==null?new UFDouble(0):hvo.getSuttle().div(1000); //净重（千克)/1000
        	UFDouble bzkz=hvo.getBzkz()==null?new UFDouble(0):hvo.getBzkz().div(1000);       //包装重量（千克）/1000
        	String pk_back_b=hvo.getVsourcebillrowid()==null?"":hvo.getVsourcebillrowid().toString();
        	String pk_back=hvo.getVsourcebillid()==null?"":hvo.getVsourcebillid().toString();
        	String updatesql="update eh_stock_back_b set packagweight="+bzkz+" ,spweight="+Suttle+" ,weight="+Suttle+" where pk_back_b='"+pk_back_b+"'";
        	String updatesql2="update eh_stock_back set isrk='Y' ,carnumber='"+cpah+"',tranno='"+cpih+"' where pk_back='"+pk_back+"'";
        	String selsql="select fye,taxmoney from eh_stock_back_b where pk_back_b='"+pk_back_b+"'";
        	IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(selsql, new MapListProcessor());
        	for(int i=0;i<al.size();i++){
        		HashMap hm=(HashMap) al.get(i);
        		fye=new UFDouble(hm.get("fye")==null?"0":hm.get("fye").toString());
        		taxmoney=new UFDouble(hm.get("taxmoney")==null?"0":hm.get("taxmoney").toString());
        	}
        	UFDouble fyeze=fye.multiply(Suttle);
        	UFDouble ze=taxmoney.add(fyeze);
        	String sqlupdate3="update eh_stock_back set fymoney="+fyeze+" where pk_back='"+pk_back+"'";
        	String sqlupdate4="update eh_stock_back set summoney="+ze+" where pk_back='"+pk_back+"'";
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	pubitf.updateSQL(updatesql);
        	pubitf.updateSQL(updatesql2);
        	pubitf.updateSQL(sqlupdate3);
        	pubitf.updateSQL(sqlupdate4);
        }
        
       
        
        /******************************** end *******************************************/
        
        super.onBoSave2_whitBillno(); 
       //add by houcq 2011-06-01 begin
        SbbillVO hvo = (SbbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        new PubTools().recordTime(hvo.getVbilltype(), hvo.getPk_sbbill(), hvo.getPk_corp(), hvo.getCoperatorid());
        //add by houcq end
        setBoEnabled();
    }
    
    public void onButton(ButtonObject bo){
        onButton_N(bo,null);
    }

    protected void onBoPrint() throws Exception {
    	int i=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
//    	String pk_sbbill=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill")==null?"":getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_sbbill").toString();
//    	if(i==0){
//    		onBoLineAdd();
//    		super.onBoPrint();
//    	}else{
//    		super.onBoPrint();
//    	} 
    	//add by houcq begin 2010-09-26 更新打印次数
    	
    	if(i==0)
    	{
    		onBoLineAdd();    		
    	}
    	int num=0;
    	String billno = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").getValueObject().toString();
    	String old = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_10").getValueObject().toString();
    	if (null==old||"".equals(old))
    	{
    		old="-1";
    	}
    	else 
    	{    		
        	try {
            	num= Integer.valueOf(old).intValue()+1;
        	} catch (Exception e) {
        		getBillUI().showErrorMessage("def_10字段的值不能转换为数字");
        		return;
        	}
		}
    	
    	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName()); 
    	String sql = " update eh_sbbill set def_10='"+num+"' where pk_corp='"+ce.getCorporation().getPk_corp()+"' and billno='"+billno+"'";
    	pubitf.updateSQL(sql);
    	onBoRefresh();
    	super.onBoPrint();
    	
    }    
    private QueryConditionClient dlg = null;
    protected QueryConditionClient getQueryDLG()
    {        
        if(dlg == null){
            dlg = createQueryDLG();
        }
        return dlg;
    }
    
   protected void onBoQuery() throws Exception {
   	ClientEnvironment ce = ClientEnvironment.getInstance();
   	int type = getQueryDLG().showModal();
       if(type==1){
			String sqlWhere = getQueryDLG().getWhereSQL()==null?"":getQueryDLG().getWhereSQL();
			sqlWhere = sqlWhere.replaceFirst("审批不通过", String.valueOf(IBillStatus.NOPASS));
			sqlWhere = sqlWhere.replaceFirst("审批通过", String.valueOf(IBillStatus.CHECKPASS));
			sqlWhere = sqlWhere.replaceFirst("审批中", String.valueOf(IBillStatus.CHECKGOING));
			sqlWhere = sqlWhere.replaceFirst("提交态", String.valueOf(IBillStatus.COMMIT));
			sqlWhere = sqlWhere.replaceFirst("作废", String.valueOf(IBillStatus.DELETE));
			sqlWhere = sqlWhere.replaceFirst("冲销", String.valueOf(IBillStatus.CX));
			sqlWhere = sqlWhere.replaceFirst("终止", String.valueOf(IBillStatus.ENDED));
			sqlWhere = sqlWhere.replaceFirst("冻结状态", String.valueOf(IBillStatus.FREEZE));
			sqlWhere = sqlWhere.replaceFirst("自由态", String.valueOf(IBillStatus.FREE));
			if(sqlWhere==null||sqlWhere.equals("")){
				sqlWhere =" 1=1 ";
			}
			if(addCondtion()!=null&&addCondtion().length()>0){
				sqlWhere = sqlWhere + " and "+addCondtion();
			}
			SuperVO[] queryVos = queryHeadVOs(sqlWhere+" and pk_corp = '"+ce.getCorporation().getPk_corp()+"'");
			
	       getBufferData().clear();
	       // 增加数据到Buffer
	       addDataToBuffer(queryVos);
	
	       updateBuffer();
       }
       setBoEnabled();
	}
   
   //查询模板中加上查询条件为制单一个月之内的单据
   protected QueryConditionClient createQueryDLG() {
   	QueryConditionClient dlg = new QueryConditionClient();
   	String billtype = getUIController().getBillType();           // 单据类型
       String sql = "select nodecode from bd_billtype where pk_billtypecode = '"+billtype+"' and isnull(dr,0)=0"; //取节点号
       IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
       try {
			String nodecode = iUAPQueryBS.executeQuery(sql, new ColumnProcessor())==null?null:
							iUAPQueryBS.executeQuery(sql, new ColumnProcessor()).toString();
            String lastdate = _getDate().getDateBefore(1).toString();
	        dlg.setTempletID(_getCorp().getPk_corp(), nodecode, null, null); 
	        dlg.setDefaultValue("dmakedate",lastdate,null);
	        addQueryDefaultValue();
	        dlg.setNormalShow(false);
       }catch (BusinessException e) {
			e.printStackTrace();
		}
       return dlg;
   }
   
   /**查询时增加条件 " vbilltype = "" and ..."
    * @return
    */
   public String addCondtion(){
   	return null;
   }
   
   public void addQueryDefaultValue(){

   }
  
    
    //所有客户
    @SuppressWarnings("unchecked")
    public static HashMap getAllCubasdoc(){
        HashMap hmcubasdoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql = "select pk_cubasdoc,custcode from bd_cubasdoc where isnull(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null&&arr.size()>0){
                String pk_cubasdoc = null;
                String custcode = null;
                for(int i=0; i<arr.size(); i++){
                    HashMap hmarr = (HashMap)arr.get(i);
                    pk_cubasdoc = hmarr.get("pk_cubasdoc")==null?"":hmarr.get("pk_cubasdoc").toString();
                    custcode = hmarr.get("custcode")==null?"":hmarr.get("custcode").toString();
                    hmcubasdoc.put(custcode, pk_cubasdoc);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hmcubasdoc;
    }
    
    //所有物料
    @SuppressWarnings("unchecked")
	public static HashMap getAllInvbasdoc(){
        HashMap hminvbasdoc = new HashMap();
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        String sql ="select pk_invbasdoc,invcode from eh_invbasdoc where isnull(dr,0)=0 ";
        try {
            ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(arr!=null && arr.size()>0){
                String pk_invbasdoc = null;
                String invcode = null;
                for(int i=0;i<arr.size();i++){
                    HashMap hm = (HashMap)arr.get(i);
                    pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
                    invcode = hm.get("invcode")==null?"":hm.get("invcode").toString();
                    hminvbasdoc.put(invcode, pk_invbasdoc);
                }
            }
        } catch (BusinessException e) {
            e.printStackTrace();
        }
        return hminvbasdoc;
    }
    
    
//  设置按钮的可用状态
    protected void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo=getBillUI().getVOFromUI();
        Integer vbillstatus=(Integer)aggvo.getParentVO().getAttributeValue("vbillstatus");
        if (vbillstatus==null){
        }
        else{   
            switch (vbillstatus.intValue()){
                //free
                case IBillStatus.FREE:
                        getButtonManager().getButton(IBillButton.Edit).setEnabled(true);
                        getButtonManager().getButton(IBillButton.Delete).setEnabled(true);
                        getButtonManager().getButton(IBillButton.Action).getChildButtonGroup()[0].setEnabled(true);
                        break;
                //commit
                case IBillStatus.COMMIT:
                    getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                    getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                    getButtonManager().getButton(IBillButton.Action).getChildButtonGroup()[0].setEnabled(false);
                    getButtonManager().getButton(IBillButton.Action).getChildButtonGroup()[1].setEnabled(true);
                    break;
                //CHECKGOING
                case IBillStatus.CHECKGOING:
                    getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                    getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                    break;
                //CHECKPASS
                case IBillStatus.CHECKPASS:
                	getButtonManager().getButton(IBillButton.Action).getChildButtonGroup()[0].setEnabled(false);
                	getButtonManager().getButton(IBillButton.Action).getChildButtonGroup()[1].setEnabled(false);
                //NOPASS
                case IBillStatus.NOPASS:
                        getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                        getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                        break;
                
            }
        }
        // 在有关闭按钮时对关闭按钮的控制 add by wb at 2008-6-20 14:30:23
            String[] keys = aggvo.getParentVO().getAttributeNames();
            if(keys!=null && keys.length>0){
                for(int i=0;i<keys.length;i++){
                    if(keys[i].endsWith("close_flag")){ 
                    	String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("close_flag").getValueObject()==null?"N":
                            getBillCardPanelWrapper().getBillCardPanel().getHeadItem("close_flag").getValueObject().toString();
                        if(lock_flag.equals("false")){
                        	if(getButtonManager().getButton(IEHButton.SBCLOSE)!=null){
                        		getButtonManager().getButton(IEHButton.SBCLOSE).setEnabled(true);
                        	}
                        	if(getButtonManager().getButton(IEHButton.BusinesBtn)!=null){
//                        		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(true);	
                        	}
                        }else{
                        	if(getButtonManager().getButton(IEHButton.SBCLOSE)!=null){
                        		getButtonManager().getButton(IEHButton.SBCLOSE).setEnabled(false);
                        	}
                        	if(getButtonManager().getButton(IEHButton.BusinesBtn)!=null){
                        		getButtonManager().getButton(IEHButton.BusinesBtn).getChildButtonGroup()[0].setEnabled(false);	//在业务操作下第一个按钮设为不可操作
                        	}
                        	getButtonManager().getButton(IBillButton.Edit).setEnabled(false);
                            getButtonManager().getButton(IBillButton.Delete).setEnabled(false);
                        }
                        break;
                    }
                    
                }
            }
        super.setBoEnabled();
        getBillUI().updateButtonUI();
    }
    
    @Override
    public void onBoAudit() throws Exception {
    	super.onBoAudit();
    	setBoEnabled();
    }

	/**
	 * 功能：在提交时对出库单的SB_FLAG回写Y在下次司磅时不能参照出来
	 * 时间:2009-12-16
	 * 作者：张志远
	 */
	public void onBoCommit() throws Exception {
		/** add by houcq 注释 2011-03-02将回写标记改在保存时进行
		//取得司磅类型
		String sbtype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject()==null
        ?"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject().toString();
		//取得司磅的所有出库单号
        String cppks=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject()==null?"":
        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("cppks").getValueObject().toString();
        //取得所选择的出库单表体PK和表头SB_FLAG
        StringBuffer str = new StringBuffer()
		.append(" SELECT DISTINCT b.pk_icout_b,c.sb_flag FROM eh_icout_b b,eh_icout c ")
		.append(" WHERE b.pk_icout = c.pk_icout ")
		.append(" AND c.pk_icout in "+cppks+" AND NVL(c.dr,0)=0 ");
        //有出库单的成品司磅
        if(sbtype.equals("2")&&!cppks.equals("")){
        	//存放所选出库单的所有表体PK和SB_FLAG
        	HashMap pkHm = new HashMap();
        	IUAPQueryBS iq = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	ArrayList arr = (ArrayList) iq.executeQuery(str.toString(), new MapListProcessor());
        	if(arr!=null&&arr.size()>0){
        		for(int i=0; i<arr.size();i++){
        			HashMap hm = (HashMap) arr.get(i);
        			String pk_icout = hm.get("pk_icout_b")==null?"":hm.get("pk_icout_b").toString();
        			String sb_flag = hm.get("sb_flag")==null?"N":hm.get("sb_flag").toString();
        			pkHm.put(pk_icout,sb_flag);
        		}
        		int rows = this.getBillCardPanelWrapper().getBillCardPanel().getRowCount();//表体行数
        		StringBuffer mess = new StringBuffer().append("行");
        		for(int i=0;i<rows;i++){
        			//def_6保存的是出库单的子表PK
        			String pk_icout = this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_6").toString();
        			if(pkHm.containsKey(pk_icout)){
        				String sb_flag = pkHm.get(pk_icout).toString();
            			if("Y".equals(sb_flag)){
            				mess.append(" "+(i+1)+"");
            			}
        			}
        		}
        		if(!mess.toString().equals("行")){
        			mess.append(" 出库单已被司磅！请重新选择司磅类型！");
        			this.getBillUI().showErrorMessage(mess.toString());
        			return;
        		}
        	}
        	//对所选的出库单回写SB_FLAG标记Y
        	 String sqlupdate="update eh_icout set sb_flag='Y' where pk_icout in "+cppks;
             PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
             pubitf.updateSQL(sqlupdate);
        }
        */
		//super.onBoCommit();
		super.onBoCommit2();//modify by houcq 2011-01-05
	}
    
//	protected void onBoLineDel() throws Exception {
//    	//add by houcq begin 2011-06-20删行时取消出库单司磅标记sb_flag
//      String sbtype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject()==null
//      ?"": getBillCardPanelWrapper().getBillCardPanel().getHeadItem("sbtype").getValueObject().toString();
//      if ("2".equals(sbtype))
//      {  
//      	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
//      	int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRowCount();    
//  		for (int i = 0; i < row; i++) {					
//  			String vsourcebillrowno = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_2")==null?"":
//  		                 getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "def_2").toString();  		            
//  			String Sql = " update eh_icout set sb_flag='N' where pk_corp='"+this._getCorp().getPk_corp()+"' and def_2='"+vsourcebillrowno+"'";
//  		            pubitf.updateSQL(Sql);
//  		}
//      }
//   }
}
