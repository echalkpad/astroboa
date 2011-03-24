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
package org.betaconceptframework.astroboa.model.jaxb.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.betaconceptframework.astroboa.api.model.Space;
import org.betaconceptframework.astroboa.api.model.io.ResourceRepresentationType;
import org.betaconceptframework.astroboa.model.factory.CmsRepositoryEntityFactoryForActiveClient;
import org.betaconceptframework.astroboa.model.jaxb.type.SpaceType;

/**
 * Used to control marshalling mainly of a space in order to avoid circular
 * problems.
 * 
 * Although both types are known to JAXB context, 
 * we copy provided Type to a new one which has less
 * @author Gregory Chomatas (gchomatas@betaconcept.com)
 * @author Savvas Triantafyllou (striantafyllou@betaconcept.com)
 * 
 */
public class SpaceAdapter extends XmlAdapter<SpaceType, Space>{

	@Override
	public SpaceType marshal(Space space) throws Exception {
		return marshal(space, ResourceRepresentationType.XML);
	}
		
	public SpaceType marshal(Space space, ResourceRepresentationType<?>  resourceRepresentationType) throws Exception {

		if (space != null){
			SpaceType spaceType = new SpaceType();
			spaceType.setId(space.getId());
			spaceType.setName(space.getName());
			spaceType.setSystemBuiltinEntity(space.isSystemBuiltinEntity());
			spaceType.getLocalizedLabels().putAll(space.getLocalizedLabels());
			spaceType.setOwner(space.getOwner());
			spaceType.setUrl(space.getResourceApiURL(resourceRepresentationType, false));

			if (space.getNumberOfChildren() > 0){
				spaceType.setNumberOfChildren(space.getNumberOfChildren());
			}

			return spaceType;
		}

		return null;
	}

	@Override
	public Space unmarshal(SpaceType spaceType) throws Exception {

		if (spaceType != null){

			Space space = (Space) spaceType.getCmsRepositoryEntityFromContextUsingCmsIdentifierOrReference();

			if (space != null){
				return space;
			}

			if (spaceType.getName() != null){
				space = (Space) spaceType.getCmsRepositoryEntityFromContextUsingKey(spaceType.getName());

				if (space != null){
					return space;
				}
			}


			space = CmsRepositoryEntityFactoryForActiveClient.INSTANCE.getFactory().newSpace();

			space.setId(spaceType.getId());
			space.setName(spaceType.getName());
			space.setSystemBuiltinEntity(spaceType.isSystemBuiltinEntity());
			space.getLocalizedLabels().putAll(spaceType.getLocalizedLabels());
			space.setOwner(spaceType.getOwner());

			spaceType.registerCmsRepositoryEntityToContext(space);

			return (Space) space;
		}

		return null;
	}

}