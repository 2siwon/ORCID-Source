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
package org.orcid.api.memberV3.server.delegator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.orcid.core.exception.OrcidAccessControlException;
import org.orcid.core.exception.OrcidDuplicatedActivityException;
import org.orcid.core.exception.OrcidUnauthorizedException;
import org.orcid.core.exception.OrcidValidationException;
import org.orcid.core.exception.OrcidVisibilityException;
import org.orcid.core.exception.VisibilityMismatchException;
import org.orcid.core.exception.WrongSourceException;
import org.orcid.core.utils.SecurityContextTestUtils;
import org.orcid.jaxb.model.groupid_v2.GroupIdRecord;
import org.orcid.jaxb.model.message.ScopePathType;
import org.orcid.jaxb.model.v3.dev1.common.DisambiguatedOrganization;
import org.orcid.jaxb.model.v3.dev1.common.LastModifiedDate;
import org.orcid.jaxb.model.v3.dev1.common.Url;
import org.orcid.jaxb.model.v3.dev1.common.Visibility;
import org.orcid.jaxb.model.v3.dev1.record.Address;
import org.orcid.jaxb.model.v3.dev1.record.AffiliationType;
import org.orcid.jaxb.model.v3.dev1.record.Distinction;
import org.orcid.jaxb.model.v3.dev1.record.Education;
import org.orcid.jaxb.model.v3.dev1.record.Employment;
import org.orcid.jaxb.model.v3.dev1.record.ExternalID;
import org.orcid.jaxb.model.v3.dev1.record.ExternalIDs;
import org.orcid.jaxb.model.v3.dev1.record.Funding;
import org.orcid.jaxb.model.v3.dev1.record.InvitedPosition;
import org.orcid.jaxb.model.v3.dev1.record.Keyword;
import org.orcid.jaxb.model.v3.dev1.record.Membership;
import org.orcid.jaxb.model.v3.dev1.record.OtherName;
import org.orcid.jaxb.model.v3.dev1.record.PeerReview;
import org.orcid.jaxb.model.v3.dev1.record.PersonExternalIdentifier;
import org.orcid.jaxb.model.v3.dev1.record.Qualification;
import org.orcid.jaxb.model.v3.dev1.record.Relationship;
import org.orcid.jaxb.model.v3.dev1.record.ResearcherUrl;
import org.orcid.jaxb.model.v3.dev1.record.Service;
import org.orcid.jaxb.model.v3.dev1.record.Work;
import org.orcid.jaxb.model.v3.dev1.record.WorkBulk;
import org.orcid.jaxb.model.v3.dev1.record.summary.ActivitiesSummary;
import org.orcid.jaxb.model.v3.dev1.record.summary.InvitedPositionSummary;
import org.orcid.jaxb.model.v3.dev1.record.summary.InvitedPositions;
import org.orcid.test.DBUnitTest;
import org.orcid.test.OrcidJUnit4ClassRunner;
import org.orcid.test.helper.v3.Utils;
import org.springframework.test.context.ContextConfiguration;

@RunWith(OrcidJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:orcid-api-web-context.xml", "classpath:orcid-api-security-context.xml" })
public class MemberV3ApiServiceDelegator_InvitedPositionsTest extends DBUnitTest {
    protected static final List<String> DATA_FILES = Arrays.asList("/data/EmptyEntityData.xml", "/data/SecurityQuestionEntityData.xml",
            "/data/SourceClientDetailsEntityData.xml", "/data/ProfileEntityData.xml", "/data/WorksEntityData.xml", "/data/ClientDetailsEntityData.xml",
            "/data/Oauth2TokenDetailsData.xml", "/data/OrgsEntityData.xml", "/data/ProfileFundingEntityData.xml", "/data/OrgAffiliationEntityData.xml",
            "/data/PeerReviewEntityData.xml", "/data/GroupIdRecordEntityData.xml", "/data/RecordNameEntityData.xml", "/data/BiographyEntityData.xml");

    // Now on, for any new test, PLAESE USE THIS ORCID ID
    protected final String ORCID = "0000-0000-0000-0003";

    @Resource(name = "memberV3ApiServiceDelegatorV3_0_dev1")
    protected MemberV3ApiServiceDelegator<Distinction, Education, Employment, PersonExternalIdentifier, InvitedPosition, Funding, GroupIdRecord, Membership, OtherName, PeerReview, Qualification, ResearcherUrl, Service, Work, WorkBulk, Address, Keyword> serviceDelegator;

    @BeforeClass
    public static void initDBUnitData() throws Exception {
        initDBUnitData(DATA_FILES);
    }

    @AfterClass
    public static void removeDBUnitData() throws Exception {
        Collections.reverse(DATA_FILES);
        removeDBUnitData(DATA_FILES);
    }

    @Test(expected = OrcidUnauthorizedException.class)
    public void testViewInvitedPositionsWrongToken() {
        SecurityContextTestUtils.setUpSecurityContext("some-other-user", ScopePathType.READ_LIMITED);
        serviceDelegator.viewInvitedPositions(ORCID);
    }

    @Test(expected = OrcidUnauthorizedException.class)
    public void testViewInvitedPositionWrongToken() {
        SecurityContextTestUtils.setUpSecurityContext("some-other-user", ScopePathType.READ_LIMITED);
        serviceDelegator.viewInvitedPosition(ORCID, 20L);
    }

    @Test
    public void testViewInvitedPositionReadPublic() {
        SecurityContextTestUtils.setUpSecurityContextForClientOnly("APP-5555555555555555", ScopePathType.READ_PUBLIC);
        Response r = serviceDelegator.viewInvitedPosition(ORCID, 20L);
        InvitedPosition element = (InvitedPosition) r.getEntity();
        assertNotNull(element);
        assertEquals("/0000-0000-0000-0003/invited-position/20", element.getPath());
        Utils.assertIsPublicOrSource(element, "APP-5555555555555555");
    }

    @Test(expected = OrcidUnauthorizedException.class)
    public void testViewInvitedPositionSummaryWrongToken() {
        SecurityContextTestUtils.setUpSecurityContext("some-other-user", ScopePathType.READ_LIMITED);
        serviceDelegator.viewInvitedPositionSummary(ORCID, 20L);
    }

    @Test
    public void testViewInvitedPositionsReadPublic() {
        SecurityContextTestUtils.setUpSecurityContextForClientOnly("APP-5555555555555555", ScopePathType.READ_PUBLIC);
        Response r = serviceDelegator.viewInvitedPositions(ORCID);
        InvitedPositions element = (InvitedPositions) r.getEntity();
        assertNotNull(element);
        assertEquals("/0000-0000-0000-0003/invited-positions", element.getPath());
        Utils.assertIsPublicOrSource(element, "APP-5555555555555555");
    }

    @Test
    public void testViewInvitedPositionSummaryReadPublic() {
        SecurityContextTestUtils.setUpSecurityContextForClientOnly("APP-5555555555555555", ScopePathType.READ_PUBLIC);
        Response r = serviceDelegator.viewInvitedPositionSummary(ORCID, 20L);
        InvitedPositionSummary element = (InvitedPositionSummary) r.getEntity();
        assertNotNull(element);
        assertEquals("/0000-0000-0000-0003/invited-position/20", element.getPath());
        Utils.assertIsPublicOrSource(element, "APP-5555555555555555");
    }

    @Test
    public void testViewPublicInvitedPosition() {
        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        Response response = serviceDelegator.viewInvitedPosition(ORCID, 32L);
        assertNotNull(response);
        InvitedPosition invitedPosition = (InvitedPosition) response.getEntity();
        assertNotNull(invitedPosition);
        Utils.verifyLastModified(invitedPosition.getLastModifiedDate());
        assertEquals(Long.valueOf(32L), invitedPosition.getPutCode());
        assertEquals("/0000-0000-0000-0003/invited-position/32", invitedPosition.getPath());
        assertEquals("PUBLIC Department", invitedPosition.getDepartmentName());
        assertEquals(Visibility.PUBLIC.value(), invitedPosition.getVisibility().value());
    }

    @Test
    public void testViewLimitedInvitedPosition() {
        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        Response response = serviceDelegator.viewInvitedPosition(ORCID, 35L);
        assertNotNull(response);
        InvitedPosition invitedPosition = (InvitedPosition) response.getEntity();
        assertNotNull(invitedPosition);
        Utils.verifyLastModified(invitedPosition.getLastModifiedDate());
        assertEquals(Long.valueOf(35L), invitedPosition.getPutCode());
        assertEquals("/0000-0000-0000-0003/invited-position/35", invitedPosition.getPath());
        assertEquals("SELF LIMITED Department", invitedPosition.getDepartmentName());
        assertEquals(Visibility.LIMITED.value(), invitedPosition.getVisibility().value());
    }

    @Test
    public void testViewPrivateInvitedPosition() {
        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        Response response = serviceDelegator.viewInvitedPosition(ORCID, 34L);
        assertNotNull(response);
        InvitedPosition invitedPosition = (InvitedPosition) response.getEntity();
        assertNotNull(invitedPosition);
        Utils.verifyLastModified(invitedPosition.getLastModifiedDate());
        assertEquals(Long.valueOf(34L), invitedPosition.getPutCode());
        assertEquals("/0000-0000-0000-0003/invited-position/34", invitedPosition.getPath());
        assertEquals("PRIVATE Department", invitedPosition.getDepartmentName());
        assertEquals(Visibility.PRIVATE.value(), invitedPosition.getVisibility().value());
    }

    @Test(expected = OrcidVisibilityException.class)
    public void testViewPrivateInvitedPositionWhereYouAreNotTheSource() {
        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        serviceDelegator.viewInvitedPosition(ORCID, 31L);
        fail();
    }

    @Test(expected = NoResultException.class)
    public void testViewInvitedPositionThatDontBelongToTheUser() {
        SecurityContextTestUtils.setUpSecurityContext("4444-4444-4444-4446", ScopePathType.READ_LIMITED);
        // InvitedPosition 32 belongs to 0000-0000-0000-0003
        serviceDelegator.viewInvitedPosition("4444-4444-4444-4446", 32L);
        fail();
    }

    @Test
    public void testViewInvitedPositions() {
        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        Response r = serviceDelegator.viewInvitedPositions(ORCID);
        assertNotNull(r);
        InvitedPositions invitedPositions = (InvitedPositions) r.getEntity();
        assertNotNull(invitedPositions);
        assertEquals("/0000-0000-0000-0003/invited-positions", invitedPositions.getPath());
        Utils.verifyLastModified(invitedPositions.getLastModifiedDate());
        assertNotNull(invitedPositions.getSummaries());
        assertEquals(4, invitedPositions.getSummaries().size());
        boolean found1 = false, found2 = false, found3 = false, found4 = false;
        for (InvitedPositionSummary summary : invitedPositions.getSummaries()) {
            Utils.verifyLastModified(summary.getLastModifiedDate());
            if (Long.valueOf(32).equals(summary.getPutCode())) {
                assertEquals("PUBLIC Department", summary.getDepartmentName());
                found1 = true;
            } else if (Long.valueOf(33).equals(summary.getPutCode())) {
                assertEquals("LIMITED Department", summary.getDepartmentName());
                found2 = true;
            } else if (Long.valueOf(34).equals(summary.getPutCode())) {
                assertEquals("PRIVATE Department", summary.getDepartmentName());
                found3 = true;
            } else if (Long.valueOf(35).equals(summary.getPutCode())) {
                assertEquals("SELF LIMITED Department", summary.getDepartmentName());
                found4 = true;
            } else {
                fail("Invalid invitedPosition found: " + summary.getPutCode());
            }
        }
        assertTrue(found1);
        assertTrue(found2);
        assertTrue(found3);
        assertTrue(found4);
    }

    @Test
    public void testReadPublicScope_InvitedPositions() {
        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_PUBLIC);
        Response r = serviceDelegator.viewInvitedPosition(ORCID, 32L);
        assertNotNull(r);
        assertEquals(InvitedPosition.class.getName(), r.getEntity().getClass().getName());

        r = serviceDelegator.viewInvitedPositionSummary(ORCID, 32L);
        assertNotNull(r);
        assertEquals(InvitedPositionSummary.class.getName(), r.getEntity().getClass().getName());

        // Limited that am the source of should work
        serviceDelegator.viewInvitedPosition(ORCID, 33L);
        serviceDelegator.viewInvitedPositionSummary(ORCID, 33L);
        
        // Private that am the source of should work
        serviceDelegator.viewInvitedPosition(ORCID, 34L);
        serviceDelegator.viewInvitedPositionSummary(ORCID, 34L);
        
        
        // Limited that am not the source of should fail
        try {
            serviceDelegator.viewInvitedPosition(ORCID, 35L);
            fail();
        } catch (OrcidAccessControlException e) {

        } catch (Exception e) {
            fail();
        }

        try {
            serviceDelegator.viewInvitedPositionSummary(ORCID, 35L);
            fail();
        } catch (OrcidAccessControlException e) {

        } catch (Exception e) {
            fail();
        }

        // Private that am not the source of should fails
        try {
            serviceDelegator.viewInvitedPosition(ORCID, 35L);
            fail();
        } catch (OrcidAccessControlException e) {

        } catch (Exception e) {
            fail();
        }

        try {
            serviceDelegator.viewInvitedPositionSummary(ORCID, 35L);
            fail();
        } catch (OrcidAccessControlException e) {

        } catch (Exception e) {
            fail();
        }
    }

    @Test
    public void testAddInvitedPosition() {
        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED, ScopePathType.ACTIVITIES_UPDATE);
        Response response = serviceDelegator.viewActivities(ORCID);
        assertNotNull(response);
        ActivitiesSummary originalSummary = (ActivitiesSummary) response.getEntity();
        assertNotNull(originalSummary);
        Utils.verifyLastModified(originalSummary.getLastModifiedDate());
        assertNotNull(originalSummary.getInvitedPositions());
        Utils.verifyLastModified(originalSummary.getInvitedPositions().getLastModifiedDate());
        assertNotNull(originalSummary.getInvitedPositions().getSummaries());
        assertNotNull(originalSummary.getInvitedPositions().getSummaries().get(0));
        Utils.verifyLastModified(originalSummary.getInvitedPositions().getSummaries().get(0).getLastModifiedDate());
        assertEquals(5, originalSummary.getInvitedPositions().getSummaries().size());

        response = serviceDelegator.createInvitedPosition(ORCID, (InvitedPosition) Utils.getAffiliation(AffiliationType.INVITED_POSITION));
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        Map<?, ?> map = response.getMetadata();
        assertNotNull(map);
        assertTrue(map.containsKey("Location"));
        List<?> resultWithPutCode = (List<?>) map.get("Location");
        Long putCode = Long.valueOf(String.valueOf(resultWithPutCode.get(0)));

        response = serviceDelegator.viewActivities(ORCID);
        assertNotNull(response);
        ActivitiesSummary summaryWithNewElement = (ActivitiesSummary) response.getEntity();
        assertNotNull(summaryWithNewElement);
        Utils.verifyLastModified(summaryWithNewElement.getLastModifiedDate());
        assertNotNull(summaryWithNewElement.getInvitedPositions());
        Utils.verifyLastModified(summaryWithNewElement.getInvitedPositions().getLastModifiedDate());
        assertNotNull(summaryWithNewElement.getInvitedPositions().getSummaries());
        assertEquals(6, summaryWithNewElement.getInvitedPositions().getSummaries().size());
        
        boolean haveNew = false;

        for (InvitedPositionSummary invitedPositionSummary : summaryWithNewElement.getInvitedPositions().getSummaries()) {
            assertNotNull(invitedPositionSummary.getPutCode());
            Utils.verifyLastModified(invitedPositionSummary.getLastModifiedDate());
            if (invitedPositionSummary.getPutCode() == putCode) {
                assertEquals("My department name", invitedPositionSummary.getDepartmentName());
                haveNew = true;
            } else {
                assertTrue(originalSummary.getInvitedPositions().getSummaries().contains(invitedPositionSummary));
            }
        }
        
        assertTrue(haveNew);
        
        //Remove new element
        serviceDelegator.deleteAffiliation(ORCID, putCode);
        response = serviceDelegator.viewActivities(ORCID);
        assertNotNull(response);
        ActivitiesSummary summaryAfterRemovingNewElement = (ActivitiesSummary) response.getEntity();
        assertNotNull(summaryAfterRemovingNewElement);
        assertEquals(5, summaryAfterRemovingNewElement.getInvitedPositions().getSummaries().size());
        
    }
    
    @Test(expected = OrcidValidationException.class)
    public void testAddInvitedPositionNoStartDate() {
        SecurityContextTestUtils.setUpSecurityContext("4444-4444-4444-4442", ScopePathType.READ_LIMITED, ScopePathType.ACTIVITIES_UPDATE);
        InvitedPosition invitedPosition = (InvitedPosition) Utils.getAffiliation(AffiliationType.INVITED_POSITION);
        invitedPosition.setStartDate(null);
        serviceDelegator.createInvitedPosition("4444-4444-4444-4442", invitedPosition);
    }
    
    @Test(expected = OrcidDuplicatedActivityException.class)
    public void testAddInvitedPositionsDuplicateExternalIDs() {
        SecurityContextTestUtils.setUpSecurityContext("4444-4444-4444-4447", ScopePathType.READ_LIMITED, ScopePathType.ACTIVITIES_UPDATE);

        ExternalID e1 = new ExternalID();
        e1.setRelationship(Relationship.SELF);
        e1.setType("erm");
        e1.setUrl(new Url("https://orcid.org"));
        e1.setValue("err");

        ExternalID e2 = new ExternalID();
        e2.setRelationship(Relationship.SELF);
        e2.setType("err");
        e2.setUrl(new Url("http://bbc.co.uk"));
        e2.setValue("erm");

        ExternalIDs externalIDs = new ExternalIDs();
        externalIDs.getExternalIdentifier().add(e1);
        externalIDs.getExternalIdentifier().add(e2);

        InvitedPosition invitedPosition = (InvitedPosition) Utils.getAffiliation(AffiliationType.INVITED_POSITION);
        invitedPosition.setExternalIDs(externalIDs);

        Response response = serviceDelegator.createInvitedPosition("4444-4444-4444-4447", invitedPosition);
        assertNotNull(response);
        assertEquals(HttpStatus.SC_CREATED, response.getStatus());

        Map<?, ?> map = response.getMetadata();
        assertNotNull(map);
        assertTrue(map.containsKey("Location"));
        List<?> resultWithPutCode = (List<?>) map.get("Location");
        Long putCode = Long.valueOf(String.valueOf(resultWithPutCode.get(0)));

        try {
            InvitedPosition duplicate = (InvitedPosition) Utils.getAffiliation(AffiliationType.INVITED_POSITION);
            duplicate.setExternalIDs(externalIDs);
            serviceDelegator.createInvitedPosition("4444-4444-4444-4447", duplicate);
        } finally {
            serviceDelegator.deleteAffiliation("4444-4444-4444-4447", putCode);
        }
    }


    @Test
    public void testUpdateInvitedPosition() {
        SecurityContextTestUtils.setUpSecurityContext("4444-4444-4444-4443", ScopePathType.READ_LIMITED, ScopePathType.ACTIVITIES_UPDATE);
        Response response = serviceDelegator.viewInvitedPosition("4444-4444-4444-4443", 3L);
        assertNotNull(response);
        InvitedPosition invitedPosition = (InvitedPosition) response.getEntity();
        assertNotNull(invitedPosition);
        assertEquals("Another Department", invitedPosition.getDepartmentName());
        assertEquals("Student", invitedPosition.getRoleTitle());
        Utils.verifyLastModified(invitedPosition.getLastModifiedDate());

        LastModifiedDate before = invitedPosition.getLastModifiedDate();

        invitedPosition.setDepartmentName("Updated department name");
        invitedPosition.setRoleTitle("The updated role title");
        
        // disambiguated org is required in API v3
        DisambiguatedOrganization disambiguatedOrg = new DisambiguatedOrganization();
        disambiguatedOrg.setDisambiguatedOrganizationIdentifier("abc456");
        disambiguatedOrg.setDisambiguationSource("WDB");
        invitedPosition.getOrganization().setDisambiguatedOrganization(disambiguatedOrg);

        response = serviceDelegator.updateInvitedPosition("4444-4444-4444-4443", 3L, invitedPosition);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        response = serviceDelegator.viewInvitedPosition("4444-4444-4444-4443", 3L);
        assertNotNull(response);
        invitedPosition = (InvitedPosition) response.getEntity();
        assertNotNull(invitedPosition);
        Utils.verifyLastModified(invitedPosition.getLastModifiedDate());
        assertTrue(invitedPosition.getLastModifiedDate().after(before));
        assertEquals("Updated department name", invitedPosition.getDepartmentName());
        assertEquals("The updated role title", invitedPosition.getRoleTitle());

        // Rollback changes
        invitedPosition.setDepartmentName("Another Department");
        invitedPosition.setRoleTitle("Student");

        response = serviceDelegator.updateInvitedPosition("4444-4444-4444-4443", 3L, invitedPosition);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test(expected = WrongSourceException.class)
    public void testUpdateInvitedPositionYouAreNotTheSourceOf() {
        SecurityContextTestUtils.setUpSecurityContext("4444-4444-4444-4442", ScopePathType.READ_LIMITED, ScopePathType.ACTIVITIES_UPDATE);
        Response response = serviceDelegator.viewInvitedPosition("4444-4444-4444-4442", 1L);
        assertNotNull(response);
        InvitedPosition invitedPosition = (InvitedPosition) response.getEntity();
        assertNotNull(invitedPosition);
        invitedPosition.setDepartmentName("Updated department name");
        invitedPosition.setRoleTitle("The updated role title");
        serviceDelegator.updateInvitedPosition("4444-4444-4444-4442", 1L, invitedPosition);
        fail();
    }

    @Test(expected = VisibilityMismatchException.class)
    public void testUpdateInvitedPositionChangingVisibilityTest() {
        SecurityContextTestUtils.setUpSecurityContext("4444-4444-4444-4443", ScopePathType.READ_LIMITED, ScopePathType.ACTIVITIES_UPDATE);
        Response response = serviceDelegator.viewInvitedPosition("4444-4444-4444-4443", 3L);
        assertNotNull(response);
        InvitedPosition invitedPosition = (InvitedPosition) response.getEntity();
        assertNotNull(invitedPosition);
        assertEquals(Visibility.PUBLIC, invitedPosition.getVisibility());

        invitedPosition.setVisibility(invitedPosition.getVisibility().equals(Visibility.PRIVATE) ? Visibility.LIMITED : Visibility.PRIVATE);

        response = serviceDelegator.updateInvitedPosition("4444-4444-4444-4443", 3L, invitedPosition);
        fail();
    }

    @Test
    public void testUpdateInvitedPositionLeavingVisibilityNullTest() {
        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED, ScopePathType.ACTIVITIES_UPDATE);
        Response response = serviceDelegator.viewInvitedPosition(ORCID, 32L);
        assertNotNull(response);
        InvitedPosition invitedPosition = (InvitedPosition) response.getEntity();
        assertNotNull(invitedPosition);
        assertEquals(Visibility.PUBLIC, invitedPosition.getVisibility());
        
        invitedPosition.setVisibility(null);

        response = serviceDelegator.updateInvitedPosition(ORCID, 32L, invitedPosition);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        invitedPosition = (InvitedPosition) response.getEntity();
        assertNotNull(invitedPosition);
        assertEquals(Visibility.PUBLIC, invitedPosition.getVisibility());
    }
    
    @Test(expected = OrcidDuplicatedActivityException.class)
    public void testUpdateInvitedPositionDuplicateExternalIDs() {
        SecurityContextTestUtils.setUpSecurityContext("4444-4444-4444-4447", ScopePathType.READ_LIMITED, ScopePathType.ACTIVITIES_UPDATE);

        ExternalID e1 = new ExternalID();
        e1.setRelationship(Relationship.SELF);
        e1.setType("erm");
        e1.setUrl(new Url("https://orcid.org"));
        e1.setValue("err");

        ExternalID e2 = new ExternalID();
        e2.setRelationship(Relationship.SELF);
        e2.setType("err");
        e2.setUrl(new Url("http://bbc.co.uk"));
        e2.setValue("erm");

        ExternalIDs externalIDs = new ExternalIDs();
        externalIDs.getExternalIdentifier().add(e1);
        externalIDs.getExternalIdentifier().add(e2);

        InvitedPosition invitedPosition = (InvitedPosition) Utils.getAffiliation(AffiliationType.INVITED_POSITION);
        invitedPosition.setExternalIDs(externalIDs);

        Response response = serviceDelegator.createInvitedPosition("4444-4444-4444-4447", invitedPosition);
        assertNotNull(response);
        assertEquals(HttpStatus.SC_CREATED, response.getStatus());
        
        Map<?, ?> map = response.getMetadata();
        assertNotNull(map);
        assertTrue(map.containsKey("Location"));
        List<?> resultWithPutCode = (List<?>) map.get("Location");
        Long putCode1 = Long.valueOf(String.valueOf(resultWithPutCode.get(0)));

        InvitedPosition another = (InvitedPosition) Utils.getAffiliation(AffiliationType.INVITED_POSITION);
        response = serviceDelegator.createInvitedPosition("4444-4444-4444-4447", another);
        
        map = response.getMetadata();
        assertNotNull(map);
        assertTrue(map.containsKey("Location"));
        resultWithPutCode = (List<?>) map.get("Location");
        Long putCode2 = Long.valueOf(String.valueOf(resultWithPutCode.get(0)));
        
        response = serviceDelegator.viewInvitedPosition("4444-4444-4444-4447", putCode2);
        another = (InvitedPosition) response.getEntity();
        another.setExternalIDs(externalIDs);
        
        try {
            serviceDelegator.updateInvitedPosition("4444-4444-4444-4447", putCode2, another);
        } finally {
            serviceDelegator.deleteAffiliation("4444-4444-4444-4447", putCode1);
            serviceDelegator.deleteAffiliation("4444-4444-4444-4447", putCode2);
        }
    }

    @Test(expected = NoResultException.class)
    public void testDeleteInvitedPosition() {
        SecurityContextTestUtils.setUpSecurityContext("4444-4444-4444-4447", ScopePathType.READ_LIMITED, ScopePathType.ACTIVITIES_UPDATE);
        Response response = serviceDelegator.viewInvitedPosition("4444-4444-4444-4447", 12L);
        assertNotNull(response);
        InvitedPosition invitedPosition = (InvitedPosition) response.getEntity();
        assertNotNull(invitedPosition);

        response = serviceDelegator.deleteAffiliation("4444-4444-4444-4447", 12L);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        serviceDelegator.viewInvitedPosition("4444-4444-4444-4447", 12L);
    }

    @Test(expected = WrongSourceException.class)
    public void testDeleteInvitedPositionYouAreNotTheSourceOf() {
        SecurityContextTestUtils.setUpSecurityContext("4444-4444-4444-4446", ScopePathType.READ_LIMITED, ScopePathType.ACTIVITIES_UPDATE);
        serviceDelegator.deleteAffiliation("4444-4444-4444-4446", 9L);
        fail();
    }

}
