package org.orcid.core.adapter.jsonidentifier.converter;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.orcid.core.adapter.jsonidentifier.JSONExternalIdentifier;
import org.orcid.core.adapter.jsonidentifier.JSONFundingExternalIdentifiers;
import org.orcid.core.adapter.jsonidentifier.JSONUrl;
import org.orcid.core.utils.JsonUtils;
import org.orcid.jaxb.model.message.FundingExternalIdentifierType;
import org.orcid.jaxb.model.v3.dev1.common.Url;
import org.orcid.jaxb.model.v3.dev1.record.ExternalID;
import org.orcid.jaxb.model.v3.dev1.record.ExternalIDs;
import org.orcid.jaxb.model.v3.dev1.record.Relationship;
import org.orcid.pojo.ajaxForm.PojoUtil;
import org.orcid.test.OrcidJUnit4ClassRunner;

import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.metadata.Type;

@RunWith(OrcidJUnit4ClassRunner.class)
@Ignore
public class JSONFundingExternalIdentifiersConverterV3Test {
    
    private ExternalIdentifierTypeConverter conv = new ExternalIdentifierTypeConverter();

    @Test
    public void testConvertTo(ExternalIDs source, Type<String> destinationType) {
//        JSONFundingExternalIdentifiers jsonFundingExternalIdentifiers = new JSONFundingExternalIdentifiers();
//        for (ExternalID externalID : source.getExternalIdentifier()) {
//            JSONExternalIdentifier jsonExternalIdentifier = new JSONExternalIdentifier();
//            if (externalID.getType() != null) {
//                jsonExternalIdentifier.setType(conv.convertTo(externalID.getType(), null));
//            }
//
//            if (externalID.getUrl() != null) {
//                jsonExternalIdentifier.setUrl(new JSONUrl(externalID.getUrl().getValue()));
//            }
//
//            if (!PojoUtil.isEmpty(externalID.getValue())) {
//                jsonExternalIdentifier.setValue(externalID.getValue());
//            }
//
//            if (externalID.getRelationship() != null) {
//                jsonExternalIdentifier.setRelationship(conv.convertTo(externalID.getRelationship().value(), null));
//            }
//            jsonFundingExternalIdentifiers.getFundingExternalIdentifier().add(jsonExternalIdentifier);
//        }
//        return JsonUtils.convertToJsonString(jsonFundingExternalIdentifiers);
    }

    @Test
    public void testConvertFrom(String source, Type<ExternalIDs> destinationType) {
//        JSONFundingExternalIdentifiers fundingExternalIdentifiers = JsonUtils.readObjectFromJsonString(source, JSONFundingExternalIdentifiers.class);
//        ExternalIDs externalIDs = new ExternalIDs();
//        for (JSONExternalIdentifier externalIdentifier : fundingExternalIdentifiers.getFundingExternalIdentifier()) {
//            ExternalID id = new ExternalID();
//            if (externalIdentifier.getType() == null) {
//                id.setType(FundingExternalIdentifierType.GRANT_NUMBER.value());
//            } else {
//                id.setType(externalIdentifier.getType().toLowerCase());
//            }
//            if (externalIdentifier.getUrl() != null && !PojoUtil.isEmpty(externalIdentifier.getUrl().getValue())) {
//                Url url = new Url(externalIdentifier.getUrl().getValue());
//                id.setUrl(url);
//            }
//
//            if (!PojoUtil.isEmpty(externalIdentifier.getValue())) {
//                id.setValue(externalIdentifier.getValue());
//            }
//
//            if (externalIdentifier.getRelationship() != null) {
//                id.setRelationship(Relationship.fromValue(conv.convertFrom(externalIdentifier.getRelationship(), null)));
//            }
//            externalIDs.getExternalIdentifier().add(id);
//        }
//        return externalIDs;
    }

}
