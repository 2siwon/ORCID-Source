//Import all the angular components

import { NgFor, NgIf } 
    from '@angular/common'; 

import { AfterViewInit, Component, OnDestroy, OnInit } 
    from '@angular/core';

import { Observable } 
    from 'rxjs/Rx';

import { Subject } 
    from 'rxjs/Subject';

import { Subscription }
    from 'rxjs/Subscription';

import { AdminDelegatesService } 
    from '../../shared/adminDelegates.service.ts'; 


@Component({
    selector: 'admin-verify-email-ng2',
    template:  scriptTmpl("admin-verify-email-ng2-template")
})
export class AdminVerifyEmailComponent implements AfterViewInit, OnDestroy, OnInit {
    private ngUnsubscribe: Subject<void> = new Subject<void>();

    email: any;
    result: any;
    showSection: boolean;
   
    constructor(
        private adminDelegatesService: AdminDelegatesService
    ) {
        this.email = null;
        this.result = {
        };
        this.showSection = false;
    }

    verifyEmail(): void {
        this.adminDelegatesService.verifyEmail( this.email )
        .takeUntil(this.ngUnsubscribe)
        .subscribe(
            data => {
                this.result = data;
            },
            error => {
                //console.log('setformDataError', error);
            } 
        );
    };

    toggleSection(): void{
        this.showSection = !this.showSection;
        $('#verify_email_section').toggle();
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
    }; 
}

