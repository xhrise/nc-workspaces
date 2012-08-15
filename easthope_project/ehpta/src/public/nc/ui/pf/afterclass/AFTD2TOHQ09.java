package nc.ui.pf.afterclass;

import java.util.Vector;

import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.ehpta.pub.UAPQueryBS;
import nc.vo.pf.change.IchangeVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public class AFTD2TOHQ09 implements IchangeVO {

	public AggregatedValueObject retChangeBusiVO(AggregatedValueObject preVo,
			AggregatedValueObject nowVo) throws BusinessException {
		
//		"H_djzbid->H_djbh",
//		"H_contno->H_contno",
//		"H_version->H_version",
//		"H_transtype->H_transtype",
//		"H_custname->B_hbbm",
//		"H_redate->H_djrq",
//		"H_remny->H_bbje",
//		"H_billtype->B_notetype",
		
		Object pk_contract = preVo.getParentVO().getAttributeValue("zyx6");
		
		Vector retVector = (Vector) UAPQueryBS.iUAPQueryBS.executeQuery("select vbillno , version , case when contype = '长单合同' then '20' when contype = '现货合同' then '10' else '' end contype from ehpta_sale_contract where pk_contract = '"+pk_contract+"'", new VectorProcessor());
		
		if(retVector != null && retVector.size() > 0) {
			nowVo.getParentVO().setAttributeValue("contno", ((Vector)retVector.get(0)).get(0));
			nowVo.getParentVO().setAttributeValue("version", ((Vector)retVector.get(0)).get(1));
			nowVo.getParentVO().setAttributeValue("transtype", ((Vector)retVector.get(0)).get(2));
		}
		
		nowVo.getParentVO().setAttributeValue("djzbid", preVo.getParentVO().getAttributeValue("djbh"));
		if(preVo.getChildrenVO() != null && preVo.getChildrenVO().length > 0)
			nowVo.getParentVO().setAttributeValue("hbbm", preVo.getChildrenVO()[0].getAttributeValue("hbbm"));
		
		nowVo.getParentVO().setAttributeValue("djrq", preVo.getParentVO().getAttributeValue("djrq"));
		nowVo.getParentVO().setAttributeValue("bbje", preVo.getParentVO().getAttributeValue("bbje"));
		nowVo.getParentVO().setAttributeValue("notetype", preVo.getParentVO().getAttributeValue("notetype"));
		nowVo.getParentVO().setAttributeValue("vbillstatus", 8);
		nowVo.getParentVO().setAttributeValue("pk_receivable", preVo.getParentVO().getAttributeValue("vouchid"));
		nowVo.getParentVO().setAttributeValue("def1", pk_contract);
		nowVo.getParentVO().setAttributeValue("redate", preVo.getParentVO().getAttributeValue("djrq"));
		nowVo.getParentVO().setAttributeValue("remny", preVo.getParentVO().getAttributeValue("bbje"));
		
		return nowVo;
	}

	public AggregatedValueObject[] retChangeBusiVOs(
			AggregatedValueObject[] preVOs, AggregatedValueObject[] nowVOs)
			throws BusinessException {

		if(nowVOs.length > 1)
			throw new BusinessException("只能参照一条收款单记录！");
		
		AggregatedValueObject[] retVOs = nowVOs;
		if(nowVOs != null && nowVOs.length > 0) {
			for(int i = 0 , j = nowVOs.length ; i < j ; i ++) {
				retVOs[i] = retChangeBusiVO(preVOs[i], nowVOs[i]);
			}
		}
		
		return retVOs;
		
	}

}
