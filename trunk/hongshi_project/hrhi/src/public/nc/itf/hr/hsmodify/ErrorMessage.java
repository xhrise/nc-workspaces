
package nc.itf.hr.hsmodify;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ErrorMessage complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ErrorMessage">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="errDescriptor" type="{http://schemas.datacontract.org/2004/07/UFSoft.UBF}ErrorDescriptor"/>
 *         &lt;element name="errorType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="innerMessages" type="{http://schemas.datacontract.org/2004/07/UFSoft.UBF}ArrayOfErrorMessage" minOccurs="0"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ErrorMessage", propOrder = {
    "errDescriptor",
    "errorType",
    "innerMessages",
    "message"
})
public class ErrorMessage {

    @XmlElement(required = true, nillable = true)
    protected ErrorDescriptor errDescriptor;
    @XmlElement(required = true, nillable = true)
    protected String errorType;
    @XmlElementRef(name = "innerMessages", namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", type = JAXBElement.class)
    protected JAXBElement<ArrayOfErrorMessage> innerMessages;
    @XmlElementRef(name = "message", namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", type = JAXBElement.class)
    protected JAXBElement<String> message;

    /**
     * Gets the value of the errDescriptor property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorDescriptor }
     *     
     */
    public ErrorDescriptor getErrDescriptor() {
        return errDescriptor;
    }

    /**
     * Sets the value of the errDescriptor property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorDescriptor }
     *     
     */
    public void setErrDescriptor(ErrorDescriptor value) {
        this.errDescriptor = value;
    }

    /**
     * Gets the value of the errorType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * Sets the value of the errorType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorType(String value) {
        this.errorType = value;
    }

    /**
     * Gets the value of the innerMessages property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfErrorMessage }{@code >}
     *     
     */
    public JAXBElement<ArrayOfErrorMessage> getInnerMessages() {
        return innerMessages;
    }

    /**
     * Sets the value of the innerMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfErrorMessage }{@code >}
     *     
     */
    public void setInnerMessages(JAXBElement<ArrayOfErrorMessage> value) {
        this.innerMessages = ((JAXBElement<ArrayOfErrorMessage> ) value);
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMessage(JAXBElement<String> value) {
        this.message = ((JAXBElement<String> ) value);
    }

}
