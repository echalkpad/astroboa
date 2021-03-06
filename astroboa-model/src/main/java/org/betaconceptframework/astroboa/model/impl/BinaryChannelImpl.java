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
package org.betaconceptframework.astroboa.model.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Calendar;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.betaconceptframework.astroboa.api.model.BinaryChannel;
import org.betaconceptframework.astroboa.api.model.exception.CmsException;
import org.betaconceptframework.astroboa.api.model.io.ResourceRepresentationType;
import org.betaconceptframework.astroboa.context.AstroboaClientContextHolder;
import org.betaconceptframework.astroboa.context.RepositoryContext;
import org.betaconceptframework.astroboa.model.jaxb.adapter.BinaryChannelAdapter;
import org.betaconceptframework.astroboa.model.lazy.LazyLoader;
import org.betaconceptframework.astroboa.util.CmsConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Repository's resource content 
 * 
 * @author Gregory Chomatas (gchomatas@betaconcept.com)
 * @author Savvas Triantafyllou (striantafyllou@betaconcept.com)
 *
 */
@XmlJavaTypeAdapter(value=BinaryChannelAdapter.class)
public class BinaryChannelImpl extends CmsRepositoryEntityImpl implements BinaryChannel, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6485262251238864739L;

	private String name;

	private long size;

	private String encoding;

	private Calendar modified;

	/**
	 * Name of the content channel
	 */
	private String sourceFilename;

	/**
	 * MIME type  (audio, image, text, video, etc) of resource's content  
	 */
	private String mimeType;

	/**
	 * Actual resource's content
	 */
	private byte[] newContent;

	/**
	 * Used when binary content is not managed by the 
	 * JCR implementation but rather is managed by the 
	 * Astroboa engine.
	 * 
	 */
	private String relativeFileSystemPath;
	private String absoluteBinaryChannelContentPath;

	private String repositoryId;

	private String contentObjectId;
	private String contentObjectSystemName;
	
	private String binaryPropertyPermanentPath;
	
	private boolean unmanaged = false;

	private boolean binaryPropertyIsMultiValued;
	
	/*
	 * This variable represents the external location of the content
	 * of the binary channel. This location can either be a URI or a simple
	 * key which makes sense only in the context of the module (in this
	 * case the PopulateSimpleCmsProperty class) which is 
	 * responsible to populate the binary channel (and its content) to the repository.
	 * 
	 * The value of this variable is used primarily when saving an object which 
	 * contains one or more properties of type Binary, using  XML or JSON format.
	 * 
	 * According to Astroboa model, the binary channel type defines an attribute named 'url'
	 * which represents the URI location of the content of the binary channel and an
	 * element of type xs:base64Binary, named 'content', which represents the actual content 
	 * of the binary channel.
	 * 
	 * When importing an object in XML/JSON format, it is practically unrealistic to require that 
	 * binary content must exist inside the XML/JSON source under the 'content' element. 
	 * A more practical way would be to allow users
	 * to define the URI location of the content so that Astroboa could download it upon save 
	 * or to allow users to provide the actual content along with the XML/JSON
	 * source but not inside the source. (see more in 
	 * ImportService.importContentObject(String contentSource,boolean version, boolean updateLastModificationTime, boolean save, Map<String, byte[]> binaryContentMap); 
	 * 
	 * In either cases, users must simply supply a value to the 'url' attribute of the element which represents the binary property
	 * and Astroboa will then discover where to go to get the actual content
	 * 
	 * This is an example (in JSON) where user specifies an external location for the content
	 * 
	 * "image" : {
	 *  "lastModificationDate" : "2008-12-17T16:33:02.474+02:00",
	 *	"mimeType" : "image/jpeg",
	 *	"sourceFileName" : "image1.jpg",
	 *  "url" : "http://myserver/temp/images/image1.jpg"
	 *  }
	 * 
	 * 
	 * and this is an example (in JSON) where user specifies a key as the location of the content
	 * 
	 * "image" : {
	 *  "lastModificationDate" : "2008-12-17T16:33:02.474+02:00",
	 *	"mimeType" : "image/jpeg",
	 *	"sourceFileName" : "image1.jpg",
	 *  "url" : "image1"
	 *  }
	 *  
	 *  In this case, user must also provide Astroboa with a binary content map
	 * 
	 */
	private String externalLocationOfTheContent;

	/**
	 * @return Returns the content.
	 */
	public byte[] getContent() {

		if (newContent != null){
			/*
			 * this.newContent = content
			 * 
			 * produces the following bug report from FindBugs 
			 * Returning a reference to a mutable object value stored in one of the object's fields exposes the internal representation of the object.
			 * If instances are accessed by untrusted code, and unchecked changes to the mutable object would compromise security or other important 
			 * properties, you will need to do something different. Returning a new copy of the object is better approach in many situations.
			 * 
			 * The following approach would be more preferable
			 * 
			 *  byte[] clone = new byte[newContent.length];
				System.arraycopy(newContent, 0, clone, 0, newContent.length);
				return clone;
				
				However, we need to examine the performance issue rising from copying the byte array
				each time user uses method getContent(). Therefore for the moment we return the reference
				to the provided content.
			*/
			return newContent;
		}

		//Return content from file system
		InputStream inputStream = null;
		try {

			inputStream = getContentAsStream();

			if (inputStream != null){

				newContent = IOUtils.toByteArray(inputStream);
			}
			else
			{
				newContent = new byte[0];
			}
			
			return newContent;

		} catch (Exception e) {
			throw new CmsException(e);
		}
		finally
		{
			if (inputStream != null)
				try {
					inputStream.close();
				} catch (IOException e) {
					throw new CmsException(e);
				}
		}
	}

	/**
	 * Content is provided only if path for the given binary channel is set
	 * @param content The content to set.
	 */
	public void setContent(byte[] content) {

		/*
		 * this.newContent = content
		 * 
		 * produces the following bug report from FindBugs 
		 * This code stores a reference to an externally mutable object into the internal representation of the object.
		 * If instances are accessed by untrusted code, and unchecked changes to the mutable object would compromise security 
		 * or other important properties,
		 * you will need to do something different. Storing a copy of the object is better approach in many situations
		 * 
		 * The following approach would be more preferable
			if (content != null){
				System.arraycopy(content, 0, newContent, 0, content.length);
			}
			else{
				newContent = null;
			}
			
			However, we need to examine the performance issue rising from copying the byte array
			each time user uses method setContent(). Therefore for the moment we create a reference
			to the provided content.
		*/

		newContent = content;

		if (newContent != null)
		{
			setSize(newContent.length);
		}
		else
		{
			setSize(0);
		}
		//Reset paths since a new content has been defined
		absoluteBinaryChannelContentPath = null;
		relativeFileSystemPath = null;
		
	}


	/**
	 * 	 * @return Returns the mimeType.
	 */
	public String getMimeType() {
		return mimeType;
	}

	/**
	 * @param mimeType The mimeType to set.
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * @return Returns the sourceFilename.
	 */
	public String getSourceFilename() {
		return sourceFilename;
	}

	/**
	 * @param sourceFilename The sourceFilename to set.
	 */
	public void setSourceFilename(String name) {
		this.sourceFilename = name;
	}
	
	/**
	 * @return Returns the sourceFilename suffix.
	 */
	public String getSourceFilenameSuffix() {
		return StringUtils.substringAfterLast(sourceFilename, ".");
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getCalculatedSize() {
		Long roundedSize = Math.round(getSize() / (double)1024);
		return roundedSize.toString() + "Kb";
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public Calendar getModified() {
		return modified;
	}

	public void setModified(Calendar modified) {
		this.modified = modified;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNewContentLoaded() {
		return newContent != null;
	}

	public String getRelativeFileSystemPath() {
		return relativeFileSystemPath;
	}

	public void setRelativeFileSystemPath(String fileSystemPath) {
		this.relativeFileSystemPath = fileSystemPath;

	}

	public InputStream getContentAsStream() {
		try {

			//return new content if any
			if (newContent != null)
				return new ByteArrayInputStream(newContent);

			//Look for file content only if id exists
			//or binary channel is unmanaged
			try{
				if (unmanaged){
					if (StringUtils.isNotBlank(absoluteBinaryChannelContentPath)){
						return new FileInputStream(absoluteBinaryChannelContentPath);
					}
				}
				else if (StringUtils.isNotBlank(getId())){
					//Use binary channel id to load binary value from the content repository
					LazyLoader lazyLoader = getLazyLoader();

					if (lazyLoader != null){
						byte[] binaryValue = lazyLoader.lazyLoadBinaryValue(getId(), authenticationToken);
						return new ByteArrayInputStream(binaryValue);
					}
					else{
						final Logger logger = LoggerFactory.getLogger(getClass());
						logger.warn("Could not activate lazy loader for token {}", authenticationToken);
					}
				}
				
				return null;
			}
			catch(Exception e){

				final Logger logger = LoggerFactory.getLogger(getClass());
				logger.error("",e);
				return null;

			}
				

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setAbsoluteBinaryChannelContentPath(
			String absoluteBinaryChannelContentPath) {
		this.absoluteBinaryChannelContentPath = absoluteBinaryChannelContentPath;

	}


	public void setRepositoryId(String repositoryId){
		this.repositoryId = repositoryId;
	}


	public String getServerURL() {
		RepositoryContext repositoryContext = AstroboaClientContextHolder.getRepositoryContextForClient(authenticationToken);
		if (repositoryContext != null && repositoryContext.getCmsRepository() != null && 
				StringUtils.isNotBlank(repositoryContext.getCmsRepository().getServerURL())){
			String serverURL = repositoryContext.getCmsRepository().getServerURL().trim();
			
			return serverURL.endsWith("/")? serverURL.substring(0, serverURL.length()-1) : serverURL; 
		}

		return null;
	}
	
	public String getRestfulApiBasePath() {
		RepositoryContext repositoryContext = AstroboaClientContextHolder.getRepositoryContextForClient(authenticationToken);
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

	public boolean contentExists() {
		if (newContent != null){
			return true;
		}

		if (unmanaged && StringUtils.isNotBlank(absoluteBinaryChannelContentPath)){
			return new File(absoluteBinaryChannelContentPath).exists();
		}
		
		//TODO: Change this code so that binary content should not be loaded
		getContent();

		return newContent != null;
	}

	public void setUnmanaged(boolean unmanaged){
		this.unmanaged = unmanaged;
	}



	
	public String buildResourceApiURL(Integer width, Integer height, Double aspectRatio,  CropPolicy cropPolicy, ContentDispositionType contentDispositionType, boolean friendlyUrl, boolean relative) {

		if (StringUtils.isBlank(contentObjectId) || StringUtils.isBlank(binaryPropertyPermanentPath)){
			return "";
		}
		
		// Astroboa RESTful API URL pattern for accessing the value of content object properties
		// http://server/resource-api/
		// <reposiotry-id>/objects/<contentObjectId>/<binaryChannelPropertyValuePath>
		// ?contentDispositionType=<contentDispositionType>&width=<width>&height=<height>
			
		StringBuilder resourceApiURLBuilder = new StringBuilder();
		
		if (! relative){
			String serverURL = getServerURL();
			
			if (serverURL != null){
				resourceApiURLBuilder.append(serverURL);
			}
		}
		
		resourceApiURLBuilder.append(getRestfulApiBasePath()).append(CmsConstants.FORWARD_SLASH); 

		resourceApiURLBuilder.append((StringUtils.isBlank(repositoryId) ? "no-repository":repositoryId));
		resourceApiURLBuilder.append(CmsConstants.RESOURCE_API_OBJECTS_COLLECTION_URI_PATH);
		
		if (friendlyUrl) {
			resourceApiURLBuilder.append(CmsConstants.FORWARD_SLASH).append(contentObjectSystemName);
		}
		else {
			resourceApiURLBuilder.append(CmsConstants.FORWARD_SLASH).append(contentObjectId);
		}
		
		resourceApiURLBuilder.append(CmsConstants.FORWARD_SLASH).append(binaryPropertyPermanentPath);
		
		if (binaryPropertyIsMultiValued){
			resourceApiURLBuilder.append(CmsConstants.LEFT_BRACKET)
			.append(getId())
			.append(CmsConstants.RIGHT_BRACKET);
		}
		
		StringBuilder urlParametersBuilder = new StringBuilder();
		
		if (contentDispositionType !=null){
			urlParametersBuilder
				.append(CmsConstants.AMPERSAND)
				.append("contentDispositionType")
				.append(CmsConstants.EQUALS_SIGN)
				.append(contentDispositionType.toString().toLowerCase());
		}
		
		if (isJPGorPNGorGIFImage()){
			if (width != null && width > 0){
				urlParametersBuilder
					.append(CmsConstants.AMPERSAND)
					.append("width")
					.append(CmsConstants.EQUALS_SIGN)
					.append(width);
			}
			
			if (height != null && height > 0){
				urlParametersBuilder.append(CmsConstants.AMPERSAND)
					.append("height")
					.append(CmsConstants.EQUALS_SIGN)
					.append(height);
			}
			
			// we accept to set a new aspect ratio only if  
			if (aspectRatio != null && (width == null || height == null)) {
				urlParametersBuilder.append(CmsConstants.AMPERSAND)
					.append("aspectRatio")
					.append(CmsConstants.EQUALS_SIGN)
					.append(aspectRatio);
				
				if (cropPolicy != null) {
					urlParametersBuilder.append(CmsConstants.AMPERSAND)
					.append("cropPolicy")
					.append(CmsConstants.EQUALS_SIGN)
					.append(cropPolicy.toString());
				}
			}

		}
		
		if (urlParametersBuilder.length() > 0) {
			urlParametersBuilder.replace(0, 1, CmsConstants.QUESTION_MARK);
		}
		return resourceApiURLBuilder.append(urlParametersBuilder).toString();
	}
	
	// we need this in order to determine if width, height, aspectRatio and cropPolicy url parameters should be appended in the URL 
	private boolean isJPGorPNGorGIFImage(){
		return StringUtils.isNotBlank(mimeType) && 
		(mimeType.equals("image/jpeg") || 
				mimeType.equals("image/png") || 
				mimeType.equals("image/x-png") ||
				mimeType.equals("image/gif"));
	}
	
	public void setContentObjectId(String contentObjectId){
		this.contentObjectId = contentObjectId;
	}
	
	public void setContentObjectSystemName(String systemName){
		this.contentObjectSystemName = systemName;
	}
	
	/**
	 * 
	 */
	public void clean() {
		getContent();
		absoluteBinaryChannelContentPath = null;
		contentObjectId = null;
		contentObjectSystemName = null;
		relativeFileSystemPath = null;
		
		setId(null);
		
	}
	
	@Override
	public String getResourceApiURL(ResourceRepresentationType<?>  resourceRepresentationType, boolean relative, boolean friendlyUrl) {
		
		return buildResourceApiURL(null, null, null, null, null, friendlyUrl, relative);
		
	}

	public void binaryPropertyIsMultiValued() {
		binaryPropertyIsMultiValued = true;
		
	}

	public void setBinaryPropertyPermanentPath(String binaryPropertyPermanentPath) {
		this.binaryPropertyPermanentPath = binaryPropertyPermanentPath;
	}

	public void setExternalLocationOfTheContent(String externalLocationOfTheContent) {
		this.externalLocationOfTheContent = externalLocationOfTheContent;
	}

	public String getExternalLocationOfTheContent() {
		return externalLocationOfTheContent;
	}
	
	private LazyLoader getLazyLoader() {
		return AstroboaClientContextHolder.getLazyLoaderForClient(authenticationToken);
	}

	
}
