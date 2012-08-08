package nc.impl.ehpta.pub;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;

public final class UAPServQueryBS {
	
	public static final IUAPQueryBS iUAPQueryBS = (IUAPQueryBS) NCLocator.getInstance().lookup(IUAPQueryBS.class);
	
	private UAPServQueryBS() { } 
	
	
}
