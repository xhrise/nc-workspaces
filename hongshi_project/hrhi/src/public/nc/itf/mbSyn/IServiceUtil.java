package nc.itf.mbSyn;

import java.rmi.RemoteException;

public interface IServiceUtil {
	public long getAccountId(String accountName) throws RemoteException;
}
