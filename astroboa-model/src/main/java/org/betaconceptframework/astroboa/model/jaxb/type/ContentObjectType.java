/*
 * Copyright (C) 2005-2011 BetaCONCEPT LP.
 *
 * This file is part of Astroboa.
 *
 * Astroboa is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Astroboa is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Astroboa.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.betaconceptframework.astroboa.model.jaxb.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.betaconceptframework.astroboa.api.model.RepositoryUser;
import org.betaconceptframework.astroboa.model.jaxb.adapter.RepositoryUserAdapter;

/**
 * @author Gregory Chomatas (gchomatas@betaconcept.com)
 * @author Savvas Triantafyllou (striantafyllou@betaconcept.com)
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contentObjectType", propOrder = {
		"systemName",
		"contentObjectTypeName",
    "owner",
    "cmsProperties"
})
public class ContentObjectType extends AstroboaEntityType{

	/**
	 * 
	 */
	@XmlTransient
	private static final long serialVersionUID = 4114438247709387539L;
	
	@XmlAttribute
	private String systemName;

	@XmlAttribute
	private String contentObjectTypeName;

	@XmlElement
	@XmlJavaTypeAdapter(value=RepositoryUserAdapter.class)
	private RepositoryUser owner;
    
    @XmlAnyElement(lax = true)
    private List<CmsPropertyType> cmsProperties;

	public RepositoryUser getOwner() {
		return owner;
	}


	public void setOwner(RepositoryUser owner) {
		this.owner = owner;
	}



	public String getContentObjectTypeName() {
		return contentObjectTypeName;
	}



	public void setContentObjectTypeName(String contentObjectTypeName) {
		this.contentObjectTypeName = contentObjectTypeName;
	}



	public String getSystemName() {
		return systemName;
	}



	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}


	public List<CmsPropertyType> getCmsProperties() {
        if (cmsProperties == null) {
            cmsProperties = new ArrayList<CmsPropertyType>();
        }
        return this.cmsProperties;
    }
	
}