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

import { AccountService } 
    from '../../shared/account.service.ts'; 


@Component({
    selector: 'password-edit-ng2',
    template:  scriptTmpl("password-edit-ng2-template")
})
export class PasswordEditComponent implements AfterViewInit, OnDestroy, OnInit {
    private ngUnsubscribe: Subject<void> = new Subject<void>();
   
    changePasswordPojo: any;

    constructor(
        private accountService: AccountService
    ) {
        this.changePasswordPojo = {};
    }

    getChangePassword(): void {
        this.accountService.getChangePassword()
        .takeUntil(this.ngUnsubscribe)
        .subscribe(
            data => {
                if(data) {
                    this.changePasswordPojo = data;
                }
            },
            error => {
                //console.log('getformDataError', error);
            } 
        );
 
    };


    saveChangePassword(): void {
        this.accountService.saveChangePassword( this.changePasswordPojo )
        .takeUntil(this.ngUnsubscribe)
        .subscribe(
            data => {
                this.changePasswordPojo = data;

            },
            error => {
                //console.log('setformDataError', error);
            } 
        );
    }

    //Default init functions provided by Angular Core
    ngAfterViewInit() {
        //Fire functions AFTER the view inited. Useful when DOM is required or access children directives
    };

    ngOnDestroy() {
        this.ngUnsubscribe.next();
        this.ngUnsubscribe.complete();
    };

    ngOnInit() {
        this.getChangePassword()
    }; 
}