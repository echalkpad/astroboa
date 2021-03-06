/*
 * Copyright (C) 2005-2012 BetaCONCEPT Limited
 *
 * This file is part of Astroboa.
 *
 * Astroboa is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Astroboa is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Astroboa.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.betaconceptframework.astroboa.serializer;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.betaconceptframework.astroboa.api.model.io.ResourceRepresentationType;
import org.betaconceptframework.astroboa.api.service.DefinitionService;
import org.betaconceptframework.astroboa.configuration.RepositoryRegistry;
import org.betaconceptframework.astroboa.util.CmsConstants;


/**
 * 
 * Class responsible to serialize the content model of a repository
 * into one XML or JSON file
 * 
 * @author Gregory Chomatas (gchomatas@betaconcept.com)
 * @author Savvas Triantafyllou (striantafyllou@betaconcept.com)
 * 
 */

public class ModelSerializer extends AbstractSerializer{

	private DefinitionService definitionService;
	private String repository;
	

	public ModelSerializer(boolean prettyPrint, boolean jsonOutput, DefinitionService definitionService, String repository) {
	
		super(prettyPrint, jsonOutput);
		
		this.repository = repository;

		this.definitionService = definitionService;
	}

	public String serialize(){
		
		
		if (! outputIsJSON()){
			startElement("astroboa", true, true);
		}
		
		writeAttribute("server", RepositoryRegistry.INSTANCE.getDefaultServerURL());
		writeAttribute("repository", repository);
		
		if (! outputIsJSON()){
			endElement("astroboa", true, false);	
		}
		
		serializeModel();

		if (! outputIsJSON()){
			endElement("astroboa", false, true);
		}
		
		return super.serialize();

		
	}

	private void serializeModel() {

		ResourceRepresentationType<String> output = outputIsJSON() ? ResourceRepresentationType.JSON : ResourceRepresentationType.XML;
		boolean prettyPrintEnabled = prettyPrintEnabled();
		
		boolean firstType = true;

		
		startElement(CmsConstants.ARRAY_OF_OBJECT_TYPE_ELEMENT_NAME, false, true);
		
		if (outputIsJSON()){
			startArray(CmsConstants.OBJECT_TYPE_ELEMENT_NAME);
		}
		
		List<String> contentTypes = definitionService.getContentObjectTypes();

		for (String contentType : contentTypes){
			
			String contentTypeAsXmlorJson = definitionService.getCmsDefinition(contentType, output, prettyPrintEnabled);
		
			if (!outputIsJSON()){
				//Remove XML headers
				contentTypeAsXmlorJson = StringUtils.remove(contentTypeAsXmlorJson, "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>");
			}
			else{
				//Remove root element
				//contentTypeAsXmlorJson = contentTypeAsXmlorJson.replaceFirst("\\{","").replaceFirst("(.*?):", "");
				//Remove end
				//contentTypeAsXmlorJson = StringUtils.removeEnd(contentTypeAsXmlorJson, "}");
				
				if (!firstType){
					contentTypeAsXmlorJson = ","+contentTypeAsXmlorJson;
				}
				else{
					firstType = false;
				}
			}
			
			
			writeContent(contentTypeAsXmlorJson, false);
			
		}
		
		if (outputIsJSON()){
			endArray(CmsConstants.OBJECT_TYPE_ELEMENT_NAME);
		}

		endElement(CmsConstants.ARRAY_OF_OBJECT_TYPE_ELEMENT_NAME, false, true);
		
	}
}
