package nc.bs.ehpta.dmo;

import nc.bs.pub.pf.IQueryData;
import nc.impl.ehpta.pub.UifService;
import nc.vo.ehpta.hq010401.SaleContractVO;
import nc.vo.ehpta.hq010402.AidcustVO;
import nc.vo.ehpta.hq010402.MultiBillVO;
import nc.vo.ehpta.hq010402.SaleContractBVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;

/**
 * 
 * @author river
 * 
 * Create Date : 2012-07-01
 *
 * 功能： PTA销售 长单合同的下游单据查询DMO
 * 
 */
public class SaleContractDMO implements IQueryData {

	public CircularlyAccessibleValueObject[] queryAllBodyData(String key)
			throws BusinessException {
		
		AggregatedValueObject aggVO = UifService.builder().queryBillVOByPrimaryKey(new String[] { 
				MultiBillVO.class.getName(),
				SaleContractVO.class.getName(),
				SaleContractBVO.class.getName(),
				AidcustVO.class.getName(),
//				PrepolicyVO.class.getName() 
		}, key);
		
		if(aggVO != null && aggVO.getParentVO() != null) {
			return ((MultiBillVO)aggVO).getTableVO(((MultiBillVO)aggVO).getDefaultTableCode());
		}
		
		return null;
	}

	public CircularlyAccessibleValueObject[] queryAllHeadData(String whereString)
			throws BusinessException {
		
		whereString += " and vbillstatus = 1 and pk_contract not in (select pk_contract from so_sale where pk_contract is not null and nvl(dr,0)=0 and close_flag = 'Y' ) order by dmakedate desc ";
		
		SuperVO[] superVOs =  UifService.builder().queryByCondition(SaleContractVO.class, whereString);
		
		return superVOs;
		
	}



}
