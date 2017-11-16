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
<#escape x as x?html>

<div class="id-banner <#if inDelegationMode>delegation-mode</#if>"> 
    
    <#if inDelegationMode><span class="delegation-mode-warning">${springMacroRequestContext.getMessage("delegate.managing_record")}</span></#if>
    
    <!-- Name -->
    
    <name-ng2 class="clearfix"></name-ng2>

    <div class="oid">
        <div class="id-banner-header">
            <span><@orcid.msg 'common.orcid_id' /></span>
        </div>
        <div class="orcid-id-container">
            <div class="orcid-id-options">
                <@orcid.checkFeatureStatus featureName='HTTPS_IDS'>
                    <div class="orcid-id-info">
                        <span class="mini-orcid-icon-16"></span>
                        <!-- Reference: orcid.js:removeProtocolString() -->
                        <span id="orcid-id" class="orcid-id-https">${baseUri}/${(effectiveUserOrcid)!}</span>
                    </div>
                    <a href="${baseUri}/${(effectiveUserOrcid)!}" class="gray-button" target="id_banner.viewpublicprofile"><@orcid.msg 'id_banner.viewpublicprofile'/></a>
                </@orcid.checkFeatureStatus>
                <@orcid.checkFeatureStatus featureName='HTTPS_IDS' enabled=false>
                    <div class="orcid-id-info">
                        <span class="mini-orcid-icon"></span>
                        <!-- Reference: orcid.js:removeProtocolString() -->
                        <span id="orcid-id" class="orcid-id shortURI">${baseDomainRmProtocall}/${(effectiveUserOrcid)!}</span>
                    </div>
                    <a href="${baseUriHttp}/${(effectiveUserOrcid)!}" class="gray-button" target="id_banner.viewpublicprofile"><@orcid.msg 'id_banner.viewpublicprofile'/></a>
                </@orcid.checkFeatureStatus>
            </div>
        </div>
    </div>
    <#if (locked)?? && !locked>
        <div ng-controller="SwitchUserCtrl" class="dropdown id-banner-container" ng-show="unfilteredLength" ng-cloak>
            <a ng-click="openMenu($event)" class="id-banner-switch"><@orcid.msg 'public-layout.manage_proxy_account'/><span class="glyphicon glyphicon-chevron-right"></span></a>
            <ul class="dropdown-menu id-banner-dropdown" ng-show="isDroppedDown" ng-cloak>
                <li>
                    <input id="delegators-search" type="text" ng-model="searchTerm" ng-change="search()" placeholder="<@orcid.msg 'manage_delegators.search.placeholder'/>"></input>
                </li>
                <li ng-show="me && !searchTerm">
                    <a href="<@orcid.rootPath '/switch-user?username='/>{{me.delegateSummary.orcidIdentifier.path}}">
                        <ul>
                            <li><@orcid.msg 'id_banner.switchbacktome'/></li>
                            <li>{{me.delegateSummary.orcidIdentifier.uri}}</li>
                        </ul>
                    </a>
                </li>
                <li ng-repeat="delegationDetails in delegators.delegationDetails | orderBy:'delegateSummary.creditName.content' | limitTo:10">
                    <a href="<@orcid.rootPath '/switch-user?username='/>{{delegationDetails.delegateSummary.orcidIdentifier.path}}">
                        <ul>
                            <li>{{delegationDetails.delegateSummary.creditName.content}}</li>
                            <li>{{delegationDetails.delegateSummary.orcidIdentifier.uri}}</li>
                        </ul>
                    </a>
                </li>
                <li><a href="<@orcid.rootPath '/delegators?delegates'/>"><@orcid.msg 'id_banner.more'/></a></li>
            </ul>
        </div>  
    </#if>
</div>
</#escape>
