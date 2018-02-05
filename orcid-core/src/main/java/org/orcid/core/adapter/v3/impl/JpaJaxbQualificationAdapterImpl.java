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
package org.orcid.core.adapter.v3.impl;

import java.util.Collection;
import java.util.List;

import ma.glasnost.orika.MapperFacade;

import org.orcid.core.adapter.v3.JpaJaxbQualificationAdapter;
import org.orcid.jaxb.model.v3.dev1.record.summary.QualificationSummary;
import org.orcid.jaxb.model.v3.dev1.record.Qualification;
import org.orcid.persistence.jpa.entities.OrgAffiliationRelationEntity;

/**
 * 
 * @author Angel Montenegro
 * 
 */
public class JpaJaxbQualificationAdapterImpl implements JpaJaxbQualificationAdapter {

    private MapperFacade mapperFacade;

    public void setMapperFacade(MapperFacade mapperFacade) {
        this.mapperFacade = mapperFacade;
    }

    @Override
    public OrgAffiliationRelationEntity toOrgAffiliationRelationEntity(Qualification qualification) {
        if(qualification == null)
            return null;
        return mapperFacade.map(qualification, OrgAffiliationRelationEntity.class);
    }

    @Override
    public Qualification toQualification(OrgAffiliationRelationEntity entity) {
        if(entity == null)
            return null;
        return mapperFacade.map(entity, Qualification.class);
    }

    public QualificationSummary toQualificationSummary(OrgAffiliationRelationEntity entity) {
        if(entity == null)
            return null;
        return mapperFacade.map(entity, QualificationSummary.class);
    }
    
    @Override
    public List<Qualification> toQualification(Collection<OrgAffiliationRelationEntity> entities) {
        if(entities == null)
            return null;
        return mapperFacade.mapAsList(entities, Qualification.class);
    }

    @Override
    public List<QualificationSummary> toQualificationSummary(Collection<OrgAffiliationRelationEntity> entities) {
        if(entities == null)
            return null;
        return mapperFacade.mapAsList(entities, QualificationSummary.class);
    }
    
    @Override
    public OrgAffiliationRelationEntity toOrgAffiliationRelationEntity(Qualification qualification, OrgAffiliationRelationEntity existing) {
        if (qualification == null) {
            return null;
        }
        mapperFacade.map(qualification, existing);
        return existing;
    }
}
