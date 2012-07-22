package nc.vo.so.so002;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import nc.bs.framework.common.NCLocator;
import nc.itf.scm.so.so002.ISaleinvoiceQuery;
import nc.ui.pub.para.SysInitBO_Client;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.pu.RelationsCalVO;
import nc.vo.scm.pub.SCMEnv;
import nc.vo.scm.relacal.SCMRelationsCal;
import nc.vo.so.pub.SOCurrencyRateUtil;
import nc.vo.transfer.IEqualValueDownBill;
import nc.vo.transfer.IEqualValueUpBill;

public class OrderToInvoiceChangeVO implements IchangeVO {
  //������ֶ�Ӧ���۱�����
  private HashMap<String,UFDouble> hsexchangeRate = new HashMap<String,UFDouble>();
	/**
	 * ���ݲ�Ʒ���Լ������󣬰�ԴVO����Ϣͨ�����㣬
	 */
	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
  
		SaleinvoiceVO saleinvoice = null;
		try {
			nc.vo.so.so001.SaleOrderVO saleorder = (nc.vo.so.so001.SaleOrderVO) preVo;
			saleinvoice = (SaleinvoiceVO) nowVo;
      pk_corp = saleinvoice.getHeadVO().getPk_corp();
      String billdate =saleinvoice.getHeadVO().getDbilldate().toString();
			SaleinvoiceBVO[] saleItems = saleinvoice.getBodyVO();

			Vector<String> vecString = new Vector<String>();
      //�����ۿ�
      UFDouble ndiscount = null;
      //��Ʒ�ۿ�
      UFDouble nitemdiscount= null;
			if (saleItems != null && saleItems.length > 0) {
				for (int i = 0, loop = saleItems.length; i < loop; i++) {
          ndiscount = saleItems[i].getNdiscountrate();
          if(null == ndiscount)
            ndiscount = new UFDouble(100);
          //�������۷�Ʊ�ж����ۿ��ֶ�ֵ
          nitemdiscount = saleItems[i].getNitemdiscountrate();
          if(null == nitemdiscount)
            nitemdiscount = new UFDouble(100);
          saleItems[i].setNorderDiscount(ndiscount.multiply(nitemdiscount)
              .div(new UFDouble(100)));
          // �۱�����ʹ�õ�ʱ����
          UFDouble newbaserate = getExchangeRate(saleItems[i].getCcurrencytypeid(),billdate);
          saleItems[i].setNexchangeotobrate(newbaserate);
					vecString.add(saleItems[i].getCupsourcebillbodyid());
				}
        // ���ڶ�ο�Ʊ������ɿɿ�Ʊ���������۶������Ǻϣ���Ҫ����������
        if (getBTaxPrior().intValue() == 0) {
          nc.vo.scm.relacal.SCMRelationsCal.calculate(saleinvoice.getChildrenVO(), saleinvoice
              .getParentVO(), new int[] { RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE,
              RelationsCalVO.PRICE_PRIOR_TO_ITEMDISCOUNTRATE, RelationsCalVO.YES_LOCAL_FRAC },
              "nnumber", SaleinvoiceBVO.getDescriptions(), SaleinvoiceBVO.getKeysForVO(),
              new int[] { SCMRelationsCal.TAXPRICE_ORIGINAL, SCMRelationsCal.PRICE_ORIGINAL,
                  SCMRelationsCal.NET_TAXPRICE_ORIGINAL, SCMRelationsCal.NET_PRICE_ORIGINAL });
        } else {
          nc.vo.scm.relacal.SCMRelationsCal.calculate(saleinvoice.getChildrenVO(), saleinvoice
              .getParentVO(), new int[] { RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE,
              RelationsCalVO.PRICE_PRIOR_TO_ITEMDISCOUNTRATE, RelationsCalVO.YES_LOCAL_FRAC },
              "nnumber", SaleinvoiceBVO.getDescriptions(), SaleinvoiceBVO.getKeysForVO(),
              new int[] { SCMRelationsCal.TAXPRICE_ORIGINAL, SCMRelationsCal.PRICE_ORIGINAL,
                  SCMRelationsCal.NET_TAXPRICE_ORIGINAL, SCMRelationsCal.NET_PRICE_ORIGINAL });
        }
				String[] strOrderbid = null;
				if (vecString.size() > 0) {
					strOrderbid = new String[vecString.size()];
					vecString.copyInto(strOrderbid);
				}
				ISaleinvoiceQuery isq = (ISaleinvoiceQuery) NCLocator.getInstance().lookup(
						ISaleinvoiceQuery.class.getName());
				Hashtable ht = isq.queryBodyDataByUpsourcebid(strOrderbid);
				ArrayList<IEqualValueDownBill[]> list = new ArrayList<IEqualValueDownBill[]>();
				IEqualValueDownBill[] downbill;
				for (String bid : strOrderbid) {
					if (ht.containsKey(bid)) {
						downbill = new IEqualValueDownBill[((ArrayList) ht.get(bid)).size()];
						((ArrayList) ht.get(bid)).toArray(downbill);
						list.add(downbill);
					}
				}

					nc.vo.transfer.UpToDownEqualValueTool.setValueEqualToUpBillForStore("30", "32", getBTaxPrior()
							.intValue(), (IEqualValueUpBill[]) saleorder.getChildrenVO(), list,
							(IEqualValueDownBill[]) saleinvoice.getChildrenVO());
					// ����β���֮�󣬿��ܵ��еļ�˰�ϼơ�˰���˰���֮��Ĺ�ϵ�����㣬������Ҫ���µ���
					if (getBTaxPrior().intValue() == 0) {
						nc.vo.scm.relacal.SCMRelationsCal.calculate(saleinvoice.getChildrenVO(), saleinvoice
								.getParentVO(), new int[] { RelationsCalVO.TAXPRICE_PRIOR_TO_PRICE,
								RelationsCalVO.PRICE_PRIOR_TO_ITEMDISCOUNTRATE, RelationsCalVO.YES_LOCAL_FRAC },
								"noriginalcursummny", SaleinvoiceBVO.getDescriptions(), SaleinvoiceBVO.getKeysForVO(),
								new int[] { SCMRelationsCal.TAXPRICE_ORIGINAL, SCMRelationsCal.PRICE_ORIGINAL,
										SCMRelationsCal.NET_TAXPRICE_ORIGINAL, SCMRelationsCal.NET_PRICE_ORIGINAL });
					} else {
						nc.vo.scm.relacal.SCMRelationsCal.calculate(saleinvoice.getChildrenVO(), saleinvoice
								.getParentVO(), new int[] { RelationsCalVO.PRICE_PRIOR_TO_TAXPRICE,
								RelationsCalVO.PRICE_PRIOR_TO_ITEMDISCOUNTRATE, RelationsCalVO.YES_LOCAL_FRAC },
								"noriginalcurmny", SaleinvoiceBVO.getDescriptions(), SaleinvoiceBVO.getKeysForVO(),
								new int[] { SCMRelationsCal.TAXPRICE_ORIGINAL, SCMRelationsCal.PRICE_ORIGINAL,
										SCMRelationsCal.NET_TAXPRICE_ORIGINAL, SCMRelationsCal.NET_PRICE_ORIGINAL });
					}
			}

			setNsubsummny(saleinvoice);

		} catch (Exception e) {
			SCMEnv.out(e);
			if(e instanceof BusinessException)
				throw (BusinessException)e;
			else
				throw new BusinessException(e);
		}
		return saleinvoice;
	}

	private void setNsubsummny(AggregatedValueObject retvo) {
		for (CircularlyAccessibleValueObject childrenvo : retvo.getChildrenVO()) {
			childrenvo.setAttributeValue("nsubsummny", childrenvo.getAttributeValue("noriginalcursummny"));
			childrenvo.setAttributeValue("nsubcursummny", childrenvo.getAttributeValue("nsummny"));
		}
	}

	/**
	 * ���ݲ�Ʒ���Լ������󣬰�ԴVO����Ϣͨ���������ת�� ����ת��
	 */
	public AggregatedValueObject[] retChangeBusiVOs(AggregatedValueObject[] preVo,
			AggregatedValueObject[] nowVo) throws BusinessException {
		AggregatedValueObject[] retvos = nowVo;
		for (int i = 0, loop = nowVo == null ? 0 : nowVo.length; i < loop; i++) {
			pk_corp = (String) preVo[i].getParentVO().getAttributeValue("pk_corp");
			retvos[i] = retChangeBusiVO(preVo[i], nowVo[i]);
		}
		return retvos;
	}

	private String pk_corp;
	private Integer bTaxPrior;

	private Integer getBTaxPrior() {
  		if (bTaxPrior == null) {
			try {
				bTaxPrior = SysInitBO_Client.getParaBoolean(pk_corp, "SA02").booleanValue() ? 0 : 1;
			} catch (BusinessException e) {
		  	SCMEnv.out(e);
				return 1;
			}
		}

		return bTaxPrior;
	}
  /**
   * ����������������õ����۱����ʡ�
   * <b>����˵��</b>
   * @param curtypeid
   * @param dbilldate
   * @return
   * @time 2008-12-8 ����07:00:56
   */
  private UFDouble getExchangeRate(String curtypeid, String dbilldate){
   if(hsexchangeRate.containsKey(curtypeid))
      return hsexchangeRate.get(curtypeid);
   UFDouble exchangerate  = null;
   try{
    exchangerate  = new SOCurrencyRateUtil(pk_corp).getExchangeRate(curtypeid, dbilldate);
   }catch(Exception e){
     SCMEnv.out(e);
   }
   hsexchangeRate.put(curtypeid, exchangerate);
   return exchangerate; 
  }
}
