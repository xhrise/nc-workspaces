package three;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public abstract interface IQueryList extends Service
{
  public abstract String getIQueryListSOAP11port_httpAddress();

  public abstract IQueryListPortType getIQueryListSOAP11port_http()
    throws ServiceException;

  public abstract IQueryListPortType getIQueryListSOAP11port_http(URL paramURL)
    throws ServiceException;
}