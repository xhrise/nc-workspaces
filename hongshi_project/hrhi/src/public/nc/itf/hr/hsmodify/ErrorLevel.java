
package nc.itf.hr.hsmodify;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ErrorLevel.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ErrorLevel">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Debug"/>
 *     &lt;enumeration value="Info"/>
 *     &lt;enumeration value="Warn"/>
 *     &lt;enumeration value="Error"/>
 *     &lt;enumeration value="Fatal"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ErrorLevel")
@XmlEnum
public enum ErrorLevel {

    @XmlEnumValue("Debug")
    DEBUG("Debug"),
    @XmlEnumValue("Info")
    INFO("Info"),
    @XmlEnumValue("Warn")
    WARN("Warn"),
    @XmlEnumValue("Error")
    ERROR("Error"),
    @XmlEnumValue("Fatal")
    FATAL("Fatal");
    private final String value;

    ErrorLevel(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ErrorLevel fromValue(String v) {
        for (ErrorLevel c: ErrorLevel.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
