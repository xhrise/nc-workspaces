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
 * ˵�����ۿ۵�����
 * @author ����Դ 
 * ʱ�䣺2008-4-12
 */
public class ClientEventHandler extends AbstractEventHandler {

	public ClientEventHandler(BillManageUI arg0, IControllerBase arg1) {
		super(arg0, arg1);
	}

	@Override
	public void onBoSave() throws Exception {
		//�ǿ��ж�
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        
        //�������Ψһ��У��
        BillModel bm=getBillCardPanelWrapper().getBillCardPanel().getBillModel();
        
        int res=new PubTools().uniqueCheck(bm, new String[]{"pk_invbasdoc","pk_discounttype"});
        if(res==1){
            getBillUI().showErrorMessage("��ͬ�ۿ����͵����ϱ��룬�����������");
            return;
        }
        /**��������ۿ��ܶ�ܴ��ڵ����ͻ������ add by wb at 2008-11-26 14:16:21**/
        DiscountAdjustVO hvo = (DiscountAdjustVO)getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        UFDouble custdiscount = hvo.getDef_6();				//�����ͻ��ۿ����
        DiscountAdjustBVO[] bvos = (DiscountAdjustBVO[])getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        if(bvos!=null&&bvos.length>0){
        	UFDouble sumdiscount = new UFDouble(0);
        	for(int i=0;i<bvos.length;i++){
        		sumdiscount = sumdiscount.add(bvos[i].getAdjustmoney());
        	}
        	if(sumdiscount.compareTo(custdiscount)>0){
        		getBillUI().showWarningMessage("��������ۿ��ܶ�ܴ��ڵ����ͻ����!");
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
	            case IEHButton.GENRENDETAIL:  //������ϸ 
//	                QueryCust();
	            	onBOGenrendetail();
	                break;
	        
	        }   
	}
	
	/**
	 * ���ɵ��ͻ����ۿ��������ۿ۴���0��������ϸ
	 * wb at 2008-10-23 10:16:22
	 * edit ���ɵ���ͻ�����ϸ��by wb at 2008-11-27 9:12:53
	 */
	@SuppressWarnings("unchecked")
	private void onBOGenrendetail() {
		//����ձ���
		int rows= getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        if(rows>0){
        	int res = getBillUI().showOkCancelMessage("������ϸ��Ҫ����ձ���,�Ƿ�ȷ�����?");
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
			getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_cubasdocadd").getValueObject().toString();  // ����ͻ�
		if(pk_cubasdoc==null){
			getBillUI().showErrorMessage("������ϸ,��ѡ�����ͻ�!");
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
				getBillUI().showErrorMessage("û�д˿ͻ����ۿ���ϸ!");
			}
		}catch (BusinessException e) {
			e.printStackTrace();
		}
			
	}

	@SuppressWarnings("unchecked")
	public void onButton_N(ButtonObject bo, BillModel model) {
		 //�Թ�˾�ڳ����˽��п���ʱ�䣺2009-12-15���ߣ���־Զ
         if(!super.getEveDiscount()){
        	 this.getBillUI().showErrorMessage("�����ڼ��ۿ�δ���ʣ������ۿ��ڼ����ڵ㣬���б����ۿ��ڳ�����");
        	 return;
         }else{
        	//���ܣ��Ա����ڳ��ۿ۵Ŀ��� ʱ�䣺2009-12-15���ߣ���־Զ
        	 if(!super.getDiscount()){
            	 this.getBillUI().showErrorMessage("�����ڼ��ۿ�δ���㣬�����ۿ��ڼ����ڵ㣬�����������ݣ�");
            	 return;
             }
         }
         if ("����".equals(bo.getCode()))
         {
        	 //���ۿ��ڼ�����������������ݡ����ù��ܣ��κ�һ�����ܱ�Ǻ󣬾Ͳ��������ڸ��¶���¼���ۿ۵�������add by houcq 2011-07-07
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
      				getBillUI().showErrorMessage("�����ۿ��ڼ������ѽ�ת����ֹ�µ��Ƶ�����!");
      				return;
      	        }
      		} catch (BusinessException e) {
      			e.printStackTrace();
      		}
         }        
		 super.onButton_N(bo, model);
	 }

}