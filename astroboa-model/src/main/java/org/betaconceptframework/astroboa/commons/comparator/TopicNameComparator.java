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
package org.betaconceptframework.astroboa.commons.comparator;

import java.io.Serializable;
import java.util.Comparator;

import org.betaconceptframework.astroboa.api.model.Topic;

/**
 * @author Gregory Chomatas (gchomatas@betaconcept.com)
 * @author Savvas Triantafyllou (striantafyllou@betaconcept.com)
 * 
 */
public class TopicNameComparator implements Comparator<Topic>, Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2515011686550633265L;

	@Override
	public int compare(Topic topic1, Topic topic2) {
		
		if (topic1 == null)
			return -1;
		
		if (topic2 == null)
			return 1;
		
		if (topic1.getName() == null && topic2.getName() == null){
			return 0;
		}
		else{

			if (topic1.getName() == null){
				return -1;
			}	

			if (topic2.getName() == null){
				return -1;
			}

			return topic1.getName().compareTo(topic2.getName());
		}
	}

}