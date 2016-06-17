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
package org.orcid.core.manager.impl;

import java.util.Date;

import javax.annotation.Resource;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.orcid.core.manager.ClientDetailsEntityCacheManager;
import org.orcid.core.manager.ClientDetailsManager;
import org.orcid.persistence.jpa.entities.ClientDetailsEntity;
import org.orcid.utils.ReleaseNameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientDetailsEntityCacheManagerImpl implements ClientDetailsEntityCacheManager {

    private static final Logger LOG = LoggerFactory.getLogger(ClientDetailsEntityCacheManagerImpl.class);
    
    @Resource
    private ClientDetailsManager clientDetailsManager;
    
    @Resource(name = "clientDetailsEntityCache")
    private Cache clientDetailsCache;        
    
    private String releaseName = ReleaseNameUtils.getReleaseName();
    
    LockerObjectsManager lockers = new LockerObjectsManager();
    
    @Override
    public ClientDetailsEntity retrieve(String clientId) throws IllegalArgumentException {
        Object key = new ClientIdCacheKey(clientId, releaseName);
        Date dbDate = retrieveLastModifiedDate(clientId);
        ClientDetailsEntity clientDetails = toClientDetailsEntity(clientDetailsCache.get(key));
        if (needsFresh(dbDate, clientDetails)) {
            try {
                synchronized (lockers.obtainLock(clientId)) {
                    ///---------------------------------------------------------> is this ok? should we search by key or by client id?
                    clientDetails = toClientDetailsEntity(clientDetailsCache.get(clientId));
                    if (needsFresh(dbDate, clientDetails)) {
                        clientDetails = clientDetailsManager.findByClientId(clientId);
                        if(clientDetails == null)
                            throw new IllegalArgumentException("Invalid client id " + clientId);
                        clientDetailsCache.put(new Element(key, clientDetails));
                    }
                }
            } finally {
                lockers.releaseLock(clientId);
            }
        }
        return clientDetails;
    }
    
    @Override
    public ClientDetailsEntity retrieveByIdP(String idp) throws IllegalArgumentException {
        Object key = new ClientIdCacheKey("IdP+" + idp, releaseName);
        Date dbDate = retrieveLastModifiedDateByIdP(idp);
        ClientDetailsEntity clientDetails = toClientDetailsEntity(clientDetailsCache.get(key));
        if (needsFresh(dbDate, clientDetails)) {
            try {
                synchronized (lockers.obtainLock(idp)) {
                    ///---------------------------------------------------------> is this ok? should we search by key or by client id?
                    clientDetails = toClientDetailsEntity(clientDetailsCache.get(clientId));
                    if (needsFresh(dbDate, clientDetails)) {
                        clientDetails = clientDetailsManager.findByClientId(clientId);
                        if(clientDetails == null)
                            throw new IllegalArgumentException("Invalid client id " + clientId);
                        clientDetailsCache.put(new Element(key, clientDetails));
                    }
                }
            } finally {
                lockers.releaseLock(clientId);
            }
        }
        return clientDetails;
    }

    @Override
    public void put(ClientDetailsEntity clientDetailsEntity) {
        put(clientDetailsEntity.getId(), clientDetailsEntity);
        
    }

    public void put(String clientId, ClientDetailsEntity client) {
        try {
            synchronized (lockers.obtainLock(clientId)) {
                clientDetailsCache.put(new Element(new ClientIdCacheKey(clientId, releaseName), client));
            }
        } finally {
            lockers.releaseLock(clientId);
        }
    }
    
    @Override
    public void removeAll() {
        clientDetailsCache.removeAll();
        
    }

    @Override
    public void remove(String clientId) {
        clientDetailsCache.remove(new ClientIdCacheKey(clientId, releaseName));
    }    
    
    private Date retrieveLastModifiedDate(String clientId) {
        Date date = null;
        try {
            date = clientDetailsManager.getLastModified(clientId);
        } catch (javax.persistence.NoResultException e) {
             LOG.debug("Missing lastModifiedDate clientId:" + clientId);   
        }
        return date;
    }
    
    private Date retrieveLastModifiedDateByIdP(String idp) {
        Date date = null;
        try {
            date = clientDetailsManager.getLastModifiedByIdp(idp);
        } catch (javax.persistence.NoResultException e) {
             LOG.debug("Missing lastModifiedDate idp:" + idp);   
        }
        return date;
    }
    
    static public ClientDetailsEntity toClientDetailsEntity(Element element) {
        return (ClientDetailsEntity) (element != null ? element.getObjectValue() : null);
    }
    
    static public boolean needsFresh(Date dbDate, ClientDetailsEntity clientDetailsEntity) {
        if (clientDetailsEntity == null)
            return true;
        if (dbDate == null) // not sure when this happens?
            return true;
        if (clientDetailsEntity.getLastModified().getTime() != dbDate.getTime())
            return true;
        return false;
    }
}
