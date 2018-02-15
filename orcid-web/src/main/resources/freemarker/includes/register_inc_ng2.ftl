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
<div id="register" class="oauth-registration">
    <!-- First name -->
    <div class="form-group clear-fix">
        <label for="givenNames" class="control-label"><@orcid.msg 'oauth_sign_up.labelfirstname'/></label>
        <div class="bottomBuffer">
            <input id="register-form-given-names" name="givenNames" type="text" tabindex="1" [(ngModel)]="registrationForm.givenNames.value" (blur)="serverValidate('GivenNames')"/>                         
            <span class="required" [ngClass]="isValidClass(registrationForm.givenNames)">*</span>            
            <div class="popover-help-container">
                <a href="javascript:void(0);"><i class="glyphicon glyphicon-question-sign"></i></a>
                <div id="name-help" class="popover bottom">
                    <div class="arrow"></div>
                    <div class="popover-content">
                        <p><@orcid.msg 'orcid.frontend.register.help.first_name'/></p>
                        <p><@orcid.msg 'orcid.frontend.register.help.last_name'/></p>
                        <p><@orcid.msg 'orcid.frontend.register.help.update_names'/></p>
                        <a href="<@orcid.msg 'orcid.frontend.register.help.more_info.link.url'/>" target="orcid.frontend.register.help.more_info.link.text"><@orcid.msg 'orcid.frontend.register.help.more_info.link.text'/></a>
                    </div>
                </div>
            </div>
            <span class="orcid-error" *ngIf="registrationForm.givenNames.errors.length > 0">
                <div *ngFor="let error of registrationForm.givenNames.errors" [innerHTML]="error"></div>
            </span>
        </div>
    </div>	
     <!-- Last name -->
    <div class="form-group clear-fix">
        <label class="control-label"><@orcid.msg 'oauth_sign_up.labellastname'/></label>
        <div class="bottomBuffer">
            <input id="register-form-family-name" name="familyNames" type="text" tabindex="2" class=""  [(ngModel)]="registrationForm.familyNames.value" (blur)="serverValidate('familyNames')"/>
            <span class="orcid-error" *ngIf="registrationForm.familyNames.errors.length > 0">
                <div *ngFor="let error of registrationForm.familyNames.errors" [innerHTML]="error"></div>
            </span>
        </div>
    </div>
    <@orcid.checkFeatureStatus featureName='REG_MULTI_EMAIL'>
        <!-- Primary email -->
        <div class="form-group clear-fix">
            <label class="control-label">${springMacroRequestContext.getMessage("oauth_sign_up.labelemailprimary")}</label>
            <div class="relative">          
                <input name="emailprimary234" type="text" tabindex="3" class="input-xlarge" [(ngModel)]="registrationForm.email.value" (blur)="serverValidate('Email')"/>
                <span class="required" [ngClass]="isValidClass(register.email)">*</span>
                <span class="orcid-error" *ngIf="registrationForm.email.errors.length > 0 && !showDeactivatedError && !showReactivationSent">
                    <div *ngFor="let error of registrationForm.email.errors" [innerHTML]="error"></div>
                </span>
                <span class="orcid-error" *ngIf="showDeactivatedError" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.1")}<a href="" (click)="sendReactivationEmail()">${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.3")}
                </span>
                <span class="orcid-error" *ngIf="showReactivationSent" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.1")}<a href="mailto:support@orcid.org">${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.3")}
                </span>
            </div>
        </div>  
        <!-- Additional emails -->
        <div class="form-group clear-fix" *ngFor="let emailAdditional of registrationForm.emailsAdditional;trackBy:$index">
            <label class="control-label">${springMacroRequestContext.getMessage("oauth_sign_up.labelemailadditional")}</label>
            <div class="relative">
                <input name="emailadditional234" type="text" tabindex="3" class="input-xlarge" [(ngModel)]="registrationForm.emailsAdditional[$index].value" focus-last-input="$index == focusIndex" (blur)="serverValidate('EmailsAdditional')"/>
                <div *ngIf="$first" class="popover-help-container leftBuffer">
                    <a href="javascript:void(0);"><i class="glyphicon glyphicon-question-sign"></i></a>
                    <div id="email-additional-help" class="popover bottom">
                        <div class="arrow"></div>
                        <div class="popover-content">
                            <p><@orcid.msg ''/></p>
                            <p><@orcid.msg 'orcid.frontend.register.help.email_additional'/></p>
                        </div>
                    </div>
                </div>
                <div *ngIf="!$first" class="popover-help-container leftBuffer">
                    <a class="btn-white-no-border" (click)="removeEmailField($index)"><i class="glyphicon glyphicon-remove-sign"></i></a>
                </div>
                <span class="orcid-error" *ngIf="registrationForm.emailsAdditional[$index].errors.length > 0 && !showEmailsAdditionalDeactivatedError[$index] && !showEmailsAdditionalReactivationSent[$index]">
                    <div *ngFor="let error of registrationForm.emailsAdditional[$index].errors;trackBy:$index" [innerHTML]="error"></div>
                </span>
                <span class="orcid-error" *ngIf="showEmailsAdditionalDeactivatedError[$index]" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.1")}<a href="" (click)="sendEmailsAdditionalReactivationEmail($index)">${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.3")}
                </span>
                <span class="orcid-error" *ngIf="showEmailsAdditionalReactivationSent[$index]" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.1")}<a href="mailto:support@orcid.org">${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.3")}
                </span>
            </div>
        </div>
        <button (click)="addEmailField()" class="left btn-white-no-border"><i class="glyphicon glyphicon-plus-sign"></i> ${springMacroRequestContext.getMessage("oauth_sign_up.buttonaddemail")}</button>
    </@orcid.checkFeatureStatus>
    <@orcid.checkFeatureStatus featureName='REG_MULTI_EMAIL' enabled=false> 
        <!-- Email -->                  
        <!--<div class="form-group clear-fix">
            <label class="control-label"><@orcid.msg 'oauth_sign_up.labelemail'/></label>
            <div class="bottomBuffer">
                <input id="register-form-email" name="email" type="email" tabindex="3" class="" [(ngModel)]="registrationForm.email.value" (blur)="serverValidate('Email')" />
                <span class="required" [ngClass]="isValidClass(registrationForm.email)">*</span> <span class="orcid-error" *ngIf="emailTrustAsHtmlErrors.length > 0 && !showDeactivatedError && !showReactivationSent">
                    <div *ngFor="let error of emailTrustAsHtmlErrors" [innerHTML]="error" compile="html"></div>
                </span>
                <span class="orcid-error" *ngIf="showDeactivatedError" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.1")}<a href="" (click)="sendReactivationEmail(registrationForm.email.value)">${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.3")}
                </span>
                <span class="orcid-error" *ngIf="showReactivationSent" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.1")}<a href="mailto:support@orcid.org">${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.3")}
                </span>                                             
            </div>
        </div>-->
        <!--Re-enter email-->
        <div class="form-group clear-fix">
            <label class="control-label"><@orcid.msg 'oauth_sign_up.labelreenteremail'/></label>
            <div class="bottomBuffer">
                <input id="register-form-confirm-email" name="confirmedEmail" type="email" tabindex="4" class="" [(ngModel)]="registrationForm.emailConfirm.value" (blur)="serverValidate('EmailConfirm')" />
                <span class="required" [ngClass]="isValidClass(registrationForm.emailConfirm)">*</span>                  
                <span class="orcid-error" *ngIf="registrationForm.emailConfirm.errors.length > 0 && !showDeactivatedError && !showReactivationSent">
                    <div *ngFor="let error of registrationForm.emailConfirm.errors" [innerHTML]="error"></div>
                </span>
            </div>
        </div>
    </@orcid.checkFeatureStatus>
    <!--Password-->
    <div class="form-group clear-fix">
        <label class="control-label"><@orcid.msg 'oauth_sign_up.labelpassword'/></label>
        <div class="bottomBuffer">
            <input id="register-form-password" type="password" name="password" tabindex="5" class="" [(ngModel)]="registrationForm.password.value" (blur)="serverValidate('Password')"/>
            <span class="required" [ngClass]="isValidClass(registrationForm.password)">*</span>
            <@orcid.passwordHelpPopup />
            <span class="orcid-error" *ngIf="registrationForm.password.errors.length > 0">
                <div *ngFor="let error of registrationForm.password.errors" [innerHTML]="error"></div>
            </span>
        </div>
    </div>  	
	<!--Confirm password-->
    <div class="form-group clear-fix">
        <label class="control-label"><@orcid.msg 'password_one_time_reset.labelconfirmpassword'/></label>
        <input id="register-form-confirm-password" type="password" name="confirmPassword" tabindex="6" class="" [(ngModel)]="registrationForm.passwordConfirm.value" (blur)="serverValidate('PasswordConfirm')"/>
        <span class="required" [ngClass]="isValidClass(registrationForm.passwordConfirm)">*</span>                 
        <span class="orcid-error" *ngIf="registrationForm.passwordConfirm.errors.length > 0">
            <div *ngFor="let error of registrationForm.passwordConfirm.errors" [innerHTML]="error"></div>
        </span>
    </div>
    <@orcid.checkFeatureStatus featureName='GDPR_UI'> 
        <!--Visibility default-->
        <div class="form-group clear-fix popover-registry">  
            <h4>${springMacroRequestContext.getMessage("register.privacy_settings")}</h4>         
            <p>${springMacroRequestContext.getMessage("privacy_preferences.activitiesVisibilityDefault")}</p> 
            <p><b>${springMacroRequestContext.getMessage("privacy_preferences.activitiesVisibilityDefault.who_can_see_this")}</b></p>
            <div class="visibilityDefault">
                <div class="radio">
                  <label><input type="radio" name="defaultVisibility" [(ngModel)]="registrationForm.activitiesVisibilityDefault.visibility" value="PUBLIC" (blur)="serverValidate('ActivitiesVisibilityDefault')"><span class="public"></span><span class="defaultVisLabel"><b><@orcid.msg 'manage.lipublic'/></b> <@orcid.msg 'register.privacy_everyone_text'/></span></label>
                </div>
                <div class="radio">
                  <label><input type="radio" name="defaultVisibility" [(ngModel)]="registrationForm.activitiesVisibilityDefault.visibility" value="LIMITED" (blur)="serverValidate('ActivitiesVisibilityDefault')"><span class="limited"></span><span class="defaultVisLabel"><b><@orcid.msg 'manage.lilimited'/></b> <@orcid.msg 'register.privacy_limited_text'/></span></label>
                </div>
                <div class="radio">
                  <label><input type="radio" name="defaultVisibility" [(ngModel)]="registrationForm.activitiesVisibilityDefault.visibility" value="PRIVATE" (blur)="serverValidate('ActivitiesVisibilityDefault')"><span class="private"></span><span class="defaultVisLabel"><b><@orcid.msg 'manage.liprivate'/></b> <@orcid.msg 'register.privacy_private_text'/></span></label>
                </div>
            </div>
            <div class="visibilityHelp">
                <span class="required" [ngClass]="isValidClass(registrationForm.activitiesDefaultVisibility)">*</span>
                <div class="popover-help-container">
                    <a href="javascript:void(0);"><i class="glyphicon glyphicon-question-sign"></i></a>
                    <div id="name-help" class="popover bottom">
                        <div class="arrow"></div>
                        <div class="popover-content">
                            <strong>${springMacroRequestContext.getMessage("privacyToggle.help.who_can_see")}</strong>
                            <ul class="privacyHelp">
                                <li class="public" style="color: #009900;">${springMacroRequestContext.getMessage("privacyToggle.help.everyone")}</li>
                                <li class="limited"style="color: #ffb027;">${springMacroRequestContext.getMessage("privacyToggle.help.trusted_parties")}</li>
                                <li class="private" style="color: #990000;">${springMacroRequestContext.getMessage("privacyToggle.help.only_me")}</li>
                            </ul>
                            <a href="${knowledgeBaseUri}/articles/124518-orcid-privacy-settings" target="privacyToggle.help.more_information">${springMacroRequestContext.getMessage("privacyToggle.help.more_information")}</a>
                        </div>
                    </div>
                </div>
            </div>
            <span class="orcid-error" *ngIf="registrationForm.activitiesVisibilityDefault.errors.length > 0">
            <div *ngFor="let error of registrationForm.activitiesVisibilityDefault.errors" [innerHTML]="error"></div>
        </span>
        </div>
        <!--Email frequency-->
        <div class="form-group clear-fix">              
            <div>   
                <h4 class="dark-label">${springMacroRequestContext.getMessage("claim.notifications")}</h4>                
                <p>
                    ${springMacroRequestContext.getMessage("claim.notificationsemailfrequency_1")}<a href="https://support.orcid.org/knowledgebase/articles/665437" target="learn_more">${springMacroRequestContext.getMessage("claim.notificationsemailfrequency_2")}</a>${springMacroRequestContext.getMessage("claim.notificationsemailfrequency_3")}
                </p>
                <select id="sendEmailFrequencyDays" name="sendEmailFrequencyDays"
                    class="input-xlarge"
                    [(ngModel)]="registrationForm.sendEmailFrequencyDays.value">
                    <#list sendEmailFrequencies?keys as key>
                        <option value="${key}" [selected]="registrationForm.sendEmailFrequencyDays.value === ${key}">${sendEmailFrequencies[key]}</option>
                    </#list>
                </select>        
            </div>
        </div>
        <!--Terms and conditions-->
        <!--<div class="bottomBuffer">
            <h4><@orcid.msg 'register.labelTermsofUse'/>
                <span class="required" [ngClass]="{'text-error':register.termsOfUse.value == false}"></span></h4>  
            <p>
                <input id="register-form-term-box" type="checkbox" name="termsConditions" tabindex="9" name="acceptTermsAndConditions" [(ngModel)]="registrationForm.termsOfUse.value" (blur)="serverValidate('TermsOfUse')" />
                <@orcid.msg 'register.labelconsent'/> <a href="${aboutUri}/footer/privacy-policy" target="register.labelprivacypolicy"><@orcid.msg 'register.labelprivacypolicy'/></a>&nbsp;<@orcid.msg 'register.labeland'/>&nbsp;<@orcid.msg 'common.termsandconditions1'/><a href="${aboutUri}/content/orcid-terms-use" target="common.termsandconditions2"><@orcid.msg 'common.termsandconditions2'/></a>&nbsp;<@orcid.msg 'common.termsandconditions3'/>
            </p>
            <span class="orcid-error" *ngIf="registrationForm.termsOfUse.errors.length > 0">
                <div *ngFor="let error of registrationForm.termsOfUse.errors" [innerHTML]="error"></div>
            </span>
        </div>-->
        <!--Recaptcha-->
        <@orcid.checkFeatureStatus featureName='DISABLE_RECAPTCHA' enabled=false> 
            <div>
                <div class="bottomBuffer relative recaptcha"  id="recaptcha">
                    <div vc-recaptcha
                    theme="'light'"
                    key="model.key"
                    on-create="setRecaptchaWidgetId(widgetId)"
                    on-success="setRecatchaResponse(response)"></div>
                        <span class="orcid-error" *ngIf="registrationForm.grecaptcha.errors.length > 0">
                            <div *ngFor="let error of registrationForm.grecaptcha.errors;trackBy:$index" [innerHTML]="error"></div>
                        </span>
                </div>
            </div>
        </@orcid.checkFeatureStatus> 
    </@orcid.checkFeatureStatus>
    <@orcid.checkFeatureStatus featureName='GDPR_UI' enabled=false> 
        <!--Visibility default-->
        <div class="form-group clear-fix popover-registry">
            <div class="oauth-privacy">                   
                <label class="privacy-toggle-lbl">${springMacroRequestContext.getMessage("privacy_preferences.activitiesVisibilityDefault")}</label> 
                <label class="privacy-toggle-lbl">${springMacroRequestContext.getMessage("privacy_preferences.activitiesVisibilityDefault.who_can_see_this")}</label>
                <@orcid.privacyToggle3Ng2
                    angularModel="default_visibility"
                    publicClick="updateActivitiesVisibilityDefault('PUBLIC', $event)" 
                    limitedClick="updateActivitiesVisibilityDefault('LIMITED', $event)" 
                    privateClick="updateActivitiesVisibilityDefault('PRIVATE', $event)" 
                    elementId="workPrivHelp" /> 
            </div>
        </div>
        <!--Email frequency-->
        <div>
            <div class="relative">              
                <@orcid.registrationEmailFrequencySelector angularElementName="registrationForm" />
            </div>
        </div>
        
        <!--Recaptcha-->
        <@orcid.checkFeatureStatus featureName='DISABLE_RECAPTCHA' enabled=false> 
            <div>
                <div class="bottomBuffer relative recaptcha"  id="recaptcha">
                    <div vc-recaptcha
                    theme="'light'"
                    key="model.key"
                    on-create="setRecaptchaWidgetId(widgetId)"
                    on-success="setRecatchaResponse(response)"></div>
                        <span class="orcid-error" *ngIf="registrationForm.grecaptcha.errors.length > 0">
                            <div *ngFor="let error of registrationForm.grecaptcha.errors;trackBy:$index" [innerHTML]="error"></div>
                        </span>
                </div>
            </div>
        </@orcid.checkFeatureStatus>
        <!--Terms and conditions-->
        <!--<div class="bottomBuffer">
            <label for="termsConditions">
                <@orcid.msg 'register.labelTermsofUse'/>
                <span class="required"  [ngClass]="{'text-error':register.termsOfUse.value == false}">*</span>
            </label>
            <p>
                <input id="register-form-term-box" type="checkbox" name="termsConditions" tabindex="9" name="acceptTermsAndConditions" [(ngModel)]="registrationForm.termsOfUse.value" (blur)="serverValidate('TermsOfUse')" />
                <@orcid.msg 'register.labelconsent'/> <a href="${aboutUri}/footer/privacy-policy" target="register.labelprivacypolicy"><@orcid.msg 'register.labelprivacypolicy'/></a>&nbsp;<@orcid.msg 'register.labeland'/>&nbsp;<@orcid.msg 'common.termsandconditions1'/><a href="${aboutUri}/content/orcid-terms-use" target="common.termsandconditions2"><@orcid.msg 'common.termsandconditions2'/></a>&nbsp;<@orcid.msg 'common.termsandconditions3'/>
            </p>
            <span class="orcid-error" *ngIf="registrationForm.termsOfUse.errors.length > 0">
                <div *ngFor="let error of registrationForm.termsOfUse.errors" [innerHTML]="error"></div>
            </span>
        </div>-->
    </@orcid.checkFeatureStatus>  
    <!--Registration error-->
    <div style="margin-bottom: 15px;" *ngIf="generalRegistrationError == null">
        <span class="orcid-error" [innerHtml]="generalRegistrationError"></span>
    </div> 
    <!-- Buttons  -->
    <div class="bottomBuffer col-xs-12 col-sm-3">
        <#if (RequestParameters['linkRequest'])??>
            <button id="register-authorize-button" class="btn btn-primary" name="authorize" value="<@orcid.msg 'confirm-oauth-access.Authorize'/>" (click)="oauth2ScreensRegister('${RequestParameters.linkRequest}')">${springMacroRequestContext.getMessage("header.register")}</button>
        <#else>
            <button id="register-authorize-button" class="btn btn-primary" name="authorize" value="<@orcid.msg 'confirm-oauth-access.Authorize'/>" (click)="oauth2ScreensRegister(null)">${springMacroRequestContext.getMessage("header.register")}</button>
        </#if>
    </div>   
</div>    