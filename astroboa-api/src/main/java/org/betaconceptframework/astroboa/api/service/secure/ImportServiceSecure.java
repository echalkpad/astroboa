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

package org.betaconceptframework.astroboa.api.service.secure;


import java.net.URI;
import java.util.concurrent.Future;

import org.betaconceptframework.astroboa.api.model.ContentObject;
import org.betaconceptframework.astroboa.api.model.RepositoryUser;
import org.betaconceptframework.astroboa.api.model.Space;
import org.betaconceptframework.astroboa.api.model.Taxonomy;
import org.betaconceptframework.astroboa.api.model.Topic;
import org.betaconceptframework.astroboa.api.model.io.ImportConfiguration;
import org.betaconceptframework.astroboa.api.model.io.ImportReport;
import org.betaconceptframework.astroboa.api.security.AstroboaCredentials;
import org.betaconceptframework.astroboa.api.security.CmsRole;
import org.betaconceptframework.astroboa.api.service.ImportService;

/**
  * Secure Import Service API. 
 * 
 * <p>
 * It contains the same methods provided by 
 * {@link ImportService} with the addition that each method requires
 * an authentication token as an extra parameter, in order to ensure
 * that client has been successfully logged in an Astroboa repository and
 * therefore has been granted access to further use Astroboa services
 * </p>
 * 
 * @author Gregory Chomatas (gchomatas@betaconcept.com)
 * @author Savvas Triantafyllou (striantafyllou@betaconcept.com)
 * 
 */
public interface ImportServiceSecure {

	/**
	 * Same semantics with {@link ImportService#importRepositoryContentFromURI(URI)}
	 * augmented with the requirement of providing an authentication token.
	 * 
 	 *<p>
	 * This method is executed only if user has any of the following roles depending on 
	 * importing content :
	 * <ul>
	 * <li>{@link CmsRole#ROLE_CMS_EDITOR}</li>
	 * <li>{@link CmsRole#ROLE_CMS_PORTAL_EDITOR}</li>
	 * <li>{@link CmsRole#ROLE_CMS_TAXONOMY_EDITOR}</li>
	 * </ul>
	 *  
	 * Information about user's roles is available through provided authentication 
	 * token.
	 *</p>
	 *
	 * @param contentSource Import source location.
	 * @param configuration Import configuration.
	 * @param authenticationToken A token provided during client login ({@link RepositoryServiceSecure#login(String, AstroboaCredentials, String)}) to an Astroboa repository.
	 * 
	 * @return A report about import progress. In local invocations of this method it is possible to follow import progress.
	 */
	Future<ImportReport> importRepositoryContentFromURI(URI contentSource, ImportConfiguration configuration, String authenticationToken);
	
	/**
	 * Same semantics with {@link ImportService#importRepositoryContentFromString(String)}
	 * augmented with the requirement of providing an authentication token.
	 * 
 	 *<p>
	 * This method is executed only if user has any of the following roles depending on 
	 * importing content :
	 * <ul>
	 * <li>{@link CmsRole#ROLE_CMS_EDITOR}</li>
	 * <li>{@link CmsRole#ROLE_CMS_PORTAL_EDITOR}</li>
	 * <li>{@link CmsRole#ROLE_CMS_TAXONOMY_EDITOR}</li>
	 * </ul>
	 *  
	 * Information about user's roles is available through provided authentication 
	 * token.
	 *</p>
	 * 
	 * @param contentSource Import source.
	 * @param configuration Import configuration.
	 * @param authenticationToken A token provided during client login ({@link RepositoryServiceSecure#login(String, AstroboaCredentials, String)}) to an Astroboa repository.
	 * 
	 * @return A report about import progress. In local invocations of this method it is possible to follow import progress.
	 */
	Future<ImportReport> importRepositoryContentFromString(String contentSource, ImportConfiguration configuration, String authenticationToken);
	
	/**
	 * Same semantics with {@link ImportService#importContentObject(String)}
	 * augmented with the requirement of providing an authentication token.
	 * 
 	 *<p>
	 * This method is executed only if user has role
	 * {@link CmsRole#ROLE_CMS_EDITOR} upon connected Astroboa repository.
	 * Information about user's roles is available through provided authentication 
	 * token.
	 *</p>
	 * 
	 * @param contentSource Import source. 
	 * @param configuration Import configuration.
	 * @param authenticationToken A token provided during client login ({@link RepositoryServiceSecure#login(String, AstroboaCredentials, String)}) to an Astroboa repository.
	 * 
	 * @return Imported {@link ContentObject}
	 */
	ContentObject importContentObject(String contentSource, ImportConfiguration configuration,  String authenticationToken);
	
	/**
	 * Same semantics with {@link ImportService#importRepositoryUser(String)}
	 * augmented with the requirement of providing an authentication token.
	 * 
 	 *<p>
	 * This method is executed only if user has role
	 * {@link CmsRole#ROLE_CMS_EDITOR} upon connected Astroboa repository.
	 * Information about user's roles is available through provided authentication 
	 * token.
	 *</p>
	 * 
	 * @param repositoryUserSource Xml or JSON representation of a {@link RepositoryUser}.
	 * @param configuration Import configuration.
	 * @param authenticationToken A token provided during client login ({@link RepositoryServiceSecure#login(String, AstroboaCredentials, String)}) to an Astroboa repository.
	 * 
	 * @return Newly created or updated RepositoryUser
	 */
	RepositoryUser importRepositoryUser(String repositoryUserSource, ImportConfiguration configuration, String authenticationToken);

	/**
	 * Same semantics with {@link ImportService#importTopic(String)}
	 * augmented with the requirement of providing an authentication token.
	 * 
 	 *<p>
	 * This method is executed only if user has role
	 * {@link CmsRole#ROLE_CMS_TAXONOMY_EDITOR} upon connected Astroboa repository.
	 * Information about user's roles is available through provided authentication 
	 * token.
	 *</p>
	 * 
	 * @param topicSource Xml or JSON representation of a {@link Topic}.
	 * @param configuration Import configuration.
	 * @param authenticationToken A token provided during client login ({@link RepositoryServiceSecure#login(String, AstroboaCredentials, String)}) to an Astroboa repository.
	 * 
	 * @return Newly created or updated Topic
	 */
	Topic importTopic(String topicSource, ImportConfiguration configuration, String authenticationToken);

	/**
	 * Same semantics with {@link ImportService#importSpace(String)}
	 * augmented with the requirement of providing an authentication token.
	 * 
 	 *<p>
	 * This method is executed only if user has role
	 * {@link CmsRole#ROLE_CMS_TAXONOMY_EDITOR} upon connected Astroboa repository.
	 * Information about user's roles is available through provided authentication 
	 * token.
	 *</p>
	 * 
	 * @param spaceSource Xml or JSON representation of a {@link Space}.
	 * @param configuration Import configuration.
	 * @param authenticationToken A token provided during client login ({@link RepositoryServiceSecure#login(String, AstroboaCredentials, String)}) to an Astroboa repository.
	 * 
	 * @return Newly created or updated Space
	 */
	Space importSpace(String spaceSource, ImportConfiguration configuration, String authenticationToken);
	
	/**
	 * Same semantics with {@link ImportService#importTaxonomy(String)}
	 * augmented with the requirement of providing an authentication token.
	 * 
 	 *<p>
	 * This method is executed only if user has role
	 * {@link CmsRole#ROLE_CMS_TAXONOMY_EDITOR} upon connected Astroboa repository.
	 * Information about user's roles is available through provided authentication 
	 * token.
	 *</p>
	 * 
	 * @param taxonomySource Xml or JSON representation of a {@link Taxonomy}.
	 * @param configuration Import configuration.
	 * @param authenticationToken A token provided during client login ({@link RepositoryServiceSecure#login(String, AstroboaCredentials, String)}) to an Astroboa repository.
	 * 
	 * @return Newly created or updated Taxonomy
	 */
	Taxonomy importTaxonomy(String taxonomySource, ImportConfiguration configuration, String authenticationToken);
	
}
