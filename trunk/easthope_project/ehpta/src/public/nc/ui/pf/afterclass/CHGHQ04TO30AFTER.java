package nc.ui.pf.afterclass;

import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public class CHGHQ04TO30AFTER implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVO,
			AggregatedValueObject nowVO) throws BusinessException {
		
		System.out.println(nowVO);
		
		return nowVO;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVOs, AggregatedValueObject[] nowVOs)
			throws BusinessException {
		
		if(nowVOs != null && nowVOs.length > 0) {
			for(String attr : nowVOs[0].getParentVO().getAttributeNames()) {
				System.out.println(attr + " - " + nowVOs[0].getParentVO().getAttributeValue(attr));
			}
		}
		
		return nowVOs;
	}

}
