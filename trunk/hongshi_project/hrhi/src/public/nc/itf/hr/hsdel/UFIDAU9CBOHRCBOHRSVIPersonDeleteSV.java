package nc.itf.hr.hsdel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.Action;
import javax.xml.ws.FaultAction;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.2.3
 * Mon Oct 26 13:49:12 CST 2009
 * Generated source version: 2.2.3
 * 
 */
 
@WebService(targetNamespace = "http://www.UFIDA.org", name = "UFIDA.U9.CBO.HR.CBOHRSV.IPersonDeleteSV")
@XmlSeeAlso({ObjectFactory.class})
public interface UFIDAU9CBOHRCBOHRSVIPersonDeleteSV {

    @ResponseWrapper(localName = "DoResponse", targetNamespace = "http://www.UFIDA.org", className = "nc.itf.hr.hsdel.DoResponse")
    @Action(input = "http://www.UFIDA.org/UFIDA.U9.CBO.HR.CBOHRSV.IPersonDeleteSV/Do", output = "http://www.UFIDA.org/UFIDA.U9.CBO.HR.CBOHRSV.IPersonDeleteSV/DoResponse", fault = {@FaultAction(className = UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoExceptionFaultFaultMessage.class, value = "http://www.UFIDA.org/UFIDA.U9.CBO.HR.CBOHRSV.IPersonDeleteSV/DoExceptionFault"),@FaultAction(className = UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoExceptionBaseFaultFaultMessage.class, value = "http://www.UFIDA.org/UFIDA.U9.CBO.HR.CBOHRSV.IPersonDeleteSV/DoExceptionBaseFault"),@FaultAction(className = UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoServiceLostExceptionFaultFaultMessage.class, value = "http://www.UFIDA.org/UFIDA.U9.CBO.HR.CBOHRSV.IPersonDeleteSV/DoServiceLostExceptionFault"),@FaultAction(className = UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoServiceExceptionFaultFaultMessage.class, value = "http://www.UFIDA.org/UFIDA.U9.CBO.HR.CBOHRSV.IPersonDeleteSV/DoServiceExceptionFault")})
    @RequestWrapper(localName = "Do", targetNamespace = "http://www.UFIDA.org", className = "nc.itf.hr.hsdel.Do")
    @WebMethod(operationName = "Do", action = "http://www.UFIDA.org/UFIDA.U9.CBO.HR.CBOHRSV.IPersonDeleteSV/Do")
    public void _do(
        @WebParam(name = "context", targetNamespace = "http://www.UFIDA.org")
        java.lang.Object context,
        @WebParam(name = "persons", targetNamespace = "http://www.UFIDA.org")
        nc.itf.hr.hsdel.ArrayOfstring persons,
        @WebParam(mode = WebParam.Mode.OUT, name = "DoResult", targetNamespace = "http://www.UFIDA.org")
        javax.xml.ws.Holder<java.lang.String> doResult,
        @WebParam(mode = WebParam.Mode.OUT, name = "outMessages", targetNamespace = "http://www.UFIDA.org")
        javax.xml.ws.Holder<ArrayOfMessageBase> outMessages
    ) throws UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoExceptionFaultFaultMessage, UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoExceptionBaseFaultFaultMessage, UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoServiceLostExceptionFaultFaultMessage, UFIDAU9CBOHRCBOHRSVIPersonDeleteSVDoServiceExceptionFaultFaultMessage;
}
