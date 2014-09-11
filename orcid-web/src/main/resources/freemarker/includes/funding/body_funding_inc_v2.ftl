<#--

    =============================================================================

    ORCID (R) Open Source
    http://orcid.org

    Copyright (c) 2012-2013 ORCID, Inc.
    Licensed under an MIT-Style License (MIT)
    http://orcid.org/open-source-license

    This copyright and license information (including a link to the full license)
    shall be included in its entirety in all copies or substantial portion of
    the software.

    =============================================================================

-->
<ul ng-hide="!fundingSrvc.groups.length" class="workspace-fundings workspace-body-list bottom-margin-medium" ng-cloak>
	<li class="bottom-margin-small" ng-repeat="funding in fundingSrvc.groups |  orderBy:['-dateSortString', 'affiliationName']"> 
		<div class="row">        			
			<!-- Information -->
			<div class="col-md-9 col-sm-9">
				<h3 class="workspace-title">
					<strong ng-show="funding.fundingTitle.title.value">{{funding.fundingTitle.title.value}}:</strong>
					<span class="funding-name" ng-bind-html="funding.fundingName.value"></span>					
				</h3>
				
				<div class="info-detail">
					<span class="funding-date" ng-show="funding.startDate && !funding.endDate">
						<span ng-show="funding.startDate.year">{{funding.startDate.year}}</span><span ng-show="funding.startDate.month">-{{funding.startDate.month}}</span>						
				    	<@orcid.msg 'workspace_fundings.dateSeparator'/>
				    	<@orcid.msg 'workspace_fundings.present'/>
					</span>
					<span class="funding-date" ng-show="funding.startDate && funding.endDate">
						<span ng-show="funding.startDate.year">{{funding.startDate.year}}</span><span ng-show="funding.startDate.month">-{{funding.startDate.month}}</span>						
						<@orcid.msg 'workspace_fundings.dateSeparator'/>
						<span ng-show="funding.endDate.year">{{funding.endDate.year}}</span><span ng-show="funding.endDate.month">-{{funding.endDate.month}}</span>
					</span>
					<span class="funding-date" ng-show="!funding.startDate && funding.endDate">
					     <span ng-show="funding.endDate.year">{{funding.endDate.year}}</span><span ng-show="funding.endDate.month">-{{funding.endDate.month}}</span>
					</span>
				</div>
			</div>	
			
			<!-- Privacy Settings -->
	        <div class="col-md-3 col-sm-3 workspace-toolbar">
	        	<#if !(isPublicProfile??)>
	        		<!-- <a href ng-click="deleteFunding(funding)" class="glyphicon glyphicon-trash grey"></a> -->
	        		<ul class="workspace-private-toolbar">
	        			<li>
					 		<a href="" class="toolbar-button edit-item-button">
					 			<span class="glyphicon glyphicon-pencil edit-option-toolbar" title=""></span>
					 		</a>	
					 	</li>
	        			<li>
							<@orcid.privacyToggle2  angularModel="funding.visibility.visibility"
							questionClick="toggleClickPrivacyHelp(funding.putCode.value)"
							clickedClassCheck="{'popover-help-container-show':privacyHelp[funding.putCode.value]==true}" 
							publicClick="setPrivacy(funding, 'PUBLIC', $event)" 
		                	limitedClick="setPrivacy(funding, 'LIMITED', $event)" 
		                	privateClick="setPrivacy(funding, 'PRIVATE', $event)" />
	                	</li>
	                	 <li class="submenu-tree">
		            		<a href="" class="toolbar-button toggle-menu" id="more-options-button">
		            			<span class="glyphicon glyphicon-align-left edit-option-toolbar"></span>
		            		</a>
		            		<ul class="workspace-submenu-options">
		            			<li>
		            				<a href=""><span class="glyphicon glyphicon-file"></span>Review Versions</a>
		            			</li>
		            			<li>
		            				<a href=""><span class="glyphicon glyphicon-trash"></span>Delete</a>
		            			</li>
		            			<li>
		            				<a href=""><span class="glyphicon glyphicon-question-sign"></span>Help</a>
		            			</li>
		            		</ul>
            			</li>			        
		        	</ul>
		        </#if>
			</div>
		</div>
		<div class="row bottomBuffer" ng-show="funding.externalIdentifiers.length > 0" ng-cloak>
				<div class="col-md-9 col-sm-9">					
					<div>					
						<span ng-repeat='ei in funding.externalIdentifiers'>							
							<span ng-bind-html='ei | externalIdentifierHtml:$first:$last:funding.externalIdentifiers.length'>
							</span>
						</span>
					</div>
				</div>
				<div class="col-md-3 col-sm-3">
					<ul class="validations-versions nav nav-pills nav-stacked">
						<li><a href=""><span class="glyphicon glyphicon-ok green"></span><strong></strong><span class="badge pull-right blue">2</span>Validated</a></li>
						<li><a href=""><span class="glyphicon glyphicon-file green"></span><span class="badge pull-right blue">3</span>Versions</a></li> <!-- for non versions use class 'opaque' instead green -->
					</ul>
				</div>
			</div>			
		<div ng-show="moreInfo[funding.putCode.value]">
			<div class="content">			
				<#include "funding_more_info_inc_v2.ftl"/>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 col-sm-12 col-xs-12">
				<div class="show-more-info-tab">			
					<a href="" ng-show="!moreInfo[funding.putCode.value]" ng-click="showDetailsMouseClick(funding.putCode.value,$event);" class=""><span class="glyphicon glyphicon-chevron-down"></span><@orcid.msg 'manage.developer_tools.show_details'/></a>
					<a href="" ng-show="moreInfo[funding.putCode.value]" ng-click="showDetailsMouseClick(funding.putCode.value,$event);" class="ng-hide"><span class="glyphicon glyphicon-chevron-up"></span><@orcid.msg 'manage.developer_tools.hide_details'/></a>
				</div>
			</div>		
		</div>	
	</li>
</ul>

<div ng-show="fundingSrvc.loading == true;" class="text-center">
    <i class="glyphicon glyphicon-refresh spin x4 green" id="spinner"></i>
    <!--[if lt IE 8]>    
    	<img src="${staticCdn}/img/spin-big.gif" width="85" height ="85"/>
    <![endif]-->
</div>
<div ng-show="fundingSrvc.loading == false && fundingSrvc.groups.length == 0" class="alert alert-info" ng-cloak>
    <strong><#if (publicProfile)?? && publicProfile == true><@orcid.msg 'workspace_fundings_body_list.nograntaddedyet' /><#else><@orcid.msg 'workspace_fundings.havenotaddaffiliation' /><a ng-click="addFundingModal()"> <@orcid.msg 'workspace_fundings_body_list.addsomenow'/></a></#if></strong>
</div>

