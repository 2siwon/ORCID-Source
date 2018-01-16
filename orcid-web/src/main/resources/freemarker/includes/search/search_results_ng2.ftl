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
<div class="col-md-12">
    <p *ngIf="areResults()">${springMacroRequestContext.getMessage("search_results.showing")} {{resultsShowing}} ${springMacroRequestContext.getMessage("search_results.of")} {{numFound}} <span *ngIf="numFound==1">${springMacroRequestContext.getMessage("search_results.result")}</span><span *ngIf="numFound>1">${springMacroRequestContext.getMessage("search_results.results")}</span></p>
    <table class="table table-striped" *ngIf="areResults()">
        <thead>
            <tr>
                <th>${springMacroRequestContext.getMessage("search_results.thORCIDID")}</th>
                <th>${springMacroRequestContext.getMessage("search_results.thGivenname")}</th>
                <th>${springMacroRequestContext.getMessage("search_results.thFamilynames")}</th>
                <th>${springMacroRequestContext.getMessage("search_results.thOthernames")}</th>
                <th>${springMacroRequestContext.getMessage("workspace_bio.Affiliations")}</th>
            </tr>
        </thead>
        <tbody>
            <tr *ngFor="let result of allResults" class="new-search-result">
                <td class='search-result-orcid-id'><a href="{{result['orcid-identifier'].uri}}">{{result['orcid-identifier'].uri}}</a></td>
                <td>{{result['given-names']}}</td>
                <td>{{result['family-name']}}</td>  
                <td>{{concatPropertyValues(result['other-name'], 'content')}}</td>
                <td>{{result['affiliations']}}</td>
            </tr>
        </tbody>
    </table>
    <div id="show-more-button-container">
        <button id="show-more-button" type="submit" class="btn btn-primary" (click)="getMoreResults()" *ngIf="areMoreResults">Show more</button>
        <span id="ajax-loader-show-more" class="orcid-hide"><i class="glyphicon glyphicon-refresh spin x2 green"></i></span>
    </div>
    <div id="no-results-alert" class="orcid-hide alert alert-error"><@spring.message "orcid.frontend.web.no_results"/></div>
    <div id="search-error-alert" class="orcid-hide alert alert-error"><@spring.message "orcid.frontend.web.search_error"/></div>
</div>