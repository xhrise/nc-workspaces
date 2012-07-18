package nc.bs.ehpta.dmo;

import nc.bs.pub.pf.IQueryData;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.ehpta.hq010401.SaleContractBsVO;
import nc.vo.ehpta.hq010401.SaleContractVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;


public class SpotContractDMO implements IQueryData {
	
	public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
			throws BusinessException {
		
		return HYPubBO_Client.queryAllBodyData("HQ04", SaleContractBsVO.class, key, " nvl(dr , 0 ) = 0 ");
		
	}

	public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString)
			throws BusinessException {
		
		
		whereString += " and vbillstatus = 1 and pk_contract not in (select nvl(pk_contract , 'null') from so_sale where nvl(contracttype , 10) = 10 and nvl(dr ,0) = 0 ) ";
		
		SuperVO[] superVOs =  HYPubBO_Client.queryByCondition(SaleContractVO.class, whereString);
		
		return superVOs;
		
	}



}
