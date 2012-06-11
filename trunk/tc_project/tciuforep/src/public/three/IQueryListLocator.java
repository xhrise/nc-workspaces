package three;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;

public class IQueryListLocator extends Service
  implements IQueryList
{
													//这里的地址是WEBSERVICE的地址
  private String IQueryListSOAP11port_http_address = "http://localhost:89/axis/QueryList.jws?wsdl";

  private String IQueryListSOAP11port_httpWSDDServiceName = "IQueryListSOAP11port_http";

  private HashSet ports = null;

  public IQueryListLocator()
  {
	  super();
  }

  public IQueryListLocator(EngineConfiguration config)
  {
    super(config);
  }

  public IQueryListLocator(String wsdlLoc, QName sName) throws ServiceException {
    super(wsdlLoc, sName);
  }

  public String getIQueryListSOAP11port_httpAddress()
  {
    return this.IQueryListSOAP11port_http_address;
  }

  public String getIQueryListSOAP11port_httpWSDDServiceName()
  {
    return this.IQueryListSOAP11port_httpWSDDServiceName;
  }

  public void setIQueryListSOAP11port_httpWSDDServiceName(String name) {
    this.IQueryListSOAP11port_httpWSDDServiceName = name;
  }

  public IQueryListPortType getIQueryListSOAP11port_http() throws ServiceException {
    URL endpoint;
    try {
      endpoint = new URL(this.IQueryListSOAP11port_http_address);
    }
    catch (MalformedURLException e) {
      throw new ServiceException(e);
    }
    return getIQueryListSOAP11port_http(endpoint);
  }

  public IQueryListPortType getIQueryListSOAP11port_http(URL portAddress) throws ServiceException {
    try {
      IQueryListSOAP11BindingStub _stub = new IQueryListSOAP11BindingStub(portAddress, this);
      _stub.setPortName(getIQueryListSOAP11port_httpWSDDServiceName());
      return _stub;
    } catch (AxisFault e) {
    }
    return null;
  }

  public void setIQueryListSOAP11port_httpEndpointAddress(String address)
  {
    this.IQueryListSOAP11port_http_address = address;
  }

  public Remote getPort(Class serviceEndpointInterface)
    throws ServiceException
  {
    try
    {
      if (IQueryListPortType.class.isAssignableFrom(serviceEndpointInterface)) {
        IQueryListSOAP11BindingStub _stub = new IQueryListSOAP11BindingStub(new URL(this.IQueryListSOAP11port_http_address), this);
        _stub.setPortName(getIQueryListSOAP11port_httpWSDDServiceName());
        return _stub;
      }
    }
    catch (Throwable t) {
      throw new ServiceException(t);
    }
    throw new ServiceException("There is no stub implementation for the interface:  " + ((serviceEndpointInterface == null) ? "null" : serviceEndpointInterface.getName()));
  }

  public Remote getPort(QName portName, Class serviceEndpointInterface)
    throws ServiceException
  {
    if (portName == null) {
      return getPort(serviceEndpointInterface);
    }
    String inputPortName = portName.getLocalPart();
    if ("IQueryListSOAP11port_http".equals(inputPortName)) {
      return getIQueryListSOAP11port_http();
    }

    Remote _stub = getPort(serviceEndpointInterface);
    ((Stub)_stub).setPortName(portName);
    return _stub;
  }

  public QName getServiceName()
  {
    return new QName("http://imp.tc.itf.nc/IQueryList", "IQueryList");
  }

  public Iterator getPorts()
  {
    if (this.ports == null) {
      this.ports = new HashSet();
      this.ports.add(new QName("http://imp.tc.itf.nc/IQueryList", "IQueryListSOAP11port_http"));
    }
    return this.ports.iterator();
  }

  public void setEndpointAddress(String portName, String address)
    throws ServiceException
  {
    if ("IQueryListSOAP11port_http".equals(portName)) {
      setIQueryListSOAP11port_httpEndpointAddress(address);
    }
    else
    {
      throw new ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
    }
  }

  public void setEndpointAddress(QName portName, String address)
    throws ServiceException
  {
    setEndpointAddress(portName.getLocalPart(), address);
  }
}