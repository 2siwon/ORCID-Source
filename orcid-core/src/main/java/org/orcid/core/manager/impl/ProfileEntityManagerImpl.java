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

import java.security.InvalidParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.orcid.core.adapter.Jpa2JaxbAdapter;
import org.orcid.core.locale.LocaleManager;
import org.orcid.core.manager.AddressManager;
import org.orcid.core.manager.AffiliationsManager;
import org.orcid.core.manager.BiographyManager;
import org.orcid.core.manager.ClientDetailsEntityCacheManager;
import org.orcid.core.manager.EmailManager;
import org.orcid.core.manager.EncryptionManager;
import org.orcid.core.manager.ExternalIdentifierManager;
import org.orcid.core.manager.NotificationManager;
import org.orcid.core.manager.OrcidProfileManager;
import org.orcid.core.manager.OtherNameManager;
import org.orcid.core.manager.PeerReviewManager;
import org.orcid.core.manager.PersonalDetailsManager;
import org.orcid.core.manager.ProfileEntityCacheManager;
import org.orcid.core.manager.ProfileEntityManager;
import org.orcid.core.manager.ProfileFundingManager;
import org.orcid.core.manager.ProfileKeywordManager;
import org.orcid.core.manager.RecordNameManager;
import org.orcid.core.manager.ResearcherUrlManager;
import org.orcid.core.manager.SourceManager;
import org.orcid.core.manager.WorkManager;
import org.orcid.core.manager.read_only.impl.ProfileEntityManagerReadOnlyImpl;
import org.orcid.core.oauth.OrcidOauth2TokenDetailService;
import org.orcid.core.security.visibility.OrcidVisibilityDefaults;
import org.orcid.jaxb.model.clientgroup.MemberType;
import org.orcid.jaxb.model.common_rc3.Visibility;
import org.orcid.jaxb.model.message.Locale;
import org.orcid.jaxb.model.message.OrcidProfile;
import org.orcid.jaxb.model.message.OrcidType;
import org.orcid.jaxb.model.message.ScopePathType;
import org.orcid.jaxb.model.notification.amended_rc3.AmendedSection;
import org.orcid.jaxb.model.record_rc3.Biography;
import org.orcid.persistence.dao.OrgAffiliationRelationDao;
import org.orcid.persistence.dao.UserConnectionDao;
import org.orcid.persistence.jpa.entities.AddressEntity;
import org.orcid.persistence.jpa.entities.BiographyEntity;
import org.orcid.persistence.jpa.entities.ClientDetailsEntity;
import org.orcid.persistence.jpa.entities.EmailEntity;
import org.orcid.persistence.jpa.entities.ExternalIdentifierEntity;
import org.orcid.persistence.jpa.entities.IndexingStatus;
import org.orcid.persistence.jpa.entities.OrcidOauth2TokenDetail;
import org.orcid.persistence.jpa.entities.OrgAffiliationRelationEntity;
import org.orcid.persistence.jpa.entities.OtherNameEntity;
import org.orcid.persistence.jpa.entities.ProfileEntity;
import org.orcid.persistence.jpa.entities.ProfileFundingEntity;
import org.orcid.persistence.jpa.entities.ProfileKeywordEntity;
import org.orcid.persistence.jpa.entities.RecordNameEntity;
import org.orcid.persistence.jpa.entities.ResearcherUrlEntity;
import org.orcid.pojo.ApplicationSummary;
import org.orcid.pojo.ajaxForm.Claim;
import org.orcid.pojo.ajaxForm.PojoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Declan Newman (declan) Date: 10/02/2012
 */
public class ProfileEntityManagerImpl extends ProfileEntityManagerReadOnlyImpl implements ProfileEntityManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileEntityManagerImpl.class);    

    @Resource
    private Jpa2JaxbAdapter jpa2JaxbAdapter;

    @Resource
    private AffiliationsManager affiliationsManager;

    @Resource
    private ProfileFundingManager fundingManager;

    @Resource
    private PeerReviewManager peerReviewManager;

    @Resource
    private ProfileEntityCacheManager profileEntityCacheManager;

    @Resource
    private WorkManager workManager;

    @Resource
    private EncryptionManager encryptionManager;

    @Resource
    private AddressManager addressManager;

    @Resource
    private ExternalIdentifierManager externalIdentifierManager;

    @Resource
    private ProfileKeywordManager profileKeywordManager;

    @Resource
    private PersonalDetailsManager personalDetailsManager;

    @Resource
    private OtherNameManager otherNameManager;

    @Resource
    private ResearcherUrlManager researcherUrlManager;
    
    @Resource
    private EmailManager emailManager;
    
    @Resource
    private OrgAffiliationRelationDao orgRelationAffiliationDao;    
    
    @Resource
    private OtherNameManager otherNamesManager;        
    
    @Resource
    private BiographyManager biographyManager;
    
    @Resource
    private UserConnectionDao userConnectionDao;
    
    @Resource
    private OrcidProfileManager orcidProfileManager;
    
    @Resource
    private NotificationManager notificationManager;
    
    @Resource
    private SourceManager sourceManager;
    
    @Resource
    private OrcidOauth2TokenDetailService orcidOauth2TokenService;
    
    @Resource
    private ClientDetailsEntityCacheManager clientDetailsEntityCacheManager;
    
    @Resource
    private OrcidUrlManager orcidUrlManager;
    
    @Resource
    private LocaleManager localeManager;
    
    @Resource
    private RecordNameManager recordNameManager;            
    
    @Override
    public boolean orcidExists(String orcid) {
        return profileDao.orcidExists(orcid);
    }

    @Override
    public boolean hasBeenGivenPermissionTo(String giverOrcid, String receiverOrcid) {
        return profileDao.hasBeenGivenPermissionTo(giverOrcid, receiverOrcid);
    }

    @Override
    public boolean existsAndNotClaimedAndBelongsTo(String messageOrcid, String clientId) {
        return profileDao.existsAndNotClaimedAndBelongsTo(messageOrcid, clientId);
    }    

    @Override
    public String findByCreditName(String creditName) {
        RecordNameEntity recordName = recordNameManager.findByCreditName(creditName);
        if(recordName == null) {
            return null;
        }
        return recordName.getProfile().getId();
    }
    
    /**
     * Deprecates a profile
     * 
     * @param deprecatedProfile
     *            The profile that want to be deprecated
     * @param primaryProfile
     *            The primary profile for the deprecated profile
     * @return true if the account was successfully deprecated, false otherwise
     */
    @Override
    @Transactional 
    public boolean deprecateProfile(ProfileEntity deprecatedProfile, ProfileEntity primaryProfile) {
        deprecatedProfile.setActivitiesVisibilityDefault(org.orcid.jaxb.model.message.Visibility.PRIVATE); 
        boolean wasDeprecated = profileDao.deprecateProfile(deprecatedProfile, primaryProfile.getId());        
        // If it was successfully deprecated
        if (wasDeprecated) {
            LOGGER.info("Account {} was deprecated to primary account: {}", deprecatedProfile.getId(), primaryProfile.getId());
            ProfileEntity deprecated = profileDao.find(deprecatedProfile.getId());
            
            String deprecatedOrcid = deprecatedProfile.getId();
            String primaryOrcid = primaryProfile.getId();
            
            // Remove works
            workManager.removeAllWorks(deprecatedOrcid);
            
            // Remove funding
            if (deprecated.getProfileFunding() != null) {
                for(ProfileFundingEntity funding : deprecated.getProfileFunding()) {
                    fundingManager.removeProfileFunding(funding.getProfile().getId(), funding.getId());
                }
            }
            
            // Remove affiliations
            if (deprecated.getOrgAffiliationRelations() != null) {
                for(OrgAffiliationRelationEntity affiliation : deprecated.getOrgAffiliationRelations()) {                    
                    orgRelationAffiliationDao.removeOrgAffiliationRelation(affiliation.getProfile().getId(), affiliation.getId());
                }
            }
            
            // Remove external identifiers
            if (deprecated.getExternalIdentifiers() != null) {
                for (ExternalIdentifierEntity externalIdentifier : deprecated.getExternalIdentifiers()) {
                    externalIdentifierManager.deleteExternalIdentifier(deprecated.getId(), externalIdentifier.getId(), false);
                }
            }

            // Remove researcher urls
            if(deprecated.getResearcherUrls() != null) {
                for(ResearcherUrlEntity rUrl : deprecated.getResearcherUrls()) {
                    researcherUrlManager.deleteResearcherUrl(deprecatedOrcid, rUrl.getId(), false);
                }
            }
            
            // Remove other names
            if(deprecated.getOtherNames() != null) {
                for(OtherNameEntity otherName : deprecated.getOtherNames()) {
                    otherNamesManager.deleteOtherName(deprecatedOrcid, otherName.getId(), false);
                }                            
            }
            
            // Remove keywords
            if(deprecated.getKeywords() != null) {
                for(ProfileKeywordEntity keyword : deprecated.getKeywords()) {
                    profileKeywordManager.deleteKeyword(deprecatedOrcid, keyword.getId(), false);
                }                                                        
            }
            
            //Remove biography
            Biography deprecatedBio = new Biography();
            deprecatedBio.setContent(null);
            deprecatedBio.setVisibility(Visibility.PRIVATE);
            
            BiographyEntity bioEntity = deprecated.getBiographyEntity();
            if(bioEntity != null) {
                biographyManager.updateBiography(deprecatedOrcid,deprecatedBio);
            } else {
                biographyManager.createBiography(deprecatedOrcid, deprecatedBio);    
            }
            
            
            //Set the deactivated names
            RecordNameEntity recordName = deprecated.getRecordNameEntity();
            if(recordName != null) {
                recordName.setCreditName(null);
                recordName.setGivenNames("Given Names Deactivated");
                recordName.setFamilyName("Family Name Deactivated");
                recordName.setVisibility(org.orcid.jaxb.model.common_rc3.Visibility.PRIVATE);
                recordName.setProfile(new ProfileEntity(deprecatedOrcid));
                recordNameManager.updateRecordName(recordName);                
            } 
                                                        
            // Move all emails to the primary email
            Set<EmailEntity> deprecatedAccountEmails = deprecated.getEmails();
            if (deprecatedAccountEmails != null) {
                // For each email in the deprecated profile                            
                for (EmailEntity email : deprecatedAccountEmails) {
                    // Delete each email from the deprecated
                    // profile
                    LOGGER.info("About to move email {} from profile {} to profile {}", new Object[] {email.getId(), deprecatedOrcid, primaryOrcid });
                    emailManager.moveEmailToOtherAccount(email.getId(), deprecatedOrcid, primaryOrcid);
                }
            }
            
            return true;
        }
        
        return false; 
    }

    /**
     * Enable developer tools
     * 
     * @param profile
     *            The profile to update
     * @return true if the developer tools where enabled on that profile
     */
    @Override
    public boolean enableDeveloperTools(OrcidProfile profile) {
        boolean result = profileDao.updateDeveloperTools(profile.getOrcidIdentifier().getPath(), true);
        return result;
    }

    /**
     * Disable developer tools
     * 
     * @param profile
     *            The profile to update
     * @return true if the developer tools where disabeled on that profile
     */
    @Override
    public boolean disableDeveloperTools(OrcidProfile profile) {
        boolean result = profileDao.updateDeveloperTools(profile.getOrcidIdentifier().getPath(), false);
        return result;
    }

    @Override
    public boolean isProfileClaimed(String orcid) {
        return profileDao.getClaimedStatus(orcid);
    }    

    /**
     * Get the group type of a profile
     * 
     * @param orcid
     *            The profile to look for
     * @return the group type, null if it is not a client
     */
    @Override
    public MemberType getGroupType(String orcid) {
        return profileDao.getGroupType(orcid);
    }    

    /** Updates the DB and the cached value in the request scope.
     * 
     */
    @Override
    public void updateLastModifed(String orcid) {
        profileLastModifiedAspect.updateLastModifiedDateAndIndexingStatus(orcid);
    }

    @Override
    public boolean isDeactivated(String orcid) {
        return profileDao.isDeactivated(orcid);
    }

    @Override
    public boolean reviewProfile(String orcid) {
        return profileDao.reviewProfile(orcid);
    }

    @Override
    public boolean unreviewProfile(String orcid) {
        return profileDao.unreviewProfile(orcid);
    }
   
    @Override
    public void disableApplication(Long tokenId, String userOrcid) {
        orcidOauth2TokenService.disableAccessToken(tokenId, userOrcid);
    }    
    
    @Override
    public List<ApplicationSummary> getApplications(String orcid) {
        List<OrcidOauth2TokenDetail> tokenDetails = orcidOauth2TokenService.findByUserName(orcid);
        List<ApplicationSummary> applications = new ArrayList<ApplicationSummary>();
        Map<Pair<String, Set<ScopePathType>>, ApplicationSummary> existingApplications = new HashMap<Pair<String, Set<ScopePathType>>, ApplicationSummary>();
        if(tokenDetails != null && !tokenDetails.isEmpty()) {
            for(OrcidOauth2TokenDetail token : tokenDetails) {
                if (token.getTokenDisabled() == null || !token.getTokenDisabled()) {
                    ClientDetailsEntity client = clientDetailsEntityCacheManager.retrieve(token.getClientDetailsId());
                    if(client != null) {
                        ApplicationSummary applicationSummary = new ApplicationSummary();
                        // Check the scopes
                        Set<ScopePathType> scopesGrantedToClient = ScopePathType.getScopesFromSpaceSeparatedString(token.getScope());
                        Map<ScopePathType, String> scopePathMap = new HashMap<ScopePathType, String>();
                        String scopeFullPath = ScopePathType.class.getName() + ".";
                        for (ScopePathType tempScope : scopesGrantedToClient) {                            
                            scopePathMap.put(tempScope, localeManager.resolveMessage(scopeFullPath + tempScope.toString()));
                        }
                        //If there is at least one scope in this token, fill the application summary element
                        if(!scopePathMap.isEmpty()) {
                            applicationSummary.setScopePaths(scopePathMap);
                            applicationSummary.setOrcidHost(orcidUrlManager.getBaseHost());                        
                            applicationSummary.setOrcidUri(orcidUrlManager.getBaseUriHttp() + "/" + client.getId());
                            applicationSummary.setOrcidPath(client.getId());
                            applicationSummary.setName(client.getClientName());
                            applicationSummary.setWebsiteValue(client.getClientWebsite());
                            applicationSummary.setApprovalDate(token.getDateCreated());
                            applicationSummary.setTokenId(String.valueOf(token.getId()));
                            // Add member information
                            if (!PojoUtil.isEmpty(client.getGroupProfileId())) {
                                ProfileEntity member = profileEntityCacheManager.retrieve(client.getGroupProfileId());
                                applicationSummary.setGroupOrcidPath(member.getId());
                                applicationSummary.setGroupName(getMemberDisplayName(member));
                            }
                            
                            if(shouldBeAddedToTheApplicationsList(applicationSummary, scopesGrantedToClient, existingApplications)) {
                                applications.add(applicationSummary);
                            }
                        }
                    }
                }
            }
        }        
        
        return applications;
    }
    
    private boolean shouldBeAddedToTheApplicationsList(ApplicationSummary application , Set<ScopePathType> scopes, Map<Pair<String, Set<ScopePathType>>, ApplicationSummary> existingApplications) {
        boolean result = false;
        Pair<String, Set<ScopePathType>> key = Pair.of(application.getOrcidPath(), scopes);
        if(!existingApplications.containsKey(key)) {
            result = true;
        } else {
            Date existingAppCreatedDate = existingApplications.get(key).getApprovalDate();
            
            //This case should never happen
            if(existingAppCreatedDate == null) {
                result = true;
            }
            
            if(application.getApprovalDate().before(existingAppCreatedDate)) {
                result = true;
            }
        }
         
        if(result) {
            existingApplications.put(key, application);
        }                                        
        return result;
    }
                    
    private String getMemberDisplayName(ProfileEntity member) {   
        RecordNameEntity recordName = member.getRecordNameEntity(); 
        if(recordName == null) {
            return StringUtils.EMPTY;
        }
        
        //If it is a member, return the credit name
        if(OrcidType.GROUP.equals(member.getOrcidType())) {
            return recordName.getCreditName();                    
        }
        
        Visibility namesVisibilty = recordName.getVisibility();   
        if(Visibility.PUBLIC.equals(namesVisibilty)) {
            if(!PojoUtil.isEmpty(recordName.getCreditName())) {
                return recordName.getCreditName();
            } else {
                String displayName = recordName.getGivenNames();
                String familyName = recordName.getFamilyName();
                if (StringUtils.isNotBlank(familyName)) {
                    displayName += " " + familyName;
                }                
                return displayName;
            }
        }
        
        return StringUtils.EMPTY;
    }   

    @Override
    public String getOrcidHash(String orcid) throws NoSuchAlgorithmException {
        if (PojoUtil.isEmpty(orcid)) {
            return null;
        }
        return encryptionManager.sha256Hash(orcid);
    }

    @Override
    public String retrivePublicDisplayName(String orcid) {
        String publicName = "";
        ProfileEntity profile = profileEntityCacheManager.retrieve(orcid);
        if (profile != null) {
            RecordNameEntity recordName = profile.getRecordNameEntity();
            if(recordName != null) {
                Visibility namesVisibility = (recordName.getVisibility() != null) ? Visibility.fromValue(recordName.getVisibility().value())
                        : Visibility.fromValue(OrcidVisibilityDefaults.NAMES_DEFAULT.getVisibility().value());
                if (Visibility.PUBLIC.equals(namesVisibility)) {
                    if (!PojoUtil.isEmpty(recordName.getCreditName())) {
                        publicName = recordName.getCreditName();
                    } else {
                        publicName = PojoUtil.isEmpty(recordName.getGivenNames()) ? "" : recordName.getGivenNames();
                        publicName += PojoUtil.isEmpty(recordName.getFamilyName()) ? "" : " " + recordName.getFamilyName();
                    }
                }
            } 
        }
        return publicName;
    }

    @Override
    @Transactional
    public boolean claimProfileAndUpdatePreferences(String orcid, String email, Locale locale, Claim claim) {
        //Verify the email
        boolean emailVerified = emailManager.verifySetCurrentAndPrimary(orcid, email);
        if(!emailVerified) {
            throw new InvalidParameterException("Unable to claim and verify email: " + email + " for user: " + orcid);
        }
        
        //Update the profile entity fields
        ProfileEntity profile = profileDao.find(orcid);
        profile.setLastModified(new Date());
        profile.setIndexingStatus(IndexingStatus.REINDEX);
        profile.setClaimed(true);
        profile.setCompletedDate(new Date());
        profile.setLocale(locale);
        if(claim != null) {
            profile.setSendChangeNotifications(claim.getSendChangeNotifications().getValue());
            profile.setSendOrcidNews(claim.getSendOrcidNews().getValue());
            profile.setActivitiesVisibilityDefault(claim.getActivitiesVisibilityDefault().getVisibility());
        }
                
        //Update the visibility for every bio element to the visibility selected by the user
        //Update the bio
        org.orcid.jaxb.model.common_rc3.Visibility defaultVisibility = org.orcid.jaxb.model.common_rc3.Visibility.fromValue(claim.getActivitiesVisibilityDefault().getVisibility().value());
        if(profile.getBiographyEntity() != null) {
            profile.getBiographyEntity().setVisibility(defaultVisibility);
        }
        //Update address
        if(profile.getAddresses() != null) {
            for(AddressEntity a : profile.getAddresses()) {
                a.setVisibility(defaultVisibility);
            }
        }
        
        //Update the keywords
        if(profile.getKeywords() != null) {
            for(ProfileKeywordEntity k : profile.getKeywords()) {
                k.setVisibility(defaultVisibility);
            }
        }
        
        //Update the other names
        if(profile.getOtherNames() != null) {
            for(OtherNameEntity o : profile.getOtherNames()) {
                o.setVisibility(defaultVisibility);
            }
        }
        
        //Update the researcher urls
        if(profile.getResearcherUrls() != null) {
            for(ResearcherUrlEntity r : profile.getResearcherUrls()) {
                r.setVisibility(defaultVisibility);
            }
        }
        
        //Update the external identifiers
        if(profile.getExternalIdentifiers() != null) {
            for(ExternalIdentifierEntity e : profile.getExternalIdentifiers()) {
                e.setVisibility(defaultVisibility);
            }
        }
        profileDao.merge(profile);
        profileDao.flush();
        return true;
    }

    @Override
    @Transactional
    public boolean deactivateRecord(String orcid) {
        //Clear the record
        ProfileEntity toClear = profileDao.find(orcid);
        toClear.setLastModified(new Date());
        toClear.setDeactivationDate(new Date());
        toClear.setActivitiesVisibilityDefault(org.orcid.jaxb.model.message.Visibility.PRIVATE);
        toClear.setIndexingStatus(IndexingStatus.REINDEX);
        
        // Remove works
        workManager.removeAllWorks(orcid);
        
        // Remove funding
        if (toClear.getProfileFunding() != null) {
            toClear.getProfileFunding().clear();
        }
        
        // Remove affiliations
        if (toClear.getOrgAffiliationRelations() != null) {
            toClear.getOrgAffiliationRelations().clear();
        }
        
        // Remove external identifiers
        if (toClear.getExternalIdentifiers() != null) {
            toClear.getExternalIdentifiers().clear();
        }

        // Remove researcher urls
        if(toClear.getResearcherUrls() != null) {
            toClear.getResearcherUrls().clear();
        }
        
        // Remove other names
        if(toClear.getOtherNames() != null) {
            toClear.getOtherNames().clear();      
        }
        
        // Remove keywords
        if(toClear.getKeywords() != null) {
            toClear.getKeywords().clear();                                                        
        }        
        
        // Remove address
        if(toClear.getAddresses() != null) {
            toClear.getAddresses().clear();
        }
        
        BiographyEntity bioEntity = toClear.getBiographyEntity();
        if(bioEntity != null) {
            bioEntity.setBiography("");
            bioEntity.setVisibility(Visibility.PRIVATE);
        } 
                
        //Set the deactivated names
        RecordNameEntity recordName = toClear.getRecordNameEntity();
        if(recordName != null) {
            recordName.setCreditName(null);
            recordName.setGivenNames("Given Names Deactivated");
            recordName.setFamilyName("Family Name Deactivated");
            recordName.setVisibility(org.orcid.jaxb.model.common_rc3.Visibility.PUBLIC);                                      
        }
        
        Set<EmailEntity> emails = toClear.getEmails();
        if (emails != null) {
            // For each email in the deprecated profile                            
            for (EmailEntity email : emails) {
                email.setVisibility(org.orcid.jaxb.model.message.Visibility.PRIVATE);
            }        
        }
        
        profileDao.merge(toClear);
        profileDao.flush();
        
        //Delete all connections
        userConnectionDao.deleteByOrcid(orcid);                
        
        OrcidProfile deactivated = orcidProfileManager.retrieveOrcidProfile(orcid);
        
        notificationManager.sendAmendEmail(deactivated, AmendedSection.UNKNOWN);
        return false;
    }

    @Override
    public void updateLocale(String orcid, Locale locale) {
        profileDao.updateLocale(orcid, locale);
    }    
}