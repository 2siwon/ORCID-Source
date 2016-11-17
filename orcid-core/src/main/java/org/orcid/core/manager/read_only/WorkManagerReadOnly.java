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

import java.util.List;

import org.orcid.jaxb.model.record.summary_rc3.WorkSummary;
import org.orcid.jaxb.model.record.summary_rc3.Works;
import org.orcid.jaxb.model.record_rc3.Work;

public interface WorkManagerReadOnly extends ManagerReadOnlyBase{           
    
    /**
     * Find the works for a specific user
     * 
     * @param orcid
     * 		the Id of the user
     * @return the list of works associated to the specific user 
     * */
    List<Work> findWorks(String orcid, long lastModified); 
    
    /**
     * Find the public works for a specific user
     * 
     * @param orcid
     * 		the Id of the user
     * @return the list of works associated to the specific user 
     * */
    List<Work> findPublicWorks(String orcid, long lastModified);
    
    /**
     * Get the given Work from the database
     * @param orcid
     *          The work owner
     * @param workId
     *          The work id             
     * */
    Work getWork(String orcid, Long workId, long lastModified);
    
    WorkSummary getWorkSummary(String orcid, Long workId, long lastModified);
    
    /**
     * Get the list of works that belongs to a user
     * 
     * @param userOrcid
     * @param lastModified
     *          Last modified date used to check the cache
     * @return the list of works that belongs to this user
     * */
    List<WorkSummary> getWorksSummaryList(String orcid, long lastModified);
    
    /**
     * Generate a grouped list of works with the given list of works
     * 
     * @param works
     *          The list of works to group
     * @param justPublic
     *          Specify if we want to group only the public elements in the given list
     * @return Works element with the WorkSummary elements grouped                  
     * */
    Works groupWorks(List<WorkSummary> works, boolean justPublic);
}
