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
package org.orcid.core.manager.read_only.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.orcid.core.adapter.JpaJaxbAddressAdapter;
import org.orcid.core.manager.read_only.AddressManagerReadOnly;
import org.orcid.core.version.impl.Api2_0_rc4_LastModifiedDatesHelper;
import org.orcid.jaxb.model.common_rc4.Visibility;
import org.orcid.jaxb.model.record_rc4.Address;
import org.orcid.jaxb.model.record_rc4.Addresses;
import org.orcid.persistence.dao.AddressDao;
import org.orcid.persistence.jpa.entities.AddressEntity;
import org.springframework.cache.annotation.Cacheable;

public class AddressManagerReadOnlyImpl extends ManagerReadOnlyBaseImpl implements AddressManagerReadOnly {
    
    @Resource
    protected JpaJaxbAddressAdapter adapter;
    
    protected AddressDao addressDao;
    
    public void setAddressDao(AddressDao addressDao) {
        this.addressDao = addressDao;
    }

    @Override
    @Cacheable(value = "primary-address", key = "#orcid.concat('-').concat(#lastModified)")
    public Address getPrimaryAddress(String orcid, long lastModified) {        
        List<AddressEntity> addresses = addressDao.getAddresses(orcid, getLastModified(orcid));
        Address address = null;
        if(addresses != null) {
            //Look for the address with the largest display index
            for(AddressEntity entity : addresses) {
                if(address == null || address.getDisplayIndex() < entity.getDisplayIndex()) {
                    address = adapter.toAddress(entity);                    
                } 
            }
        }                    
        return address;
    }

    @Override
    public Address getAddress(String orcid, Long putCode) {
        AddressEntity entity = addressDao.getAddress(orcid, putCode);
        return adapter.toAddress(entity);
    }
    
    @Override
    @Cacheable(value = "address", key = "#orcid.concat('-').concat(#lastModified)")
    public Addresses getAddresses(String orcid, long lastModified) {
        return getAddresses(orcid, null);        
    }

    @Override
    @Cacheable(value = "public-address", key = "#orcid.concat('-').concat(#lastModified)")
    public Addresses getPublicAddresses(String orcid, long lastModified) {
        return getAddresses(orcid, Visibility.PUBLIC);
    }
    
    private Addresses getAddresses(String orcid, Visibility visibility) {
        List<AddressEntity> addresses = new ArrayList<AddressEntity>();
        
        if (visibility == null) {
            addresses = addressDao.getAddresses(orcid, getLastModified(orcid));
        } else {
            addresses = addressDao.getAddresses(orcid, visibility);
        }           
        
        Addresses result = adapter.toAddressList(addresses);
        Api2_0_rc4_LastModifiedDatesHelper.calculateLatest(result);
        
        return result;
    }    
}
