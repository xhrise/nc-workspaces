
/*
 * 
 */

package nc.itf.hr.hsmodify;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.2.3
 * Mon Oct 26 13:46:18 CST 2009
 * Generated source version: 2.2.3
 * 
 */


@WebServiceClient(name = "PersonModifySVStub", 
                  wsdlLocation = "http://u9waj.u9.ufsoft.com/Portal/services/UFIDA.U9.CBO.HR.CBOHRSV.IPersonModifySV.svc?wsdl",
                  targetNamespace = "http://tempuri.org/") 
public class PersonModifySVStub extends Service {

    public final static URL WSDL_LOCATION;
    public final static QName SERVICE = new QName("http://tempuri.org/", "PersonModifySVStub");
    public final static QName BasicHttpBindingUFIDAU9CBOHRCBOHRSVIPersonModifySV = new QName("http://tempuri.org/", "BasicHttpBinding_UFIDA.U9.CBO.HR.CBOHRSV.IPersonModifySV");
    static {
        URL url = null;
        try {
            url = new URL("http://u9waj.u9.ufsoft.com/Portal/services/UFIDA.U9.CBO.HR.CBOHRSV.IPersonModifySV.svc?wsdl");
        } catch (MalformedURLException e) {
            System.err.println("Can not initialize the default wsdl from http://u9waj.u9.ufsoft.com/Portal/services/UFIDA.U9.CBO.HR.CBOHRSV.IPersonModifySV.svc?wsdl");
            // e.printStackTrace();
        }
        WSDL_LOCATION = url;
    }

    public PersonModifySVStub(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public PersonModifySVStub(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public PersonModifySVStub() {
        super(WSDL_LOCATION, SERVICE);
    }

    /**
     * 
     * @return
     *     returns UFIDAU9CBOHRCBOHRSVIPersonModifySV
     */
    @WebEndpoint(name = "BasicHttpBinding_UFIDA.U9.CBO.HR.CBOHRSV.IPersonModifySV")
    public UFIDAU9CBOHRCBOHRSVIPersonModifySV getBasicHttpBindingUFIDAU9CBOHRCBOHRSVIPersonModifySV() {
        return super.getPort(BasicHttpBindingUFIDAU9CBOHRCBOHRSVIPersonModifySV, UFIDAU9CBOHRCBOHRSVIPersonModifySV.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns UFIDAU9CBOHRCBOHRSVIPersonModifySV
     */
    @WebEndpoint(name = "BasicHttpBinding_UFIDA.U9.CBO.HR.CBOHRSV.IPersonModifySV")
    public UFIDAU9CBOHRCBOHRSVIPersonModifySV getBasicHttpBindingUFIDAU9CBOHRCBOHRSVIPersonModifySV(WebServiceFeature... features) {
        return super.getPort(BasicHttpBindingUFIDAU9CBOHRCBOHRSVIPersonModifySV, UFIDAU9CBOHRCBOHRSVIPersonModifySV.class, features);
    }

}
