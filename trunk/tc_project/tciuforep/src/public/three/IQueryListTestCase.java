package three;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;
import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

public class IQueryListTestCase extends TestCase
{
  public IQueryListTestCase(String name)
  {
    super(name);
  }

  public void testIQueryListSOAP11port_httpWSDL() throws Exception {
    ServiceFactory serviceFactory = ServiceFactory.newInstance();
    URL url = new URL(new IQueryListLocator().getIQueryListSOAP11port_httpAddress() + "?WSDL");
    Service service = serviceFactory.createService(url, new IQueryListLocator().getServiceName());
    assertTrue(service != null);
  }

  public void test1IQueryListSOAP11port_httpImportXMLRep() throws Exception {
    IQueryListSOAP11BindingStub binding;
    try {
      binding = (IQueryListSOAP11BindingStub)
        new IQueryListLocator().getIQueryListSOAP11port_http();
    }
    catch (ServiceException jre) {
      if (jre.getLinkedCause() != null)
        jre.getLinkedCause().printStackTrace();
      throw new AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
    }
    assertNotNull("binding is null", binding);

    binding.setTimeout(60000);

    String value = null;
    value = binding.importXSLRep(new String(), new String(), new String(), new String(), new String());
  }

  public void test2IQueryListSOAP11port_httpTestText() throws Exception
  {
    IQueryListSOAP11BindingStub binding;
    try {
      binding = (IQueryListSOAP11BindingStub)
        new IQueryListLocator().getIQueryListSOAP11port_http();
    }
    catch (ServiceException jre) {
      if (jre.getLinkedCause() != null)
        jre.getLinkedCause().printStackTrace();
      throw new AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
    }
    assertNotNull("binding is null", binding);

    binding.setTimeout(60000);

    String value = null;
    value = binding.testText(new String());
  }
}