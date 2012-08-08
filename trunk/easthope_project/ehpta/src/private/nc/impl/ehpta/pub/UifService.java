package nc.impl.ehpta.pub;

import nc.bs.framework.common.NCLocator;
import nc.itf.uif.pub.IUifService;

public class UifService {
	
	private static IUifService serviceImp = null;
	
	private UifService() { }
	
	public static final IUifService builder() {
		if(serviceImp == null)
			serviceImp = NCLocator.getInstance().lookup(IUifService.class);
		
		return serviceImp;
	}
}
