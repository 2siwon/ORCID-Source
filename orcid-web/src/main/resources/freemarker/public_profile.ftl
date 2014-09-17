<#--

    =============================================================================

    ORCID (R) Open Source
    http://orcid.org

    Copyright (c) 2012-2014 ORCID, Inc.
    Licensed under an MIT-Style License (MIT)
    http://orcid.org/open-source-license

    This copyright and license information (including a link to the full license)
    shall be included in its entirety in all copies or substantial portion of
    the software.

    =============================================================================

-->
<#-- @ftlvariable name="profile" type="org.orcid.jaxb.model.message.OrcidProfile" -->
<@public >
<#escape x as x?html>
<div class="row workspace-top public-profile">
    <div class="col-md-3 left-aside">
        <div class="workspace-left workspace-profile">
            <h2 class="full-name">
                <#if (profile.orcidBio.personalDetails.creditName.content)??>
                    ${(profile.orcidBio.personalDetails.creditName.content)!}
                <#else>
                    ${(profile.orcidBio.personalDetails.givenNames.content)!} ${(profile.orcidBio.personalDetails.familyName.content)!}
                </#if>
            </h2>
            <div class="oid">
            	<p class="orcid-id-container">		
	            	<span class="mini-orcid-icon"></span>
	            	<a href="${baseUriHttp}/${(profile.orcidIdentifier.path)!}" id="orcid-id" class="orcid-id" title="Click for public view of ORCID iD">${baseUriHttp}/${(profile.orcidIdentifier.path)!}</a>
            	<p>
            </div>            
            <#if (profile.orcidBio.personalDetails.otherNames)?? && (profile.orcidBio.personalDetails.otherNames.otherName?size != 0)>
                <p><strong>${springMacroRequestContext.getMessage("public_profile.labelAlsoknownas")}</strong><br />
                  <#list profile.orcidBio.personalDetails.otherNames.otherName as otherName>
                    ${otherName.content}<#if otherName_has_next><br /></#if>
                  </#list>
            </#if>
            <#if (countryName)??>
                <p><strong><@orcid.msg 'public_profile.labelCountry'/></strong>
                ${(countryName)!}
                </p>
            </#if>
            <#if (profile.orcidBio.keywords)?? && (profile.orcidBio.keywords.keyword?size != 0)>
                <p><strong>${springMacroRequestContext.getMessage("public_profile.labelKeywords")}</strong> 
                    <#list profile.orcidBio.keywords.keyword as keyword>
                        ${keyword.content}<#if keyword_has_next>,</#if>
                    </#list></p>
            </#if>
            <#if (profile.orcidBio.researcherUrls)?? && (profile.orcidBio.researcherUrls.researcherUrl?size != 0)>
                <p><strong>${springMacroRequestContext.getMessage("public_profile.labelWebsites")}</strong><br/>
                    <#list profile.orcidBio.researcherUrls.researcherUrl as url>
                        <a href="<@orcid.absUrl url.url/>" target="_blank" rel="nofollow"><#if (url.urlName.content)! != "">${url.urlName.content}<#else>${url.url.value}</#if></a><#if url_has_next><br/></#if>
                    </#list></p>
            </#if>
            <#if (profile.orcidBio.externalIdentifiers)?? && (profile.orcidBio.externalIdentifiers.externalIdentifier?size != 0)>
                <p><strong>${springMacroRequestContext.getMessage("public_profile.labelOtherIDs")}</strong> <br/>
                    <#list profile.orcidBio.externalIdentifiers.externalIdentifier as external>
                        <#if (external.externalIdUrl.value)??>
                            <a href="${external.externalIdUrl.value}" target="_blank">${(external.externalIdCommonName.content)!}: ${(external.externalIdReference.content)!}</a><#if external_has_next><br/></#if>
                        <#else>
                            ${(external.externalIdCommonName.content)!}: ${(external.externalIdReference.content)!}<#if external_has_next><br/></#if>
                        </#if>    
                    </#list>
                </p>
            </#if>
        </div>
    </div>
    <div class="col-md-9 right-aside">
        <div class="workspace-right" ng-controller="PersonalInfoCtrl">
        	<#if (deprecated)??>
	        	<div class="alert alert-error readme">
	        		<p><b><@orcid.msg 'public_profile.deprecated_account.1'/>&nbsp;<a href="${baseUriHttp}/${primaryRecord}">${baseUriHttp}/${primaryRecord}</a>&nbsp;<@orcid.msg 'public_profile.deprecated_account.2'/></b></p>
	        	</div>
        	</#if>
        	<div class="workspace-inner-public workspace-public">
        		<#if (isProfileEmpty)?? && isProfileEmpty>
        			<p class="margin-top-box"><b><@orcid.msg 'public_profile.empty_profile'/></b></p>
        		<#else>	            
	                <#if (profile.orcidBio.biography.content)?? && (profile.orcidBio.biography.content)?has_content>
		                <div class="workspace-accordion-header" style="position: relative;">
	        			   <ul class="personal-inf-display">        			   		
	        			   		<li>
	        			   			<a href="" ng-click="toggleDisplayInfo()" class="toggle-text">
		        						<i class="glyphicon-chevron-down glyphicon x0" ng-class="{'glyphicon-chevron-right':displayInfo==false}"></i>
		        					</a>		        			   		
	        			   			<a href="" ng-click="toggleDisplayInfo()" class="toggle-text">Personal Information</a></li>
	        			   		</li>	        			   		
	        			   </ul>
	        			</div>	        			
	        			<div class="workspace-accordion-content" ng-show="displayInfo">
	        				<p><b>${springMacroRequestContext.getMessage("public_profile.labelBiography")}</b><br /><div style="white-space: pre-wrap;">${(profile.orcidBio.biography.content)!}</div></p>
	        			</div>
	                </#if>
	                <#assign publicProfile = true />
	                <#include "workspace_preview_activities.ftl"/>	                    	
        		</#if>
        	</div>            
        </div>
    </div>
</div>
</#escape>
</@public>