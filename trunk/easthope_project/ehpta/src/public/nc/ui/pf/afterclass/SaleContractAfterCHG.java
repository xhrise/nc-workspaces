package nc.ui.pf.afterclass;

import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.ehpta.hq010401.SaleContractVO;
import nc.vo.ehpta.hq010402.AidcustVO;
import nc.vo.ehpta.hq010402.MultiBillVO;
import nc.vo.ehpta.hq010402.PrepolicyVO;
import nc.vo.ehpta.hq010402.SaleContractBVO;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.so.so001.SaleorderBVO;

public class SaleContractAfterCHG implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVO,
			AggregatedValueObject nowVO) throws BusinessException {
		
		if(nowVO != null) 
			nowVO.getParentVO().setAttributeValue("version", preVO.getParentVO().getAttributeValue("version") == null ? null : preVO.getParentVO().getAttributeValue("version") + ".0");
		
		// 多表体无法获取表体数据，这里直接进行赋值.
		setMultiBody(preVO , nowVO);
		
		return nowVO;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVOs, AggregatedValueObject[] nowVOs)
			throws BusinessException {
		
		if(nowVOs != null && nowVOs.length > 0) {
			for(int i = 0 , j = nowVOs.length ; i < j ; i ++) {
				retChangeBusiVO(preVOs[i], nowVOs[i]);
			}
		}
		
		return nowVOs;
	}
	
	private final AggregatedValueObject setMultiBody(AggregatedValueObject preVO, AggregatedValueObject nowVO) throws BusinessException {
		
		// 多表体无法获取表体数据，这里直接进行赋值.
		if(preVO instanceof MultiBillVO && Integer.valueOf(nowVO.getParentVO().getAttributeValue("contracttype").toString()) == 20) {
			if(preVO.getChildrenVO() == null || preVO.getChildrenVO().length == 0) {
				
				AggregatedValueObject aggVO = HYPubBO_Client.queryBillVOByPrimaryKey(new String[] { MultiBillVO.class.getName() , SaleContractVO.class.getName(), SaleContractBVO.class.getName(), AidcustVO.class.getName(), PrepolicyVO.class.getName() }, preVO.getParentVO().getPrimaryKey());
				
				if(aggVO != null && aggVO.getParentVO() != null) {
					CircularlyAccessibleValueObject[] cavos = ((MultiBillVO)aggVO).getTableVO(((MultiBillVO)aggVO).getDefaultTableCode());
					
					if(cavos != null && cavos.length > 0) {
						SaleorderBVO[] bvos = new SaleorderBVO[cavos.length];
						int i = 0;
						for(CircularlyAccessibleValueObject cavo : cavos) {
							SaleorderBVO bvo = new SaleorderBVO();
							bvo.setAttributeValue("creceipttype", "HQ06");
							bvo.setAttributeValue("ndiscountmny", 0);
							bvo.setAttributeValue("ndiscountrate", 100);
							bvo.setAttributeValue("nitemdiscountrate", 100);
							bvo.setAttributeValue("noriginalcurdiscountmny", 0);
							bvo.setAttributeValue("ccurrencytypeid", "00010000000000000001");
							bvo.setAttributeValue("nexchangeotobrate", 1);
							
							bvo.setAttributeValue("cinvbasdocid", cavo.getAttributeValue("pk_invbasdoc"));
							bvo.setAttributeValue("cinventoryid", cavo.getAttributeValue("pk_invbasdoc"));
							bvo.setAttributeValue("crowno", cavo.getAttributeValue("def1"));
							bvo.setAttributeValue("nnumber", cavo.getAttributeValue("num"));
							
							UFDouble num = (UFDouble) cavo.getAttributeValue("num");
							Object invspec = cavo.getAttributeValue("invspec");
							Integer numof = 0;
							try{ numof = num.div(Double.valueOf(invspec.toString())).intValue(); } catch(Exception e) {}
							
							bvo.setAttributeValue("numof", numof);
							bvo.setAttributeValue("nquoteunitnum", cavo.getAttributeValue("num"));
							bvo.setAttributeValue("ntaxrate", cavo.getAttributeValue("taxrate"));
							bvo.setAttributeValue("pk_corp", cavo.getAttributeValue("pk_corp"));
							bvo.setAttributeValue("csourcebillbodyid", cavo.getAttributeValue("pk_contract_b"));
							bvo.setAttributeValue("csourcebillid", cavo.getAttributeValue("pk_contract"));
							
							bvos[i] = bvo;
							i ++ ;
						}
						
						nowVO.setChildrenVO(bvos);
					}
				}
			}
		}
		
		return nowVO;
	}

}
