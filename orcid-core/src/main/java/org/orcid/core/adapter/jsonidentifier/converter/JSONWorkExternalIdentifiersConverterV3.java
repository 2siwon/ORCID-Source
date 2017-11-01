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
package org.orcid.core.adapter.jsonidentifier.converter;

import org.orcid.core.adapter.jsonidentifier.JSONWorkExternalIdentifier.WorkExternalIdentifierId;

import org.orcid.core.adapter.jsonidentifier.JSONUrl;
import org.orcid.core.adapter.jsonidentifier.JSONWorkExternalIdentifier;
import org.orcid.core.adapter.jsonidentifier.JSONWorkExternalIdentifiers;
import org.orcid.core.utils.JsonUtils;
import org.orcid.core.utils.v3.identifiers.NormalizationService;
import org.orcid.jaxb.model.message.WorkExternalIdentifierType;
import org.orcid.jaxb.model.v3.dev1.common.TransientNonEmptyString;
import org.orcid.jaxb.model.v3.dev1.common.Url;
import org.orcid.jaxb.model.v3.dev1.record.ExternalID;
import org.orcid.jaxb.model.v3.dev1.record.Relationship;
import org.orcid.pojo.ajaxForm.PojoUtil;
import org.orcid.jaxb.model.v3.dev1.record.ExternalIDs;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class JSONWorkExternalIdentifiersConverterV3 extends BidirectionalConverter<ExternalIDs, String> {

    private NormalizationService norm;
    
    public JSONWorkExternalIdentifiersConverterV3(NormalizationService norm){
        this.norm=norm;
    }
    
    private ExternalIdentifierTypeConverter conv = new ExternalIdentifierTypeConverter();

    @Override
    public String convertTo(ExternalIDs source, Type<String> destinationType) {
        JSONWorkExternalIdentifiers jsonWorkExternalIdentifiers = new JSONWorkExternalIdentifiers();
        for (ExternalID externalID : source.getExternalIdentifier()) {
            JSONWorkExternalIdentifier jsonWorkExternalIdentifier = new JSONWorkExternalIdentifier();
            if (externalID.getType() != null) {
                jsonWorkExternalIdentifier.setWorkExternalIdentifierType(conv.convertTo(externalID.getType(), null));
            }

            if (externalID.getUrl() != null) {
                jsonWorkExternalIdentifier.setUrl(new JSONUrl(externalID.getUrl().getValue()));
            }

            if (!PojoUtil.isEmpty(externalID.getValue())) {
                jsonWorkExternalIdentifier.setWorkExternalIdentifierId(new WorkExternalIdentifierId(externalID.getValue()));
            }

            if (externalID.getRelationship() != null) {
                jsonWorkExternalIdentifier.setRelationship(conv.convertTo(externalID.getRelationship().value(), null));
            }
            jsonWorkExternalIdentifiers.getWorkExternalIdentifier().add(jsonWorkExternalIdentifier);
        }
        return JsonUtils.convertToJsonString(jsonWorkExternalIdentifiers);
    }

    @Override
    public ExternalIDs convertFrom(String source, Type<ExternalIDs> destinationType) {
        JSONWorkExternalIdentifiers workExternalIdentifiers = JsonUtils.readObjectFromJsonString(source, JSONWorkExternalIdentifiers.class);
        ExternalIDs externalIDs = new ExternalIDs();
        for (JSONWorkExternalIdentifier workExternalIdentifier : workExternalIdentifiers.getWorkExternalIdentifier()) {
            ExternalID id = new ExternalID();
            if (workExternalIdentifier.getWorkExternalIdentifierType() == null) {
                id.setType(WorkExternalIdentifierType.OTHER_ID.value());
            } else {
                id.setType(conv.convertFrom(workExternalIdentifier.getWorkExternalIdentifierType(), null));
            }
            if (workExternalIdentifier.getWorkExternalIdentifierId() != null) {
                id.setValue(workExternalIdentifier.getWorkExternalIdentifierId().content);
                //note, uses API type name.
                id.setNormalized(new TransientNonEmptyString(norm.normalise(id.getType(), workExternalIdentifier.getWorkExternalIdentifierId().content)));
            }
            if (workExternalIdentifier.getUrl() != null) {
                id.setUrl(new Url(workExternalIdentifier.getUrl().getValue()));
            }
            if (workExternalIdentifier.getRelationship() != null) {
                id.setRelationship(Relationship.fromValue(conv.convertFrom(workExternalIdentifier.getRelationship(), null)));
            }
            
            //create normalized value
            
            externalIDs.getExternalIdentifier().add(id);
        }
        return externalIDs;
    }

}
