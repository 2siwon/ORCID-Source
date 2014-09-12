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
<div class="lhs">
	<#include "includes/id_banner.ftl"/>
</div>
<div class="lhs override">
	<ul class="settings-nav">
		<li><a href="#account-settings">${springMacroRequestContext.getMessage("manage.accountsettings")}</a></li>
		<li><a href="#manage-permissions">${springMacroRequestContext.getMessage("manage.managepermission")}</a></li>			
		<#if (profile.groupType)?? && ((profile.groupType) = "BASIC" ||	(profile.groupType) = "PREMIUM" || (profile.groupType) = "BASIC_INSTITUTION" || (profile.groupType) = "PREMIUM_INSTITUTION")>
			<li><a href="<@spring.url "/manage-clients" />">${springMacroRequestContext.getMessage("workspace.developer_tools")}</a></li>
		</#if>
		<#if RequestParameters['PublicClient']??>			
			<li><a href="#manage-developer-tools">${springMacroRequestContext.getMessage("workspace.developer_tools")}</a></li>
		</#if>
	</ul>
</div>