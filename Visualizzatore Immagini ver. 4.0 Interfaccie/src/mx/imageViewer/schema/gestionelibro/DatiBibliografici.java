//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.11 at 01:03:27 PM CEST 
//


package mx.imageViewer.schema.gestionelibro;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="autore" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="titolo" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="urlDatiBibliografici" type="{http://www.w3.org/2001/XMLSchema}anyURI" minOccurs="0"/>
 *         &lt;element name="pubblicazione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="unitaFisica" type="{http://www.w3.org/2001/XMLSchema}anyType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "autore",
    "titolo",
    "urlDatiBibliografici",
    "pubblicazione",
    "unitaFisica"
})
@XmlRootElement(name = "datiBibliografici")
public class DatiBibliografici {

    protected String autore;
    @XmlElement(required = true)
    protected String titolo;
    @XmlSchemaType(name = "anyURI")
    protected String urlDatiBibliografici;
    protected String pubblicazione;
    protected Object unitaFisica;

    /**
     * Gets the value of the autore property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAutore() {
        return autore;
    }

    /**
     * Sets the value of the autore property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAutore(String value) {
        this.autore = value;
    }

    /**
     * Gets the value of the titolo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitolo() {
        return titolo;
    }

    /**
     * Sets the value of the titolo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitolo(String value) {
        this.titolo = value;
    }

    /**
     * Gets the value of the urlDatiBibliografici property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrlDatiBibliografici() {
        return urlDatiBibliografici;
    }

    /**
     * Sets the value of the urlDatiBibliografici property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrlDatiBibliografici(String value) {
        this.urlDatiBibliografici = value;
    }

    /**
     * Gets the value of the pubblicazione property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPubblicazione() {
        return pubblicazione;
    }

    /**
     * Sets the value of the pubblicazione property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPubblicazione(String value) {
        this.pubblicazione = value;
    }

    /**
     * Gets the value of the unitaFisica property.
     * 
     * @return
     *     possible object is
     *     {@link Object }
     *     
     */
    public Object getUnitaFisica() {
        return unitaFisica;
    }

    /**
     * Sets the value of the unitaFisica property.
     * 
     * @param value
     *     allowed object is
     *     {@link Object }
     *     
     */
    public void setUnitaFisica(Object value) {
        this.unitaFisica = value;
    }

}
