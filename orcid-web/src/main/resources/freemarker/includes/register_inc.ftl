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
    new template!
    <!-- First name -->
    <div class="form-group clear-fix">
        <label for="givelNames" class="control-label"><@orcid.msg 'oauth_sign_up.labelfirstname'/></label>
        <div class="bottomBuffer">
        	<#if (client_name)??>
        	<#assign js_group_name = client_group_name?replace('"', '&quot;')?js_string>
	        <#assign js_client_name = client_name?replace('"', '&quot;')?js_string>	        
        	<input type="hidden" name="client_group_name" value="${js_group_name}" />
        	<input type="hidden" name="client_name" value="${js_client_name}" />
        	<input type="hidden" name="client_id" value="${client_id}" />        	
        	</#if>
            <input id="register-form-given-names" name="givenNames" type="text" tabindex="1" class="" ng-model="registrationForm.givenNames.value" ng-model-onblur ng-change="serverValidate('GivenNames')"/>                         
            <span class="required" ng-class="isValidClass(registrationForm.givenNames)">*</span>            
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
            <span class="orcid-error" ng-show="registrationForm.givenNames.errors.length > 0">
                <div ng-repeat='error in registrationForm.givenNames.errors' ng-bind-html="error"></div>
            </span>
        </div>
    </div>				
	<!-- Last name -->
    <div class="form-group clear-fix">
        <label class="control-label"><@orcid.msg 'oauth_sign_up.labellastname'/></label>
        <div class="bottomBuffer">
            <input id="register-form-family-name" name="familyNames" type="text" tabindex="2" class=""  ng-model="registrationForm.familyNames.value" ng-model-onblur/>
            <span class="orcid-error" ng-show="registrationForm.familyNames.errors.length > 0">
                <div ng-repeat='error in registrationForm.familyNames.errors' ng-bind-html="error"></div>
            </span>
        </div>
    </div>
    <@orcid.checkFeatureStatus featureName='REG_MULTI_EMAIL'>
        <!-- Primary email -->
        <div class="form-group clear-fix">
            <label class="control-label">${springMacroRequestContext.getMessage("oauth_sign_up.labelemailprimary")}</label>
            <div class="relative">          
                <input name="emailprimary234" type="text" tabindex="3" class="input-xlarge" ng-model="registrationForm.email.value" ng-blur="serverValidate('Email')"/>
                <span class="required" ng-class="isValidClass(register.email)">*</span>
                <span class="orcid-error" ng-show="registrationForm.email.errors.length > 0 && !showDeactivatedError && !showReactivationSent">
                    <div ng-repeat='error in registrationForm.email.errors' ng-bind-html="error"></div>
                </span>
                <span class="orcid-error" ng-show="showDeactivatedError" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.1")}<a href="" ng-click="sendReactivationEmail()">${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.3")}
                </span>
                <span class="orcid-error" ng-show="showReactivationSent" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.1")}<a href="mailto:support@orcid.org">${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.3")}
                </span>
            </div>
        </div>  
        <!-- Additional emails -->
        <div class="form-group clear-fix" ng-repeat="emailAdditional in registrationForm.emailsAdditional track by $index">
            <label class="control-label">${springMacroRequestContext.getMessage("oauth_sign_up.labelemailadditional")}</label>
            <div class="relative">
                <input name="emailadditional234" type="text" tabindex="3" class="input-xlarge" ng-model="registrationForm.emailsAdditional[$index].value" focus-last-input="$index == focusIndex" ng-blur="serverValidate('EmailsAdditional')"/>
                <div ng-show="$first" class="popover-help-container leftBuffer">
                    <a href="javascript:void(0);"><i class="glyphicon glyphicon-question-sign"></i></a>
                    <div id="email-additional-help" class="popover bottom">
                        <div class="arrow"></div>
                        <div class="popover-content">
                            <p><@orcid.msg ''/></p>
                            <p><@orcid.msg 'orcid.frontend.register.help.email_additional'/></p>
                        </div>
                    </div>
                </div>
                <div ng-show="!$first" class="popover-help-container leftBuffer">
                    <a class="btn-white-no-border" ng-click="removeEmailField($index)"><i class="glyphicon glyphicon-remove-sign"></i></a>
                </div>
                <span class="orcid-error" ng-show="registrationForm.emailsAdditional[$index].errors.length > 0 && !showEmailsAdditionalDeactivatedError[$index] && !showEmailsAdditionalReactivationSent[$index]">
                    <div ng-repeat='error in registrationForm.emailsAdditional[$index].errors track by $index' ng-bind-html="error"></div>
                </span>
                <span class="orcid-error" ng-show="showEmailsAdditionalDeactivatedError[$index]" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.1")}<a href="" ng-click="sendEmailsAdditionalReactivationEmail($index)">${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.3")}
                </span>
                <span class="orcid-error" ng-show="showEmailsAdditionalReactivationSent[$index]" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.1")}<a href="mailto:support@orcid.org">${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.3")}
                </span>
            </div>
        </div>
        <button ng-click="addEmailField()" class="left btn-white-no-border"><i class="glyphicon glyphicon-plus-sign"></i> ${springMacroRequestContext.getMessage("oauth_sign_up.buttonaddemail")}</button>
    </@orcid.checkFeatureStatus>
    <@orcid.checkFeatureStatus featureName='REG_MULTI_EMAIL' enabled=false> 
        <!-- Email -->                  
        <div class="form-group clear-fix">
            <label class="control-label"><@orcid.msg 'oauth_sign_up.labelemail'/></label>
            <div class="bottomBuffer">
                <input id="register-form-email" name="email" type="email" tabindex="3" class="" ng-model="registrationForm.email.value" ng-model-onblur ng-change="serverValidate('Email')" />
                <span class="required" ng-class="isValidClass(registrationForm.email)">*</span> <span class="orcid-error" ng-show="emailTrustAsHtmlErrors.length > 0 && !showDeactivatedError && !showReactivationSent">
                    <div ng-repeat='error in emailTrustAsHtmlErrors' ng-bind-html="error" compile="html"></div>
                </span>
                <span class="orcid-error" ng-show="showDeactivatedError" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.1")}<a href="" ng-click="sendReactivationEmail(registrationForm.email.value)">${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.deactivated_email.3")}
                </span>
                <span class="orcid-error" ng-show="showReactivationSent" ng-cloak>
                    ${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.1")}<a href="mailto:support@orcid.org">${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.2")}</a>${springMacroRequestContext.getMessage("orcid.frontend.verify.reactivation_sent.3")}
                </span>                                             
            </div>
        </div>
        <!--Re-enter email-->
        <div class="form-group clear-fix">
            <label class="control-label"><@orcid.msg 'oauth_sign_up.labelreenteremail'/></label>
            <div class="bottomBuffer">
                <input id="register-form-confirm-email" name="confirmedEmail" type="email" tabindex="4" class="" ng-model="registrationForm.emailConfirm.value" ng-model-onblur ng-change="serverValidate('EmailConfirm')" />
                <span class="required" ng-class="isValidClass(registrationForm.emailConfirm)">*</span>                  
                <span class="orcid-error" ng-show="registrationForm.emailConfirm.errors.length > 0 && !showDeactivatedError && !showReactivationSent">
                    <div ng-repeat='error in registrationForm.emailConfirm.errors' ng-bind-html="error"></div>
                </span>
            </div>
        </div>
    </@orcid.checkFeatureStatus>
    <!--Password-->
    <div class="form-group clear-fix">
        <label class="control-label"><@orcid.msg 'oauth_sign_up.labelpassword'/></label>
        <div class="bottomBuffer">
            <input id="register-form-password" type="password" name="password" tabindex="5" class="" ng-model="registrationForm.password.value" ng-change="serverValidate('Password')"/>
            <span class="required" ng-class="isValidClass(registrationForm.password)">*</span>
            <@orcid.passwordHelpPopup />
            <span class="orcid-error" ng-show="registrationForm.password.errors.length > 0">
                <div ng-repeat='error in registrationForm.password.errors' ng-bind-html="error"></div>
            </span>
        </div>
    </div>
    <!--Confirm password-->
    <div class="form-group clear-fix">
        <label class="control-label"><@orcid.msg 'password_one_time_reset.labelconfirmpassword'/></label>
        <div class="bottomBuffer">
            <input id="register-form-confirm-password" type="password" name="confirmPassword" tabindex="6" class="" ng-model="registrationForm.passwordConfirm.value" ng-change="serverValidate('PasswordConfirm')"/>
            <span class="required" ng-class="isValidClass(registrationForm.passwordConfirm)">*</span>                 
            <span class="orcid-error" ng-show="registrationForm.passwordConfirm.errors.length > 0">
                <div ng-repeat='error in registrationForm.passwordConfirm.errors' ng-bind-html="error"></div>
            </span>
        </div>
    </div>
    <!--Visibility default-->
    <div class="form-group clear-fix">
        <div class="oauth-privacy">                      
            <label class="privacy-toggle-lbl">${springMacroRequestContext.getMessage("privacy_preferences.activitiesVisibilityDefault")}</label> 
            <label class="privacy-toggle-lbl">${springMacroRequestContext.getMessage("privacy_preferences.activitiesVisibilityDefault.who_can_see_this")}</label>
            <@orcid.privacyToggle 
                angularModel="registrationForm.activitiesVisibilityDefault.visibility" 
                questionClick="toggleClickPrivacyHelp('workPrivHelp')"
                clickedClassCheck="{'popover-help-container-show':privacyHelp['workPrivHelp']==true}" 
                publicClick="updateActivitiesVisibilityDefault('PUBLIC', $event)"
                limitedClick="updateActivitiesVisibilityDefault('LIMITED', $event)"
                privateClick="updateActivitiesVisibilityDefault('PRIVATE', $event)" />
        </div>
    </div>
    <!--Email frequency-->
    <div>
        <div class="relative">              
            <@orcid.registrationEmailFrequencySelector angularElementName="registrationForm" />
        </div>
    </div>
    <!--Recaptcha-->
    <div>
        <div class="bottomBuffer relative recaptcha"  id="recaptcha">
            <div vc-recaptcha
            theme="'light'"
            key="model.key"
            on-create="setRecaptchaWidgetId(widgetId)"
            on-success="setRecatchaResponse(response)"></div>
                <span class="orcid-error" ng-show="registrationForm.grecaptcha.errors.length > 0">
                    <div ng-repeat='error in registrationForm.grecaptcha.errors track by $index' ng-bind-html="error"></div>
                </span>
        </div>
    </div>
    <!--Terms and conditions-->
    <div class="bottomBuffer">
        <label for="termsConditions">
            <@orcid.msg 'register.labelTermsofUse'/>
            <span class="required"  ng-class="{'text-error':register.termsOfUse.value == false}">*</span>
        </label>
        <p>
            <input id="register-form-term-box" type="checkbox" name="termsConditions" tabindex="9" name="acceptTermsAndConditions" ng-model="registrationForm.termsOfUse.value" ng-change="serverValidate('TermsOfUse')" />
            <@orcid.msg 'register.labelconsent'/> <a href="${aboutUri}/footer/privacy-policy" target="register.labelprivacypolicy"><@orcid.msg 'register.labelprivacypolicy'/></a>&nbsp;<@orcid.msg 'register.labeland'/>&nbsp;<@orcid.msg 'common.termsandconditions1'/><a href="${aboutUri}/content/orcid-terms-use" target="common.termsandconditions2"><@orcid.msg 'common.termsandconditions2'/></a>&nbsp;<@orcid.msg 'common.termsandconditions3'/>
        </p>
        <span class="orcid-error" ng-show="registrationForm.termsOfUse.errors.length > 0">
            <div ng-repeat='error in registrationForm.termsOfUse.errors' ng-bind-html="error"></div>
        </span>
    </div>
    <!--Registration error-->
    <div style="margin-bottom: 15px;" ng-show="generalRegistrationError != null">
        <span class="orcid-error" ng-bind-html="generalRegistrationError"></span>
    </div>	 
    <!-- Buttons  -->
    <div class="bottomBuffer col-xs-12 col-sm-3">
    	<#if (RequestParameters['linkRequest'])??>
			<button id="register-authorize-button" class="btn btn-primary" name="authorize" value="<@orcid.msg 'confirm-oauth-access.Authorize'/>" ng-click="oauth2ScreensRegister('${RequestParameters.linkRequest}')">${springMacroRequestContext.getMessage("header.register")}</button>
		<#else>
			<button id="register-authorize-button" class="btn btn-primary" name="authorize" value="<@orcid.msg 'confirm-oauth-access.Authorize'/>" ng-click="oauth2ScreensRegister(null)">${springMacroRequestContext.getMessage("header.register")}</button>
		</#if>
    </div>  
</div>
<#include "/includes/duplicates_modal_inc.ftl" />        