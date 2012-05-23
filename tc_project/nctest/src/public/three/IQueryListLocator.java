package three;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.rmi.Remote;
import java.util.HashSet;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nc.vo.jcom.xml.XMLUtil;

import org.apache.axis.AxisFault;
import org.apache.axis.EngineConfiguration;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;
import org.xml.sax.SAXException;

import com.ufida.web.util.WebGlobalValue;

public class IQueryListLocator extends Service
  implements IQueryList
{
	
													//这里的地址是WEBSERVICE的地址 http://10.15.6.174
  private String IQueryListSOAP11port_http_address = "/uapws/service/tcService";

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
    	String ncFileName = "Ufida_IUFO/service56ip";
    	String xmlfile = "Ufida_IUFO/service56ip/ip.xml";
    	org.w3c.dom.NodeList ip = null;
    	org.w3c.dom.Document document = null;
    	java.io.File xmlList=new java.io.File(xmlfile);
    	
        File myFilePath = new File(ncFileName);
        if (!(myFilePath.exists())) {
          myFilePath.mkdirs();
        }
        
		//如果xmlList.xml不存在则创建
		Boolean bl=xmlList.exists();
		if(!bl){
			
			try {
				document = XMLUtil.newDocument();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			org.w3c.dom.Element element_ip=document.createElement("ip");
			element_ip.setTextContent("在此处设置WEBSERVICE服务器的IP地址，例如:127.0.0.1:80");
			document.appendChild(element_ip);
			this.toSave(document, xmlList.getPath());
		}else{
			try {
				document =XMLUtil.getDocumentBuilder().parse(xmlList);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			ip = document.getElementsByTagName("ip");
			ip.item(0).getTextContent();
		}
    	this.IQueryListSOAP11port_http_address = "http://" + ip.item(0).getTextContent() + this.IQueryListSOAP11port_http_address;
    	System.out.println("WEBSERVICE的地址：" + this.IQueryListSOAP11port_http_address);
    	endpoint = new URL(this.IQueryListSOAP11port_http_address);
    }
    catch (MalformedURLException e) {
      throw new ServiceException(e);
    }
    return getIQueryListSOAP11port_http(endpoint);
  }
  
  public void toSave(org.w3c.dom.Document document,String filename) {
	  try {
	   TransformerFactory tf = TransformerFactory.newInstance();
	   Transformer transformer = tf.newTransformer();
	   DOMSource source = new DOMSource(document);
	   transformer.setOutputProperty(OutputKeys.ENCODING, "GB2312");
	   transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	   PrintWriter pw = new PrintWriter(new FileOutputStream(filename));
	   StreamResult result = new StreamResult(pw);
	   transformer.transform(source, result);
	  } catch (TransformerException mye) {
	   mye.printStackTrace();
	  } catch (IOException exp) {
	   exp.printStackTrace();
	  }
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