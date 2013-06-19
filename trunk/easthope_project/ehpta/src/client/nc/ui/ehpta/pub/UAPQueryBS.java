package nc.ui.ehpta.pub;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;

public final class UAPQueryBS {
	
	private static final IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
	
	private UAPQueryBS() { } 
	
	public static final IUAPQueryBS getInstance() {
		return iUAPQueryBS;
	}
	
	
}