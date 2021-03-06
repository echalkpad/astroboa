/*
 * Copyright (C) 2005-2012 BetaCONCEPT Limited
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

package org.betaconceptframework.astroboa.engine.service.jcr;


import java.util.List;
import java.util.Map;

import org.betaconceptframework.astroboa.api.model.ValueType;
import org.betaconceptframework.astroboa.api.model.definition.CmsPropertyDefinition;
import org.betaconceptframework.astroboa.api.model.definition.ComplexCmsPropertyDefinition;
import org.betaconceptframework.astroboa.api.model.definition.ContentObjectTypeDefinition;
import org.betaconceptframework.astroboa.api.model.exception.CmsException;
import org.betaconceptframework.astroboa.api.model.io.ResourceRepresentationType;
import org.betaconceptframework.astroboa.api.service.DefinitionService;
import org.betaconceptframework.astroboa.engine.definition.ContentDefinitionConfiguration;
import org.betaconceptframework.astroboa.engine.jcr.dao.ContentDefinitionDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Gregory Chomatas (gchomatas@betaconcept.com)
 * @author Savvas Triantafyllou (striantafyllou@betaconcept.com)
 * 
 */
class DefinitionServiceImpl implements DefinitionService {

	@Autowired
	private ContentDefinitionDao contentDefinitionDao;
	
	@Autowired
	private ContentDefinitionConfiguration contentDefinitionConfiguration;

	public boolean hasContentObjectTypeDefinition(String contentObjectTypeDefinitionName)  {

		try {
			return contentDefinitionDao.hasContentObjectTypeDefinition(contentObjectTypeDefinitionName);
		}
		catch(CmsException e){
			throw e;
		}
		catch (Exception e) { 
			throw new CmsException(e); 		
		}
	}

	public List<String> getContentObjectTypes()  {
		try {
			return contentDefinitionDao.getContentObjectTypes();
		}
		catch(CmsException e){
			throw e;
		}
		catch (Exception e) { 
			throw new CmsException(e); 		
		}
	}

	public List<ComplexCmsPropertyDefinition> getAspectDefinitionsSortedByLocale(
			List<String> aspects, String locale) {
		try {
			return contentDefinitionDao.getAspectDefinitionsSortedByLocale(aspects, locale);
		}
		catch(CmsException e){
			throw e;
		}
		catch (Exception e) { 
			throw new CmsException(e); 		
		}

	}

	public List<ComplexCmsPropertyDefinition> getAvailableAspectDefinitionsSortedByLocale(String locale){
		try {
			return contentDefinitionDao.getAvailableAspectDefinitionsSortedByLocale(locale);
		}
		catch(CmsException e){
			throw e;
		}
		catch (Exception e) { 
			throw new CmsException(e); 		
		} 
	}

	@Override
	public Map<String, List<String>> getTopicPropertyPathsPerTaxonomies() {
		return contentDefinitionDao.getTopicPropertyPathsPerTaxonomies();
	}

	@Override
	public List<String> getMultivalueProperties() {
		return contentDefinitionDao.getMultivalueProperties();
	}

	@Override
	public Map<String, List<String>> getContentTypeHierarchy() {
		return contentDefinitionDao.getContentTypeHierarchy();
	}

	@Override
	public <T> T getCmsDefinition(String fullPropertyDefinitionPath,
			ResourceRepresentationType<T> output, boolean prettyPrint) {
		try{
			return contentDefinitionDao.getCmsDefinition(fullPropertyDefinitionPath, output, prettyPrint);
		}
		catch(CmsException e){
			throw e;
		}
		catch (Exception e) { 
			throw new CmsException(e); 		
		}
	}

	@Override
	public boolean validateDefinintion(String definition, String definitionFileName) {
		
		try{
			return contentDefinitionConfiguration.definitionFileForActiveRepositoryIsValid(definition, definitionFileName);
		}
		catch(CmsException e){
			throw e;
		}
		catch (Exception e) { 
			throw new CmsException(e); 		
		}

	}

	@Override
	public Map<String, Integer> getDefinitionHierarchyDepthPerContentType() {
		return contentDefinitionDao.getDefinitionHierarchyDepthPerContentType();
	}

	@Override
	public Integer getDefinitionHierarchyDepthForContentType(String contentType) {
		return contentDefinitionDao.getDefinitionHierarchyDepthForContentType(contentType);
	}

	@Override
	public ValueType getTypeForProperty(String contentType, String propertyPath) {
		return contentDefinitionDao.getTypeForProperty(contentType, propertyPath);
	}
}
