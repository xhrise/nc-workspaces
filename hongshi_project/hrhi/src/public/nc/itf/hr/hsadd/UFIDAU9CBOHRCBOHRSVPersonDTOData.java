
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
 * <p>Java class for UFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="UFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.UFIDA.org/EntityData}UFSoft.UBF.Business.DataTransObjectBase">
 *       &lt;sequence>
 *         &lt;element name="m_assgnBeginDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="m_businessOrgCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_certificateType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_country" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_createOrgCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_deptCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_dimissionDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="m_employeeCategoryCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_firstName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_hROrgCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_jobCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_lastName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_ownerOrgCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_personCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_positionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_responsibilityType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_sex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_startDate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="m_superiorPositionCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_superiorWorkOrgCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="m_workingOrgCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData", namespace = "http://www.UFIDA.org/EntityData", propOrder = {
    "mAssgnBeginDate",
    "mBusinessOrgCode",
    "mCertificateType",
    "mCountry",
    "mCreateOrgCode",
    "mDeptCode",
    "mDimissionDate",
    "mEmployeeCategoryCode",
    "mFirstName",
    "mhrOrgCode",
    "mJobCode",
    "mLastName",
    "mOwnerOrgCode",
    "mPersonCode",
    "mPositionCode",
    "mResponsibilityType",
    "mSex",
    "mStartDate",
    "mSuperiorPositionCode",
    "mSuperiorWorkOrgCode",
    "mWorkingOrgCode"
})
public class UFIDAU9CBOHRCBOHRSVPersonDTOData
    extends UFSoftUBFBusinessDataTransObjectBase
{

    @XmlElement(name = "m_assgnBeginDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar mAssgnBeginDate;
    @XmlElementRef(name = "m_businessOrgCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mBusinessOrgCode;
    @XmlElementRef(name = "m_certificateType", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mCertificateType;
    @XmlElementRef(name = "m_country", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mCountry;
    @XmlElementRef(name = "m_createOrgCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mCreateOrgCode;
    @XmlElementRef(name = "m_deptCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mDeptCode;
    @XmlElement(name = "m_dimissionDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar mDimissionDate;
    @XmlElementRef(name = "m_employeeCategoryCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mEmployeeCategoryCode;
    @XmlElementRef(name = "m_firstName", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mFirstName;
    @XmlElementRef(name = "m_hROrgCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mhrOrgCode;
    @XmlElementRef(name = "m_jobCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mJobCode;
    @XmlElementRef(name = "m_lastName", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mLastName;
    @XmlElementRef(name = "m_ownerOrgCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mOwnerOrgCode;
    @XmlElementRef(name = "m_personCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mPersonCode;
    @XmlElementRef(name = "m_positionCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mPositionCode;
    @XmlElementRef(name = "m_responsibilityType", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mResponsibilityType;
    @XmlElementRef(name = "m_sex", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mSex;
    @XmlElement(name = "m_startDate")
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar mStartDate;
    @XmlElementRef(name = "m_superiorPositionCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mSuperiorPositionCode;
    @XmlElementRef(name = "m_superiorWorkOrgCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mSuperiorWorkOrgCode;
    @XmlElementRef(name = "m_workingOrgCode", namespace = "http://www.UFIDA.org/EntityData", type = JAXBElement.class)
    protected JAXBElement<String> mWorkingOrgCode;

    /**
     * Gets the value of the mAssgnBeginDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMAssgnBeginDate() {
        return mAssgnBeginDate;
    }

    /**
     * Sets the value of the mAssgnBeginDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMAssgnBeginDate(XMLGregorianCalendar value) {
        this.mAssgnBeginDate = value;
    }

    /**
     * Gets the value of the mBusinessOrgCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMBusinessOrgCode() {
        return mBusinessOrgCode;
    }

    /**
     * Sets the value of the mBusinessOrgCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMBusinessOrgCode(JAXBElement<String> value) {
        this.mBusinessOrgCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mCertificateType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMCertificateType() {
        return mCertificateType;
    }

    /**
     * Sets the value of the mCertificateType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMCertificateType(JAXBElement<String> value) {
        this.mCertificateType = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mCountry property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMCountry() {
        return mCountry;
    }

    /**
     * Sets the value of the mCountry property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMCountry(JAXBElement<String> value) {
        this.mCountry = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mCreateOrgCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMCreateOrgCode() {
        return mCreateOrgCode;
    }

    /**
     * Sets the value of the mCreateOrgCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMCreateOrgCode(JAXBElement<String> value) {
        this.mCreateOrgCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mDeptCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMDeptCode() {
        return mDeptCode;
    }

    /**
     * Sets the value of the mDeptCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMDeptCode(JAXBElement<String> value) {
        this.mDeptCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mDimissionDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMDimissionDate() {
        return mDimissionDate;
    }

    /**
     * Sets the value of the mDimissionDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMDimissionDate(XMLGregorianCalendar value) {
        this.mDimissionDate = value;
    }

    /**
     * Gets the value of the mEmployeeCategoryCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMEmployeeCategoryCode() {
        return mEmployeeCategoryCode;
    }

    /**
     * Sets the value of the mEmployeeCategoryCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMEmployeeCategoryCode(JAXBElement<String> value) {
        this.mEmployeeCategoryCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mFirstName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMFirstName() {
        return mFirstName;
    }

    /**
     * Sets the value of the mFirstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMFirstName(JAXBElement<String> value) {
        this.mFirstName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mhrOrgCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMHROrgCode() {
        return mhrOrgCode;
    }

    /**
     * Sets the value of the mhrOrgCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMHROrgCode(JAXBElement<String> value) {
        this.mhrOrgCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mJobCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMJobCode() {
        return mJobCode;
    }

    /**
     * Sets the value of the mJobCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMJobCode(JAXBElement<String> value) {
        this.mJobCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mLastName property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMLastName() {
        return mLastName;
    }

    /**
     * Sets the value of the mLastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMLastName(JAXBElement<String> value) {
        this.mLastName = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mOwnerOrgCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMOwnerOrgCode() {
        return mOwnerOrgCode;
    }

    /**
     * Sets the value of the mOwnerOrgCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMOwnerOrgCode(JAXBElement<String> value) {
        this.mOwnerOrgCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mPersonCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMPersonCode() {
        return mPersonCode;
    }

    /**
     * Sets the value of the mPersonCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMPersonCode(JAXBElement<String> value) {
        this.mPersonCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mPositionCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMPositionCode() {
        return mPositionCode;
    }

    /**
     * Sets the value of the mPositionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMPositionCode(JAXBElement<String> value) {
        this.mPositionCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mResponsibilityType property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMResponsibilityType() {
        return mResponsibilityType;
    }

    /**
     * Sets the value of the mResponsibilityType property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMResponsibilityType(JAXBElement<String> value) {
        this.mResponsibilityType = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mSex property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMSex() {
        return mSex;
    }

    /**
     * Sets the value of the mSex property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMSex(JAXBElement<String> value) {
        this.mSex = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mStartDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getMStartDate() {
        return mStartDate;
    }

    /**
     * Sets the value of the mStartDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setMStartDate(XMLGregorianCalendar value) {
        this.mStartDate = value;
    }

    /**
     * Gets the value of the mSuperiorPositionCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMSuperiorPositionCode() {
        return mSuperiorPositionCode;
    }

    /**
     * Sets the value of the mSuperiorPositionCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMSuperiorPositionCode(JAXBElement<String> value) {
        this.mSuperiorPositionCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mSuperiorWorkOrgCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMSuperiorWorkOrgCode() {
        return mSuperiorWorkOrgCode;
    }

    /**
     * Sets the value of the mSuperiorWorkOrgCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMSuperiorWorkOrgCode(JAXBElement<String> value) {
        this.mSuperiorWorkOrgCode = ((JAXBElement<String> ) value);
    }

    /**
     * Gets the value of the mWorkingOrgCode property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getMWorkingOrgCode() {
        return mWorkingOrgCode;
    }

    /**
     * Sets the value of the mWorkingOrgCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setMWorkingOrgCode(JAXBElement<String> value) {
        this.mWorkingOrgCode = ((JAXBElement<String> ) value);
    }

}
