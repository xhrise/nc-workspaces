package nc.ui.ic.pub.pfconv;

import nc.ui.ic.pub.ICCommonBusi;
import nc.ui.ic.pub.QualityCheckHelper;
import nc.ui.ic.pub.tools.GenMethod;
import nc.vo.ic.pub.bill.GeneralBillVO;
import nc.vo.ic.pub.billtype.BillTypeFactory;
import nc.vo.ic.pub.billtype.IBillType;
import nc.vo.ic.pub.billtype.ModuleCode;
import nc.vo.pub.lang.UFBoolean;

/**
 * 类的功能、用途、现存BUG，以及其它别人可能感兴趣的介绍。
 * 作者：王乃军
 * @version	最后修改日期(2002-4-26 10:58:20)
 * @see		需要参见的其它类
 * @since		从产品的那一个版本，此类被添加进来。（可选）
 * 修改人 + 修改日期
 * 修改说明
 */
public class HardLockChgVO implements nc.vo.pf.change.IchangeVO {
/**
 * VOChange 构造子注解。
 */
public HardLockChgVO() {
	super();
}
/*
 	根据产品组自己的需求，把源VO中信息通过运算，
*/
public nc.vo.pub.AggregatedValueObject retChangeBusiVO(
	nc.vo.pub.AggregatedValueObject preVo,
	nc.vo.pub.AggregatedValueObject nowVo)
	throws nc.vo.pub.BusinessException {

	if (preVo == null || nowVo == null)
		return nowVo;
	
	nc.vo.pub.AggregatedValueObject[] vos=retChangeBusiVOs(new nc.vo.pub.AggregatedValueObject[]{preVo},
		new nc.vo.ic.pub.bill.GeneralBillVO[]{( nc.vo.ic.pub.bill.GeneralBillVO)nowVo}
		);
	if(vos!=null&&vos.length>0)
		return vos[0];
	else 
		return nowVo;
}
/*
 	根据产品组自己的需求，把源VO中信息通过运算进行转换
 	数组转换
*/
public nc.vo.pub.AggregatedValueObject[] retChangeBusiVOs(nc.vo.pub.AggregatedValueObject[] preVo, nc.vo.pub.AggregatedValueObject[] nowVo) throws nc.vo.pub.BusinessException {
	if(preVo==null||nowVo==null)
		return nowVo;
	if(nowVo==null||nowVo.length==0)
		return nowVo;	
	
	GenMethod.retChangeBusiVOs(preVo,nowVo);
	try{
		String cfirsttype = null;
		String cfirsthid = null;
		String csourctype = null;
		//参照修改，取掉检查
		if(nowVo.length>0&&nowVo[0]!=null){
			cfirsttype = (String) ((GeneralBillVO) nowVo[0]).getItemValue(0, "cfirsttype");
			cfirsthid = (String) ((GeneralBillVO) nowVo[0]).getItemValue(0, "cfirstbillhid");
			csourctype=(String)((GeneralBillVO)nowVo[0]).getItemValue(0,"csourcetype");
			IBillType billType = BillTypeFactory.getInstance().getBillType(csourctype);
			if(csourctype!=null&&billType.typeOf(ModuleCode.SO)&&!csourctype.equals("30")&&!csourctype.equals("32")&&!csourctype.equals("4331"))
				nc.vo.ic.pub.check.VOCheck.checkSrcUnique(preVo,nc.vo.ic.pub.check.VOCheck.getUniKeysSO());	
		}
	
	
	nc.vo.ic.pub.check.VOCheck vocheck=new nc.vo.ic.pub.check.VOCheck();
  
  vocheck.setAddInfo(GenMethod.getIntance(), (GeneralBillVO[])nowVo);

	nc.ui.ic.pub.QueryInfo iQuery=new nc.ui.ic.pub.QueryInfo();
	//	源头如果是订单,来源是发票，那么查一下源头是否是退货类型。
	String sReplenishflag = "N";
	boolean isQueryFirst = false;
	if(csourctype != null && csourctype.equals("32") && cfirsttype != null && cfirsttype.equals("30")){
		sReplenishflag = iQuery.queryReplenishflag(cfirsthid);
		isQueryFirst = true;
	}
	for(int i=0;i<preVo.length;i++){
    if(csourctype.equals("30") || csourctype.equals("4331"))
      nowVo[i]=vocheck.setFreezeInfo(iQuery,preVo[i],nowVo[i]);
		//读取韩税单加、不韩税单加等
		nowVo[i]=vocheck.setOrderPrice(iQuery,preVo[i],nowVo[i]);
		if(isQueryFirst){
			if("N".equals(sReplenishflag))
				nowVo[i].getParentVO().setAttributeValue("freplenishflag",new UFBoolean("N"));
			else
				nowVo[i].getParentVO().setAttributeValue("freplenishflag",new UFBoolean("Y"));		
		}
	}
	
	//根据业务类型得到收发类别.
	//**********add by cqw at 04-07-22 *******************
	String cBiztype = (String)((GeneralBillVO)nowVo[0]).getBizTypeid();
	String cdispatchid = iQuery.getDispatchForGenBill(cBiztype);
	if(cdispatchid != null){
		vocheck.setDispatchID(cdispatchid,nowVo);
	}
	//**********处理收发类别结束**************************
	
	//退货接受单设置检验状态
	nc.vo.ic.pub.bill.GeneralBillVO[] voaRet = null;
	voaRet =
			QualityCheckHelper.setChkStaToFreeItems(
					(nc.vo.ic.pub.bill.GeneralBillVO[]) nowVo);
			nc.vo.scm.pub.SCMEnv.out("load check transfer ...............");
	if (voaRet != null && voaRet.length > 0) {
		nc.vo.ic.pub.bill.GeneralBillItemVO[] voaItem = null, voaItem2 = null;
		for (int kk = 0; kk < voaRet.length; kk++) {
			voaItem = voaRet[kk].getItemVOs();
			voaItem2 = (nc.vo.ic.pub.bill.GeneralBillItemVO[]) nowVo[kk].getChildrenVO();
			//reset free items        
			for (int i = 0; i < voaItem.length; i++)
				voaItem2[i].setFreeItemVO(voaItem[i].getFreeItemVO());
		}
	}
	
  if("4C".equals(((GeneralBillVO)nowVo[0]).getHeaderVO().getCbilltypecode())&& (
      "3U".equals(((GeneralBillVO)nowVo[0]).getItemValue(0, "csourcetype")) ||
      "3V".equals(((GeneralBillVO)nowVo[0]).getItemValue(0, "csourcetype")) ||
      "32".equals(((GeneralBillVO)nowVo[0]).getItemValue(0, "csourcetype")) ) ){
    ICCommonBusi.proccHslFromOut((GeneralBillVO[])nowVo);
  }
  //取直运标记
  ICCommonBusi.setBDirecttranflag((GeneralBillVO[])nowVo);
  
			
	}catch(nc.vo.pub.BusinessException e){
		throw e;
	}catch(Exception e1){
		throw new nc.vo.pub.BusinessException("Remote Call",e1);
		}

	

	return nowVo;
}
}
