
package nc.itf.hr.hsadd;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the nc.itf.hr.hsadd package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DoContext_QNAME = new QName("http://www.UFIDA.org", "context");
    private final static QName _DoPersonDTOs_QNAME = new QName("http://www.UFIDA.org", "personDTOs");
    private final static QName _ErrorDescriptorCategory_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF", "category");
    private final static QName _ErrorDescriptorOwner_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF", "owner");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMPersonCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_personCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMSuperiorWorkOrgCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_superiorWorkOrgCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMHROrgCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_hROrgCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMLastName_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_lastName");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMSuperiorPositionCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_superiorPositionCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMOwnerOrgCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_ownerOrgCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMDeptCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_deptCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMCertificateType_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_certificateType");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMResponsibilityType_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_responsibilityType");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMEmployeeCategoryCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_employeeCategoryCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMBusinessOrgCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_businessOrgCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMCountry_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_country");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMFirstName_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_firstName");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMPositionCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_positionCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMCreateOrgCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_createOrgCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMWorkingOrgCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_workingOrgCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMJobCode_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_jobCode");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTODataMSex_QNAME = new QName("http://www.UFIDA.org/EntityData", "m_sex");
    private final static QName _MultiLangDataDictLangs_QNAME = new QName("UFSoft.UBF.Util.Data", "_langs");
    private final static QName _DoResponseOutMessages_QNAME = new QName("http://www.UFIDA.org", "outMessages");
    private final static QName _DoResponseDoResult_QNAME = new QName("http://www.UFIDA.org", "DoResult");
    private final static QName _MultiLangData_QNAME = new QName("UFSoft.UBF.Util.Data", "MultiLangData");
    private final static QName _ServiceException_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.Service", "ServiceException");
    private final static QName _ThreadContext_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.Util.Context", "ThreadContext");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _AttrsContainerException_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.Business", "AttrsContainerException");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _ApplicationContext_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.Util.Context", "ApplicationContext");
    private final static QName _EntityNotExistException_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.Business", "EntityNotExistException");
    private final static QName _Exception_QNAME = new QName("http://schemas.datacontract.org/2004/07/System", "Exception");
    private final static QName _ArrayOfException_QNAME = new QName("http://schemas.datacontract.org/2004/07/System", "ArrayOfException");
    private final static QName _MultiLangDataDict_QNAME = new QName("UFSoft.UBF.Util.Data", "MultiLangDataDict");
    private final static QName _ExceptionBase_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF", "ExceptionBase");
    private final static QName _AttributeInValidException_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.Business", "AttributeInValidException");
    private final static QName _MessageBase_QNAME = new QName("UFSoft.UBF.Exceptions", "MessageBase");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _ExceptionBaseFormatState_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF", "ExceptionBase.FormatState");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _UFSoftUBFBusinessDataTransObjectBase_QNAME = new QName("http://www.UFIDA.org/EntityData", "UFSoft.UBF.Business.DataTransObjectBase");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _ArrayOfMultiLangData_QNAME = new QName("UFSoft.UBF.Util.Data", "ArrayOfMultiLangData");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _ArrayOfKeyValueOfanyTypeanyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfKeyValueOfanyTypeanyType");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _ArrayOfMessageBase_QNAME = new QName("UFSoft.UBF.Exceptions", "ArrayOfMessageBase");
    private final static QName _UFIDAU9CBOHRCBOHRSVPersonDTOData_QNAME = new QName("http://www.UFIDA.org/EntityData", "UFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData");
    private final static QName _MessageBaseFormatState_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.Exceptions", "MessageBase.FormatState");
    private final static QName _ObjectState_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.PL.Engine", "ObjectState");
    private final static QName _ErrorMessage_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF", "ErrorMessage");
    private final static QName _PlatformContext_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.Util.Context", "PlatformContext");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _UnknownException_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.Exceptions", "UnknownException");
    private final static QName _DataRowState_QNAME = new QName("http://schemas.datacontract.org/2004/07/System.Data", "DataRowState");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _ServiceLostException_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.Service", "ServiceLostException");
    private final static QName _BusinessException_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF.Business", "BusinessException");
    private final static QName _ErrorLevel_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF", "ErrorLevel");
    private final static QName _ArrayOfErrorMessage_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF", "ArrayOfErrorMessage");
    private final static QName _ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData_QNAME = new QName("http://www.UFIDA.org/EntityData", "ArrayOfUFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData");
    private final static QName _ErrorDescriptor_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF", "ErrorDescriptor");
    private final static QName _MessageBaseAttributeMetadataID_QNAME = new QName("UFSoft.UBF.Exceptions", "attributeMetadataID");
    private final static QName _MessageBaseInnerMessages_QNAME = new QName("UFSoft.UBF.Exceptions", "innerMessages");
    private final static QName _MessageBaseEntityFullName_QNAME = new QName("UFSoft.UBF.Exceptions", "entityFullName");
    private final static QName _MessageBaseAttributeName_QNAME = new QName("UFSoft.UBF.Exceptions", "attributeName");
    private final static QName _MessageBaseEntityMetadataID_QNAME = new QName("UFSoft.UBF.Exceptions", "entityMetadataID");
    private final static QName _MessageBaseOrginalEntityFullName_QNAME = new QName("UFSoft.UBF.Exceptions", "orginalEntityFullName");
    private final static QName _MessageBaseLocalMessage_QNAME = new QName("UFSoft.UBF.Exceptions", "localMessage");
    private final static QName _ErrorMessageMessage_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF", "message");
    private final static QName _ErrorMessageInnerMessages_QNAME = new QName("http://schemas.datacontract.org/2004/07/UFSoft.UBF", "innerMessages");
    private final static QName _MultiLangDataLangType_QNAME = new QName("UFSoft.UBF.Util.Data", "LangType");
    private final static QName _MultiLangDataLangValue_QNAME = new QName("UFSoft.UBF.Util.Data", "LangValue");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: nc.itf.hr.hsadd
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ArrayOfMultiLangData }
     * 
     */
    public ArrayOfMultiLangData createArrayOfMultiLangData() {
        return new ArrayOfMultiLangData();
    }

    /**
     * Create an instance of {@link ApplicationContext }
     * 
     */
    public ApplicationContext createApplicationContext() {
        return new ApplicationContext();
    }

    /**
     * Create an instance of {@link UnknownException }
     * 
     */
    public UnknownException createUnknownException() {
        return new UnknownException();
    }

    /**
     * Create an instance of {@link ArrayOfMessageBase }
     * 
     */
    public ArrayOfMessageBase createArrayOfMessageBase() {
        return new ArrayOfMessageBase();
    }

    /**
     * Create an instance of {@link ArrayOfKeyValueOfanyTypeanyType.KeyValueOfanyTypeanyType }
     * 
     */
    public ArrayOfKeyValueOfanyTypeanyType.KeyValueOfanyTypeanyType createArrayOfKeyValueOfanyTypeanyTypeKeyValueOfanyTypeanyType() {
        return new ArrayOfKeyValueOfanyTypeanyType.KeyValueOfanyTypeanyType();
    }

    /**
     * Create an instance of {@link EntityNotExistException }
     * 
     */
    public EntityNotExistException createEntityNotExistException() {
        return new EntityNotExistException();
    }

    /**
     * Create an instance of {@link MultiLangData }
     * 
     */
    public MultiLangData createMultiLangData() {
        return new MultiLangData();
    }

    /**
     * Create an instance of {@link AttrsContainerException }
     * 
     */
    public AttrsContainerException createAttrsContainerException() {
        return new AttrsContainerException();
    }

    /**
     * Create an instance of {@link PlatformContext }
     * 
     */
    public PlatformContext createPlatformContext() {
        return new PlatformContext();
    }

    /**
     * Create an instance of {@link ArrayOfKeyValueOfanyTypeanyType }
     * 
     */
    public ArrayOfKeyValueOfanyTypeanyType createArrayOfKeyValueOfanyTypeanyType() {
        return new ArrayOfKeyValueOfanyTypeanyType();
    }

    /**
     * Create an instance of {@link MessageBase }
     * 
     */
    public MessageBase createMessageBase() {
        return new MessageBase();
    }

    /**
     * Create an instance of {@link UFSoftUBFBusinessDataTransObjectBase }
     * 
     */
    public UFSoftUBFBusinessDataTransObjectBase createUFSoftUBFBusinessDataTransObjectBase() {
        return new UFSoftUBFBusinessDataTransObjectBase();
    }

    /**
     * Create an instance of {@link ServiceException }
     * 
     */
    public ServiceException createServiceException() {
        return new ServiceException();
    }

    /**
     * Create an instance of {@link Do }
     * 
     */
    public Do createDo() {
        return new Do();
    }

    /**
     * Create an instance of {@link ErrorDescriptor }
     * 
     */
    public ErrorDescriptor createErrorDescriptor() {
        return new ErrorDescriptor();
    }

    /**
     * Create an instance of {@link UFIDAU9CBOHRCBOHRSVPersonDTOData }
     * 
     */
    public UFIDAU9CBOHRCBOHRSVPersonDTOData createUFIDAU9CBOHRCBOHRSVPersonDTOData() {
        return new UFIDAU9CBOHRCBOHRSVPersonDTOData();
    }

    /**
     * Create an instance of {@link AttributeInValidException }
     * 
     */
    public AttributeInValidException createAttributeInValidException() {
        return new AttributeInValidException();
    }

    /**
     * Create an instance of {@link MultiLangDataDict }
     * 
     */
    public MultiLangDataDict createMultiLangDataDict() {
        return new MultiLangDataDict();
    }

    /**
     * Create an instance of {@link DoResponse }
     * 
     */
    public DoResponse createDoResponse() {
        return new DoResponse();
    }

    /**
     * Create an instance of {@link ErrorMessage }
     * 
     */
    public ErrorMessage createErrorMessage() {
        return new ErrorMessage();
    }

    /**
     * Create an instance of {@link ArrayOfErrorMessage }
     * 
     */
    public ArrayOfErrorMessage createArrayOfErrorMessage() {
        return new ArrayOfErrorMessage();
    }

    /**
     * Create an instance of {@link BusinessException }
     * 
     */
    public BusinessException createBusinessException() {
        return new BusinessException();
    }

    /**
     * Create an instance of {@link ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData }
     * 
     */
    public ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData createArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData() {
        return new ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData();
    }

    /**
     * Create an instance of {@link ThreadContext }
     * 
     */
    public ThreadContext createThreadContext() {
        return new ThreadContext();
    }

    /**
     * Create an instance of {@link ExceptionBase }
     * 
     */
    public ExceptionBase createExceptionBase() {
        return new ExceptionBase();
    }

    /**
     * Create an instance of {@link Exception }
     * 
     */
    public Exception createException() {
        return new Exception();
    }

    /**
     * Create an instance of {@link ServiceLostException }
     * 
     */
    public ServiceLostException createServiceLostException() {
        return new ServiceLostException();
    }

    /**
     * Create an instance of {@link ArrayOfException }
     * 
     */
    public ArrayOfException createArrayOfException() {
        return new ArrayOfException();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org", name = "context", scope = Do.class)
    public JAXBElement<Object> createDoContext(Object value) {
        return new JAXBElement<Object>(_DoContext_QNAME, Object.class, Do.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org", name = "personDTOs", scope = Do.class)
    public JAXBElement<ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData> createDoPersonDTOs(ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData value) {
        return new JAXBElement<ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData>(_DoPersonDTOs_QNAME, ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData.class, Do.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", name = "category", scope = ErrorDescriptor.class)
    public JAXBElement<String> createErrorDescriptorCategory(String value) {
        return new JAXBElement<String>(_ErrorDescriptorCategory_QNAME, String.class, ErrorDescriptor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", name = "owner", scope = ErrorDescriptor.class)
    public JAXBElement<String> createErrorDescriptorOwner(String value) {
        return new JAXBElement<String>(_ErrorDescriptorOwner_QNAME, String.class, ErrorDescriptor.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_personCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMPersonCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMPersonCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_superiorWorkOrgCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMSuperiorWorkOrgCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMSuperiorWorkOrgCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_hROrgCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMHROrgCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMHROrgCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_lastName", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMLastName(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMLastName_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_superiorPositionCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMSuperiorPositionCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMSuperiorPositionCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_ownerOrgCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMOwnerOrgCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMOwnerOrgCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_deptCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMDeptCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMDeptCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_certificateType", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMCertificateType(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMCertificateType_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_responsibilityType", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMResponsibilityType(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMResponsibilityType_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_employeeCategoryCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMEmployeeCategoryCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMEmployeeCategoryCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_businessOrgCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMBusinessOrgCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMBusinessOrgCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_country", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMCountry(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMCountry_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_firstName", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMFirstName(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMFirstName_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_positionCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMPositionCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMPositionCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_createOrgCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMCreateOrgCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMCreateOrgCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_workingOrgCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMWorkingOrgCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMWorkingOrgCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_jobCode", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMJobCode(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMJobCode_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "m_sex", scope = UFIDAU9CBOHRCBOHRSVPersonDTOData.class)
    public JAXBElement<String> createUFIDAU9CBOHRCBOHRSVPersonDTODataMSex(String value) {
        return new JAXBElement<String>(_UFIDAU9CBOHRCBOHRSVPersonDTODataMSex_QNAME, String.class, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMultiLangData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Util.Data", name = "_langs", scope = MultiLangDataDict.class)
    public JAXBElement<ArrayOfMultiLangData> createMultiLangDataDictLangs(ArrayOfMultiLangData value) {
        return new JAXBElement<ArrayOfMultiLangData>(_MultiLangDataDictLangs_QNAME, ArrayOfMultiLangData.class, MultiLangDataDict.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMessageBase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org", name = "outMessages", scope = DoResponse.class)
    public JAXBElement<ArrayOfMessageBase> createDoResponseOutMessages(ArrayOfMessageBase value) {
        return new JAXBElement<ArrayOfMessageBase>(_DoResponseOutMessages_QNAME, ArrayOfMessageBase.class, DoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org", name = "DoResult", scope = DoResponse.class)
    public JAXBElement<String> createDoResponseDoResult(String value) {
        return new JAXBElement<String>(_DoResponseDoResult_QNAME, String.class, DoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultiLangData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Util.Data", name = "MultiLangData")
    public JAXBElement<MultiLangData> createMultiLangData(MultiLangData value) {
        return new JAXBElement<MultiLangData>(_MultiLangData_QNAME, MultiLangData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Service", name = "ServiceException")
    public JAXBElement<ServiceException> createServiceException(ServiceException value) {
        return new JAXBElement<ServiceException>(_ServiceException_QNAME, ServiceException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ThreadContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Util.Context", name = "ThreadContext")
    public JAXBElement<ThreadContext> createThreadContext(ThreadContext value) {
        return new JAXBElement<ThreadContext>(_ThreadContext_QNAME, ThreadContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttrsContainerException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Business", name = "AttrsContainerException")
    public JAXBElement<AttrsContainerException> createAttrsContainerException(AttrsContainerException value) {
        return new JAXBElement<AttrsContainerException>(_AttrsContainerException_QNAME, AttrsContainerException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ApplicationContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Util.Context", name = "ApplicationContext")
    public JAXBElement<ApplicationContext> createApplicationContext(ApplicationContext value) {
        return new JAXBElement<ApplicationContext>(_ApplicationContext_QNAME, ApplicationContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link EntityNotExistException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Business", name = "EntityNotExistException")
    public JAXBElement<EntityNotExistException> createEntityNotExistException(EntityNotExistException value) {
        return new JAXBElement<EntityNotExistException>(_EntityNotExistException_QNAME, EntityNotExistException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Exception }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/System", name = "Exception")
    public JAXBElement<Exception> createException(Exception value) {
        return new JAXBElement<Exception>(_Exception_QNAME, Exception.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/System", name = "ArrayOfException")
    public JAXBElement<ArrayOfException> createArrayOfException(ArrayOfException value) {
        return new JAXBElement<ArrayOfException>(_ArrayOfException_QNAME, ArrayOfException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MultiLangDataDict }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Util.Data", name = "MultiLangDataDict")
    public JAXBElement<MultiLangDataDict> createMultiLangDataDict(MultiLangDataDict value) {
        return new JAXBElement<MultiLangDataDict>(_MultiLangDataDict_QNAME, MultiLangDataDict.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExceptionBase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", name = "ExceptionBase")
    public JAXBElement<ExceptionBase> createExceptionBase(ExceptionBase value) {
        return new JAXBElement<ExceptionBase>(_ExceptionBase_QNAME, ExceptionBase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttributeInValidException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Business", name = "AttributeInValidException")
    public JAXBElement<AttributeInValidException> createAttributeInValidException(AttributeInValidException value) {
        return new JAXBElement<AttributeInValidException>(_AttributeInValidException_QNAME, AttributeInValidException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageBase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Exceptions", name = "MessageBase")
    public JAXBElement<MessageBase> createMessageBase(MessageBase value) {
        return new JAXBElement<MessageBase>(_MessageBase_QNAME, MessageBase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExceptionBaseFormatState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", name = "ExceptionBase.FormatState")
    public JAXBElement<ExceptionBaseFormatState> createExceptionBaseFormatState(ExceptionBaseFormatState value) {
        return new JAXBElement<ExceptionBaseFormatState>(_ExceptionBaseFormatState_QNAME, ExceptionBaseFormatState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UFSoftUBFBusinessDataTransObjectBase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "UFSoft.UBF.Business.DataTransObjectBase")
    public JAXBElement<UFSoftUBFBusinessDataTransObjectBase> createUFSoftUBFBusinessDataTransObjectBase(UFSoftUBFBusinessDataTransObjectBase value) {
        return new JAXBElement<UFSoftUBFBusinessDataTransObjectBase>(_UFSoftUBFBusinessDataTransObjectBase_QNAME, UFSoftUBFBusinessDataTransObjectBase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMultiLangData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Util.Data", name = "ArrayOfMultiLangData")
    public JAXBElement<ArrayOfMultiLangData> createArrayOfMultiLangData(ArrayOfMultiLangData value) {
        return new JAXBElement<ArrayOfMultiLangData>(_ArrayOfMultiLangData_QNAME, ArrayOfMultiLangData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfKeyValueOfanyTypeanyType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays", name = "ArrayOfKeyValueOfanyTypeanyType")
    public JAXBElement<ArrayOfKeyValueOfanyTypeanyType> createArrayOfKeyValueOfanyTypeanyType(ArrayOfKeyValueOfanyTypeanyType value) {
        return new JAXBElement<ArrayOfKeyValueOfanyTypeanyType>(_ArrayOfKeyValueOfanyTypeanyType_QNAME, ArrayOfKeyValueOfanyTypeanyType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMessageBase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Exceptions", name = "ArrayOfMessageBase")
    public JAXBElement<ArrayOfMessageBase> createArrayOfMessageBase(ArrayOfMessageBase value) {
        return new JAXBElement<ArrayOfMessageBase>(_ArrayOfMessageBase_QNAME, ArrayOfMessageBase.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UFIDAU9CBOHRCBOHRSVPersonDTOData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "UFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData")
    public JAXBElement<UFIDAU9CBOHRCBOHRSVPersonDTOData> createUFIDAU9CBOHRCBOHRSVPersonDTOData(UFIDAU9CBOHRCBOHRSVPersonDTOData value) {
        return new JAXBElement<UFIDAU9CBOHRCBOHRSVPersonDTOData>(_UFIDAU9CBOHRCBOHRSVPersonDTOData_QNAME, UFIDAU9CBOHRCBOHRSVPersonDTOData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageBaseFormatState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Exceptions", name = "MessageBase.FormatState")
    public JAXBElement<MessageBaseFormatState> createMessageBaseFormatState(MessageBaseFormatState value) {
        return new JAXBElement<MessageBaseFormatState>(_MessageBaseFormatState_QNAME, MessageBaseFormatState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ObjectState }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.PL.Engine", name = "ObjectState")
    public JAXBElement<ObjectState> createObjectState(ObjectState value) {
        return new JAXBElement<ObjectState>(_ObjectState_QNAME, ObjectState.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErrorMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", name = "ErrorMessage")
    public JAXBElement<ErrorMessage> createErrorMessage(ErrorMessage value) {
        return new JAXBElement<ErrorMessage>(_ErrorMessage_QNAME, ErrorMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PlatformContext }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Util.Context", name = "PlatformContext")
    public JAXBElement<PlatformContext> createPlatformContext(PlatformContext value) {
        return new JAXBElement<PlatformContext>(_PlatformContext_QNAME, PlatformContext.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link UnknownException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Exceptions", name = "UnknownException")
    public JAXBElement<UnknownException> createUnknownException(UnknownException value) {
        return new JAXBElement<UnknownException>(_UnknownException_QNAME, UnknownException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link List }{@code <}{@link String }{@code >}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/System.Data", name = "DataRowState")
    public JAXBElement<List<String>> createDataRowState(List<String> value) {
        return new JAXBElement<List<String>>(_DataRowState_QNAME, ((Class) List.class), null, ((List<String> ) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceLostException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Service", name = "ServiceLostException")
    public JAXBElement<ServiceLostException> createServiceLostException(ServiceLostException value) {
        return new JAXBElement<ServiceLostException>(_ServiceLostException_QNAME, ServiceLostException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BusinessException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Business", name = "BusinessException")
    public JAXBElement<BusinessException> createBusinessException(BusinessException value) {
        return new JAXBElement<BusinessException>(_BusinessException_QNAME, BusinessException.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErrorLevel }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", name = "ErrorLevel")
    public JAXBElement<ErrorLevel> createErrorLevel(ErrorLevel value) {
        return new JAXBElement<ErrorLevel>(_ErrorLevel_QNAME, ErrorLevel.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErrorMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", name = "ArrayOfErrorMessage")
    public JAXBElement<ArrayOfErrorMessage> createArrayOfErrorMessage(ArrayOfErrorMessage value) {
        return new JAXBElement<ArrayOfErrorMessage>(_ArrayOfErrorMessage_QNAME, ArrayOfErrorMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.UFIDA.org/EntityData", name = "ArrayOfUFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData")
    public JAXBElement<ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData> createArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData(ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData value) {
        return new JAXBElement<ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData>(_ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData_QNAME, ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ErrorDescriptor }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", name = "ErrorDescriptor")
    public JAXBElement<ErrorDescriptor> createErrorDescriptor(ErrorDescriptor value) {
        return new JAXBElement<ErrorDescriptor>(_ErrorDescriptor_QNAME, ErrorDescriptor.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Exceptions", name = "attributeMetadataID", scope = MessageBase.class)
    public JAXBElement<String> createMessageBaseAttributeMetadataID(String value) {
        return new JAXBElement<String>(_MessageBaseAttributeMetadataID_QNAME, String.class, MessageBase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMessageBase }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Exceptions", name = "innerMessages", scope = MessageBase.class)
    public JAXBElement<ArrayOfMessageBase> createMessageBaseInnerMessages(ArrayOfMessageBase value) {
        return new JAXBElement<ArrayOfMessageBase>(_MessageBaseInnerMessages_QNAME, ArrayOfMessageBase.class, MessageBase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Exceptions", name = "entityFullName", scope = MessageBase.class)
    public JAXBElement<String> createMessageBaseEntityFullName(String value) {
        return new JAXBElement<String>(_MessageBaseEntityFullName_QNAME, String.class, MessageBase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Exceptions", name = "attributeName", scope = MessageBase.class)
    public JAXBElement<String> createMessageBaseAttributeName(String value) {
        return new JAXBElement<String>(_MessageBaseAttributeName_QNAME, String.class, MessageBase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Exceptions", name = "entityMetadataID", scope = MessageBase.class)
    public JAXBElement<String> createMessageBaseEntityMetadataID(String value) {
        return new JAXBElement<String>(_MessageBaseEntityMetadataID_QNAME, String.class, MessageBase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Exceptions", name = "orginalEntityFullName", scope = MessageBase.class)
    public JAXBElement<String> createMessageBaseOrginalEntityFullName(String value) {
        return new JAXBElement<String>(_MessageBaseOrginalEntityFullName_QNAME, String.class, MessageBase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Exceptions", name = "localMessage", scope = MessageBase.class)
    public JAXBElement<String> createMessageBaseLocalMessage(String value) {
        return new JAXBElement<String>(_MessageBaseLocalMessage_QNAME, String.class, MessageBase.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", name = "message", scope = ErrorMessage.class)
    public JAXBElement<String> createErrorMessageMessage(String value) {
        return new JAXBElement<String>(_ErrorMessageMessage_QNAME, String.class, ErrorMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfErrorMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF", name = "innerMessages", scope = ErrorMessage.class)
    public JAXBElement<ArrayOfErrorMessage> createErrorMessageInnerMessages(ArrayOfErrorMessage value) {
        return new JAXBElement<ArrayOfErrorMessage>(_ErrorMessageInnerMessages_QNAME, ArrayOfErrorMessage.class, ErrorMessage.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Util.Data", name = "LangType", scope = MultiLangData.class)
    public JAXBElement<String> createMultiLangDataLangType(String value) {
        return new JAXBElement<String>(_MultiLangDataLangType_QNAME, String.class, MultiLangData.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "UFSoft.UBF.Util.Data", name = "LangValue", scope = MultiLangData.class)
    public JAXBElement<String> createMultiLangDataLangValue(String value) {
        return new JAXBElement<String>(_MultiLangDataLangValue_QNAME, String.class, MultiLangData.class, value);
    }

}
