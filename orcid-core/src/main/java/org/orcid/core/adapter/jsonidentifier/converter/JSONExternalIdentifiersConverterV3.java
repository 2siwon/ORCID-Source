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

import org.orcid.core.adapter.jsonidentifier.JSONExternalIdentifier;
import org.orcid.core.adapter.jsonidentifier.JSONExternalIdentifiers;
import org.orcid.core.adapter.jsonidentifier.JSONUrl;
import org.orcid.core.utils.JsonUtils;
import org.orcid.jaxb.model.message.FundingExternalIdentifierType;
import org.orcid.jaxb.model.v3.dev1.common.Url;
import org.orcid.jaxb.model.v3.dev1.record.ExternalID;
import org.orcid.jaxb.model.v3.dev1.record.ExternalIDs;
import org.orcid.jaxb.model.v3.dev1.record.Relationship;
import org.orcid.pojo.ajaxForm.PojoUtil;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

public class JSONExternalIdentifiersConverterV3 extends BidirectionalConverter<ExternalIDs, String> {
    
    private ExternalIdentifierTypeConverter conv = new ExternalIdentifierTypeConverter();

    @Override
    public String convertTo(ExternalIDs source, Type<String> destinationType) {
        JSONExternalIdentifiers jsonExternalIdentifiers = new JSONExternalIdentifiers();
        for (ExternalID externalID : source.getExternalIdentifier()) {
            JSONExternalIdentifier jsonExternalIdentifier = new JSONExternalIdentifier();
            if (externalID.getType() != null) {
                jsonExternalIdentifier.setType(conv.convertTo(externalID.getType(), null));
            }

            if (externalID.getUrl() != null) {
                jsonExternalIdentifier.setUrl(new JSONUrl(externalID.getUrl().getValue()));
            }

            if (!PojoUtil.isEmpty(externalID.getValue())) {
                jsonExternalIdentifier.setValue(externalID.getValue());
            }

            if (externalID.getRelationship() != null) {
                jsonExternalIdentifier.setRelationship(conv.convertTo(externalID.getRelationship().value(), null));
            }
            jsonExternalIdentifiers.getExternalIdentifier().add(jsonExternalIdentifier);
        }
        return JsonUtils.convertToJsonString(jsonExternalIdentifiers);
    }

    @Override
    public ExternalIDs convertFrom(String source, Type<ExternalIDs> destinationType) {
        JSONExternalIdentifiers externalIdentifiers = JsonUtils.readObjectFromJsonString(source, JSONExternalIdentifiers.class);
        ExternalIDs externalIDs = new ExternalIDs();
        for (JSONExternalIdentifier externalIdentifier : externalIdentifiers.getExternalIdentifier()) {
            ExternalID id = new ExternalID();
            if (externalIdentifier.getType() == null) {
                id.setType(FundingExternalIdentifierType.GRANT_NUMBER.value());
            } else {
                id.setType(externalIdentifier.getType().toLowerCase());
            }
            if (externalIdentifier.getUrl() != null && !PojoUtil.isEmpty(externalIdentifier.getUrl().getValue())) {
                Url url = new Url(externalIdentifier.getUrl().getValue());
                id.setUrl(url);
            }

            if (!PojoUtil.isEmpty(externalIdentifier.getValue())) {
                id.setValue(externalIdentifier.getValue());
            }

            if (externalIdentifier.getRelationship() != null) {
                id.setRelationship(Relationship.fromValue(conv.convertFrom(externalIdentifier.getRelationship(), null)));
            }
            externalIDs.getExternalIdentifier().add(id);
        }
        return externalIDs;
    }

}
