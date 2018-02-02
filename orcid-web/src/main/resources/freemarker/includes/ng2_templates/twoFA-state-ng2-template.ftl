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

<script type="text/ng-template" id="two-fa-state-ng2-template">

    <p>
        ${springMacroRequestContext.getMessage("2FA.details")}
        <br />
        <a href="${knowledgeBaseUri}/articles/1190068"
            target="2FA.learn_more_link">${springMacroRequestContext.getMessage("2FA.learn_more_link")}</a>
    </p>
    <div *ngIf="showEnabled2FA" >
        <span class="on">${springMacroRequestContext.getMessage("2FA.state.on.heading")} <span class="glyphicon glyphicon-ok"></span></span>
        <span class="small bold leftBuffer">${springMacroRequestContext.getMessage("2FA.state.on.description")}</span>
        <a class="leftBuffer" id="disable2FA" (click)="disable2FA()" href="#">${springMacroRequestContext.getMessage("2FA.disable")}</a>
    </div>
    <div *ngIf="showDisabled2FA" >
        <span class="off">${springMacroRequestContext.getMessage("2FA.state.off.heading")} <span class="glyphicon glyphicon-remove"></span></span>
        <span class="small bold leftBuffer">${springMacroRequestContext.getMessage("2FA.state.off.description")}</span>
        <button (click)="enable2FA()" class="btn btn-primary leftBuffer">${springMacroRequestContext.getMessage("2FA.enable")}</button>
    </div>
  
</script>