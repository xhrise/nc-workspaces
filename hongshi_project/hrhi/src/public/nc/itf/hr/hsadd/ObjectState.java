
package nc.itf.hr.hsadd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ObjectState.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ObjectState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="NotExist"/>
 *     &lt;enumeration value="Unchanged"/>
 *     &lt;enumeration value="Unknown"/>
 *     &lt;enumeration value="Inserted"/>
 *     &lt;enumeration value="Updated"/>
 *     &lt;enumeration value="Deleted"/>
 *     &lt;enumeration value="Operating"/>
 *     &lt;enumeration value="NeedLoad"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ObjectState", namespace = "http://schemas.datacontract.org/2004/07/UFSoft.UBF.PL.Engine")
@XmlEnum
public enum ObjectState {

    @XmlEnumValue("NotExist")
    NOT_EXIST("NotExist"),
    @XmlEnumValue("Unchanged")
    UNCHANGED("Unchanged"),
    @XmlEnumValue("Unknown")
    UNKNOWN("Unknown"),
    @XmlEnumValue("Inserted")
    INSERTED("Inserted"),
    @XmlEnumValue("Updated")
    UPDATED("Updated"),
    @XmlEnumValue("Deleted")
    DELETED("Deleted"),
    @XmlEnumValue("Operating")
    OPERATING("Operating"),
    @XmlEnumValue("NeedLoad")
    NEED_LOAD("NeedLoad");
    private final String value;

    ObjectState(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ObjectState fromValue(String v) {
        for (ObjectState c: ObjectState.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
