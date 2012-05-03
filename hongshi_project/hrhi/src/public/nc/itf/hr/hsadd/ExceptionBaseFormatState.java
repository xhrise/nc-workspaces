
package nc.itf.hr.hsadd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ExceptionBase.FormatState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ExceptionBase.FormatState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="None"/>
 *     &lt;enumeration value="Formatting"/>
 *     &lt;enumeration value="Formatted"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ExceptionBase.FormatState")
@XmlEnum
public enum ExceptionBaseFormatState {

    @XmlEnumValue("None")
    NONE("None"),
    @XmlEnumValue("Formatting")
    FORMATTING("Formatting"),
    @XmlEnumValue("Formatted")
    FORMATTED("Formatted");
    private final String value;

    ExceptionBaseFormatState(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ExceptionBaseFormatState fromValue(String v) {
        for (ExceptionBaseFormatState c: ExceptionBaseFormatState.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
