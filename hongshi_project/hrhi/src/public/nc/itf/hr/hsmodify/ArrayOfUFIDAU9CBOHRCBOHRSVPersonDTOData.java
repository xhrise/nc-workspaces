
package nc.itf.hr.hsmodify;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ArrayOfUFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfUFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData" type="{http://www.UFIDA.org/EntityData}UFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfUFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData", namespace = "http://www.UFIDA.org/EntityData", propOrder = {
    "ufidau9CBOHRCBOHRSVPersonDTOData"
})
public class ArrayOfUFIDAU9CBOHRCBOHRSVPersonDTOData {

    @XmlElement(name = "UFIDA.U9.CBO.HR.CBOHRSV.PersonDTOData", nillable = true)
    protected List<UFIDAU9CBOHRCBOHRSVPersonDTOData> ufidau9CBOHRCBOHRSVPersonDTOData;

    /**
     * Gets the value of the ufidau9CBOHRCBOHRSVPersonDTOData property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ufidau9CBOHRCBOHRSVPersonDTOData property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUFIDAU9CBOHRCBOHRSVPersonDTOData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UFIDAU9CBOHRCBOHRSVPersonDTOData }
     * 
     * 
     */
    public List<UFIDAU9CBOHRCBOHRSVPersonDTOData> getUFIDAU9CBOHRCBOHRSVPersonDTOData() {
        if (ufidau9CBOHRCBOHRSVPersonDTOData == null) {
            ufidau9CBOHRCBOHRSVPersonDTOData = new ArrayList<UFIDAU9CBOHRCBOHRSVPersonDTOData>();
        }
        return this.ufidau9CBOHRCBOHRSVPersonDTOData;
    }

}
