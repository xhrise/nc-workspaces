package nc.bs.pub.action;

import nc.vo.pub.pf.PfUtilActionVO;
import nc.bs.pub.compiler.*;
import nc.vo.pub.compiler.PfParameterVO;
import java.math.*;
import java.util.*;
import nc.vo.pub.lang.*;
import nc.bs.pub.pf.PfUtilTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.uap.pf.PFBusinessException;
/**
 * 备注：贴息计算的保存
单据动作执行中的动态执行类的动态执行类。
 *
 * 创建日期：(2012-8-20)
 * @author 平台脚本生成
 */
public class N_HQ09_WRITE extends AbstractCompiler2 {
private java.util.Hashtable m_methodReturnHas=new java.util.Hashtable();
private Hashtable m_keyHas=null;
/**
 * N_HQ09_WRITE 构造子注解。
 */
public N_HQ09_WRITE() {
	super();
}
/*
* 备注：平台编写规则类
* 接口执行类
*/
public Object runComClass(PfParameterVO vo) throws BusinessException {
try{
	super.m_tmpVo=vo;
	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####
	Object retObj  =null;
	//####重要说明：生成的业务组件方法尽量不要进行修改####
	//方法说明:公共保存方法
	// 生成单据号
	nc.bs.pub.billcodemanage.BillcodeGenerater gene  =
	new nc.bs.pub.billcodemanage.BillcodeGenerater ();
	if ( nc.vo.jcom.lang.StringUtil.isEmpty ( ( (String)vo.m_preValueVo.getParentVO ().getAttributeValue ( "vbillno")))) {
		String billno  = gene.getBillCode (vo.m_billType,vo.m_coId,null,null);
		vo.m_preValueVo.getParentVO ().setAttributeValue ( "vbillno",billno);
	}
	retObj  =runClass( "nc.bs.trade.comsave.BillSave", "saveBill", "nc.vo.pub.AggregatedValueObject:01",vo,m_keyHas,m_methodReturnHas);
	//#################################################
	return retObj;
} catch (Exception ex) {
	if (ex instanceof BusinessException)
		throw (BusinessException) ex;
	else 
    throw new PFBusinessException(ex.getMessage(), ex);
}
}
/*
* 备注：平台编写原始脚本
*/
public String getCodeRemark(){
	return "	//####本脚本必须含有返回值,返回DLG和PNL的组件不允许有返回值####\n	Object retObj  =null;\n	//####重要说明：生成的业务组件方法尽量不要进行修改####\n	//方法说明:公共保存方法\n	// 生成单据号\n	nc.bs.pub.billcodemanage.BillcodeGenerater gene  =\n	new nc.bs.pub.billcodemanage.BillcodeGenerater ();\n	if ( nc.vo.jcom.lang.StringUtil.isEmpty ( ( (String)vo.m_preValueVo.getParentVO ().getAttributeValue ( \"vbillno\")))) {\n		String billno  = gene.getBillCode (vo.m_billType,vo.m_coId,null,null);\n		vo.m_preValueVo.getParentVO ().setAttributeValue ( \"vbillno\",billno);\n	}\n	retObj  =runClassCom@ \"nc.bs.trade.comsave.BillSave\", \"saveBill\", \"nc.vo.pub.AggregatedValueObject:01\"@;\n	//#################################################\n	return retObj;\n";}
/*
* 备注：设置脚本变量的HAS
*/
private void setParameter(String key,Object val)	{
	if (m_keyHas==null){
		m_keyHas=new Hashtable();
	}
	if (val!=null)	{
		m_keyHas.put(key,val);
	}
}
}
