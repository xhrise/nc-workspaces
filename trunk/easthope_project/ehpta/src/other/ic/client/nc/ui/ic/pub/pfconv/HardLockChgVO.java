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
 * ��Ĺ��ܡ���;���ִ�BUG���Լ��������˿��ܸ���Ȥ�Ľ��ܡ�
 * ���ߣ����˾�
 * @version	����޸�����(2002-4-26 10:58:20)
 * @see		��Ҫ�μ���������
 * @since		�Ӳ�Ʒ����һ���汾�����౻��ӽ���������ѡ��
 * �޸��� + �޸�����
 * �޸�˵��
 */
public class HardLockChgVO implements nc.vo.pf.change.IchangeVO {
/**
 * VOChange ������ע�⡣
 */
public HardLockChgVO() {
	super();
}
/*
 	���ݲ�Ʒ���Լ������󣬰�ԴVO����Ϣͨ�����㣬
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
 	���ݲ�Ʒ���Լ������󣬰�ԴVO����Ϣͨ���������ת��
 	����ת��
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
		//�����޸ģ�ȡ�����
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
	//	Դͷ����Ƕ���,��Դ�Ƿ�Ʊ����ô��һ��Դͷ�Ƿ����˻����͡�
	String sReplenishflag = "N";
	boolean isQueryFirst = false;
	if(csourctype != null && csourctype.equals("32") && cfirsttype != null && cfirsttype.equals("30")){
		sReplenishflag = iQuery.queryReplenishflag(cfirsthid);
		isQueryFirst = true;
	}
	for(int i=0;i<preVo.length;i++){
    if(csourctype.equals("30") || csourctype.equals("4331"))
      nowVo[i]=vocheck.setFreezeInfo(iQuery,preVo[i],nowVo[i]);
		//��ȡ��˰���ӡ�����˰���ӵ�
		nowVo[i]=vocheck.setOrderPrice(iQuery,preVo[i],nowVo[i]);
		if(isQueryFirst){
			if("N".equals(sReplenishflag))
				nowVo[i].getParentVO().setAttributeValue("freplenishflag",new UFBoolean("N"));
			else
				nowVo[i].getParentVO().setAttributeValue("freplenishflag",new UFBoolean("Y"));		
		}
	}
	
	//����ҵ�����͵õ��շ����.
	//**********add by cqw at 04-07-22 *******************
	String cBiztype = (String)((GeneralBillVO)nowVo[0]).getBizTypeid();
	String cdispatchid = iQuery.getDispatchForGenBill(cBiztype);
	if(cdispatchid != null){
		vocheck.setDispatchID(cdispatchid,nowVo);
	}
	//**********�����շ�������**************************
	
	//�˻����ܵ����ü���״̬
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
  //ȡֱ�˱��
  ICCommonBusi.setBDirecttranflag((GeneralBillVO[])nowVo);
  
			
	}catch(nc.vo.pub.BusinessException e){
		throw e;
	}catch(Exception e1){
		throw new nc.vo.pub.BusinessException("Remote Call",e1);
		}

	

	return nowVo;
}
}
