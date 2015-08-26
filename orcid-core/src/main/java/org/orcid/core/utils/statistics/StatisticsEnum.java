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
package org.orcid.core.utils.statistics;

public enum StatisticsEnum {
    KEY_LIVE_IDS("liveIds"), 
    KEY_IDS_WITH_VERIFIED_EMAIL("idsWithVerifiedEmail"),
    KEY_IDS_WITH_WORKS("idsWithWorks"),
    KEY_NUMBER_OF_WORKS("works"),
    KEY_WORKS_WITH_DOIS("worksWithDois"),
    KEY_LOCKED_RECORDS("lockedRecords"),
    KEY_UNIQUE_DOIS("uniqueDois");
    
    /** For use as allowable values list for swagger
     * Annoyingly this can only be an inline static final if we want it to work
     * There is a unit test to check it correctly contains all values in declared order
     */
    public static final String allowableSwaggerValues = "liveIds,idsWithVerifiedEmail,idsWithWorks,works,worksWithDois,lockedRecords,uniqueDois";
    
    private final String value;

    StatisticsEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    //not sure if this is used silently, so not removed yet.
    @Deprecated
    public static StatisticsEnum fromValue(String v) {
        for (StatisticsEnum c : StatisticsEnum.values()) {
            if (c.value.equals(v.toLowerCase())) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
    
    /** Method called by JAX-RS when parsing path parameters
     * 
     * @param v the path param
     * @return the matching enum 
     */
    public static StatisticsEnum fromString(String v) {
        for (StatisticsEnum c : StatisticsEnum.values()) {
            if (c.value().equalsIgnoreCase(v.trim())) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }
        
}
