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
package org.orcid.jaxb.model.statistics;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a timeline of statistics.
 * 
 * @author tom
 *
 */
@XmlRootElement(name = "statistics-timeline")
@XmlAccessorType(XmlAccessType.FIELD)
public class StatisticsTimeline {
    @XmlElement(name = "statistic-name")
    protected String statisticName;
    protected Map<Date, Long> timeline;

    public String getStatisticName() {
        return statisticName;
    }

    public void setStatisticName(String name) {
        this.statisticName = name;
    }

    public Map<Date, Long> getTimeline() {
        return timeline;
    }

    public void setTimeline(Map<Date, Long> timeline) {
        this.timeline = timeline;
    }
}
