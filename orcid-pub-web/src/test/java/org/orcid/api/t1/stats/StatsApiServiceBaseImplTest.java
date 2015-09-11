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
package org.orcid.api.t1.stats;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.orcid.api.t1.stats.delegator.StatsApiServiceDelegator;
import org.orcid.core.manager.StatisticsManager;
import org.orcid.core.utils.statistics.StatisticsEnum;
import org.orcid.jaxb.model.statistics.StatisticsSummary;
import org.orcid.jaxb.model.statistics.StatisticsTimeline;
import org.orcid.persistence.dao.StatisticsDao;
import org.orcid.persistence.jpa.entities.StatisticKeyEntity;
import org.orcid.persistence.jpa.entities.StatisticValuesEntity;
import org.orcid.test.OrcidJUnit4ClassRunner;
import org.orcid.test.TargetProxyHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;

@RunWith(OrcidJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:orcid-t1-web-context.xml", "classpath:orcid-core-context.xml", "classpath:orcid-t1-security-context.xml" })
@SuppressWarnings("deprecation")
public class StatsApiServiceBaseImplTest {

    @Resource(name = "statsApiServiceDelegator")
    StatsApiServiceDelegator serviceDelegator;

    // the class that contains the thing we're mocking
    @Resource(name = "statisticsManager")
    StatisticsManager statsManager;

    StatisticsDao statisticsDao = mock(StatisticsDao.class);

    @Before
    public void init() {
        // create our mock data
        List<StatisticValuesEntity> statsTimelineValues = new ArrayList<StatisticValuesEntity>();
        List<StatisticValuesEntity> statsSummaryValues = new ArrayList<StatisticValuesEntity>();

        StatisticValuesEntity a = new StatisticValuesEntity();
        a.setId(1l);
        a.setStatisticName(StatisticsEnum.KEY_LIVE_IDS.value());
        a.setStatisticValue(100l);
        StatisticKeyEntity akey = new StatisticKeyEntity();
        akey.setGenerationDate(new Date(2000, 1, 1));
        akey.setId(200l);
        a.setKey(akey);

        StatisticValuesEntity b = new StatisticValuesEntity();
        b.setId(1l);
        b.setStatisticName(StatisticsEnum.KEY_LIVE_IDS.value());
        b.setStatisticValue(101l);
        StatisticKeyEntity bkey = new StatisticKeyEntity();
        bkey.setGenerationDate(new Date(1999, 1, 1));
        bkey.setId(201l);
        b.setKey(bkey);

        StatisticValuesEntity c = new StatisticValuesEntity();
        c.setId(1l);
        c.setStatisticName(StatisticsEnum.KEY_NUMBER_OF_WORKS.value());
        c.setStatisticValue(102l);
        c.setKey(akey);

        statsTimelineValues.add(a);
        statsTimelineValues.add(b);
        statsSummaryValues.add(a);
        statsSummaryValues.add(c);

        // mock the methods used
        when(statisticsDao.getLatestKey()).thenReturn(akey);
        when(statisticsDao.getStatistic(StatisticsEnum.KEY_LIVE_IDS.value())).thenReturn(statsTimelineValues);
        when(statisticsDao.getStatistic(200l)).thenReturn(statsSummaryValues);

        //statsManager.setStatisticsDao(statisticsDao);
        TargetProxyHelper.injectIntoProxy(statsManager, "statisticsDao", statisticsDao);
        
        // setup security context
        ArrayList<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        roles.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
        Authentication auth = new AnonymousAuthenticationToken("anonymous", "anonymous", roles);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    public void testViewStatsSummary() {
        Assert.assertEquals(serviceDelegator.getStatsSummary().getStatus(), 200);
        StatisticsSummary s = (StatisticsSummary) serviceDelegator.getStatsSummary().getEntity();
        Assert.assertEquals(s.getDate(), new Date(2000, 1, 1));
        Assert.assertEquals(s.getStatistics().size(), 2);
        Assert.assertEquals((long) s.getStatistics().get(StatisticsEnum.KEY_LIVE_IDS.value()), 100l);
        Assert.assertEquals((long) s.getStatistics().get(StatisticsEnum.KEY_NUMBER_OF_WORKS.value()), 102l);

    }

    @Test
    public void testViewStatsTimeline() {
        Assert.assertEquals(serviceDelegator.getStatsSummary().getStatus(), 200);
        StatisticsTimeline s = (StatisticsTimeline) serviceDelegator.getStatsTimeline(StatisticsEnum.KEY_LIVE_IDS).getEntity();
        Assert.assertEquals(s.getStatisticName(), StatisticsEnum.KEY_LIVE_IDS.value());
        Assert.assertEquals(s.getTimeline().size(), 2);
        Assert.assertEquals((long) s.getTimeline().get(new Date(1999, 1, 1)), 101l);
        Assert.assertEquals((long) s.getTimeline().get(new Date(2000, 1, 1)), 100l);
    }
    
    @Test
    public void testEnumAndToStringListMatch(){
        StatisticsEnum[] it = StatisticsEnum.values();
        String list = it[0].value();
        for (int i=1;i<it.length;i++){
            list += ","+it[i].value();
        }
        Assert.assertEquals(StatisticsEnum.allowableSwaggerValues, list);
    }
    
}
