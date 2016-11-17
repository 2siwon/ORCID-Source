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
package org.orcid.core.manager.read_only;

import java.util.Map;

import org.orcid.jaxb.model.record_rc3.Emails;

/**
 * 
 * @author Will Simpson
 *
 */
public interface EmailManagerReadOnly extends ManagerReadOnlyBase {

    boolean emailExists(String email);

    Map<String, String> findIdByEmail(String email);
    
    boolean isPrimaryEmailVerified(String orcid);
    
    boolean haveAnyEmailVerified(String orcid);
    
    @Deprecated
    org.orcid.pojo.ajaxForm.Emails getEmailsAsForm(String orcid);
    
    Emails getEmails(String orcid, long lastModified);
    
    Emails getPublicEmails(String orcid, long lastModified);
}
