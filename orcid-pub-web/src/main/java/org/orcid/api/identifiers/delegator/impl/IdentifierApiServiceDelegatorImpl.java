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
package org.orcid.api.identifiers.delegator.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javax.annotation.Resource;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.LocaleUtils;
import org.orcid.api.identifiers.delegator.IdentifierApiServiceDelegator;
import org.orcid.core.locale.LocaleManager;
import org.orcid.core.manager.IdentifierTypeManager;
import org.orcid.core.security.visibility.aop.AccessControl;
import org.orcid.jaxb.model.message.ScopePathType;
import org.orcid.pojo.IdentifierType;

public class IdentifierApiServiceDelegatorImpl implements IdentifierApiServiceDelegator {
    
    @Resource 
    IdentifierTypeManager identifierTypeManager;
    
    @Resource
    private LocaleManager localeManager;
    
    @Override
    @AccessControl(requiredScope = ScopePathType.READ_PUBLIC, enableAnonymousAccess = true)
    public Response getIdentifierTypes(String locale) {
        Optional<Locale> loc = Optional.of(LocaleUtils.toLocale(locale));
        Collection<IdentifierType> types = identifierTypeManager.fetchIdentifierTypesByAPITypeName(loc).values();
        return Response.ok(types).build();
    }

}
