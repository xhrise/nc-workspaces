package nc.ui.eh.trade.z0205523;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessException;

/**
 * 
 * 功能：临时折扣
 * 时间：2009-11-18下午06:14:28
 * 作者：张志远
 */
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	public void onBoSave() throws Exception {
//		//非空判断
//        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
//        
//        //表体项次唯一性校验
//        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
//        
//        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc","pk_discounttype"});
//        if(res==1){
//            getBillUI().showErrorMessage("有同折扣类型的物料编码，不允许操作！");
//            return;
//        }
        /**表体调入折扣总额不能大于调出客户总余额 add by wb at 2008-11-26 14:16:21**/
//        DiscountAdjustVO hvo = (DiscountAdjustVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
//        UFDouble custdiscount = hvo.getDef_6();				//调出客户折扣余额
//        DiscountAdjustBVO[] bvos = (DiscountAdjustBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
//        if(bvos!=null&&bvos.length>0){
//        	UFDouble sumdiscount = new UFDouble(0);
//        	for(int i=0;i<bvos.length;i++){
//        		sumdiscount = sumdiscount.add(bvos[i].getAdjustmoney());
//        	}
//        	if(sumdiscount.compareTo(custdiscount)>0){
//        		getBillUI().showWarningMessage("表体调入折扣总额不能大于调出客户余额!");
//        		return; 
//        	}
//        }
        super.onBoSave();

		}

	protected void onBoElse(int intBtn) throws Exception {
		super.onBoElse(intBtn);
		  switch (intBtn)
	        {
	            case IEHButton.GENRENDETAIL:  //生成明细 
//	                QueryCust();
	            	onBOGenrendetail();
	                break;
	        
	        }   
	}
	
	/**
	 * 生成调客户在折扣余额表中折扣大于0的物料明细
	 * wb at 2008-10-23 10:16:22
	 * edit 生成调入客户的明细，by wb at 2008-11-27 9:12:53
	 */
	@SuppressWarnings("unchecked")
	private void onBOGenrendetail() {
		//先清空表体
		int rows= getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        if(rows>0){
        	int res = getBillUI().showOkCancelMessage("生成明细需要先清空表体,是否确认清空?");
        	if(res==1){
        		int[] rowcount=new int[rows];
                for(int i=rows - 1;i>=0;i--){
                	rowcount[i]=i;
                }
                getBillCardPanelWrapper().getBillCardPanel().getBillModel().delLine(rowcount);
        	}else{
        		return;
        	}
        }
        //客户PK
		String pk_cubasdoc=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?null:
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();  // 调入客户
		if(pk_cubasdoc==null){
			getBillUI().showErrorMessage("生成明细,请先选择客户!");
			return;
		}
		StringBuffer sql = new StringBuffer()
		.append(" select b.pk_invbasdoc ")
		.append(" ,c.invcode,c.invname,c.invspec,c.invtype,h.measname,c.def1,g.brandname ")
		.append(" from eh_custkxl b,bd_cumandoc cuman ,bd_cubasdoc cubas  ")
		.append(" ,bd_invmandoc cc ")
		.append(" ,bd_invbasdoc c ")
		.append(" ,eh_brand g ")
		.append(" ,bd_measdoc h ")
		.append(" where b.pk_cubasdoc = cuman.pk_cumandoc ")
		.append(" and cuman.pk_cubasdoc = cubas.pk_cubasdoc ")
		.append(" and b.pk_invbasdoc = cc.pk_invmandoc ")
		.append(" and cc.pk_invbasdoc = c.pk_invbasdoc ")
		.append(" and c.pk_measdoc = h.pk_measdoc ")
		.append(" and c.invpinpai = g.pk_brand ")
		.append(" and b.pk_cubasdoc = '"+pk_cubasdoc+"'  ")
		.append(" and cuman.pk_corp = '"+_getCorp().getPk_corp()+"' ")
		.append(" and cuman.dr = '0' ")
		.append(" and b.dr = '0' ")
		.append(" and cc.dr = '0' and h.dr = '0' and g.dr = '0' ");
		
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(al!=null&&al.size()>0){
				for(int i=0;i<al.size();i++){
					HashMap hm=(HashMap) al.get(i);
					String pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
					this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(pk_invbasdoc, i, "pk_invbasdoc");
					this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invcode"), i, "vcode");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invname"), i, "vname");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invspec"), i, "gg");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("invtype"), i, "invtype");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("measname"), i, "vunit");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("def1"), i, "colour");
		            this.getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(hm.get("brandname"), i, "vbrand");
//					String[] pk_invbasdo=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vcode").getLoadFormula();
//					getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i, pk_invbasdo);
				}
			}else{
				getBillUI().showErrorMessage("没有此客户的折扣明细!");
			}
		}catch (BusinessException e) {
			e.printStackTrace();
		}
			
	}
	
	 @SuppressWarnings("unchecked")
	public void onButton_N(ButtonObject bo, BillModel model) {
	 	
		 String code = bo.getCode()==null?"":bo.getCode();
		 if("自制单据".equals(code)){
			 //当折扣期间计算中生成下月数据、启用功能，任何一个功能标记后，就不允许再在该月度内录入临时折扣。add by houcq 2011-07-07
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
		 }
		 
		 super.onButton_N(bo, model);
	 }

}