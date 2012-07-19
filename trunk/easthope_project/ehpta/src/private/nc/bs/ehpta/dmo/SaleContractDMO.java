package nc.bs.ehpta.dmo;

import nc.bs.pub.pf.IQueryData;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.ehpta.hq010401.SaleContractVO;
import nc.vo.ehpta.hq010402.AidcustVO;
import nc.vo.ehpta.hq010402.MultiBillVO;
import nc.vo.ehpta.hq010402.PrepolicyVO;
import nc.vo.ehpta.hq010402.SaleContractBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;


public class SaleContractDMO implements IQueryData {

	public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
			throws BusinessException {
		
		AggregatedValueObject aggVO = HYPubBO_Client.queryBillVOByPrimaryKey(new String[] { 
				MultiBillVO.class.getName(),
				SaleContractVO.class.getName(),
				SaleContractBVO.class.getName(),
				AidcustVO.class.getName(),
				PrepolicyVO.class.getName() }, key);
		
		if(aggVO != null && aggVO.getParentVO() != null) {
			return ((MultiBillVO)aggVO).getTableVO(((MultiBillVO)aggVO).getDefaultTableCode());
		}
		
		return null;
	}

	public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString)
			throws BusinessException {
		
		whereString += " and vbillstatus = 1 and pk_contract not in (select nvl(pk_contract , 'null') from so_sale where nvl(contracttype , 20) = 20 and nvl(dr ,0) = 0 )";
		
		SuperVO[] superVOs =  HYPubBO_Client.queryByCondition(SaleContractVO.class, whereString);
		
		return superVOs;
		
	}



}
