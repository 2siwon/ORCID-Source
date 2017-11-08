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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class BibcodeNormalizerTest {

    BibcodeNormalizer norm = new BibcodeNormalizer();
    List<String> tests = Lists.newArrayList(
            "123456789.A23456789",
            "123456789.A23456789 ",
            " 123456789.A23456789",
            " 123456789.A23456789 ",
            " 123456789.A2345678 ",
            " 123456789. A23456789 ",
            "ABCD56789.A23456789"
            );
    
    List<String> results = Lists.newArrayList(
            "123456789.A23456789",
            "123456789.A23456789",
            "123456789.A23456789",
            "123456789.A23456789",
            "",
            "",
            ""
            );
    
    @Test
    public void go(){
        for (int i=0;i<tests.size();i++){
            assertEquals(results.get(i),norm.normalise("bibcode", tests.get(i)));
        }
    }
}
