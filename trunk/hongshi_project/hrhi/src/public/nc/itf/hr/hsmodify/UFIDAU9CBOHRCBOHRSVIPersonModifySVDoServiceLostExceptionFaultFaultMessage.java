
package nc.itf.hr.hsmodify;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.3
 * Mon Oct 26 13:46:18 CST 2009
 * Generated source version: 2.2.3
 * 
 */

@WebFault(name = "ServiceLostException", targetNamespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Service")
public class UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceLostExceptionFaultFaultMessage extends java.lang.Exception {
    public static final long serialVersionUID = 20091026134618L;
    
    private nc.itf.hr.hsmodify.ServiceLostException serviceLostException;

    public UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceLostExceptionFaultFaultMessage() {
        super();
    }
    
    public UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceLostExceptionFaultFaultMessage(String message) {
        super(message);
    }
    
    public UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceLostExceptionFaultFaultMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceLostExceptionFaultFaultMessage(String message, nc.itf.hr.hsmodify.ServiceLostException serviceLostException) {
        super(message);
        this.serviceLostException = serviceLostException;
    }

    public UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceLostExceptionFaultFaultMessage(String message, nc.itf.hr.hsmodify.ServiceLostException serviceLostException, Throwable cause) {
        super(message, cause);
        this.serviceLostException = serviceLostException;
    }

    public nc.itf.hr.hsmodify.ServiceLostException getFaultInfo() {
        return this.serviceLostException;
    }
}
