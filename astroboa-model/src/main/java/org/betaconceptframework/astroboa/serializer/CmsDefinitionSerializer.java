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
package org.betaconceptframework.astroboa.serializer;

import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.betaconceptframework.astroboa.api.model.definition.BooleanPropertyDefinition;
import org.betaconceptframework.astroboa.api.model.definition.CalendarPropertyDefinition;
import org.betaconceptframework.astroboa.api.model.definition.CmsPropertyDefinition;
import org.betaconceptframework.astroboa.api.model.definition.ComplexCmsPropertyDefinition;
import org.betaconceptframework.astroboa.api.model.definition.ContentObjectTypeDefinition;
import org.betaconceptframework.astroboa.api.model.definition.DoublePropertyDefinition;
import org.betaconceptframework.astroboa.api.model.definition.LocalizableCmsDefinition;
import org.betaconceptframework.astroboa.api.model.definition.LongPropertyDefinition;
import org.betaconceptframework.astroboa.api.model.definition.ObjectReferencePropertyDefinition;
import org.betaconceptframework.astroboa.api.model.definition.SimpleCmsPropertyDefinition;
import org.betaconceptframework.astroboa.api.model.definition.StringPropertyDefinition;
import org.betaconceptframework.astroboa.api.model.definition.TopicReferencePropertyDefinition;
import org.betaconceptframework.astroboa.api.model.io.ResourceRepresentationType;
import org.betaconceptframework.astroboa.commons.visitor.AbstractCmsPropertyDefinitionVisitor;
import org.betaconceptframework.astroboa.context.AstroboaClientContextHolder;
import org.betaconceptframework.astroboa.context.RepositoryContext;
import org.betaconceptframework.astroboa.model.impl.definition.DoublePropertyDefinitionImpl;
import org.betaconceptframework.astroboa.model.impl.definition.LongPropertyDefinitionImpl;

/**
 * 
 * Class responsible to export definition to XML or JSON
 * 
 * Currently only JSON is supported
 * 
 * @author Gregory Chomatas (gchomatas@betaconcept.com)
 * @author Savvas Triantafyllou (striantafyllou@betaconcept.com)
 * 
 */
public class CmsDefinitionSerializer extends AbstractCmsPropertyDefinitionVisitor{

	private MarkerSerializer serializer = null;
	
	private LocalizableCmsDefinition rootDefinition;
	
	public CmsDefinitionSerializer(boolean prettyPrint, boolean jsonOutput) {
		
		serializer = new MarkerSerializer(prettyPrint, jsonOutput);

	}

	@Override
	public void visit(ContentObjectTypeDefinition contentObjectTypeDefinition) {
		
		serializer.startElement(contentObjectTypeDefinition.getName(), true, true);

		rootDefinition = contentObjectTypeDefinition;
		
		exportDefinitionObjectAndBasicProperties(contentObjectTypeDefinition);
		
		if (!serializer.outputIsJSON()){
			serializer.endElement(contentObjectTypeDefinition.getName(), true, false);
		}

		exportDisplayName(contentObjectTypeDefinition);

	
	}

	@Override
	public void visitComplexPropertyDefinition(ComplexCmsPropertyDefinition complexPropertyDefinition) {

		
		
		if (rootDefinition != null){
			serializer.startElement("property", true, false);
		}
		else{
			serializer.startElement(complexPropertyDefinition.getName(), true, true);
			rootDefinition = complexPropertyDefinition;
		}
		
		exportDefinitionObjectAndBasicProperties(complexPropertyDefinition);
		
		if (!serializer.outputIsJSON()){
			serializer.endElement("property", true, false);
		}

		exportDisplayName(complexPropertyDefinition);

	}

	@Override
	public <T> void visitSimplePropertyDefinition(
			SimpleCmsPropertyDefinition<T> simplePropertyDefinition) {
		
		
		
		if (simplePropertyDefinition != null){
			
			if (rootDefinition != null){
				serializer.startElement("property", true, false);
			}
			else{
				serializer.startElement(simplePropertyDefinition.getName(), true, true);
				rootDefinition = simplePropertyDefinition;
			}

			
			exportDefinitionObjectAndBasicProperties(simplePropertyDefinition);
			
			switch (simplePropertyDefinition.getValueType()) {
			case Boolean:
				serializeBooleanPropertyDefinition(simplePropertyDefinition);
				break;
			case Date:
				serializeDatePropertyDefinition(simplePropertyDefinition);
				break;
			case Double:
				serializeDoublePropertyDefinition(simplePropertyDefinition);
				break;
			case Long:
				serializeLongPropertyDefinition(simplePropertyDefinition);
				break;
			case String:
				serializeStringPropertyDefinition(simplePropertyDefinition);
				break;
			case ObjectReference:
				serializeObjectReferencePropertyDefinition(simplePropertyDefinition);
				break;
			case TopicReference:
				serializeTopicReferenceDefinition(simplePropertyDefinition);
				break;

			default:
				break;
			}
			
			if (!serializer.outputIsJSON()){
				serializer.endElement("property", true, false);
			}

			exportDisplayName(simplePropertyDefinition);

			serializer.endElement("property", false,true);
			
		}
		
	}

	private <T> void serializeTopicReferenceDefinition(
			SimpleCmsPropertyDefinition<T> simplePropertyDefinition) {
		if (CollectionUtils.isNotEmpty(((TopicReferencePropertyDefinition)simplePropertyDefinition).getAcceptedTaxonomies())){ 
			serializer.writeAttribute("acceptedTaxonomies",StringUtils.join(((TopicReferencePropertyDefinition)simplePropertyDefinition).getAcceptedTaxonomies(), ","));
		}
	}

	private <T> void serializeObjectReferencePropertyDefinition(
			SimpleCmsPropertyDefinition<T> simplePropertyDefinition) {
		if (CollectionUtils.isNotEmpty(((ObjectReferencePropertyDefinition)simplePropertyDefinition).getExpandedAcceptedContentTypes())){ 
			serializer.writeAttribute("acceptedContentTypes",StringUtils.join(((ObjectReferencePropertyDefinition)simplePropertyDefinition).getExpandedAcceptedContentTypes(),","));
		}
	}

	private <T> void serializeStringPropertyDefinition(
			SimpleCmsPropertyDefinition<T> simplePropertyDefinition) {
		StringPropertyDefinition stringDefinition = (StringPropertyDefinition)simplePropertyDefinition;
		
		if (stringDefinition.isSetDefaultValue()){
			serializer.writeAttribute("defaultValue",stringDefinition.getDefaultValue());
		}
		
		if (stringDefinition.getMinLength() != null){
			serializer.writeAttribute("minLength",String.valueOf(stringDefinition.getMinLength()));
		}

		if (stringDefinition.getMaxLength() != null){
			serializer.writeAttribute("maxLength",String.valueOf(stringDefinition.getMaxLength()));
		}
		
		if (stringDefinition.getPattern() != null){
			serializer.writeAttribute("pattern",String.valueOf(stringDefinition.getPattern()));
		}
		
		serializer.writeAttribute("stringFormat",String.valueOf(stringDefinition.getStringFormat()));
	}

	private <T> void serializeLongPropertyDefinition(
			SimpleCmsPropertyDefinition<T> simplePropertyDefinition) {
		LongPropertyDefinition longDefinition = (LongPropertyDefinition)simplePropertyDefinition;
		
		if (longDefinition.isSetDefaultValue()){
			serializer.writeAttribute("defaultValue",String.valueOf(longDefinition.getDefaultValue()));
		}
		
		if (longDefinition.getMinValue() != null && longDefinition.getMinValue() != Long.MIN_VALUE){
			serializer.writeAttribute("minValue",String.valueOf(longDefinition.getMinValue()));
			serializer.writeAttribute("minValueIsExclusive",String.valueOf(((LongPropertyDefinitionImpl)longDefinition).isMinValueExclusive()));
		}

		if (longDefinition.getMaxValue() != null && longDefinition.getMaxValue() != Long.MAX_VALUE){
			serializer.writeAttribute("maxValue",String.valueOf(longDefinition.getMaxValue()));
			serializer.writeAttribute("maxValueIsExclusive",String.valueOf(((LongPropertyDefinitionImpl)longDefinition).isMaxValueExclusive()));
		}
	}

	private <T> void serializeDoublePropertyDefinition(
			SimpleCmsPropertyDefinition<T> simplePropertyDefinition) {
		DoublePropertyDefinition doubleDefinition = (DoublePropertyDefinition)simplePropertyDefinition;
		
		if (doubleDefinition.isSetDefaultValue()){
			serializer.writeAttribute("defaultValue", String.valueOf(doubleDefinition.getDefaultValue()));
		}
		
		if (doubleDefinition.getMinValue() != null && doubleDefinition.getMinValue() != Double.MIN_VALUE){
			serializer.writeAttribute("minValue",String.valueOf(doubleDefinition.getMinValue()));
			serializer.writeAttribute("minValueIsExclusive",String.valueOf(((DoublePropertyDefinitionImpl)doubleDefinition).isMinValueExclusive()));
		}

		if (doubleDefinition.getMaxValue() != null && doubleDefinition.getMinValue() != Double.MAX_VALUE){
			serializer.writeAttribute("maxValue",String.valueOf(doubleDefinition.getMaxValue()));
			serializer.writeAttribute("maxValueIsExclusive",String.valueOf(((DoublePropertyDefinitionImpl)doubleDefinition).isMaxValueExclusive()));
		}
	}

	private <T> void serializeDatePropertyDefinition(
			SimpleCmsPropertyDefinition<T> simplePropertyDefinition) {
		CalendarPropertyDefinition calendarDefinition = (CalendarPropertyDefinition)simplePropertyDefinition;
		
		serializer.writeAttribute("pattern",calendarDefinition.getPattern());

		if (calendarDefinition.isSetDefaultValue()){
			serializer.writeAttribute("defaultValue", DateFormatUtils.format(calendarDefinition.getDefaultValue().getTimeInMillis(), calendarDefinition.getPattern()));
		}
	}

	private <T> void serializeBooleanPropertyDefinition(
			SimpleCmsPropertyDefinition<T> simplePropertyDefinition) {
		BooleanPropertyDefinition booleanDefinition = (BooleanPropertyDefinition)simplePropertyDefinition;
		
		if (booleanDefinition.isSetDefaultValue()){
			serializer.writeAttribute("defaultValue", String.valueOf(booleanDefinition.getDefaultValue()));
		}
	}
	
	@Override
	public void finishedChildDefinitionsVisit(LocalizableCmsDefinition parentDefinition) {
		
		super.finishedChildDefinitionsVisit(parentDefinition);
		
		serializer.endArray("propertyList");
		
		if (rootDefinition != null && rootDefinition != parentDefinition){
			serializer.endElement("property", false, true);
		}

	}
	
	

	@Override
	public void startChildDefinitionsVisit(
			LocalizableCmsDefinition parentDefinition) {
		
		super.startChildDefinitionsVisit(parentDefinition);
		
		serializer.startArray("propertyList");
	}

	public String exportOutcome() {
		
		serializer.endElement(rootDefinition.getName(), false,true);
		
		return serializer.serialize();

	}
	
	private void exportDefinitionObjectAndBasicProperties(LocalizableCmsDefinition cmsDefinition) {

		//Name
		exportName(cmsDefinition);

		//Path
		exportPath(cmsDefinition);
			
		//Value Type
		exportValueType(cmsDefinition);
		
		//Url
		exportUrl(cmsDefinition);
		
		if (cmsDefinition instanceof CmsPropertyDefinition){
			//Cardinality
			exportCardinality((CmsPropertyDefinition)cmsDefinition);
		}
	}

	private void exportCardinality(CmsPropertyDefinition cmsDefinition) {
			serializer.writeAttribute("mandatory",String.valueOf(((CmsPropertyDefinition)cmsDefinition).isMandatory()));
			serializer.writeAttribute("multiple",String.valueOf(((CmsPropertyDefinition)cmsDefinition).isMultiple()));
	}

	private void exportUrl(LocalizableCmsDefinition cmsDefinition) {
		serializer.writeAttribute("url",cmsDefinition.url(serializer.outputIsJSON()? ResourceRepresentationType.JSON : ResourceRepresentationType.XML)); 
	}

	private void exportDisplayName(LocalizableCmsDefinition cmsDefinition) {
		if (cmsDefinition.getDisplayName() != null && cmsDefinition.getDisplayName().hasLocalizedLabels()){

			serializer.startElement("label",true,true);
			
			for (Entry<String,String> localizedLabel : cmsDefinition.getDisplayName().getLocalizedLabels().entrySet()){
				serializer.writeAttribute(localizedLabel.getKey(),localizedLabel.getValue());
			}
			
			serializer.endElement("label",true,true);
		}
	}

	private void exportValueType(LocalizableCmsDefinition cmsDefinition) {
		serializer.writeAttribute("valueType",cmsDefinition.getValueType().toString());
	}

	private void exportName(LocalizableCmsDefinition cmsDefinition) {
		serializer.writeAttribute("name",cmsDefinition.getName());
	}

	private void exportPath(LocalizableCmsDefinition cmsDefinition) {
		if (cmsDefinition instanceof CmsPropertyDefinition){
			serializer.writeAttribute("path",((CmsPropertyDefinition)cmsDefinition).getPath());
		}
		else{
			serializer.writeAttribute("path",cmsDefinition.getName());
		}
	}

	public String getServerURL() {
		RepositoryContext repositoryContext = AstroboaClientContextHolder.getRepositoryContextForActiveClient();
		if (repositoryContext != null && repositoryContext.getCmsRepository() != null && 
				StringUtils.isNotBlank(repositoryContext.getCmsRepository().getServerURL())){
			String serverURL = repositoryContext.getCmsRepository().getServerURL().trim();
			
			return serverURL.endsWith("/")? serverURL.substring(0, serverURL.length()-1) : serverURL; 
		}

		return null;
	}
	
	public String getRestfulApiBasePath() {
		
		RepositoryContext repositoryContext = AstroboaClientContextHolder.getRepositoryContextForActiveClient();
		if (
			repositoryContext != null && 
			repositoryContext.getCmsRepository() != null && 
			StringUtils.isNotBlank(repositoryContext.getCmsRepository().getRestfulApiBasePath())) {
			String restfulApiBasePath = repositoryContext.getCmsRepository().getRestfulApiBasePath().trim();
			if (!restfulApiBasePath.startsWith("/")) {
				restfulApiBasePath = "/" + restfulApiBasePath;
			}
			 
			return restfulApiBasePath.endsWith("/")? restfulApiBasePath.substring(0, restfulApiBasePath.length()-1) : restfulApiBasePath;
		}

		return null;
	}

	private class MarkerSerializer extends AbstractSerializer {

		public MarkerSerializer(boolean prettyPrint, boolean jsonOutput) {
			super(prettyPrint, jsonOutput);
		}
		
	}
}