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
package org.orcid.frontend.web.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.orcid.core.oauth.OrcidClientCredentialEndPointDelegator;
import org.orcid.core.security.aop.LockedException;
import org.orcid.frontend.web.controllers.oauth.error.OAuthError;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

public class OauthGenericCallsControllerTest {

    @Mock
    private OrcidClientCredentialEndPointDelegator orcidClientCredentialEndPointDelegator;

    @InjectMocks
    private OauthGenericCallsController controller = new OauthGenericCallsController();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testObtainOauth2TokenPost() {
        when(orcidClientCredentialEndPointDelegator.obtainOauth2Token(anyString(), Matchers.<MultivaluedMap<String, String>> any())).thenReturn(
                Response.ok("some-success-entity").build());
        ResponseEntity<?> responseEntity = controller.obtainOauth2TokenPost(new MockHttpServletRequest());
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertEquals("some-success-entity", responseEntity.getBody());
    }

    @Test
    public void testObtainOauth2TokenPostLockedClient() {
        when(orcidClientCredentialEndPointDelegator.obtainOauth2Token(anyString(), Matchers.<MultivaluedMap<String, String>> any())).thenThrow(
                new LockedException("Client is locked"));
        ResponseEntity<?> responseEntity = controller.obtainOauth2TokenPost(new MockHttpServletRequest());
        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody());
        assertTrue(responseEntity.getBody() instanceof OAuthError);

        OAuthError error = (OAuthError) responseEntity.getBody();
        assertEquals(OAuthError.UNAUTHORIZED_CLIENT, error.getError());
        assertEquals("Client is locked", error.getErrorDescription());
    }

}
