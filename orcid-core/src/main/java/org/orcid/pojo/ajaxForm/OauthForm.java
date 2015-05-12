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
package org.orcid.pojo.ajaxForm;

public interface OauthForm {
    Text getClientId();

    void setClientId(Text clientId);

    Text getRedirectUri();

    void setRedirectUri(Text redirectUri);

    Text getScope();

    void setScope(Text scope);

    Text getResponseType();

    void setResponseType(Text responseType);
    
    boolean getApproved();

    void setApproved(boolean approved);
    
    boolean getPersistentTokenEnabled();

    void setPersistentTokenEnabled(boolean persistentTokenEnabled);
}
