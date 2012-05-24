package nc.ui.eh.trade.z0206510;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import nc.bs.eh.trade.pub.Tools;
import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.businessref.YsContractRefModel;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IPubInterface;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.pub.PubTool;
import nc.vo.eh.trade.z0206510.LadingbillBVO;
import nc.vo.eh.trade.z0206510.LadingbillVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;


/**
 * 功能 提货通知单
 * @author 洪海
 * 2008-04-08
 */

public class ClientEventHandler extends AbstractEventHandler {
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    public static int flag = 0;            // 增加单据标记(自制 0,从销售订单 1)
    public static int invoiceflag = 0; // 增加单据标记(2 销售发票)
    
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }
    //add by houcq 2010-11-23解决单据提交后，查询出来后可以修改
    protected void onBoQuery() throws Exception {
    	   super.onBoQuery();
           setBoEnabled();
    	}
    @Override
    protected void onBoEdit() throws Exception {
        String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("不允许修改他人申请！");
            return;
        }
        super.onBoEdit();
        int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        for(int i=0;i<row;i++){
            Object vsourcebillid=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "vsourcebillid");
            if(vsourcebillid!=null){
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vinvbascode", false);
            }
        }
        String pk_cubasdoc = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
        YsContractRefModel.pk_cubasdoc = pk_cubasdoc;  //将客商传到参照中
        getBillUI().updateUI();
        
    }
    
    /* （非 Javadoc）
     * @see nc.ui.trade.bill.BillEventHandler#onBoElse(int)
     */
    protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.TEMPLETDISCOUNT:    //临时折扣
                onBoTEMPLETDISCOUNT();
                break;
        }
        super.onBoElse(intBtn);
    }
    @SuppressWarnings("unchecked")
	@Override
    protected void onBoDelete() throws Exception {
    	int res = onBoDeleteN(); // 1为删除 0为取消删除
    	if(res==0){
    		return;
    	}
        String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
        if(!coperatorid.equals(_getOperator())){
            getBillUI().showErrorMessage("不允许删除他人申请！");
            return;
        }
       //回写到销售订单
        LadingbillVO vo=(LadingbillVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        //add by houcq 2011-11-18 begin 当前要删除的提货通知单如果有二次折扣,则删除折扣期间中的二次折扣,并将已扣减的余额恢复
        String pk_ladingbill = vo.getPk_ladingbill();
        if (pk_ladingbill!=null)
        {
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        	String sql ="delete from eh_perioddiscount where def_1='"+pk_ladingbill+"'";
        	pubitf.updateSQL(sql);
        	String sqlnew = "select pk_cubasdoc pk_cubasdoc,bcyfje kuye ,pk_corp pk_corp  from eh_ladingbill where pk_ladingbill = '"+pk_ladingbill+"'";
        	new Tools().changeCusOverage(sqlnew,"+");
        }
       //add by houcq 2011-11-18 end
        LadingbillBVO [] bvo=(LadingbillBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        ArrayList alpk_order=new ArrayList();
        //单据类型
        String billtype = vo.getVsourcebilltype()==null?"":vo.getVsourcebilltype();
        if(billtype.equals("ZA09")){
        for(int i=0;i<bvo.length;i++){
        	String pk_main=bvo[i].getVsourcebillrowid()==null?"":bvo[i].getVsourcebillrowid().toString();
        	if(!pk_main.equals("")){
        		alpk_order.add(pk_main);
        	}
        }
        if(alpk_order!=null&&alpk_order.size()>0){
        	String [] pk_order=(String[]) alpk_order.toArray(new String[alpk_order.size()]);
        	String pk_orde=PubTool.combinArrayToString(pk_order);
	        PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			String sqlupdate="update eh_order set th_flag='N' where pk_order in"+pk_orde;
			pubitf.updateSQL(sqlupdate);
        	
        }
        //end 
        }else if(billtype.equals("ZA14")){
        //-------------------------回写到销售发票-----------------
        for(int i=0;i<bvo.length;i++){
        	String pk_main=bvo[i].getVsourcebillrowid()==null?"":bvo[i].getVsourcebillrowid().toString();
        	if(!pk_main.equals("")){
        		alpk_order.add(pk_main);
        	}
        }
        if(alpk_order!=null&&alpk_order.size()>0){
        	String [] pk_order=(String[]) alpk_order.toArray(new String[alpk_order.size()]);
        	String pk_orde=PubTool.combinArrayToString(pk_order);
	        PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
			String sqlupdate="update eh_invoice set th_flag='N' where pk_invoice in"+pk_orde;
			pubitf.updateSQL(sqlupdate);
        }
        //------------------------------------------
        }
        super.onBoTrueDelete();
    }
    
        //提交
        public void onBoCommit() throws Exception {
            // TODO 自动生成方法存根
            String  coperatorid=getBillCardPanelWrapper().getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
            if(!coperatorid.equals(_getOperator())){
                getBillUI().showErrorMessage("不允许对他人申请进行提交！");
                return;
            }
           
            LadingbillVO  ladVO = (LadingbillVO)getBillUI().getChangedVOFromUI().getParentVO();            
            //add by houcq 2011-15 begin
            String billId=ladVO.getPk_ladingbill();
            if (billId!=null)
            {
            	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
                pubitf.kjye(billId,_getDate());
            }
            else
            {
            	 getBillUI().showErrorMessage("主键为空,不允许提交!");
                 return;
            }
          //add by houcq 2011-15 end
            super.onBoCommit();
        }       
        @SuppressWarnings("unchecked")
		public void onButton_N(ButtonObject bo, BillModel model) {
        	
        	flag = 0;            // 增加单据标记(自制 0,从销售订单 1)
        	invoiceflag = 0; 	// 增加单据标记(2 销售发票)
        	String bocode=bo.getCode()==null?"":bo.getCode();        	
        	if(bocode.equals("自制单据")||bocode.equals("销售订单")||bocode.equals("销售发票")){

       		 //当折扣期间计算中生成下月数据、启用功能，任何一个功能标记后，就不允许再在该月度内录入折扣调整单。add by houcq 2011-07-07
        		StringBuffer str = new StringBuffer()
        		.append(" SELECT * FROM eh_perioddiscount_h ")
        		.append(" WHERE nyear = "+_getDate().getYear())
        		.append(" AND nmonth = "+_getDate().getMonth())
        		.append(" and pk_corp = '"+_getCorp().getPrimaryKey()+"'")
        		.append(" AND NVL(dr,0)=0 ")
        		.append(" and (scxy_flag='Y' or qy_flag='Y')");
        		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        		try {
        			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(str.toString(), new MapListProcessor());
        			if(arr!=null && arr.size()>0){
        				getBillUI().showErrorMessage("本期折扣期间数据已结转，禁止新的制单操作!");
        				return;
        	        }
        		} catch (BusinessException e) {
        			e.printStackTrace();
        		}
                 /**提货单：如超24小时没有计算盈亏考核系统将不允许再开提货单 add by zqy 2008年10月22日18:20:10*/
                 String befordate = _getDate().getDateBefore(2).toString();     //系统登陆前2天
                 String bjdate = null;    //比较日期
                 if(befordate.substring(8, 9).equals("0")){
                     bjdate = befordate.substring(9, 10);
                 }else{
                     bjdate = befordate.substring(8, 10);
                 }
                 
                 ArrayList list = new ArrayList();
                 StringBuffer sql = new StringBuffer()
                 .append(" select vdate from eh_trade_checkdate where pk_corp='"+_getCorp().getPk_corp()+"' and isnull(dr,0)=0 ");
                 try {
                     ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
                     if(arr!=null && arr.size()>0){
                         String date = null;  //核算日期
                         for(int i=0;i<arr.size();i++){
                             HashMap hm = (HashMap)arr.get(i);
                             date = hm.get("date")==null?"":hm.get("date").toString();
                             if(date.equals(bjdate)){
                                 list.add("Y");
                             }
                         }                   
                     }
                 } catch (BusinessException e1) {
                     e1.printStackTrace();
                 }
                 String SQL = " select * from eh_trade_surpluscheck where dmakedate='"+befordate+"' and isnull(dr,0)=0 and pk_corp='"+_getCorp().getPk_corp()+"' ";
                 ArrayList all = null;
                 try {
                     all = (ArrayList)iUAPQueryBS.executeQuery(SQL.toString(), new MapListProcessor());
                 } catch (BusinessException e1) {
                     e1.printStackTrace();
                 }
                 if(list.size()>0 && list!=null && (all==null||all.size()==0) ){
                     getBillUI().showErrorMessage("目前已超过计算盈亏考核24小时,系统将不允许开提货单!");
                     return;
                 }
                 
                 //对公司期初建账进行控制时间：2009-12-15作者：张志远
                 if(!super.getEveDiscount()){
                	 this.getBillUI().showErrorMessage("本月期间折扣未建帐，请至折扣期间计算节点，进行本月折扣期初建帐");
                	 return;
                 }else{
                	//功能：对本月期初折扣的控制 时间：2009-12-15作者：张志远
                	 if(!super.getDiscount()){
                    	 this.getBillUI().showErrorMessage("本月期间折扣未计算，请至折扣期间计算节点，生成下月数据！");
                    	 return;
                     }
                 }
                //取消临时折扣BUTTON
             	//getButtonManager().getButton(IEHButton.TEMPLETDISCOUNT).setEnabled(true);
                 try {
                     getBillUI().updateButtonUI();
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             }
        	 
        	
        	super.onButton_N(bo, model);
        	//add by houcq 2011-05-06 begin 根据营销代表带出部门
            Object pk_psndoc =getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").getValueObject();
         	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
         	if (pk_psndoc!=null)
         	{
         		String sql ="select pk_deptdoc from bd_psndoc where pk_psndoc='"+pk_psndoc+"'";
             	Object obj;
 				try {
 					obj = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
 					getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_2",obj);
 				} catch (BusinessException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}            	
         	}
         	//add by houcq 2011-05-06 end
            //当从销售订单生成提货单时，客户和产品不允许编辑，同时表体不能进行行操作
            if(bocode.equals("销售订单")){
              flag = 1;
              getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
              getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(false);           
              int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            try {
              AggregatedValueObject vo = getBillCardPanelWrapper().getBillVOFromUI();
              for(int i=0;i<row;i++){
                 getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(i,"vinvbascode", false);
                 /**加上物料的已开提货单未出库数,已生产派工未入库数量  add by wb at 2008-10-22 18:51:11*/
                 String pk_invbasdoc = getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
               	  getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
     			UFDouble[] unckrk = ((ClientUI)getBillUI()).getUnckUnrk(pk_invbasdoc,_getDate());
//     			UFDouble kcamount = new PubTools().getKCamountByinv_Back(pk_invbasdoc, _getCorp().getPk_corp(),_getDate());
			//HashMap hmkc = new PubTools().getDateinvKC(null, pk_invbasdoc, _getDate(), "1", _getCorp().getPk_corp());
			//UFDouble kcamount = new UFDouble(hmkc.get(pk_invbasdoc)==null?"":hmkc.get(pk_invbasdoc).toString());
			//modify by houcq 2011-06-20修改取库存方法
			UFDouble kcamount = new PubTools().getInvKcAmount(_getCorp().getPk_corp(),_getDate(),pk_invbasdoc);;
			
     			if(unckrk!=null&&unckrk.length==2){
     				UFDouble ytwcamount = unckrk[0];		//已开提货单未出库数
     				UFDouble ypgwrkamount = unckrk[1];		//已生产派工未入库数量
     				UFDouble maxthamount = kcamount.sub(ytwcamount).add(ypgwrkamount);		//最大提货量
     				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(kcamount, i, "storeamount"); //实际库存量
     				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(ytwcamount, i, "ytwcamount");
     				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(ypgwrkamount, i, "ypgwrkamount");
     				getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(maxthamount, i, "maxthamount");
     			}
     			//getButtonManager().getButton(IEHButton.TEMPLETDISCOUNT).setEnabled(true);
                //下面一行由houcq注释2010-12-09解决速度问题
     			//getBillUI().updateButtonUI();
     			/********************************** end ********************************************/
     			 
     			/***从订单做提货时将提货数量默认 add by wb 2008-12-23 9:58:48****************************************/
     			
     			setBcthamount(vo);
     			
     			/************* end **********************************/
				}
              }catch (Exception e) {
					e.printStackTrace();
			  }
     			
              }
            
            //当从销售订单生成提货单时，客户和产品不允许编辑，同时表体不能进行行操作
            if(bocode.equals("销售发票")){
              invoiceflag = 2;
              getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").setEnabled(false);
              getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_psndoc").setEnabled(false);
              int i = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
              for(int j=0;j<i;j++){
            	  this.getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvbascode").setEnabled(false);
              }
     			
            }
            
            getBillUI().updateUI();
            
        }
       
        /***
         * 在通过订单生成提货通知单时将本次提货数量带入默认，以及相应的折扣
         * add by wb 2008-12-22 18:33:16 三期需求
         * @param i 行号
         * @throws Exception 
         */
        @SuppressWarnings("unchecked")
		public void setBcthamount(AggregatedValueObject vo) throws Exception{
        	LadingbillVO hvo = (LadingbillVO)vo.getParentVO();
        	LadingbillBVO[] bvos = (LadingbillBVO[])vo.getChildrenVO();
        	String pk_cubasdoc = hvo.getPk_cubasdoc();
        	UFDouble ze=new UFDouble(0);                	 	//本次应收金额
            UFDouble vjeze=new UFDouble(0);               	 	//货款总额
            UFDouble firstdiscze=new UFDouble(0);         		//一次折扣总额
            UFDouble seccountze=new UFDouble(0);          		//二次折扣总额
        	HashMap hm = getCustZK(pk_cubasdoc);				//客户一次折扣
            for(int i=0;i<bvos.length;i++){
        		LadingbillBVO bvo = bvos[i];
        		String pk_invbasdoc = bvo.getPk_invbasdoc();
        		UFDouble orderamount = bvo.getOrderamount()==null?new UFDouble(0):bvo.getOrderamount();		//订单数量
            	UFDouble ytamount = bvo.getYtamount()==null?new UFDouble(0):bvo.getYtamount();				//已提数量
            	UFDouble bcthamount = orderamount.sub(ytamount);											//本次提货数量(袋)
             	String pk_measdoc = bvo.getPk_measdoc();				//辅助单位
            	UFDouble amount = getMainUnitamount(pk_invbasdoc, pk_measdoc, bcthamount);					//本次提货数量(吨)
            	UFDouble fisdiscount = new UFDouble(hm.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm.get(pk_invbasdoc+pk_cubasdoc).toString()).multiply(amount);
            	UFDouble price = bvo.getZprice();
            	UFDouble bcysje = amount.multiply(price).sub(fisdiscount);
            	UFDouble zje = amount.multiply(price);
            	ze = ze.add(bcysje);							//本次应收金额
            	vjeze = vjeze.add(zje);							//货款金额
            	firstdiscze = firstdiscze.add(fisdiscount);		//一次折扣总额
            	//add by houcq 2011-05-25 begin
            	UFDouble seconddiscount = new UFDouble(bvo.getSeconddiscount()==null?"0":bvo.getSeconddiscount().toString()).multiply(amount);
            	UFDouble bjj =amount.multiply(price).sub(fisdiscount).sub(seconddiscount).div(bcthamount);
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bjj, i, "def_7");		//包净价
            	//end
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bcthamount, i, "ladingamount");		//袋重	
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(amount, i, "zamount");				//吨重
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(fisdiscount, i, "firstdiscount");	//一次折扣
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(zje, i, "vje");						//总金额
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bcysje, i, "bcysje");				//本次应收金额
        	}
            //将总金额及相关信息带到表头
            UFDouble lastzk = new UFDouble(hvo.getDef_7()==null?"0":hvo.getDef_7().toString());
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("bcyfje",ze);
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("seconddiscount",lastzk.sub(seccountze));                         // 二次折扣余额 = 二次折扣金额(上月折扣)-本次所用二次折扣
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("dkze",vjeze);
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_8",vjeze.multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscze));  // 理论二次折扣 (金额*40%-一次折扣总额) 
            
            //账户余额 
            UFDouble overage = new PubTools().getCustOverage(pk_cubasdoc,_getCorp().getPk_corp(),_getDate().toString());		//查找客户余额
        	getBillCardPanelWrapper().getBillCardPanel().setHeadItem("sxje",overage);
        }
        
        /***
         * 客户的一次折扣
         * @param pk_cubasdoc
         * @return
         */
        @SuppressWarnings("unchecked")
		public HashMap getCustZK(String pk_cubasdoc){
        	HashMap hm = new HashMap();
        	try {
                IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
                //取得该客户该物料的一次折扣金额
                StringBuffer sql=new StringBuffer("select b.pk_invbasdoc||b.pk_cubasdoc keys,isnull(b.newdiscount,0) value from eh_firstdiscount a ")
                .append(" ,eh_firstdiscount_b b where a.pk_firstdiscount=b.pk_firstdiscount and '"+_getDate()+"' between b.zxdate ") // 加上最新标记
                .append(" and b.yxdate and a.pk_corp='"+_getCorp().getPk_corp()+"' and b.pk_cubasdoc='"+pk_cubasdoc+"' and isnull(a.def_1,'N')='Y'   and isnull(a.lock_flag,'N')='N'  and isnull(b.lock_flag,'N')='N' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0  and vbillstatus="+IBillStatus.CHECKPASS+"");
                ArrayList arr=(ArrayList)iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
                if(!arr.isEmpty()){
                    for(int i=0;i<arr.size();i++){
                        HashMap hmA = (HashMap) arr.get(i);
                        hm.put(hmA.get("keys").toString(),hmA.get("value").toString());
                    }
                }
        	}catch (Exception ex) {
        		ex.printStackTrace();
			}
			return hm;
        }
        
        /***
         * 根据辅助单位数量换算出主计量单位数量 
         * @param pk_invbasdoc
         * @param pk_measdoc
         * @param fzamount
         * @return
         * @throws Exception
         */
        @SuppressWarnings("unchecked")
		public UFDouble getMainUnitamount(String pk_invbasdoc,String pk_measdoc,UFDouble fzamount) throws Exception{
        	String sql="select mainmeasrate changerate " +
        			" from bd_convert where " +
        			" pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') " +
        			" and pk_measdoc='"+pk_measdoc+"' and nvl(dr,0)=0";
        	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
        	UFDouble changerate=new UFDouble(-1000000);
        	for(int i=0;i<al.size();i++){
        		HashMap hm=(HashMap) al.get(i);
        		changerate=new UFDouble(hm.get("changerate")==null?"-10000":hm.get("changerate").toString());
        	}
        	UFDouble amount=fzamount.multiply(changerate);
        	return amount;
        }
        
        @SuppressWarnings({ "unchecked" })
		@Override
        public void onBoSave() throws Exception {
            getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
            //唯一性校验
            BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
            
            int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc","vsourcebillid"});
            if(res==1){
                getBillUI().showErrorMessage("物料有重复！");
                return;
            }
            /**本次提货量不能大于最大提货量 edit by wb at 2008-10-23 18:21:34*/
            LadingbillVO  ladVO = (LadingbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
            LadingbillBVO[] bvos = (LadingbillBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
			for(int i=0; i<bvos.length; i++){
				LadingbillBVO bvo = bvos[i];
				UFDouble ladingamount = bvo.getZamount()==null?new UFDouble(0): bvo.getZamount(); 								// 本次提货数量
				UFDouble maxthamount = bvo.getMaxthamount()==null?new UFDouble(0):bvo.getMaxthamount();                     	// 最大提货数量
				double subamount = ladingamount.sub(maxthamount).doubleValue();   	// 本次提货数量-最大提货数量
			    if(subamount>0){
			    	getBillUI().showErrorMessage("第"+(i+1)+"行提货数量超过最大提货数量,请确认本次提货数量!");
                    return;			    	
			    }
			   //add by houcq 2011-03-14 begin
			    if (bvo.getBcysje()==null || bvo.getBcysje().toDouble()==0)
			    {
			    	getBillUI().showErrorMessage("第"+(i+1)+"行本次应收金额不能为空和零!");
                    return;		
			    }
			  //add by houcq 2011-03-14 end
			  //add by houcq 2011-04-14 begin 增加判断如果总金额-一次折扣-二次折扣金额<>本次应收金额提示
			  UFDouble vje=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vje")==null?"0":
			    		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vje").toString());
			  UFDouble firstdiscount=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"firstdiscount")==null?"0":
		    		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"firstdiscount").toString());
			  UFDouble seconddiscount=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"seconddiscount")==null?"0":
		    		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"seconddiscount").toString());
			  UFDouble bcysje=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"bcysje")==null?"0":
		    		getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"bcysje").toString());
			  UFDouble temp =bcysje.sub(vje.sub(firstdiscount).sub(seconddiscount));
			  if (temp.toDouble()!=0)
			  {
				  getBillUI().showErrorMessage("第"+(i+1)+"行总金额-一次折扣-二次折扣不等于本次应收/退金额,请检查!");
                  return;	
			  }
			//add by houcq 2011-04-14 end
			}
			/**********************  end  *******************************/
			
			String vsourceBillType = ladVO.getVsourcebilltype() == null?"":ladVO.getVsourcebilltype();
			
    		AggregatedValueObject aggVO = getBillCardPanelWrapper().getBillVOFromUI();
    		LadingbillBVO[] bvs = (LadingbillBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
    		if(isEditing() && aggVO!=null && aggVO.getChildrenVO()!=null && aggVO.getParentVO().getPrimaryKey().length()>0){
            	LadingbillBVO[] bvoss = (LadingbillBVO[])aggVO.getChildrenVO();
            	IVOPersistence iVOPersistence =(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
            	iVOPersistence.updateVOArray(bvoss);
            	getBillUI().updateUI();
            }
    		
    		UFDouble zje = new UFDouble(0);			//总金额
    		for(int i=0;i<bvs.length;i++){
    			UFDouble bcysje = bvs[i].getBcysje();
    			zje = zje.add(bcysje);	
    		}
    		getBillCardPanelWrapper().getBillCardPanel().setHeadItem("bcyfje", zje);//将总金额
    		super.onBoSave();
   		    //add by houcq 2011-06-01 begin modify by 2011-06-09
    		LadingbillVO   vo =(LadingbillVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
            new PubTools().recordTime(vo.getVbilltype(), vo.getPk_ladingbill(), vo.getPk_corp(), vo.getCoperatorid());
            //add by houcq end
    		/**当单据被驳回并且不是本人提货在保存时提醒是否需要修改运输合同 add by wb at 2008-11-18 11:21:55**/
    		String pk_ladingbill = ladVO.getPk_ladingbill();
    		UFBoolean self_flag = ladVO.getSelf_flag();
    		if(!self_flag.booleanValue()&&pk_ladingbill!=null&&pk_ladingbill.length()>0){
    			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    			StringBuffer sql = new StringBuffer()
    			.append(" select * from pub_workflownote where pk_wf_task in ")
    			.append(" ( select pk_wf_task from pub_wf_task where pk_wf_instance in ")
    			.append(" ( select pk_wf_instance from pub_wf_instance where billid = '"+pk_ladingbill+"' ) and tasktype != 1 ")
    			.append(" )")
    			.append(" and ischeck = 'X' and approvestatus = 4 ");
    			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql.toString(),new MapListProcessor());
    			if(al!=null&&al.size()>0){
    				getBillUI().showWarningMessage("单据被驳回，请确认运输合同是否需要修改?");
    			}
    		}
            /************************** end *******************************/
    		
    		//把提货通知单保存的时候回写标记的功能写在一个事务里,分为2种情况(异常与非异常) add by zqy 2010-11-20 14:18:19
    		UpdateFlag(vsourceBillType);
    
    		super.onBoRefresh();
        }
        
        /**
         * 把提货通知单保存的时候回写标记的功能写在一个事务里,分为2种情况(异常与非异常) add by zqy 2010-11-20 14:18:19
         * @param vsourceBillType 单据类型
         */
        @SuppressWarnings({ "unchecked", "null" })
		private void UpdateFlag(String vsourceBillType) throws Exception {
			try {
				if(vsourceBillType.equals("ZA09")){
	    			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		            LadingbillBVO[] reBVO=(LadingbillBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		    		String [] pk_order_b=new String [reBVO.length];//存放上游子表的PK
		    		String [] pk_ladingbill_b =new String [reBVO.length];//存放自己子表的PK
		    		@SuppressWarnings("unused")
					String info="";
		    		for(int i=0;i<reBVO.length;i++){
		    			pk_order_b[i] = reBVO[i].getVsourcebillid()==null?"":reBVO[i].getVsourcebillid().toString(); //来源单据子表主键
		    			pk_ladingbill_b[i]=reBVO[i].getPk_ladingbill_b()==null?"":reBVO[i].getPk_ladingbill_b().toString();  //子表主键
		    		}
		    		String pksql2=PubTool.combinArrayToString(pk_ladingbill_b); 		//转换成('','')形式    
	                 
		    		 //自己库中的数量（上游主键PK的全部数量）
		    		HashMap hmde=new HashMap();//上游主键的PK和对应的以生产的数量和将要生产数量的和
		    		for(int i=0;i<reBVO.length;i++){
		    			String pk_in=reBVO[i].getVsourcebillrowid()==null?"":reBVO[i].getVsourcebillrowid().toString();//上游的主键的PK的集合
		    			UFDouble scmount=new UFDouble(reBVO[i].getLadingamount()==null?"0":reBVO[i].getLadingamount().toString());//界面上的生产数量
		    			if(!pk_in.equals("")){
		    				if(hmde.containsKey(pk_in)){
		    					UFDouble old=new UFDouble(hmde.get(pk_in)==null?"":hmde.get(pk_in).toString());
		    					UFDouble newmount=scmount.add(old);
		    					hmde.put(pk_in, newmount);
		    				}else{
		    					hmde.put(pk_in, scmount);
		    				}
		    			}
		    		}
		    		//找到对应的主表 pk的的数量的值
		    		Object [] keyset=(Object [])hmde.keySet().toArray();
		    		if(keyset!=null||keyset.length<=0){
		    			String [] pk_orde=new String [keyset.length];
		    			for(int i=0;i<keyset.length;i++){
		    				pk_orde[i]=(String) keyset[i];
		    			}
		    			String pk_or=PubTool.combinArrayToString(pk_orde);
		    			StringBuffer sql2 = new StringBuffer("")
		    			.append(" select pk_order pk,sum(isnull(amount,0)) amount,'A' flag  from eh_order_b")
		    			.append(" where pk_order in "+pk_or+" and  isnull(dr,0)=0 group by  pk_order ")
		    			.append(" union all ")
		    			.append("select vsourcebillrowid pk, sum(isnull(ladingamount,0)) amount ,")
		    			.append("'B' flag from eh_ladingbill_b where vsourcebillrowid in "+pk_or+" and pk_ladingbill_b not in "+pksql2+" ")
		    			.append(" and isnull(dr,0)=0 group by vsourcebillrowid ");
		    			
		    			
		    			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql2.toString(),new MapListProcessor());
		    			HashMap hmA=new HashMap();//界面上所有上游主键 对应的上游的主键下的所有物料
		    			HashMap hmB=new HashMap();
		    			for(int i=0;i<al.size();i++){
		    				HashMap hm=(HashMap) al.get(i);
		    				String pk_in=hm.get("pk")==null?"":hm.get("pk").toString();
		    				UFDouble amount=new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
		    				String flag=hm.get("flag")==null?"":hm.get("flag").toString();
		    				if(flag.equals("A")){
		    					hmA.put(pk_in, amount);	
		    				}
		    				if(flag.equals("B")){
		    					hmB.put(pk_in, amount);
		    				}
		    						
		    			}
		    			ArrayList alac=new ArrayList();
		    			for(int i=0;i<keyset.length;i++){
		    				String pk_order=keyset[i]==null?"":keyset[i].toString();
		    				//界面上的值
		    				UFDouble panel=new UFDouble(hmde.get(pk_order)==null?"0":hmde.get(pk_order).toString());
		    				//自己库中的值
		    				UFDouble Myamount=new UFDouble(hmB.get(pk_order)==null?"0":hmB.get(pk_order).toString());
		    				//上游库中的值
		    				UFDouble foramount=new UFDouble(hmA.get(pk_order)==null?"0":hmA.get(pk_order).toString());		//订单数量
		    				
		    				UFDouble add=Myamount.add(panel);			//提货数量
		    				UFDouble a=add.sub(foramount);
		    				/**当提货数量大于或等于订单数量时关闭上游订单 edit by wb at 2008-10-23 18:02:06*/
		    				if(a.toDouble()>=0){
		    					alac.add(pk_order);
		    					
		    				}			
		    			}
		    			String [] ac=(String[]) alac.toArray(new String [alac.size()]);
		    			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		    			if(keyset!=null||keyset.length<=0){
		    				String sqlupdate="update eh_order set th_flag='N' where pk_order in"+pk_or;
		    				pubitf.updateSQL(sqlupdate);
		    			}
		    			if(ac!=null){
		    				String df=PubTool.combinArrayToString(ac);
		    				String sqlupdate="update eh_order set th_flag='Y' where pk_order in"+df;
		    				pubitf.updateSQL(sqlupdate);
		    			}
		    		}
		    		
		    		//当销售订单中的提货通知单的关闭标记和销售订单中生产任务单的关闭标记二者都关闭时，该张销售订单单据则关闭(lock_flag) add by zqy 2008-10-6 9:27:16
		            LadingbillBVO[] lbvo = (LadingbillBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		            int length = lbvo.length;
		            ArrayList list = new ArrayList();//存放销售订单子表Vsourcebillrowid即为主表的PK
		            for(int i=0;i<length;i++){
		                String vsourcebillrowid = lbvo[i].getVsourcebillrowid()==null?"":lbvo[i].getVsourcebillrowid().toString();//销售订单PK
		                list.add(vsourcebillrowid);
		            }
		            StringBuffer alsql = new StringBuffer();
		            for (int i = 0; i < list.size(); i++) {
		                alsql.append("'");
		                alsql.append(list.get(i));
		                alsql.append("'");
		                if ((i + 1) < list.size()) {
		                    alsql.append(",");
		                } else {
		                    alsql.append("");
		                }
		            }          
		            //根据来源单据的PK查找相应的数据，如果scrw_flag与th_flag都打上标记时，则该销售订单的关闭按钮则关闭
		            StringBuffer sql = new StringBuffer()
		            .append(" select scrw_flag,th_flag,pk_order from eh_order where pk_order in ("+alsql+") and isnull(dr,0)=0 ");
		            ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		            if(arr!=null && arr.size()>0){
		                for(int j=0;j<arr.size();j++){
		                    HashMap hm = (HashMap) arr.get(j);
		                    String scrw_flag = hm.get("scrw_flag")==null?"":hm.get("scrw_flag").toString();//生产任务单的关闭标记
		                    String th_flag = hm.get("th_flag")==null?"":hm.get("th_flag").toString();//提货通知单的关闭标记
		                    String pk_order = hm.get("pk_order")==null?"":hm.get("pk_order").toString();//销售订单的PK
		                    if(("Y".equals(scrw_flag)) && ("Y".equals(th_flag))){
		                        String updatesql = " update eh_order set lock_flag='Y' where pk_order='"+pk_order+"' ";
		                        PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		                        pubitf.updateSQL(updatesql);
		                    }
		                }
		            }
	    	}else if(vsourceBillType.equals("ZA14")){
	    		int row = this.getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
	    		UFDouble za = new UFDouble(0);
	    		for(int i = 0;i<row;i++){
	    			
	    			//辅助计量值，退还总量
	    			UFDouble orderamount=  new UFDouble(this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "orderamount")==null?"0":this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "orderamount").toString());
	    			//辅助计量值，已提数量
	    			UFDouble ytamount=  new UFDouble(this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ytamount")==null?"0":this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ytamount").toString());
	    			//辅助计量值，本次提货数量
	    			UFDouble ladingamount=  new UFDouble(this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ladingamount")==null?"0":this.getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ladingamount").toString());
	    			
	    			if(ladingamount.abs().compareTo(orderamount.sub(ytamount.abs()))>0){
	    				this.getBillUI().showErrorMessage("第"+(i+1)+"行提货数量超出可提数量,请重新输入!");
	    				return;
	    			}
	    		}
	    	
	    		PubItf  pubitf=(PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
	            AggregatedValueObject aggvo=getBillCardPanelWrapper().getBillVOFromUI();
	            LadingbillBVO[] ibvos=(LadingbillBVO[])aggvo.getChildrenVO();
	    	       String pk_invoce  = null;
	    	        for(int i=0;i<ibvos.length;i++){
	    	        	LadingbillBVO ibvo=(ibvos[i]);
	    	        	pk_invoce = ibvo.getVsourcebillrowid();
	    	        	za = za.add(ibvo.getZamount());
	    	        }
	    	        //将发票上的数量和退货单上的数量拿出来比较
	    	        StringBuffer sql1=new StringBuffer("")
	    	        .append(" select pk_invoice pk,sum(nvl(amount,0)) amount,'A' flag from eh_invoice_b where ")
	    	        .append("  pk_invoice = '"+pk_invoce+"' and nvl(dr,0)=0 group by pk_invoice ")
	    	        .append("  union select vsourcebillrowid pk,abs(sum(nvl(zamount,0))) amount,'B' flag from eh_ladingbill_b where ")
	    	        .append("  vsourcebillrowid = '"+pk_invoce+"' and nvl(dr,0)=0 group by vsourcebillrowid ");
	    	        
	    	        IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
	    	        ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(sql1.toString(),new MapListProcessor());
	    	        if(arr!=null&&arr.size()>0){
	    				HashMap hma = new HashMap();
	    				for(int i=0; i<arr.size(); i++){
	    					HashMap hm=(HashMap)arr.get(i);
	    					String flag = hm.get("flag")==null?"":hm.get("flag").toString();
	    					UFDouble amount = new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
	    					hma.put(flag, amount);
	    				}
	    				String updateSQL = null;
	    				//发票总数量
	    				UFDouble voiceA = new UFDouble(hma.get("A")==null?"0":hma.get("A").toString());
	    				//提单总数量
	    				UFDouble ladingB = new UFDouble(hma.get("B")==null?"0":hma.get("B").toString());
	    				//全部退回时将销售发票关闭
	    				//voiceA.compareTo(ladingB.add(za.abs())改为voiceA.compareTo(ladingB) modify by houcq 2010-12-10
	    				if(voiceA.compareTo(ladingB) ==0){
	    					updateSQL = "update eh_invoice set th_flag = 'Y' where pk_invoice = '"+pk_invoce+"'";
	    				}
	    				else{
	    					updateSQL = "update eh_invoice set th_flag = 'N' where pk_invoice = '"+pk_invoce+"'";
	    				}
	    				pubitf.updateSQL(updateSQL);
	    			}
	    	}
				
			} catch (Exception e) {
				if(vsourceBillType.equals("ZA09")){
	    			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		            LadingbillBVO[] reBVO=(LadingbillBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		    		String [] pk_order_b=new String [reBVO.length];//存放上游子表的PK
		    		String [] pk_ladingbill_b =new String [reBVO.length];//存放自己子表的PK
		    		for(int i=0;i<reBVO.length;i++){
		    			pk_order_b[i] = reBVO[i].getVsourcebillid()==null?"":reBVO[i].getVsourcebillid().toString(); //来源单据子表主键
		    			pk_ladingbill_b[i]=reBVO[i].getPk_ladingbill_b()==null?"":reBVO[i].getPk_ladingbill_b().toString();  //子表主键
		    		}
		    		String pksql2=PubTool.combinArrayToString(pk_ladingbill_b); 		//转换成('','')形式    
	                 
		    		 //自己库中的数量（上游主键PK的全部数量）
		    		HashMap hmde=new HashMap();//上游主键的PK和对应的以生产的数量和将要生产数量的和
		    		for(int i=0;i<reBVO.length;i++){
		    			String pk_in=reBVO[i].getVsourcebillrowid()==null?"":reBVO[i].getVsourcebillrowid().toString();//上游的主键的PK的集合
		    			UFDouble scmount=new UFDouble(reBVO[i].getLadingamount()==null?"0":reBVO[i].getLadingamount().toString());//界面上的生产数量
		    			if(!pk_in.equals("")){
		    				if(hmde.containsKey(pk_in)){
		    					UFDouble old=new UFDouble(hmde.get(pk_in)==null?"":hmde.get(pk_in).toString());
		    					UFDouble newmount=scmount.add(old);
		    					hmde.put(pk_in, newmount);
		    				}else{
		    					hmde.put(pk_in, scmount);
		    				}
		    			}
		    		}
		    		//找到对应的主表 pk的的数量的值
		    		Object [] keyset=(Object [])hmde.keySet().toArray();
		    		if(keyset!=null||keyset.length<=0){
		    			String [] pk_orde=new String [keyset.length];
		    			for(int i=0;i<keyset.length;i++){
		    				pk_orde[i]=(String) keyset[i];
		    			}
		    			String pk_or=PubTool.combinArrayToString(pk_orde);
		    			StringBuffer sql2 = new StringBuffer("")
		    			.append(" select pk_order pk,sum(isnull(amount,0)) amount,'A' flag  from eh_order_b")
		    			.append(" where pk_order in "+pk_or+" and  isnull(dr,0)=0 group by  pk_order ")
		    			.append(" union all ")
		    			.append("select vsourcebillrowid pk, sum(isnull(ladingamount,0)) amount ,")
		    			.append("'B' flag from eh_ladingbill_b where vsourcebillrowid in "+pk_or+" and pk_ladingbill_b not in "+pksql2+" ")
		    			.append(" and isnull(dr,0)=0 group by vsourcebillrowid ");
		    			
		    			
		    			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql2.toString(),new MapListProcessor());
		    			HashMap hmA=new HashMap();//界面上所有上游主键 对应的上游的主键下的所有物料
		    			HashMap hmB=new HashMap();
		    			for(int i=0;i<al.size();i++){
		    				HashMap hm=(HashMap) al.get(i);
		    				String pk_in=hm.get("pk")==null?"":hm.get("pk").toString();
		    				UFDouble amount=new UFDouble(hm.get("amount")==null?"0":hm.get("amount").toString());
		    				String flag=hm.get("flag")==null?"":hm.get("flag").toString();
		    				if(flag.equals("A")){
		    					hmA.put(pk_in, amount);	
		    				}
		    				if(flag.equals("B")){
		    					hmB.put(pk_in, amount);
		    				}
		    						
		    			}
		    			ArrayList alac=new ArrayList();
		    			for(int i=0;i<keyset.length;i++){
		    				String pk_order=keyset[i]==null?"":keyset[i].toString();
		    				//界面上的值
		    				UFDouble panel=new UFDouble(hmde.get(pk_order)==null?"0":hmde.get(pk_order).toString());
		    				//自己库中的值
		    				UFDouble Myamount=new UFDouble(hmB.get(pk_order)==null?"0":hmB.get(pk_order).toString());
		    				//上游库中的值
		    				UFDouble foramount=new UFDouble(hmA.get(pk_order)==null?"0":hmA.get(pk_order).toString());		//订单数量
		    				
		    				UFDouble add=Myamount.add(panel);			//提货数量
		    				UFDouble a=add.sub(foramount);
		    				/**当提货数量大于或等于订单数量时关闭上游订单 edit by wb at 2008-10-23 18:02:06*/
		    				if(a.toDouble()>=0){
		    					alac.add(pk_order);
		    					
		    				}			
		    			}
		    			String [] ac=(String[]) alac.toArray(new String [alac.size()]);
		    			PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		    			if(keyset!=null||keyset.length<=0){
		    				String sqlupdate="update eh_order set th_flag='N' where pk_order in"+pk_or;
		    				pubitf.updateSQL(sqlupdate);
		    			}
		    			if(ac!=null){
		    				String df=PubTool.combinArrayToString(ac);
		    				String sqlupdate="update eh_order set th_flag='N' where pk_order in"+df;
		    				pubitf.updateSQL(sqlupdate);
		    			}
		    		}
		    		
		            LadingbillBVO[] lbvo = (LadingbillBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
		            int length = lbvo.length;
		            ArrayList list = new ArrayList();//存放销售订单子表Vsourcebillrowid即为主表的PK
		            for(int i=0;i<length;i++){
		                String vsourcebillrowid = lbvo[i].getVsourcebillrowid()==null?"":lbvo[i].getVsourcebillrowid().toString();//销售订单PK
		                list.add(vsourcebillrowid);
		            }
		            StringBuffer alsql = new StringBuffer();
		            for (int i = 0; i < list.size(); i++) {
		                alsql.append("'");
		                alsql.append(list.get(i));
		                alsql.append("'");
		                if ((i + 1) < list.size()) {
		                    alsql.append(",");
		                } else {
		                    alsql.append("");
		                }
		            }          
		            //根据来源单据的PK查找相应的数据，如果scrw_flag与th_flag都打上标记时，则该销售订单的关闭按钮则关闭
		            StringBuffer sql = new StringBuffer()
		            .append(" select scrw_flag,th_flag,pk_order from eh_order where pk_order in ("+alsql+") and isnull(dr,0)=0 ");
		            ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		            if(arr!=null && arr.size()>0){
		                for(int j=0;j<arr.size();j++){
		                    HashMap hm = (HashMap) arr.get(j);
		                    String pk_order = hm.get("pk_order")==null?"":hm.get("pk_order").toString();//销售订单的PK
		                    String updatesql = " update eh_order set lock_flag='N' where pk_order='"+pk_order+"' ";
		                    PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
		                    pubitf.updateSQL(updatesql);
		                }
		            }
	    	}else if(vsourceBillType.equals("ZA14")){
	    		PubItf pubitf=(PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
	            AggregatedValueObject aggvo=getBillCardPanelWrapper().getBillVOFromUI();
	            LadingbillBVO[] ibvos=(LadingbillBVO[])aggvo.getChildrenVO();
	    	       String pk_invoce  = null;
	    	        for(int i=0;i<ibvos.length;i++){
	    	        	LadingbillBVO ibvo=(ibvos[i]);
	    	        	pk_invoce = ibvo.getVsourcebillrowid();
	    	        	String updateSQL = "update eh_invoice set th_flag = 'N' where pk_invoice = '"+pk_invoce+"'";
	    	        	pubitf.updateSQL(updateSQL);
	    	        }
	    	}
				
				e.printStackTrace();
				throw new Exception(e.getMessage());
			}
			
		}

		protected void onBoTEMPLETDISCOUNT() throws Exception{
            int row = getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            for(int i=0;i<row;i++){
                double ladingamount=getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ladingamount")==null?0:
                   Double.parseDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i, "ladingamount").toString());
                if(ladingamount==0){
                    getBillUI().showErrorMessage("请在("+(i+1)+")输入提货数量！");
                    return;
                }
            }
            TempletDialog dlg=new TempletDialog("TEST");
            AggregatedValueObject vos=getBillCardPanelWrapper().getBillVOFromUI();
            dlg.setVOs(vos);                                                      //将界面上的vo放入dialog中去
            dlg.showModal();
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_6",null);
            AggregatedValueObject vo=dlg.getVOs();                                //取得加入临时折扣后的vo
            //将含有临时折扣的vo进行计算后放入表体
            LadingbillBVO[] bodyvos=(LadingbillBVO[])vo.getChildrenVO();
            BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
            UFDouble lsdiscountze = new UFDouble(0);        //临时折扣额
            for(int i=0;i<bodyvos.length;i++){
            	if(bm.getRowState(i)==BillModel.ADD)
                    bm.setRowState(i,BillModel.ADD);
                else
                    bm.setRowState(i,BillModel.MODIFICATION);
            	LadingbillBVO bodyvo =bodyvos[i];
                double lsdiscount=bodyvo.getLsdiscount()==null?0:bodyvo.getLsdiscount().doubleValue();
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(0, i, "lsyydiscount");
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(lsdiscount, i, "lssydiscount");
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(lsdiscount, i, "lsdiscount");
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(lsdiscount, i, "lssydiscount");
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(lsdiscount, i, "lsyydiscount");
                lsdiscountze=lsdiscountze.add(lsdiscount);
            }
            UFDouble cusdiscount = new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_9").getValueObject()==null?"0":
            		getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_9").getValueObject().toString());  //二次折扣金额
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_7",cusdiscount.add(lsdiscountze));                    // 增加客户的二次折扣金额
            UFDouble secye = cusdiscount.add(lsdiscountze);
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("seconddiscount",secye);
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("tempdiscount",lsdiscountze);
            getBillUI().updateUI();
            
        }
        @Override
        protected void onBoLineAdd() throws Exception {
            // TODO Auto-generated method stub
            super.onBoLineAdd();
          //add by houcq 2011-02-25 begin
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_6", null);
            int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            for (int i=0;i<rows;i++)
            {
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(null, i, "seconddiscount");
            }
          //add by houcq 2011-02-25 end
            int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
            getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(row,"vinvbascode", true);
//            getBillCardPanelWrapper().getBillCardPanel().getBillModel().removeRowStateChangeEventListener()
            getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), row, "pk_corp");
            getBillCardPanelWrapper().getBillCardPanel().setFocusable(true);
            getBillCardPanelWrapper().getBillCardPanel().setFocusTraversalKeysEnabled(true);
//            getBillCardPanelWrapper().getBillCardPanel().get
//            getBillCardPanelWrapper().getBillCardPanel().getComponent(0).mouseExit(evt, x, y)
            
        }
        @Override
        protected void onBoLineIns() throws Exception {
            // TODO Auto-generated method stub
            super.onBoLineIns();
            int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
            getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(row,"vinvbascode", true);
        }
        @Override
        protected void onBoLinePaste() throws Exception {
            // TODO Auto-generated method stub
            super.onBoLinePaste();
            int row=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getSelectedRow();
            getBillCardPanelWrapper().getBillCardPanel().getBillModel().setCellEditable(row,"vinvbascode", true);
        }
        
        @Override
        protected void onBoLineDel() throws Exception {
        	super.onBoLineDel();
        	//表体中的本次应收金额合计后写入表头中的应收款总额
            int rows=getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
            UFDouble ze=new UFDouble(0);                 //本次应收金额
            UFDouble vjeze=new UFDouble(0);                //货款总额
            UFDouble firstdiscze=new UFDouble(0);          //一次折扣总额
            UFDouble seccountze=new UFDouble(0);           //二次折扣总额
            UFDouble lsdisze=new UFDouble(0);              //临时折扣总额
            for(int i=0;i<rows;i++){
            	UFDouble scount= new UFDouble(0);
                seccountze=seccountze.add(scount);
            	getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(scount,i,"seconddiscount");
            	getBillUI().updateUI();
                
                UFDouble firstdis=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"firstdiscount")==null?"0":
                	getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"firstdiscount").toString());
                firstdiscze = firstdiscze.add(firstdis);
                UFDouble lsdis=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"lsdiscount")==null?"0":
                	getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"lsdiscount").toString());
                lsdisze=lsdisze.add(lsdis);
                UFDouble vje=new UFDouble(getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vje")==null?"0":
                	getBillCardPanelWrapper().getBillCardPanel().getBodyValueAt(i,"vje").toString());						//货款总额
                vjeze=vjeze.add(vje);
                //下面一行代码由houcq修改2010-11-03，原代码为UFDouble bcysje= vje.sub(firstdiscze).sub(scount);
                UFDouble bcysje= vje.sub(firstdis).sub(scount);															//本次应收金额
                ze=ze.add(bcysje);
                getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(bcysje, i, "bcysje");
            }
            try {
				LadingbillVO ladVO = (LadingbillVO)getBillCardPanelWrapper().getChangedVOFromUI().getParentVO();
			
	            UFDouble lastzk = new UFDouble(ladVO.getDef_7()==null?"0":ladVO.getDef_7().toString());
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("bcyfje",ze);
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("seconddiscount",lastzk.sub(seccountze));                         // 二次折扣余额 = 二次折扣金额(上月折扣)-本次所用二次折扣
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("dkze",vjeze);
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("tempdiscount",lsdisze);
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_6",null);
	            //	            UFDouble def_8 = vjeze.multiply(IPubInterface.DISCOUNTRATE);                         
	            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("def_8",vjeze.multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscze));  // 理论二次折扣 (金额*40%-一次折扣总额) edit by wb at 2008-7-10 12:25:07
            } catch (Exception e1) {
				e1.printStackTrace();
			}
        }
        
        /***
         * 判断当前提货量是否超过最高日开票量标准
         * wb 2008-12-23 11:09:00
         * @return
         * @throws Exception 
         */
        @SuppressWarnings("unchecked")
		public UFDouble  isMaxthamount() throws Exception{
        	LadingbillVO hvo = (LadingbillVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        	@SuppressWarnings("unused")
			String pk_ladingbill = hvo.getPk_ladingbill();		//主键
        	UFDate ladingdate = hvo.getLadingdate();			//提货日期
        	@SuppressWarnings("unused")
			String isMax = null;
        	HashMap hmbz = new HashMap();					//最高日开票量标准
			StringBuffer gzdj = new StringBuffer()
			.append(" select beginamount,endamount,maxamount")
			.append(" from eh_trade_salestand")
			.append(" where isnull(dr,0)=0")
			.append(" order by beginamount");
			UFDouble beginamount = new UFDouble(0);
			UFDouble endamount = new UFDouble(0);
			UFDouble bzamount = new UFDouble(0);
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			ArrayList gzdjarr = (ArrayList) iUAPQueryBS.executeQuery(gzdj.toString(), new MapListProcessor());
			if(gzdjarr!=null&&gzdjarr.size()>0){
				for(int i=0;i<gzdjarr.size();i++){
					HashMap hm = (HashMap)gzdjarr.get(i);
					beginamount = new UFDouble(hm.get("beginamount")==null?"0":hm.get("beginamount").toString());			//起始值
					endamount = new UFDouble(hm.get("endamount")==null?"0":hm.get("endamount").toString());					//结束值
					bzamount = new UFDouble(hm.get("maxamount")==null?"0":hm.get("maxamount").toString());					//开票量
					hmbz.put(beginamount+"-"+endamount, bzamount);
				}
			}
			
			//当日开票量汇总
			StringBuffer ladSQL = new StringBuffer()
			.append(" select sum(isnull(b.zamount,0)) amount")
			.append(" from eh_ladingbill a,eh_ladingbill_b b")
			.append(" where a.pk_ladingbill = b.pk_ladingbill")
			.append(" and ( a.ladingdate = '"+ladingdate+"' )")
			.append(" and a.pk_corp = '"+_getCorp().getPk_corp()+"' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0");
			Object amountobj = iUAPQueryBS.executeQuery(ladSQL.toString(), new ColumnProcessor());
        	UFDouble ladingamount = new UFDouble(amountobj==null?"0":amountobj.toString());
        	
        	//本月销售任务单总销量 
        	StringBuffer planSQL = new StringBuffer()
        	.append(" select sum(isnull(b.amount,0)) planamount")
        	.append(" from eh_trade_plan a,eh_trade_plan_b b")
        	.append(" where a.pk_plan = b.pk_plan")
        	.append(" and a.nyear = "+ladingdate.getYear()+" and a.nmonth = "+ladingdate.getMonth()+"")
        	.append(" and a.pk_corp = '"+_getCorp().getPk_corp()+"' and a.vbillstatus = 1 ")
        	.append(" and isnull(a.dr,0)=0 ")
        	.append(" and isnull(b.dr,0)=0");
        	Object plamamountobj = iUAPQueryBS.executeQuery(planSQL.toString(), new ColumnProcessor());
        	UFDouble planamount = new UFDouble(plamamountobj==null?"0":plamamountobj.toString());
        	
        	
        	//得到销售任务单总销量对应的最高开票量标准
        	UFDouble maxamount = new UFDouble(0);		//最高日开票量
        	String key = null;
            Iterator iter = hmbz.keySet().iterator();
            while(iter.hasNext()){ 					
                Object o = iter.next();
                key =o.toString();
                String[] amounts = key.split("-");
                beginamount = new UFDouble(amounts[0]);
                endamount = new UFDouble(amounts[1]);
                bzamount = new UFDouble(hmbz.get(key).toString());
                if(planamount.sub(beginamount).toDouble()>0&&endamount.sub(planamount).toDouble()>=0){		
                	maxamount = bzamount;
                	break;
                }
            }
            UFDouble subamount = ladingamount.sub(maxamount);
			return subamount;
        }
        
      //add by houcq 2010-09-27f覆盖方法， 更新打印次数
    	@Override
    	protected void onBoPrint() throws Exception {
     	   	int num=0;
        	String billno = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("billno").getValueObject().toString();
        	String old = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("def_3").getValueObject().toString();
        	if (null==old||"".equals(old))
        	{
        		old="-1";
        	}
        	else 
        	{    		
            	try {
                	num= Integer.valueOf(old).intValue()+1;
            	} catch (Exception e) {
            		getBillUI().showErrorMessage("def_3字段的值不能转换为数字");
            		return;
            	}
    		}
        	
        	PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName()); 
        	String sql = " update eh_ladingbill set def_3='"+num+"' where pk_corp='"+ClientEnvironment.getInstance().getCorporation().getPk_corp()+"' and billno='"+billno+"'";
        	pubitf.updateSQL(sql);
        	onBoRefresh();
        	super.onBoPrint();
    	}    
    	//取提货通知单中的折扣（订单折扣【正数】和发票折扣【负数】）  
    	public String getFlow(String billId){
    		
    		StringBuffer strBuf = new StringBuffer()
    		.append(" SELECT a.billno, a.pk_cubasdoc, b.pk_invbasdoc, SUM(NVL(b.seconddiscount,0)) discount ")
    		.append(" FROM eh_ladingbill a,eh_ladingbill_b b ")
    		.append(" WHERE a.pk_ladingbill = b.pk_ladingbill AND a.pk_ladingbill = '"+billId+"' AND NVL(b.dr,0)=0  ")
    		.append(" AND NVL(b.seconddiscount,0)<>0 ")
    		.append(" GROUP BY a.billno,a.pk_cubasdoc,b.pk_invbasdoc ");
    		return strBuf.toString();
    		
    	}
}

