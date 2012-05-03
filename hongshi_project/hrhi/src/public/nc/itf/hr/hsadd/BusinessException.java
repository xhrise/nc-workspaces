
package nc.itf.hr.hsadd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BusinessException complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BusinessException">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/UFSoft.UBF}ExceptionBase">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusinessException", namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Business")
@XmlSeeAlso({
    EntityNotExistException.class,
    AttrsContainerException.class,
    AttributeInValidException.class
})
public class BusinessException
    extends ExceptionBase
{


}
