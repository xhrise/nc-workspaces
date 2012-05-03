package nc.itf.hi;

import java.io.FileNotFoundException;
import java.io.IOException;

import nc.vo.pub.BusinessException;

public interface IHIFileTrans {
	public final static int TRANS_PORT = 2718;
	/**
	 * �ϴ��ĵ�(�ɴ�����ļ�����)
	 * @param remoteAbsPath
	 * @throws java.rmi.RemoteException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public abstract java.net.InetSocketAddress upload(String remoteAbsPath)
			throws BusinessException;
			//throws java.rmi.RemoteException, FileNotFoundException, IOException;

}