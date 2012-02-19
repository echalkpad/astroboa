//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.10.31 at 01:02:45 PM EET 
//


package org.betaconceptframework.astroboa.configuration;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.betaconceptframework.astroboa.configuration package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.betaconceptframework.astroboa.configuration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link LocalizationType }
     * 
     */
    public LocalizationType createLocalizationType() {
        return new LocalizationType();
    }

    /**
     * Create an instance of {@link SecurityType }
     * 
     */
    public SecurityType createSecurityType() {
        return new SecurityType();
    }

    /**
     * Create an instance of {@link SecurityType.SecretUserKeyList }
     * 
     */
    public SecurityType.SecretUserKeyList createSecurityTypeSecretUserKeyList() {
        return new SecurityType.SecretUserKeyList();
    }

    /**
     * Create an instance of {@link SecurityType.PermanentUserKeyList }
     * 
     */
    public SecurityType.PermanentUserKeyList createSecurityTypePermanentUserKeyList() {
        return new SecurityType.PermanentUserKeyList();
    }

    /**
     * Create an instance of {@link Repositories }
     * 
     */
    public Repositories createRepositories() {
        return new Repositories();
    }

    /**
     * Create an instance of {@link RepositoryType }
     * 
     */
    public RepositoryType createRepositoryType() {
        return new RepositoryType();
    }

    /**
     * Create an instance of {@link JcrCacheType }
     * 
     */
    public JcrCacheType createJcrCacheType() {
        return new JcrCacheType();
    }

    /**
     * Create an instance of {@link LocalizationType.Label }
     * 
     */
    public LocalizationType.Label createLocalizationTypeLabel() {
        return new LocalizationType.Label();
    }

    /**
     * Create an instance of {@link SecurityType.SecretUserKeyList.AdministratorSecretKey }
     * 
     */
    public SecurityType.SecretUserKeyList.AdministratorSecretKey createSecurityTypeSecretUserKeyListAdministratorSecretKey() {
        return new SecurityType.SecretUserKeyList.AdministratorSecretKey();
    }

    /**
     * Create an instance of {@link SecurityType.SecretUserKeyList.SecretUserKey }
     * 
     */
    public SecurityType.SecretUserKeyList.SecretUserKey createSecurityTypeSecretUserKeyListSecretUserKey() {
        return new SecurityType.SecretUserKeyList.SecretUserKey();
    }

    /**
     * Create an instance of {@link SecurityType.PermanentUserKeyList.PermanentUserKey }
     * 
     */
    public SecurityType.PermanentUserKeyList.PermanentUserKey createSecurityTypePermanentUserKeyListPermanentUserKey() {
        return new SecurityType.PermanentUserKeyList.PermanentUserKey();
    }

}
