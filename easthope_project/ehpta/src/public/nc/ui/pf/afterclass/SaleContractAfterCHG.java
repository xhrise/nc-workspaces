package nc.ui.pf.afterclass;

import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public class SaleContractAfterCHG implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVO,
			AggregatedValueObject nowVO) throws BusinessException {
		
		if(nowVO != null) 
			nowVO.getParentVO().setAttributeValue("version", preVO.getParentVO().getAttributeValue("version") == null ? null : preVO.getParentVO().getAttributeValue("version") + ".0");
		
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

}
