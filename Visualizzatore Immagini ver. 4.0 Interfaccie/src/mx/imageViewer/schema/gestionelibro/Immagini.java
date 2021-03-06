//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.09.11 at 01:03:27 PM CEST 
//


package mx.imageViewer.schema.gestionelibro;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
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
 *         &lt;element name="immagine" maxOccurs="unbounded">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element ref="{http://www.imageViewer.mx/schema/gestioneLibro}nomenclatura"/>
 *                   &lt;element name="altezza" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="larghezza" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                 &lt;/sequence>
 *                 &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute ref="{http://www.imageViewer.mx/schema/gestioneLibro}sequenza"/>
 *                 &lt;attribute name="visPaginaDoppia" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="numImg" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="isCostola" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "immagine"
})
@XmlRootElement(name = "immagini")
public class Immagini {

    @XmlElement(required = true)
    protected List<Immagini.Immagine> immagine;
    @XmlAttribute(name = "numImg")
    protected Integer numImg;
    @XmlAttribute(name = "isCostola")
    protected Boolean isCostola;

    /**
     * Gets the value of the immagine property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the immagine property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getImmagine().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Immagini.Immagine }
     * 
     * 
     */
    public List<Immagini.Immagine> getImmagine() {
        if (immagine == null) {
            immagine = new ArrayList<Immagini.Immagine>();
        }
        return this.immagine;
    }

    /**
     * Gets the value of the numImg property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNumImg() {
        return numImg;
    }

    /**
     * Sets the value of the numImg property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNumImg(Integer value) {
        this.numImg = value;
    }

    /**
     * Gets the value of the isCostola property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsCostola() {
        if (isCostola == null) {
            return false;
        } else {
            return isCostola;
        }
    }

    /**
     * Sets the value of the isCostola property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCostola(Boolean value) {
        this.isCostola = value;
    }


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
     *         &lt;element ref="{http://www.imageViewer.mx/schema/gestioneLibro}nomenclatura"/>
     *         &lt;element name="altezza" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="larghezza" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *       &lt;/sequence>
     *       &lt;attribute name="ID" type="{http://www.w3.org/2001/XMLSchema}string" />
     *       &lt;attribute ref="{http://www.imageViewer.mx/schema/gestioneLibro}sequenza"/>
     *       &lt;attribute name="visPaginaDoppia" type="{http://www.w3.org/2001/XMLSchema}boolean" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "nomenclatura",
        "altezza",
        "larghezza"
    })
    public static class Immagine {

        @XmlElement(namespace = "http://www.imageViewer.mx/schema/gestioneLibro", required = true)
        protected String nomenclatura;
        protected int altezza;
        protected int larghezza;
        @XmlAttribute(name = "ID")
        protected String id;
        @XmlAttribute(name = "sequenza", namespace = "http://www.imageViewer.mx/schema/gestioneLibro")
        protected Integer sequenza;
        @XmlAttribute(name = "visPaginaDoppia")
        protected Boolean visPaginaDoppia;

        /**
         * Nomenclatura dell'immagine da visualizzare
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getNomenclatura() {
            return nomenclatura;
        }

        /**
         * Sets the value of the nomenclatura property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setNomenclatura(String value) {
            this.nomenclatura = value;
        }

        /**
         * Gets the value of the altezza property.
         * 
         */
        public int getAltezza() {
            return altezza;
        }

        /**
         * Sets the value of the altezza property.
         * 
         */
        public void setAltezza(int value) {
            this.altezza = value;
        }

        /**
         * Gets the value of the larghezza property.
         * 
         */
        public int getLarghezza() {
            return larghezza;
        }

        /**
         * Sets the value of the larghezza property.
         * 
         */
        public void setLarghezza(int value) {
            this.larghezza = value;
        }

        /**
         * Gets the value of the id property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getID() {
            return id;
        }

        /**
         * Sets the value of the id property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setID(String value) {
            this.id = value;
        }

        /**
         * Gets the value of the sequenza property.
         * 
         * @return
         *     possible object is
         *     {@link Integer }
         *     
         */
        public Integer getSequenza() {
            return sequenza;
        }

        /**
         * Sets the value of the sequenza property.
         * 
         * @param value
         *     allowed object is
         *     {@link Integer }
         *     
         */
        public void setSequenza(Integer value) {
            this.sequenza = value;
        }

        /**
         * Gets the value of the visPaginaDoppia property.
         * 
         * @return
         *     possible object is
         *     {@link Boolean }
         *     
         */
        public Boolean isVisPaginaDoppia() {
            return visPaginaDoppia;
        }

        /**
         * Sets the value of the visPaginaDoppia property.
         * 
         * @param value
         *     allowed object is
         *     {@link Boolean }
         *     
         */
        public void setVisPaginaDoppia(Boolean value) {
            this.visPaginaDoppia = value;
        }

    }

}
