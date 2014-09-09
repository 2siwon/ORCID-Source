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
<div class="more-info">
	<div class="row"> 
		
		<div class="col-md-6" ng-show="funding.fundingType.value" ng-cloak>			
			<div class="bottomBuffer">					
				<strong><@orcid.msg 'manual_funding_form_contents.grant_type'/></strong>
				<div ng-bind="funding.fundingTypeForDisplay"></div>
			</div>
		</div>

		<div class="col-md-6" ng-show="funding.organizationDefinedFundingSubType.subtype.value" ng-cloak>
			<div class="bottomBuffer">					
				<strong><@orcid.msg 'manual_funding_form_contents.organization_defined_type.label'/></strong>
				<div ng-bind="funding.organizationDefinedFundingSubType.subtype.value"></div>
			</div>		
		</div>
		
		<div class="col-md-6" ng-show="funding.fundingTitle.title.value" ng-cloak>
			<div class="bottomBuffer">
				<strong><@orcid.msg 'manual_funding_form_contents.label_title'/></strong>
				<div ng-bind="funding.fundingTitle.title.value"></div>
			</div>
		</div>
		
		<div class="col-md-6" ng-show="funding.fundingTitle.translatedTitle.content" ng-cloak>
			<div class="bottomBuffer">				
				<strong><@orcid.msg
					'manual_funding_form_contents.label_translated_title'/></strong>
				<div ng-bind="renderTranslatedTitleInfo(funding)"></div>					
			</div>		
		</div>
		
		<div class="col-md-6" ng-show="funding.amount.value" ng-cloak>
			<div class="bottomBuffer">				
				<strong><@orcid.msg 'manual_funding_form_contents.label_amount'/></strong>
				<div>{{funding.currencyCode.value}} {{funding.amount.value}}</div>				
			</div>
		</div>
		<div class="col-md-6" ng-show="funding.startDate.year" ng-cloak>
			<div class="bottomBuffer">				
				<strong><@orcid.msg 'manual_funding_form_contents.labelStartDate'/></strong>
				<div>
					<span ng-show="funding.startDate.year">{{funding.startDate.year}}</span><span ng-show="funding.startDate.month">-{{funding.startDate.month}}</span><span ng-show="funding.startDate.day && funding.startDate.month">-{{funding.startDate.day}}</span>						
				</div>				
			</div>
		</div>
		<div class="col-md-6" ng-show="funding.endDate.year" ng-cloak>
			<div class="bottomBuffer">				
				<strong><@orcid.msg 'manual_funding_form_contents.labelEndDate'/></strong>
				<div>
					<span ng-show="funding.endDate.year">{{funding.endDate.year}}</span><span ng-show="funding.endDate.month">-{{funding.endDate.month}}</span><span ng-show="funding.endDate.day && funding.endDate.month">-{{funding.endDate.day}}</span>						
				</div>				
			</div>
		</div>
		<div class="col-md-6" ng-show="funding.contributors.length > 0" ng-cloak>
			<div class="bottomBuffer">
				<strong><@orcid.msg 'manual_funding_form_contents.label_contributors'/></strong>
				<div ng-repeat="contributor in funding.contributors">
					{{contributor.creditName.value}} <span
						ng-bind='contributor | contributorFilter'></span>
				</div>		
			</div>
		</div>
		<div class="col-md-6" ng-show="funding.description.value" ng-cloak>
			<div class="bottomBuffer">				
				<strong><@orcid.msg 'manual_funding_form_contents.label_description'/></strong>
				<div ng-bind="funding.description.value"></div>				
			</div>
		</div>
		<div class="col-md-6"  ng-show="funding.fundingName.value" ng-cloak>
			<div class="bottomBuffer">									
				<strong><@orcid.msg 'manual_funding_form_contents.label_funding_agency_name'/></strong>
				<div ng-bind="funding.fundingName.value"></div>				
			</div>
		</div>
		<div class="col-md-6" ng-show="funding.city.value" ng-cloak>
			<div class="bottomBuffer">				
				<strong><@orcid.msg 'manual_funding_form_contents.label_city'/></strong>
				<div ng-bind="funding.city.value"></div>
			</div>
		</div>
		<div class="col-md-6" ng-show="funding.region.value" ng-cloak>
			<div class="bottomBuffer">				
				<strong><@orcid.msg 'manual_funding_form_contents.label_region'/></strong>
				<div ng-bind="funding.region.value"></div>				
			</div>
		</div>	
		<div class="col-md-6"  ng-show="funding.country.value" ng-cloak>
			<div class="bottomBuffer">				
				<strong><@orcid.msg 'manual_funding_form_contents.label_country'/></strong>
				<div ng-bind="funding.countryForDisplay"></div>				
			</div>
		</div>
		
		<div class="col-md-6" ng-show="funding.url.value" ng-cloak>
			<div class="bottomBuffer">				
				<strong><@orcid.msg 'manual_funding_form_contents.label_url'/></strong>
				<div ng-bind="funding.url.value"></div>				
			</div>
		</div>
	</div>
	<div class="row">									
		<div class="col-md-12">
			<div class="bottomBuffer" ng-show="funding.sourceName" ng-cloak>				
				<strong><@orcid.msg 'manual_funding_form_contents.label_source'/></strong>
				<div ng-bind="funding.sourceName"></div>						
			</div>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12">
			<div class="work-source bottombuffer" ng-show="worksSrvc.details[work.putCode.value].workSourceName.value" ng-cloak>
				<strong> <@orcid.msg
				'manual_work_form_contents.labelWorkSource'/></strong>
				<div ng-bind="worksSrvc.details[work.putCode.value].workSourceName.value"></div>
			</div>
		</div>
	</div>
</div>
