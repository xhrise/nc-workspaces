package nc.ui.eh.cw.h13006;

import java.awt.Container;
import nc.ui.trade.businessaction.IPFACTION;
import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.checkrule.ICheckRule;
import nc.vo.trade.checkrule.ICheckRules;
import nc.vo.trade.checkrule.ICheckRules2;
import nc.vo.trade.checkrule.ICompareRule;
import nc.vo.trade.checkrule.ISpecialChecker;
import nc.vo.trade.checkrule.IUniqueRule;
import nc.vo.trade.checkrule.IUniqueRules;
import nc.vo.trade.checkrule.UniqueRule;
import nc.vo.trade.checkrule.VOChecker;

/*
 * ���ܣ��ɱ����÷�̯
 * ���ߣ�zqy
 * ʱ�䣺2008-9-10 10:00:00
 */

public class ClientUICheckRule extends BeforeActionCHK  implements ICheckRules,IUniqueRules,ICheckRules2{

	/**
	 * ���ر�ͷΨһ���򣬽����ں�̨��顣
	 */
	public IUniqueRule[] getHeadUniqueRules() {
		return null;
	}

	/**
	 * ���ر���Ψһ���򣬽�����ǰ̨��顣
	 */
	public IUniqueRule[] getItemUniqueRules(String tablecode) {	
		UniqueRule rule = new UniqueRule("�����������ظ��������������",new String[]{"pk_invbasdoc"});
		UniqueRule[] rules = new UniqueRule[1];
		rules[0]=rule;
		return rules;
	}
	
	/**
	 * �����������ࡣ ���VOChecker��������Ҫ�󣬿����ô�������顣
	 */
	public ISpecialChecker getSpecialChecker() {
		return null;
	}
	
	/**
	 * �Ƿ��������Ϊ��
	 */
	public boolean isAllowEmptyBody(String tablecode) {
			return false;
	}
	
	public ICheckRule[] getHeadCheckRules() {
		return null;
	}

	public ICheckRule[] getItemCheckRules(String tablecode) {
return null;
	}

	public ICompareRule[] getHeadCompareRules() {
		return null;
	}

	public String[] getHeadIntegerField() {
		return null;
	}

	public String[] getHeadUFDoubleField() {
		return null;
	}

	public ICompareRule[] getItemCompareRules(String tablecode) {
		return null;
	}

	public String[] getItemIntegerField(String tablecode) {
		return null;
	}

	public String[] getItemUFDoubleField(String tablecode) {
		return null;
	}
	
	public void runBatchClass(Container parent, String billType, String actionName, AggregatedValueObject[] vos, Object[] obj) throws Exception {
	}


	public void runClass(Container parent, String billType, String actionName, AggregatedValueObject vo, Object obj) throws Exception {
		if(actionName.equals(IPFACTION.SAVE))
		{	
			if(!VOChecker.check(vo,this ))
				throw new nc.vo.pub.BusinessException(VOChecker.getErrorMessage());
		}
		
	}


}
