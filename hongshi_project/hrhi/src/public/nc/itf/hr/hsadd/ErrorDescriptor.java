
package nc.itf.hr.hsadd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ErrorDescriptor complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ErrorDescriptor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="category" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="errorid" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="level" type="{http://schemas.datacontract.org/2004/07/UFSoft.UBF}ErrorLevel" minOccurs="0"/>
 *         &lt;element name="owner" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timeStamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ErrorDescriptor", propOrder = {
    "category",
    "errorid",
    "level",
    "owner",
    "timeStamp"
})
public class ErrorDescriptor {

    @XmlElementRef(name = "category", namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", type = JAXBElement.class)
    protected JAXBElement<String> category;
    @XmlElement(required = true, nillable = true)
    protected String errorid;
    protected ErrorLevel level;
    @XmlElementRef(name = "owner", namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", type = JAXBElement.class)
    protected JAXBElement<String> owner;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar timeStamp;

    /**
     * Gets the value of the category property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getCategory() {
        return category;
    }

    /**
     * Sets the value of the category property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setCategory(JAXBElement<String> value) {
        this.category = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the errorid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorid() {
        return errorid;
    }

    /**
     * Sets the value of the errorid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorid(String value) {
        this.errorid = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorLevel }
     *     
     */
    public ErrorLevel getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorLevel }
     *     
     */
    public void setLevel(ErrorLevel value) {
        this.level = value;
    }

    /**
     * Gets the value of the owner property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getOwner() {
        return owner;
    }

    /**
     * Sets the value of the owner property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setOwner(JAXBElement<String> value) {
        this.owner = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the timeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTimeStamp() {
        return timeStamp;
    }

    /**
     * Sets the value of the timeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTimeStamp(XMLGregorianCalendar value) {
        this.timeStamp = value;
    }

}
