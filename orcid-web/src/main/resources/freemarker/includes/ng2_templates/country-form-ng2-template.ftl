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

<script type="text/ng-template" id="country-form-ng2-template">        
    <div class="edit-record <#if RequestParameters['bulkEdit']??>edit-record-bulk-edit</#if> edit-country row">

        <div class="col-md-12 col-sm-12 col-xs-12">           
            <div class=""> 
                <h1 class="lightbox-title pull-left">
                    <@orcid.msg 'manage_bio_settings.editCountry'/>
                </h1>
            </div>          
        </div>
        <div class="bottomBuffer" style="margin: 0!important;">                          
            <!-- Move this to component - Begin of bulk component-->
            <div class="row bulk-edit-modal">
                <div class="pull-right bio-edit-modal">             
                    <span class="right">Edit all privacy settings</span>
                    <div class="bulk-privacy-bar">
                        <div [ngClass]="{'relative' : modal == false}" id="privacy-bar">
                            <ul class="privacyToggle" ng-mouseenter="commonSrvc.showPrivacyHelp(bulkEdit +'-privacy', $event, 145)" ng-mouseleave="commonSrvc.hideTooltip(bulkEdit +'-privacy')">
                                <li class="publicActive publicInActive" [ngClass]="{publicInActive: bioModel != 'PUBLIC'}"><a (click)="setBulkGroupPrivacy('PUBLIC', $event, bioModel)" name="privacy-toggle-3-public" id=""></a></li>
                                <li class="limitedActive limitedInActive" [ngClass]="{limitedInActive: bioModel != 'LIMITED'}"><a (click)="setBulkGroupPrivacy('LIMITED', $event, bioModel)" name="privacy-toggle-3-limited" id=""></a></li>
                                <li class="privateActive privateInActive" [ngClass]="{privateInActive: bioModel != 'PRIVATE'}"><a (click)="setBulkGroupPrivacy('PRIVATE', $event, bioModel)" name="privacy-toggle-3-private" id=""></a></li>
                            </ul>
                        </div>
                        <div class="popover-help-container" style="top: -75px; left: 512px;">
                            <div class="popover top privacy-myorcid3" [ngClass]="commonSrvc.shownElement[bulkEdit +'-privacy'] == true ? 'block' : ''">
                                <div class="arrow"></div>
                                <div class="popover-content">
                                    <strong>Who can see this? </strong>
                                    <ul class="privacyHelp">
                                        <li class="public" style="color: #009900;">everyone</li>
                                        <li class="limited" style="color: #ffb027;">trusted parties</li>
                                        <li class="private" style="color: #990000;">only me</li>
                                    </ul>
                                    <a href="https://support.orcid.org/knowledgebase/articles/124518-orcid-privacy-settings" target="privacyToggle.help.more_information">More information on privacy settings</a>
                                </div>                
                            </div>                              
                        </div>

                    </div>
                    <div class="bulk-help popover-help-container">
                        <a href="javascript:void(0);"><i class="glyphicon glyphicon-question-sign"></i></a>
                        <div id="bulk-help" class="popover bottom">
                            <div class="arrow"></div>
                            <div class="popover-content">
                                <p>Use Edit all privacy settings to change the visibility level of all items, or Edit individual privacy settings to select different visibility levels for each item.</p>
                            </div>
                       </div>
                    </div>
                </div>          
            </div>
            <!-- End of bulk edit -->          
        </div>    
        <div class="col-md-12 col-sm-12 col-xs-12">
            <div class=" padding-right-reset">
                <span class="right"><@orcid.msg 'groups.common.edit_individual_privacy' /></span>   
            </div>
        </div>      
        <div class="col-md-12 col-xs-12 col-sm-12">
            <div style="position: static">
                <div class="fixed-area" scroll>             
                    <div class="scroll-area">       
                                    
                        <div class="row aka-row" *ngFor="let country of countryFormAddresses; let index = index; let first = first; let last = last">
                            <div class="col-md-6">                                  
                                <div class="aka">
                                    <select 
                                        [(ngModel)]="country.iso2Country" 
                                        [disabled]="country.source != orcidId"
                                        [ngClass]="{ 'not-allowed': country?.source != orcidId }"
                                        focus-me="newInput"
                                        name="country" 
                                    >

                                        <option value=""><@orcid.msg 'org.orcid.persistence.jpa.entities.CountryIsoEntity.empty' /></option>
                                        <#list isoCountries?keys as key>
                                            <option value="${key}">${isoCountries[key]}</option>
                                        </#list>
                                    </select>                                      
                                </div>         
                                                        
                                <div class="source" *ngIf="country.sourceName || country?.sourceName == null">
                                    <@orcid.msg 'manage_bio_settings.source'/>: <span *ngIf="country.sourceName">{{country.sourceName}}</span><span *ngIf="country.sourceName == null">{{orcidId}}</span>
                                </div>
                                
                            </div> 
                            <div class="col-md-6" style="position: static">
                                <ul class="record-settings pull-right">              
                                    <li>                                    
                                        <div 
                                            (click)="first || swapUp(index)" 
                                            (mouseenter)="commonSrvc.showTooltip('tooltip-country-move-up-'+index, $event, 37, -33, 44)" 
                                            (mouseleave)="commonSrvc.hideTooltip('tooltip-country-move-up-'+index)"
                                            class="glyphicon glyphicon-arrow-up circle" 
                                        ></div>
                                        <@orcid.tooltip elementId="'tooltip-country-move-up-'+index" message="common.modals.move_up"/>                                         
                                    </li>
                                    <li>
                                        <div 
                                            class="glyphicon glyphicon-arrow-down circle" 
                                            (click)="last || swapDown(index)" 
                                            (mouseenter)="commonSrvc.showTooltip('tooltip-country-move-down-'+index, $event, 37, -2, 53)" 
                                            (mouseleave)="commonSrvc.hideTooltip('tooltip-country-move-down-'+index)"
                                        ></div>
                                        <@orcid.tooltip elementId="'tooltip-country-move-down-'+index" message="common.modals.move_down" />
                                    </li>
                                    <li>
                                        <div 
                                            (click)="deleteCountry(country)" 
                                            (mouseenter)="commonSrvc.showTooltip('tooltip-country-delete-'+$index, $event, 37, 50, 39)" 
                                            (mouseleave)="commonSrvc.hideTooltip('tooltip-country-delete-'+$index)"
                                            class="glyphicon glyphicon-trash" 
                                        ></div>
                                        <@orcid.tooltip elementId="'tooltip-country-delete-'+$index" message="common.modals.delete" />                               
                                    </li>
                                    <li>
                                        <!--
                                        <privacy-toggle-ng2 elementId="bio-privacy-toggle" [dataPrivacyObj]="country.visibility.visibility" (privacyUpdate)="privacyChange($event)"></privacy-toggle-ng2>
                                        -->
                                        <!--
                                        -->
                                        <@orcid.privacyToggle3  angularModel="country.visibility.visibility"
                                            questionClick="toggleClickPrivacyHelp($index)"
                                            clickedClassCheck="{'popover-help-container-show':privacyHelp==true}" 
                                            publicClick="setPrivacyModal('PUBLIC', $event, country)" 
                                            limitedClick="setPrivacyModal('LIMITED', $event, country)" 
                                            privateClick="setPrivacyModal('PRIVATE', $event, country)"
                                            elementId="$index"/>   
                                    </li>
                                </ul>
                                <span class="created-date pull-right hidden-xs" *ngIf="country.createdDate"><@orcid.msg 'manage_bio_settings.created'/>: {{country.createdDate.year + '-' + country.createdDate.month + '-' + country.createdDate.day}}</span>
                                <span class="created-date pull-left visible-xs" *ngIf="country.createdDate"><@orcid.msg 'manage_bio_settings.created'/>: {{country.createdDate.year + '-' + country.createdDate.month + '-' + country.createdDate.day}}</span>
                            </div>                                  
                        </div>                                          
                    </div>         
                    <div *ngIf="countryForm?.errors?.length > 0">
                        <div *ngFor="let error of countryFormErrors">
                            <span class="red">{{error}}</span>
                        </div>
                    </div>
                </div>                  
                <div class="record-buttons">                        
                    <a (click)="addNewCountry()"><span class="glyphicon glyphicon-plus pull-left">
                        <div class="popover popover-tooltip-add top">
                            <div class="arrow"></div>
                            <div class="popover-content">
                                <span><@orcid.msg 'common.modals.add' /></span>
                            </div>
                        </div>
                    </span></a>                         
                    <button class="btn btn-primary pull-right" (click)="setCountryForm()"><@spring.message "freemarker.btnsavechanges"/></button>
                    <a class="cancel-option pull-right" (click)="closeEditModal()"><@spring.message "freemarker.btncancel"/></a> 
                </div>
            </div>
        </div>
    </div>
</script>