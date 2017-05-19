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
 * <p>Java class for DataType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DataType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Journal"/>
 *     &lt;enumeration value="Database"/>
 *     &lt;enumeration value="Platform"/>
 *     &lt;enumeration value="Book"/>
 *     &lt;enumeration value="Collection"/>
 *     &lt;enumeration value="Multimedia"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "DataType")
@XmlEnum
public enum DataType {

    @XmlEnumValue("Journal")
    JOURNAL("Journal"),
    @XmlEnumValue("Database")
    DATABASE("Database"),
    @XmlEnumValue("Platform")
    PLATFORM("Platform"),
    @XmlEnumValue("Book")
    BOOK("Book"),
    @XmlEnumValue("Collection")
    COLLECTION("Collection"),
    @XmlEnumValue("Multimedia")
    MULTIMEDIA("Multimedia");
    private final String value;

    DataType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static DataType fromValue(String v) {
        for (DataType c: DataType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
