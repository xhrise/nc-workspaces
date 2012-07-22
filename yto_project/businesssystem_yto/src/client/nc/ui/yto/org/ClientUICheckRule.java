package nc.ui.yto.org;

import java.awt.Container;

import nc.ui.trade.check.BeforeActionCHK;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.checkrule.ICheckRule;
import nc.vo.trade.checkrule.ICheckRules;
import nc.vo.trade.checkrule.ICheckRules2;
import nc.vo.trade.checkrule.ICompareRule;
import nc.vo.trade.checkrule.ISpecialChecker;
import nc.vo.trade.checkrule.IUniqueRule;
import nc.vo.trade.checkrule.IUniqueRules;

public class ClientUICheckRule extends BeforeActionCHK  implements ICheckRules,IUniqueRules,ICheckRules2{

	public void runBatchClass(Container arg0, String arg1, String arg2,
			AggregatedValueObject[] arg3, Object[] arg4) throws Exception {
		
	}

	public void runClass(Container arg0, String arg1, String arg2,
			AggregatedValueObject arg3, Object arg4) throws Exception {
		
	}

	public ISpecialChecker getSpecialChecker() {
		return null;
	}

	public boolean isAllowEmptyBody(String s) {
		return false;
	}

	public IUniqueRule[] getHeadUniqueRules() {
		return null;
	}

	public IUniqueRule[] getItemUniqueRules(String s) {
		return null;
	}

	public ICheckRule[] getHeadCheckRules() {
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

	public ICheckRule[] getItemCheckRules(String s) {
		return null;
	}

	public ICompareRule[] getItemCompareRules(String s) {
		return null;
	}

	public String[] getItemIntegerField(String s) {
		return null;
	}

	public String[] getItemUFDoubleField(String s) {
		return null;
	}

}
