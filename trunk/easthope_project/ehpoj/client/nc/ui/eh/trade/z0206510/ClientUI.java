/*
 * 创建日期 2006-6-7
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */
package nc.ui.eh.trade.z0206510;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.businessref.YsContractRefModel;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IPubInterface;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.InvdocByCusdocRefModel;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.trade.z0206510.LadingbillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ct.OperationState;
import nc.vo.trade.pub.IBillStatus;

/**
 * 功能 提货通知单
 * @author 洪海
 * 2008-04-08
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI {

	public static String nowdate = null;          // 客商
    public String pk_measdoc = null; // 单位主键
    public static String pk_cubasdoc = null;          // 客商
    UIRefPane ref=null;
	
    public ClientUI() {
        super();
        nowdate = _getDate().toString();
        ref=(UIRefPane) getBillCardPanel().getHeadItem("pk_yscontracts").getComponent();
		ref.setMultiSelectedEnabled(true);
		ref.setProcessFocusLost(false);
		ref.setButtonFireEvent(true);
		ref.setTreeGridNodeMultiSelected(true);
        // TODO 自动生成构造函数存根
    }

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
        // TODO 自动生成构造函数存根
    }


    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }
    protected AbstractManageController createController() {
        return new ClientCtrl();
    }
    public ManageEventHandler createEventHandler() {
        return new ClientEventHandler(this,this.getUIControl());
    }  
    @Override
    public boolean beforeEdit(BillEditEvent e) {
    	String strKey=e.getKey();
    	if (e.getPos()==BODY){
            if("vinvbascode".equalsIgnoreCase(strKey)){
            	pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                    getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
            	InvdocByCusdocRefModel.pk_cubasdoc = pk_cubasdoc;  //将客商传到参照中
            	if(pk_cubasdoc==null||pk_cubasdoc.length()<=0){
            		showErrorMessage("客户不能为空,请先选择客户!");
            	}
            }
    	   if(strKey.equals("dw")){
			int row=getBillCardPanel().getBillTable().getSelectedRow();
			pk_measdoc = getBillCardPanel().getBodyValueAt(row,"pk_measdoc")==null?"":
                        getBillCardPanel().getBodyValueAt(row,"pk_measdoc").toString();            //单位
		  }
    	}
    	return super.beforeEdit(e);
    }
    @SuppressWarnings("unchecked")
	@Override
    public void afterEdit(BillEditEvent e) {
        // TODO Auto-generated method stub
        //super.afterEdit(arg0);
        String strKey=e.getKey();
        //2008年9月27日13:27:42 by wm 选着物料的时候清空原有的数据
        if(e.getPos()==BODY){
        	if(strKey.equals("vinvbascode")){
        		getBillCardPanel().setBodyValueAt("", e.getRow(), "ladingamount"); //本次提货量 的清空
        		getBillCardPanel().setBodyValueAt("", e.getRow(), "zamount"); //吨重 的清空
        		getBillCardPanel().setBodyValueAt("", e.getRow(), "dw"); //辅助单位 的清空
        		getBillCardPanel().setBodyValueAt("", e.getRow(), "pk_measdoc"); //辅助单位 的清空
        	}
        	
        }
        // end 2008年9月27日13:29:09
        if(e.getPos()==HEAD){
            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//获取编辑公式
            getBillCardPanel().execHeadFormulas(formual);
        }else if (e.getPos()==BODY){
            String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//获取编辑公式
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
        }else{
            getBillCardPanel().execTailEditFormulas();
        }
        if("pk_yscontracts".equalsIgnoreCase(strKey)){				//选择运输合同后带出单号 add by wb at 2008-10-14 10:21:00
        	String[] billnos = ref.getRefNames();
        	String billno = PubTools.combinArrayToString3(billnos);
          	getBillCardPanel().setHeadItem("ysbillnos", billno);
          }
        
        //选择客户时对折扣的处理
        if(strKey.equals("pk_cubasdoc")){
            String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
            ArrayList arr=new nc.ui.eh.trade.z0206005.ClientUI().getDiscount(pk_cubasdoc,null);
            @SuppressWarnings("unused")
			HashMap hm=(HashMap)arr.get(0);
            @SuppressWarnings("unused")
			HashMap hm2=(HashMap)arr.get(1);       
            HashMap hm3=(HashMap)arr.get(2);    
            int rows=getBillCardPanel().getBillTable().getRowCount();
            YsContractRefModel.pk_cubasdoc = pk_cubasdoc;  //将客商传到参照中   add by wb at 2008-10-14 10:18:52
//            UFDouble ze=new UFDouble(0);
//            UFDouble seccountze=new UFDouble(0);          //二次折扣总额
//            for(int i=0;i<rows;i++){
//                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
//                String pk_invbasdoc=getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc")==null?"":
//                                        getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc").toString();
//                UFDouble amount=new UFDouble(getBillCardPanel().getBodyValueAt(i,"ladingamount")==null?"0":
//                   getBillCardPanel().getBodyValueAt(i,"ladingamount").toString());
//                UFDouble firstdiscount=new UFDouble(hm.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm.get(pk_invbasdoc+pk_cubasdoc).toString());
//                getBillCardPanel().setBodyValueAt(amount.multiply(firstdiscount), i, "firstdiscount");
//                UFDouble price=new UFDouble(getBillCardPanel().getBodyValueAt(i,"price")==null?"0":
//                    getBillCardPanel().getBodyValueAt(i,"price").toString());                                      //得到牌价
////                UFDouble secondcount=price.multiply(amount).multiply(IPubInterface.DISCOUNTRATE).sub(amount.multiply(firstdiscount));    
//                UFDouble seccount=new UFDouble(hm2.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm2.get(pk_invbasdoc+pk_cubasdoc).toString());
//                //如果计算所得折扣小于折扣表上的可用折扣则以折扣表上的二次折扣为折扣额
////                if(seccount.compareTo(secondcount)<0){
////                    secondcount=seccount;
////                }//计算得到二次折扣
//               
//                getBillCardPanel().setBodyValueAt(seccount, i, "seconddiscount");   
//                String[] formual=getBillCardPanel().getBodyItem("ladingamount").getEditFormulas();//获取编辑公式
//                getBillCardPanel().execBodyFormulas(i,formual);
//               
//                
//                
//                UFDouble bcysje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"bcysje")==null?"0":
//                    getBillCardPanel().getBodyValueAt(i,"bcysje").toString());
//                ze=ze.add(bcysje);
//                UFDouble scount=new UFDouble(getBillCardPanel().getBodyValueAt(i,"seconddiscount")==null?"0":
//                    getBillCardPanel().getBodyValueAt(i,"seconddiscount").toString());
//                seccountze=seccountze.add(scount);
////                hm2.put(pk_invbasdoc+pk_cubasdoc, seccount.sub(secondcount));
//                hm2.put(pk_invbasdoc+pk_cubasdoc, seccount);
//            }
            //当选择客户时将客户可用二次折扣余额写入表头二次折扣金额
            UFDouble secondamount=new UFDouble(hm3.get(pk_cubasdoc)==null?"0":hm3.get(pk_cubasdoc).toString());
            getBillCardPanel().setHeadItem("def_7",secondamount);
            getBillCardPanel().setHeadItem("def_9",secondamount);
            getBillCardPanel().setHeadItem("def_8",null);
            getBillCardPanel().setHeadItem("def_6",null);
            getBillCardPanel().setHeadItem("dkze",null);
            getBillCardPanel().setHeadItem("tempdiscount",null);
            getBillCardPanel().setHeadItem("bcyfje",null);
            getBillCardPanel().setHeadItem("seconddiscount",null);
            //客商营销代表 余额
//            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
//            StringBuffer sql = new StringBuffer()
//            .append(" select pk_psndoc from eh_custyxdb ")
//            .append(" where pk_cubasdoc ='"+pk_cubasdoc+"' and ismain ='Y' and isnull(dr,0)=0 ");
//            StringBuffer yesql = new StringBuffer()         
//            .append(" select overage from eh_custoverage ")
//            .append(" where pk_cubasdoc ='"+pk_cubasdoc+"' and isnull(dr,0)=0 ");
            try {
            	PubTools tools = new PubTools();
                UFDouble overage = tools.getCustOverage(pk_cubasdoc,_getCorp().getPk_corp(),_getDate().toString());		//查找客户余额
                
                String pk_psndoc = tools.getPk_custpsndoc(pk_cubasdoc, _getCorp().getPk_corp());	//客商代表  edit by wb 2009-11-4 16:19:49
//            	Object pk_psnobj = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
//            	Object overageobj = iUAPQueryBS.executeQuery(yesql.toString(), new ColumnProcessor());
//            	UFDouble overage = new UFDouble(overageobj==null?"0":overageobj.toString());
//            	String pk_psndoc = pk_psnobj==null?"":pk_psnobj.toString();
            	getBillCardWrapper().getBillCardPanel().setHeadItem("sxje",overage);
            	getBillCardWrapper().getBillCardPanel().setHeadItem("pk_psndoc", pk_psndoc);
            	//add by houcq 2011-05-04 begin 根据营销代表带出部门
            	IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            	if (pk_psndoc!=null)
            	{
            		String sql ="select pk_deptdoc from bd_psndoc where pk_psndoc='"+pk_psndoc+"'";
                	Object obj = iUAPQueryBS.executeQuery(sql.toString(), new ColumnProcessor());
                	getBillCardWrapper().getBillCardPanel().setHeadItem("def_2",obj);
            	}
            	//add by houcq 2011-05-04 end
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//再选择客户时清空表体  add by wb at 2008-5-19 15:38:59
            int[] rowcount=new int[rows];
            for(int i=rows - 1;i>=0;i--){
            	rowcount[i]=i;
            }
            getBillCardPanel().getBillModel().delLine(rowcount);
            this.updateUI();
        }
        
        if(strKey.equals("vinvbascode")){
            int row=getBillCardPanel().getBillTable().getSelectedRow();
            String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
            					getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();
            //取出物料的所有库存
			try {
				//HashMap hmkc = new PubTools().getDateinvKC(null, pk_invbasdoc, _getDate(), "1", _getCorp().getPk_corp());
				//UFDouble amountkc = new UFDouble(hmkc.get(pk_invbasdoc)==null?"":hmkc.get(pk_invbasdoc).toString());
				//modify by houcq 2011-06-20修改取库存方法
				UFDouble amountkc = new PubTools().getInvKcAmount(_getCorp().getPk_corp(),_getDate(),pk_invbasdoc);
	            getBillCardPanel().setBodyValueAt(amountkc, row, "storeamount");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "firstdiscount");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "seconddiscount");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "bcysje");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "lsdiscount");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "lsyydiscount");
	            getBillCardPanel().setBodyValueAt(0, e.getRow(), "lssydiscount");
	            
	            getBillCardPanel().setBodyValueAt(0, row, "firstcount");
	            getBillCardPanel().setBodyValueAt(0, row, "secondcount");
	            getBillCardPanel().setBodyValueAt(0, row, "bcysje");
	            
	            //modfiy by houcq 2011-04-18
	            //UFDouble price = new PubTools().getInvPrice(pk_invbasdoc);
	            UFDouble price = new PubTools().getInvPrice(pk_invbasdoc,_getDate());
	            int rows=e.getRow();
	        	String pk_measdoc=getBillCardPanel().getBodyValueAt(rows, "pk_measdoc")==null?"":
	        		getBillCardPanel().getBodyValueAt(rows, "pk_measdoc").toString();
	        	String pk_invbasdoc2 = getBillCardPanel().getBodyValueAt(rows, "pk_invbasdoc")==null?"":
	        		getBillCardPanel().getBodyValueAt(rows, "pk_invbasdoc").toString();
				setUA2(pk_measdoc,pk_invbasdoc2,price,rows);
            
				getBillCardPanel().setBodyValueAt(price, row, "zprice");
				
				/**加上物料的已开提货单未出库数,已生产派工未入库数量  add by wb at 2008-10-22 18:51:11*/
				UFDouble[] unckrk = getUnckUnrk(pk_invbasdoc,_getDate());
				if(unckrk!=null&&unckrk.length==2){
					UFDouble ytwcamount = unckrk[0];		//已开提货单未出库数
					UFDouble ypgwrkamount = unckrk[1];		//已生产派工未入库数量
					UFDouble maxthamount = amountkc.sub(ytwcamount).add(ypgwrkamount);		//最大提货量
					getBillCardPanel().setBodyValueAt(ytwcamount, row, "ytwcamount");
					getBillCardPanel().setBodyValueAt(ypgwrkamount, row, "ypgwrkamount");
					getBillCardPanel().setBodyValueAt(maxthamount, row, "maxthamount");
				}
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//add by houcq 2011-02-25 begin,选择物料时清空表体二次折扣和表头分摊折扣
			getBillCardPanel().setHeadItem("def_6",null);
			int rows1 = getBillCardPanel().getBillTable().getRowCount();
			for (int i=0;i<rows1;i++)
			{
				getBillCardPanel().setBodyValueAt(null, i, "seconddiscount");
			}
			//add by houcq 2011-02-25 end
		}
        //选择单位时的处理  add by wb at 2008-5-6 14:11:50
//        if(strKey.equals("dw")){
//        	super.changeDW(pk_measdoc,"pk_measdoc","ladingamount", "price", "vje");
//        }
       
        //分摊折扣的处理  add by wb at 2008-6-5 15:31:36
        if(strKey.equals("def_6")){
        	 BillModel model = getBillCardPanel().getBillModel();
             if (model != null) {
            	 int rowCount = model.getRowCount();
                 if (rowCount < 1) {
                     showErrorMessage("表体为空,不能分摊折扣!");
                     getBillCardPanel().setHeadItem("def_6", null);
                     return;
                 }
               try {
                 LadingbillVO ladVO = (LadingbillVO)getChangedVOFromUI().getParentVO();
            	 UFDouble dkze = ladVO.getDkze()==null?null:ladVO.getDkze();                    // 总额
            	 if (dkze==null) {
                     showErrorMessage("提货数量未输入不能分摊,请先输入数量!");
                     getBillCardPanel().setHeadItem("def_6", null);
                     return;
                 }
            	    UFDouble zkye = ladVO.getDef_8()==null?new UFDouble(0):ladVO.getDef_8();                    // 可用折扣总额
					UFDouble seconddiscount = ladVO.getDef_7()==null?new UFDouble(0):ladVO.getDef_7();          // 二次折扣金额(客户二次折扣余额)
					UFDouble ftdiscount = ladVO.getDef_6()==null?new UFDouble(0):ladVO.getDef_6();             // 分摊的总折扣
					if(ftdiscount.compareTo(zkye)>0){
						showErrorMessage("分摊折扣不能大于理论折扣总额!");
						getBillCardPanel().setHeadItem("def_6", null);
						return;
					}
					else if(ftdiscount.compareTo(seconddiscount)>0){
						showErrorMessage("分摊折扣不能大于二次折扣金额!");
						getBillCardPanel().setHeadItem("def_6", null);
						return;
					}
					else{
//						int iRet = showYesNoMessage("是否确定分摊此折扣?");
//		                if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
		                	int row=getBillCardPanel().getBillTable().getRowCount();
		                    UFDouble vjeze = dkze;         // 总金额 根据表体数据金额与总金额的比例来分摊折扣
		                    UFDouble ze = new UFDouble(0); // 表体分摊的金额合计
		                    DecimalFormat df = new DecimalFormat("#.00"); 
		                    for(int i=0;i<row;i++){
		                		UFDouble vje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"vje")==null?"0":
		                            getBillCardPanel().getBodyValueAt(i,"vje").toString());
		                 		UFDouble bl = vje.div(vjeze);  // 所占比例
		                		UFDouble ftdisco = new UFDouble(df.format(ftdiscount.multiply(bl).toDouble()));
		                		getBillCardPanel().setBodyValueAt(ftdisco, i,"seconddiscount");
		                        String[] formual=getBillCardPanel().getBodyItem("ladingamount").getEditFormulas();//获取编辑公式
//		                        UFDouble a=new UFDouble(getBillCardPanel().getBodyValueAt(i, "vje").toString());
//		                        UFDouble b=new UFDouble(getBillCardPanel().getBodyValueAt(i, "seconddiscount").toString());
		                        getBillCardPanel().execBodyFormulas(i,formual);
		                        ze = ze.add(ftdisco);
		                	}
		                	//表体的分摊金额合计与表体分摊总额比较,由于比例的四舍五入,需将多余的折扣分摊到最后一行表体中
		                	UFDouble chae = ftdiscount.sub(ze);
//		                	if(chae.toDouble()<0){
		                		UFDouble lastdis = new UFDouble(getBillCardPanel().getBodyValueAt(row-1,"seconddiscount")==null?"0":
		                            		getBillCardPanel().getBodyValueAt(row-1,"seconddiscount").toString());
		                		getBillCardPanel().setBodyValueAt(lastdis.add(chae), row-1, "seconddiscount");
		                		
		                		UFDouble bcysje2 = new UFDouble(getBillCardPanel().getBodyValueAt(row-1,"bcysje")==null?"0":
                            		getBillCardPanel().getBodyValueAt(row-1,"bcysje").toString());
		                		getBillCardPanel().setBodyValueAt(bcysje2.sub(chae), row-1, "bcysje");
//		                	}
		                    updateUI();
//		                }
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
                 
                	 
             }
        }
        // 填写本次提货数量后实时带出已提货量(不包括本次提货货量) add by wb at 2008-5-15 11:47:22
        int flag = ClientEventHandler.flag;          // 单据增加标记
        int invoiceflag = ClientEventHandler.invoiceflag;          // 单据增加标记
        if(e.getKey().equals("ladingamount")&&flag==1){  // 当销售订单增加时带出已提货量
	       	 int row = getBillCardPanel().getBillTable().getSelectedRow();
	       	 String vsourcebillid = getBillCardPanel().getBodyValueAt(row, "vsourcebillid")==null?null:
	       		                   getBillCardPanel().getBodyValueAt(row, "vsourcebillid").toString();
	       	 String pk_ladingbill_b = getBillCardPanel().getBodyValueAt(row, "pk_ladingbill_b")==null?null:
	                                  getBillCardPanel().getBodyValueAt(row, "pk_ladingbill_b").toString();  // 子表主键
	            //实时的已收货量(不包括本次收货量)
	       	 UFDouble amount = PubTools.calTotalamount("eh_ladingbill_b", "ladingamount", vsourcebillid, "pk_ladingbill_b", pk_ladingbill_b);
	       	 getBillCardPanel().setBodyValueAt(amount, row, "ytamount");
	       	 int rowcount=getBillCardPanel().getBillTable().getRowCount();
	       	 for(int i=0;i<rowcount;i++){
	            	getBillCardPanel().setBodyValueAt(null, i, "seconddiscount");    // 输入提货量时将二次折扣设为空
	         }
	       	getBillCardPanel().setHeadItem("def_6",null);
	       	ClientEventHandler.flag = 0;//将订单标志初始化
        }
        //---------------------------------------------------------------------------------
        if(e.getKey().equals("ladingamount")&&invoiceflag==2){  // 当销售发票增加时带出已提货量
	       	 int row = getBillCardPanel().getBillTable().getSelectedRow();
	       	 @SuppressWarnings("unused")
			String vsourcebillid = getBillCardPanel().getBodyValueAt(row, "vsourcebillid")==null?null:
	       		                   getBillCardPanel().getBodyValueAt(row, "vsourcebillid").toString();
	       	 @SuppressWarnings("unused")
			String pk_ladingbill_b = getBillCardPanel().getBodyValueAt(row, "pk_ladingbill_b")==null?null:
	                                  getBillCardPanel().getBodyValueAt(row, "pk_ladingbill_b").toString();  // 子表主键
	            //实时的已收货量(不包括本次收货量)
//	       	 UFDouble amount = PubTools.calTotalamount("eh_ladingbill_b", "ladingamount", vsourcebillid, "pk_ladingbill_b", pk_ladingbill_b);
//	       	 getBillCardPanel().setBodyValueAt(amount, row, "ytamount");
//	       	 int rowcount=getBillCardPanel().getBillTable().getRowCount();
//	       	 for(int i=0;i<rowcount;i++){
//	            	getBillCardPanel().setBodyValueAt(null, i, "seconddiscount");    // 输入提货量时将二次折扣设为空
//	         }
//	       	getBillCardPanel().setHeadItem("def_6",null);
	       	 
	       	String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();            //产品
	       	
	       	String pk_unit = getBillCardPanel().getBodyValueAt(row,"pk_measdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_measdoc").toString();            //单位
            
            UFDouble amount=new UFDouble(getBillCardPanel().getBodyValueAt(row,"ladingamount")==null?"0":
               getBillCardPanel().getBodyValueAt(row,"ladingamount").toString());                                      //数量
           
            //根据单位转换一次折扣额  add by wb at 2008-5-31 15:19:15
            UFDouble rate = new PubTools().getInvRate(pk_invbasdoc, pk_unit);
            
            UFDouble zamount = amount.multiply(rate);//吨重
            
            UFDouble orderamount=new UFDouble(getBillCardPanel().getBodyValueAt(row,"orderamount")==null?"0":
                getBillCardPanel().getBodyValueAt(row,"orderamount").toString());    
            
            UFDouble fircut=new UFDouble(getBillCardPanel().getBodyValueAt(row,"def_10")==null?"0":
                getBillCardPanel().getBodyValueAt(row,"def_10").toString());    //一次折扣总额
            
            UFDouble seccut=new UFDouble(getBillCardPanel().getBodyValueAt(row,"def_9")==null?"0":
                getBillCardPanel().getBodyValueAt(row,"def_9").toString());    //二次折扣总额
            
            UFDouble firdiscut = amount.div(orderamount).multiply(fircut);//本次一次折扣
            UFDouble secdiscut = amount.div(orderamount).multiply(seccut);//本次二次折扣
            
            this.getBillCardPanel().setBodyValueAt(zamount, row, "zamount");//吨重
            
            this.getBillCardPanel().setBodyValueAt(firdiscut, row, "firstdiscount");
            this.getBillCardPanel().setBodyValueAt(secdiscut, row, "seconddiscount");
            UFDouble price=new UFDouble(getBillCardPanel().getBodyValueAt(row,"price")==null?"0":
                getBillCardPanel().getBodyValueAt(row,"price").toString());    //二次折扣
            
            UFDouble bcysje = amount.multiply(price).sub(firdiscut).sub(secdiscut);
            this.getBillCardPanel().setBodyValueAt(bcysje, row, "bcysje");
            
            String[] formual=getBillCardPanel().getBodyItem("ladingamount").getEditFormulas();//获取编辑公式
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            
            
            //ClientEventHandler.invoiceflag = 0;//将销售发票标志初始化
       }
        //---------------------------------------------------------------------------------
        
        //选择产品时计算折扣,修改表体中的产品数量，将表体中的本次应收金额合计后写入表头中的应收款总额
        if((strKey.equals("vinvbascode")||strKey.equals("ladingamount")||strKey.equals("dw")||strKey.equals("def_6"))&&invoiceflag != 2){
            String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();   //客户
            int row=getBillCardPanel().getBillTable().getSelectedRow();
            String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();            //产品
            @SuppressWarnings("unused")
			UFDouble seconddisount  = new UFDouble(getBillCardPanel().getBodyValueAt(row,"def_7")==null?"0":
                								getBillCardPanel().getBodyValueAt(row,"def_7").toString());            
            ArrayList arr=new nc.ui.eh.trade.z0206005.ClientUI().getDiscount(pk_cubasdoc,pk_invbasdoc);
            HashMap hm=(HashMap)arr.get(0);                                                                       //一次折扣
            @SuppressWarnings("unused")
			HashMap hm2=(HashMap)arr.get(1);
            
            @SuppressWarnings("unused")
			int rowcount=getBillCardPanel().getBillTable().getRowCount();
          
            String pk_unit = getBillCardPanel().getBodyValueAt(row,"pk_measdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_measdoc").toString();            //单位
            
            UFDouble amount=new UFDouble(getBillCardPanel().getBodyValueAt(row,"ladingamount")==null?"0":
               getBillCardPanel().getBodyValueAt(row,"ladingamount").toString());                                      //数量
           
            //根据单位转换一次折扣额  add by wb at 2008-5-31 15:19:15
            UFDouble rate = new PubTools().getInvRate(pk_invbasdoc, pk_unit);
            UFDouble firstdiscount=new UFDouble(hm.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm.get(pk_invbasdoc+pk_cubasdoc).toString()).multiply(rate);
            getBillCardPanel().setBodyValueAt(amount.multiply(firstdiscount), row, "firstdiscount");                //根据客户和产品计算一次折扣
           
//            UFDouble seccount=new UFDouble(hm2.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm2.get(pk_invbasdoc+pk_cubasdoc).toString()).div(rate);//可用二次折扣
//           
//            for(int i=0;i<rowcount;i++){
//                String pk_inv=getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc")==null?"":
//                    getBillCardPanel().getBodyValueAt(i,"pk_invbasdoc").toString();     
//                if(pk_inv.equals(pk_invbasdoc)){
//                    UFDouble amount1=new UFDouble(getBillCardPanel().getBodyValueAt(i,"ladingamount")==null?"0":
//                        getBillCardPanel().getBodyValueAt(i,"ladingamount").toString());     
//                    UFDouble firstdiscount1=new UFDouble(getBillCardPanel().getBodyValueAt(i,"firstdiscount")==null?"0":
//                        getBillCardPanel().getBodyValueAt(i,"firstdiscount").toString());  
//                    UFDouble price1=new UFDouble(getBillCardPanel().getBodyValueAt(i,"price")==null?"0":
//                        getBillCardPanel().getBodyValueAt(i,"price").toString());
//                    
//                    UFDouble secondcount=price1.multiply(amount1).multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscount1); //计算所得的二次折扣
//                    
//                    UFDouble lsdiscount=new UFDouble(getBillCardPanel().getBodyValueAt(i,"lsdiscount")==null?"0":
//                        getBillCardPanel().getBodyValueAt(i,"lsdiscount").toString());  
//                    
//                    
//                        //如果计算所得折扣小于折扣表上的可用折扣则以折扣表上的二次折扣为折扣额
//                        if(seccount.compareTo(secondcount)<0){
//                            secondcount=seccount;
//                        }
//                        if(secondcount.doubleValue()<0){
//                            secondcount=new UFDouble(0);
//                        }
//                        
//                        
//                        UFDouble templediscount =(price1.multiply(amount1).multiply(IPubInterface.DISCOUNTRATE)).sub(firstdiscount1).sub(secondcount);
//                        
//                        //计算一次折扣和二次折扣之和是否满足折扣总额,若已满不做处理，如不满加上临时折扣
//                        if(templediscount.doubleValue()>0){
//                            UFDouble addlidiscount= lsdiscount.sub(templediscount);                     //剩余临时折扣额
//                            //若临时折扣多于所需要的折扣额则加上多出折扣额，若少于则将临时折扣全部加上
//                            if(addlidiscount.doubleValue()>0){
//                                getBillCardPanel().setBodyValueAt(secondcount.add(templediscount), i, "seconddiscount");  
//                                getBillCardPanel().setBodyValueAt(templediscount, i, "lsyydiscount");  
//                                getBillCardPanel().setBodyValueAt(addlidiscount, i, "lssydiscount");  
//                            }else{
//                                getBillCardPanel().setBodyValueAt(secondcount.add(lsdiscount), i, "seconddiscount");  
//                                getBillCardPanel().setBodyValueAt(lsdiscount, i, "lsyydiscount");  
//                                getBillCardPanel().setBodyValueAt(0, i, "lssydiscount");  
//                            }
//                        }else{
//                            getBillCardPanel().setBodyValueAt(secondcount, i, "seconddiscount");  
//                            getBillCardPanel().setBodyValueAt(0, i, "lsyydiscount");  
//                            getBillCardPanel().setBodyValueAt(lsdiscount, i, "lssydiscount");  
//                        }
//                        
//                        seccount=seccount.sub(secondcount);
//                        getBillCardPanel().setBodyValueAt(secondcount, i, "def_9");
//                }
//               
//            }
        }
            if((strKey.equals("vinvbascode")||strKey.equals("ladingamount")||strKey.equals("dw")||strKey.equals("def_6"))&&invoiceflag == 2){
            	
            	UFDouble ze=new UFDouble(0);                 //本次应收金额
            	//表体中的本次应收金额合计后写入表头中的应收款总额
                int rows=getBillCardPanel().getBillTable().getRowCount();
                for(int i=0;i<rows;i++){
                    UFDouble bcysje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"bcysje")==null?"0":
                        getBillCardPanel().getBodyValueAt(i,"bcysje").toString());
                    ze=ze.add(bcysje);
                }
                try {
    	            getBillCardPanel().setHeadItem("bcyfje",ze);
    	           } catch (Exception e1) {
    				e1.printStackTrace();
    			}
            }
            
            if(strKey.equals("ladingamount")){
            	int rows=e.getRow();
            	String pk_measdoc=getBillCardPanel().getBodyValueAt(rows, "pk_measdoc")==null?"":
            		getBillCardPanel().getBodyValueAt(rows, "pk_measdoc").toString();
            	String pk_invbasdoc2 = getBillCardPanel().getBodyValueAt(rows, "pk_invbasdoc")==null?"":
            		getBillCardPanel().getBodyValueAt(rows, "pk_invbasdoc").toString();
            	UFDouble amounts=new UFDouble(getBillCardPanel().getBodyValueAt(rows, "ladingamount")==null?"-1000":
            		getBillCardPanel().getBodyValueAt(rows, "ladingamount").toString());
            	try {
					setUA(pk_measdoc,pk_invbasdoc2,amounts,rows);
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//add by houcq 2011-02-25 begin,填提货数量时先清空表体二次折扣和表头分摊折扣
				getBillCardPanel().setHeadItem("def_6",null);
				int rows1 = getBillCardPanel().getBillTable().getRowCount();
				for (int i=0;i<rows1;i++)
				{
					getBillCardPanel().setBodyValueAt(null, i, "seconddiscount");
					
					UFDouble bcysje=new UFDouble(0);
	                 
	                UFDouble vje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"vje")==null?"0":
	                    getBillCardPanel().getBodyValueAt(i,"vje").toString());
	              
	                UFDouble firstdis=new UFDouble(getBillCardPanel().getBodyValueAt(i,"firstdiscount")==null?"0":
	                    getBillCardPanel().getBodyValueAt(i,"firstdiscount").toString());
	                bcysje = vje.sub(firstdis);
	                getBillCardPanel().setBodyValueAt(bcysje, i, "bcysje");
				}
				//add by houcq 2011-02-25 end
            }
            
            
            
            String[] formual=getBillCardPanel().getBodyItem("ladingamount").getEditFormulas();                        //获取编辑公式
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
            
            
            //表体中的本次应收金额合计后写入表头中的应收款总额
            int rows=getBillCardPanel().getBillTable().getRowCount();
            UFDouble ze=new UFDouble(0);                 //本次应收金额
            UFDouble vjeze=new UFDouble(0);                //货款总额
            UFDouble firstdiscze=new UFDouble(0);          //一次折扣总额
            UFDouble seccountze=new UFDouble(0);          //二次折扣总额
            for(int i=0;i<rows;i++){
                UFDouble bcysje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"bcysje")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"bcysje").toString());
                ze=ze.add(bcysje);
                UFDouble vje=new UFDouble(getBillCardPanel().getBodyValueAt(i,"vje")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"vje").toString());
                vjeze=vjeze.add(vje);
                UFDouble scount=new UFDouble(getBillCardPanel().getBodyValueAt(i,"seconddiscount")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"seconddiscount").toString());
                seccountze=seccountze.add(scount);
                UFDouble firstdis=new UFDouble(getBillCardPanel().getBodyValueAt(i,"firstdiscount")==null?"0":
                    getBillCardPanel().getBodyValueAt(i,"firstdiscount").toString());
                firstdiscze=firstdiscze.add(firstdis);
            }
            try {
				LadingbillVO ladVO = (LadingbillVO)getChangedVOFromUI().getParentVO();
			
	            UFDouble lastzk = new UFDouble(ladVO.getDef_7()==null?"0":ladVO.getDef_7().toString());
	            getBillCardPanel().setHeadItem("bcyfje",ze);
	            getBillCardPanel().setHeadItem("seconddiscount",lastzk.sub(seccountze));                         // 二次折扣余额 = 二次折扣金额(上月折扣)-本次所用二次折扣
	            getBillCardPanel().setHeadItem("dkze",vjeze);
//	            UFDouble def_8 = vjeze.multiply(IPubInterface.DISCOUNTRATE); 
	            UFDouble sczk = new UFDouble(0);
	            UFDouble zk = vjeze.multiply(IPubInterface.DISCOUNTRATE).sub(firstdiscze);// 理论二次折扣 (金额*40%-一次折扣总额)
	            if(zk.compareTo(new UFDouble(0))>0){//当理论二次折扣为负数时用0替换
	            	sczk = zk;
	            }
	            getBillCardPanel().setHeadItem("def_8",sczk);  // 理论二次折扣 (金额*40%-一次折扣总额) edit by wb at 2008-7-10 12:25:07
            } catch (Exception e1) {
				e1.printStackTrace();
			}
       // }
        

        
        updateUI();
    }
    
    
    
    
    public void setDefaultData() throws Exception {
        String pk_corp = _getCorp().getPrimaryKey();
        
        getBillCardPanel().setHeadItem("ladingdate", _getDate());
        //add by houcq modify 2010-11-22 提退货日期设为自动延+30天
        getBillCardPanel().setHeadItem("enddate", _getDate().getDateAfter(30));
        //add by houcq 2010-12-09默认提货人是本人
        getBillCardPanel().setHeadItem("self_flag",new UFBoolean(true));
        BillItem oper = getBillCardPanel().getTailItem("coperatorid");
        if (oper != null)
            oper.setValue(_getOperator());
        else
            getBillCardPanel().getHeadItem("coperatorid").setValue(_getOperator());
        BillItem date = getBillCardPanel().getTailItem("dmakedate");
        if (date != null)
            date.setValue(_getDate());
        else
            getBillCardPanel().getHeadItem("dmakedate").setValue(_getDate());
        BillItem busitype = getBillCardPanel().getHeadItem("pk_busitype");
        if (busitype != null)
            getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
       
        getBillCardPanel().getHeadItem("pk_corp").setValue(pk_corp);

        getBillCardPanel().setHeadItem("vbilltype", this.getUIControl().getBillType());
        BillItem vbillstatus = getBillCardPanel().getHeadItem("vbillstatus");
        if (vbillstatus!= null)
            getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));
    
    }
    
    /*
     * 注册自定义按钮
     * 2008-04-02
     */
    protected void initPrivateButton() {    	
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
        btn.setOperateStatus(new int[]{OperationState.EDIT,OperationState.ADD});
        addPrivateButton(btn);
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.TEMPLETDISCOUNT,"临时折扣","临时折扣");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_EDIT});
        addPrivateButton(btn1);
        super.initPrivateButton();
    }
    
    @Override
    protected void initSelfData() {
//    	getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
    	super.initSelfData();
    }

    @SuppressWarnings("unchecked")
	public HashMap getInvRate2(String pk_invbasdoc){
    	HashMap hm = new HashMap();
//    	StringBuffer sql = new StringBuffer()
//        .append(" select c.pk_measdoc,a.price,b.changerate from eh_invbasdoc a,eh_invbasdoc_b b,bd_measdoc c")
//        .append(" where a.pk_invbasdoc = b.pk_invbasdoc")
//        .append(" and b.pk_measdoc = c.pk_measdoc")
//        .append(" and a.pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(a.dr,0)=0 and isnull(b.dr,0)=0 and isnull(c.dr,0)=0 ")
//        .append(" union all ")
//        .append(" select c.pk_measdoc,a.price,1 changerate")
//        .append(" from  eh_invbasdoc a,bd_measdoc c")
//        .append(" where a.pk_measdoc = c.pk_measdoc")
//        .append(" and a.pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(a.dr,0)=0 and isnull(c.dr,0)=0");
    	//升级SQL语句
    	String sql = " select c.pk_measdoc,a.def3 price,b.mainmeasrate changerate "+
    				 " from bd_invbasdoc a,bd_convert b,bd_measdoc c "+
    				 " where a.pk_invbasdoc = b.pk_invbasdoc "+
    				 " and b.pk_measdoc = c.pk_measdoc "+
    				 " and a.pk_invbasdoc = (select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') "+
    				 " and nvl(a.dr,0)=0 and nvl(b.dr,0)=0 and nvl(c.dr,0)=0 "+
    				 " union all "+
    				 " select c.pk_measdoc,a.def3 price,1 changerate "+
    				 " from  bd_invbasdoc a,bd_measdoc c "+
    				 " where a.pk_measdoc = c.pk_measdoc "+
    				 " and a.pk_invbasdoc = (select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"') "+
    				 " and nvl(a.dr,0)=0 and nvl(c.dr,0)=0 ";

        IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        try {
        	Vector vc = (Vector)iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
		    if(vc!=null&&vc.size()>0){
		    	for(int i=0; i<vc.size(); i++){
		    		Vector vcc = (Vector)vc.get(i);
		    		String pk_measdoc = vcc.get(0)==null?"":vcc.get(0).toString();
		    		UFDouble price = new UFDouble(vcc.get(1)==null?"0":vcc.get(1).toString());
		    		UFDouble changerate = new UFDouble(vcc.get(2)==null?"0":vcc.get(2).toString());
		    		hm.put(pk_measdoc, new UFDouble[]{price,changerate});
		    	}
		    }
         }catch (BusinessException e1) {
 			e1.printStackTrace();
 		}
        return hm;
    }
    
    /**
     * 主计量单位和辅助计量单位之间的装换系数和数量的变换
     * @throws BusinessException 
     */
    @SuppressWarnings("unchecked")
	public void setUA(String pk_measdoc,String pk_invbasdoc,UFDouble amount,int row) throws BusinessException{
    	String sql="select mainmeasrate changerate from bd_convert " +
    			" where pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc " +
    			" where pk_invmandoc='"+pk_invbasdoc+"') " +
    			" and pk_measdoc='"+pk_measdoc+"' and nvl(dr,0)=0";
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
    	UFDouble changerate=new UFDouble(-1000000);
    	for(int i=0;i<al.size();i++){
    		HashMap hm=(HashMap) al.get(i);
    		changerate=new UFDouble(hm.get("changerate")==null?"-10000":hm.get("changerate").toString());
    	}
//    	UFDouble fzamount=amount.div(changerate);
    	/**原来系数是 1吨 = 25袋，1袋=1/25吨 
    	 * 升级后系数的维护为 1袋 = 0.04吨  则 吨数 = 订单数量*系数 edit by wb 2009年11月4日11:23:25***/
    	UFDouble fzamount=amount.multiply(changerate);
    	getBillCardPanel().setBodyValueAt(fzamount, row, "zamount");
    	
    }
    
    
    @SuppressWarnings("unchecked")
	public void setUA2(String pk_measdoc,String pk_invbasdoc,UFDouble zprice,int row) throws BusinessException{
    	String sql="select mainmeasrate changerate from bd_convert " +
				" where pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc " +
				" where pk_invmandoc='"+pk_invbasdoc+"') " +
				" and pk_measdoc='"+pk_measdoc+"' and nvl(dr,0)=0";
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
    	UFDouble changerate=new UFDouble(-1000000);
    	for(int i=0;i<al.size();i++){
    		HashMap hm=(HashMap) al.get(i);
    		changerate=new UFDouble(hm.get("changerate")==null?"-10000":hm.get("changerate").toString());
    	}
//    	UFDouble fzamount=zprice.div(changerate);
    	/**原来系数是 1吨 = 25袋，1袋=1/25吨 
    	 * 升级后系数的维护为 1袋 = 0.04吨  则 吨数 = 订单数量*系数 edit by wb 2009年11月4日11:23:25***/
    	UFDouble fzamount=zprice.multiply(changerate);
    	getBillCardPanel().setBodyValueAt(fzamount, row, "price");
    }
    
    /***
	 * 得到登录日期前2天(含当日)的物料的已生产派工未入库数量和所有已开提货单未出库数
	 * @param pk_invbasdoc 物料管理档案PK
	 * @return
	 * wb 2008-10-22 18:25:34
	 */
	@SuppressWarnings("unchecked")
	public UFDouble[] getUnckUnrk(String pk_invbasdoc,UFDate date){
		UFDouble[] unrksc = new UFDouble[2];
		String pk_corp = _getCorp().getPk_corp();
		UFDate beforeDate = date.getDateBefore(1);
		StringBuffer sql = new StringBuffer()
		.append(" select sum(isnull(a.pgamount,0)) pgamount,sum(isnull(a.scamount,0)) scamount")
		.append(" from ")
//		.append(" ---登录日期前11天(含当日)已生产派工未入库数量")
		.append(" (select b.pk_invbasdoc,sum(isnull(b.pgamount,0)) pgamount,0 scamount")
		.append(" from eh_sc_pgd a,eh_sc_pgd_b b")
		.append(" where a.pk_pgd = b.pk_pgd")
		.append(" and isnull(a.lock_flag,'N') <> 'Y'")
		.append(" and isnull(a.rk_flag,'N')<>'Y'")
		.append(" and isnull(a.xdflag,'N')='Y'")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"' ")
		.append(" and isnull(a.dr,0) = 0")
		.append(" and isnull(b.dr,0) = 0")
		.append(" and a.dmakedate between '"+beforeDate+"' and '"+date+"'")
		.append(" group by b.pk_invbasdoc")
		.append(" union all ")
		//modify by houcq 2011-08-16
		//已开提货单未出库数
//		.append(" select b.pk_invbasdoc,0 pgamount,sum(isnull(b.zamount,0)) scamount")
//		.append(" from eh_ladingbill a,eh_ladingbill_b b")
//		.append(" where a.pk_ladingbill = b.pk_ladingbill")
//		.append(" and isnull(a.ck_flag,'N') <> 'Y'")
//		.append(" and isnull(a.lock_flag,'N') <> 'Y'")//add by houcq 2011-08-05
//		.append(" and isnull(b.isfull,'N') <> 'Y'")
//		.append(" and a.vbillstatus = 1")
//		.append(" and a.pk_corp = '"+pk_corp+"'")
//		.append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"'  ")
//		.append(" and isnull(a.dr,0) = 0")
//		.append(" and isnull(b.dr,0) = 0")
//		.append(" group by b.pk_invbasdoc")
		.append(" select pk_invbasdoc, 0 pgamount,sum(nvl(zamount,0))-sum(nvl(outamount,0)) scamount from (")
		.append(" select a.pk_ladingbill_b,a.pk_invbasdoc,a.zamount,b.outamount from")
		.append(" (select b.pk_invbasdoc,b.pk_ladingbill_b, sum(nvl(b.zamount,0)) zamount")
		.append(" from eh_ladingbill a, eh_ladingbill_b b")
		.append(" where a.pk_ladingbill = b.pk_ladingbill and a.dmakedate <= '"+date+"'")
		//.append(" and nvl(a.dr,0) = 0 and a.vbillstatus = 1 and nvl(b.dr,0) = 0")
		.append(" and nvl(a.dr,0) = 0 and nvl(b.dr,0) = 0")//最大提货量扣除自由态单据的提货量 。
		.append(" and nvl(a.lock_flag,'N') = 'N' and nvl(a.ck_flag,'N') = 'N' and nvl(b.isfull,'N') = 'N'")
		.append(" and a.pk_corp = '"+pk_corp+"' and b.pk_invbasdoc = '"+pk_invbasdoc+"'")
		.append(" group by b.pk_ladingbill_b,b.pk_invbasdoc) a,")
		.append(" (select b.vsourcebillid, sum(nvl(b.outamount,0)) outamount from eh_icout a, eh_icout_b b")
		.append(" where a.pk_icout = b.pk_icout and nvl(a.dr,0) = 0 and a.vbillstatus = 1 and nvl(b.dr,0) = 0")
		.append(" and a.pk_corp='"+pk_corp+"' and b.pk_invbasdoc='"+pk_invbasdoc+"'")
		.append(" group by b.vsourcebillid) b")
		.append(" where a.pk_ladingbill_b = b.vsourcebillid(+))")
		.append(" group by pk_invbasdoc")
		.append(" ) a");
		IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
    		ArrayList  arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
    		if(arr!=null&&arr.size()>0){
				HashMap hmA = (HashMap)arr.get(0);
				unrksc[0] = new UFDouble(hmA.get("scamount")==null?"0":hmA.get("scamount").toString());			//已开提货单未出库数
				unrksc[1] = new UFDouble(hmA.get("pgamount")==null?"0":hmA.get("pgamount").toString());			//已生产派工未入库数量
    		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return unrksc;
	}
}

   
    

