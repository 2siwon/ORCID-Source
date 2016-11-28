/**
 * =============================================================================
 *
 * ORCID (R) Open Source
 * http://orcid.org
 *
 * Copyright (c) 2012-2014 ORCID, Inc.
 * Licensed under an MIT-Style License (MIT)
 * http://orcid.org/open-source-license
 *
 * This copyright and license information (including a link to the full license)
 * shall be included in its entirety in all copies or substantial portion of
 * the software.
 *
 * =============================================================================
 */
package org.orcid.api.publicV2.server.security;

import org.orcid.jaxb.model.common_rc3.Filterable;
import org.orcid.jaxb.model.common_rc3.VisibilityType;
import org.orcid.jaxb.model.record.summary_rc3.ActivitiesSummary;
import org.orcid.jaxb.model.record_rc3.ActivitiesContainer;
import org.orcid.jaxb.model.record_rc3.Addresses;
import org.orcid.jaxb.model.record_rc3.Biography;
import org.orcid.jaxb.model.record_rc3.Emails;
import org.orcid.jaxb.model.record_rc3.GroupsContainer;
import org.orcid.jaxb.model.record_rc3.Keywords;
import org.orcid.jaxb.model.record_rc3.Name;
import org.orcid.jaxb.model.record_rc3.OtherNames;
import org.orcid.jaxb.model.record_rc3.Person;
import org.orcid.jaxb.model.record_rc3.PersonExternalIdentifiers;
import org.orcid.jaxb.model.record_rc3.PersonalDetails;
import org.orcid.jaxb.model.record_rc3.Record;
import org.orcid.jaxb.model.record_rc3.ResearcherUrls;

public interface PublicAPISecurityManagerV2 {
	void checkIsPublic(Filterable filterable);
    
	void checkIsPublic(VisibilityType visibilityType);
	
    void checkIsPublic(Biography biography);
    
    void checkIsPublic(Name name);
    
    void filter(ActivitiesSummary activitiesSummary);
    
    void filter(ActivitiesContainer container);        
    
    void filter(GroupsContainer container);
    
    void filter(Addresses addresses);
    
    void filter(Emails emails);
    
    void filter(Keywords keywords);
    
    void filter(OtherNames otherNames);
    
    void filter(PersonExternalIdentifiers extIds);
    
    void filter(ResearcherUrls researcherUrls);
    
    void filter(PersonalDetails personalDetails);
    
    void filter(Person person);

    void filter(Record record);
}
