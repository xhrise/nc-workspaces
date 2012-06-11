package three;

import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface IQueryListPortType extends Remote
{
  public abstract String importXSLRep(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws RemoteException;

  public abstract String testText(String paramString)
    throws RemoteException;
}