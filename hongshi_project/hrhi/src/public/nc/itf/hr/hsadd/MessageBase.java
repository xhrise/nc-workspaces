
package nc.itf.hr.hsadd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageBase complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MessageBase">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attributeMetadataID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attributeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entityFullName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="entityID" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="entityMetadataID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorLevel" type="{http://www.w3.org/2001/XMLSchema}short" minOccurs="0"/>
 *         &lt;element name="formated" type="{http://schemas.datacontract.org/2004/07/UFSoft.UBF.Exceptions}MessageBase.FormatState" minOccurs="0"/>
 *         &lt;element name="innerMessages" type="{UFSoft.UBF.Exceptions}ArrayOfMessageBase" minOccurs="0"/>
 *         &lt;element name="isValidEntityID" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="localMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orginalEntityFullName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MessageBase", namespace = "UFSoft.UBF.Exceptions", propOrder = {
    "attributeMetadataID",
    "attributeName",
    "entityFullName",
    "entityID",
    "entityMetadataID",
    "errorLevel",
    "formated",
    "innerMessages",
    "isValidEntityID",
    "localMessage",
    "orginalEntityFullName"
})
public class MessageBase {

    @XmlElementRef(name = "attributeMetadataID", namespace = "UFSoft.UBF.Exceptions", type = JAXBElement.class)
    protected JAXBElement<String> attributeMetadataID;
    @XmlElementRef(name = "attributeName", namespace = "UFSoft.UBF.Exceptions", type = JAXBElement.class)
    protected JAXBElement<String> attributeName;
    @XmlElementRef(name = "entityFullName", namespace = "UFSoft.UBF.Exceptions", type = JAXBElement.class)
    protected JAXBElement<String> entityFullName;
    protected Long entityID;
    @XmlElementRef(name = "entityMetadataID", namespace = "UFSoft.UBF.Exceptions", type = JAXBElement.class)
    protected JAXBElement<String> entityMetadataID;
    protected Short errorLevel;
    protected MessageBaseFormatState formated;
    @XmlElementRef(name = "innerMessages", namespace = "UFSoft.UBF.Exceptions", type = JAXBElement.class)
    protected JAXBElement<ArrayOfMessageBase> innerMessages;
    protected Boolean isValidEntityID;
    @XmlElementRef(name = "localMessage", namespace = "UFSoft.UBF.Exceptions", type = JAXBElement.class)
    protected JAXBElement<String> localMessage;
    @XmlElementRef(name = "orginalEntityFullName", namespace = "UFSoft.UBF.Exceptions", type = JAXBElement.class)
    protected JAXBElement<String> orginalEntityFullName;

    /**
     * Gets the value of the attributeMetadataID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAttributeMetadataID() {
        return attributeMetadataID;
    }

    /**
     * Sets the value of the attributeMetadataID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAttributeMetadataID(JAXBElement<String> value) {
        this.attributeMetadataID = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the attributeName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getAttributeName() {
        return attributeName;
    }

    /**
     * Sets the value of the attributeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setAttributeName(JAXBElement<String> value) {
        this.attributeName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the entityFullName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEntityFullName() {
        return entityFullName;
    }

    /**
     * Sets the value of the entityFullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEntityFullName(JAXBElement<String> value) {
        this.entityFullName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the entityID property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getEntityID() {
        return entityID;
    }

    /**
     * Sets the value of the entityID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setEntityID(Long value) {
        this.entityID = value;
    }

    /**
     * Gets the value of the entityMetadataID property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getEntityMetadataID() {
        return entityMetadataID;
    }

    /**
     * Sets the value of the entityMetadataID property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setEntityMetadataID(JAXBElement<String> value) {
        this.entityMetadataID = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the errorLevel property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getErrorLevel() {
        return errorLevel;
    }

    /**
     * Sets the value of the errorLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setErrorLevel(Short value) {
        this.errorLevel = value;
    }

    /**
     * Gets the value of the formated property.
     * 
     * @return
     *     possible object is
     *     {@link MessageBaseFormatState }
     *     
     */
    public MessageBaseFormatState getFormated() {
        return formated;
    }

    /**
     * Sets the value of the formated property.
     * 
     * @param value
     *     allowed object is
     *     {@link MessageBaseFormatState }
     *     
     */
    public void setFormated(MessageBaseFormatState value) {
        this.formated = value;
    }

    /**
     * Gets the value of the innerMessages property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfMessageBase }{@code >}
     *     
     */
    public JAXBElement<ArrayOfMessageBase> getInnerMessages() {
        return innerMessages;
    }

    /**
     * Sets the value of the innerMessages property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfMessageBase }{@code >}
     *     
     */
    public void setInnerMessages(JAXBElement<ArrayOfMessageBase> value) {
        this.innerMessages = ((JAXBElement<ArrayOfMessageBase> ) value);
    }

    /**
     * Gets the value of the isValidEntityID property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsValidEntityID() {
        return isValidEntityID;
    }

    /**
     * Sets the value of the isValidEntityID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsValidEntityID(Boolean value) {
        this.isValidEntityID = value;
    }

    /**
     * Gets the value of the localMessage property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getLocalMessage() {
        return localMessage;
    }

    /**
     * Sets the value of the localMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setLocalMessage(JAXBElement<String> value) {
        this.localMessage = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the orginalEntityFullName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOrginalEntityFullName() {
        return orginalEntityFullName;
    }

    /**
     * Sets the value of the orginalEntityFullName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOrginalEntityFullName(JAXBElement<String> value) {
        this.orginalEntityFullName = ((JAXBElement<String> ) value);
    }

}
