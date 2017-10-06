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
<div class="row"> 
         
  	<!-- Information -->
	<div class="col-md-9 col-sm-9 col-xs-7">
	    <h3 class="workspace-title">	    	
        	<span ng-bind="group.getActive().affiliationName.value"></span>:
        	<span ng-bind="group.getActive().city.value"></span><span ng-if="group.getActive().region.value">, </span><span ng-bind="group.getActive().region.value"></span>, <span ng-bind="group.getActive().countryForDisplay"></span>        	        	        	        	
        </h3>
        <div class="info-detail">
        	<div class="info-date">        	        	
	        	<span class="affiliation-date" ng-if="group.getActive().startDate">
	        	    <span ng-if="group.getActive().startDate.year">{{group.getActive().startDate.year}}</span><span ng-if="group.getActive().startDate.month">-{{group.getActive().startDate.month}}</span><span ng-if="group.getActive().startDate.day">-{{group.getActive().startDate.day}}</span>
	        	    <span><@orcid.msg 'workspace_affiliations.dateSeparator'/></span>
	        	    <span ng-hide="group.getActive().endDate.year"><@orcid.msg 'workspace_affiliations.present'/></span>
	        		<span ng-if="group.getActive().endDate.year">{{group.getActive().endDate.year}}</span><span ng-if="group.getActive().endDate.month">-{{group.getActive().endDate.month}}</span><span ng-if="group.getActive().endDate.day">-{{group.getActive().endDate.day}}</span>
	            </span>
	            <span class="affiliation-date" ng-if="!group.getActive().startDate && group.getActive().endDate">
	        	     <span ng-if="group.getActive().endDate.year">{{group.getActive().endDate.year}}</span><span ng-if="group.getActive().endDate.month">-{{group.getActive().endDate.month}}</span><span ng-if="group.getActive().endDate.day">-{{group.getActive().endDate.day}}</span>
	        	</span>
                <span ng-if="(group.getActive().startDate || group.getActive().endDate) && (group.getActive().roleTitle.value || group.getActive().departmentName.value)"> | </span> <span ng-if="group.getActive().roleTitle.value" ng-bind="group.getActive().roleTitle.value"></span>        
                <span ng-if="group.getActive().departmentName.value">
                <span ng-if="group.getActive().roleTitle.value && !printView">&nbsp;</span>(<span ng-bind="group.getActive().departmentName.value" ng-cloak></span>)
                </span>
        	</div>
        </div>
    </div>

    <!-- Privacy Settings -->
    <div class="col-md-3 col-sm-3 col-xs-5 padding-left-fix">          
        <div class="workspace-toolbar">         
            <ul class="workspace-private-toolbar"> 
                <@orcid.checkFeatureStatus 'AFFILIATION_ORG_ID'> 
                    <li class="works-details">
                        <a ng-click="showDetailsMouseClick(group,$event);showMozillaBadges(group.activePutCode)" ng-mouseenter="showTooltip(group.groupId+'-showHideDetails')" ng-mouseleave="hideTooltip(group.groupId+'-showHideDetails')">
                            <span ng-class="(moreInfo[group.groupId] == true) ? 'glyphicons collapse_top' : 'glyphicons expand'">
                            </span>
                        </a>
                        <div class="popover popover-tooltip top show-hide-details-popover" ng-if="showElement[group.groupId+'-showHideDetails']">
                             <div class="arrow"></div>
                            <div class="popover-content">   
                                <span ng-if="moreInfo[group.groupId] == false || moreInfo[group.groupId] == null"><@orcid.msg 'common.details.show_details'/></span>   
                                <span ng-if="moreInfo[group.groupId]"><@orcid.msg 'common.details.hide_details'/></span>
                            </div>
                        </div>
                    </li>
                </@orcid.checkFeatureStatus>
                <#if !(isPublicProfile??)> 
                    <li>
                        <@orcid.privacyToggle2  angularModel="group.getActive().visibility.visibility"
                        questionClick="toggleClickPrivacyHelp(group.getActive().putCode.value)"
                        clickedClassCheck="{'popover-help-container-show':privacyHelp[group.getActive().putCode.value]==true}" 
                        publicClick="setPrivacy(group.getActive(), 'PUBLIC', $event)" 
                            limitedClick="setPrivacy(group.getActive(), 'LIMITED', $event)" 
                            privateClick="setPrivacy(group.getActive(), 'PRIVATE', $event)" />
                    </li>
                </#if>
            </ul>
        </div>
    </div>  
</div><!--row-->
<@orcid.checkFeatureStatus 'AFFILIATION_ORG_ID'>
    <!-- more info -->
    <#include "affiliate_more_info_inc_v3.ftl"/>
</@orcid.checkFeatureStatus>
<div class="row source-line">
	<div class="col-md-12 col-sm-12 col-xs-12">
		<div class="sources-container-header">          
			<div class="row">
				<div class="col-md-7 col-sm-7 col-xs-12">
					<@orcid.msg 'groups.common.source'/>: {{(group.getActive().sourceName == null || group.getActive().sourceName == '') ? group.getActive().source : group.getActive().sourceName}}	
				</div>
                <@orcid.checkFeatureStatus featureName='AFFILIATION_ORG_ID' enabled=false>
    				<div class="col-md-3 col-sm-3 col-xs-6">
    					<@orcid.msg 'groups.common.created'/>: <span ng-bind="group.getActive().createdDate | ajaxFormDateToISO8601"></span>
    				</div>
                </@orcid.checkFeatureStatus>			
				<div class="col-md-2 col-sm-2 col-xs-6 pull-right">
					<ul class="sources-options">
						<#if !(isPublicProfile??)>
							<li ng-if="group.getActive().source == '${effectiveUserOrcid}'">
								<a ng-click="openEditAffiliation(group.getActive())" ng-mouseenter="showTooltip(group.getActive().putCode.value+'-edit')" ng-mouseleave="hideTooltip(group.getActive().putCode.value+'-edit')">
								    <span class="glyphicon glyphicon-pencil"></span>
								</a>
								<div class="popover popover-tooltip top edit-source-popover" ng-if="showElement[group.getActive().putCode.value+'-edit']"> 
								    <div class="arrow"></div>
								    <div class="popover-content">
								        <span ><@orcid.msg 'groups.common.edit_my'/></span>
								    </div>                
								</div>	
							</li>   
					        <li>
					            <a id="delete-affiliation_{{group.getActive().putCode.value}}" href ng-click="deleteAffiliation(group.getActive())" ng-mouseenter="showTooltip(group.getActive().putCode.value+'-delete')" ng-mouseleave="hideTooltip(group.getActive().putCode.value+'-delete')" class="glyphicon glyphicon-trash"></a>
					            <div class="popover popover-tooltip top delete-source-popover" ng-if="showElement[group.getActive().putCode.value+'-delete']"> 
								 	<div class="arrow"></div>
								    <div class="popover-content">
								    	 <@orcid.msg 'groups.common.delete_this_source' />
								    </div>                
								</div>
					        </li>
				        </#if>  
					</ul>
				</div>
			</div>
		</div>
	</div>
</div>
