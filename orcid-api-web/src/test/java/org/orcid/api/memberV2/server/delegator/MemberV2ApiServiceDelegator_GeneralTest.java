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
package org.orcid.api.memberV2.server.delegator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.NoResultException;
import javax.ws.rs.core.Response;

import org.orcid.test.DBUnitTest;
import org.orcid.test.helper.Utils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.orcid.core.exception.OrcidAccessControlException;
import org.orcid.core.utils.SecurityContextTestUtils;
import org.orcid.jaxb.model.common_v2.Iso3166Country;
import org.orcid.jaxb.model.groupid_v2.GroupIdRecord;
import org.orcid.jaxb.model.client_v2.Client;
import org.orcid.jaxb.model.message.ScopePathType;
import org.orcid.jaxb.model.record_v2.Address;
import org.orcid.jaxb.model.record_v2.Education;
import org.orcid.jaxb.model.record_v2.Employment;
import org.orcid.jaxb.model.record_v2.Funding;
import org.orcid.jaxb.model.record_v2.Keyword;
import org.orcid.jaxb.model.record_v2.OtherName;
import org.orcid.jaxb.model.record_v2.PeerReview;
import org.orcid.jaxb.model.record_v2.PersonExternalIdentifier;
import org.orcid.jaxb.model.record_v2.ResearcherUrl;
import org.orcid.jaxb.model.record_v2.Work;
import org.orcid.jaxb.model.record_v2.WorkBulk;
import org.orcid.test.OrcidJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;

@RunWith(OrcidJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:orcid-api-web-context.xml", "classpath:orcid-api-security-context.xml" })
public class MemberV2ApiServiceDelegator_GeneralTest extends DBUnitTest {
    protected static final List<String> DATA_FILES = Arrays.asList("/data/EmptyEntityData.xml", "/data/SecurityQuestionEntityData.xml",
            "/data/SourceClientDetailsEntityData.xml", "/data/ProfileEntityData.xml", "/data/WorksEntityData.xml", "/data/ClientDetailsEntityData.xml",
            "/data/Oauth2TokenDetailsData.xml", "/data/OrgsEntityData.xml", "/data/ProfileFundingEntityData.xml", "/data/OrgAffiliationEntityData.xml",
            "/data/PeerReviewEntityData.xml", "/data/GroupIdRecordEntityData.xml", "/data/RecordNameEntityData.xml", "/data/BiographyEntityData.xml");

    // Now on, for any new test, PLAESE USER THIS ORCID ID
    protected final String ORCID = "0000-0000-0000-0003";

    @Resource(name = "memberV2ApiServiceDelegator")
    protected MemberV2ApiServiceDelegator<Education, Employment, PersonExternalIdentifier, Funding, GroupIdRecord, OtherName, PeerReview, ResearcherUrl, Work, WorkBulk, Address, Keyword> serviceDelegator;

    @BeforeClass
    public static void initDBUnitData() throws Exception {
        initDBUnitData(DATA_FILES);
    }

    @AfterClass
    public static void removeDBUnitData() throws Exception {
        Collections.reverse(DATA_FILES);
        removeDBUnitData(DATA_FILES);
    }

    @Test
    public void testOrcidProfileCreate_CANT_AddOnClaimedAccounts() {
        SecurityContextTestUtils.setUpSecurityContextForClientOnly();

        // Test can't create
        try {
            serviceDelegator.createAddress(ORCID, Utils.getAddress());
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.createEducation(ORCID, Utils.getEducation());
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.createEmployment(ORCID, Utils.getEmployment());
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.createExternalIdentifier(ORCID, Utils.getPersonExternalIdentifier());
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.createFunding(ORCID, Utils.getFunding());
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.createKeyword(ORCID, Utils.getKeyword());
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.createOtherName(ORCID, Utils.getOtherName());
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.createPeerReview(ORCID, Utils.getPeerReview());
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.createResearcherUrl(ORCID, Utils.getResearcherUrl());
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.createWork(ORCID, Utils.getWork("work # 1 " + System.currentTimeMillis()));
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.createGroupIdRecord(Utils.getGroupIdRecord());
        } catch (OrcidAccessControlException e) {
            
        } catch(Exception e) {
            fail();
        } 
    }

    @Test
    public void testOrcidProfileCreate_CANT_ViewOnClaimedAccounts() {
        SecurityContextTestUtils.setUpSecurityContextForClientOnly();
        try {
            serviceDelegator.viewActivities(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewAddress(ORCID, 9L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewAddresses(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewBiography(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewEducation(ORCID, 20L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewEducationSummary(ORCID, 20L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewEducations(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewEmails(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewEmployment(ORCID, 17L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewEmploymentSummary(ORCID, 17L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewEmployments(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewExternalIdentifier(ORCID, 13L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewExternalIdentifiers(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewFunding(ORCID, 10L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewFundingSummary(ORCID, 10L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewFundings(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewKeyword(ORCID, 9L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewKeywords(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewOtherName(ORCID, 13L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewOtherNames(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewPeerReview(ORCID, 9L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewPeerReviewSummary(ORCID, 9L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewPeerReviews(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewPerson(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewPersonalDetails(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewResearcherUrl(ORCID, 13L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewResearcherUrls(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewWork(ORCID, 11L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewWorkSummary(ORCID, 11L);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewWorks(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewRecord(ORCID);
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.viewGroupIdRecord(1L);
        } catch (OrcidAccessControlException e) {
            
        } catch(Exception e) {
            fail();
        } 
        try {
            serviceDelegator.viewGroupIdRecords("10", "0");
        } catch (OrcidAccessControlException e) {
            
        } catch(Exception e) {
            fail();
        } 
    }

    @Test
    public void testOrcidProfileCreate_CANT_DeleteOnClaimedAccounts() {
        SecurityContextTestUtils.setUpSecurityContextForClientOnly();
        try {
            serviceDelegator.deleteAddress(ORCID, 9L);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.deleteAffiliation(ORCID, 20L);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.deleteExternalIdentifier(ORCID, 13L);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.deleteFunding(ORCID, 10L);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.deleteKeyword(ORCID, 9L);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.deleteOtherName(ORCID, 13L);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.deletePeerReview(ORCID, 9L);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.deleteResearcherUrl(ORCID, 13L);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
        try {
            serviceDelegator.deleteWork(ORCID, 11L);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
    }

    @Test
    public void testOrcidProfileCreate_CANT_UpdateOnClaimedAccounts() {
        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        Response response = serviceDelegator.viewAddress(ORCID, 9L);
        assertNotNull(response);
        Address a = (Address) response.getEntity();
        assertNotNull(a);
        try {
            SecurityContextTestUtils.setUpSecurityContextForClientOnly();
            serviceDelegator.updateAddress(ORCID, a.getPutCode(), a);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }

        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        response = serviceDelegator.viewEducation(ORCID, 20L);
        assertNotNull(response);
        Education edu = (Education) response.getEntity();
        assertNotNull(edu);
        try {
            SecurityContextTestUtils.setUpSecurityContextForClientOnly();
            serviceDelegator.updateEducation(ORCID, edu.getPutCode(), edu);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }

        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        response = serviceDelegator.viewEmployment(ORCID, 17L);
        assertNotNull(response);
        Employment emp = (Employment) response.getEntity();
        assertNotNull(emp);
        try {
            SecurityContextTestUtils.setUpSecurityContextForClientOnly();
            serviceDelegator.updateEmployment(ORCID, emp.getPutCode(), emp);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }

        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        response = serviceDelegator.viewExternalIdentifier(ORCID, 13L);
        assertNotNull(response);
        PersonExternalIdentifier extId = (PersonExternalIdentifier) response.getEntity();
        assertNotNull(extId);
        try {
            SecurityContextTestUtils.setUpSecurityContextForClientOnly();
            serviceDelegator.updateExternalIdentifier(ORCID, extId.getPutCode(), extId);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }

        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        response = serviceDelegator.viewFunding(ORCID, 10L);
        assertNotNull(response);
        Funding f = (Funding) response.getEntity();
        assertNotNull(f);
        try {
            SecurityContextTestUtils.setUpSecurityContextForClientOnly();
            serviceDelegator.updateFunding(ORCID, f.getPutCode(), f);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }

        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        response = serviceDelegator.viewKeyword(ORCID, 9L);
        assertNotNull(response);
        Keyword k = (Keyword) response.getEntity();
        assertNotNull(k);
        try {
            SecurityContextTestUtils.setUpSecurityContextForClientOnly();
            serviceDelegator.updateKeyword(ORCID, k.getPutCode(), k);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }

        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        response = serviceDelegator.viewOtherName(ORCID, 13L);
        assertNotNull(response);
        OtherName o = (OtherName) response.getEntity();
        assertNotNull(o);
        try {
            SecurityContextTestUtils.setUpSecurityContextForClientOnly();
            serviceDelegator.updateOtherName(ORCID, o.getPutCode(), o);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }

        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        response = serviceDelegator.viewPeerReview(ORCID, 9L);
        assertNotNull(response);
        PeerReview p = (PeerReview) response.getEntity();
        assertNotNull(p);
        try {
            SecurityContextTestUtils.setUpSecurityContextForClientOnly();
            serviceDelegator.updatePeerReview(ORCID, p.getPutCode(), p);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }

        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        response = serviceDelegator.viewResearcherUrl(ORCID, 13L);
        assertNotNull(response);
        ResearcherUrl r = (ResearcherUrl) response.getEntity();
        assertNotNull(r);
        try {
            SecurityContextTestUtils.setUpSecurityContextForClientOnly();
            serviceDelegator.updateResearcherUrl(ORCID, r.getPutCode(), r);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }

        SecurityContextTestUtils.setUpSecurityContext(ORCID, ScopePathType.READ_LIMITED);
        response = serviceDelegator.viewWork(ORCID, 11L);
        assertNotNull(response);
        Work w = (Work) response.getEntity();
        assertNotNull(w);
        try {
            SecurityContextTestUtils.setUpSecurityContextForClientOnly();
            serviceDelegator.updateWork(ORCID, w.getPutCode(), w);
            fail();
        } catch (IllegalStateException e) {
            assertEquals("Non client credential scope found in client request", e.getMessage());
        }
    }

    @Test
    public void testOrcidProfileCreate_CAN_CRUDOnUnclaimedAccounts() {
        String orcid = "0000-0000-0000-0001";
        SecurityContextTestUtils.setUpSecurityContextForClientOnly();
        // Test address
        Response response = serviceDelegator.createAddress(orcid, Utils.getAddress());
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        Long putCode = Utils.getPutCode(response);
        response = serviceDelegator.viewAddress(orcid, putCode);
        assertNotNull(response);
        Address address = (Address) response.getEntity();
        assertNotNull(address);
        address.getCountry().setValue(Iso3166Country.ZW);
        response = serviceDelegator.updateAddress(orcid, putCode, address);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response = serviceDelegator.deleteAddress(orcid, putCode);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        // Test education
        response = serviceDelegator.createEducation(orcid, Utils.getEducation());
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        putCode = Utils.getPutCode(response);
        response = serviceDelegator.viewEducation(orcid, putCode);
        assertNotNull(response);
        Education education = (Education) response.getEntity();
        assertNotNull(education);
        education.setDepartmentName("Updated department name");
        response = serviceDelegator.updateEducation(orcid, putCode, education);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response = serviceDelegator.deleteAffiliation(orcid, putCode);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        // Test employment
        response = serviceDelegator.createEmployment(orcid, Utils.getEmployment());
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        putCode = Utils.getPutCode(response);
        response = serviceDelegator.viewEmployment(orcid, putCode);
        assertNotNull(response);
        Employment employment = (Employment) response.getEntity();
        assertNotNull(employment);
        employment.setDepartmentName("Updated department name");
        response = serviceDelegator.updateEmployment(orcid, putCode, employment);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response = serviceDelegator.deleteAffiliation(orcid, putCode);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        // Test external identifiers
        response = serviceDelegator.createExternalIdentifier(orcid, Utils.getPersonExternalIdentifier());
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        putCode = Utils.getPutCode(response);
        response = serviceDelegator.viewExternalIdentifier(orcid, putCode);
        assertNotNull(response);
        PersonExternalIdentifier externalIdentifier = (PersonExternalIdentifier) response.getEntity();
        assertNotNull(externalIdentifier);
        response = serviceDelegator.updateExternalIdentifier(orcid, putCode, externalIdentifier);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response = serviceDelegator.deleteExternalIdentifier(orcid, putCode);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        // Test funding
        response = serviceDelegator.createFunding(orcid, Utils.getFunding());
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        putCode = Utils.getPutCode(response);
        response = serviceDelegator.viewFunding(orcid, putCode);
        assertNotNull(response);
        Funding funding = (Funding) response.getEntity();
        assertNotNull(funding);
        response = serviceDelegator.updateFunding(orcid, putCode, funding);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response = serviceDelegator.deleteFunding(orcid, putCode);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        // Test keyword
        response = serviceDelegator.createKeyword(orcid, Utils.getKeyword());
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        putCode = Utils.getPutCode(response);
        response = serviceDelegator.viewKeyword(orcid, putCode);
        assertNotNull(response);
        Keyword keyword = (Keyword) response.getEntity();
        assertNotNull(keyword);
        response = serviceDelegator.updateKeyword(orcid, putCode, keyword);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response = serviceDelegator.deleteKeyword(orcid, putCode);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        // Test other names
        response = serviceDelegator.createOtherName(orcid, Utils.getOtherName());
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        putCode = Utils.getPutCode(response);
        response = serviceDelegator.viewOtherName(orcid, putCode);
        assertNotNull(response);
        OtherName otherName = (OtherName) response.getEntity();
        assertNotNull(otherName);
        response = serviceDelegator.updateOtherName(orcid, putCode, otherName);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response = serviceDelegator.deleteOtherName(orcid, putCode);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        // Test peer review
        response = serviceDelegator.createPeerReview(orcid, Utils.getPeerReview());
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        putCode = Utils.getPutCode(response);
        response = serviceDelegator.viewPeerReview(orcid, putCode);
        assertNotNull(response);
        PeerReview peerReview = (PeerReview) response.getEntity();
        assertNotNull(peerReview);
        response = serviceDelegator.updatePeerReview(orcid, putCode, peerReview);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response = serviceDelegator.deletePeerReview(orcid, putCode);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        // Test researcher url
        response = serviceDelegator.createResearcherUrl(orcid, Utils.getResearcherUrl());
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        putCode = Utils.getPutCode(response);
        response = serviceDelegator.viewResearcherUrl(orcid, putCode);
        assertNotNull(response);
        ResearcherUrl rUrl = (ResearcherUrl) response.getEntity();
        assertNotNull(rUrl);
        response = serviceDelegator.updateResearcherUrl(orcid, putCode, rUrl);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response = serviceDelegator.deleteResearcherUrl(orcid, putCode);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());

        // Test work
        response = serviceDelegator.createWork(orcid, Utils.getWork("work # 1 " + System.currentTimeMillis()));
        assertNotNull(response);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        putCode = Utils.getPutCode(response);
        response = serviceDelegator.viewWork(orcid, putCode);
        assertNotNull(response);
        Work work = (Work) response.getEntity();
        assertNotNull(work);
        response = serviceDelegator.updateWork(orcid, putCode, work);
        assertNotNull(response);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        response = serviceDelegator.deleteWork(orcid, putCode);
        assertNotNull(response);
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatus());
    }
    
    @Test(expected = NoResultException.class)
    public void testViewClientNonExistent() {
        serviceDelegator.viewClient("some-client-that-doesn't-exist");
        fail();
    }

    @Test
    public void testViewClient() {
        Response response = serviceDelegator.viewClient("APP-6666666666666666");
        assertNotNull(response.getEntity());
        assertTrue(response.getEntity() instanceof Client);

        Client client = (Client) response.getEntity();
        assertEquals("Source Client 2", client.getName());
        assertEquals("A test source client", client.getDescription());
    }
}
