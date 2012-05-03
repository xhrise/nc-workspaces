
package nc.itf.hr.hsmodify;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MessageBase.FormatState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="MessageBase.FormatState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Formatting"/>
 *     &lt;enumeration value="Formatted"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "MessageBase.FormatState", namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.Exceptions")
@XmlEnum
public enum MessageBaseFormatState {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Formatting")
    FORMATTING("Formatting"),
    @XmlEnumValue("Formatted")
    FORMATTED("Formatted");
    private final String value;

    MessageBaseFormatState(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static MessageBaseFormatState fromValue(String v) {
        for (MessageBaseFormatState c: MessageBaseFormatState.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
