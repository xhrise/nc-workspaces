
package nc.itf.hr.hsdel;

import javax.xml.ws.WebFault;


/**
 * This class was generated by Apache CXF 2.2.3
 * Mon Oct 26 13:49:12 CST 2009
 * Generated source version: 2.2.3
 * 
 */

@WebFault(name = "ExceptionBase", targetNamespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF")
public class UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoExceptionBaseFaultFaultMessage extends java.lang.Exception {
    public static final long serialVersionUID = 20091026134912L;
    
    private nc.itf.hr.hsdel.ExceptionBase exceptionBase;

    public UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoExceptionBaseFaultFaultMessage() {
        super();
    }
    
    public UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoExceptionBaseFaultFaultMessage(String message) {
        super(message);
    }
    
    public UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoExceptionBaseFaultFaultMessage(String message, Throwable cause) {
        super(message, cause);
    }

    public UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoExceptionBaseFaultFaultMessage(String message, nc.itf.hr.hsdel.ExceptionBase exceptionBase) {
        super(message);
        this.exceptionBase = exceptionBase;
    }

    public UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoExceptionBaseFaultFaultMessage(String message, nc.itf.hr.hsdel.ExceptionBase exceptionBase, Throwable cause) {
        super(message, cause);
        this.exceptionBase = exceptionBase;
    }

    public nc.itf.hr.hsdel.ExceptionBase getFaultInfo() {
        return this.exceptionBase;
    }
}
