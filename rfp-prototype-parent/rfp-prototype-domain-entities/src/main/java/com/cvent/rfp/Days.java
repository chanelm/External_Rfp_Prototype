//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.04.23 at 12:32:42 PM EDT 
//


package com.cvent.rfp;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.cvent.rfp.serializers.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


/**
 * <p>Java class for Days complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Days">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DayNumber" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="Date" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Days", propOrder = {
    "dayNumber",
    "date"
})
public class Days {

    @XmlElement(name = "DayNumber")
    @JsonProperty("")
    @Min(value = 0, payload = {

    }, groups = {

    })
    protected int dayNumber;
    @XmlElement(name = "Date", required = true)
    @JsonProperty("")
    @JsonSerialize(using = JsonDateSerializer.class)
    protected String date;

    /**
     * Gets the value of the dayNumber property.
     * 
     */
    public int getDayNumber() {
        return dayNumber;
    }

    /**
     * Sets the value of the dayNumber property.
     * 
     */
    public void setDayNumber(int value) {
        this.dayNumber = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

}
