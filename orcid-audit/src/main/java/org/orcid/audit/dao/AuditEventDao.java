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
package org.orcid.audit.dao;

import org.orcid.audit.entities.AuditEvent;

/**
 * @author Declan Newman (declan)
 *         Date: 24/07/2012
 */
public interface AuditEventDao {

    Long count();

    void persist(AuditEvent event);

    void removeAll();
}
