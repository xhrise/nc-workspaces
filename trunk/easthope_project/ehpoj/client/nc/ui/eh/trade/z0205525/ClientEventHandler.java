package nc.ui.eh.trade.z0205525;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractEventHandler;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.eh.trade.z0205525.DiscountAdjustBVO;
import nc.vo.eh.trade.z0205525.DiscountAdjustVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * 说明：折扣调整单
 * @author 张起源 
 * 时间：2008-4-12
 */
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@Override
	public void onBoSave() throws Exception {
		//非空判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        
        //表体项次唯一性校验
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc","pk_discounttype"});
        if(res==1){
            getBillUI().showErrorMessage("有同折扣类型的物料编码，不允许操作！");
            return;
        }
        /**表体调入折扣总额不能大于调出客户总余额 add by wb at 2008-11-26 14:16:21**/
        DiscountAdjustVO hvo = (DiscountAdjustVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        UFDouble custdiscount = hvo.getDef_6();				//调出客户折扣余额
        DiscountAdjustBVO[] bvos = (DiscountAdjustBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        if(bvos!=null&&bvos.length>0){
        	UFDouble sumdiscount = new UFDouble(0);
        	for(int i=0;i<bvos.length;i++){
        		sumdiscount = sumdiscount.add(bvos[i].getAdjustmoney());
        	}
        	if(sumdiscount.compareTo(custdiscount)>0){
        		getBillUI().showWarningMessage("表体调入折扣总额不能大于调出客户余额!");
        		return; 
        	}
        }
        super.onBoSave();

		}
	@Override
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
		String pk_cubasdoc=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdocadd").getValueObject()==null?null:
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdocadd").getValueObject().toString();  // 调入客户
		if(pk_cubasdoc==null){
			getBillUI().showErrorMessage("生成明细,请选择调入客户!");
			return;
		}
		int nyear = _getDate().getYear();
		int nmonth = _getDate().getMonth();
		StringBuffer sql = new StringBuffer()
		.append(" select a.pk_invbasdoc,sum(nvl(a.ediscount,0)) ediscount")
		.append(" from eh_perioddiscount a")
		.append(" where nyear = "+nyear+"")
		.append(" and nmonth = "+nmonth+"")
		.append(" and pk_cubasdoc = '"+pk_cubasdoc+"'")
//		.append(" and ediscount>0")
		.append(" and isnull(a.dr,0)=0 group by a.pk_invbasdoc");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		try {
			ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
			if(al!=null&&al.size()>0){
				for(int i=0;i<al.size();i++){
					DiscountAdjustBVO bvo=new DiscountAdjustBVO();
					HashMap hm=(HashMap) al.get(i);
					String pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
					String pk_discounttype = hm.get("pk_discounttype")==null?"":hm.get("pk_discounttype").toString();
					UFDouble discount = new UFDouble(hm.get("ediscount")==null?"0":hm.get("ediscount").toString());
					bvo.setPk_invbasdoc(pk_invbasdoc);
					bvo.setPk_discountype(pk_discounttype);
					bvo.setAdjustmoney(discount);
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().addLine();
					getBillCardPanelWrapper().getBillCardPanel().getBillModel().setBodyRowVO(bvo, i);
					String[] pk_invbasdo=getBillCardPanelWrapper().getBillCardPanel().getBodyItem("vinvcode").getLoadFormula();
					getBillCardPanelWrapper().getBillCardPanel().execBodyFormulas(i, pk_invbasdo);
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
         if ("增加".equals(bo.getCode()))
         {
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
         }        
		 super.onButton_N(bo, model);
	 }

}