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

import { AlsoKnownAsService } 
    from '../../shared/alsoKnownAs.service.ts';

import { CommonService } 
    from '../../shared/commonService.ts';

import { ModalService } 
    from '../../shared/modalService.ts'; 

@Component({
    selector: 'also-known-as-form-ng2',
    template:  scriptTmpl("also-known-as-form-ng2-template")
})
export class AlsoKnownAsFormComponent implements AfterViewInit, OnDestroy, OnInit {
    private ngUnsubscribe: Subject<void> = new Subject<void>();
    private subscription: Subscription;

    defaultVisibility: any;
    emails: any;
    formData: any;
    newElementDefaultVisibility: any;
    orcidId: any;
    privacyHelp: any;
    scrollTop: any;
    showEdit: any;
    showElement: any;

    constructor( 
        private alsoKnownAsService: AlsoKnownAsService,
        private commonSrvc: CommonService,
        private modalService: ModalService
    ) {
        this.defaultVisibility = null;
        this.emails = {};
        this.formData = {
        };
        this.newElementDefaultVisibility = null;
        this.orcidId = orcidVar.orcidId; 
        this.privacyHelp = false;
        this.scrollTop = 0;
        this.showEdit = false;
        this.showElement = {};
    }

    addNew(): void {
        let tmpObj = {
            "errors":[],
            "url":null,
            "urlName":null,
            "putCode":null,
            "visibility":{
                "errors":[],
                "required":true,
                "getRequiredMessage":null,
                "visibility": this.newElementDefaultVisibility
            },
            "source":this.orcidId,
            "sourceName":"", 
            "displayIndex": 1
        };        
        this.formData.otherNames.push(tmpObj);        
        this.updateDisplayIndex();          
        //this.newInput = true;
    };

    closeEditModal(): void{
        this.modalService.notifyOther({action:'close', moduleId: 'modalAlsoKnownAsForm'});
    };

    deleteOtherName(otherName): void{
        let otherNames = this.formData.otherNames;
        let len = otherNames.length;
        while (len--) {            
            if (otherNames[len] == otherName){                
                otherNames.splice(len,1);
            }
        }        
    };

    getformData(): void {
        this.alsoKnownAsService.getData()
        .takeUntil(this.ngUnsubscribe)
        .subscribe(
            data => {
                this.formData = data;
                //console.log('this.getForm', this.formData);
            },
            error => {
                console.log('getAlsoKnownAsFormError', error);
            } 
        );
    };

    privacyChange( obj ): any {
        this.formData.visibility.visibility = obj;
        this.setFormData( false );   
    };

    setFormData( closeAfterAction ): void {
        this.alsoKnownAsService.setData( this.formData )
        .takeUntil(this.ngUnsubscribe)
        .subscribe(
            data => {
                this.formData = data;
                if (this.formData.errors.length == 0){
                    this.getformData();
                    this.alsoKnownAsService.notifyOther();
                    if( closeAfterAction == true ) {
                        this.closeEditModal();
                    }
                }else{
                    console.log(this.formData.errors);
                }

            },
            error => {
                console.log('setAlsoKnownAs', error);
            } 
        );
        this.formData.visibility = null;
    }

    swapDown(index): void{
        let temp;
        let tempDisplayIndex;
        if (index < this.formData.otherNames.length - 1) {
            temp = this.formData.otherNames[index];
            tempDisplayIndex = this.formData.otherNames[index]['displayIndex'];
            temp['displayIndex'] = this.formData.otherNames[index + 1]['displayIndex']
            this.formData.otherNames[index] = this.formData.otherNames[index + 1];
            this.formData.otherNames[index]['displayIndex'] = tempDisplayIndex;
            this.formData.otherNames[index + 1] = temp;
        }
    };

    swapUp(index): void{
        let temp;
        let tempDisplayIndex;
        if (index > 0) {
            temp = this.formData.otherNames[index];
            tempDisplayIndex =this.formData.otherNames[index]['displayIndex'];
            temp['displayIndex'] = this.formData.otherNames[index - 1]['displayIndex']
            this.formData.otherNames[index] = this.formData.otherNames[index - 1];
            this.formData.otherNames[index]['displayIndex'] = tempDisplayIndex;
            this.formData.otherNames[index - 1] = temp;
        }
    };

    updateDisplayIndex(): void{
        let idx: any;
        for (idx in this.formData.otherNames) {         
            this.formData.otherNames[idx]['displayIndex'] = this.formData.otherNames.length - idx;
        }
    };

    //Default init functions provided by Angular Core
    ngAfterViewInit() {
        //Fire functions AFTER the view inited. Useful when DOM is required or access children directives
        this.subscription = this.alsoKnownAsService.notifyObservable$.subscribe(
            (res) => {
                this.getformData();
            }
        );
    };

    ngOnDestroy() {
        this.ngUnsubscribe.next();
        this.ngUnsubscribe.complete();
    };

    ngOnInit() {
        this.getformData();
    };

}
