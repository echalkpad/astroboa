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

package org.betaconceptframework.astroboa.engine.jcr.util;


import org.betaconceptframework.astroboa.api.model.query.criteria.CmsCriteria;
import org.betaconceptframework.astroboa.api.model.query.render.RenderProperties;
import org.betaconceptframework.astroboa.model.impl.query.render.RenderPropertiesImpl;

/**
 * 
 * @author Gregory Chomatas (gchomatas@betaconcept.com)
 * @author Savvas Triantafyllou (striantafyllou@betaconcept.com)
 *
 */
public class RendererUtils {

	public RenderProperties copyRenderPropertiesFromCriteria(CmsCriteria cmsCriteria){
		RenderProperties renderPropertiesForCache = null;
		if (cmsCriteria != null){
			renderPropertiesForCache = new RenderPropertiesImpl();
			renderPropertiesForCache.getRenderInstructions().putAll(cmsCriteria.getRenderProperties().getRenderInstructions());
		}
		
		return renderPropertiesForCache;
	}
}