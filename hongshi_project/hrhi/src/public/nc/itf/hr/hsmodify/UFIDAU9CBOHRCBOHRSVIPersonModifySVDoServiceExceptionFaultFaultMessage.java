
package nc.itf.hr.hsmodify;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.3
 * Mon Oct 26 13:46:18 CST 2009
 * Generated source version: 2.2.3
 * 
 */

@WebFault(name = "ServiceException", targetNamespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Service")
public class UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceExceptionFaultFaultMessage extends java.lang.Exception {
    public static final long serialVersionUID = 20091026134618L;
    
    private nc.itf.hr.hsmodify.ServiceException serviceException;

    public UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceExceptionFaultFaultMessage() {
        super();
    }
    
    public UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceExceptionFaultFaultMessage(String message) {
        super(message);
    }
    
    public UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceExceptionFaultFaultMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceExceptionFaultFaultMessage(String message, nc.itf.hr.hsmodify.ServiceException serviceException) {
        super(message);
        this.serviceException = serviceException;
    }

    public UFIDAU9CBOHRCBOHRSVIPersonModifySVDoServiceExceptionFaultFaultMessage(String message, nc.itf.hr.hsmodify.ServiceException serviceException, Throwable cause) {
        super(message, cause);
        this.serviceException = serviceException;
    }

    public nc.itf.hr.hsmodify.ServiceException getFaultInfo() {
        return this.serviceException;
    }
}
