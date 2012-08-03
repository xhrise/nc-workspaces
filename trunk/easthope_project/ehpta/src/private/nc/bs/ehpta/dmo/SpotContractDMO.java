package nc.bs.ehpta.dmo;

import nc.bs.pub.pf.IQueryData;
import nc.impl.ehpta.pub.UifService;
import nc.ui.trade.business.HYPubBO_Client;
import nc.vo.ehpta.hq010401.SaleContractBsVO;
import nc.vo.ehpta.hq010401.SaleContractVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
 * 
 * @author river
 * 
 * Create Date : 2012-07-01
 *
 * 功能： PTA销售 现货合同的下游单据查询DMO
 * 
 */
public class SpotContractDMO implements IQueryData {
	
	public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
			throws BusinessException {
		
		return UifService.builder().queryAllBodyData("HQ04", SaleContractBsVO.class, key, " nvl(dr , 0 ) = 0 ");
		
	}

	public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString)
			throws BusinessException {
		
		whereString += " and vbillstatus = 1 and pk_contract not in (select pk_contract from so_sale where pk_contract is not null and nvl(dr,0)=0) order by dmakedate desc "; 
		
		SuperVO[] superVOs =  UifService.builder().queryByCondition(SaleContractVO.class, whereString);
		
		return superVOs;
		
	}



}
