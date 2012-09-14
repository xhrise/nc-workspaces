
package nc.itf.hr.hsmodify;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="context" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *         &lt;element name="personDTOs" type="{http://www.UFIDA.org/EntityData}ArrayOfUFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "context",
    "personDTOs"
})
@XmlRootElement(name = "Do", namespace = "http://www.UFIDA.org")
public class Do {

    @XmlElementRef(name = "context", namespace = "http://www.UFIDA.org", type = JAXBElement.class)
    protected JAXBElement<Object> context;
    @XmlElementRef(name = "personDTOs", namespace = "http://www.UFIDA.org", type = JAXBElement.class)
    protected JAXBElement<ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData> personDTOs;

    /**
     * Gets the value of the context property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public JAXBElement<Object> getContext() {
        return context;
    }

    /**
     * Sets the value of the context property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public void setContext(JAXBElement<Object> value) {
        this.context = ((JAXBElement<Object> ) value);
    }

    /**
     * Gets the value of the personDTOs property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData }{@code >}
     *     
     */
    public JAXBElement<ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData> getPersonDTOs() {
        return personDTOs;
    }

    /**
     * Sets the value of the personDTOs property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData }{@code >}
     *     
     */
    public void setPersonDTOs(JAXBElement<ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData> value) {
        this.personDTOs = ((JAXBElement<ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData> ) value);
    }

}