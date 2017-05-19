//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.16 at 02:53:56 PM MESZ 
//


package unidue.ub.sushiclient.service;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IdentifierType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="IdentifierType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Online_ISSN"/>
 *     &lt;enumeration value="Print_ISSN"/>
 *     &lt;enumeration value="EISSN"/>
 *     &lt;enumeration value="ISSN"/>
 *     &lt;enumeration value="ISBN"/>
 *     &lt;enumeration value="Online_ISBN"/>
 *     &lt;enumeration value="Print_ISBN"/>
 *     &lt;enumeration value="DOI"/>
 *     &lt;enumeration value="Proprietary"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "IdentifierType")
@XmlEnum
public enum IdentifierType {

    @XmlEnumValue("Online_ISSN")
    ONLINE_ISSN("Online_ISSN"),
    @XmlEnumValue("Print_ISSN")
    PRINT_ISSN("Print_ISSN"),
    EISSN("EISSN"),
    ISSN("ISSN"),
    ISBN("ISBN"),
    @XmlEnumValue("Online_ISBN")
    ONLINE_ISBN("Online_ISBN"),
    @XmlEnumValue("Print_ISBN")
    PRINT_ISBN("Print_ISBN"),
    DOI("DOI"),
    @XmlEnumValue("Proprietary")
    PROPRIETARY("Proprietary");
    private final String value;

    IdentifierType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static IdentifierType fromValue(String v) {
        for (IdentifierType c: IdentifierType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
