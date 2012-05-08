package nc.impl.mbSyn;

import java.rmi.RemoteException;

import nc.bs.framework.codesync.client.NCClassLoader;
import nc.itf.mbSyn.IServiceUtil;

import com.seeyon.client.AccountServiceStub;
import com.seeyon.client.AuthorityServiceStub;
import com.seeyon.client.ServiceException;
import com.ufida.iufo.pub.tools.AppDebug;

public class ServiceUtilImpl implements IServiceUtil{
	public static String getToken() throws RemoteException {
		AuthorityServiceStub.Authenticate req = new AuthorityServiceStub.Authenticate();
		req.setUserName("service-admin");
		req.setPassword("123456");

		AuthorityServiceStub stub = new AuthorityServiceStub();
		AuthorityServiceStub.AuthenticateResponse resp = null;
		try {
			resp = stub.authenticate(req);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		AuthorityServiceStub.UserToken token = resp.get_return();
		
		stub._getServiceClient().cleanupTransport();
		stub._getServiceClient().cleanup();
		stub.cleanup();
		stub = null;
		
		
		
		return token.getId();
	}
	
	public long getAccountId(String accountName) throws RemoteException {
		
		AccountServiceStub stub = new AccountServiceStub();
		AccountServiceStub.GetAccountId req = new AccountServiceStub.GetAccountId();
		req.setAccountName(accountName);

		AccountServiceStub.GetAccountIdResponse resp = stub.getAccountId(req);
		AccountServiceStub.ServiceResponse r = resp.get_return();
		
		stub._getServiceClient().cleanupTransport();
		stub._getServiceClient().cleanup();
		stub.cleanup();
		stub = null;
		
		return r.getResult();
	}	
}
