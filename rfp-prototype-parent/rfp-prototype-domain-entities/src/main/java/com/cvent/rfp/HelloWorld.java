//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2014.04.22 at 02:47:40 PM EDT 
//


package com.cvent.rfp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * <p>Java class for HelloWorld complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HelloWorld">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hello" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HelloWorld", propOrder = {
    "hello"
})
public class HelloWorld {

    @XmlElement(required = true)
    @JsonProperty("")
    protected String hello;

    /**
     * Gets the value of the hello property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHello() {
        return hello;
    }

    /**
     * Sets the value of the hello property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHello(String value) {
        this.hello = value;
    }

}