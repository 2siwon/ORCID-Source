import * as angular 
    from 'angular';

import { CommonModule } 
    from '@angular/common'; 

import { Directive, NgModule } 
    from '@angular/core';

import { FormsModule }
    from '@angular/forms'; // <-- NgModel lives here

//User generated filters
import { AjaxFormDateToISO8601Pipe }
    from '../../pipes/ajaxFormDateToISO8601Ng2.ts'; 

import { ContributorFilterPipe }
    from '../../pipes/contributorFilterNg2.ts';

import { FilterImportWizardsPipe }
    from '../../pipes/filterImportWizardsNg2.ts'

import { LatexPipe }
    from '../../pipes/latexNg2.ts';

    import { OrgIdentifierHtmlPipe }
    from '../../pipes/orgIdentifierHtmlNg2.ts';    
    
import { OrderByPipe }
    from '../../pipes/orderByNg2.ts';

import { OrderObjectByPipe }
    from '../../pipes/orderObjectByNg2.ts'

import { UrlProtocolPipe }
    from '../../pipes/urlProtocolNg2.ts';

import { WorkExternalIdentifierHtmlPipe }
    from '../../pipes/workExternalIdentifierHtmlNg2.ts';

//User generated modules
import { PrivacytoggleNg2Module }
    from './../privacytoggle/privacyToggle.ts';

import { EmailFrecuencyNg2Module }
    from './../emailFrecuency/emailFrecuency.ts';

import { LanguageNg2Module }
    from './../language/language.ts';

import { RequestPasswordResetNg2Module }
    from './../requestPasswordReset/requestPasswordReset.ts';

//User generated services

import { AffiliationService } 
    from '../../shared/affiliation.service.ts';

import { AlsoKnownAsService } 
    from '../../shared/alsoKnownAs.service.ts';

import { BiographyService } 
    from '../../shared/biography.service.ts';

import { CommonService }
    from '../../shared/common.service.ts'

import { ConsortiaService }
    from '../../shared/consortia.service.ts'

import { CountryService } 
    from '../../shared/country.service.ts';

import { EmailService } 
    from '../../shared/email.service.ts';

import { FeaturesService }
    from '../../shared/features.service.ts';

import { FundingService } 
    from '../../shared/funding.service.ts';

//import { GroupedActivitiesUtilService } 
//    from '../shared/groupedActivities.service.ts';

import { KeywordsService } 
    from '../../shared/keywords.service.ts';

import { LanguageService }
    from '../../shared/language.service.ts';

import { ModalService } 
    from '../../shared/modal.service.ts';

import { NameService } 
    from '../../shared/name.service.ts';

import { OauthService } 
    from '../../shared/oauth.service.ts';

import { PreferencesService }
    from '../../shared/preferences.service.ts';

import { RequestPasswordResetService }
    from '../../shared/requestPasswordReset.service.ts';

import { TwoFAStateService } 
    from '../../shared/twoFAState.service.ts';

import { WebsitesService } 
    from '../../shared/websites.service.ts';

import { WidgetService }
    from '../../shared/widget.service.ts'

import { WorkspaceService } 
    from '../../shared/workspace.service.ts'; 

import { WorksService } 
    from '../../shared/works.service.ts';

// This is the Angular 2 part of the module
@NgModule(
    {
        imports: [
            //Angular Libraries
            CommonModule,
            FormsModule,
            //User Modules
            EmailFrecuencyNg2Module,
            LanguageNg2Module,
            PrivacytoggleNg2Module,
            RequestPasswordResetNg2Module
        ],
        declarations: [ 
            AjaxFormDateToISO8601Pipe,
            ContributorFilterPipe,
            FilterImportWizardsPipe,
            LatexPipe,
            OrgIdentifierHtmlPipe,
            OrderByPipe,
            OrderObjectByPipe,
            UrlProtocolPipe,
            WorkExternalIdentifierHtmlPipe,

        ],
        exports: [
            //Angular Libraries
            CommonModule,
            FormsModule,
            //User Pipes
            AjaxFormDateToISO8601Pipe,
            ContributorFilterPipe,
            FilterImportWizardsPipe,
            LatexPipe,
            OrgIdentifierHtmlPipe,
            OrderByPipe,
            OrderObjectByPipe,
            UrlProtocolPipe,
            WorkExternalIdentifierHtmlPipe,
            //User Modules
            LanguageNg2Module,
            EmailFrecuencyNg2Module,
            PrivacytoggleNg2Module,
            RequestPasswordResetNg2Module
        ],
        providers: [
            AffiliationService,
            AlsoKnownAsService,
            BiographyService,
            CommonService,
            ConsortiaService,
            CountryService,
            EmailService,
            FeaturesService,
            FundingService,
            //GroupedActivitiesUtilService,
            KeywordsService,
            LanguageService,
            ModalService,
            NameService,
            OauthService,
            PreferencesService,
            RequestPasswordResetService,
            TwoFAStateService,
            WebsitesService,
            WidgetService,
            WorksService,
            WorkspaceService
        ]
    }
)
export class CommonNg2Module {}