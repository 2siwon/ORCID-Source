declare var getWindowWidth: any;
declare var orcidVar: any;

//Import all the angular components

import { NgForOf, NgIf } 
    from '@angular/common'; 

import { AfterViewInit, Component, OnDestroy, OnInit } 
    from '@angular/core';

import { FormsModule }
    from '@angular/forms';

import { Observable, Subject, Subscription } 
    from 'rxjs';
import { takeUntil } 
    from 'rxjs/operators';

import { NotificationsService } 
    from '../../shared/notifications.service.ts'; 


@Component({
    selector: 'header-ng2',
    template:  scriptTmpl("header-ng2-template")
})
export class HeaderComponent implements AfterViewInit, OnDestroy, OnInit {
    private ngUnsubscribe: Subject<void> = new Subject<void>();
    
    conditionsActive: boolean;
    filterActive: boolean;
    getUnreadCount: any;
    menuVisible: boolean;
    headerSearch: any;
    searchFilterChanged: boolean;
    searchVisible: boolean;
    secondaryMenuVisible: any;
    settingsVisible: boolean;
    tertiaryMenuVisible: any;

    constructor(
        private notificationsSrvc: NotificationsService
    ) {
        this.conditionsActive = false;
        this.filterActive = false;
        this.getUnreadCount = 0;
        this.headerSearch = {};
        this.menuVisible = false;
        this.searchFilterChanged = false;
        this.searchVisible = false;
        this.secondaryMenuVisible = {};
        this.settingsVisible = false;
        this.tertiaryMenuVisible = {};
    }

    
    filterChange(): void {
        this.searchFilterChanged = true;
    };

    handleMobileMenuOption( $event ): void{
        let w = getWindowWidth();           
        
        $event.preventDefault();
        
        if( w > 767) {               
            window.location.href = $event.target.getAttribute('href');
        }
    };

    hideSearchFilter(): void{
        if (!this.headerSearch.searchInput){
            var timeoutFunction = (function() { 
                if ( this.searchFilterChanged === false ) {
                    this.filterActive = false;
                }           
            }).bind(this);
            setTimeout(timeoutFunction, 3000);
        }
    };

    isCurrentPage(path): any {
        return window.location.href.startsWith(orcidVar.baseUri + '/' + path);
    };

    onResize(event?): void {
        let windowWidth = getWindowWidth();
        if(windowWidth > 767){ /* Desktop view */
            this.menuVisible = true;
            this.searchVisible = true;
            this.settingsVisible = true;
        }else{
            this.menuVisible = false;
            this.searchVisible = false;
            this.settingsVisible = false;
        }
    };

    retrieveUnreadCount(): any {
        if( this.notificationsSrvc.retrieveCountCalled == false ) {
            this.notificationsSrvc.retrieveUnreadCount()
            .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
            .subscribe(
                data => {
                    this.getUnreadCount = data;
                },
                error => {
                    //console.log('verifyEmail', error);
                } 
            );
        }
    };

    searchBlur(): void {    
        this.hideSearchFilter();
        this.conditionsActive = false;        
    };

    searchFocus(): void {
        this.filterActive = true;
        this.conditionsActive = true;
    };

    searchSubmit(): void {
        if (this.headerSearch.searchOption=='website'){
            window.location.assign(orcidVar.baseUri + '/search/node/' + encodeURIComponent(this.headerSearch.searchInput));
        }
        if(this.headerSearch.searchOption=='registry'){
            window.location.assign(orcidVar.baseUri
                    + "/orcid-search/quick-search/?searchQuery="
                    + encodeURIComponent(this.headerSearch.searchInput));
        }
    }
  
    toggleMenu(): void {
        this.menuVisible = !this.menuVisible;
        this.searchVisible = false;
        this.settingsVisible = false;     
    };
    
    toggleSearch(): void {
        this.searchVisible = !this.searchVisible;
        this.menuVisible = false;     
        this.settingsVisible = false;
    };

    toggleSecondaryMenu(submenu): void {
        this.secondaryMenuVisible[submenu] = !this.secondaryMenuVisible[submenu];
    };

    toggleSettings(): void {
        this.settingsVisible = !this.settingsVisible;
        this.menuVisible = false;
        this.searchVisible = false;
    };
    
    toggleTertiaryMenu(submenu): void {
        this.tertiaryMenuVisible[submenu] = !this.tertiaryMenuVisible[submenu];
    };

    //Default init functions provided by Angular Core
    ngAfterViewInit() {
        //Fire functions AFTER the view inited. Useful when DOM is required or access children directives
    };

    ngOnDestroy() {
        this.ngUnsubscribe.next();
        this.ngUnsubscribe.complete();
    };

    ngOnInit() {
        this.onResize(); 
        this.headerSearch.searchOption = 'registry'; 
    }; 
}
