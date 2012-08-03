package nc.impl.ehpta.pub;

import nc.impl.uif.pub.UifServiceImp;

public class UifService {
	
	private static UifServiceImp serviceImp = null;
	
	private UifService() { }
	
	public static final UifServiceImp builder() {
		if(serviceImp == null)
			serviceImp = new UifServiceImp();
		
		return serviceImp;
	}
}
