//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.03.19 at 09:44:46 μμ EET 
//


package org.betaconceptframework.astroboa.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element name="repository" type="{http://www.betaconceptframework.org/schema/astroboa/configuration/repositories}RepositoryType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="serverURL" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="jaasApplicationPolicyName" type="{http://www.w3.org/2001/XMLSchema}string" default="astroboa" />
 *       &lt;attribute name="authenticationTokenTimeout" type="{http://www.w3.org/2001/XMLSchema}int" default="30" />
 *       &lt;attribute name="identity-store-repository-id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="external-identity-store-jndi-name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "repository"
})
@XmlRootElement(name = "repositories")
public class Repositories {

    @XmlElement(required = true)
    protected List<RepositoryType> repository;
    @XmlAttribute(name = "serverURL", required = true)
    @XmlSchemaType(name = "anyURI")
    protected String serverURL;
    @XmlAttribute(name = "jaasApplicationPolicyName")
    protected String jaasApplicationPolicyName;
    @XmlAttribute(name = "authenticationTokenTimeout")
    protected Integer authenticationTokenTimeout;
    @XmlAttribute(name = "identity-store-repository-id")
    protected String identityStoreRepositoryId;
    @XmlAttribute(name = "external-identity-store-jndi-name")
    protected String externalIdentityStoreJndiName;

    /**
     * Gets the value of the repository property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the repository property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRepository().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RepositoryType }
     * 
     * 
     */
    public List<RepositoryType> getRepository() {
        if (repository == null) {
            repository = new ArrayList<RepositoryType>();
        }
        return this.repository;
    }

    /**
     * Gets the value of the serverURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServerURL() {
        return serverURL;
    }

    /**
     * Sets the value of the serverURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServerURL(String value) {
        this.serverURL = value;
    }

    /**
     * Gets the value of the jaasApplicationPolicyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getJaasApplicationPolicyName() {
        if (jaasApplicationPolicyName == null) {
            return "astroboa";
        } else {
            return jaasApplicationPolicyName;
        }
    }

    /**
     * Sets the value of the jaasApplicationPolicyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setJaasApplicationPolicyName(String value) {
        this.jaasApplicationPolicyName = value;
    }

    /**
     * Gets the value of the authenticationTokenTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getAuthenticationTokenTimeout() {
        if (authenticationTokenTimeout == null) {
            return  30;
        } else {
            return authenticationTokenTimeout;
        }
    }

    /**
     * Sets the value of the authenticationTokenTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAuthenticationTokenTimeout(Integer value) {
        this.authenticationTokenTimeout = value;
    }

    /**
     * Gets the value of the identityStoreRepositoryId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentityStoreRepositoryId() {
        return identityStoreRepositoryId;
    }

    /**
     * Sets the value of the identityStoreRepositoryId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentityStoreRepositoryId(String value) {
        this.identityStoreRepositoryId = value;
    }

    /**
     * Gets the value of the externalIdentityStoreJndiName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExternalIdentityStoreJndiName() {
        return externalIdentityStoreJndiName;
    }

    /**
     * Sets the value of the externalIdentityStoreJndiName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExternalIdentityStoreJndiName(String value) {
        this.externalIdentityStoreJndiName = value;
    }

}