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
<@protected classes=['manage'] nav="settings">
<div id="2FASetup" ng-controller="2FASetupCtrl" ng-init="startSetup()">
    <div class="row">        
        <div class="col-md-9 col-md-offset-3 col-sm-12 col-xs-12">
            <h1>${springMacroRequestContext.getMessage("2FA.enable")}</h1>
            <hr>
            <div ng-show="showSetup2FA" ng-cloak>
                <p>
                    ${springMacroRequestContext.getMessage("2FA.details")}
                    <br />
                    <a href="${knowledgeBaseUri}/articles/580410"
                        target="2FA.learn_more_link">${springMacroRequestContext.getMessage("2FA.learn_more_link")}</a>
                </p>
                <ul id="2FASetupSteps" class="twoFactorAuthSetup">
                    <li class="bold">${springMacroRequestContext.getMessage("2FA.setup.step1")}</li>
                    <p>${springMacroRequestContext.getMessage("2FA.setup.step1.details")}</p>
                    <li class="bold">${springMacroRequestContext.getMessage("2FA.setup.step2")}</li>
                    <p>${springMacroRequestContext.getMessage("2FA.setup.step2.details")}</p>
                    <p><span class="bold">${springMacroRequestContext.getMessage("2FA.setup.cannot.scan1")}</span> <a href="javascript:void(0);" id="getTextCode">${springMacroRequestContext.getMessage("2FA.setup.cannot.scan2")}</a> ${springMacroRequestContext.getMessage("2FA.setup.cannot.scan3")}</p>
                    <img id="2FA-QR-code" ng-show="showQRCode" />
                    <pre ng-bind="textCodeFor2FA" ng-show="showTextCode"></pre>
                    <li class="bold">${springMacroRequestContext.getMessage("2FA.setup.step3")}</li>
                    <p>${springMacroRequestContext.getMessage("2FA.setup.step3.details")}</p>
                </ul>
                <form>
                    <input type="text" ng-model="twoFactorAuthRegistration.verificationCode" />
                    <div id="invalidCodeError" class="orcid-error" ng-show="showInvalidCodeError" ng-cloak">${springMacroRequestContext.getMessage("2FA.setup.invalidCode")}</div>
                    <hr>
                    <button id="sendVerificationCode" ng-click="sendVerificationCode()" class="btn btn-primary">${springMacroRequestContext.getMessage("2FA.continue")}</button>
                    <a class="leftBuffer" id="cancel2FASetup" ng-click="cancel2FASetup()" href="javascript:void(0);">${springMacroRequestContext.getMessage("2FA.cancel")}</a>
                </form>
             </div>
             <div ng-show="show2FARecoveryCodes" ng-cloak>
                <h2 id="saveRecoveryCodesHeading">${springMacroRequestContext.getMessage("2FA.recoveryCodes.heading")}</h2>
                <p>
                    ${springMacroRequestContext.getMessage("2FA.recoveryCodes.details")}
                    <br />
                    <a href="${knowledgeBaseUri}/articles/1190068" target="2FA.learn_more_link">${springMacroRequestContext.getMessage("2FA.learn_more_link")}</a>
                </p>
                <div id="recoveryCodes">
                    <span ng-repeat="recoveryCode in recoveryCodes">{{recoveryCode}}<br></span>
                </div>
                <div class="form-group">
                    <button ng-click="downloadRecoveryCodes()" class="btn btn-white"><span class="glyphicon glyphicon-download-alt"></span> ${springMacroRequestContext.getMessage("2FA.recoveryCodes.download")}</button>
                    <button ng-click="copyRecoveryCodes()" class="btn btn-white"><span class="glyphicon glyphicon-file"></span> ${springMacroRequestContext.getMessage("2FA.recoveryCodes.copy")}</button>
                </div>
                <div class="form-group">
                    <p><span>${springMacroRequestContext.getMessage("2FA.recoveryCodes.warning.heading")}</span>&nbsp;${springMacroRequestContext.getMessage("2FA.recoveryCodes.warning.details")}</p>
                    <button ng-click="done()" class="btn btn-primary">${springMacroRequestContext.getMessage("2FA.recoveryCodes.done")}</button>
                </div>
             </div>
        </div>
    </div>
</div>
</@protected>