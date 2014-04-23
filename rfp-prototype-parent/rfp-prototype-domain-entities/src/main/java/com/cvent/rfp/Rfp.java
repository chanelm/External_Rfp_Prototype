//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.04.23 at 05:49:48 PM EDT 
//


package com.cvent.rfp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * <p>Java class for Rfp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Rfp">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="rfpStub" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="rfpName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Rfp", propOrder = {
    "accountId",
    "rfpStub",
    "rfpName"
})
public class Rfp {

    @JsonProperty("")
    protected long accountId;
    @XmlElement(required = true)
    @JsonProperty("")
    protected String rfpStub;
    @XmlElement(required = true)
    @JsonProperty("")
    protected String rfpName;

    /**
     * Gets the value of the accountId property.
     * 
     */
    public long getAccountId() {
        return accountId;
    }

    /**
     * Sets the value of the accountId property.
     * 
     */
    public void setAccountId(long value) {
        this.accountId = value;
    }

    /**
     * Gets the value of the rfpStub property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfpStub() {
        return rfpStub;
    }

    /**
     * Sets the value of the rfpStub property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfpStub(String value) {
        this.rfpStub = value;
    }

    /**
     * Gets the value of the rfpName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfpName() {
        return rfpName;
    }

    /**
     * Sets the value of the rfpName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfpName(String value) {
        this.rfpName = value;
    }

}
