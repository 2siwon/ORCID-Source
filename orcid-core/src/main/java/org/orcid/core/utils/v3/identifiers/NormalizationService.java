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
package org.orcid.core.utils.v3.identifiers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.orcid.core.manager.IdentifierTypeManager;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.stereotype.Component;

@Component
public class NormalizationService {

    @Resource
    List<Normalizer> normalizers = new ArrayList<Normalizer>();

    @Resource
    IdentifierTypeManager idman;

    Map<String, LinkedList<Normalizer>> map = new HashMap<String, LinkedList<Normalizer>>();

    @PostConstruct
    public void init() {
        Collections.sort(normalizers, AnnotationAwareOrderComparator.INSTANCE);
        for (String type : idman.fetchIdentifierTypesByAPITypeName(Locale.ENGLISH).keySet()) {
            map.put(type, new LinkedList<Normalizer>());
        }
        for (Normalizer n : normalizers) {
            List<String> supported = n.canHandle();
            if (supported.isEmpty()) {
                for (String type : map.keySet())
                    map.get(type).add(n);
            } else {
                for (String type : supported) {
                    map.get(type).add(n);
                }
            }
        }
    }

    /**
     * Ensure this is the API type name, not the DB type name
     * 
     * @param type
     * @param value
     * @return
     */
    public String normalise(String apiTypeName, String value) {
        if (apiTypeName == null)
            return value;
        String returnValue = value;
        for (Normalizer n : normalizers) {
            returnValue = n.normalise(apiTypeName, returnValue);
        }
        return returnValue;
    }

}
